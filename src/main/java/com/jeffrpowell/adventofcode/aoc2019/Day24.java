package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.algorithms.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Day24 extends Solution2019<List<String>>
{
	@Override
	public int getDay()
	{
		return 24;
	}

	@Override
	public InputParser<List<String>> getInputParser()
	{
		return InputParserFactory.getTokenSVParser("");
	}

	@Override
	protected String part1(List<List<String>> input)
	{
		Map<Point2D, Bug> bugs = new HashMap<>();
		for (int y = 0; y < 5; y++)
		{
			List<String> row = input.get(y);
			for (int x = 0; x < 5; x++)
			{
				Point2D pt = new Point2D.Double(x, y);
				bugs.put(pt, new Bug(pt, row.get(x)));
			}
		}
		for (Bug bug : bugs.values())
		{
			Set<Point2D> neighborPoints = Point2DUtils.getBoundedAdjacentPts(bug.pt, 0, 4, 4, 0, true, false);
			bug.registerNeighbors(neighborPoints.stream().map(bugs::get).collect(Collectors.toList()));
		}
		Set<Integer> biodiversityRatings = new HashSet<>();
		while (biodiversityRatings.add(getBiodiversityRating(bugs))) {
			bugs.values().stream().forEach(Bug::primeNextState);
			bugs.values().stream().forEach(Bug::moveToNextState);
		}
		
		return Integer.toString(getBiodiversityRating(bugs));
	}
	
	private static int getBiodiversityRating(Map<Point2D, Bug> bugs) {
		int biodiversityRating = 1;
		int score = 0;
		for (int y = 0; y < 5; y++)
		{
			for (int x = 0; x < 5; x++)
			{
				Point2D pt = new Point2D.Double(x, y);
				score += bugs.get(pt).isAlive() ? biodiversityRating : 0;
				biodiversityRating *= 2;
			}
		}
		return score;
	}
	
	// private static void printGrid(Map<Point2D, Bug> bugs) {
	// 	StringBuilder builder = new StringBuilder();
	// 	for (int y = 0; y < 5; y++)
	// 	{
	// 		for (int x = 0; x < 5; x++)
	// 		{
	// 			Point2D pt = new Point2D.Double(x, y);
	// 			builder.append(bugs.get(pt));
	// 		}
	// 		builder.append("\n");
	// 	}
	// 	builder.append("\n");
	// 	System.out.println(builder);
	// }

	@Override
	protected String part2(List<List<String>> input)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	private static class Bug {
		private final Point2D pt;
		private final List<Bug> neighbors;
		private boolean alive;
		private boolean toggle;

		public Bug(Point2D pt, String inputSymbol)
		{
			this.pt = pt;
			this.neighbors = new ArrayList<>();
			this.alive = inputSymbol.equals("#");
			this.toggle = false;
		}
		
		public void registerNeighbors(List<Bug> neighbors) {
			this.neighbors.addAll(neighbors);
		}
		
		public boolean isAlive() {
			return alive;
		}
		
		public void primeNextState() {
			long buggyNeighbors = neighbors.stream().filter(Bug::isAlive).count();
			if (alive) {
				toggle = buggyNeighbors != 1;
			}
			else {
				toggle = buggyNeighbors == 1 || buggyNeighbors == 2;
			}
		}
		
		public void moveToNextState() {
			if (toggle) {
				toggle = false;
				alive = !alive;
			}
		}

		@Override
		public int hashCode()
		{
			int hash = 7;
			hash = 89 * hash + Objects.hashCode(this.pt);
			hash = 89 * hash + (this.alive ? 1 : 0);
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
			final Bug other = (Bug) obj;
			if (this.alive != other.alive)
				return false;
			return Objects.equals(this.pt, other.pt);
		}
		
		@Override
		public String toString() {
			return alive ? "#" : ".";
		}
	}
}
