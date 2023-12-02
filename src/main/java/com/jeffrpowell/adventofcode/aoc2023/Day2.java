package com.jeffrpowell.adventofcode.aoc2023;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day2 extends Solution2023<String>{
    @Override
    public int getDay() {
        return 2;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }


    @Override
    protected String part1(List<String> input) {
        return Integer.toString(input.stream()
            .map(Game::new)
            .filter(g -> g.isGamePossible(12, 13, 14))
            .map(g -> g.id)
            .reduce(0, Math::addExact));
    }

    @Override
    protected String part2(List<String> input) {
        return Long.toString(input.stream()
            .map(Game::new)
            .map(Game::power)
            .reduce(0L, Math::addExact));
    }

    private static class Game {
        private static final Pattern GAME_REGEX = Pattern.compile("^Game (\\d+): (.+)$");
        int id;
        List<Round> rounds;
        public Game(String game) {
            Matcher m = GAME_REGEX.matcher(game);
            m.find();
            this.id = Integer.parseInt(m.group(1));
            this.rounds = Arrays.stream(m.group(2).split("; ")).map(Round::new).collect(Collectors.toList());
        }

        public boolean isGamePossible(int red, int green, int blue) {
            for (Round r : rounds) {
                if (r.red > red || r.green > green || r.blue > blue) {
                    return false;
                }
            }
            return true;
        }

        public long power() {
            long maxRed = 0;
            long maxGreen = 0;
            long maxBlue = 0;
            for (Round r : rounds) {
                if (r.red > maxRed) {
                    maxRed = r.red;
                }
                if (r.green > maxGreen) {
                    maxGreen = r.green;
                }
                if (r.blue > maxBlue) {
                    maxBlue = r.blue;
                }
            }
            return maxRed * maxGreen * maxBlue;
        }
    }

    private static class Round {
        long red = 0;
        long green = 0;
        long blue = 0;

        public Round(String round) {
            Arrays.stream(round.split(", ")).forEach(this::setColor);
        }

        private void setColor(String colorStr) {
            String[] tokens = colorStr.split(" ");
            if (tokens[1].equals("red")) {
                this.red = Integer.parseInt(tokens[0]);
            }
            else if (tokens[1].equals("green")) {
                this.green = Integer.parseInt(tokens[0]);
            }
            else {
                this.blue = Integer.parseInt(tokens[0]);
            }
        }
    }

}
