package com.jeffrpowell.adventofcode.aoc2022;

import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.algorithms.Direction;
import com.jeffrpowell.adventofcode.algorithms.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day14 extends Solution2022<String>{

    private static final Point2D START = new Point2D.Double(500,0);
    private Deque<Point2D> dropPts = new ArrayDeque<>();
    @Override
    public int getDay() {
        return 14;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }

    @Override
    protected String part1(List<String> input) {
        Map<Point2D, Integer> grid = new HashMap<>();
        input.stream()
            .map(s -> Arrays.stream(s.split(" -> "))
                    .map(ptStr -> new Rule(List.of(ptStr), "", 0).getPoint2D(0))
                    .collect(Collectors.toList()))
            .forEach(ptStream -> {
                for (int i = 1; i < ptStream.size(); i++) {
                    Point2D lastPt = ptStream.get(i-1);
                    Point2D headPt = ptStream.get(i);
                    Point2D vector = new Point2D.Double(
                        Math.signum(headPt.getX()-lastPt.getX()),
                        Math.signum(headPt.getY()-lastPt.getY())
                    );
                    grid.put(lastPt, 1); //wall
                    while (!lastPt.equals(headPt)) {
                        lastPt = Point2DUtils.applyVectorToPt(vector, lastPt);
                        grid.put(lastPt, 1); //wall
                    }
                }
            });
        // Point2DUtils.printPoints(grid.keySet());
        double maxY = grid.keySet().stream().map(Point2D::getY).max(Comparator.naturalOrder()).get();
        dropPts.push(START);
        boolean done = false;
        do {
            done = sendSand(grid, maxY, true);
        }
        while (!done);
        // printFinalGrid(grid);
        return Long.toString(grid.values().stream().filter(i -> i == 2).count());
    }

    private void printFinalGrid(Map<Point2D, Integer> grid) {
        Map<Integer, Set<Point2D>> map = grid.entrySet().stream()
            .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toSet())));
        Map<Point2D, String> finalMap = new HashMap<>();
        map.get(1).stream().forEach(pt -> finalMap.put(pt, "#"));            
        map.get(2).stream().forEach(pt -> finalMap.put(pt, "o"));            
        Point2DUtils.printPoints(finalMap);
    }

    private boolean sendSand(Map<Point2D, Integer> grid, double maxY, boolean part1) {
        boolean atRest = false;
        Point2D sandPt = dropPts.peek();
        boolean immediateRest = true;
        while (!atRest) {
            Point2D explore = Point2DUtils.movePtInDirection(sandPt, Direction.DOWN, 1);
            if (part1 && explore.getY() > maxY) {
                return true;
            }
            if (explore.getY() < maxY + 2 && !grid.containsKey(explore)) {
                dropPts.push(sandPt);
                sandPt = explore;
                immediateRest = false;
                continue;
            }
            explore = Point2DUtils.movePtInDirection(sandPt, Direction.DOWN_LEFT, 1);
            if (explore.getY() < maxY + 2 && !grid.containsKey(explore)) {
                sandPt = explore;
                immediateRest = false;
                continue;
            }
            explore = Point2DUtils.movePtInDirection(sandPt, Direction.DOWN_RIGHT, 1);
            if (explore.getY() < maxY + 2 && !grid.containsKey(explore)) {
                sandPt = explore;
                immediateRest = false;
                continue;
            }
            if (immediateRest) {
                dropPts.pop();
                if (!part1 && dropPts.isEmpty()) {
                    return true;
                }
            }
            grid.put(sandPt, 2);
            atRest = true;
        }
        return false;
    }

    @Override
    protected String part2(List<String> input) {
        Map<Point2D, Integer> grid = new HashMap<>();
        input.stream()
            .map(s -> Arrays.stream(s.split(" -> "))
                    .map(ptStr -> new Rule(List.of(ptStr), "", 0).getPoint2D(0))
                    .collect(Collectors.toList()))
            .forEach(ptStream -> {
                for (int i = 1; i < ptStream.size(); i++) {
                    Point2D lastPt = ptStream.get(i-1);
                    Point2D headPt = ptStream.get(i);
                    Point2D vector = new Point2D.Double(
                        Math.signum(headPt.getX()-lastPt.getX()),
                        Math.signum(headPt.getY()-lastPt.getY())
                    );
                    grid.put(lastPt, 1); //wall
                    while (!lastPt.equals(headPt)) {
                        lastPt = Point2DUtils.applyVectorToPt(vector, lastPt);
                        grid.put(lastPt, 1); //wall
                    }
                }
            });
        // Point2DUtils.printPoints(grid.keySet());
        double maxY = grid.keySet().stream().map(Point2D::getY).max(Comparator.naturalOrder()).get();
        dropPts.push(START);
        boolean done = false;
        do {
            done = sendSand(grid, maxY, false);
        }
        while (!done);
        printFinalGrid(grid);
        return Long.toString(grid.values().stream().filter(i -> i == 2).count());
    }

}
