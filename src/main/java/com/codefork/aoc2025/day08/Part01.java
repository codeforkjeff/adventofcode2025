package com.codefork.aoc2025.day08;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

public class Part01 extends Problem {

    @Override
    public String solve() {
        Assert.assertEquals("40", String.valueOf(CircuitsConnector.connectAndCount(getSampleInput(), 10)));
        return String.valueOf(CircuitsConnector.connectAndCount(getInput(), 1000));
    }

    public static void main(String[] args) {
        new Part01().run();
    }

}
