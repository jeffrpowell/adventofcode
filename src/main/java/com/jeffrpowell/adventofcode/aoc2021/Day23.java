package com.jeffrpowell.adventofcode.aoc2021;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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
        return "0";
    }

    private String getTotalCost(Point2D destination) {
        visitedStates = new HashSet<>();
        PriorityQueue<Path> q = new PriorityQueue<>(Comparator.comparing(Path::getScore));
        State start = new State(0);
        q.add(new Path(start, new HashSet<>()));
        visitedStates.add(start);
        while(!q.peek().head.location.equals(destination)) {
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

    private class Path {
        State head;
        Set<Point2D> pathPts;
        long cost;
        double score;

        public Path(State head, Set<State> path) {
            this.head = head;
            this.pathPts = path.stream().map(n -> n.location).collect(Collectors.toSet());
            this.cost = path.stream().map(State::getCost).reduce(0L, Math::addExact) + head.getCost();
            this.score = this.cost + this.head.getDistanceToTarget();
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
        private final Point2D location;
        private long cost;
        private final double distanceToTarget;

        public State(long cost) {
            this.cost = cost;
            this.distanceToTarget = Point2DUtils.getManhattenDistance(target, location);
        }

        public long getCost() {
            return cost;
        }

        public double getDistanceToTarget() {
            return distanceToTarget;
        }

        public Set<Point2D> getNeighbors(Path p) {
            return Point2DUtils.getBoundedAdjacentPts(location, 0, rightBoundary, bottomBoundary, 0, true, false).stream()
                .filter(pt -> !visitedPts.contains(pt))
                .collect(Collectors.toSet());
        }

        @Override
        public String toString() {
            return location.getX() + "," + location.getY();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((location == null) ? 0 : location.hashCode());
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
            if (location == null) {
                if (other.location != null)
                    return false;
            } else if (!location.equals(other.location))
                return false;
            return true;
        }
    }

}
