package com.jeffrpowell.adventofcode.aoc2015;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.algorithms.CharArrayUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day8 extends Solution2015<String>{
    // //
    // /"
    // /x##
    private static final Pattern ESCAPE_SEQUENCE_PATTERN = Pattern.compile("(\\\\\\\\|\\\\\"|\\\\x[0-9a-fA-F]{2})");
    @Override
    public int getDay() {
        return 8;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }
    
    @Override
    protected String part1(List<String> input) {
        return Integer.toString(input.stream()
            .mapToInt(s -> s.length() - countStringLength(s))
            .sum());
    }

    private int countStringLength(String s) {
        Matcher m = ESCAPE_SEQUENCE_PATTERN.matcher(s);
        int extraChars = 0;
        while (m.find()) {
            if (m.group().startsWith("\\x")) {
                extraChars += 3;
            } else {
                extraChars += 1;
            }
        }
        return s.length() - extraChars - 2 /* Surrounding quote chars */;
    }

    @Override
    protected String part2(List<String> input) {
        return Long.toString(input.stream()
            .mapToLong(s -> countCharsNeedingEscape(s) + 2 /* Add new surrounding quotes */)
            .sum());
    }

    private long countCharsNeedingEscape(String s) {
        return CharArrayUtils.toStream(s.toCharArray())
            .filter(c -> c == '\\' || c == '"')
            .count();
    }
}
