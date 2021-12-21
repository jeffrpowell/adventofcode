package com.jeffrpowell.adventofcode.aoc2021;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day21 extends Solution2021<Rule> {
    int die = 0;
    int dieRollCount = 0;

    @Override
    public int getDay() {
        return 21;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("Player (\\d) starting position: (\\d)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        long p1 = input.get(0).getLong(1) - 1;
        long p2 = input.get(1).getLong(1) - 1;
        long p1Score = 0;
        long p2Score = 0;
        boolean p1Turn = true;
        while(p1Score < 1000 && p2Score < 1000) {
            long roll = rollDie();
            roll += rollDie();
            roll += rollDie();
            if (p1Turn) {
                p1 = (p1 + roll) % 10;
                p1Score += p1 + 1;
            }
            else {
                p2 = (p2 + roll) % 10;
                p2Score += p2 + 1;
            }
            p1Turn = !p1Turn;
        }
        return Long.toString(Math.min(p1Score, p2Score) * dieRollCount);
    }

    private int rollDie() {
        die = Math.max((die + 1) % 101, 1);
        dieRollCount++;
        return die;
    }
    
    @Override
    protected String part2(List<Rule> input) {
        int p1 = input.get(0).getInt(1) - 1;
        int p2 = input.get(1).getInt(1) - 1;
        Deque<Universe> q = new ArrayDeque<>();
        q.push(new Universe(p1, p2, 0, 0, true, 1));
        long p1Wins = 0;
        long p2Wins = 0;
        while(!q.isEmpty()) {
            Universe u = q.pop();
            List<Universe> next = u.nextUniverses();
            for (Universe universe : next) {
                if (universe.p1Score >= 21) {
                    p1Wins += universe.knownCopies;
                }
                else if (universe.p2Score >= 21) {
                    p2Wins += universe.knownCopies;
                }
                else {
                    q.push(universe);
                }
            }
        }
        return Long.toString(Math.max(p1Wins, p2Wins));
    }

    private static class Universe {
        private static final Map<Integer, List<MoveCache>> MOVES;

        static {
            MOVES = new HashMap<>();
            IntFunction<List<MoveCache>> moveGen = location -> 
                IntStream.range(0, 10)
                    .mapToObj(newLocation -> new MoveCache(location, newLocation))
                    .filter(mc -> mc.copies > 0)
                    .collect(Collectors.toList());
            for (int i = 0; i < 10; i++) {
                MOVES.put(i, moveGen.apply(i));
            }
        }

        int p1;
        int p2;
        int p1Score;
        int p2Score;
        boolean p1Turn;
        long knownCopies;

        public Universe(int p1, int p2, int p1Score, int p2Score, boolean p1Turn, long knownCopies) {
            this.p1 = p1;
            this.p2 = p2;
            this.p1Score = p1Score;
            this.p2Score = p2Score;
            this.p1Turn = p1Turn;
            this.knownCopies = knownCopies;
        }
        
        public List<Universe> nextUniverses() {
            boolean nextTurn = !p1Turn;
            List<Universe> universes = new ArrayList<>();
            if (p1Turn) {
                for (MoveCache newSpot : MOVES.get(p1)) {
                    universes.add(new Universe(newSpot.location, p2, p1Score + newSpot.location + 1, p2Score, nextTurn, knownCopies * newSpot.copies));
                }
            }
            else {
                for (MoveCache newSpot : MOVES.get(p2)) {
                    universes.add(new Universe(p1, newSpot.location, p1Score, p2Score + newSpot.location + 1, nextTurn, knownCopies * newSpot.copies));
                }
            }
            return universes;
        }

        private static class MoveCache {
            private static final long[] BELL_CURVE = new long[]{0, 0, 0, 1, 3, 6, 7, 6, 3, 1, 0};
            int location;
            long copies;

            public MoveCache(int oldLocation, int newLocation) {
                this.location = newLocation;
                int distance = findDistance(oldLocation, newLocation);
                this.copies = BELL_CURVE[distance];
            }

            private int findDistance(int oldLocation, int newLocation) {
                if (newLocation >= oldLocation) {
                    return newLocation - oldLocation;
                }
                else {
                    return 10 - oldLocation + newLocation;
                }
            }
        }
    }
    
}
