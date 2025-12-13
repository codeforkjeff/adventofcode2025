package com.codefork.aoc2025.day11;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

public class Part02 extends Problem {

    @Override
    public String solve() {
        Assert.assertEquals("2", String.valueOf(Rack.parseInput(getFileAsStream("sample2")).getNumPathsToOutThroughDacAndFft()));
        return String.valueOf(Rack.parseInput(getInput()).getNumPathsToOutThroughDacAndFft());
    }

    public static void main(String[] args) {
        new Part02().run();
    }

}
