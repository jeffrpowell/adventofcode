package com.jeffrpowell.adventofcode.aoc2020;

import com.google.common.collect.Lists;
import com.jeffrpowell.adventofcode.algorithms.SlidingWindowSpliterator;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day9 extends Solution2020<Long> {

    @Override
    public int getDay() {
        return 9;
    }

    @Override
    public InputParser<Long> getInputParser() {
        return InputParserFactory.getLongParser();
    }

    @Override
    protected String part1(List<Long> input) {
        input = Lists.reverse(input);
        return Long.toString(SlidingWindowSpliterator.windowed(input, 26)
            .limit(input.size() - 1)
            .map(windowStream -> windowStream.collect(Collectors.toList()))
            .filter(Predicate.not(Day9::isSumOfPairWithinPrevious25))
            .map(window -> window.get(0))
            .reduce((a, b) -> b).get());
    }

    private static boolean isSumOfPairWithinPrevious25(List<Long> window) {
        long target = window.get(0);
        Set<Long> range = new HashSet<>();
        for (int i = 1; i < window.size(); i++) {
            range.add(window.get(i));
        }
        return window.stream().anyMatch(i -> range.contains(target - i));
    }
    
    @Override
    protected String part2(List<Long> input) {
        long part1 = 217430975;
        int first = 0;
        int last = 1;
        long sum = input.get(0) + input.get(1);
        while (sum != part1) {
            if (sum > part1) {
                sum -= input.get(first);
                first++;
            }
            else{
                last++;
                sum += input.get(last);
            }
        }
        Long min = Long.MAX_VALUE;
        Long max = Long.MIN_VALUE;
        for (int i = first; i <= last; i++) {
            Long l = input.get(i);
            if (l < min) {
                min = l;
            }
            if (l > max) {
                max = l;
            }
        }
        return Long.toString(min + max);
    }
}
