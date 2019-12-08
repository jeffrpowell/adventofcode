package com.jeffrpowell.adventofcode.aoc2019;

import com.google.common.collect.Lists;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.List;
import java.util.stream.Collectors;

public class Day8 extends Solution2019<List<String>>{
	private static final int WIDTH = 25;
	private static final int HEIGHT = 6;
	
	@Override
	public int getDay()
	{
		return 8;
	}

	@Override
	public InputParser<List<String>> getInputParser()
	{
		return InputParserFactory.getTokenSVParser("");
	}

	@Override
	protected String part1(List<List<String>> input)
	{
		List<SpaceImageLayer> spaceImageLayers = parseLayers(input);
		SpaceImageLayer winner = spaceImageLayers.stream().min((img1, img2) -> Long.compare(img1.numOfDigitsPresent(0), img2.numOfDigitsPresent(0))).get();
		return Long.toString(winner.numOfDigitsPresent(1) * winner.numOfDigitsPresent(2));
	}

	@Override
	protected String part2(List<List<String>> input)
	{
		List<SpaceImageLayer> spaceImageLayers = parseLayers(input);
		StringBuilder image = new StringBuilder();
		for (int y = 0; y < HEIGHT; y++)
		{
			for (int x = 0; x < WIDTH; x++)
			{
				for (SpaceImageLayer layer : spaceImageLayers)
				{
					if (layer.getPixel(x, y) == 0) {
						image.append(0);
						break;
					}
					else if (layer.getPixel(x, y) == 1) {
						image.append(1);
						break;
					}
				}
				image.append(' ');
			}
			image.append("\n");
		}
		return image.toString();
	}
	
	private static List<SpaceImageLayer> parseLayers(List<List<String>> input) {
		List<Integer> parsedInput = input.get(0).stream().map(Integer::parseInt).collect(Collectors.toList());
		List<List<Integer>> rows = Lists.partition(parsedInput, WIDTH);
		return Lists.partition(rows, HEIGHT).stream().map(SpaceImageLayer::new).collect(Collectors.toList());
	}
	
	private static class SpaceImageLayer {
		private final List<List<Integer>> rows;

		public SpaceImageLayer(List<List<Integer>> rows)
		{
			this.rows = rows;
		}
		
		public long numOfDigitsPresent(int targetDigit) {
			return rows.stream().flatMap(List::stream).filter(digit -> digit == targetDigit).count();
		}
		
		public int getPixel(int x, int y) {
			return rows.get(y).get(x);
		}
	}
}
