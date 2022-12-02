package com.jeffrpowell.adventofcode.aoc2022;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 extends Solution2022<Section>{

    @Override
    public int getDay() {
        return 1;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, true));
    }

    @Override
    protected String part1(List<Section> input) {
        return Integer.toString(input.stream()
            .map(s -> s.getInput(InputParserFactory.getIntegerParser()))
            .map(l -> l.stream().reduce(0, Math::addExact))
            .max(Comparator.naturalOrder()).get());
    }

    @Override
    protected String part2(List<Section> input) {
        return Integer.toString(input.stream()
            .map(s -> s.getInput(InputParserFactory.getIntegerParser()))
            .map(l -> l.stream().reduce(0, Math::addExact))
            .sorted(Comparator.reverseOrder())
            .limit(3)
            .collect(Collectors.reducing(0, Math::addExact)));
    }

}
