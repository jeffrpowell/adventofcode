package com.jeffrpowell.adventofcode.aoc2022;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
        return run(input, true);
    }

    @Override
    protected String part2(List<Section> input) {
        return run(input, false);
    }

    private String run(List<Section> input, boolean divideBy3) {
        List<Monkey> monkeys = input.stream().map(Monkey::new).collect(Collectors.toList());
        monkeys.sort(Comparator.comparing(Monkey::getId));
        long modPeriod = monkeys.stream().map(Monkey::getDivisibleBy).collect(Collectors.reducing(1L, Math::multiplyExact));
        monkeys.stream().forEach(m -> m.setModPeriod(modPeriod));
        for (int i = 0; i < (divideBy3 ? 20 : 10_000); i++) {
            for (Monkey monkey : monkeys) {
                Map<Integer, List<Long>> thrownItems = monkey.inspectItems(divideBy3);
                for (Map.Entry<Integer, List<Long>> thrownItemEntry : thrownItems.entrySet()) {
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
        private List<Long> items;
        private Function<Long, Long> operation;
        private Long divisibleBy;
        private int trueTarget;
        private int falseTarget;
        private long inspectedItems;
        private long modPeriod;
        // private Map<Long, Boolean> divisibleCache;

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
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                        break;
                    case LINE_OPERATION:
                        parseOperation(line.getString(0), line.getString(1));
                        break;
                    case LINE_TEST:
                        this.divisibleBy = line.getLong(0);
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
            // Long cacheThresholdStart = this.divisibleBy.multiply(Long.valueOf(1_000_000));
            // this.divisibleCache = Stream.iterate(Long.ONE, (Long i) -> i.add(Long.ONE))
            //     .limit(cacheThresholdStart.longValue())
            //     .collect(Collectors.toMap(
            //         Function.identity(),
            //         k -> false
            //     ));
            // for (Long i = this.divisibleBy; i.compareTo(cacheThresholdStart) < 0; i.add(i)) {
            //     this.divisibleCache.put(i, true);
            // }
        }

        private void parseOperation(String operator, String operand) {
            if (operator.equals("*")) {
                if (operand.equals("old")) {
                    this.operation = (Long old) -> old * old;
                }
                else {
                    Long num = Long.parseLong(operand);
                    this.operation = (Long old) -> old * num;
                }
            }
            else {
                Long num = Long.parseLong(operand);
                this.operation = (Long old) -> old + num;
            }
        }

        public Map<Integer,List<Long>> inspectItems(boolean divideBy3) {
            List<Long> trueMatches = new ArrayList<>();
            List<Long> falseMatches = new ArrayList<>();
            for (Long item : items) {
                item = operation.apply(item);
                if (divideBy3) {
                    item /= 3;
                }
                else if (item > modPeriod) {
                    item %= modPeriod;
                }
                if (item % divisibleBy == 0) {
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
        
        public void setModPeriod(long modPeriod) {
            this.modPeriod = modPeriod;
        }

        public void catchItems(List<Long> items) {
            this.items.addAll(items);
        }

        public int getId() {
            return id;
        }

        public List<Long> getItems() {
            return items;
        }

        public Long getDivisibleBy() {
            return divisibleBy;
        }

        public Long getInspectedItems() {
            return inspectedItems;
        }

    }
}
