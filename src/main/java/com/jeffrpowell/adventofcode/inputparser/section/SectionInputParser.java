package com.jeffrpowell.adventofcode.inputparser.section;

import java.util.ArrayList;
import java.util.List;

import com.jeffrpowell.adventofcode.inputparser.InputParser;

public class SectionInputParser implements InputParser<Section>{

    private final SectionSplitStrategy strategy;

    public SectionInputParser(SectionSplitStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public List<Section> parseInput(List<String> input) {
        List<List<String>> splitInputs = strategy.splitSectionInputs(input);
        List<Section> sections = new ArrayList<>();
        for (int i = 0; i < splitInputs.size(); i++) {
            Section section = new Section(splitInputs.get(i));
            sections.add(section);
        }
        return sections;
    }
    
}
