package com.codefork.aoc2025.day06;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.codefork.aoc2025.util.FoldLeft.foldLeft;

public class Part02 extends Problem {

    // end is inclusive
    record Group(String op, int start, int end) {
    }

    public static final long NO_NUMBER = -1L;

    public static long getSumOfGroupOps(Stream<String> input) {
        var lines = input.collect(Collectors.toList());
        var operators = lines.removeLast();

        // we have to use longestLine when iterating over any line, to ensure we
        // deal with extra spaces
        int longestLine = lines.stream()
                .mapToInt(String::length)
                .max()
                .orElseThrow();

        // parse operatorsRaw into a list of Groups indicating start and end columns
        // pertaining to that operator
        var operatorGroups = IntStream.range(0, longestLine).boxed().collect(foldLeft(
                () -> new ArrayList<Group>(),
                (acc, idx) -> {
                    var op = idx < operators.length() ? operators.substring(idx, idx + 1) : " ";
                    // handle last element
                    if (idx == longestLine - 1) {
                        var lastGroup = acc.removeLast();
                        acc.add(new Group(lastGroup.op(), lastGroup.start(), idx));
                    } else {
                        if (!op.equals(" ")) {
                            if (!acc.isEmpty()) {
                                var lastGroup = acc.removeLast();
                                acc.add(new Group(lastGroup.op(), lastGroup.start(), idx - 1));
                            }
                            // use -1 as placeholder for end until we reach the next group
                            acc.add(new Group(op, idx, -1));
                        }
                    }
                    return acc;
                }
        ));

        // pivot the input: transform the columns of numbers into numbers
        var numbers = IntStream.range(0, longestLine).mapToObj(idx -> {
            var digitsString = lines.stream()
                    .map(line -> idx < line.length() ? line.substring(idx, idx + 1) : "")
                    .filter(digit -> !digit.equals("") && !digit.equals(" "))
                    .collect(Collectors.joining()).strip();
            if (!digitsString.isEmpty()) {
                return Long.parseLong(digitsString.strip());
            }
            return NO_NUMBER;
        }).toList();

        // sum the result of operations on each group
        return operatorGroups.stream().collect(foldLeft(
                () -> 0L,
                ((sumOfAllOps, group) -> {
                    var result = IntStream.range(group.start(), group.end() + 1)
                            .mapToLong(numbers::get)
                            .filter(n -> n != NO_NUMBER)
                            .reduce((acc, n) ->
                                    switch (group.op()) {
                                        case "*" -> acc * n;
                                        case "+" -> acc + n;
                                        default -> throw new RuntimeException("unrecognized operator: " + group.op());
                                    }
                            );
                    return sumOfAllOps + result.orElseThrow();
                }))
        );
    }

    @Override
    public String solve() {
        Assert.assertEquals("3263827", String.valueOf(getSumOfGroupOps(getSampleInput())));
        return String.valueOf(getSumOfGroupOps(getInput()));
    }

    public static void main(String[] args) {
        new Part02().run();
    }
}
