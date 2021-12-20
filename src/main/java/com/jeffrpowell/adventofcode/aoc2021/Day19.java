package com.jeffrpowell.adventofcode.aoc2021;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

import javafx.geometry.Point3D;

public class Day19 extends Solution2021<Section> {

    @Override
    public int getDay() {
        return 19;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, true));
    }

    @Override
    protected String part1(List<Section> input) {
        List<ProbeScanner> scanners = input.stream().map(s -> new ProbeScanner(extractPoints(s))).collect(Collectors.toList());
        return null;
    }

    @Override
    protected String part2(List<Section> input) {
        // TODO Auto-generated method stub
        return null;
    }

    private Set<Point3D> extractPoints(Section s) {
        return s.getInput(InputParserFactory.getSectionParser(SectionSplitStrategyFactory.countNewlines(1, false))).get(1)
            .getInput(InputParserFactory.getRuleParser("\n", Pattern.compile("(-?\\d+),(-?\\d+),(-?\\d+)"))).stream()
                .map(r -> new Point3D(r.getDouble(0), r.getDouble(1), r.getDouble(2)))
                .collect(Collectors.toSet());
    }
    
    private static class ProbeScanner {
        Set<Point3D> pts;

        public ProbeScanner(Set<Point3D> pts) {
            this.pts = pts;
        }
        
    }
}
