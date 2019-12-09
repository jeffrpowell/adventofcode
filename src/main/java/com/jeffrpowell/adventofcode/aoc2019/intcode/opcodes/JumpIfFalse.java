package com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes;

import com.jeffrpowell.adventofcode.aoc2019.intcode.Argument;
import com.jeffrpowell.adventofcode.aoc2019.intcode.Opcode;
import com.jeffrpowell.adventofcode.aoc2019.intcode.OpcodeExecutionResponse;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class JumpIfFalse extends Opcode
{
	private static final int NUM_ARGS = 2;
	
	public JumpIfFalse(int opcodePosition)
	{
		super(NUM_ARGS, opcodePosition);
	}

	@Override
	protected OpcodeExecutionResponse performOperation(List<Argument> args, List<BigInteger> tape, int relativeBase, BlockingQueue<BigInteger> inputQueue, BlockingQueue<BigInteger> outputQueue)
	{
		boolean jump = getArgValue(args.get(0), tape).compareTo(BigInteger.ZERO) == 0;
		int nextInstructionHead = jump ? getArgValue(args.get(1), tape).intValue() : args.get(1).getArgPosition() + 1;
		return new OpcodeExecutionResponse(tape, nextInstructionHead, relativeBase);
	}
}
