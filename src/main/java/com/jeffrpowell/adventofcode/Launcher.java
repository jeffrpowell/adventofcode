package com.jeffrpowell.adventofcode;

import com.jeffrpowell.adventofcode.aoc2018.solutions.Day25;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Launcher
{
	public static final Solution DAY = new Day25();
	
    public static void main(String[] args) {
		InputStream part1InputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(getInputFileName(DAY, 1));
		List<String> part1Input = inputStreamToStringList(part1InputStream);
        System.out.println(DAY.parseAndRunPart1(part1Input));
		
		InputStream part2InputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(getInputFileName(DAY, 2));
		List<String> part2Input = inputStreamToStringList(part2InputStream);
        System.out.println(DAY.parseAndRunPart2(part2Input));
    }
	
	private static String getInputFileName(Solution day, int partNumber) {
		return "/"+day.getYear()+"/day"+day.getDay()+"."+partNumber;
	}
	
	private static List<String> inputStreamToStringList(InputStream inputStream) {
		return new BufferedReader(new InputStreamReader(inputStream))
			.lines().collect(Collectors.toList());
	}
}
