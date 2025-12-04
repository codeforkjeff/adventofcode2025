package com.codefork.aoc2025.day04;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Part02 extends Problem {

    // TODO: this takes 8 seconds. there should be a way to optimize it.
    public int removeRolls(List<PrintingDeptFloor.Coord> rolls, int limit, int removed) {
        var adjacencyMap = PrintingDeptFloor.buildAdjacencyMap(rolls);
        var removable = adjacencyMap.entrySet().stream()
                .filter(entry -> entry.getValue() < limit)
                .map(Map.Entry::getKey)
                .toList();
        if(removable.isEmpty()) {
            return removed;
        } else {
            rolls.removeAll(removable);
            return removeRolls(rolls, limit, removed + removable.size());
        }
    }

    public int countRemovedRolls(Stream<String> input) {
        var rolls = PrintingDeptFloor.buildRolls(input);
        return removeRolls(rolls, 4, 0);
    }

    @Override
    public String solve() {
        //Assert.assertEquals("43", String.valueOf(countRemovedRolls(getSampleInput())));
        return String.valueOf(countRemovedRolls(getInput()));
    }

    public static void main(String[] args) {
        new Part02().run();
    }

}
