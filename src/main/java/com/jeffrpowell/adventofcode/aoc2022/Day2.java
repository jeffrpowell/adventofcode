package com.jeffrpowell.adventofcode.aoc2022;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day2 extends Solution2022<String>{

    private static final Map<String, Integer> SCORES1;
    private static final Map<String, Integer> SCORES2;

    static {
        SCORES1 = new HashMap<>();
        SCORES1.put("A X", 3 + 1);
        SCORES1.put("A Y", 6 + 2);
        SCORES1.put("A Z", 0 + 3);
        SCORES1.put("B X", 0 + 1);
        SCORES1.put("B Y", 3 + 2);
        SCORES1.put("B Z", 6 + 3);
        SCORES1.put("C X", 6 + 1);
        SCORES1.put("C Y", 0 + 2);
        SCORES1.put("C Z", 3 + 3);
        SCORES2 = new HashMap<>();
        SCORES2.put("A X", 0 + 3);
        SCORES2.put("A Y", 3 + 1);
        SCORES2.put("A Z", 6 + 2);
        SCORES2.put("B X", 0 + 1);
        SCORES2.put("B Y", 3 + 2);
        SCORES2.put("B Z", 6 + 3);
        SCORES2.put("C X", 0 + 2);
        SCORES2.put("C Y", 3 + 3);
        SCORES2.put("C Z", 6 + 1);
    }
    @Override
    public int getDay() {
        return 2;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }

    @Override
    protected String part1(List<String> input) {
        return Integer.toString(input.stream()
            .map(SCORES1::get)
            .collect(Collectors.reducing(0, Math::addExact))
        );
    }

    @Override
    protected String part2(List<String> input) {
        return Integer.toString(input.stream()
            .map(SCORES2::get)
            .collect(Collectors.reducing(0, Math::addExact))
        );
    }

}
