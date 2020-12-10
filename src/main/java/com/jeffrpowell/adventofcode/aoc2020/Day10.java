package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.List;

public class Day10 extends Solution2020<Integer>{

    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public InputParser<Integer> getInputParser() {
        return InputParserFactory.getIntegerParser();
    }

    @Override
    protected String part1(List<Integer> input) {
        input.sort(Integer::compare);
        int lastJoltage = 0;
        int diff1 = 0;
        int diff3 = 1;
        for (Integer i : input) {
            switch (i - lastJoltage) {
                case 1 -> diff1++;
                case 3 -> diff3++;
            }
            lastJoltage = i;
        }
        return Integer.toString(diff1 * diff3);
    }
    
    @Override
    protected String part2(List<Integer> input) {
        input.sort(Integer::compare);
        input.add(input.get(input.size() - 1) + 3);
        int lastJoltage = 0;
        int consecutiveDiff1 = 0;
        long total = 1;
        for (Integer i : input) {
            switch (i - lastJoltage) {
                case 1 -> consecutiveDiff1++;
                case 3 -> {
                    switch (consecutiveDiff1) {
                        case 2 -> total *= 2;
                        case 3 -> total *= 4;
                        case 4 -> total *= 7;
                    }
                    consecutiveDiff1 = 0;
                }
            }
            lastJoltage = i;
        }
        return Long.toString(total);
    }

}
