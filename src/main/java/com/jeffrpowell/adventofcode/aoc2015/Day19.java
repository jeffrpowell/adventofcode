package com.jeffrpowell.adventofcode.aoc2015;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day19 extends Solution2015<Section>{

    @Override
    public int getDay() {
        return 19;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, false));
    }

    record ReplacementRule(String before, String after) implements Function<String, Set<String>>{
        @Override
        public Set<String> apply(String s) {
            Set<String> results = new HashSet<>();
            int index = s.indexOf(before);
            while (index != -1) {
                String replaced = s.substring(0, index) + after + s.substring(index + before.length());
                results.add(replaced);
                index = s.indexOf(before, index + 1);
            }
            return results;
        }

        public boolean moleculeCanBeReplaced(String molecule) {
            return molecule.contains(before);
        }
    }

    @Override
    protected String part1(List<Section> input) {
        Set<ReplacementRule> replacementRules = input.get(0)
            .getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("(.+) => (.+)")))
            .stream()
            .map(r -> new ReplacementRule(r.getString(0), r.getString(1)))
            .collect(Collectors.toSet());
        
        String molecule = input.get(1).getInput(InputParserFactory.getStringParser()).get(0);
        return Integer.toString(replacementRules.stream()
            .map(rule -> rule.apply(molecule))
            .flatMap(Set::stream)
            .collect(Collectors.toSet())
            .size());
    }


    @Override
    protected String part2(List<Section> input) {
        String molecule = input.get(1).getInput(InputParserFactory.getStringParser()).get(0);
        MoleculeStats stats = analyzeMolecule(molecule);
        // Formula derived by analyzing the structure of the replacements and the target molecule
        // https://www.reddit.com/r/adventofcode/comments/3xflz8/comment/cy4etju/
        int steps = stats.elements - stats.rnArCount - 2 * stats.yCount - 1;
        return Integer.toString(steps);
    }

    record MoleculeStats(int elements, int rnArCount, int yCount) {}

    private MoleculeStats analyzeMolecule(String molecule) {
        String[] tokens = molecule.split("(?=[A-Z])");
        int elements = tokens.length;
        int rnArCount = 0;
        int yCount = 0;
        for (String token : tokens) {
            if (token.equals("Rn") || token.equals("Ar")) {
                rnArCount++;
            } else if (token.equals("Y")) {
                yCount++;
            }
        }
        return new MoleculeStats(elements, rnArCount, yCount);
    }
}
