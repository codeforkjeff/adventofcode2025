package com.codefork.aoc2025.day07;

import com.codefork.aoc2025.util.Grid;
import com.codefork.aoc2025.util.Pos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

record Manifold(Set<Pos> splitters, Pos start, int maxX, int maxY) {

    public static Manifold init(Stream<String> input) {
        return Grid.parse(
                input,
                () -> new Manifold(new HashSet<>(), new Pos(-1, -1), 0, 0),
                (acc, x, y, ch) -> {
                    var pos = new Pos(x, y);
                    var newStart = ch.equals("S") ? pos : acc.start();
                    if (ch.equals("^")) {
                        acc.splitters().add(pos);
                    }
                    return new Manifold(acc.splitters(), newStart, x, y);
                }
        );
    }

    record RunResult(long numSplittersHit, long numTimelines) {
    }

    /**
     * Run the manifold. Does the work to solve both part 1 and part 2,
     * updating separate state for each, as it encounters splitters.
     */
    public RunResult run() {
        var splittersHit = new HashSet<Pos>();
        var numTimelines = new HashMap<Pos, Long>();
        var count = run(start, splittersHit, numTimelines);
        return new RunResult(splittersHit.size(), count);
    }

    private long run(Pos pos, Set<Pos> splittersHit, Map<Pos, Long> numTimelines) {
        // track which splitter we've encountered in splittersHit for part 1.
        // return the number of timelines for the tachyon at position 'pos', calculating
        //   it recursively, for part 2.

        if (pos.y() <= maxY()) {
            var nextPos = new Pos(pos.x(), pos.y() + 1);
            if (splitters().contains(nextPos)) {
                //System.out.println("Split at " + nextPos);
                splittersHit.add(nextPos);
                var branches = Set.of(nextPos.withX(nextPos.x() - 1), nextPos.withX(nextPos.x() + 1));
                return branches.stream().mapToLong(branch -> {
                            var opt = Optional.ofNullable(numTimelines.get(branch));
                            return opt.orElseGet(() -> {
                                var count = run(branch, splittersHit, numTimelines);
                                numTimelines.put(branch, count);
                                return count;
                            });
                        }
                ).sum();
            } else {
                return run(nextPos, splittersHit, numTimelines);
            }
        } else {
            return 1;
        }
    }

}
