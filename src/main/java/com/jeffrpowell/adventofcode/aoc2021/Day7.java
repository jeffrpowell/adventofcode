package com.jeffrpowell.adventofcode.aoc2021;

import java.util.Comparator;
import java.util.List;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day7 extends Solution2021<List<Integer>>{

    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerCSVParser();
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        List<Integer> x = input.get(0);
        x.sort(Comparator.naturalOrder());
        int try1 = x.get(499);
        int distances = x.stream().map(i -> Math.abs(i - try1)).reduce(0, Math::addExact);
        int try2 = x.get(500);
        distances = Math.min(distances, x.stream().map(i -> Math.abs(i - try2)).reduce(0, Math::addExact));
        return Integer.toString(distances);
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        List<Integer> x = input.get(0);
        long avg = Math.round(x.stream().map(Integer::doubleValue).reduce(0D, (accum, next) -> accum + next) / x.size());
        long fuel = Long.MAX_VALUE;
        for (long nearAvg = avg - 2; nearAvg < avg + 2; nearAvg++) {
            final long l = nearAvg;
            fuel = Math.min(fuel, x.stream().map(i -> fuelConsumption(i, l)).reduce(0L, (accum, next) -> accum + next));
        }
        return Long.toString(fuel);
    }
    
    private long fuelConsumption(int pt, long target) {
        long start = Math.min(pt, target);
        long end = Math.max(pt, target);
        long fuel = 0;
        for (int i = 1; start < end; start++) {
            fuel += i;
            i++;
        }
        return fuel;
    }
}
