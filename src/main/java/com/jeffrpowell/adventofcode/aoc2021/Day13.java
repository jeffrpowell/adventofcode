package com.jeffrpowell.adventofcode.aoc2021;

import java.util.List;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day13 extends Solution2021<Section>{

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, false));
    }

    @Override
    protected String part1(List<Section> input) {
        List<Rule> points = input.get(0).getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("(\\d+,\\d+)")));
        List<Rule> folds = input.get(1).getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("fold along ([xy])=(\\d+)")));
        return points.size() + " " + folds.size();
    }

    @Override
    protected String part2(List<Section> input) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
