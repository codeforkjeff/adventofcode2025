package com.codefork.aoc2025.day06;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;
import com.codefork.aoc2025.util.WithIndex;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codefork.aoc2025.util.FoldLeft.foldLeft;

public class Part01 extends Problem {

    public static long getSumOfAllOps(Stream<String> input) {
        var lines = input
                .map(line -> line.strip().split("\\s+"))
                .collect(Collectors.toList());

        var operators = lines.removeLast();

        var linesOfNumbers = lines.stream().map(numberStrings ->
                Arrays.stream(numberStrings).map(Long::parseLong).toList()
        ).toList();

        return Arrays.stream(operators).map(WithIndex.indexed()).map(entry -> {
            var idx = entry.index();
            var op = entry.value();
            return linesOfNumbers.stream().collect(foldLeft(
                    () -> -1L,
                    (acc, line) -> {
                        return switch (op) {
                            case "+" -> acc != -1 ? acc + line.get(idx) : line.get(idx);
                            case "*" -> acc != -1 ? acc * line.get(idx) : line.get(idx);
                            default -> throw new RuntimeException("unrecognized operator " + op);
                        };
                    }
            ));
        }).reduce(0L, Long::sum);
    }

    @Override
    public String solve() {
        Assert.assertEquals("4277556", String.valueOf(getSumOfAllOps(getSampleInput())));
        return String.valueOf(getSumOfAllOps(getInput()));
    }

    public static void main(String[] args) {
        new Part01().run();
    }
}
