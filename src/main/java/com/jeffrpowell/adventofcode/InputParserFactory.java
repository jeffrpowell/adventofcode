package com.jeffrpowell.adventofcode;

public class InputParserFactory
{
	private static final InputParser<Integer> INTEGER_PARSER = new IntegerParser();
	private static final InputParser<String> STRING_PARSER = new StringParser();
	
	private InputParserFactory() {}
	
	public static InputParser<Integer> getIntegerParser() {
		return INTEGER_PARSER;
	}
	
	public static InputParser<String> getStringParser() {
		return STRING_PARSER;
	}
}
