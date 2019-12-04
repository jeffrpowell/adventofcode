package com.jeffrpowell.adventofcode;

import java.awt.geom.Point2D;

public class Point2DUtils
{
	private Point2DUtils() {}
	
	public static double getManhattenDistance(Point2D pt1, Point2D pt2) {
		return Math.abs(pt1.getY() - pt2.getY()) + Math.abs(pt1.getX() - pt2.getX());
	}
}
