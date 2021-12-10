package com.jeffrpowell.adventofcode.aoc2021;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day10 extends Solution2021<List<String>>{

    static final Map<String, String> PAIRS = Map.of("(", ")", "<", ">", "[", "]", "{", "}");
    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        int score = 0;
        for (List<String> line : input) {
            Deque<String> stack = new ArrayDeque<>();
            for (String c : line) {
                if (isOpen(c)) {
                    stack.push(c);
                }
                else {
                    String pair = stack.pop();
                    if (!PAIRS.get(pair).equals(c)) {
                        score += score(c);
                    }
                }
            }
        }
        return Integer.toString(score);
    }

    private static boolean isOpen(String c) {
        return PAIRS.containsKey(c);
    }

    private static int score(String c) {
        return switch (c) {
            case ")" -> 3;
            case "]" -> 57;
            case "}" -> 1197;
            case ">" -> 25137;
            default -> 0;
        };
    }

    @Override
    protected String part2(List<List<String>> input) {
        List<Deque<String>> stacks = new ArrayList<>();
        for (List<String> line : input) {
            addStack(line, stacks);
        }

        return Long.toString(
            stacks.stream()
                .map(this::unwind)
                .map(this::score2)
                .sorted()
                .skip(stacks.size() / 2)
                .limit(1)
                .findFirst().get()
        );
    }

    private void addStack(List<String> line, List<Deque<String>> stacks) {
        Deque<String> stack = new ArrayDeque<>();
        for (String c : line) {
            if (isOpen(c)) {
                stack.push(c);
            }
            else {
                String pair = stack.pop();
                if (!PAIRS.get(pair).equals(c)) {
                    return;
                }
            }
        }
        stacks.add(stack);
    }

    private List<String> unwind(Deque<String> stack) {
        return stack.stream().map(PAIRS::get).collect(Collectors.toList());
    }

    private long score2(List<String> completion) {
        long score = 0;
        for (String c : completion) {
            score = switch (c) {
                case ")" -> score * 5 + 1;
                case "]" -> score * 5 + 2;
                case "}" -> score * 5 + 3;
                case ">" -> score * 5 + 4;
                default -> score;
            };
        }
        return score;
    }
    
}
