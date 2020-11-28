package com.jeffrpowell.adventofcode.inputparser.rule;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RuleParser implements InputParser<Rule>{

    private final Pattern regex;
    private final String ruleDelimiter;

    public RuleParser(Pattern regex, String ruleDelimiter) {
        this.regex = regex;
        this.ruleDelimiter = ruleDelimiter;
    }
    
    @Override
    public List<Rule> parseInput(List<String> input) {
        return input.stream()
            .map(regex::matcher)
            .filter(Matcher::find)
            .map(m -> {
                List<String> tokens = new ArrayList<>();
                for (int i = 1; i < m.groupCount() + 1; i++) {
                    tokens.add(m.group(i));
                }
                return new Rule(tokens);
            })
            .collect(Collectors.toList());
        
    }

}
