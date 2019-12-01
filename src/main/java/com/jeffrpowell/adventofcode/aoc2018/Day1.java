package com.jeffrpowell.adventofcode.aoc2018;

import com.jeffrpowell.adventofcode.Solution;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day1 implements Solution<Integer>
{
    @Override
    public String part1(List<Integer> input) {
        return Integer.toString(input.stream().reduce(0, Math::addExact));
    }

    @Override
    public String part2(List<Integer> input) {
        Set<Integer> visitedTotals = new HashSet<>();
        int total = 0;
        while(true) {
            for (Integer i : input) {
                total += i;
                boolean added = visitedTotals.add(total);
                if (!added) {
                    return Integer.toString(total);
                }
            }
        }
    }
}
