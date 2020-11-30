package com.jeffrpowell.adventofcode.inputparser.rule;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RuleListUtil {
    private RuleListUtil(){}
    
    public static Map<String, List<Rule>> groupByRulePatternKey(List<Rule> rules) {
        return rules.stream().collect(Collectors.groupingBy(Rule::getRulePatternKey));
    }
}
