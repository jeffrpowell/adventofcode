package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.aoc2019.intcode.IntCodeComputer;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Day13 extends Solution2019<List<BigInteger>>{

	@Override
	public int getDay()
	{
		return 13;
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
		IntCodeComputer computer = new IntCodeComputer(input.get(0), IntCodeComputer.generateDefaultBlockingQueue(), outputQueue);
		computer.executeProgram();
		
		int blockTileCount = 0;
		while(true)
		{
			try
			{
				if (outputQueue.poll(3, TimeUnit.SECONDS) == null) {
					break;
				}
				if (outputQueue.poll(3, TimeUnit.SECONDS) == null) {
					break;
				}
				BigInteger tileType = outputQueue.poll(3, TimeUnit.SECONDS);
				if (tileType.equals(BigInteger.valueOf(Tile.BLOCK.ordinal()))) {
					blockTileCount++;
				}
			} catch (InterruptedException ex)
			{
				break;
			}
		}
		return Integer.toString(blockTileCount);
	}

	@Override
	protected String part2(List<List<BigInteger>> input)
	{
		BlockingQueue<BigInteger> outputQueue = IntCodeComputer.generateDefaultBlockingQueue();
		List<BigInteger> tape = input.get(0);
		tape.set(0, BigInteger.TWO); //play for free!
		IntCodeComputer computer = new IntCodeComputer(tape, IntCodeComputer.generateDefaultBlockingQueue(), outputQueue);
		computer.executeProgram();
		return "NOPE";
	}

	private enum Tile {
		EMPTY, WALL, BLOCK, PADDLE, BALL;
	}
}
