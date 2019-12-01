package com.jeffrpowell.adventofcode.aoc2018;

import com.jeffrpowell.adventofcode.SlidingWindowSpliterator;
import com.jeffrpowell.adventofcode.Solution;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day12 implements Solution<String>{
	private static final Pattern REGEX = Pattern.compile("(\\.|#)(\\.|#)(\\.|#)(\\.|#)(\\.|#) => (\\.|#)");
	private static List<Pot> pots;
	private static Map<List<Boolean>, Boolean> rules;
	private static long frontId;
	private static long lastId;
    private static String lastPotString = "";
    private static boolean switchModes = false;
    private static long numInfinitePlants = 0;
    private static long score;
	
	@Override
	public String part1(List<String> input)
	{
		return run(input, 20);
	}

	@Override
	public String part2(List<String> input)
	{
		return run(input, 50_000_000_000L);
	}

    private String run(List<String> input, long generations) {
        String state = input.remove(0);
        pots = IntStream.range(0, state.length()).mapToObj(i -> new Pot(i, state.charAt(i))).collect(Collectors.toList());
        rules = input.stream().map(REGEX::matcher).map(Rule::new).collect(Collectors.toMap(
            Rule::getPlantArrangement,
            Rule::getResult
        ));
        frontId = 0;
        lastId = pots.size() - 1;
        LongStream.range(0, generations).forEach(i -> nextGen(i));
        return evaluateResult();
    }
	
	private static void nextGen(long i) {
//        if (i % 1_000_000 == 0) {
//            System.out.println(i);
//        }
        if (switchModes) {
            nextGenShort();
        }
        else {
            nextGenFull();
        }
	}
    
    private static void nextGenFull() {
        trimPotList();
		List<Boolean> newStates = SlidingWindowSpliterator.windowed(pots, 5, new Pot(Long.MIN_VALUE, false))
            .map(stream -> stream.map(Pot::hasAPlant).collect(Collectors.toList()))
            .map(potInContext -> rules.get(potInContext))
            .map(newState -> newState == null ? false : newState)
            .collect(Collectors.toList());
        //newStates should have size greater than pots, such that newStates.size() - pots.size() == 4 (two padded onto each end)
        frontId--;
        pots.add(0, new Pot(frontId, false));
        frontId--;
        pots.add(0, new Pot(frontId, false));
        lastId++;
        pots.add(new Pot(lastId, false));
        lastId++;
        pots.add(new Pot(lastId, false));
        Iterator<Pot> potIterator = pots.iterator();
        Iterator<Boolean> newStateIterator = newStates.iterator();
        while(potIterator.hasNext()) {
            potIterator.next().nextGen(newStateIterator.next());
        }
        String newPotString = getTrimmedPotList();
        if (newPotString.equals(lastPotString)) {
            switchModes = true;
            for (char c : lastPotString.toCharArray()) {
                if (c == '#') {
                    numInfinitePlants++;
                }
            }
            score = pots.stream().filter(Pot::hasAPlant).map(Pot::getId).reduce(0L, Math::addExact);
        }
        else {
            lastPotString = newPotString;
        }
//        System.out.println(pots.get(0).getId() + pots.stream().map(Pot::getPlantString).collect(Collectors.joining()));
//        System.out.println(pots.stream().map(Pot::getId).map(Integer::valueOf).map(i -> i.toString()).collect(Collectors.joining()));
    }
    
    private static void nextGenShort() {
        score += numInfinitePlants;
    }
    
    private static void trimPotList() {
        while(!pots.get(0).hasAPlant()) {
            pots.remove(0);
            frontId++;
        }
        while(!pots.get(pots.size() - 1).hasAPlant()) {
            pots.remove(pots.size() - 1);
            lastId--;
        }
    }
    
    private static String getTrimmedPotList() {
        int trimmedFrontIndex = Integer.MAX_VALUE;
        int trimmedBackIndex = Integer.MIN_VALUE;
        for (int i = 0; i < pots.size(); i++) {
            if (pots.get(i).hasAPlant()) {
                trimmedFrontIndex = i;
                break;
            }
        }
        for (int i = pots.size() - 1; i >= 0; i--) {
            if (pots.get(i).hasAPlant()) {
                trimmedBackIndex = i;
                break;
            }
        }
        return IntStream.rangeClosed(trimmedFrontIndex, trimmedBackIndex).mapToObj(pots::get).map(Pot::getPlantString).collect(Collectors.joining());
    }
	
	private static String evaluateResult() {
		if (switchModes) {
            return Long.toString(score);
        }
        else {
            return Long.toString(pots.stream().filter(Pot::hasAPlant).map(Pot::getId).reduce(0L, Math::addExact));
        }
	}

	private static boolean hasPlant(String s) {
		return s.equals("#");
	}

	private static boolean hasPlant(char c) {
		return c == '#';
	}
	
	private static class Pot {
		long id;
		boolean hasPlant;

		public Pot(long id, char hasPlantC)
		{
			this.id = id;
			this.hasPlant = hasPlant(hasPlantC);
		}

		public Pot(long id, boolean hasPlant)
		{
			this.id = id;
			this.hasPlant = hasPlant;
		}

		public long getId()
		{
			return id;
		}

		public boolean hasAPlant()
		{
			return hasPlant;
		}

		public void nextGen(boolean hasPlant)
		{
			this.hasPlant = hasPlant;
		}
        
        public String getPlantString() {
            return hasPlant ? "#" : ".";
        }

        @Override
        public String toString() {
            return "("+id+") " + getPlantString();
        }
	}
	
	private static class Rule {
		List<Boolean> plantArrangement;
		boolean result;
		
		public Rule(Matcher m) {
            m.find();
			this.plantArrangement = new ArrayList<>();
			plantArrangement.add(hasPlant(m.group(1)));
			plantArrangement.add(hasPlant(m.group(2)));
			plantArrangement.add(hasPlant(m.group(3)));
			plantArrangement.add(hasPlant(m.group(4)));
			plantArrangement.add(hasPlant(m.group(5)));
			result = hasPlant(m.group(6));
		}

		public boolean getResult()
		{
			return result;
		}

        public List<Boolean> getPlantArrangement() {
            return plantArrangement;
        }
        
		public static int hash(List<Boolean> arrangement) {
			int hash = 7;
			hash = 41 * hash + Objects.hashCode(arrangement);
			return hash;
		}

		@Override
		public int hashCode()
		{
			return hash(plantArrangement);
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (obj == null)
			{
				return false;
			}
			if (getClass() != obj.getClass())
			{
				return false;
			}
			final Rule other = (Rule) obj;
			return Objects.equals(this.plantArrangement, other.plantArrangement);
		}
		
	}
}
