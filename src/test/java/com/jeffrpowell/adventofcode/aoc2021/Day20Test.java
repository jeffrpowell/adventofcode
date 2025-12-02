package com.jeffrpowell.adventofcode.aoc2021;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;

public class Day20Test
{
	private Solution<?> day;

	@BeforeEach
	public void setUp()
	{
		day = new Day20();
	}

	@Disabled
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("35", day.parseAndRunPart1(input));
	}
	
	@Disabled
	public void testPart2()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("3351", day.parseAndRunPart2(input));
	}
}
