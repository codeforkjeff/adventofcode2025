package com.codefork.aoc2025.day07;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

import java.util.stream.Stream;

public class Part02 extends Problem {

    public long runManifold(Stream<String> input) {
        return Manifold.init(input).run().numTimelines();
    }

    @Override
    public String solve() {
        Assert.assertEquals("40", String.valueOf(runManifold(getSampleInput())));
        return String.valueOf(runManifold(getInput()));
    }

    public static void main(String[] args) {
        new Part02().run();
    }
}
