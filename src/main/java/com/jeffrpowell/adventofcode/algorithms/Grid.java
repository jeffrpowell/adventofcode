package com.jeffrpowell.adventofcode.algorithms;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grid<T> extends HashMap<Point2D, T>{
    public Map<Point2D, T> map;
    public Point2DUtils.BoundingBox inclusiveBoundingBox;
    
    public static int d2i(Double d) {
        return d.intValue();
    }

    public static Stream<Point2D> generatePointStream(double leftBoundary, double topBoundary, double rightBoundary, double bottomBoundary) {
        Stream.Builder<Point2D> pointStream = Stream.<Point2D>builder();
        for (double row = topBoundary; row < bottomBoundary; row += 1.0) {
            for (double col = leftBoundary; col < rightBoundary; col += 1.0) {
                pointStream.accept(new Point2D.Double(col, row));
            }
        }
        return pointStream.build();
    }

    private static <T> BiFunction<List<List<T>>, Point2D, T> defaultMapFn(List<List<T>> input) {
        return (i, pt) -> i.get(d2i(pt.getY())).get(d2i(pt.getX()));
    }
    
    public Grid(double leftBoundaryInc, double topBoundaryInc, double rightBoundaryExc, double bottomBoundaryExc, Function<Point2D, T> mapFn) {
        super();
        putAll(generatePointStream(leftBoundaryInc, topBoundaryInc, rightBoundaryExc, bottomBoundaryExc)
            .collect(Collectors.toMap(
                Function.identity(), 
                mapFn
            )));
        Point2D topLeft = new Point2D.Double(leftBoundaryInc, topBoundaryInc);
        Point2D bottomRight = new Point2D.Double(rightBoundaryExc - 1, bottomBoundaryExc - 1);
        this.inclusiveBoundingBox = new Point2DUtils.BoundingBox(topLeft, bottomRight);
    }

    public Grid(List<List<T>> input) {
        this(input, 0, 0, input.get(0).size(), input.size(), defaultMapFn(input));
    }

    public Grid(List<List<T>> input, double leftBoundary, double topBoundary, double rightBoundary, double bottomBoundary) {
        this(input, leftBoundary, topBoundary, rightBoundary, bottomBoundary, defaultMapFn(input));
    }

    public <I> Grid(List<List<I>> input, BiFunction<List<List<I>>, Point2D, T> mapFn) {
        this(input, 0, 0, input.get(0).size(), input.size(), mapFn);
    }

    public <I> Grid(List<List<I>> input, double leftBoundary, double topBoundary, double rightBoundary, double bottomBoundary, BiFunction<List<List<I>>, Point2D, T> mapFn) {
        super();
        putAll(generatePointStream(0, 0, input.get(0).size(), input.size())
            .collect(Collectors.toMap(
                Function.identity(), 
                pt -> mapFn.apply(input, pt)
            )));
        Point2D topLeft = new Point2D.Double(0, 0);
        Point2D bottomRight = new Point2D.Double(input.size() - 1, input.get(0).size() - 1);
        this.inclusiveBoundingBox = new Point2DUtils.BoundingBox(topLeft, bottomRight);
    }
}
