package com.jeffrpowell.adventofcode.aoc2021;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day4 extends Solution2021<List<Integer>>{
    private static final String BINGO_STR = "23,30,70,61,79,49,19,37,64,48,72,34,69,53,15,74,89,38,46,36,28,32,45,2,39,58,11,62,97,40,14,87,96,94,91,92,80,99,6,31,57,98,65,10,33,63,42,17,47,66,26,22,73,27,7,0,55,8,56,29,86,25,4,12,51,60,35,50,5,75,95,44,16,93,21,3,24,52,77,76,43,41,9,84,67,71,83,88,59,68,85,82,1,18,13,78,20,90,81,54";
    private static final List<Integer> BINGO = InputParserFactory.getIntegerCSVParser().parseInput(List.of(BINGO_STR)).get(0);
    @Override
    public int getDay() {
        return 4;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser(" ");
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        int rowCount = 0;
        List<List<Integer>> card = new ArrayList<>();
        List<BingoCard> bingoCards = new ArrayList<>();
        for (List<Integer> row : input) {
            if (rowCount == 5) {
                bingoCards.add(new BingoCard(card));
                rowCount = 0;
                card.clear();
            }
            else {
                card.add(row);
                rowCount++;
            }
        }
        for (Integer call : BINGO) {
            for (BingoCard bingoCard : bingoCards) {
                if (bingoCard.callNumber(call)) {
                    return Integer.toString(bingoCard.score());
                }
            }
        }
        return "MISSED";
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        int rowCount = 0;
        List<List<Integer>> card = new ArrayList<>();
        List<BingoCard> bingoCards = new ArrayList<>();
        for (List<Integer> row : input) {
            if (rowCount == 5) {
                bingoCards.add(new BingoCard(card));
                rowCount = 0;
                card.clear();
            }
            else {
                card.add(row);
                rowCount++;
            }
        }
        for (Integer call : BINGO) {
            for (BingoCard bingoCard : bingoCards) {
                if (bingoCard.callNumber(call) && bingoCards.size() == 1) {
                    return Integer.toString(bingoCard.score());
                }
            }
            bingoCards = bingoCards.stream().filter(c -> c.winningCall == -1).collect(Collectors.toList());
        }
        return "MISSED";
    }

    private static class BingoCard {
        List<Set<Integer>> winningCombos;
        Set<Integer> card;
        int winningCall = -1;

        public BingoCard(List<List<Integer>> rows) {
            winningCombos = rows.stream().map(i -> new HashSet<>(i)).collect(Collectors.toList());
            List<List<Integer>> cols = Stream.generate(ArrayList<Integer>::new).limit(5).collect(Collectors.toList());
            for (int i = 0; i < 5; i++) {
                for (List<Integer> row : rows) {
                    cols.get(i).add(row.get(i));
                }
            }
            winningCombos.addAll(cols.stream().map(i -> new HashSet<>(i)).collect(Collectors.toList()));
            card = rows.stream().flatMap(List::stream).collect(Collectors.toSet());
        }

        public boolean callNumber(int num) {
            card.remove(num);
            for (Set<Integer> combo : winningCombos) {
                combo.remove(num);
                if (combo.isEmpty()) {
                    winningCall = num;
                    return true;
                }
            }
            return false;
        }

        public int score() {
            return card.stream().reduce(0, Math::addExact) * winningCall;
        }
    }
    
}
