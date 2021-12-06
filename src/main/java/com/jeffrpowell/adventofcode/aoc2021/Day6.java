package com.jeffrpowell.adventofcode.aoc2021;

import java.util.ArrayList;
import java.util.List;
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
        int initialCount = input.get(0).size();
        input.get(0).stream()
            .map(timer -> new Lanternfish(2, timer))
            .forEach(pool::invoke);
        pool.awaitQuiescence(30, TimeUnit.MINUTES);
        pool.shutdown();
        return Integer.toString(Lanternfish.getTotal() + initialCount);
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        // TODO Auto-generated method stub
        return null;
    }

    private static class Lanternfish extends RecursiveAction {
        private static int ID_SEQUENCE = 0;
        private static int total = 0;
        private synchronized static void increment() {
            total++;
        }
        public static int getTotal() {
            return total;
        }

        private int id;
        private int timeLeft;
        private int timer;

        public Lanternfish(int timeLeft, int initTimer) {
            this.timeLeft = timeLeft;
            this.timer = initTimer;
            this.id = ID_SEQUENCE++;
        }

        @Override
        protected void compute() {
            List<RecursiveAction> offspring = new ArrayList<>();
            while (timeLeft >= timer) {
                timeLeft -= timer;
                offspring.add(new Lanternfish(timeLeft, 8));
                increment();
                timer = 6;
            }
            invokeAll(offspring);
        }
        
    }
    
}
