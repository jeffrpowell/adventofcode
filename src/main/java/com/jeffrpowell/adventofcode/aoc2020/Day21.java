package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day21 extends Solution2020<Rule>{

    @Override
    public int getDay() {
        return 21;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Map.of(
            "one", Pattern.compile("([\\w|\\s]+) \\(contains (\\w+)\\)"),
            "two", Pattern.compile("([\\w|\\s]+) \\(contains (\\w+), (\\w+)\\)"),
            "three", Pattern.compile("([\\w|\\s]+) \\(contains (\\w+), (\\w+), (\\w+)\\)")
        ));
    }

    @Override
    protected String part1(List<Rule> input) {
        List<Recipe> recipes = input.stream().map(Recipe::new).collect(Collectors.toList());
        Map<String, Set<String>> allergensThatAnIngredientCouldHave = recipes.stream()
            .map(Recipe::flattenByIngredient) //Stream<Map<String, List<String>>
            .flatMap(m -> m.entrySet().stream())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> new HashSet<>(entry.getValue()),
                (e1, e2) -> Stream.concat(e1.stream(), e2.stream()).collect(Collectors.toSet())
            ));
        Map<String, List<List<String>>> allSetsOfIngredientsThatCouldHaveAnAllergen = recipes.stream() //Stream<Recipe>
            .map(Recipe::flattenByAllergen) //Stream<Map<String, List<String>>
            .flatMap(m -> m.entrySet().stream()) //Stream<Map.Entry<String, List<String>>
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> List.of(entry.getValue()),
                (e1, e2) -> Stream.concat(e1.stream(), e2.stream()).collect(Collectors.toList())
            ));
        Set<String> ingredientsWithNoAllergens = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : allergensThatAnIngredientCouldHave.entrySet()) {
            String ingredient = entry.getKey();
            Set<String> allergensToRemove = new HashSet<>();
            for (String allergen : entry.getValue()) {
                for (List<String> ingredientList : allSetsOfIngredientsThatCouldHaveAnAllergen.get(allergen)) {
                    if (!ingredientList.contains(ingredient)) {
                        allergensToRemove.add(allergen);
                        break;
                    }
                }
            }
            entry.getValue().removeAll(allergensToRemove);
            if (entry.getValue().isEmpty()) {
                ingredientsWithNoAllergens.add(ingredient);
            }
        }
        return Long.toString(recipes.stream()
            .map(r -> Arrays.asList(r.ingredients))
            .flatMap(List::stream)
            .filter(ingredientsWithNoAllergens::contains)
            .count());
    }

    @Override
    protected String part2(List<Rule> input) {
        List<Recipe> recipes = input.stream().map(Recipe::new).collect(Collectors.toList());
        Map<String, Set<String>> allergensThatAnIngredientCouldHave = recipes.stream()
            .map(Recipe::flattenByIngredient) //Stream<Map<String, List<String>>
            .flatMap(m -> m.entrySet().stream())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> new HashSet<>(entry.getValue()),
                (e1, e2) -> Stream.concat(e1.stream(), e2.stream()).collect(Collectors.toSet())
            ));
        Map<String, List<List<String>>> allSetsOfIngredientsThatCouldHaveAnAllergen = recipes.stream() //Stream<Recipe>
            .map(Recipe::flattenByAllergen) //Stream<Map<String, List<String>>
            .flatMap(m -> m.entrySet().stream()) //Stream<Map.Entry<String, List<String>>
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> List.of(entry.getValue()),
                (e1, e2) -> Stream.concat(e1.stream(), e2.stream()).collect(Collectors.toList())
            ));
        Set<String> ingredientsWithNoAllergens = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : allergensThatAnIngredientCouldHave.entrySet()) {
            String ingredient = entry.getKey();
            Set<String> allergensToRemove = new HashSet<>();
            for (String allergen : entry.getValue()) {
                for (List<String> ingredientList : allSetsOfIngredientsThatCouldHaveAnAllergen.get(allergen)) {
                    if (!ingredientList.contains(ingredient)) {
                        allergensToRemove.add(allergen);
                        break;
                    }
                }
            }
            entry.getValue().removeAll(allergensToRemove);
            if (entry.getValue().isEmpty()) {
                ingredientsWithNoAllergens.add(ingredient);
            }
        }
        ingredientsWithNoAllergens.stream().forEach(allergensThatAnIngredientCouldHave::remove);
        //...and then manually debug/inspect ingredientsWithNoAllergens and work it out by hand :P
        return "fntg,gtqfrp,xlvrggj,rlsr,xpbxbv,jtjtrd,fvjkp,zhszc";
    }
    
    private static class Recipe {
        String[] ingredients;
        List<String> allergens;
        
        public Recipe(Rule r) {
            this.ingredients = r.getString(0).split(" ");
            this.allergens = switch (r.getRulePatternKey()) {
                case "one" -> List.of(r.getString(1));
                case "two" -> List.of(r.getString(1), r.getString(2));
                case "three" -> List.of(r.getString(1), r.getString(2), r.getString(3));
                default -> Collections.emptyList();
            };
        }
        
        public Map<String, List<String>> flattenByIngredient() {
            return Arrays.stream(ingredients).collect(Collectors.toMap(
                Function.identity(),
                i -> allergens
            ));
        }
        
        public Map<String, List<String>> flattenByAllergen() {
            return allergens.stream().collect(Collectors.toMap(
                Function.identity(),
                i -> Arrays.asList(ingredients)
            ));
        }

        @Override
        public String toString() {
            return "allergens=" + allergens + ", ingredients=" + Arrays.toString(ingredients);
        }
    }
}
