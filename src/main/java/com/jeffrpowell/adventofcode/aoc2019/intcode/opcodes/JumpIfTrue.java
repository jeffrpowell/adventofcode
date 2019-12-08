package com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes;

import com.jeffrpowell.adventofcode.aoc2019.intcode.Argument;
import com.jeffrpowell.adventofcode.aoc2019.intcode.Opcode;
import com.jeffrpowell.adventofcode.aoc2019.intcode.OpcodeExecutionResponse;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class JumpIfTrue extends Opcode
{
	private static final int NUM_ARGS = 2;
	
	public JumpIfTrue(int opcodePosition)
	{
		super(NUM_ARGS, opcodePosition);
	}

	@Override
	protected OpcodeExecutionResponse performOperation(List<Argument> args, List<Integer> tape, BlockingQueue<Integer> inputQueue, BlockingQueue<Integer> outputQueue)
	{
		boolean jump = getArgValue(args.get(0), tape) != 0;
		int nextInstructionHead = jump ? getArgValue(args.get(1), tape) : args.get(1).getArgPosition() + 1;
		return new OpcodeExecutionResponse(tape, nextInstructionHead);
	}
}
