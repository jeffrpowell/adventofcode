package com.jeffrpowell.adventofcode.aoc2024;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.SplitPartParser.Part;

public class Day7 extends Solution2024<Part<Long, List<Long>>>{
    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public InputParser<Part<Long, List<Long>>> getInputParser() {
        return InputParserFactory.getSplitPartParser(Pattern.compile(": "), 
            InputParserFactory.getLongParser(),
            InputParserFactory.getLongTokenSVParser(" ")
        );
    }

    @Override
    protected String part1(List<Part<Long, List<Long>>> input) {
        return Long.toString(input.stream()
            .filter(p -> search(p.firstPart(), p.secondPart().removeFirst(), p.secondPart(), false))
            .map(Part::firstPart)
            .reduce(0L, Math::addExact));
    }

    private boolean search(Long target, Long totalSoFar, List<Long> operands, boolean allowConcat) {
        if (operands.isEmpty()) {
            return Long.compare(totalSoFar, target) == 0;
        }
        Long next = operands.removeFirst();
        if (allowConcat) {
            boolean tryConcatBranch = search(target, Long.parseLong(totalSoFar.toString() + next.toString()), operands.stream().collect(Collectors.toList()), allowConcat);
            if (tryConcatBranch) {
                return true;
            }
        }
        boolean tryAddBranch = search(target, totalSoFar + next, operands.stream().collect(Collectors.toList()), allowConcat);
        if (tryAddBranch) {
            return true;
        }
        else {
            return search(target, totalSoFar * next, operands.stream().collect(Collectors.toList()), allowConcat);
        }
    }

    @Override
    protected String part2(List<Part<Long, List<Long>>> input) {
        return Long.toString(input.stream()
            .filter(p -> search(p.firstPart(), p.secondPart().removeFirst(), p.secondPart(), true))
            .map(Part::firstPart)
            .reduce(0L, Math::addExact));
    }

}
