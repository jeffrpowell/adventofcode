package com.jeffrpowell.adventofcode;

import java.awt.geom.Point2D;

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
}
