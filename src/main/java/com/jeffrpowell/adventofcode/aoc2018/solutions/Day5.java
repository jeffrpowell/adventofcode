package com.jeffrpowell.adventofcode.aoc2018.solutions;

import com.jeffrpowell.adventofcode.InputParser;
import com.jeffrpowell.adventofcode.InputParserFactory;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day5 extends Solution2018<String>{

	private static final String REGEX = "aA|Aa|bB|Bb|cC|Cc|dD|Dd|eE|Ee|fF|Ff|gG|Gg|hH|Hh|iI|Ii|jJ|Jj|kK|Kk|lL|Ll|mM|Mm|nN|Nn|oO|Oo|pP|Pp|qQ|Qq|rR|Rr|sS|Ss|tT|Tt|uU|Uu|vV|Vv|wW|Ww|xX|Xx|yY|Yy|zZ|Zz";
	
	@Override
	public int getDay()
	{
		return 5;
	}
	
	@Override
	public InputParser<String> getInputParser()
	{
		return InputParserFactory.getStringParser();
	}
	
	@Override
	public String part1(List<String> input)
	{
		String shrunkenStr = reactPolymer(input.get(0));
		return Integer.toString(shrunkenStr.length());
	}

	@Override
	public String part2(List<String> input)
	{
		String polymer = input.get(0);
		return Integer.toString(Stream.of("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z")
			.map(c -> Pattern.compile(c, Pattern.CASE_INSENSITIVE).matcher(polymer).replaceAll(""))
			.map(this::reactPolymer)
			.map(String::length)
			.reduce(Integer.MAX_VALUE, BinaryOperator.minBy(Integer::compare)));
	}

	private String reactPolymer(String polymer) {
		boolean digest = true; //like AngularJs digest
		while(digest) {
			digest = false;
			String evalStr = polymer.replaceAll(REGEX, "");
			if (!evalStr.equals(polymer)) {
				polymer = evalStr;
				digest = true;
			}
		}
		return polymer;
	}
}
