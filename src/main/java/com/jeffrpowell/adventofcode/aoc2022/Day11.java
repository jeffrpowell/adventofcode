package com.jeffrpowell.adventofcode.aoc2022;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return run(input, true);
    }

    @Override
    protected String part2(List<Section> input) {
        return run(input, false);
    }

    private String run(List<Section> input, boolean divideBy3) {
        List<Monkey> monkeys = input.stream().map(Monkey::new).collect(Collectors.toList());
        monkeys.sort(Comparator.comparing(Monkey::getId));
        for (int i = 0; i < (divideBy3 ? 20 : 10_000); i++) {
            for (Monkey monkey : monkeys) {
                Map<Integer, List<BigInteger>> thrownItems = monkey.inspectItems(divideBy3);
                for (Map.Entry<Integer, List<BigInteger>> thrownItemEntry : thrownItems.entrySet()) {
                    monkeys.get(thrownItemEntry.getKey()).catchItems(thrownItemEntry.getValue());
                }
            }
            // if ((i+1) % 20 == 0) {
            //     System.out.println(i + 1);
            //     System.out.println(monkeys.stream().map(Monkey::getInspectedItems).map(l -> l.toString()).collect(Collectors.joining("\n")));
            //     System.out.println(monkeys.stream().map(Monkey::getItems).map(List::toString).collect(Collectors.joining("\n")));
            //     System.out.println();
            // }
        }
        return monkeys.stream()
            .map(Monkey::getInspectedItems)
            .sorted(Comparator.reverseOrder())
            .limit(2)
            .collect(Collectors.reducing(1L, Math::multiplyExact)).toString();
    }

    private static class Monkey {
        private static final String LINE_ID = "LINE_ID";
        private static final String LINE_ITEMS = "LINE_ITEMS";
        private static final String LINE_OPERATION = "LINE_OPERATION";
        private static final String LINE_TEST = "LINE_TEST";
        private static final String LINE_TRUE = "LINE_TRUE";
        private static final String LINE_FALSE = "LINE_FALSE";

        private int id;
        private List<BigInteger> items;
        private Function<BigInteger, BigInteger> operation;
        private BigInteger divisibleBy;
        private int trueTarget;
        private int falseTarget;
        private long inspectedItems;
        private Map<BigInteger, Boolean> divisibleCache;

        public Monkey(Section input) {
            List<Rule> lines = input.getInput(
                InputParserFactory.getRuleParser("\n", Map.of(
                    LINE_ID, Pattern.compile("\\s*Monkey (\\d):"),
                    LINE_ITEMS, Pattern.compile("\\s*Starting items: (.+)"),
                    LINE_OPERATION, Pattern.compile("\\s*Operation: new = old (.) (.+)"),
                    LINE_TEST, Pattern.compile("\\s*Test: divisible by (\\d+)"),
                    LINE_TRUE, Pattern.compile("\\s*If true: throw to monkey (\\d)"),
                    LINE_FALSE, Pattern.compile("\\s*If false: throw to monkey (\\d)")
                ))
            );
            for (Rule line : lines) {
                switch (line.getRulePatternKey()) {
                    case LINE_ID:
                        this.id = line.getInt(0);
                        break;
                    case LINE_ITEMS:
                        String itemsStr = line.getString(0);
                        this.items = Arrays.stream(itemsStr.split(", "))
                            .map(BigInteger::new)
                            .collect(Collectors.toList());
                        break;
                    case LINE_OPERATION:
                        parseOperation(line.getString(0), line.getString(1));
                        break;
                    case LINE_TEST:
                        this.divisibleBy = BigInteger.valueOf(line.getLong(0));
                        break;
                    case LINE_TRUE:
                        this.trueTarget = line.getInt(0);
                        break;
                    case LINE_FALSE:
                        this.falseTarget = line.getInt(0);
                        break;
                }
            }
            this.inspectedItems = 0L;
            BigInteger cacheThresholdStart = this.divisibleBy.multiply(BigInteger.valueOf(1_000_000));
            this.divisibleCache = Stream.iterate(BigInteger.ONE, (BigInteger i) -> i.add(BigInteger.ONE))
                .limit(cacheThresholdStart.longValue())
                .collect(Collectors.toMap(
                    Function.identity(),
                    k -> false
                ));
            for (BigInteger i = this.divisibleBy; i.compareTo(cacheThresholdStart) < 0; i.add(i)) {
                this.divisibleCache.put(i, true);
            }
        }

        private void parseOperation(String operator, String operand) {
            if (operator.equals("*")) {
                if (operand.equals("old")) {
                    this.operation = (BigInteger old) -> old.pow(2);
                }
                else {
                    BigInteger num = new BigInteger(operand);
                    this.operation = (BigInteger old) -> old.multiply(num);
                }
            }
            else {
                BigInteger num = new BigInteger(operand);
                this.operation = (BigInteger old) -> old.add(num);
            }
        }

        public Map<Integer,List<BigInteger>> inspectItems(boolean divideBy3) {
            List<BigInteger> trueMatches = new ArrayList<>();
            List<BigInteger> falseMatches = new ArrayList<>();
            for (BigInteger item : items) {
                item = operation.apply(item);
                if (divideBy3) {
                    item = item.divide(BigInteger.valueOf(3));
                }
                if (isDivisible(item)) {
                    trueMatches.add(item);
                }
                else {
                    falseMatches.add(item);
                }
                inspectedItems++;
            }
            items.clear();
            return Map.of(
                trueTarget, trueMatches,
                falseTarget, falseMatches
            );
        }

        private boolean isDivisible(BigInteger item) {
            return divisibleCache.computeIfAbsent(item, i -> i.mod(divisibleBy).equals(BigInteger.ZERO));
        }

        public void catchItems(List<BigInteger> items) {
            this.items.addAll(items);
        }

        public int getId() {
            return id;
        }

        public List<BigInteger> getItems() {
            return items;
        }

        public Long getInspectedItems() {
            return inspectedItems;
        }
    }
}
