package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day19Test
{
	private Solution<?> day;

	@BeforeEach
	public void setUp()
	{
		day = new Day19();
	}

    @Test
	public void testPart2()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("12", day.parseAndRunPart2(input));
	}

}
