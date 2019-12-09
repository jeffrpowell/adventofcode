package com.jeffrpowell.adventofcode.aoc2019.intcode;

import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.Halt;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class Instruction {
	private final Opcode opcode;
	private final List<Argument> args;
	private final List<BigInteger> tape;
	private final OpcodeExecutionResponse opcodeExecutionResponse;

	public Instruction(int opcodePosition, List<BigInteger> tape, int relativeBase, BlockingQueue<BigInteger> inputQueue, BlockingQueue<BigInteger> outputQueue)
	{
		this.opcode = Opcode.fromCodeAndModes(tape.get(opcodePosition).intValue(), opcodePosition);
		this.args = new ArrayList<>();
		this.tape = tape;
		initializeArgsList(opcodePosition, relativeBase);
		this.opcodeExecutionResponse = opcode.execute(args, tape, relativeBase, inputQueue, outputQueue);
	}
	
	private void initializeArgsList(int opcodePosition, int relativeBase) {
		int parameterModes = tape.get(opcodePosition).intValue() / 100;
		int currentArgPosition = opcodePosition + 1;
		for (int i = 0; i < this.opcode.getNumArgs(); i++)
		{
			int parameterMode = parameterModes % 10;
			BigInteger argumentValue = tape.get(currentArgPosition);
			args.add(new Argument(parameterMode, argumentValue, currentArgPosition++, relativeBase));
			parameterModes /= 10;
		}
	}

	public boolean isHalt() {
		return opcode instanceof Halt;
	}

	public List<BigInteger> getNewTape() {
		return opcodeExecutionResponse.getTape();
	}
	
	public int getNewInstructionHeadPosition() {
		return opcodeExecutionResponse.getNewInstructionHeadPosition();
	}
	
	public int getNewRelativeBase() {
		return opcodeExecutionResponse.getNewRelativeBase();
	}
	
	@Override
	public String toString() {
		return opcode.getClass().getSimpleName() + " " + args.stream().map(Argument::toString).collect(Collectors.joining(", "));
	}
}
