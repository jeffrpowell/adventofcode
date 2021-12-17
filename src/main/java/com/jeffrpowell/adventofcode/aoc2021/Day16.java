package com.jeffrpowell.adventofcode.aoc2021;

import java.util.BitSet;
import java.util.List;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day16 extends Solution2021<List<String>>{

    @Override
    public int getDay() {
        return 16;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        List<String> line = input.get(0);
        byte[] byteArr = new byte[line.size() / 2];
        for (int i = 0; i < line.size() - 1; i += 2) {
            byteArr[i / 2] = Byte.parseByte(line.get(i) + line.get(i + 1));
        }
        BitSet bits = BitSet.valueOf(byteArr);
        State s = new State();
        parseNextToken(bits, s);
        return Integer.toString(s.versionCounter);
    }

    private void parseNextToken(BitSet remaining, State s) {

    }

    @Override
    protected String part2(List<List<String>> input) {
        // TODO Auto-generated method stub
        return null;
    }

    private static class State {
        int versionCounter = 0;
    }
    
}
