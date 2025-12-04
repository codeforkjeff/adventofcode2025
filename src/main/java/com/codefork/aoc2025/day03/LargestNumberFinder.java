package com.codefork.aoc2025.day03;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.codefork.aoc2025.util.FoldLeft.foldLeft;

public class LargestNumberFinder {
    /**
     * Finds the largest digit within (start, end) of bank,
     * and returns its index. "end" is exclusive.
     * If the largest digit occurs multiple times, return the first one,
     * since that maximizes selection of remaining digits.
     */
    private static int indexOfLargestDigit(List<Integer> bank, int start, int end) {
        record Largest(int index, int digit) {
        }

        return IntStream.range(start, end).boxed().collect(
                foldLeft(
                        () -> new Largest(-1, -1),
                        (acc, idx) -> {
                            var digit = bank.get(idx);
                            if (digit > acc.digit) {
                                return new Largest(idx, digit);
                            }
                            return acc;
                        })).index();
    }

    public static long sumOfLargestNumberPerLine(Stream<String> input, int totalDigits) {
        record LargestNumberBuilder(long largestNumber, int numRemainingDigits, int remainderIdx) {
        }
        return input.mapToLong(line -> {
            var bank = line.chars().mapToObj(ch -> Integer.parseInt(String.valueOf((char) ch))).toList();
            return IntStream.range(0, totalDigits).boxed().collect(
                    foldLeft(
                            () -> new LargestNumberBuilder(0, totalDigits, 0),
                            (acc, idx) -> {
                                // strategy: successive digits must be found within the first N digits of whatever's remaining

                                // get largest number in relevant slice of the List
                                var largestDigitIdx = indexOfLargestDigit(bank, acc.remainderIdx(), bank.size() - acc.numRemainingDigits + 1);
                                var newLargestNumber = (acc.largestNumber * 10) + bank.get(largestDigitIdx);
                                //System.out.println("largest number so far = " + newLargestNumber);
                                return new LargestNumberBuilder(newLargestNumber, acc.numRemainingDigits - 1, largestDigitIdx + 1);
                            }
                    )
            ).largestNumber();
        }).sum();
    }

}
