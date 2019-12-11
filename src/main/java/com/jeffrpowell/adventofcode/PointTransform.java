package com.jeffrpowell.adventofcode;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PointTransform implements Function<Point2D, Point2D>{
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
	
	public static Map<Double, Map<Double, PointTransform>> createTransformFunctionCache(double minXBar, double minYBar, double maxXBar, double maxYBar) {
		Map<Double, Map<Double, PointTransform>> transformCache = new HashMap<>();
		for (double x = minXBar; x <= maxXBar; x++)
		{
			transformCache.put(x, new HashMap<>());
			if (x == 0) {
				transformCache.put(-x, new HashMap<>());
			}
			for (double y = minYBar; y <= maxYBar; y++)
			{
				if (x == 0 && y == 0) {
					continue;
				}
				else if (x == 0) {
					transformCache.get(-x).put(y, new PointTransform(-x, y));
				}
				else if (y == 0) {
					transformCache.get(x).put(-y, new PointTransform(x, -y));
				}
				transformCache.get(x).put(y, new PointTransform(x, y));
			}
		}
		return transformCache;
	}
}
