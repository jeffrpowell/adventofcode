package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.aoc2019.intcode.IntCodeComputer;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class Day5 extends Solution2019<List<BigInteger>>{
	@Override
	public int getDay()
	{
		return 5;
	}

	@Override
	public InputParser<List<BigInteger>> getInputParser()
	{
		return InputParserFactory.getBigIntegerCSVParser();
	}

	@Override
	protected String part1(List<List<BigInteger>> input)
	{
		LinkedBlockingDeque<BigInteger> outputDeque = new LinkedBlockingDeque<>();
		IntCodeComputer computer = new IntCodeComputer(input.get(0), new LinkedBlockingDeque<>(Collections.singletonList(BigInteger.ONE)), outputDeque);
		computer.executeProgram();
		List<BigInteger> output = new ArrayList<>();
		outputDeque.drainTo(output);
		return output.stream().map(i -> i.toString()).collect(Collectors.joining("\n"));
	}

	@Override
	protected String part2(List<List<BigInteger>> input)
	{
		LinkedBlockingDeque<BigInteger> outputDeque = new LinkedBlockingDeque<>();
		IntCodeComputer computer = new IntCodeComputer(input.get(0), new LinkedBlockingDeque<>(Collections.singletonList(BigInteger.valueOf(5))), outputDeque);
		computer.executeProgram();
		List<BigInteger> output = new ArrayList<>();
		outputDeque.drainTo(output);
		return output.stream().map(i -> i.toString()).collect(Collectors.joining("\n"));
	}

}
