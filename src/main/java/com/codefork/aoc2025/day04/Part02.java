package com.codefork.aoc2025.day04;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

public class Part02 extends Problem {

    @Override
    public String solve() {
        Assert.assertEquals("43", String.valueOf(PrintingDeptFloor.countRemovedRolls(getSampleInput())));
        return String.valueOf(PrintingDeptFloor.countRemovedRolls(getInput()));
    }

    public static void main(String[] args) {
        new Part02().run();
    }

}
