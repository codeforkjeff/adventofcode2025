package com.codefork.aoc2025.day08;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.codefork.aoc2025.util.FoldLeft.foldLeft;

public class CircuitsConnector {

    record JBox(int x, int y, int z) implements Comparable<JBox> {
        public double dist(JBox other) {
            return Math.sqrt(
                    Math.pow(other.x - x, 2) +
                            Math.pow(other.y - y, 2) +
                            Math.pow(other.z - z, 2)
            );
        }

        @Override
        public int compareTo(JBox o) {
            var x_ = Integer.compare(x(), o.x());
            if (x_ == 0) {
                var y_ = Integer.compare(y(), o.y());
                if (y_ == 0) {
                    return Integer.compare(z(), o.z());
                }
                return y_;
            }
            return x_;
        }

        public static List<JBox> parseInput(Stream<String> input) {
            return input.map(line -> {
                var parts = Arrays.stream(line.split(",")).map(Integer::parseInt).toList();
                return new JBox(parts.get(0), parts.get(1), parts.get(2));
            }).toList();
        }

    }

    record Pair(JBox a, JBox b, double dist) implements Comparable<Pair> {
        // constructor: make sure "lower" JBox is first, so pairs can be compared properly
        public static Pair normalize(JBox a, JBox b, double dist) {
            if (a.compareTo(b) <= 0) {
                return new Pair(a, b, dist);
            }
            return new Pair(b, a, dist);
        }

//        public boolean contains(JBox candidate) {
//            return a.equals(candidate) || b.equals(candidate);
//        }

        @Override
        public int compareTo(Pair o) {
            var a_ = a().compareTo(o.a());
            return a_ != 0 ? a_ : b().compareTo(o.b());
        }
    }

    // simple wrapper around a Set. we don't need to track actual pairs
    // since the puzzles don't call for using that information. we just
    // store the boxes contained in a circuit in a Set.
    record Circuit(Set<JBox> boxes) {
        // returns true if candidate is part of any pairs in the circuit
        public boolean contains(JBox candidate) {
            return boxes.contains(candidate);
        }

        // add the boxes in a Pair to this circuit
        public void add(Pair pair) {
            boxes.add(pair.a());
            boxes.add(pair.b());
        }

        // count unique boxes in this circuit
        public int countBoxes() {
            return boxes.size();
        }

        // returns true if this circuit contains all the passed-in boxes
        public boolean containsAll(List<JBox> targets) {
            return boxes().containsAll(targets);
        }

        public void print() {
            boxes().stream().sorted().forEach(box -> {
                System.out.println("  " + box);
            });
        }

        // merge circuits together into a single circuit
        public static Circuit merge(List<Circuit> toMerge) {
            return new Circuit(toMerge.stream()
                    .flatMap((Circuit circuit) -> circuit.boxes.stream())
                    .collect(Collectors.toSet())
            );
        }
    }

    public record ConnectorState(int connectionsMade, List<Circuit> circuits) { }

    private final List<JBox> boxes;
    private final Predicate<ConnectorState> stopCondition;

    private List<Circuit> circuits = new ArrayList<>();
    private Pair lastPair;

    private CircuitsConnector(List<JBox> boxes, Predicate<ConnectorState> stopCondition) {
        this.boxes = boxes;
        this.stopCondition = stopCondition;
        run();
    }

    private void run() {
        // generate permutations: every possible pair and their distance, sorted
        List<Pair> allPairs = IntStream.range(0, boxes.size()).boxed().flatMap(i -> {
                    var box = boxes.get(i);
                    var candidates = boxes.subList(i + 1, boxes.size());
                    return candidates.stream().map(candidate -> Pair.normalize(box, candidate, box.dist(candidate)));
                })
                .sorted(Comparator.comparingDouble((Pair pair) -> pair.dist()))
                .collect(Collectors.toList());

        // loop: build circuits until we've met the stop condition
        var keepGoing = true;
        var i = 0;
        while(keepGoing) {
            var shortest = allPairs.removeFirst();
            var foundExistingCircuit = false;

            // this will replace the "circuits" variable
            var newCircuits = new ArrayList<Circuit>();

            var toMerge = new ArrayList<Circuit>();
            for (Circuit circuit : circuits) {
                if (circuit.contains(shortest.a()) && circuit.contains(shortest.b())) {
                    // if both are already in the circuit, do nothing
                    newCircuits.add(circuit);
                    foundExistingCircuit = true;
                } else if (circuit.contains(shortest.a()) || circuit.contains(shortest.b())) {
                    toMerge.add(circuit);
                    foundExistingCircuit = true;
                } else {
                    newCircuits.add(circuit);
                }
            }
            if (!toMerge.isEmpty()) {
                var merged = Circuit.merge(toMerge);
                merged.add(shortest);
                newCircuits.add(merged);
            }
            if (!foundExistingCircuit) {
                var newCircuit = new Circuit(new HashSet<>());
                newCircuit.add(shortest);
                newCircuits.add(newCircuit);
            }

            // update state variables for our loop
            circuits = newCircuits;
            i++;
            lastPair = shortest;

            keepGoing = !stopCondition.test(new ConnectorState(i, circuits));
        }
    }

    // part 1: return sum of sizes of three largest circuits
    public static int connectAndCount(Stream<String> input, int connections) {
        var boxes = JBox.parseInput(input);
        var connector = new CircuitsConnector(boxes, (state) -> state.connectionsMade == connections);
        var bySize = connector.circuits.stream()
                .sorted(Comparator.comparingInt((Circuit circuit) -> circuit.countBoxes()).reversed())
                .toList();
        return bySize.stream()
                .limit(3)
                .collect(foldLeft(() -> 1, (acc, circuit) -> acc * circuit.countBoxes()));
    }

    // part 2: return x values multiplied together, of the last pair that created a single circuit containing
    // all the boxes
    public static long connectUntilSingleCircuit(Stream<String> input) {
        var boxes = JBox.parseInput(input);
        var connector = new CircuitsConnector(boxes, (state) -> state.circuits().size() == 1 && state.circuits().getFirst().containsAll(boxes));
        var lastPair = connector.lastPair;
        return (long) lastPair.a().x() * (long) lastPair.b().x();
    }

}