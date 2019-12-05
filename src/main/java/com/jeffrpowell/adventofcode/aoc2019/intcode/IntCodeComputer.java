package com.jeffrpowell.adventofcode.aoc2019.intcode;

import java.util.List;
import java.util.stream.Collectors;

public class IntCodeComputer {

	private final List<Integer> inputTape;
	private List<Integer> outputTape;
	private boolean hasRun;
	
	public IntCodeComputer(List<Integer> tape) {
		this.inputTape = tape;
		this.outputTape = inputTape.stream().collect(Collectors.toList());
		this.hasRun = false;
	}
	
	public void executeProgram() {
		if (hasRun) {
			return;
		}
		int i = 0;
		Instruction instruction;
		do {
			instruction = new Instruction(outputTape.get(i++), i, outputTape);
			outputTape = instruction.executeOperation(outputTape);
			i = instruction.advancePositionHead(i);
		} while (!instruction.isHalt());
		hasRun = true;
	}
	
	public Integer getTapePosition(int position) {
		return outputTape.get(position);
	}
	
	public List<Integer> getOutputTape() {
		return outputTape;
	}
}
