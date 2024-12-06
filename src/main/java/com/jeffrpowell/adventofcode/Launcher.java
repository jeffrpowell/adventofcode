package com.jeffrpowell.adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.aoc2024.*;

public class Launcher
{
	public static final Solution<?> DAY = new Day6();
	
    public static void main(String[] args) {
		List<String> puzzleInput = getPuzzleInput();
		
		System.out.println();
		System.out.println(DAY.getYear() + " Day " + DAY.getDay());
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

	private static List<String> getPuzzleInput() {
		InputStream puzzleInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(getInputFileName(DAY, false));
		if (puzzleInputStream == null) {
			InputStream sessionToken = Thread.currentThread().getContextClassLoader().getResourceAsStream("sessionToken");
			if (sessionToken == null) {
				throw new IllegalArgumentException("Missing valid session token in src/main/resources/sessionToken");
			}
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://adventofcode.com/"+getInputFileName(DAY, true)+"/input"))
				.header("Cookie", "session="+inputStreamToStringList(sessionToken).get(0))
				.build();
			HttpResponse<String> response;
			try {
				response = client.send(request, BodyHandlers.ofString());
				String body = response.body();
				if (body.charAt(body.length()-1) == '\n');
				{
					body = body.substring(0, body.length() - 1);
				}
				if (response.statusCode() != 200) {
					throw new IllegalArgumentException("Failed to fetch input data; tried \"" + getInputFileName(DAY, true) + "/input\" and got HTTP " + response.statusCode());
				}
				else {
					File resourceFolder = new File("src/main/resources/"+DAY.getYear());
					if (!resourceFolder.exists()){
						resourceFolder.mkdir();
					}
					try (FileWriter f = new FileWriter("src/main/resources/"+getInputFileName(DAY, false)); PrintWriter out = new PrintWriter(f)){
						out.print(body);
					}
					catch (IOException e) {
						throw new IllegalArgumentException("Failed to write input data file", e);
					}
					return Arrays.asList(body.split("\n"));
				}
			} catch (IOException | InterruptedException e) {
				throw new IllegalArgumentException("Failed to fetch input data; tried \"" + getInputFileName(DAY, true) + "/input\" and got exception.", e);
			}
		}
		return inputStreamToStringList(puzzleInputStream);
	}
	
	private static String getInputFileName(Solution<?> day, boolean forWeb) {
		return day.getYear()+"/day"+(forWeb ? "/" : "")+day.getDay();
	}
	
	private static List<String> inputStreamToStringList(InputStream inputStream) {
		return new BufferedReader(new InputStreamReader(inputStream))
			.lines().collect(Collectors.toList());
	}
}
