package com.codefork.aoc2025.day09;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

public class Part01 extends Problem {

    @Override
    public String solve() {
        Assert.assertEquals("50", String.valueOf(TileFloor.findAreaOfLargestRectangle(getSampleInput())));
        return String.valueOf(TileFloor.findAreaOfLargestRectangle(getInput()));
    }

    public static void main(String[] args) {
        new Part01().run();
    }

}
