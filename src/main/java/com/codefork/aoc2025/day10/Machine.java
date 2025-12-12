package com.codefork.aoc2025.day10;

import com.codefork.aoc2025.util.WithIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.codefork.aoc2025.util.FoldLeft.foldLeft;

public record Machine(List<Boolean> lightsToStart, List<Button> buttons, List<Integer> joltages) {

    public record Button(List<Integer> toggles) {

        // press this record's button to change lights, returning the result
        public List<Boolean> press(List<Boolean> lights) {
            return IntStream.range(0, lights.size()).mapToObj(lightIndex ->
                    toggles.contains(lightIndex) ? !lights.get(lightIndex) : lights.get(lightIndex)
            ).toList();
        }

        // press this record's button to change joltage counters, returning the result
        public List<Integer> pressJoltages(List<Integer> counters) {
            return IntStream.range(0, counters.size()).mapToObj(jIndex ->
                    counters.get(jIndex) - (toggles.contains(jIndex) ? 1 : 0)
            ).toList();
        }

    }

    // track state of a Machine for part 1 (lights)
    record MachineState(Machine machine, List<Boolean> lights, List<Button> pressed) {
        public MachineState addButtonPress(Button button) {
            var newLights = button.press(lights);
            var newPressed = new ArrayList<>(pressed);
            newPressed.add(button);
            return new MachineState(machine, newLights, newPressed);
        }

        public boolean doesMachineStart() {
            return machine.lightsToStart().equals(lights);
        }
    }

    // track state of a Machine for part 2 (joltage counters)
    public record JoltagesState(Machine machine, List<Integer> counters, int numPressed) {
//        public JoltagesState addButtonPress(Button button) {
//            var newCounters = button.pressJoltages(counters);
//            return new JoltagesState(machine, newCounters, numPressed + 1);
//        }

        public boolean joltagesMatch() {
            return counters().stream().allMatch(i -> i == 0);
        }
    }

