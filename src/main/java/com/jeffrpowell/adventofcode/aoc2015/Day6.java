package com.jeffrpowell.adventofcode.aoc2015;

import com.jeffrpowell.adventofcode.algorithms.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Day6 extends Solution2015<Rule>{

    @Override
    public int getDay() {
        return 6;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        String regex = "(turn on|turn off|toggle) (\\d+,\\d+) through (\\d+,\\d+)";
        return InputParserFactory.getRuleParser("\n", Map.of("rule", Pattern.compile(regex)));
    }

    @Override
    protected String part1(List<Rule> input) {
        int countOn = 0;
        for (int y = 0; y < 1000; y++) {
            for (int x = 0; x < 1000; x++) {
                StatefulPt pt = new StatefulPt(new Point2D.Double(x, y));
                input.stream().forEach(i -> actOnPt_part1(i, pt));
                if (pt.on) {
                    countOn++;
                }
            }
        }
        return Integer.toString(countOn);
    }

    @Override
    protected String part2(List<Rule> input) {
        int brightness = 0;
        for (int y = 0; y < 1000; y++) {
            for (int x = 0; x < 1000; x++) {
                StatefulPt pt = new StatefulPt(new Point2D.Double(x, y));
                input.stream().forEach(i -> actOnPt_part2(i, pt));
                brightness += pt.brightness;
            }
        }
        return Integer.toString(brightness);
    }
    
    private void actOnPt_part1(Rule r, StatefulPt pt) {
        Type type = r.getCustomType(0, Type::fromString);
        Point2D start = r.getPoint2D(1);
        Point2D finish = r.getPoint2D(2);
        if (Point2DUtils.pointInsideBoundary(pt.pt, true, start.getY(), finish.getX(), finish.getY(), start.getX())) {
            pt.on = type.onFn.apply(pt.on);
        }
    }
    
    private void actOnPt_part2(Rule r, StatefulPt pt) {
        Type type = r.getCustomType(0, Type::fromString);
        Point2D start = r.getPoint2D(1);
        Point2D finish = r.getPoint2D(2);
        if (Point2DUtils.pointInsideBoundary(pt.pt, true, start.getY(), finish.getX(), finish.getY(), start.getX())) {
            pt.brightness = type.brightFn.apply(pt.brightness);
        }
    }
    
    private static enum Type {
        ON("turn on", b -> true, i -> i + 1), 
        OFF("turn off", b -> false, i -> Math.max(i - 1, 0)), 
        TOGGLE("toggle", b -> !b, i -> i + 2);

        public String label;
        public Function<Boolean, Boolean> onFn;
        public Function<Integer, Integer> brightFn;

        private Type(String label, Function<Boolean, Boolean> onFn, Function<Integer, Integer> brightFn) {
            this.label = label;
            this.onFn = onFn;
            this.brightFn = brightFn;
        }
        
        public static Type fromString(String s) {
            return Arrays.stream(values()).filter(t -> t.label.equals(s)).findAny().get();
        }
    }
    
    private static class StatefulPt {
        public Point2D pt;
        public boolean on;
        public int brightness;
        
        public StatefulPt(Point2D pt) {
            this.pt = pt;
            this.on = false;
            this.brightness = 0;
        }
    }
}
