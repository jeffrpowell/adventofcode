package com.jeffrpowell.adventofcode.aoc2025;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day5 extends Solution2025<Section>{
    @Override
    public int getDay() {
        return 5;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, false));
    }

    private static class Range {
        private long start;
        private long end;

        Range(long start, long end) {
            this.start = start;
            this.end = end;
        }

        public boolean contains(long value) {
            return value >= start && value <= end;
        }

        public void expand(Range other) {
            this.start = Math.min(this.start, other.start);
            this.end = Math.max(this.end, other.end);
        }

        public long getStart() {
            return start;
        }

        public long getEnd() {
            return end;
        }
    }

    @Override
    protected String part1(List<Section> input) {
        List<Range> rawRanges = input.get(0)
            .getInput(InputParserFactory.getRuleParser("\n", "(\\d+)-(\\d+)"))
            .stream()
            .map(rule -> {
                return new Range(rule.getLong(0), rule.getLong(1));
            })
            .sorted(Comparator.comparingLong(Range::getStart))
            .toList();
        List<Range> mergedRanges = new ArrayList<>();
        Range priorRange = rawRanges.get(0);
        mergedRanges.add(priorRange);
        for (Range range : rawRanges) {
            if (priorRange.getEnd() >= range.getStart() - 1) {
                priorRange.expand(range);
            } else {
                mergedRanges.add(range);
                priorRange = range;
            }
        }

        List<Long> ingredientIds = input.get(1).getInput(InputParserFactory.getLongParser());

        return Long.toString(ingredientIds.stream()
            .filter(id -> mergedRanges.stream().anyMatch(range -> range.contains(id)))
            .count());
    }

    @Override
    protected String part2(List<Section> input) {
        List<Range> rawRanges = input.get(0)
            .getInput(InputParserFactory.getRuleParser("\n", "(\\d+)-(\\d+)"))
            .stream()
            .map(rule -> {
                return new Range(rule.getLong(0), rule.getLong(1));
            })
            .sorted(Comparator.comparingLong(Range::getStart))
            .toList();
        List<Range> mergedRanges = new ArrayList<>();
        Range priorRange = rawRanges.get(0);
        mergedRanges.add(priorRange);
        for (Range range : rawRanges) {
            if (priorRange.getEnd() >= range.getStart() - 1) {
                priorRange.expand(range);
            } else {
                mergedRanges.add(range);
                priorRange = range;
            }
        }
        return Long.toString(mergedRanges.stream()
            .mapToLong(range -> range.getEnd() - range.getStart() + 1)
            .sum());
    }
}
