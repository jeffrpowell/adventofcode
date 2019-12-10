package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class Day10 extends Solution2019<List<String>>{

	@Override
	public int getDay()
	{
		return 10;
	}

	@Override
	public InputParser<List<String>> getInputParser()
	{
		return InputParserFactory.getTokenSVParser("");
	}

	@Override
	protected String part1(List<List<String>> input)
	{
		Set<Asteroid> asteroids = new HashSet<>();
		Set<Point2D> asteroidLocations = new HashSet<>();
		int height = input.size();
		int width = input.get(0).size();
		for (int y = 0; y < input.size(); y++)
		{
			List<String> row = input.get(y);
			for (int x = 0; x < row.size(); x++)
			{
				if (row.get(x).equals("#")) {
					Point2D asteroidPt = new Point2D.Double(x, y);
					asteroids.add(new Asteroid(asteroidPt));
					asteroidLocations.add(asteroidPt);
				}
			}
		}
		Grid grid = new Grid(asteroids, asteroidLocations, width, height);
		
		return Integer.toString(grid.findMostConnectedAsteroid().getSeenAsteroids());
	}

	@Override
	protected String part2(List<List<String>> input)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	private static class Grid {
		private final Set<Asteroid> asteroids;
		private final Set<Point2D> asteroidLocations;
		private final Map<Double, Map<Double, PointTransform>> transformCache;
		private final double leftBoundary;
		private final double topBoundary;
		private final double rightBoundary;
		private final double bottomBoundary;

		public Grid(Set<Asteroid> asteroids, Set<Point2D> asteroidLocations, double rightBoundary, double bottomBoundary)
		{
			this.asteroids = asteroids;
			this.asteroidLocations = asteroidLocations;
			this.leftBoundary = 0;
			this.topBoundary = 0;
			this.rightBoundary = rightBoundary;
			this.bottomBoundary = bottomBoundary;
			this.transformCache = new HashMap<>();
			for (double x = 0; x <= rightBoundary; x++)
			{
				transformCache.put(x, new HashMap<>());
				transformCache.put(-x, new HashMap<>());
				for (double y = 0; y <= bottomBoundary; y++)
				{
					if (x == 0 && y == 0) {
						continue;
					}
					transformCache.get(x).put(y, new PointTransform(x, y));
					transformCache.get(x).put(-y, new PointTransform(x, -y));
					transformCache.get(-x).put(y, new PointTransform(-x, y));
					transformCache.get(-x).put(-y, new PointTransform(-x, -y));
				}
			}
		}

		public Asteroid findMostConnectedAsteroid() {
			for (Asteroid asteroid : asteroids)
			{
				setSeenAsteroids(asteroid);
			}
			return asteroids.stream().max((a1, a2) -> Integer.compare(a1.getSeenAsteroids(), a2.getSeenAsteroids())).get();
		}
		
		private void setSeenAsteroids(Asteroid asteroid) {
			Point2D asteroidPt = asteroid.getPt();
			double distanceToLeft = asteroidPt.getX() - leftBoundary;
			double distanceToTop = asteroidPt.getY() - topBoundary;
			double distanceToRight = rightBoundary - asteroidPt.getX();
			double distanceToBottom = bottomBoundary - asteroidPt.getY();
			int asteroidsSeen = 0;
			for (double x = -1 * distanceToLeft; x <= distanceToRight; x++)
			{
				for (double y = -1 * distanceToTop; y <= distanceToBottom; y++) {
					if (x == 0 && y == 0) {
						continue;
					}
					if (canSeeAsteroid(asteroidPt, x, y)) {
						asteroidsSeen++;
					}
				}
			}
			asteroid.setSeenAsteroids(asteroidsSeen);
		}
		
		private boolean canSeeAsteroid(Point2D asteroidPt, double x, double y) {
			Point2D seekPt = transformCache.get(x).get(y).apply(asteroidPt);
			if (asteroidLocations.contains(seekPt)) {
				return true;
			}
			else if (Point2DUtils.pointInsideBoundary(seekPt, true, topBoundary, rightBoundary, bottomBoundary, leftBoundary)) {
				return canSeeAsteroid(seekPt, x, y);
			}
			else {
				return false;
			}
		}
		
	}
	
	private static class PointTransform implements Function<Point2D, Point2D> {

		private final double xBar;
		private final double yBar;

		public PointTransform(double xBar, double yBar)
		{
			this.xBar = xBar;
			this.yBar = yBar;
		}
		
		@Override
		public Point2D apply(Point2D pt)
		{
			return new Point2D.Double(pt.getX() + xBar, pt.getY() + yBar);
		}
		
	}

	private static class Asteroid {
		private final Point2D pt;
		private int seenAsteroids;

		public Asteroid(Point2D pt)
		{
			this.pt = pt;
		}

		public int getSeenAsteroids()
		{
			return seenAsteroids;
		}

		public void setSeenAsteroids(int seenAsteroids)
		{
			this.seenAsteroids = seenAsteroids;
		}

		public Point2D getPt()
		{
			return pt;
		}

		@Override
		public int hashCode()
		{
			int hash = 7;
			hash = 79 * hash + Objects.hashCode(this.pt);
			return hash;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (obj == null)
			{
				return false;
			}
			if (getClass() != obj.getClass())
			{
				return false;
			}
			final Asteroid other = (Asteroid) obj;
			return Objects.equals(this.pt, other.pt);
		}
		
		
	}
}
