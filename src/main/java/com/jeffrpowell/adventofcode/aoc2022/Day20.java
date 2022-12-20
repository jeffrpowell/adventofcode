package com.jeffrpowell.adventofcode.aoc2022;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day20 extends Solution2022<Integer>{

    @Override
    public int getDay() {
        return 20;
    }

    @Override
    public InputParser<Integer> getInputParser() {
        return InputParserFactory.getIntegerParser();
    }

    @Override
    protected String part1(List<Integer> input) {
        List<Mover> copy = input.stream().map(n -> new Mover(n, false)).collect(Collectors.toList());
        int size = input.size();
        for (int nums = 0; nums < size; nums++) {
            for (int i = 0; i < size; i++) {
                Mover m = copy.get(i);
                if (m.moved()) continue;
                int newIndex = i + m.num();
                if (m.num() < 0 && newIndex < 0) {
                    newIndex = size - (Math.abs(newIndex) % size) - 1;
                }
                else if (m.num() >= 0 && newIndex >= size) {
                    newIndex %= size;
                }
                copy.remove(i);
                copy.add(newIndex, new Mover(m.num(), true));
                break;
            }
        }
        List<Integer> newList = copy.stream()
            .map(Mover::num)
            .collect(Collectors.toList());
        int start = newList.indexOf(0);
        int x = (start + 1000) % size;
        int y = (x + 1000) % size;
        int z = (y + 1000) % size;
        return Integer.toString(newList.get(x) + newList.get(y) + newList.get(z));
    }
    record Mover(int num, boolean moved){}

    private String attempt1(List<Integer> input){
        Map<Key, Index> indices = new HashMap<>();
        int size = input.size();
        for (int i = 0; i < size; i++) {
            int num = input.get(i);
            int newIndex = i;
            if (num < 0) {
                int vector = num < -size ? -(Math.abs(num) % size) : num;
                newIndex += vector;
                if (newIndex < 0) {
                    newIndex = size + newIndex;
                }
            }
            else {
                int vector = num > size ? num % size : num;
                newIndex += vector;
                if (newIndex >= size) {
                    newIndex %= size;
                }
            }
            indices.put(new Key(input.get(i), i), new Index(i, newIndex));
        }
        List<Integer> newList = indices.entrySet().stream()
            .sorted((e1, e2) -> {
                if (e1.getValue().newi() != e2.getValue().newi()) {
                    return Integer.compare(e1.getValue().newi(), e2.getValue().newi());
                }
                return -Integer.compare(e1.getValue().original(), e2.getValue().original());
            })
            .map(Map.Entry::getKey)
            .map(Key::num)
            .collect(Collectors.toList());
        int start = newList.indexOf(0);
        int x = (start + 1000) % size;
        int y = (x + 1000) % size;
        int z = (y + 1000) % size;
        return Integer.toString(newList.get(x) + newList.get(y) + newList.get(z));
    }
    record Key(int num, int i){}
    record Index(int original, int newi){}

    @Override
    protected String part2(List<Integer> input) {
        return null;
    }

}
