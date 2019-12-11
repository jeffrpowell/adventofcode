package com.jeffrpowell.adventofcode.aoc2019;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.aoc2019.intcode.IntCodeComputer;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.awt.geom.Point2D;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Day11 extends Solution2019<List<BigInteger>>
{
	@Override
	public int getDay()
	{
		return 11;
	}

	@Override
	public InputParser<List<BigInteger>> getInputParser()
	{
		return InputParserFactory.getBigIntegerCSVParser();
	}

	@Override
	protected String part1(List<List<BigInteger>> input)
	{
		HullPainterRobot robot = new HullPainterRobot(input.get(0));
		return Integer.toString(robot.getNumPanelsPainted());
	}

	@Override
	protected String part2(List<List<BigInteger>> input)
	{
		HullPainterRobot robot = new HullPainterRobot(input.get(0));
		Map<Point2D, HullPainterRobot.OutputReaction.PanelColor> panelMap = robot.getPaintedPanels();
		Set<Point2D> paintedPoints = panelMap.keySet();
		double minY = paintedPoints.stream().map(Point2D::getY).min(Double::compare).get();
		double maxY = paintedPoints.stream().map(Point2D::getY).max(Double::compare).get();
		double minX = paintedPoints.stream().map(Point2D::getX).min(Double::compare).get();
		double maxX = paintedPoints.stream().map(Point2D::getX).max(Double::compare).get();
		StringBuilder builder = new StringBuilder();
		for (double y = minY; y <= maxY; y++)
		{
			for (double x = minX; x <= maxX; x++) {
				HullPainterRobot.OutputReaction.PanelColor color = panelMap.get(new Point2D.Double(x, y));
				if (color == HullPainterRobot.OutputReaction.PanelColor.BLACK) {
					builder.append('.');
				}
				else {
					builder.append('#');
				}
			}
			builder.append("\n");
		}
		return builder.toString();
	}
	
	private static class HullPainterRobot {
		private final IntCodeComputer computer;
		private final BlockingQueue<BigInteger> inputQueue;
		private final BlockingQueue<BigInteger> outputQueue;

		public HullPainterRobot(List<BigInteger> tape) 
		{
			this.inputQueue = IntCodeComputer.generateDefaultBlockingQueue();
			this.outputQueue = IntCodeComputer.generateDefaultBlockingQueue();
			this.computer = new IntCodeComputer(tape, inputQueue, outputQueue);
		}
		
		public int getNumPanelsPainted() {
			OutputReaction reactionThread = new OutputReaction(inputQueue, outputQueue, OutputReaction.PanelColor.BLACK);
			ExecutorService executor = Executors.newFixedThreadPool(2);
			executor.submit(reactionThread);
			computer.executeProgram();
			reactionThread.cancelReactor();
			executor.shutdown();
			return reactionThread.getPanelsPainted();
		}
		
		public Map<Point2D, OutputReaction.PanelColor> getPaintedPanels() {
			OutputReaction reactionThread = new OutputReaction(inputQueue, outputQueue, OutputReaction.PanelColor.WHITE);
			ExecutorService executor = Executors.newFixedThreadPool(2);
			executor.submit(reactionThread);
			computer.executeProgram();
			reactionThread.cancelReactor();
			executor.shutdown();
			return reactionThread.getPanelMap();
		}
		
		private static class OutputReaction implements Runnable {
			private enum PanelColor {
				BLACK(BigInteger.ZERO), WHITE(BigInteger.ONE);

				private final BigInteger colorCode;

				private PanelColor(BigInteger colorCode)
				{
					this.colorCode = colorCode;
				}

				public BigInteger getColorCode()
				{
					return colorCode;
				}
				
				public static PanelColor fromCode(BigInteger code) {
					if (code.equals(BigInteger.ONE)) {
						return WHITE;
					}
					else {
						return BLACK;
					}
				}
			}
			private final BlockingQueue<BigInteger> inputQueue;
			private final BlockingQueue<BigInteger> outputQueue;
			private final Map<Point2D, PanelColor> panelMap;
			private final Set<Point2D> panelsVisited;
			private Point2D location;
			private Direction direction;
			private int panelsPainted;
			private boolean cancel;

			public OutputReaction(BlockingQueue<BigInteger> inputQueue, BlockingQueue<BigInteger> outputQueue, PanelColor startingColor)
			{
				this.inputQueue = inputQueue;
				this.inputQueue.add(startingColor.getColorCode());
				this.outputQueue = outputQueue;
				this.panelMap = new HashMap<>();
				this.panelMap.put(new Point2D.Double(0.0, 0.0), startingColor);
				this.panelsVisited = new HashSet<>();
				this.location = new Point2D.Double(0.0, 0.0);
				this.direction = Direction.UP;
				this.cancel = false;
			}
			
			public void cancelReactor() {
				this.cancel = true;
			}

			public int getPanelsPainted()
			{
				return panelsPainted;
			}
			
			public Map<Point2D, PanelColor> getPanelMap() {
				return panelMap;
			}
			
			@Override
			public void run()
			{
				while(!cancel) {
					try
					{
						BigInteger colorToPaint = outputQueue.take();
						BigInteger directionToRotate = outputQueue.take();
						panelMap.put(location, PanelColor.fromCode(colorToPaint));
						if (panelsVisited.add(location)) {
							panelsPainted++;
						}
						if (directionToRotate.equals(BigInteger.ZERO)) {
							direction = direction.rotateLeft90();
							location = direction.travelFrom(location);
						}
						else if (directionToRotate.equals(BigInteger.ONE)) {
							direction = direction.rotateRight90();
							location = direction.travelFrom(location);
						}
						inputQueue.put(panelMap.getOrDefault(location, PanelColor.BLACK).getColorCode());
					}
					catch (InterruptedException ex) {}
				}
			}
		}
	}
}
