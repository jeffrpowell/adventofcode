package com.jeffrpowell.adventofcode.aoc2024;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.SplitPartParser.Part;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day14 extends Solution2024<Part<Rule, Rule>>{
    private static final Point2D START = new Point2D.Double(0, 0);
    private static final Point2D END = new Point2D.Double(100, 102);
    private static Point2DUtils.BoundingBox bb;

    public Day14() {
        bb = new Point2DUtils.BoundingBox(START, END);
    }

    public Day14(Point2D _start, Point2D _end) {
        bb = new Point2DUtils.BoundingBox(_start, _end);
    }

    @Override
    public int getDay() {
        return 14;
    }

    //p=60,96 v=-71,-27
    @Override
    public InputParser<Part<Rule, Rule>> getInputParser() {
        return InputParserFactory.getSplitPartParser(Pattern.compile(" "), 
            InputParserFactory.getRuleParser("", Pattern.compile("p=(-?\\d+,-?\\d+)")), 
            InputParserFactory.getRuleParser("", Pattern.compile("v=(-?\\d+,-?\\d+)"))
        );
    }

    record Robot(Point2D pt, Point2D v) {}

    @Override
    protected String part1(List<Part<Rule, Rule>> input) {
        List<Robot> robots = input.stream()
            .map(part -> new Robot(part.firstPart().getPoint2D(0), part.secondPart().getPoint2D(0)))
            .map(r -> new Robot(transformPoint(r), r.v()))
            .collect(Collectors.toList());
        Point2DUtils.printPoints(robots.stream().map(Robot::pt).collect(Collectors.toList()));
        double xMid = bb.max().getX() / 2;
        double yMid = bb.max().getY() / 2;
        Map<Integer, Long> quadrants = robots.stream()
            .collect(Collectors.groupingBy(r -> {
                if (r.pt().getX() < xMid && r.pt().getY() < yMid) {
                    return 1;
                }
                else if (r.pt().getX() > xMid && r.pt().getY() < yMid) {
                    return 2;
                }
                else if (r.pt().getX() < xMid && r.pt().getY() > yMid) {
                    return 3;
                }
                else {
                    return 4;
                }
            }, Collectors.counting()));
        return Long.toString(quadrants.values().stream().reduce(1L, Math::multiplyExact));
    }

    private Point2D transformPoint(Robot r) {
        Point2D pt = r.pt();
        pt = Point2DUtils.applyVectorToPtNTimes(r.v(), r.pt(), 100);
        double newX;
        if (pt.getX() >= 0) {
            newX = pt.getX() % bb.max().getX();
        }
        else {
            /*
                # - grid size
                Absolute value
                Mod grid size
                Grid size - #
             */
            newX = bb.max().getX() - (Math.abs(pt.getX() - bb.max().getX()) % bb.max().getX());
        }
        double newY;
        if (pt.getY() >= 0) {
            newY = pt.getY() % bb.max().getY();
        }
        else {
            newY = bb.max().getY() - (Math.abs(pt.getY() - bb.max().getY()) % bb.max().getY());
        }
        return new Point2D.Double(newX, newY);
    }


    @Override
    protected String part2(List<Part<Rule, Rule>> input) {
        return part1(input);
    }
}
