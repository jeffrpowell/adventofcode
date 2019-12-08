package com.jeffrpowell.adventofcode.aoc2019.intcode.opcodes;

import com.jeffrpowell.adventofcode.aoc2019.intcode.Argument;
import com.jeffrpowell.adventofcode.aoc2019.intcode.Opcode;
import com.jeffrpowell.adventofcode.aoc2019.intcode.OpcodeExecutionResponse;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Input extends Opcode
{
	private static final Logger log = LogManager.getLogger(Input.class);
	private static final int NUM_ARGS = 1;
	
	public Input(int opcodePosition)
	{
		super(NUM_ARGS, opcodePosition);
	}

	@Override
	protected OpcodeExecutionResponse performOperation(List<Argument> args, List<Integer> tape, BlockingQueue<Integer> inputQueue, BlockingQueue<Integer> outputQueue)
	{
		try
		{
			log.debug("Checking for input");
			int input = inputQueue.take();
			log.debug("Received input: " + input);
			tape.set(args.get(0).getValue(), input);
		} catch (InterruptedException ex)
		{
			log.debug("Interrupted out of input");
		}
		return new OpcodeExecutionResponse(tape, args.get(0).getArgPosition() + 1);
	}
	
}
