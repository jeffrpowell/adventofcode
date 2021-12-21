package com.jeffrpowell.adventofcode.aoc2021;

import java.util.List;
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
        // TODO Auto-generated method stub
        return null;
    }
    
}
