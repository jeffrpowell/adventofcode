package com.jeffrpowell.adventofcode.aoc2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        rockGrid = rotateListClockwise(rockGrid);
        rockGrid = rotateListClockwise(rockGrid);
        rockGrid = rotateListClockwise(rockGrid);
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

    // 98894
    // Initial iterations before period encountered: 382 (I think?)
    // My full period:
    // 98887 98887 106853 106853 98886 98886 106857 106857 98898 98898 106863 106863 98899 98899 106864 106864 98889 98889 106858 106858 98890 98890
    // 106851 106851 98883 98883 106849 106849 98876 98876 106848 106848 98873 98873 106846 106846 98885 98885 106855 106855 98893 98893 106857 10685
    // 98888 98888 106854 106854 98885 98885 106856 106856 98899 98899 106864 106864 98898 98898 106863 106863 98890 98890 106859 106859 98889 98889
    // 106850 106850 98884 98884 106850 106850 98875 98875 106847 106847 98874 98874 106847 106847 98884 98884 106854 106854 98894 98894 106858 106858
    // Period start: 98874 98874 106847 106847
    @Override
    protected String part2(List<List<String>> input) {
        List<List<RockType>> rockGrid = input.stream()
            .map(list -> list.stream().map(RockType::parse).collect(Collectors.toList()))
            .collect(Collectors.toList());
        // rockGrid.stream().forEach(line -> System.out.println(line.stream().map(RockType::toString).collect(Collectors.joining())));
        // System.out.println();
        rockGrid = rotateListClockwise(rockGrid);
        rockGrid = rotateListClockwise(rockGrid);
        Map<List<List<RockType>>, List<List<RockType>>> rotateAndRollCache = new HashMap<>();
        Map<MeasurementKey, Long> measurementCache = new HashMap<>();
        Map<List<RockType>, List<RockType>> rollCache = new HashMap<>();
        // int printRotations = 1;
        int measurementRotations = 0;
        int rotationsSeenBefore = 0;
        long periodSample = -1;
        int iAtPeriodSample = -1;
        Set<Integer> periodTests = new HashSet<>();
        int period = -1;
        int rotationsLeft = -1;
        for (int i = 0; i < 1_000_000_000; i++) {
            if (!rotateAndRollCache.containsKey(rockGrid)) {
                rotationsSeenBefore = 0;
            }
            else {
                rotationsSeenBefore++;
            }
            rockGrid = rotateAndRollCache.computeIfAbsent(rockGrid, grid -> {
                grid = rotateListClockwise(grid);
                List<List<RockType>> rolledGrid = grid.stream()
                    .map(list -> list.stream().collect(Collectors.toList()))
                    .collect(Collectors.toList());
                for (int col = 0; col < rolledGrid.size(); col++) {
                    rolledGrid.set(col, rollCache.computeIfAbsent(grid.get(col), this::rollLine));
                }
                return rolledGrid;
            });
            long measurement = measurementCache.computeIfAbsent(new MeasurementKey(rockGrid, measurementRotations), key -> measureWeightPart2(key.rockGrid(), key.rotationsRequired()));
            if (rotationsLeft == 0 || (period > 0 && period > 80 && ((i - 382) % period) == 1)) {
                break;
            }
            else if (rotationsLeft > 0) {
                rotationsLeft--;
            }
            if (iAtPeriodSample == -1 && rotationsSeenBefore >= rotateAndRollCache.size() && (measurementRotations == 1 || measurementRotations == 3)){
                //we have experienced the full period
                //now to figure out the size of the period
                periodSample = measurement;
                iAtPeriodSample = i;
            }
            else if ((measurementRotations == 1 || measurementRotations == 3) && measurement == periodSample && rotationsLeft == -1) {
                if (!periodTests.add(i - iAtPeriodSample)) {
                    period = periodTests.stream().reduce(0, Math::addExact);
                    rotationsLeft = ((1_000_000_000 - i) % period) + 1;
                }
                iAtPeriodSample = i;
            }
            measurementRotations--;
            if (measurementRotations == -1) {
                measurementRotations = 3;
            }
            // List<List<RockType>> printGrid = rockGrid;
            // for (int rotations = 0; rotations < printRotations; rotations++) {
            //     printGrid = pivotList(printGrid);
            // }
            // printRotations--;
            // if (printRotations == -1) {
            //     printRotations = 3;
            // }
            // printGrid.stream().forEach(line -> System.out.println(line.stream().map(RockType::toString).collect(Collectors.joining())));
            // System.out.println();
        }
        return Long.toString(measurementCache.computeIfAbsent(new MeasurementKey(rockGrid, measurementRotations), key -> measureWeightPart2(key.rockGrid(), key.rotationsRequired())));
    }

    record MeasurementKey(List<List<RockType>> rockGrid, int rotationsRequired){}

    private long measureWeightPart2(List<List<RockType>> rockGrid, int rotationsRequired) {
        for (int i = 0; i < rotationsRequired; i++) {
            rockGrid = rotateListClockwise(rockGrid);
        }
        long totalWeight = 0;
        for (int col = 0; col < rockGrid.size(); col++) {
            List<RockType> column = rockGrid.get(col);
            for (int row = 0; row < column.size(); row++) {
                if (column.get(row) == RockType.ROUND) {
                    totalWeight += column.size() - row;
                }
            }
        }
        return totalWeight;
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

    public <T> List<List<T>> rotateListClockwise(List<List<T>> inputList) {
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
