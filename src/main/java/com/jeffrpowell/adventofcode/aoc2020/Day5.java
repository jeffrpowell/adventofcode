package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day5 extends Solution2020<String>{

    @Override
    public int getDay() {
        return 5;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }

    @Override
    protected String part1(List<String> input) {
        return Integer.toString(input.stream().map(Day5::findSeatId).max(Integer::compare).get());
    }
    
    private static int findSeatId(String pass) {
        int rowF = 0;
        int rowB = 127;
        int colL = 0;
        int colR = 7;
        for (int i = 0; i < 7; i++) {
            if (pass.charAt(i) == 'F') {
                rowB -= (rowB - rowF) / 2 + 1;
            }
            else {
                rowF += (rowB - rowF) / 2 + 1;
            }
        }
        for (int i = 7; i < 10; i++) {
            if (pass.charAt(i) == 'L') {
                colR -= (colR - colL) / 2 + 1;
            }
            else {
                colL += (colR - colL) / 2 + 1;
            }
        }
        return 8 * rowF + colL;
    }

    @Override
    protected String part2(List<String> input) {
        Set<Integer> seatIds = input.stream().map(Day5::findSeatId).collect(Collectors.toCollection(HashSet::new));
        for (int i = 45; i < 815; i++) { //45 was min seat id; 816 was max seat id
            if (!seatIds.contains(i + 1)) {
                return Integer.toString(i + 1);
            }
        }
        return "Missed it";
    }

}
