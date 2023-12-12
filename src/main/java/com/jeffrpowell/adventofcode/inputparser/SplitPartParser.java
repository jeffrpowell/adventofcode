package com.jeffrpowell.adventofcode.inputparser;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SplitPartParser<A,B> implements InputParser<SplitPartParser.Part<A,B>>{
    private final Pattern splitterRegex;
    private final InputParser<A> aParser;
    private final InputParser<B> bParser;
    
    public SplitPartParser(Pattern splitterRegex, InputParser<A> aParser, InputParser<B> bParser) {
        this.splitterRegex = splitterRegex;
        this.aParser = aParser;
        this.bParser = bParser;
    }

    @Override
    public List<Part<A, B>> parseInput(List<String> input) {
        return input.stream()
            .map(splitterRegex::split)
            .map(arr -> new Part<>(
                aParser.parseInput(List.of(arr[0])).get(0),
                bParser.parseInput(List.of(arr[1])).get(0)
            ))
            .collect(Collectors.toList());
    }

    public static record Part<A,B>(A firstPart, B secondPart) {}
}
