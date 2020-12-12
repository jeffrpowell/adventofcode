package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.regex.Pattern;

public class Day12 extends Solution2020<Rule>{

    @Override
    public int getDay() {
        return 12;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("(\\w)(\\d+)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        Direction direction = Direction.RIGHT;
        Point2D boat = new Point2D.Double(0, 0);
        for (Rule rule : input) {
            switch (rule.getChar(0)) {
                case 'F' -> boat = direction.travelFromNTimes(boat, rule.getInt(1));
                case 'N' -> boat = Direction.UP.travelFromNTimes(boat, rule.getInt(1));
                case 'S' -> boat = Direction.DOWN.travelFromNTimes(boat, rule.getInt(1));
                case 'E' -> boat = Direction.RIGHT.travelFromNTimes(boat, rule.getInt(1));
                case 'W' -> boat = Direction.LEFT.travelFromNTimes(boat, rule.getInt(1));
                case 'L' -> {
                    direction = switch (rule.getInt(1)) {
                        case 90 -> direction.rotateLeft90();
                        case 180 -> direction.opposite();
                        case 270 -> direction.rotateRight90();
                        default -> direction;
                    };
                }
                case 'R' -> {
                    direction = switch (rule.getInt(1)) {
                        case 90 -> direction.rotateRight90();
                        case 180 -> direction.opposite();
                        case 270 -> direction.rotateLeft90();
                        default -> direction;
                    };
                }
            }
        }
        return Double.toString(Point2DUtils.getManhattenDistance(new Point2D.Double(0, 0), boat));
    }

    @Override
    protected String part2(List<Rule> input) {
        Point2D boat = new Point2D.Double(0, 0);
        Point2D wayPt = new Point2D.Double(10, -1);
        for (Rule rule : input) {
            switch (rule.getChar(0)) {
                case 'F' -> boat = Point2DUtils.applyVectorToPtNTimes(wayPt, boat, rule.getInt(1));
                case 'N' -> wayPt.setLocation(wayPt.getX(), wayPt.getY() - rule.getInt(1));
                case 'S' -> wayPt.setLocation(wayPt.getX(), wayPt.getY() + rule.getInt(1));
                case 'E' -> wayPt.setLocation(wayPt.getX() + rule.getInt(1), wayPt.getY());
                case 'W' -> wayPt.setLocation(wayPt.getX() - rule.getInt(1), wayPt.getY());
                case 'L' -> wayPt = Point2DUtils.rotatePtLeftDegreesAround0(wayPt, rule.getDouble(1));
                case 'R' -> wayPt = Point2DUtils.rotatePtRightDegreesAround0(wayPt, rule.getDouble(1));
            }
        }
        return Double.toString(Point2DUtils.getManhattenDistance(new Point2D.Double(0, 0), boat));
    }
}
