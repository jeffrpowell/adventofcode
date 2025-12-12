package com.jeffrpowell.adventofcode.aoc2025;

import java.util.List;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day12 extends Solution2025<Section>{
    @Override
    public int getDay() {
        return 12;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, true));
    }

    @Override
    protected String part1(List<Section> input) {
        List<List<String>> presentStrings = input.stream()
            .limit(6)
            .map(s -> s.getInput(InputParserFactory.getStringParser()))
            .collect(Collectors.toList());
        List<Integer> presentRequiredArea = presentStrings.stream()
            .map(present -> present.stream()
                .mapToInt(line -> (int) line.chars().filter(c -> c == '#').count())
                .sum()
            )
            .collect(Collectors.toList());
        List<Rule> constraints = input.getLast().getInput(
            InputParserFactory.getRuleParser("\n", "(\\d+)x(\\d+): (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+)")
        );
        long validRegions = constraints.stream()
            .filter(rule -> {
                long regionArea = (long) rule.getInt(0) * (long) rule.getInt(1);
                long requiredArea = 0;
                for (int i = 0; i < 6; i++) {
                    requiredArea += (long) presentRequiredArea.get(i) * (long) rule.getInt(i + 2);
                }
                return regionArea >= requiredArea;
            })
            .count();
        return String.valueOf(validRegions);
    }

    @Override
    protected String part2(List<Section> input) {
        return null;
    }
}
