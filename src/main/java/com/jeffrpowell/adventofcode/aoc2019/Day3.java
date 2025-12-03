package com.jeffrpowell.adventofcode.aoc2019;

import com.google.common.collect.Sets;
import com.jeffrpowell.adventofcode.algorithms.Point2DUtils;
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
import java.util.stream.Collectors;

public class Day3 extends Solution2019<List<String>> {
	@Override
	public int getDay()
	{
		return 3;
	}

	@Override
	public InputParser<List<String>> getInputParser()
	{
		return InputParserFactory.getCSVParser();
	}

	@Override
	protected String part1(List<List<String>> input)
	{
		WireTracer wire1 = new WireTracer(input.get(0));
		WireTracer wire2 = new WireTracer(input.get(1));
		Set<TrackingPoint> intersections = wire1.getIntersectionPoints(wire2);
		Point2D zero = new Point2D.Double(0, 0);
		return Integer.toString(intersections.stream()
			.map(trackingPt -> Point2DUtils.getManhattenDistance(zero, trackingPt.getPt()))
			.min(Double::compare).get().intValue());
	}

	@Override
	protected String part2(List<List<String>> input)
	{
		WireTracer wire1 = new WireTracer(input.get(0));
		WireTracer wire2 = new WireTracer(input.get(1));
		Set<TrackingPoint> intersections = wire1.getIntersectionPoints(wire2);
		return Integer.toString(intersections.stream()
			.map(trackingPt -> wire1.getDistanceToPoint(trackingPt.getPt()) + wire2.getDistanceToPoint(trackingPt.getPt()))
			.min(Integer::compare).get());
	}
	
	private static class WireTracer {
		private final List<Vector> vectors;
		private final Set<TrackingPoint> pts;
		private final Map<Point2D, TrackingPoint> lookupMap;

		public WireTracer(List<String> vectors)
		{
			this.vectors = vectors.stream().map(Vector::new).collect(Collectors.toList());
			this.pts = new HashSet<>();
			this.lookupMap = new HashMap<>();
			traceVectors();
		}
		
		private void traceVectors() {
			TrackingPoint currentPoint = new TrackingPoint(new Point2D.Double(0, 0), 0);
			for (Vector vector : vectors)
			{
				currentPoint = vector.applyVectorToPointAndReturnFinalPoint(currentPoint, pts);
			}
			for (TrackingPoint pt : pts)
			{
				lookupMap.put(pt.getPt(), pt);
			}
		}
		
		public Set<TrackingPoint> getIntersectionPoints(WireTracer otherTracer) {
			return Sets.intersection(pts, otherTracer.pts);
		}
		
		public int getDistanceToPoint(Point2D pt) {
			return lookupMap.get(pt).getDistance();
		}
	}
	
	private static class TrackingPoint {
		private final Point2D pt;
		private final int distance;

		public TrackingPoint(Point2D pt, int distance)
		{
			this.pt = pt;
			this.distance = distance;
		}

		public Point2D getPt()
		{
			return pt;
		}

		public int getDistance()
		{
			return distance;
		}

		@Override
		public int hashCode()
		{
			int hash = 7;
			hash = 53 * hash + Objects.hashCode(this.pt);
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
			final TrackingPoint other = (TrackingPoint) obj;
			return Objects.equals(this.pt, other.pt);
		}
		
	}
	
	private static class Vector {
		enum Direction {
			R(pt -> new Point2D.Double(pt.getX() + 1, pt.getY())),
			D(pt -> new Point2D.Double(pt.getX(), pt.getY() + 1)),
			L(pt -> new Point2D.Double(pt.getX() - 1, pt.getY())),
			U(pt -> new Point2D.Double(pt.getX(), pt.getY() - 1));
			
			private final Function<Point2D, Point2D> transformation;

			private Direction(Function<Point2D, Point2D> transformation)
			{
				this.transformation = transformation;
			}
			
			public TrackingPoint transformPt(TrackingPoint trackingPt) {
				Point2D newPt = transformation.apply(trackingPt.getPt());
				return new TrackingPoint(newPt, trackingPt.getDistance() + 1);
			}
		}
		
		private final Direction direction;
		private final int distance;

		public Vector(String vectorStr)
		{
			this.direction = Direction.valueOf(String.valueOf(vectorStr.charAt(0)));
			this.distance = Integer.parseInt(vectorStr.substring(1));
		}
		
		public TrackingPoint applyVectorToPointAndReturnFinalPoint(TrackingPoint startPt, Set<TrackingPoint> pointCollection) {
			TrackingPoint nextPt = startPt;
			for (int i = 0; i < distance; i++)
			{
				nextPt = direction.transformPt(nextPt);
				pointCollection.add(nextPt);
			}
			return nextPt;
		}
	}
}
