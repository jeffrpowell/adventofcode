package com.jeffrpowell.adventofcode.aoc2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;

public class Day13Test
{
	private Solution<?> day;

	@BeforeEach
	public void setUp()
	{
		day = new Day13();
	}

	@Test
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("405", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart1_liveExample()
	{
		List<String> input = TestDataLoader.getTestData(day, 2);
		assertEquals("8", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart1_liveExample2()
	{
		List<String> input = TestDataLoader.getTestData(day, 3);
		assertEquals("400", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart2()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("400", day.parseAndRunPart2(input));
	}

	@Test
	public void testPart1_liveExample3()
	{
		List<String> input = TestDataLoader.getTestData(day, 4);
		assertEquals("1000", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart2_liveExample3()
	{
		List<String> input = TestDataLoader.getTestData(day, 4);
		assertEquals("200", day.parseAndRunPart2(input));
	}
}
