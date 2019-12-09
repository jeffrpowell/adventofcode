package com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes;

import com.jeffrpowell.adventofcode.aoc2019.intcode.Argument;
import com.jeffrpowell.adventofcode.aoc2019.intcode.Opcode;
import com.jeffrpowell.adventofcode.aoc2019.intcode.OpcodeExecutionResponse;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Halt extends Opcode
{
	private static final int NUM_ARGS = 0;
	
	public Halt(int opcodePosition)
	{
		super(NUM_ARGS, opcodePosition);
	}

	@Override
	protected OpcodeExecutionResponse performOperation(List<Argument> args, List<BigInteger> tape, int relativeBase, BlockingQueue<BigInteger> inputQueue, BlockingQueue<BigInteger> outputQueue)
	{
		return new OpcodeExecutionResponse(tape, getOpcodePosition(), relativeBase);
	}
}
