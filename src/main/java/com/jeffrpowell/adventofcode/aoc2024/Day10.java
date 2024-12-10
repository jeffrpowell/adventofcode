package com.jeffrpowell.adventofcode.aoc2024;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.Grid;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day10 extends Solution2024<List<Integer>>{

    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser("");
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        Grid<Integer> grid = new Grid<>(input);
        Map<Point2D, Set<Point2D>> trailHeads = grid.entrySet().stream()
            .filter(e -> e.getValue().equals(0))
            .map(Map.Entry::getKey)
            .collect(Collectors.toMap(
                Function.identity(),
                Set::of
            ));
        for (int i = 1; i < 10; i++) {
            final int nextHeight = i;
            for (Map.Entry<Point2D, Set<Point2D>> trailTailEntries : trailHeads.entrySet()) {
                Set<Point2D> nextSteps = new HashSet<>();
                for (Point2D tail : trailTailEntries.getValue()) {
                    Point2DUtils.getBoundedAdjacentPts(tail, grid.inclusiveBoundingBox, true, false).stream()
                        .filter(next -> grid.get(next).equals(nextHeight))
                        .forEach(nextSteps::add);
                }
                trailHeads.put(trailTailEntries.getKey(), nextSteps);
            }
        }
        return Integer.toString(trailHeads.values().stream().map(Set::size).reduce(0, Math::addExact));
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        Grid<Integer> grid = new Grid<>(input);
        Map<Point2D, List<Point2D>> trailHeads = grid.entrySet().stream()
            .filter(e -> e.getValue().equals(0))
            .map(Map.Entry::getKey)
            .collect(Collectors.toMap(
                Function.identity(),
                List::of
            ));
        for (int i = 1; i < 10; i++) {
            final int nextHeight = i;
            for (Map.Entry<Point2D, List<Point2D>> trailTailEntries : trailHeads.entrySet()) {
                List<Point2D> nextSteps = new ArrayList<>();
                for (Point2D tail : trailTailEntries.getValue()) {
                    Point2DUtils.getBoundedAdjacentPts(tail, grid.inclusiveBoundingBox, true, false).stream()
                        .filter(next -> grid.get(next).equals(nextHeight))
                        .forEach(nextSteps::add);
                }
                trailHeads.put(trailTailEntries.getKey(), nextSteps);
            }
        }
        return Integer.toString(trailHeads.values().stream().map(List::size).reduce(0, Math::addExact));
    }
}
