package com.jeffrpowell.adventofcode.aoc2024;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;

import java.awt.geom.Point2D;

public class Day18Test
{
	private Solution<?> day;

	@BeforeEach
	public void setUp()
	{
		day = new Day18(new Point2D.Double(0, 0), new Point2D.Double(6, 6), 12);
	}

	@Test
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("22", day.parseAndRunPart1(input));
	}

	@Test
	public void testPart2()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("6.0,1.0", day.parseAndRunPart2(input));
	}
}
