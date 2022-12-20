package com.jeffrpowell.adventofcode.aoc2022;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day19 extends Solution2022<Rule>{

    @Override
    public int getDay() {
        return 19;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian."));
    }

    @Override
    protected String part1(List<Rule> input) {
        return input.stream().map(
            r -> new Blueprint(
                r.getInt(0),
                r.getInt(1),
                r.getInt(2),
                r.getInt(3),
                r.getInt(4),
                r.getInt(5),
                r.getInt(6)
            )
        )
        .map(Blueprint::quality)
        .collect(Collectors.reducing(0, Math::addExact)).toString();
    }

    @Override
    protected String part2(List<Rule> input) {
        return null;
    }

    private static class Blueprint {
        private static final int TIMER = 24;
        int id;
        int oreCost_ore;
        int clayCost_ore;
        int obsidianCost_ore;
        int obsidianCost_clay;
        int geodeCost_ore;
        int geodeCost_obsidian;
        Deque<Sim> stack = new ArrayDeque<>();
        public Blueprint(int id, int oreCost_ore, int clayCost_ore, int obsidianCost_ore, int obsidianCost_clay,
                int geodeCost_ore, int geodeCost_obsidian) {
            this.id = id;
            this.oreCost_ore = oreCost_ore;
            this.clayCost_ore = clayCost_ore;
            this.obsidianCost_ore = obsidianCost_ore;
            this.obsidianCost_clay = obsidianCost_clay;
            this.geodeCost_ore = geodeCost_ore;
            this.geodeCost_obsidian = geodeCost_obsidian;
        }
        
        record Sim(
            int time,
            int ore, int clay, int obsidian, int geode,
            int oBot, int cBot, int sBot, int gBot
        ){}

        public int quality() {
            System.out.println("Trying blueprint " + id);
            Set<Sim> visited = new HashSet<>();
            stack.push(new Sim(TIMER, 0, 0, 0, 0, 1, 0, 0, 0));
            int max = 0;
            while (!stack.isEmpty()) {
                Sim s = stack.pop();
                visited.add(s);
                if (s.time == 0) {
                    if (max < s.geode) {
                        max = s.geode;
                    }
                }
                else if (s.time > 0 && maxGeodesPossible(s) <= max) {
                }
                else {
                    purchaseBots(s)
                        .map(this::doHarvest)
                        .filter(sim -> !visited.contains(sim))
                        .forEach(stack::push);
                }
            }
            return max * id;
        }

        private int maxGeodesPossible(Sim s) {
            return ((s.time * (s.time+1)) / 2)  + s.time*s.gBot + s.geode;
        }

        private Stream<Sim> purchaseBots(Sim s) {
            if (s.ore >= geodeCost_ore && s.obsidian >= geodeCost_obsidian) {
                return Stream.of(new Sim(
                    s.time,
                    s.ore - geodeCost_ore, s.clay, s.obsidian - geodeCost_obsidian, s.geode - 1,
                    s.oBot, s.cBot, s.sBot, s.gBot + 1
                ));
            }
            else if (s.cBot == 0) {
                if (s.ore >= clayCost_ore) {
                    return Stream.of(new Sim(
                        s.time,
                        s.ore - clayCost_ore, s.clay - 1, s.obsidian, s.geode,
                        s.oBot, s.cBot + 1, s.sBot, s.gBot
                    ));
                }
                else {
                    return Stream.of(s);
                }
            }
            else {
                boolean buildClay = s.ore >= clayCost_ore 
                    && s.cBot < (obsidianCost_clay / obsidianCost_ore);
                boolean buildObsidian = s.ore >= obsidianCost_ore && s.clay >= obsidianCost_clay
                    && s.oBot < (geodeCost_obsidian / geodeCost_ore);
                List<Sim> sims = new ArrayList<>();
                sims.add(s);
                if (buildClay) {
                    sims.add(new Sim(
                        s.time,
                        s.ore - clayCost_ore, s.clay - 1, s.obsidian, s.geode,
                        s.oBot, s.cBot + 1, s.sBot, s.gBot
                    ));
                }
                if (buildObsidian) {
                    sims.add(new Sim(
                        s.time,
                        s.ore - obsidianCost_ore, s.clay - obsidianCost_clay, s.obsidian - 1, s.geode,
                        s.oBot, s.cBot, s.sBot + 1, s.gBot
                    ));
                }
                return sims.stream();
            }
        }

        private Sim doHarvest(Sim s) {
            return new Sim(
                s.time - 1,
                s.ore + s.oBot, s.clay + s.cBot, s.obsidian + s.sBot, s.geode + s.gBot, 
                s.oBot, s.cBot, s.sBot, s.gBot
            );
        }
    }
}
