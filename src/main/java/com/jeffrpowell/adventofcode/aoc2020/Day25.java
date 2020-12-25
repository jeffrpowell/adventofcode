package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day25 extends Solution2020<Integer>{

    @Override
    public int getDay() {
        return 25;
    }

    @Override
    public InputParser<Integer> getInputParser() {
        return InputParserFactory.getIntegerParser();
    }

    @Override
    protected String part1(List<Integer> input) {
        Set<Long> publicKeys = input.stream().map(Long::new).collect(Collectors.toSet());
        int subject = 7;
        long value = 1;
        int smallestLoopSize = 0;
        while(!publicKeys.contains(value)) {
            value *= subject;
            value = value % 20201227;
            smallestLoopSize++;
        }
        final long finalValue = value;
        long otherPublicKey = publicKeys.stream().filter(key -> key != finalValue).findAny().get();
        value = 1;
        for (int i = 0; i < smallestLoopSize; i++) {
            value *= otherPublicKey;
            value = value % 20201227;
        }
        return Long.toString(value);
    }

    @Override
    protected String part2(List<Integer> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
