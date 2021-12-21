package com.jeffrpowell.adventofcode.aoc2021;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		assertEquals("739785", day.parseAndRunPart1(input));
	}
}
