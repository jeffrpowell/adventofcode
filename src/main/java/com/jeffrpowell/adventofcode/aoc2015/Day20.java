package com.jeffrpowell.adventofcode.aoc2015;

import java.util.List;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day20 extends Solution2015<Long>{

    @Override
    public int getDay() {
        return 20;
    }

    @Override
    public InputParser<Long> getInputParser() {
        return InputParserFactory.getLongParser();
    }

    @Override
    protected String part1(List<Long> input) {
        int target = input.get(0).intValue() / 10;
        long[] houses = new long[target + 1];
        for (int elf = 1; elf < target + 1; elf++) {
            for (int house = elf; house < target + 1; house += elf) {
                houses[house] += elf;
            }
        }
        for (int house = 1; house < target + 1; house++) {
            if (houses[house] >= target) {
                return Integer.toString(house);
            }
        }
        return null;
    }


    @Override
    protected String part2(List<Long> input) {
        long target = input.get(0);
        long[] houses = new long[Long.valueOf(target + 1).intValue()];
        for (int elf = 1; elf < target + 1; elf++) {
            int deliveries = 0;
            for (int house = elf; house < target + 1 && deliveries < 50; house += elf) {
                houses[house] += elf * 11;
                deliveries++;
            }
        }
        for (int house = 1; house < target + 1; house++) {
            if (houses[house] >= target) {
                return Integer.toString(house);
            }  
        }
        return null;
    }
}
