package com.jeffrpowell.adventofcode.aoc2022;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day3 extends Solution2022<String>{

    @Override
    public int getDay() {
        return 3;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }

    @Override
    protected String part1(List<String> input) {
        return input.stream()
            .map(this::findSharedItem)
            .map(this::priority)
            .collect(Collectors.reducing(0, Math::addExact)).toString();
    }

    private Character findSharedItem(String s) {
        Set<Character> visited = new HashSet<>();
        String lastHalf = s.substring(s.length() / 2);
        IntStream.range(0, s.length() / 2)
            .mapToObj(lastHalf::charAt)
            .forEach(visited::add);
        for (int i = 0; i < s.length(); i++) {
            if (visited.contains(s.charAt(i))) {
                return s.charAt(i);
            }
        }
        return null;
    }

    private int priority(Character c) {
        if (c.charValue() < ('Z' + 1)) {
            return c.charValue() - 'A' + 27;
        }
        else {
            return c.charValue() - 'a' + 1;
        }
    }

    @Override
    protected String part2(List<String> input) {
        List<List<String>> groups = new ArrayList<>();
        for (int i = 2; i < input.size(); i+=3) {
            List<String> group = new ArrayList<>();
            group.add(input.get(i-2));
            group.add(input.get(i-1));
            group.add(input.get(i));
            groups.add(group);
        }
        return groups.stream()
            .map(this::findSharedItem2)
            .map(this::priority)
            .collect(Collectors.reducing(0, Math::addExact)).toString();
    }

    private Character findSharedItem2(List<String> group) {
        Set<Character> chars = new HashSet<>();
        String s = group.get(0);
        IntStream.range(0, s.length())
            .mapToObj(s::charAt)
            .forEach(chars::add);
        s = group.get(1);
        chars = IntStream.range(0, s.length())
            .mapToObj(s::charAt)
            .filter(chars::contains)
            .collect(Collectors.toSet());
        s = group.get(2);
        return IntStream.range(0, s.length())
            .mapToObj(s::charAt)
            .filter(chars::contains)
            .findAny().get();
    }
}
