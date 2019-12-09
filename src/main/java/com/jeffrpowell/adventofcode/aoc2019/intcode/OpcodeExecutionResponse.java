package com.jeffrpowell.adventofcode.aoc2019.intcode;

import java.math.BigInteger;
import java.util.List;

public class OpcodeExecutionResponse
{
	private final List<BigInteger> tape;
	private final int newInstructionHeadPosition;
	private final int newRelativeBase;

	public OpcodeExecutionResponse(List<BigInteger> tape, int newInstructionHeadPosition, int newRelativeBase)
	{
		this.tape = tape;
		this.newInstructionHeadPosition = newInstructionHeadPosition;
		this.newRelativeBase = newRelativeBase;
	}

	public List<BigInteger> getTape()
	{
		return tape;
	}

	public int getNewInstructionHeadPosition()
	{
		return newInstructionHeadPosition;
	}

	public int getNewRelativeBase()
	{
		return newRelativeBase;
	}
	 
}
