package com.jeffrpowell.adventofcode;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.geometry.Point3D;

public class Point3DUtils
{
	private Point3DUtils() {}
    
    public static Stream<Point3D> generateGrid(double minX, double minY, double maxX, double maxY, double minZ, double maxZ) {
        Stream.Builder<Point3D> pointStream = Stream.<Point3D>builder();
        for (double row = minY; row < maxY; row += 1.0) {
            for (double col = minX; col < maxX; col += 1.0) {
                for (double dep = minZ; dep < maxZ; dep += 1.0) {
                    pointStream.accept(new Point3D(col, row, dep));
                }
            }
        }
        return pointStream.build();
    }
	
	public static double getManhattenDistance(Point3D pt1, Point3D pt2) {
		return Math.abs(pt1.getY() - pt2.getY()) + Math.abs(pt1.getX() - pt2.getX()) + Math.abs(pt1.getZ() - pt2.getZ());
	}
	
	public static double getEuclideanDistance(Point3D pt1, Point3D pt2) {
		return pt1.distance(pt2);
	}
    
    public static Point3D applyVectorToPt(Point3D vector, Point3D pt) {
        return new Point3D(pt.getX() + vector.getX(), pt.getY() + vector.getY(), pt.getZ() + vector.getZ());
    }
    
    public static Point3D applyVectorToPtNTimes(Point3D vector, Point3D pt, int n) {
        return new Point3D(pt.getX() + vector.getX() * n, pt.getY() + vector.getY() * n, pt.getZ() + vector.getZ() * n);
    }
	
	public static boolean pointInsideBoundary(Point3D pt, boolean inclusive, double minY, double maxX, double maxY, double minX, double minZ, double maxZ) {
		double x = pt.getX();
		double y = pt.getY();
		double z = pt.getZ();
		if (inclusive) {
			return x <= maxX &&
				x >= minX &&
				y <= maxY &&
				y >= minY &&
                z >= minZ &&
                z <= maxZ;
		}
		else {
			return x < maxX &&
				x > minX &&
				y < maxY &&
				y > minY &&
                z > minZ &&
                z < maxZ;
		}
	}
	
	public static boolean pointInsideBoundary(Point3D pt, boolean inclusive, BoundingBox boundary) {
		return pointInsideBoundary(pt, inclusive, boundary.min().getY(), boundary.max().getX(), boundary.max().getY(), boundary.min().getX(), boundary.min().getZ(), boundary.max().getZ());
	}
	
	/**
	 * Defaults to cardinal (4) adjacent neighbors. Use getAdjacentPts(point, true) to get 8 diagonal neighbors.
	 * @param point
	 * @return 
	 */
	public static Set<Point3D> getAdjacentPts(Point3D point) {
		return getAdjacentPts(point, false);
	}
	
	public static Set<Point3D> getAdjacentPts(Point3D point, boolean includeDiagonalNeighbors) {
		double x = point.getX();
		double y = point.getY();
		double z = point.getZ();
		if (includeDiagonalNeighbors) {
			return Stream.of(
				new Point3D(x, y - 1, z),
				new Point3D(x - 1, y - 1, z),
				new Point3D(x - 1, y, z),
				new Point3D(x - 1, y + 1, z),
				new Point3D(x, y + 1, z),
				new Point3D(x + 1, y + 1, z),
				new Point3D(x + 1, y, z),
				new Point3D(x + 1, y - 1, z),
				new Point3D(x, y - 1, z - 1),
				new Point3D(x - 1, y - 1, z - 1),
				new Point3D(x - 1, y, z - 1),
				new Point3D(x - 1, y + 1, z - 1),
				new Point3D(x, y + 1, z - 1),
				new Point3D(x + 1, y + 1, z - 1),
				new Point3D(x + 1, y, z - 1),
				new Point3D(x + 1, y - 1, z - 1),
				new Point3D(x, y, z + 1),
				new Point3D(x, y - 1, z + 1),
				new Point3D(x - 1, y - 1, z + 1),
				new Point3D(x - 1, y, z + 1),
				new Point3D(x - 1, y + 1, z + 1),
				new Point3D(x, y + 1, z + 1),
				new Point3D(x + 1, y + 1, z + 1),
				new Point3D(x + 1, y, z + 1),
				new Point3D(x + 1, y - 1, z + 1),
				new Point3D(x, y, z - 1)
                )
				.collect(Collectors.toSet());
		}
		else {
			return Stream.of(
				new Point3D(x, y - 1, z),
				new Point3D(x - 1, y, z),
				new Point3D(x + 1, y, z),
				new Point3D(x, y + 1, z),
				new Point3D(x, y, z + 1),
				new Point3D(x, y, z - 1))
				.collect(Collectors.toSet());
		}
	}
	
	public static Set<Point3D> getBoundedAdjacentPts(Point3D point, double minY, double maxX, double maxY, double minX, double minZ, double maxZ, boolean inclusiveBoundary, boolean includeDiagonalNeighbors) {
		Set<Point3D> adjacentPoints = getAdjacentPts(point, includeDiagonalNeighbors);
		return adjacentPoints.stream()
			.filter(pt -> Point3DUtils.pointInsideBoundary(pt, inclusiveBoundary, minY, maxX, maxY, minX, minZ, maxZ))
			.collect(Collectors.toSet());
	}

	public record BoundingBox(Point3D min, Point3D max){};

	public static BoundingBox getBoundingBox(Collection<Point3D> pts) {
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double minZ = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;
		double maxZ = Double.MIN_VALUE;
		for (Point3D p : pts) {
			if (p.getX() > maxX) {
				maxX = p.getX();
			}
			if (p.getY() > maxY) {
				maxY = p.getY();
			}
			if (p.getZ() > maxZ) {
				maxZ = p.getZ();
			}
			if (p.getX() < minX) {
				minX = p.getX();
			}
			if (p.getY() < minY) {
				minY = p.getY();
			}
			if (p.getZ() < minZ) {
				minZ = p.getZ();
			}
		}
		return new BoundingBox(new Point3D(minX, minY, minZ), new Point3D(maxX, maxY, maxZ));
	}
}
