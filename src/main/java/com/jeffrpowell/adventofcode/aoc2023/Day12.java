package com.jeffrpowell.adventofcode.aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.CharArrayUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.SplitPartParser;

public class Day12 extends Solution2023<SplitPartParser.Part<String, List<Integer>>> {

    private static final Map<SpringGroup.CacheKey, Long> CACHE = new HashMap<>();

    @Override
    public int getDay() {
        return 12;
    }

    @Override
    public InputParser<SplitPartParser.Part<String, List<Integer>>> getInputParser() {
        return InputParserFactory.getSplitPartParser(
                Pattern.compile(" "),
                InputParserFactory.getPreParser(
                        s -> {
                            String collapsedS = s.replaceAll("\\.{2,}", "."); // contiguous groups of . are equivalent
                                                                              // to a single .
                            if (collapsedS.startsWith(".")) {
                                collapsedS = collapsedS.substring(1); // no need to consider a leading .
                            }
                            if (collapsedS.endsWith(".")) {
                                collapsedS = collapsedS.substring(0, collapsedS.length() - 1); // no need to consider a
                                                                                               // trailing .
                            }
                            return collapsedS;
                        },
                        InputParserFactory.getStringParser()),
                InputParserFactory.getIntegerCSVParser());
    }

    @Override
    protected String part1(List<SplitPartParser.Part<String, List<Integer>>> input) {
        return Long.toString(input.stream()
                .map(parts -> numWaysToResolve(
                        splitIntoGroups(parts.firstPart()),
                        parts.secondPart()))
                .reduce(0L, Math::addExact));
    }

    @Override
    protected String part2(List<SplitPartParser.Part<String, List<Integer>>> input) {
        return part1(input);
    }

    private List<SpringGroup> splitIntoGroups(String str) {
        // System.out.println(str);
        return Arrays.stream(str.split("\\."))
                .map(SpringGroup::new)
                .collect(Collectors.toList());
    }

    private long numWaysToResolve(List<SpringGroup> groups, List<Integer> requirements) {
        // if (groups.size() == requirements.size()) {
        // // System.out.println("Instance of groups being equal to requirements: "
        // // +
        // groups.stream().map(SpringGroup::toString).collect(Collectors.joining("."))
        // // + " | " + requirements.stream().map(i ->
        // i.toString()).collect(Collectors.joining(",")));
        // else if (groups.size() < requirements.size()) {
        // // System.out.println("Instance of groups being less than requirements: "
        // // +
        // groups.stream().map(SpringGroup::toString).collect(Collectors.joining("."))
        // // + " | " + requirements.stream().map(i ->
        // i.toString()).collect(Collectors.joining(",")));
        // }
        // else {
        // // System.out.println("Instance of groups being greater than requirements: "
        // // +
        // groups.stream().map(SpringGroup::toString).collect(Collectors.joining("."))
        // // + " | " + requirements.stream().map(i ->
        // i.toString()).collect(Collectors.joining(",")));
        // }
        List<List<List<Integer>>> requirementsPermutations = permuteRequirements(requirements, groups.size());
        return requirementsPermutations.stream()
            .filter(reqPerm -> precheckRequirementsToGroupsMapping(groups, reqPerm))
            .map(reqPerm -> {
                long numWays = 0;
                for (int i = 0; i < reqPerm.size(); i++) {
                    numWays += resolve(groups.get(i), reqPerm.get(i));
                }
                return numWays;
            })
            .reduce(0L, Math::addExact);
    }

    /**
     * Intended effect:
     * 4 SpringGroups | requirements list of size 6
     * [][][][] | [1, 2, 3, 4, 5, 6]
     * splitRequirementsIntoGroups(requirements, List.of(0, 0, 0)); // [|||123456]
     * splitRequirementsIntoGroups(requirements, List.of(0, 0, 1)); // [||1|23456]
     * splitRequirementsIntoGroups(requirements, List.of(0, 0, 2)); // [||12|3456]
     * splitRequirementsIntoGroups(requirements, List.of(2, 4, 5)); // [12|34|5|6]
     * ... etc.
     * @param requirements
     * @param groupSize
     * @return All permutations of splitting the requirements list into the specified number of groups
     */
    private List<List<List<Integer>>> permuteRequirements(List<Integer> requirements, int groupSize) {
        return generateSets(requirements.size(), groupSize).stream()
            .map(splitIndices -> splitRequirementsIntoGroups(requirements, splitIndices))
            .collect(Collectors.toList());
    }
 
    /**
     * Entry point for requirements indices permutation recursion
     * @param numRequirements
     * @param numGroups
     * @return
     */
    private static List<List<Integer>> generateSets(int numRequirements, int numGroups) {
        List<List<Integer>> result = new ArrayList<>();
        generateSetsHelper(result, new ArrayList<>(), numRequirements, numGroups - 1, 0);
        return result;
    }
 
