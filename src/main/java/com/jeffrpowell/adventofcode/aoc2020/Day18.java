package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day18 extends Solution2020<String>{
    static final Pattern OPERAND_PATTERN = Pattern.compile("(\\+|\\*)?(\\d+)");
    static final Pattern PLUS_PATTERN = Pattern.compile("(\\d+)\\+(\\d+)");
    
    @Override
    public int getDay() {
        return 18;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }

    @Override
    protected String part1(List<String> input) {
        long total = 0;
        for (String expression : input) {
            expression = expression.replaceAll("\\s+", "");
            int endParen = expression.indexOf(")");
            while (endParen > -1) {
                for (int i = endParen - 1; i >= 0; i--) {
                    if (expression.charAt(i) == '(') {
                        long evaluation = evaluateParenthetical(expression.substring(i + 1, endParen));
                        expression = expression.substring(0, i) + evaluation + expression.substring(endParen + 1);
                        endParen = expression.indexOf(")");
                        break;
                    }
                }
            }
            total += evaluateParenthetical(expression);
        }
        return Long.toString(total);
    }
    
    private long evaluateParenthetical(String expression) {
        Matcher m = OPERAND_PATTERN.matcher(expression);
        boolean first = true;
        long val = 0;
        while(m.find()) {
            if (first) {
                val = Integer.parseInt(m.group(2));
                first = false;
            }
            else {
                if (m.group(1).equals("*")) {
                    val *= Integer.parseInt(m.group(2));
                }
                else {
                    val += Integer.parseInt(m.group(2));
                }
            }
        }
        return val;
    }

    @Override
    protected String part2(List<String> input) {
        long total = 0;
        for (String expression : input) {
            expression = expression.replaceAll("\\s+", "");
            int endParen = expression.indexOf(")");
            while (endParen > -1) {
                for (int i = endParen - 1; i >= 0; i--) {
                    if (expression.charAt(i) == '(') {
                        long evaluation = evaluateParentheticalPart2(expression.substring(i + 1, endParen));
                        expression = expression.substring(0, i) + evaluation + expression.substring(endParen + 1);
                        endParen = expression.indexOf(")");
                        break;
                    }
                }
            }
            total += evaluateParentheticalPart2(expression);
        }
        return Long.toString(total);
    }
    
    private long evaluateParentheticalPart2(String expression) {
        Matcher m = PLUS_PATTERN.matcher(expression);
        while(m.find(0)) {
            expression = m.replaceFirst(matchResult -> Long.toString(Long.parseLong(matchResult.group(1)) + Long.parseLong(matchResult.group(2))));
            m.reset(expression);
        }
        Matcher m2 = OPERAND_PATTERN.matcher(expression);
        long val = 1;
        boolean first = true;
        while(m2.find()) {
            if (first) {
                val = Long.parseLong(m2.group(2));
                first = false;
            }
            else {
                val *= Long.parseLong(m2.group(2));
            }
        }
        return val;
    }

}
