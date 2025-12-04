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
}
