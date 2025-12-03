package com.codefork.aoc2025.day01;

import java.util.stream.Stream;

import static com.codefork.aoc2025.util.FoldLeft.foldLeft;

public record Dial(int position, int landingAtZeroCount, int encounteringZeroCount) {

    public static Dial doRotations(Stream<String> input) {
        var dialSize = 100;
        return input.collect(foldLeft(
                () -> new Dial(50, 0, 0),
                (acc, line) -> {
                    var dir = line.charAt(0);
                    var distance = Integer.parseInt(line.substring(1));
                    var distanceToAdd = distance * (dir == 'L' ? -1 : 1);
                    var newPositionModded = (acc.position + distanceToAdd) % dialSize;
                    var newPosition = newPositionModded + (newPositionModded < 0 ? dialSize : 0);

                    var newLandingAtZeroCount = acc.landingAtZeroCount + (newPosition == 0 ? 1 : 0);

                    // number of complete rotations, which each pass 0 once
                    var completeRotations = distance / dialSize;
                    // remainder after complete rotations, which may or may not pass 0
                    var remainder = distance % dialSize;
                    // if starting position is 0, the remainder can't pass zero
                    var incompleteRotationsPassingZero = (acc.position != 0) ?
                            ((dir == 'L' && acc.position - remainder <= 0) || (dir == 'R' && acc.position + remainder >= dialSize) ? 1 : 0) :
                            0;

                    var newEncounteringZeroCount = acc.encounteringZeroCount + completeRotations + incompleteRotationsPassingZero;
                    return new Dial(newPosition, newLandingAtZeroCount, newEncounteringZeroCount);
                }));
    }

}
