package com.jeffrpowell.adventofcode.aoc2024;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;

public class Day8Test
{
	private Solution<?> day;

	@BeforeEach
	public void setUp()
	{
		day = new Day8();
	}

	@Test
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("14", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart1_easy()
	{
		List<String> input = TestDataLoader.getTestData(day, 2);
		assertEquals("4", day.parseAndRunPart1(input));
	}
}
