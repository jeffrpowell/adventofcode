package com.jeffrpowell.adventofcode.aoc2024;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.Grid;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day4 extends Solution2024<List<String>>{

    @Override
    public int getDay() {
        return 4;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        Grid<String> grid = new Grid<>(input);
        return Long.toString(grid.entrySet().stream()
            .filter(entry -> entry.getValue().equals("X"))
            .map(Map.Entry::getKey)
            .map(this::generateBranches)
            .flatMap(List::stream)
            .filter(sb -> sb.findXmas(grid))
            .count());
        
    }
    
    private List<SearchBranch> generateBranches(Point2D startPt) {
        return Direction.getAllDirections()
            .map(d -> new SearchBranch(startPt, d.asVector()))
            .collect(Collectors.toList());
    }

    private static class SearchBranch {
        private static final List<String> XMAS = List.of("X","M","A","S");
        private final Point2D startPt;
        private final Point2D vector;
        public SearchBranch(Point2D startPt, Point2D vector) {
            this.startPt = startPt;
            this.vector = vector;
        }

        public boolean findXmas(Map<Point2D, String> grid) {
            List<String> result = Point2DUtils.repeatVectorToPtNTimes(vector, startPt, 3)
                .map(pt -> grid.getOrDefault(pt, "N"))
                .collect(Collectors.toList());
            result.add(0, "X");
            return result.equals(XMAS);
        }
    }

    private static class XSearchBranch {
        private static final List<Direction> ANGLE1 = List.of(Direction.UP_LEFT, Direction.DOWN_RIGHT);
        private static final List<Direction> ANGLE2 = List.of(Direction.UP_RIGHT, Direction.DOWN_LEFT);
        private final Point2D startPt;
        public XSearchBranch(Point2D startPt) {
            this.startPt = startPt;
        }

        public boolean findMAS(Map<Point2D, String> grid) {
            List<String> angle1 = ANGLE1.stream().map(d -> grid.getOrDefault(Point2DUtils.applyVectorToPt(d.asVector(), startPt), "N")).collect(Collectors.toList());
            List<String> angle2 = ANGLE2.stream().map(d -> grid.getOrDefault(Point2DUtils.applyVectorToPt(d.asVector(), startPt), "N")).collect(Collectors.toList());
            return angle1.contains("M") && angle1.contains("S") && angle2.contains("M") && angle2.contains("S");
        }
    }

    @Override
    protected String part2(List<List<String>> input) {
        Grid<String> grid = new Grid<>(input);
        return Long.toString(grid.entrySet().stream()
            .filter(entry -> entry.getValue().equals("A"))
            .map(Map.Entry::getKey)
            .map(XSearchBranch::new)
            .filter(sb -> sb.findMAS(grid))
            .count());
    }
}
