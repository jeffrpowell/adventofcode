package com.jeffrpowell.adventofcode.inputparser;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PreParser<T> implements InputParser<T>{
    private final InputParser<T> delegate;
    private final Function<String, String> fn;

    public PreParser(InputParser<T> delegate, Function<String, String> fn) {
        this.delegate = delegate;
        this.fn = fn;
    }

    @Override
    public List<T> parseInput(List<String> input) {
        return delegate.parseInput(input.stream().map(fn::apply).collect(Collectors.toList()));
    }
}
