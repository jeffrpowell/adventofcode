package com.jeffrpowell.adventofcode.aoc2015;

import java.util.List;

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
        int targetWeight = input.stream().reduce(0, Math::addExact) / 3;
        return Long.toString(findMinQuantumEntanglementForTargetWeight(input, targetWeight));
    }


    @Override
    protected String part2(List<Integer> input) {
        int targetWeight = input.stream().reduce(0, Math::addExact) / 4;
        return Long.toString(findMinQuantumEntanglementForTargetWeight(input, targetWeight));
    }

    private long findMinQuantumEntanglementForTargetWeight(List<Integer> input, int targetWeight) {
        List<Integer> weightsDesc = input.stream()
            .sorted((a, b) -> Integer.compare(b, a))
            .toList();
        int n = weightsDesc.size();
        int[] suffixSums = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            suffixSums[i] = Math.addExact(suffixSums[i + 1], weightsDesc.get(i));
        }

        for (int groupSize = 1; groupSize <= n; groupSize++) {
            long[] bestQE = new long[] { Long.MAX_VALUE };
            searchMinQE(weightsDesc, suffixSums, 0, groupSize, 0, targetWeight, 1L, bestQE);
            if (bestQE[0] != Long.MAX_VALUE) {
                return bestQE[0];
            }
        }
        return -1;
    }

    private void searchMinQE(
        List<Integer> weightsDesc,
        int[] suffixSums,
        int index,
        int remainingCount,
        int currentSum,
        int targetWeight,
        long currentQE,
        long[] bestQE
    ) {
        if (currentSum > targetWeight) {
            return;
        }
        if (currentSum + suffixSums[index] < targetWeight) {
            return;
        }
        if (remainingCount == 0) {
            if (currentSum == targetWeight) {
                bestQE[0] = Math.min(bestQE[0], currentQE);
            }
            return;
        }
        if (index >= weightsDesc.size()) {
            return;
        }
        if (weightsDesc.size() - index < remainingCount) {
            return;
        }
        if (currentQE >= bestQE[0]) {
            return;
        }

        int w = weightsDesc.get(index);
        searchMinQE(
            weightsDesc,
            suffixSums,
            index + 1,
            remainingCount - 1,
            Math.addExact(currentSum, w),
            targetWeight,
            Math.multiplyExact(currentQE, (long) w),
            bestQE
        );
        searchMinQE(
            weightsDesc,
            suffixSums,
            index + 1,
            remainingCount,
            currentSum,
            targetWeight,
            currentQE,
            bestQE
        );
    }
}
