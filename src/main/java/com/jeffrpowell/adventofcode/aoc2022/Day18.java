package com.jeffrpowell.adventofcode.aoc2022;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.Point3DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

import javafx.geometry.Point3D;

public class Day18 extends Solution2022<Rule>{

    @Override
    public int getDay() {
        return 18;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("(\\d+,\\d+,\\d+)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        Set<Point3D> cubes = input.stream().map(r -> r.getPoint3D(0)).collect(Collectors.toSet());
        return Integer.toString(cubes.stream()
            .map(pt -> Point3DUtils.getAdjacentPts(pt).stream()
                .filter(p -> !cubes.contains(p))
                .collect(Collectors.toSet())
            )
            .map(Set::size)
            .collect(Collectors.reducing(0, Math::addExact)));
    }

    @Override
    protected String part2(List<Rule> input) {
        Set<Point3D> cubes = input.stream().map(r -> r.getPoint3D(0)).collect(Collectors.toSet());
        Point3DUtils.BoundingBox boundary = Point3DUtils.getBoundingBox(cubes);
        return Integer.toString(cubes.stream()
            .map(pt -> Point3DUtils.getAdjacentPts(pt).stream()
                .filter(p -> !cubes.contains(p))
                .filter(p -> exposed(p, cubes, boundary))
                .collect(Collectors.toSet())
            )
            .map(Set::size)
            .collect(Collectors.reducing(0, Math::addExact)));
    }

    record Traversal(Point3D head, Set<Point3D> visited, double distance){};

    private boolean exposed(Point3D pt, Set<Point3D> cubes, Point3DUtils.BoundingBox boundary) {
        PriorityQueue<Traversal> q = new PriorityQueue<>(Comparator.comparing(Traversal::distance).reversed());
        q.add(new Traversal(pt, Collections.emptySet(), 0));
        while (!q.isEmpty()) {
            Traversal t = q.poll();
            if (!Point3DUtils.pointInsideBoundary(pt, true, boundary)) {
                return true;
            }
            Point3DUtils.getAdjacentPts(pt, false).stream()
                .filter(p -> !cubes.contains(p))
                .map(p -> new Traversal(p, Stream.of(Set.of(p), t.visited()).flatMap(Set::stream).collect(Collectors.toSet()), t.distance() + 1))
                .forEach(q::add);
        }
        return false;
    }

}
