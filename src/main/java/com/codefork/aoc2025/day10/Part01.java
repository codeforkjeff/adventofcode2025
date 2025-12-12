package com.codefork.aoc2025.day10;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

public class Part01 extends Problem {

    @Override
    public String solve() {
        var machinesSample = Machine.parseInput(getSampleInput());
        Assert.assertEquals("7", String.valueOf(Machine.sumSmallestNumOfButtonPresses(machinesSample)));

        var machines = Machine.parseInput(getInput());
        return String.valueOf(Machine.sumSmallestNumOfButtonPresses(machines));
    }

    public static void main(String[] args) {
        new Part01().run();
    }

}
