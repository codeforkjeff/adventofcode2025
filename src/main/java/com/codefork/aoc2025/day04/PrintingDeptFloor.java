package com.codefork.aoc2025.day04;

import com.codefork.aoc2025.util.Grid;
import com.codefork.aoc2025.util.Pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.codefork.aoc2025.util.FoldLeft.foldLeft;

public class PrintingDeptFloor {

    // build a list of coordinates of rolls from input
    public static List<Pos> buildRolls(Stream<String> input) {
        return Grid.parse(input,
                () -> new ArrayList<Pos>(),
                (acc, x, y, ch) -> {
                    if (ch.equals("@")) {
                        acc.add(new Pos(x, y));
                    }
                    return acc;
                });
    }

    public static Map<Pos, Integer> buildAdjacencyMap(List<Pos> rolls) {
        // build a map of how many rolls are adjacent to a roll
        return rolls.stream().collect(foldLeft(
                () -> new HashMap<Pos, Integer>(),
                (acc, roll) -> {
                    var adjacent = roll.getAdjacent().stream()
                            .filter(r -> rolls.contains(r)).count();
                    acc.put(roll, (int) adjacent);
                    return acc;
                }));
    }

    public static long countRollsWithAdjacent(Stream<String> input, int limit) {
        var rolls = buildRolls(input);
        var adjacencyMap = buildAdjacencyMap(rolls);
        return adjacencyMap.entrySet().stream().filter(entry ->
                entry.getValue() < limit
        ).count();
    }

    // iteratively remove rolls from adjacencyMap until there are no rolls
    // with fewer than "limit" adjacent rolls
    public static int removeRolls(Map<Pos, Integer> adjacencyMap, int limit, int removed) {
        var removable = adjacencyMap.entrySet().stream()
                .filter(entry -> entry.getValue() < limit)
                .map(Map.Entry::getKey)
                .toList();
        if (removable.isEmpty()) {
            return removed;
        } else {
            // mutate adjacencyMap, instead of doing it in a more FP style, for performance
            for (Pos roll : removable) {
                roll.getAdjacent().stream()
                        .filter(adjacencyMap::containsKey)
                        .forEach(c -> {
                            var newValue = adjacencyMap.get(c) - 1;
                            adjacencyMap.put(c, newValue);
                        });
                adjacencyMap.remove(roll);
            }
            return removeRolls(adjacencyMap, limit, removed + removable.size());
        }
    }

    // iteratively remove rolls and return a count of total rolls removed
    public static int countRemovedRolls(Stream<String> input) {
        var rolls = buildRolls(input);
        var adjacencyMap = buildAdjacencyMap(rolls);
        return removeRolls(adjacencyMap, 4, 0);
    }

}
