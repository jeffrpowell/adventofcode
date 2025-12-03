package com.jeffrpowell.adventofcode.algorithms;

import java.util.ArrayList;
import java.util.List;

public class Permutations {
    public static <T> List<List<T>> getAllPermutations(List<T> items) {
        List<List<T>> result = new ArrayList<>();
        if (items == null || items.isEmpty()) {
            result.add(new ArrayList<>());
            return result;
        }
        permute(new ArrayList<>(items), 0, result);
        return result;
    }

    private static <T> void permute(List<T> items, int start, List<List<T>> result) {
        if (start == items.size()) {
            result.add(new ArrayList<>(items));
            return;
        }
        for (int i = start; i < items.size(); i++) {
            swap(items, start, i);
            permute(items, start + 1, result);
            swap(items, start, i); // backtrack
        }
    }

    private static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    public static <T> List<T[]> getAllPermutations(T[] items) {
        List<T[]> result = new ArrayList<>();
        if (items == null) {
            return null;
        }
        if (items.length == 0) {
            result.add(items);
            return result;
        }
        T[] working = items.clone();
        permute(working, 0, result);
        return result;
    }

    private static <T> void permute(T[] items, int start, List<T[]> result) {
        if (start == items.length) {
            result.add(items.clone());
            return;
        }
        for (int i = start; i < items.length; i++) {
            swap(items, start, i);
            permute(items, start + 1, result);
            swap(items, start, i); // backtrack
        }
    }

    private static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
