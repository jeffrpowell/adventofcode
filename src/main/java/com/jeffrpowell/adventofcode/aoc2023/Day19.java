package com.jeffrpowell.adventofcode.aoc2023;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.SplitPartParser;
import com.jeffrpowell.adventofcode.inputparser.SplitPartParser.Part;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day19 extends Solution2023<Section>{

    private static Map<String, Function<MachinePart, Integer>> categoryFnFactory = Map.of(
        "x", MachinePart::x,
        "m", MachinePart::m,
        "a", MachinePart::a,
        "s", MachinePart::s
    );

    @Override
    public int getDay() {
        return 19;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.countNewlines(1, false));
    }

    @Override
    protected String part1(List<Section> input) {
        List<SplitPartParser.Part<String, String>> rules = input.get(0).getInput(
            InputParserFactory.getSplitPartParser(
                Pattern.compile("\\{"), 
                InputParserFactory.getStringParser(), 
                InputParserFactory.getPreParser(s -> s.substring(0, s.length() - 1),
                    InputParserFactory.getStringParser()))
        );
        InputParser<Rule> ruleParser = InputParserFactory.getRuleParser(",", Map.of(
            "normal", Pattern.compile("(\\w)([<>])(\\d+):(\\w+)"),
            "end", Pattern.compile("^([^:<>]+)$")
        ));
        Map<String, List<Condition>> conditions = rules.stream()
            .collect(Collectors.toMap(
                p -> p.firstPart(),
                p -> ruleParser.parseInput(List.of(p.secondPart())).stream()
                        .map(r -> {
                            if (r.getRulePatternKey().equals("normal")) {
                                return new Condition(categoryFnFactory.get(r.getString(0)), r.getString(1).equals(">"), r.getInt(2), r.getString(3));
                            }
                            return new Condition(categoryFnFactory.get("x"), true, Integer.MIN_VALUE, r.getString(0));
                        })
                        .collect(Collectors.toList())
            ));
        List<MachinePart> parts = input.get(1).getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)\\}")))
            .stream()
            .map(r -> new MachinePart(r.getInt(0), r.getInt(1), r.getInt(2), r.getInt(3)))
            .collect(Collectors.toList());

        return Integer.toString(parts.stream()
            .filter(p -> isAccepted(p, conditions))
            .map(MachinePart::rating)
            .reduce(0, Math::addExact));
    }

    record Condition(Function<MachinePart, Integer> categoryFn, boolean isGreaterThan, int condition, String outcome) {
        boolean matches(MachinePart p) {
            return isGreaterThan ? 
                categoryFn.apply(p) > condition :
                categoryFn.apply(p) < condition;
        }
    }

    record MachinePart(int x, int m, int a, int s){
        int rating() {
            return x + m + a + s;
        }
    }

    private boolean isAccepted(MachinePart part, Map<String, List<Condition>> conditionsMap) {
        List<Condition> conditions = conditionsMap.get("in");
        while (true) {
            Condition match = conditions.stream()
                .filter(condition -> condition.matches(part))
                .findFirst().get();
            if (match.outcome().equals("R")) {
                return false;
            }
            else if (match.outcome().equals("A")) {
                return true;
            }
            conditions = conditionsMap.get(match.outcome());
        }
    }

    @Override
    protected String part2(List<Section> input) {
        return "";
    }
}
