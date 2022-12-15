package com.jeffrpowell.adventofcode.aoc2022;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day14 extends Solution2022<String>{

    private static final Point2D START = new Point2D.Double(500,0);
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
        boolean done = false;
        do {
            done = sendSand(grid);
        }
        while (!done);
        return Long.toString(grid.values().stream().filter(i -> i == 2).count());
    }

    private boolean sendSand(Map<Point2D, Integer> grid) {
        return true;
    }

    @Override
    protected String part2(List<String> input) {
        return null;
    }

}
