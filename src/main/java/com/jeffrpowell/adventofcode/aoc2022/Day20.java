package com.jeffrpowell.adventofcode.aoc2022;

import java.util.List;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day20 extends Solution2022<Long>{
    private static final long DECRYPT_KEY = 811589153L;
    @Override
    public int getDay() {
        return 20;
    }

    @Override
    public InputParser<Long> getInputParser() {
        return InputParserFactory.getLongParser();
    }

    @Override
    protected String part1(List<Long> input) {
        List<Mover> copy = input.stream().map(n -> new Mover(n, false)).collect(Collectors.toList());
        int size = input.size();
        for (int nums = 0; nums < size; nums++) {
            for (int i = 0; i < size; i++) {
                Mover m = copy.get(i);
                if (m.moved()) continue;
                long newIndex = i + m.num();
                if (m.num() < 0 && newIndex < 1) {
                    newIndex = size - (Math.abs(newIndex) % size) - 1;
                }
                else if (m.num() >= 0 && newIndex >= size - 1) {
                    newIndex %= size - 1;
                }
                copy.remove(i);
                copy.add(Long.valueOf(newIndex).intValue(), new Mover(m.num(), true));
                break;
            }
        }
        List<Long> newList = copy.stream()
            .map(Mover::num)
            .collect(Collectors.toList());
        int start = newList.indexOf(0L);
        int x = (start + 1000) % size;
        int y = (x + 1000) % size;
        int z = (y + 1000) % size;
        return Long.toString(newList.get(x) + newList.get(y) + newList.get(z));
    }
    record Mover(long num, boolean moved){}

    private void printList(List<Mover> copy) {
        for (int i = 0; i < copy.size(); i++) {
            Mover m = copy.get(i);
            System.out.println(i + ": " + m.num + " (" + m.moved + ")");
        }
        System.out.println();
    }

    @Override
    protected String part2(List<Long> input) {
        return null;
    }

}
