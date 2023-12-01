package com.jeffrpowell.adventofcode.aoc2023;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        String runningLine = line;
        while(true) {
            final String runningLine_ro = runningLine;
            Map<Integer, String> numberMap = Map.of(
                1, "one",
                2, "two",
                3, "three",
                4, "four",
                5, "five",
                6, "six",
                7, "seven",
                8, "eight",
                9, "nine"
            );
            Map<Integer, String> replaceMap = Map.of(
                1, "1e", //oneight
                2, "2o", //twone
                3, "3e", //threeight
                4, "4r",
                5, "5e", //fiveight
                6, "6x",
                7, "7n", //sevenine
                8, "8t", //eightwo, eighthree
                9, "9e" //nineight
            );
            Map<Integer, Integer> findMap = numberMap.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey, 
                    e -> runningLine_ro.indexOf(e.getValue())));
            Map<Integer, List<Integer>> groupMap = findMap.entrySet().stream()
                .filter(e -> e.getValue() != -1)
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
            if (groupMap.isEmpty()){
                System.out.println(line + " => " + runningLine_ro);
                return runningLine_ro;
            }
            Integer firstFind = groupMap.get(groupMap.keySet().stream().min(Comparator.naturalOrder()).get()).get(0);
            runningLine = runningLine_ro.replaceFirst(numberMap.get(firstFind), replaceMap.get(firstFind));
        }
    }

    @Override
    protected String part1(List<Rule> input) {
        Map<String, List<Rule>> rules = RuleListUtil.groupByRulePatternKey(input);
        // debugPart1(rules);
        long multi = rules.get("multi").stream()
            .map(r -> r.getLong(0) + "" + r.getLong(1))
            .map(Long::parseLong)
            .reduce(0L, Math::addExact);
        long one = 0L;
        if (rules.containsKey("one")) {
            one = rules.get("one").stream()
                .map(r -> r.getLong(0) + "" + r.getLong(0))
                .map(Long::parseLong)
                .reduce(0L, Math::addExact);
        }
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
