package com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes;

import com.jeffrpowell.adventofcode.aoc2019.intcode.Argument;
import com.jeffrpowell.adventofcode.aoc2019.intcode.Opcode;
import com.jeffrpowell.adventofcode.aoc2019.intcode.OpcodeExecutionResponse;
import java.util.List;

public class Output extends Opcode
{
	private static final int NUM_ARGS = 1;
	
	public Output(int opcodePosition)
	{
		super(NUM_ARGS, opcodePosition);
	}

	@Override
	protected OpcodeExecutionResponse performOperation(List<Argument> args, List<Integer> tape)
	{
		writeOutput(getArgValue(args.get(0), tape));
		return new OpcodeExecutionResponse(tape, args.get(0).getArgPosition() + 1);
	}
	
	private static void writeOutput(int output) {
		System.out.println(output);
	}
}
