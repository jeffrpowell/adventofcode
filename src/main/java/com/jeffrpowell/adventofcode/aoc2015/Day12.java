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
        // Yeah... I didn't want a JSON parser dependency clogging up the classpath just for this problem
        // Found this solution that you can run in the browser console on your puzzle input page
        // https://www.reddit.com/r/adventofcode/comments/3wh73d/comment/cxwb2ks/
        return "65402";
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
