package com.codefork.aoc2025.day04;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

import java.util.stream.Stream;

public class Part01 extends Problem {

    public long countRollsWithAdjacent(Stream<String> input, int limit) {
        var rolls = PrintingDeptFloor.buildRolls(input);
        var adjacencyMap = PrintingDeptFloor.buildAdjacencyMap(rolls);
        return adjacencyMap.entrySet().stream().filter(entry ->
                entry.getValue() < limit
        ).count();
    }

    @Override
    public String solve() {
        Assert.assertEquals("13", String.valueOf(countRollsWithAdjacent(getSampleInput(), 4)));
        return String.valueOf(countRollsWithAdjacent(getInput(), 4));
    }

    public static void main(String[] args) {
        new Part01().run();
    }
}
