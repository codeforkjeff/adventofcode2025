package com.codefork.aoc2025.day01;

import com.codefork.aoc2025.Problem;

public class Part02 extends Problem {

    @Override
    public String solve() {
        return String.valueOf(Dial.doRotations(getInput()).encounteringZeroCount());
    }

    public static void main(String[] args) {
        new Part02().run();
    }
}
