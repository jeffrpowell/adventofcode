package com.jeffrpowell.adventofcode.aoc2022;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day12 extends Solution2022<List<String>>{

    static Point2D start = new Point2D.Double(0, 0);
    static Point2D end = new Point2D.Double(0, 0);
    @Override
    public int getDay() {
        return 12;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        int width = input.get(0).size();
        int height = input.size();
        
        Map<Point2D, Integer> grid = Point2DUtils.generateGrid(0, 0, width, height)
            .collect(Collectors.toMap(
                Function.identity(),
                pt -> {
                    String c = input.get(d2i(pt.getY())).get(d2i(pt.getX()));
                    if (c.equals("S")){
                        start = pt;
                        return 0;
                    }
                    else if (c.equals("E")){
                        end = pt;
                        return 25;
                    }
                    else {
                        return c.charAt(0) - 'a';
                    }
                }
            ));
        PriorityQueue<Branch> q = new PriorityQueue<>(Comparator.comparing(Branch::heuristic));
        q.add(new Branch(0, start, 0));
        Set<Point2D> visited = new HashSet<>();
        int maxElevation = 0;
        while(!q.isEmpty()) {
            Branch b = q.poll();
            if (visited.contains(b.head)) {
                continue;
            }
            if (b.head.equals(end)) {
                return Integer.toString(b.distance);
            }
            if (b.elevation > maxElevation) {
                maxElevation = b.elevation;
                System.out.println("Reached elevation " + maxElevation + " at " + b.head);
            }
            visited.add(b.head);
            Point2DUtils.getBoundedAdjacentPts(b.head, 0, width-1, height-1, 0, true, false)
                .stream()
                .filter(pt -> Math.abs(grid.get(pt) - b.elevation) <= 1)
                .map(pt -> new Branch(b.distance + 1, pt, grid.get(pt)))
                .forEach(q::add);
        }
        return null;
    }

    private int d2i(Double d) {
        return d.intValue();
    }

    @Override
    protected String part2(List<List<String>> input) {
        return null;
    }

    private static class Branch {
        private int distance;
        private Point2D head;
        private int elevation;
        public Branch(int distance, Point2D head, int elevation) {
            this.distance = distance;
            this.head = head;
            this.elevation = elevation;
        }

        public double heuristic() {
            return distance + head.distance(end);
        }
    } 

}
