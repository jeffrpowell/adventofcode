package com.jeffrpowell.adventofcode.aoc2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.SplitPartParser;

public class Day12 extends Solution2023<SplitPartParser.Part<String, List<Integer>>> {

    private static final Map<CacheKey, Long> CACHE = new HashMap<>();

    @Override
    public int getDay() {
        return 12;
    }

    @Override
    public InputParser<SplitPartParser.Part<String, List<Integer>>> getInputParser() {
        return InputParserFactory.getSplitPartParser(
                Pattern.compile(" "),
                InputParserFactory.getStringParser(),
                InputParserFactory.getIntegerCSVParser());
    }

    @Override
    protected String part1(List<SplitPartParser.Part<String, List<Integer>>> input) {
        return Long.toString(input.stream()
                .map(parts -> numWaysToResolve(parts.firstPart(), parts.secondPart()))
                .reduce(0L, Math::addExact));
    }

    @Override
    protected String part2(List<SplitPartParser.Part<String, List<Integer>>> input) {
        return Long.toString(input.stream()
                .map(parts -> numWaysToResolve(
                    Stream.generate(parts::firstPart).limit(5).collect(Collectors.joining("?")), 
                    Stream.generate(parts::secondPart).limit(5).flatMap(List::stream).collect(Collectors.toList())
                ))
                .reduce(0L, Math::addExact));
    }

    record CacheKey(String group, List<Integer> requirements){}

    private long numWaysToResolve(String group, List<Integer> requirements) {
        if (group.length() == 0) {
            return requirements.isEmpty() ? 1 : 0;
        }
        if (requirements.isEmpty()) {
            return group.contains("#") ? 0 : 1;
        }
        CacheKey key = new CacheKey(group, requirements);
        if (CACHE.containsKey(key)) {
            return CACHE.get(key);
        }
        long numWays = 0;
        if (group.startsWith(".") || group.startsWith("?")) {
            numWays += numWaysToResolve(group.substring(1), requirements);
        }
        if (group.startsWith("#") || group.startsWith("?")) {
            int nextReq = requirements.get(0);
            if (nextReq <= group.length()
                && !group.substring(0, nextReq).contains(".")
                && (nextReq == group.length() || group.charAt(nextReq) != '#')) {
                    String nextGroup = "";
                    if (group.length() > nextReq) {
                        nextGroup = group.substring(nextReq + 1);
                    }
                    List<Integer> nextRequirements = new ArrayList<>();
                    if (requirements.size() > 1) {
                        nextRequirements = requirements.stream().skip(1).collect(Collectors.toList());
                    }
                    numWays += numWaysToResolve(nextGroup, nextRequirements);
                }
        }
        CACHE.put(key, numWays);
        return numWays;
    }
}
