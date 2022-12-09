package com.jeffrpowell.adventofcode.aoc2022;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day9 extends Solution2022<Rule>{
    private static final double DIAG_DIST_1 = new Point2D.Double(0, 0).distance(new Point2D.Double(1, 1));
    private static final double DIAG_DIST_2 = new Point2D.Double(0, 0).distance(new Point2D.Double(2, 2));

    @Override
    public int getDay() {
        return 9;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("(.) (\\d+)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        Set<Point2D> visited = new HashSet<>();
        Point2D start = new Point2D.Double(0, 0);
        Point2D head = start;
        Point2D tail = start;
        visited.add(tail);
        for (Rule line : input) {
            Direction d = switch (line.getChar(0)) {
                case 'U' -> Direction.UP;
                case 'D' -> Direction.DOWN;
                case 'L' -> Direction.LEFT;
                default -> Direction.RIGHT;
            };
            for (int i = 0; i < line.getInt(1); i++) {
                head = Point2DUtils.movePtInDirection(head, d, 1);
                tail = moveTail(head, tail);
                visited.add(tail);
                // printPts(Stream.of(start, head, tail).collect(Collectors.toSet()));
            }
        }
        return Integer.toString(visited.size());
    }

    private Point2D moveTail(Point2D head, Point2D tail) {
        if (head.distance(tail) >= DIAG_DIST_2) {
            return Point2DUtils.getAdjacentPts(head, true).stream()
                .min(Comparator.comparing(tail::distance)).get();
        }
        else if (head.distance(tail) > DIAG_DIST_1) {
            return Point2DUtils.getAdjacentPts(head, false).stream()
                .min(Comparator.comparing(tail::distance)).get();
        }
        return tail;
    }

    private void printPts(Set<Point2D> pts) {
        Point2DUtils.printPoints(pts);
    }

    @Override
    protected String part2(List<Rule> input) {
        Set<Point2D> visited = new HashSet<>();
        Point2D start = new Point2D.Double(0, 0);
        Point2D head = start;
        List<Point2D> pts = Stream.generate(() -> start).limit(9).collect(Collectors.toList());
        visited.add(start);
        for (Rule line : input) {
            Direction d = switch (line.getChar(0)) {
                case 'U' -> Direction.UP;
                case 'D' -> Direction.DOWN;
                case 'L' -> Direction.LEFT;
                default -> Direction.RIGHT;
            };
            for (int i = 0; i < line.getInt(1); i++) {
                head = Point2DUtils.movePtInDirection(head, d, 1);
                Point2D prevPt = head;
                for (int knot = 0; knot < pts.size(); knot++) {
                    Point2D newKnotPt = moveTail(prevPt, pts.get(knot));
                    pts.set(knot, newKnotPt);
                    prevPt = newKnotPt;
                }
                visited.add(pts.get(pts.size() - 1));
                // printPts(Stream.of(Set.of(head), pts).flatMap(Collection::stream).collect(Collectors.toSet()));
            }
        }
        return Integer.toString(visited.size());
    }
}
