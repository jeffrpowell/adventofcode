package com.jeffrpowell.adventofcode.aoc2022;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day22 extends Solution2022<Section>{

    @Override
    public int getDay() {
        return 22;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, false));
    }
    
    @Override
    protected String part1(List<Section> input) {
        List<List<String>> map = input.get(0).getInput(InputParserFactory.getTokenSVParser(""));
        Map<Point2D, Boolean> grid = new HashMap<>();
        for (int y = 0; y < map.size(); y++) {
            List<String> row = map.get(y);
            for (int x = 0; x < row.size(); x++) {
                String pt = row.get(x);
                if (pt.equals(" ")) {
                    continue;
                }
                grid.put(new Point2D.Double(x, y), pt.equals("."));
            }
        }
        String steps = input.get(1).getInput(InputParserFactory.getStringParser()).get(0);
        List<Integer> walks = Arrays.stream(Pattern.compile("\\w").split(steps)).map(Integer::parseInt).collect(Collectors.toList());
        List<Direction> turns = Arrays.stream(Pattern.compile("\\d+").split(steps)).map(s -> {
            return switch(s) {
                case "R" -> Direction.RIGHT;
                case "L" -> Direction.LEFT;
                case "U" -> Direction.UP;
                default -> Direction.DOWN;
            };
        }).collect(Collectors.toList());
        return null;
    }

    @Override
    protected String part2(List<Section> input) {
        return null;
    }

}
