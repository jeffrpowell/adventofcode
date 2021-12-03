package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Day18Test
{
	private Solution<?> day;

	@BeforeEach
	public void setUp()
	{
		day = new Day18();
	}

	@Test
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("51", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart1_2()
	{
		List<String> input = TestDataLoader.getTestData(day, 2);
		assertEquals("13632", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart1_3()
	{
		List<String> input = TestDataLoader.getTestData(day, 3);
		assertEquals("71", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart2()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("51", day.parseAndRunPart1(input));
	}

	@Disabled
	public void testPart2_2()
	{
		List<String> input = TestDataLoader.getTestData(day, 2);
		assertEquals("23340", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart2_3()
	{
		List<String> input = TestDataLoader.getTestData(day, 3);
		assertEquals("231", day.parseAndRunPart2(input));
	}
}
