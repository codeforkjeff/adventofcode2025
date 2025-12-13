package com.codefork.aoc2025.day11;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

public class Part01 extends Problem {

    @Override
    public String solve() {
        Assert.assertEquals("5", String.valueOf(Rack.parseInput(getSampleInput()).getNumPathsToOut()));
        return String.valueOf(Rack.parseInput(getInput()).getNumPathsToOut());
    }

    public static void main(String[] args) {
        new Part01().run();
    }

}
