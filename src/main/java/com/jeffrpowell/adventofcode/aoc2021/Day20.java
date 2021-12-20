package com.jeffrpowell.adventofcode.aoc2021;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeffrpowell.adventofcode.Point2DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day20 extends Solution2021<Section> {
    Map<Point2D, Boolean> image;
    List<Boolean> algorithm;
    double currentMinX;
    double currentMaxX;
    double currentMinY;
    double currentMaxY;
    boolean liveMode;

    @Override
    public int getDay() {
        return 20;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, false));
    }

    @Override
    protected String part1(List<Section> input) {
        instantiate(input);
        liveMode = Boolean.TRUE.equals(algorithm.get(0));
        // Point2DUtils.printPoints(image.entrySet().stream().filter(entry -> Boolean.TRUE.equals(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toSet()));
        enhance(liveMode);
        // Point2DUtils.printPoints(image.entrySet().stream().filter(entry -> Boolean.TRUE.equals(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toSet()));
        enhance(false);
        // Point2DUtils.printPoints(image.entrySet().stream().filter(entry -> Boolean.TRUE.equals(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toSet()));
        return Long.toString(image.entrySet().stream().filter(entry -> {
            Point2D pt = entry.getKey();
            return pt.getX() != 105 && pt.getY() != -6 && pt.getY() != 105;
        }).map(Map.Entry::getValue).filter(Boolean.TRUE::equals).count());
    }
    
    @Override
    protected String part2(List<Section> input) {
        instantiate(input);
        liveMode = Boolean.TRUE.equals(algorithm.get(0));
        for (int i = 0; i < 50; i++) {
            enhance(i % 2 == 0 && liveMode);
        }
        Point2DUtils.printPoints(image.entrySet().stream().filter(entry -> Boolean.TRUE.equals(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toSet()));
        return Long.toString(image.entrySet().stream().filter(entry -> {
            Point2D pt = entry.getKey();
            return pt.getX() != 297 && pt.getY() != -198 && pt.getY() != 297;
        }).map(Map.Entry::getValue).filter(Boolean.TRUE::equals).count());
    }

    private void instantiate(List<Section> input) {
        algorithm = input.get(0).getInput(InputParserFactory.getStringParser()).get(0)
            .chars()
            .mapToObj(c -> c == '#')
            .collect(Collectors.toList());
        List<String> imageLines = input.get(1).getInput(InputParserFactory.getStringParser());
        currentMinX = -2D;
        currentMinY = -2D;
        currentMaxX = imageLines.get(0).length() + 1D;
        currentMaxY = imageLines.size() + 1D;
        image = Point2DUtils.generateGrid(0, 0, imageLines.get(0).length(), imageLines.size()).collect(Collectors.toMap(
            Function.identity(),
            pt -> imageLines.get(d2i(pt.getY())).charAt(d2i(pt.getX())) == '#'
        ));
        for (double col = currentMinX; col <= currentMaxX; col++) {
            image.put(new Point2D.Double(col, currentMinY), false);
            image.put(new Point2D.Double(col, currentMinY + 1), false);
            image.put(new Point2D.Double(col, currentMaxY - 1), false);
            image.put(new Point2D.Double(col, currentMaxY), false);
        }
        for (double row = 0; row < currentMaxY; row++) {
            image.put(new Point2D.Double(currentMinX, row), false);
            image.put(new Point2D.Double(currentMinX + 1, row), false);
            image.put(new Point2D.Double(currentMaxX - 1, row), false);
            image.put(new Point2D.Double(currentMaxX, row), false);
        }
    }

    private static int d2i(double d) {
        return Double.valueOf(d).intValue();
    }

    private static int l2i(long d) {
        return Long.valueOf(d).intValue();
    }

    private void enhance(Boolean frontierBit) {
        image = image.keySet().stream()
            .collect(Collectors.toMap(
                Function.identity(),
                pt -> algorithmLookup(bitsToLong(grabNeighbors(pt)))
            ));
            expandFrontierSimple(frontierBit);
    }

    private List<Boolean> grabNeighbors(Point2D pt) {
        double x = pt.getX();
        double y = pt.getY();
        return Stream.of(
            new Point2D.Double(x - 1, y - 1),
            new Point2D.Double(x, y - 1),
            new Point2D.Double(x + 1, y - 1),
            new Point2D.Double(x - 1, y),
            pt,
            new Point2D.Double(x + 1, y),
            new Point2D.Double(x - 1, y + 1),
            new Point2D.Double(x, y + 1),
            new Point2D.Double(x + 1, y + 1)
        ).map(image::get).collect(Collectors.toList());
    }

    public static long bitsToLong(List<Boolean> bits) {
        long value = 0L;
        for (int i = 0; i < bits.size(); ++i) {
            value += Boolean.TRUE.equals(bits.get(i)) ? (1L << (bits.size() - i - 1)) : 0L;
        }
        return value;
    }

    public Boolean algorithmLookup(long index) {
        return algorithm.get(l2i(index));
    }

    private void expandFrontierSimple(Boolean frontierBit) {
        currentMinX -= 4;
        currentMinY -= 4;
        currentMaxY += 4;
        currentMaxX += 4;
        for (double row = currentMinY; row <= currentMaxY; row++) {
            for (double col = currentMinX; col <= currentMaxX; col++) {
                Point2D pt = new Point2D.Double(col, row);
                image.computeIfAbsent(pt, p -> frontierBit);
            }
        }
    }
}
