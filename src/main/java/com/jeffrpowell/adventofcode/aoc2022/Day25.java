package com.jeffrpowell.adventofcode.aoc2022;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day25 extends Solution2022<List<String>>{

    private static final Map<String, Integer> digitMap = Map.of(
        "2", 2,
        "1", 1,
        "0", 0,
        "-", -1,
        "=", -2
    );

    private static final Map<String, String> nextSnafuDown = Map.of(
        "2", "1",
        "1", "0",
        "0", "-",
        "-", "=",
        "=", "2"
    );

    @Override
    public int getDay() {
        return 25;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }

    @Override
    protected String part1(List<List<String>> input) {
        return longToSnafu2(input.stream().map(this::snafuToLong)
            .reduce(0L, Math::addExact));
    }

    private long snafuToLong(List<String> snafu) {
        List<Integer> digits = snafu.stream().map(digitMap::get).collect(Collectors.toList());
        long sum = 0;
        long place = 1;
        for (int i = digits.size() - 1; i >= 0; i--) {
            sum += digits.get(i) * place;
            place *= 5;
        }
        return sum;
    }

    private String longToSnafu(Long lng) {
        Deque<Double> maxRanges = new ArrayDeque<>();
        double maxRange = 2;
        long place = 1;
        while (maxRange < lng) {
            maxRanges.push(maxRange);      
            place++;
            maxRange += 2.0 * Math.pow(5.0, place - 1.0); 
        }
        long test = Double.valueOf(maxRange).longValue();
        return null;
    }

    private String longToSnafu2(Long lng) {
        //lng = 32,405,707,664,897
        long i = 32_409_667_968_750L; //2=222000000000000000
        List<String> snafu = Stream.generate(() -> "0").limit(15).collect(Collectors.toList());
        snafu.add(0, "2");
        snafu.add(0, "2");
        snafu.add(0, "2");
        snafu.add(0, "=");
        snafu.add(0, "2");
        int onesPlace = snafu.size() - 1;
        int placeToChange = onesPlace;
        while (i != lng) {
            i--;
            String nextDigitDown = nextSnafuDown.get(snafu.get(placeToChange));
            snafu.set(placeToChange, nextDigitDown);
            while (nextDigitDown.equals("2")) {
                placeToChange--;
                nextDigitDown = nextSnafuDown.get(snafu.get(placeToChange));
                snafu.set(placeToChange, nextDigitDown);
            }
            if (i == lng) {
                return snafu.stream().collect(Collectors.joining());
            }
            else {
                placeToChange = onesPlace;
            }
        }
        return null;
    }

    @Override
    protected String part2(List<List<String>> input) {
        return null;
    }

}
