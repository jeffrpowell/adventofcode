package com.jeffrpowell.adventofcode.aoc2015;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.algorithms.Permutations;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day9 extends Solution2015<Rule>{
    @Override
    public int getDay() {
        return 9;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("(\\w+) to (\\w+) = (\\d+)"));
    }
    
    @Override
    protected String part1(List<Rule> input) {
        Map<String, Map<String, Integer>> distanceMap = new HashMap<>();
        for (Rule rule : input) {
            String from = rule.getString(0);
            String to = rule.getString(1);
            int distance = rule.getInt(2);
            distanceMap.computeIfAbsent(from, k -> new HashMap<>()).put(to, distance);
            distanceMap.computeIfAbsent(to, k -> new HashMap<>()).put(from, distance);
        }
        List<String> locations = distanceMap.keySet().stream().toList();
        //Generate all permutations of locations and calculate distances
        List<List<String>> permutations = Permutations.getAllPermutations(locations);
        int minDistance = Integer.MAX_VALUE;
        for (List<String> path : permutations) {
            int totalDistance = 0;
            for (int i = 0; i < path.size() - 1; i++) {
                String from = path.get(i);
                String to = path.get(i + 1);
                totalDistance += distanceMap.get(from).get(to);
            }
            if (totalDistance < minDistance) {
                minDistance = totalDistance;
            }
        }    
        return Integer.toString(minDistance);
    }

    @Override
    protected String part2(List<Rule> input) {
        Map<String, Map<String, Integer>> distanceMap = new HashMap<>();
        for (Rule rule : input) {
            String from = rule.getString(0);
            String to = rule.getString(1);
            int distance = rule.getInt(2);
            distanceMap.computeIfAbsent(from, k -> new HashMap<>()).put(to, distance);
            distanceMap.computeIfAbsent(to, k -> new HashMap<>()).put(from, distance);
        }
        List<String> locations = distanceMap.keySet().stream().toList();
        //Generate all permutations of locations and calculate distances
        List<List<String>> permutations = Permutations.getAllPermutations(locations);
        int maxDistance = Integer.MIN_VALUE;
        for (List<String> path : permutations) {
            int totalDistance = 0;
            for (int i = 0; i < path.size() - 1; i++) {
                String from = path.get(i);
                String to = path.get(i + 1);
                totalDistance += distanceMap.get(from).get(to);
            }
            if (totalDistance > maxDistance) {
                maxDistance = totalDistance;
            }
        }    
        return Integer.toString(maxDistance);
    }
}
