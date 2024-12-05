package com.jeffrpowell.adventofcode.aoc2024;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day5 extends Solution2024<Section>{

    @Override
    public int getDay() {
        return 5;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, false));
    }

    record PageRule(int before, int after){}

    @Override
    protected String part1(List<Section> input) {
        Set<PageRule> pageRules = input.get(0)
            .getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("(\\d+)\\|(\\d+)")))
            .stream()
            .map(r -> new PageRule(r.getInt(0), r.getInt(1)))
            .collect(Collectors.toSet());
        
        return Integer.toString(input.get(1)
            .getInput(InputParserFactory.getIntegerCSVParser())
            .stream()
            .map(PageUpdates::new)
            .filter(pu -> pu.keepsTheRules(pageRules))
            .map(PageUpdates::getMiddlePage)
            .reduce(0, Math::addExact));
    }

    private static class PageUpdates {
        List<Integer> pageUpdates;
        Set<PageRule> reversePageRules;
        public PageUpdates(List<Integer> pageUpdates) {
            this.pageUpdates = pageUpdates;
            this.reversePageRules = new HashSet<>();
            for (int i = 0; i < pageUpdates.size() - 1; i++) {
                for (int j = i+1; j < pageUpdates.size(); j++) {
                    reversePageRules.add(new PageRule(pageUpdates.get(j), pageUpdates.get(i)));
                }
            }
        }

        public boolean keepsTheRules(Set<PageRule> rules) {
            return reversePageRules.stream().noneMatch(rules::contains);
        }

        public boolean breaksTheRules(Set<PageRule> rules) {
            return reversePageRules.stream().anyMatch(rules::contains);
        }

        public int getMiddlePage() {
            return pageUpdates.get(pageUpdates.size() / 2);
        }

        public PageUpdates swapTwoPages(Set<PageRule> rules) {
            PageRule badRule = reversePageRules.stream()
                .filter(rules::contains)
                .findFirst()
                .get();
            int iFirst = pageUpdates.indexOf(badRule.before);
            int iSecond = pageUpdates.indexOf(badRule.after);
            List<Integer> newPageUpdates = new ArrayList<>();
            for (int i = 0; i < pageUpdates.size(); i++) {
                if (i == iFirst) {
                    newPageUpdates.add(pageUpdates.get(iSecond));
                }
                else if (i == iSecond) {
                    newPageUpdates.add(pageUpdates.get(iFirst));
                }
                else {
                    newPageUpdates.add(pageUpdates.get(i));
                }
            }
            return new PageUpdates(newPageUpdates);
        }
    }

    @Override
    protected String part2(List<Section> input) {
        Set<PageRule> pageRules = input.get(0)
            .getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("(\\d+)\\|(\\d+)")))
            .stream()
            .map(r -> new PageRule(r.getInt(0), r.getInt(1)))
            .collect(Collectors.toSet());
        
        return Integer.toString(input.get(1)
            .getInput(InputParserFactory.getIntegerCSVParser())
            .stream()
            .map(PageUpdates::new)
            .filter(pu -> pu.breaksTheRules(pageRules))
            .map(pu -> changeTheOrdering(pageRules, pu))
            .map(PageUpdates::getMiddlePage)
            .reduce(0, Math::addExact));
    }

    private PageUpdates changeTheOrdering(Set<PageRule> rules, PageUpdates badUpdateList) {
        PageUpdates attempt = badUpdateList.swapTwoPages(rules);
        while(attempt.breaksTheRules(rules)) {
            attempt = attempt.swapTwoPages(rules);
        }
        return attempt;
    }
}
