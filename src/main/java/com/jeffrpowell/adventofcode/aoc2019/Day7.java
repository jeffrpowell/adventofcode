package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.aoc2019.intcode.IntCodeComputer;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
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

public class Day7 extends Solution2019<List<Integer>>{

	
	@Override
	public int getDay()
	{
		return 7;
	}

	@Override
	public InputParser<List<Integer>> getInputParser()
	{
		return InputParserFactory.getIntegerCSVParser();
	}

	@Override
	protected String part1(List<List<Integer>> input)
	{
		List<Integer> tape = input.get(0);
		List<int[]> phasePermutations = generatePhasePermutations(new int[]{0,1,2,3,4});
		return Integer.toString(phasePermutations.stream().map(permutation -> Day7.runSimulation(tape, permutation, false)).max(Integer::compare).get());
	}
	
	private static int runSimulation(List<Integer> tape, int[] phasePermutation, boolean setupFeedbackLoop) {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		List<Amplifier> amplifiers = new ArrayList<>();
		BlockingDeque<Integer> priorOutputQueue = new LinkedBlockingDeque<>(Collections.singletonList(0)); //initial input of 0 to first amp
		for (int i = 0; i < phasePermutation.length; i++)
		{
			int phaseInput = phasePermutation[i];
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
			return -1;
		}
		finally {
			executor.shutdown();
		}
	}
	
	private static List<int[]> generatePhasePermutations(int[] elements) {
		List<int[]> permutations = new ArrayList<>();
		perm(elements, elements.length, permutations);
		return permutations;
	}
	
	public static void perm(int[] list, int n, List<int[]> permutations)
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

				int t = list[n-1];              
				list[n-1] = list[j];
				list[j] = t;                
			}
			perm(list,n-1, permutations);
		}
	}
	
	@Override
	protected String part2(List<List<Integer>> input)
	{
		List<Integer> tape = input.get(0);
		List<int[]> phasePermutations = generatePhasePermutations(new int[]{5,6,7,8,9});
		return Integer.toString(phasePermutations.stream().map(permutation -> Day7.runSimulation(tape, permutation, true)).max(Integer::compare).get());
	}

	private static class Amplifier implements Runnable{
		private final CountDownLatch haltLatch;
		private final List<Integer> tape;
		private final BlockingDeque<Integer> inputQueue;
		private BlockingDeque<Integer> outputQueue;
		
		public Amplifier(List<Integer> tape, int phaseInput, BlockingDeque<Integer> inputQueue) {
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

		public BlockingDeque<Integer> getInputQueue()
		{
			return inputQueue;
		}

		public void setOutputQueue(BlockingDeque<Integer> outputQueue)
		{
			this.outputQueue = outputQueue;
		}
		
		public BlockingDeque<Integer> getOutputQueue() {
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
