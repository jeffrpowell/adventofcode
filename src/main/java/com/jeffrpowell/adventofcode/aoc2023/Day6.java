package com.jeffrpowell.adventofcode.aoc2023;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.rule.RuleListUtil;


public class Day6 extends Solution2023<Rule>{
    @Override
    public int getDay() {
        return 6;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser(
            "\n", Map.of(
                "time", Pattern.compile("^Time:\\s+(\\d.+$)"),
                "distance", Pattern.compile("^Distance:\\s+(\\d.+$)")
            )
        );
    }

    /**
     * Math-based solution
     * Find all integers, PressTime, such that PrevRecord < PressTime * (RaceLen - PressTime)
     * Abbreviated to r < p(t-p)
     * 0 < -p^2 + tp - r
     * p > (-t (+-) sqrt(t^2-4(-1)(-r))) / 2(-1)
     */
    @Override
    protected String part1(List<Rule> input) {
        Map<String, List<Rule>> ruleGrouping = RuleListUtil.groupByRulePatternKey(input);
        String timeStr = ruleGrouping.get("time").get(0).getString(0);
        String distanceStr = ruleGrouping.get("distance").get(0).getString(0);
        List<Integer> times = InputParserFactory.getIntegerTokenSVParser(" ").parseInput(List.of(timeStr)).get(0);
        List<Integer> distances = InputParserFactory.getIntegerTokenSVParser(" ").parseInput(List.of(distanceStr)).get(0);
        long result = 1L;
        for (int i = 0; i < times.size(); i++) {
            MinMaxBounds solution = quadraticSolve(times.get(i), distances.get(i));
            result *= solution.max - solution.min + 1;
        }
        return Long.toString(result);
    }

    @Override
    protected String part2(List<Rule> input) {
        Map<String, List<Rule>> ruleGrouping = RuleListUtil.groupByRulePatternKey(input);
        String timeStr = ruleGrouping.get("time").get(0).getString(0);
        String distanceStr = ruleGrouping.get("distance").get(0).getString(0);
        long time = Long.parseLong(timeStr.replaceAll("\\s+", ""));
        long distance = Long.parseLong(distanceStr.replaceAll("\\s+", ""));
        MinMaxBounds solution = quadraticSolve(time, distance);
        return Long.toString(solution.min - solution.max - 1);
    }

    private record MinMaxBounds(long min, long max){}
    
    private MinMaxBounds quadraticSolve(long time, long distance) {
        double min = (-time - Math.sqrt(Math.pow(time,2)-4*distance)) / -2;
        double max = (-time + Math.sqrt(Math.pow(time,2)-4*distance)) / -2;
        return new MinMaxBounds(d2l(Math.ceil(min)), d2l(Math.floor(max)));
    }

    private long d2l(Double d) {
        return d.longValue();
    }
}
