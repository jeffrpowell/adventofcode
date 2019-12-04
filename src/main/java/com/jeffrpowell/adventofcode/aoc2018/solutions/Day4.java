package com.jeffrpowell.adventofcode.aoc2018.solutions;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.List;
import java.util.regex.Pattern;

public class Day4 extends Solution2018<String>
{
    private static final Pattern REGEX = Pattern.compile("\\[1518-(\\d\\d)-(\\d\\d) (\\d\\d):(\\d\\d)] (Guard #(\\d+) begins shift|falls asleep|wakes up)");
    
	@Override
	public int getDay()
	{
		return 4;
	}
	
	@Override
	public InputParser<String> getInputParser()
	{
		return InputParserFactory.getStringParser();
	}
	
    @Override
    public String part1(List<String> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String part2(List<String> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
