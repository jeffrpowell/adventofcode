package com.jeffrpowell.adventofcode.aoc2015;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeffrpowell.adventofcode.algorithms.PointTransform;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day25 extends Solution2015<Rule>{
    private static PointTransform FLOAT_UP_FN = new PointTransform(1, -1);

    @Override
    public int getDay() {
        return 25;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", "To continue, please consult the code grid in the manual\\.  Enter the code at row (\\d+), column (\\d+)\\.");
    }

    @Override
    protected String part1(List<Rule> input) {
        long code = 20151125;
        Point2D pt = new Point2D.Double(0, 0);
        Point2D target = new Point2D.Double(input.get(0).getInt(0) - 1, input.get(0).getInt(1) - 1);
        while(!pt.equals(target)) {
            pt = nextPt(pt);
            code = (code * 252533L) % 33554393L;
        }
        return Long.toString(code); //currently too high
    }

    private Point2D nextPt(Point2D prior) {
        if (prior.getY() != 0L) {
            return FLOAT_UP_FN.apply(prior);
        }
        else {
            return new Point2D.Double(0, prior.getX() + 1);
        }
    }

    @Override
    protected String part2(List<Rule> input) {
        return null;
    }
}
