package com.jeffrpowell.adventofcode.aoc2019.intcode;

import com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes.Halt;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class Instruction {
	private final Opcode opcode;
	private final List<Argument> args;
	private final List<Integer> tape;
	private final OpcodeExecutionResponse opcodeExecutionResponse;

	public Instruction(int opcodePosition, List<Integer> tape, BlockingQueue<Integer> inputQueue, BlockingQueue<Integer> outputQueue)
	{
		this.opcode = Opcode.fromCodeAndModes(tape.get(opcodePosition), opcodePosition);
		this.args = new ArrayList<>();
		this.tape = tape;
		initializeArgsList(opcodePosition);
		this.opcodeExecutionResponse = opcode.execute(args, tape, inputQueue, outputQueue);
	}
	
	private void initializeArgsList(int opcodePosition) {
		int parameterModes = tape.get(opcodePosition) / 100;
		int currentArgPosition = opcodePosition + 1;
		for (int i = 0; i < this.opcode.getNumArgs(); i++)
		{
			int parameterMode = parameterModes % 10;
			int argumentValue = tape.get(currentArgPosition);
			args.add(new Argument(parameterMode, argumentValue, currentArgPosition++));
			parameterModes /= 10;
		}
	}

	public boolean isHalt() {
		return opcode instanceof Halt;
	}

	public List<Integer> getNewTape() {
		return opcodeExecutionResponse.getTape();
	}
	
	public int getNewInstructionHeadPosition() {
		return opcodeExecutionResponse.getNewInstructionHeadPosition();
	}
	
	@Override
	public String toString() {
		return opcode.getClass().getSimpleName() + " " + args.stream().map(Argument::toString).collect(Collectors.joining(", "));
	}
}
