package com.jeffrpowell.adventofcode.aoc2019.intcode;

import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.Add;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.AdjustRelativeBase;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.Equals;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.Halt;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.Input;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.JumpIfFalse;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.JumpIfTrue;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.LessThan;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.Multiply;
import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.Output;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		OPCODE_INDEX.put(9, AdjustRelativeBase.class);
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
	
	protected static BigInteger getArgValue(Argument arg, List<BigInteger> tape) {
		return arg.getValue(tape);
	}

	protected static void writeIntToPosition(List<BigInteger> tape, int positionYouNeedToWriteTo, BigInteger valueToWrite) {
		if (positionYouNeedToWriteTo >= tape.size()) {
			Stream.generate(() -> BigInteger.ZERO).limit(positionYouNeedToWriteTo - tape.size() + 1).forEach(tape::add);
		}
		tape.set(positionYouNeedToWriteTo, valueToWrite);
	}

	public int getNumArgs()
	{
		return numArgs;
	}

	public int getOpcodePosition()
	{
		return opcodePosition;
	}
	
	public OpcodeExecutionResponse execute(List<Argument> args, List<BigInteger> tape, int relativeBase, BlockingQueue<BigInteger> inputQueue, BlockingQueue<BigInteger> outputQueue) {
		List<BigInteger> newTape = tape.stream().collect(Collectors.toList());
		return performOperation(args, newTape, relativeBase, inputQueue, outputQueue);
	}
	protected abstract OpcodeExecutionResponse performOperation(List<Argument> args, List<BigInteger> tape, int relativeBase, BlockingQueue<BigInteger> inputQueue, BlockingQueue<BigInteger> outputQueue);
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " (position " + opcodePosition + ")";
	}
}
