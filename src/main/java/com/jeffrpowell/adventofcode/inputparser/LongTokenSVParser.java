package com.jeffrpowell.adventofcode.inputparser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LongTokenSVParser implements InputParser<List<Long>>{

	private final String delimiterRegex;

	public LongTokenSVParser(String delimiterRegex)
	{
		this.delimiterRegex = delimiterRegex;
	}
	
	@Override
	public List<List<Long>> parseInput(List<String> input)
	{
		return input.stream()
			.map(line -> line.split(delimiterRegex))
			.map(Arrays::asList)
			.map(strList -> strList.stream().filter(s -> !s.isEmpty()).map(Long::parseLong).collect(Collectors.toList()))
			.collect(Collectors.toList());
	}

}
