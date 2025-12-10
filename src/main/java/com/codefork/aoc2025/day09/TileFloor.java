package com.codefork.aoc2025.day09;

import com.codefork.aoc2025.util.Pos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.codefork.aoc2025.util.FoldLeft.foldLeft;

public class TileFloor {

    public record Rectangle(Pos corner1, Pos corner2, long area) {
        public static Rectangle init(Pos corner1, Pos corner2) {
            var area = (long) (Math.abs(corner1.x() - corner2.x()) + 1) *
                    (long) (Math.abs(corner1.y() - corner2.y()) + 1);
            return new Rectangle(corner1, corner2, area);
        }

        public List<Pos> getOtherCorners() {
            return List.of(new Pos(corner1.x(), corner2.y()),
                    new Pos(corner2.x(), corner1.y()));
        }

        public static Comparator<Rectangle> areaComparator = Comparator.comparingLong(Rectangle::area);
    }

    public static List<Pos> parseInput(Stream<String> input) {
        return input.map(line -> {
            var parts = Arrays.stream(line.split(",")).map(Integer::parseInt).toList();
            return new Pos(parts.get(0), parts.get(1));
        }).toList();
    }

    public static List<Rectangle> getCandidateRectangles(List<Pos> redTiles) {
        return IntStream.range(0, redTiles.size()).boxed()
                .flatMap(i -> {
                    var corner1 = redTiles.get(i);
                    var candidateCorners = redTiles.subList(i + 1, redTiles.size());
                    return candidateCorners.stream().map(corner2 -> Rectangle.init(corner1, corner2));
                })
                .toList();
    }

    // part 1
    public static long findAreaOfLargestRectangle(Stream<String> input) {
        var redTiles = parseInput(input);
        var candidateRectangles = getCandidateRectangles(redTiles);
        var largest = candidateRectangles.stream().max(Rectangle.areaComparator);
        return largest.orElseThrow().area();
    }

    record Perimeter(Set<Pos> tiles, Pos first, Pos last) { }

    public static final Pos INITIAL = new Pos(-1, -1);

    // part 2: this doesn't work
    public static long findAreaOfLargestRectangleInsideLoop(Stream<String> input) {
        var redTiles = parseInput(input);

        var perimeter = redTiles.stream().collect(foldLeft(
                () -> new Perimeter(new HashSet<>(), INITIAL, INITIAL),
                (acc, redTile) -> {
                    acc.tiles().add(redTile);
                    if(!INITIAL.equals(acc.last())) {
                        var perimeterEdge = redTile.line(acc.last());
                        acc.tiles().addAll(perimeterEdge);
                    }
                    return new Perimeter(
                            acc.tiles(),
                            acc.first().equals(INITIAL) ? redTile : acc.first(),
                            redTile);
                }));
        // add last edge
        perimeter.tiles().addAll(perimeter.last().line(perimeter.first()));

        System.out.println("num of tiles in perimeter = " + perimeter.tiles().size());

        var candidateRectangles = getCandidateRectangles(redTiles);
        System.out.println("total number of rectangles = " + candidateRectangles.size());

        // reduce candidate space: keep only rectangles where at least 3 corners are on the perimeter.
        // this has to be true of the solution. Nope, this isn't true, and doesn't work!
        var filtered = candidateRectangles.stream().filter(rect -> {
            var numOtherCornersOnPerimeter =  rect.getOtherCorners().stream().filter(corner ->
                perimeter.tiles().contains(corner)
            ).count();
            // if the remaining corner is inside the loop
            return numOtherCornersOnPerimeter >= 1;
        }).toList();

        System.out.println("total number after filtering = " + filtered.size());

        return 0;
    }

}
