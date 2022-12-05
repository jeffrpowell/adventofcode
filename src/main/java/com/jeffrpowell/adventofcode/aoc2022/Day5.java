package com.jeffrpowell.adventofcode.aoc2022;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day5 extends Solution2022<Section>{

    @Override
    public int getDay() {
        return 5;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, false));
    }

    @Override
    protected String part1(List<Section> input) {
        CargoShip c = new CargoShip(input.get(0));
        //c.printShip();
        c.moveCargo(input.get(1));
        //c.printShip();
        return c.readTopOfStacks();
    }

    @Override
    protected String part2(List<Section> input) {
        CargoShip c = new CargoShip(input.get(0));
        //c.printShip();
        c.moveCargo2(input.get(1));
        //c.printShip();
        return c.readTopOfStacks();
    }

    private static class CargoShip {
        private static final int STACK_COUNT = 9;
        Deque<Character>[] stacks;

        public CargoShip(Section input) {
            this.stacks = Stream.generate(() -> new ArrayDeque<Character>()).limit(STACK_COUNT).collect(Collectors.toList()).toArray(new ArrayDeque[]{});
            for (String line : input.getInput(InputParserFactory.getStringParser())) {
                if (line.charAt(1) == '1') {
                    break;
                }
                int stackNum = 0;
                for (int i = 1; i < line.length(); i+=4) {
                    if (line.charAt(i) != ' ') {
                        this.stacks[stackNum].offerLast(line.charAt(i));
                    }
                    stackNum++;
                }
            }
        }

        public void moveCargo(Section input) {
            List<Rule> moves = input.getInput(InputParserFactory.getRuleParser("\n",Pattern.compile("move (\\d+) from (\\d+) to (\\d+)")));
            for (Rule move : moves) {
                int count = move.getInt(0);
                int source = move.getInt(1) - 1;
                int dest = move.getInt(2) - 1;
                for (int i = 0; i < count; i++) {
                    if (!stacks[source].isEmpty()){
                        stacks[dest].push(stacks[source].pop());
                    }
                }
            }
        }

        public void moveCargo2(Section input) {
            List<Rule> moves = input.getInput(InputParserFactory.getRuleParser("\n",Pattern.compile("move (\\d+) from (\\d+) to (\\d+)")));
            for (Rule move : moves) {
                int count = move.getInt(0);
                int source = move.getInt(1) - 1;
                int dest = move.getInt(2) - 1;
                Deque<Character> temp = new ArrayDeque<>();
                for (int i = 0; i < count; i++) {
                    if (!stacks[source].isEmpty()){
                        temp.push(stacks[source].pop());
                    }
                }
                for (int i = 0; i < count; i++) {
                    if (!temp.isEmpty()){
                        stacks[dest].push(temp.pop());
                    }
                }
            }
        }

        public String readTopOfStacks() {
            return Arrays.stream(stacks)
                .map((Deque<Character> s) -> s.peek())
                .map(s -> s.toString())
                .collect(Collectors.joining(""));
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < stacks.length; i++) {
                b.append(i + 1).append(" ");
                b.append(stacks[i].stream().map(s -> s.toString()).collect(Collectors.joining(" ")));
                b.append("\n");
            }
            return b.toString();
        }

        public void printShip() {
            System.out.println(toString());
        }
    }
}
