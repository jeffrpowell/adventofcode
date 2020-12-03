package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.Arrays;
import java.util.List;

public class Day3 extends Solution2020<String>{

    @Override
    public int getDay() {
        return 3;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }

    @Override
    protected String part1(List<String> input) {
        int x = 0;
        int trees = 0;
        int width = input.get(0).length();
        for (String row : input) {
            char c = row.charAt(x);
            if (c == '#') { 
                trees++;
            }
            x = (x + 3) % width;
        }
        return Integer.toString(trees);
    }

    @Override
    protected String part2(List<String> input) {
        int[] x = new int[]{0, 0, 0, 0, 0};
        long[] trees = new long[]{0, 0, 0, 0, 0};
        int width = input.get(0).length();
        for (int rowI = 0; rowI < input.size(); rowI++) {
            String row = input.get(rowI);
            for (int i = 0; i < 5; i++) {
                if (i != 4) {
                    if (treeHit(row, x[i])) {
                        trees[i] = trees[i] + 1;
                    }
                    x[i] = (x[i] + 1 + 2 * i) % width;
                }
                else if (i == 4 && rowI % 2 == 0) {
                    if (treeHit(row, x[i])) {
                        trees[4] = trees[4] + 1;
                    }
                    x[i] = (x[i] + 1) % width;
                }
            }
        }
        return Long.toString(Arrays.stream(trees).reduce(1, Math::multiplyExact));
    }
    
    private static boolean treeHit(String row, int x) {
        char c = row.charAt(x);
        return c == '#';
    }

}
