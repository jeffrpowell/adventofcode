package com.jeffrpowell.adventofcode.aoc2022;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.Grid;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day8 extends Solution2022<List<Integer>>{
    private static final Point2D VECTOR_DOWN = new Point2D.Double(0,1);
    private static final Point2D VECTOR_RIGHT = new Point2D.Double(1,0);
    private static final Point2D VECTOR_LEFT = new Point2D.Double(-1,0);
    private static final Point2D VECTOR_UP = new Point2D.Double(0,-1);
    
    @Override
    public int getDay() {
        return 8;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser("");
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        int width = input.get(0).size();
        int height = input.size();
        Grid<Integer> grid = new Grid<>(input);
        Set<Point2D> visible = new HashSet<>();
        for (int x = 0; x < width; x++) {
            //TOP-DOWN
            Point2D entry = new Point2D.Double(x, 0);
            visible.add(entry);
            int lastBiggestSize = grid.get(entry);
            entry = Point2DUtils.applyVectorToPt(VECTOR_DOWN, entry);
            while (entry.getY() < height) {
                if (grid.get(entry) > lastBiggestSize) {
                    visible.add(entry);
                    lastBiggestSize = grid.get(entry);
                }
                entry = Point2DUtils.applyVectorToPt(VECTOR_DOWN, entry);
            }
            //BOTTOM-UP
            entry = new Point2D.Double(x, height - 1);
            visible.add(entry);
            lastBiggestSize = grid.get(entry);
            entry = Point2DUtils.applyVectorToPt(VECTOR_UP, entry);
            while (entry.getY() > -1) {
                if (grid.get(entry) > lastBiggestSize) {
                    visible.add(entry);
                    lastBiggestSize = grid.get(entry);
                }
                entry = Point2DUtils.applyVectorToPt(VECTOR_UP, entry);
            }
        }
        for (int y = 0; y < height; y++) {
            //LEFT-RIGHT
            Point2D entry = new Point2D.Double(0, y);
            visible.add(entry);
            int lastBiggestSize = grid.get(entry);
            entry = Point2DUtils.applyVectorToPt(VECTOR_RIGHT, entry);
            while (entry.getX() < width) {
                if (grid.get(entry) > lastBiggestSize) {
                    visible.add(entry);
                    lastBiggestSize = grid.get(entry);
                }
                entry = Point2DUtils.applyVectorToPt(VECTOR_RIGHT, entry);
            }
            //RIGHT-LEFT
            entry = new Point2D.Double(width - 1, y);
            visible.add(entry);
            lastBiggestSize = grid.get(entry);
            entry = Point2DUtils.applyVectorToPt(VECTOR_LEFT, entry);
            while (entry.getX() > -1) {
                if (grid.get(entry) > lastBiggestSize) {
                    visible.add(entry);
                    lastBiggestSize = grid.get(entry);
                }
                entry = Point2DUtils.applyVectorToPt(VECTOR_LEFT, entry);
            }
        }
        return Integer.toString(visible.size());
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        int width = input.get(0).size();
        int height = input.size();
        Grid<Integer> grid = new Grid<>(input);
        return Long.toString(grid.entrySet().stream()
            .map(e -> calcScenicScore(e, grid, width, height))
            .max(Comparator.naturalOrder()).get());
    }

    private long calcScenicScore(Map.Entry<Point2D, Integer> tree, Map<Point2D, Integer> grid, int width, int height) {
        return Point2DUtils.getPointsFromSource(tree.getKey(), 0, width-1, height-1, 0, true, false)
            .values().stream()
            .map(listOfPts -> listOfPts.stream().map(grid::get).collect(Collectors.toList()))
            .map(listOfHeights -> treesICanSee(tree.getValue(), listOfHeights))
            .collect(Collectors.reducing(1L, Math::multiplyExact));
    }

    private long treesICanSee(Integer mySize, List<Integer> otherTrees) {
        for (int i = 0; i < otherTrees.size(); i++) {
            if (otherTrees.get(i) >= mySize) {
                return i + 1L;
            }
        }
        return otherTrees.size();
    }
}
