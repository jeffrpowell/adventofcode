package com.jeffrpowell.adventofcode.aoc2019;

import com.google.common.collect.Sets;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
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
		Set<Point2D> intersections = wire1.getIntersectionPoints(wire2);
		Point2D zero = new Point2D.Double(0, 0);
		return Integer.toString(intersections.stream()
			.map(pt -> Point2DUtils.getManhattenDistance(zero, pt))
			.min(Double::compare).get().intValue());
	}
	
	private static class WireTracer {
		private final List<Vector> vectors;
		private final Set<Point2D> pts;

		public WireTracer(List<String> vectors)
		{
			this.vectors = vectors.stream().map(Vector::new).collect(Collectors.toList());
			this.pts = new HashSet<>();
			traceVectors();
		}
		
		private void traceVectors() {
			Point2D currentPoint = new Point2D.Double(0, 0);
			for (Vector vector : vectors)
			{
				currentPoint = vector.applyVectorToPointAndReturnFinalPoint(currentPoint, pts);
			}
		}
		
		public Set<Point2D> getIntersectionPoints(WireTracer otherTracer) {
			return Sets.intersection(pts, otherTracer.pts);
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
			
			public Point2D transformPt(Point2D pt) {
				return transformation.apply(pt);
			}
		}
		
		private final Direction direction;
		private final int distance;

		public Vector(String vectorStr)
		{
			this.direction = Direction.valueOf(String.valueOf(vectorStr.charAt(0)));
			this.distance = Integer.parseInt(vectorStr.substring(1));
		}
		
		public Point2D applyVectorToPointAndReturnFinalPoint(Point2D startPt, Set<Point2D> pointCollection) {
			Point2D nextPt = startPt;
			for (int i = 0; i < distance; i++)
			{
				nextPt = direction.transformPt(nextPt);
				pointCollection.add(nextPt);
			}
			return nextPt;
		}
	}

	@Override
	protected String part2(List<List<String>> input)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
