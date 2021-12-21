package com.jeffrpowell.adventofcode.aoc2021;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

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
            long roll = rollDie(100);
            roll += rollDie(100);
            roll += rollDie(100);
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

    private int rollDie(int maxVal) {
        die = Math.max((die + 1) % (maxVal + 1), 1);
        dieRollCount++;
        return die;
    }

    @Override
    protected String part2(List<Rule> input) {
        Set<Universe> knownUniverses = new HashSet<>();
        return null;
    }

    private static class Universe {
        int die;
        int p1;
        int p2;
        boolean p1Winning;
        
        public Universe(int die, int p1, int p2, boolean p1Winning) {
            this.die = die;
            this.p1 = p1;
            this.p2 = p2;
            this.p1Winning = p1Winning;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + die;
            result = prime * result + p1;
            result = prime * result + (p1Winning ? 1231 : 1237);
            result = prime * result + p2;
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
            Universe other = (Universe) obj;
            if (die != other.die)
                return false;
            if (p1 != other.p1)
                return false;
            if (p1Winning != other.p1Winning)
                return false;
            if (p2 != other.p2)
                return false;
            return true;
        }

        
    }
    
}
