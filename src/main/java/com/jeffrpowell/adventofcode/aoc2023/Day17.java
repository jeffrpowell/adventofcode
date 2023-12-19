package com.jeffrpowell.adventofcode.aoc2023;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day17 extends Solution2023<List<Integer>>{

    private static Point2D START = new Point2D.Double(0, 0);
    private static Point2D END;
    @Override
    public int getDay() {
        return 17;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser("");
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        final int rightBoundary = input.get(0).size();
        final int bottomBoundary = input.size();
        END = new Point2D.Double(rightBoundary - 1, bottomBoundary - 1);
        Map<Point2D, Integer> grid = Point2DUtils.generateGrid(0, 0, rightBoundary, bottomBoundary, pt -> input.get(d2i(pt.getY())).get(d2i(pt.getX())));
        PriorityQueue<Path> q = new PriorityQueue<>(Comparator.comparing(Path::heuristic));
        Set<Point2D> visited = new HashSet<>();
        Point2DUtils.BoundingBox innerBox = Point2DUtils.getBoundingBox(grid.entrySet().stream()
            .filter(e -> e.getValue() >= 7)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList())
        );
        //unwinding once to avoid a conditional in the while loop
        visited.add(START);
        Point2D next = Direction.RIGHT.travelFrom(START);
        q.add(new Path(next, grid.get(next), Direction.RIGHT, 1));
        next = Direction.DOWN.travelFrom(START);
        q.add(new Path(next, grid.get(next), Direction.DOWN, 1));
        while(!q.isEmpty()) {
            Path p = q.poll();
            visited.add(p.tip);
            Direction lastDir = p.lastDirection;
            if (p.tip.equals(END)) {
                return Integer.toString(p.cost);
            }
            Set<Point2D> neighbors = Point2DUtils.getBoundedAdjacentPts(p.tip, -1, rightBoundary, bottomBoundary, -1, false, false).stream()
                .filter(neighbor -> !visited.contains(neighbor))
                .filter(neighbor -> !Point2DUtils.pointInsideBoundary(neighbor, true, innerBox))
                .collect(Collectors.toSet());
            Point2D nextPt = lastDir.travelFrom(p.tip);
            if (p.sameDirectionCount < 3 && neighbors.contains(nextPt)) {
                q.add(new Path(nextPt, p.cost + grid.get(nextPt), lastDir, p.sameDirectionCount + 1));
            }
            for (int i = 0; i < 3; i++) {
                lastDir = lastDir.rotateRight90();
                nextPt = lastDir.travelFrom(p.tip);
                if (neighbors.contains(nextPt)) {
                    q.add(new Path(nextPt, p.cost + grid.get(nextPt), lastDir, 1));
                }
            }
        }
        return "-1";
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        final int rightBoundary = input.get(0).size();
        final int bottomBoundary = input.size();
        END = new Point2D.Double(rightBoundary - 1, bottomBoundary - 1);
        Map<Point2D, Integer> grid = Point2DUtils.generateGrid(0, 0, rightBoundary, bottomBoundary, pt -> input.get(d2i(pt.getY())).get(d2i(pt.getX())));
        return "";
    }

    private int d2i(Double d) {
        return d.intValue();
    }

    record Path(Point2D tip, int cost, Direction lastDirection, int sameDirectionCount){
        double heuristic() {
            return cost + tip.distance(END);
        }
    }
}
