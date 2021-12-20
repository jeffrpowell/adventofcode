package com.jeffrpowell.adventofcode.aoc2021;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Pattern;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day12 extends Solution2021<Rule> {
    static Map<String, Cave> caves = new HashMap<>();
    
    @Override
    public int getDay() {
        return 12;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("(\\w+)-(\\w+)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        for (Rule rule : input) {
            String cave1 = rule.getString(0);
            String cave2 = rule.getString(1);
            caves.putIfAbsent(cave1, new Cave(cave1));
            caves.putIfAbsent(cave2, new Cave(cave2));
            caves.get(cave1).addNeighbor(caves.get(cave2));
            caves.get(cave2).addNeighbor(caves.get(cave1));
        }
        caves.get("end").neighbors.clear();
        Queue<CavePath> q = new ArrayDeque<>();
        CavePath start = new CavePath(caves.get("start"), false);
        q.add(start);
        List<CavePath> finishedPaths = new ArrayList<>();
        while(!q.isEmpty()) {
            CavePath path = q.poll();
            List<CavePath> nextPaths = path.nextPaths();
            for (CavePath nextPath : nextPaths) {
                if (nextPath.terminated()) {
                    finishedPaths.add(nextPath);
                }
                else {
                    q.add(nextPath);
                }
            }
        }
        return Integer.toString(finishedPaths.size());
    }

    @Override
    protected String part2(List<Rule> input) {
        for (Rule rule : input) {
            String cave1 = rule.getString(0);
            String cave2 = rule.getString(1);
            caves.putIfAbsent(cave1, new Cave(cave1));
            caves.putIfAbsent(cave2, new Cave(cave2));
            caves.get(cave1).addNeighbor(caves.get(cave2));
            caves.get(cave2).addNeighbor(caves.get(cave1));
        }
        caves.get("end").neighbors.clear();
        Deque<CavePath> q = new ArrayDeque<>();
        CavePath start = new CavePath(caves.get("start"), true);
        q.push(start);
        long finishedPaths = 0;
        while(!q.isEmpty()) {
            CavePath path = q.pop();
            List<CavePath> nextPaths = path.nextPaths();
            for (CavePath nextPath : nextPaths) {
                if (nextPath.terminated()) {
                    finishedPaths++;
                }
                else {
                    q.add(nextPath);
                }
            }
        }
        return Long.toString(finishedPaths);
    }
    
    private static class Cave {
        List<Cave> neighbors;
        String name;
        boolean multiVisit;

        public Cave(String name) {
            this.neighbors = new ArrayList<>();
            this.name = name;
            this.multiVisit = !name.equals(name.toLowerCase());
        }

        public void addNeighbor(Cave c) {
            neighbors.add(c);
        }
    }

    private static class CavePath extends HashSet<String> {
        Cave head;
        boolean tookSmallException;
        boolean part2;

        public CavePath(Cave head, boolean part2) {
            super();
            this.head = head;
            this.part2 = part2;
            this.tookSmallException = false;
            visit(head);
        }

        public CavePath(CavePath o) {
            super(o); //TODO: Profiler indicates this is what blows heap for part 2 (coming from CavePath::nextPaths)
            this.head = o.head;
            this.part2 = o.part2;
            this.tookSmallException = o.tookSmallException;
        }

        public List<CavePath> nextPaths() {
            List<CavePath> next = new ArrayList<>();
            for (Cave neighbor : head.neighbors) {
                if (!visited(neighbor)) {
                    CavePath copy = copy();
                    copy.visit(neighbor);
                    next.add(copy);
                }
            }
            return next;
        }

        public boolean visited(Cave cave) {
            if (cave.multiVisit || canTakeException(cave.name) || !contains(cave.name)) {
                return false;
            }
            return true;
        }

        private boolean canTakeException(String name) {
            return part2 && !tookSmallException && !name.equals("start") && !name.equals("end");
        }

        public void visit(Cave cave) {
            if (part2 && contains(cave.name) && !cave.multiVisit) {
                tookSmallException = true;
            }
            add(cave.name);
            head = cave;
        }

        public boolean terminated() {
            return contains("end");
        }

        public CavePath copy() {
            return new CavePath(this);
        }
    }

}
