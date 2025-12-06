package com.jeffrpowell.adventofcode.aoc2025;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day6 extends Solution2025<List<String>>{
    @Override
    public int getDay() {
        return 6;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("\\s+");
    }

    @Override
    protected String part1(List<List<String>> input) {
        List<String> operand0 = input.get(0);
        List<String> operand1 = input.get(1);
        List<String> operand2 = input.get(2);
        List<String> operand3 = input.get(3);
        List<Boolean> operatorIsMultiply = input.get(4).stream()
            .map(s -> s.equals("*"))
            .toList();
        long total = 0;
        for (int i = 0; i < operand0.size(); i++) {
            if (operatorIsMultiply.get(i)) {
                total += Long.parseLong(operand0.get(i)) * Long.parseLong(operand1.get(i)) * Long.parseLong(operand2.get(i)) * Long.parseLong(operand3.get(i));
            } else {
                total += Long.parseLong(operand0.get(i)) + Long.parseLong(operand1.get(i)) + Long.parseLong(operand2.get(i)) + Long.parseLong(operand3.get(i));
            }
        }
        return Long.toString(total);
    }

    @Override
    public InputParser<List<String>> getInputParserPart2() {
        return InputParserFactory.getTokenSVParser("$");
    }

    private static final Pattern OPERATOR_PATTERN = Pattern.compile("[+*]");

    @Override
    protected String part2(List<List<String>> input) {
        List<String> operandLines = input.stream().limit(4).map(l -> l.get(0)).collect(Collectors.toList());
        String operators = input.get(4).get(0);
        long total = 0;
        Matcher m = OPERATOR_PATTERN.matcher(operators);
        int currentOperatorIndex = -1;
        m.find();
        int nextOperatorIndex = m.start(); //should be 0
        while(nextOperatorIndex < operators.length()) {
            currentOperatorIndex = nextOperatorIndex;
            if (m.find()) {
                nextOperatorIndex = m.start();
            }
            else {
                nextOperatorIndex = operators.length() + 1; //we do the -1 later when we set the columnSize
            }
            boolean multiply = operators.charAt(currentOperatorIndex) == '*';
            int columnSize = nextOperatorIndex - currentOperatorIndex - 1; //1 space between columns
            List<Long> operands = new ArrayList<>();
            OperandBuilder operandBuilder = new OperandBuilder();
            for (int i = currentOperatorIndex; i < currentOperatorIndex + columnSize; i++) {
                for (int j = 0; j < operandLines.size(); j++) {
                    char digit = operandLines.get(j).charAt(i);
                    if (digit != ' ') {
                        operandBuilder.addDigit(digit);
                    }
                }
                operands.add(operandBuilder.join());
                operandBuilder.clear();
            }
            if (multiply) {
                total += operands.stream().reduce(1L, Math::multiplyExact);
            }
            else {
                total += operands.stream().reduce(0L, Math::addExact);
            }
        }
        return Long.toString(total);
    }

    private static class OperandBuilder {
        private final List<Character> digits;

        public OperandBuilder() {
            this.digits = new ArrayList<>();
        }

        public void addDigit(char digit) {
            digits.add(digit);
        }

        public long join() {
            return Long.parseLong(digits.stream()
                .map(String::valueOf)
                .collect(Collectors.joining()));
        }

        public void clear() {
            digits.clear();
        }
    }
}
