package com.jeffrpowell.adventofcode.aoc2015;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.rule.RuleListUtil;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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
        Map<String, List<String>> upGraph = new HashMap<>();
        Map<String, List<String>> downGraph = new HashMap<>();
        Map<String, BiFunction<Future<Integer>, Future<Integer>, Signal>> m = new HashMap<>();
        Deque<Signal> q = new ArrayDeque<>();
        for (Rule rule : groupedRules.get("comparisons")) {
            upGraph.put(rule.getString(3), List.of(rule.getString(0), rule.getString(2)));
            downGraph.putIfAbsent(rule.getString(0), new ArrayList<>());
            downGraph.putIfAbsent(rule.getString(2), new ArrayList<>());
            downGraph.get(rule.getString(0)).add(rule.getString(3));
            downGraph.get(rule.getString(2)).add(rule.getString(3));
        }
        for (Rule rule : groupedRules.get("shift")) {
            upGraph.put(rule.getString(3), List.of(rule.getString(0)));
            downGraph.putIfAbsent(rule.getString(0), new ArrayList<>());
            downGraph.get(rule.getString(0)).add(rule.getString(3));
        }
        for (Rule rule : groupedRules.get("not")) {
            upGraph.put(rule.getString(1), List.of(rule.getString(0)));
            downGraph.putIfAbsent(rule.getString(0), new ArrayList<>());
            downGraph.get(rule.getString(0)).add(rule.getString(1));
        }
        for (Rule rule : groupedRules.get("input")) {
            upGraph.put(rule.getString(1), List.of());
            q.add(new Signal(rule.getString(1), rule.getInt(0)));
        }
        while (!q.isEmpty()) {
            Signal s = q.poll();
            List<String> descendants = downGraph.get(s.wireName);
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
    
    private static class Gate {
        private final List<Integer> inputs;
        private final Function<List<Integer>, Signal> gateFn;
        
        public static Gate and(String target) {
            return new Gate(inputs -> new Signal(target, inputs.get(0) & inputs.get(1)));
        }
        
        public static Gate or(String target) {
            return new Gate(inputs -> new Signal(target, inputs.get(0) | inputs.get(1)));
        }
        
        public static Gate lshift(String target) {
            return new Gate(inputs -> new Signal(target, inputs.get(0) << inputs.get(1)));
        }
        
        public static Gate rshift(String target) {
            return new Gate(inputs -> new Signal(target, inputs.get(0) >> inputs.get(1)));
        }
        
        public static Gate not(String target) {
            return new Gate(inputs -> new Signal(target, ~inputs.get(0)));
        }
        
        private Gate(Function<List<Integer>, Signal> gateFn) {
            this.inputs = new ArrayList<>();   
            this.gateFn = gateFn;
        }
    
        public void acceptInput(int input) {
            inputs.add(input);
        }
    }
}
