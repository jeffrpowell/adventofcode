package com.jeffrpowell.adventofcode.inputparser.rule;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Rule {
    private final List<String> tokens;
    private final Map<Integer, Object> cache;
    private final String rulePatternKey;
    private final Integer sortKey;
    
// 6V8)T2J\n
// R1009,
// 172930-683082
// <x=9, y=13, z=-8>\n
// 1,-1,-1,-2\n
// #1 @ 387,801: 11x22\n
// position=<-31761,  10798> velocity=< 3, -1>\n
// Step K must be finished before step B can begin\n
// y=1587, x=415..417\n
// [1518-10-13 00:03] Guard #2539 begins shift\n
// ##.## => #\n

    public Rule(List<String> tokens, String rulePatternKey, Integer sortKey) {
        this.tokens = tokens;
        this.cache = new HashMap<>();
        this.rulePatternKey = rulePatternKey;
        this.sortKey = sortKey;
    }

    public String getRulePatternKey() {
        return rulePatternKey;
    }

    public Integer getSortKey() {
        return sortKey;
    }
    
    public <T> T getCustomType(int i, Function<String, T> fn) {
        this.cache.putIfAbsent(i, fn.apply(tokens.get(i)));
        return (T) this.cache.get(i);
    }
    
    public String getString(int i) {
        this.cache.putIfAbsent(i, tokens.get(i));
        return (String) this.cache.get(i);
    }
    
    public Integer getInt(int i) {
        this.cache.putIfAbsent(i, Integer.parseInt(tokens.get(i)));
        return (Integer) this.cache.get(i);
    }
    
    public Double getDouble(int i) {
        this.cache.putIfAbsent(i, Double.parseDouble(tokens.get(i)));
        return (Double) this.cache.get(i);
    }
    
    public Character getChar(int i) {
        this.cache.putIfAbsent(i, tokens.get(i).charAt(0));
        return (Character) this.cache.get(i);
    }
    
    public Point2D getPoint2D(int i) {
        this.cache.putIfAbsent(i, parsePoint2D(tokens.get(i)));
        return (Point2D) this.cache.get(i);
    }
    
    private Point2D parsePoint2D(String s) {
        String[] coords = s.split(",");
        return new Point2D.Double(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
    }
}
