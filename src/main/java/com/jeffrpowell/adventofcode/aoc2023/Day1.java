package com.jeffrpowell.adventofcode.aoc2023;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.rule.RuleListUtil;

public class Day1 extends Solution2023<Rule>{
    @Override
    public int getDay() {
        return 1;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Map.of(
                "multi", Pattern.compile("^[^0-9]*([0-9]).*([0-9])[^0-9]*$"),
                "one", Pattern.compile("^[^0-9]*([0-9])[^0-9]*$")
            ));
    }

    @Override
    public InputParser<Rule> getInputParserPart2() {
        return InputParserFactory.getPreParser(
            this::preprocess,
            InputParserFactory.getRuleParser("\n", Map.of(
                "multi", Pattern.compile("^[^0-9]*([0-9]).*([0-9])[^0-9]*$"),
                "one", Pattern.compile("^[^0-9]*([0-9])[^0-9]*$")
            ))
        );
    }

    private String preprocess(String line) {
        return line
            .replaceAll("one", "1")
            .replaceAll("two", "2")
            .replaceAll("three", "3")
            .replaceAll("four", "4")
            .replaceAll("five", "5")
            .replaceAll("six", "6")
            .replaceAll("seven", "7")
            .replaceAll("eight", "8")
            .replaceAll("nine", "9");
    }

    @Override
    protected String part1(List<Rule> input) {
        Map<String, List<Rule>> rules = RuleListUtil.groupByRulePatternKey(input);
        debugPart1(rules);
        long multi = rules.get("multi").stream()
            .map(r -> r.getLong(0) + "" + r.getLong(1))
            .map(Long::parseLong)
            .reduce(0L, Math::addExact);
        long one = rules.get("one").stream()
            .map(r -> r.getLong(0) + "" + r.getLong(0))
            .map(Long::parseLong)
            .reduce(0L, Math::addExact);
        return Long.toString(multi + one);
    }

    private void debugPart1(Map<String, List<Rule>> rules) {
        rules.values().stream().flatMap(List::stream).forEach(r -> {
            System.out.print(r.getLine());
            System.out.print(": " + r.getLong(0));
            if (r.getRulePatternKey().equals("multi")) {
                System.out.print(" " + r.getLong(1));
            }
            else {
                System.out.print(" " + r.getLong(0));
            }
            System.out.println();
        });
    }

    @Override
    protected String part2(List<Rule> input) {
        return part1(input);
    }

}
