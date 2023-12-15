package com.jeffrpowell.adventofcode.aoc2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<List<RockType>> rockGrid = input.stream()
            .map(list -> list.stream().map(RockType::parse).collect(Collectors.toList()))
            .collect(Collectors.toList());
        return measureWeight(pivotList(rockGrid));
        
    }

    @Override
    protected String part2(List<List<String>> input) {
        List<List<RockType>> rockGrid = input.stream()
            .map(list -> list.stream().map(RockType::parse).collect(Collectors.toList()))
            .collect(Collectors.toList());
        rockGrid = pivotList(rockGrid);
        rockGrid = pivotList(rockGrid);
        Map<List<List<RockType>>, List<List<RockType>>> pivotCache = new HashMap<>();
        Map<List<List<RockType>>, List<List<RockType>>> gridRollCache = new HashMap<>();
        Map<List<List<RockType>>, String> measurementCache = new HashMap<>();
        Map<List<RockType>, List<RockType>> rollCache = new HashMap<>();
        for (int i = 0; i < 1_000_000_001; i++) {
            rockGrid = pivotCache.computeIfAbsent(rockGrid, this::pivotList);
            rockGrid = gridRollCache.computeIfAbsent(rockGrid, grid -> {
                List<List<RockType>> rolledGrid = grid.stream()
                    .map(list -> list.stream().collect(Collectors.toList()))
                    .collect(Collectors.toList());
                for (int col = 0; col < rolledGrid.size(); col++) {
                    rolledGrid.set(col, rollCache.computeIfAbsent(grid.get(col), this::rollLine));
                }
                return rolledGrid;
            });
            System.out.println(i + ": " + measurementCache.computeIfAbsent(rockGrid, this::measureWeight));
            // if (i > 1_000_000) {
                rockGrid.stream().forEach(line -> System.out.println(line.stream().map(RockType::toString).collect(Collectors.joining())));
                System.out.println();
            // }
        }
        return measureWeight(rockGrid);
    }

    private String measureWeight(List<List<RockType>> rockGrid) {
        long totalWeight = 0;
        for (int col = 0; col < rockGrid.size(); col++) {
            List<RockType> column = rockGrid.get(col);
            int nextOpenSpot = column.size();
            for (int row = 0; row < column.size(); row++) {
                if (column.get(row) == RockType.ROUND) {
                    totalWeight += nextOpenSpot;
                    nextOpenSpot--;
                }
                else if (column.get(row) == RockType.SQUARE) {
                    nextOpenSpot = column.size() - row - 1;
                }
            }
        }
        return Long.toString(totalWeight);
    }

    private List<RockType> rollLine(List<RockType> line) {
        int nextOpenSpot = 0;
        List<RockType> rolledLine = line.stream().collect(Collectors.toList());
        for (int row = 0; row < rolledLine.size(); row++) {
            if (rolledLine.get(row) == RockType.ROUND) {
                rolledLine.set(nextOpenSpot, RockType.ROUND);
                if (nextOpenSpot != row) {
                    rolledLine.set(row, RockType.BLANK);
                }
                nextOpenSpot++;
            }
            else if (rolledLine.get(row) == RockType.SQUARE) {
                nextOpenSpot = row + 1;
            }
        }
        return rolledLine;
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

        @Override
        public String toString() {
            if (this == ROUND) {
                return "O";
            }
            else if (this == SQUARE) {
                return "#";
            }
            else {
                return ".";
            }
        }
    }

    public <T> List<List<T>> pivotList(List<List<T>> inputList) {
        int numRows = inputList.size();
        int numCols = inputList.get(0).size();
        List<List<T>> rotatedList = Stream.generate(() -> new ArrayList<T>()).limit(numCols).collect(Collectors.toList());
        for (int rowI = numRows - 1; rowI >= 0; rowI--) {
            List<T> row = inputList.get(rowI);
            for (int colI = 0; colI < numCols; colI++) {
                rotatedList.get(colI).add(row.get(colI));
            }
        }

        return rotatedList;
    }
}
