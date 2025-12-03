package com.jeffrpowell.adventofcode.aoc2015;

import java.util.List;

import com.jeffrpowell.adventofcode.algorithms.Grid;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

import java.awt.geom.Point2D;

public class Day18 extends Solution2015<List<String>>{
    @Override
    public int getDay() {
        return 18;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        Grid<Boolean> g = new Grid<>(input, (gridChars, pt) -> gridChars.get(Grid.d2i(pt.getY())).get(Grid.d2i(pt.getX())).equals("#"));
        for (int i = 0; i < 100; i++) {
            g = iterate(g, false);
        }
        return Long.toString(g.values().stream().filter(v -> v).count());
    }

    @Override
    protected String part2(List<List<String>> input) {
        Grid<Boolean> g = new Grid<>(input, (gridChars, pt) -> gridChars.get(Grid.d2i(pt.getY())).get(Grid.d2i(pt.getX())).equals("#"));
        for (int i = 0; i < 100; i++) {
            g = iterate(g, true);
        }
        return Long.toString(g.values().stream().filter(v -> v).count());
    }

    private Grid<Boolean> iterate(Grid<Boolean> g, boolean part2) {
        Grid<Boolean> newGrid = g.copy();
        g.entrySet().stream()
            .forEach(entry -> {
                Point2D pt = entry.getKey();
                Boolean val = entry.getValue();
                int onNeighbors = 0;
                for (double row = pt.getY() - 1; row <= pt.getY() + 1; row++) {
                    for (double col = pt.getX() - 1; col <= pt.getX() + 1; col++) {
                        if (row == pt.getY() && col == pt.getX()) {
                            continue;
                        }
                        Boolean neighborVal = g.get(new Point2D.Double(col, row));
                        if (neighborVal != null && neighborVal) {
                            onNeighbors++;
                        }
                    }
                }
                if (val) {
                    // On
                    if (onNeighbors != 2 && onNeighbors != 3) {
                        newGrid.put(pt, false);
                    }
                } else {
                    // Off
                    if (onNeighbors == 3) {
                        newGrid.put(pt, true);
                    }
                }
            });
        if (part2) {
            // Set corners to on
            newGrid.put(g.inclusiveBoundingBox.min(), true);
            newGrid.put(new Point2D.Double(0, g.inclusiveBoundingBox.max().getY()), true);
            newGrid.put(new Point2D.Double(g.inclusiveBoundingBox.max().getX(), 0), true);
            newGrid.put(g.inclusiveBoundingBox.max(), true);
        }
        return newGrid;
    }
}
