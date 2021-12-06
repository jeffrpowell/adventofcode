package com.jeffrpowell.adventofcode.aoc2021;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day6 extends Solution2021<List<Integer>>{

    static ForkJoinPool pool = new ForkJoinPool();

    @Override
    public int getDay() {
        return 6;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerCSVParser();
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        // for (int i = 0; i < 79; i++) {
        //     simulate(i, input.get(0));
        //     Lanternfish.allFish.clear();
        // }
        String answer = simulate(80, input.get(0));
        pool.shutdown();
        return answer;
    }
    
    @Override
    protected String part2(List<List<Integer>> input) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private String simulate(int days, List<Integer> initFish) {
        initFish.stream()
            .map(timer -> new Lanternfish(days, timer))
            .forEach(pool::invoke);
        pool.awaitQuiescence(30, TimeUnit.MINUTES);
        // System.out.println("After " + days + " days: " + stringAllFish());
        return Integer.toString(Lanternfish.getAllFish().size());
    }

    // private String stringAllFish() {
    //     return Lanternfish.getAllFish().stream()
    //         .sorted(Comparator.comparing(Lanternfish::getId))
    //         .map(Lanternfish::toString)
    //         .collect(Collectors.joining(","));
    // }

    private static class Lanternfish extends RecursiveAction {
        private static int ID_SEQUENCE = 1;
        private static Set<Lanternfish> allFish = new HashSet<>();
        private synchronized static void addFish(Lanternfish fish) {
            allFish.add(fish);
        }
        public static Set<Lanternfish> getAllFish() {
            return allFish;
        }

        private int id;
        private int timeLeft;
        private int timer;

        public Lanternfish(int timeLeft, int initTimer) {
            this.id = ID_SEQUENCE++;
            this.timeLeft = timeLeft;
            this.timer = initTimer;
            addFish(this);
        }

        @Override
        protected void compute() {
            Collection<Lanternfish> offspring = new ArrayList<>();
            while (timeLeft > timer) {
                timeLeft -= timer;
                Lanternfish fish = new Lanternfish(timeLeft, 9);
                if (timeLeft > 9) {
                    offspring.add(fish);
                }
                addFish(fish);
                timer = 7;
            }
            timer -= timeLeft;
            offspring.stream().forEach(Lanternfish::invoke);
        }
        
        // public int getId() {
        //     return id;
        // }

        @Override
        public String toString() {
            return Integer.toString(timer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public boolean equals(Object o) {
            return Objects.equals(id, ((Lanternfish)o).id);
        }
    }
    
}
