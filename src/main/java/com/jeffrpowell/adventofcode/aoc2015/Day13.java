package com.jeffrpowell.adventofcode.aoc2015;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.Permutations;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day13 extends Solution2015<Rule>{

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("(\\w+) would (gain|lose) (\\d+) happiness units by sitting next to (\\w+)."));
    }
    
    @Override
    protected String part1(List<Rule> input) {
        Map<String, Map<String, Integer>> distanceMap = new HashMap<>();
        for (Rule rule : input) {
            String from = rule.getString(0);
            String to = rule.getString(3);
            int distance = rule.getInt(2);
            boolean negative = rule.getString(1).equals("gain"); //reversing sign to turn into a minimization problem
            if (negative) {
                distance = -distance;
            }
            distanceMap.computeIfAbsent(from, k -> new HashMap<>()).put(to, distance);
        }
        return simulate(distanceMap);
    }

    @Override
    protected String part2(List<Rule> input) {
        Map<String, Map<String, Integer>> distanceMap = new HashMap<>();
        for (Rule rule : input) {
            String from = rule.getString(0);
            String to = rule.getString(3);
            int distance = rule.getInt(2);
            boolean negative = rule.getString(1).equals("gain"); //reversing sign to turn into a minimization problem
            if (negative) {
                distance = -distance;
            }
            distanceMap.computeIfAbsent(from, k -> new HashMap<>()).put(to, distance);
        }
        distanceMap.put("me", new HashMap<>());
        for (String person : distanceMap.keySet()) {
            distanceMap.get("me").put(person, 0);
            distanceMap.get(person).put("me", 0);
        }
        return simulate(distanceMap);
    }

    private String simulate(Map<String, Map<String, Integer>> distanceMap) {
        List<String> people = distanceMap.keySet().stream().toList();
        //Generate all permutations of locations and calculate distances
        List<List<String>> permutations = Permutations.getAllPermutations(people);
        int minDistance = Integer.MAX_VALUE;
        for (List<String> path : permutations) {
            int totalDistance = 0;
            //Traverse in one direction
            for (int i = 0; i < path.size() - 1; i++) {
                String from = path.get(i);
                String to = path.get(i + 1);
                totalDistance += distanceMap.get(from).get(to);
            }
            totalDistance += distanceMap.get(path.get(path.size() - 1)).get(path.get(0));
            //Traverse in the opposite direction
            for (int i = path.size() - 1; i > 0; i--) {
                String from = path.get(i);
                String to = path.get(i - 1);
                totalDistance += distanceMap.get(from).get(to);
            }
            totalDistance += distanceMap.get(path.get(0)).get(path.get(path.size() - 1));
            if (totalDistance < minDistance) {
                minDistance = totalDistance;
            }
        }    
        return Integer.toString(-minDistance);
    }
}
