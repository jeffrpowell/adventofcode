package com.jeffrpowell.adventofcode.aoc2018.solutions;

import com.jeffrpowell.adventofcode.InputParser;
import com.jeffrpowell.adventofcode.InputParserFactory;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day3 extends Solution2018<String>
{
    private static final Pattern REGEX = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");
    
	@Override
	public int getDay()
	{
		return 3;
	}
	
	@Override
	public InputParser<String> getInputParser()
	{
		return InputParserFactory.getStringParser();
	}
	
    @Override
    public String part1(List<String> input) {
        List<Point2D> pts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                pts.add(new Point2D.Double(i, j));
            }
        }
        List<Claim> claims = input.stream().map(Claim::new).collect(Collectors.toList());
        return Long.toString(pts.stream()
            .map(pt -> claims.stream().filter(c -> c.containsPt(pt)).count())
            .filter(claimCount -> claimCount > 1)
            .count()
        );
    }

    @Override
    public String part2(List<String> input) {
        List<Point2D> pts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                pts.add(new Point2D.Double(i, j));
            }
        }
        List<Claim> claims = input.stream().map(Claim::new).collect(Collectors.toList());
        Set<Integer> conflictedClaims = pts.stream()
            .map(pt -> claims.stream().filter(c -> c.containsPt(pt)).collect(Collectors.toSet()))
            .filter(set -> set.size() > 1)
            .flatMap(Set::stream)
            .map(Claim::getId)
            .collect(Collectors.toSet());
        return Integer.toString(claims.stream().map(Claim::getId).filter(id -> !conflictedClaims.contains(id)).findAny().get());
    }
    
    private static class Claim {
        private final int id;
        private final Point2D topLeft;
        private final int width;
        private final int height;
        
        public Claim(String rawStr) {
            Matcher m = REGEX.matcher(rawStr);
            m.find();
            this.id = Integer.parseInt(m.group(1));
            this.topLeft = new Point2D.Double(Double.parseDouble(m.group(2)), Double.parseDouble(m.group(3)));
            this.width = Integer.parseInt(m.group(4));
            this.height = Integer.parseInt(m.group(5));
        }

        public int getId() {
            return id;
        }
        
        public boolean containsPt(Point2D pt) {
            return topLeft.getX() <= pt.getX() &&
                   pt.getX() < topLeft.getX() + width &&
                   topLeft.getY() <= pt.getY() &&
                   pt.getY() < topLeft.getY() + height;
        }
    }
}
