package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day10Test
{
	private Solution day;

	@BeforeEach
	public void setUp()
	{
		day = new Day10();
	}

	@Test
	public void testPart1_small()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("8", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart1_large()
	{
		List<String> input = TestDataLoader.getTestData(day, 2);
		assertEquals("210", day.parseAndRunPart1(input));
	}
}
