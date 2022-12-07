package com.jeffrpowell.adventofcode.aoc2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day7Test
{
	private Solution<?> day;

	@BeforeEach
	public void setUp()
	{
		day = new Day7();
	}

	@Test
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("95437", day.parseAndRunPart1(input));
	}
}
