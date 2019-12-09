package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.aoc2019.intcode.IntCodeComputer;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class Day7 extends Solution2019<List<BigInteger>>{

	
	@Override
	public int getDay()
	{
		return 7;
	}

	@Override
	public InputParser<List<BigInteger>> getInputParser()
	{
		return InputParserFactory.getBigIntegerCSVParser();
	}

	@Override
	protected String part1(List<List<BigInteger>> input)
	{
		List<BigInteger> tape = input.get(0);
		List<BigInteger[]> phasePermutations = generatePhasePermutations(new BigInteger[]{BigInteger.ZERO, BigInteger.ONE, BigInteger.TWO, BigInteger.valueOf(3), BigInteger.valueOf(4)});
		return phasePermutations.stream().map(permutation -> Day7.runSimulation(tape, permutation, false)).max(BigInteger::compareTo).map(BigInteger::toString).get();
	}
	
	private static BigInteger runSimulation(List<BigInteger> tape, BigInteger[] phasePermutation, boolean setupFeedbackLoop) {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		List<Amplifier> amplifiers = new ArrayList<>();
		BlockingDeque<BigInteger> priorOutputQueue = new LinkedBlockingDeque<>(Collections.singletonList(BigInteger.ZERO)); //initial input of 0 to first amp
		for (int i = 0; i < phasePermutation.length; i++)
		{
			BigInteger phaseInput = phasePermutation[i];
			Amplifier amp = new Amplifier(tape.stream().collect(Collectors.toList()), phaseInput, priorOutputQueue);
			amplifiers.add(amp);
			priorOutputQueue = amp.getOutputQueue();
		}
		if (setupFeedbackLoop) {
			amplifiers.get(4).setOutputQueue(amplifiers.get(0).getInputQueue());
		}
		amplifiers.stream().forEach(executor::execute);
		try
		{
			for (Amplifier amplifier : amplifiers)
			{
				amplifier.getHaltLatch().await();
			}
			return amplifiers.get(4).getOutputQueue().take();
		} catch (InterruptedException ex)
		{
			return BigInteger.valueOf(-1);
		}
		finally {
			executor.shutdown();
		}
	}
	
	private static List<BigInteger[]> generatePhasePermutations(BigInteger[] elements) {
		List<BigInteger[]> permutations = new ArrayList<>();
		perm(elements, elements.length, permutations);
		return permutations;
	}
	
	public static void perm(BigInteger[] list, int n, List<BigInteger[]> permutations)
	{
		if(n == 1)
		{
			permutations.add(Arrays.copyOf(list, list.length));
		} 
		else 
		{
			for(int i=0; i<n-1; i++)
			{
				perm(list,n-1, permutations);

				int j = ( n % 2 == 0 ) ? i : 0; 

				BigInteger t = list[n-1];              
				list[n-1] = list[j];
				list[j] = t;                
			}
			perm(list,n-1, permutations);
		}
	}
	
	@Override
	protected String part2(List<List<BigInteger>> input)
	{
		List<BigInteger> tape = input.get(0);
		List<BigInteger[]> phasePermutations = generatePhasePermutations(new BigInteger[]{BigInteger.valueOf(5), BigInteger.valueOf(6), BigInteger.valueOf(7), BigInteger.valueOf(8), BigInteger.valueOf(9)});
		return phasePermutations.stream().map(permutation -> Day7.runSimulation(tape, permutation, true)).max(BigInteger::compareTo).map(BigInteger::toString).get();
	}

	private static class Amplifier implements Runnable{
		private final CountDownLatch haltLatch;
		private final List<BigInteger> tape;
		private final BlockingDeque<BigInteger> inputQueue;
		private BlockingDeque<BigInteger> outputQueue;
		
		public Amplifier(List<BigInteger> tape, BigInteger phaseInput, BlockingDeque<BigInteger> inputQueue) {
			this.tape = tape;
			this.inputQueue = inputQueue;
			this.outputQueue = new LinkedBlockingDeque<>();
			this.haltLatch = new CountDownLatch(1);
			try
			{
				this.inputQueue.putFirst(phaseInput);
			} catch (InterruptedException ex)
			{
			}
		}

		public BlockingDeque<BigInteger> getInputQueue()
		{
			return inputQueue;
		}

		public void setOutputQueue(BlockingDeque<BigInteger> outputQueue)
		{
			this.outputQueue = outputQueue;
		}
		
		public BlockingDeque<BigInteger> getOutputQueue() {
			return outputQueue;
		}

		public CountDownLatch getHaltLatch()
		{
			return haltLatch;
		}

		@Override
		public void run()
		{
			IntCodeComputer comp = new IntCodeComputer(tape, inputQueue, outputQueue);
			comp.executeProgram();
			haltLatch.countDown();
		}
	}
}
