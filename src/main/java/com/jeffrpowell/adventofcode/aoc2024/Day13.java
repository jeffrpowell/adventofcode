package com.jeffrpowell.adventofcode.aoc2024;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.rule.RuleListUtil;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day13 extends Solution2024<Section>{

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, true));
    }

    record SystemEq(long ax, long ay, long bx, long by, long tx, long ty){}

    @Override
    protected String part1(List<Section> input) {
        InputParser<Rule> ip = InputParserFactory.getRuleParser("\n", Map.of(
            "A", Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)"),
            "B", Pattern.compile("Button B: X\\+(\\d+), Y\\+(\\d+)"),
            "T", Pattern.compile("Prize: X=(\\d+), Y=(\\d+)")
        ));
        return Long.toString(input.stream()
            .map(s -> s.getInput(ip))
            .map(listRules -> {
                Map<String, List<Rule>> groupedRules = RuleListUtil.groupByRulePatternKey(listRules);
                Rule a = groupedRules.get("A").get(0);
                Rule b = groupedRules.get("B").get(0);
                Rule t = groupedRules.get("T").get(0);
                return new SystemEq(a.getLong(0), a.getLong(1), b.getLong(0), b.getLong(1), t.getLong(0), t.getLong(1));
            })
            .map(s -> solve(s, true))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .reduce(0L, Math::addExact));
    }

    /**
     * 
     * @param s
     * @return tokens required to move to target (3a+1b)
     */
    private Optional<Long> solve(SystemEq s, boolean part1) {
        long a = ((s.bx * s.ty) - (s.by * s.tx)) / (s.bx  * s.ay - s.by * s.ax);
        long b = (s.tx - s.ax * a) / s.bx;
        double ad = ((s.bx * s.ty) - (s.by * s.tx)) / (double)(s.bx  * s.ay - s.by * s.ax);
        double bd = (s.tx - s.ax * a) / (double)s.bx;
        if (part1 && (a > 100 || b > 100)) {
            return Optional.empty();
        }
        else if (Math.abs(ad - a) > 0.001 || Math.abs(bd - b) > 0.001){
            return Optional.empty();
        }
        return Optional.of(3 * a + b);
    }


    @Override
    protected String part2(List<Section> input) {
        InputParser<Rule> ip = InputParserFactory.getRuleParser("\n", Map.of(
            "A", Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)"),
            "B", Pattern.compile("Button B: X\\+(\\d+), Y\\+(\\d+)"),
            "T", Pattern.compile("Prize: X=(\\d+), Y=(\\d+)")
        ));
        return Long.toString(input.stream()
            .map(s -> s.getInput(ip))
            .map(listRules -> {
                Map<String, List<Rule>> groupedRules = RuleListUtil.groupByRulePatternKey(listRules);
                Rule a = groupedRules.get("A").get(0);
                Rule b = groupedRules.get("B").get(0);
                Rule t = groupedRules.get("T").get(0);
                return new SystemEq(a.getLong(0), a.getLong(1), b.getLong(0), b.getLong(1), t.getLong(0)+10000000000000L, t.getLong(1)+10000000000000L);
            })
            .map(s -> solve(s, false))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .reduce(0L, Math::addExact));
    }
}
