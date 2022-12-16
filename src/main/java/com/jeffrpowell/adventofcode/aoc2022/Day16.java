package com.jeffrpowell.adventofcode.aoc2022;

import java.util.List;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.section.Section;

public class Day16 extends Solution2022<Rule>{

    @Override
    public int getDay() {
        return 16;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("Valve (\\w\\w) has flow rate=(\\d+); tunnels? leads? to valves? (.+)"));
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
