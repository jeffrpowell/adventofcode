package com.jeffrpowell.adventofcode.aoc2015;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day6Test
{
	private Solution day;

	@BeforeEach
	public void setUp()
	{
		day = new Day6();
	}

	@Test
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("1000", day.parseAndRunPart1(input));
	}

}
