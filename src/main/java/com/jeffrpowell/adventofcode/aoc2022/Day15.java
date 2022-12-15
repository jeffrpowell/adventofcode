package com.jeffrpowell.adventofcode.aoc2022;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.annotations.VisibleForTesting;
import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day15 extends Solution2022<Rule>{
    long targetRow = 2000000;
    double targetMax = 4000000;

    @VisibleForTesting
    public void setTargetRow(long row) {
        targetRow = row;
    }
    @VisibleForTesting
    public void setTargetMax(double max) {
        targetMax = max;
    }

    @Override
    public int getDay() {
        return 15;
    }
    //Sensor at x=2208586, y=2744871: closest beacon is at x=2094814, y=3380585

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n",Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        List<Sensor> sensors = input.stream()
            .map(line -> 
                new Sensor(new Point2D.Double(line.getLong(0), line.getLong(1)),new Point2D.Double(line.getLong(2), line.getLong(3)))
            )
            .collect(Collectors.toList());
        Set<Point2D> beacons = sensors.stream()
            .map(s -> s.beacon)
            .collect(Collectors.toSet());
        return Integer.toString(sensors.stream().filter(s -> Math.abs(s.pt.getY() - targetRow) <= s.distance)
            .map(s -> s.getPointsOnRow(targetRow))
            .flatMap(List::stream)
            .filter(p -> !beacons.contains(p))
            .collect(Collectors.toSet()).size());
    }

    @Override
    protected String part2(List<Rule> input) {
        List<Sensor> sensors = input.stream()
            .map(line -> 
                new Sensor(new Point2D.Double(line.getLong(0), line.getLong(1)),new Point2D.Double(line.getLong(2), line.getLong(3)))
            )
            .collect(Collectors.toList());
        Set<Point2D> beacons = sensors.stream()
            .map(s -> s.beacon)
            .collect(Collectors.toSet());
        Set<Point2D> suspectPts = sensors.stream() 
            .map(Sensor::getPerimeter)
            .flatMap(Set::stream)
            .filter(p -> !beacons.contains(p))
            .collect(Collectors.toSet());
        Point2D beacon = suspectPts.stream()
            .filter(p -> sensors.stream().noneMatch(s -> s.containsPt(p)))
            .findAny()
            .get();
        return Long.toString(Double.valueOf(beacon.getX() * 4000000 + beacon.getY()).longValue());
    }

    private class Sensor {
        private Point2D pt;
        private Point2D beacon;
        private double distance;
        public Sensor(Point2D pt, Point2D beacon) {
            this.pt = pt;
            this.beacon = beacon;
            this.distance = distanceCalc();
        }
        
        public double distanceCalc() {
            return Point2DUtils.getManhattenDistance(pt, beacon);
        }

        public List<Point2D> getPointsOnRow(long row) {
            Point2D start = new Point2D.Double(pt.getX() - distance, row);
            Point2D right = new Point2D.Double(1, 0);
            return Stream.iterate(start, p -> p.getX() < pt.getX() + distance, p -> Point2DUtils.applyVectorToPt(right, p))
                .filter(p -> Point2DUtils.getManhattenDistance(p, pt) <= distance)
                .collect(Collectors.toList());
        }

        public Set<Point2D> getPerimeter() {
            Set<Point2D> perimeter = new HashSet<>();
            Point2D walk = new Point2D.Double(pt.getX() - distance - 1, pt.getY());
            while (walk.getX() != pt.getX()) {
                if (Point2DUtils.pointInsideBoundary(walk, true, 0, targetMax, targetMax, 0)) {
                    perimeter.add(walk);
                }
                walk = Point2DUtils.movePtInDirection(walk, Direction.UP_RIGHT, 1);
            }
            while (walk.getY() != pt.getY()) {
                if (Point2DUtils.pointInsideBoundary(walk, true, 0, targetMax, targetMax, 0)) {
                    perimeter.add(walk);
                }
                walk = Point2DUtils.movePtInDirection(walk, Direction.DOWN_RIGHT, 1);
            }
            while (walk.getX() != pt.getX()) {
                if (Point2DUtils.pointInsideBoundary(walk, true, 0, targetMax, targetMax, 0)) {
                    perimeter.add(walk);
                }
                walk = Point2DUtils.movePtInDirection(walk, Direction.DOWN_LEFT, 1);
            }
            while (walk.getY() != pt.getY()) {
                if (Point2DUtils.pointInsideBoundary(walk, true, 0, targetMax, targetMax, 0)) {
                    perimeter.add(walk);
                }
                walk = Point2DUtils.movePtInDirection(walk, Direction.UP_LEFT, 1);
            }
            return perimeter;
        }

        public boolean containsPt(Point2D p) {
            return Point2DUtils.getManhattenDistance(p, pt) <= distance;
        }
    }

}
