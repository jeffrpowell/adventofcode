package com.jeffrpowell.adventofcode.aoc2025;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day2 extends Solution2025<Rule>{
    Set<Long> invalids = new HashSet<>();

    @Override
    public int getDay() {
        return 2;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser(",", Pattern.compile("(\\d+)-(\\d+)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        return String.valueOf(input.stream()
            .mapToLong(this::sumRepeatsPart1)
            .sum());
    }

    @Override
    protected String part2(List<Rule> input) {
        input.stream()
            .mapToLong(this::sumRepeatsPart2)
            .sum();
        return String.valueOf(input.stream()
            .mapToLong(this::sumRepeatsPart2_attempt2)
            .sum());
    }

    /**
     * Only even-digit count can be split into two repeating halves
     * @param i
     * @return
     */
    private boolean canContainRepeat(String i) {
        return (i.length() % 2) == 0;
    }

    private long sumRepeatsPart1(Rule r) {
        String firstDigits = r.getString(0);
        String secondDigits = r.getString(1);
        long first = Long.parseLong(firstDigits);
        long second = Long.parseLong(secondDigits);
        boolean firstCanContainRepeat = canContainRepeat(firstDigits);
        boolean secondCanContainRepeat = canContainRepeat(secondDigits);
        if (!firstCanContainRepeat && !secondCanContainRepeat) {
            return 0;
        }
        // If first can't repeat, round first up to next power of 10
        if (!firstCanContainRepeat) {
            long nextPowerOf10 = (long) Math.pow(10, firstDigits.length());
            first = nextPowerOf10;
        }
        // If second can't repeat, round down to 1 - current power of 10
        if (!secondCanContainRepeat) {  
            long currentPowerOf10 = (long) Math.pow(10, secondDigits.length() - 1);
            second = currentPowerOf10 - 1;
        }
        // Loop through range and sum repeats
        long sum = 0;
        for (long i = first; i <= second; i++) {
            String digits = String.valueOf(i);
            int halfLength = digits.length() / 2;
            String firstHalf = digits.substring(0, halfLength);
            String secondHalf = digits.substring(halfLength);
            if (firstHalf.equals(secondHalf)) {
                sum += i;
            }
        }
        return sum;
    }

    private long sumRepeatsPart2(Rule r) { //49356706806
        String firstDigits = r.getString(0);
        String secondDigits = r.getString(1);
        long first = Long.parseLong(firstDigits);
        long second = Long.parseLong(secondDigits);
        // Loop through range and sum repeats
        long sum = 0;
        for (long i = first; i <= second; i++) {
            if (isRepeatSequence(i)) {
                sum += i;
                invalids.add(i);
            }
        }
        return sum;
    }

    private boolean isRepeatSequence(long i) {
        String digits = String.valueOf(i);
        if (digits.length() == 1) {
            return false;
        }
        int repeatPoint = 0;
        int repeatPosition = 0;
        String repeatSequence = digits.substring(0, 1);
        for (int position = 1; position < digits.length(); position++) {
            char c = digits.charAt(position);
            if (c == repeatSequence.charAt(repeatPosition)) {
                repeatPosition++;
                if (repeatPosition >= repeatSequence.length()) {
                    repeatPosition = 0;
                }
            } else {
                // Mismatch, build new repeat sequence candidate
                repeatPoint = position;
                repeatSequence = digits.substring(0, repeatPoint + 1);
                repeatPosition = 0;
            }
        }
        boolean sequenceIsNumber = repeatPoint == digits.length() - 1;
        boolean midScanningSequence = repeatPosition != 0;
        return !sequenceIsNumber && !midScanningSequence;
    }

    private long sumRepeatsPart2_attempt2(Rule r) {
        String firstDigits = r.getString(0);
        String secondDigits = r.getString(1);
        long first = Long.parseLong(firstDigits);
        long second = Long.parseLong(secondDigits);
        Range range = new Range(first, second);
        // try all patterns of a length 1..maxDigits
        int maxDigits = secondDigits.length() / 2;
        // we might try the same number multiple times, e.g. pattern "3" * 4 = "3333" which is the same as
        // "33" * 2 = "3333". So just use a set.
        Set<Long> hits = new TreeSet<>();
        for (int nDigits = 1; nDigits <= maxDigits; nDigits++) {
            long mult = (long) Math.pow(10, nDigits);
            long firstPat = (long) Math.pow(10, nDigits - 1);
            // try all patterns of length nDigits, e.g. for nDigits = 2: firstPat = 10 and mult = 100.
            for (long pattern = firstPat; pattern < mult; pattern++) {
                long cand = pattern; // start with pattern, because the puzzle says it must be repeated
                for (int i = 1; i < secondDigits.length() / nDigits; i++) {
                    // add pattern to the end only as long as the total length is less than the second number
                    cand = cand * mult + pattern;
                    if (range.contains(cand)) {
                        hits.add(cand);
                        if (!invalids.contains(cand)) {
                            System.out.println("Missing invalid: " + cand);
                        }
                    }
                }
            }
        }
        return hits.stream().reduce(0L, Long::sum);
    }

    private static class Range {
        long min;
        long max;

        public Range(long min, long max) {
            this.min = min;
            this.max = max;
        }

        public boolean contains(long value) {
            return value >= min && value <= max;
        }
    }
}
