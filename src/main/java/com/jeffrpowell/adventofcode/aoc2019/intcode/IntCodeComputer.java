package com.jeffrpowell.adventofcode.aoc2019.intcode;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class IntCodeComputer {

	private final List<Integer> inputTape;
	private final BlockingQueue<Integer> inputQueue;
	private final BlockingQueue<Integer> outputQueue;
	private List<Integer> outputTape;
	private boolean hasRun;
	
	public IntCodeComputer(List<Integer> tape, BlockingQueue<Integer> inputQueue, BlockingQueue<Integer> outputQueue) {
		this.inputTape = tape;
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;
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
			instruction = new Instruction(i, outputTape, inputQueue, outputQueue);
			outputTape = instruction.getNewTape();
			i = instruction.getNewInstructionHeadPosition();
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
