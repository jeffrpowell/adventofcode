package com.jeffrpowell.adventofcode.aoc2018;

import com.google.common.collect.Lists;
import com.jeffrpowell.adventofcode.Solution;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day14 implements Solution<Integer>{

	private Map<Integer, Integer> scores = new HashMap<>();
	private List<Integer> answer = new ArrayList<>();
	private AnswerChecker answerChecker;
	private int recipeCap;
	private int currentIndex1;
	private int currentIndex2;
	
	@Override
	public String part1(List<Integer> input)
	{
		scores.put(0, 3);
		scores.put(1, 7);
		currentIndex1 = 0;
		currentIndex2 = 1;
		recipeCap = input.get(0);
		while(answer.size() < 10) {
			iterate();
		}
		return answer.stream().map(i -> i.toString()).collect(Collectors.joining());
	}
	
	private void iterate() {
		int recipe1 = scores.get(currentIndex1);
		int recipe2 = scores.get(currentIndex2);
		Integer sum = recipe1 + recipe2;
		int newScore2 = sum % 10;
		if (sum > 9) {
			int newScore1 = sum / 10;
			scores.put(scores.size(), newScore1);
			if (scores.size() > recipeCap && answer.size() < 10) {
				answer.add(newScore1);
			}
		}
		scores.put(scores.size(), newScore2);
		if (scores.size() > recipeCap && answer.size() < 10) {
			answer.add(newScore2);
		}
		currentIndex1 = rotateIndexForward(currentIndex1, recipe1 + 1);
		currentIndex2 = rotateIndexForward(currentIndex2, recipe2 + 1);
	}

	@Override
	public String part2(List<Integer> input)
	{
		answerChecker = new AnswerChecker(input.get(0));
		scores.put(0, 3);
		scores.put(1, 7);
		currentIndex1 = 0;
		currentIndex2 = 1;
		boolean done = false;
		while(!done) {
			done = iterate2();
		}
		return Integer.toString(answerChecker.getAnswer());
	}
	
	private boolean iterate2() {
		int recipe1 = scores.get(currentIndex1);
		int recipe2 = scores.get(currentIndex2);
		Integer sum = recipe1 + recipe2;
		int newScore2 = sum % 10;
		if (sum > 9) {
			int newScore1 = sum / 10;
			scores.put(scores.size(), newScore1);
			if (answerChecker.check(newScore1)) {
				return true;
			}
		}
		scores.put(scores.size(), newScore2);
		if (answerChecker.check(newScore2)) {
			return true;
		}
		currentIndex1 = rotateIndexForward(currentIndex1, recipe1 + 1);
		currentIndex2 = rotateIndexForward(currentIndex2, recipe2 + 1);
		return false;
	}

	private int rotateIndexForward(int currentIndex, int distance) {
		return (currentIndex + distance) % scores.size();
	}
	
	private static class AnswerChecker {
		private int currentIndex;
		private List<Integer> target;
		private int checkCount;

		public AnswerChecker(int targetSequence)
		{
			this.target = new ArrayList<>();
			while (targetSequence != 0) {
				this.target.add(targetSequence % 10);
				targetSequence /= 10;
			}
			this.target = Lists.reverse(this.target);
			currentIndex = 0;
			checkCount = 2;
		}
		
		public boolean check(int input) {
			checkCount++;
			if (target.get(currentIndex) == input) {
				currentIndex++;
				if (currentIndex == target.size()) {
					return true;
				}
			}
			else {
				currentIndex = 0;
			}
			return false;
		}

		public int getAnswer()
		{
			return checkCount - target.size();
		}
		
	}
}
