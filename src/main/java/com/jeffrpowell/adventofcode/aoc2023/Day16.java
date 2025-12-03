package com.jeffrpowell.adventofcode.aoc2023;

import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import com.jeffrpowell.adventofcode.algorithms.Direction;
import com.jeffrpowell.adventofcode.algorithms.Grid;
import com.jeffrpowell.adventofcode.algorithms.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day16 extends Solution2023<List<String>>{

    @Override
    public int getDay() {
        return 16;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        Grid<GridType> grid = new Grid<>(input, (in, pt) -> GridType.parse(in.get(d2i(pt.getY())).get(d2i(pt.getX()))));
        return Long.toString(getEnergyLevel(grid, new Vector(new Point2D.Double(0, 0), Direction.RIGHT)));
    }

    @Override
    protected String part2(List<List<String>> input) {
        final int rightBoundary = input.get(0).size();
        final int bottomBoundary = input.size();
        Grid<GridType> grid = new Grid<>(input, (in, pt) -> GridType.parse(in.get(d2i(pt.getY())).get(d2i(pt.getX()))));
        return Long.toString(
            grid.keySet().stream()
                .filter(pt -> pt.getX() == 0 || pt.getX() == rightBoundary - 1 || pt.getY() == 0 || pt.getY() == bottomBoundary - 1)
                .map(pt -> {
                    if (pt.getY() == 0) {
                        return new Vector(pt, Direction.DOWN);
                    }
                    else if (pt.getY() == bottomBoundary - 1) {
                        return new Vector(pt, Direction.UP);
                    }
                    else if (pt.getX() == 0) {
                        return new Vector(pt, Direction.RIGHT);
                    }
                    else {
                        return new Vector(pt, Direction.LEFT);
                    }
                })
                .map(v -> getEnergyLevel(grid, v))
                .max(Comparator.naturalOrder()).get()
        );
    }

    private int d2i(Double d) {
        return d.intValue();
    }

    private long getEnergyLevel(Grid<GridType> grid, Vector startV) {
        Set<Point2D> energized = new HashSet<>();
        Set<Vector> visitedReflectors = new HashSet<>();
        Deque<Vector> beamQ = new ArrayDeque<>();
        beamQ.push(startV);
        while(!beamQ.isEmpty()) {
            Vector v = beamQ.pop();
            while(visitedReflectors.add(v) && Point2DUtils.pointInsideBoundary(v.pt, true, grid.inclusiveBoundingBox)) {
                energized.add(v.pt);
                GridType gridType = grid.get(v.pt);
                gridType.splitFn.apply(v).ifPresent(beamQ::add);
                v = gridType.travelFn.apply(v);
            }
        }
        return energized.size();
    }

    enum GridType {
        BLANK(".", v -> new Vector(v.d.travelFrom(v.pt), v.d), v -> Optional.empty()),
        LEFT_UP("/", v -> {
            if (v.d == Direction.RIGHT || v.d == Direction.LEFT) {
                return new Vector(v.d.rotateLeft90().travelFrom(v.pt), v.d.rotateLeft90());
            }
            return new Vector(v.d.rotateRight90().travelFrom(v.pt), v.d.rotateRight90());
        }, v -> Optional.empty()),
        RIGHT_UP("\\", v -> {
            if (v.d == Direction.DOWN || v.d == Direction.UP) {
                return new Vector(v.d.rotateLeft90().travelFrom(v.pt), v.d.rotateLeft90());
            }
            return new Vector(v.d.rotateRight90().travelFrom(v.pt), v.d.rotateRight90());
        }, v -> Optional.empty()),
        DOWN_UP("|", v -> {
            if (v.d == Direction.UP || v.d == Direction.DOWN) {
                return new Vector(v.d.travelFrom(v.pt), v.d);
            }
            return new Vector(Direction.UP.travelFrom(v.pt), Direction.UP);
        }, v -> {
            if (v.d == Direction.UP || v.d == Direction.DOWN) {
                return Optional.empty();
            }
            return Optional.of(new Vector(Direction.DOWN.travelFrom(v.pt), Direction.DOWN));
        }),
        LEFT_RIGHT("-", v -> {
            if (v.d == Direction.LEFT || v.d == Direction.RIGHT) {
                return new Vector(v.d.travelFrom(v.pt), v.d);
            }
            return new Vector(Direction.LEFT.travelFrom(v.pt), Direction.LEFT);
        }, v -> {
            if (v.d == Direction.LEFT || v.d == Direction.RIGHT) {
                return Optional.empty();
            }
            return Optional.of(new Vector(Direction.RIGHT.travelFrom(v.pt), Direction.RIGHT));
        });

        String c;
        Function<Vector, Vector> travelFn;
        Function<Vector, Optional<Vector>> splitFn;
        private GridType(String c, Function<Vector, Vector> travelFn, Function<Vector, Optional<Vector>> splitFn) {
            this.c = c;
            this.travelFn = travelFn;
            this.splitFn = splitFn;
        }

        public static GridType parse(String s) {
            return Arrays.stream(values()).filter(type -> type.c.equals(s)).findAny().orElse(BLANK);
        }
    }

    record Vector(Point2D pt, Direction d){}
}
