package com.codefork.aoc2025.day02;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

import java.util.stream.IntStream;

public class Part02 extends Problem {

    public boolean isInvalid(Long i) {
        var s = String.valueOf(i);
        var strlen = s.length();
        // find all the divisors for length of the string; include its length too
        var divisors = IntStream.concat(
                IntStream.range(2, (strlen / 2) + 1).filter(n -> strlen % n == 0),
                IntStream.of(strlen)
        );
        return divisors.anyMatch(divisor -> {
            var partLength = strlen / divisor;
            var parts = IntStream.range(0, divisor)
                    .mapToObj(n -> s.substring(n * partLength, (n * partLength) + partLength)).toList();
            // has to repeat at least twice
            if (parts.size() >= 2) {
                var unequal = parts.stream().reduce((acc, part) -> {
                    if (!acc.equals(part)) {
                        return "UNEQUAL";
                    }
                    return part;
                });
                return unequal.filter(value -> value.equals("UNEQUAL")).isEmpty();
            } else {
                return false;
            }
        });
    }

    @Override
    public String solve() {
        Assert.assertEquals("4174379265", String.valueOf(Range.sumOfInvalidIds(getSampleInput(), this::isInvalid)));
        return String.valueOf(Range.sumOfInvalidIds(getInput(), this::isInvalid));
    }

    public static void main(String[] args) {
        new Part02().run();
    }
}
