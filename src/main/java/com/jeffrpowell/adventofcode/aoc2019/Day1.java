package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.InputParser;
import com.jeffrpowell.adventofcode.InputParserFactory;
import java.util.List;

public class Day1 extends Solution2019<Integer>{

	@Override
	public int getDay()
	{
		return 1;
	}
	
	@Override
	public InputParser<Integer> getInputParser()
	{
		return InputParserFactory.getIntegerParser();
	}
	
	@Override
	public String part1(List<Integer> input)
	{
		return Integer.toString(input.stream().map(this::getFuelRequiredPart1).reduce(0, Integer::sum, Integer::sum));
	}
	
	private int getFuelRequiredPart1(int mass) {
		return mass / 3 - 2;
	}

	@Override
	public String part2(List<Integer> input)
	{
		return Integer.toString(input.stream().map(this::getFuelRequiredRecursive).reduce(0, Integer::sum, Integer::sum));
	}
	
	private int getFuelRequiredRecursive(int mass) {
		if (mass < 9) {
			return 0;
		}
		int fuelRequired = getFuelRequiredPart1(mass);
		return fuelRequired + getFuelRequiredRecursive(fuelRequired);
	}

}
