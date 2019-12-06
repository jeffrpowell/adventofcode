package com.jeffrpowell.adventofcode.inputparser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TokenSVParser implements InputParser<List<String>>{

	private final String delimiterRegex;

	public TokenSVParser(String delimiterRegex)
	{
		this.delimiterRegex = delimiterRegex;
	}
	
	@Override
	public List<List<String>> parseInput(List<String> input)
	{
		return input.stream().map(line -> line.split(delimiterRegex)).map(Arrays::asList).collect(Collectors.toList());
	}

}
