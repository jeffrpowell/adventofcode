package com.jeffrpowell.adventofcode.aoc2021;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day6 extends Solution2021<List<Integer>>{

    static ForkJoinPool pool = new ForkJoinPool();
    static Map<Integer, List<Integer>> childrenLUT = new HashMap<>();
    static Map<Integer, List<Integer>> rootLUT = new HashMap<>();

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
        int days = 256;
        
        for (int i = 1; i < 6; i++) {
            List<Integer> birthDays = new ArrayList<>();
            int countdown = days;
            countdown -= i + 1;
            birthDays.add(countdown);
            while(countdown > 7) {
                countdown -= 7;
                birthDays.add(countdown);
            }
            rootLUT.put(i, birthDays);
        }
        for (int i = 0; i <= days; i++) {
            List<Integer> birthDays = new ArrayList<>();
            if (i < 9) {
                childrenLUT.put(i, birthDays);
                continue;
            }
            int countdown = days;
            countdown -= 9;
            birthDays.add(countdown);
            while(countdown > 7) {
                countdown -= 7;
                birthDays.add(countdown);
            }
            childrenLUT.put(i, birthDays);
        }

        return Long.toString(
            input.get(0).stream()
                .map(rootLUT::get)
                .map(root -> root.stream()
                    .map(child -> countChildren(child))
                    .collect(Collectors.reducing(0L, Math::addExact)) + 1)
                .collect(Collectors.reducing(0L, Math::addExact))
        );
    }

    private long countChildren(int child) {
        return childrenLUT.get(child).stream()
            .map(grandchild -> countChildren(grandchild))
            .collect(Collectors.reducing(0L, Math::addExact)) + 1;
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

    private static class LookupPacket {
        List<Integer> birthDays;

        public LookupPacket(List<Integer> birthDays) {
            this.birthDays = birthDays;
        }


    }

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
