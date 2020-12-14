package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day14 extends Solution2020<Rule>{

    @Override
    public int getDay() {
        return 14;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Map.of(
            "mask", Pattern.compile("mask = (\\w+)"),
            "memory", Pattern.compile("mem\\[(\\d+)] = (\\d+)")
        ));
    }

    @Override
    protected String part1(List<Rule> input) {
        Map<Integer, Long> memory = new HashMap<>();
        DoubleMask mask = new DoubleMask("0");
        for (Rule rule : input) {
            if (rule.getRulePatternKey().equals("mask")) {
                mask = new DoubleMask(rule.getString(0));
            }
            else {
                memory.put(rule.getInt(0), mask.applyMask(rule.getLong(1)));
            }
        }
        return Long.toString(memory.values().stream().reduce(0L, Math::addExact));
    }
    
    private static class DoubleMask {
        private final long zeroMask;
        private final long oneMask;
        
        public DoubleMask(String maskString) {
            String zeroMaskString = maskString.replaceAll("X", "1");
            String oneMaskString = maskString.replaceAll("X", "0");
            zeroMask = Long.parseLong(zeroMaskString, 2);
            oneMask = Long.parseLong(oneMaskString, 2);
        }
        
        public long applyMask(long value) {
            return (value | oneMask) & zeroMask;
        }
        
        @Override
        public String toString() {
            return Long.toBinaryString(zeroMask) + " + " + Long.toBinaryString(oneMask);
        }
    }

    @Override
    protected String part2(List<Rule> input) {
        Map<Long, Long> memory = new HashMap<>();
        DoubleMaskPt2 mask = new DoubleMaskPt2("0");
        for (Rule rule : input) {
            if (rule.getRulePatternKey().equals("mask")) {
                mask = new DoubleMaskPt2(rule.getString(0));
            }
            else {
                mask.applyMask(rule.getLong(0)).stream().forEach(address -> memory.put(address, rule.getLong(1)));
            }
        }
        return Long.toString(memory.values().stream().reduce(0L, Math::addExact));
    }

    private static class DoubleMaskPt2 {
        private final long originalMask;
        private final List<DoubleMask> floatingMasks;
        
        public DoubleMaskPt2(String maskString) {
            String originalMaskString = maskString.replaceAll("X", "0");
            this.originalMask = Long.parseLong(originalMaskString, 2);
            this.floatingMasks = new ArrayList<>();
            String modifiedMask = maskString.replaceAll("X", "Y");
            fillInFloatingBits(modifiedMask.replaceAll("0|1", "X"));
        }
    
        private void fillInFloatingBits(String maskString) {
            if (maskString.contains("Y")) {
                fillInFloatingBits(maskString.replaceFirst("Y", "1"));
                fillInFloatingBits(maskString.replaceFirst("Y", "0"));
            }
            else {
                floatingMasks.add(new DoubleMask(maskString));
            }
        }
        
        public List<Long> applyMask(long value) {
            long newBaseAddress = value | originalMask;
            return floatingMasks.stream().map(mask -> mask.applyMask(newBaseAddress)).collect(Collectors.toList());
        }
    }
}
