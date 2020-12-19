package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.rule.RuleListUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19 extends Solution2020<Rule>{

    boolean loopOn = false;
    
    @Override
    public int getDay() {
        return 19;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Map.of(
            "letter", Pattern.compile("(\\d+): \"(\\w)\""),
            "concat", Pattern.compile("(\\d+): (\\d+) (\\d+)$"),
            "baton", Pattern.compile("(\\d+): (\\d+)$"),
            "or", Pattern.compile("(\\d+): (\\d+) \\| (\\d+)"),
            "doubleor", Pattern.compile("(\\d+): (\\d+) (\\d+) \\| (\\d+) (\\d+)"),
            "message", Pattern.compile("(\\w+)")
        ));
    }

    @Override
    protected String part1(List<Rule> input) {
        Map<String, List<Rule>> groupedRules = RuleListUtil.groupByRulePatternKey(input);
        Map<Integer, Function<String, String>> rules = new HashMap<>();
        for (Rule rule : groupedRules.get("letter")) {
            rules.put(rule.getInt(0), message -> message.startsWith(rule.getString(1)) ? message.substring(1) : "x");
        }
        for (Rule rule : groupedRules.get("baton")) {
            int innerRuleI = rule.getInt(1);
            rules.put(rule.getInt(0), message -> {
                Function<String, String> innerRule = rules.get(innerRuleI);
                return innerRule.apply(message);
            });
        }
        for (Rule rule : groupedRules.get("concat")) {
            int left = rule.getInt(1);
            int right = rule.getInt(2);
            rules.put(rule.getInt(0), message -> {
                Function<String, String> leftRule = rules.get(left);
                Function<String, String> rightRule = rules.get(right);
                return rightRule.apply(leftRule.apply(message));
            });
        }
        for (Rule rule : groupedRules.get("or")) {
            int left = rule.getInt(1);
            int right = rule.getInt(2);
            rules.put(rule.getInt(0), message -> {
                Function<String, String> leftRule = rules.get(left);
                String result1 = leftRule.apply(message);
                if (!result1.contains("x")) {
                    return result1;
                }
                Function<String, String> rightRule = rules.get(right);
                String result2 = rightRule.apply(message);
                if (!result2.contains("x")) {
                    return result2;
                }
                return "x";
            });
        }
        for (Rule rule : groupedRules.get("doubleor")) {
            int left1 = rule.getInt(1);
            int right1 = rule.getInt(2);
            int left2 = rule.getInt(3);
            int right2 = rule.getInt(4);
            rules.put(rule.getInt(0), message -> {
                Function<String, String> left1Rule = rules.get(left1);
                Function<String, String> right1Rule = rules.get(right1);
                String result1 = right1Rule.apply(left1Rule.apply(message));
                if (!result1.contains("x")) {
                    return result1;
                }
                Function<String, String> left2Rule = rules.get(left2);
                Function<String, String> right2Rule = rules.get(right2);
                String result2 = right2Rule.apply(left2Rule.apply(message));
                if (!result2.contains("x")) {
                    return result2;
                }
                return "x";
            });
        }
        return Long.toString(groupedRules.get("message").stream()
            .map(rule -> rules.get(0).apply(rule.getString(0)).isEmpty())
            .filter(b -> b)
            .count());
    }

    @Override
    protected String part2(List<Rule> input) {
        Map<String, List<Rule>> groupedRules = RuleListUtil.groupByRulePatternKey(input);
        Map<Integer, Function<String, String>> rules = new HashMap<>();
        for (Rule rule : groupedRules.get("letter")) {
            rules.put(rule.getInt(0), message -> message.startsWith(rule.getString(1)) ? message.substring(1) : "x");
        }
        for (Rule rule : groupedRules.get("baton")) {
            if (rule.getInt(0) == 8) {
                //hard-coded to 8: 42 | 42 8
                rules.put(8, message -> {
                    Function<String, String> plain = rules.get(42);
                    Function<String, String> loop = rules.get(8);
                    if (message.contains("x")) {
                        return "";
                    }
                    String tryLoop = !loopOn ? "x" : loop.apply(plain.apply(message));
                    if (tryLoop.isEmpty()) {
                        return message;
                    }
                    if (!tryLoop.contains("x")) {
                        return tryLoop;
                    }
                    String endLoop = plain.apply(message);
                    if (!endLoop.contains("x")) {
                        return endLoop;
                    }
                    return "x";
                });
            }
            else {
                int innerRuleI = rule.getInt(1);
                rules.put(rule.getInt(0), message -> {
                    Function<String, String> innerRule = rules.get(innerRuleI);
                    return innerRule.apply(message);
                });
            }
        }
        for (Rule rule : groupedRules.get("concat")) {
            int left = rule.getInt(1);
            int right = rule.getInt(2);
            rules.put(rule.getInt(0), message -> {
                Function<String, String> leftRule = rules.get(left);
                Function<String, String> rightRule = rules.get(right);
                return rightRule.apply(leftRule.apply(message));
            });
        }
        for (Rule rule : groupedRules.get("or")) {
            if (rule.getInt(0) == 11) {
                //hard-coded to 11: 42 31 | 42 11 31
                rules.put(11, message -> {
                    Function<String, String> left = rules.get(42);
                    Function<String, String> loop = rules.get(11);
                    Function<String, String> right = rules.get(31);
                    if (message.contains("x")) {
                        return "";
                    }
                    String tryLoop = !loopOn ? "x" : right.apply(loop.apply(left.apply(message)));
                    if (tryLoop.isEmpty()) {
                        return message;
                    }
                    if (!tryLoop.contains("x")) {
                        return tryLoop;
                    }
                    String endLoop = right.apply(left.apply(message));
                    if (!endLoop.contains("x")) {
                        return endLoop;
                    }
                    return "x";
                });
            }
            else {
                int left = rule.getInt(1);
                int right = rule.getInt(2);
                rules.put(rule.getInt(0), message -> {
                    Function<String, String> leftRule = rules.get(left);
                    String result1 = leftRule.apply(message);
                    if (!result1.contains("x")) {
                        return result1;
                    }
                    Function<String, String> rightRule = rules.get(right);
                    String result2 = rightRule.apply(message);
                    if (!result2.contains("x")) {
                        return result2;
                    }
                    return "x";
                });
            }
        }
        for (Rule rule : groupedRules.get("doubleor")) {
            int left1 = rule.getInt(1);
            int right1 = rule.getInt(2);
            int left2 = rule.getInt(3);
            int right2 = rule.getInt(4);
            rules.put(rule.getInt(0), message -> {
                Function<String, String> left1Rule = rules.get(left1);
                Function<String, String> right1Rule = rules.get(right1);
                String result1 = right1Rule.apply(left1Rule.apply(message));
                if (!result1.contains("x")) {
                    return result1;
                }
                Function<String, String> left2Rule = rules.get(left2);
                Function<String, String> right2Rule = rules.get(right2);
                String result2 = right2Rule.apply(left2Rule.apply(message));
                if (!result2.contains("x")) {
                    return result2;
                }
                return "x";
            });
        }
        List<String> possibleLoopingMessages = groupedRules.get("message").stream()
            .map(rule -> rule.getString(0))
            .filter(message -> !rules.get(0).apply(message).isEmpty())
//            .peek(System.out::println)
            .collect(Collectors.toList());
        long matchedSoFar = groupedRules.get("message").size() - possibleLoopingMessages.size();
        loopOn = true;
        return Long.toString(possibleLoopingMessages.stream()
            .map(message -> rules.get(0).apply(message).isEmpty())
            .filter(b -> b)
            .count() + matchedSoFar);
    }

}