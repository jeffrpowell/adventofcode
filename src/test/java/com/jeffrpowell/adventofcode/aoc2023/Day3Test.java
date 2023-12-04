package com.jeffrpowell.adventofcode.aoc2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;

public class Day3Test
{
	private Solution<?> day;

	@BeforeEach
	public void setUp()
	{
		day = new Day3();
	}

	@Test
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("4361", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart2()
	{
		List<String> input = TestDataLoader.getTestData(day, 2);
		assertEquals("467835", day.parseAndRunPart1(input));
	}
}
