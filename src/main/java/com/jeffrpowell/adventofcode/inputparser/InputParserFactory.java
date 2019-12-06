package com.jeffrpowell.adventofcode.inputparser;

import java.util.List;

public class InputParserFactory
{
	private static final InputParser<Integer> INTEGER_PARSER = new IntegerParser();
	private static final InputParser<String> STRING_PARSER = new StringParser();
	private static final InputParser<List<Integer>> INT_CSV_PARSER = new IntegerCSVParser();
	private static final InputParser<List<String>> CSV_PARSER = new TokenSVParser(",");
	
	private InputParserFactory() {}
	
	public static InputParser<Integer> getIntegerParser() {
		return INTEGER_PARSER;
	}
	
	public static InputParser<String> getStringParser() {
		return STRING_PARSER;
	}
	
	public static InputParser<List<Integer>> getIntegerCSVParser() {
		return INT_CSV_PARSER;
	}
	
	public static InputParser<List<String>> getCSVParser() {
		return CSV_PARSER;
	}
	
	public static InputParser<List<String>> getTokenSVParser(String delimiter) {
		return new TokenSVParser(delimiter);
	}
}
