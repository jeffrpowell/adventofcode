package com.jeffrpowell.adventofcode.aoc2020;

import com.google.common.collect.Lists;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day22 extends Solution2020<Integer>{

    @Override
    public int getDay() {
        return 22;
    }

    @Override
    public InputParser<Integer> getInputParser() {
        return InputParserFactory.getIntegerParser();
    }

    @Override
    protected String part1(List<Integer> input) {
        List<List<Integer>> decks = Lists.partition(input, input.size() / 2);
        List<Integer> deck1 = new ArrayList<>(decks.get(0));
        List<Integer> deck2 = new ArrayList<>(decks.get(1));
        while(!deck1.isEmpty() && !deck2.isEmpty()) {
            int play1 = deck1.remove(0);
            int play2 = deck2.remove(0);
            if (play1 > play2) {
                deck1 = Stream.concat(deck1.stream(), Stream.of(play1, play2)).collect(Collectors.toList());
            }
            else {
                deck2 = Stream.concat(deck2.stream(), Stream.of(play2, play1)).collect(Collectors.toList());
            }
        }
        return Long.toString(scoreDeck(deck1.isEmpty() ? deck2 : deck1));
    }

    @Override
    protected String part2(List<Integer> input) {
        List<List<Integer>> decks = Lists.partition(input, input.size() / 2);
        List<Integer> deck1 = new ArrayList<>(decks.get(0));
        List<Integer> deck2 = new ArrayList<>(decks.get(1));
        GameResult result = playGame(deck1, deck2);
        return Long.toString(result.score);
    }
    
    private GameResult playGame(List<Integer> deck1, List<Integer> deck2) {
        Set<Integer> previousStates = new HashSet<>();
        boolean infiniteGameTermination = false;
        while(!deck1.isEmpty() && !deck2.isEmpty()) {
            if (!previousStates.add(stateHash(deck1, deck2))) {
                infiniteGameTermination = true;
                break;
            };
            int play1 = deck1.remove(0);
            int play2 = deck2.remove(0);
            if (player1Wins(play1, play2, deck1, deck2)) {
                deck1 = Stream.concat(deck1.stream(), Stream.of(play1, play2)).collect(Collectors.toList());
            }
            else {
                deck2 = Stream.concat(deck2.stream(), Stream.of(play2, play1)).collect(Collectors.toList());
            }
        }
        if (deck2.isEmpty() || infiniteGameTermination) {
            return new GameResult(true, deck1, scoreDeck(deck1));
        }
        else {
            return new GameResult(false, deck2, scoreDeck(deck2));
        }
    }
    
    private int stateHash(List<Integer> deck1, List<Integer> deck2) {
        return deck1.hashCode() + 7 * deck2.hashCode();
    }
    
    private boolean player1Wins(int play1, int play2, List<Integer> deck1, List<Integer> deck2) {
        if (deck1.size() >= play1 && deck2.size() >= play2) {
            //recursive game
            GameResult result = playGame(deck1.stream().limit(play1).collect(Collectors.toList()), deck2.stream().limit(play2).collect(Collectors.toList()));
            return result.player1Wins;
        }
        else {
            return play1 > play2;
        }
    }
    
    private long scoreDeck(List<Integer> winningDeck) {
        winningDeck = Lists.reverse(winningDeck);
        long score = 0;
        for (int i = 0; i < winningDeck.size(); i++) {
            score += winningDeck.get(i) * (i + 1);
        }
        return score;
    }

    private static class GameResult
    {
        boolean player1Wins;
        long score;

        public GameResult(boolean player1Wins, List<Integer> deck, long score) {
            this.player1Wins = player1Wins;
            this.score = score;
        }
        
    }
}
