package com.jeffrpowell.adventofcode.aoc2022;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day4 extends Solution2022<Rule>{

    @Override
    public int getDay() {
        return 4;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n",Pattern.compile("(\\d+)-(\\d+),(\\d+)-(\\d+)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        return Long.toString(input.stream()
            .filter(this::duplicateAssignments)
            .count());
    }

    private boolean duplicateAssignments(Rule r) {
        int min1 = r.getInt(0);
        int max1 = r.getInt(1);
        int min2 = r.getInt(2);
        int max2 = r.getInt(3);
        return (min1 <= min2 && max2 <= max1 
            || min2 <= min1 && max1 <= max2);
    }

    @Override
    protected String part2(List<Rule> input) {
        return Long.toString(input.stream()
            .filter(this::overlappingAssignments)
            .count());
    }
    private boolean overlappingAssignments(Rule r) {
        int min1 = r.getInt(0);
        int max1 = r.getInt(1);
        int min2 = r.getInt(2);
        int max2 = r.getInt(3);
        return (min1 <= min2 && min2 <= max1
            || min2 <= min1 && min1 <= max2
            || min1 <= max2 && max2 <= max1
            || min2 <= max1 && max1 <= max2);
    }
}
