package com.jeffrpowell.adventofcode.aoc2021;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.CharArrayUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day18 extends Solution2021<String> {

    @Override
    public int getDay() {
        return 18;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }

    @Override
    protected String part1(List<String> input) {
        List<Pair> numbers = input.stream().map(this::parseInput).collect(Collectors.toList());
        return Long.toString(numbers.stream().reduce((acc, next) -> new Pair(acc, next)).get().getMagnitude());
    }

    @Override
    protected String part2(List<String> input) {
        // TODO Auto-generated method stub
        return null;
    }

    Pair parseInput(String line) {
        Deque<Pair.Builder> stack = new ArrayDeque<>();
        Pair.Builder builderInContext = null;
        int depth = -1;
        for (Character c : CharArrayUtils.toList(line.toCharArray())) {
            if (c == '[') {
                depth++;
                if (depth > 0) {
                    stack.push(builderInContext);
                }
                builderInContext = new Pair.Builder();
                builderInContext.depth(depth);
            }
            else if (c >= '0' && c <= '9') {
                builderInContext.pushNumber(new Num(depth, Integer.parseInt(String.valueOf(c))));
            }
            else if (c == ']') {
                Pair p = builderInContext.build();
                if (stack.isEmpty()) {
                    return p;
                }
                else {
                    depth--;
                    builderInContext = stack.pop();
                    builderInContext.pushNumber(p);
                }
            }
        }
        return null;
    }

    static class Num implements Member {
        private int depth;
        private int value;
        public Num(int depth, int value) {
            this.depth = depth;
            this.value = value;
        }
        @Override
        public long getMagnitude() {
            return value;
        }

        @Override
        public int getDepth() {
            return depth;
        }

        @Override
        public void acceptVisitor(Visitor v) {
            v.visitNum(this);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + depth;
            result = prime * result + value;
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
            Num other = (Num) obj;
            if (depth != other.depth)
                return false;
            if (value != other.value)
                return false;
            return true;
        }
        
    }

    static class Pair implements Member {
        private int depth;
        private Member left;
        private Member right;
        
        public Pair(int depth, Member left, Member right) {
            this.depth = depth;
            this.left = left;
            this.right = right;
        }

        /**
         * Addition constructor; automatically reduces
         * @param left
         * @param right
         */
        public Pair(Member left, Member right) {
            this.left = left;
            this.right = right;
            this.depth = 0;
            updateDepthsAndReduce();
        }

        @Override
        public long getMagnitude() {
            return 3 * left.getMagnitude() + 2 * right.getMagnitude();
        }
        
        @Override
        public int getDepth() {
            return depth;
        }

        private void updateDepthsAndReduce() {

        }

        @Override
        public void acceptVisitor(Visitor v) {
            v.visitPair(this);
            left.acceptVisitor(v);
            right.acceptVisitor(v);
        }

        public static class Builder {
            private Member left;
            private Member right;
            private boolean pushLeft;
            private int depth;

            public Builder() {
                this.pushLeft = true;
            }

            public void pushNumber(Member member) {
                if (pushLeft) {
                    this.left = member;
                    pushLeft = false;
                }
                else {
                    this.right = member;
                }
            }

            public void depth(int depth) {
                this.depth = depth;
            }

            public Pair build() {
                return new Pair(depth, left, right);
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + depth;
            result = prime * result + ((left == null) ? 0 : left.hashCode());
            result = prime * result + ((right == null) ? 0 : right.hashCode());
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
            Pair other = (Pair) obj;
            if (depth != other.depth)
                return false;
            if (left == null) {
                if (other.left != null)
                    return false;
            } else if (!left.equals(other.left))
                return false;
            if (right == null) {
                if (other.right != null)
                    return false;
            } else if (!right.equals(other.right))
                return false;
            return true;
        }
    }
    
    static interface Member {
        public long getMagnitude();
        public int getDepth();
        public void acceptVisitor(Visitor v);
    }

    static interface Visitor {
        public void visitPair(Pair p);
        public void visitNum(Num n);
    }

    static class ExplodeChecker implements Visitor {

        @Override
        public void visitPair(Pair p) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void visitNum(Num n) {
            // TODO Auto-generated method stub
            
        }

    }

    static class SplitChecker implements Visitor {

        @Override
        public void visitPair(Pair p) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void visitNum(Num n) {
            // TODO Auto-generated method stub
            
        }

    }
}
