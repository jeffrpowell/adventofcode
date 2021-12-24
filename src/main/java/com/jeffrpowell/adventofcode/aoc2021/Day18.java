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
        return Long.toString(numbers.stream().reduce((acc, next) -> {
            Pair p = new Pair(acc, next);
            acc.setParent(p);
            next.setParent(p);
            p.reduce();
            return p;
        }).get().getMagnitude());
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
        private Pair parent;
        private long value;
        public Num(long value) {
            this.value = value;
        }
        @Override
        public long getMagnitude() {
            return value;
        }

        @Override
        public boolean acceptVisitor(Visitor v, int depth) {
            if (v.visitNum(this)) {
                return true;
            }
            return false;
        }

        @Override
        public void setParent(Pair p) {
            this.parent = p;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (int) (value ^ (value >>> 32));
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
        private Pair parent;
        private Member left;
        private Member right;
        
        public Pair(Pair parent, Member left, Member right) {
            this.parent = parent;
            this.left = left;
            this.right = right;
        }
        
        public Pair(Member left, Member right) {
            this.parent = null;
            this.left = left;
            this.right = right;
        }

        @Override
        public long getMagnitude() {
            return 3 * left.getMagnitude() + 2 * right.getMagnitude();
        }
        
        public void reduce() {
            handleExplosions();
            handleSplit(new SplitChecker());
        }
        
        private void handleExplosions() {
            ExplodeChecker explodeChecker = new ExplodeChecker();
            boolean tryAgain = acceptVisitor(explodeChecker, 0);
            while (tryAgain) {
                tryAgain = acceptVisitor(explodeChecker, 0);
            }
        }
        
        private void handleSplit(SplitChecker splitChecker) {
            boolean splitted = acceptVisitor(splitChecker, 0);
            if (splitted) {
                handleExplosions();
                handleSplit(splitChecker);
            }
        }

        @Override
        public boolean acceptVisitor(Visitor v, int depth) {
            if (left.acceptVisitor(v, depth + 1)) {
                return true;
            }
            if (v.visitPair(this, depth)){
                return true;
            }
            if (right.acceptVisitor(v, depth + 1)) {
                return true;
            }
            return false;
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
                Pair p = new Pair(left, right);
                left.setParent(p);
                right.setParent(p);
                return p;
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

        public Pair getParent() {
            return parent;
        }

        @Override
        public void setParent(Pair p) {
            this.parent = p;
        }

        public Member getLeft() {
            return left;
        }

        public void setLeft(Member left) {
            this.left = left;
        }

        public Member getRight() {
            return right;
        }

        public void setRight(Member right) {
            this.right = right;
        }
    }
    
    static interface Member {
        public long getMagnitude();
        public boolean acceptVisitor(Visitor v, int depth); //true => early return
        public void setParent(Pair p);
    }

    static interface Visitor {
        public boolean visitPair(Pair p, int depth);
        public boolean visitNum(Num n);
    }

    static class ExplodeChecker implements Visitor {

        @Override
        public boolean visitPair(Pair p, int depth) {
            if (depth > 3) {
                long left = p.getLeft().getMagnitude();
                long right = p.getRight().getMagnitude();
                Pair parent = p.getParent();
                Pair currentPair = p;
                while (parent != null) {
                    if (parent.getRight() == currentPair) {
                        currentPair = currentPair.getParent();
                        parent = currentPair.getParent();
                    }
                    else {
                        Member currentRight = currentPair.right;
                        while(currentRight instanceof Pair) {
                            currentRight = ((Pair) currentRight).right;
                        }
                        ((Num)currentRight).value += left; 
                    }
                }
                parent = p.getParent();
                currentPair = p;
                while (parent != null) {
                    if (parent.getLeft() == currentPair) {
                        currentPair = currentPair.getParent();
                        parent = currentPair.getParent();
                    }
                    else {
                        Member currentLeft = currentPair.left;
                        while(currentLeft instanceof Pair) {
                            currentLeft = ((Pair) currentLeft).left;
                        }
                        ((Num)currentLeft).value += right; 
                    }
                }
                if (p.getParent().right == p) {
                    p.getParent().setRight(new Num(0));
                }
                else {
                    p.getParent().setLeft(new Num(0));
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean visitNum(Num n) {
            return false; //noop for exploding
        }
    }

    static class SplitChecker implements Visitor {
        public boolean unhappy;

        public SplitChecker() {
            this.unhappy = true;
        }

        @Override
        public boolean visitPair(Pair p, int depth) {
            return false; //noop for splitting
        }

        @Override
        public boolean visitNum(Num n) {
            long val = n.getMagnitude();
            boolean roundUp = (val / 2) * 2 != val;
            if (val > 9) {
                if (n.parent.right == n) {
                    n.parent.setRight(new Pair(new Num(val / 2), new Num(val / 2 + (roundUp ? 1 : 0))));
                }
                else {
                    n.parent.setLeft(new Pair(new Num(val / 2), new Num(val / 2 + (roundUp ? 1 : 0))));
                }
                unhappy = true;
                return true;
            }
            return false;
        }
    }
}
