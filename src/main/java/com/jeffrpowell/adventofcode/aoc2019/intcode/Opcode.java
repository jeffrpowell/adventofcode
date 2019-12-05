package com.jeffrpowell.adventofcode.aoc2019.intcode;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public enum Opcode {
	ADD(1, 3, (List<Argument> args, List<Integer> tape) -> 
		tape.set(args.get(2).getValue(), getArgValue(args.get(0), tape) + getArgValue(args.get(1), tape))), 
	MULTIPLY(2, 3, (List<Argument> args, List<Integer> tape) -> 
		tape.set(args.get(2).getValue(), getArgValue(args.get(0), tape) * getArgValue(args.get(1), tape))), 
	INPUT(3, 1, (List<Argument> args, List<Integer> tape) -> 
		tape.set(args.get(0).getValue(), Opcode.collectInput())), 
	OUTPUT(4, 1, (List<Argument> args, List<Integer> tape) -> {
		writeOutput(getArgValue(args.get(0), tape)); return 1;
	}), 
	HALT(99, 0, (List<Argument> args, List<Integer> tape) -> 0);
			
	private final int code;
	private final int args;
	private final BiFunction<List<Argument>, List<Integer>, Integer> howToExecute;

	private Opcode(int code, int args, BiFunction<List<Argument>, List<Integer>, Integer> howToExecute)
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
	
	private static int getArgValue(Argument arg, List<Integer> tape) {
		return arg.getValue(tape);
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
		howToExecute.apply(args, newTape);
		return newTape;
	}
}
