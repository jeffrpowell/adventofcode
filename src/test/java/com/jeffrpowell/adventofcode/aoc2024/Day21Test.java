package com.jeffrpowell.adventofcode.aoc2024;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;

public class Day21Test
{
	private Solution<?> day;

	@BeforeEach
	public void setUp()
	{
		day = new Day21();
	}

	@Test
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("126384", day.parseAndRunPart1(input));
	}
}
