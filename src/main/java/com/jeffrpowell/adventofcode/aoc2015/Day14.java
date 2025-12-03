package com.jeffrpowell.adventofcode.aoc2015;

import java.util.List;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day14 extends Solution2015<Rule>{

    @Override
    public int getDay() {
        return 14;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds."));
    }
    
    @Override
    protected String part1(List<Rule> input) {
        int totalTime = 2503;
        long maxDistance = 0;
        for (Rule rule : input) {
            int speed = rule.getInt(1);
            int flyTime = rule.getInt(2);
            int restTime = rule.getInt(3);
            long distance = simulateReindeer(speed, flyTime, restTime, totalTime);
            if (distance > maxDistance) {
                maxDistance = distance;
            }
        }
        return Long.toString(maxDistance);
    }

    @Override
    protected String part2(List<Rule> input) {
        int totalTime = 2503;
        int numReindeer = input.size();
        long[] distances = new long[numReindeer];
        int[] points = new int[numReindeer];
        for (int t = 1; t <= totalTime; t++) {
            for (int i = 0; i < numReindeer; i++) {
                Rule rule = input.get(i);
                int speed = rule.getInt(1);
                int flyTime = rule.getInt(2);
                int restTime = rule.getInt(3);
                distances[i] = simulateReindeer(speed, flyTime, restTime, t);
            }
            long maxDistanceThisSecond = 0;
            for (long distance : distances) {
                if (distance > maxDistanceThisSecond) {
                    maxDistanceThisSecond = distance;
                }
            }
            for (int i = 0; i < numReindeer; i++) {
                if (distances[i] == maxDistanceThisSecond) {
                    points[i]++;
                }
            }
        }
        int maxPoints = 0;
        for (int point : points) {
            if (point > maxPoints) {
                maxPoints = point;
            }
        }
        return Integer.toString(maxPoints);
    }

    private long simulateReindeer(int speed, int flyTime, int restTime, int totalTime) {
        long distance = 0;
        int timeRemaining = totalTime;
        while (timeRemaining > 0) {
            int currentFlyTime = Math.min(flyTime, timeRemaining);
            distance += currentFlyTime * speed;
            timeRemaining -= currentFlyTime;
            if (timeRemaining <= 0) {
                break;
            }
            int currentRestTime = Math.min(restTime, timeRemaining);
            timeRemaining -= currentRestTime;
        }
        return distance;
    }
}
