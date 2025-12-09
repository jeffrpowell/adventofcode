package com.jeffrpowell.adventofcode.aoc2025;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    private List<Point2D> attemptSplit2(Point2D beamTip, Grid<WhatsThere> g) {
        if (g.get(beamTip) == WhatsThere.SPLITTER) {
            return List.of(SPLIT_LEFT.apply(beamTip), SPLIT_RIGHT.apply(beamTip));
        }
        else {
            return List.of(beamTip);
        }
    }

    record BeamStack(Point2D beamTip, int duplicateBeams){}

    @Override
    protected String part2(List<List<String>> input) {
        Grid<WhatsThere> g = new Grid<>(input, (rows, pt) -> WhatsThere.fromString(rows.get(Grid.d2i(pt.getY())).get(Grid.d2i(pt.getX()))));
        Point2D start = g.entrySet().stream().filter(entry -> entry.getValue() == WhatsThere.START).map(Map.Entry::getKey).findFirst().orElseThrow();
        Map<Point2D, Integer> stacks = new HashMap<>();
        stacks.put(start, 1);
        for (int i = 0; i < input.size(); i++) {
            Map<Point2D, Integer> tempStacks = new HashMap<>();
            for (Map.Entry<Point2D, Integer> entry : stacks.entrySet()) {
                Point2D downPt = MOVE_DOWN.apply(entry.getKey());
                tempStacks.put(downPt, stacks.getOrDefault(entry.getKey(), 0) + stacks.get(entry.getKey()));
                List<Point2D> split = attemptSplit2(downPt, g);
                if (split.size() > 1) {
                    int incomingBeamSize = tempStacks.get(downPt);
                    for (Point2D splitPt : split) {
                        tempStacks.put(splitPt, stacks.getOrDefault(splitPt, 0) + incomingBeamSize);
                    }
                }
            }
            stacks.putAll(tempStacks);
        }
        return Integer.toString(stacks.entrySet().stream()
            .filter(entry -> entry.getKey().getY() == input.size() - 1)
            .mapToInt(Map.Entry::getValue)
            .sum()
        );
    }
}
