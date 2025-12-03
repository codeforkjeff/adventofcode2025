package com.codefork.aoc2025.day01;

import com.codefork.aoc2025.Problem;

public class Part01 extends Problem {

    @Override
    public String solve() {
        return String.valueOf(Dial.doRotations(getInput()).landingAtZeroCount());
    }

    public static void main(String[] args) {
        new Part01().run();
    }
}
