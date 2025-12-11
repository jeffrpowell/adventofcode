package com.jeffrpowell.adventofcode.aoc2025;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.SplitPartParser.Part;

public class Day11 extends Solution2025<Part<String, List<String>>>{
    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public InputParser<Part<String, List<String>>> getInputParser() {
        return InputParserFactory.getSplitPartParser(Pattern.compile(": "), InputParserFactory.getStringParser(), InputParserFactory.getTokenSVParser(" "));
    }

    @Override
    protected String part1(List<Part<String, List<String>>> input) {
        Map<String, List<String>> outputs = new HashMap<>();
        for (Part<String,List<String>> part : input) {
            outputs.putIfAbsent(part.firstPart(), new ArrayList<>());
            for (String down : part.secondPart()) {
                outputs.get(part.firstPart()).add(down);
            }
        }
        Map<String, Map<String, Long>> cache = new HashMap<>();
        return Long.toString(dig("you", "out", outputs, cache));
    }

    @Override
    protected String part2(List<Part<String, List<String>>> input) {
        Map<String, List<String>> outputs = new HashMap<>();
        for (Part<String,List<String>> part : input) {
            outputs.putIfAbsent(part.firstPart(), new ArrayList<>());
            for (String down : part.secondPart()) {
                outputs.get(part.firstPart()).add(down);
            }
        }
        Map<String, Map<String, Long>> cache = new HashMap<>();
        return Long.toString(
            dig("dac", "out", outputs, cache)
            * dig("fft", "dac", outputs, cache)
            * dig("svr", "fft", outputs, cache)
        );
    }

    private long dig(String start, String target, Map<String, List<String>> outputs, Map<String, Map<String, Long>> cache) {
        long winners = 0;
        for (String down : outputs.get(start)) {
            if (down.equals(target)) {
                winners++;
            }
            else if (!outputs.containsKey(down)) {
                continue;
            }
            else if (cache.containsKey(down) && cache.get(down).containsKey(target)) {
                winners += cache.get(down).get(target);
            }
            else {
                long downstreamWinners = dig(down,target, outputs, cache);
                cache.putIfAbsent(down, new HashMap<>());
                cache.get(down).put(target, downstreamWinners);
                winners += downstreamWinners;
            }
        }
        return winners;
    }
}
