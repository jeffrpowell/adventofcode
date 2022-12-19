package com.jeffrpowell.adventofcode.aoc2022;

import java.util.List;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day19 extends Solution2022<Rule>{

    @Override
    public int getDay() {
        return 19;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("Blueprint 23: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 17 clay. Each geode robot costs 4 ore and 20 obsidian."));
    }

    @Override
    protected String part1(List<Rule> input) {
        return null;
    }

    @Override
    protected String part2(List<Rule> input) {
        return null;
    }

}
