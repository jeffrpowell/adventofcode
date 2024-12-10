package com.jeffrpowell.adventofcode.aoc2024;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.Grid;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day8 extends Solution2024<List<String>>{

    @Override
    public int getDay() {
        return 8;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        Grid<String> grid = new Grid<>(input);
        Map<String, List<Point2D>> signals = grid.entrySet().stream()
            .filter(e -> !e.getValue().equals("."))
            .collect(Collectors.toMap(
                Map.Entry::getValue,
                e -> List.of(e.getKey()),
                (a, b) -> Stream.concat(a.stream(), b.stream()).collect(Collectors.toList())
            ));
        return Integer.toString(
            signals.values().stream()
                .map(v -> findAntinodes(grid.inclusiveBoundingBox, v))
                .flatMap(Set::stream)
                .collect(Collectors.toSet())
                .size()
        );
    }

    private Set<Point2D> findAntinodes(Point2DUtils.BoundingBox bb, List<Point2D> signals) {
        Set<Point2D> antinodes = new HashSet<>();
        for (int i = 0; i < signals.size() - 1; i++) {
            for (int j = i+1; j < signals.size(); j++) {
                Point2D vector = Point2DUtils.getVector(signals.get(i), signals.get(j));
                Point2D antinode = Point2DUtils.applyVectorToPt(vector, signals.get(j));
                if (Point2DUtils.pointInsideBoundary(antinode, true, bb)) {
                    antinodes.add(antinode);
                }
                vector = Point2DUtils.getVector(signals.get(j), signals.get(i));
                antinode = Point2DUtils.applyVectorToPt(vector, signals.get(i));
                if (Point2DUtils.pointInsideBoundary(antinode, true, bb)) {
                    antinodes.add(antinode);
                }
            }
        }
        return antinodes;
    }

    private Set<Point2D> findAntinodesPart2(Point2DUtils.BoundingBox bb, List<Point2D> signals) {
        if (signals.size() == 1) {
            return Collections.emptySet();
        }
        Set<Point2D> antinodes = signals.stream().collect(Collectors.toSet());
        for (int i = 0; i < signals.size() - 1; i++) {
            for (int j = i+1; j < signals.size(); j++) {
                Point2D vector = Point2DUtils.getVector(signals.get(i), signals.get(j));
                Point2D antinode = Point2DUtils.applyVectorToPt(vector, signals.get(j));
                while (Point2DUtils.pointInsideBoundary(antinode, true, bb)) {
                    antinodes.add(antinode);
                    antinode = Point2DUtils.applyVectorToPt(vector, antinode);
                }
                vector = Point2DUtils.getVector(signals.get(j), signals.get(i));
                antinode = Point2DUtils.applyVectorToPt(vector, signals.get(i));
                while (Point2DUtils.pointInsideBoundary(antinode, true, bb)) {
                    antinodes.add(antinode);
                    antinode = Point2DUtils.applyVectorToPt(vector, antinode);
                }
            }
        }
        return antinodes;
    }

    @Override
    protected String part2(List<List<String>> input) {
        Grid<String> grid = new Grid<>(input);
        Map<String, List<Point2D>> signals = grid.entrySet().stream()
            .filter(e -> !e.getValue().equals("."))
            .collect(Collectors.toMap(
                Map.Entry::getValue,
                e -> List.of(e.getKey()),
                (a, b) -> Stream.concat(a.stream(), b.stream()).collect(Collectors.toList())
            ));
        return Integer.toString(
            signals.values().stream()
                .map(v -> findAntinodesPart2(grid.inclusiveBoundingBox, v))
                .flatMap(Set::stream)
                .collect(Collectors.toSet())
                .size()
        );
    }
}
