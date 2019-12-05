package com.jeffrpowell.adventofcode.aoc2019.intcode;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public enum Opcode {
	ADD(1, 3, (List<Integer> args, List<Integer> tape) -> tape.set(args.get(2), args.get(0) + args.get(1))), 
	MULTIPLY(2, 3, (List<Integer> args, List<Integer> tape) -> tape.set(args.get(2), args.get(0) * args.get(1))), 
	INPUT(3, 1, (List<Integer> args, List<Integer> tape) -> tape.set(args.get(0), Opcode.collectInput())), 
	OUTPUT(4, 1, (List<Integer> args, List<Integer> tape) -> {writeOutput(args.get(0)); return 1;}), 
	HALT(99, 0, (List<Integer> args, List<Integer> tape) -> 0);
			
	private final int code;
	private final int args;
	private final BiFunction<List<Integer>, List<Integer>, Integer> howToExecute;

	private Opcode(int code, int args, BiFunction<List<Integer>, List<Integer>, Integer> howToExecute)
	{
		this.code = code;
		this.args = args;
		this.howToExecute = howToExecute;
	}

	/**
	 * Must not contain parameter modes in the input
	 * @param code
	 * @return 
	 */
	public static Opcode fromCode(int code) {
		return Arrays.stream(values()).filter(opcode -> opcode.code == code).findAny().orElse(null);
	}
	
	/**
	 * Will contain parameter modes in the number. Be sure to check just the final two digits.
	 * @param codeAndModes
	 * @return 
	 */
	public static Opcode fromCodeAndModes(int codeAndModes) {
		int code = codeAndModes % 100;
		return fromCode(code);
	}
	
	private static int collectInput() {
		System.out.println("\nPlease provide an input int:");
		Scanner in = new Scanner(System.in);
		return in.nextInt();
	}
	
	private static void writeOutput(int output) {
		System.out.println(output);
	}

	public int getArgs()
	{
		return args;
	}
	
	public List<Integer> execute(List<Argument> args, List<Integer> tape) {
		List<Integer> newTape = tape.stream().collect(Collectors.toList());
		howToExecute.apply(args.stream().map(arg -> arg.getValue(newTape)).collect(Collectors.toList()), newTape);
		return newTape;
	}
}
