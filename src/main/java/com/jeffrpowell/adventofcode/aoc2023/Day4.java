package com.jeffrpowell.adventofcode.aoc2023;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Sets;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;


public class Day4 extends Solution2023<List<String>>{
    @Override
    public int getDay() {
        return 4;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("(?::\\s+|\\s\\|\\s+)");
    }


    @Override
    protected String part1(List<List<String>> input) {
        List<String> winningNumbersStrs = input.stream()
            .map(i -> i.get(1))
            .collect(Collectors.toList());
        List<String> drawnNumbersStrs = input.stream()
            .map(i -> i.get(2))
            .collect(Collectors.toList());
        InputParser<List<Integer>> intParser = InputParserFactory.getIntegerTokenSVParser("\\s+");
        List<List<Integer>> winningNumbersList = intParser.parseInput(winningNumbersStrs);
        List<List<Integer>> drawnNumbersList = intParser.parseInput(drawnNumbersStrs);
        long points = 0;
        for (int i = 0; i < winningNumbersList.size(); i++) {
            Set<Integer> winningNumbers = winningNumbersList.get(i).stream().collect(Collectors.toSet());
            Set<Integer> drawnNumbers = drawnNumbersList.get(i).stream().collect(Collectors.toSet());
            Set<Integer> wins = Sets.intersection(winningNumbers, drawnNumbers);
            points += Math.pow(2, wins.size() - 1);
        }
        return Long.toString(points);
    }

    @Override
    protected String part2(List<List<String>> input) {
        List<String> winningNumbersStrs = input.stream()
        .map(i -> i.get(1))
        .collect(Collectors.toList());
        List<String> drawnNumbersStrs = input.stream()
        .map(i -> i.get(2))
        .collect(Collectors.toList());
        Map<Integer, Long> copies = Stream.iterate(1, i -> i+1).limit(drawnNumbersStrs.size())
            .collect(Collectors.toMap(
                Function.identity(), 
                i -> 1L
            ));
        InputParser<List<Integer>> intParser = InputParserFactory.getIntegerTokenSVParser("\\s+");
        List<List<Integer>> winningNumbersList = intParser.parseInput(winningNumbersStrs);
        List<List<Integer>> drawnNumbersList = intParser.parseInput(drawnNumbersStrs);
        for (int card = 0; card < winningNumbersList.size(); card++) {
            final int cardid_ro = card + 1;
            Set<Integer> winningNumbers = winningNumbersList.get(card).stream().collect(Collectors.toSet());
            Set<Integer> drawnNumbers = drawnNumbersList.get(card).stream().collect(Collectors.toSet());
            Set<Integer> wins = Sets.intersection(winningNumbers, drawnNumbers);
            for (int copyCard = cardid_ro + 1; copyCard < cardid_ro + 1 + wins.size(); copyCard++) {
                copies.put(copyCard, copies.compute(copyCard, (k, v) -> v + copies.get(cardid_ro)));
            }
        }
        return Long.toString(copies.values().stream().reduce(0L, Math::addExact));
    }
}
