package com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes;

import com.jeffrpowell.adventofcode.aoc2019.intcode.Argument;
import com.jeffrpowell.adventofcode.aoc2019.intcode.Opcode;
import com.jeffrpowell.adventofcode.aoc2019.intcode.OpcodeExecutionResponse;
import java.util.List;

public class Halt extends Opcode
{
	private static final int NUM_ARGS = 0;
	
	public Halt(int opcodePosition)
	{
		super(NUM_ARGS, opcodePosition);
	}

	@Override
	protected OpcodeExecutionResponse performOperation(List<Argument> args, List<Integer> tape)
	{
		return new OpcodeExecutionResponse(tape, getOpcodePosition());
	}
}
