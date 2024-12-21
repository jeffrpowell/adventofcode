package com.jeffrpowell.adventofcode.aoc2024;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.Direction;
import static com.jeffrpowell.adventofcode.Direction.UP;
import static com.jeffrpowell.adventofcode.Direction.DOWN;
import static com.jeffrpowell.adventofcode.Direction.LEFT;
import static com.jeffrpowell.adventofcode.Direction.RIGHT;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day21 extends Solution2024<List<String>>{
    private static final Direction A_DIRECTION = Direction.UP_RIGHT;
    private static final int A_INT = 10;
    private static final Memory M = new Memory();

    @Override
    public int getDay() {
        return 21;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    private static class Memory{
        Map<Integer, Point2D> keypadLocations;
        Map<Point2D, Integer> keypadKeys;
        Map<Direction, Point2D> dpadLocations;
        Map<Point2D, Direction> dpadKeys;
        Map<KeyMove, List<Direction>> keypadCache;
        Map<DMove, List<Direction>> dpadCache;
        public Memory() {
            keypadLocations = new HashMap<>();
            keypadLocations.put(7, new Point2D.Double(0, 0));
            keypadLocations.put(8, new Point2D.Double(1, 0));
            keypadLocations.put(9, new Point2D.Double(2, 0));
            keypadLocations.put(4, new Point2D.Double(0, 1));
            keypadLocations.put(5, new Point2D.Double(1, 1));
            keypadLocations.put(6, new Point2D.Double(2, 1));
            keypadLocations.put(1, new Point2D.Double(0, 2));
            keypadLocations.put(2, new Point2D.Double(1, 2));
            keypadLocations.put(3, new Point2D.Double(2, 2));
            // keypadLocations.put(GAP_INT, new Point2D.Double(0, 3)); //gap
            keypadLocations.put(0, new Point2D.Double(1, 3));
            keypadLocations.put(A_INT, new Point2D.Double(2, 3)); //A
            keypadKeys = keypadLocations.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getValue,
                Map.Entry::getKey
            ));
            
            dpadLocations = new HashMap<>();
            // dpadLocations.put(GAP_DIRECTION, new Point2D.Double(0, 0)); //gap
            dpadLocations.put(UP, new Point2D.Double(1, 0));
            dpadLocations.put(A_DIRECTION, new Point2D.Double(2, 0)); //A
            dpadLocations.put(LEFT, new Point2D.Double(0, 1));
            dpadLocations.put(DOWN, new Point2D.Double(1, 1));
            dpadLocations.put(RIGHT, new Point2D.Double(2, 1));
            dpadKeys = dpadLocations.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getValue,
                Map.Entry::getKey
            ));

            keypadCache = new HashMap<>();
            dpadCache = new HashMap<>();
            //some hard-coded paths to work around hard-to-explain issues
            keypadCache.put(new KeyMove(A_INT, 3), List.of(UP, A_DIRECTION));
            keypadCache.put(new KeyMove(3, 4), List.of(LEFT, LEFT, UP, A_DIRECTION));
            keypadCache.put(new KeyMove(4, 0), List.of(RIGHT, DOWN, DOWN, A_DIRECTION));
            keypadCache.put(new KeyMove(0, A_INT), List.of(RIGHT, A_DIRECTION));
            keypadCache.put(new KeyMove(A_INT, 1), List.of(UP, LEFT, LEFT, A_DIRECTION));
            keypadCache.put(new KeyMove(1, 4), List.of(UP, A_DIRECTION));
            keypadCache.put(new KeyMove(4, 9), List.of(RIGHT, RIGHT, UP, A_DIRECTION));
            keypadCache.put(new KeyMove(9, A_INT), List.of(DOWN, DOWN, DOWN, A_DIRECTION));
            keypadCache.put(new KeyMove(A_INT, 5), List.of(UP, UP, LEFT, A_DIRECTION));
            keypadCache.put(new KeyMove(5, 8), List.of(UP, A_DIRECTION));
            keypadCache.put(new KeyMove(8, 2), List.of(DOWN, DOWN, A_DIRECTION));
            keypadCache.put(new KeyMove(2, A_INT), List.of(RIGHT, DOWN, A_DIRECTION));
            keypadCache.put(new KeyMove(A_INT, 7), List.of(UP, UP, UP, LEFT, LEFT, A_DIRECTION));
            keypadCache.put(new KeyMove(7, 8), List.of(RIGHT, A_DIRECTION));
            keypadCache.put(new KeyMove(8, 0), List.of(DOWN, DOWN, DOWN, A_DIRECTION));
            keypadCache.put(new KeyMove(A_INT, 4), List.of(UP, UP, LEFT, LEFT, A_DIRECTION));
            keypadCache.put(new KeyMove(4, 6), List.of(RIGHT, RIGHT, A_DIRECTION));
            keypadCache.put(new KeyMove(6, 3), List.of(DOWN, A_DIRECTION));
            keypadCache.put(new KeyMove(3, 7), List.of(LEFT, LEFT, UP, UP, A_DIRECTION));
        }

        record KeyMove(int start, int end){}
        record DMove(Direction start, Direction end){}
        record TraceState(TraceState prior, Point2D pt, int distance, Point2D end, double boost) implements Comparable<TraceState>{
            private Double heuristic() {
                return boost + distance + Point2DUtils.getManhattenDistance(pt, end);
            }

            @Override
            public int compareTo(TraceState o) {
                return heuristic().compareTo(o.heuristic());
            }

            public TraceState generateNext(Point2D next) {
                if (prior() == null) {
                    return new TraceState(this, next, distance + 1, end, 0);
                }
                double boost = 0.0;
                Direction lastDirectionPressed = Direction.getDirectionThisLineTravels(prior().pt(), pt).get();
                Direction nextDirection = Direction.getDirectionThisLineTravels(pt, next).get();
                if (lastDirectionPressed == nextDirection) {
                    boost -= 0.25;
                }
                return new TraceState(this, next, distance + 1, end, boost);
            }

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((pt == null) ? 0 : pt.hashCode());
                result = prime * result + distance;
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (getClass() != obj.getClass())
                    return false;
                TraceState other = (TraceState) obj;
                if (pt == null) {
                    if (other.pt != null)
                        return false;
                } else if (!pt.equals(other.pt))
                    return false;
                if (distance != other.distance)
                    return false;
                return true;
            }
        }

        public List<Direction> moveKeypad(int start, int finish) {
            KeyMove move = new KeyMove(start, finish);
            return keypadCache.computeIfAbsent(move, this::moveKeypad);
        }

        private List<Direction> moveKeypad(KeyMove move) {
            PriorityQueue<TraceState> q = new PriorityQueue<>();
            Point2D end = keypadLocations.get(move.end());
            TraceState start = new TraceState(null, keypadLocations.get(move.start()), 0, end, 0);
            q.add(start);
            TraceState current = start;
            Set<Point2D> visited = new HashSet<>();
            while(!q.peek().pt().equals(end)){
                current = q.poll();
                visited.add(current.pt());
                TraceState _current = current;
                Point2DUtils.getAdjacentPts(_current.pt(), false).stream()
                    .filter(p -> keypadKeys.containsKey(p))
                    .filter(p -> !visited.contains(p))
                    .map(p -> _current.generateNext(p))
                    .forEach(q::add);
            }
            current = q.poll();
            List<Direction> directions = new ArrayList<>();
            while(current.prior() != null) {
                TraceState priorState = current.prior();
                Direction d = Direction.getDirectionThisLineTravels(priorState.pt(), current.pt()).get();
                directions.add(0, d);
                current = priorState;
            }
            directions.add(A_DIRECTION);
            return directions;
        }

        public List<Direction> moveDpad(Direction start, Direction finish) {
            DMove move = new DMove(start, finish);
            return dpadCache.computeIfAbsent(move, this::moveDpad);
        }

        private List<Direction> moveDpad(DMove move) {
            PriorityQueue<TraceState> q = new PriorityQueue<>();
            Point2D end = dpadLocations.get(move.end());
            TraceState start = new TraceState(null, dpadLocations.get(move.start()), 0, end, 0);
            q.add(start);
            TraceState current = start;
            Set<Point2D> visited = new HashSet<>();
            while(!q.peek().pt().equals(end)){
                current = q.poll();
                visited.add(current.pt());
                TraceState _current = current;
                Point2DUtils.getAdjacentPts(_current.pt(), false).stream()
                    .filter(p -> dpadKeys.containsKey(p))
                    .filter(p -> !visited.contains(p))
                    .map(p -> _current.generateNext(p))
                    .forEach(q::add);
            }
            current = q.poll();
            List<Direction> directions = new ArrayList<>();
            while(current.prior() != null) {
                TraceState priorState = current.prior();
                Direction d = Direction.getDirectionThisLineTravels(priorState.pt(), current.pt()).get();
                directions.add(0, d);
                current = priorState;
            }
            directions.add(A_DIRECTION);
            return directions;
        }
    }

    @Override
    protected String part1(List<List<String>> input) {
        return Long.toString(input.stream()
            .map(this::parseCode)
            .map(this::scoreMyDPadDirections)
            .reduce(0L, Math::addExact));
        //163246 too high
        //161886 too high
    }

    private List<Integer> parseCode(List<String> codeStr) {
        System.out.println(codeStr.stream().collect(Collectors.joining()));
        return codeStr.stream()
            .map(c -> {
                try {
                    return Integer.parseInt(c);
                }
                catch (NumberFormatException e) {
                    return A_INT;
                }
            })
            .collect(Collectors.toList());
    }

    private long scoreMyDPadDirections(List<Integer> code) {
        List<Direction> keypadDirections = new ArrayList<>();
        keypadDirections.add(A_DIRECTION);
        keypadDirections.addAll(M.moveKeypad(A_INT, code.get(0)));
        keypadDirections.addAll(M.moveKeypad(code.get(0), code.get(1)));
        keypadDirections.addAll(M.moveKeypad(code.get(1), code.get(2)));
        keypadDirections.addAll(M.moveKeypad(code.get(2), code.get(3)));
        printDirections(keypadDirections);
        List<Direction> dpad1Directions = new ArrayList<>();
        dpad1Directions.add(A_DIRECTION);
        for (int i = 0; i < keypadDirections.size() - 1; i++) {
            dpad1Directions.addAll(M.moveDpad(keypadDirections.get(i), keypadDirections.get(i+1)));
        }
        printDirections(dpad1Directions);
        List<Direction> dpad2Directions = new ArrayList<>();
        for (int i = 0; i < dpad1Directions.size() - 1; i++) {
            dpad2Directions.addAll(M.moveDpad(dpad1Directions.get(i), dpad1Directions.get(i+1)));
        }
        printDirections(dpad2Directions);
        return Long.parseLong(code.stream().map(i -> i.toString()).limit(3).collect(Collectors.joining())) * dpad2Directions.size();
    }

    private void printDirections(List<Direction> directions) {
        System.out.println(directions.stream()
            .map(d -> {
                if (d == A_DIRECTION) {
                    return "A";
                }
                else {
                    return d.toString();
                }
            })
            .collect(Collectors.joining()));
    }

    @Override
    protected String part2(List<List<String>> input) {
        return part1(input);
    }

}
