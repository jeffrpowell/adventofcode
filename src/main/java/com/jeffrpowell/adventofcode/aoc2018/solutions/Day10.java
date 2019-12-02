package com.jeffrpowell.adventofcode.aoc2018.solutions;

import com.jeffrpowell.adventofcode.InputParser;
import com.jeffrpowell.adventofcode.InputParserFactory;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day10 extends Solution2018<String>
{
    private static final Pattern REGEX = Pattern.compile("position=<\\s*(-?\\d+),\\s*(-?\\d+)> velocity=<\\s*(-?\\d+),\\s*(-?\\d+)>");
    
	@Override
	public int getDay()
	{
		return 10;
	}
	
	@Override
	public InputParser<String> getInputParser()
	{
		return InputParserFactory.getStringParser();
	}
	
	@Override
    public String part1(List<String> input) {
        List<PtFactory> pts = input.stream().map(REGEX::matcher).map(PtFactory::new).collect(Collectors.toList());
        Point2D lastWidth = getConvexHullWidth(pts);
        Point2D lastHeight = getConvexHullHeight(pts);
        Point2D nextWidth = dupPt(lastWidth);
        Point2D nextHeight = dupPt(lastHeight);
        List<PtFactory> nextPts = pts;
//        System.out.println(printGrid(lastWidth, lastHeight, pts));
        while (calculateHullDistance(nextHeight) <= calculateHullDistance(lastHeight) && calculateHullDistance(nextWidth) <= calculateHullDistance(lastWidth)) {
            pts = nextPts;
            lastWidth = dupPt(nextWidth);
            lastHeight = dupPt(nextHeight);
            
            nextPts = pts.stream().map(PtFactory::cloneFactory).map(PtFactory::iterate).collect(Collectors.toList());
            nextWidth = getConvexHullWidth(nextPts);
            nextHeight = getConvexHullHeight(nextPts);
//            System.out.println(printGrid(nextWidth, nextHeight, nextPts));
        }
        return printGrid(lastWidth, lastHeight, pts);
    }
    
    private static Point2D dupPt(Point2D pt) {
        return new Point2D.Double(pt.getX(), pt.getY());
    }
    
    private static Point2D getConvexHullWidth(List<PtFactory> pts) {
        return pts.stream().map(PtFactory::getLastPt)
            .reduce(new Point2D.Double(Double.MAX_VALUE, Double.MIN_VALUE), (minMax, pt) -> {
                double minX = Math.min(minMax.getX(), pt.getX());
                double maxX = Math.max(minMax.getY(), pt.getX());
                return new Point2D.Double(minX, maxX);
            });
    }
    
    private static Point2D getConvexHullHeight(List<PtFactory> pts) {
        return pts.stream().map(PtFactory::getLastPt)
            .reduce(new Point2D.Double(Double.MAX_VALUE, Double.MIN_VALUE), (minMax, pt) -> {
                double minY = Math.min(minMax.getX(), pt.getY());
                double maxY = Math.max(minMax.getY(), pt.getY());
                return new Point2D.Double(minY, maxY);
            });
    }
    
    private static double calculateHullDistance(Point2D minMaxXPair) {
        return minMaxXPair.getY() - minMaxXPair.getX();
    }

    private String printGrid(Point2D lastWidth, Point2D lastHeight, List<PtFactory> messagePtFactories) {
        StringBuilder grid = new StringBuilder();
        Set<Point2D> messagePts = messagePtFactories.stream().map(PtFactory::getLastPt).collect(Collectors.toSet());
        for (double x = lastWidth.getX(); x <= lastWidth.getY(); x++) {
            for (double y = lastHeight.getX(); y <= lastHeight.getY(); y++) {
                if (messagePts.contains(new Point2D.Double(x, y))) {
                    grid.append("*");
                }
                else {
                    grid.append(".");
                }
            }
            grid.append("\n");
        }
        grid.append("\n\n");
        return grid.toString();
    }

    private static class PtFactory{
        private final Point2D lastPt;
        private final Point2D vector;
        
        public PtFactory(Matcher m) {
            m.find();
            this.lastPt = new Point2D.Double(Double.parseDouble(m.group(2)), Double.parseDouble(m.group(1)));
            this.vector = new Point2D.Double(Double.parseDouble(m.group(4)), Double.parseDouble(m.group(3)));
        }

        public PtFactory(Point2D vector, Point2D lastPt) {
            this.vector = vector;
            this.lastPt = new Point2D.Double(lastPt.getX(), lastPt.getY());
        }
        
        public PtFactory cloneFactory() {
            return new PtFactory(vector, lastPt);
        }
        

        public Point2D getLastPt() {
            return lastPt;
        }
        
        public PtFactory iterate() {
            lastPt.setLocation(lastPt.getX() + vector.getX(), lastPt.getY() + vector.getY());
            return this;
        }
    }
    
    @Override
    public String part2(List<String> input) {
        List<PtFactory> pts = input.stream().map(REGEX::matcher).map(PtFactory::new).collect(Collectors.toList());
        Point2D lastWidth = getConvexHullWidth(pts);
        Point2D lastHeight = getConvexHullHeight(pts);
        Point2D nextWidth = dupPt(lastWidth);
        Point2D nextHeight = dupPt(lastHeight);
        List<PtFactory> nextPts = pts;
        int t = 0;
        while (calculateHullDistance(nextHeight) <= calculateHullDistance(lastHeight) && calculateHullDistance(nextWidth) <= calculateHullDistance(lastWidth)) {
            pts = nextPts;
            lastWidth = dupPt(nextWidth);
            lastHeight = dupPt(nextHeight);
            
            nextPts = pts.stream().map(PtFactory::cloneFactory).map(PtFactory::iterate).collect(Collectors.toList());
            nextWidth = getConvexHullWidth(nextPts);
            nextHeight = getConvexHullHeight(nextPts);
            t++;
        }
        return Integer.toString(t-1);
    }
}
