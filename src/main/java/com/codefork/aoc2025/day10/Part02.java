package com.codefork.aoc2025.day10;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

public class Part02 extends Problem {

    @Override
    public String solve() {
        var machinesSample = Machine.parseInput(getSampleInput());
        Assert.assertEquals("33", String.valueOf(Machine.sumSmallestNumOfButtonPressesForJoltages(machinesSample)));

        var machines = Machine.parseInput(getInput());
        return String.valueOf(Machine.sumSmallestNumOfButtonPressesForJoltages(machines));
    }

    public static void main(String[] args) {
        new Part02().run();
    }

}
