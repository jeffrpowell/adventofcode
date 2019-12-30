package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.aoc2019.intcode.IntCodeComputer;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Day25 extends Solution2019<List<BigInteger>>
{
	private final StringBuilder outputBuilder = new StringBuilder();
	private final Scanner scanner = new Scanner(System.in);
	
	@Override
	public int getDay()
	{
		return 25;
	}

	@Override
	public InputParser<List<BigInteger>> getInputParser()
	{
		return InputParserFactory.getBigIntegerCSVParser();
	}

	@Override
	protected String part1(List<List<BigInteger>> input)
	{
		ExecutorService executor = Executors.newSingleThreadExecutor();
		BlockingQueue<BigInteger> inputQueue = IntCodeComputer.generateDefaultBlockingQueue();
		BlockingQueue<BigInteger> outputQueue = IntCodeComputer.generateDefaultBlockingQueue();
		IntCodeComputer computer = new IntCodeComputer(input.get(0), inputQueue, outputQueue);
		executor.submit(computer::executeProgram);
		while(true) {
			for (Character c : computer.dumpOutputAsASCII())
			{
				outputBuilder.append(c);
				if (c.equals('?')) {
					String output = outputBuilder.toString();
					System.out.println(output);
					if (output.contains("Command?")) {
						outputBuilder.setLength(0);
						submitInput(scanner.nextLine(), inputQueue);
					}
				}
				else if (outputBuilder.toString().contains("anta")) {
					System.out.println(outputBuilder.toString());
				}
			}
		}
	}
	
	private void submitInput(String command, BlockingQueue<BigInteger> inputQueue) {
		for (char c : command.toCharArray())
		{
			try
			{
				inputQueue.put(BigInteger.valueOf((int) c));
			}
			catch (InterruptedException ex)
			{
			}
		}
		try
		{
			inputQueue.put(BigInteger.valueOf(10)); //newline
		}
		catch (InterruptedException ex)
		{
		}
	}

	@Override
	protected String part2(List<List<BigInteger>> input)
	{
		return "";
	}
	
	/*
	 * Part 1 solution:
Solution:
east
east
south
take monolith
north
west
north
north
take planetoid
west
south
south
take fuel cell
north
north
east
east
south
west
north
take astrolabe
west
north

All pick-up-able items:
east
east
south
take monolith
north
east
take shell
west
west
north
west
take bowl of rice
east
north
take planetoid
west
take ornament
south
south
take fuel cell
north
north
east
east
take cake
south
west
north
take astrolabe
west
	 */
}
