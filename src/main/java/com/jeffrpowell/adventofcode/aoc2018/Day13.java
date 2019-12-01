package com.jeffrpowell.adventofcode.aoc2018;

import com.jeffrpowell.adventofcode.Solution;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day13 implements Solution<String>
{
    private final Map<Point2D, TrackType> tracks = new HashMap<>();
    private List<Cart> carts = new ArrayList<>();
    
    @Override
    public String part1(List<String> input) {
        setupState(input);
        Point2D collision = null;
        while(Objects.isNull(collision)) {
            collision = tickPart1();
        }
        return collision.getX() + "," + collision.getY();
    }

    @Override
    public String part2(List<String> input) {
        setupState(input);
        while(carts.size() > 1) {
            tickPart2();
        }
        Point2D answer = carts.get(0).getLocation();
        return answer.getX() + "," + answer.getY();
    }
    
    private void setupState(List<String> input) {
        for (int y = 0; y < input.size(); y++) {
            char[] rowChars = input.get(y).toCharArray();
            for (int x = 0; x < rowChars.length; x++) {
                char track = rowChars[x];
                Point2D currentPt = new Point2D.Double(x, y);
                tracks.put(currentPt, TrackType.parse(track, x == 0 ? null : rowChars[x - 1]));
                if (Cart.trackSymbolIsCart(track)) {
                    carts.add(new Cart(track, currentPt));
                }
            }
        }
    }
    
    /**
     * @return If a collision happens, returns point of collision
     */
    private Point2D tickPart1() {
        carts.sort(null);
        for (Cart cart : carts) {
            Point2D nextLocation = cart.travelToNextLocation(tracks.get(cart.getLocation()));
            if (collision(nextLocation)) {
                return nextLocation;
            }
        }
        return null;
    }
    
    private void tickPart2() {
        carts.sort(null);
        Set<Cart> removedCarts = new HashSet<>();
        for (Cart cart : carts) {
            if (removedCarts.contains(cart)) {
                continue;
            }
            Point2D nextLocation = cart.travelToNextLocation(tracks.get(cart.getLocation()));
            if (collision(nextLocation, removedCarts)) {
                carts.stream().filter(c -> c.getLocation().equals(nextLocation)).forEach(removedCarts::add);
            }
        }
        carts = carts.stream().filter(c -> !removedCarts.contains(c)).sorted().collect(Collectors.toList());
    }
    
    private boolean collision(Point2D checkPt) {
        return carts.stream().map(Cart::getLocation).filter(pt -> pt.equals(checkPt)).count() > 1;
    }
    
    private boolean collision(Point2D checkPt, Set<Cart> removedCarts) {
        return carts.stream().filter(c -> !removedCarts.contains(c)).map(Cart::getLocation).filter(pt -> pt.equals(checkPt)).count() > 1;
    }
    
    private static enum Direction {
        LEFT(pt -> new Point2D.Double(pt.getX() - 1, pt.getY())), 
        RIGHT(pt -> new Point2D.Double(pt.getX() + 1, pt.getY())), 
        UP(pt -> new Point2D.Double(pt.getX(), pt.getY() - 1)), 
        DOWN(pt -> new Point2D.Double(pt.getX(), pt.getY() + 1));
        
        private final Function<Point2D, Point2D> travelFn;

        private Direction(Function<Point2D, Point2D> travelFn) {
            this.travelFn = travelFn;
        }
        
        public Point2D travel(Point2D location) {
            return travelFn.apply(location);
        }
        
        public Direction opposite() {
            switch (this) {
                case LEFT:
                    return RIGHT;
                case RIGHT:
                    return LEFT;
                case UP:
                    return DOWN;
                case DOWN:
                    return UP;
            }
            return null;
        }
    }
    
    private static class Cart implements Comparable<Cart>{
        private Direction direction;
        private Point2D location;
        private RelativeDirection nextIntersectionDecision;
        
        public static boolean trackSymbolIsCart(char c) {
            return c == '<' || c == '>' || c == 'v' || c == '^';
        }

        public Cart(char direction, Point2D location) {
            switch (direction) {
                case '<':
                    this.direction = Direction.LEFT;
                    break;
                case '>':
                    this.direction = Direction.RIGHT;
                    break;
                case 'v':
                    this.direction = Direction.DOWN;
                    break;
                case '^':
                    this.direction = Direction.UP;
                    break;
            }
            this.location = location;
            this.nextIntersectionDecision = RelativeDirection.LEFT;
        }
        
        public Point2D travelToNextLocation(TrackType currentTrackType) {
            if (currentTrackType == TrackType.INTERSECTION) {
                direction = nextIntersectionDecision.eval(direction);
                nextIntersectionDecision = RelativeDirection.rotateToNextRelativeDirection(nextIntersectionDecision);
            }
            else {
                direction = currentTrackType.getNextDirection(direction);
            }
            location = direction.travel(location);
            return location;
        }

        public Direction getDirection() {
            return direction;
        }

        public Point2D getLocation() {
            return location;
        }

        @Override
        public int compareTo(Cart o) {
            if (location.equals(o.getLocation())) {
                return 0;
            }
            if (location.getY() < o.getLocation().getY()) {
                return -1;
            }
            else if (location.getY() > o.getLocation().getY()){
                return 1;
            }
            else {
                return location.getX() < o.getLocation().getX() ? -1 : 1;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 43 * hash + Objects.hashCode(this.direction);
            hash = 43 * hash + Objects.hashCode(this.location);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Cart other = (Cart) obj;
            if (this.direction != other.direction)
                return false;
            return Objects.equals(this.location, other.location);
        }
    }
    
    private static enum RelativeDirection {
        LEFT, STRAIGHT, RIGHT;
        
        public Direction eval(Direction goingDirection) {
            if (this == STRAIGHT) {
                return goingDirection;
            }
            switch (goingDirection) {
                case LEFT:
                    switch (this) {
                        case LEFT:
                            return Direction.DOWN;
                        case RIGHT:
                            return Direction.UP;
                    }
                case RIGHT:
                    switch (this) {
                        case LEFT:
                            return Direction.UP;
                        case RIGHT:
                            return Direction.DOWN;
                    }
                case UP:
                    switch (this) {
                        case LEFT:
                            return Direction.LEFT;
                        case RIGHT:
                            return Direction.RIGHT;
                    }
                case DOWN:
                    switch (this) {
                        case LEFT:
                            return Direction.RIGHT;
                        case RIGHT:
                            return Direction.LEFT;
                    }
            }
            return null;
        }
        
        public static RelativeDirection rotateToNextRelativeDirection(RelativeDirection d) {
            switch (d) {
                case LEFT:
                    return STRAIGHT;
                case STRAIGHT:
                    return RIGHT;
                case RIGHT:
                    return LEFT;
            }
            return null;
        }
    }
    
    private static enum TrackType {
        VERTICAL(Direction.UP, Direction.DOWN), 
        HORIZONTAL(Direction.LEFT, Direction.RIGHT), 
        CURVE_NE(Direction.UP, Direction.RIGHT), 
        CURVE_NW(Direction.UP, Direction.LEFT), 
        CURVE_SE(Direction.DOWN, Direction.RIGHT), 
        CURVE_SW(Direction.DOWN, Direction.LEFT),
        INTERSECTION(Direction.values()),
        BLANK();
        
        Direction[] directions;
        
        TrackType(Direction... directions) {
            this.directions = directions;
        }
        
        public static TrackType parse(char c, Character leftNeighbor) {
            switch (c) {
                case '+':
                    return INTERSECTION;
                case '|':
                case '^':
                case 'v':
                    return VERTICAL;
                case '-':
                case '<':
                case '>':
                    return HORIZONTAL;
                case '/':
                    if (leftNeighbor == null || leftNeighbor == ' ' || leftNeighbor == '|' || leftNeighbor == '/' || leftNeighbor == '\\') {
                        return CURVE_SE;
                    }
                    else {
                        return CURVE_NW;
                    }
                case '\\':
                    if (leftNeighbor == null || leftNeighbor == ' ' || leftNeighbor == '|' || leftNeighbor == '/' || leftNeighbor == '\\') {
                        return CURVE_NE;
                    }
                    else {
                        return CURVE_SW;
                    }
                default:
                    return BLANK;
            }
        }
        
        public Direction getNextDirection(Direction goingDirection) {
            if (this == INTERSECTION || this == BLANK) {
                return goingDirection;
            }
            Direction approachingDirection = goingDirection.opposite();
            for (Direction outboundDirection : directions) {
                if (outboundDirection != approachingDirection) {
                    return outboundDirection;
                }
            }
            return goingDirection;
        }
    }
}
