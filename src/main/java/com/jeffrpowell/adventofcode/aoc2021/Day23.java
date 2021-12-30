package com.jeffrpowell.adventofcode.aoc2021;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Sets;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

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
        Set<State> pathPts;
        long cost;
        double score;

        public Path(State head, Set<State> path) {
            this.head = head;
            this.pathPts = path;
            this.cost = path.stream().map(State::getCost).reduce(0L, Math::addExact) + head.getCost();
            this.score = this.cost + this.head.getDistanceToGoal();
        }

        public Stream<Path> getNewPaths() {
            return head.getNeighbors().stream()
                .map(newHead -> new Path(
                    newHead, 
                    Stream.concat(pathPts.stream(), Stream.of(head)).collect(Collectors.toSet())
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

        public Set<State> getNeighbors() {
            for (Occupant o : Occupant.values()) {
                
            }
        }

        //null return means that the column is full
        private Optional<Point2D> getDeepestOpenPoint(Occupant o, Map<Point2D, Occupant> grid) {
            return Sets.difference(PointUtilities.getDestinationPts(o), grid.keySet()).stream().sorted(Comparator.comparing(Point2D::getY)).findFirst();
        }

        private boolean columnReadyToFill(Occupant o) {
            return PointUtilities.getDestinationPts(o).stream().map(grid::get).allMatch(v -> v == null || v == o);
        }

        private Set<Point2D> findOpenHallwayFromColumn(Occupant o) {
            double x = PointUtilities.getOccupantX(o);
            Point2D left = new Point2D.Double(x - 1, 0);
            Point2D right = new Point2D.Double(x + 1, 0);
            Set<Point2D> openHallway = new HashSet<>();
            while (left.getX() >= 0) {
                if (!grid.containsKey(left)) {
                    openHallway.add(left);
                    left = Point2DUtils.applyVectorToPt(left, new Point2D.Double(left.getX() == 1.0 ? -1 : -2, 0));
                }
                else {
                    break;
                }
            }
            while (right.getX() <= 10) {
                if (!grid.containsKey(right)) {
                    openHallway.add(right);
                    right = Point2DUtils.applyVectorToPt(right, new Point2D.Double(right.getX() == 9.0 ? 1 : 2, 0));
                }
                else {
                    break;
                }
            }
            return openHallway;
        }

        private Set<Point2D> findValidFillerForColumn(Occupant o) {
            double x = PointUtilities.getOccupantX(o);
            Point2D left = new Point2D.Double(x - 1, 0);
            Point2D right = new Point2D.Double(x + 1, 0);
            Set<Point2D> valid = new HashSet<>();
            while (left.getX() >= 0) {
                if (!grid.containsKey(left)) {
                    left = Point2DUtils.applyVectorToPt(left, new Point2D.Double(left.getX() == 1.0 ? -1 : -2, 0));
                }
                else {
                    if (grid.get(left) == o) {
                        valid.add(left);
                    }
                    break;
                }
            }
            while (right.getX() <= 10) {
                if (!grid.containsKey(right)) {
                    right = Point2DUtils.applyVectorToPt(right, new Point2D.Double(right.getX() == 9.0 ? 1 : 2, 0));
                }
                else {
                    if (grid.get(left) == o) {
                        valid.add(left);
                    }
                    break;
                }
            }
            return valid;
        }
    }

    public static class PointUtilities {
        private PointUtilities() {}

        static final double A_X = 2;
        static final double B_X = 4;
        static final double C_X = 6;
        static final double D_X = 8;

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
                new Point2D.Double(A_X,1),
                new Point2D.Double(A_X,2),
                new Point2D.Double(A_X,3),
                new Point2D.Double(A_X,4)
            );
        static final Supplier<Set<Point2D>> GET_B_DESTINATION_PTS = () -> Set.of(
                new Point2D.Double(B_X,1),
                new Point2D.Double(B_X,2),
                new Point2D.Double(B_X,3),
                new Point2D.Double(B_X,4)
            );
        static final Supplier<Set<Point2D>> GET_C_DESTINATION_PTS = () -> Set.of(
                new Point2D.Double(C_X,1),
                new Point2D.Double(C_X,2),
                new Point2D.Double(C_X,3),
                new Point2D.Double(C_X,4)
            );
        static final Supplier<Set<Point2D>> GET_D_DESTINATION_PTS = () -> Set.of(
                new Point2D.Double(D_X,1),
                new Point2D.Double(D_X,2),
                new Point2D.Double(D_X,3),
                new Point2D.Double(D_X,4)
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

        public static double getOccupantX(Occupant o) {
            return switch (o) {
                case A -> A_X;
                case B -> B_X;
                case C -> C_X;
                case D -> D_X;
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
