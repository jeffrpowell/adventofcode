package com.jeffrpowell.adventofcode.aoc2015;

import java.util.ArrayList;
import java.util.List;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day11 extends Solution2015<List<String>>{
    private static final int Z_INT = 'z' - 'a';

    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser("");
    }
    
    @Override
    protected String part1(List<List<String>> input) {
        List<Integer> passwordInts = convertPasswordToInts(input.get(0));
        return incrementPassword(passwordInts);
    }
    
    @Override
    protected String part2(List<List<String>> input) {
        List<Integer> passwordInts = convertPasswordToInts(input.get(0));
        incrementPassword(passwordInts);
        return incrementPassword(passwordInts);
    }

    private List<Integer> convertPasswordToInts(List<String> password) {
        List<Integer> passwordInts = new ArrayList<>();
        for (String passwordChar: password) {
            passwordInts.add(convertCharToInt(passwordChar));
        }
        return passwordInts;
    }

    private int convertCharToInt(String passwordChar) {
        return (int)passwordChar.charAt(0) - (int)'a';
    }

    private String convertIntToChar(int passwordInt) {
        return Character.toString((int)'a' + passwordInt);
    }

    private String incrementPassword(List<Integer> passwordInts) {
        while (true) {
            // Increment password
            int carry = 1;
            for (int i = passwordInts.size() - 1; i >= 0; i--) {
                int newValue = passwordInts.get(i) + carry;
                if (newValue > Z_INT) {
                    passwordInts.set(i, 0);
                    carry = 1;
                } else {
                    passwordInts.set(i, newValue);
                    carry = 0;
                    break;
                }
            }
            // Check password validity
            boolean hasStraight = false;
            int straightCount = 1;
            int pairCount = 0;
            boolean lastWasPair = false;
            for (int i = 1; i < passwordInts.size(); i++) {
                int priorChar = passwordInts.get(i - 1);
                int currentChar = passwordInts.get(i);
                // Check straight
                if (currentChar == priorChar + 1) {
                    straightCount++;
                    if (straightCount >= 3) {
                        hasStraight = true;
                    }
                } else {
                    straightCount = 1;
                }
                // Check pairs
                if (currentChar == priorChar && !lastWasPair) {
                    pairCount++;
                    lastWasPair = true;
                } else if (currentChar != priorChar) {
                    lastWasPair = false;
                }
            }
            if (hasStraight && pairCount >= 2) {
                StringBuilder sb = new StringBuilder();
                for (int passwordInt: passwordInts) {
                    sb.append(convertIntToChar(passwordInt));
                }
                return sb.toString();
            }
        }
    }
}
