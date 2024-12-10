package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.Grid;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day11 extends Solution2020<String> {

    private static int width = 0;
    private static int height = 0;
    private static boolean changed = false;
    
    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }

    @Override
    protected String part1(List<String> input) {
        height = input.size();
        width = input.get(0).length();
        Map<Point2D, Character> grid = Grid.generatePointStream(0, 0, width, height).collect(Collectors.toMap(
            Function.identity(),
            pt -> input.get(Double.valueOf(pt.getY()).intValue()).charAt(Double.valueOf(pt.getX()).intValue())
        ));
        do {
            changed = false;
            grid = nextState(grid);
        } while (changed);
        return Long.toString(grid.values().stream().filter(c -> c == '#').count());
    }
    
    private static Map<Point2D, Character> nextState(Map<Point2D, Character> grid) {
        return grid.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> {
                    if (entry.getValue() == '.') {
                        return '.';
                    }
                    else {
                        char toggledChar = entry.getValue() == '#' ? 'L' : '#';
                        long occupiedNeighbors = Point2DUtils.getBoundedAdjacentPts(entry.getKey(), 0, width - 1, height - 1, 0, true, true).stream()
                            .filter(neighbor -> grid.get(neighbor) == '#')
                            .count();
                        if (entry.getValue() == '#' && occupiedNeighbors >= 4 || entry.getValue() == 'L' && occupiedNeighbors == 0) {
                            changed = true;
                            return toggledChar;
                        }
                        else {
                            return entry.getValue();
                        }
                    }
                    
                }
            ));
    }

    @Override
    protected String part2(List<String> input) {
        height = input.size();
        width = input.get(0).length();
        Map<Point2D, Character> grid = Grid.generatePointStream(0, 0, width, height).collect(Collectors.toMap(
            Function.identity(),
            pt -> input.get(Double.valueOf(pt.getY()).intValue()).charAt(Double.valueOf(pt.getX()).intValue())
        ));
        do {
            changed = false;
            grid = nextStatePt2(grid);
        } while (changed);
        return Long.toString(grid.values().stream().filter(c -> c == '#').count());
    }

    private static Map<Point2D, Character> nextStatePt2(Map<Point2D, Character> grid) {
        return grid.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                oldEntry -> {
                    if (oldEntry.getValue() == '.') {
                        return '.';
                    }
                    else {
                        char toggledChar = oldEntry.getValue() == '#' ? 'L' : '#';
                        long occupiedNeighbors = Point2DUtils.getPointsFromSource(oldEntry.getKey(), 0, width - 1, height - 1, 0, true, true).entrySet().stream()
                            .peek(entry -> entry.getValue().removeIf(pt -> grid.get(pt) == '.'))
                            .filter(entry -> !entry.getValue().isEmpty())
                            .filter(entry -> grid.get(entry.getValue().get(0)) == '#')
                            .count();
                        if (oldEntry.getValue() == '#' && occupiedNeighbors >= 5 || oldEntry.getValue() == 'L' && occupiedNeighbors == 0) {
                            changed = true;
                            return toggledChar;
                        }
                        else {
                            return oldEntry.getValue();
                        }
                    }
                    
                }
            ));
    }
}
