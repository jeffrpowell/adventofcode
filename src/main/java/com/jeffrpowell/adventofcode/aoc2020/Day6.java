package com.jeffrpowell.adventofcode.aoc2020;

import com.google.common.collect.Sets;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6 extends Solution2020<String>{

    @Override
    public int getDay() {
        return 6;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }

    @Override
    protected String part1(List<String> input) {
        var answeredYes = new HashSet<Character>();
        var count = 0;
        for (String line : input) {
            if (line.isBlank()) {
                count += answeredYes.size();
                answeredYes.clear();
            }
            else {
                answeredYes.addAll(line.chars().mapToObj(i -> (char) i).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
            }
        }
        count += answeredYes.size();
        return Integer.toString(count);
    }

    @Override
    protected String part2(List<String> input) {
        Set<Character> firstChars = new HashSet<>();
        var firstLine = true;
        var count = 0;
        for (String line : input) {
            if (line.isBlank()) {
                count += firstChars.size();
                firstChars.clear();
                firstLine = true;
            }
            else if (firstLine) {
                firstChars.addAll(line.chars().mapToObj(i -> (char) i).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
                firstLine = false;
            }
            else {
                Set<Character> comparisonSet = new HashSet<>();
                comparisonSet.addAll(line.chars().mapToObj(i -> (char) i).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
                firstChars = ((Sets.SetView)Sets.intersection(firstChars, comparisonSet)).copyInto(new HashSet<>());
            }
        }
        count += firstChars.size();
        return Integer.toString(count);
    }

}
