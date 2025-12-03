package com.jeffrpowell.adventofcode.algorithms;

import java.util.ArrayList;
import java.util.List;

public class PowerSets {
    public static <T> List<List<T>> getPowerSet(List<T> items) {
        return generate(items, 0);
    }

    private static <T> List<List<T>> generate(List<T> items, int index) {
        // Base case: if we've considered all items, return a list with one subset — the empty set
        if (index == items.size()) {
            List<List<T>> emptySetList = new ArrayList<>();
            emptySetList.add(new ArrayList<>()); // empty set
            return emptySetList;
        }

        T currentItem = items.get(index);

        // Recursively get all subsets for remaining items
        List<List<T>> subsetsWithoutCurrent = generate(items, index + 1);

        // Now create subsets that include the current item
        List<List<T>> subsetsWithCurrent = new ArrayList<>();
        for (List<T> subset : subsetsWithoutCurrent) {
            List<T> newSubset = new ArrayList<>(subset);
            newSubset.add(currentItem);
            subsetsWithCurrent.add(newSubset);
        }

        // Combine subsets without current and subsets with current
        List<List<T>> allSubsets = new ArrayList<>(subsetsWithoutCurrent);
        allSubsets.addAll(subsetsWithCurrent);
        return allSubsets;
    }
}
