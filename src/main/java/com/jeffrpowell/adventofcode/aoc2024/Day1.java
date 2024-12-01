package com.jeffrpowell.adventofcode.aoc2024;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day1 extends Solution2024<List<Integer>>{
    @Override
    public int getDay() {
        return 1;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser("   ");
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();
        for (List<Integer> row : input) {
            leftList.add(row.get(0));
            rightList.add(row.get(1));
        }
        leftList.sort(Comparator.naturalOrder());
        rightList.sort(Comparator.naturalOrder());
        long distances = 0L;
        for (int i = 0; i < leftList.size(); i++) {
            distances += Math.abs(rightList.get(i) - leftList.get(i));
        }
        return Long.toString(distances);
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        List<Integer> leftList = new ArrayList<>();
        Map<Integer, Long> rightMap = new HashMap<>();
        for (List<Integer> row : input) {
            leftList.add(row.get(0));
            int rightNum = row.get(1);
            rightMap.putIfAbsent(rightNum, 0L);
            rightMap.put(rightNum, rightMap.get(rightNum) + 1);
        }
        return Long.toString(leftList.stream()
            .map(leftNum -> leftNum * rightMap.getOrDefault(leftNum, 0L))
            .reduce(0L, Math::addExact));
    }

}
