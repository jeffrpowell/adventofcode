package com.jeffrpowell.adventofcode.aoc2024;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day18 extends Solution2024<Rule>{
    private static final Point2D START = new Point2D.Double(0, 0);
    private static final Point2D END = new Point2D.Double(70, 70);
    private static final long LIMIT = 1024;
    private static Point2D start;
    private static Point2D end;
    private static long limit;

    public Day18() {
        start = START;
        end = END;
        limit = LIMIT;
    }

    public Day18(Point2D _start, Point2D _end, long _limit) {
        start = _start;
        end = _end;
        limit = _limit;
    }

    @Override
    public int getDay() {
        return 18;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("(\\d+),(\\d+)"));
    }

    record WalkState(WalkState prior, Point2D pt, int distanceWalked) implements Comparable<WalkState> {
        private Double heuristic() {
            return distanceWalked + Point2DUtils.getEuclideanDistance(pt, end);
        }

        public long minStepsLeft() {
            return Point2DUtils.getManhattenDistance(pt, end);
        }

        public boolean nextPointWillBeSafe(Point2D next, List<Point2D> allCorruptedPts) {
            return !allCorruptedPts.contains(next);
            // This is what I thought the problem was going to be
            // would love to have an Indiana Jones kind of problem where there's an opening as you're running
            // return allCorruptedPts.stream()
            //     .limit(distanceWalked + 1)
            //     .noneMatch(next::equals);
        }

        @Override
        public int compareTo(WalkState o) {
            return heuristic().compareTo(o.heuristic());
        }

        public WalkState generateNext(Point2D next) {
            return new WalkState(this, next, distanceWalked + 1);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((pt == null) ? 0 : pt.hashCode());
            result = prime * result + distanceWalked;
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
            WalkState other = (WalkState) obj;
            if (pt == null) {
                if (other.pt != null)
                    return false;
            } else if (!pt.equals(other.pt))
                return false;
            if (distanceWalked != other.distanceWalked)
                return false;
            return true;
        }
        
    }

    @Override
    protected String part1(List<Rule> input) {
        Point2DUtils.BoundingBox bb = new Point2DUtils.BoundingBox(start, end);
        List<Point2D> allCorruptedPts = input.stream()
            .limit(limit)
            .map(r -> new Point2D.Double(r.getDouble(0), r.getDouble(1)))
            .collect(Collectors.toList());
        Set<Point2D> visited = new HashSet<>();
        PriorityQueue<WalkState> q = new PriorityQueue<>();
        q.add(new WalkState(null, start, 0));
        WalkState w = null;
        // int printThreshold = 100;
        while(!q.peek().pt().equals(end)) {
            w = q.poll();
            WalkState _w = w;
            visited.add(w.pt());
            // if (visited.size() > printThreshold) {
            //     printThreshold += 100;
            //     printGrid(allCorruptedPts, visited);
            // }
            Point2DUtils.getBoundedAdjacentPts(w.pt(), bb, true, false).stream()
                .filter(p -> !visited.contains(p) && _w.nextPointWillBeSafe(p, allCorruptedPts))
                .map(w::generateNext)
                .forEach(nextW -> {
                    if (!q.contains(nextW)) {
                        q.add(nextW);
                    }
                });
        }
        return Long.toString(w.distanceWalked()+1);
    }

    // private void printGrid(List<Point2D> allCorruptedPts, Set<Point2D> visited) {
    //     Map<Point2D, String> printMap = allCorruptedPts.stream().collect(Collectors.toMap(Function.identity(), p -> "#"));
    //     visited.stream().forEach(p -> printMap.put(p, "O"));
    //     Point2DUtils.printPoints(printMap);
    // }

    @Override
    protected String part2(List<Rule> input) {
        List<Point2D> startingCorruptedPts = input.stream()
            .limit(limit)
            .map(r -> new Point2D.Double(r.getDouble(0), r.getDouble(1)))
            .collect(Collectors.toList());
        List<Point2D> remainingCorruptedPts = input.stream()
            .skip(limit)
            .map(r -> new Point2D.Double(r.getDouble(0), r.getDouble(1)))
            .collect(Collectors.toList());
        WalkState w = findSolution(startingCorruptedPts);
        Set<Point2D> solution = new HashSet<>();
        solution.add(w.pt());
        while(w.prior() != null) {
            w = w.prior();
            solution.add(w.pt());
        }
        int loopTimes = remainingCorruptedPts.size();
        for (int i = 0; i < loopTimes; i++) {
            Point2D nextByte = remainingCorruptedPts.removeFirst();
            startingCorruptedPts.add(nextByte);
            if (solution.contains(nextByte)) {
                solution.clear();
                w = findSolution(startingCorruptedPts);
                if (w == null) {
                    return nextByte.getX() + "," + nextByte.getY(); //manually drop the ".0" at the end of each number
                }
                solution.add(w.pt());
                while(w.prior() != null) {
                    w = w.prior();
                    solution.add(w.pt());
                }
            }
        }
        return "Solution not found";
    }

    private WalkState findSolution(List<Point2D> startingCorruptedPts) {
        Point2DUtils.BoundingBox bb = new Point2DUtils.BoundingBox(start, end);
        Set<Point2D> visited = new HashSet<>();
        PriorityQueue<WalkState> q = new PriorityQueue<>();
        q.add(new WalkState(null, start, 0));
        WalkState w = null;
        // int printThreshold = 100;
        while(!q.isEmpty() && !q.peek().pt().equals(end)) {
            w = q.poll();
            WalkState _w = w;
            visited.add(w.pt());
            // if (visited.size() > printThreshold) {
            //     printThreshold += 100;
            //     printGrid(allCorruptedPts, visited);
            // }
            Point2DUtils.getBoundedAdjacentPts(w.pt(), bb, true, false).stream()
                .filter(p -> !visited.contains(p) && _w.nextPointWillBeSafe(p, startingCorruptedPts))
                .map(w::generateNext)
                .forEach(nextW -> {
                    if (!q.contains(nextW)) {
                        q.add(nextW);
                    }
                });
        }
        if (q.isEmpty()) {
            return null;
        }
        return q.poll();
    }
}
