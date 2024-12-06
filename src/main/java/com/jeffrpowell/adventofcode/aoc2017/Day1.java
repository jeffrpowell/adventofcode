package com.jeffrpowell.adventofcode.aoc2017;

import java.util.List;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day1 extends Solution2017<List<Integer>>{
    @Override
    public int getDay() {
        return 1;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser("");
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        List<Integer> list = input.get(0);
        long sum = 0;
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                if (list.get(i).equals(list.get(0))) {
                    sum += list.get(i);
                }
            }
            else if (list.get(i).equals(list.get(i + 1))) {
                sum += list.get(i);
            }
        }
        return Long.toString(sum);
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        List<Integer> list = input.get(0);
        long sum = 0;
        int pair = list.size() / 2;
        for (int i = 0; i < list.size(); i++) {
            if (pair == list.size()) {
                pair = 0;
            }
            if (list.get(i).equals(list.get(pair))) {
                sum += list.get(i);
            }
            pair++;
        }
        return Long.toString(sum);
    }

}
