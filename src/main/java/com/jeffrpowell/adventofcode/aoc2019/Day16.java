package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16 extends Solution2019<List<Integer>>
{
	@Override
	public int getDay()
	{
		return 16;
	}

	@Override
	public InputParser<List<Integer>> getInputParser()
	{
		return InputParserFactory.getIntegerTokenSVParser("");
	}

	@Override
	protected String part1(List<List<Integer>> input)
	{
		ExecutorService executor = Executors.newFixedThreadPool(8);
		List<Integer> newInput = input.get(0);
		for (int i = 0; i < 100; i++)
		{
			Phase phaseI = new Phase(newInput);
			newInput = phaseI.execute(executor);
		}
		executor.shutdown();
		return newInput.stream().limit(8).map(i -> i.toString()).collect(Collectors.joining(""));
	}

	@Override
	protected String part2(List<List<Integer>> input)
	{
		ExecutorService executor = Executors.newFixedThreadPool(8);
		List<Integer> input0 = input.get(0);
		List<Integer> newInput = input.get(0);
		for (int i = 1; i < 10000; i++)
		{
			newInput.addAll(input0);
		}
		for (int i = 0; i < 100; i++)
		{
			Phase phaseI = new Phase(newInput);
			newInput = phaseI.execute(executor);
		}
		executor.shutdown();
		return newInput.stream().skip(5972877).limit(8).map(i -> i.toString()).collect(Collectors.joining(""));
	}
	
	private static class Phase {
		private final List<PhaseDigit> outputDigits;

		public Phase(List<Integer> input)
		{
			this.outputDigits = IntStream.range(0, input.size())
				.mapToObj(i -> new PhaseDigit(i, input))
				.collect(Collectors.toList());
		}

		public List<Integer> execute(ExecutorService executor) {
			try
			{
				List<Future<Integer>> futureDigits = executor.invokeAll(outputDigits);
				return futureDigits.stream().map(Phase::getFutureValue).collect(Collectors.toList());
			}
			catch (InterruptedException ex)
			{
			}
			return outputDigits.stream().map(phaseDigit -> -1).collect(Collectors.toList());
		}
		
		private static Integer getFutureValue(Future<Integer> future) {
			try
			{
				return future.get();
			}
			catch (InterruptedException | ExecutionException ex)
			{
				return -1;
			}
		}
	}
	
	private static class PhaseDigit implements Callable<Integer>{
		private static final List<Integer> PATTERN = List.of(0, 1, 0, -1);
		private final int outputId;
		private final List<Integer> input;

		public PhaseDigit(int outputId, List<Integer> input)
		{
			this.outputId = outputId;
			this.input = input;
		}

		@Override
		public Integer call() throws Exception
		{
			List<Integer> output = new ArrayList<>();
			List<Integer> patternList = createPatternList();
			for (int i = 0; i < input.size(); i++)
			{
				output.add(input.get(i) * patternList.get(i));
			}
			return fetchOnesDigit(output.stream().reduce(0, Integer::sum, Integer::sum));
		}

		private List<Integer> createPatternList() {
			List<Integer> patternList = new ArrayList<>();
			while(patternList.size() < input.size() + 1) {
				for (Integer patternElement : PATTERN)
				{
					for (int i = 0; i < outputId + 1; i++)
					{
						patternList.add(patternElement);
					}
				}
			}
			patternList.remove(0);
			return patternList;
		}
		
		private static Integer fetchOnesDigit(Integer i) {
			String iStr = i.toString();
			return Integer.parseInt(String.valueOf(iStr.charAt(iStr.length() - 1)));
		}
	}
}
