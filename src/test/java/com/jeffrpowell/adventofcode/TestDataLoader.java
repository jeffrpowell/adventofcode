package com.jeffrpowell.adventofcode;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class TestDataLoader
{
	private TestDataLoader() {}
	
	public static List<String> getTestData(Solution<?> day, int inputId) {
		InputStream testDataInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(getInputFileName(day, inputId));
		return inputStreamToStringList(testDataInputStream);
	}
	
	private static String getInputFileName(Solution<?> day, int inputId) {
		return day.getYear()+"/day"+day.getDay()+"."+inputId;
	}
	
	private static List<String> inputStreamToStringList(InputStream inputStream) {
		return new BufferedReader(new InputStreamReader(inputStream))
			.lines().collect(Collectors.toList());
	}
}
