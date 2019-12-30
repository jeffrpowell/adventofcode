package com.jeffrpowell.adventofcode;

import com.jeffrpowell.adventofcode.aoc2019.Day25;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Launcher
{
	public static final Solution DAY = new Day25();
	
    public static void main(String[] args) {
		InputStream puzzleInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(getInputFileName(DAY));
		if (puzzleInputStream == null) {
			System.err.println("Missing input data; expected to find \"" + getInputFileName(DAY) + "\"");
		}
		else {
			List<String> puzzleInput = inputStreamToStringList(puzzleInputStream);
			
			System.out.println(DAY.parseAndRunPart1(puzzleInput));
			System.out.println(DAY.parseAndRunPart2(puzzleInput));
		}
    }
	
	private static String getInputFileName(Solution day) {
		return day.getYear()+"/day"+day.getDay();
	}
	
	private static List<String> inputStreamToStringList(InputStream inputStream) {
		return new BufferedReader(new InputStreamReader(inputStream))
			.lines().collect(Collectors.toList());
	}
}
