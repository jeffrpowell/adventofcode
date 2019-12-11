package com.jeffrpowell.adventofcode;

import java.awt.geom.Point2D;
import java.util.Map;

public class Point2DUtils
{
	private Point2DUtils() {}
	
	public static double getManhattenDistance(Point2D pt1, Point2D pt2) {
		return Math.abs(pt1.getY() - pt2.getY()) + Math.abs(pt1.getX() - pt2.getX());
	}
	
	public static double getEuclideanDistance(Point2D pt1, Point2D pt2) {
		return pt1.distance(pt2);
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
	 * 
	 * @param minXBar
	 * @param minYBar
	 * @param maxXBar
	 * @param maxYBar
	 * @return
	 * @deprecated You can just use the static method directly on PointTransform
	 */
	@Deprecated
	public static Map<Double, Map<Double, PointTransform>> createTransformFunctionCache(double minXBar, double minYBar, double maxXBar, double maxYBar) {
		return PointTransform.createTransformFunctionCache(minXBar, minYBar, maxXBar, maxYBar);
	}
	
}
