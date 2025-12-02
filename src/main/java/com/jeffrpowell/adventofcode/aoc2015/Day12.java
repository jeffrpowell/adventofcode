package com.jeffrpowell.adventofcode.aoc2015;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day12 extends Solution2015<String>{
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+");

    @Override
    public int getDay() {
        return 12;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }
    
    @Override
    protected String part1(List<String> input) {
        return Long.toString(input.stream()
            .mapToLong(this::sumNumbersInString)
            .sum());
    }

    @Override
    protected String part2(List<String> input) {
        return null;
    }

    private long sumNumbersInString(String line) {
        Matcher m = NUMBER_PATTERN.matcher(line);
        long sum = 0;
        while (m.find()) {
            sum += Long.parseLong(m.group());
        }
        return sum;
    }

}
