package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.aoc2019.intcode.IntCodeComputer;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class Day9 extends Solution2019<List<BigInteger>>{

	@Override
	public int getDay()
	{
		return 9;
	}

	@Override
	public InputParser<List<BigInteger>> getInputParser()
	{
		return InputParserFactory.getBigIntegerCSVParser();
	}

	@Override
	protected String part1(List<List<BigInteger>> input)
	{
		BlockingQueue<BigInteger> outputQueue = IntCodeComputer.generateDefaultBlockingQueue();
		IntCodeComputer computer = new IntCodeComputer(input.get(0), IntCodeComputer.generateDefaultBlockingQueue(BigInteger.ONE), outputQueue);
		computer.executeProgram();
		List<BigInteger> output = new ArrayList<>();
		outputQueue.drainTo(output);
		return output.stream().map(BigInteger::toString).collect(Collectors.joining("\n"));
	}

	@Override
	protected String part2(List<List<BigInteger>> input)
	{
		BlockingQueue<BigInteger> outputQueue = IntCodeComputer.generateDefaultBlockingQueue();
		IntCodeComputer computer = new IntCodeComputer(input.get(0), IntCodeComputer.generateDefaultBlockingQueue(BigInteger.TWO), outputQueue);
		computer.executeProgram();
		List<BigInteger> output = new ArrayList<>();
		outputQueue.drainTo(output);
		return output.stream().map(BigInteger::toString).collect(Collectors.joining("\n"));
	}

}
