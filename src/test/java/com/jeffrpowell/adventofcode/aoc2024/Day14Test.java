package com.jeffrpowell.adventofcode.aoc2024;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;

import java.awt.geom.Point2D;

public class Day14Test
{
	private Solution<?> day;

	@BeforeEach
	public void setUp()
	{
		day = new Day14(new Point2D.Double(0, 0), new Point2D.Double(10, 6));
	}

	@Test
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("12", day.parseAndRunPart1(input));
	}
}
