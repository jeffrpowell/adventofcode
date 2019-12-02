package com.jeffrpowell.adventofcode;

import java.util.List;

public abstract class Solution<T>
{
	public abstract int getYear();
	public abstract int getDay();
	public abstract InputParser<T> getInputParser();
	
    protected abstract String part1(List<T> input);
    protected abstract String part2(List<T> input);
	
    public String parseAndRunPart1(List<String> input) {
		return part1(getInputParser().parseInput(input));
	}
    public String parseAndRunPart2(List<String> input) {
		return part2(getInputParser().parseInput(input));
	}
}
