package com.jeffrpowell.adventofcode.inputparser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IntegerTokenSVParser implements InputParser<List<Integer>>{

	private final String delimiterRegex;

	public IntegerTokenSVParser(String delimiterRegex)
	{
		this.delimiterRegex = delimiterRegex;
	}
	
	@Override
	public List<List<Integer>> parseInput(List<String> input)
	{
		return input.stream()
			.map(line -> line.split(delimiterRegex))
			.map(Arrays::asList)
			.map(strList -> strList.stream().filter(s -> !s.isEmpty()).map(Integer::parseInt).collect(Collectors.toList()))
			.collect(Collectors.toList());
	}

}
