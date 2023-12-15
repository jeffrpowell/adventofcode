package com.jeffrpowell.adventofcode.aoc2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day15 extends Solution2023<List<String>>{

    private static final Pattern INSERT_REGEX = Pattern.compile("^(\\w+)=(\\d+)$");
    private static final Pattern REMOVE_REGEX = Pattern.compile("^(\\w+)-$");

    @Override
    public int getDay() {
        return 15;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getCSVParser();
    }

    @Override
    protected String part1(List<List<String>> input) {
        return Long.toString(input.stream()
            .flatMap(line -> line.stream().map(this::hash))
            .map(Long::valueOf)
            .reduce(0L, Math::addExact));
    }

    @Override
    protected String part2(List<List<String>> input) {
        InputParser<Rule> ruleParser = InputParserFactory.getRuleParser(",", Map.of(
            "I", INSERT_REGEX,
            "R", REMOVE_REGEX
        ));
        List<Rule> instructions = ruleParser.parseInput(input.get(0));
        Map<Integer, List<String>> boxes = Stream.iterate(0, i -> i + 1)
            .limit(256)
            .collect(Collectors.toMap(
                Function.identity(),
                i -> new ArrayList<>()
            ));
        Map<FocalKey, Integer> focalLookup = new HashMap<>();
        for (Rule rule : instructions) {
            String lens = rule.getString(0);
            int hash = hash(lens);
            if (rule.getRulePatternKey().equals("R")) {
                boxes.get(hash).remove(lens);
                focalLookup.remove(new FocalKey(hash, lens));
            }
            else {
                int focalLength = rule.getInt(1);
                if (!boxes.get(hash).contains(lens)) {
                    boxes.get(hash).add(lens);
                }
                focalLookup.put(new FocalKey(hash, lens), focalLength);
            }
        }
        long focalPower = 0;
        for (int box = 0; box < 256; box++) {
            List<String> slots = boxes.get(box);
            for (int slot = 0; slot < slots.size(); slot++) {
                focalPower += (box + 1) * (slot + 1) * focalLookup.get(new FocalKey(box, slots.get(slot)));
            }
        };
        return Long.toString(focalPower);
    }

    private int hash(String s) {
        int currentValue = 0;
        for (int ci : s.toCharArray()) {
            currentValue += ci;
            currentValue *= 17;
            currentValue %= 256;
        }
        return currentValue;
    }

    record FocalKey(int box, String lens) {}
}
