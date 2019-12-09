package com.jeffrpowell.adventofcode.aoc2019.intcode;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class IntCodeComputer {

	private final List<BigInteger> inputTape;
	private final BlockingQueue<BigInteger> inputQueue;
	private final BlockingQueue<BigInteger> outputQueue;
	private List<BigInteger> outputTape;
	private int relativeBase;
	private boolean hasRun;
	
	public IntCodeComputer(List<BigInteger> tape, BlockingQueue<BigInteger> inputQueue, BlockingQueue<BigInteger> outputQueue) {
		this.inputTape = tape;
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;
		this.outputTape = inputTape.stream().collect(Collectors.toList());
		this.relativeBase = 0;
		this.hasRun = false;
	}
	
	public void executeProgram() {
		if (hasRun) {
			return;
		}
		int i = 0;
		Instruction instruction;
		do {
			instruction = new Instruction(i, outputTape, relativeBase, inputQueue, outputQueue);
			outputTape = instruction.getNewTape();
			i = instruction.getNewInstructionHeadPosition();
			relativeBase = instruction.getNewRelativeBase();
		} while (!instruction.isHalt());
		hasRun = true;
	}
	
	public BigInteger getTapePosition(int position) {
		return outputTape.get(position);
	}
	
	public List<BigInteger> getOutputTape() {
		return outputTape;
	}
}
