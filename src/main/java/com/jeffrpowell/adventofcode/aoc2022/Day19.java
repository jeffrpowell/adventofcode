package com.jeffrpowell.adventofcode.aoc2022;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        List<Blueprint> blueprints = input.stream().map(
            r -> new Blueprint(
                r.getInt(0),
                r.getInt(1),
                r.getInt(2),
                r.getInt(3),
                r.getInt(4),
                r.getInt(5),
                r.getInt(6)
            )
        ).collect(Collectors.toList());
        return null;
    }

    @Override
    protected String part2(List<Rule> input) {
        return null;
    }

    private static class Blueprint {
        int id;
        int oreCost_ore;
        int clayCost_ore;
        int obsidianCost_ore;
        int obsidianCost_clay;
        int geodeCost_ore;
        int geodeCost_obsidian;
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

    }
}
