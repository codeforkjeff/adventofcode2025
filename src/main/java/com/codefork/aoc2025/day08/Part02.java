package com.codefork.aoc2025.day08;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

public class Part02 extends Problem {

    @Override
    public String solve() {
        Assert.assertEquals("25272", String.valueOf(CircuitsConnector.connectUntilSingleCircuit(getSampleInput())));
        return String.valueOf(CircuitsConnector.connectUntilSingleCircuit(getInput()));
    }

    public static void main(String[] args) {
        new Part02().run();
    }

}
