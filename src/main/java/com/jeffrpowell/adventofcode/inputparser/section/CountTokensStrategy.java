package com.jeffrpowell.adventofcode.inputparser.section;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CountTokensStrategy implements SectionSplitStrategy {

    private final String token;
    private final int count;
    private final boolean repeat;

    public CountTokensStrategy(String token, int count, boolean repeat) {
        this.token = token;
        this.count = count;
        this.repeat = repeat;
    }

    @Override
    public List<List<String>> splitSectionInputs(List<String> allInput) {
        List<List<String>> sections = new ArrayList<>();
        int lastSectionCutoff = 0;
        int runningCount = count;
        for (int i = 0; i < allInput.size(); i++) {
            String line = allInput.get(i);
            int lastIndex = 0;
            while(lastIndex > -1 && runningCount != 0){
                lastIndex = line.indexOf(token, lastIndex);
            }
            if (runningCount == 0) {
                sections.add(allInput.stream().skip(lastSectionCutoff).limit(i - lastSectionCutoff + 1).collect(Collectors.toList()));
                if (repeat) {
                    runningCount = count;
                    lastSectionCutoff = i;
                }
                else {
                    sections.add(allInput.stream().skip(i + 1).collect(Collectors.toList()));
                    return sections;
                }
            }
        }
        return sections;
    }
    
}
