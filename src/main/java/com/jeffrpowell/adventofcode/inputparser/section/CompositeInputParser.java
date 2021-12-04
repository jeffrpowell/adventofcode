package com.jeffrpowell.adventofcode.inputparser.section;

import java.util.ArrayList;
import java.util.List;

import com.jeffrpowell.adventofcode.inputparser.InputParser;

public class CompositeInputParser implements InputParser<Section>{

    private final SectionSplitStrategy strategy;
    private final InputParser<?>[] parsers;

    public CompositeInputParser(SectionSplitStrategy strategy, InputParser<?>[] parsers) {
        this.strategy = strategy;
        this.parsers = parsers;
    }

    @Override
    public List<Section> parseInput(List<String> input) {
        List<List<String>> splitInputs = strategy.splitSectionInputs(input);
        List<Section> sections = new ArrayList<>();
        for (int i = 0; i < splitInputs.size(); i++) {
            InputParser<?> parser = parsers[Math.min(i, parsers.length - 1)];
            Section section = new Section(splitInputs.get(i));
            sections.add(section);
        }
        return sections;
    }
    
}
