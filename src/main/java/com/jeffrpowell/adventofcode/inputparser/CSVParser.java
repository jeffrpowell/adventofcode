package com.jeffrpowell.adventofcode.inputparser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVParser implements InputParser<List<String>>{

	@Override
	public List<List<String>> parseInput(List<String> input)
	{
		return input.stream().map(line -> line.split(",")).map(Arrays::asList).collect(Collectors.toList());
	}

}
