package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.rule.RuleListUtil;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7 extends Solution2020<Rule>{
    private static final Pattern BAG_CONTENTS_REGEX = Pattern.compile("(\\d+) (\\w+ \\w+) bags?(?:, )?");
    private Map<String, BagRule> rules = new HashMap<>();
    
    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        
        //vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
        //faded blue bags contain no other bags.
        return InputParserFactory.getRuleParser("\n", 
            Map.of(
                "regular", Pattern.compile("(\\w+ \\w+) bags contain ([^n].+)"),
                "empty", Pattern.compile("(\\w+ \\w+) bags contain no other bags\\.")
            ));
    }

    @Override
    protected String part1(List<Rule> input) {
        Map<String, List<Rule>> groupedRules = RuleListUtil.groupByRulePatternKey(input);
        Map<String, Set<String>> ancestors = new HashMap<>();
        for (Rule rule : groupedRules.get("regular")) {
            BagRule bagRule = new BagRule(rule.getString(0));
            Matcher m = BAG_CONTENTS_REGEX.matcher(rule.getString(1));
            while (m.find()) {
                bagRule.addContents(m.group(2), Integer.parseInt(m.group(1)));
                ancestors.putIfAbsent(m.group(2), new HashSet<>());
                ancestors.get(m.group(2)).add(rule.getString(0));
            }
        }
        Deque<String> goingUp = new ArrayDeque<>(ancestors.get("shiny gold"));
        Set<String> visited = new HashSet<>();
        int count = 0;
        while(!goingUp.isEmpty()) {
            String ancestor = goingUp.pollFirst();
            if (!visited.contains(ancestor)) {
                count++;
                visited.add(ancestor);
                if (ancestors.containsKey(ancestor)) {
                    goingUp.addAll(ancestors.get(ancestor));
                }
            }
        }
        return Integer.toString(count);
    }

    @Override
    protected String part2(List<Rule> input) {
        Map<String, List<Rule>> groupedRules = RuleListUtil.groupByRulePatternKey(input);
        
        for (Rule rule : groupedRules.get("empty")){
            rules.put(rule.getString(0), new BagRule(rule.getString(0)));
        }
        for (Rule rule : groupedRules.get("regular")) {
            BagRule bagRule = new BagRule(rule.getString(0));
            Matcher m = BAG_CONTENTS_REGEX.matcher(rule.getString(1));
            while (m.find()) {
                bagRule.addContents(m.group(2), Integer.parseInt(m.group(1)));
            }
            rules.put(rule.getString(0), bagRule);
        }
        return Integer.toString(recursiveDive(rules.get("shiny gold")) - 1);
    }
    
    private int recursiveDive(BagRule rule) {
        int count = 1;
        for (BagRuleContent descendant : rule.contents) {
            count += descendant.num * recursiveDive(rules.get(descendant.name));
        }
        return count;
    }

    private static class BagRuleContent {
        private final String name;
        private final int num;

        public BagRuleContent(String name, int num) {
            this.name = name;
            this.num = num;
        }
        
        @Override
        public String toString() {
            return num + " " + name;
        }
    }
    
    private static class BagRule {
        private final String name;
        private final List<BagRuleContent> contents;

        public BagRule(String name) {
            this.name = name;
            this.contents = new ArrayList<>();
        }
        
        public void addContents(String name, int num) {
            contents.add(new BagRuleContent(name, num));
        }
        
        @Override
        public String toString() {
            return name + " => " + contents;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 89 * hash + Objects.hashCode(this.name);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final BagRule other = (BagRule) obj;
            return Objects.equals(this.name, other.name);
        }
    }
    
}
