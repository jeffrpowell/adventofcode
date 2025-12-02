package com.jeffrpowell.adventofcode.aoc2015;

import java.util.ArrayList;
import java.util.List;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day10 extends Solution2015<List<Integer>>{
    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser("");
    }
    
    @Override
    protected String part1(List<List<Integer>> input) {
        return runSequence(input, 40);
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        return runSequence(input, 50);
    }

    private String runSequence(List<List<Integer>> input, int iterations) {
        List<Integer> priorLine = input.get(0);
        for (int i = 0; i < iterations; i++) {
            List<Integer> nextLine = new ArrayList<>();
            int count = 0;
            int currentDigit = priorLine.get(0);
            for (int j = 0; j < priorLine.size(); j++) {
                int digit = priorLine.get(j);
                if (digit == currentDigit) {
                    count++;
                } else {
                    nextLine.add(count);
                    nextLine.add(currentDigit);
                    currentDigit = digit;
                    count = 1;
                }
                if (j == priorLine.size() - 1) {
                    nextLine.add(count);
                    nextLine.add(currentDigit);
                }
            }
            priorLine = nextLine;
        }
        return Integer.toString(priorLine.size());
    }
}
