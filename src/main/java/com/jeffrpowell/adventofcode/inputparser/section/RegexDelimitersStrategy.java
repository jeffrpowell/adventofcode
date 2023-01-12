package com.jeffrpowell.adventofcode.inputparser.section;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexDelimitersStrategy implements SectionSplitStrategy{
    private final List<Pattern> patterns;
    private final boolean includeMatchingLines;
    private final boolean matchAsHeader;
    
    public RegexDelimitersStrategy(List<Pattern> patterns, boolean includeMatchingLines, boolean matchAsHeader) {
        this.patterns = patterns;
        this.includeMatchingLines = includeMatchingLines;
        this.matchAsHeader = matchAsHeader;
    }

    @Override
    public List<List<String>> splitSectionInputs(List<String> allInput) {
        List<List<String>> sections = new ArrayList<>();
        List<String> section = new ArrayList<>();
        Iterator<Pattern> iPattern = patterns.iterator();
        Pattern pattern = iPattern.next();
        if (matchAsHeader && includeMatchingLines) {
            if (!iPattern.hasNext()) {
                sections.add(allInput);
                return sections;
            }
            section.add(allInput.get(0));
            pattern = iPattern.next();
        }
        for (int i = matchAsHeader ? 1 : 0; i < allInput.size(); i++) {
            String line = allInput.get(i);
            Matcher m = pattern.matcher(line);
            m.find();
            if (m.matches()) {
                if (!section.isEmpty()) {
                    sections.add(section);
                    section = new ArrayList<>();
                }
                if (includeMatchingLines) {
                    section.add(line);
                }
                if (iPattern.hasNext()) {
                    pattern = iPattern.next();
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
