package com.jeffrpowell.adventofcode.aoc2018.solutions;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;

public class Day11 extends Solution2018<Integer>
{
    private static final int GRID_SIZE = 300;
    private Map<Point2D, Long> summedAreaPowerGrid;
    private Set<Integer> windowSizesToSkip;
    private Point2D bestWindowLocation;
    private long bestSum;
    private int bestWindowSize;
    private int serial;
    
	@Override
	public int getDay()
	{
		return 11;
	}
	
	@Override
	public InputParser<Integer> getInputParser()
	{
		return InputParserFactory.getIntegerParser();
	}
	
    @Override
    public String part1(List<Integer> input) {
        serial = input.get(0);
        List<List<Integer>> powerLevels = new ArrayList<>();
        for (int x = 1; x <= GRID_SIZE; x++) {
            List<Integer> row = new ArrayList<>();
            for (int y = 1; y <= GRID_SIZE; y++) {
                row.add(getPowerLevel(new Point2D.Double(x, y)));
            }
            powerLevels.add(row);
        }
        Map<Point2D, Integer> sums = new HashMap<>();
        
        for (int x = 0; x < GRID_SIZE - 2; x++) {
            for (int y = 0; y < GRID_SIZE - 2; y++) {
                int sum = powerLevels.get(x).get(y) + 
                          powerLevels.get(x+1).get(y) + 
                          powerLevels.get(x+2).get(y) + 
                          powerLevels.get(x).get(y+1) + 
                          powerLevels.get(x+1).get(y+1) + 
                          powerLevels.get(x+2).get(y+1) + 
                          powerLevels.get(x).get(y+2) + 
                          powerLevels.get(x+1).get(y+2) + 
                          powerLevels.get(x+2).get(y+2);
                sums.put(new Point2D.Double(x + 1, y + 1), sum);
            }
        }
        Point2D highestSumPt = sums.entrySet().stream().reduce(BinaryOperator.maxBy(Comparator.comparing(Map.Entry::getValue))).get().getKey();
        return highestSumPt.getX() + "," + highestSumPt.getY();
    }
    
    private int getPowerLevel(Point2D pt) {
        double rackId = getRackId(pt);
        double powerLevelCipher = rackId * pt.getY();
        powerLevelCipher += serial;
        powerLevelCipher *= rackId;
        String powerLevelCipherText = Integer.toString(Double.valueOf(powerLevelCipher).intValue());
        int powerLevelCipherTextLength = powerLevelCipherText.length();
        int powerLevel;
        if (powerLevelCipherTextLength < 3) {
            powerLevel = 0;
        }
        else {
            powerLevel = Integer.parseInt(powerLevelCipherText.substring(powerLevelCipherTextLength - 3, powerLevelCipherTextLength - 2));
        }
        return powerLevel - 5;
    }
    
    private double getRackId(Point2D pt) {
        return pt.getX() + 10;
    }

    @Override
    public String part2(List<Integer> input) {
        serial = input.get(0);
        summedAreaPowerGrid = new HashMap<>();
        windowSizesToSkip = new HashSet<>();
        for (int y = 1; y <= GRID_SIZE; y++) {
            for (int x = 1; x <= GRID_SIZE; x++) {
                Point2D pt = new Point2D.Double(x, y);
                long powerLevel = getPowerLevel(pt);
                long topSum = 0;
                long leftSum = 0;
                long diagonalSum = 0;
                if (x > 1) {
                    leftSum = summedAreaPowerGrid.get(new Point2D.Double(x - 1, y));
                }
                if (y > 1) {
                    topSum = summedAreaPowerGrid.get(new Point2D.Double(x, y - 1));
                }
                if (x > 1 && y > 1) {
                    diagonalSum = summedAreaPowerGrid.get(new Point2D.Double(x - 1, y - 1));
                }
                summedAreaPowerGrid.put(pt, powerLevel + leftSum + topSum - diagonalSum);
            }
        }
        bestSum = Long.MIN_VALUE;
        for (int i = 2; i <= 300; i++) {
            if (windowSizesToSkip.contains(i)) continue;
            searchForBetterWindow(i);
        }
        //Off by one error never ironed out
        //Answer is either x+1,y+1,size-1 or x-1,y-1,size+1
        return bestWindowLocation.getX() + "," + bestWindowLocation.getY() + "," + bestWindowSize + "  (" + bestSum+")";
    }
    
    private void searchForBetterWindow(int windowSize) {
        int window = windowSize - 1;
        long localBestSum = Long.MIN_VALUE;
        for (int y = 1; y <= GRID_SIZE - window; y++) {
            for (int x = 1; x <= GRID_SIZE - window; x++) {
                //+-------
                //|  a---b
                //|  |   |
                //|  c---d
                Point2D pt = new Point2D.Double(x, y);
                long a = summedAreaPowerGrid.get(pt);
                long b = summedAreaPowerGrid.get(new Point2D.Double(x + window, y));
                long c = summedAreaPowerGrid.get(new Point2D.Double(x, y + window));
                long d = summedAreaPowerGrid.get(new Point2D.Double(x + window, y + window));
                long sum = d - b - c + a;
                localBestSum = Math.max(localBestSum, sum);
                if (sum > bestSum) {
                    bestSum = sum;
                    bestWindowLocation = pt;
                    bestWindowSize = windowSize;
                }
            }
        }
        if (localBestSum <= 0) {
            while(windowSize < GRID_SIZE) {
                windowSize += windowSize;
                windowSizesToSkip.add(windowSize);
            }
        }
    }
}
