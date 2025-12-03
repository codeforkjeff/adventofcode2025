package com.codefork.aoc2025.day02;

import com.codefork.aoc2025.Problem;

public class Part01 extends Problem {

    @Override
    public String solve() {
        return String.valueOf(Range.sumOfInvalidIds(getInput(), (i) -> {
            var s = String.valueOf(i);
            if (s.length() % 2 == 0) {
                var firstHalf = s.substring(0, s.length() / 2);
                var secondHalf = s.substring(s.length() / 2);
                return firstHalf.equals(secondHalf);
            }
            return false;
        }));
    }

    public static void main(String[] args) {
        new Part01().run();
    }
}
