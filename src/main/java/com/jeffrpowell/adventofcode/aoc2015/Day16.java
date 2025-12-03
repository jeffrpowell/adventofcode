package com.jeffrpowell.adventofcode.aoc2015;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day16 extends Solution2015<Rule>{
    @Override
    public int getDay() {
        return 16;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("Sue (\\d+): (\\w+): (\\d+), (\\w+): (\\d+), (\\w+): (\\d+)"));
    }
    
    /*
    children: 3
    cats: 7
    samoyeds: 2
    pomeranians: 3
    akitas: 0
    vizslas: 0
    goldfish: 5
    trees: 3
    cars: 2
    perfumes: 1
    */

    @Override
    protected String part1(List<Rule> input) {
        List<Sue> sues = input.stream().map(Sue::fromRule).toList();
        Sue targetSue = new Sue(-1, List.of(
            new Attribute("children", 3),
            new Attribute("cats", 7),
            new Attribute("samoyeds", 2),
            new Attribute("pomeranians", 3),
            new Attribute("akitas", 0),
            new Attribute("vizslas", 0),
            new Attribute("goldfish", 5),
            new Attribute("trees", 3),
            new Attribute("cars", 2),
            new Attribute("perfumes", 1)
        ));
        return Integer.toString(sues.stream()
            .filter(sue -> sue.attributes().stream()
                .allMatch(attr -> {
                    Attribute targetAttr = targetSue.attributes().stream()
                        .filter(ta -> ta.name() == attr.name())
                        .findFirst().orElseThrow();
                    return attr.value() == targetAttr.value();
                }))
            .findFirst().orElseThrow().number());
    }

    @Override
    protected String part2(List<Rule> input) {
        List<Sue> sues = input.stream().map(Sue::fromRule).toList();
        return Integer.toString(sues.stream()
            .filter(sue -> sue.attributes().stream()
                .allMatch(attr -> attr.name.matches(attr.value())))
            .findFirst().orElseThrow().number());
    }

    record Sue(int number, List<Attribute> attributes) {
        static Sue fromRule(Rule rule) {
            int number = rule.getInt(0);
            List<Attribute> attributes = List.of(
                new Attribute(rule.getString(1), rule.getInt(2)),
                new Attribute(rule.getString(3), rule.getInt(4)),
                new Attribute(rule.getString(5), rule.getInt(6))
            );
            return new Sue(number, attributes);
        }
    }

    record Attribute(Name name, int value) {
        enum Name {
            CHILDREN(i -> i == 3),
            CATS(i -> i > 7),
            SAMOYEDS(i -> i == 2),
            POMERANIANS(i -> i < 3),
            AKITAS(i -> i == 0),
            VIZSLAS(i -> i == 0),
            GOLDFISH(i -> i < 5),
            TREES(i -> i > 3),
            CARS(i -> i == 2),
            PERFUMES(i -> i == 1);

            private Predicate<Integer> compareFn;

            static Name fromString(String s) {
                return Name.valueOf(s.toUpperCase());
            }

            private Name(Predicate<Integer> compareFn) {
                this.compareFn = compareFn;
            }

            public boolean matches(int otherValue) {
                return compareFn.test(otherValue);
            }
        }

        public Attribute(String name, int value) {
            this(Name.fromString(name), value);
        }
    }
}
