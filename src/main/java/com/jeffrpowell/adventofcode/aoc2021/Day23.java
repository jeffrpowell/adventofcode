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
import java.util.function.Function;
import java.util.stream.Collectors;

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
        for (List<String> line : input) {
            for (String c : line) {
                if (Occupant.ALL_TYPES.contains(c)) {
                    grid.put(startPositions.remove(0), Occupant.valueOf(c));
                }
            }
        }
        return getTotalCost(grid);
    }

    private String getTotalCost(Map<Point2D, Occupant> grid) {
        visitedStates = new HashSet<>();
        PriorityQueue<State> q = new PriorityQueue<>(Comparator.comparing(State::getScore));
        q.add(new State(0, grid));
        while(!q.isEmpty()) {
            State current = q.poll();
            visitedStates.add(current);
            current.getNeighbors().forEach(q::add);
        }
        return Long.toString(visitedStates.stream()
            .filter(Day23.State::isDone)
            .map(Day23.State::getCost)
            .min(Comparator.naturalOrder())
            .get()
        );
    }

    private long d2l(double d) {
        return Double.valueOf(d).longValue();
    }

    enum Occupant {
        A(1), B(10), C(100), D(1000);

        static Set<String> ALL_TYPES = Set.of("A","B","C","D");

        private int coefficient;

        Occupant(int coefficient) {
            this.coefficient = coefficient;
        }

        public int getCoefficient() {
            return coefficient;
        }
    } 

    public class State {
        private long cost;
        private Map<Point2D, Occupant> grid;
        private final int distanceToGoal;
        private final long score;
        private final Map<Occupant, Optional<Point2D>> deepestOpenPts;

        public State(long cost, Map<Point2D, Occupant> grid) {
            this.cost = cost;
            this.grid = grid;
            this.distanceToGoal = calculateDistanceToGoal();
            this.score = this.cost + this.distanceToGoal;
            this.deepestOpenPts = Arrays.stream(Occupant.values()).collect(Collectors.toMap(Function.identity(), o -> getDeepestOpenPoint(o)));
        }

        public long getCost() {
            return cost;
        }

        public long getScore() {
            return score;
        }

        public boolean isDone() {
            return distanceToGoal == 0;
        }

        public int getDistanceToGoal() {
            return distanceToGoal;
        }

        private int calculateDistanceToGoal() {
            int total = 0;
            for (Occupant o : Occupant.values()) {
                for (Point2D pt : PointUtilities.getDestinationPts(o)) {
                    if (!grid.containsKey(pt)) {
                        continue;
                    }
                    else if (grid.get(pt) == o) {
                        total--;
                    }
                    else {
                        total++;
                    }
                }
            }
            return total + 16;
        }

        public Set<State> getNeighbors() {
            Set<State> states = new HashSet<>();
            for (Occupant o : Occupant.values()) {
                if (columnFilled(o)) {
                    continue;
                }
                if (columnReadyToFill(o)) {
                    Point2D dest = deepestOpenPts.get(o).get();
                    findValidFillerForColumn(o).stream()
                        .map(src -> movePtToPt(grid, src, dest))
                        .forEach(states::add);
                }
                else {
                    Point2D src = deepestOpenPts.get(o).orElse(new Point2D.Double(PointUtilities.getOccupantX(o), 0));
                    if (src.getY() < 4) {
                        src = Point2DUtils.applyVectorToPt(new Point2D.Double(0, 1), src);
                    }
                    final Point2D finalSrc = src;
                    findOpenHallwayFromColumn(o).stream()
                        .map(dest -> movePtToPt(grid, finalSrc, dest))
                        .forEach(states::add);
                }
            }
            return Sets.difference(states, visitedStates);
        }

        //null return means that the column is full
        private Optional<Point2D> getDeepestOpenPoint(Occupant o) {
            return Sets.difference(PointUtilities.getDestinationPts(o), grid.keySet()).stream().sorted(Comparator.comparing(Point2D::getY).reversed()).findFirst();
        }

        private boolean columnFilled(Occupant o) {
            return PointUtilities.getDestinationPts(o).stream().allMatch(pt -> grid.containsKey(pt) && grid.get(pt) == o);
        }

        private boolean columnReadyToFill(Occupant o) {
            return PointUtilities.getDestinationPts(o).stream().map(grid::get).allMatch(v -> v == null || v == o)
                && deepestOpenPts.get(o).isPresent();
        }

        private Set<Point2D> findOpenHallwayFromColumn(Occupant o) {
            double x = PointUtilities.getOccupantX(o);
            Point2D left = new Point2D.Double(x - 1, 0);
            Point2D right = new Point2D.Double(x + 1, 0);
            Set<Point2D> openHallway = new HashSet<>();
            while (left.getX() >= 0) {
                if (!grid.containsKey(left)) {
                    openHallway.add(left);
                    left = Point2DUtils.applyVectorToPt(new Point2D.Double(left.getX() == 1.0 ? -1 : -2, 0), left);
                }
                else {
                    break;
                }
            }
            while (right.getX() <= 10) {
                if (!grid.containsKey(right)) {
                    openHallway.add(right);
                    right = Point2DUtils.applyVectorToPt(new Point2D.Double(right.getX() == 9.0 ? 1 : 2, 0), right);
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
                    left = Point2DUtils.applyVectorToPt(new Point2D.Double(left.getX() == 1.0 ? -1 : -2, 0), left);
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
                    right = Point2DUtils.applyVectorToPt(new Point2D.Double(right.getX() == 9.0 ? 1 : 2, 0), right);
                }
                else {
                    if (grid.get(right) == o) {
                        valid.add(right);
                    }
                    break;
                }
            }
            return valid;
        }

        private State movePtToPt(Map<Point2D, Occupant> grid, Point2D src, Point2D dest) {
            Map<Point2D, Occupant> newGrid = new HashMap<>(grid);
            Occupant o = newGrid.remove(src);
            newGrid.put(dest, o);
            long newCost = cost + d2l(Point2DUtils.getManhattenDistance(src, dest) * o.getCoefficient());
            return new State(newCost, newGrid);
        }

        @Override
        public String toString() {
            return cost + "\n" + Point2DUtils.pointsToString(grid.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().name())), ".");
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + ((grid == null) ? 0 : grid.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            State other = (State) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (grid == null) {
                if (other.grid != null)
                    return false;
            } else if (!grid.equals(other.grid))
                return false;
            return true;
        }

        private Day23 getEnclosingInstance() {
            return Day23.this;
        }
    }

    public static class PointUtilities {
        private PointUtilities() {}

        static final double A_X = 2;
        static final double B_X = 4;
        static final double C_X = 6;
        static final double D_X = 8;

        static final Set<Point2D> A_DESTINATION_PTS = Set.of(
                new Point2D.Double(A_X,1),
                new Point2D.Double(A_X,2),
                new Point2D.Double(A_X,3),
                new Point2D.Double(A_X,4)
            );
        static final Set<Point2D> B_DESTINATION_PTS = Set.of(
                new Point2D.Double(B_X,1),
                new Point2D.Double(B_X,2),
                new Point2D.Double(B_X,3),
                new Point2D.Double(B_X,4)
            );
        static final Set<Point2D> C_DESTINATION_PTS = Set.of(
                new Point2D.Double(C_X,1),
                new Point2D.Double(C_X,2),
                new Point2D.Double(C_X,3),
                new Point2D.Double(C_X,4)
            );
        static final Set<Point2D> D_DESTINATION_PTS = Set.of(
                new Point2D.Double(D_X,1),
                new Point2D.Double(D_X,2),
                new Point2D.Double(D_X,3),
                new Point2D.Double(D_X,4)
            );
        
        public static Set<Point2D> getDestinationPts(Occupant o) {
            return switch (o) {
                case A -> A_DESTINATION_PTS;
                case B -> B_DESTINATION_PTS;
                case C -> C_DESTINATION_PTS;
                case D -> D_DESTINATION_PTS;
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
    }

}
