package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Day13 extends Solution2020<List<String>>{

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public InputParser<List<String>> getInputParser() {
        return InputParserFactory.getCSVParser();
    }

    @Override
    protected String part1(List<List<String>> input) {
        throw new UnsupportedOperationException("This was solved manually");
        /*
        <busId> * min(<busId> - (1001798 % <busId>))
        <busId> == 29
        answer == 203
        */
    }

    @Override
    protected String part2(List<List<String>> input) {
        /*
        We have a few hints that provide some quick constraints:
        Puzzle prompt says to start searching after 10^14 (i.e. 100TT)
        The starting timestamp must be divisible by 19
        The longest-looping bus loops every 859 minutes (so don't check all numbers divisible by 19; check all numbers divisible by (19 * 859) => 16321)
        
        Scratch notes:
        100_000_000_000_869L % 859 == 0
        100_000_000_000_858L % 19 == 0
        diff between those two timestamps is 11 => we want it to be 19
        100_000_000_001_728L % 859 == 0
        100_000_000_001_713L % 19 == 0
        diff between those two timestamps is 15 => we want it to be 19
        100_000_000_002_587L % 859 == 0
        100_000_000_002_568L % 19 == 0
        diff between those two timestamps is 19!
        check my assertion (incrementing by 16321)
        100_000_000_018_908L % 859 == 0
        100_000_000_018_889L % 19 == 0
        diff between those two timestamps is 19!
        maybe I increment adding one bus at a time to this multiplier
            we have a starting number that works for two buses
            we have a multiplier that guarantees that it will always work for those two buses
            pick another bus, calculate its relative timestamp position
            if not a match, use the previous multiplier to try again
            at this point, we can shortcut and observe the diff between the two relative timestamp positions we have and extrapolate how many times we expect to have to do the "try again" loop
            we should now have a new starting point and a new multiplier that works for *3* buses!
        I'm thinking we sort the bus ids in descending order to bulk up the multiplier for quicker space-searching earlier
        So I ran with that algorithm; I found a ts for two more bus ids, then combinatorically exploded...
            Found ts for 373!  100000002499681  multipler: 6087733
            Found ts for 41!  100000045113812  multipler: 249597053
        Going in ascending order was worse
        Debugging revealed infinite loop looking for busId 37 (offset 87)
        Ah, bug: need to breakdown the 87 offset to actually look for an offset of 87 % 37 (the bus id)
        */
        Solver solver = new Solver(100_000_000_002_568L, 16_321L); //divisible by 19; offset +19 is divisible by 859
        NavigableMap<Integer, Integer> busIdToOffset = new TreeMap<>(Comparator.naturalOrder());
        List<String> buses = input.get(1);
        for (int i = 0; i < buses.size(); i++) {
            String busIdString = buses.get(i);
            try {
                int busId = Integer.parseInt(busIdString);
                if (busId != 19 && busId != 859) { //manually incorporated these ones already ^
                    busIdToOffset.put(busId, i);
                }
            }
            catch (NumberFormatException e) {}
        }
        busIdToOffset.descendingMap().entrySet().stream().forEachOrdered(entry -> solver.incorporateNextBus(entry.getKey(), entry.getValue()));
        return Long.toString(solver.ts);
    }
    
    private static class Solver {
        long ts;
        long multiplier;

        public Solver(long ts, long multiplier) {
            this.ts = ts;
            this.multiplier = multiplier;
        }
        
        /*
        pick another bus, calculate its relative timestamp position
        if not a match, use the previous multiplier to try again
        at this point, we can shortcut and observe the diff between the two relative timestamp positions we have and extrapolate how many times we expect to have to do the "try again" loop
        we should now have a new starting point and a new multiplier that works for *3* buses!
        */
        public void incorporateNextBus(int busId, int offset) {
            offset %= busId;
            long testOffset = busId - (ts % busId);
            System.out.println("Finding " + busId);
            while(testOffset != offset) {
//                System.out.println(ts + ": testOffset == " + testOffset + "; looking for " + offset);
                ts += multiplier;
                testOffset = busId - (ts % busId);
            }
            multiplier *= busId;
            System.out.println("Found ts for " + busId + "!  " + ts + "  multipler: " + multiplier);
        }
    }

}
