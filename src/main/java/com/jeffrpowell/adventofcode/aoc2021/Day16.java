package com.jeffrpowell.adventofcode.aoc2021;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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
        bits.remove(bits.size() - 1);
        bits.remove(bits.size() - 1);
        bits.remove(bits.size() - 1);
        State s = new State();
        do {
            bits = parseNextToken(bits, s);
        }
        while (!bits.isEmpty());
        return Integer.toString(s.versionCounter);
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
                remaining = parseOperator0(remaining.subList(7, remaining.size()), s);
            }
            else {
                remaining = parseOperator1(remaining.subList(7, remaining.size()), s);
            }
        }
        return remaining;
    }

    List<Boolean> parseLiteral(List<Boolean> remaining, State s) {
        for (int i = 0; i < remaining.size(); i += 5) {
            if (!remaining.get(i)) {
                return remaining.subList(i + 5, remaining.size());
            }
        }
        return new ArrayList<>();
    }

    //15 bits, length of all sub-packets
    List<Boolean> parseOperator0(List<Boolean> remaining, State s) {
        int length = Long.valueOf(bitsToLong(remaining.subList(0, 15))).intValue();
        int originalSize = remaining.size();
        remaining = remaining.subList(15, remaining.size());
        while(remaining.size() != originalSize - length) {
            remaining = parseNextToken(remaining, s);
        }
        return remaining;
    }

    //11 bits, number of sub-packets
    List<Boolean> parseOperator1(List<Boolean> remaining, State s) {
        long numSubpackets = bitsToLong(remaining.subList(0, 11));
        remaining = remaining.subList(11, remaining.size());
        for (; numSubpackets >= 0; numSubpackets--) {
            remaining = parseNextToken(remaining, s);
        }
        return remaining;
    }

    public static List<Boolean> hexToBits(String hex) {
        long[] bits = new long[hex.length() / 16 + 1];
        for(int i = 0; i < hex.length(); i += 16) {
            bits[i/16] = new BigInteger(hex.substring(i, Math.min(i+16, hex.length())), 16).longValue();
        }
        return Arrays.stream(bits).mapToObj(Long::toBinaryString).flatMap(Day16::stringToBits).collect(Collectors.toList());
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

    @Override
    protected String part2(List<String> input) {
        // TODO Auto-generated method stub
        return null;
    }

    public static class State {
        int versionCounter = 0;
    }
    
}
