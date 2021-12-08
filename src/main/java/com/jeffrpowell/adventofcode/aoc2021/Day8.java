package com.jeffrpowell.adventofcode.aoc2021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day8 extends Solution2021<Rule>{

    @Override
    public int getDay() {
        return 8;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("(\\w+) (\\w+) (\\w+) (\\w+) (\\w+) (\\w+) (\\w+) (\\w+) (\\w+) (\\w+) \\| (\\w+) (\\w+) (\\w+) (\\w+)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        return Long.toString(input.stream()
            .map(rule -> List.of(rule.getString(10), rule.getString(11), rule.getString(12), rule.getString(13)))
            .flatMap(List::stream)
            .filter(this::isUnique)
            .count());
    }

    private boolean isUnique(String wires) {
        return wires.length() == 2
        || wires.length() == 3
        || wires.length() == 4
        || wires.length() == 7;
    }

    @Override
    protected String part2(List<Rule> input) {
        return Integer.toString(input.stream().map(this::solve).reduce(0, Math::addExact));
    }
    
    /*
        7 -> 8
        6 -> 0, 6, 9
        5 -> 2, 3, 5
        4 -> 4
        3 -> 7
        2 -> 1
    */
    //identify all 1, 4, 7, 8
    //find common three letters for 5-counters
    //if there is a 5-counter that shares the two letters from 1, it's a 3
    //check the remaining 5-counters with the four: 3 matches => 5, 2 matches => 2
    //if there is a 6-counter that shares one letter from 1, it's a 6
    //check remaining 6-counters with the four: 3 matches => 0, 4 matches => 9
    private int solve(Rule rule) {
        Map<Integer, Digit> knownDigits = new HashMap<>();
        List<Digit> inputs = IntStream.range(0, 10)
            .mapToObj(rule::getString)
            .map(str -> new Digit(str.chars().mapToObj(i -> (char)i).collect(Collectors.toSet())))
            .collect(Collectors.toList());
        List<Digit> outputs = IntStream.range(10, 14)
            .mapToObj(rule::getString)
            .map(str -> new Digit(str.chars().mapToObj(i -> (char)i).collect(Collectors.toSet())))
            .collect(Collectors.toList());
        
        for (Digit digit : inputs) {
            switch (digit.getCharCount()) {
                case 2: 
                    knownDigits.put(1, digit);
                    digit.knownValue = "1";
                    continue;
                    case 3: 
                    knownDigits.put(7, digit);
                    digit.knownValue = "7";
                    continue;
                    case 4: 
                    knownDigits.put(4, digit);
                    digit.knownValue = "4";
                    continue;
                    case 7: 
                    knownDigits.put(8, digit);
                    digit.knownValue = "8";
                    continue;
                default:
            }
        }
        for (Digit digit : outputs) {
            switch (digit.getCharCount()) {
                case 2: 
                    knownDigits.put(1, digit);
                    digit.knownValue = "1";
                    continue;
                    case 3: 
                    knownDigits.put(7, digit);
                    digit.knownValue = "7";
                    continue;
                    case 4: 
                    knownDigits.put(4, digit);
                    digit.knownValue = "4";
                    continue;
                    case 7: 
                    knownDigits.put(8, digit);
                    digit.knownValue = "8";
                    continue;
                default:
            }
        }

        if (done(outputs)) {
            return getNumber(outputs);
        }

        Map<Integer, List<Digit>> digitsByLength = Stream.concat(inputs.stream(), outputs.stream()).collect(Collectors.groupingBy(Digit::getCharCount));
        List<Digit> nonThrees = new ArrayList<>();
        for (Digit five_digit : digitsByLength.get(5)) {
            if (five_digit.containsLetters(knownDigits.get(1).chars)) {
                knownDigits.put(3, five_digit);
                five_digit.knownValue = "3";
            }
            else {
                nonThrees.add(five_digit);
            }
        }
        
        if (done(outputs)) {
            return getNumber(outputs);
        }

        for (Digit fiver : nonThrees) {
            switch(knownDigits.get(4).getSharedCharacters(fiver)) {
                case 3:
                    knownDigits.put(5, fiver);
                    fiver.knownValue = "5";
                    break;
                case 2:
                default:
                    knownDigits.put(2, fiver);
                    fiver.knownValue = "2";
                    break;
            }
        }

        if (done(outputs)) {
            return getNumber(outputs);
        }

        List<Digit> nonSixes = new ArrayList<>();
        for (Digit six_digit : digitsByLength.get(6)) {
            if (six_digit.getSharedCharacters(knownDigits.get(1)) == 1) {
                knownDigits.put(6, six_digit);
                six_digit.knownValue = "6";
            }
            else {
                nonSixes.add(six_digit);
            }
        }
        
        if (done(outputs)) {
            return getNumber(outputs);
        }

        for (Digit sixer : nonSixes) {
            switch(knownDigits.get(4).getSharedCharacters(sixer)) {
                case 3:
                    knownDigits.put(0, sixer);
                    sixer.knownValue = "0";
                    break;
                case 4:
                default:
                    knownDigits.put(9, sixer);
                    sixer.knownValue = "9";
                    break;
            }
        }
        
        return getNumber(outputs);
    }

    private static boolean done(List<Digit> outputs) {
        return outputs.stream().allMatch(Digit::valueKnown);
    }

    private static int getNumber(List<Digit> outputs) {
        return Integer.parseInt(outputs.stream().map(Digit::getValue).collect(Collectors.joining()));
    }

    private static class Digit {
        Set<Character> chars;
        String knownValue;

        public Digit(Set<Character> chars) {
            this.chars = chars;
            this.knownValue = "";
        }
        
        public int getSharedCharacters(Digit d) {
            return Long.valueOf(d.chars.stream().filter(chars::contains).count()).intValue();
        }

        public boolean containsLetters(Set<Character> c) {
            return c.stream().allMatch(chars::contains);
        }

        public int getCharCount() {
            return chars.size();
        }

        public boolean valueKnown() {
            return !knownValue.isEmpty();
        }
        
        public String getValue() {
            return knownValue;
        }
    }
    
}
