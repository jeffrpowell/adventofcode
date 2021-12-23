package com.jeffrpowell.adventofcode.aoc2021;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day18Test
{
	private Day18 day;

	@BeforeEach
	public void setUp()
	{
		day = new Day18();
	}

	@Test
	public void testParseInput_simple()
	{
		String input = "[1,2]";
		Day18.Pair expected = new Day18.Pair(new Day18.Num(1), new Day18.Num(2));
		Day18.Pair actual = day.parseInput(input);
		assertEquals(expected, actual);
	}

	@Test
	public void testParseInput_nestedLeftTwoLevels()
	{
		String input = "[[[1,2],3],4]";
		Day18.Pair expected = 
			new Day18.Pair( 
				new Day18.Pair( 
					new Day18.Pair( 
						new Day18.Num(1), 
						new Day18.Num(2)
						),
					new Day18.Num(3)
					),
				new Day18.Num(4)
			);
		Day18.Pair actual = day.parseInput(input);
		assertEquals(expected, actual);
	}

	@Test
	public void testParseInput_nestedRightTwoLevels()
	{
		String input = "[1,[2,[3,4]]]";
		Day18.Pair expected = 
			new Day18.Pair(
				new Day18.Num(1),
				new Day18.Pair( 
					new Day18.Num(2),
					new Day18.Pair( 
						new Day18.Num(3), 
						new Day18.Num(4)
					)
				)
			);
		Day18.Pair actual = day.parseInput(input);
		assertEquals(expected, actual);
	}

	@Test
	public void testParseInput_complex()
	{
		String input = "[[2,[[2,8],[3,3]]],[[[1,9],9],6]]";
		Day18.Pair expected = 
			new Day18.Pair(
				new Day18.Pair( 
					new Day18.Num(2),
					new Day18.Pair( 
						new Day18.Pair(
							new Day18.Num(2),
							new Day18.Num(8)
						),
						new Day18.Pair( 
							new Day18.Num(3), 
							new Day18.Num(3)
						)
					)
				),
				new Day18.Pair( 
					new Day18.Pair( 
						new Day18.Pair( 
							new Day18.Num(1),
							new Day18.Num(9)
						),
						new Day18.Num(9)
					),
					new Day18.Num(6)
				)
			);
		Day18.Pair actual = day.parseInput(input);
		assertEquals(expected, actual);
	}
}
