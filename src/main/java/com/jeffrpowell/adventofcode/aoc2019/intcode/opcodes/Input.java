package com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes;

import com.jeffrpowell.adventofcode.aoc2019.intcode.Argument;
import com.jeffrpowell.adventofcode.aoc2019.intcode.Opcode;
import com.jeffrpowell.adventofcode.aoc2019.intcode.OpcodeExecutionResponse;
import java.util.List;
import java.util.Scanner;

public class Input extends Opcode
{
	private static final int NUM_ARGS = 1;
	
	public Input(int opcodePosition)
	{
		super(NUM_ARGS, opcodePosition);
	}

	@Override
	protected OpcodeExecutionResponse performOperation(List<Argument> args, List<Integer> tape)
	{
		tape.set(args.get(0).getValue(), Input.collectInput());
		return new OpcodeExecutionResponse(tape, args.get(0).getArgPosition() + 1);
	}
	
	private static int collectInput() {
		System.out.println("\nPlease provide an input int:");
		Scanner in = new Scanner(System.in);
		return in.nextInt();
	}
	
}
