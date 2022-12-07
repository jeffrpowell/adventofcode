package com.jeffrpowell.adventofcode.aoc2022;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        return Long.toString(directories.values().stream()
            .map(Directory::getTotalSize)
            .filter(l -> l < 100_000L)
            .collect(Collectors.reducing(0L, Math::addExact)));
    }

    private Rule parseDirectory(Iterator<Rule> i, Directory context, Map<String, Directory> directories) {
        while (i.hasNext()) {
            Rule line = i.next();
            switch (line.getRulePatternKey()) {
                case LINE_DIR:
                    context.children.add(directories.computeIfAbsent(line.getString(0), Directory::new));
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
        private static Map<String, Long> sizeCache = new HashMap<>();
        private final String name;
        private final List<Directory> children;
        private long size;

        public Directory(String name) {
            this.name = name;
            this.children = new ArrayList<>();
            this.size = 0L;
            sizeCache.put(name, 0L);
        }

        public void addSize(long size) {
            this.size += size;
        }

        public long getTotalSize() {
            return children.stream()
                .map(d -> sizeCache.computeIfAbsent(d.name, n -> d.getTotalSize()))
                .collect(Collectors.reducing(size, Math::addExact));
        }
    }
}
