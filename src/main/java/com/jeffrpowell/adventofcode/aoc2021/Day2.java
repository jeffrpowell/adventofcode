package com.jeffrpowell.adventofcode.aoc2021;

import com.jeffrpowell.adventofcode.algorithms.Direction;
import com.jeffrpowell.adventofcode.algorithms.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

import java.awt.geom.Point2D;

import java.util.List;
import java.util.stream.Collectors;

public class Day2 extends Solution2021<List<String>>{

    @Override
    public int getDay() {
        return 2;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser(" ");
    }

    @Override
    protected String part1(List<List<String>> input) {
        Point2D pt = input.stream()
            .map(Command::new)
            .map(Command::getAsVector)
            .collect(Collectors.reducing(new Point2D.Double(0, 0), (accumulator, next) -> Point2DUtils.applyVectorToPt(next, accumulator)));
        return Double.toString(pt.getX() * pt.getY());
    }

    @Override
    protected String part2(List<List<String>> input) {
        Point2D pt = new Point2D.Double(0, 0);
        int aim = 0;

        for (Command c : input.stream().map(Command::new).collect(Collectors.toList())) {
            aim = c.newAim(aim);
            pt = c.newPoint(pt, aim);
        }
                
        return Integer.toString(Double.valueOf(pt.getX() * pt.getY()).intValue());
    }

    private static class Command {
        final Direction d;
        final int magnitude;

        public Command(List<String> command) {
            d = switch(command.get(0)) {
                case "forward" -> Direction.RIGHT;
                case "down" -> Direction.DOWN;
                case "up" -> Direction.UP;
                default -> Direction.LEFT;
            };
            this.magnitude = Integer.parseInt(command.get(1));
        }

        public Point2D getAsVector() {
            return switch (d) {
                case DOWN -> new Point2D.Double(0, magnitude);
                case RIGHT -> new Point2D.Double(magnitude, 0);
                case UP -> new Point2D.Double(0, -magnitude);
                default -> new Point2D.Double(0, 0);
            };
        }
        
        public int newAim(int aim) {
            return switch (d) {
                case DOWN -> aim + magnitude;
                case UP -> aim - magnitude;
                default -> aim;
            };
        }

        public Point2D newPoint(Point2D pt, int aim) {
            if (d == Direction.RIGHT) {
                return Point2DUtils.applyVectorToPt(new Point2D.Double(magnitude, magnitude * aim), pt);
            }
            else {
                return pt;
            }
        }
    }
}
