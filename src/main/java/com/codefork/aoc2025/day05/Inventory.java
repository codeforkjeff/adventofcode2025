package com.codefork.aoc2025.day05;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.codefork.aoc2025.util.FoldLeft.foldLeft;

public class Inventory {

    public record Range(long start, long end) {

        public boolean overlaps(Range other) {
            // handle all the 4 cases here:
            // 1) start of other range falls within this range
            // 2) end of other range falls within this range
            // 3) other range falls entirely within this range
            // 4) other range's start and end are outside the bounds of this range
            return (other.start >= start && other.start <= end) || (other.end >= start && other.end <= end) ||
                    (start >= other.start && start <= other.end) || (end >= other.start && end <= other.end);
        }

        // returns the superset of this range + other range
        public Optional<Range> combine(Range other) {
            if (overlaps(other)) {
                return Optional.of(new Range(Math.min(start, other.start), Math.max(end, other.end)));
            }
            return Optional.empty();
        }

        public long size() {
            return end - start + 1;
        }
    }

    public record Database(List<Range> fresh, List<Long> ingredients) {

        /**
         * transform a list of ranges by merging overlapping ones
         */
        public static List<Range> merge(List<Range> ranges) {
            // sort our ranges, then transform list into another list, combining each item
            // with the last one if possible
            var sorted = ranges.stream().sorted(Comparator.comparing(Range::start));
            return sorted.collect(foldLeft(
                    () -> new ArrayList<Range>(),
                    (acc, range) -> {
                        if(!acc.isEmpty()) {
                            Range last = acc.getLast();
                            var combined = last.combine(range);
                            acc.removeLast();
                            acc.addAll(combined.map(List::of).orElse(List.of(last, range)));
                        } else {
                            acc.add(range);
                        }
                        return acc;
                    })).stream().toList();
        }

        public long getNumFreshIngredients() {
            return ingredients().stream().filter(ingredient ->
                            fresh().stream().anyMatch(fresh ->
                                    ingredient >= fresh.start() && ingredient <= fresh.end()))
                    .count();
        }

        public long getNumFreshIngredientIDs() {
            // we can easily count all the fresh ingredient IDs if none of the ranges are overlapping.
            // so merge the overlapping ones.
            return merge(fresh).stream().mapToLong(Range::size).sum();
        }
    }

    private sealed interface Mode { }
    private record Ranges() implements Mode { }
    private record Ingredients() implements Mode { }

    private record FileState(Database db, Mode mode) {
    }

    public static Database readDatabase(Stream<String> input) {
        return input.collect(foldLeft(() -> new FileState(new Database(new ArrayList<Range>(), new ArrayList<Long>()), new Ranges()), (acc, line) -> {
            if (!line.equals("")) {
                // look ma, pattern matching!
                switch(acc.mode) {
                    case Ranges() -> {
                        var parts = line.split("-");
                        var range = new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
                        acc.db().fresh().add(range);
                    }
                    case Ingredients() -> {
                        acc.db().ingredients().add(Long.parseLong(line));
                    }
                };
                return acc;
            } else {
                return new FileState(acc.db(), new Ingredients());
            }
        })).db();
    }
}
