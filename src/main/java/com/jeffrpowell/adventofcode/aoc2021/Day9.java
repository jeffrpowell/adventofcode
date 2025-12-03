package com.jeffrpowell.adventofcode.aoc2021;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.algorithms.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day9 extends Solution2021<List<Integer>>{

    @Override
    public int getDay() {
        return 9;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser("");
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        Map<Point2D, Integer> grid = new HashMap<>();
        for (int row = 0; row < input.size(); row++) {
            for (int col = 0; col < input.get(0).size(); col++) {
                grid.put(new Point2D.Double(col, row), input.get(row).get(col));
            }
        }

        return Integer.toString(grid.entrySet().stream()
            .filter(pt -> isLow(pt, grid, input.size() - 1, input.get(0).size() - 1))
            .map(Map.Entry::getValue)
            .map(i -> i + 1)
            .reduce(0, Math::addExact));
    }

    private boolean isLow(Map.Entry<Point2D, Integer> entry, Map<Point2D, Integer> grid, int maxRows, int maxCols) {
        return Point2DUtils.getBoundedAdjacentPts(entry.getKey(), 0, maxCols, maxRows, 0, true, false).stream()
            .map(grid::get)
            .allMatch(neighbor -> entry.getValue() < neighbor);
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        Map<Point2D, Integer> grid = new HashMap<>();
        int maxRows = input.size();
        int maxCols = input.get(0).size();
        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < maxCols; col++) {
                grid.put(new Point2D.Double(col, row), input.get(row).get(col));
            }
        }

        Set<Point2D> sinks = grid.entrySet().stream()
            .filter(pt -> isLow(pt, grid, input.size() - 1, input.get(0).size() - 1))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
        
        return Integer.toString(sinks.stream()
            .map(pt -> getBasinNeighbors(pt, Stream.of(pt).collect(Collectors.toSet()), grid, maxRows - 1, maxCols - 1))
            .sorted(Comparator.comparing(Set<Point2D>::size).reversed())
            .limit(3)
            .map(Set::size)
            .reduce(1, Math::multiplyExact)
        );
    }

    private Set<Point2D> getBasinNeighbors(Point2D pt, Set<Point2D> basin, Map<Point2D, Integer> grid, int maxRows, int maxCols) {
        Point2DUtils.getBoundedAdjacentPts(pt, 0, maxCols, maxRows, 0, true, false).stream()
            .filter(neighbor -> grid.get(neighbor) != 9)
            .forEach(neighbor -> {
                if (basin.add(neighbor)) {
                    getBasinNeighbors(neighbor, basin, grid, maxRows, maxCols);
                }
            });
        return basin;
    }
    
}
