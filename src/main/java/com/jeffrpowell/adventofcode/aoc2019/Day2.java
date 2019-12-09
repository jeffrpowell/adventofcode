package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.aoc2019.intcode.IntCodeComputer;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class Day2 extends Solution2019<List<BigInteger>>{

	@Override
	public int getDay()
	{
		return 2;
	}

	@Override
	public InputParser<List<BigInteger>> getInputParser()
	{
		return InputParserFactory.getBigIntegerCSVParser();
	}

	@Override
	protected String part1(List<List<BigInteger>> input)
	{
		IntCodeComputer computer = new IntCodeComputer(input.get(0), new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>());
		computer.executeProgram();
		return computer.getTapePosition(0).toString();
	}
	
	@Override
	protected String part2(List<List<BigInteger>> inputWrapper)
	{
		List<BigInteger> input = inputWrapper.get(0);
		//part 1 asked you to manually change the day's input. 
		input.set(1, BigInteger.ZERO); //I don't want to set multiple inputs for the day and I want part 1 to work out of the box
		input.set(2, BigInteger.ZERO); //Manually setting things to how they originally were in code here
		
		String targetOutput = "19690720";
		for (int noun = 0; noun < 100; noun++)
		{
			for (int verb = 0; verb < 100; verb++)
			{
				List<BigInteger> testInput = input.stream().collect(Collectors.toList());
				testInput.set(1, BigInteger.valueOf(noun));
				testInput.set(2, BigInteger.valueOf(verb));
				if (part1(Collections.singletonList(testInput)).equals(targetOutput)) {
					return Integer.toString(100 * noun + verb);
				}
			}
		}
		return "IDK!";
	}

}
