package com.jeffrpowell.adventofcode.aoc2022;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day16 extends Solution2022<Rule>{

    @Override
    public int getDay() {
        return 16;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("Valve (\\w\\w) has flow rate=(\\d+); tunnels? leads? to valves? (.+)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        Map<String, Valve> valveIndex = input.stream()
            .collect(Collectors.toMap(
                r -> r.getString(0),
                r -> new Valve(r.getString(0), r.getInt(1), r.getString(2))
            ));
        
        valveIndex.values().stream().forEach(v -> v.hookUpNeighbors(valveIndex));
        valveIndex.values().stream().forEach(Valve::squashNeighbors);
        return null;
    }

    @Override
    protected String part2(List<Rule> input) {
        return null;
    }

    private static class Valve {
        String name;
        Map<Valve, Integer> neighbors;
        int pressure;
        String neighborStr;
        public Valve(String name, int pressure, String neighborStr) {
            this.name = name;
            this.pressure = pressure;
            this.neighborStr = neighborStr;
        }
        
        public void hookUpNeighbors(Map<String, Valve> valveIndex) {
            neighbors = Arrays.stream(neighborStr.split(", "))
                .map(valveIndex::get)
                .collect(Collectors.toMap(
                    Function.identity(),
                    v -> 1
                ));
        }

        public void squashNeighbors() {
            Set<Valve> cyclePrevention = new HashSet<>();
            while(neighbors.keySet().stream().anyMatch(v -> v.getPressure() == 0))
            {
                Map<Valve, Integer> newNeighbors = neighbors.entrySet().stream()
                    .filter(e -> e.getKey().getPressure() == 0)
                    .map(e -> e.getKey().getNeighbors().entrySet())
                    .flatMap(Set::stream)
                    .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue() + 1,
                        Math::min
                    ));
                Set<Valve> toRemove = neighbors.keySet().stream().filter(v -> v.getPressure() == 0).collect(Collectors.toSet());
                toRemove.stream().forEach(v -> {neighbors.remove(v); cyclePrevention.add(v);});
                for (Map.Entry<Valve, Integer> entry : newNeighbors.entrySet()) {
                    if (cyclePrevention.contains(entry.getKey())) {
                        continue;
                    }
                    if (neighbors.containsKey(entry.getKey())) {
                        neighbors.put(entry.getKey(), Math.min(entry.getValue(), neighbors.get(entry.getKey())));
                    }
                    else {
                        neighbors.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        public int getTotalPressure(int minutesLeft) {
            return pressure * minutesLeft;
        }

        public int getPressure() {
            return pressure;
        }

        public Map<Valve, Integer> getNeighbors() {
            return neighbors;
        }

        @Override
        public String toString() {
            return name + "(" + pressure + ") -> " + neighbors.entrySet().stream().map(e -> e.getKey().name + "@" + e.getValue() + "(" + e.getKey().pressure + ")").collect(Collectors.joining(","));
        }
    }
}
