package com.jeffrpowell.adventofcode;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import java.util.List;

public abstract class Solution<T>
{
	public abstract int getYear();
	public abstract int getDay();
	public abstract InputParser<T> getInputParser();
	public InputParser<T> getInputParserPart1() {
		return getInputParser();
	}
	public InputParser<T> getInputParserPart2() {
		return getInputParser();
	}
	
    protected abstract String part1(List<T> input);
    protected abstract String part2(List<T> input);
	
    public String parseAndRunPart1(List<String> input) {
		return part1(getInputParserPart1().parseInput(input));
	}
    public String parseAndRunPart2(List<String> input) {
		return part2(getInputParserPart2().parseInput(input));
	}
}
