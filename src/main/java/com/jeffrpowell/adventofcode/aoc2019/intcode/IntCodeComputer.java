package com.jeffrpowell.adventofcode.aoc2019.intcode;

import java.util.List;

public class IntCodeComputer {

	private IntCodeComputer() {}
	
	public static Integer executeProgram(List<Integer> tape) {
		int i = 0;
		Instruction instruction;
		do {
			instruction = new Instruction(tape.get(i++), i, tape);
			tape = instruction.executeOperation(tape);
			i = instruction.advancePositionHead(i);
		} while (!instruction.isHalt());
		return tape.get(0);
	}
}
