package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day2Test
{
	private Solution day;

	@BeforeEach
	public void setUp()
	{
		day = new Day2();
	}

	@Test
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("3500", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart2()
	{
//		List<String> input = TestDataLoader.getTestData(day, 2);
//		assertEquals("50346", day.parseAndRunPart2(input));
	}
}
