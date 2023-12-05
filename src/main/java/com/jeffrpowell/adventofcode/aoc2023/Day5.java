package com.jeffrpowell.adventofcode.aoc2023;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;


public class Day5 extends Solution2023<Section>{
    @Override
    public int getDay() {
        return 5;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, true));
    }

    @Override
    protected String part1(List<Section> input) {
        List<Long> seeds = input.get(0).getInput(InputParserFactory.getPreParser(str -> str.replace("seeds: ", ""), InputParserFactory.getLongTokenSVParser("\\s"))).get(0);
        List<List<List<Long>>> rules = input.stream()
            .skip(1)
            .map(section -> section.getInput(
                InputParserFactory.getPreParser(str -> str.replaceFirst(".+-to-.+ map:", "-1 -1 -1"),
                InputParserFactory.getLongTokenSVParser("\\s"))
            ))
            .collect(Collectors.toList());
        rules.stream().forEach(list -> list.remove(0)); //header row
        Map<Integer, List<SankeyNode>> nodes = new HashMap<>();
        for (int tier = 0; tier < rules.size(); tier++) {
            nodes.computeIfAbsent(tier, i -> new ArrayList<>());
            for (List<Long> mappingRule : rules.get(tier)) {
                long range = mappingRule.get(2);
                long inputLow = mappingRule.get(1);
                long inputHigh = inputLow + range - 1;
                long outputLow = mappingRule.get(0);
                nodes.get(tier).add(new SankeyNode(inputLow, inputHigh, outputLow));
            }
        }
        return Long.toString(
            seeds.stream()
                .map(s -> traverseDiagram(s, nodes))
                .min(Comparator.naturalOrder())
                .get()
        );
    }

    /*
        Part 1
        --------------------------
        806029445
        --------------------------
        Completed part 1 in 27 ms

        Part 2
        --------------------------
        Running seed 3416930225 for range 56865175
        Updated minLocation to 2733200236
        Updated minLocation to 2509416930
        Running seed 4245248379 for range 7142355
        Updated minLocation to 1339903174
        Running seed 1808166864 for range 294882110
        Updated minLocation to 1020468114
        Updated minLocation to 77781696
        Running seed 863761171 for range 233338109
        Running seed 4114335326 for range 67911591
        Running seed 1198254212 for range 504239157
        Updated minLocation to 59370572
        Running seed 3491380151 for range 178996923
        Running seed 3965970270 for range 15230597
        Running seed 2461206486 for range 133606394
        Running seed 2313929258 for range 84595688
        59370572
        --------------------------
        Completed part 2 in 1130695 ms
     */
    @Override
    protected String part2(List<Section> input) {
        List<List<List<Long>>> rules = input.stream()
            .skip(1)
            .map(section -> section.getInput(
                InputParserFactory.getPreParser(str -> str.replaceFirst(".+-to-.+ map:", "-1 -1 -1"),
                InputParserFactory.getLongTokenSVParser("\\s"))
            ))
            .collect(Collectors.toList());
        rules.stream().forEach(list -> list.remove(0)); //header row
        Map<Integer, List<SankeyNode>> nodes = new HashMap<>();
        for (int tier = 0; tier < rules.size(); tier++) {
            nodes.computeIfAbsent(tier, i -> new ArrayList<>());
            for (List<Long> mappingRule : rules.get(tier)) {
                long range = mappingRule.get(2);
                long inputLow = mappingRule.get(1);
                long inputHigh = inputLow + range - 1;
                long outputLow = mappingRule.get(0);
                nodes.get(tier).add(new SankeyNode(inputLow, inputHigh, outputLow));
            }
        }
        List<Long> seedTokens = input.get(0).getInput(InputParserFactory.getPreParser(str -> str.replace("seeds: ", ""), InputParserFactory.getLongTokenSVParser("\\s"))).get(0);
        Long minLocation = Long.MAX_VALUE;
        for (int i = 1; i < seedTokens.size(); i = i + 2) {
            Long seedStart = seedTokens.get(i-1);
            Long seedRange = seedTokens.get(i);
            System.out.println("Running seed " + seedStart + " for range " + seedRange);
            for (long seed = seedStart; seed < seedStart + seedRange - 1; seed++) {
                Long location = traverseDiagram(seed, nodes);
                if (location < minLocation) {
                    minLocation = location;
                    System.out.println("Updated minLocation to " + minLocation);
                }
            }
        }
        return minLocation.toString();
    }

    private Long traverseDiagram(Long seed, Map<Integer, List<SankeyNode>> nodes) {
        long runningValue = seed;
        for (int tier = 0; tier < nodes.size(); tier++) {
            final long runningValue_ro = runningValue;
            runningValue = nodes.get(tier).stream()
                .filter(node -> node.inputLow <= runningValue_ro && runningValue_ro <= node.inputHigh)
                .map(node -> node.fn.apply(runningValue_ro))
                .findFirst().orElse(runningValue_ro);
        }
        return runningValue;
    }

    class SankeyNode {
        private final long inputLow;
        private final long inputHigh;
        final Function<Long, Long> fn;

        public SankeyNode(long inputLow, long inputHigh, long outputLow) {
            this.inputLow = inputLow;
            this.inputHigh = inputHigh;
            this.fn = input -> input - inputLow + outputLow;
        }
    }
}
