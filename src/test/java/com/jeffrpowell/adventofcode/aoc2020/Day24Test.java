package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day24Test
{
	private Solution day;

	@BeforeEach
	public void setUp()
	{
		day = new Day24();
	}

	@Test
	public void testPart2()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("2208", day.parseAndRunPart2(input));
	}
}
