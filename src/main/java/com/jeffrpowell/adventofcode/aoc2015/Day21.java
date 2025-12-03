package com.jeffrpowell.adventofcode.aoc2015;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day21 extends Solution2015<Rule>{

    @Override
    public int getDay() {
        return 21;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Map.of(
            "HP", Pattern.compile("Hit Points: (\\d+)"),
            "Damage", Pattern.compile("Damage: (\\d+)"),
            "Armor", Pattern.compile("Armor: (\\d+)")
        ));
    }

    @Override
    protected String part1(List<Rule> input) {
        int HP = input.stream().filter(r -> r.getRulePatternKey().equals("HP")).findFirst().get().getInt(0);
        int damage = input.stream().filter(r -> r.getRulePatternKey().equals("Damage")).findFirst().get().getInt(0);
        int armor = input.stream().filter(r -> r.getRulePatternKey().equals("Armor")).findFirst().get().getInt(0);
        Stats bossStats = new Stats(HP, damage, armor);
        int playerHP = 100;
        List<Loadout> loadouts = generateAllLoadouts(weapons, armors, rings);
        return Integer.toString(loadouts.stream()
            .map(loadout -> loadout.analyze(bossStats, playerHP))
            .filter(analysis -> analysis.turnsToKill() <= analysis.turnsToDie())
            .mapToInt(Analysis::cost)
            .min()
            .orElseThrow());
    }


    @Override
    protected String part2(List<Rule> input) {
        int HP = input.stream().filter(r -> r.getRulePatternKey().equals("HP")).findFirst().get().getInt(0);
        int damage = input.stream().filter(r -> r.getRulePatternKey().equals("Damage")).findFirst().get().getInt(0);
        int armor = input.stream().filter(r -> r.getRulePatternKey().equals("Armor")).findFirst().get().getInt(0);
        Stats bossStats = new Stats(HP, damage, armor);
        int playerHP = 100;
        List<Loadout> loadouts = generateAllLoadouts(weapons, armors, rings);
        return Integer.toString(loadouts.stream()
            .map(loadout -> loadout.analyze(bossStats, playerHP))
            .filter(analysis -> analysis.turnsToKill() > analysis.turnsToDie())
            .mapToInt(Analysis::cost)
            .max()
            .orElseThrow());
    }

    record Stats(int hitPoints, int damage, int armor) {}
    record Item(int cost, int damage, int armor) {}
    record Analysis(int turnsToKill, int turnsToDie, int cost) {}
    record Loadout(Item weapon, Optional<Item> armor, Optional<Item> ring1, Optional<Item> ring2) {
        Analysis analyze(Stats bossStats, int playerHP) {
            int totalDamage = weapon.damage() + armor.map(Item::damage).orElse(0) + ring1.map(Item::damage).orElse(0) + ring2.map(Item::damage).orElse(0);
            int totalArmor = weapon.armor() + armor.map(Item::armor).orElse(0) + ring1.map(Item::armor).orElse(0) + ring2.map(Item::armor).orElse(0);
            int damageToBoss = Math.max(1, totalDamage - bossStats.armor());
            int damageToPlayer = Math.max(1, bossStats.damage() - totalArmor);
            int turnsToKill = (int)Math.ceil((double)bossStats.hitPoints() / damageToBoss);
            int turnsToDie = (int)Math.ceil((double)playerHP / damageToPlayer);
            int totalCost = weapon.cost() + armor.map(Item::cost).orElse(0) + ring1.map(Item::cost).orElse(0) + ring2.map(Item::cost).orElse(0);
            return new Analysis(turnsToKill, turnsToDie, totalCost);
        }
    }

    static List<Item> weapons = List.of(
        new Item(8, 4, 0),
        new Item(10, 5, 0),
        new Item(25, 6, 0),
        new Item(40, 7, 0),
        new Item(74, 8, 0)
    );

    static List<Item> armors = List.of(
        new Item(13, 0, 1),
        new Item(31, 0, 2),
        new Item(53, 0, 3),
        new Item(75, 0, 4),
        new Item(102, 0, 5)
    );

    static List<Item> rings = List.of(
        new Item(25, 1, 0),
        new Item(50, 2, 0),
        new Item(100, 3, 0),
        new Item(20, 0, 1),
        new Item(40, 0, 2),
        new Item(80, 0, 3)
    );

    private List<Loadout> generateAllLoadouts(List<Item> weapons, List<Item> armors, List<Item> rings) {
        List<Loadout> loadouts = new ArrayList<>();
        for (Item weapon : weapons) {
            // No armor, no rings
            loadouts.add(new Loadout(weapon, Optional.empty(), Optional.empty(), Optional.empty()));
            // With armor, no rings
            for (Item armor : armors) {
                loadouts.add(new Loadout(weapon, Optional.of(armor), Optional.empty(), Optional.empty()));
            }
            // No armor, one ring
            for (Item ring : rings) {
                loadouts.add(new Loadout(weapon, Optional.empty(), Optional.of(ring), Optional.empty()));
            }
            // With armor, one ring
            for (Item armor : armors) {
                for (Item ring : rings) {
                    loadouts.add(new Loadout(weapon, Optional.of(armor), Optional.of(ring), Optional.empty()));
                }
            }
            // No armor, two rings
            for (int i = 0; i < rings.size(); i++) {
                for (int j = i + 1; j < rings.size(); j++) {
                    loadouts.add(new Loadout(weapon, Optional.empty(), Optional.of(rings.get(i)), Optional.of(rings.get(j))));
                }
            }
            // With armor, two rings
            for (Item armor : armors) {
                for (int i = 0; i < rings.size(); i++) {
                    for (int j = i + 1; j < rings.size(); j++) {
                        loadouts.add(new Loadout(weapon, Optional.of(armor), Optional.of(rings.get(i)), Optional.of(rings.get(j))));
                    }
                }
            }
        }
        return loadouts;
    }
}
