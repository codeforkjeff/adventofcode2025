package com.codefork.aoc2025.day05;

import java.util.ArrayList;
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

        // iteratively merge until there's nothing left to merge.
        // there's probably a clever way to do this in a single pass, but this is already very fast.
        public static List<Range> merge(List<Range> ranges) {
            record MergeResult(boolean merged, Range range) {
            }

            var mergedRanges = ranges.stream().collect(foldLeft(() -> new ArrayList<Range>(), (acc, range) -> {
                var results = acc.stream().map(rangeItem -> {
                    var combined = rangeItem.combine(range);
                    return new MergeResult(combined.isPresent(), combined.orElse(rangeItem));
                }).toList();
                // this can probably be accomplished with a foldLeft, to avoid collecting the stream
                // into a List, but this is easier to read and understand
                var anyMerged = results.stream().anyMatch(result -> result.merged);
                var newDistinctRanges = new ArrayList<>(results.stream().map(MergeResult::range).toList());
                if (!anyMerged) {
                    newDistinctRanges.add(range);
                }
                return newDistinctRanges;
            }));

            if (ranges.size() != mergedRanges.size()) {
                return merge(mergedRanges);
            }
            return mergedRanges;
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
