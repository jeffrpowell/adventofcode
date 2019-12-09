package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day9Test
{
	private Solution day;

	@BeforeEach
	public void setUp()
	{
		day = new Day9();
	}

	@Test
	public void testPart1_useAdditionalMemory()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("109\n1\n204\n-1\n1001\n100\n1\n100\n1008\n100\n16\n101\n1006\n101\n0\n99", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart1_bigIntOperation()
	{
		List<String> input = TestDataLoader.getTestData(day, 2);
		assertEquals("1219070632396864", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart1_bigIntValue()
	{
		List<String> input = TestDataLoader.getTestData(day, 3);
		assertEquals("1125899906842624", day.parseAndRunPart1(input));
	}
}
