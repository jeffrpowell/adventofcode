package com.jeffrpowell.adventofcode.aoc2023;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day14 extends Solution2023<List<String>>{
    @Override
    public int getDay() {
        return 14;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        List<List<String>> pivotedInput = pivotList(input);
        long totalWeight = 0;
        for (int col = 0; col < pivotedInput.size(); col++) {
            List<String> column = pivotedInput.get(col);
            int nextOpenSpot = column.size();
            for (int row = 0; row < column.size(); row++) {
                if (column.get(row).equals("O")) {
                    totalWeight += nextOpenSpot;
                    nextOpenSpot--;
                }
                else if (column.get(row).equals("#")) {
                    nextOpenSpot = column.size() - row - 1;
                }
            }
        }
        return Long.toString(totalWeight);
    }

    @Override
    protected String part2(List<List<String>> input) {
        final int rightBoundary = input.get(0).size();
        final int bottomBoundary = input.size();
        Map<Point2D, RockType> grid = Point2DUtils.generateGrid(0, 0, rightBoundary, bottomBoundary, pt -> RockType.parse(input.get(d2i(pt.getY())).get(d2i(pt.getX()))));
        return "";
    }

    enum RockType{
        ROUND, SQUARE, BLANK;
        
        public static RockType parse(String s) {
            return switch (s) {
                case "." -> BLANK;
                case "#" -> SQUARE;
                default -> ROUND;
            };
        }
    }

    private int d2i(Double d) {
        return d.intValue();
    }

    public static List<List<String>> pivotList(List<List<String>> inputList) {
        List<List<String>> outputList = new ArrayList<>();

        int numRows = inputList.size();
        int numCols = inputList.get(0).size();

        for (int i = 0; i < numCols; i++) {
            List<String> newRow = new ArrayList<>();
            for (int j = 0; j < numRows; j++) {
                newRow.add(inputList.get(j).get(i));
            }
            outputList.add(newRow);
        }

        return outputList;
    }
}
