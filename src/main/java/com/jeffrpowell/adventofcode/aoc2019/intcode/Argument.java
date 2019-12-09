package com.jeffrpowell.adventofcode.aoc2019.intcode;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class Argument {
	private enum Mode {
		POSITION(0, (argValue, tape, relativeBase) -> getValueFromTape(argValue.intValue(), tape)), 
		IMMEDIATE(1, (argValue, tape, relativeBase) -> argValue),
		RELATIVE(2, (argValue, tape, relativeBase) -> getValueFromTape(argValue.intValue() + relativeBase, tape));
		
		private static BigInteger getValueFromTape(int position, List<BigInteger> tape) {
			if (position >= tape.size()) {
				return BigInteger.ZERO;
			}
			return tape.get(position);
		}
		
		private final int modeCode;
		private final ArgFetchFunction howToGetValue;

		private Mode(int modeCode, ArgFetchFunction howToGetValue)
		{
			this.modeCode = modeCode;
			this.howToGetValue = howToGetValue;
		}

		public static Mode fromModeCode(int modeCode) {
			return Arrays.stream(values()).filter(mode -> mode.modeCode == modeCode).findAny().orElse(null);
		}
		
		public BigInteger getValue(BigInteger argValue, List<BigInteger> tape, int relativeBase) {
			return howToGetValue.apply(argValue, tape, relativeBase);
		}
	}
	private final Mode parameterMode;
	private final BigInteger value;
	private final int argPosition;
	private final int relativeBase;

	public Argument(int parameterMode, BigInteger value, int argPosition, int relativeBase)
	{
		this.parameterMode = Mode.fromModeCode(parameterMode);
		this.value = value;
		this.argPosition = argPosition;
		this.relativeBase = relativeBase;
	}

	public BigInteger getValue(List<BigInteger> tape) {
		return parameterMode.getValue(value, tape, relativeBase);
	}
	
	/**
	 * Override to allow for position-writing arguments to get the raw value, despite being in position mode
	 * @return 
	 */
	public BigInteger getValue() {
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
	
	@FunctionalInterface
	private static interface ArgFetchFunction {
		public BigInteger apply (BigInteger argValue, List<BigInteger> tape, int relativeBase);
	}
}
