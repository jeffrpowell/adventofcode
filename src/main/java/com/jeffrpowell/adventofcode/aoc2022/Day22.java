package com.jeffrpowell.adventofcode.aoc2022;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.Grid;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.Point3DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

import javafx.geometry.Point3D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

public class Day22 extends Solution2022<Section>{

    private final boolean real;

    public Day22() {
        real = true;
    }

    public Day22(boolean real) {
        this.real = real;
    }

    @Override
    public int getDay() {
        return 22;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, false));
    }
    
    @Override
    protected String part1(List<Section> input) {
        List<List<String>> map = input.get(0).getInput(InputParserFactory.getTokenSVParser(""));
        Map<Point2D, Boolean> grid = new HashMap<>();
        for (int y = 0; y < map.size(); y++) {
            List<String> row = map.get(y);
            for (int x = 0; x < row.size(); x++) {
                String pt = row.get(x);
                if (pt.equals(" ")) {
                    continue;
                }
                grid.put(new Point2D.Double(x, y), pt.equals("."));
            }
        }
        String steps = input.get(1).getInput(InputParserFactory.getStringParser()).get(0);
        List<Integer> walks = Arrays.stream(Pattern.compile("[R|L]").split(steps)).map(Integer::parseInt).collect(Collectors.toList());
        List<String> turns = Arrays.stream(Pattern.compile("\\d+").split(steps)).collect(Collectors.toList());
        turns.remove(0);
        Point2D start = new Point2D.Double(map.get(0).indexOf("."), 0);
        Position p = new Position(start, Direction.RIGHT);
        List<Position> visited = new ArrayList<>();
        visited.add(p);
        for (int i = 0; i < walks.size(); i++) {
            p = walk(p, walks.get(i), grid, visited);
            if (i < turns.size()) {
                Direction newD = turns.get(i).equals("R") ? p.d.rotateRight90() : p.d.rotateLeft90();
                p = new Position(p.pt, newD);
            }
            visited.add(p);
        }
        int directionScore = switch(p.d) {
            case UP -> 3;
            case DOWN -> 1;
            case LEFT -> 2;
            default -> 0;
        };
        return Double.toString(1000 * (p.pt.getY() + 1) + 4 * (p.pt.getX() + 1) + directionScore);
    }

    record Position(Point2D pt, Direction d){}

    private Position walk(Position p, int distance, Map<Point2D, Boolean> grid, List<Position> visited) {
        for (int i = 0; i < distance; i++) {
            Point2D explore = Point2DUtils.movePtInDirection(p.pt, p.d, 1);
            if (!grid.containsKey(explore)) {
                //wrap
                Stream<Point2D> gridPts = grid.keySet().stream();
                final Position oldP = p;
                explore = switch(p.d) {
                    case UP -> gridPts.filter(gridPt -> gridPt.getX() == oldP.pt.getX()).max(Comparator.comparing(Point2D::getY)).get();
                    case DOWN -> gridPts.filter(gridPt -> gridPt.getX() == oldP.pt.getX()).min(Comparator.comparing(Point2D::getY)).get();
                    case LEFT -> gridPts.filter(gridPt -> gridPt.getY() == oldP.pt.getY()).max(Comparator.comparing(Point2D::getX)).get();
                    default -> gridPts.filter(gridPt -> gridPt.getY() == oldP.pt.getY()).min(Comparator.comparing(Point2D::getX)).get();
                };
            }
            if (Boolean.TRUE.equals(grid.get(explore))) {
                //walk
                p = new Position(explore, p.d);
                visited.add(p);
            }
            else {
                //hit wall
                return p;
            }
        }
        visited.remove(visited.size() - 1);
        return p;
    }
    
    private void printGrid(Map<Point2D, Boolean> grid, List<Position> visited) {
        Map<Point2D, String> printGrid = grid.entrySet().stream().collect(Collectors.toMap(
            Map.Entry::getKey,
            e -> e.getValue() ? "." : "#"
        ));
        visited.stream().forEach(
            p -> {
                String dStr = switch(p.d) {
                    case UP -> "^";
                    case DOWN -> "v";
                    case LEFT -> "<";
                    default -> ">";
                };
                printGrid.put(p.pt, dStr);
            }
        );
        Point2DUtils.printPoints(printGrid, " ");
    }

    @Override
    protected String part2(List<Section> input) {
        List<List<String>> map = input.get(0).getInput(InputParserFactory.getTokenSVParser(""));
        try {
            List<Side> sides = makeSides();
            Map<Point3D, Boolean> grid = sides.stream().map(s -> s.parseSide(map)).collect(HashMap::new, Map::putAll, Map::putAll);
            Point3D start = new Point3D(0,0,50);
            Point3D vector = new Point3D(1, 0, 0);
            String steps = input.get(1).getInput(InputParserFactory.getStringParser()).get(0);
            List<Integer> walks = Arrays.stream(Pattern.compile("[R|L]").split(steps)).map(Integer::parseInt).collect(Collectors.toList());
            List<String> turns = Arrays.stream(Pattern.compile("\\d+").split(steps)).collect(Collectors.toList());
            turns.remove(0);
            Position2 p = new Position2(start, vector);
            List<Position2> visited = new ArrayList<>();
            visited.add(p);
            for (int i = 0; i < walks.size(); i++) {
                p = walk2(p, walks.get(i), grid, visited);
                if (i < turns.size()) {
                    Point3D newV = rotateVector(turns.get(i).equals("R"), p, sides);
                    p = new Position2(p.pt, newV);
                }
                visited.add(p);
            }
            final Point3D pt = p.pt;
            Point3D remappedPt = sides.stream().filter(s -> s.containsPt(pt)).map(s -> {
                try {
                    return s.reverseMapPt(pt);
                } catch (NonInvertibleTransformException e) {
                    return pt;
                }
            }).findFirst().get();
            final Point3D lastPt = visited.get(visited.size() - 1).pt;
            Point3D remappedLastPt = sides.stream().filter(s -> s.containsPt(lastPt)).map(s -> {
                try {
                    return s.reverseMapPt(lastPt);
                } catch (NonInvertibleTransformException e) {
                    return lastPt;
                }
            }).findFirst().get();
            int directionScore = 0;
            if (remappedPt.getX() - remappedLastPt.getX() == 1) {
                directionScore = 0;
            }
            else if (remappedPt.getX() - remappedLastPt.getX() == -1) {
                directionScore = 2;
            }
            else if (remappedPt.getY() - remappedLastPt.getY() == -1) {
                directionScore = 3;
            }
            else if (remappedPt.getY() - remappedLastPt.getY() == 1) {
                directionScore = 1;
            }
            return Double.toString(1000 * (p.pt.getY() + 1) + 4 * (p.pt.getX() + 1) + directionScore);
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Point3D rotateVector(boolean right, Position2 p, List<Side> sides) throws NonInvertibleTransformException {
        final Point3D pt = p.pt;
        Point3D axis = sides.stream().filter(s -> s.containsPt(pt)).map(Side::getAxis).findFirst().get();
        Rotate r = new Rotate(90, axis);
        if ((right && axis != Rotate.Z_AXIS) || (!right && axis == Rotate.Z_AXIS)) {
            return r.transform(p.v);
        }
        else {
            return r.createInverse().transform(p.v);
        }
    }

    private List<Side> makeSides() throws NonInvertibleTransformException {
        List<Side> sides = new ArrayList<>();
        Point3D center = new Point3D(24.5, 24.5, 24.5);
        
        Rectangle top_rect = new Rectangle(50, 0, 50, 50);
        Rectangle right_rect = new Rectangle(100, 0, 50, 50);
        Rectangle front_rect = new Rectangle(50, 50, 50, 50);
        Rectangle bottom_rect = new Rectangle(50, 100, 50, 50);
        Rectangle left_rect = new Rectangle(0, 100, 50, 50);
        Rectangle back_rect = new Rectangle(0, 150, 50, 50);

        Point3D top_offset = new Point3D(0,0,1);
        Point3D right_offset = new Point3D(1,0,0);
        Point3D front_offset = new Point3D(0,1,0);
        Point3D bottom_offset = new Point3D(0,0,-1);
        Point3D left_offset = new Point3D(-1,0,0);
        Point3D back_offset = new Point3D(0,-1,0);
        
        Point3D top_axis = Rotate.Z_AXIS;
        Point3D right_axis = Rotate.X_AXIS;
        Point3D front_axis = Rotate.Y_AXIS;
        Point3D bottom_axis = Rotate.Z_AXIS;
        Point3D left_axis = Rotate.X_AXIS;
        Point3D back_axis = Rotate.Y_AXIS;

        /*
         *   ##
         *   #
         *  ##
         *  #
         */
        Transform top_transform = new Translate(top_offset.getX(), top_offset.getY(), top_offset.getZ())
            .createConcatenation(new Translate(-top_rect.getX(), -top_rect.getY(), center.getZ() * 2.0))
        ;
        Transform right_transform = new Translate(right_offset.getX() + center.getX() * 2.0, right_offset.getY(), right_offset.getZ())
        .createConcatenation(new Rotate(90, center.getX(), center.getY(), center.getZ(), Rotate.Y_AXIS))
        .createConcatenation(new Translate(-right_rect.getX(), -right_rect.getY()))
        ;
        Transform front_transform = new Translate(front_offset.getX(), front_offset.getY() + center.getY() * 2.0, front_offset.getZ())
        .createConcatenation(new Rotate(90, center.getX(), center.getY(), center.getZ(), Rotate.X_AXIS))
        .createConcatenation(new Translate(-front_rect.getX(), -front_rect.getY()))
        ;
        Transform bottom_transform = new Translate(bottom_offset.getX(), bottom_offset.getY(), bottom_offset.getZ() - center.getZ() * 2.0)
            .createConcatenation(new Rotate(180, center.getX(), center.getY(), center.getZ(), Rotate.X_AXIS))
            .createConcatenation(new Translate(-bottom_rect.getX(), -bottom_rect.getY()))
        ;
        Transform left_transform = new Translate(left_offset.getX(), left_offset.getY(), left_offset.getZ())
            .createConcatenation(new Rotate(90, center.getX(), center.getY(), center.getZ(), Rotate.Y_AXIS).createInverse())
            .createConcatenation(new Rotate(180, center.getX(), center.getY(), center.getZ(), Rotate.X_AXIS))
            .createConcatenation(new Translate(-left_rect.getX(), -left_rect.getY()))
        ;
        Transform back_transform = new Translate(back_offset.getX(), back_offset.getY(), back_offset.getZ())
            .createConcatenation(new Rotate(90, center.getX(), center.getY(), center.getZ(), Rotate.Z_AXIS).createInverse())
            .createConcatenation(new Rotate(90, center.getX(), center.getY(), center.getZ(), Rotate.Y_AXIS))
            .createConcatenation(new Rotate(180, center.getX(), center.getY(), center.getZ(), Rotate.X_AXIS))
            .createConcatenation(new Translate(-back_rect.getX(), -back_rect.getY()))
        ;

        if (!real) {
            center = new Point3D(1.5, 1.5, 1.5);
            top_rect = new Rectangle(8, 0, 4, 4);
            right_rect = new Rectangle(12, 8, 4, 4);
            front_rect = new Rectangle(8, 4, 4, 4);
            bottom_rect = new Rectangle(8, 8, 4, 4);
            left_rect = new Rectangle(4, 4, 4, 4);
            back_rect = new Rectangle(0, 4, 4, 4);

            /*
             *   #
             * ###
             *   ##
             */
            top_transform = new Translate(top_offset.getX(), top_offset.getY(), top_offset.getZ())
                .createConcatenation(new Translate(-top_rect.getX(), -top_rect.getY(), center.getZ() * 2.0))
            ;
            right_transform = new Translate(right_offset.getX(), right_offset.getY(), right_offset.getZ())
                .createConcatenation(new Rotate(90, center.getX(), center.getY(), center.getZ(), Rotate.Y_AXIS))
                .createConcatenation(new Translate(-right_rect.getX(), -right_rect.getY()))
            ;
            front_transform = new Translate(front_offset.getX(), front_offset.getY(), front_offset.getZ())
                .createConcatenation(new Rotate(90, center.getX(), center.getY(), center.getZ(), Rotate.X_AXIS))
                .createConcatenation(new Translate(-front_rect.getX(), -front_rect.getY()))
            ;
            bottom_transform = new Translate(bottom_offset.getX(), bottom_offset.getY(), bottom_offset.getZ() - center.getZ() * 2.0)
                .createConcatenation(new Rotate(180, center.getX(), center.getY(), center.getZ(), Rotate.X_AXIS))
                .createConcatenation(new Translate(-bottom_rect.getX(), -bottom_rect.getY()))
            ;
            left_transform = new Translate(left_offset.getX(), left_offset.getY(), left_offset.getZ())
                .createConcatenation(new Rotate(180, center.getX(), center.getY(), center.getZ(), Rotate.X_AXIS))
                .createConcatenation(new Rotate(90, center.getX(), center.getY(), center.getZ(), Rotate.Y_AXIS))
                .createConcatenation(new Translate(-left_rect.getX(), -left_rect.getY()))
            ;
            back_transform = new Translate(back_offset.getX(), back_offset.getY(), back_offset.getZ())
                .createConcatenation(new Rotate(90, center.getX(), center.getY(), center.getZ(), Rotate.Z_AXIS).createInverse())
                .createConcatenation(new Rotate(90, center.getX(), center.getY(), center.getZ(), Rotate.Y_AXIS))
                .createConcatenation(new Rotate(180, center.getX(), center.getY(), center.getZ(), Rotate.X_AXIS))
                .createConcatenation(new Translate(-back_rect.getX(), -back_rect.getY()))
            ;
        }

        sides.add(new Side("top", top_transform, top_offset, top_axis, top_rect));
        sides.add(new Side("right", right_transform, right_offset, right_axis, right_rect));
        sides.add(new Side("front", front_transform, front_offset, front_axis, front_rect));
        sides.add(new Side("bottom", bottom_transform, bottom_offset, bottom_axis, bottom_rect));
        sides.add(new Side("left", left_transform, left_offset, left_axis, left_rect));
        sides.add(new Side("back", back_transform, back_offset, back_axis, back_rect));
        return sides;
    }

    private static class Side {
        private final String name;
        private final Transform transform;
        private final Point3D offsetVector;
        private final Point3D axis;
        private final Rectangle mapRectangle;
        private Set<Point3D> transformedPts;
        private Point3DUtils.BoundingBox box;

        public Side(String name, Transform transform, Point3D offsetVector, Point3D axis, Rectangle mapRectangle) {
            this.name = name;
            this.transform = transform;
            this.offsetVector = offsetVector;
            this.axis = axis;
            this.mapRectangle = mapRectangle;
        }
        
        public Map<Point3D, Boolean> parseSide(List<List<String>> map) {
            Map<Point3D, Boolean> pts = Grid.generatePointStream(mapRectangle.getX(), mapRectangle.getY(), mapRectangle.getX() + mapRectangle.getWidth(), mapRectangle.getY() + mapRectangle.getHeight())
                .collect(Collectors.toMap(
                    p -> transform(p.getX(), p.getY(), 0, transform),
                    p -> getValue(map, d2i(p.getX()), d2i(p.getY()))
                ));
            this.transformedPts = pts.keySet();
            this.box = Point3DUtils.getBoundingBox(transformedPts);
            return pts;
        }

        private Point3D transform(double x, double y, double z, Transform t) {
            return roundPt(t.transform(x, y, z));
        }
    
        private boolean getValue(List<List<String>> map, int x, int y) {
            return map.get(y).get(x).equals(".");
        }
        
        private int d2i(Double d) {
            return d.intValue();
        }

        public boolean containsPt(Point3D p) {
            return transformedPts.contains(p);
        }

        public Point3D reverseMapPt(Point3D p) throws NonInvertibleTransformException {
            return transform.inverseTransform(p);
        }

        public Point3D getAxis() {
            return axis;
        }
    }

    private static Point3D roundPt(Point3D p) {
        return new Point3D(Math.round(p.getX()), Math.round(p.getY()), Math.round(p.getZ()));
    }

    record Position2(Point3D pt, Point3D v){}
    private Position2 walk2(Position2 p, int distance, Map<Point3D, Boolean> grid, List<Position2> visited) {
        for (int i = 0; i < distance; i++) {
            Point3D explore = roundPt(Point3DUtils.applyVectorToPt(p.v, p.pt));
            Point3D vector = p.v;
            if (!grid.containsKey(explore)) {
                //wrap around
                Point3D oldExplore = explore;
                final Position2 oldP = p;
                // System.out.println("Wrapping while walking " + distance + ". Exited " + p.pt + " in direction " + p.v + " onto " + explore);
                explore = Point3DUtils.getAdjacentPts(explore, false).stream()
                    .filter(pt -> !oldP.pt.equals(pt))
                    .filter(grid::containsKey)
                    .findFirst().get();
                vector = roundPt(explore.subtract(oldExplore));
            }
            if (Boolean.TRUE.equals(grid.get(explore))) {
                //walk
                p = new Position2(explore, vector);
                visited.add(p);
            }
            else {
                //hit wall
                return p;
            }
        }
        visited.remove(visited.size() - 1);
        return p;
    }
}
