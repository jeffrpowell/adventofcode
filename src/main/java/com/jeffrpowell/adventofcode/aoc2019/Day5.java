package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.aoc2019.intcode.IntCodeComputer;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.List;

public class Day5 extends Solution2019<List<Integer>>{
	@Override
	public int getDay()
	{
		return 5;
	}

	@Override
	public InputParser<List<Integer>> getInputParser()
	{
		return InputParserFactory.getIntegerCSVParser();
	}

	@Override
	protected String part1(List<List<Integer>> input)
	{
		IntCodeComputer computer = new IntCodeComputer(input.get(0));
		computer.executeProgram(); // Input 1
		return "";
	}

	@Override
	protected String part2(List<List<Integer>> input)
	{
		return part1(input); //Input 5
	}

}
