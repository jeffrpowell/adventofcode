package com.jeffrpowell.adventofcode.aoc2015;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day23 extends Solution2015<Rule>{

    @Override
    public int getDay() {
        return 23;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Map.of(
            "hlf", Pattern.compile("hlf (\\w)"),
            "tpl", Pattern.compile("tpl (\\w)"),
            "inc", Pattern.compile("inc (\\w)"),
            "jmp", Pattern.compile("jmp ([+-]\\d+)"),
            "jie", Pattern.compile("jie (\\w), ([+-]\\d+)"),
            "jio", Pattern.compile("jio (\\w), ([+-]\\d+)")
        ));
    }

    @Override
    protected String part1(List<Rule> input) {
        return simulate(input, 0, 0);
    }


    @Override
    protected String part2(List<Rule> input) {
        return simulate(input, 1, 0);
    }

    private String simulate(List<Rule> input, int a, int b) {
        int pt = 0;
        while (pt >= 0 && pt < input.size()) {
            Rule rule = input.get(pt);
            switch (rule.getRulePatternKey()) {
                case "hlf" -> {
                    if (rule.getString(0).equals("a")) {
                        a /= 2;
                    } else {
                        b /= 2;
                    }
                    pt++;
                }
                case "tpl" -> {
                    if (rule.getString(0).equals("a")) {
                        a *= 3;
                    } else {
                        b *= 3;
                    }
                    pt++;
                }
                case "inc" -> {
                    if (rule.getString(0).equals("a")) {
                        a++;
                    } else {
                        b++;
                    }
                    pt++;
                }
                case "jmp" -> pt += rule.getInt(0);
                case "jie" -> {
                    if (rule.getString(0).equals("a")) {
                        pt += (a % 2 == 0) ? rule.getInt(1) : 1;
                    } else {
                        pt += (b % 2 == 0) ? rule.getInt(1) : 1;
                    }
                }
                case "jio" -> {
                    if (rule.getString(0).equals("a")) {
                        pt += (a == 1) ? rule.getInt(1) : 1;
                    } else {
                        pt += (b == 1) ? rule.getInt(1) : 1;
                    }
                }
            }
        }
        return Integer.toString(b);
    }
}
