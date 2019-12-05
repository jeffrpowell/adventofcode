package com.jeffrpowell.adventofcode.aoc2019.intcode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Instruction {
	private final Opcode opcode;
	private final List<Argument> args;

	public Instruction(int opcodeAndParameterModes, int arg1Position, List<Integer> tape)
	{
		this.opcode = Opcode.fromCodeAndModes(opcodeAndParameterModes);
		this.args = new ArrayList<>();
		initializeArgsList(opcodeAndParameterModes, arg1Position, tape);
	}
	
	private void initializeArgsList(int opcodeAndParameterModes, int arg1Position, List<Integer> tape) {
		int parameterModes = opcodeAndParameterModes / 100;
		for (int i = 0; i < this.opcode.getArgs(); i++)
		{
			int parameterMode = parameterModes % 10;
			int argumentValue = tape.get(arg1Position++);
			args.add(new Argument(parameterMode, argumentValue));
			parameterModes /= 10;
		}
	}

	public boolean isHalt() {
		return opcode == Opcode.HALT;
	}

	public List<Integer> executeOperation(List<Integer> tape) {
		return opcode.execute(args, tape);
	}
	
	public int advancePositionHead(int currentPositionHead) {
		return currentPositionHead + opcode.getArgs();
	}
	
	@Override
	public String toString() {
		return opcode.name() + " " + args.stream().map(Argument::toString).collect(Collectors.joining(", "));
	}
}
