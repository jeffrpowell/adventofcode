package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.algorithms.Direction;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.geometry.Point3D;

public class Day24 extends Solution2020<String>{

    @Override
    public int getDay() {
        return 24;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }

    @Override
    protected String part1(List<String> input) {
        Map<Point3D, Boolean> grid = initializeGrid(input);
        return Long.toString(blackTileCount(grid));
    }
    
    private static Direction charsToDirection(String s) {
        return switch (s) {
            case "e" -> Direction.RIGHT;
            case "se" -> Direction.DOWN_RIGHT;
            case "sw" -> Direction.DOWN_LEFT;
            case "w" -> Direction.LEFT;
            case "nw" -> Direction.UP_LEFT;
            case "ne" -> Direction.UP_RIGHT;
            default -> null;
        };
    }
    
    private static Point3D directionToPoint(Point3D startPt, Direction direction) {
        return switch (direction) {
            case RIGHT -> startPt.add(1, -1, 0);
            case DOWN_RIGHT -> startPt.add(0, -1, 1);
            case DOWN_LEFT -> startPt.add(-1, 0, 1);
            case LEFT -> startPt.add(-1, 1, 0);
            case UP_LEFT -> startPt.add(0, 1, -1);
            case UP_RIGHT -> startPt.add(1, 0, -1);
            default -> startPt;
        };
    }
    
    private Map<Point3D, Boolean> initializeGrid(List<String> input) {
        Map<Point3D, Boolean> grid = new HashMap<>();
        for (String line : input) {
            Point3D pt = new Point3D(0, 0, 0);
            int i = 0;
            while(i < line.length()) {
                if (line.charAt(i) == 'e' || line.charAt(i) == 'w') {
                    pt = directionToPoint(pt, charsToDirection(line.substring(i, i + 1)));
                    i++;
                }
                else {
                    pt = directionToPoint(pt, charsToDirection(line.substring(i, i + 2)));
                    i += 2;
                }
            }
            grid.putIfAbsent(pt, Boolean.FALSE);
            grid.put(pt, !grid.get(pt));
        }
        return grid;
    }

    @Override
    protected String part2(List<String> input) {
        Map<Point3D, Boolean> grid = initializeGrid(input);
        Set<Point3D> neighborsToAdd = grid.entrySet().stream()
            .filter(Map.Entry::getValue)
            .flatMap(entry -> getAdjacentPts(entry.getKey()))
            .collect(Collectors.toSet());
        neighborsToAdd.stream()
            .forEach(pt -> grid.putIfAbsent(pt, Boolean.FALSE));
        Set<Point3D> pointsToCheck = new HashSet<>();
        Set<Point3D> pointsToToggle = new HashSet<>();
//        System.out.println(blackTileCount(grid));
        for (int i = 0; i < 100; i++) {
            grid.entrySet().stream()
                .filter(Map.Entry::getValue)
                .peek(entry -> pointsToCheck.add(entry.getKey()))
                .flatMap(entry -> getAdjacentPts(entry.getKey()))
                .forEach(pointsToCheck::add);
            for (Point3D pt : pointsToCheck) {
                boolean iAmBlack = grid.get(pt);
                long blackNeighbors = blackNeighborCount(pt, grid);
                if (iAmBlack && (blackNeighbors > 2 || blackNeighbors == 0)) {
                    pointsToToggle.add(pt);
                }
                else if (!iAmBlack && blackNeighbors == 2) {
                    pointsToToggle.add(pt);
                }
            }
            pointsToToggle.stream()
                .peek(pt -> {
                    if (!grid.get(pt)) { //will be black after toggle, need to warm cache with neighbors
                        getAdjacentPts(pt).forEach(neighbor -> grid.putIfAbsent(neighbor, Boolean.FALSE));
                    }
                })
                .forEach(pt -> grid.put(pt, !grid.get(pt)));
            pointsToCheck.clear();
            pointsToToggle.clear();
//            System.out.println(blackTileCount(grid));
        }
        return Long.toString(blackTileCount(grid));
    }
    
    private static long blackNeighborCount(Point3D pt, Map<Point3D, Boolean> grid) {
        return getAdjacentPts(pt).filter(neighbor -> grid.getOrDefault(neighbor, Boolean.FALSE)).count();
    }
    
    private static Stream<Point3D> getAdjacentPts(Point3D pt) {
        return Stream.of(
            directionToPoint(pt, Direction.RIGHT),
            directionToPoint(pt, Direction.DOWN_RIGHT),
            directionToPoint(pt, Direction.DOWN_LEFT),
            directionToPoint(pt, Direction.LEFT),
            directionToPoint(pt, Direction.UP_LEFT),
            directionToPoint(pt, Direction.UP_RIGHT)
        );
    }

    private static long blackTileCount(Map<Point3D, Boolean> grid) {
        return grid.values().stream()
            .filter(b -> b)
            .count();
    }
}
