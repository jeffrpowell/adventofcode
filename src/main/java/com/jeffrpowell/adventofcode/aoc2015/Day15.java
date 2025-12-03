package com.jeffrpowell.adventofcode.aoc2015;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day15 extends Solution2015<Rule>{

    @Override
    public int getDay() {
        return 15;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("(\\w+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)"));
    }
    
    @Override
    protected String part1(List<Rule> input) {
        List<Ingredient> ingredients = input.stream().map(Ingredient::fromRule).toList();
        List<List<Integer>> ingredientCounts = generateCombinations(100, ingredients.size());
        return Long.toString(ingredientCounts.stream()
            .map(combo -> mapCountToIngredients(ingredients, combo))
            .mapToLong(this::scoreIngredients)
            .max().getAsLong());
    }

    @Override
    protected String part2(List<Rule> input) {
        List<Ingredient> ingredients = input.stream().map(Ingredient::fromRule).toList();
        List<List<Integer>> ingredientCounts = generateCombinations(100, ingredients.size());
        return Long.toString(ingredientCounts.stream()
            .map(combo -> mapCountToIngredients(ingredients, combo))
            .filter(ingredientsOption -> totalCalories(ingredientsOption) == 500)
            .mapToLong(this::scoreIngredients)
            .max().getAsLong());
    }

    record Ingredient(int capacity, int durability, int flavor, int texture, int calories) {
        static Ingredient fromRule(Rule rule) {
            int capacity = rule.getInt(1);
            int durability = rule.getInt(2);
            int flavor = rule.getInt(3);
            int texture = rule.getInt(4);
            int calories = rule.getInt(5);
            return new Ingredient(capacity, durability, flavor, texture, calories);
        }

        public Ingredient multiply(int factor) {
            return new Ingredient(
                this.capacity * factor,
                this.durability * factor,
                this.flavor * factor,
                this.texture * factor,
                this.calories * factor
            );
        }
    }

    private static List<List<Integer>> generateCombinations(int totalTsp, int ingredientsNum) {
        List<List<Integer>> combinations = new ArrayList<>();
        generateCombinationsHelper(totalTsp, ingredientsNum, new ArrayList<>(), combinations);
        return combinations;
    }

    private static void generateCombinationsHelper(int total, int parts, List<Integer> current, List<List<Integer>> combinations) {
        if (parts == 1) {
            current.add(total);
            combinations.add(new ArrayList<>(current));
            current.remove(current.size() - 1);
            return;
        }
        for (int i = 0; i <= total; i++) {
            current.add(i);
            generateCombinationsHelper(total - i, parts - 1, current, combinations);
            current.remove(current.size() - 1);
        }
    }

    private List<Ingredient> mapCountToIngredients(List<Ingredient> ingredients, List<Integer> counts) {
        List<Ingredient> result = new ArrayList<>();
        for (int i = 0; i < ingredients.size(); i++) {
            result.add(ingredients.get(i).multiply(counts.get(i)));
        }
        return result;
    }

    private long scoreIngredients(List<Ingredient> ingredients) {
        long capacity = ingredients.stream().mapToLong(Ingredient::capacity).sum();
        long durability = ingredients.stream().mapToLong(Ingredient::durability).sum();
        long flavor = ingredients.stream().mapToLong(Ingredient::flavor).sum();
        long texture = ingredients.stream().mapToLong(Ingredient::texture).sum();

        capacity = Math.max(capacity, 0);
        durability = Math.max(durability, 0);
        flavor = Math.max(flavor, 0);
        texture = Math.max(texture, 0);

        return capacity * durability * flavor * texture;
    }

    private long totalCalories(List<Ingredient> ingredients) {
        return ingredients.stream().mapToLong(Ingredient::calories).sum();
    }
}
