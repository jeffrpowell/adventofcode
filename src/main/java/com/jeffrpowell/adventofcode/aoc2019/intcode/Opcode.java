package com.jeffrpowell.adventofcode.aoc2019.intcode;

import java.util.Arrays;

public enum Opcode {
	ADD(1), MULTIPLY(2), HALT(99);
			
	private final int code;
	Opcode(int code) {
		this.code = code;
	}

	public static Opcode fromCode(int code) {
		return Arrays.stream(values()).filter(opcode -> opcode.code == code).findAny().orElse(null);
	}
}
