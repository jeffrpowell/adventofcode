package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.PointND;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day17 extends Solution2020<List<String>>{

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
        final Map<PointND, Cube> finalPocket = new HashMap<>();
        for (int rowI = 0; rowI < input.size(); rowI++) {
            List<String> row = input.get(rowI);
            for (int col = 0; col < row.size(); col++) {
                PointND pt = new PointND(col, rowI, 0);
                Cube cube = new Cube(row.get(col));
                finalPocket.put(pt, cube);
                if (cube.isActive()) {
                    pt.getAdjacentPts(true).stream()
                        .forEach(p -> finalPocket.putIfAbsent(p, new Cube(false)));
                }
            }
        }
        Map<PointND, Cube> pocket = finalPocket;
//        printPocket(pocket);
        for (int i = 0; i < 6; i++) {
            pocket = cycle(pocket);
//            printPocket(pocket);
        }
        return Long.toString(pocket.values().stream()
            .filter(Cube::isActive)
            .count());
    }
    
    private static Map<PointND, Cube> cycle(Map<PointND, Cube> pocket) {
        Map<PointND, Cube> newPocket = new HashMap<>();
        
        for (Map.Entry<PointND, Cube> entry : pocket.entrySet()) {
            Set<PointND> neighborPts = entry.getKey().getAdjacentPts(true);
            Map<PointND, Cube> neighbors = neighborPts.stream()
                .collect(Collectors.toMap(
                    Function.identity(),
                    pt -> pocket.getOrDefault(pt, new Cube(false))
                ));
            boolean cyclesToActive = entry.getValue().cyclesToActive(neighbors.values());
            newPocket.put(entry.getKey(), new Cube(cyclesToActive));
            if (cyclesToActive) {
                neighborPts.stream().forEach(p -> newPocket.putIfAbsent(p, new Cube(false)));
            }
        }
        return newPocket;
    }

    @Override
    protected String part2(List<List<String>> input) {
        final Map<PointND, Cube> finalPocket = new HashMap<>();
        for (int rowI = 0; rowI < input.size(); rowI++) {
            List<String> row = input.get(rowI);
            for (int col = 0; col < row.size(); col++) {
                PointND pt = new PointND(col, rowI, 0, 0);
                Cube cube = new Cube(row.get(col));
                finalPocket.put(pt, cube);
                if (cube.isActive()) {
                    pt.getAdjacentPts(true).stream()
                        .forEach(p -> finalPocket.putIfAbsent(p, new Cube(false)));
                }
            }
        }
        Map<PointND, Cube> pocket = finalPocket;
//        printPocket(pocket);
        for (int i = 0; i < 6; i++) {
            pocket = cycle(pocket);
//            printPocket(pocket);
        }
        return Long.toString(pocket.values().stream()
            .filter(Cube::isActive)
            .count());
    }
    
    private static void printPocket(Map<PointND, Cube> pocket) {
        double minX = pocket.keySet().stream().map(pt -> pt.getDimensionN(0)).min(Double::compare).get();
        double maxX = pocket.keySet().stream().map(pt -> pt.getDimensionN(0)).max(Double::compare).get();
        double minY = pocket.keySet().stream().map(pt -> pt.getDimensionN(1)).min(Double::compare).get();
        double maxY = pocket.keySet().stream().map(pt -> pt.getDimensionN(1)).max(Double::compare).get();
        double minZ = pocket.keySet().stream().map(pt -> pt.getDimensionN(2)).min(Double::compare).get();
        double maxZ = pocket.keySet().stream().map(pt -> pt.getDimensionN(2)).max(Double::compare).get();
        System.out.println("\n\n###########################################");
        System.out.println("#           THE POCKET DIMENSION          #");
        System.out.println("###########################################\n\n");
        for (double z = minZ; z <= maxZ; z += 1.0) {
            System.out.println("z = " + z);
            for (double y = minY; y <= maxY; y += 1.0) {
                if (Math.abs(y - 0.0) < 0.0001) {
                    IntStream.rangeClosed(Double.valueOf(minX).intValue(), Double.valueOf(maxX).intValue())
                        .mapToObj(i -> "-")
                        .forEach(System.out::print);
                    System.out.println("-");
                }
                for (double x = minX; x <= maxX; x += 1.0) {
                    Cube c = pocket.getOrDefault(new PointND(x, y, z), new Cube(false));
                    if (Math.abs(x - 0.0) < 0.0001) {
                        System.out.print("|");
                    }
                    System.out.print(c.isActive() ? "#" : ".");
                }
                System.out.println("");
            }
            System.out.println("");
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

        @Override
        public String toString() {
            return "active=" + active;
        }
    }
}
