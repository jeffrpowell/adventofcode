package com.jeffrpowell.adventofcode.inputparser.rule;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RuleParser implements InputParser<Rule>{

    private static final Logger log = LogManager.getLogger(InputParser.class);
    private final String ruleDelimiter;
    private final Map<String, Pattern> patterns;

    public RuleParser(String ruleDelimiter, Map<String, Pattern> patterns) {
        this.ruleDelimiter = ruleDelimiter;
        this.patterns = patterns;
    }

    @Override
    public List<Rule> parseInput(List<String> inputs) {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<Rule>> ruleJobs = new ArrayList<>();
        int inlineCounter = 0;
        for (String input: inputs) {
            String[] splitInputs = input.split(ruleDelimiter);
            for (String splitInput : splitInputs) {
                ruleJobs.add(executor.submit(new RuleParserJob(inlineCounter++, splitInput, patterns)));
            }
        }
        List<Rule> rules = ruleJobs.stream().parallel()
            .map(f -> {
                try {
                    return Optional.of(f.get());
                } catch (InterruptedException | ExecutionException ex) {
                    log.error("Problem while parsing inputs through rule parser", ex);
                }
                return Optional.<Rule>empty();
            })
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        executor.shutdown();
        return rules;
    }
}
