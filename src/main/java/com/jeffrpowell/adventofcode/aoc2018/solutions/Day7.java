package com.jeffrpowell.adventofcode.aoc2018.solutions;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SortedSetMultimap;
import com.jeffrpowell.adventofcode.InputParser;
import com.jeffrpowell.adventofcode.InputParserFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day7 extends Solution2018<String>{

	private static final Pattern REGEX = Pattern.compile("Step (.) must be finished before step (.) can begin\\.");
	private final SortedSetMultimap<String, String> prereqs;

	public Day7()
	{
		this.prereqs = MultimapBuilder.treeKeys().treeSetValues().build();
	}
	
	@Override
	public int getDay()
	{
		return 7;
	}
	
	@Override
	public InputParser<String> getInputParser()
	{
		return InputParserFactory.getStringParser();
	}
	
	@Override
	public String part1(List<String> input)
	{
		input.stream().map(REGEX::matcher).forEach(m -> {
			m.find();
			prereqs.put(m.group(2), m.group(1));
		});
		Set<String> allSteps = prereqs.entries().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
		allSteps.addAll(prereqs.entries().stream().map(Map.Entry::getKey).collect(Collectors.toSet()));
		List<String> steps = new ArrayList<>();
		while(!prereqs.isEmpty()) {
			steps.add(iterate());
		}
		allSteps.removeAll(steps);
		allSteps.stream().sorted().forEach(steps::add);
		return steps.stream().collect(Collectors.joining());
	}
	
	private String iterate() {
		Set<String> allSteps = prereqs.entries().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
		String stepReadyToExecute = allSteps.stream().filter(step -> !prereqs.containsKey(step)).sorted(String::compareTo).findFirst().get();
		Set<String> keysThatNeedToBeCleaned = prereqs.entries().stream().filter(entry -> stepReadyToExecute.equals(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toSet());
		keysThatNeedToBeCleaned.stream().forEach(key -> prereqs.remove(key, stepReadyToExecute));
		return stepReadyToExecute;
	}

	@Override
	public String part2(List<String> input)
	{
		input.stream().map(REGEX::matcher).forEach(m -> {
			m.find();
			prereqs.put(m.group(2), m.group(1));
		});
		Set<String> allSteps = prereqs.entries().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
		allSteps.addAll(prereqs.entries().stream().map(Map.Entry::getKey).collect(Collectors.toSet()));
		int target = allSteps.size();
		List<String> steps = new ArrayList<>();
		int t = 0;
		List<Worker> workers = Stream.generate(Worker::new).limit(5).collect(Collectors.toList());
		while(steps.size() < target) {
			List<String> stepsDone = iterate(t, workers, allSteps);
			steps.addAll(stepsDone);
			allSteps.removeAll(stepsDone);
			t++;
		}
		return Integer.toString(t - 1);
	}
	
	private List<String> iterate(int t, List<Worker> workers, Set<String> lastSteps) {
		List<String> stepsDone = new ArrayList<>();
		List<String> stepsStillCooking = new ArrayList<>();
		for (Worker worker : workers)
		{
			if (worker.isActive()) {
				if (worker.isFinished(t)) {
					stepsDone.add(worker.step);
					worker.reset();
				}
				else {
					stepsStillCooking.add(worker.step);
				}
			}
		}
		Set<String> keysThatNeedToBeCleaned = prereqs.entries().stream().filter(entry -> stepsDone.contains(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toSet());
		for (String key : keysThatNeedToBeCleaned)
		{
			for (String step : stepsDone)
			{
				prereqs.remove(key, step);
				lastSteps.remove(step);
			}
		}
		Set<String> allSteps = prereqs.entries().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
		List<String> stepsReadyToExecute = allSteps.stream().filter(step -> !prereqs.containsKey(step)).sorted(String::compareTo).collect(Collectors.toList());
		if (stepsReadyToExecute.isEmpty()) {
			stepsReadyToExecute.addAll(lastSteps);
		}
		stepsReadyToExecute.removeAll(stepsStillCooking);
		for (Worker worker : workers)
		{
			if (stepsReadyToExecute.isEmpty()) {
				break;
			}
			if (!worker.isActive()){
				worker.reset(stepsReadyToExecute.remove(0), t);
			}
		}
		return stepsDone;
	}
	
	private static class Worker {
		String step;
		int startingSecond;
		boolean active;

		public Worker() {
			this.active = false;
		}
		
		public Worker(String step, int startingSecond)
		{
			this.step = step;
			this.startingSecond = startingSecond;
			this.active = true;
		}
		
		public void reset(String step, int startingSecond) {
			this.step = step;
			this.startingSecond = startingSecond;
			this.active = true;
		}
		
		public void reset() {
			this.active = false;
			this.step = null;
			this.startingSecond = -1;
		}
		
		public boolean isActive() {
			return active;
		}
		
		public boolean isFinished(int second) {
			return active ? timeRequirement() + startingSecond == second : false;
		}
		
		private int timeRequirement() {
			return 61 + step.charAt(0) - 'A';
		}
	}

}
