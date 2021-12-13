package com.jeffrpowell.adventofcode.aoc2021;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day11 extends Solution2021<List<Integer>>{
    Map<Point2D, Integer> grid;
    int rightBoundary = 0;
    int bottomBoundary = 0;
    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser("");
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        rightBoundary = input.get(0).size() - 1;
        bottomBoundary = input.size() - 1;
        grid = Point2DUtils.generateGrid(0, 0, rightBoundary, bottomBoundary).collect(Collectors.toMap(
            Function.identity(),
            pt -> input.get(Double.valueOf(pt.getX()).intValue()).get(Double.valueOf(pt.getY()).intValue())
        ));
        long flashes = 0;
        for (int i = 0; i < 100; i++) {
            flashes += step();
        }
        return Long.toString(flashes);
    }

    private long step() {
        grid = grid.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue() + 1
            ));
        List<Point2D> impacted = new ArrayList<>();
        for (Map.Entry<Point2D, Integer> entry : grid.entrySet()) {
            if (entry.getValue() >= 10) {
                impacted.addAll(Point2DUtils.getBoundedAdjacentPts(entry.getKey(), 0, rightBoundary, bottomBoundary, 0, true, true).stream()
                    .filter(pt -> grid.get(pt) != 0)
                    .collect(Collectors.toList()));
            }
        }

        while(checkForFlashes()) {}
        Set<Point2D> flashes = grid.entrySet().stream().filter(entry -> entry.getValue() > 9).map(Map.Entry::getKey).collect(Collectors.toSet());
        flashes.stream().forEach(pt -> grid.put(pt, 0));
        return flashes.size();
    }

    private boolean checkForFlashes() {
        List<Point2D> changesMade = new ArrayList<>();
        grid.entrySet().stream()
            .filter(entry -> entry.getValue() == 10)
            .map(entry -> Point2DUtils.getBoundedAdjacentPts(entry.getKey(), 0, rightBoundary, bottomBoundary, 0, true, true))
            .flatMap(Set::stream)
            .peek(changesMade::add)
            .forEach(pt -> grid.compute(pt, (k, v) -> v < 10 ? v + 1 : v));
        return !changesMade.isEmpty();
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
