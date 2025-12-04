package com.codefork.aoc2025.day04;

import com.codefork.aoc2025.util.Grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.codefork.aoc2025.util.FoldLeft.foldLeft;

public class PrintingDeptFloor {

    public record Coord(int x, int y) {
    }

    // build a list of coordinates of rolls from input
    public static List<Coord> buildRolls(Stream<String> input) {
        return Grid.parse(input,
                () -> new ArrayList<Coord>(),
                (acc, x, y, ch) -> {
                    if (ch.equals("@")) {
                        acc.add(new Coord(x, y));
                    }
                    return acc;
                });
    }

    public static Map<Coord, Integer> buildAdjacencyMap(List<Coord> rolls) {
        // build a map of how many rolls are adjacent to a roll
        return rolls.stream().collect(foldLeft(
                () -> new HashMap<Coord, Integer>(),
                (acc, roll) -> {
                    var x = roll.x;
                    var y = roll.y;
                    var adjacent = IntStream.range(x - 1, x + 2).boxed().flatMap(newX ->
                            IntStream.range(y - 1, y + 2).mapToObj(newY -> new Coord(newX, newY))
                    ).filter(r -> !r.equals(roll) && rolls.contains(r)).count();
                    acc.put(roll, (int) adjacent);
                    return acc;
                }));
    }

    // iteratively remove rolls from adjacencyMap until there are no rolls
    // with fewer than "limit" adjacent rolls
    public static int removeRolls(Map<Coord, Integer> adjacencyMap, int limit, int removed) {
        var removable = adjacencyMap.entrySet().stream()
                .filter(entry -> entry.getValue() < limit)
                .map(Map.Entry::getKey)
                .toList();
        if (removable.isEmpty()) {
            return removed;
        } else {
            // mutate adjacencyMap, instead of doing it in a more FP style, for performance
            for (Coord coord : removable) {
                var x = coord.x();
                var y = coord.y();
                IntStream.range(x - 1, x + 2).boxed().flatMap(newX ->
                                IntStream.range(y - 1, y + 2).mapToObj(newY -> new Coord(newX, newY))
                        ).filter(c -> adjacencyMap.containsKey(c) && !c.equals(coord))
                        .forEach(c -> {
                            var newValue = adjacencyMap.get(c) - 1;
                            adjacencyMap.put(c, newValue);
                        });
                adjacencyMap.remove(coord);
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
