package com.jeffrpowell.adventofcode.aoc2023;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;


public class Day8 extends Solution2023<Section>{
    @Override
    public int getDay() {
        return 8;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, false));
    }

    @Override
    protected String part1(List<Section> input) {
        List<String> directionsStrs = input.get(0).getInput(InputParserFactory.getTokenSVParser("")).get(0);
        List<Boolean> directions_goLeft = directionsStrs.stream().map(s -> s.equals("L")).collect(Collectors.toList());
        List<Rule> graphStrs = input.get(1).getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("^(...) = \\((...), (...)\\)")));
        Map<String, String> leftGraph = graphStrs.stream()
            .collect(Collectors.toMap(
                r -> r.getString(0),
                r -> r.getString(1)
            ));
        Map<String, String> rightGraph = graphStrs.stream()
            .collect(Collectors.toMap(
                r -> r.getString(0),
                r -> r.getString(1)
            ));
        boolean foundZZZ = false;
        long steps = 0;
        int i = 0;
        String currentNode = "AAA";
        while(!foundZZZ) {
            Boolean goLeft = directions_goLeft.get(i);
            currentNode = goLeft ? leftGraph.get(currentNode) : rightGraph.get(currentNode);
            if (currentNode.equals("ZZZ")) {
                foundZZZ = true;
            }
            steps++;
            i = (i + 1) % directions_goLeft.size();
        }
        return Long.toString(steps);
    }

    @Override
    protected String part2(List<Section> input) {
        return part1(input);
    }

}
