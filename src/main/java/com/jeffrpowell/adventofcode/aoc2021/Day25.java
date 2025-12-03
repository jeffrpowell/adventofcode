package com.jeffrpowell.adventofcode.aoc2021;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.algorithms.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day25 extends Solution2021<List<String>> {
    Map<Point2D, Occupant> grid;
    Set<Point2D> unlockedPts;
    boolean changed = true;
    int maxX; 
    int maxY;

    @Override
    public int getDay() {
        return 25;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        grid = new HashMap<>();
        unlockedPts = new HashSet<>();
        maxX = input.get(0).size() - 1;
        maxY = input.size() - 1;
        for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.get(0).size(); x++) {
                String value = input.get(y).get(x);
                Occupant o = Occupant.fromString(value);
                Point2D pt = new Point2D.Double(x, y);
                grid.put(pt, o);
                if (o == Occupant.EMPTY) {
                    unlockedPts.add(pt);
                }
            }
        }
        long i = 0;
        while(changed) {
            i++;
            changed = false;
            stepEast();
            stepSouth();
        }
        return Long.toString(i);
    }

    private void stepEast() {
        Map<Point2D, Point2D> moves = new HashMap<>();
        for (Point2D dest : unlockedPts) {
            Point2D src = getWesternNeighbor(dest);
            if (grid.get(src) == Occupant.EAST) {
                moves.put(src, dest);
            }
        }
        moves.entrySet().stream().forEach(entry -> {
            grid.put(entry.getKey(), Occupant.EMPTY);
            grid.put(entry.getValue(), Occupant.EAST);
        });
        unlockedPts.removeAll(moves.values());
        unlockedPts.addAll(moves.keySet());
        if (!moves.isEmpty()) {
            changed = true;
        }
    }

    private void stepSouth() {
        Map<Point2D, Point2D> moves = new HashMap<>();
        for (Point2D dest : unlockedPts) {
            Point2D src = getNorthernNeighbor(dest);
            if (grid.get(src) == Occupant.SOUTH) {
                moves.put(src, dest);
            }
        }
        moves.entrySet().stream().forEach(entry -> {
            grid.put(entry.getKey(), Occupant.EMPTY);
            grid.put(entry.getValue(), Occupant.SOUTH);
        });
        unlockedPts.removeAll(moves.values());
        unlockedPts.addAll(moves.keySet());
        if (!moves.isEmpty()) {
            changed = true;
        }
    }

    @Override
    protected String part2(List<List<String>> input) {
        // TODO Auto-generated method stub
        return null;
    }
    
    enum Occupant {
        EMPTY, EAST, SOUTH;

        public static Occupant fromString(String value) {
            if (value.equals("v")) {
                return Occupant.SOUTH;
            }
            else if (value.equals(">")) {
                return Occupant.EAST;
            }
            else {
                return Occupant.EMPTY;
            }
        }
    }

    private Stream<Point2D> getNorthernNeighbors(Set<Point2D> pts) {
        return pts.stream().map(this::getNorthernNeighbor);
    }

    private Point2D getNorthernNeighbor(Point2D pt) {
        Point2D p = Point2DUtils.applyVectorToPt(new Point2D.Double(0, -1), pt);
        if (p.getY() < 0) {
            p.setLocation(p.getX(), maxY);
        }
        return p;
    }

    private Stream<Point2D> getWesternNeighbors(Set<Point2D> pts) {
        return pts.stream().map(this::getWesternNeighbor);
    }

    private Point2D getWesternNeighbor(Point2D pt) {
        Point2D p = Point2DUtils.applyVectorToPt(new Point2D.Double(-1, 0), pt);
        if (p.getX() < 0) {
            p.setLocation(maxX, p.getY());
        }
        return p;
    }
}
