package com.jeffrpowell.adventofcode.inputparser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IntegerCSVParser implements InputParser<List<Integer>>{

	@Override
	public List<List<Integer>> parseInput(List<String> input)
	{
		return input.stream()
			.map(line -> line.split(","))
			.map(Arrays::asList)
			.map(IntegerCSVParser::stringToIntList)
			.collect(Collectors.toList());
	}
	
	private static List<Integer> stringToIntList(List<String> strings) {
		return strings.stream().map(Integer::parseInt).collect(Collectors.toList());
	}
}
