package com.jeffrpowell.adventofcode.aoc2022;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

public class Day7 extends Solution2022<Rule>{
    private static final String LINE_CD_UP = "LINE_CD_UP";
    private static final String LINE_CD = "LINE_CD";
    private static final String LINE_LS = "LINE_LS";
    private static final String LINE_DIR = "LINE_DIR";
    private static final String LINE_FILE = "LINE_FILE";
    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n",
            Map.of(
                LINE_CD_UP, Pattern.compile("\\$ cd \\.\\."),
                LINE_CD, Pattern.compile("\\$ cd ([^\\.].*)"),
                LINE_LS, Pattern.compile("\\$ ls"),
                LINE_DIR, Pattern.compile("dir (.+)"),
                LINE_FILE, Pattern.compile("(\\d+) .+")
            )
        );
    }

    @Override
    protected String part1(List<Rule> input) {
        Map<String, Directory> directories = new HashMap<>();
        Iterator<Rule> i = input.iterator();
        Directory context = new Directory("/");
        while (i.hasNext()) {
            Rule line = i.next();
            if (line.getRulePatternKey().equals(LINE_CD)) {
                context = directories.computeIfAbsent(line.getString(0), Directory::new);
            }
            else if (line.getRulePatternKey().equals(LINE_LS)) {
                Rule nextLine = parseDirectory(i, context, directories);
                if (nextLine != null && nextLine.getRulePatternKey().equals(LINE_CD)) {
                    context = directories.computeIfAbsent(nextLine.getString(0), Directory::new);
                }
            }
        }
        directories.get("/").printDirectory();
        return Long.toString(directories.values().stream()
            .sorted(Comparator.comparing(Directory::getParentsCount).reversed())
            .peek(Directory::cacheSize)
            .map(Directory::getTotalSize)
            .filter(l -> l <= 100_000L)
            .collect(Collectors.reducing(0L, Math::addExact)));
    }

    private Rule parseDirectory(Iterator<Rule> i, Directory context, Map<String, Directory> directories) {
        while (i.hasNext()) {
            Rule line = i.next();
            switch (line.getRulePatternKey()) {
                case LINE_DIR:
                    context.addChild(directories.computeIfAbsent(line.getString(0), Directory::new));
                    break;
                case LINE_FILE:
                    context.size += line.getLong(0);
                    break;
                default:
                    return line;
            }
        }
        return null;
    }

    @Override
    protected String part2(List<Rule> input) {
        return null;
    }

    private static class Directory {
        protected static final Map<String, Long> SIZE_CACHE = new HashMap<>();
        private final String name;
        private final List<Directory> children;
        private Directory parent;
        private long size;

        public Directory(String name) {
            this.name = name;
            this.children = new ArrayList<>();
            this.parent = null;
            this.size = 0L;
            SIZE_CACHE.put(name, 0L);
        }

        public void addChild(Directory child) {
            children.add(child);
            child.parent = this;
        }

        public long getDescendency() {
            return children.size() + children.stream()
                .map(Directory::getDescendency)
                .collect(Collectors.reducing(0L, Math::addExact));
        }

        public long getParentsCount() {
            Directory parentTemp = this;
            int counter = 0;
            while (parentTemp.parent != null) {
                counter++;
                parentTemp = parentTemp.parent;
            }
            return counter;
        }

        public void cacheSize() {
            for (Directory child : children) {
                if (SIZE_CACHE.get(child.name) == 0L) {
                    long childSize = child.getTotalSize();
                    SIZE_CACHE.put(child.name, childSize);
                }
                size += SIZE_CACHE.get(child.name);
            }
            SIZE_CACHE.replace(name, size);
        }

        public long getTotalSize() {
            return size;
        }

        public void printDirectory() {
            System.out.println(name);
            int tabs = 1;
            children.stream().forEach(c -> c.printDirectory(tabs));
        }

        private void printDirectory(int tabs) {
            IntStream.range(0, tabs).forEach(i -> System.out.print("  "));
            System.out.println(name);
            children.stream().forEach(c -> c.printDirectory(tabs+1));
        }
    }
}
