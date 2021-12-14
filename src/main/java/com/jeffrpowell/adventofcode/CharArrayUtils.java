package com.jeffrpowell.adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CharArrayUtils {
    private CharArrayUtils(){}

    public static List<Character> toList(char[] arr) {
        List<Character> list = new ArrayList<>();
        for (Character c : arr) {
            list.add(c);            
        }
        return list;
    }

    public static Stream<Character> toStream(char[] arr) {
        return toList(arr).stream();
    }
}
