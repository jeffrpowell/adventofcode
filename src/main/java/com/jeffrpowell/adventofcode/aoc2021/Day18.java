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
        return Long.toString(numbers.stream().reduce((acc, next) -> new Pair(acc, next, true)).get().getMagnitude());
    }

    @Override
    protected String part2(List<String> input) {
        // TODO Auto-generated method stub
        return null;
    }

    Pair parseInput(String line) {
        Deque<Pair.Builder> stack = new ArrayDeque<>();
        Pair.Builder builderInContext = null;
        for (Character c : CharArrayUtils.toList(line.toCharArray())) {
            if (c == '[') {
                if (builderInContext != null) {
                    stack.push(builderInContext);
                }
                builderInContext = new Pair.Builder();
            }
            else if (c >= '0' && c <= '9') {
                builderInContext.pushNumber(new Num(Integer.parseInt(String.valueOf(c))));
            }
            else if (c == ']') {
                Pair p = builderInContext.build();
                if (stack.isEmpty()) {
                    return p;
                }
                else {
                    builderInContext = stack.pop();
                    builderInContext.pushNumber(p);
                }
            }
        }
        return null;
    }

    static class Num implements Member {
        private int value;
        public Num(int value) {
            this.value = value;
        }
        @Override
        public long getMagnitude() {
            return value;
        }

        @Override
        public void acceptVisitor(Visitor v, int depth) {
            v.visitNum(this);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
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
            if (value != other.value)
                return false;
            return true;
        }
        
    }

    static class Pair implements Member {
        private Member left;
        private Member right;
        
        public Pair(Member left, Member right, boolean reduce) {
            this.left = left;
            this.right = right;
            if (reduce) {
                reduce();
            }
        }

        @Override
        public long getMagnitude() {
            return 3 * left.getMagnitude() + 2 * right.getMagnitude();
        }
        
        private void reduce() {
            ExplodeChecker explodeChecker = new ExplodeChecker();
            while(explodeChecker.unhappy) {
                explodeChecker.reset();
                acceptVisitor(explodeChecker, 0);
            }
        }

        @Override
        public void acceptVisitor(Visitor v, int depth) {
            v.visitPair(this, depth);
            left.acceptVisitor(v, depth + 1);
            right.acceptVisitor(v, depth + 1);
        }

        public static class Builder {
            private Member left;
            private Member right;
            private boolean pushLeft;

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

            public Pair build() {
                return new Pair(left, right, false);
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
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
        public void acceptVisitor(Visitor v, int depth);
    }

    static interface Visitor {
        public void visitPair(Pair p, int depth);
        public void visitNum(Num n);
        public boolean isUnhappy();
        public void reset();
    }

    static class ExplodeChecker implements Visitor {

        public boolean unhappy;

        public ExplodeChecker() {
            this.unhappy = true;
        }

        @Override
        public void visitPair(Pair p, int depth) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void visitNum(Num n) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isUnhappy() {
            return unhappy;
        }

        @Override
        public void reset() {
            // TODO Auto-generated method stub
            
        }

    }

    static class SplitChecker implements Visitor {
        public boolean unhappy;

        public SplitChecker() {
            this.unhappy = true;
        }

        @Override
        public void visitPair(Pair p, int depth) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void visitNum(Num n) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isUnhappy() {
            return unhappy;
        }

        @Override
        public void reset() {
            // TODO Auto-generated method stub
            
        }

    }
}
