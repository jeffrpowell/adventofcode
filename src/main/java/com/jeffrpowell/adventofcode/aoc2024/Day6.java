package com.jeffrpowell.adventofcode.aoc2024;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.algorithms.Direction;
import com.jeffrpowell.adventofcode.algorithms.Grid;
import com.jeffrpowell.adventofcode.algorithms.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day6 extends Solution2024<List<String>>{
    private static final StringBuilder log = new StringBuilder();

    @Override
    public int getDay() {
        return 6;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        Map<Point2D, String> grid = Grid.generatePointStream(0, 0, input.get(0).size(), input.size())
            .collect(Collectors.toMap(
                Function.identity(), 
                pt -> input.get(d2i(pt.getY())).get(d2i(pt.getX()))
            ));
        Point2D topLeft = new Point2D.Double(0, 0);
        Point2D bottomRight = new Point2D.Double(input.size(), input.get(0).size());
        Set<Point2D> obstacles = grid.entrySet().stream()
            .filter(e -> e.getValue().equals("#"))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
        Point2D guard = grid.entrySet().stream()
            .filter(e -> e.getValue().equals("^"))
            .map(Map.Entry::getKey)
            .findFirst().get();
        Direction d = Direction.UP;
        Set<Point2D> visited = new HashSet<>();
        Point2DUtils.BoundingBox bb = new Point2DUtils.BoundingBox(topLeft, bottomRight);
        while(Point2DUtils.pointInsideBoundary(guard, false, bb)){ 
            visited.add(guard);
            Point2D next = Point2DUtils.applyVectorToPt(d.asVector(), guard);
            if (obstacles.contains(next)) {
                d = d.rotateRight90();
                next = Point2DUtils.applyVectorToPt(d.asVector(), guard);
            }
            guard = next;
        }
        return Integer.toString(visited.size());
    }

    private void printGrid(Point2D startGuard, Point2D guard, Set<Point2D> obstacles, Set<Point2D> initialVisited, Set<Point2D> newVisited, Point2DUtils.BoundingBox bb, Optional<Point2D> newObstacle) {
        Map<Point2D, String> printVals = new HashMap<>();
        printVals.put(bb.min(), ".");
        printVals.put(bb.max(), ".");
        obstacles.stream().forEach(p -> printVals.put(p, "#"));
        initialVisited.stream().forEach(p -> printVals.put(p, "*"));
        newVisited.stream().forEach(p -> printVals.put(p, "o"));
        newObstacle.ifPresent(p -> printVals.put(p, "$"));
        printVals.put(startGuard, "^");
        printVals.put(guard, "G");
        log.append("\n");
        log.append(Point2DUtils.pointsToString(printVals, "."));
    }

    private int d2i(Double d) {
        return d.intValue();
    }

    record CacheKey(Point2D p, Direction d){}

    @Override
    protected String part2(List<List<String>> input) {
        Map<Point2D, String> grid = Grid.generatePointStream(0, 0, input.get(0).size(), input.size())
            .collect(Collectors.toMap(
                Function.identity(), 
                pt -> input.get(d2i(pt.getY())).get(d2i(pt.getX()))
            ));
        Point2D topLeft = new Point2D.Double(0, 0);
        Point2D bottomRight = new Point2D.Double(input.size(), input.get(0).size());
        Set<Point2D> obstacles = grid.entrySet().stream()
            .filter(e -> e.getValue().equals("#"))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
        Point2D startGuard = grid.entrySet().stream()
            .filter(e -> e.getValue().equals("^"))
            .map(Map.Entry::getKey)
            .findFirst().get();
        Point2D guard = startGuard;
        Direction d = Direction.UP;
        Set<CacheKey> visited = new HashSet<>();
        Point2DUtils.BoundingBox bb = new Point2DUtils.BoundingBox(topLeft, bottomRight);
        Set<Point2D> loopObstaclePts = new HashSet<>();
        while(Point2DUtils.pointInsideBoundary(guard, false, bb)){ 
            Point2D tempNext = Point2DUtils.applyVectorToPt(d.asVector(), guard);
            if (obstacles.contains(tempNext)) {
                d = d.rotateRight90();
                tempNext = Point2DUtils.applyVectorToPt(d.asVector(), guard);
                if (obstacles.contains(tempNext)) {
                    d = d.rotateRight90();
                    tempNext = Point2DUtils.applyVectorToPt(d.asVector(), guard);
                }
            }
            final Point2D next = tempNext;
            if (Point2DUtils.pointInsideBoundary(next, false, bb) && !obstacles.contains(next) && Direction.getAllCardinalDirections().map(cardinalD -> new CacheKey(next, cardinalD)).noneMatch(visited::contains)) {
                Set<Point2D> simulatedObstacles = Stream.concat(Stream.of(next), obstacles.stream()).collect(Collectors.toSet());
                Set<CacheKey> visitedCopy = visited.stream().collect(Collectors.toSet());
                if (obstacleProducesLoop(startGuard, guard, d, visitedCopy, simulatedObstacles, next, bb)) {
                    loopObstaclePts.add(next);
                }
            }
            visited.add(new CacheKey(guard, d));
            guard = next;
        }
        // try {
        //     Files.writeString(Path.of("output.txt"), log.toString());
        // } catch (IOException e) {}
        return Integer.toString(loopObstaclePts.size());
    }

    private boolean obstacleProducesLoop(Point2D startGuard, Point2D guard, Direction d, Set<CacheKey> visited, Set<Point2D> obstacles, Point2D newObstacle, Point2DUtils.BoundingBox bb) {
        Set<Point2D> initialVisited = visited.stream().map(CacheKey::p).collect(Collectors.toSet());
        Set<Point2D> newVisited = new HashSet<>();
        initialVisited.add(guard);
        while(Point2DUtils.pointInsideBoundary(guard, false, bb)){ 
            if (!visited.add(new CacheKey(guard, d))) {
                printGrid(startGuard, guard, obstacles, initialVisited, newVisited, bb, Optional.of(newObstacle));
                return true;
            };
            Point2D next = Point2DUtils.applyVectorToPt(d.asVector(), guard);
            if (obstacles.contains(next)) {
                d = d.rotateRight90();
                next = Point2DUtils.applyVectorToPt(d.asVector(), guard);
                if (obstacles.contains(next)) {
                    d = d.rotateRight90();
                    next = Point2DUtils.applyVectorToPt(d.asVector(), guard);
                }
            }
            guard = next;
            newVisited.add(next);
        }
        return false;
    }
}
