package com.jeffrpowell.adventofcode.aoc2023;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;


public class Day8 extends Solution2023<Section>{
    @Override
    public int getDay() {
        return 8;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, false));
    }

    @Override
    protected String part1(List<Section> input) {
        List<String> directionsStrs = input.get(0).getInput(InputParserFactory.getTokenSVParser("")).get(0);
        List<Rule> graphStrs = input.get(1).getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("^(...) = \\((...), (...)\\)")));
        List<Boolean> directions_goLeft = directionsStrs.stream().map(s -> s.equals("L")).collect(Collectors.toList());
        Map<String, String> leftGraph = graphStrs.stream()
            .collect(Collectors.toMap(
                r -> r.getString(0),
                r -> r.getString(1)
            ));
        Map<String, String> rightGraph = graphStrs.stream()
            .collect(Collectors.toMap(
                r -> r.getString(0),
                r -> r.getString(2)
            ));
        boolean foundZZZ = false;
        long steps = 0;
        int i = 0;
        String currentNode = "AAA";
        while(!foundZZZ) {
            Boolean goLeft = directions_goLeft.get(i);
            currentNode = goLeft ? leftGraph.get(currentNode) : rightGraph.get(currentNode);
            if (currentNode.equals("ZZZ")) {
                foundZZZ = true;
            }
            steps++;
            i = (i + 1) % directions_goLeft.size();
        }
        return Long.toString(steps);
    }

    @Override
    protected String part2(List<Section> input) {
        List<String> directionsStrs = input.get(0).getInput(InputParserFactory.getTokenSVParser("")).get(0);
        List<Rule> graphStrs = input.get(1).getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("^(...) = \\((...), (...)\\)")));
        List<Boolean> directions_goLeft = directionsStrs.stream().map(s -> s.equals("L")).collect(Collectors.toList());
        Map<String, DesertNode> leftGraph = graphStrs.stream()
            .collect(Collectors.toMap(
                    r -> r.getString(0),
                    r -> new DesertNode(r.getString(1), r.getString(1).endsWith("Z"), r.getString(1).endsWith("A"))
                ));
                Map<String, DesertNode> rightGraph = graphStrs.stream()
                .collect(Collectors.toMap(
                    r -> r.getString(0),
                    r -> new DesertNode(r.getString(2), r.getString(2).endsWith("Z"), r.getString(2).endsWith("A"))
            ));
        boolean done = false;
        long steps = 0;
        int i = 0;
        Set<DesertNode> currentNodes = Stream.concat(leftGraph.keySet().stream(), rightGraph.keySet().stream())
            .map(name -> new DesertNode(name, name.endsWith("Z"), name.endsWith("A")))
            .filter(DesertNode::endsA)
            .collect(Collectors.toSet());
        Map<String, EndNodeObservations> endNodeObservationMap = Stream.concat(leftGraph.keySet().stream(), rightGraph.keySet().stream())
            .map(name -> new DesertNode(name, name.endsWith("Z"), name.endsWith("A")))
            .filter(DesertNode::endsZ)
            .map(node -> new EndNodeObservations(node.name, -1, -1))
            .collect(Collectors.toMap(
                eno -> eno.name,
                Function.identity(),
                (a, b) -> a
            ));
        while(!done) {
            Boolean goLeft = directions_goLeft.get(i);
            currentNodes = currentNodes.stream()
                .map(n -> goLeft ? leftGraph.get(n.name) : rightGraph.get(n.name))
                .collect(Collectors.toSet());
            steps++;
            final long steps_ro = steps;
            currentNodes.stream()
                .filter(DesertNode::endsZ)
                .forEach(node -> {
                    EndNodeObservations observedSoFar = endNodeObservationMap.get(node.name);
                    if (observedSoFar.stepsToFirstEncounter == -1) {
                        endNodeObservationMap.put(node.name, new EndNodeObservations(node.name, steps_ro, -1));
                    }
                    else if (observedSoFar.period == -1) {
                        endNodeObservationMap.put(node.name, new EndNodeObservations(node.name, observedSoFar.stepsToFirstEncounter, steps_ro - observedSoFar.stepsToFirstEncounter));
                    }
                });
            if (endNodeObservationMap.values().stream().allMatch(eno -> eno.period > 0)) {
                done = true;
            }
            i = (i + 1) % directions_goLeft.size();
        }
        // System.out.println("Steps until everyone has first encounter");
        // EndNodeObservations lastENO = endNodeObservationMap.values().stream().max(Comparator.comparing(EndNodeObservations::stepsToFirstEncounter)).get();
        // System.out.println(lastENO.stepsToFirstEncounter + " ("+lastENO.name+")");
        // System.out.println("All periods with first steps");
        // endNodeObservationMap.values().stream().forEach(eno -> {
        //     System.out.println(eno.period + " ("+eno.name+" " + eno.stepsToFirstEncounter + ")");
        // });
        return Long.toString(findLCM(endNodeObservationMap.values().stream().map(EndNodeObservations::period).collect(Collectors.toList())));
    }

    private long findLCM(List<Long> periods) {
        long lcm = 1;
        for (Long period : periods) {
            lcm = lcm(lcm, period);
        }
        return lcm;
    }

    private long gcd(long a, long b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    private long lcm(long a, long b) {
        return (a * b) / gcd(a, b);
    }

    record DesertNode(String name, boolean endsZ, boolean endsA){}
    record EndNodeObservations(String name, long stepsToFirstEncounter, long period){}
}
