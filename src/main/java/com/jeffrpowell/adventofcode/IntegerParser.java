package com.jeffrpowell.adventofcode;

import java.util.List;
import java.util.stream.Collectors;

public class IntegerParser implements InputParser<Integer>
{
	@Override
	public List<Integer> parseInput(List<String> input)
	{
		return input.stream().map(Integer::parseInt).collect(Collectors.toList());
	}
}
