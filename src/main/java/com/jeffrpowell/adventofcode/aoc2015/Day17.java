package com.jeffrpowell.adventofcode.aoc2015;

import java.util.List;

import com.jeffrpowell.adventofcode.algorithms.PowerSets;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day17 extends Solution2015<Integer>{
    @Override
    public int getDay() {
        return 17;
    }

    @Override
    public InputParser<Integer> getInputParser() {
        return InputParserFactory.getIntegerParser();
    }

    @Override
    protected String part1(List<Integer> input) {
        List<List<Integer>> combinations = PowerSets.getPowerSet(input);
        return Long.toString(combinations.stream()
            .filter(combination -> combination.stream().mapToInt(Integer::intValue).sum() == 150)
            .count());
    }

    @Override
    protected String part2(List<Integer> input) {
        List<List<Integer>> combinations = PowerSets.getPowerSet(input);
        long minContainerCount = combinations.stream()
            .filter(combination -> combination.stream().mapToInt(Integer::intValue).sum() == 150)
            .mapToInt(List::size)
            .min()
            .orElse(0);
        return Long.toString(combinations.stream()
            .filter(combination -> combination.stream().mapToInt(Integer::intValue).sum() == 150)
            .filter(combination -> combination.size() == minContainerCount)
            .count());
    }
}
