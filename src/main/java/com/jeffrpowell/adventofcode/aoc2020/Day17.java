package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.Point3DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.geometry.Point3D;

public class Day17 extends Solution2020<List<String>>{
    double minX;
    double minY;
    double minZ;
    double maxX;
    double maxY;
    double maxZ;
    
    @Override
    public int getDay() {
        return 17;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        final Map<Point3D, Cube> finalPocket = new HashMap<>();
        for (int rowI = 0; rowI < input.size(); rowI++) {
            List<String> row = input.get(rowI);
            for (int col = 0; col < row.size(); col++) {
                Point3D pt = new Point3D(col, rowI, 0);
                Cube cube = new Cube(row.get(col));
                finalPocket.put(pt, cube);
                if (cube.isActive()) {
                    Point3DUtils.getAdjacentPts(pt, true).stream()
                        .forEach(p -> finalPocket.putIfAbsent(p, new Cube(false)));
                }
            }
        }
        Map<Point3D, Cube> pocket = finalPocket;
        printPocket(pocket);
        for (int i = 0; i < 6; i++) {
            pocket = cycle(pocket);
            printPocket(pocket);
        }
        return Long.toString(pocket.values().stream()
            .filter(Cube::isActive)
            .count());
    }
    
    private static Map<Point3D, Cube> cycle(Map<Point3D, Cube> pocket) {
        Map<Point3D, Cube> newPocket = new HashMap<>();
        
        for (Map.Entry<Point3D, Cube> entry : pocket.entrySet()) {
            Set<Point3D> neighbors = Point3DUtils.getAdjacentPts(entry.getKey(), true);
            boolean cyclesToActive = entry.getValue().cyclesToActive(neighbors.stream()
                .map(pt -> pocket.getOrDefault(pt, new Cube(false))).collect(Collectors.toList()));
            newPocket.put(entry.getKey(), new Cube(cyclesToActive));
            if (cyclesToActive) {
                neighbors.stream().forEach(p -> newPocket.putIfAbsent(p, new Cube(false)));
            }
        }
        return newPocket;
    }

    @Override
    protected String part2(List<List<String>> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private static void printPocket(Map<Point3D, Cube> pocket) {
        double minX = pocket.keySet().stream().map(Point3D::getX).min(Double::compare).get();
        double maxX = pocket.keySet().stream().map(Point3D::getX).max(Double::compare).get();
        double minY = pocket.keySet().stream().map(Point3D::getY).min(Double::compare).get();
        double maxY = pocket.keySet().stream().map(Point3D::getY).max(Double::compare).get();
        double minZ = pocket.keySet().stream().map(Point3D::getZ).min(Double::compare).get();
        double maxZ = pocket.keySet().stream().map(Point3D::getZ).max(Double::compare).get();
        System.out.println("\n\n###########################################");
        System.out.println("#           THE POCKET DIMENSION          #");
        System.out.println("###########################################\n\n");
        for (double z = minZ; z <= maxZ; z += 1.0) {
            System.out.println("z = " + z);
            for (double y = minY; y <= maxY; y += 1.0) {
                for (double x = minX; x <= maxX; x += 1.0) {
                    Cube c = pocket.getOrDefault(new Point3D(x, y, z), new Cube(false));
                    System.out.print(c.isActive() ? "#" : ".");
                }
                System.out.println("");
            }
        }
    }

    private static class Cube {
        boolean active;
        
        public Cube(String init) {
            this.active = init.equals("#");
        }

        public Cube(boolean active) {
            this.active = active;
        }
        
        public boolean isActive() {
            return active;
        }
        
        public boolean cyclesToActive(Collection<Cube> neighbors) {
            long activeNeighbors = neighbors.stream().filter(Cube::isActive).count();
            return active && (activeNeighbors == 2 || activeNeighbors == 3) || !active && activeNeighbors == 3;
        }
    }
}
