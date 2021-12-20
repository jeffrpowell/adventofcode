package com.jeffrpowell.adventofcode.aoc2021;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

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
        Set<Point2D> winners = new HashSet<>();
        for (int y = yMin - 1; y <= Math.abs(yMin) + 1; y++) {
            for (int x = 4; x <= xMax + 1; x++) {
                Point2D initVelocity = new Point2D.Double(x, y);
                if (simulate(initVelocity, xMin, xMax, yMin, yMax)) { 
                    count++;
                    winners.add(initVelocity);
                }
            }
        }
        Set<Point2D> wrong = getWrongPts(winners);
        return Integer.toString(count);
    }

    private static Set<Point2D> getWrongPts(Set<Point2D> winners) {
        Set<Point2D> testWin = InputParserFactory.getRuleParser("\n", Pattern.compile("(.+)")).parseInput(List.of(testWinners)).stream()
            .map(r -> r.getPoint2D(0))
            .collect(Collectors.toSet());
        return Sets.difference(winners, testWin);
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
            initialVector = applyDrag(initialVector);
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

    private Point2D applyDrag(Point2D pt) {
        return new Point2D.Double(pt.getX() - Math.signum(pt.getX()), pt.getY() - 1);
    }

    static String testWinners = "23,-10\n25,-9\n27,-5\n29,-6\n22,-6\n21,-7\n9,0\n27,-7\n24,-5\n25,-7\n26,-6\n25,-5\n6,8\n11,-2\n20,-5\n29,-10\n6,3\n28,-7\n8,0\n30,-6\n29,-8\n20,-10\n6,7\n6,4\n6,1\n14,-4\n21,-6\n26,-10\n7,-1\n7,7\n8,-1\n21,-9\n6,2\n20,-7\n30,-10\n14,-3\n20,-8\n13,-2\n7,3\n28,-8\n29,-9\n15,-3\n22,-5\n26,-8\n25,-8\n25,-6\n15,-4\n9,-2\n15,-2\n12,-2\n28,-9\n12,-3\n24,-6\n23,-7\n25,-10\n7,8\n11,-3\n26,-7\n7,1\n23,-9\n6,0\n22,-10\n27,-6\n8,1\n22,-8\n13,-4\n7,6\n28,-6\n11,-4\n12,-4\n26,-9\n7,4\n24,-10\n23,-8\n30,-8\n7,0\n9,-1\n10,-1\n26,-5\n22,-9\n6,5\n7,5\n23,-6\n28,-10\n10,-2\n11,-1\n20,-9\n14,-2\n29,-7\n13,-3\n23,-5\n24,-8\n27,-9\n30,-7\n28,-5\n21,-10\n7,9\n6,6\n21,-5\n27,-10\n7,2\n30,-9\n21,-8\n22,-7\n24,-9\n20,-6\n6,9\n29,-5\n8,-2\n27,-8\n30,-5\n24,-7";
}
