package com.jeffrpowell.adventofcode.aoc2023;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        Set<List<String>> cache = new HashSet<>();
        int lastFailedIndex = 0;
        int mirroredIndices = 0;
        for (int i = 0; i < input.size(); i++) {
            if (cache.add(input.get(i))) {
                lastFailedIndex = i;
            }
            else {
                mirroredIndices++;
                if (mirroredIndices - 1 == lastFailedIndex) {
                    break;
                }
            }
        }
        if (lastFailedIndex < input.size() - 1) {
            return lastFailedIndex + 1;
        }
        cache.clear();
        List<List<String>> pivotedInput = pivotList(input);
        lastFailedIndex = 0;
        mirroredIndices = 0;
        for (int i = 0; i < pivotedInput.size(); i++) {
            if (cache.add(pivotedInput.get(i))) {
                lastFailedIndex = i;
            }
            else {
                mirroredIndices++;
                if (mirroredIndices - 1 == lastFailedIndex) {
                    break;
                }
            }
        }
        if (lastFailedIndex < pivotedInput.size() - 1) {
            return 100 * (lastFailedIndex + 1);
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
