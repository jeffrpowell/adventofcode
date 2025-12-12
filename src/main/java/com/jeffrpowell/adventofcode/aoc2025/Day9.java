package com.jeffrpowell.adventofcode.aoc2025;

import java.awt.geom.Point2D;
import java.util.List;
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
        return Long.toString(runPart2(redPts));
    }

    private long runPart2(List<Point2D> redPts) {
        int n = redPts.size();
        long bestArea = Long.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            Point2D corner1 = redPts.get(i);
            for (int j = 0; j < n; j++) {
                Point2D corner2 = redPts.get(j);
                long area = rectangleArea(corner1, corner2);
                if (area <= bestArea) {
                    continue;
                }

                Point2DUtils.BoundingBox bb = Point2DUtils.getBoundingBox(List.of(corner1, corner2));
                double minX = bb.min().getX();
                double maxX = bb.max().getX();
                double minY = bb.min().getY();
                double maxY = bb.max().getY();

                if (Math.abs(minX - maxX) < 0.0001 || Math.abs(minY - maxY) < 0.0001) {
                    continue;
                }

                boolean intersectsInterior = false;
                for (int e = 0; e < n; e++) {
                    Point2D a = redPts.get(e);
                    Point2D b = redPts.get((e + 1) % n);
                    if (segmentIntersectsOpenRectangleInterior(a, b, minX, maxX, minY, maxY)) {
                        intersectsInterior = true;
                        break;
                    }
                }
                if (intersectsInterior) {
                    continue;
                }

                Point2D center = Point2DUtils.midpoint(corner1, corner2);
                if (!Point2DUtils.isPointInPolygon(center, redPts)) {
                    continue;
                }

                bestArea = area;
            }
        }
        return bestArea;
    }

    private static long rectangleArea(Point2D corner1, Point2D corner2) {
        long width = Double.valueOf(Math.abs(corner1.getX() - corner2.getX()) + 1).longValue();
        long height = Double.valueOf(Math.abs(corner1.getY() - corner2.getY()) + 1).longValue();
        return width * height;
    }

    private static boolean segmentIntersectsOpenRectangleInterior(Point2D a, Point2D b, double minX, double maxX, double minY, double maxY) {
        double ax = a.getX();
        double ay = a.getY();
        double bx = b.getX();
        double by = b.getY();

        if (Math.abs(ax - bx) < 0.0001) {
            double x = ax;
            if (!(x > minX && x < maxX)) {
                return false;
            }
            double segMinY = Math.min(ay, by);
            double segMaxY = Math.max(ay, by);
            return segMinY < maxY && segMaxY > minY;
        }
        else if (Math.abs(ay - by) < 0.0001) {
            double y = ay;
            if (!(y > minY && y < maxY)) {
                return false;
            }
            double segMinX = Math.min(ax, bx);
            double segMaxX = Math.max(ax, bx);
            return segMinX < maxX && segMaxX > minX;
        }
        else {
            throw new IllegalArgumentException("Non-axis-aligned path segment: " + a + " -> " + b);
        }
    }
}
