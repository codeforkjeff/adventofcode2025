package com.codefork.aoc2025.util;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Encapsulates an x,y position in a grid which is central to many puzzles.
 *
 * @param x
 * @param y
 */
public record Pos(int x, int y) implements Comparable<Pos> {

    /**
     * returns adjacent positions, including ones adjacent on the diagonal.
     * this doesn't check bounds, so invalid positions (e.g. -1, -1) may be returned.
     */
    public List<Pos> getAdjacent() {
        return IntStream.range(x - 1, x + 2).boxed().flatMap(newX ->
                IntStream.range(y - 1, y + 2).mapToObj(newY -> new Pos(newX, newY))
        ).filter(pos -> !pos.equals(this)).toList();
    }

    public Pos withX(int x) {
        return new Pos(x, y);
    }

    public Pos withY(int y) {
        return new Pos(x, y);
    }

    // return the set of points between this point and other, inclusive.
    // both points might lie on the same axis (either have same x or y value)
    public List<Pos> line(Pos other) {
        if(x() == other.x()) {
            var minY = Math.min(y(), other.y());
            var maxY = Math.max(y(), other.y());
            return IntStream.range(minY, maxY).mapToObj(y -> new Pos(x(), y)).toList();
        } else if(y() == other.y()) {
            var minX = Math.min(x(), other.x());
            var maxX = Math.max(x(), other.x());
            return IntStream.range(minX, maxX).mapToObj(x -> new Pos(x, y())).toList();
        }
        throw new RuntimeException("can't return points for a line between " + this + " and " + other);
    }

    @Override
    public int compareTo(Pos o) {
        var x_ = Integer.compare(x(), o.x());
        return x_ != 0 ? x_ : Integer.compare(y(), o.y());
    }
}
