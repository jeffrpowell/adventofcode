package com.jeffrpowell.adventofcode.aoc2018.solutions;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day17 implements Solution<String>
{
    private static final Point2D WATER_SPRING = new Point2D.Double(500, 0);
    @Override
    public String part1(List<String> input) {
        List<PointRange> clayPointRanges = input.stream().map(PointRange::parse).collect(Collectors.toList());
        Point2D topLeft = clayPointRanges.stream().map(PointRange::getStart).reduce(WATER_SPRING, (minPt, pt) -> {
           double minX = Math.min(minPt.getX(), pt.getX()); 
           double minY = Math.min(minPt.getY(), pt.getY()); 
           return new Point2D.Double(minX, minY);
        });
        Point2D bottomRight = clayPointRanges.stream().map(PointRange::getStart).reduce(WATER_SPRING, (maxPt, pt) -> {
           double maxX = Math.max(maxPt.getX(), pt.getX()); 
           double maxY = Math.max(maxPt.getY(), pt.getY()); 
           return new Point2D.Double(maxX, maxY);
        });
        StringBuilder builder = new StringBuilder();
        for (double y = topLeft.getY(); y <= bottomRight.getY(); y++) {
            for (double x = topLeft.getX(); x <= bottomRight.getX(); x++) {
                Point2D pt = new Point2D.Double(x, y);
                if (pt.equals(WATER_SPRING)) {
                    builder.append("+");
                }
                else {
                    boolean foundClay = clayPointRanges.stream().anyMatch(pointRange -> pointRange.containsPt(pt));
                    builder.append(foundClay ? "#" : ".");
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    public String part2(List<String> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private static class PointRange{
        private static final Pattern REGEX = Pattern.compile("(x|y)=(\\d+)(?:\\.\\.(\\d+))?");
        Point2D start;
        Point2D end;

        private PointRange(Point2D start, Point2D end) {
            this.start = start;
            this.end = end;
        }
        
        public static PointRange parse(String s) {
            Matcher m = REGEX.matcher(s);
            m.find();
            if (m.group(1).equals("x")) {
                return PointRange.overY(m);
            }
            else {
                return PointRange.overX(m);
            }
        }
        
        private static PointRange overX(Matcher m) {
            double y = d(m.group(2));
            m.find();
            double x1 = d(m.group(2));
            double x2 = d(m.group(3));
            Point2D start = new Point2D.Double(x1, y);
            Point2D end = new Point2D.Double(x2, y);
            return new PointRange(start, end);
        }
        
        private static PointRange overY(Matcher m) {
            double x = d(m.group(2));
            m.find();
            double y1 = d(m.group(2));
            double y2 = d(m.group(3));
            Point2D start = new Point2D.Double(x, y1);
            Point2D end = new Point2D.Double(x, y2);
            return new PointRange(start, end);
        }
        
        private static double d(String s) {
            return Double.parseDouble(s);
        }
        
        public boolean containsPt(Point2D pt) {
            return pt.getX() >= start.getX() && 
                   pt.getX() <= end.getX() &&
                   pt.getY() >= start.getY() &&
                   pt.getY() <= end.getY();
        }

        public Point2D getStart() {
            return start;
        }

        public Point2D getEnd() {
            return end;
        }
        
    }
}
