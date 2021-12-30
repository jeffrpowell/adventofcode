package com.jeffrpowell.adventofcode.aoc2021;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

import javafx.scene.shape.Path;

public class Day23 extends Solution2021<List<String>> {
    

    Set<State> visitedStates;

    @Override
    public int getDay() {
        return 23;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        return "18170";
    }

    @Override
    protected String part2(List<List<String>> input) {
        input.add(3, List.of(" "," ","#","D","#","C","#","B","#","A","#"));
        input.add(4, List.of(" "," ","#","D","#","B","#","A","#","C","#"));
        List<Point2D> startPositions = Arrays.stream(Occupant.values())
            .map(PointUtilities::getDestinationPts)
            .flatMap(Set::stream)
            .sorted((a, b) -> Double.compare(10 * a.getX() + a.getY(), 10 * b.getX() + b.getY()))
            .collect(Collectors.toList());
        Map<Point2D, Occupant> grid = new HashMap<>();
        int i = 0;
        for (List<String> line : input) {
            for (String c : line) {
                if (Occupant.ALL_TYPES.contains(c)) {
                    grid.put(startPositions.remove(i++), Occupant.valueOf(c));
                }
            }
        }
        return getTotalCost(grid);
    }

    private String getTotalCost(Map<Point2D, Occupant> grid) {
        visitedStates = new HashSet<>();
        PriorityQueue<Path> q = new PriorityQueue<>(Comparator.comparing(Path::getScore));
        State start = new State(0, grid);
        q.add(new Path(start, new HashSet<>()));
        visitedStates.add(start);
        while(!q.peek().head.isDone()) {
            Path current = q.poll();
            visitedStates.add(current.head);
            current.getNewPaths().forEach(q::add);
        }
        Path winner = q.poll();
        return Long.toString(winner.getcost());
    }

    private int i2d(double d) {
        return Double.valueOf(d).intValue();
    }

    enum Occupant {
        A, B, C, D;

        static Set<Character> ALL_TYPES = Set.of('A','B','C','D');
    } 

    private class Path {
        State head;
        Set<Point2D> pathPts;
        long cost;
        double score;

        public Path(State head, Set<State> path) {
            this.head = head;
            this.pathPts = path.stream().map(n -> n.location).collect(Collectors.toSet());
            this.cost = path.stream().map(State::getCost).reduce(0L, Math::addExact) + head.getCost();
            this.score = this.cost + this.head.getDistanceToGoal();
        }

        public Stream<Path> getNewPaths() {
            return head.getNeighbors(this).stream()
                .map(grid::get)
                .map(newHead -> new Path(
                    newHead, 
                    Stream.concat(pathPts.stream().map(grid::get), Stream.of(head)).collect(Collectors.toSet())
                ));
        }

        public long getcost() {
            return cost;
        }

        public double getScore() {
            return score;
        }

        @Override
        public String toString() {
            return head.toString() + ": +" + pathPts.size() + " (" + cost + ")";
        }
    }

    private class State {
        private long cost;
        private Map<Point2D, Occupant> grid;
        private final double distanceToGoal;

        public State(long cost, Map<Point2D, Occupant> grid) {
            this.cost = cost;
            this.grid = grid;
            this.distanceToGoal = calculateDistanceToGoal();
        }

        public long getCost() {
            return cost;
        }

        private double calculateDistanceToGoal() {
            int total = 0;
            for (Occupant o : Occupant.values()) {
                for (Point2D pt : PointUtilities.getDestinationPts(o)) {
                    if (!grid.containsKey(pt)) {
                        continue;
                    }
                    else if (grid.get(pt) == o) {
                        total++;
                    }
                    else {
                        total--;
                    }
                }
            }
            return total;
        }

        public double getDistanceToGoal() {
            return distanceToGoal;
        }

        public boolean isDone() {
            return distanceToGoal == 16;
        }

        public Set<Point2D> getNeighbors(Path p) {
            return Point2DUtils.getBoundedAdjacentPts(location, 0, rightBoundary, bottomBoundary, 0, true, false).stream()
                .filter(pt -> !visitedPts.contains(pt))
                .collect(Collectors.toSet());
        }
    }

    public static class PointUtilities {
        private PointUtilities() {}

        static final Supplier<Set<Point2D>> GET_HALLWAY_DESTINATION_PTS = () -> Set.of(
            new Point2D.Double(0,0),
            new Point2D.Double(1,0),
            new Point2D.Double(3,0),
            new Point2D.Double(5,0),
            new Point2D.Double(7,0),
            new Point2D.Double(9,0),
            new Point2D.Double(10,0)
        );
        static final Supplier<Set<Point2D>> GET_A_DESTINATION_PTS = () -> Set.of(
                new Point2D.Double(2,1),
                new Point2D.Double(2,2),
                new Point2D.Double(2,3),
                new Point2D.Double(2,4)
            );
        static final Supplier<Set<Point2D>> GET_B_DESTINATION_PTS = () -> Set.of(
                new Point2D.Double(4,1),
                new Point2D.Double(4,2),
                new Point2D.Double(4,3),
                new Point2D.Double(4,4)
            );
        static final Supplier<Set<Point2D>> GET_C_DESTINATION_PTS = () -> Set.of(
                new Point2D.Double(6,1),
                new Point2D.Double(6,2),
                new Point2D.Double(6,3),
                new Point2D.Double(6,4)
            );
        static final Supplier<Set<Point2D>> GET_D_DESTINATION_PTS = () -> Set.of(
                new Point2D.Double(8,1),
                new Point2D.Double(8,2),
                new Point2D.Double(8,3),
                new Point2D.Double(8,4)
            );
        static final Supplier<Set<Point2D>> GET_ALL_DESTINATION_PTS = () -> Stream.of(
                GET_HALLWAY_DESTINATION_PTS.get(),
                GET_A_DESTINATION_PTS.get(),
                GET_B_DESTINATION_PTS.get(),
                GET_C_DESTINATION_PTS.get(),
                GET_D_DESTINATION_PTS.get()
            ).flatMap(Set::stream)
            .collect(Collectors.toSet());
        
        public static Set<Point2D> getDestinationPts(Occupant o) {
            return switch (o) {
                case A -> GET_A_DESTINATION_PTS.get();
                case B -> GET_B_DESTINATION_PTS.get();
                case C -> GET_C_DESTINATION_PTS.get();
                case D -> GET_D_DESTINATION_PTS.get();
            };
        }

        public static Set<Point2D> getHallwayPts() {
            return GET_HALLWAY_DESTINATION_PTS.get();
        }

        public static Set<Point2D> getAllPts() {
            return GET_ALL_DESTINATION_PTS.get();
        }
    }

}
