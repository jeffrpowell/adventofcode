package com.jeffrpowell.adventofcode.aoc2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;

public class Day17Test
{
	private Solution<?> day;

	@BeforeEach
	public void setUp()
	{
		day = new Day17();
	}

	@Test
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("3068.0", day.parseAndRunPart1(input));
	}

}
