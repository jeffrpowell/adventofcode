package com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes;

import com.jeffrpowell.adventofcode.aoc2019.intcode.Argument;
import com.jeffrpowell.adventofcode.aoc2019.intcode.Opcode;
import com.jeffrpowell.adventofcode.aoc2019.intcode.OpcodeExecutionResponse;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Output extends Opcode
{
	private static final Logger log = LogManager.getLogger(Output.class);
	private static final int NUM_ARGS = 1;
	
	public Output(int opcodePosition)
	{
		super(NUM_ARGS, opcodePosition);
	}

	@Override
	protected OpcodeExecutionResponse performOperation(List<Argument> args, List<BigInteger> tape, int relativeBase, BlockingQueue<BigInteger> inputQueue, BlockingQueue<BigInteger> outputQueue)
	{
		try
		{
			log.debug("Outputting value");
			outputQueue.put(getArgValue(args.get(0), tape));
			log.debug("Output value: " + getArgValue(args.get(0), tape));
		} 
		catch (InterruptedException ex)
		{
			log.debug("Interrupted out of output");
		}
		return new OpcodeExecutionResponse(tape, args.get(0).getArgPosition() + 1, relativeBase);
	}
}
