package com.jeffrpowell.adventofcode.aoc2023;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day13 extends Solution2023<Section>{
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
            .map(this::findCenterAndCountColsRows)
            .reduce(0L, Math::addExact));
    }

    @Override
    protected String part2(List<Section> input) {
        return part1(input);
    }

    private long findCenterAndCountColsRows(List<List<String>> input) {
        Deque<List<String>> history = new ArrayDeque<>();
        Deque<List<String>> mirrorCheck = new ArrayDeque<>();
        for (int i = 0; i < input.size(); i++) {
            if (!history.isEmpty() && history.peek().equals(input.get(i))) {
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
            return 100 * (history.size() + mirrorCheck.size());
        }
        history.clear();
        mirrorCheck.clear();
        List<List<String>> pivotedInput = pivotList(input);
        for (int i = 0; i < pivotedInput.size(); i++) {
            if (!history.isEmpty() && history.peek().equals(pivotedInput.get(i))) {
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
}
