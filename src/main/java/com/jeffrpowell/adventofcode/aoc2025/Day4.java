package com.jeffrpowell.adventofcode.aoc2025;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.algorithms.Grid;
import com.jeffrpowell.adventofcode.algorithms.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day4 extends Solution2025<List<String>>{
    @Override
    public int getDay() {
        return 4;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        Grid<Boolean> g = new Grid<>(input, (gridChars, pt) -> 
            gridChars.get(Grid.d2i(pt.getY())).get(Grid.d2i(pt.getX())).equals("@"));
        return Long.toString(g.entrySet().stream()
            .filter(entry -> entry.getValue())
            .map(Map.Entry::getKey)
            .filter(pt -> canBeRemoved(g, pt))
            .count());
    }

    private boolean canBeRemoved(Grid<Boolean> g, Point2D pt) {
        return Point2DUtils.getBoundedAdjacentPts(
                pt, 
                g.inclusiveBoundingBox, 
                true, 
                true).stream()
            .filter(neighbor -> Boolean.TRUE.equals(g.get(neighbor)))
            .count() < 4;
    }

    @Override
    protected String part2(List<List<String>> input) {
        Grid<Boolean> g = new Grid<>(input, (gridChars, pt) -> gridChars.get(Grid.d2i(pt.getY())).get(Grid.d2i(pt.getX())).equals("@"));
        Set<Point2D> toRemove = new HashSet<>();
        toRemove = g.entrySet().stream()
            .filter(entry -> entry.getValue())
            .map(Map.Entry::getKey)
            .filter(pt -> canBeRemoved(g, pt))
            .collect(Collectors.toSet());
        long removedCount = 0;
        while (!toRemove.isEmpty()) {
            removedCount += toRemove.size();
            for (Point2D pt : toRemove) {
                g.put(pt, false);
            }
            toRemove = g.entrySet().stream()
                .filter(entry -> entry.getValue())
                .map(Map.Entry::getKey)
                .filter(pt -> canBeRemoved(g, pt))
                .collect(Collectors.toSet());
        }
        return Long.toString(removedCount);
    }
}
