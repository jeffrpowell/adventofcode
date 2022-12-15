package com.jeffrpowell.adventofcode.aoc2022;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day15 extends Solution2022<Rule>{

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
        long targetRow = 2000000;
        List<Sensor> sensors = input.stream()
            .map(line -> 
                new Sensor(new Point2D.Double(line.getLong(0), line.getLong(1)),new Point2D.Double(line.getLong(2), line.getLong(3)))
            )
            .collect(Collectors.toList());

        return Long.toString(sensors.stream().filter(s -> Math.abs(s.pt.getY() - targetRow) <= s.distance())
            .map(s -> s.getPointsOnRow(targetRow))
            .flatMap(List::stream)
            .count());
    }

    @Override
    protected String part2(List<Rule> input) {
        return null;
    }

    private static class Sensor {
        private Point2D pt;
        private Point2D beacon;
        public Sensor(Point2D pt, Point2D beacon) {
            this.pt = pt;
            this.beacon = beacon;
        }
        
        public double distance() {
            return Point2DUtils.getManhattenDistance(pt, beacon);
        }

        public List<Point2D> getPointsOnRow(long row) {
            double dist = distance();
            Point2D start = new Point2D.Double(pt.getX() - dist, row);
            Point2D right = new Point2D.Double(1, 0);
            return Stream.iterate(start, p -> p.getX() < pt.getX() + dist, p -> Point2DUtils.applyVectorToPt(right, p))
                .filter(p -> Point2DUtils.getManhattenDistance(p, pt) <= dist)
                .collect(Collectors.toList());
        }
    }

}