    public static List<Machine> parseInput(Stream<String> input) {
        return input.map(line -> {
            return Arrays.stream(line.split(" ")).collect(foldLeft(
                    () -> new Machine(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                    (acc, chunk) -> {
                        if (chunk.startsWith("[")) {
                            var lightsToStart = chunk.substring(1, chunk.length() - 1).chars().boxed()
                                    .map(ch -> ch == '#').toList();
                            return new Machine(lightsToStart, acc.buttons(), acc.joltages());
                        } else if (chunk.startsWith("(")) {
                            var toggles = Arrays.stream(chunk.substring(1, chunk.length() - 1).split(","))
                                    .map(Integer::parseInt).toList();
                            var newButtons = Stream.concat(
                                    acc.buttons().stream(),
                                    Stream.of(new Button(toggles))
                            ).toList();
                            return new Machine(acc.lightsToStart(), newButtons, acc.joltages());
                        } else if (chunk.startsWith("{")) {
                            var joltages = Arrays.stream(chunk.substring(1, chunk.length() - 1).split(","))
                                    .map(Integer::parseInt).toList();
                            return new Machine(acc.lightsToStart(), acc.buttons(), joltages);
                        } else {
                            throw new RuntimeException("unexpected chunk: " + chunk);
                        }
                    }
            ));
        }).toList();
    }

    public static int findSmallestNumOfButtonPresses(Machine machine) {
        // effectively do a breadth-first search until we find a solution
        var states = IntStream.range(0, machine.buttons().size()).mapToObj(i ->
                new MachineState(machine, machine.initialLights(), new ArrayList<>())
        ).toList();
        while (true) {
            var newStates = new ArrayList<MachineState>();
            for (var state : states) {
                for (var button : machine.buttons()) {
                    var newState = state.addButtonPress(button);
                    if (newState.doesMachineStart()) {
                        return newState.pressed().size();
                    } else {
                        newStates.add(newState);
                    }
                }
            }

            // filter out duplicates of MachineState whose lights are in the same state,
            // since the next iteration will end up creating the same paths for that state.
            // this reduces runtime of part 1 from 27s to under 1s
            var byLights = newStates.stream().collect(Collectors.groupingBy(MachineState::lights));
            var dupesRemoved = byLights.values().stream().map(List::getFirst).toList();

            states = dupesRemoved;
        }
    }

    public static int sumSmallestNumOfButtonPresses(List<Machine> machines) {
        return machines.stream().map(Machine::findSmallestNumOfButtonPresses).mapToInt(i -> i).sum();
    }

    public interface Callback<T> {
        public boolean apply(List<T> elements);
    }

    // adaptation of Heap's algorithm found at https://www.baeldung.com/java-array-permutations
    public static <T> void permutations(int n, List<T> elements, Callback<T> callback) {
        if (n == 1) {
            var result = callback.apply(elements);
        } else {
            for (int i = 0; i < n - 1; i++) {
                permutations(n - 1, elements, callback);
                if (n % 2 == 0) {
                    swap(elements, i, n - 1);
                } else {
                    swap(elements, 0, n - 1);
                }
            }
            permutations(n - 1, elements, callback);
        }
    }

    private static <T> void swap(List<T> elements, int a, int b) {
        T tmp = elements.get(a);
        elements.set(a, elements.get(b));
        elements.set(b, tmp);
    }

    // tries to press the ordered list of buttons the appropriate the number of times,
    // to get to state where JoltageState matches the machine's desired joltages
    public static Optional<JoltagesState> solveButtonPressesForJoltages(Machine machine, List<Button> orderedButtons, int limit) {
        // work in reverse: set initial counters to desired joltages. when pressing buttons,
        // subtract instead of add. this lets us easily calculate how many times to press a button,
        // and check when we're done (when counts are all 0)
        var state = new JoltagesState(machine, machine.joltages(), 0);

        for (var button : orderedButtons) {
            // min of relevant joltages = the most times we can press this button
            var min = state.counters().stream()
                    .map(WithIndex.indexed())
                    .filter(entry -> button.toggles().contains(entry.index()))
                    .map(WithIndex::value)
                    .min(Integer::compare)
                    .orElseThrow();

            if (min > 0) {
                // press the button min times
                var newCounters = state.counters().stream()
                        .map(WithIndex.indexed())
                        .map(entry ->
                                entry.value() - (button.toggles().contains(entry.index()) ? min : 0)
                        )
                        .toList();
                //System.out.println("pressed " + button.toggles() + " " + min + " times, result=" + newCounters);
                state = new JoltagesState(machine, newCounters, state.numPressed() + min);
            } else {
                //System.out.println("couldn't press " + button.toggles() + " b/c current state=" + state.counters());
            }
            if (state.numPressed() > limit) {
                return Optional.empty();
            }
            if (state.joltagesMatch()) {
                return Optional.of(state);
            }
        }

        return Optional.empty();
    }

    public static int findSmallestNumOfButtonPressesForJoltages(Machine machine) {
        // sort buttons by # of toggles in it: higher numbers of toggles
        // minimizes number of button presses, so start with those.
        // actually, since we're trying all permutations, this probably doesn't matter
        var sortedButtons = machine.buttons().stream()
                .sorted(Comparator.comparingInt(b -> b.toggles.size()))
                .collect(Collectors.toList())
                .reversed();

        List<JoltagesState> solutions = new ArrayList<>();
        AtomicInteger smallest = new AtomicInteger(Integer.MAX_VALUE);

        // TODO: trying all permutations takes too long, plus this doesn't actually work for real input
        System.out.println("running permutations of size " + sortedButtons.size());
        permutations(sortedButtons.size(), sortedButtons, (orderedButtons) -> {
            var result = solveButtonPressesForJoltages(machine, orderedButtons, smallest.get());
            result.ifPresent(solution -> {
                solutions.add(solution);
                smallest.set(Math.min(solution.numPressed(), smallest.get()));
            });
            return true;
        });

        return solutions.stream()
                .min(Comparator.comparingInt(JoltagesState::numPressed))
                .orElseThrow()
                .numPressed();
    }

    public static int sumSmallestNumOfButtonPressesForJoltages(List<Machine> machines) {
        return machines.stream()
                .peek(System.out::println)
                .map(Machine::findSmallestNumOfButtonPressesForJoltages)
                .mapToInt(i -> i)
                .sum();
    }

    // returns a list of lights all initialized to false
    public List<Boolean> initialLights() {
        return IntStream.range(0, lightsToStart().size()).mapToObj(i -> false).toList();
    }
}
