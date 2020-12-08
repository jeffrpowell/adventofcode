package com.jeffrpowell.adventofcode.aoc2015;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.rule.RuleListUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Day7 extends Solution2015<Rule>{

    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        /*
            123 -> x
            x AND y -> z
            p LSHIFT 2 -> q
            NOT e -> f
        */
        return InputParserFactory.getRuleParser("\n", Map.of(
            "input", Pattern.compile("(\\d+) -> (\\w+)"),
            "comparison", Pattern.compile("(\\w+) (AND|OR) (\\w+) -> (\\w+)"),
            "shift", Pattern.compile("(\\w+) (LSHIFT|RSHIFT) (\\d+) -> (\\w+)"),
            "not", Pattern.compile("NOT (\\w+) -> (\\w+)")
        ));
    }

    @Override
    protected String part1(List<Rule> input) {
        Map<String, List<Rule>> groupedRules = RuleListUtil.groupByRulePatternKey(input);
        Map<String, BiFunction<Future<Integer>, Future<Integer>, Signal>> m = new HashMap<>();
        for (Rule rule : groupedRules.get("comparisons")) {
            m.put(rule.getString(3), FnFactory.and(rule.getString(3)));
        }
        return "";
    }

    @Override
    protected String part2(List<Rule> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private static class Signal {
        String wireName;
        int signal;

        public Signal(String wireName, int signal) {
            this.wireName = wireName;
            this.signal = signal;
        }
    }

    private static class FnFactory {
        private FnFactory(){}
        
        public static BiFunction<Future<Integer>, Future<Integer>, Signal> and(String target) {
            return (a, b) -> {
                try {
                    return new Signal(target, a.get() & b.get());
                } catch (InterruptedException | ExecutionException ex) { }
                return null;
            };
        }
        
        public static BiFunction<Future<Integer>, Future<Integer>, Signal> or(String target) {
            return (a, b) -> {
                try {
                    return new Signal(target, a.get() | b.get());
                } catch (InterruptedException | ExecutionException ex) { }
                return null;
            };
        }
        
        public static BiFunction<Future<Integer>, Future<Integer>, Signal> lshift(String target) {
            return (a, b) -> {
                try {
                    return new Signal(target, a.get() << b.get());
                } catch (InterruptedException | ExecutionException ex) { }
                return null;
            };
        }
        
        public static BiFunction<Future<Integer>, Future<Integer>, Signal> rshift(String target) {
            return (a, b) -> {
                try {
                    return new Signal(target, a.get() >> b.get());
                } catch (InterruptedException | ExecutionException ex) { }
                return null;
            };
        }
        
        public static Function<Future<Integer>, Signal> not(String target) {
            return a -> {
                try {
                    return new Signal(target, ~(a.get()));
                } catch (InterruptedException | ExecutionException ex) { }
                return null;
            };
        }
        
        public static Function<Integer, Signal> input(String target) {
            return a -> new Signal(target, a);
        }
    }
}
