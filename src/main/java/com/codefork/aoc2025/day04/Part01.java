package com.codefork.aoc2025.day04;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

public class Part01 extends Problem {

    @Override
    public String solve() {
        Assert.assertEquals("13", String.valueOf(PrintingDeptFloor.countRollsWithAdjacent(getSampleInput(), 4)));
        return String.valueOf(PrintingDeptFloor.countRollsWithAdjacent(getInput(), 4));
    }

    public static void main(String[] args) {
        new Part01().run();
    }
}
