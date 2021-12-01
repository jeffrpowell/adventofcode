package com.jeffrpowell.adventofcode.aoc2021;

import com.jeffrpowell.adventofcode.SlidingWindowSpliterator;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 extends Solution2021<Integer>{

    @Override
    public int getDay() {
        return 1;
    }

    @Override
    public InputParser<Integer> getInputParser() {
        return InputParserFactory.getIntegerParser();
    }

    @Override
    protected String part1(List<Integer> input) {
        int last = Integer.MAX_VALUE;
        int count = 0;
        for (Integer i : input) {
            if (i > last) {
                count++;
            }
            last = i;
        }
        return Integer.toString(count);
    }

    @Override
    protected String part2(List<Integer> input) {
        List<Integer> sums = SlidingWindowSpliterator.windowed(input, 3)
            .map(window -> window.reduce(0, Math::addExact))
            .collect(Collectors.toList());
        return part1(sums);
    }

}
