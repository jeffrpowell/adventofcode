package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Day8 extends Solution2020<Rule>{
    private int accumulator;
    private boolean inAFlippedState;
    private int branchPt;
    
    @Override
    public int getDay() {
        return 8;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("(acc|jmp|nop) ((?:\\+|-)\\d+)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        Set<Integer> visited = new HashSet<>();
        int nextPointer = 0;
        accumulator = 0;
        while (!visited.contains(nextPointer)) {
            visited.add(nextPointer);
            nextPointer = parseAndMovePointer(input.get(nextPointer));
        }
        return Integer.toString(accumulator);
    }
    
    private int parseAndMovePointer(Rule r) {
        if (r.getString(0).equals("jmp")) {
            return r.getSortKey() + r.getInt(1);
        }
        else {
            if (r.getString(0).equals("acc")) {
                accumulator += r.getInt(1);
            }
            return r.getSortKey() + 1;
        }
    }

    @Override
    protected String part2(List<Rule> input) {
        Deque<StackFrame> stack = new ArrayDeque<>();
        Set<Integer> visited = new HashSet<>();
        int nextPointer = 0;
        stack.push(new StackFrame(null, 0));
        while (nextPointer < input.size()) {
            if (visited.contains(nextPointer) && !inAFlippedState) { //looped! Gotta backtrack...
                while (stack.peek().getOp().equals("acc")) {
                    StackFrame frame = stack.pop();
                    visited.remove(frame.r.getSortKey());
                }
                StackFrame frame = stack.peek();
                frame.flipIt();
                inAFlippedState = true;
                branchPt = frame.r.getSortKey();
                frame.accumulatorAfter = frame.accumulatorBefore;
                nextPointer = parseAndMovePointer(frame);
            }
            else if (visited.contains(nextPointer)) {
                boolean seenBranchPt = false;
                while (stack.peek().getOp().equals("acc") || !seenBranchPt) {
                    StackFrame frame = stack.pop();
                    visited.remove(frame.r.getSortKey());
                    if (frame.r.getSortKey() == branchPt) {
                        seenBranchPt = true;
                    }
                }
                StackFrame frame = stack.peek();
                frame.flipIt();
                inAFlippedState = true;
                branchPt = frame.r.getSortKey();
                frame.accumulatorAfter = frame.accumulatorBefore;
                nextPointer = parseAndMovePointer(frame);
            }
            else {
                visited.add(nextPointer);
                StackFrame frame = new StackFrame(input.get(nextPointer), stack.peek().accumulatorAfter);
                stack.push(frame);
                nextPointer = parseAndMovePointer(frame);
            }
        }
        return Integer.toString(stack.peek().accumulatorAfter);
    }
    
    private int parseAndMovePointer(StackFrame frame) {
        if (frame.getOp().equals("jmp")) {
            return frame.r.getSortKey() + frame.r.getInt(1);
        }
        else {
            if (frame.getOp().equals("acc")) {
                frame.accumulatorAfter = frame.accumulatorBefore + frame.r.getInt(1);
            }
            return frame.r.getSortKey() + 1;
        }
    }

    private static class StackFrame {
        Rule r;
        int accumulatorBefore;
        int accumulatorAfter;
        boolean flipped;

        public StackFrame(Rule r, int accumulator) {
            this.r = r;
            this.accumulatorBefore = accumulator;
            this.accumulatorAfter = accumulator;
        }
        
        public void flipIt() {
            flipped = true;
        }
        
        public String getOp() {
            String op = r.getString(0);
            if (!flipped || op.equals("acc")) {
                return op;
            }
            else if (op.equals("jmp")) {
                return "nop";
            }
            else {
                return "jmp";
            }
        }
        
        @Override
        public String toString() {
            return getOp() + r.getInt(1);
        }
    }
}
