package com.codefork.aoc2025.day03;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

public class Part02 extends Problem {

    @Override
    public String solve() {
        Assert.assertEquals("3121910778619", String.valueOf(LargestNumberFinder.sumOfLargestNumberPerLine(getSampleInput(), 12)));
        return String.valueOf(LargestNumberFinder.sumOfLargestNumberPerLine(getInput(), 12));
    }

    public static void main(String[] args) {
        new Part02().run();
    }
}
