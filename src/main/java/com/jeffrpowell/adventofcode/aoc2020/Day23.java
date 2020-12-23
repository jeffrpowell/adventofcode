package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day23 extends Solution2020<List<Integer>>{

    @Override
    public int getDay() {
        return 23;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser("");
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        List<Integer> cupList = input.get(0);
        int minLabel = cupList.stream().min(Integer::compare).get();
        int maxLabel = cupList.stream().max(Integer::compare).get();
        Map<Integer, Cup> cups = cupList.stream()
            .map(Cup::new)
            .collect(Collectors.toMap(
                Cup::getLabel,
                Function.identity()
            ));
        cups.get(cupList.get(0)).initLeftCup(cups.get(cups.size() - 1));
        for (int i = 1; i < cups.size(); i++) {
            cups.get(cupList.get(i)).initLeftCup(cups.get(cupList.get(i - 1)));
        }
        Cup currentCup = cups.get(cupList.get(0));
        for (int i = 0; i < 100; i++) {
            Cup cup1 = currentCup.rightCup;
            Cup cup2 = cup1.rightCup;
            Cup cup3 = cup2.rightCup;
            cup1.removeCup();
            cup2.removeCup();
            cup3.removeCup();
            List<Integer> removedLabels = List.of(cup1.label, cup2.label, cup3.label);
            int destination = currentCup.getLabel() - 1;
            while(destination < minLabel || removedLabels.contains(destination)) {
                destination--;
                if (destination < minLabel) {
                    destination = maxLabel;
                }
            }
            Cup destinationCup = cups.get(destination);
            cup1.setLeftCup(destinationCup);
            cup2.setLeftCup(cup1);
            cup3.setLeftCup(cup2);
        }
        currentCup = cups.get(1).rightCup;
        String answer = "";
        while(currentCup.label != 1) {
            answer += currentCup.label;
            currentCup = currentCup.rightCup;
        }
        return answer;
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static class Cup {
        int label;
        Cup leftCup;
        Cup rightCup;
        
        public Cup(int label) {
            this.label = label;
        }
        
        public int getLabel() {
            return label;
        }
        
        public void initLeftCup(Cup cup) {
            this.leftCup = cup;
            cup.rightCup = this;
        }
        
        public void removeCup() {
            leftCup.rightCup = rightCup;
            rightCup.leftCup = leftCup;
        }
        
        public void setLeftCup(Cup cup) {
            this.leftCup = cup;
            this.rightCup = cup.rightCup;
            cup.rightCup = this;
        }
    }
}
