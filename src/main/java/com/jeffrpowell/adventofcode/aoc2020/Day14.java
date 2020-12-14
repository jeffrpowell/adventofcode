package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
