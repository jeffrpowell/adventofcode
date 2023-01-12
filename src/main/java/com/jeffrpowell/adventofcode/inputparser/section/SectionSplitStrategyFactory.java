package com.jeffrpowell.adventofcode.inputparser.section;

import java.util.List;
import java.util.regex.Pattern;

public class SectionSplitStrategyFactory {
    private SectionSplitStrategyFactory(){}

    public static SectionSplitStrategy countNewlines(int count, boolean repeat) {
        return new CountNewlinesStrategy(count, repeat);
    }
    
    public static SectionSplitStrategy countTokens(String token, int count, boolean repeat) {
        return new CountTokensStrategy(token, count, repeat);
    }

    public static SectionSplitStrategy emptyLines(int count, boolean repeat) {
        return new SplitEmptyLinesStrategy(count, repeat);
    }

    public static SectionSplitStrategy regexDelimiters(List<Pattern> patterns, boolean includeMatchingLines, boolean matchAsHeader) {
        return new RegexDelimitersStrategy(patterns, includeMatchingLines, matchAsHeader);
    }
}
