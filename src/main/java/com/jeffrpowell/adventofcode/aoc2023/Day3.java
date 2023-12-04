package com.jeffrpowell.adventofcode.aoc2023;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;


public class Day3 extends Solution2023<List<String>>{
    private static final Pattern SYMBOL_REGEX = Pattern.compile("[^\\.\\d]");
    private static final Pattern DIGIT_REGEX = Pattern.compile("\\d");
    private static final Pattern GEAR_REGEX = Pattern.compile("\\*");

    @Override
    public int getDay() {
        return 3;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }


    @Override
    protected String part1(List<List<String>> input) {
        final int rightBoundary = input.get(0).size();
        final int bottomBoundary = input.size();
        Map<Point2D, String> grid = Point2DUtils.generateGrid(0, 0, rightBoundary, bottomBoundary)
            .collect(Collectors.toMap(
                Function.identity(),
                pt -> input.get(d2i(pt.getY())).get(d2i(pt.getX()))
            ));
        Set<Point2D> symbolPts = grid.entrySet().stream()
            .filter(e -> SYMBOL_REGEX.matcher(e.getValue()).find())
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
        Set<Point2D> goodPts = grid.entrySet().stream()
            .filter(e -> DIGIT_REGEX.matcher(e.getValue()).find())
            .filter(e -> {
                Set<Point2D> neighbors = Point2DUtils.getBoundedAdjacentPts(e.getKey(), 0, rightBoundary, bottomBoundary, 0, true, true);
                return neighbors.stream().anyMatch(symbolPts::contains);
            })
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
        List<Integer> partNumbers = new ArrayList<>();
        Set<Point2D> visited = new HashSet<>();
        for (Point2D pt : goodPts) {
            List<Point2D> digitPts = new ArrayList<>();
            digitPts.add(pt);
            if (!visited.add(pt)) {
                continue;
            }
            Point2D left = Point2DUtils.movePtInDirection(pt, Direction.LEFT, 1);
            boolean breakout = false;
            while(grid.containsKey(left) && DIGIT_REGEX.matcher(grid.get(left)).find()) {
                digitPts.add(0, left);
                if (!visited.add(left)) {
                    breakout = true;
                    break;
                }
                left = Point2DUtils.movePtInDirection(left, Direction.LEFT, 1);
            }
            Point2D right = Point2DUtils.movePtInDirection(pt, Direction.RIGHT, 1);
            while(grid.containsKey(right) && DIGIT_REGEX.matcher(grid.get(right)).find()) {
                digitPts.add(right);
                if (!visited.add(right)) {
                    breakout = true;
                    break;
                }
                right = Point2DUtils.movePtInDirection(right, Direction.RIGHT, 1);
            }
            if (breakout) {
                continue;
            }
            partNumbers.add(Integer.parseInt(digitPts.stream().map(grid::get).map(i -> i.toString()).collect(Collectors.joining(""))));
        }
        return Integer.toString(partNumbers.stream().reduce(0, Math::addExact));
    }

    private int d2i(double d) {
        return Double.valueOf(d).intValue();
    }

    private record GearPt(Point2D startPt, Point2D gearPt, Long partNumber){}

    @Override
    protected String part2(List<List<String>> input) {
        final int rightBoundary = input.get(0).size();
        final int bottomBoundary = input.size();
        Map<Point2D, String> grid = Point2DUtils.generateGrid(0, 0, rightBoundary, bottomBoundary)
            .collect(Collectors.toMap(
                Function.identity(),
                pt -> input.get(d2i(pt.getY())).get(d2i(pt.getX()))
            ));
        Set<Point2D> symbolPts = grid.entrySet().stream()
            .filter(e -> GEAR_REGEX.matcher(e.getValue()).find())
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
        Set<GearPt> goodPts = grid.entrySet().stream()
            .filter(e -> DIGIT_REGEX.matcher(e.getValue()).find())
            .filter(e -> {
                Set<Point2D> neighbors = Point2DUtils.getBoundedAdjacentPts(e.getKey(), 0, rightBoundary - 1, bottomBoundary - 1, 0, true, true);
                return neighbors.stream().anyMatch(symbolPts::contains);
            })
            .map(e -> {
                Set<Point2D> neighbors = Point2DUtils.getBoundedAdjacentPts(e.getKey(), 0, rightBoundary - 1, bottomBoundary - 1, 0, true, true);
                return neighbors.stream()
                    .filter(pt -> grid.get(pt).equals("*"))
                    .map(pt -> new GearPt(e.getKey(), pt, -1L))
                    .findFirst().get();
            })
            .collect(Collectors.toSet());
        List<GearPt> partNumbers = new ArrayList<>();
        Set<Point2D> visited = new HashSet<>();
        for (GearPt gearPt : goodPts) {
            List<Point2D> digitPts = new ArrayList<>();
            Point2D startPt = gearPt.startPt;
            digitPts.add(startPt);
            if (!visited.add(startPt)) {
                continue;
            }
            Point2D left = Point2DUtils.movePtInDirection(startPt, Direction.LEFT, 1);
            boolean breakout = false;
            while(grid.containsKey(left) && DIGIT_REGEX.matcher(grid.get(left)).find()) {
                digitPts.add(0, left);
                if (!visited.add(left)) {
                    breakout = true;
                    break;
                }
                left = Point2DUtils.movePtInDirection(left, Direction.LEFT, 1);
            }
            Point2D right = Point2DUtils.movePtInDirection(startPt, Direction.RIGHT, 1);
            while(grid.containsKey(right) && DIGIT_REGEX.matcher(grid.get(right)).find()) {
                digitPts.add(right);
                if (!visited.add(right)) {
                    breakout = true;
                    break;
                }
                right = Point2DUtils.movePtInDirection(right, Direction.RIGHT, 1);
            }
            if (breakout) {
                continue;
            }
            Long partNumber = Long.parseLong(digitPts.stream().map(grid::get).map(i -> i.toString()).collect(Collectors.joining("")));
            partNumbers.add(new GearPt(startPt, gearPt.gearPt, partNumber));
        }
        Map<Point2D, List<Long>> gearPtMap = partNumbers.stream()
            .collect(Collectors.groupingBy(GearPt::gearPt, Collectors.mapping(GearPt::partNumber, Collectors.toList())));
        return Long.toString(gearPtMap.values().stream()
            .filter(l -> l.size() == 2)
            .map(l -> l.stream().reduce(1L, Math::multiplyExact))
            .reduce(0L, Math::addExact)
        );
    }
}
