package com.jeffrpowell.adventofcode;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Point2D;

import org.junit.jupiter.api.Test;

public class Point2DUtilsTest {
    @Test
    void testPointInsideLine_horizontal() {
        Point2D pt = new Point2D.Double(3, 4);
        Point2D start = new Point2D.Double(2, 4);
        Point2D end = new Point2D.Double(4, 4);
        assertTrue(Point2DUtils.pointInsideLine(pt, true, start, end));
    }

    @Test
    void testPointInsideLine_vertical() {
        Point2D pt = new Point2D.Double(3, 3);
        Point2D start = new Point2D.Double(3, 2);
        Point2D end = new Point2D.Double(3, 4);
        assertTrue(Point2DUtils.pointInsideLine(pt, true, start, end));
    }
    
    @Test
    void testPointInsideLine_45() {
        Point2D pt = new Point2D.Double(4, 4);
        Point2D start = new Point2D.Double(3, 3);
        Point2D end = new Point2D.Double(5, 5);
        assertTrue(Point2DUtils.pointInsideLine(pt, true, start, end));
    }
    
    @Test
    void testPointInsideLine_45up() {
        Point2D pt = new Point2D.Double(4, 4);
        Point2D start = new Point2D.Double(5, 5);
        Point2D end = new Point2D.Double(3, 3);
        assertTrue(Point2DUtils.pointInsideLine(pt, true, start, end));
    }
    
    @Test
    void testPointInsideLine_false() {
        Point2D pt = new Point2D.Double(4, 3.9);
        Point2D start = new Point2D.Double(3, 3);
        Point2D end = new Point2D.Double(5, 5);
        assertFalse(Point2DUtils.pointInsideLine(pt, true, start, end));
    }
}
