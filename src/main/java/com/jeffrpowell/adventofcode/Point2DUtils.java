package com.jeffrpowell.adventofcode;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Point2DUtils
{
	private Point2DUtils() {}
    
    public static Stream<Point2D> generateGrid(double leftBoundary, double topBoundary, double rightBoundary, double bottomBoundary) {
        Stream.Builder<Point2D> pointStream = Stream.<Point2D>builder();
        for (double row = topBoundary; row < bottomBoundary; row += 1.0) {
            for (double col = leftBoundary; col < rightBoundary; col += 1.0) {
                pointStream.accept(new Point2D.Double(col, row));
            }
        }
        return pointStream.build();
    }
	
	public static double getManhattenDistance(Point2D pt1, Point2D pt2) {
		return Math.abs(pt1.getY() - pt2.getY()) + Math.abs(pt1.getX() - pt2.getX());
	}
	
	public static double getEuclideanDistance(Point2D pt1, Point2D pt2) {
		return pt1.distance(pt2);
	}
    
    public static Point2D applyVectorToPt(Point2D vector, Point2D pt) {
        return new Point2D.Double(pt.getX() + vector.getX(), pt.getY() + vector.getY());
    }
    
    public static Point2D applyVectorToPtNTimes(Point2D vector, Point2D pt, int n) {
        return new Point2D.Double(pt.getX() + vector.getX() * n, pt.getY() + vector.getY() * n);
    }
	
	public static boolean pointInsideBoundary(Point2D pt, boolean inclusive, double topBoundary, double rightBoundary, double bottomBoundary, double leftBoundary) {
		double x = pt.getX();
		double y = pt.getY();
		if (inclusive) {
			return x <= rightBoundary &&
				x >= leftBoundary &&
				y <= bottomBoundary &&
				y >= topBoundary;
		}
		else {
			return x < rightBoundary &&
				x > leftBoundary &&
				y < bottomBoundary &&
				y > topBoundary;
		}
	}
	
	public static double getSlope(Point2D pt1, Point2D pt2) {
		return (pt1.getY() - pt2.getY()) / (pt1.getX() - pt2.getX());
	}
	
	/**
	 * Defaults to cardinal (4) adjacent neighbors. Use getAdjacentPts(point, true) to get 8 diagonal neighbors.
	 * @param point
	 * @return 
	 */
	public static Set<Point2D> getAdjacentPts(Point2D point) {
		return getAdjacentPts(point, false);
	}
	
	public static Set<Point2D> getAdjacentPts(Point2D point, boolean includeDiagonalNeighbors) {
		double x = point.getX();
		double y = point.getY();
		if (includeDiagonalNeighbors) {
			return Stream.of(
				new Point2D.Double(x, y - 1),
				new Point2D.Double(x - 1, y - 1),
				new Point2D.Double(x - 1, y),
				new Point2D.Double(x - 1, y + 1),
				new Point2D.Double(x, y + 1),
				new Point2D.Double(x + 1, y + 1),
				new Point2D.Double(x + 1, y),
				new Point2D.Double(x + 1, y - 1))
				.collect(Collectors.toSet());
		}
		else {
			return Stream.of(
				new Point2D.Double(x, y - 1),
				new Point2D.Double(x - 1, y),
				new Point2D.Double(x + 1, y),
				new Point2D.Double(x, y + 1))
				.collect(Collectors.toSet());
		}
	}
	
	public static Set<Point2D> getBoundedAdjacentPts(Point2D point, double topBoundary, double rightBoundary, double bottomBoundary, double leftBoundary, boolean inclusiveBoundary, boolean includeDiagonalNeighbors) {
		Set<Point2D> adjacentPoints = getAdjacentPts(point, includeDiagonalNeighbors);
		return adjacentPoints.stream()
			.filter(pt -> Point2DUtils.pointInsideBoundary(pt, inclusiveBoundary, topBoundary, rightBoundary, bottomBoundary, leftBoundary))
			.collect(Collectors.toSet());
	}
    
    public static Map<Direction, List<Point2D>> getPointsFromSource(Point2D point, double topBoundary, double rightBoundary, double bottomBoundary, double leftBoundary, boolean inclusiveBoundary, boolean includeDiagonalNeighbors) {
        return Arrays.stream(Direction.values())
            .filter(direction -> includeDiagonalNeighbors || direction.isCardinal())
            .collect(Collectors.toMap(
                Function.identity(),
                direction -> {
                    Point2D next = direction.travelFrom(point);
                    List<Point2D> line = new ArrayList<>();
                    while(pointInsideBoundary(next, inclusiveBoundary, topBoundary, rightBoundary, bottomBoundary, leftBoundary)) {
                        line.add(next);
                        next = direction.travelFrom(next);
                    }
                    return line;
                }
            ));
    }
    
    /**
     * This assumes a left-handed coordinate system, with positive-x going right and positive-y going down
     * https://en.wikipedia.org/wiki/Rotation_matrix
     * @param pt
     * @param degrees
     * @return 
     */
    public static Point2D rotatePtRightDegreesAround0(Point2D pt, double degrees) {
        double radians = degrees * Math.PI / 180.0;
        return new Point2D.Double(
            pt.getX() * Math.round(Math.cos(radians)) - pt.getY() * Math.round(Math.sin(radians)), 
            pt.getX() * Math.round(Math.sin(radians)) + pt.getY() * Math.round(Math.cos(radians))
        );
    }
    
    /**
     * This assumes a left-handed coordinate system, with positive-x going right and positive-y going down
     * https://en.wikipedia.org/wiki/Rotation_matrix
     * @param pt
     * @param degrees
     * @return 
     */
    public static Point2D rotatePtLeftDegreesAround0(Point2D pt, double degrees) {
        double radians = degrees * Math.PI / 180.0;
        return new Point2D.Double(
            pt.getX() * Math.round(Math.cos(radians)) + pt.getY() * Math.round(Math.sin(radians)), 
            pt.getY() * Math.round(Math.cos(radians)) - pt.getX() * Math.round(Math.sin(radians))
        );
    }	
}
