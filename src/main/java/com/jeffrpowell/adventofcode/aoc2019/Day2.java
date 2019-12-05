package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.aoc2019.intcode.IntCodeComputer;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 extends Solution2019<List<Integer>>{

	@Override
	public int getDay()
	{
		return 2;
	}

	@Override
	public InputParser<List<Integer>> getInputParser()
	{
		return InputParserFactory.getIntegerCSVParser();
	}

	@Override
	protected String part1(List<List<Integer>> inputWrapper)
	{
		List<Integer> input = inputWrapper.get(0);
		return IntCodeComputer.executeProgram(input).toString();
	}
	
	@Override
	protected String part2(List<List<Integer>> inputWrapper)
	{
		List<Integer> input = inputWrapper.get(0);
		//part 1 asked you to manually change the day's input. 
		input.set(1, 0); //I don't want to set multiple inputs for the day and I want part 1 to work out of the box
		input.set(2, 0); //Manually setting things to how they originally were in code here
		
		String targetOutput = "19690720";
		for (int noun = 0; noun < 100; noun++)
		{
			for (int verb = 0; verb < 100; verb++)
			{
				List<Integer> testInput = input.stream().collect(Collectors.toList());
				testInput.set(1, noun);
				testInput.set(2, verb);
				if (part1(Collections.singletonList(testInput)).equals(targetOutput)) {
					return Integer.toString(100 * noun + verb);
				}
			}
		}
		return "IDK!";
	}

}
