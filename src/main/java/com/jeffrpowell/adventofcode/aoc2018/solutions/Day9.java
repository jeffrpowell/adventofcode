package com.jeffrpowell.adventofcode.aoc2018.solutions;

import com.jeffrpowell.adventofcode.InputParser;
import com.jeffrpowell.adventofcode.InputParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;

public class Day9 extends Solution2018<Integer>{

	private final List<Integer> circle;
	private final Set<Integer> multsOf23;

	public Day9()
	{
		this.circle = new ArrayList<>();
		circle.add(0);
		this.multsOf23 = new HashSet<>();
	}
	
	@Override
	public int getDay()
	{
		return 25;
	}
	
	@Override
	public InputParser<Integer> getInputParser()
	{
		return InputParserFactory.getIntegerParser();
	}
	
	@Override
	public String part1(List<Integer> input)
	{
		int players = input.get(0);
		int finalMarble = input.get(1);
		for (int mult = 23; mult <= finalMarble; mult += 23)
		{
			multsOf23.add(mult);
		}
		int currentMarbleIndex = 0;
		int currentPlayer = 1;
		Map<Integer, Long> scores = new HashMap<>();
		for (int marble = 1; marble < finalMarble; marble++)
		{
			if (multsOf23.contains(marble)) {
				currentMarbleIndex = scoreRound(currentMarbleIndex, scores, currentPlayer, marble);
				if (marble % 1000 == 0) {
					System.out.println(marble + "/" + finalMarble);
				}
			}
			else {
				currentMarbleIndex = normalRound(currentMarbleIndex, marble);
			}
			currentPlayer = rotatePlayer(currentPlayer, players);
		}
		return Long.toString(scores.values().stream().reduce(0L, BinaryOperator.maxBy(Long::compare)));
	}

	private int scoreRound(int currentMarbleIndex, Map<Integer, Long> scores, int currentPlayer, int marble)
	{
		int indexToRemove = rotateIndexBackward(currentMarbleIndex, 7);
		int removedMarble = circle.remove(indexToRemove);
		scores.put(currentPlayer, scores.computeIfAbsent(currentPlayer, p -> 0L) + marble + removedMarble);
		currentMarbleIndex = indexToRemove;
		return currentMarbleIndex;
	}

	private int normalRound(int currentMarbleIndex, int marble)
	{
		currentMarbleIndex = rotateIndexForward(currentMarbleIndex, 2);
		circle.add(currentMarbleIndex, marble);
		return currentMarbleIndex;
	}
	
	private int rotateIndexForward(int currentIndex, int distance) {
		int i = (currentIndex + distance) % circle.size();
		if (i == 0) {
			return circle.size();
		}
		else {
			return i;
		}
	}

	private int rotatePlayer(int currentPlayer, int players)
	{
		currentPlayer = (currentPlayer + 1) % players;
		if (currentPlayer == 0) {
			currentPlayer = players;
		}
		return currentPlayer;
	}
	
	private int rotateIndexBackward(int currentIndex, int distance) {
		if (currentIndex <= distance) {
			return circle.size() - distance + currentIndex;
		}
		else {
			return currentIndex - distance;
		}
	}

	@Override
	public String part2(List<Integer> input)
	{
		input.set(1, input.get(1) * 100);
		return part1(input);
	}

}
