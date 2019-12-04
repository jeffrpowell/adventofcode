package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.InputParser;
import com.jeffrpowell.adventofcode.InputParserFactory;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 extends Solution2019<Integer>{

	@Override
	public int getDay()
	{
		return 2;
	}

	@Override
	public InputParser<Integer> getInputParser()
	{
		return InputParserFactory.getIntegerCSVParser();
	}

	@Override
	protected String part1(List<Integer> input)
	{
		int i = 0;
		Instruction instruction;
		do {
			instruction = new Instruction(input.get(i++), getOrNull(input, i++), getOrNull(input, i++), getOrNull(input, i++));
			input = instruction.executeOperation(input);
		} while (!instruction.isHalt());
		return input.get(0).toString();
	}
	
	private static Integer getOrNull(List<Integer> tape, int i) {
		if (i > tape.size() ) {
			return 0;
		}
		else {
			return tape.get(i);
		}
	}
	
	private static class Instruction {
		private enum Opcode {
			ADD(1), MULTIPLY(2), HALT(99);
			
			private final int code;
			Opcode(int code) {
				this.code = code;
			}
			
			private static Opcode fromCode(int code) {
				return Arrays.stream(values()).filter(opcode -> opcode.code == code).findAny().orElse(null);
			}
		}
		private final Opcode opcode;
		private final Integer arg1Pointer;
		private final Integer arg2Pointer;
		private final Integer targetPointer;

		public Instruction(int opcode, Integer arg1Pointer, Integer arg2Pointer, Integer targetPointer)
		{
			this.opcode = Opcode.fromCode(opcode);
			this.arg1Pointer = arg1Pointer;
			this.arg2Pointer = arg2Pointer;
			this.targetPointer = targetPointer;
		}
		
		public boolean isHalt() {
			return opcode == Opcode.HALT;
		}
		
		public List<Integer> executeOperation(List<Integer> tape) {
			if (isHalt()) {
				return tape;
			}
			List<Integer> newTape = tape.stream().collect(Collectors.toList());
			int arg1 = newTape.get(arg1Pointer);
			int arg2 = newTape.get(arg2Pointer);
			if (opcode == Opcode.ADD) {
				newTape.set(targetPointer, arg1 + arg2);
			}
			if (opcode == Opcode.MULTIPLY) {
				newTape.set(targetPointer, arg1 * arg2);
			}
			return newTape;
		}
	}

	@Override
	protected String part2(List<Integer> input)
	{
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
				if (part1(testInput).equals(targetOutput)) {
					return Integer.toString(100 * noun + verb);
				}
			}
		}
		return "IDK!";
	}

}
