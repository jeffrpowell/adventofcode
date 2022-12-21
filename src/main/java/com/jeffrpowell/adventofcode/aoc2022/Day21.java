package com.jeffrpowell.adventofcode.aoc2022;

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
        Map<String, Supplier<Long>> map = new HashMap<>();
        map.putAll(input.stream()
            .collect(Collectors.toMap(
                r -> r.getString(0),
                r -> makeFn(r, map)
            )));
        return Long.toString(map.get("root").get());
    }

    private Supplier<Long> makeFn(Rule r, Map<String, Supplier<Long>> map) {
        return switch (r.getRulePatternKey()) {
            case PLUS -> () -> map.get(r.getString(1)).get() + map.get(r.getString(2)).get();
            case MINUS -> () -> map.get(r.getString(1)).get() - map.get(r.getString(2)).get();
            case MULT -> () -> map.get(r.getString(1)).get() * map.get(r.getString(2)).get();
            case DIV -> () -> map.get(r.getString(1)).get() / map.get(r.getString(2)).get();
            default -> () -> r.getLong(1);
        };
    }

    @Override
    protected String part2(List<Rule> input) {
        //root: sbtm + bmgf
        Map<String, Supplier<Long>> map = new HashMap<>();
        map.putAll(input.stream()
            .collect(Collectors.toMap(
                r -> r.getString(0),
                r -> makeFn(r, map)
            )));
        map.put("humn", () -> 0L);
        long sbtm = map.get("sbtm").get();
        long bmgf = map.get("bmgf").get();
        map.put("humn", () -> -10_000_000_000_000L);
        String dependant;
        boolean linearlyCorrelated = true;
        if (map.get("sbtm").get() != sbtm && map.get("bmgf").get() != bmgf) {
            return "Houston...problem";
        }
        long targetToEqual;
        long dependantVar;
        long newSbtm = map.get("sbtm").get();
        long bottom;
        long top;
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
            long newBmgf = map.get("bmgf").get();
            dependantVar = bmgf;
            targetToEqual = sbtm;
            if (newBmgf > bmgf) {
                linearlyCorrelated = false;
            }
        }

        long humn = 0L;
        map.put("humn", () -> 0L);
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
            BigInteger bigTop = BigInteger.valueOf(top);
            BigInteger bigBottom = BigInteger.valueOf(bottom);
            humn = bigTop.add(bigBottom).divide(BigInteger.TWO).longValue();
            final long h = humn;
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
                List<Long> answers = new ArrayList<>();
                while (dependantVar == targetToEqual) {
                    answers.add(humn);
                    humn--;
                    final long temp = humn;
                    map.put("humn", () -> temp);
                    dependantVar = map.get(dependant).get();
                }
                return answers.stream().max(Comparator.naturalOrder()).get().toString();
            }
        }
        return Long.toString(humn);
    }

}
