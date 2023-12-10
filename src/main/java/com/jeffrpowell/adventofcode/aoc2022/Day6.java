package com.jeffrpowell.adventofcode.aoc2022;

import java.util.List;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.SlidingWindowSpliterator;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day6 extends Solution2022<List<String>>{

    @Override
    public int getDay() {
        return 6;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        List<String> firstMarker = SlidingWindowSpliterator.windowed(input.get(0), 4)
            .map(group -> group.collect(Collectors.toList()))
            .filter(groupList -> groupList.stream().collect(Collectors.toSet()).size() == 4)
            .findFirst().get();
        String str = input.get(0).stream().collect(Collectors.joining());
        String target = firstMarker.stream().collect(Collectors.joining());
        return Integer.toString(str.indexOf(target) + 4);
    }

    @Override
    protected String part2(List<List<String>> input) {
        List<String> firstMarker = SlidingWindowSpliterator.windowed(input.get(0), 14)
            .map(group -> group.collect(Collectors.toList()))
            .filter(groupList -> groupList.stream().collect(Collectors.toSet()).size() == 14)
            .findFirst().get();
        String str = input.get(0).stream().collect(Collectors.joining());
        String target = firstMarker.stream().collect(Collectors.joining());
        return Integer.toString(str.indexOf(target) + 14);
    }
}
