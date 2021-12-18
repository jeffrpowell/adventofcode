package com.jeffrpowell.adventofcode.aoc2021;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;
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
        List<Boolean> bits = hexToBitSet(input.get(0));
        State s = new State();
        parseNextToken(bits, s);
        return Integer.toString(s.versionCounter);
    }

    private void parseNextToken(List<Boolean> remaining, State s) {
        long version = bitsToLong(remaining.subList(0, 3));
        long type = bitsToLong(remaining.subList(3, 6));
    }

    private static List<Boolean> hexToBitSet(String hex) {
        long[] bits = new long[hex.length() / 16 + 1];
        for(int i = 0; i < hex.length(); i += 16) {
            bits[i/16] = new BigInteger(hex.substring(i, Math.min(i+16, hex.length())), 16).longValue();
        }
        return Arrays.stream(bits).mapToObj(Long::toBinaryString).flatMap(Day16::stringToBits).collect(Collectors.toList());
    }

    private static Stream<Boolean> stringToBits(String s) {
        return s.chars().mapToObj(c -> c == '1');
    }

    private static long bitsToLong(List<Boolean> bits) {
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

    private static class State {
        int versionCounter = 0;
    }
    
}
