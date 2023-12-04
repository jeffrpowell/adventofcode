package com.jeffrpowell.adventofcode.aoc2023;

import java.util.List;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;


public class Day4 extends Solution2023<Rule>{
    @Override
    public int getDay() {
        return 4;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("^Card (\\d+):\\s(.+)$"));
    }


    @Override
    protected String part1(List<Rule> input) {
        input.stream()
            .forEach(i -> {
                System.out.print(i.getString(1));
            });
        return "";
    }

    @Override
    protected String part2(List<Rule> input) {
        return part1(input);
    }
}
