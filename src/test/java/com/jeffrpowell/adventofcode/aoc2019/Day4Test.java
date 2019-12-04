package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day4Test
{
	private Solution day;

	@BeforeEach
	public void setUp()
	{
		day = new Day4();
	}

	@Test
	public void testPart1_happyPath()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("1", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart1_failIncreasingDigits()
	{
		List<String> input = TestDataLoader.getTestData(day, 2);
		assertEquals("0", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart1_failDuplicateDigits()
	{
		List<String> input = TestDataLoader.getTestData(day, 3);
		assertEquals("0", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart2_happyPath()
	{
		List<String> input = TestDataLoader.getTestData(day, 4);
		assertEquals("1", day.parseAndRunPart2(input));
	}

	@Test
	public void testPart2_failExactlyTwoDuplicateDigits()
	{
		List<String> input = TestDataLoader.getTestData(day, 5);
		assertEquals("0", day.parseAndRunPart2(input));
	}

	@Test
	public void testPart2_happyPath2()
	{
		List<String> input = TestDataLoader.getTestData(day, 6);
		assertEquals("1", day.parseAndRunPart2(input));
	}
}
