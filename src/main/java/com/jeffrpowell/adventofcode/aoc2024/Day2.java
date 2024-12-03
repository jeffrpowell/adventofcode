package com.jeffrpowell.adventofcode.aoc2024;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day2 extends Solution2024<List<Integer>>{
    @Override
    public int getDay() {
        return 2;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser(" ");
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        return Long.toString(input.stream()
            .filter(report -> goodDifferences(report) && (isDescending(report) || isAscending(report)))
            .count());
    }
    
    private boolean goodDifferences(List<Integer> input) {
        int last = input.get(0);
        for (int i = 1; i < input.size(); i++) {
            int diff = Math.abs(last - input.get(i));
            if (diff < 1 || diff > 3) {
                return false;
            }
            last = input.get(i);
        }
        return true;
    }

    private boolean isDescending(List<Integer> input) {
        int last = input.get(0);
        for (int i = 1; i < input.size(); i++) {
            if (last < input.get(i)) {
                return false;
            }
            last = input.get(i);
        }
        return true;
    }

    private boolean isAscending(List<Integer> input) {
        int last = input.get(0);
        for (int i = 1; i < input.size(); i++) {
            if (last > input.get(i)) {
                return false;
            }
            last = input.get(i);
        }
        return true;
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        int safe = 0;
        for (List<Integer> report : input) {
            if (goodDifferences(report) && (isDescending(report) || isAscending(report))) {
                safe++;
            }
            else if (tryRemovingLevels(report)){
                safe++;
            }
        }
        return Integer.toString(safe);
    }

    private boolean tryRemovingLevels(List<Integer> report) {
        for (int i = 0; i < report.size(); i++) {
            final int reportI = i;
            List<Integer> newReport = IntStream.range(0, report.size())
                .filter(streamI -> streamI != reportI)
                .map(report::get)
                .collect(ArrayList::new, List::add, List::addAll);
            if (goodDifferences(newReport) && (isDescending(newReport) || isAscending(newReport))) {
                return true;
            }
        }
        return false;
    }

}
