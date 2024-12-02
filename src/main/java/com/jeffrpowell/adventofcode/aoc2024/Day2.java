package com.jeffrpowell.adventofcode.aoc2024;

import java.util.List;
import java.util.stream.Collectors;

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
            .filter(report -> goodDifferences(report, false) && (isDescending(report, false) || isAscending(report, false)))
            .count());
    }
    
    private boolean goodDifferences(List<Integer> input, boolean forgiveOne) {
        int last = input.get(0);
        boolean forgivenessUsed = !forgiveOne;
        for (int i = 1; i < input.size(); i++) {
            int diff = Math.abs(last - input.get(i));
            if (diff < 1 || diff > 3) {
                if (forgivenessUsed) {
                    return false;
                }
                else {
                    forgivenessUsed = true;
                    continue;
                }
            }
            last = input.get(i);
        }
        return true;
    }

    private boolean isDescending(List<Integer> input, boolean forgiveOne) {
        int last = input.get(0);
        boolean forgivenessUsed = !forgiveOne;
        for (int i = 1; i < input.size(); i++) {
            if (last < input.get(i)) {
                if (forgivenessUsed) {
                    return false;
                }
                else {
                    forgivenessUsed = true;
                    continue;
                }
            }
            last = input.get(i);
        }
        return true;
    }

    private boolean isAscending(List<Integer> input, boolean forgiveOne) {
        int last = input.get(0);
        boolean forgivenessUsed = !forgiveOne;
        for (int i = 1; i < input.size(); i++) {
            if (last > input.get(i)) {
                if (forgivenessUsed) {
                    return false;
                }
                else {
                    forgivenessUsed = true;
                    continue;
                }
            }
            last = input.get(i);
        }
        return true;
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        int safe = 0;
        for (List<Integer> report : input) {
            if (goodDifferences(report, true) && (isDescending(report, true) || isAscending(report, true))) {
                safe++;
            }
            else {
                List<Integer> tryWithoutFirst = report.stream().skip(1).collect(Collectors.toList());
                if (goodDifferences(tryWithoutFirst, true) && (isDescending(tryWithoutFirst, true) || isAscending(tryWithoutFirst, true))) {
                    safe++;
                }
            }
        }
        return Integer.toString(safe);
    }

}
