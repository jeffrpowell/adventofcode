package com.jeffrpowell.adventofcode.aoc2022;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day10 extends Solution2022<Rule>{
    private static final String LINE_NOOP = "LINE_NOOP";
    private static final String LINE_ADDX = "LINE_ADDX";

    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Map.of(
            LINE_NOOP, Pattern.compile("noop"),
            LINE_ADDX, Pattern.compile("addx (-?\\d+)")
        ));
    }

    @Override
    protected String part1(List<Rule> input) {
        int cycle = 0;
        int x = 1;
        int targetCycle = 20;
        int sumSignals = 0;
        for (Rule line : input) {
            switch (line.getRulePatternKey()) {
                case LINE_ADDX:
                    cycle += 2;
                    x += line.getInt(0);
                    break;
                case LINE_NOOP:
                default:
                    cycle++;
            }
            if (cycle >= targetCycle) {
                int signalStrength = x;
                if (line.getRulePatternKey().equals(LINE_ADDX)) {
                    signalStrength = x - line.getInt(0);
                }
                sumSignals += signalStrength * targetCycle;
                targetCycle += 40;
            }
            if (targetCycle > 220) {
                break;
            }
        }
        return Integer.toString(sumSignals);
    }

    @Override
    protected String part2(List<Rule> input) {
        int x = 1;
        List<List<Boolean>> crt = Stream.generate(
            () -> Stream.generate(() -> false).limit(40).collect(Collectors.toList())
        ).limit(6).collect(Collectors.toList());
        Iterator<Rule> lines = input.iterator();
        Command instruction = Command.init(lines.next());
        for (List<Boolean> crtRow : crt) {
            for (int cycle = 0; cycle < crtRow.size(); cycle++) {
                //During cycle
                if (cycle >= x - 1 && cycle <= x + 1){
                    crtRow.set(cycle, true);
                }
                //After cycle
                boolean swapCommand = instruction.countdown();
                if (swapCommand) {
                    x = instruction.newSignal(x);
                    if (lines.hasNext()) {
                        instruction = Command.init(lines.next());
                    }
                    else {
                        instruction = new Noop();
                    }
                }
            }
        }
        
        return crt.stream()
            .map(row -> row.stream()
                .map(pixel -> pixel ? "#" : ".")
                .collect(Collectors.joining()))
            .collect(Collectors.joining("\n"));
    }

    private static interface Command {
        public boolean countdown();
        public int newSignal(int oldSignal);

        public static Command init(Rule line) {
            if (line.getRulePatternKey().equals(LINE_NOOP)) {
                return new Noop();
            }
            else {
                return new Addx(line.getInt(0));
            }
        }
    }

    private static class Noop implements Command {

        @Override
        public boolean countdown() {
            return true;
        }

        @Override
        public int newSignal(int oldSignal) {
            return oldSignal;
        }

    }
    private static class Addx implements Command {
        private int cycleCounter = 2;
        private int signalModifier;

        
        public Addx(int signalModifier) {
            this.signalModifier = signalModifier;
        }

        @Override
        public boolean countdown() {
            cycleCounter--;
            return cycleCounter == 0;
        }

        @Override
        public int newSignal(int oldSignal) {
            return oldSignal + signalModifier;
        }

    }
}
