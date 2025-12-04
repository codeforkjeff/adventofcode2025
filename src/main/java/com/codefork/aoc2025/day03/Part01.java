package com.codefork.aoc2025.day03;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

public class Part01 extends Problem {

    @Override
    public String solve() {
        Assert.assertEquals("357", String.valueOf(LargestNumberFinder.sumOfLargestNumberPerLine(getSampleInput(), 2)));
        return String.valueOf(LargestNumberFinder.sumOfLargestNumberPerLine(getInput(), 2));
    }

    public static void main(String[] args) {
        new Part01().run();
    }
}
