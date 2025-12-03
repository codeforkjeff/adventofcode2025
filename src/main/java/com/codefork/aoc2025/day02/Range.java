package com.codefork.aoc2025.day02;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public record Range(long start, long end) {

    public static Long sumOfInvalidIds(Stream<String> input, Predicate<Long> isInvalid) {
        return input.flatMap(line -> {
                    var rangeList = line.split(",");
                    return Arrays.stream(rangeList).map(rangeStr -> {
                        var rangeParts = rangeStr.split("-");
                        return new Range(Long.parseLong(rangeParts[0]), Long.parseLong(rangeParts[1]));
                    });
                }).flatMap(range -> {
                    return LongStream.range(range.start, range.end + 1).filter(isInvalid::test).boxed();
                }).reduce(Long::sum)
                .orElseGet(() -> 0L);
    }

}

