package com.jeffrpowell.adventofcode.aoc2015;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.jeffrpowell.adventofcode.algorithms.Permutations;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day24 extends Solution2015<Integer>{

    @Override
    public int getDay() {
        return 24;
    }

    @Override
    public InputParser<Integer> getInputParser() {
        return InputParserFactory.getIntegerParser();
    }

    @Override
    protected String part1(List<Integer> input) {
        List<Allocation> partitionGroups = permuteAndPartition(input);
        int smallestSetSize = partitionGroups.stream()
            .mapToInt(Allocation::smallestGroupSize)
            .min()
            .orElseThrow();
        return Long.toString(partitionGroups.stream()
            .filter(allocation -> allocation.smallestGroupSize() == smallestSetSize)
            .mapToLong(Allocation::quantumEntanglement)
            .min()
            .orElseThrow());
    }


    @Override
    protected String part2(List<Integer> input) {
        return null;
    }

    List<Allocation> permuteAndPartition(List<Integer> input) {
        List<List<Integer>> permutations = Permutations.getAllPermutations(input);
        List<Allocation> allPartitionGroups = new ArrayList<>();
        int numPackages = input.size();
        for (List<Integer> permutation : permutations) {
            // Find partitions
            for (int i = 1; i < numPackages - 2; i++) {
                for (int j = i; j < numPackages - 1; j++) {
                    List<Set<Integer>> grouping = new ArrayList<>();
                    grouping.add(Set.copyOf(permutation.subList(0, i)));
                    grouping.add(Set.copyOf(permutation.subList(i, j)));
                    grouping.add(Set.copyOf(permutation.subList(j, numPackages - 1)));
                    grouping.sort(Comparator.comparingInt(Set::size));
                    if (grouping.stream()
                        .mapToInt(s -> s.stream().reduce(0, Math::addExact))
                        .distinct() //If there is only one distinct sum, all groups are equal
                        .count() == 1) {
                            allPartitionGroups.add(new Allocation(grouping, calculateQuantumEntanglement(grouping)));
                    }
                }
            }
        }
        return allPartitionGroups;
    }

    private long calculateQuantumEntanglement(List<Set<Integer>> groups) {
        int smallestSize = groups.stream()
            .mapToInt(Set::size)
            .min()
            .orElseThrow();
        return groups.stream()
            .filter(set -> set.size() == smallestSize)
            .mapToInt(set -> set.stream().reduce(1, Math::multiplyExact))
            .min()
            .orElseThrow();
    }

    record Allocation(List<Set<Integer>> groups, long quantumEntanglement) {
        public int smallestGroupSize() {
            return groups.stream().mapToInt(Set::size).min().orElseThrow();
        }
    }
}
