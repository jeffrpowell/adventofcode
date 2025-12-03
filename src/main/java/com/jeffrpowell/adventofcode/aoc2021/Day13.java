package com.jeffrpowell.adventofcode.aoc2021;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.algorithms.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day13 extends Solution2021<Section>{

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, false));
    }

    @Override
    protected String part1(List<Section> input) {
        List<Rule> pointRules = input.get(0).getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("(\\d+,\\d+)")));
        List<Rule> folds = input.get(1).getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("fold along ([xy])=(\\d+)")));
        List<Point2D> points = pointRules.stream().map(r -> r.getPoint2D(0)).collect(Collectors.toList());
        Set<Point2D> pts = points.stream().map(pt -> fold(pt, folds.get(0).getChar(0), folds.get(0).getInt(1))).collect(Collectors.toSet());
        return Integer.toString(pts.size());
    }

    private Point2D fold(Point2D pt, char xy, int line) {
        if (xy == 'x') {
            return foldX(pt, line);
        }
        else {
            return foldY(pt, line);
        }
    }

    private Point2D foldX(Point2D pt, int x) {
        if (pt.getX() <= x) {
            return pt;
        }
        return Point2DUtils.applyVectorToPt(new Point2D.Double((x - pt.getX())*2, 0), pt);
    }

    private Point2D foldY(Point2D pt, int y) {
        if (pt.getY() <= y) {
            return pt;
        }
        return Point2DUtils.applyVectorToPt(new Point2D.Double(0, (y - pt.getY())*2), pt);
    }

    @Override
    protected String part2(List<Section> input) {
        List<Rule> pointRules = input.get(0).getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("(\\d+,\\d+)")));
        List<Rule> folds = input.get(1).getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("fold along ([xy])=(\\d+)")));
        List<Point2D> points = pointRules.stream().map(r -> r.getPoint2D(0)).collect(Collectors.toList());
        Set<Point2D> pts = new HashSet<>(points);
        for (Rule fold : folds) {
            pts = pts.stream().map(pt -> fold(pt, fold.getChar(0), fold.getInt(1))).collect(Collectors.toSet());
        }
        Point2D max = pts.stream().reduce(new Point2D.Double(Integer.MIN_VALUE, Integer.MIN_VALUE), (accum, next) -> new Point2D.Double(Math.max(accum.getX(), next.getX()), Math.max(accum.getY(), next.getY())));
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < max.getY() + 1; row++) {
            for (int col = 0; col < max.getX() + 1; col++) {
                Point2D pt = new Point2D.Double(col, row);
                builder.append(pts.contains(pt) ? "#": ".");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
    

}
