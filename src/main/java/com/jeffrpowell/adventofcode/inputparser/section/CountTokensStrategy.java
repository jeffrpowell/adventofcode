package com.jeffrpowell.adventofcode.inputparser.section;

import java.util.List;

public class CountTokensStrategy implements SectionSplitStrategy {

    private final String token;
    private final int count;

    public CountTokensStrategy(String token, int count) {
        this.token = token;
        this.count = count;
    }

    @Override
    public List<List<String>> splitSectionInputs(List<String> allInput) {
        for (String line : allInput) {
            
        }
    }
    
}
