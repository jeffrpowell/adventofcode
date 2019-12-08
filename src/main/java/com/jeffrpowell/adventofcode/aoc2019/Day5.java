package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.aoc2019.intcode.IntCodeComputer;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

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
		LinkedBlockingDeque<Integer> outputDeque = new LinkedBlockingDeque<>();
		IntCodeComputer computer = new IntCodeComputer(input.get(0), new LinkedBlockingDeque<>(Collections.singletonList(1)), outputDeque);
		computer.executeProgram();
		List<Integer> output = new ArrayList<>();
		outputDeque.drainTo(output);
		return output.stream().map(i -> i.toString()).collect(Collectors.joining("\n"));
	}

	@Override
	protected String part2(List<List<Integer>> input)
	{
		LinkedBlockingDeque<Integer> outputDeque = new LinkedBlockingDeque<>();
		IntCodeComputer computer = new IntCodeComputer(input.get(0), new LinkedBlockingDeque<>(Collections.singletonList(5)), outputDeque);
		computer.executeProgram();
		List<Integer> output = new ArrayList<>();
		outputDeque.drainTo(output);
		return output.stream().map(i -> i.toString()).collect(Collectors.joining("\n"));
	}

}
