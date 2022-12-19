package com.jeffrpowell.adventofcode.aoc2022;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Set<Valve> pressureValves = valveIndex.values().stream().filter(v -> v.getPressure() > 0).collect(Collectors.toSet());
        valveIndex.values().stream().forEach(v -> v.completeGraph(pressureValves));
        return getMaxPressure(valveIndex);
    }

    private String getMaxPressure(Map<String, Valve> valveIndex) {
        Deque<Traversal> q = new ArrayDeque<>();
        valveIndex.get("AA").getNeighbors().entrySet().stream()
            .forEach(e -> {
                q.push(new Traversal(e.getKey().getTotalPressure(30 - e.getValue() - 1), Collections.emptySet(), e.getKey().name, 30 - e.getValue() - 1));
            });
        int maxScore = 0;
        while (!q.isEmpty()) {
            Traversal t = q.pop();
            if (t.pressureScore() > maxScore) {
                maxScore = t.pressureScore();
            }
            for (var entry : valveIndex.get(t.head()).getNeighbors().entrySet()) {
                if (entry.getValue() >= t.timeLeft() || t.openedValves().contains(entry.getKey().name)) {
                    continue;
                }
                if (!t.openedValves().contains(t.head())) {
                    Set<String> newOpenedValves = Stream.of(Set.of(t.head), t.openedValves()).flatMap(Set::stream).collect(Collectors.toSet());
                    q.push(new Traversal(
                        t.pressureScore() + entry.getKey().getTotalPressure(t.timeLeft() - entry.getValue() - 1), 
                        newOpenedValves, 
                        entry.getKey().name, 
                        t.timeLeft() - entry.getValue() - 1)
                    ); //choose to open the valve
                }
            }
        }
        return Integer.toString(maxScore);
    }

    record Traversal(int pressureScore, Set<String> openedValves, String head, int timeLeft){};

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
            cyclePrevention.add(this);
            while(neighbors.keySet().stream().anyMatch(v -> v.getPressure() == 0))
            {
                Map<Valve, Integer> newNeighbors = new HashMap<>();
                neighbors.entrySet().stream()
                    .filter(e -> e.getKey().getPressure() == 0)
                    .forEach(e -> {
                        for (var v : e.getKey().getNeighbors().entrySet()){
                            if (newNeighbors.containsKey(e.getKey())) {
                                newNeighbors.put(v.getKey(), Math.min(e.getValue() + v.getValue(), newNeighbors.get(e.getKey())));
                            }
                            else {
                                newNeighbors.put(v.getKey(), e.getValue() + v.getValue());
                            }
                        }
                    });
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

        public void completeGraph(Set<Valve> pressureValves) {
            Set<Valve> targets = pressureValves.stream()
                .filter(v -> !neighbors.keySet().contains(v))
                .filter(v -> !v.name.equals(name))
                .collect(Collectors.toSet());
            targets.stream().forEach(t -> neighbors.put(t, distanceToTarget(t)));
        }

        record Hunt(Valve head, int distance, Set<String> visited){}

        private int distanceToTarget(Valve target) {
            PriorityQueue<Hunt> q = new PriorityQueue<>(Comparator.comparing(Hunt::distance));
            Set<String> rootVisited = neighbors.keySet().stream().map(v -> v.name).collect(Collectors.toSet());
            rootVisited.add(name);
            neighbors.entrySet().stream().forEach(e -> q.add(new Hunt(e.getKey(), e.getValue(), rootVisited)));
            while (!q.isEmpty()) {
                Hunt h = q.poll();
                if (h.head().equals(target)) {
                    return h.distance();
                }
                Set<String> visited = Stream.concat(Stream.of(h.head().name), h.visited().stream()).collect(Collectors.toSet());
                h.head().getNeighbors().entrySet().stream()
                    .filter(n -> !visited.contains(n.getKey().name))
                    .filter(n -> !neighbors.containsKey(n.getKey()))
                    .forEach(e -> q.add(new Hunt(e.getKey(), h.distance + e.getValue(), visited)));
            }
            return Integer.MAX_VALUE;
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
