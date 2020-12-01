package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day1 extends Solution2020<Integer>{

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
        Set<Integer> set = new HashSet<>(input);
        return Integer.toString(input.stream()
            .filter(i -> set.contains(2020 - i))
            .map(i -> i * (2020 - i))
            .findAny().get());
    }

    @Override
    protected String part2(List<Integer> input) {
        Set<Integer> set = new HashSet<>(input);
        for (int first = 0; first < input.size() - 1; first++) {
            Integer one = input.get(first);
            for (int second = first + 1; second < input.size(); second++) {
                Integer two = input.get(second);
                if (set.contains(2020 - one - two)) {
                    return Integer.toString(one * two * (2020 - one - two));
                }
            }
        }
        return "Missed it";
    }

}
