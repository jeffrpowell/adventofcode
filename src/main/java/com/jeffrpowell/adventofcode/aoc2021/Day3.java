package com.jeffrpowell.adventofcode.aoc2021;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day3 extends Solution2021<List<Integer>>{

    @Override
    public int getDay() {
        return 3;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser("");
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        List<Integer> gamma = new ArrayList<>();
        List<Integer> epsilon = new ArrayList<>();
        for(int i = 0; i < input.get(0).size(); i++) {
            int ones = 0;
            int zeroes = 0;
            for (List<Integer> list : input) {
                if (list.get(i) == 0) {
                    zeroes++;
                }
                else {
                    ones++;
                }
            }
            if (ones > zeroes) {
                gamma.add(1);
                epsilon.add(0);
            }
            else {
                gamma.add(0);
                epsilon.add(1);
            }
        }
        int gammaDec = Integer.parseInt(gamma.stream().map(i -> i.toString()).collect(Collectors.joining()),2);
        int epsilonDec = Integer.parseInt(epsilon.stream().map(i -> i.toString()).collect(Collectors.joining()),2);
        return Integer.toString(gammaDec * epsilonDec);
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        List<List<Integer>> O2Candidates = input;
        List<List<Integer>> CO2Candidates = input;
        for(int i = 0; i < input.get(0).size(); i++) {
            O2Candidates = getNextCandidates(i, O2Candidates, true);
            CO2Candidates = getNextCandidates(i, CO2Candidates, false);
        }
        int o2 = Integer.parseInt(O2Candidates.get(0).stream().map(i -> i.toString()).collect(Collectors.joining()),2);
        int co2 = Integer.parseInt(CO2Candidates.get(0).stream().map(i -> i.toString()).collect(Collectors.joining()),2);
        return Integer.toString(o2 * co2);
    }

    private List<List<Integer>> getNextCandidates(int i, List<List<Integer>> candidates, boolean isO2) {
        if (candidates.size() == 1) {
            return candidates;
        }
        List<List<Integer>> onesList = new ArrayList<>();
        List<List<Integer>> zeroesList = new ArrayList<>();
        for (List<Integer> list : candidates) {
            if (list.get(i) == 0) {
                zeroesList.add(list);
            }
            else {
                onesList.add(list);
            }
        }
        if (zeroesList.size() > onesList.size()) {
            return isO2 ? zeroesList : onesList;
        }
        else if (onesList.size() > zeroesList.size()){
            return isO2 ? onesList : zeroesList;
        }
        else {
            return isO2 ? onesList : zeroesList;
        }
    } 
    
}
