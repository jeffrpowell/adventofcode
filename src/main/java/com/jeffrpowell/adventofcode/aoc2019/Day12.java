package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.geometry.Point3D;

public class Day12 extends Solution2019<String> {

	// <x=9, y=13, z=-8>
	private static final Pattern MOON_REGEX = Pattern.compile("<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>");
	
	@Override
	public int getDay()
	{
		return 12;
	}

	@Override
	public InputParser<String> getInputParser()
	{
		return InputParserFactory.getStringParser();
	}

	@Override
	protected String part1(List<String> input)
	{
		List<Moon> moons = input.stream().map(Day12::parseMoon).collect(Collectors.toList());
		return "";
	}

	@Override
	protected String part2(List<String> input)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	private static Moon parseMoon(String str) {
		Matcher matcher = MOON_REGEX.matcher(str);
		return new Moon(new Point3D(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)), Double.parseDouble(matcher.group(3))));
	}

	private static class Moon {
		private Point3D pt;
		private Point3D velocity;

		public Moon(Point3D pt)
		{
			this.pt = pt;
			this.velocity = Point3D.ZERO;
		}
		
		public void tick(List<Moon> otherMoons) {
			
		}
	}
}
