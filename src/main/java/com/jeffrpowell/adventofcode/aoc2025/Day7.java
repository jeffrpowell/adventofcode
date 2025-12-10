package com.jeffrpowell.adventofcode.aoc2025;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.algorithms.Grid;
import com.jeffrpowell.adventofcode.algorithms.PointTransform;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day7 extends Solution2025<List<String>>{
    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    private enum WhatsThere{START, SPLITTER, OPEN;

        static WhatsThere fromString(String s) {
            return switch(s) {
                case "." -> OPEN;
                case "^" -> SPLITTER;
                default -> START;
            };
        }
    }

    private static final PointTransform MOVE_DOWN = new PointTransform(0, 1);
    private static final PointTransform SPLIT_LEFT = new PointTransform(-1, 0);
    private static final PointTransform SPLIT_RIGHT = new PointTransform(1, 0);

    @Override
    protected String part1(List<List<String>> input) {
        Grid<WhatsThere> g = new Grid<>(input, (rows, pt) -> WhatsThere.fromString(rows.get(Grid.d2i(pt.getY())).get(Grid.d2i(pt.getX()))));
        Set<Point2D> beamTips = new HashSet<>();
        beamTips.add(g.entrySet().stream().filter(entry -> entry.getValue() == WhatsThere.START).map(Map.Entry::getKey).findFirst().orElseThrow());
        List<Integer> counterContainer = new ArrayList<>();
        counterContainer.add(0);
        for (int i = 0; i < input.size(); i++) {
            beamTips = beamTips.stream()
                .map(pt -> MOVE_DOWN.apply(pt))
                .map(pt -> attemptSplit(pt, g))
                .peek(ptSet -> {
                    if (ptSet.size() == 2) {
                        counterContainer.set(0, counterContainer.get(0) + 1);
                    }
                })
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        }
        return Integer.toString(counterContainer.get(0));
    }

    private Set<Point2D> attemptSplit(Point2D beamTip, Grid<WhatsThere> g) {
        if (g.get(beamTip) == WhatsThere.SPLITTER) {
            return Set.of(SPLIT_LEFT.apply(beamTip), SPLIT_RIGHT.apply(beamTip));
        }
        else {
            return Set.of(beamTip);
        }
    }

    record BeamStack(Point2D beamTip, int duplicateBeams){}

    @Override
    protected String part2(List<List<String>> input) {
        Grid<WhatsThere> g = new Grid<>(input, (rows, pt) -> 
            WhatsThere.fromString(
                rows.get(Grid.d2i(pt.getY())).get(Grid.d2i(pt.getX()))
            ));
        List<Point2D> splitters = g.entrySet().stream()
            .filter(entry -> entry.getValue() == WhatsThere.SPLITTER)
            .map(Map.Entry::getKey)
            .sorted(this::compareSplitterPoints)
            .collect(Collectors.toList());
        int maxSplitterX = splitters.stream()
            .mapToDouble(Point2D::getX)
            .mapToInt(d -> Double.valueOf(d).intValue())
            .max().orElseThrow();
        List<Long> beamColumns = Stream.generate(() -> 0L).limit(maxSplitterX + 2).collect(Collectors.toList());
        Point2D start = g.entrySet().stream().filter(entry -> entry.getValue() == WhatsThere.START).map(Map.Entry::getKey).findFirst().orElseThrow();
        beamColumns.set(d2i(start.getX()), 1L);
        for (Point2D splitter : splitters) {
            int column = d2i(splitter.getX());
            long priorBeams = beamColumns.get(column);
            if (priorBeams > 0L) {
                beamColumns.set(column, 0L);
                beamColumns.set(column-1, priorBeams + beamColumns.get(column-1));
                beamColumns.set(column+1, priorBeams + beamColumns.get(column+1));
            }
        }
        return Long.toString(beamColumns.stream().reduce(0L, Math::addExact));
    }

    private int d2i (Double d) {
        return d.intValue();
    }

    private int compareSplitterPoints(Point2D pt1, Point2D pt2) {
        int c = Double.compare(pt1.getY(), pt2.getY());
        if (c == 0) {
            c = Double.compare(pt1.getX(), pt2.getX());
        }
        return c;
    }
}
