package com.jeffrpowell.adventofcode.aoc2021;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day24 extends Solution2021<Rule> {
    private static final String INP = "inp";
    private static final String ADD_VAR = "add var";
    private static final String ADD_NUM = "add num";
    private static final String MUL_VAR = "mul var";
    private static final String MUL_NUM = "mul num";
    private static final String DIV_VAR = "div var";
    private static final String DIV_NUM = "div num";
    private static final String MOD_VAR = "mod var";
    private static final String MOD_NUM = "mod num";
    private static final String EQL_VAR = "eql var";
    private static final String EQL_NUM = "eql num";
    Consumer<Map<String, Long>> RESET_REGISTERS = map -> {
        map.put("w", 0L);
        map.put("x", 0L);
        map.put("y", 0L);
        map.put("z", 0L);
    };
    Map<String, Long> registers = new HashMap<>();

    @Override
    public int getDay() {
        return 24;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        Map<String, Pattern> patterns = new HashMap<>();
        patterns.put(INP, Pattern.compile("inp ([a-z])"));
        patterns.put(ADD_VAR, Pattern.compile("add ([a-z]) ([a-z])"));
        patterns.put(ADD_NUM, Pattern.compile("add ([a-z]) (-?\\d+)"));
        patterns.put(MUL_VAR, Pattern.compile("mul ([a-z]) ([a-z])"));
        patterns.put(MUL_NUM, Pattern.compile("mul ([a-z]) (-?\\d+)"));
        patterns.put(DIV_VAR, Pattern.compile("div ([a-z]) ([a-z])"));
        patterns.put(DIV_NUM, Pattern.compile("div ([a-z]) (-?\\d+)"));
        patterns.put(MOD_VAR, Pattern.compile("mod ([a-z]) ([a-z])"));
        patterns.put(MOD_NUM, Pattern.compile("mod ([a-z]) (-?\\d+)"));
        patterns.put(EQL_VAR, Pattern.compile("eql ([a-z]) ([a-z])"));
        patterns.put(EQL_NUM, Pattern.compile("eql ([a-z]) (-?\\d+)"));
        return InputParserFactory.getRuleParser("\n", patterns);
    }

    @Override
    protected String part1(List<Rule> input) {
        long test = 11_111_111_111_111L;
        String output = "99";
        while (test < 99_999_999_999_999L) {
            String testString = Long.toString(test);
            if (!testString.substring(0, 2).equals(output)) {
                output = testString.substring(0, 2);
                System.out.println(testString);
            }
            if (testString.contains("0")) {
                continue;
            }
            if (testNumber(testString, input)) {
                return testString;
            }
            else {
                test++;
            }
        }
        return "FAILED";
    }

    @Override
    protected String part2(List<Rule> input) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private boolean testNumber(String number, List<Rule> input) {
        RESET_REGISTERS.accept(registers);
        System.out.println("\n########### Testing " + number + " #################");
        for (Rule r : input) {
            number = parseAndExecute(number, r);
        }
        return registers.get("z") == 0;
    }
    
    private String parseAndExecute(String number, Rule r) {
        String left;
        String right;
        long leftVal;
        long rightVal;
        long result;
        switch (r.getRulePatternKey()) {
            case INP:
                String input = number.substring(0, 1);
                registers.put(r.getString(0), Long.parseLong(input));
                System.out.println(r.getString(0) + " = " + registers.get(r.getString(0)));
                return number.substring(1);
            case ADD_VAR:
                left = r.getString(0);
                right = r.getString(1);
                leftVal = registers.get(left);
                rightVal = registers.get(right);
                result = leftVal + rightVal;
                System.out.println(left + " = " + result + " (" + left + "=" + leftVal + " + " + right + "=" + rightVal + ")");
                registers.put(left, result);
                break;
            case ADD_NUM:
                left = r.getString(0);
                leftVal = registers.get(left);
                rightVal = r.getLong(1);
                result = leftVal + rightVal;
                System.out.println(left + " = " + result + " (" + left + "=" + leftVal + " + " + rightVal + ")");
                registers.put(left, result);
                break;
            case MUL_VAR:
                left = r.getString(0);
                right = r.getString(1);
                leftVal = registers.get(left);
                rightVal = registers.get(right);
                result = leftVal * rightVal;
                System.out.println(left + " = " + result + " (" + left + "=" + leftVal + " * " + right + "=" + rightVal + ")");
                registers.put(left, result);
                break;
            case MUL_NUM:
                left = r.getString(0);
                leftVal = registers.get(left);
                rightVal = r.getLong(1);
                result = leftVal * rightVal;
                System.out.println(left + " = " + result + " (" + left + "=" + leftVal + " * " + rightVal + ")");
                registers.put(left, result);
                break;
            case DIV_VAR:
                left = r.getString(0);
                right = r.getString(1);
                leftVal = registers.get(left);
                rightVal = registers.get(right);
                result = leftVal / rightVal;
                System.out.println(left + " = " + result + " (" + left + "=" + leftVal + " / " + right + "=" + rightVal + ")");
                registers.put(left, result);
                break;
            case DIV_NUM:
                left = r.getString(0);
                leftVal = registers.get(left);
                rightVal = r.getLong(1);
                result = leftVal / rightVal;
                System.out.println(left + " = " + result + " (" + left + "=" + leftVal + " / " + rightVal + ")");
                registers.put(left, result);
                break;
            case MOD_VAR:
                left = r.getString(0);
                right = r.getString(1);
                leftVal = registers.get(left);
                rightVal = registers.get(right);
                result = leftVal % rightVal;
                System.out.println(left + " = " + result + " (" + left + "=" + leftVal + " % " + right + "=" + rightVal + ")");
                registers.put(left, result);
                break;
            case MOD_NUM:
                left = r.getString(0);
                leftVal = registers.get(left);
                rightVal = r.getLong(1);
                result = leftVal % rightVal;
                System.out.println(left + " = " + result + " (" + left + "=" + leftVal + " % " + rightVal + ")");
                registers.put(left, result);    
                break;
            case EQL_VAR:
                left = r.getString(0);
                right = r.getString(1);
                leftVal = registers.get(left);
                rightVal = registers.get(right);
                result = leftVal == rightVal ? 1L : 0L;
                System.out.println(left + " = " + result + " (" + left + "=" + leftVal + " == " + right + "=" + rightVal + ")");
                registers.put(left, result);
                break;
            case EQL_NUM:
                left = r.getString(0);
                leftVal = registers.get(left);
                rightVal = r.getLong(1);
                result = leftVal == rightVal ? 1L : 0L;
                System.out.println(left + " = " + result + " (" + left + "=" + leftVal + " == " + rightVal + ")");
                registers.put(left, result);
                break;
        }
        return number;
    }
    
}
