package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.TestDataLoader;
import java.util.List;
import org.junit.Before;
import org.junit.jupiter.api.Test;

public class Day1Test
{
	private Day1 day;

	@Before
	public void setUp()
	{
		day = new Day1();
	}

	@Test
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		day.parseAndRunPart1(input);
	}

	@Test
	public void testPart2()
	{
	}
}
