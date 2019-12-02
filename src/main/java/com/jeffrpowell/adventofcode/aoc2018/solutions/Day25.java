package com.jeffrpowell.adventofcode.aoc2018.solutions;

import com.jeffrpowell.adventofcode.InputParser;
import com.jeffrpowell.adventofcode.InputParserFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day25 extends Solution2018<String>
{
    List<Set<Point4>> constellations;

    public Day25() {
        this.constellations = new ArrayList<>();
    }

	@Override
	public int getDay()
	{
		return 25;
	}
	
	@Override
	public InputParser<String> getInputParser()
	{
		return InputParserFactory.getStringParser();
	}
    
    @Override
    public String part1(List<String> input) {
        List<Point4> pts = input.stream().map(Point4::new).sorted().collect(Collectors.toList());
        for (Point4 pt : pts) {
            Iterator<Set<Point4>> i = constellations.iterator();
            boolean attachedToConstellation = false;
            Set<Point4> closestConstellation = null;
            while(i.hasNext()) {
                Set<Point4> constellation = i.next();
                if (constellation.stream().anyMatch(cPt -> pt.distanceTo(cPt) <= 3)) {
                    attachedToConstellation = true;
                    if (closestConstellation == null) {
                        closestConstellation = constellation;
                        closestConstellation.add(pt);
                    }
                    else {
                        closestConstellation.addAll(constellation);
                        i.remove();
                    }
                }
            }
            if (!attachedToConstellation) {
                constellations.add(Stream.of(pt).collect(Collectors.toCollection(HashSet::new)));
            }
        }
        return Integer.toString(constellations.size());
    }

    @Override
    public String part2(List<String> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private static class Point4 implements Comparable<Point4>{
        private static final Pattern REGEX = Pattern.compile("(-?\\d+),(-?\\d+),(-?\\d+),(-?\\d+)");
        
        private final int x;
        private final int y;
        private final int z;
        private final int t;
        
        public Point4(String s) {
            Matcher m = REGEX.matcher(s);
            m.find();
            this.x = Integer.parseInt(m.group(1));
            this.y = Integer.parseInt(m.group(2));
            this.z = Integer.parseInt(m.group(3));
            this.t = Integer.parseInt(m.group(4));
        }
        
        public int distanceTo(Point4 pt) {
            return Math.abs(pt.x - x) + 
                   Math.abs(pt.y - y) + 
                   Math.abs(pt.z - z) + 
                   Math.abs(pt.t - t);
        }

        @Override
        public int compareTo(Point4 o) {
            int comparison = Integer.compare(x, o.x);
            if (comparison != 0) {
                return comparison;
            }
            comparison = Integer.compare(y, o.y);
            if (comparison != 0) {
                return comparison;
            }
            comparison = Integer.compare(z, o.z);
            if (comparison != 0) {
                return comparison;
            }
            return Integer.compare(t, o.t);
        }
    }
}
