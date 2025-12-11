package com.jeffrpowell.adventofcode.aoc2025;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.SplitPartParser.Part;

public class Day11 extends Solution2025<Part<String, List<String>>>{
    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public InputParser<Part<String, List<String>>> getInputParser() {
        return InputParserFactory.getSplitPartParser(Pattern.compile(": "), InputParserFactory.getStringParser(), InputParserFactory.getTokenSVParser(" "));
    }

    private record Path(String tip, boolean visitedDAC, boolean visitedFFT){
        public boolean completed() {
            return tip.equals("out");
        }

        public boolean winner() {
            return visitedDAC && visitedFFT;
        }
    }
    
    @Override
    protected String part1(List<Part<String, List<String>>> input) {
        Map<String, List<String>> outputs = new HashMap<>();
        for (Part<String,List<String>> part : input) {
            outputs.putIfAbsent(part.firstPart(), new ArrayList<>());
            for (String down : part.secondPart()) {
                outputs.get(part.firstPart()).add(down);
            }
        }
        return runPart1(outputs, "you", "out");
    }

    private String runPart1(Map<String, List<String>> outputs, String start, String finish) {
        List<String> tips = new ArrayList<>();
        tips.add(start);
        long count = 0;
        // int debug = 0;
        while (!tips.isEmpty()) {
            List<String> newTips = new ArrayList<>();
            for (String next : tips) {
                if (next.equals(finish)) {
                    count++;
                }
                else if (outputs.containsKey(next)){
                    newTips.addAll(outputs.get(next));
                }
            }
            tips = newTips;
            // if (++debug == 6) {
            //     System.out.println(tips.size());
            //     debug = 0;
            // }
        }
        return Long.toString(count);
    }


    @Override
    protected String part2(List<Part<String, List<String>>> input) {
        Map<String, List<String>> outputs = new HashMap<>();
        for (Part<String,List<String>> part : input) {
            outputs.putIfAbsent(part.firstPart(), new ArrayList<>());
            for (String down : part.secondPart()) {
                outputs.get(part.firstPart()).add(down);
            }
        }
        // return runPart1(outputs, "dac", "out");
        List<Path> tips = new ArrayList<>();
        tips.add(new Path("svr", false, false));
        long count = 0;
        while (!tips.isEmpty()) {
            List<Path> newTips = new ArrayList<>();
            for (Path next : tips) {
                if (next.completed()) {
                    if (next.winner()) {
                        count++;
                    }
                }
                else if (outputs.containsKey(next.tip())){
                    for (String output : outputs.get(next.tip())) {
                        boolean isFFT = output.equals("fft");
                        boolean isDAC = output.equals("dac");
                        newTips.add(new Path(output, isDAC || next.visitedDAC(), isFFT || next.visitedFFT()));
                    }
                }
            }
            tips = newTips;
        }
        return Long.toString(count);
    }
}
