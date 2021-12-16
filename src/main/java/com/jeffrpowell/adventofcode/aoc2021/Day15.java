package com.jeffrpowell.adventofcode.aoc2021;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day15 extends Solution2021<List<Integer>> {
    Map<Point2D, Node> grid;
    Set<Point2D> visitedPts;

    @Override
    public int getDay() {
        return 15;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser("");
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        Point2D destination = new Point2D.Double(input.get(0).size() - 1, input.size() - 1);
        grid = Point2DUtils.generateGrid(0, 0, input.get(0).size(), input.size())
            .collect(Collectors.toMap(
                Function.identity(), 
                pt -> new Node(pt, input.get(i2d(pt.getY())).get(i2d(pt.getX())), destination)
            ));
        return getTotalRisk(destination);
    }
    
    @Override
    protected String part2(List<List<Integer>> input) {
        Point2D destination = new Point2D.Double(input.get(0).size() * 5 - 1, input.size() * 5 - 1);
        grid = Point2DUtils.generateGrid(0, 0, input.get(0).size(), input.size())
            .collect(Collectors.toMap(
                Function.identity(), 
                pt -> new Node(pt, input.get(i2d(pt.getY())).get(i2d(pt.getX())), destination)
            ));
        int blockWidth = input.get(0).size();
        int blockHeight = input.size();
        Map<Point2D, Node> tempGrid = new HashMap<>();
        for(Map.Entry<Point2D, Node> entry : grid.entrySet()) {
            for (int blockX = 0; blockX < 5; blockX++) {
                for(int blockY = 0; blockY < 5; blockY++) {
                    if (blockY == 0 && blockX == 0) {
                        continue;
                    }
                    int width = blockWidth * blockX;
                    int height = blockHeight * blockY;
                    long risk = Math.max(1, (entry.getValue().getRisk() + blockX + blockY) % 10);
                    Point2D nextLocation = Point2DUtils.applyVectorToPt(new Point2D.Double(width, height), entry.getKey());
                    tempGrid.put(nextLocation, new Node(nextLocation, risk, destination));
                }
            }
        }
        tempGrid.putAll(grid);
        grid = tempGrid;
        return getTotalRisk(destination);
    }

    private String getTotalRisk(Point2D destination) {
        visitedPts = new HashSet<>();
        PriorityQueue<Path> q = new PriorityQueue<>(Comparator.comparing(Path::getScore));
        Node start = grid.get(new Point2D.Double(0, 0));
        start.risk = 0L;
        q.add(new Path(start, new HashSet<>()));
        visitedPts.add(start.location);
        while(!q.peek().head.location.equals(destination)) {
            Path current = q.poll();
            visitedPts.add(current.head.location);
            current.getNewPaths().forEach(q::add);
        }
        Path winner = q.poll();
        return Long.toString(winner.getRisk());
    }

    private int i2d(double d) {
        return Double.valueOf(d).intValue();
    }

    private class Path {
        Node head;
        Set<Point2D> pathPts;
        long risk;
        double score;

        public Path(Node head, Set<Node> path) {
            this.head = head;
            this.pathPts = path.stream().map(n -> n.location).collect(Collectors.toSet());
            this.risk = path.stream().map(Node::getRisk).reduce(0L, Math::addExact) + head.getRisk();
            this.score = this.risk + this.head.getDistanceToTarget();
        }

        public Stream<Path> getNewPaths() {
            return head.getNeighbors(this).stream()
                .map(grid::get)
                .map(newHead -> new Path(
                    newHead, 
                    Stream.concat(pathPts.stream().map(grid::get), Stream.of(head)).collect(Collectors.toSet())
                ));
        }

        public long getRisk() {
            return risk;
        }

        public double getScore() {
            return score;
        }

        @Override
        public String toString() {
            return head.toString() + ": +" + pathPts.size() + " (" + risk + ")";
        }
    }

    private class Node {
        private final Point2D location;
        private long risk;
        private final double distanceToTarget;
        private final double rightBoundary;
        private final double bottomBoundary;

        public Node(Point2D location, long risk, Point2D target) {
            this.location = location;
            this.risk = risk;
            this.distanceToTarget = Point2DUtils.getEuclideanDistance(target, location);
            this.rightBoundary = target.getX();
            this.bottomBoundary = target.getY();
        }

        public long getRisk() {
            return risk;
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
            Node other = (Node) obj;
            if (location == null) {
                if (other.location != null)
                    return false;
            } else if (!location.equals(other.location))
                return false;
            return true;
        }
    }
    
}
