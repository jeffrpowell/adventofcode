package com.jeffrpowell.adventofcode.aoc2018.solutions;

import com.jeffrpowell.adventofcode.InputParser;
import com.jeffrpowell.adventofcode.InputParserFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day2 extends Solution2018<String>
{
	
	@Override
	public int getDay()
	{
		return 2;
	}
	
	@Override
	public InputParser<String> getInputParser()
	{
		return InputParserFactory.getStringParser();
	}
	
    @Override
    public String part1(List<String> input) {
        List<Map<Character, Integer>> charMaps = input.stream().map(String::toCharArray).map(this::countChars).collect(Collectors.toList());
        long twoCharCount = charMaps.stream().filter(m -> m.containsValue(2)).count();
        long threeCharCount = charMaps.stream().filter(m -> m.containsValue(3)).count();
        return Long.toString(twoCharCount * threeCharCount);
    }

    @Override
    public String part2(List<String> input) {
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.size(); j++) {
                if (i == j) continue;
                if (calculateLevenshtein(input.get(i), input.get(j)) == 1) {
                    return input.get(i) + " - " + input.get(j); //manually inspect common letters
                }
            }
        }
        return null;
    }
    
    private Map<Character, Integer> countChars(char[] cArr) {
        Map<Character, Integer> charMap = new HashMap<>();
        for (char c : cArr) {
            charMap.put(c, charMap.computeIfAbsent(c, key -> 0) + 1);
        }
        return charMap;
    }
    
    //https://www.baeldung.com/java-levenshtein-distance;
    static int calculateLevenshtein(String x, String y) {
        int[][] dp = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }
                else if (j == 0) {
                    dp[i][j] = i;
                }
                else {
                    dp[i][j] = min(dp[i - 1][j - 1] 
                     + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), 
                      dp[i - 1][j] + 1, 
                      dp[i][j - 1] + 1);
                }
            }
        }

        return dp[x.length()][y.length()];
    }
    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }
 
    public static int min(int... numbers) {
        return Arrays.stream(numbers)
          .min().orElse(Integer.MAX_VALUE);
    }
}
