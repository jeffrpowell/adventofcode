package com.jeffrpowell.adventofcode.aoc2025;

import java.util.List;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day1 extends Solution2025<Rule>{
    @Override
    public int getDay() {
        return 1;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("(R|L)(\\d+)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        int dial = 50;
        int zeroes = 0;
        for (Rule rule : input) {
            boolean right = rule.getString(0).equals("R");
            int distance = rule.getInt(1);
            dial = (dial + (right ? distance : -distance) + 100) % 100;
            if (dial == 0) {
                zeroes++;
            }
        }
        return Integer.toString(zeroes);
    }

    @Override
    protected String part2(List<Rule> input) {
        int dial = 50;
        int zeroes = 0;
        int priorDial = 50;
        for (Rule rule : input) {
            boolean right = rule.getString(0).equals("R");
            int distance = rule.getInt(1);
            while (distance > 0) {
                int distanceToTravel = Math.min(100, distance);
                if (distanceToTravel < 100) {
                    dial = (dial + (right ? distanceToTravel : -distanceToTravel) + 100) % 100;
                    if (right && dial < priorDial && priorDial != 0) {
                        zeroes++;
                    }
                    else if (!right && dial > priorDial && priorDial != 0) {
                        zeroes++;
                    }
                    else if (dial == 0) {
                        zeroes++;
                    }
                    priorDial = dial;
                }
                else {
                    zeroes++;
                }
                distance = Math.max(0, distance - distanceToTravel);
            }
        }
        return Integer.toString(zeroes);
    }

}
