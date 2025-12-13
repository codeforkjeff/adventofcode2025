package com.codefork.aoc2025.day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codefork.aoc2025.util.FoldLeft.foldLeft;

record Rack(Map<String, Set<String>> nodes) {
    public static Rack parseInput(Stream<String> input) {
        var nodes = input.collect(foldLeft(
                        () -> new HashMap<String, Set<String>>(),
                        (acc, line) -> {
                            var parts = line.split(":");
                            var id = parts[0];
                            var outputs = Arrays.stream(parts[1].strip().split(" ")).collect(Collectors.toSet());
                            acc.put(id, outputs);
                            return acc;
                        }
                )
        );
        return new Rack(nodes);
    }

    // part 1
    public int getNumPathsToOut() {
        return getNumPathsToOut("you");
    }

    public int getNumPathsToOut(String current) {
        if (current.equals("out")) {
            return 1;
        }
        var outputs = nodes.get(current);
        if (outputs != null) {
            return outputs.stream()
                    .mapToInt(output -> getNumPathsToOut(output))
                    .sum();
        } else {
            return 0;
        }
    }

    // part 2
    public long getNumPathsToOutThroughDacAndFft() {
        // strategy: naively traversing every the path in the graph has a few challenges:
        // - it's way too many paths to traverse, takes too long
        // - we can't store the successful paths we find, there's too many of them, we'd run out of memory
        // - since we don't store actual paths, tracking the state of when we've encountered fft and dac is tricky
        // - we can't cache the results of subgraphs we've traverse, because it's the nodes we've visited already
        //   SO FAR in a path that matters
        //
        // we MUST cache in order for this solution to be performant. and the only way caching
        // works is if we break up the problem in the fashion below. broken up in this way, we don't need to track
        // state whatsoever, we just count unique paths from src to dest, and then we multiply to get the
        // final answer

        // since there are no cycles, traversing from svr to out must encounter either dac or fft first.
        // determine which.
        var countFftFirst = getNumPathsBetween("fft", "dac", new ArrayList<String>(), new HashMap<String, Integer>());
        var countDacFirst = getNumPathsBetween("dac", "fft", new ArrayList<String>(), new HashMap<String, Integer>());

        var src = countFftFirst > 0 ? "fft" : "dac";
        var dest = countFftFirst > 0 ? "dac" : "fft";
        var numPathsBetweenFftAndDac = Math.max(countFftFirst, countDacFirst);

        var numPathsBeforeSubgraph = getNumPathsBetween("svr", src, new ArrayList<String>(), new HashMap<String, Integer>());
        var numPathsAfterSubgraph = getNumPathsBetween(dest, "out", new ArrayList<String>(), new HashMap<String, Integer>());

        return (long) numPathsBeforeSubgraph * (long) numPathsBetweenFftAndDac * (long) numPathsAfterSubgraph;
    }

    // find the number of unique paths between src and dest, caching the counts of intermediate nodes encountered
    public int getNumPathsBetween(
            String src,
            String dest,
            List<String> path,
            Map<String, Integer> countCache) {

        if (countCache.containsKey(src)) {
            return countCache.get(src);
        }

        var newPath = new ArrayList<>(path);
        newPath.add(src);

        if (src.equals(dest)) {
            return 1;
        }

        var outputs = nodes.get(src);
        if (outputs != null) {
            var sumOfBranches = 0;
            for (var output : outputs) {
                var countsForBranch = getNumPathsBetween(
                        output, dest, newPath, countCache);
                countCache.put(output, countsForBranch);
                sumOfBranches += countsForBranch;
            }
            return sumOfBranches;
        } else {
            return 0;
        }
    }
}
