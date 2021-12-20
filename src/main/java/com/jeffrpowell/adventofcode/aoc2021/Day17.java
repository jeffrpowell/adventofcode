package com.jeffrpowell.adventofcode.aoc2021;

import java.util.List;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

import java.awt.geom.Point2D;

public class Day17 extends Solution2021<Rule> {

    @Override
    public int getDay() {
        return 17;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("target area: x=(-?\\d+)\\.\\.(-?\\d+), y=(-?\\d+)\\.\\.(-?\\d+)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        Rule r = input.get(0);
        int yMin = r.getInt(2);
        int yMax = r.getInt(3);

        int maxVal = 0;
        for (int y = 1; y < 150; y++) {
            maxVal = Math.max(maxVal, simulateY(y, yMin, yMax));
        }
        return Integer.toString(maxVal);
    }
    
    @Override
    protected String part2(List<Rule> input) {
        Rule r = input.get(0);
        int xMin = r.getInt(0);
        int xMax = r.getInt(1);
        int yMin = r.getInt(2);
        int yMax = r.getInt(3);
        int count = 0;
        for (int y = yMin; y < Math.abs(yMin); y++) {
            for (int x = 13; x < xMax; x++) {
                if (simulate(new Point2D.Double(x, y), xMin, xMax, yMin, yMax)) { 
                    count++;
                }
            }
        }
        return Integer.toString(count);
    }

    private int simulateY(int initialY, int yMin, int yMax) {
        int val = 0;
        int speed = initialY;
        int maxVal = 0;
        while (val >= yMin) {
            val += speed;
            speed--;
            maxVal = Math.max(maxVal, val);
            if (val <= yMax && val >= yMin) {
                return maxVal;
            }
        }
        return -1;
    }

    private boolean simulate(Point2D initialVector, int xMin, int xMax, int yMin, int yMax) {
        Point2D pt = new Point2D.Double(0, 0);
        while (pt.getX() < xMax && pt.getY() > yMin) {
            pt = Point2DUtils.applyVectorToPt(initialVector, pt);
            if (inside(pt, yMax, xMax, yMin, xMin)) {
                return true;
            }
            applyDrag(initialVector);
        }
        return false;
    }

    private boolean inside(Point2D pt, int top, int right, int bottom, int left) {
        double x = pt.getX();
		double y = pt.getY();
        return x <= right &&
            x >= left &&
            y >= bottom &&
            y <= top;
    }

    private void applyDrag(Point2D pt) {
        pt.setLocation(Math.signum(pt.getX()), pt.getY() - 1);
    }

}
