package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        Map<String, List<List<String>>> claimedAllergensByIngredient = recipes.stream() //Stream<Recipe>
            .map(Recipe::flatten) //Stream<Map<String, List<String>>
            .flatMap(m -> m.entrySet().stream()) //Stream<Map.Entry<String, List<String>>
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> List.of(entry.getValue()),
                (e1, e2) -> Stream.concat(e1.stream(), e2.stream()).collect(Collectors.toList())
            ));
        return "";
    }

    @Override
    protected String part2(List<Rule> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        
        public Map<String, List<String>> flatten() {
            return Arrays.stream(ingredients).collect(Collectors.toMap(
                Function.identity(),
                i -> allergens
            ));
        }

        @Override
        public String toString() {
            return "allergens=" + allergens + ", ingredients=" + Arrays.toString(ingredients);
        }
    }
}
