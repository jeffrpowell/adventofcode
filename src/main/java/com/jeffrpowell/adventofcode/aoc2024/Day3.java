package com.jeffrpowell.adventofcode.aoc2024;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day3 extends Solution2024<String>{
    private static final Pattern MUL_REGEX = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
    private static final Pattern DO_REGEX = Pattern.compile("do\\(\\)");
    private static final Pattern DONT_REGEX = Pattern.compile("don't\\(\\)");

    @Override
    public int getDay() {
        return 3;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }

    @Override
    protected String part1(List<String> input) {
        long sum = 0;
        for (String line : input) {
            Matcher m = MUL_REGEX.matcher(line);
            while(m.find()) {
                sum += Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2));
            }
        }
        return Long.toString(sum);
    }
    

    @Override
    protected String part2(List<String> input) {
        Map<Integer, Boolean> enabledState = new HashMap<>();
        enabledState.put(0, true);
        String combinedInput = input.stream().collect(Collectors.joining("#"));
        Matcher mDo = DO_REGEX.matcher(combinedInput);
        int highestIndex = 0;
        while(mDo.find()) {
            enabledState.put(mDo.end(), true);
            highestIndex = mDo.end();
        }
        Matcher mDont = DONT_REGEX.matcher(combinedInput);
        while(mDont.find()) {
            enabledState.put(mDont.end(), false);
            if (mDont.end() > highestIndex) {
                highestIndex = mDont.end();
            }
        }
        boolean enabled = true;
        for (int i = 0; i < highestIndex + 600; i++) { //manually scanned input, found last do/dont and counted chars to the end of the line
            if (enabledState.containsKey(i)) {
                enabled = enabledState.get(i);
                continue;
            }
            enabledState.put(i, enabled);
        }
        long sum = 0;
        Matcher m = MUL_REGEX.matcher(combinedInput);
        while(m.find()) {
            if (Boolean.TRUE.equals(enabledState.get(m.start()))) {
                sum += Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2));
            }
        }
        return Long.toString(sum);
    }
}
