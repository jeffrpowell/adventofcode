package com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes;

import com.jeffrpowell.adventofcode.aoc2019.intcode.Argument;
import com.jeffrpowell.adventofcode.aoc2019.intcode.Opcode;
import com.jeffrpowell.adventofcode.aoc2019.intcode.OpcodeExecutionResponse;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Multiply extends Opcode
{
	private static final int NUM_ARGS = 3;
	
	public Multiply(int opcodePosition)
	{
		super(NUM_ARGS, opcodePosition);
	}

	@Override
	protected OpcodeExecutionResponse performOperation(List<Argument> args, List<Integer> tape, BlockingQueue<Integer> inputQueue, BlockingQueue<Integer> outputQueue)
	{
		tape.set(args.get(2).getValue(), getArgValue(args.get(0), tape) * getArgValue(args.get(1), tape));
		return new OpcodeExecutionResponse(tape, args.get(2).getArgPosition() + 1);
	}
}
