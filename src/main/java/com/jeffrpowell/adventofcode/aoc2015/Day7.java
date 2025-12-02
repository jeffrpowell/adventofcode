package com.jeffrpowell.adventofcode.aoc2015;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.rule.RuleListUtil;

public class Day7 extends Solution2015<Rule>{

    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        /*
            123 -> x
            lx -> a
            1 AND x -> y
            x AND y -> z
            p LSHIFT 2 -> q
            NOT e -> f
        */
        return InputParserFactory.getRuleParser("\n", Map.of(
            "input", Pattern.compile("^(\\w+) -> (\\w+)"),
            "comparison", Pattern.compile("(\\w+) (AND|OR) (\\w+) -> (\\w+)"),
            "shift", Pattern.compile("(\\w+) (LSHIFT|RSHIFT) (\\d+) -> (\\w+)"),
            "not", Pattern.compile("NOT (\\w+) -> (\\w+)")
        ));
    }

    @Override
    protected String part1(List<Rule> input) {
        Map<String, List<Rule>> groupedRules = RuleListUtil.groupByRulePatternKey(input);
        Map<String, Signal> signalMap = new HashMap<>();
        for (Rule rule : groupedRules.get("comparison")) {
            if (rule.getString(1).equals("AND")) {
                signalMap.put(rule.getString(3), Gate.and(rule.getString(0), rule.getString(2)));
                if (rule.getString(0).matches("\\d+")) {
                    signalMap.put(rule.getString(0), new StaticSignal(Integer.parseInt(rule.getString(0))));
                }
            } else {
                signalMap.put(rule.getString(3), Gate.or(rule.getString(0), rule.getString(2)));
            }
        }
        for (Rule rule : groupedRules.get("shift")) {
            if (rule.getString(1).equals("LSHIFT")) {
                signalMap.put(rule.getString(3), Gate.lshift(rule.getString(0), rule.getInt(2)));
            } else {
                signalMap.put(rule.getString(3), Gate.rshift(rule.getString(0), rule.getInt(2)));
            }
        }
        for (Rule rule : groupedRules.get("not")) {
            signalMap.put(rule.getString(1), Gate.not(rule.getString(0)));
        }
        for (Rule rule : groupedRules.get("input")) {
            Signal source;
            if (rule.getString(0).matches("\\d+")) {
                source = new StaticSignal(Integer.parseInt(rule.getString(0)));
            }
            else {
                source = new WireSignal(rule.getString(0));
            }
            signalMap.put(rule.getString(1), source);
        }
        return Integer.toString(signalMap.get("a").getSignal(signalMap));
    }

    @Override
    protected String part2(List<Rule> input) {
        String part1Result = part1(input);
        Map<String, List<Rule>> groupedRules = RuleListUtil.groupByRulePatternKey(input);
        Map<String, Signal> signalMap = new HashMap<>();
        for (Rule rule : groupedRules.get("comparison")) {
            if (rule.getString(1).equals("AND")) {
                signalMap.put(rule.getString(3), Gate.and(rule.getString(0), rule.getString(2)));
                if (rule.getString(0).matches("\\d+")) {
                    signalMap.put(rule.getString(0), new StaticSignal(Integer.parseInt(rule.getString(0))));
                }
            } else {
                signalMap.put(rule.getString(3), Gate.or(rule.getString(0), rule.getString(2)));
            }
        }
        for (Rule rule : groupedRules.get("shift")) {
            if (rule.getString(1).equals("LSHIFT")) {
                signalMap.put(rule.getString(3), Gate.lshift(rule.getString(0), rule.getInt(2)));
            } else {
                signalMap.put(rule.getString(3), Gate.rshift(rule.getString(0), rule.getInt(2)));
            }
        }
        for (Rule rule : groupedRules.get("not")) {
            signalMap.put(rule.getString(1), Gate.not(rule.getString(0)));
        }
        for (Rule rule : groupedRules.get("input")) {
            Signal source;
            if (rule.getString(0).matches("\\d+")) {
                source = new StaticSignal(Integer.parseInt(rule.getString(0)));
            }
            else {
                source = new WireSignal(rule.getString(0));
            }
            signalMap.put(rule.getString(1), source);
        }
        signalMap.put("b", new StaticSignal(Integer.parseInt(part1Result)));
        return Integer.toString(signalMap.get("a").getSignal(signalMap));
    }

    private static abstract class Signal {
        private boolean signalCached = false;
        private int cachedSignal = -1;
        abstract int loadSignal(Map<String, Signal> signalMap);
        public int getSignal(Map<String, Signal> signalMap) {
            if (!signalCached) {
                int signal = loadSignal(signalMap) & 0xFFFF;
                signalCached = true;
                cachedSignal = signal;
                return signal;
            } else {
                return cachedSignal;
            }
        }
    }

    private static class StaticSignal extends Signal {
        private final int signal;
        
        public StaticSignal(int signal) {
            this.signal = signal;
        }

        @Override
        public int loadSignal(Map<String, Signal> signalMap) {
            return signal;
        }
    }

    private static class WireSignal extends Signal {
        private final String wireName;
        
        public WireSignal(String wireName) {
            this.wireName = wireName;
        }

        @Override
        public int loadSignal(Map<String, Signal> signalMap) {
            return signalMap.get(wireName).getSignal(signalMap);
        }
    }
    
    private static class Gate extends Signal{
        private final List<String> sources;
        private final BiFunction<Map<String, Signal>, List<Signal>, Integer> gateFn;
        
        public static Gate and(String source1, String source2) {
            return new Gate(List.of(source1, source2), (signalMap, signals) -> (signals.get(0).getSignal(signalMap) & signals.get(1).getSignal(signalMap)) & 0xFFFF);
        }
        
        public static Gate or(String source1, String source2) {
            return new Gate(List.of(source1, source2), (signalMap, signals) -> (signals.get(0).getSignal(signalMap) | signals.get(1).getSignal(signalMap)) & 0xFFFF);
        }
        
        public static Gate lshift(String source, int shift) {
            return new Gate(List.of(source), (signalMap, signals) -> (signals.get(0).getSignal(signalMap) << shift) & 0xFFFF);
        }
        
        public static Gate rshift(String source, int shift) {
            return new Gate(List.of(source), (signalMap, signals) -> (signals.get(0).getSignal(signalMap) >> shift) & 0xFFFF);
        }
        
        public static Gate not(String source) {
            return new Gate(List.of(source), (signalMap, signals) -> (~signals.get(0).getSignal(signalMap)) & 0xFFFF);
        }
        
        private Gate(List<String> sources, BiFunction<Map<String, Signal>, List<Signal>, Integer> gateFn) {
            this.sources = sources;
            this.gateFn = gateFn;
        }

        @Override
        public int loadSignal(Map<String, Signal> signalMap) {
            return gateFn.apply(signalMap, sources.stream().map(signalMap::get).toList());
        }
    }
}
