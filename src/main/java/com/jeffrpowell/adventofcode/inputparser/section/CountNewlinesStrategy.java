package com.jeffrpowell.adventofcode.inputparser.section;

import java.util.ArrayList;
import java.util.List;

public class CountNewlinesStrategy implements SectionSplitStrategy{

    private final int count;
    private final boolean repeat;
    
    public CountNewlinesStrategy(int count, boolean repeat) {
        this.count = count;
        this.repeat = repeat;
    }

    @Override
    public List<List<String>> splitSectionInputs(List<String> allInput) {
        if (repeat) {
            List<List<String>> sections = new ArrayList<>();
            int lastIndex = 0;
            while(lastIndex < allInput.size()) {
                sections.add(allInput.subList(lastIndex, Math.min(lastIndex + count, allInput.size() - 1));
            }
            return sections;
        }
        else {
            return List.of(allInput.subList(0, count), allInput.subList(count, allInput.size()));
        }
    }
    
}
