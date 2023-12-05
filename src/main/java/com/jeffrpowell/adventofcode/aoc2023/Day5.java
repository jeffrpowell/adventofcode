package com.jeffrpowell.adventofcode.aoc2023;

import java.util.ArrayList;
import java.util.List;
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
        List<List<SplittableMappingFunction>> rules = input.stream()
            .skip(1)
            .map(section -> section.getInput(
                InputParserFactory.getPreParser(str -> str.replaceFirst(".+-to-.+ map:", "-1 -1 -1"),
                InputParserFactory.getLongTokenSVParser("\\s"))
            ))
            .map(listListLong -> listListLong.stream().map(SplittableMappingFunction::new).collect(Collectors.toList()))
            .collect(Collectors.toList());
        List<SplittableMappingFunction> masterMappingFunctionList = rules.get(0);
        for (int i = 1; i < rules.size(); i++) {
            List<SplittableMappingFunction> nextMappingFunctionList = rules.get(i);

        }
    }

    @Override
    protected String part2(List<Section> input) {
        return part1(input);
    }

    private static class SplittableMappingFunction {
        List<Function<Long, Long>> fnChain;
        Long inputLow;
        Long inputHigh;
        Long outputLow;
        Long outputHigh;

        public SplittableMappingFunction(List<Long> mappingRule) {
            long destStart = mappingRule.get(0);
            long srcStart = mappingRule.get(1);
            long range = mappingRule.get(2);
            this.inputLow = srcStart;
            this.inputHigh = srcStart + range - 1;
            this.outputLow = destStart;
            this.inputHigh = destStart + range - 1;
            this.fnChain = new ArrayList<>();
            this.fnChain.add(input -> input - inputLow + outputLow);
        }

        public boolean hasInputsInMyOutputRange(SplittableMappingFunction fn) {
            return fn.inputHigh >= outputLow && fn.inputLow <= outputHigh;
        }

        public List<SplittableMappingFunction> addFunctionToChain(SplittableMappingFunction fn) {
            
        }
    }
}
