package com.jeffrpowell.aoc2018;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

public class Day8 implements Solution<Integer>{

	@Override
	public String part1(List<Integer> input)
	{
		Node rootNode = new Node(input.iterator());
		return Integer.toString(rootNode.getTotal());
	}

	@Override
	public String part2(List<Integer> input)
	{
		Node rootNode = new Node(input.iterator());
		return Integer.toString(rootNode.getValue());
	}

	private static class Node {
		List<Node> children;
		List<Integer> metadata;
		
		public Node(Iterator<Integer> iterator){
			int numChildren = iterator.next();
			int numMetadata = iterator.next();
			this.children = new ArrayList<>();
			this.metadata = new ArrayList<>();
			for (int i = 0; i < numChildren; i++)
			{
				children.add(new Node(iterator));
			}
			IntStream.range(0, numMetadata).map(i -> iterator.next()).forEach(metadata::add);
		}
		
		public int getTotal() {
			return children.stream().map(Node::getTotal).reduce(metadata.stream().reduce(0, Math::addExact), Math::addExact);
		}
		
		public int getValue() {
			if (children.isEmpty()) {
				return metadata.stream().reduce(0, Math::addExact);
			}
			else {
				return metadata.stream().map(this::getValue).reduce(0, Math::addExact);
			}
		}
		
		private int getValue(int i) {
			int index = i - 1;
			if (children.size() < i) {
				return 0;
			}
			else {
				return children.get(index).getValue();
			}
		}
	}
}
