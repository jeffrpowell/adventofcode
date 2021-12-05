package com.jeffrpowell.adventofcode.aoc2021;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day5 extends Solution2021<Rule>{

    @Override
    public int getDay() {
        return 5;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("(\\d+,\\d+) -> (\\d+,\\d+)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        List<Line> lines = input.stream().map(Line::new).filter(Line::qualifiesForPt1).collect(Collectors.toList());
        Point2D upperLeft = lines.stream().map(Line::getUpperLeftCorner).reduce((accum, next) -> new Point2D.Double(Math.min(accum.getX(), next.getX()), Math.min(accum.getY(), next.getY()))).get();
        Point2D bottomRight = lines.stream().map(Line::getBottomRightCorner).reduce((accum, next) -> new Point2D.Double(Math.max(accum.getX(), next.getX()), Math.max(accum.getY(), next.getY()))).get();
        return Long.toString(Point2DUtils.generateGrid(upperLeft.getX(), upperLeft.getY(), bottomRight.getX(), bottomRight.getY())
            .map(pt -> lines.stream().filter(line -> line.containsPt(pt)).count())
            .filter(ct -> ct > 1)
            .count());
    }

    @Override
    protected String part2(List<Rule> input) {
        List<Line> lines = input.stream().map(Line::new).collect(Collectors.toList());
        Point2D upperLeft = lines.stream().map(Line::getUpperLeftCorner).reduce((accum, next) -> new Point2D.Double(Math.min(accum.getX(), next.getX()), Math.min(accum.getY(), next.getY()))).get();
        Point2D bottomRight = lines.stream().map(Line::getBottomRightCorner).reduce((accum, next) -> new Point2D.Double(Math.max(accum.getX(), next.getX()), Math.max(accum.getY(), next.getY()))).get();
        return Long.toString(Point2DUtils.generateGrid(upperLeft.getX(), upperLeft.getY(), bottomRight.getX(), bottomRight.getY())
            .map(pt -> lines.stream().filter(line -> line.containsPt(pt)).count())
            .filter(ct -> ct > 1)
            .count());
    }
    
    private static class Line {
        Point2D start;
        Point2D end;

        public Line(Rule rule) {
            start = rule.getPoint2D(0);
            end = rule.getPoint2D(1);
        }

        public boolean qualifiesForPt1() {
            return start.getX() == end.getX() || start.getY() == end.getY();
        }

        public boolean containsPt(Point2D pt) {
            return Point2DUtils.pointInsideLine(pt, true, start, end);
        }

        public Point2D getUpperLeftCorner() {
            return new Point2D.Double(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()));
        }

        public Point2D getBottomRightCorner() {
            return new Point2D.Double(Math.max(start.getX(), end.getX()), Math.max(start.getY(), end.getY()));
        }
    }
}
