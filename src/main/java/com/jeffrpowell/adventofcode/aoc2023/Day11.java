package com.jeffrpowell.adventofcode.aoc2023;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;


public class Day11 extends Solution2023<List<String>>{
    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        final int rightBoundary = input.get(0).size();
        final int bottomBoundary = input.size();
        Map<Point2D, Boolean> grid = Point2DUtils.generateGrid(0, 0, rightBoundary, bottomBoundary, pt -> input.get(d2i(pt.getY())).get(d2i(pt.getX())).equals("#"));
        List<Point2D> galaxies = expandUniverse(rightBoundary, bottomBoundary, grid, 1);
        long distance = 0;
        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = i + 1; j < galaxies.size(); j++) {
                distance += Point2DUtils.getManhattenDistance(galaxies.get(i), galaxies.get(j));
            }
        }
        return Long.toString(distance);
    }

    @Override
    protected String part2(List<List<String>> input) {
        final int rightBoundary = input.get(0).size();
        final int bottomBoundary = input.size();
        Map<Point2D, Boolean> grid = Point2DUtils.generateGrid(0, 0, rightBoundary, bottomBoundary, pt -> input.get(d2i(pt.getY())).get(d2i(pt.getX())).equals("#"));
        List<Point2D> galaxies = expandUniverse(rightBoundary, bottomBoundary, grid, 1_000_000L);
        long distance = 0;
        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = i + 1; j < galaxies.size(); j++) {
                distance += Point2DUtils.getManhattenDistance(galaxies.get(i), galaxies.get(j));
            }
        }
        return Long.toString(distance);
    }

    private int d2i(Double d) {
        return d.intValue();
    }

    /**
     * 
     * @param width
     * @param height
     * @param grid
     * @return Just returns the transformed set of galaxy points; all other points are implied to be empty space
     */
    private List<Point2D> expandUniverse(int width, int height, Map<Point2D, Boolean> grid, long expansionAmount) {
        List<Integer> colsToExpand = new ArrayList<>();
        for (int col = 0; col < width; col++) {
            boolean allEmpty = true;
            for (Point2D runner = new Point2D.Double(col, 0); runner.getY() < height; runner = Direction.DOWN.travelFrom(runner)) {
                if (grid.get(runner)) {
                    allEmpty = false;
                    break;
                }
            }
            if (allEmpty) {
                colsToExpand.add(col);
            }
        }
        List<Integer> rowsToExpand = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            boolean allEmpty = true;
            for (Point2D runner = new Point2D.Double(0, row); runner.getX() < width; runner = Direction.RIGHT.travelFrom(runner)) {
                if (grid.get(runner)) {
                    allEmpty = false;
                    break;
                }
            }
            if (allEmpty) {
                rowsToExpand.add(row);
            }
        }
        return grid.entrySet().stream()
            .filter(e -> e.getValue())
            .map(Map.Entry::getKey)
            .map(pt -> {
                long xModifier = colsToExpand.stream().filter(col -> col < pt.getX()).count() * Math.max(1, expansionAmount - 1);
                long yModifier = rowsToExpand.stream().filter(row -> row < pt.getY()).count() * Math.max(1, expansionAmount - 1);
                return Point2DUtils.applyVectorToPt(new Point2D.Double(xModifier, yModifier), pt);
            })
            .collect(Collectors.toList());
    }
}
