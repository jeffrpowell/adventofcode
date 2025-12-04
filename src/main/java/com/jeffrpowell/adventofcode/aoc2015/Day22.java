package com.jeffrpowell.adventofcode.aoc2015;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day22 extends Solution2015<Rule>{

    @Override
    public int getDay() {
        return 22;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Map.of(
            "HP", Pattern.compile("Hit Points: (\\d+)"),
            "Damage", Pattern.compile("Damage: (\\d+)")
        ));
    }

    @Override
    protected String part1(List<Rule> input) {
        return simulate(input, false);
    }
    
    @Override
    protected String part2(List<Rule> input) {
        return simulate(input, true);
        //1289 too low
        //1362 too high
    }

    private String simulate(List<Rule> input, boolean part2) {
        int bossHP = input.stream().filter(r -> r.getRulePatternKey().equals("HP")).findFirst().get().getInt(0);
        int bossDamage = input.stream().filter(r -> r.getRulePatternKey().equals("Damage")).findFirst().get().getInt(0);
        int playerHP = 50;
        int playerMana = 500;
        PriorityQueue<GameState> queue = new PriorityQueue<>();
        queue.add(new GameState(
            new Stats(playerHP, 0, playerMana),
            new Stats(bossHP, bossDamage, 0),
            0,
            List.of(),
            true
        ));
        while (!queue.isEmpty()) {
            GameState state = queue.poll();
            Stats player = state.player();
            Stats boss = state.boss();
            int armor = 0;
            if (part2 && state.playerTurn()) {
                player = new Stats(player.hitPoints() - 1, player.damage(), player.mana());
                if (player.hitPoints() <= 0) {
                    continue;
                }
            }
            List<ActiveEffect> newEffects = state.activeEffects().stream()
                .filter(effect -> effect.remainingDuration() > 1)
                .map(effect -> new ActiveEffect(effect.spell(), effect.remainingDuration() - 1))
                .toList();
            for (ActiveEffect effect : state.activeEffects()) {
                // Apply active effects
                Spell spell = effect.spell();
                if (spell.damage() > 0) {
                    boss = new Stats(boss.hitPoints() - spell.damage(), boss.damage(), boss.mana());
                }
                if (spell.mana() > 0) {
                    player = new Stats(player.hitPoints(), player.damage(), player.mana() + spell.mana());
                }
                if (spell.armor() > 0) {
                    armor += spell.armor();
                }
            }
            if (boss.hitPoints() <= 0) {
                return Integer.toString(state.manaSpent());
            }
            if (state.playerTurn()) {
                // Player's turn: try casting each spell
                for (Spell spell : spells) {
                    if (player.mana() >= spell.cost() &&
                        newEffects.stream().noneMatch(effect -> effect.spell() == spell)) {
                        Stats newPlayer = new Stats(
                            player.hitPoints() + spell.heal(),
                            player.damage(),
                            player.mana() - spell.cost()
                        );
                        List<ActiveEffect> effectsWithNew = new ArrayList<>(newEffects);
                        if (spell.duration() > 0) {
                            effectsWithNew.add(new ActiveEffect(spell, spell.duration()));
                        }
                        Stats newBoss = new Stats(
                            boss.hitPoints() - (spell.duration() > 0 ? 0 : spell.damage()), 
                            boss.damage(), 
                            boss.mana()
                        );
                        if (newBoss.hitPoints() <= 0) {
                            return Integer.toString(state.manaSpent() + spell.cost());
                        }
                        else {
                            queue.add(new GameState(
                                newPlayer,
                                newBoss,
                                state.manaSpent() + spell.cost(),
                                effectsWithNew,
                                false
                            ));
                        }
                    }
                }
            } else {
                // Boss's turn: attack
                int damageToPlayer = Math.max(1, boss.damage() - armor);
                Stats newPlayer = new Stats(player.hitPoints() - damageToPlayer, player.damage(), player.mana());
                if (newPlayer.hitPoints() > (part2 ? 1 : 0)) {
                    queue.add(new GameState(
                        newPlayer,
                        boss,
                        state.manaSpent(),
                        newEffects,
                        true
                    ));
                }
            }
        }
        throw new IllegalStateException("No winning strategy found");
    }

    record Stats(int hitPoints, int damage, int mana) {}
    record Spell(int cost, int damage, int heal, int armor, int mana, int duration) {}
    static final List<Spell> spells = List.of(
        new Spell(53, 4, 0, 0, 0, 0),
        new Spell(73, 2, 2, 0, 0, 0),
        new Spell(113, 0, 0, 7, 0, 6),
        new Spell(173, 3, 0, 0, 0, 6),
        new Spell(229, 0, 0, 0, 101, 5)
    );
    record GameState(Stats player, Stats boss, int manaSpent, List<ActiveEffect> activeEffects, boolean playerTurn) implements Comparable<GameState> {
        @Override
        public int compareTo(GameState other) {
            return Integer.compare(this.manaSpent, other.manaSpent);
        }
    }
    record ActiveEffect(Spell spell, int remainingDuration) {}
}
