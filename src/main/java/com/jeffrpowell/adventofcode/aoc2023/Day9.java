package com.jeffrpowell.adventofcode.aoc2023;

import java.util.ArrayList;
import java.util.List;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;


public class Day9 extends Solution2023<List<Integer>>{
    @Override
    public int getDay() {
        return 9;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser(" ");
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        return Long.toString(input.stream().map(this::solveNextNumber).reduce(0L, Math::addExact));
    }

    private long solveNextNumber(List<Integer> input) {
        List<List<Integer>> layers = new ArrayList<>();
        layers.add(input);
        List<Integer> priorLayer = input;
        while (!priorLayer.stream().allMatch(n -> n == 0)) {
            List<Integer> nextLayer = new ArrayList<>();
            for (int i = 1; i < priorLayer.size(); i++) {
                nextLayer.add(priorLayer.get(i) - priorLayer.get(i-1));
            }
            layers.add(nextLayer);
            priorLayer = nextLayer;
        }
        return layers.stream().map(list -> list.get(list.size() - 1)).map(Long::valueOf).reduce(0L, Math::addExact);
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        return Long.toString(input.stream().map(this::solvePriorNumber).reduce(0L, Math::addExact));
    }

    private long solvePriorNumber(List<Integer> input) {
        List<List<Integer>> layers = new ArrayList<>();
        layers.add(input);
        List<Integer> priorLayer = input;
        while (!priorLayer.stream().allMatch(n -> n == 0)) {
            List<Integer> nextLayer = new ArrayList<>();
            for (int i = 1; i < priorLayer.size(); i++) {
                nextLayer.add(priorLayer.get(i) - priorLayer.get(i-1));
            }
            layers.add(nextLayer);
            priorLayer = nextLayer;
        }
        long lastPriorNumber = 0;
        for (int i = layers.size() - 2; i >= 0; i--) {
            lastPriorNumber = layers.get(i).get(0) - lastPriorNumber;
        }
        return lastPriorNumber;
    }

}
