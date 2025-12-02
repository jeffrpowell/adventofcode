package com.jeffrpowell.adventofcode.aoc2022;

import java.util.ArrayList;
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
        List<Mover> index = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            index.add(new Mover(input.get(i), i));
        }
        List<Mover> copy = index.stream().collect(Collectors.toList());
        mixList(index, copy);
        List<Long> newList = copy.stream()
            .map(Mover::num)
            .collect(Collectors.toList());
        int start = newList.indexOf(0L);
        int size = copy.size();
        int x = (start + 1000) % size;
        int y = (x + 1000) % size;
        int z = (y + 1000) % size;
        return Long.toString(newList.get(x) + newList.get(y) + newList.get(z));
    }
    record Mover(long num, int originalIndex){}

    @SuppressWarnings("unused")
    private void printList(List<Mover> copy) {
        for (int i = 0; i < copy.size(); i++) {
            Mover m = copy.get(i);
            System.out.println(i + ": " + m.num + " (" + m.originalIndex + ")");
        }
        System.out.println();
    }

    @Override
    protected String part2(List<Long> input) {
        List<Mover> index = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            index.add(new Mover(input.get(i) * DECRYPT_KEY, i));
        }
        List<Mover> copy = index.stream().collect(Collectors.toList());
        for (int i = 0; i < 10; i++) {
            mixList(index, copy);
        }
        List<Long> newList = copy.stream()
            .map(Mover::num)
            .collect(Collectors.toList());
        int start = newList.indexOf(0L);
        int size = copy.size();
        int x = (start + 1000) % size;
        int y = (x + 1000) % size;
        int z = (y + 1000) % size;
        return Long.toString(newList.get(x) + newList.get(y) + newList.get(z));
    }

    private void mixList(List<Mover> index, List<Mover> mover) {
        for (Mover mIndex : index) {
            int i = mover.indexOf(mIndex);
            Mover m = mover.remove(i);
            int size = mover.size();
            long newIndex = i + m.num();
            if (m.num() < 0L && newIndex < 1L) {
                newIndex = size - (Math.abs(newIndex) % size);
            }
            else if (m.num() >= 0L && newIndex >= size) {
                newIndex %= size;
            }
            mover.add(Long.valueOf(newIndex).intValue(), m);
        }
    }

}
