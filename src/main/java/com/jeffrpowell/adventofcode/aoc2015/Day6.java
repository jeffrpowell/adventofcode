package com.jeffrpowell.adventofcode.aoc2015;

import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day6 extends Solution2015<List<String>>{

    @Override
    public int getDay() {
        return 6;
    }

    @Override
    public InputParser getInputParser() {
        return InputParserFactory.getTokenSVParser(" ");
    }

    @Override
    protected String part1(List<List<String>> input) {
        List<Instruction> instructions = input.stream().map(Instruction::new).collect(Collectors.toList());
        int countOn = 0;
        for (int y = 0; y < 1000; y++) {
            for (int x = 0; x < 1000; x++) {
                StatefulPt pt = new StatefulPt(new Point2D.Double(x, y));
                instructions.stream().forEach(i -> i.actOnPt_part1(pt));
                if (pt.on) {
                    countOn++;
                }
            }
        }
        return Integer.toString(countOn);
    }

    @Override
    protected String part2(List<List<String>> input) {
        List<Instruction> instructions = input.stream().map(Instruction::new).collect(Collectors.toList());
        int brightness = 0;
        for (int y = 0; y < 1000; y++) {
            for (int x = 0; x < 1000; x++) {
                StatefulPt pt = new StatefulPt(new Point2D.Double(x, y));
                instructions.stream().forEach(i -> i.actOnPt_part2(pt));
                brightness += pt.brightness;
            }
        }
        return Integer.toString(brightness);
    }
    
    private static class Instruction {
        enum Type {
            ON(b -> true, i -> i + 1), 
            OFF(b -> false, i -> Math.max(i - 1, 0)), 
            TOGGLE(b -> !b, i -> i + 2);
            
            public Function<Boolean, Boolean> onFn;
            public Function<Integer, Integer> brightFn;

            private Type(Function<Boolean, Boolean> onFn, Function<Integer, Integer> brightFn) {
                this.onFn = onFn;
                this.brightFn = brightFn;
            }
        }
        
        public Type type;
        public Point2D start;
        public Point2D finish;
        
        public Instruction(List<String> input) {
            String type0 = input.get(0);
            int startIndex = 1;
            if (type0.equals("turn")) {
                if (input.get(1).equals("on")) {
                    this.type = Type.ON;
                }
                else {
                    this.type = Type.OFF;
                }
                startIndex = 2;
            }
            else {
                this.type = Type.TOGGLE;
            }
            this.start = csvToPoint(input.get(startIndex));
            startIndex += 2; // skip "through"
            this.finish = csvToPoint(input.get(startIndex));
        }
        
        private static Point2D csvToPoint(String csv) {
            String[] coords = csv.split(",");
            return new Point2D.Double(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
        }
        
        public void actOnPt_part1(StatefulPt pt) {
            if (Point2DUtils.pointInsideBoundary(pt.pt, true, start.getY(), finish.getX(), finish.getY(), start.getX())) {
                pt.on = type.onFn.apply(pt.on);
            }
        }
        public void actOnPt_part2(StatefulPt pt) {
            if (Point2DUtils.pointInsideBoundary(pt.pt, true, start.getY(), finish.getX(), finish.getY(), start.getX())) {
                pt.brightness = type.brightFn.apply(pt.brightness);
            }
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
