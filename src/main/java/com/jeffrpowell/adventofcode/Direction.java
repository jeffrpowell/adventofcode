package com.jeffrpowell.adventofcode;

import java.awt.geom.Point2D;
import java.util.function.Function;

public enum Direction
{
	LEFT(pt->new Point2D.Double(pt.getX() - 1, pt.getY())),
	RIGHT(pt->new Point2D.Double(pt.getX() + 1, pt.getY())),
	UP(pt->new Point2D.Double(pt.getX(), pt.getY() - 1)),
	DOWN(pt->new Point2D.Double(pt.getX(), pt.getY() + 1));
	
	private final Function<Point2D, Point2D> travelFn;

	private Direction(Function<Point2D, Point2D> travelFn)
	{
		this.travelFn = travelFn;
	}

	public Point2D travelFrom(Point2D location)
	{
		return travelFn.apply(location);
	}

	public Direction rotateLeft90()
	{
		switch (this)
		{
			case LEFT:
				return DOWN;
			case RIGHT:
				return UP;
			case UP:
				return LEFT;
			case DOWN:
				return RIGHT;
		}
		return null;
	}

	public Direction rotateRight90()
	{
		switch (this)
		{
			case LEFT:
				return UP;
			case RIGHT:
				return DOWN;
			case UP:
				return RIGHT;
			case DOWN:
				return LEFT;
		}
		return null;
	}

	public Direction opposite()
	{
		switch (this)
		{
			case LEFT:
				return RIGHT;
			case RIGHT:
				return LEFT;
			case UP:
				return DOWN;
			case DOWN:
				return UP;
		}
		return null;
	}
}
