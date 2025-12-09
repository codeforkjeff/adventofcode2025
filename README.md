
# Advent of Code 2025

See https://adventofcode.com/2025

## How to run this

### Get the input files

The input files in `src/main/resources` are encrypted. If you're not me, you'll need
to download the relevant input files from the AoC website and replace the encrypted ones
in that directory.

### Run the code

You can run the Part* classes within IntelliJ interactively.

To run using the command line, install maven, then run `mvn package appassembler:assemble`
to build the application.

Run the main program, supplying the day and part you want to run as arguments:

```sh
# runs day 2, part 1 
target/appassembler/bin/aoc2025 2 1
# runs day 3, both parts
target/appassembler/bin/aoc2025 3
# run everything
target/appassembler/bin/aoc2025 all
```

## Journal

### 12/3/2025

Off to a late start, but caught up to day 3 pretty quickly.

I'm liking [the changes to AoC this year](https://adventofcode.com/2025/about#faq_num_days).
12 days is so much better, in my opinion. When I did AoC for the first time in 2018, I ended up
spending a good portion of the holidays obsessing over unfinished puzzles. It actually discouraged
me from doing it in many subsequent years. 12 days gives you plenty of time to wrap things up before
the holidays if you fall behind... assuming the puzzles aren't crazy hard. We'll see!

I wanted to try using Haskell this year but didn't do enough prep ahead of time, and I didn't feel
like learning it on-the-go this year. Maybe next year.

### 12/5/2025

Used pattern matching for the day 5 solution, which was fun, though it wasn't a particularly interesting
use of it.

There was some excitement when Python added `match` in 3.10 back in 2021, but it's really just
a convenience. It's much cooler in Java >= 21: the compiler checks for exhaustiveness when switching
on a sealed interface or class. Yet another way functional programming has permeated Java, through
better support for algebraic data types.

Last year I was pretty obsessed with writing things in as strict an FP way as I could in pure Java. That
was a good learning exercise. I'm less dogmatic about it this year, especially in cases where imperative
code feels more readable to me. Which is subjective, of course, but certain habits die hard.

### 12/7/2025

Of course, the first difficult problem of AoC *would* fall on the first Saturday. :P

I wrote the solution for part 1 sloppily, had the right intuition about how to do part 2
but wrestled for a bit to get it exactly right, then ended up rewriting everything to consolidate
solving both parts using the same code. It took a little while, but I'm happy with how
compact the result is.

### 12/8/2025

Day 8 was also a tough one. Some minor spoilers below.

I incorrectly thought it would be too expensive to pre-calculate all the possible distances between
every pair of junction boxes, so I wasted time doing some weird shenanigans that I later threw out.
When I got to the point where I was fairly sure my algorithm was correct, I still wasn't getting
the right answer. Then I figured out I was misinterpreting what "nothing happens" in the puzzle prompt
meant.

Tangential thought: been considering the pros and cons of using static methods as functions vs passing in
input into a constructor, and storing the return values as members of the object. The latter makes it a little
easier to extend as compared to encapsulating the return values in a record or object, because you have to change
that signature and any relevant code each time you extend. The downside is you risk some null safety.

Obviously, as problems get more complicated, writing the solutions is a much more iterative
process, with more simplifying/optimizing both along the way, and also afterwards, when certain things
become clear. That's just to say, the code I committed for day 8 is pretty different from the code I ran
when I first generated the right answers.
