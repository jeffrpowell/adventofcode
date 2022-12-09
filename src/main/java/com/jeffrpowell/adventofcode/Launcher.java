package com.jeffrpowell.adventofcode;

import com.jeffrpowell.adventofcode.aoc2022.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Launcher
{
	public static final Solution<?> DAY = new Day9();
	
    public static void main(String[] args) {
		InputStream puzzleInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(getInputFileName(DAY));
		if (puzzleInputStream == null) {
			System.err.println("Missing input data; expected to find \"" + getInputFileName(DAY) + "\"");
		}
		else {
			List<String> puzzleInput = inputStreamToStringList(puzzleInputStream);
			
			System.out.println("\nPart 1");
			System.out.println("--------------------------");
			long t = System.currentTimeMillis();
			String answer = DAY.parseAndRunPart1(puzzleInput);
			long time = System.currentTimeMillis() - t;
			System.out.println(answer);
			System.out.println("--------------------------");
			System.out.println("Completed part 1 in " + time + " ms");
			System.out.println("\nPart 2");
			System.out.println("--------------------------");
			t = System.currentTimeMillis();
			answer = DAY.parseAndRunPart2(puzzleInput);
			time = System.currentTimeMillis() - t;
			System.out.println(answer);
			System.out.println("--------------------------");
			System.out.println("Completed part 2 in " + time + " ms");
		}
    }
	
	private static String getInputFileName(Solution<?> day) {
		return day.getYear()+"/day"+day.getDay();
	}
	
	private static List<String> inputStreamToStringList(InputStream inputStream) {
		return new BufferedReader(new InputStreamReader(inputStream))
			.lines().collect(Collectors.toList());
	}
}
