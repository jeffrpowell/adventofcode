package com.jeffrpowell.adventofcode.aoc2019.intcode;

import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.Add;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.Equals;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.Halt;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.Input;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.JumpIfFalse;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.JumpIfTrue;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.LessThan;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.Multiply;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.Output;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Opcode {
	private static final Map<Integer, Class<? extends Opcode>> OPCODE_INDEX;
	static {
		OPCODE_INDEX = new HashMap<>();
		OPCODE_INDEX.put(1, Add.class);
		OPCODE_INDEX.put(2, Multiply.class);
		OPCODE_INDEX.put(3, Input.class);
		OPCODE_INDEX.put(4, Output.class);
		OPCODE_INDEX.put(5, JumpIfTrue.class);
		OPCODE_INDEX.put(6, JumpIfFalse.class);
		OPCODE_INDEX.put(7, LessThan.class);
		OPCODE_INDEX.put(8, Equals.class);
		OPCODE_INDEX.put(99, Halt.class);
	}
			
	private final int numArgs;
	private final int opcodePosition;

	public Opcode(int numArgs, int opcodePosition)
	{
		this.numArgs = numArgs;
		this.opcodePosition = opcodePosition;
	}

	/**
	 * Must not contain parameter modes in the input
	 * @param code
	 * @param opcodePosition
	 * @return 
	 */
	public static Opcode fromCode(int code, int opcodePosition) {
		try
		{
			return OPCODE_INDEX.get(code).getConstructor(int.class).newInstance(opcodePosition);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Will contain parameter modes in the number. Be sure to check just the final two digits.
	 * @param codeAndModes
	 * @param opcodePosition
	 * @return 
	 */
	public static Opcode fromCodeAndModes(int codeAndModes, int opcodePosition) {
		int code = codeAndModes % 100;
		return fromCode(code, opcodePosition);
	}
	
	protected static int getArgValue(Argument arg, List<Integer> tape) {
		return arg.getValue(tape);
	}

	public int getNumArgs()
	{
		return numArgs;
	}

	public int getOpcodePosition()
	{
		return opcodePosition;
	}
	
	public OpcodeExecutionResponse execute(List<Argument> args, List<Integer> tape) {
		List<Integer> newTape = tape.stream().collect(Collectors.toList());
		return performOperation(args, newTape);
	}
	protected abstract OpcodeExecutionResponse performOperation(List<Argument> args, List<Integer> tape);
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " (position " + opcodePosition + ")";
	}
}
