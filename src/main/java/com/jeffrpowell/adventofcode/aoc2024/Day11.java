package com.jeffrpowell.adventofcode.aoc2024;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day11 extends Solution2024<List<Long>>{

    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public InputParser<List<Long>> getInputParser() {
        return InputParserFactory.getLongTokenSVParser(" ");
    }

    @Override
    protected String part1(List<List<Long>> input) {
        Map<Long, List<Long>> cache = new HashMap<>();
        List<Long> stones = input.get(0);
        for (int i = 0; i < 25; i++) {
            stones = stones.stream()
                .map(stone -> cache.computeIfAbsent(stone, this::blink))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        }
        return Integer.toString(stones.size());
    }

    private List<Long> blink(Long stone) {
        if (stone.equals(0L)) {
            return List.of(1L);
        }
        else {
            String stoneStr = stone.toString();
            if (stoneStr.length() % 2 == 0) {
                return List.of(
                    Long.parseLong(stoneStr.substring(0, stoneStr.length() / 2)),
                    Long.parseLong(stoneStr.substring(stoneStr.length() / 2, stoneStr.length()))
                );
            }
            else {
                return List.of(stone * 2024);
            }
        }
    }

    @Override
    protected String part2(List<List<Long>> input) {
        Map<Long, List<Long>> cache = new HashMap<>();
        Map<Long, Long> stones = input.get(0).stream()
            .collect(Collectors.toMap(
                Function.identity(),
                l -> 1L,
                Math::addExact
            ));
        for (int i = 0; i < 75; i++) {
            Map<Long, Long> nextStoneLineup = new HashMap<>();
            for (Map.Entry<Long, Long> entry : stones.entrySet()) {
                List<Long> nextStones = cache.computeIfAbsent(entry.getKey(), this::blink);
                for (Long stone : nextStones) {
                    nextStoneLineup.put(stone, nextStoneLineup.computeIfAbsent(stone, k -> 0L) + entry.getValue());
                }
            }
            stones = nextStoneLineup;
        }
        return Long.toString(stones.values().stream().reduce(0L, Math::addExact));
    }
}
