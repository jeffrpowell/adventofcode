package com.jeffrpowell.adventofcode.aoc2022;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        List<Point3D> cubes = input.stream().map(r -> r.getPoint3D(0)).collect(Collectors.toList());
        return null;
    }

    @Override
    protected String part2(List<Rule> input) {
        return null;
    }

}
