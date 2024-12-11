package com.jeffrpowell.adventofcode.aoc2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        Map<Integer, File> freeSpaces = new HashMap<>();
        boolean freeSpace = false;
        int position = 0;
        int fileId = 0;
        List<Integer> compressedDisk = new ArrayList<>();
        for (Integer block : disk) {
            if (freeSpace) {
                freeSpaces.put(position, new File(position, -1, block));
                IntStream.range(0, block).forEach(i -> compressedDisk.add(-1));
            }
            else {
                final int _fileId = fileId;
                files.add(new File(position, fileId++, block));
                IntStream.range(0, block).forEach(i -> compressedDisk.add(_fileId));
            }
            position += block;
            freeSpace = !freeSpace;
        }
        files = files.reversed();
        for (File file : files) {
            position = 0;
            while (position < file.position()) {
                if (compressedDisk.get(position) == -1) {
                    File emptySpace = freeSpaces.get(position);
                    if (emptySpace.size() >= file.size()) {
                        int _position = position;
                        IntStream.range(0, file.size()).forEach(i -> compressedDisk.set(_position+i, file.fileId()));
                        IntStream.range(0, file.size()).forEach(i -> compressedDisk.set(file.position()+i, -1));
                        freeSpaces.remove(position);
                        if (emptySpace.size() != file.size()) {
                            int newEmptySize = emptySpace.size() - file.size();
                            int newEmptyPosition = position + file.size();
                            freeSpaces.put(newEmptyPosition, new File(newEmptyPosition, -1, newEmptySize));
                        }
                        position += file.size();
                        break;
                    }
                    else {
                        position += emptySpace.size();
                    }
                }
                else {
                    position++;
                }
            }
        }
        List<Integer> finalCompressedDisk = compressedDisk.stream()
            .map(block -> block == -1 ? 0 : block)
            .collect(Collectors.toList());
        long checksum = 0;
        for (int i = 0; i < finalCompressedDisk.size(); i++) {
            checksum += finalCompressedDisk.get(i) * i;
        }
        return Long.toString(checksum);
    }

    record File(int position, int fileId, int size){}
}
