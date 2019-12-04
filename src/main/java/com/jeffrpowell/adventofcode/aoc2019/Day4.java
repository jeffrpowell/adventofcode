package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day4 extends Solution2019<String>{

	@Override
	public int getDay()
	{
		return 4;
	}

	@Override
	public InputParser<String> getInputParser()
	{
		return InputParserFactory.getStringParser();
	}
	
	private static final Predicate<List<Integer>> INCREASING_DIGITS = digits -> {
		int first = digits.get(0);
		int second;
		for (int i = 1; i < 6; i++)
		{
			second = digits.get(i);
			if (second < first) {
				return false;
			}
			first = second;
		}
		return true;
	};
		
	private static final Predicate<List<Integer>> DUPLICATE_DIGIT = digits -> {
		int first = digits.get(0);
		int second;
		for (int i = 1; i < 6; i++)
		{
			second = digits.get(i);
			if (first == second) {
				return true;
			}
			first = second;
		}
		return false;
	};	
	
	private static final Predicate<List<Integer>> EXACTLY_TWO_DUPLICATE_DIGITS = digits -> {
		boolean mightHaveAWinner = false;
		int duplicateCounter = 0;
		int first = digits.get(0);
		int second;
		for (int i = 1; i < 6; i++)
		{
			second = digits.get(i);
			if (mightHaveAWinner) {
				if (second != first) {
					if (duplicateCounter == 2) {
						return true;
					}
					else {
						mightHaveAWinner = false;
						duplicateCounter = 0;
					}
				}
				else {
					duplicateCounter++;
				}
			}
			else if (first == second) {
				mightHaveAWinner = true;
				duplicateCounter = 2;
			}
			first = second;
		}
		return mightHaveAWinner && duplicateCounter == 2;
	};

	@Override
	protected String part1(List<String> input)
	{
		String[] range = input.get(0).split("-");
		int minVal = Integer.parseInt(range[0]);
		int maxVal = Integer.parseInt(range[1]);
		return Long.toString(IntStream.rangeClosed(minVal, maxVal).parallel()
			.mapToObj(Day4::breakUpIntegerDigits)
			.filter(INCREASING_DIGITS)
			.filter(DUPLICATE_DIGIT)
			.count());
	}
	
	private static List<Integer> breakUpIntegerDigits(int i) {
		String iStr = Integer.toString(i);
		return Stream.of(iStr.charAt(0), iStr.charAt(1), iStr.charAt(2), iStr.charAt(3), iStr.charAt(4), iStr.charAt(5))
			.map(String::valueOf)
			.map(Integer::parseInt)
			.collect(Collectors.toList());
	}

	@Override
	protected String part2(List<String> input)
	{
		String[] range = input.get(0).split("-");
		int minVal = Integer.parseInt(range[0]);
		int maxVal = Integer.parseInt(range[1]);
		return Long.toString(IntStream.rangeClosed(minVal, maxVal).parallel()
			.mapToObj(Day4::breakUpIntegerDigits)
			.filter(INCREASING_DIGITS)
			.filter(EXACTLY_TWO_DUPLICATE_DIGITS)
			.count());
	}

}
