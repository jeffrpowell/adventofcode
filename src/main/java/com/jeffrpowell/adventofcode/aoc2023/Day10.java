package com.jeffrpowell.adventofcode.aoc2023;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;


public class Day10 extends Solution2023<List<String>>{
    private static final Pipe START_PIPE = new Pipe(Direction.UP_LEFT, Direction.DOWN_RIGHT);
    private static final Pipe GROUND_PIPE = new Pipe(Direction.DOWN_LEFT, Direction.UP_RIGHT);
    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        final int rightBoundary = input.get(0).size();
        final int bottomBoundary = input.size();
        Map<Point2D, Pipe> grid = Point2DUtils.generateGrid(0, 0, rightBoundary, bottomBoundary, pt -> parsePipe(input.get(d2i(pt.getY())).get(d2i(pt.getX()))));
        Point2D startPt = grid.entrySet().stream().filter(e -> e.getValue() == START_PIPE).map(Map.Entry::getKey).findFirst().get();
        List<PipeTraversal> neighbors = Point2DUtils.getAdjacentPts(startPt, false).stream()
            .map(pt -> {
                Direction outboundDirection = Direction.getDirectionThisLineTravels(startPt, pt).get();
                Pipe neighbor = grid.get(pt);
                return new PipeTraversal(pt, neighbor, outboundDirection.opposite());
            })
            .filter(traversal -> {
                return traversal.pipe.containsDirection(traversal.cameFrom);
            })
            .collect(Collectors.toList());
        long steps = 1;
        while (!neighbors.get(0).pt.equals(neighbors.get(1).pt)) {
            neighbors = neighbors.stream()
                .map(traversal -> {
                    Direction outboundDirection = traversal.pipe.getOppositeEnd(traversal.cameFrom);
                    Point2D nextPt = traversal.traverseNextPt();
                    return new PipeTraversal(nextPt, grid.get(nextPt), outboundDirection.opposite());
                })
                .collect(Collectors.toList());
            steps++;
        }
        return Long.toString(steps);
    }

    @Override
    protected String part2(List<List<String>> input) {
        final int rightBoundary = input.get(0).size();
        final int bottomBoundary = input.size();
        Map<Point2D, Pipe> grid = Point2DUtils.generateGrid(0, 0, rightBoundary, bottomBoundary, pt -> parsePipe(input.get(d2i(pt.getY())).get(d2i(pt.getX()))));
        Point2D startPt = grid.entrySet().stream().filter(e -> e.getValue() == START_PIPE).map(Map.Entry::getKey).findFirst().get();
        List<PipeTraversal> neighbors = Point2DUtils.getAdjacentPts(startPt, false).stream()
            .map(pt -> {
                Direction outboundDirection = Direction.getDirectionThisLineTravels(startPt, pt).get();
                Pipe neighbor = grid.get(pt);
                return new PipeTraversal(pt, neighbor, outboundDirection.opposite());
            })
            .filter(traversal -> traversal.pipe != null)
            .filter(traversal -> {
                return traversal.pipe.containsDirection(traversal.cameFrom);
            })
            .collect(Collectors.toList());
        Set<Point2D> mainLoop = neighbors.stream().map(PipeTraversal::pt).collect(Collectors.toSet());
        mainLoop.add(startPt);
        while (!neighbors.get(0).pt.equals(neighbors.get(1).pt)) {
            neighbors = neighbors.stream()
                .map(traversal -> {
                    Direction outboundDirection = traversal.pipe.getOppositeEnd(traversal.cameFrom);
                    Point2D nextPt = traversal.traverseNextPt();
                    return new PipeTraversal(nextPt, grid.get(nextPt), outboundDirection.opposite());
                })
                .peek(traversal -> mainLoop.add(traversal.pt))
                .collect(Collectors.toList());
        }
        /*
         * ...
         * ...
         * 
         * .......
         * .X.X.X.
         * .......
         * .X.X.X.
         * .......
         * 
         * (0,0) -> <(0,0),(2,2)>
         * (1,0) -> <(2,0),(4,2)>
         * (2,0) -> <(4,0),(6,2)>
         * (0,1) -> <(0,2),(2,4)>
         * (1,1) -> <(2,2),(4,4)>
         * (2,1) -> <(4,2),(6,4)>
         * 
         */
        final int zoomRightBoundary = rightBoundary * 2 + 2;
        final int zoomBottomBoundary = bottomBoundary * 2 + 2;
        Map<Point2D, Pipe> zoomGrid = Point2DUtils.generateGrid(0, 0, zoomRightBoundary, zoomBottomBoundary, pt -> GROUND_PIPE);
        mainLoop.stream()
            .forEach(pipePt -> {
                double newX = pipePt.getX() * 2 + 1;
                double newY = pipePt.getY() * 2 + 1;
                zoomGrid.putAll(grid.get(pipePt).zoomIn(new Point2D.Double(newX, newY)));
            });
        Set<Point2D> zoomMainLoop = zoomGrid.entrySet().stream()
            .filter(e -> !e.getValue().equals(GROUND_PIPE))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
        List<Point2D> bfs = new ArrayList<>();
        bfs.add(new Point2D.Double(0, 0));
        Set<Point2D> visited = new HashSet<>();
        while (!bfs.isEmpty()) {
            Point2D next = bfs.remove(0);
            if (!visited.add(next)) {
                continue;
            }
            Set<Point2D> zoomNeighbors = Point2DUtils.getBoundedAdjacentPts(next, 0, zoomRightBoundary-1, zoomBottomBoundary-1, 0, true, false);
            zoomNeighbors.stream()
                .filter(pt -> !zoomMainLoop.contains(pt) && !visited.contains(pt))
                .forEach(bfs::add);
        }
        zoomMainLoop.stream()
            .peek(visited::add)
            .map(loopPt -> Point2DUtils.getBoundedAdjacentPts(loopPt, 0, zoomRightBoundary-1, zoomBottomBoundary-1, 0, true, false))
            .flatMap(Set::stream)
            .forEach(visited::add);
        Set<Point2D> keepers = new HashSet<>();
        Sets.difference(zoomGrid.keySet(), visited)
            .stream()
            .sorted(Comparator.comparing(Point2D::getY).thenComparing(Point2D::getX))
            .forEach(pt -> {
                if (visited.add(pt)) {
                    keepers.add(pt);
                    visited.addAll(Point2DUtils.getBoundedAdjacentPts(pt, 0, zoomRightBoundary-1, zoomBottomBoundary-1, 0, true, false));
                }
            });
        return Long.toString(keepers.size());
    }

    private int d2i(Double d) {
        return d.intValue();
    }

    record Pipe(Direction d1, Direction d2){
        private static Pipe VERTICAL = new Pipe(Direction.UP, Direction.DOWN);
        private static Pipe HORIZONTAL = new Pipe(Direction.LEFT, Direction.RIGHT);
        boolean containsDirection(Direction d) {
            return d1 == d || d2 == d;
        }

        Direction getOppositeEnd(Direction cameFrom) {
            if (d1 != cameFrom) {
                return d1;
            }
            else {
                return d2;
            }
        }

        Map<Point2D, Pipe> zoomIn(Point2D pt) {
            Map<Point2D, Pipe> zoomGrid = new HashMap<>();
            zoomGrid.put(pt, this);
            Pipe extensionPipe1 = d1 == Direction.LEFT || d1 == Direction.RIGHT ? HORIZONTAL : VERTICAL;
            zoomGrid.put(d1.travelFrom(pt), extensionPipe1);
            Pipe extensionPipe2 = d1 == Direction.LEFT || d2 == Direction.RIGHT ? HORIZONTAL : VERTICAL;
            zoomGrid.put(d2.travelFrom(pt), extensionPipe2);
            return zoomGrid;
        }
    }

    private Pipe parsePipe(String s) {
        return switch (s) {
            case "|" -> new Pipe(Direction.UP, Direction.DOWN);
            case "-" -> new Pipe(Direction.LEFT, Direction.RIGHT);
            case "7" -> new Pipe(Direction.LEFT, Direction.DOWN);
            case "J" -> new Pipe(Direction.UP, Direction.LEFT);
            case "F" -> new Pipe(Direction.DOWN, Direction.RIGHT);
            case "L" -> new Pipe(Direction.UP, Direction.RIGHT);
            case "." -> GROUND_PIPE;
            default -> START_PIPE;
        };
    }

    record PipeTraversal(Point2D pt, Pipe pipe, Direction cameFrom){
        Point2D traverseNextPt() {
            return pipe.getOppositeEnd(cameFrom).travelFrom(pt);
        }
    }
}
