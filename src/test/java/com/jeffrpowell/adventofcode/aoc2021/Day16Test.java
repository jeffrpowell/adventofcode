package com.jeffrpowell.adventofcode.aoc2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.jeffrpowell.adventofcode.TestDataLoader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day16Test
{
	private Day16 day;

	@BeforeEach
	public void setUp()
	{
		day = new Day16();
	}

	@Test
	public void testPart1()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("16", day.parseAndRunPart1(input));
	}

	@Test
	public void testParseLiteral() {
		List<Boolean> bits = Day16.hexToBits("D2FE28");
		bits = bits.subList(6, bits.size()); //strip off version + typeid
		bits = bits.subList(0, bits.size() - 3); //strip off trailing 0s
		Day16.State s = new Day16.State();
		bits = day.parseLiteral(bits, s);
		assertTrue(bits.isEmpty());
	}

	@Test
	public void testParseOperator0() {
		List<Boolean> bits = Day16.hexToBits("38006F45291200");
		bits = bits.subList(7, bits.size()); //strip off version + typeid + lengthtypeid
		bits = bits.subList(0, bits.size() - 7); //strip off expected trailing 0s
		Day16.State s = new Day16.State();
		bits = day.parseOperator0(bits, s);
		assertTrue(bits.isEmpty());
	}

	@Test
	public void testParseOperator1() {
		List<Boolean> bits = Day16.hexToBits("EE00D40C823060");
		bits = bits.subList(7, bits.size()); //strip off version + typeid + lengthtypeid
		bits = bits.subList(0, bits.size() - 5); //strip off expected trailing 0s
		Day16.State s = new Day16.State();
		bits = day.parseOperator1(bits, s);
		assertTrue(bits.isEmpty());
	}
}
