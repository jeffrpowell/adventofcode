package com.jeffrpowell.adventofcode.aoc2015;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day19 extends Solution2015<Section>{

    @Override
    public int getDay() {
        return 19;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, false));
    }

    record ReplacementRule(String before, String after) implements Function<String, Set<String>>{
        @Override
        public Set<String> apply(String s) {
            Set<String> results = new HashSet<>();
            int index = s.indexOf(before);
            while (index != -1) {
                String replaced = s.substring(0, index) + after + s.substring(index + before.length());
                results.add(replaced);
                index = s.indexOf(before, index + 1);
            }
            return results;
        }
    }

    @Override
    protected String part1(List<Section> input) {
        Set<ReplacementRule> replacementRules = input.get(0)
            .getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("(.+) => (.+)")))
            .stream()
            .map(r -> new ReplacementRule(r.getString(0), r.getString(1)))
            .collect(Collectors.toSet());
        
        String molecule = input.get(1).getInput(InputParserFactory.getStringParser()).get(0);
        return Integer.toString(replacementRules.stream()
            .map(rule -> rule.apply(molecule))
            .flatMap(Set::stream)
            .collect(Collectors.toSet())
            .size());
    }


    @Override
    protected String part2(List<Section> input) {
        Set<ReplacementRule> replacementRules = input.get(0)
            .getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("(.+) => (.+)")))
            .stream()
            .map(r -> new ReplacementRule(r.getString(1), r.getString(0))) //backwards replacement rules
            .collect(Collectors.toSet());
        
        String molecule = input.get(1).getInput(InputParserFactory.getStringParser()).get(0);
        PriorityQueue<SearchState> q = new PriorityQueue<>(Comparator.comparing(SearchState::heuristic));
        q.add(new SearchState(molecule, 0));
        while (!q.isEmpty()) {
            SearchState s = q.poll();
            if (s.molecule().equals("e")) {
                return Integer.toString(s.steps());
            }
            replacementRules.stream()
                .map(rule -> rule.apply(s.molecule()))
                .flatMap(Set::stream)
                .map(replacement -> new SearchState(replacement, s.steps() + 1))
                .forEach(q::add);
        }
        return "No solution found";
    }

    record SearchState(String molecule, int steps){
        long heuristic() {
            return molecule.length() * steps;
        }
    }
}
