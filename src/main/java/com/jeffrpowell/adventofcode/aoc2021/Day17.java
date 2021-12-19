package com.jeffrpowell.adventofcode.aoc2021;

import java.util.List;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day17 extends Solution2021<Rule> {

    @Override
    public int getDay() {
        return 17;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("target area: x=(-?\\d+)\\.\\.(-?\\d+), y=(-?\\d+)\\.\\.(-?\\d+)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        Rule r = input.get(0);
        int xMin = r.getInt(0);
        int xMax = r.getInt(1);
        int yMin = r.getInt(2);
        int yMax = r.getInt(3);

        int maxDownwardGravity = yMax - yMin + 1;
        long distance = 0;
        for(;maxDownwardGravity >= 0; maxDownwardGravity--) {
            distance += maxDownwardGravity;
        }
        return Long.toString(distance);
    }

    @Override
    protected String part2(List<Rule> input) {
        Rule r = input.get(0);
        int xMin = r.getInt(0);
        int xMax = r.getInt(1);
        int yMin = r.getInt(2);
        int yMax = r.getInt(3);
        int maxDownwardGravity = yMax - yMin + 1;
        int minXNeeded = findMinXNeeded(xMin);
        int maxXAvailable = findMaxXAvailable(minXNeeded, xMax);
    }

    private int findMinXNeeded(int xMin) {
        for (int i = 5; i < xMin / 2; i++) {
            int distance = 0;
            for (int j = i; j >= 0; j--) {
                distance += j;
                if (distance >= xMin) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int findMaxXAvailable(int minXNeeded, int xMax) {
        for (int i = minXNeeded; i < xMax / 2; i++) {
            int distance = 0;
            for (int j = i; j >= 0; j--) {
                distance += j;
                if (distance > xMax) {
                    return i - 1;
                }
            }
        }
        return -1;
    }

}
