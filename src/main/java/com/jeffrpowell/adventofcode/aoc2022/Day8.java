package com.jeffrpowell.adventofcode.aoc2022;

import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day8 extends Solution2022<List<Integer>>{
    private static final Point2D VECTOR_DOWN = new Point2D.Double(0,1);
    private static final Point2D VECTOR_RIGHT = new Point2D.Double(1,0);
    
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
        Deque<Point2D> q = new ArrayDeque<>();
        int width = input.get(0).size();
        int height = input.size();
        Map<Point2D, Integer> grid = Point2DUtils.generateGrid(0, 0, width, height)
            .collect(Collectors.toMap(
                Function.identity(),
                p -> input.get(d2i(p.getY())).get(d2i(p.getX()))
            ));
        int visibleTrees = width * 2 + height * 2 - 4; //count perimeter
        Point2DUtils.generateGrid(1, 1, width - 1, height - 1).forEach(q::add);
        Set<Point2D> visited = new HashSet<>();
        
        while (!q.isEmpty()) {
            Point2D p = q.pop();
            if (visited.contains(p)) {
                continue;
            }
            VisibleResponse r = isVisible(grid, p, width, height);
            if (r != VisibleResponse.NOT) {
                visibleTrees++;
                List<Point2D> ptsToAdd = new ArrayList<>();
                switch (r) {
                    case VERTICAL -> ptsToAdd.add(Point2DUtils.applyVectorToPt(VECTOR_DOWN, p));
                    case HORIZONTAL -> ptsToAdd.add(Point2DUtils.applyVectorToPt(VECTOR_RIGHT, p));
                    case BOTH -> {
                        ptsToAdd.add(Point2DUtils.applyVectorToPt(VECTOR_DOWN, p));
                        ptsToAdd.add(Point2DUtils.applyVectorToPt(VECTOR_RIGHT, p));
                    }
                }
                ptsToAdd.stream()
                    .filter(pt -> pt.getX() < width - 1 && pt.getY() < height - 1)
                    .forEach(q::add);
            }
            visited.add(p);
        }
        return Integer.toString(visibleTrees);
    }

    private int d2i(Double d) {
        return d.intValue();
    }

    private enum VisibleResponse {VERTICAL,HORIZONTAL,BOTH,NOT;}

    private VisibleResponse isVisible(Map<Point2D, Integer> grid, Point2D tree, int width, int height) {
        int treeHeight = grid.get(tree);
        boolean visibleV = false;
        if (tree.getY() < height / 2) {
            visibleV = Point2DUtils.getPointsFromSource(tree, 0, tree.getX(), tree.getY() - 1, tree.getX(), true, false)
                .get(Direction.UP).stream()
                .map(grid::get)
                .allMatch(h -> h < treeHeight);
        }
        else {
            visibleV = Point2DUtils.getPointsFromSource(tree, tree.getY()+1, tree.getX(), height - 1, tree.getX(), true, false)
                .get(Direction.DOWN).stream()
                .map(grid::get)
                .allMatch(h -> h < treeHeight);
        }
        boolean visibleH = false;
        if (tree.getX() < width / 2) {
            visibleH = Point2DUtils.getPointsFromSource(tree, tree.getY(), tree.getX()-1, tree.getY(), 0, true, false)
                .get(Direction.LEFT).stream()
                .map(grid::get)
                .allMatch(h -> h < treeHeight);
        }
        else {
            visibleH = Point2DUtils.getPointsFromSource(tree, tree.getY(), width - 1, tree.getY(), tree.getX()+1, true, false)
                .get(Direction.RIGHT).stream()
                .map(grid::get)
                .allMatch(h -> h < treeHeight);
        }
        if (visibleV && visibleH) {
            return VisibleResponse.BOTH;
        }
        if (visibleV) {
            return VisibleResponse.VERTICAL;
        }
        if (visibleH) {
            return VisibleResponse.HORIZONTAL;
        }
        return VisibleResponse.NOT;
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        return null;
    }

}
