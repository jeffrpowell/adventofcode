package com.jeffrpowell.adventofcode.aoc2023;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day18 extends Solution2023<Rule>{

    @Override
    public int getDay() {
        return 18;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("(\\w) (\\d+) \\(#(\\w\\w\\w\\w\\w)(\\d)\\)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        Point2D lastPt = new Point2D.Double(0, 0);
        List<Point2D> polygonCorners = new ArrayList<>();
        Set<Point2D> insideSet = new HashSet<>();
        polygonCorners.add(lastPt);
        for (Rule rule : input) {
            Direction d = parseDirection(rule.getString(0));
            int distance = rule.getInt(1);
            Point2DUtils.repeatVectorToPtNTimes(d.asVector(), lastPt, distance).forEach(insideSet::add);
            lastPt = d.travelFromNTimes(lastPt, distance);
            polygonCorners.add(lastPt);
        }
        long inside = 0;
        Point2DUtils.BoundingBox box = Point2DUtils.getBoundingBox(polygonCorners);
        for (double row = box.min().getY(); row <= box.max().getY(); row++) {
            for (double col = box.min().getX(); col <= box.max().getX(); col++) {
                Point2D pt = new Point2D.Double(col, row);
                if (insideSet.contains(pt) || Point2DUtils.isPointInPolygon(pt, polygonCorners)) {
                    inside++;
                }
            }
        }
        return Long.toString(inside);
    }

    @Override
    protected String part2(List<Rule> input) {
        Point2D lastPt = new Point2D.Double(0, 0);
        List<Point2D> polygonCorners = new ArrayList<>();
        Set<Point2D> insideSet = new HashSet<>();
        polygonCorners.add(lastPt);
        for (Rule rule : input) {
            Direction d = parseDirection(rule.getInt(3));
            String distanceHex = rule.getString(2);
            int distance = Integer.parseInt(distanceHex, 16);
            Point2DUtils.repeatVectorToPtNTimes(d.asVector(), lastPt, distance).forEach(insideSet::add);
            lastPt = d.travelFromNTimes(lastPt, distance);
            polygonCorners.add(lastPt);
        }
        long inside = 0;
        Point2DUtils.BoundingBox box = Point2DUtils.getBoundingBox(polygonCorners);
        for (double row = box.min().getY(); row <= box.max().getY(); row++) {
            for (double col = box.min().getX(); col <= box.max().getX(); col++) {
                Point2D pt = new Point2D.Double(col, row);
                if (insideSet.contains(pt) || Point2DUtils.isPointInPolygon(pt, polygonCorners)) {
                    inside++;
                }
            }
        }
        return Long.toString(inside);
    }

    private Direction parseDirection(String s) {
        return switch (s) {
            case "D" -> Direction.DOWN;
            case "R" -> Direction.RIGHT;
            case "L" -> Direction.LEFT;
            default -> Direction.UP;
        };
    }

    private Direction parseDirection(int i) {
        return switch (i) {
            case 0 -> Direction.RIGHT;
            case 1 -> Direction.DOWN;
            case 2 -> Direction.LEFT;
            default -> Direction.UP;
        };
    }
}
