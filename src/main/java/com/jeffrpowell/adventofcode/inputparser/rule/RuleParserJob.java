package com.jeffrpowell.adventofcode.inputparser.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RuleParserJob implements Callable<Rule>{
    
    private final Integer sortKey;
    private final String input;
    private final Map<String, Pattern> rulePatterns;

    public RuleParserJob(Integer sortKey, String input, Map<String, Pattern> rulePatterns) {
        this.sortKey = sortKey;
        this.input = input;
        this.rulePatterns = rulePatterns;
    }

    @Override
    public Rule call() throws Exception {
        Map<String, Matcher> matchers = rulePatterns.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().matcher(input)
            ));
        
        Map.Entry<String, Matcher> m = matchers.entrySet().stream()
            .filter(entry -> entry.getValue().matches())
            .findAny().get();
            
        List<String> tokens = new ArrayList<>();
        for (int i = 1; i < m.getValue().groupCount() + 1; i++) {
            tokens.add(m.getValue().group(i));
        }
        return new Rule(tokens, m.getKey(), sortKey);
    }

}
