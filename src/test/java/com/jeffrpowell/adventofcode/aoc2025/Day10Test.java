package com.jeffrpowell.adventofcode.aoc2025;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;

public class Day10Test
{
	private Solution<?> day;

	@BeforeEach
	public void setUp()
	{
		day = new Day10();
	}

	@Test
	public void testPart2()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("3", day.parseAndRunPart2(input));
	}
}
