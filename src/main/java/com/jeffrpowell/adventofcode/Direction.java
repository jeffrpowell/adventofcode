package com.jeffrpowell.adventofcode;

import java.awt.geom.Point2D;
import java.util.function.Function;

public enum Direction
{
	LEFT(pt->new Point2D.Double(pt.getX() - 1, pt.getY()), true),
	RIGHT(pt->new Point2D.Double(pt.getX() + 1, pt.getY()), true),
	UP(pt->new Point2D.Double(pt.getX(), pt.getY() - 1), true),
	DOWN(pt->new Point2D.Double(pt.getX(), pt.getY() + 1), true),
    UP_LEFT(pt->new Point2D.Double(pt.getX() - 1, pt.getY() - 1), false),
    UP_RIGHT(pt->new Point2D.Double(pt.getX() + 1, pt.getY() - 1), false),
    DOWN_LEFT(pt->new Point2D.Double(pt.getX() - 1, pt.getY() + 1), false),
    DOWN_RIGHT(pt->new Point2D.Double(pt.getX() + 1, pt.getY() + 1), false);
	
	private final Function<Point2D, Point2D> travelFn;
    private final boolean cardinal;

	private Direction(Function<Point2D, Point2D> travelFn, boolean cardinal)
	{
		this.travelFn = travelFn;
        this.cardinal = cardinal;
	}
    
    public boolean isCardinal() {
        return cardinal;
    }

	public Point2D travelFrom(Point2D location)
	{
		return travelFn.apply(location);
	}
    
	public Point2D travelFromNTimes(Point2D location, int n)
	{
		for (int i = 0; i < n; i++) {
            location = travelFn.apply(location);
        }
        return location;
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
			case UP_LEFT:
				return DOWN_LEFT;
			case UP_RIGHT:
				return UP_LEFT;
			case DOWN_LEFT:
				return DOWN_RIGHT;
			case DOWN_RIGHT:
				return UP_RIGHT;
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
			case UP_LEFT:
				return UP_RIGHT;
			case UP_RIGHT:
				return DOWN_RIGHT;
			case DOWN_LEFT:
				return UP_LEFT;
			case DOWN_RIGHT:
				return DOWN_LEFT;
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
			case UP_LEFT:
				return DOWN_RIGHT;
			case UP_RIGHT:
				return DOWN_LEFT;
			case DOWN_LEFT:
				return UP_RIGHT;
			case DOWN_RIGHT:
				return UP_LEFT;
		}
		return null;
	}
}
