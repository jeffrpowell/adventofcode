package com.jeffrpowell.adventofcode.aoc2022;

import java.nio.file.Path;
import java.util.ArrayList;
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
        Map<Path, Directory> directories = new HashMap<>();
        parseDirectoryTree(input, directories);
        return Long.toString(directories.values().stream()
            .map(Directory::getTotalSizeCompute)
            .filter(l -> l <= 100_000L)
            .collect(Collectors.reducing(0L, Math::addExact)));
    }

    private void parseDirectoryTree(List<Rule> input, Map<Path, Directory> directories) {
        Iterator<Rule> i = input.iterator();
        Path contextPath = Path.of("");
        while (i.hasNext()) {
            Rule line = i.next();
            if (line.getRulePatternKey().equals(LINE_CD)) {
                contextPath = contextPath.resolve(line.getString(0));
                directories.computeIfAbsent(
                    contextPath, 
                    p -> new Directory(line.getString(0))
                );
            }
            else if (line.getRulePatternKey().equals(LINE_CD_UP)) {
                contextPath = contextPath.getParent();
            }
            else if (line.getRulePatternKey().equals(LINE_LS)) {
                Rule nextLine = parseDirectory(i, contextPath, directories);
                if (nextLine != null) {
                    if (nextLine.getRulePatternKey().equals(LINE_CD)) {
                        contextPath = contextPath.resolve(nextLine.getString(0));
                        directories.computeIfAbsent(
                            contextPath, 
                            p -> new Directory(nextLine.getString(0))
                        );
                    }
                    else if (nextLine.getRulePatternKey().equals(LINE_CD_UP)) {
                        contextPath = contextPath.getParent();
                    }
                } 
            }
        }
    }

    private Rule parseDirectory(Iterator<Rule> i, Path contextPath, Map<Path, Directory> directories) {
        Directory context = directories.get(contextPath);
        while (i.hasNext()) {
            Rule line = i.next();
            switch (line.getRulePatternKey()) {
                case LINE_DIR:
                    context.addChild(directories.computeIfAbsent(
                        contextPath.resolve(line.getString(0)), 
                        p -> new Directory(line.getString(0))
                    ));
                    break;
                case LINE_FILE:
                    context.addFile(line.getLong(0));
                    break;
                default:
                    return line;
            }
        }
        return null;
    }

    @Override
    protected String part2(List<Rule> input) {
        Map<Path, Directory> directories = new HashMap<>();
        parseDirectoryTree(input, directories);
        directories.values().stream().forEach(Directory::cacheSize);
        // directories.get(Path.of("/")).printDirectory();
        long spaceToFreeUp = 30_000_000 - (70_000_000L - directories.get(Path.of("/")).getTotalSize());
        return Long.toString(directories.values().stream()
            .map(Directory::getTotalSize)
            .filter(size -> size >= spaceToFreeUp)
            .sorted()
            .findFirst().get());
    }

    private static class Directory {
        private final String name;
        private final List<Directory> children;
        private final List<Long> files;
        private long size;

        public Directory(String name) {
            this.name = name;
            this.children = new ArrayList<>();
            this.files = new ArrayList<>();
            this.size = 0L;
        }

        public void addChild(Directory child) {
            children.add(child);
        }

        public void addFile(long fileSize) {
            files.add(fileSize);
            size += fileSize;
        }

        public void cacheSize() {
            size = getTotalSizeCompute();
        }

        public long getTotalSizeCompute() {
            long sizeCounter = 0L;
            for (Directory child : children) {
                sizeCounter += child.getTotalSizeCompute();
            }
            return sizeCounter + files.stream().collect(Collectors.reducing(0L, Math::addExact));
        }

        public long getTotalSize() {
            return size;
        }

        public void printDirectory() {
            System.out.println(name + "("+size+")");
            int tabs = 1;
            children.stream().forEach(c -> c.printDirectory(tabs));
        }

        private void printDirectory(int tabs) {
            IntStream.range(0, tabs).forEach(i -> System.out.print("  "));
            System.out.println(name + "("+size+")");
            printFiles(tabs+1);
            children.stream().forEach(c -> c.printDirectory(tabs+1));
        }

        private void printFiles(int tabs) {
            String indent = IntStream.range(0, tabs).mapToObj(i -> "  ").collect(Collectors.joining());
            for (int i = 0; i < files.size(); i++) {
                System.out.println(indent + "file " + i + " (" + files.get(i) + ")");
            }
        }

    }
}
