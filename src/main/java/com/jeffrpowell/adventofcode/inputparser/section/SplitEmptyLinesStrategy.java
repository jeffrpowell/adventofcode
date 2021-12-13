package com.jeffrpowell.adventofcode.inputparser.section;

import java.util.ArrayList;
import java.util.List;

public class SplitEmptyLinesStrategy implements SectionSplitStrategy{
    private final int count;
    private final boolean repeat;
    
    public SplitEmptyLinesStrategy(int count, boolean repeat) {
        this.count = count;
        this.repeat = repeat;
    }

    @Override
    public List<List<String>> splitSectionInputs(List<String> allInput) {
        List<List<String>> sections = new ArrayList<>();
        List<String> section = new ArrayList<>();
        int _count = count;
        for (int i = 0; i < allInput.size(); i++) {
            String line = allInput.get(i);
            if (line.isEmpty()) {
                _count--;
                if (_count == 0 && repeat) {
                    sections.add(section);
                    section = new ArrayList<>();
                    _count = Math.min(allInput.size() - i, count);
                }
                if (_count == 0 && !repeat && i < allInput.size() - 1) {
                    sections.add(section);
                    section = new ArrayList<>();
                    _count = allInput.size() - i - 1;
                }
                else if (_count == 0) {
                    sections.add(section);
                }
            }
            else {
                section.add(line);
            }
        }
        if (!section.isEmpty()) {
            sections.add(section);
        }
        return sections;
    }
    
}
