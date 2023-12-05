package com.jeffrpowell.adventofcode.aoc2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jeffrpowell.adventofcode.TestDataLoader;

public class Day5Test
{
	private Day5 day;

	@BeforeEach
	public void setUp()
	{
		day = new Day5();
	}

	@Test
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("35", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart2()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("46", day.parseAndRunPart2(input));
	}
}