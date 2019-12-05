package com.jeffrpowell.adventofcode.aoc2019.intcode;

import java.util.List;

public class IntCodeComputer {

	private IntCodeComputer() {}
	
	public static Integer executeProgram(List<Integer> tape) {
		int i = 0;
		Instruction instruction;
		do {
			instruction = new Instruction(tape.get(i++), getOrNull(tape, i++), getOrNull(tape, i++), getOrNull(tape, i++));
			tape = instruction.executeOperation(tape);
		} while (!instruction.isHalt());
		return tape.get(0);
	}
	
	private static Integer getOrNull(List<Integer> tape, int i) {
		if (i > tape.size() ) {
			return 0;
		}
		else {
			return tape.get(i);
		}
	}
}
