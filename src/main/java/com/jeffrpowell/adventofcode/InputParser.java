package com.jeffrpowell.adventofcode;

import java.util.List;

public interface InputParser<T>
{
	public List<T> parseInput(List<String> input);
}
