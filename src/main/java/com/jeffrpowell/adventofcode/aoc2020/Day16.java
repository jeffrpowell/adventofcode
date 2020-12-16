package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16 extends Solution2020<Day16.CompositeInput>{

	@Override
	public int getDay()
	{
		return 16;
	}
	
	public static class CompositeInput {
		static final Pattern RULE_PATTERN = Pattern.compile("^(\\w+\\s*\\w+): (\\d+)-(\\d+) or (\\d+)-(\\d+)");
		Map<String, Predicate<Integer>> rules;
		List<Long> myTicket;
		List<List<Integer>> nearbyTickets;
		
		public CompositeInput(){
			rules = new HashMap<>();
			myTicket = new ArrayList<>();
			nearbyTickets = new ArrayList<>();
		}
		
		public void putNewRule(String name, Predicate<Integer> p) {
			rules.put(name, p);
		}

		public void setMyTicket(List<Long> myTicket)
		{
			this.myTicket = myTicket;
		}

		public void setNearbyTickets(List<List<Integer>> nearbyTickets)
		{
			this.nearbyTickets = nearbyTickets;
		}
		
	}

	@Override
	public InputParser<Day16.CompositeInput> getInputParser()
	{
		return new InputParser<>(){
			@Override
			public List<Day16.CompositeInput> parseInput(List<String> input)
			{
				CompositeInput composite = new CompositeInput();
				Iterator<String> i = input.iterator();
				String line = i.next();
				while(!line.isBlank()) {
					Matcher m = CompositeInput.RULE_PATTERN.matcher(line);
					m.matches();
					composite.putNewRule(m.group(1), intsToPredicate(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5))));
					line = i.next();
				}
				i.next();
				InputParser<List<Integer>> intParser = InputParserFactory.getIntegerCSVParser();
				composite.setMyTicket(intParser.parseInput(List.of(i.next())).get(0).stream().map(Long::valueOf).collect(Collectors.toList()));
				i.next();
				i.next();
				List<String> nearbyTicketStrings = new ArrayList<>();
				i.forEachRemaining(nearbyTicketStrings::add);
				composite.setNearbyTickets(intParser.parseInput(nearbyTicketStrings));
				return List.of(composite);
			}
			
			private Predicate<Integer> intsToPredicate(int a, int b, int c, int d) {
				return i -> a <= i && i <= b || c <= i && i <= d;
			}
			
		};
	}

	@Override
	protected String part1(List<Day16.CompositeInput> input)
	{
		CompositeInput i = input.get(0);
		return Integer.toString(i.nearbyTickets.stream()
			.map(intList -> 
				intList.stream().filter(value -> 
					i.rules.values().stream().noneMatch(p -> 
						p.test(value)
					)
				).findAny())
			.filter(Optional::isPresent)
			.map(Optional::get)
			.reduce(0, Math::addExact));
	}

	@Override
	protected String part2(List<Day16.CompositeInput> input)
	{
		CompositeInput i = input.get(0);
		List<List<Integer>> validTickets = i.nearbyTickets.stream()
			.filter(intList -> 
				intList.stream().noneMatch(value -> 
					i.rules.values().stream().noneMatch(p -> 
						p.test(value)
					)
				))
			.collect(Collectors.toList());
		Map<String, Integer> rulePositions = new HashMap<>();
		for (Map.Entry<String, Predicate<Integer>> ruleEntry : i.rules.entrySet())
		{
			for (int position = 0; position < i.myTicket.size(); position++)
			{
				if (allTicketsMatchRule(validTickets, position, ruleEntry.getValue())) {
					rulePositions.put(ruleEntry.getKey(), position);
					break;
				}
			}
		}
		return Long.toString(rulePositions.entrySet().stream()
			.filter(entry -> entry.getKey().startsWith("departure"))
			.map(entry -> i.myTicket.get(entry.getValue()))
			.reduce(1L, Math::multiplyExact));
	}
	
	private static boolean allTicketsMatchRule(List<List<Integer>> validTickets, int position, Predicate<Integer> rule) {
		return validTickets.stream()
			.map(list -> list.get(position))
			.allMatch(rule);
	}

}
