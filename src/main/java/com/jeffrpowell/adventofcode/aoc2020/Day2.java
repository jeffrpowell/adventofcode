package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import java.util.List;
import java.util.regex.Pattern;

public class Day2 extends Solution2020<Rule>{

    @Override
    public int getDay() {
        return 2;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        String regex = "(\\d+)-(\\d+) (\\w): (\\w+)";
        return InputParserFactory.getRuleParser("\n", Pattern.compile(regex));
    }

    @Override
    protected String part1(List<Rule> input) {
        int valid = 0;
        for (Rule rule : input) {
            int low = rule.getInt(0);
            int high = rule.getInt(1);
            String c = rule.getString(2);
            String password = rule.getString(3);
            int oldLength = password.length();
            password = password.replace(c, "");
            int newLength = password.length();
            int count = oldLength - newLength;
            if (count >= low && count <= high) {
                valid++;
            }
        }
        return Integer.toString(valid);
    }

    @Override
    protected String part2(List<Rule> input) {
        int valid = 0;
        for (Rule rule : input) {
            int low = rule.getInt(0);
            int high = rule.getInt(1);
            char c = rule.getChar(2);
            String password = rule.getString(3);
            boolean matchLow = password.charAt(low - 1) == c;
            boolean matchHigh = password.charAt(high - 1) == c;
            if (matchLow && !matchHigh || !matchLow && matchHigh) {
                valid++;
            }
        }
        return Integer.toString(valid);
    }

}
