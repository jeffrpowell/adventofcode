package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
		moons.stream().forEach(moon -> moon.registerOtherMoons(moons));
		for (int i = 0; i < 1000; i++)
		{
			moons.stream().forEach(Moon::updateVelocity);
			moons.stream().forEach(Moon::tick);
		}
		return Integer.toString(moons.stream().map(Moon::getTotalEnergy).reduce(0.0, Double::sum, Double::sum).intValue());
	}

	@Override
	protected String part2(List<String> input)
	{
		List<Moon> moons = input.stream().map(Day12::parseMoon).collect(Collectors.toList());
		moons.stream().forEach(moon -> moon.registerOtherMoons(moons));
		Set<String> pastStates = new HashSet<>();
		long iterations = 0;
		boolean done = false;
		while (!done) 
		{
			moons.stream().forEach(Moon::updateVelocity);
			moons.stream().forEach(Moon::tick);
			iterations++;
			done = moons.stream().allMatch(moon -> !pastStates.add(moon.toString()));
		}
		return Long.toString(iterations);
	}
	
	private static Moon parseMoon(String str) {
		Matcher matcher = MOON_REGEX.matcher(str);
		matcher.find();
		return new Moon(new Point3D(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)), Double.parseDouble(matcher.group(3))));
	}

	private static class Moon {
		private static int ID_SEQUENCE = 0;
		private final int id;
		private Point3D pt;
		private Point3D velocity;
		private final List<Moon> otherMoons;

		public Moon(Point3D pt)
		{
			this.id = ID_SEQUENCE++;
			this.pt = pt;
			this.velocity = Point3D.ZERO;
			this.otherMoons = new ArrayList<>();
		}
		
		public void registerOtherMoons(List<Moon> moons) {
			moons.stream().filter(moon -> !Objects.equals(this, moon)).forEach(otherMoons::add);
		}
		
		public void updateVelocity() {
			for (Moon otherMoon : otherMoons)
			{
				updateVelocity(otherMoon);
			}
		}
		
		private void updateVelocity(Moon otherMoon) {
			Point3D otherMoonPt = otherMoon.pt;
			int xChange = Double.compare(otherMoonPt.getX(), pt.getX());
			int yChange = Double.compare(otherMoonPt.getY(), pt.getY());
			int zChange = Double.compare(otherMoonPt.getZ(), pt.getZ());
			velocity = velocity.add(new Point3D(xChange, yChange, zChange));
		}
		
		public void tick() {
			pt = pt.add(velocity);
		}
		
		public double getTotalEnergy() {
			return (Math.abs(pt.getX()) + Math.abs(pt.getY()) + Math.abs(pt.getZ())) * 
				   (Math.abs(velocity.getX()) + Math.abs(velocity.getY()) + Math.abs(velocity.getZ()));
		}

		@Override
		public int hashCode()
		{
			int hash = 7;
			hash = 89 * hash + this.id;
			hash = 89 * hash + Objects.hashCode(this.pt);
			hash = 89 * hash + Objects.hashCode(this.velocity);
			return hash;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Moon other = (Moon) obj;
			if (this.id != other.id)
				return false;
			if (!Objects.equals(this.pt, other.pt))
				return false;
			return Objects.equals(this.velocity, other.velocity);
		}
		
		@Override
		public String toString() {
			return id + " (" + pt.getX() + ", " + pt.getY() + ", " + pt.getZ() + ") -> <" + velocity.getX() + ", " + velocity.getY() + ", " + velocity.getZ() + ">";
		}
	}
}
