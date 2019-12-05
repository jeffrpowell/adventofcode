package com.jeffrpowell.adventofcode.aoc2019.intcode;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class Argument {
	private enum Mode {
		POSITION(0, (argValue, tape) -> tape.get(argValue)), 
		IMMEDIATE(1, (argValue, tape) -> argValue);
		
		private final int modeCode;
		private final BiFunction<Integer, List<Integer>, Integer> howToGetValue;

		private Mode(int modeCode, BiFunction<Integer, List<Integer>, Integer> howToGetValue)
		{
			this.modeCode = modeCode;
			this.howToGetValue = howToGetValue;
		}

		public static Mode fromModeCode(int modeCode) {
			return Arrays.stream(values()).filter(mode -> mode.modeCode == modeCode).findAny().orElse(null);
		}
		
		public Integer getValue(int argValue, List<Integer> tape) {
			return howToGetValue.apply(argValue, tape);
		}
	}
	private final Mode parameterMode;
	private final int value;
	private final int argPosition;

	public Argument(int parameterMode, int value, int argPosition)
	{
		this.parameterMode = Mode.fromModeCode(parameterMode);
		this.value = value;
		this.argPosition = argPosition;
	}

	public int getValue(List<Integer> tape) {
		return parameterMode.getValue(value, tape);
	}
	
	/**
	 * Override to allow for position-writing arguments to get the raw value, despite being in position mode
	 * @return 
	 */
	public int getValue() {
		return value;
	}

	public int getArgPosition()
	{
		return argPosition;
	}
	
	@Override
	public String toString() {
		return "["+parameterMode.modeCode+"]"+value;
	}
}
