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
        return null;
    }

    @Override
    protected String part2(List<String> input) {
        // TODO Auto-generated method stub
        return null;
    }

    private Pair parseInput(String line) {
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
                    builderInContext = stack.pop();
                    builderInContext.pushNumber(p);
                }
            }
        }
        return null;
    }

    private static class Num implements Member {
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
    }

    private static class Pair implements Member {
        private int depth;
        private Member left;
        private Member right;
        
        public Pair(int depth, Member left, Member right) {
            this.depth = depth;
            this.left = left;
            this.right = right;
        }

        @Override
        public long getMagnitude() {
            return 3 * left.getMagnitude() + 2 * right.getMagnitude();
        }
        
        @Override
        public int getDepth() {
            return depth;
        }

        public static class Builder {
            private Member left;
            private Member right;
            private boolean pushLeft;
            private int depth;

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
    }
    
    private static interface Member {
        public long getMagnitude();
        public int getDepth();
    }
}
