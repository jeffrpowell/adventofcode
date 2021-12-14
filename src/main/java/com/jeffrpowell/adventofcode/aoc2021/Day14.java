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

import com.jeffrpowell.adventofcode.CharArrayUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day14 extends Solution2021<Section>{
    List<Character> template = Collections.emptyList();
    Map<Character, Map<Character, Character>> LUT = new HashMap<>();

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
        template = CharArrayUtils.toList(input.get(0).getInput(InputParserFactory.getStringParser()).get(0).toCharArray());
        List<Rule> rules = input.get(1).getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("(\\w)(\\w) -> (\\w)")));
        for (Rule rule : rules) {
            LUT.putIfAbsent(rule.getChar(0), new HashMap<>());
            LUT.get(rule.getChar(0)).put(rule.getChar(1), rule.getChar(2));
        }
        for (int i = 0; i < 10; i++) {
            template = step();
        }
        Map<Character, Long> grouping = template.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.reducing(0L, item -> 1L, (accum, next) -> accum + 1L)));
        long max = grouping.values().stream().max(Comparator.comparing(Function.identity())).get();
        long min = grouping.values().stream().min(Comparator.comparing(Function.identity())).get();
        return Long.toString(max - min);
    }

    //Should consider an ordered map
    private List<Character> step() {
        List<Character> newTemplate = new ArrayList<>();
        Character lastLetter = template.get(0);
        newTemplate.add(lastLetter);
        for (int i = 1; i < template.size(); i++) {
            Character letter = template.get(i);
            newTemplate.add(LUT.get(lastLetter).get(letter));
            newTemplate.add(letter);
            lastLetter = letter;
        }
        return newTemplate;
    }

    @Override
    protected String part2(List<Section> input) {
        template = CharArrayUtils.toList(input.get(0).getInput(InputParserFactory.getStringParser()).get(0).toCharArray());
        List<Rule> rules = input.get(1).getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("(\\w)(\\w) -> (\\w)")));
        for (Rule rule : rules) {
            LUT.putIfAbsent(rule.getChar(0), new HashMap<>());
            LUT.get(rule.getChar(0)).put(rule.getChar(1), rule.getChar(2));
        }
        for (int i = 0; i < 40; i++) {
            template = step();
        }
        Map<Character, Long> grouping = template.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.reducing(0L, item -> 1L, (accum, next) -> accum + 1L)));
        long max = grouping.values().stream().max(Comparator.comparing(Function.identity())).get();
        long min = grouping.values().stream().min(Comparator.comparing(Function.identity())).get();
        return Long.toString(max - min);
    }
    
}
