package com.jeffrpowell.adventofcode.inputparser;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BigIntegerCSVParser implements InputParser<List<BigInteger>>{

	@Override
	public List<List<BigInteger>> parseInput(List<String> input)
	{
		return input.stream()
			.map(line -> line.split(","))
			.map(Arrays::asList)
			.map(BigIntegerCSVParser::stringToIntList)
			.collect(Collectors.toList());
	}
	
	private static List<BigInteger> stringToIntList(List<String> strings) {
		return strings.stream().map(BigInteger::new).collect(Collectors.toList());
	}
}
