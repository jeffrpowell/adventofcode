package com.jeffrpowell.adventofcode.inputparser.section;

import java.util.List;

import com.jeffrpowell.adventofcode.inputparser.InputParser;

public class Section {
    private final List<String> input;
    
    public Section(List<String> input) {
        this.input = input;
    }

    public <T> List<T> getInput(InputParser<T> parser) {
        return parser.parseInput(input);
    }
}
