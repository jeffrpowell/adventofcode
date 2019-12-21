package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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
		try
		{
			List<Integer> phase0Input = input.get(0);
			Phase phase0 = new Phase(0, phase0Input);
			Phase phase1 = new Phase(1, phase0Input);
			Phase phase2 = new Phase(2, phase0Input);
			Phase phase3 = new Phase(3, phase0Input);
			Phase phase4 = new Phase(4, phase0Input);
			Phase phase5 = new Phase(5, phase0Input);
			Phase phase6 = new Phase(6, phase0Input);
			Phase phase7 = new Phase(7, phase0Input);
			List<Integer> newInput = new ArrayList<>();
			for (int i = 0; i < 100; i++)
			{
				try
				{
					newInput.add(phase0.call());
					newInput.add(phase1.call());
					newInput.add(phase2.call());
					newInput.add(phase3.call());
					newInput.add(phase4.call());
					newInput.add(phase5.call());
					newInput.add(phase6.call());
					newInput.add(phase7.call());
					newInput.clear();
					phase0 = new Phase(0, newInput);
					phase1 = new Phase(1, newInput);
					phase2 = new Phase(2, newInput);
					phase3 = new Phase(3, newInput);
					phase4 = new Phase(4, newInput);
					phase5 = new Phase(5, newInput);
					phase6 = new Phase(6, newInput);
					phase7 = new Phase(7, newInput);
				}
				catch (Exception ex)
				{
				}
			}
			System.out.println(phase0.call());
			System.out.println(phase1.call());
			System.out.println(phase2.call());
			System.out.println(phase3.call());
			System.out.println(phase4.call());
			System.out.println(phase5.call());
			System.out.println(phase6.call());
			System.out.println(phase7.call());
		}
		catch (Exception ex)
		{
		}
	}

	@Override
	protected String part2(List<List<Integer>> input)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	private static class Phase implements Callable<Integer>{
		private static final List<Integer> PATTERN = List.of(0, 1, 0, -1);
		private final int outputId;
		private final List<Integer> input;

		public Phase(int outputId, List<Integer> input)
		{
			this.outputId = outputId;
			this.input = input;
		}

		@Override
		public Integer call() throws Exception
		{
			List<Integer> output = new ArrayList<>();
			List<Integer> patternList = createPatternList();
			for (int i = 0; i < 8; i++)
			{
				output.add(input.get(i) * patternList.get(i));
			}
			return output.stream().reduce(0, Integer::sum, Integer::sum) / 10_000_000;
		}

		private List<Integer> createPatternList() {
			List<Integer> patternList = new ArrayList<>();
			while(patternList.size() < 8) {
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
	}
}
