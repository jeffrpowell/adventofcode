package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day15 extends Solution2020<List<Integer>>{

    @Override
    public int getDay() {
        return 15;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerCSVParser();
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        List<Integer> theList = input.get(0);
        Map<Integer, Integer> seenNumbers = new HashMap<>();
        for (int i = 0; i < theList.size() - 1; i++) { //purposefully omit the last number
            seenNumbers.put(theList.get(i), i + 1);
        }
        int lastNumber = theList.get(theList.size() - 1);
        for (int i = theList.size(); i < 2020; i++) {
            if (seenNumbers.containsKey(lastNumber)) {
                int age = i - seenNumbers.get(lastNumber);
                seenNumbers.put(lastNumber, i);
                lastNumber = age;
            }
            else {
                seenNumbers.put(lastNumber, i);
                lastNumber = 0;
            }
        }
        return Integer.toString(lastNumber);
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        List<Integer> theList = input.get(0);
        Map<Integer, Integer> seenNumbers = new HashMap<>();
        for (int i = 0; i < theList.size() - 1; i++) { //purposefully omit the last number
            seenNumbers.put(theList.get(i), i + 1);
        }
        int lastNumber = theList.get(theList.size() - 1);
        for (int i = theList.size(); i < 30000000; i++) {
            if (seenNumbers.containsKey(lastNumber)) {
                int age = i - seenNumbers.get(lastNumber);
                seenNumbers.put(lastNumber, i);
                lastNumber = age;
            }
            else {
                seenNumbers.put(lastNumber, i);
                lastNumber = 0;
            }
        }
        return Integer.toString(lastNumber);
    }

}
