package com.jeffrpowell.adventofcode.inputparser.section;

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
}
