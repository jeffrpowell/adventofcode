package com.jeffrpowell.adventofcode.aoc2025;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.algorithms.CharArrayUtils;
import com.jeffrpowell.adventofcode.algorithms.PowerSets;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.IntSort;
import com.microsoft.z3.Model;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Status;

public class Day10 extends Solution2025<List<String>>{
    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser(" ");
    }

    @Override
    protected String part1(List<List<String>> input) {
        List<Machine> machines = input.stream()
            .map(Machine::fromInput)
            .collect(Collectors.toList());
        long minpresses = 0;
        for (Machine machine : machines) {
            List<List<List<Boolean>>> buttonCombinations = PowerSets.getPowerSet(machine.buttons());
            int presses = Integer.MAX_VALUE;
            for (List<List<Boolean>> buttonSequence : buttonCombinations) {
                List<Boolean> buttonPressResult = pressButtonsLights(buttonSequence);
                if (buttonPressResult.equals(machine.target())) {
                    presses = Math.min(presses, buttonSequence.size());
                }
            }
            minpresses += presses;
        }
        return Long.toString(minpresses);
    }

    private List<Boolean> pressButtonsLights(List<List<Boolean>> buttonSequence) {
        if (buttonSequence.isEmpty()) {
            return List.of();
        }
        int size = buttonSequence.getFirst().size();
        List<Boolean> state = Stream.generate(() -> false).limit(size).collect(Collectors.toList());
        for (List<Boolean> button : buttonSequence) {
            for (int i = 0; i < size; i++) {
                if (button.get(i)) {
                    state.set(i, !state.get(i));
                }
            }
        }
        return state;
    }

    @Override
    protected String part2(List<List<String>> input) {
        List<Machine> machines = input.stream()
            .map(Machine::fromInput)
            .collect(Collectors.toList());
        long minpresses = 0;
        for (Machine machine : machines) {
            minpresses += minPressesForJoltages(machine);
        }
        return Long.toString(minpresses);
    }

    record Machine(List<Boolean> target, List<List<Boolean>> buttons, List<Integer> joltages){
        public static Machine fromInput(List<String> inputs) {
            String lightsString = inputs.getFirst();
            String joltageString = inputs.getLast();
            lightsString = lightsString.replaceAll("[\\[\\]]","");
            List<Boolean> target = CharArrayUtils.toStream(lightsString.toCharArray())
                .map(c -> {
                    return switch (c){
                        case '#' -> true;
                        default -> false;
                    };
                })
                .collect(Collectors.toList());
            inputs = inputs.subList(1, inputs.size() - 1);
            List<List<Boolean>> buttons = inputs.stream()
                .map(buttonString -> parseButton(target.size(), buttonString))
                .collect(Collectors.toList());
                
            joltageString = joltageString.replaceAll("[\\{\\}]","");
            List<Integer> joltages = InputParserFactory.getIntegerCSVParser().parseInput(List.of(joltageString)).getFirst();
            return new Machine(target, buttons, joltages);
        }

        private static List<Boolean> parseButton(int lightsSize, String buttonString) {
            List<Boolean> allOptions = Stream.generate(() -> false).limit(lightsSize).collect(Collectors.toList());
            buttonString = buttonString.replaceAll("[\\(\\),]","");
            CharArrayUtils.toStream(buttonString.toCharArray())
                .map(String::valueOf)
                .map(Integer::parseInt)
                .forEach(i -> allOptions.set(i, true));
            return allOptions;
        }
    }

    @SuppressWarnings("unchecked")
    private long minPressesForJoltages(Machine machine) {
        int n = machine.joltages().size();
        int m = machine.buttons().size();
        int[] b = machine.joltages().stream().mapToInt(Integer::intValue).toArray();
        int[][] a = new int[n][m];
        for (int j = 0; j < m; j++) {
            List<Boolean> button = machine.buttons().get(j);
            for (int i = 0; i < n; i++) {
                a[i][j] = button.get(i) ? 1 : 0;
            }
        }

        long[] ub = computeUpperBounds(a, b);

        try (Context ctx = new Context()) {
            Optimize opt = ctx.mkOptimize();
            IntExpr[] x = new IntExpr[m];
            for (int j = 0; j < m; j++) {
                x[j] = ctx.mkIntConst("x" + j);
                opt.Add(ctx.mkGe(x[j], ctx.mkInt(0)));
                if (ub[j] != Long.MAX_VALUE) {
                    opt.Add(ctx.mkLe(x[j], ctx.mkInt((int) ub[j])));
                }
            }

            for (int i = 0; i < n; i++) {
                ArithExpr<IntSort> sum = ctx.mkInt(0);
                for (int j = 0; j < m; j++) {
                    if (a[i][j] == 1) {
                        sum = ctx.mkAdd(sum, x[j]);
                    }
                }
                opt.Add(ctx.mkEq(sum, ctx.mkInt(b[i])));
            }

            ArithExpr<IntSort> total = ctx.mkInt(0);
            for (int j = 0; j < m; j++) {
                total = ctx.mkAdd(total, x[j]);
            }
            opt.MkMinimize(total);

            Status status = opt.Check();
            if (status != Status.SATISFIABLE) {
                throw new IllegalStateException("Z3 returned: " + status);
            }
            Model model = opt.getModel();
            long presses = 0;
            for (int j = 0; j < m; j++) {
                IntNum val = (IntNum) model.evaluate(x[j], false);
                presses += val.getInt64();
            }
            return presses;
        }
    }

    private static long[] computeUpperBounds(int[][] a, int[] b) {
        int n = a.length;
        int m = a[0].length;
        long[] ub = new long[m];
        Arrays.fill(ub, Long.MAX_VALUE);
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                if (a[i][j] == 1) {
                    ub[j] = Math.min(ub[j], b[i]);
                }
            }
            if (ub[j] == Long.MAX_VALUE) {
                ub[j] = 0;
            }
        }
        return ub;
    }
}
