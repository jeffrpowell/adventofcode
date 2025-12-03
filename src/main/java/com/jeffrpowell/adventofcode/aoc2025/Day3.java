package com.jeffrpowell.adventofcode.aoc2025;

import java.util.ArrayList;
import java.util.List;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day3 extends Solution2025<List<Integer>>{
    @Override
    public int getDay() {
        return 3;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser("");
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        long sum = 0L;
        for (List<Integer> bank : input) {
            List<Integer> joltagesEnabled = new ArrayList<>();
            findLargestJoltage(bank, joltagesEnabled, 0, 2);
            sum += collapseJoltageDigits(joltagesEnabled);
        }
        return Long.toString(sum);
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        long sum = 0L;
        for (List<Integer> bank : input) {
            List<Integer> joltagesEnabled = new ArrayList<>();
            findLargestJoltage(bank, joltagesEnabled, 0, 12);
            sum += collapseJoltageDigits(joltagesEnabled);
        }
        return Long.toString(sum);
    }

    private void findLargestJoltage(List<Integer> batteries, List<Integer> joltagesEnabled, int startIndex, int batteriesLeftToPick) {
        int largestI = -1;
        int largest = Integer.MIN_VALUE;
        for (int i = startIndex; i < batteries.size() - (batteriesLeftToPick - 1); i++) {
            int next = batteries.get(i);
            if (next > largest) {
                largest = next;
                largestI = i;
            }
        }
        joltagesEnabled.add(largest);
        if (batteriesLeftToPick > 1) {
            findLargestJoltage(batteries, joltagesEnabled, largestI+1, batteriesLeftToPick - 1);
        }
    }

    private long collapseJoltageDigits(List<Integer> joltages) {
        StringBuilder sb = new StringBuilder();
        for (Integer joltage : joltages) {
            sb.append(joltage);
        }
        return Long.parseLong(sb.toString());
    }
}
