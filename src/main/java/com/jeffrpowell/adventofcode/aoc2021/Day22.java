package com.jeffrpowell.adventofcode.aoc2021;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.algorithms.Point3DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

import javafx.geometry.Point3D;

public class Day22 extends Solution2021<Rule> {

    @Override
    public int getDay() {
        return 22;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("(on|off) x=(-?\\d+)\\.\\.(-?\\d+),y=(-?\\d+)\\.\\.(-?\\d+),z=(-?\\d+)\\.\\.(-?\\d+)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        Map<Point3D, Boolean> grid = Point3DUtils.generateGrid(-50, -50, 50, 50, -50, 50).collect(Collectors.toMap(Function.identity(), pt -> false));
        input.stream().forEach(rule -> {
            grid.keySet().stream()
                .filter(pt -> Point3DUtils.pointInsideBoundary(pt, true, 
                    rule.getDouble(3), rule.getDouble(2), rule.getDouble(4), rule.getDouble(1), rule.getDouble(5), rule.getDouble(6)
                ))
                .forEach(pt -> grid.compute(pt, (k, v) -> Boolean.TRUE.equals(rule.getString(0).equals("on"))));
        });
        return Long.toString(grid.values().stream().filter(Boolean.TRUE::equals).count());
    }

    @Override
    protected String part2(List<Rule> input) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
