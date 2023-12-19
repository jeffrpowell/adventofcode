package com.jeffrpowell.adventofcode.aoc2023;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day13 extends Solution2023<Section>{
    private static final Map<List<List<String>>, Integer> solutionCache = new HashMap<>();
    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, true));
    }


    @Override
    protected String part1(List<Section> input) {
        return Long.toString(input.stream()
            .map(section -> section.getInput(InputParserFactory.getTokenSVParser("")))
            .map(in -> findCenterAndCountColsRows2(in, 0))
            .reduce(0L, Math::addExact));
    }

    @Override
    protected String part2(List<Section> input) {
        part1(input);
        return Long.toString(input.stream()
            .map(section -> section.getInput(InputParserFactory.getTokenSVParser("")))
            .map(in -> findCenterAndCountColsRows2(in, 1))
            .reduce(0L, Math::addExact));
    }

    private long findCenterAndCountColsRows2(List<List<String>> input, int tolerance) { 
        List<String> prior = input.get(0);
        for (int i = 1; i < input.size(); i++) {
            List<String> next = input.get(i);
            int distance = distance(prior, next);
            if (distance <= tolerance 
                && (!solutionCache.containsKey(input) || solutionCache.get(input) != i)) {
                    List<List<String>> left = input.subList(0, i).reversed();
                    List<List<String>> right = input.subList(i, input.size());
                    int toleranceBudget = tolerance - distance;
                    boolean foundSolution = true;
                    for (int j = 1; j < Math.min(left.size(), right.size()); j++) {
                        List<String> leftList = left.get(j);
                        List<String> rightList = right.get(j);
                        distance = distance(leftList, rightList);
                        if (distance <= toleranceBudget) {
                            toleranceBudget -= distance;
                        }
                        else {
                            foundSolution = false;
                            break;
                        }
                    }
                    if (foundSolution) {
                        solutionCache.put(input, i);
                        return 100 * left.size();
                    }
            }
            prior = next;
        }
        List<List<String>> pivotedInput = pivotList(input);
        prior = pivotedInput.get(0);
        for (int i = 1; i < pivotedInput.size(); i++) {
            List<String> next = pivotedInput.get(i);
            int distance = distance(prior, next);
            if (distance <= tolerance 
                && (!solutionCache.containsKey(pivotedInput) || solutionCache.get(pivotedInput) == i)) {
                    List<List<String>> left = pivotedInput.subList(0, i).reversed();
                    List<List<String>> right = pivotedInput.subList(i, pivotedInput.size());
                    int toleranceBudget = tolerance - distance;
                    boolean foundSolution = true;
                    for (int j = 1; j < Math.min(left.size(), right.size()); j++) {
                        List<String> leftList = left.get(j);
                        List<String> rightList = right.get(j);
                        distance = distance(leftList, rightList);
                        if (distance <= toleranceBudget) {
                            toleranceBudget -= distance;
                        }
                        else {
                            foundSolution = false;
                            break;
                        }
                    }
                    if (foundSolution) {
                        solutionCache.put(pivotedInput, i);
                        return left.size();
                    }
            }
            prior = next;
        }
        input.stream()
            .map(line -> line.stream().collect(Collectors.joining()))
            .forEach(System.out::println);
        System.out.println();
        return Long.MIN_VALUE;
    }

    private long findCenterAndCountColsRows(List<List<String>> input, int tolerance) {
        Deque<List<String>> history = new ArrayDeque<>();
        Deque<List<String>> mirrorCheck = new ArrayDeque<>();
        int winningIndex = 0;
        for (int i = 0; i < input.size(); i++) {
            winningIndex = i;
            if (!history.isEmpty() && distance(history.peek(), input.get(i)) <= tolerance && (tolerance == 0 || (tolerance == 1 && (!solutionCache.containsKey(input) || i != solutionCache.get(input))))) {
                mirrorCheck.push(history.pop());
                if (history.isEmpty()) {
                    break;
                }
            }
            else if (!mirrorCheck.isEmpty()) {
                while (!mirrorCheck.isEmpty()) {
                    mirrorCheck.stream().forEach(history::push);
                    mirrorCheck.reversed().stream().forEach(history::push);
                    mirrorCheck.clear();
                }
                history.push(input.get(i));
            }
            else {
                history.push(input.get(i));
            }
        }
        if (!mirrorCheck.isEmpty()) {
            solutionCache.put(input, winningIndex);
            return 100 * (history.size() + mirrorCheck.size());
        }
        history.clear();
        mirrorCheck.clear();
        List<List<String>> pivotedInput = pivotList(input);
        for (int i = 0; i < pivotedInput.size(); i++) {
            winningIndex = i;
            if (!history.isEmpty() && distance(history.peek(), pivotedInput.get(i)) <= tolerance && (tolerance == 0 || (tolerance == 1 && (!solutionCache.containsKey(input) || i != solutionCache.get(input))))) {
                mirrorCheck.push(history.pop());
                if (history.isEmpty()) {
                    break;
                }
            }
            else if (!mirrorCheck.isEmpty()) {
                while (!mirrorCheck.isEmpty()) {
                    mirrorCheck.stream().forEach(history::push);
                    mirrorCheck.reversed().stream().forEach(history::push);
                    mirrorCheck.clear();
                }
                history.push(pivotedInput.get(i));
            }
            else {
                history.push(pivotedInput.get(i));
            }
        }
        if (!mirrorCheck.isEmpty()) {
            solutionCache.put(pivotedInput, winningIndex);
            return history.size() + mirrorCheck.size();
        }
        input.stream()
            .map(line -> line.stream().collect(Collectors.joining()))
            .forEach(System.out::println);
        System.out.println();
        return Long.MIN_VALUE;
    }

    public static List<List<String>> pivotList(List<List<String>> inputList) {
        List<List<String>> outputList = new ArrayList<>();

        int numRows = inputList.size();
        int numCols = inputList.get(0).size();

        for (int i = 0; i < numCols; i++) {
            List<String> newRow = new ArrayList<>();
            for (int j = 0; j < numRows; j++) {
                newRow.add(inputList.get(j).get(i));
            }
            outputList.add(newRow);
        }

        return outputList;
    }

    private int distance(List<String> a, List<String> b) {
        int distance = 0;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) {
                distance++;
            }
        }
        return distance;
    }
}
