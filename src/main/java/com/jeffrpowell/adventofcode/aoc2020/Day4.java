package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 extends Solution2020<Rule>{

    @Override
    public int getDay() {
        return 4;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        String regex = "(\\w+):(.+)";
        return InputParserFactory.getRuleParser("\\s+", Map.of(
            "entry", Pattern.compile(regex),
            "newline", Pattern.compile("^$")
        ));
    }

    @Override
    protected String part1(List<Rule> input) {
        Passport p = new Passport();
        int validCount = 0;
        for (Rule rule : input) {
            if (rule.getRulePatternKey().equals("newline")) {
                if (p.isValid()) {
                    validCount++;
                }
                p = new Passport();
            }
            else {
                p.registerField(rule);
            }
        }
        return Integer.toString(validCount);
    }

    @Override
    protected String part2(List<Rule> input) {
        Passport p = new Passport();
        int validCount = 0;
        for (Rule rule : input) {
            if (rule.getRulePatternKey().equals("newline")) {
                if (p.isValid()) {
                    validCount++;
                }
                p = new Passport();
            }
            else {
                p.registerFieldPt2(rule);
            }
        }
        return Integer.toString(validCount);
    }

    private static class Passport {
        private static final List<String> ECL;
        private static final Pattern HCL;
        private static final Pattern HGT;
        static {
            ECL = List.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
            HCL = Pattern.compile("#[0-9a-f]{6}");
            HGT = Pattern.compile("(\\d+)(in|cm)");
        }
        boolean has_ecl;
        boolean has_pid;
        boolean has_eyr;
        boolean has_hcl;
        boolean has_byr;
        boolean has_iyr;
        boolean has_cid;
        boolean has_hgt;
        boolean isInvalid = false;
        
        public void registerField(Rule r) {
            String key = r.getString(0);
            switch (key) {
                case "ecl" -> has_ecl = true;
                case "pid" -> has_pid = true;
                case "eyr" -> has_eyr = true;
                case "hcl" -> has_hcl = true;
                case "byr" -> has_byr = true;
                case "iyr" -> has_iyr = true;
                case "cid" -> has_cid = true;
                case "hgt" -> has_hgt = true;
            }
        }
        
        public void registerFieldPt2(Rule r) {
            if (isInvalid) {
                return;
            }
            String key = r.getString(0);
            try {
                switch (key) {
                    case "ecl" -> ecl(r);
                    case "pid" -> pid(r);
                    case "eyr" -> eyr(r);
                    case "hcl" -> hcl(r);
                    case "byr" -> byr(r);
                    case "iyr" -> iyr(r);
                    case "hgt" -> hgt(r);
                    case "cid" -> has_cid = true;
                }
            }
            catch (Exception e) {
                isInvalid = true;
            }
        }
        
        private void ecl(Rule r) {
            has_ecl = ECL.stream().anyMatch(color -> r.getString(1).equals(color));
        }
        
        private void pid(Rule r) {
            String allDigits = r.getString(1);
            Long.parseLong(allDigits);
            has_pid = allDigits.length() == 9;
        }
        
        private void eyr(Rule r) {
            has_eyr = r.getInt(1) >= 2020 && r.getInt(1) <= 2030;
        }
        
        private void hcl(Rule r) {
            has_hcl = HCL.matcher(r.getString(1)).matches();            
        }
        
        private void byr(Rule r) {
            has_byr = r.getInt(1) >= 1920 && r.getInt(1) <= 2002;
        }
        
        private void iyr(Rule r) {
            has_iyr = r.getInt(1) >= 2010 && r.getInt(1) <= 2020;
        }
        
        private void hgt(Rule r) {
            Matcher m = HGT.matcher(r.getString(1));
            m.matches();
            int value = Integer.parseInt(m.group(1));
            if (m.group(2).equals("cm")) {
                has_hgt = value >= 150 && value <= 193;
            }
            else {
                has_hgt = value >= 59 && value <= 76;
            }
        }
        
        public boolean isValid() { 
            return !isInvalid && has_byr && has_ecl && has_eyr && has_hcl && has_hgt && has_iyr && has_pid;
        }
    }
}
