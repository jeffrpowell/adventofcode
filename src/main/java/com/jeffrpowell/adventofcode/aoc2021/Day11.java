package com.jeffrpowell.adventofcode.aoc2021;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.algorithms.Grid;
import com.jeffrpowell.adventofcode.algorithms.Point2DUtils;
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
        grid = Grid.generatePointStream(0, 0, rightBoundary + 1, bottomBoundary + 1).collect(Collectors.toMap(
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
        
        while(checkForFlashes()) {}
        return grid.entrySet().stream().filter(entry -> entry.getValue() == 0).map(Map.Entry::getKey).count();
    }

    private boolean checkForFlashes() {
        List<Point2D> impacted = new ArrayList<>();
        List<Point2D> reset = new ArrayList<>();
        for (Map.Entry<Point2D, Integer> entry : grid.entrySet()) {
            if (entry.getValue() > 9) {
                reset.add(entry.getKey());
                impacted.addAll(Point2DUtils.getBoundedAdjacentPts(entry.getKey(), 0, rightBoundary, bottomBoundary, 0, true, true).stream()
                    .filter(this::notAlreadyFlashed)
                    .collect(Collectors.toList()));
            }
        }
        for (Point2D flash : reset) {
            grid.put(flash, 0);
        }
        for (Point2D neighbor : impacted) {
            grid.put(neighbor, grid.get(neighbor) + 1);
        }
        return !reset.isEmpty();
    }

    private boolean notAlreadyFlashed(Point2D pt) {
        int level = grid.get(pt);
        return 0 < level && level < 10;
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        rightBoundary = input.get(0).size() - 1;
        bottomBoundary = input.size() - 1;
        grid = Grid.generatePointStream(0, 0, rightBoundary + 1, bottomBoundary + 1).collect(Collectors.toMap(
            Function.identity(),
            pt -> input.get(Double.valueOf(pt.getX()).intValue()).get(Double.valueOf(pt.getY()).intValue())
        ));
        long flashes = 0;
        long steps = 0;
        while (flashes != grid.size()) {
            flashes = step();
            steps++;
        }
        return Long.toString(steps);
    }
    
    
}
