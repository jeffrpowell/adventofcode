package com.jeffrpowell.adventofcode.aoc2022;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
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
        //root: sbtm + bmgf
        Map<String, Supplier<Double>> map = new HashMap<>();
        map.putAll(input.stream()
            .collect(Collectors.toMap(
                r -> r.getString(0),
                r -> makeFn(r, map)
            )));
        map.put("humn", () -> 0.0);
        double sbtm = map.get("sbtm").get();
        double bmgf = map.get("bmgf").get();
        map.put("humn", () -> -10_000_000_000_000.0);
        String dependant;
        boolean linearlyCorrelated = true;
        if (map.get("sbtm").get() != sbtm && map.get("bmgf").get() != bmgf) {
            return "Houston...problem";
        }
        double targetToEqual;
        double dependantVar;
        double newSbtm = map.get("sbtm").get();
        double bottom;
        double top;
        if (newSbtm != sbtm) {
            dependant = "sbtm";
            dependantVar = sbtm;
            targetToEqual = bmgf;
            if (newSbtm > sbtm) {
                linearlyCorrelated = false;
            }
        }
        else {
            dependant = "bmgf";
            double newBmgf = map.get("bmgf").get();
            dependantVar = bmgf;
            targetToEqual = sbtm;
            if (newBmgf > bmgf) {
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
                List<Double> answers = new ArrayList<>();
                while (dependantVar == targetToEqual) {
                    answers.add(humn);
                    humn--;
                    final double temp = humn;
                    map.put("humn", () -> temp);
                    dependantVar = map.get(dependant).get();
                }
                return Long.toString(answers.stream().min(Comparator.naturalOrder()).get().longValue());
            }
        }
        BigDecimal bigTop = BigDecimal.valueOf(top);
        BigDecimal bigBottom = BigDecimal.valueOf(bottom);
        humn = bigTop.add(bigBottom).divide(BigDecimal.TWO).longValue();
        final double h = humn;
        map.put("humn", () -> h);
        dependantVar = map.get(dependant).get();
        if (dependantVar == targetToEqual) {
            return Long.toString(Double.doubleToLongBits(humn));
        }
        return "not found";
    }

}
