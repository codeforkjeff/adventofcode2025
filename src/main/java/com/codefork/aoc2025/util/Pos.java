package com.codefork.aoc2025.util;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Encapsulates an x,y position in a grid which is central to many puzzles.
 *
 * @param x
 * @param y
 */
public record Pos(int x, int y) {

    /**
     * returns adjacent positions, including ones adjacent on the diagonal.
     * this doesn't check bounds, so invalid positions (e.g. -1, -1) may be returned.
     */
    public List<Pos> getAdjacent() {
        return IntStream.range(x - 1, x + 2).boxed().flatMap(newX ->
                IntStream.range(y - 1, y + 2).mapToObj(newY -> new Pos(newX, newY))
        ).filter(pos -> !pos.equals(this)).toList();
    }

}
