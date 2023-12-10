package com.jeffrpowell.adventofcode.aoc2022;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day21 extends Solution2022<Rule>{
    private static final String NUMBER = "NUMBER";
    private static final String PLUS = "PLUS";
    private static final String MINUS = "MINUS";
    private static final String MULT = "MULT";
    private static final String DIV = "DIV";

    @Override
    public int getDay() {
        return 21;
    }

    //sjmn: drzm * dbpl
    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Map.of(
            NUMBER, Pattern.compile("(.+): (\\d+)"),
            PLUS, Pattern.compile("(.+): (\\w+) \\+ (\\w+)"),
            MINUS, Pattern.compile("(.+): (\\w+) - (\\w+)"),
            MULT, Pattern.compile("(.+): (\\w+) \\* (\\w+)"),
            DIV, Pattern.compile("(.+): (\\w+) / (\\w+)")
        ));
    }

    @Override
    protected String part1(List<Rule> input) {
        Map<String, Supplier<Double>> map = new HashMap<>();
        map.putAll(input.stream()
            .collect(Collectors.toMap(
                r -> r.getString(0),
                r -> makeFn(r, map)
            )));
        return Long.toString(map.get("root").get().longValue());
    }

    private Supplier<Double> makeFn(Rule r, Map<String, Supplier<Double>> map) {
        return switch (r.getRulePatternKey()) {
            case PLUS -> () -> map.get(r.getString(1)).get() + map.get(r.getString(2)).get();
            case MINUS -> () -> map.get(r.getString(1)).get() - map.get(r.getString(2)).get();
            case MULT -> () -> map.get(r.getString(1)).get() * map.get(r.getString(2)).get();
            case DIV -> () -> map.get(r.getString(1)).get() / map.get(r.getString(2)).get();
            default -> () -> r.getDouble(1);
        };
    }

    @Override
    protected String part2(List<Rule> input) {
        Map<String, Supplier<Double>> map = new HashMap<>();
        map.putAll(input.stream()
            .collect(Collectors.toMap(
                r -> r.getString(0),
                r -> makeFn(r, map)
            )));
        Rule rootRule = input.stream().filter(s -> s.getString(0).equals("root")).findAny().get();
        String leftKey = rootRule.getString(1);
        String rightKey = rootRule.getString(2);
        map.put("humn", () -> 0.0);
        double left = map.get(leftKey).get();
        double right = map.get(rightKey).get();
        map.put("humn", () -> -10_000_000_000_000.0);
        String dependant;
        boolean linearlyCorrelated = true;
        if (map.get(leftKey).get() != left && map.get(rightKey).get() != right) {
            return "Houston...problem";
        }
        double targetToEqual;
        double dependantVar;
        double newleft = map.get(leftKey).get();
        double bottom;
        double top;
        if (newleft != left) {
            dependant = leftKey;
            dependantVar = left;
            targetToEqual = right;
            if (newleft > left) {
                linearlyCorrelated = false;
            }
        }
        else {
            dependant = rightKey;
            double newright = map.get(rightKey).get();
            dependantVar = right;
            targetToEqual = left;
            if (newright > right) {
                linearlyCorrelated = false;
            }
        }

        double humn = 0.0;
        map.put("humn", () -> 0.0);
        dependantVar = map.get(dependant).get();
        if (dependantVar < targetToEqual && linearlyCorrelated
                || dependantVar > targetToEqual && !linearlyCorrelated) {
            bottom = 0L;
            top = Long.MAX_VALUE;
        }
        else {
            bottom = Long.MIN_VALUE;
            top = 0L;
        }
        while (bottom != top) {
            BigDecimal bigTop = BigDecimal.valueOf(top);
            BigDecimal bigBottom = BigDecimal.valueOf(bottom);
            humn = bigTop.add(bigBottom).divide(BigDecimal.TWO).longValue();
            final double h = humn;
            map.put("humn", () -> h);
            dependantVar = map.get(dependant).get();
            if (dependantVar < targetToEqual && linearlyCorrelated
                || dependantVar > targetToEqual && !linearlyCorrelated) {
                bottom = humn + 1;
            }
            else if (dependantVar > targetToEqual && linearlyCorrelated
            || dependantVar < targetToEqual && !linearlyCorrelated){
                top = humn - 1;
            }
            else {
                return Long.toString(Double.valueOf(humn).longValue());
            }
        }
        BigDecimal bigTop = BigDecimal.valueOf(top);
        BigDecimal bigBottom = BigDecimal.valueOf(bottom);
        humn = bigTop.add(bigBottom).divide(BigDecimal.TWO).longValue();
        final double h = humn;
        map.put("humn", () -> h);
        dependantVar = map.get(dependant).get();
        if (dependantVar == targetToEqual) {
            return Long.toString(Double.valueOf(humn).longValue());
        }
        return "not found";
    }

}
