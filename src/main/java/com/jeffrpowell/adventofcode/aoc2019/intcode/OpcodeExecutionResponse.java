package com.jeffrpowell.adventofcode.aoc2019.intcode;

import java.util.List;

public class OpcodeExecutionResponse
{
	private final List<Integer> tape;
	private final int newInstructionHeadPosition;

	public OpcodeExecutionResponse(List<Integer> tape, int newInstructionHeadPosition)
	{
		this.tape = tape;
		this.newInstructionHeadPosition = newInstructionHeadPosition;
	}

	public List<Integer> getTape()
	{
		return tape;
	}

	public int getNewInstructionHeadPosition()
	{
		return newInstructionHeadPosition;
	}
	
}
