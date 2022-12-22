package com.jeffrpowell.adventofcode.aoc2022;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.Point3DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

import javafx.geometry.Point3D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

public class Day22 extends Solution2022<Section>{

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
        Map<Point3D, Boolean> grid = parseGrid(map);
        String steps = input.get(1).getInput(InputParserFactory.getStringParser()).get(0);
        List<Integer> walks = Arrays.stream(Pattern.compile("[R|L]").split(steps)).map(Integer::parseInt).collect(Collectors.toList());
        List<String> turns = Arrays.stream(Pattern.compile("\\d+").split(steps)).collect(Collectors.toList());
        turns.remove(0);
        return null;
    }

    private Map<Point3D, Boolean> parseGrid(List<List<String>> map) {
        Map<Point3D, Boolean> grid = new HashMap<>();
        Rectangle bottom = new Rectangle(50, 0, 50, 50);
        Rectangle right = new Rectangle(100, 0, 50, 50);
        Rectangle front = new Rectangle(50, 50, 50, 50);
        Rectangle top = new Rectangle(50, 100, 50, 50);
        Rectangle left = new Rectangle(0, 100, 50, 50);
        Rectangle back = new Rectangle(0, 150, 50, 50);
        
        Point3D center = new Point3D(24.5, 24.5, 24.5);
        Point3DUtils.BoundingBox box;
        grid.putAll(parseSide(map, bottom, new Translate(-50, 0, -1)));
        box = Point3DUtils.getBoundingBox(grid.keySet());
        grid.putAll(parseSide(map, right, new Translate(-100, 0)
            .createConcatenation(new Rotate(90, center.getX(), center.getY(), center.getZ(), Rotate.Y_AXIS))
            .createConcatenation(new Translate(1, 0, 0))));
        box = Point3DUtils.getBoundingBox(grid.keySet());
        grid.putAll(parseSide(map, front, new Translate(-50, -50)
            .createConcatenation(new Rotate(90, center.getX(), center.getY(), center.getZ(), Rotate.X_AXIS))
            .createConcatenation(new Translate(0, 1, 0))));
        box = Point3DUtils.getBoundingBox(grid.keySet());
        grid.putAll(parseSide(map, top, new Translate(-50, -100)
            .createConcatenation(new Rotate(180, center.getX(), center.getY(), center.getZ(), Rotate.X_AXIS))
            .createConcatenation(new Translate(0, 0, 1))));
        box = Point3DUtils.getBoundingBox(grid.keySet());
        grid.putAll(parseSide(map, back, new Translate(0, -150)
            .createConcatenation(new Rotate(180, center.getX(), center.getY(), center.getZ(), Rotate.X_AXIS))
            .createConcatenation(new Rotate(90, center.getX(), center.getY(), center.getZ(), Rotate.Y_AXIS))
            .createConcatenation(new Rotate(90, center.getX(), center.getY(), center.getZ(), Rotate.Z_AXIS))
            .createConcatenation(new Translate(0, -1, 0))));
        box = Point3DUtils.getBoundingBox(grid.keySet());
        grid.putAll(parseSide(map, left, new Translate(0, -100)
            .createConcatenation(new Rotate(180, center.getX(), center.getY(), center.getZ(), Rotate.X_AXIS))
            .createConcatenation(new Rotate(90, center.getX(), center.getY(), center.getZ(), Rotate.Y_AXIS))
            .createConcatenation(new Translate(-1, 0, 0))));
        box = Point3DUtils.getBoundingBox(grid.keySet());
        return grid;
    }

    private Map<Point3D, Boolean> parseSide(List<List<String>> map, Rectangle rect, Transform transform) {
        return Point2DUtils.generateGrid(rect.getX(), rect.getY(), rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight())
            .collect(Collectors.toMap(
                p -> transform(p.getX(), p.getY(), 0, transform),
                p -> getValue(map, d2i(p.getX()), d2i(p.getY()))
            ));
    }
    
    private Point3D transform(double x, double y, double z, Transform t) {
        Point3D transformed = t.transform(x, y, z);
        return new Point3D(Math.round(transformed.getX()), Math.round(transformed.getY()), Math.round(transformed.getZ()));
    }

    private boolean getValue(List<List<String>> map, int x, int y) {
        return map.get(y).get(x).equals(".");
    }
    
    private int d2i(Double d) {
        return d.intValue();
    }

    record Position2(Point2D pt, Direction d){}

}
