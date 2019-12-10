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
import java.util.PriorityQueue;
import java.util.Queue;
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
		Point2D winner = grid.findLucky200();
		return Double.toString(winner.getX() * 100 + winner.getY());
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
			Set<Point2D> checkedPts = new HashSet<>();
			Point2D asteroidPt = asteroid.getPt();
			Queue<Point2D> pointsToCheck = new PriorityQueue<>((pt1, pt2) ->
				Double.compare(
					Point2DUtils.getEuclideanDistance(asteroidPt, pt1),
					Point2DUtils.getEuclideanDistance(asteroidPt, pt2)
				)
			);
			Map<Point2D, PointTransform> transformLookup = new HashMap<>();
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
					PointTransform transform = transformCache.get(x).get(y);
					Point2D seekPt = transform.apply(asteroidPt);
					pointsToCheck.add(seekPt);
					transformLookup.put(seekPt, transform);
				}
			}
			while(!pointsToCheck.isEmpty()) {
				Point2D seekPt = pointsToCheck.poll();
				if (canSeeAsteroid(asteroid, checkedPts, seekPt, transformLookup.get(seekPt), false)) {
					asteroidsSeen++;
				}
			}
			asteroid.setSeenAsteroids(asteroidsSeen);
		}
		
		private boolean canSeeAsteroid(Asteroid originalAsteroid, Set<Point2D> checkedPts, Point2D seekPt, PointTransform transformUsed, boolean alreadySawAsteroid) {
			if (checkedPts.contains(seekPt)) {
				//don't double-count
				return false;
			}
			else {
				checkedPts.add(seekPt);
			}
			if (asteroidLocations.contains(seekPt) && !alreadySawAsteroid) {
				alreadySawAsteroid = true;
				originalAsteroid.registerSeenAsteroid(seekPt);
			}
			if (Point2DUtils.pointInsideBoundary(seekPt, true, topBoundary, rightBoundary, bottomBoundary, leftBoundary)) {
				return canSeeAsteroid(originalAsteroid, checkedPts, transformUsed.apply(seekPt), transformUsed, alreadySawAsteroid);
			}
			else {
				return alreadySawAsteroid;
			}
		}
		
		public Point2D findLucky200() {
			//My answer to part 1 was 263. So I know that I won't complete a full revolution before finding the answer.
			//So work from the top and compare slopes, rotating left, until I count off the 64th asteroid in this sorted order
			Asteroid center = findMostConnectedAsteroid();
			Point2D centerPt = center.getPt();
			Set<Point2D> visibleAsteroids = center.getAsteroidsICanSee();
			//lop off the first three quadrants, the answer is in the top-left quadrant
			Queue<Point2D> asteroidsInTopLeft = visibleAsteroids.stream()
				.filter(pt -> pt.getX() < centerPt.getX() && pt.getY() < centerPt.getY())
				.collect(() -> new PriorityQueue<Point2D>((pt1, pt2) -> 
					Double.compare(
						Point2DUtils.getSlope(centerPt, pt2),
						Point2DUtils.getSlope(centerPt, pt1) //reverse sort to get the closest to top-center to come off first
					)
				), Queue::add, Queue::addAll);
			for (int i = 0; i < 63; i++)
			{
				asteroidsInTopLeft.poll();
			}
			return asteroidsInTopLeft.poll();
		}
		
		@Override
		public String toString() {
			Asteroid center = findMostConnectedAsteroid();
			StringBuilder builder = new StringBuilder();
			for (int y = 0; y < bottomBoundary; y++)
			{
				for (int x = 0; x < rightBoundary; x++)
				{
					Point2D pt = new Point2D.Double(x, y);
					if (pt.equals(center.getPt())) {
						builder.append("O");
					}
					else if (center.getAsteroidsICanSee().contains(pt)) {
						builder.append("X");
					}
					else if (asteroidLocations.contains(pt)) {
						builder.append("#");
					}
					else {
						builder.append(".");
					}
				}
				builder.append("\n");
			}
			return builder.toString();
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
		private final Set<Point2D> asteroidsICanSee;

		public Asteroid(Point2D pt)
		{
			this.pt = pt;
			this.asteroidsICanSee = new HashSet<>();
		}
		
		public void registerSeenAsteroid(Point2D asteroidPt) {
			asteroidsICanSee.add(asteroidPt);
		}

		public Set<Point2D> getAsteroidsICanSee()
		{
			return asteroidsICanSee;
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
