package com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes;

import com.jeffrpowell.adventofcode.aoc2019.intcode.Argument;
import com.jeffrpowell.adventofcode.aoc2019.intcode.Opcode;
import com.jeffrpowell.adventofcode.aoc2019.intcode.OpcodeExecutionResponse;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Add extends Opcode
{
	private static final int NUM_ARGS = 3;
	
	public Add(int opcodePosition)
	{
		super(NUM_ARGS, opcodePosition);
	}

	@Override
	protected OpcodeExecutionResponse performOperation(List<Argument> args, List<BigInteger> tape, int relativeBase, BlockingQueue<BigInteger> inputQueue, BlockingQueue<BigInteger> outputQueue)
	{
		writeIntToPosition(tape, args.get(2).getValue().intValue(), getArgValue(args.get(0), tape).add(getArgValue(args.get(1), tape)));
		return new OpcodeExecutionResponse(tape, args.get(2).getArgPosition() + 1, relativeBase);
	}
}
