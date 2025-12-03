package com.jeffrpowell.adventofcode.aoc2022;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.algorithms.Direction;
import com.jeffrpowell.adventofcode.algorithms.Grid;
import com.jeffrpowell.adventofcode.algorithms.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day23 extends Solution2022<List<String>>{

    @Override
    public int getDay() {
        return 23;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }
    
    @Override
    protected String part1(List<List<String>> input) {
        Grid<Boolean> grid = new Grid<>(input, (in, pt) -> in.get(d2i(pt.getY())).get(d2i(pt.getX())).equals("#"));
        List<Point2D> elves = grid.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).collect(Collectors.toList());
        List<Direction> directions = Stream.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).collect(Collectors.toList());
        for (int i = 0; i < 10; i++) {
            List<Proposal> proposedMoves = new ArrayList<>();
            for (Point2D elf : elves) {
                if (Point2DUtils.getAdjacentPts(elf, true).stream().map(p -> grid.computeIfAbsent(p, k -> false)).allMatch(Boolean.FALSE::equals)) {
                    proposedMoves.add(new Proposal(elf, elf));
                    continue;
                }
                boolean addNoop = true;
                for (Direction d : directions) {
                    Point2D proposal = Point2DUtils.movePtInDirection(elf, d, 1);
                    Point2D neighborR = Point2DUtils.movePtInDirection(proposal, d.rotateRight90(), 1);
                    Point2D neighborL = Point2DUtils.movePtInDirection(proposal, d.rotateLeft90(), 1);
                    if (!grid.computeIfAbsent(proposal, k -> false) && !grid.computeIfAbsent(neighborR, k -> false) && !grid.computeIfAbsent(neighborL, k -> false)) {
                        proposedMoves.add(new Proposal(proposal, elf));
                        addNoop = false;
                        break;
                    }
                }
                if (addNoop) {
                    proposedMoves.add(new Proposal(elf, elf));
                }
            }
            Set<Point2D> dupeChecker = new HashSet<>();
            Set<Point2D> proposalsToRemove = new HashSet<>();
            for (Proposal proposal : proposedMoves) {
                if (!dupeChecker.add(proposal.pt)){
                    proposalsToRemove.add(proposal.pt);
                }
            }
            elves.stream().forEach(pt -> grid.put(pt, false));
            elves = proposedMoves.stream().map(p -> {
                if (proposalsToRemove.contains(p.pt)) {
                    return p.backup;
                }
                else {
                    return p.pt;
                }
            }).collect(Collectors.toList());
            elves.stream().forEach(pt -> grid.put(pt, true));
            directions.add(directions.remove(0));
        }
        Point2DUtils.BoundingBox box = Point2DUtils.getBoundingBox(grid.entrySet().stream().filter(e -> e.getValue().equals(true)).map(Map.Entry::getKey).collect(Collectors.toList()));
        return Long.toString(Grid.generatePointStream(box.min().getX(), box.min().getY(), box.max().getX() + 1, box.max().getY() + 1)
            .filter(p -> grid.computeIfAbsent(p, k -> false).equals(false))
            .count());
    }

    private record Proposal(Point2D pt, Point2D backup){}

    private static int d2i(Double d) {
        return d.intValue();
    }

    @Override
    protected String part2(List<List<String>> input) {
        Grid<Boolean> grid = new Grid<>(input, (in, pt) -> in.get(d2i(pt.getY())).get(d2i(pt.getX())).equals("#"));
        List<Point2D> elves = grid.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).collect(Collectors.toList());
        List<Direction> directions = Stream.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).collect(Collectors.toList());
        long rounds = 0;
        boolean run = true;
        while(run) {
            List<Proposal> proposedMoves = new ArrayList<>();
            for (Point2D elf : elves) {
                if (Point2DUtils.getAdjacentPts(elf, true).stream().map(p -> grid.computeIfAbsent(p, k -> false)).allMatch(Boolean.FALSE::equals)) {
                    proposedMoves.add(new Proposal(elf, elf));
                    continue;
                }
                boolean addNoop = true;
                for (Direction d : directions) {
                    Point2D proposal = Point2DUtils.movePtInDirection(elf, d, 1);
                    Point2D neighborR = Point2DUtils.movePtInDirection(proposal, d.rotateRight90(), 1);
                    Point2D neighborL = Point2DUtils.movePtInDirection(proposal, d.rotateLeft90(), 1);
                    if (!grid.computeIfAbsent(proposal, k -> false) && !grid.computeIfAbsent(neighborR, k -> false) && !grid.computeIfAbsent(neighborL, k -> false)) {
                        proposedMoves.add(new Proposal(proposal, elf));
                        addNoop = false;
                        break;
                    }
                }
                if (addNoop) {
                    proposedMoves.add(new Proposal(elf, elf));
                }
            }
            Set<Point2D> dupeChecker = new HashSet<>();
            Set<Point2D> proposalsToRemove = new HashSet<>();
            for (Proposal proposal : proposedMoves) {
                if (!dupeChecker.add(proposal.pt)){
                    proposalsToRemove.add(proposal.pt);
                }
            }
            elves.stream().forEach(pt -> grid.put(pt, false));
            List<Point2D> newPts = proposedMoves.stream().map(p -> {
                if (proposalsToRemove.contains(p.pt)) {
                    return p.backup;
                }
                else {
                    return p.pt;
                }
            }).collect(Collectors.toList());
            Set<Point2D> elfDupeCheck = elves.stream().collect(Collectors.toSet());
            Set<Point2D> newDupeCheck = newPts.stream().collect(Collectors.toSet());
            if (elfDupeCheck.equals(newDupeCheck)) {
                run = false;
            }
            elves = newPts;
            elves.stream().forEach(pt -> grid.put(pt, true));
            directions.add(directions.remove(0));
            rounds++;
        }
        return Long.toString(rounds);
    }

}
