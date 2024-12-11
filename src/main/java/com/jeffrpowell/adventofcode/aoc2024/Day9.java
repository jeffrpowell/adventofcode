package com.jeffrpowell.adventofcode.aoc2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;

public class Day9 extends Solution2024<List<Integer>>{
    @Override
    public int getDay() {
        return 9;
    }

    @Override
    public InputParser<List<Integer>> getInputParser() {
        return InputParserFactory.getIntegerTokenSVParser("");
    }

    @Override
    protected String part1(List<List<Integer>> input) {
        List<Integer> disk = input.get(0);
        int end = disk.size()-1;
        int endBlocks = disk.get(end);
        int pt = 0;
        int blockId = 0;
        int endBlockId = end / 2;
        boolean freeSpace = false;
        List<Integer> compressedDisk = new ArrayList<>();
        while (pt < end) {
            if (freeSpace) {
                int free = disk.get(pt);
                while (free > 0) {
                    final int _endBlockId = endBlockId;
                    if (free >= endBlocks) {
                        IntStream.range(0, endBlocks).forEach(i -> compressedDisk.add(_endBlockId));
                        free -= endBlocks;
                        end -= 2;
                        endBlockId--;
                        endBlocks = disk.get(end);
                        if (end < pt) {
                            break;
                        }
                    }
                    else {
                        IntStream.range(0, free).forEach(i -> compressedDisk.add(_endBlockId));
                        endBlocks -= free;
                        free = 0;
                    }
                }
            }
            else {
                final int _blockId = blockId;
                IntStream.range(0, disk.get(pt)).forEach(i -> compressedDisk.add(_blockId));
                blockId++;
            }
            pt++;
            freeSpace = !freeSpace;
        }
        if (freeSpace) {
            final int _blockId = blockId;
            IntStream.range(0, disk.get(pt)).forEach(i -> compressedDisk.add(_blockId));
        }
        else {
            final int _endBlockId = endBlockId;
            IntStream.range(0, endBlocks).forEach(i -> compressedDisk.add(_endBlockId));
        }
        long checksum = 0;
        for (int i = 0; i < compressedDisk.size(); i++) {
            checksum += compressedDisk.get(i) * i;
        }
        return Long.toString(checksum);
    }

    @Override
    protected String part2(List<List<Integer>> input) {
        List<Integer> disk = input.get(0);
        List<File> files = new ArrayList<>();
        Map<Integer, Map<Integer, Boolean>> freeSpaces = new HashMap<>();
        IntStream.range(0, 10).forEach(i -> freeSpaces.put(i, new HashMap<>()));
        boolean freeSpace = false;
        int position = 0;
        int fileId = 0;
        for (Integer block : disk) {
            if (freeSpace) {
                freeSpaces.get(block).put(position, false);
            }
            else {
                files.add(new File(position, fileId++, block));
            }
            position += block;
            freeSpace = !freeSpace;
        }
        files = files.reversed();
        for (File file : files) {
            // freeSpaces.keySet().stream()
            //     .filter(k -> k >= file.size())
            //     .map(freeSpaces::get)
            //     .filter(freeSpacesOfSize -> freeSpacesOfSize.values().stream()
            //         .anyMatch(v -> Boolean.FALSE.equals(v))
            //     )
            //     .map(freeSpacesOfSize -> freeSpacesOfSize.entrySet().stream()
            //         .filter(e -> Boolean.FALSE.equals(e.getValue()))
            //         .map(Map.Entry::getKey)
            //         .sorted()
            //         .findFirst().get()
            //     )
            //     .so
        }
        return null;
    }

    record File(int position, int fileId, int size){}
}
