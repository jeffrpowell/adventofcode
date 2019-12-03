package com.jeffrpowell.adventofcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IntegerCSVParser implements InputParser<Integer>{

	@Override
	public List<Integer> parseInput(List<String> input)
	{
		return input.stream().map(line -> line.split(",")).flatMap(Arrays::stream).map(Integer::parseInt).collect(Collectors.toList());
	}

}
