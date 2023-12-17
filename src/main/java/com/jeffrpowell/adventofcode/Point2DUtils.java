package com.jeffrpowell.adventofcode;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

    public static <T> Map<Point2D, T> generateGrid(double leftBoundary, double topBoundary, double rightBoundary, double bottomBoundary, Function<Point2D, T> mapFn) {
        return generateGrid(0, 0, rightBoundary, bottomBoundary)
            .collect(Collectors.toMap(
                Function.identity(),
                mapFn
            ));
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

    public static Stream<Point2D> repeatVectorToPtNTimes(Point2D vector, Point2D pt, int n) {
        return Stream.iterate(applyVectorToPt(vector, pt), lastPt -> true, lastPt -> applyVectorToPt(vector, lastPt)).limit(n);
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

    public static boolean pointInsideBoundary(Point2D pt, boolean inclusive, BoundingBox boundingBox) {
        return pointInsideBoundary(pt, inclusive, boundingBox.min().getY(), boundingBox.max().getX(), boundingBox.max().getY(), boundingBox.min().getX());
    }
	
	public static boolean pointInsideLine(Point2D pt, boolean inclusive, Point2D start, Point2D end) {
		if (!inclusive && (pt.equals(start) || pt.equals(end))) {
            return false;
        }
        if (!pointInsideBoundary(pt, inclusive, Math.min(start.getY(), end.getY()), Math.max(start.getX(), end.getX()), Math.max(start.getY(), end.getY()), Math.min(start.getX(), end.getX()))){
            return false;
        }
        double m = getSlope(start, end);
        if (Double.isInfinite(m)) {
            return true;
        }
        double b = start.getY() - m * start.getX();
        return Math.abs(m * pt.getX() + b - pt.getY()) < 0.0001;
	}
	
	public static double getSlope(Point2D pt1, Point2D pt2) {
        if (Math.abs(pt1.getX() - pt2.getX()) < 0.0001) {
            return Double.POSITIVE_INFINITY;
        }
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
        if (degrees < 0) {
            return rotatePtLeftDegreesAround0(pt, -degrees);
        }
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
        if (degrees < 0) {
            return rotatePtRightDegreesAround0(pt, -degrees);
        }
        double radians = degrees * Math.PI / 180.0;
        return new Point2D.Double(
            pt.getX() * Math.round(Math.cos(radians)) + pt.getY() * Math.round(Math.sin(radians)), 
            pt.getY() * Math.round(Math.cos(radians)) - pt.getX() * Math.round(Math.sin(radians))
        );
    }	

    public static void printPoints(Collection<Point2D> pts) {
        System.out.println(pointsToString(pts));
    }

    public static String pointsToString(Collection<Point2D> pts) {
        Point2D min = pts.stream().reduce(new Point2D.Double(Integer.MAX_VALUE, Integer.MAX_VALUE), (accum, next) -> new Point2D.Double(Math.min(accum.getX(), next.getX()), Math.min(accum.getY(), next.getY())));
        Point2D max = pts.stream().reduce(new Point2D.Double(Integer.MIN_VALUE, Integer.MIN_VALUE), (accum, next) -> new Point2D.Double(Math.max(accum.getX(), next.getX()), Math.max(accum.getY(), next.getY())));
        StringBuilder builder = new StringBuilder("(").append(min.getX()).append(",").append(min.getY()).append(") -> (").append(max.getX()).append(",").append(min.getY()).append(")\n");
        for (double row = min.getY(); row < max.getY() + 1; row++) {
            for (double col = min.getX(); col < max.getX() + 1; col++) {
                Point2D pt = new Point2D.Double(col, row);
                builder.append(pts.contains(pt) ? "#": ".");
            }
            builder.append("\n");
        }
        builder.append("(").append(min.getX()).append(",").append(max.getY()).append(") -> (").append(max.getX()).append(",").append(max.getY()).append(")\n");
        return builder.toString();
    }

    public static void printPoints(Map<Point2D, String> pts) {
        System.out.println(pointsToString(pts, "."));
    }

    public static void printPoints(Map<Point2D, String> pts, String emptyString) {
        System.out.println(pointsToString(pts, emptyString));
    }

    public static String pointsToString(Map<Point2D, String> pts, String emptyString) {
        Point2D min = pts.keySet().stream().reduce(new Point2D.Double(Integer.MAX_VALUE, Integer.MAX_VALUE), (accum, next) -> new Point2D.Double(Math.min(accum.getX(), next.getX()), Math.min(accum.getY(), next.getY())));
        Point2D max = pts.keySet().stream().reduce(new Point2D.Double(Integer.MIN_VALUE, Integer.MIN_VALUE), (accum, next) -> new Point2D.Double(Math.max(accum.getX(), next.getX()), Math.max(accum.getY(), next.getY())));
        StringBuilder builder = new StringBuilder("(").append(min.getX()).append(",").append(min.getY()).append(") -> (").append(max.getX()).append(",").append(min.getY()).append(")\n");
        for (double row = min.getY(); row < max.getY() + 1; row++) {
            for (double col = min.getX(); col < max.getX() + 1; col++) {
                Point2D pt = new Point2D.Double(col, row);
                builder.append(pts.containsKey(pt) ? pts.get(pt): emptyString);
            }
            builder.append("\n");
        }
        builder.append("(").append(min.getX()).append(",").append(max.getY()).append(") -> (").append(max.getX()).append(",").append(max.getY()).append(")\n");
        return builder.toString();
    }

    public static Point2D movePtInDirection(Point2D source, Direction direction, int amount) {
        Point2D vector = switch (direction) {
            case UP -> new Point2D.Double(0, -1);
            case DOWN -> new Point2D.Double(0, 1);
            case DOWN_LEFT -> new Point2D.Double(-1, 1);
            case DOWN_RIGHT -> new Point2D.Double(1, 1);
            case LEFT -> new Point2D.Double(-1, 0);
            case RIGHT -> new Point2D.Double(1, 0);
            case UP_LEFT -> new Point2D.Double(-1, -1);
            case UP_RIGHT -> new Point2D.Double(1, -1);
            default -> throw new IllegalArgumentException("Unexpected value: " + direction);
        };
        return applyVectorToPtNTimes(vector, source, amount);
    }

    public record BoundingBox(Point2D min, Point2D max){};

	public static BoundingBox getBoundingBox(Collection<Point2D> pts) {
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = -Double.MAX_VALUE;
		double maxY = -Double.MAX_VALUE;
		for (Point2D p : pts) {
			if (p.getX() > maxX) {
				maxX = p.getX();
			}
			if (p.getY() > maxY) {
				maxY = p.getY();
			}
			if (p.getX() < minX) {
				minX = p.getX();
			}
			if (p.getY() < minY) {
				minY = p.getY();
			}
		}
		return new BoundingBox(new Point2D.Double(minX, minY), new Point2D.Double(maxX, maxY));
	}
}
