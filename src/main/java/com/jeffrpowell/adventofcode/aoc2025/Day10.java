package com.jeffrpowell.adventofcode.aoc2025;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.algorithms.CharArrayUtils;
import com.jeffrpowell.adventofcode.algorithms.PowerSets;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day10 extends Solution2025<List<String>>{
    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getTokenSVParser(" ");
    }

    @Override
    protected String part1(List<List<String>> input) {
        List<Machine> machines = input.stream()
            .map(Machine::fromInput)
            .collect(Collectors.toList());
        long minpresses = 0;
        for (Machine machine : machines) {
            List<List<List<Boolean>>> buttonCombinations = PowerSets.getPowerSet(machine.buttons());
            int presses = Integer.MAX_VALUE;
            for (List<List<Boolean>> buttonSequence : buttonCombinations) {
                List<Boolean> buttonPressResult = pressButtonsLights(buttonSequence);
                if (buttonPressResult.equals(machine.target())) {
                    presses = Math.min(presses, buttonSequence.size());
                }
            }
            minpresses += presses;
        }
        return Long.toString(minpresses);
    }

    @Override
    protected String part2(List<List<String>> input) {
        List<Machine> machines = input.stream()
            .map(Machine::fromInput)
            .collect(Collectors.toList());
        long minpresses = 0;
        for (Machine machine : machines) {
            List<List<List<Boolean>>> buttonCombinations = PowerSets.getPowerSet(machine.buttons());
            int presses = Integer.MAX_VALUE;
            for (List<List<Boolean>> buttonSequence : buttonCombinations) {
                List<Integer> buttonPressResult = pressButtonsJoltages(buttonSequence);
                if (buttonPressResult.equals(machine.joltages())) {
                    presses = Math.min(presses, buttonSequence.size());
                }
            }
            minpresses += presses;
        }
        return Long.toString(minpresses);
    }

    record Machine(List<Boolean> target, List<List<Boolean>> buttons, List<Integer> joltages){
        public static Machine fromInput(List<String> inputs) {
            String lightsString = inputs.getFirst();
            String joltageString = inputs.getLast();
            lightsString = lightsString.replaceAll("[\\[\\]]","");
            List<Boolean> target = CharArrayUtils.toStream(lightsString.toCharArray())
                .map(c -> {
                    return switch (c){
                        case '#' -> true;
                        default -> false;
                    };
                })
                .collect(Collectors.toList());
            inputs = inputs.subList(1, inputs.size() - 1);
            List<List<Boolean>> buttons = inputs.stream()
                .map(buttonString -> parseButton(target.size(), buttonString))
                .collect(Collectors.toList());
                
            joltageString = joltageString.replaceAll("[\\{\\}]","");
            List<Integer> joltages = InputParserFactory.getIntegerCSVParser().parseInput(List.of(joltageString)).getFirst();
            return new Machine(target, buttons, joltages);
        }

        private static List<Boolean> parseButton(int lightsSize, String buttonString) {
            List<Boolean> allOptions = Stream.generate(() -> false).limit(lightsSize).collect(Collectors.toList());
            buttonString = buttonString.replaceAll("[\\(\\),]","");
            CharArrayUtils.toStream(buttonString.toCharArray())
                .map(String::valueOf)
                .map(Integer::parseInt)
                .forEach(i -> allOptions.set(i, true));
            return allOptions;
        }
    }

    private List<Boolean> pressButtonsLights(List<List<Boolean>> buttonSequence) {
        if (buttonSequence.isEmpty()) {
            return List.of();
        }
        int size = buttonSequence.getFirst().size();
        List<Boolean> state = Stream.generate(() -> false).limit(size).collect(Collectors.toList());
        for (List<Boolean> button : buttonSequence) {
            for (int i = 0; i < size; i++) {
                if (button.get(i)) {
                    state.set(i, !state.get(i));
                }
            }
        }
        return state;
    }

    //DOESN'T ACCOUNT FOR PRESSING SAME BUTTON MULTIPLE TIMES
    private List<Integer> pressButtonsJoltages(List<List<Boolean>> buttonSequence) {
        if (buttonSequence.isEmpty()) {
            return List.of();
        }
        int size = buttonSequence.getFirst().size();
        List<Integer> state = Stream.generate(() -> 0).limit(size).collect(Collectors.toList());
        for (List<Boolean> button : buttonSequence) {
            for (int i = 0; i < size; i++) {
                if (button.get(i)) {
                    state.set(i, state.get(i)+1);
                }
            }
        }
        return state;
    }
}
