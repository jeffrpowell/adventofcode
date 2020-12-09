package com.jeffrpowell.adventofcode.inputparser;

import java.util.List;
import java.util.stream.Collectors;

public class LongParser implements InputParser<Long>
{
	@Override
	public List<Long> parseInput(List<String> input)
	{
		return input.stream().map(Long::parseLong).collect(Collectors.toList());
	}
}