    /**
     * Recursive method underneath generateSets()
     * @param result
     * @param current
     * @param numRequirements
     * @param numGroups
     * @param start
     */
    private static void generateSetsHelper(List<List<Integer>> result, List<Integer> current, int numRequirements, int numGroups, int start) {
        if (numGroups == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i <= numRequirements; i++) {
            current.add(i);
            generateSetsHelper(result, current, numRequirements, numGroups - 1, i);
            current.remove(current.size() - 1);
        }
    }

    /**
     * Takes requirements list, splits it at the indices provided, and returns the resulting list of sub-lists
     * @param requirements
     * @param splitIndices
     * @return
     */
    private List<List<Integer>> splitRequirementsIntoGroups(List<Integer> requirements, List<Integer> splitIndices) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> amendedSplitIndices = splitIndices.stream().collect(Collectors.toList());
        amendedSplitIndices.add(0, 0);
        amendedSplitIndices.add(requirements.size());
        for (int i = 0; i < amendedSplitIndices.size() - 1; i++) {
            result.add(requirements.subList(amendedSplitIndices.get(i), amendedSplitIndices.get(i+1)).stream().collect(Collectors.toList()));
        }
        return result;
    }

    private boolean precheckRequirementsToGroupsMapping(List<SpringGroup> groups, List<List<Integer>> requirements) {
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).size() < getRequirementsLength(requirements.get(i))) {
                return false;
            }
        }
        return true;
    }

    private int getRequirementsLength(List<Integer> requirements) {
        return requirements.stream().reduce(0, Math::addExact) 
            + requirements.size() - 1; //account for the .'s you'll need between the groups
    }

    private long resolve(SpringGroup group, List<Integer> requirements) {
        SpringGroup.CacheKey cacheKey = group.generateCacheKey(requirements);
        if (CACHE.containsKey(cacheKey)) {
            return CACHE.get(cacheKey);
        }
        long numWays = 0L;
        if (group.hasUnknowns()) {
            numWays += group.generateNextPair(requirements.size()).stream().map(g -> resolve(g, requirements)).reduce(0L, Math::addExact);
        }
        else {
            numWays = group.matchesRequirements(requirements) ? 1 : 0;
        }
        CACHE.put(cacheKey, numWays);
        return numWays;
    }

    private static enum State {
        WORKING, BROKEN, UNKNOWN;

        public static State fromChar(char s) {
            return switch (s) {
                case '#' -> WORKING;
                case '?' -> UNKNOWN;
                default -> BROKEN;
            };
        }
    }

    private static class SpringGroup {
        private final List<State> states;

        public static record CacheKey(SpringGroup group, List<Integer> requirements) {}

        public SpringGroup(String str) {
            this.states = CharArrayUtils.toStream(str.toCharArray())
                    .map(State::fromChar)
                    .collect(Collectors.toList());
        }

        public SpringGroup(List<State> states) {
            this.states = states;
        }

        public int size() {
            return states.size();
        }

        public boolean hasUnknowns() {
            return states.stream().anyMatch(s -> s == State.UNKNOWN);
        }

        public List<SpringGroup> generateNextPair(int requirementsSize) {
            List<SpringGroup> next = new ArrayList<>();
            int nextUnknownIndex = states.indexOf(State.UNKNOWN);
            List<State> nextStates = states.stream().collect(Collectors.toList());
            nextStates.set(nextUnknownIndex, State.WORKING);
            next.add(new SpringGroup(nextStates));
            // if (states.stream().filter(s -> s == State.BROKEN).count() < requirementsSize - 1) {
                List<State> nextBrokenStates = nextStates.stream().collect(Collectors.toList());
                nextBrokenStates.set(nextUnknownIndex, State.BROKEN);
                next.add(new SpringGroup(nextBrokenStates));
            // }
            return next;
        }

        public CacheKey generateCacheKey(List<Integer> requirements) {
            return new CacheKey(this, requirements);
        }

        public boolean matchesRequirements(List<Integer> requirements) {
            if (requirements.isEmpty() || requirements.get(0) == 0) {
                if (states.stream().anyMatch(s -> s == State.WORKING)) {
                    return false;
                }
                else {
                    return true;
                }
            }
            if (states.stream().filter(s -> s == State.BROKEN).count() != requirements.size() - 1) {
                return false;
            }
            int nextRequirement = requirements.removeFirst();
            boolean slide = true;
            for (int i = 0; i < states.size(); i++) {
                if (states.get(i) == State.WORKING) {
                    slide = false;
                    nextRequirement--;
                    if (nextRequirement < 0) {
                        return false;
                    }
                }
                else if (states.get(i) == State.BROKEN) {
                    if (slide) {
                        continue;
                    }
                    if (nextRequirement == 0 && !requirements.isEmpty()) {
                        nextRequirement = requirements.removeFirst();
                        slide = true;
                    }
                    else if (nextRequirement != 0) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((states == null) ? 0 : states.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SpringGroup other = (SpringGroup) obj;
            if (states == null) {
                if (other.states != null)
                    return false;
            } else if (!states.equals(other.states))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return states.stream()
                    .map(s -> switch (s) {
                        case State.WORKING -> "#";
                        case State.UNKNOWN -> "?";
                        default -> ".";
                    })
                    .collect(Collectors.joining());
        }
    }
}
