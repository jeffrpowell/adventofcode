package com.jeffrpowell.adventofcode.aoc2022;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day11 extends Solution2022<Section>{

    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, true));
    }

    @Override
    protected String part1(List<Section> input) {
        List<Monkey> monkeys = input.stream().map(Monkey::new).collect(Collectors.toList());
    }

    @Override
    protected String part2(List<Section> input) {
        return null;
    }

    private static class Monkey {
        private static final String LINE_ID = "LINE_ID";
        private static final String LINE_ITEMS = "LINE_ITEMS";
        private static final String LINE_OPERATION = "LINE_OPERATION";
        private static final String LINE_TEST = "LINE_TEST";
        private static final String LINE_TRUE = "LINE_TRUE";
        private static final String LINE_FALSE = "LINE_FALSE";

        private int id;
        private List<Integer> items;
        private Function<Integer, Integer> operation;
        private int divisibleBy;
        private int trueTarget;
        private int falseTarget;

        public Monkey(Section input) {
            List<Rule> lines = input.getInput(
                InputParserFactory.getRuleParser("\n", Map.of(
                    LINE_ID, Pattern.compile("Monkey (\\d)"),
                    LINE_ITEMS, Pattern.compile("Starting items: (.+)"),
                    LINE_OPERATION, Pattern.compile("Operation: new = old (.) (.+)"),
                    LINE_TEST, Pattern.compile("Test: divisible by (\\d+)"),
                    LINE_TRUE, Pattern.compile("If true: throw to monkey (\\d)"),
                    LINE_FALSE, Pattern.compile("If false: throw to monkey (\\d)")
                ))
            );
            for (Rule line : lines) {
                switch (line.getRulePatternKey()) {
                    case LINE_ID:
                        this.id = line.getInt(0);
                        break;
                    case LINE_ITEMS:
                        String itemsStr = line.getString(0);
                        this.items = Arrays.stream(itemsStr.split(", ")).map(Integer::parseInt).collect(Collectors.toList());
                        break;
                    case LINE_OPERATION:
                        parseOperation(line.getString(0), line.getString(1));
                        break;
                    case LINE_TEST:
                        this.divisibleBy = line.getInt(0);
                        break;
                    case LINE_TRUE:
                        this.trueTarget = line.getInt(0);
                        break;
                    case LINE_FALSE:
                        this.falseTarget = line.getInt(0);
                        break;
                }
            }
        }

        private void parseOperation(String operator, String operand) {
            if (operator.equals("*")) {
                if (operand.equals("old")) {
                    this.operation = old -> old * old;
                }
                else {
                    int num = Integer.parseInt(operand);
                    this.operation = old -> old * num;
                }
            }
            else {
                int num = Integer.parseInt(operand);
                this.operation = old -> old + num;
            }
        }
    }
}
