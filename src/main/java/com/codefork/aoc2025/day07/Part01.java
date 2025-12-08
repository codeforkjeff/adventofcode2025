package com.codefork.aoc2025.day07;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

import java.util.stream.Stream;

public class Part01 extends Problem {

    public long runManifold(Stream<String> input) {
        return Manifold.init(input).run().numSplittersHit();
    }

    @Override
    public String solve() {
        Assert.assertEquals("21", String.valueOf(runManifold(getSampleInput())));
        return String.valueOf(runManifold(getInput()));
    }

    public static void main(String[] args) {
        new Part01().run();
    }
}
