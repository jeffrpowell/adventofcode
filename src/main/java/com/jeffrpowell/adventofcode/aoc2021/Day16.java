package com.jeffrpowell.adventofcode.aoc2021;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day16 extends Solution2021<String>{

    @Override
    public int getDay() {
        return 16;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }

    @Override
    protected String part1(List<String> input) {
        List<Boolean> bits = hexToBits(input.get(0));
        State s = new State();
        do {
            bits = parseNextToken(bits, s);
        }
        while (notTheEndYet(bits));
        return Long.toString(s.versionCounter);
    }

    @Override
    protected String part2(List<String> input) {
        List<Boolean> bits = hexToBits(input.get(0));
        State s = new State();
        do {
            bits = parseNextToken(bits, s);
        }
        while (notTheEndYet(bits));
        return Long.toString(s.literals.pop());
    }


    private boolean notTheEndYet(List<Boolean> bits) {
        return !bits.isEmpty() && bits.size() < 16 && bits.stream().filter(b -> b).count() > 0;
    }

    private List<Boolean> parseNextToken(List<Boolean> remaining, State s) {
        long version = bitsToLong(remaining.subList(0, 3));
        long type = bitsToLong(remaining.subList(3, 6));
        s.versionCounter += version;
        if (type == 4) {
            remaining = parseLiteral(remaining.subList(6, remaining.size()), s);
        }
        else {
            long lengthTypeId = bitsToLong(remaining.subList(6, 7));
            if (lengthTypeId == 0) {
                remaining = parseOperator0(remaining.subList(7, remaining.size()), type, s);
            }
            else {
                remaining = parseOperator1(remaining.subList(7, remaining.size()), type, s);
            }
        }
        return remaining;
    }

    List<Boolean> parseLiteral(List<Boolean> remaining, State s) {
        long val = 0;
        for (int i = 0; i < remaining.size(); i += 5) {
            val = val << 4;
            val += bitsToLong(remaining.subList(i + 1, i + 5));
            if (!remaining.get(i)) {
                s.literals.push(val);
                return remaining.subList(i + 5, remaining.size());
            }
        }
        s.literals.push(val);
        return new ArrayList<>();
    }

    //15 bits, length of all sub-packets
    List<Boolean> parseOperator0(List<Boolean> remaining, long typeId, State s) {
        int length = Long.valueOf(bitsToLong(remaining.subList(0, 15))).intValue();
        remaining = remaining.subList(15, remaining.size());
        int originalSize = remaining.size();
        List<Long> literals = new ArrayList<>();
        while(remaining.size() != originalSize - length) {
            remaining = parseNextToken(remaining, s);
            literals.add(s.literals.pop());
        }
        s.literals.push(calc(typeId, literals));
        return remaining;
    }

    //11 bits, number of sub-packets
    List<Boolean> parseOperator1(List<Boolean> remaining, long typeId, State s) {
        long numSubpackets = bitsToLong(remaining.subList(0, 11));
        remaining = remaining.subList(11, remaining.size());
        List<Long> literals = new ArrayList<>();
        for (; numSubpackets > 0; numSubpackets--) {
            remaining = parseNextToken(remaining, s);
            literals.add(s.literals.pop());
        }
        s.literals.push(calc(typeId, literals));
        return remaining;
    }

    public static List<Boolean> hexToBits(String hex) {
        String leftPad = getLeftPad(hex.substring(0, 1));
        return stringToBits(leftPad + new BigInteger(hex, 16).toString(2)).collect(Collectors.toList());
    }

    public static String getLeftPad(String firstHex) {
        String autoString = new BigInteger(firstHex, 16).toString(2);
        if (autoString.length() < 4) {
            return Stream.generate(() -> "0").limit(4 - autoString.length()).collect(Collectors.joining());
        }
        return "";
    }

    public static Stream<Boolean> stringToBits(String s) {
        return s.chars().mapToObj(c -> c == '1');
    }

    public static long bitsToLong(List<Boolean> bits) {
        long value = 0L;
        for (int i = 0; i < bits.size(); ++i) {
            value += bits.get(i) ? (1L << (bits.size() - i - 1)) : 0L;
        }
        return value;
    }

    private static long calc(long type, List<Long> literals) {
        return switch (Long.valueOf(type).intValue()) {
            case 0 -> literals.stream().reduce(0L, Math::addExact);
            case 1 -> literals.stream().reduce(1L, Math::multiplyExact);
            case 2 -> literals.stream().min(Comparator.naturalOrder()).get();
            case 3 -> literals.stream().max(Comparator.naturalOrder()).get();
            case 5 -> literals.get(0) > literals.get(1) ? 1 : 0;
            case 6 -> literals.get(0) < literals.get(1) ? 1 : 0;
            case 7 -> literals.get(0) == literals.get(1) ? 1 : 0;
            default -> literals.get(0);
        };
    }
    public static class State {
        long versionCounter = 0;
        Deque<Long> literals = new ArrayDeque<>();
    }
    
}
