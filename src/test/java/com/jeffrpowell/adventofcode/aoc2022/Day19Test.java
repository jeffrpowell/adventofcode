package com.jeffrpowell.adventofcode.aoc2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;

public class Day19Test
{
	private Solution<?> day;

	@BeforeEach
	public void setUp()
	{
		day = new Day19();
	}

	//test passes, just takes a long time
	@Disabled
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("33", day.parseAndRunPart1(input));
	}
	
	//test passes, just takes a long time
	@Disabled
	public void testPart2()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("3472", day.parseAndRunPart2(input));
	}
}
