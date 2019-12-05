package com.jeffrpowell.adventofcode.aoc2019.intcode;

import java.util.List;
import java.util.stream.Collectors;

public class Instruction {
	private final Opcode opcode;
	private final Integer arg1Pointer;
	private final Integer arg2Pointer;
	private final Integer targetPointer;

	public Instruction(int opcode, Integer arg1Pointer, Integer arg2Pointer, Integer targetPointer)
	{
		this.opcode = Opcode.fromCode(opcode);
		this.arg1Pointer = arg1Pointer;
		this.arg2Pointer = arg2Pointer;
		this.targetPointer = targetPointer;
	}

	public boolean isHalt() {
		return opcode == Opcode.HALT;
	}

	public List<Integer> executeOperation(List<Integer> tape) {
		if (isHalt()) {
			return tape;
		}
		List<Integer> newTape = tape.stream().collect(Collectors.toList());
		int arg1 = newTape.get(arg1Pointer);
		int arg2 = newTape.get(arg2Pointer);
		if (opcode == Opcode.ADD) {
			newTape.set(targetPointer, arg1 + arg2);
		}
		if (opcode == Opcode.MULTIPLY) {
			newTape.set(targetPointer, arg1 * arg2);
		}
		return newTape;
	}
}
