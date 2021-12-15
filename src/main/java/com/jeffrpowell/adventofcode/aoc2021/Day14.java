package com.jeffrpowell.adventofcode.aoc2021;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.CharArrayUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day14 extends Solution2021<Section>{
    List<String> template = Collections.emptyList();
    Map<String, Long> pairCount = new HashMap<>();
    Map<Character, Map<Character, String>> LUT = new HashMap<>();

    @Override
    public int getDay() {
        return 14;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, false));
    }

    @Override
    protected String part1(List<Section> input) {
        template = input.get(0).getInput(InputParserFactory.getTokenSVParser("")).get(0);
        List<Rule> rules = input.get(1).getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("(\\w)(\\w) -> (\\w)")));
        for (Rule rule : rules) {
            LUT.putIfAbsent(rule.getChar(0), new HashMap<>());
            LUT.get(rule.getChar(0)).put(rule.getChar(1), rule.getString(2));
        }
        for (int i = 0; i < 10; i++) {
            template = step();
        }
        Map<String, Long> grouping = template.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.reducing(0L, item -> 1L, (accum, next) -> accum + 1L)));
        long max = grouping.values().stream().max(Comparator.comparing(Function.identity())).get();
        long min = grouping.values().stream().min(Comparator.comparing(Function.identity())).get();
        return Long.toString(max - min);
    }

    private List<String> step() {
        List<String> newTemplate = new ArrayList<>();
        String lastLetter = template.get(0);
        newTemplate.add(lastLetter);
        for (int i = 1; i < template.size(); i++) {
            String letter = template.get(i);
            newTemplate.add(LUT.get(lastLetter).get(letter));
            newTemplate.add(letter);
            lastLetter = letter;
        }
        return newTemplate;
    }

    @Override
    protected String part2(List<Section> input) {
        template = input.get(0).getInput(InputParserFactory.getTokenSVParser("")).get(0);
        List<Rule> rules = input.get(1).getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("(\\w)(\\w) -> (\\w)")));
        for (Rule rule : rules) {
            LUT.putIfAbsent(rule.getChar(0), new HashMap<>());
            LUT.get(rule.getChar(0)).put(rule.getChar(1), rule.getString(2));
            pairCount.put(CharArrayUtils.concat("", rule.getChar(0), rule.getChar(1)), 0L);
        }
        for (int i = 0; i < template.size() - 1; i++) {
            pairCount.compute(template.get(i)+template.get(i+1), (k, v) -> v+1);
        }
        for (int i = 0; i < 40; i++) {
            step2();
        }
        
        return Long.toString(pairCount.entrySet().stream()
            .filter(entry -> entry.getKey().contains("B"))
            .map(entry -> entry.getValue() * (entry.getKey().equals("BB") ? 2 : 1))
            .reduce(0L, Math::addExact) / 2L);
    }
    
    private void step2() {
        Map<String, Long> nextPairCount = pairCount.keySet().stream().collect(Collectors.toMap(Function.identity(), pair -> 0L));
        pairCount.entrySet().stream()
            .filter(entry -> entry.getValue() > 0)
            .forEach(entry -> {
                String pair = entry.getKey();
                char left = pair.charAt(0);
                char right = pair.charAt(1);
                String middle = LUT.get(left).get(right);
                String leftPair = left + middle;
                String rightPair = middle + right;
                nextPairCount.compute(leftPair, (k, v) -> v + entry.getValue());
                nextPairCount.compute(rightPair, (k, v) -> v + entry.getValue());
            });
        pairCount = nextPairCount;
    }
}
