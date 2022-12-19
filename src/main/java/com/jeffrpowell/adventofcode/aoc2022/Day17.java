package com.jeffrpowell.adventofcode.aoc2022;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day17 extends Solution2022<List<String>>{
    
    static int leftWall = 0;
    static int rightWall = 8;
    static int floor = 6000;

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
        Tetris piece = new Flat(new Point2D.Double(leftWall + 3, floor - 4));
        double minY = floor;
        int blowi = 0;
        Set<Point2D> settledPts = new HashSet<>();
        for (int i = 0; i < 2022; i++) {
            boolean settled = false;
            while (!settled) {
                String blow = input.get(0).get(blowi);
                blowi = (blowi + 1) % input.get(0).size();
                settled = piece.blow(blow, settledPts);
            }
            Set<Point2D> newPts = piece.getAllPts().collect(Collectors.toSet());
            for (Point2D p : newPts) {
                minY = Math.min(minY, p.getY());
            }
            settledPts.addAll(newPts);
            piece = piece.getNext(minY);
            // Point2DUtils.printPoints(
            //     Stream.concat(
            //         Stream.concat(
            //             piece.getAllPts(), 
            //             settledPts.stream()
            //         ), 
            //         Stream.of(
            //             new Point2D.Double(leftWall, floor), 
            //             new Point2D.Double(rightWall, floor), 
            //             new Point2D.Double(leftWall, minY), 
            //             new Point2D.Double(rightWall, minY)
            //         )
            //     )
            //     .collect(Collectors.toSet())
            // );
        }
        return Double.toString(floor - minY);
    }

    @Override
    protected String part2(List<List<String>> input) {
        return null;
    }

    private abstract static class Tetris {
        protected Point2D bottomLeft;

        public Tetris(Point2D bottomLeft) {
            this.bottomLeft = bottomLeft;
        }

        public abstract Tetris getNext(double minY);
        public abstract Stream<Point2D> getAllPts();
        public abstract Stream<Point2D> getLeftPts();
        public abstract Stream<Point2D> getRightPts();
        public abstract Stream<Point2D> getBottomPts();

        public Point2D getBottomLeft() {
            return bottomLeft;
        }

        protected double nextY(double minY) {
            return Math.min(minY, bottomLeft.getY()) - 4;
        }

        /**
         * 
         * @param blow
         * @return true if the piece should be considered settled
         */
        public boolean blow(String blow, Set<Point2D> settledPts) {
            horizontalBlow(blow, settledPts);
            return drop(settledPts);
        }

        private void horizontalBlow(String blow, Set<Point2D> settledPts) {
            if (blow.equals("<") 
                && bottomLeft.getX() > leftWall + 1
                && getLeftPts()
                    .map(p -> Point2DUtils.movePtInDirection(p, Direction.LEFT, 1))
                    .noneMatch(settledPts::contains)
            ) {
                bottomLeft = Point2DUtils.movePtInDirection(bottomLeft, Direction.LEFT, 1);
            }
            else if (blow.equals(">") 
                && getRightPts()
                    .map(p -> Point2DUtils.movePtInDirection(p, Direction.RIGHT, 1))
                    .noneMatch(p -> p.getX() == rightWall || settledPts.contains(p))
            ) {
                bottomLeft = Point2DUtils.movePtInDirection(bottomLeft, Direction.RIGHT, 1);
            }
        }
        
        private boolean drop(Set<Point2D> settledPts) {
            if (bottomLeft.getY() == floor - 1
                || getBottomPts()
                .map(p -> Point2DUtils.movePtInDirection(p, Direction.DOWN, 1))
                .anyMatch(settledPts::contains)
            ) {
                return true;
            }
            bottomLeft = Point2DUtils.movePtInDirection(bottomLeft, Direction.DOWN, 1);
            return false;
        }
    }

    private static class Flat extends Tetris {

        public Flat(Point2D bottomLeft) {
            super(bottomLeft);
        }

        @Override
        public Tetris getNext(double minY) {
            return new Cross(new Point2D.Double(leftWall + 3, nextY(minY)));
        }

        @Override
        public Stream<Point2D> getAllPts() {
            return Stream.of(
                bottomLeft, 
                Point2DUtils.movePtInDirection(bottomLeft, Direction.RIGHT, 1),
                Point2DUtils.movePtInDirection(bottomLeft, Direction.RIGHT, 2),
                Point2DUtils.movePtInDirection(bottomLeft, Direction.RIGHT, 3)
            );
        }

        @Override
        public Stream<Point2D> getLeftPts() {
            return Stream.of(bottomLeft);
        }

        @Override
        public Stream<Point2D> getRightPts() {
            return Stream.of(Point2DUtils.movePtInDirection(bottomLeft, Direction.RIGHT, 3));
        }

        @Override
        public Stream<Point2D> getBottomPts() {
            return getAllPts();
        }

    }

    private static class Cross extends Tetris {

        public Cross(Point2D bottomLeft) {
            super(bottomLeft);
        }

        @Override
        public Tetris getNext(double minY) {
            return new Corner(new Point2D.Double(leftWall + 3, nextY(minY)));
        }

        @Override
        public Stream<Point2D> getAllPts() {
            return Stream.of(
                Point2DUtils.movePtInDirection(bottomLeft, Direction.RIGHT, 1),
                Point2DUtils.movePtInDirection(bottomLeft, Direction.UP, 1),
                Point2DUtils.movePtInDirection(bottomLeft, Direction.UP_RIGHT, 1),
                Point2DUtils.applyVectorToPt(new Point2D.Double(2, -1), bottomLeft),
                Point2DUtils.applyVectorToPt(new Point2D.Double(1, -2), bottomLeft)
            );
        }

        @Override
        public Stream<Point2D> getLeftPts() {
            return Stream.of(Point2DUtils.movePtInDirection(bottomLeft, Direction.UP, 1));
        }

        @Override
        public Stream<Point2D> getRightPts() {
            return Stream.of(Point2DUtils.applyVectorToPt(new Point2D.Double(2, -1), bottomLeft));
        }

        @Override
        public Stream<Point2D> getBottomPts() {
            return Stream.of(Point2DUtils.movePtInDirection(bottomLeft, Direction.RIGHT, 1));
        }

    }

    private static class Corner extends Tetris {

        public Corner(Point2D bottomLeft) {
            super(bottomLeft);
        }

        @Override
        public Tetris getNext(double minY) {
            return new Tall(new Point2D.Double(leftWall + 3, nextY(minY)));
        }

        @Override
        public Stream<Point2D> getAllPts() {
            return Stream.of(
                bottomLeft, 
                Point2DUtils.movePtInDirection(bottomLeft, Direction.RIGHT, 1),
                Point2DUtils.movePtInDirection(bottomLeft, Direction.RIGHT, 2),
                Point2DUtils.movePtInDirection(bottomLeft, Direction.UP_RIGHT, 2),
                Point2DUtils.applyVectorToPt(new Point2D.Double(2, -1), bottomLeft)
            );
        }

        @Override
        public Stream<Point2D> getLeftPts() {
            return Stream.of(bottomLeft);
        }

        @Override
        public Stream<Point2D> getRightPts() {
            return Stream.of(
                Point2DUtils.movePtInDirection(bottomLeft, Direction.RIGHT, 2),
                Point2DUtils.movePtInDirection(bottomLeft, Direction.UP_RIGHT, 2),
                Point2DUtils.applyVectorToPt(new Point2D.Double(2, -1), bottomLeft)
            );
        }

        @Override
        public Stream<Point2D> getBottomPts() {
            return Stream.of(
                bottomLeft, 
                Point2DUtils.movePtInDirection(bottomLeft, Direction.RIGHT, 1),
                Point2DUtils.movePtInDirection(bottomLeft, Direction.RIGHT, 2)
            );
        }

    }

    private static class Tall extends Tetris {

        public Tall(Point2D bottomLeft) {
            super(bottomLeft);
        }

        @Override
        public Tetris getNext(double minY) {
            return new Square(new Point2D.Double(leftWall + 3, nextY(minY)));
        }

        @Override
        public Stream<Point2D> getAllPts() {
            return Stream.of(
                bottomLeft, 
                Point2DUtils.movePtInDirection(bottomLeft, Direction.UP, 1),
                Point2DUtils.movePtInDirection(bottomLeft, Direction.UP, 2),
                Point2DUtils.movePtInDirection(bottomLeft, Direction.UP, 3)
            );
        }

        @Override
        public Stream<Point2D> getLeftPts() {
            return getAllPts();
        }

        @Override
        public Stream<Point2D> getRightPts() {
            return getAllPts();
        }

        @Override
        public Stream<Point2D> getBottomPts() {
            return Stream.of(bottomLeft);
        }

    }

    private static class Square extends Tetris {

        public Square(Point2D bottomLeft) {
            super(bottomLeft);
        }

        @Override
        public Tetris getNext(double minY) {
            return new Flat(new Point2D.Double(leftWall + 3, nextY(minY)));
        }

        @Override
        public Stream<Point2D> getAllPts() {
            return Stream.of(
                bottomLeft, 
                Point2DUtils.movePtInDirection(bottomLeft, Direction.UP, 1),
                Point2DUtils.movePtInDirection(bottomLeft, Direction.UP_RIGHT, 1),
                Point2DUtils.movePtInDirection(bottomLeft, Direction.RIGHT, 1)
            );
        }

        @Override
        public Stream<Point2D> getLeftPts() {
            return Stream.of(
                bottomLeft, 
                Point2DUtils.movePtInDirection(bottomLeft, Direction.UP, 1)
            );
        }

        @Override
        public Stream<Point2D> getRightPts() {
            return Stream.of(
                Point2DUtils.movePtInDirection(bottomLeft, Direction.UP_RIGHT, 1),
                Point2DUtils.movePtInDirection(bottomLeft, Direction.RIGHT, 1)
            );
        }

        @Override
        public Stream<Point2D> getBottomPts() {
            return Stream.of(
                bottomLeft, 
                Point2DUtils.movePtInDirection(bottomLeft, Direction.RIGHT, 1)
            );
        }

    }

    /*
     * ####

        .#.
        ###
        .#.

        ..#
        ..#
        ###

        #
        #
        #
        #

        ##
        ##
     */
}
