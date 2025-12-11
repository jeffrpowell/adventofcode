package com.jeffrpowell.adventofcode.aoc2025;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.algorithms.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day9 extends Solution2025<Rule>{
    @Override
    public int getDay() {
        return 9;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", "(\\d+,\\d+)");
    }

    @Override
    protected String part1(List<Rule> input) {
        List<Point2D> redPts = input.stream()
            .map(r -> r.getPoint2D(0))
            .collect(Collectors.toList());
        return Long.toString(runPart1(redPts));
    }

    private long runPart1(List<Point2D> redPts) {
        long largestArea = Long.MIN_VALUE;
        for (int i = 0; i < redPts.size() - 1; i++) {
            Point2D pt1 = redPts.get(i);
            for (int j = 0; j < redPts.size(); j++) {
                Point2D pt2 = redPts.get(j);
                long area = Double.valueOf(
                    Math.abs(pt1.getX() - pt2.getX() + 1)
                    * Math.abs(pt1.getY() - pt2.getY() + 1)
                ).longValue();
                largestArea = Math.max(largestArea, area);
            }
        }
        return largestArea;
    }

    @Override
    protected String part2(List<Rule> input) {
        List<Point2D> redPts = input.stream()
            .map(r -> r.getPoint2D(0))
            .collect(Collectors.toList());
        // print(redPts);
        // Massive spike runs horizontally across the middle of a large circle of points
        // Cut the circle in half and run part 1 on both halves
        // 94985,50114
        // 94985,48652
        List<Point2D> topRedPts = redPts.stream()
            .filter(pt -> pt.getY() < 50114)
            .collect(Collectors.toList());
        List<Point2D> bottomRedPts = redPts.stream()
            .filter(pt -> pt.getY() < 50114)
            .collect(Collectors.toList());
        // return Long.toString(Math.max(runPart1(topRedPts), runPart1(bottomRedPts)));
        // 3000499218 TOO HIGH
        // return Long.toString(runPart2(redPts));
        // 1574649700 TOO LOW
        return Long.toString(Math.max(runPart2(topRedPts), runPart2(bottomRedPts)));
    }

    private long runPart2(List<Point2D> redPts) {
        redPts.add(redPts.get(0));
        List<Rectangle> rects = new ArrayList<>();
        List<Pairing> polygonEdges = new ArrayList<>();
        for (int i = 0; i < redPts.size() - 1; i++) {
            Point2D pt1 = redPts.get(i);
            Point2D nextPt = redPts.get(i + 1);
            polygonEdges.add(new Pairing(pt1, nextPt));
            for (int j = 0; j < redPts.size(); j++) {
                Point2D pt2 = redPts.get(j);
                rects.add(new Rectangle(pt1, pt2));
            }
        }
        rects = rects.stream()
            .filter(rect -> polygonEdges.stream().noneMatch(edge -> rect.pathIntersectsInside(edge.pt1(), edge.pt2())))
            .collect(Collectors.toList());
        return rects.stream()
            .filter(r -> Point2DUtils.isPointInPolygon(r.center(), redPts))
            .map(Rectangle::area)
            .sorted(Comparator.reverseOrder())
            .findFirst().orElseThrow();
    }

    record Pairing(Point2D pt1, Point2D pt2){}

    record Rectangle(Point2D corner1, Point2D corner2, Point2DUtils.BoundingBox bb){

        public Rectangle(Point2D corner1, Point2D corner2) {
            this(corner1, corner2, Point2DUtils.getBoundingBox(List.of(corner1, corner2)));
        }

        public long area() {
            return Double.valueOf(
                Math.abs(corner1.getX() - corner2.getX() + 1)
                * Math.abs(corner1.getY() - corner2.getY() + 1)
            ).longValue();
        }

        public Point2D center() {
            return Point2DUtils.midpoint(corner1, corner2);
        }

        public boolean pathIntersectsInside(Point2D pt1, Point2D pt2) {
            boolean pt1Inside = Point2DUtils.pointInsideBoundary(pt1, false, bb);
            boolean pt2Inside = Point2DUtils.pointInsideBoundary(pt2, false, bb);
            if (pt1Inside || pt2Inside) {
                return pt1.distance(pt2) < 0.0001;
            }
            else {
                return true;
            }
            // version 1 returned in 717954 ms
            // return Point2DUtils.getPointsBetweenTwoPoints(pt1, pt2, false).stream()
            //     .anyMatch(pt -> Point2DUtils.pointInsideBoundary(pt, false, bb));
        }
    }

    @SuppressWarnings("unused")
    private static void print(List<Point2D> redCornerPts) {
        Set<Point2D> allSwappableBorderPts = new HashSet<>();
        for (int i = 0; i < redCornerPts.size() - 1; i++) {
            Point2D corner1 = redCornerPts.get(i);
            Point2D corner2 = redCornerPts.get(i+1);
            allSwappableBorderPts.addAll(Point2DUtils.getPointsBetweenTwoPoints(corner1, corner2, false));
        }
        // allSwappableBorderPts = allSwappableBorderPts.stream()
        //     .map(pt -> new Point2D.Double(pt.getX() - 50000, pt.getY() - 50000))
        //     .collect(Collectors.toSet());
        Point2DUtils.printPoints(allSwappableBorderPts);
    }
}
