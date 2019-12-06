package com.jeffrpowell.adventofcode.aoc2019;

import com.google.common.collect.Sets;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Day6 extends Solution2019<List<String>>
{

	@Override
	public int getDay()
	{
		return 6;
	}

	@Override
	public InputParser<List<String>> getInputParser()
	{
		return InputParserFactory.getTokenSVParser("\\)");
	}

	@Override
	protected String part1(List<List<String>> input)
	{
		OrbitGraph graph = new OrbitGraph(input);
		return Integer.toString(graph.countAllConnections());
	}

	@Override
	protected String part2(List<List<String>> input)
	{
		OrbitGraph graph = new OrbitGraph(input);
		return Integer.toString(graph.getDistanceBetweenYouAndSanta());
	}

	private static class OrbitGraph
	{

		private final Map<String, OrbitNode> rootContenders;
		private final Map<String, OrbitNode> leafContenders;
		private final Map<String, OrbitNode> orbitNodeIndex;

		public OrbitGraph(List<List<String>> input)
		{
			rootContenders = new HashMap<>();
			leafContenders = new HashMap<>();
			orbitNodeIndex = new HashMap<>();
			input.stream().forEach(connection -> parseOrbitNodeConnection(connection.get(0), connection.get(1)));
		}

		private void parseOrbitNodeConnection(String orbitee, String orbiter)
		{
			boolean orbiteeExists = orbitNodeIndex.containsKey(orbitee);
			boolean orbiterExists = orbitNodeIndex.containsKey(orbiter);
			rootContenders.remove(orbiter);
			leafContenders.remove(orbitee);
			//OrbitNode.setParent establishes the child relationship automatically
			if (orbiteeExists && orbiterExists) {
				orbitNodeIndex.get(orbiter).setParent(orbitNodeIndex.get(orbitee));
			}
			else if (orbiteeExists) {
				OrbitNode orbiterNode = new OrbitNode(orbiter);
				orbiterNode.setParent(orbitNodeIndex.get(orbitee));
				orbitNodeIndex.put(orbiter, orbiterNode);
				leafContenders.put(orbiter, orbiterNode);
			}
			else if (orbiterExists) {
				OrbitNode orbiteeNode = new OrbitNode(orbitee);
				orbitNodeIndex.get(orbiter).setParent(orbiteeNode);
				orbitNodeIndex.put(orbitee, orbiteeNode);
				rootContenders.put(orbitee, orbiteeNode);
			}
			else {
				OrbitNode orbiteeNode = new OrbitNode(orbitee);
				OrbitNode orbiterNode = new OrbitNode(orbiter);
				orbiterNode.setParent(orbiteeNode);
				orbitNodeIndex.put(orbitee, orbiteeNode);
				orbitNodeIndex.put(orbiter, orbiterNode);
				rootContenders.put(orbitee, orbiteeNode);
				leafContenders.put(orbiter, orbiterNode);
			}
		}
		
		public int countAllConnections() {
			return orbitNodeIndex.values().stream().map(OrbitNode::countConnectionsToRoot).reduce(0, Integer::sum, Integer::sum);
		}
		
		private OrbitNode getCommonAncestorBetweenYouAndSanta() {
			OrbitNode you = orbitNodeIndex.get("YOU");
			OrbitNode santa = orbitNodeIndex.get("SAN");
			HashSet<OrbitNode> yourAncestors = new HashSet<>();
			you.collectAncestors(yourAncestors);
			HashSet<OrbitNode> santasAncestors = new HashSet<>();
			santa.collectAncestors(santasAncestors);
			return Sets.intersection(yourAncestors, santasAncestors).stream()
				.max((node1, node2) -> Integer.compare(node1.countConnectionsToRoot(), node2.countConnectionsToRoot())).get();
		}
		
		public int getDistanceBetweenYouAndSanta() {
			OrbitNode commonAncestor = getCommonAncestorBetweenYouAndSanta();
			OrbitNode you = orbitNodeIndex.get("YOU");
			OrbitNode santa = orbitNodeIndex.get("SAN");
			int yourDistanceToAncestor = you.countConnectionsToTarget(you.parent, commonAncestor);
			int santasDistanceToAncestor = santa.countConnectionsToTarget(santa.parent, commonAncestor);
			return yourDistanceToAncestor + santasDistanceToAncestor;
		}

		@Override
		public String toString() {
			return rootContenders.values().stream().map(OrbitNode::toString).collect(Collectors.joining("\n"));
		}
	}

	private static class OrbitNode
	{

		private final String name;
		private final List<OrbitNode> children;
		private OrbitNode parent;

		public OrbitNode(String name)
		{
			this.name = name;
			this.children = new ArrayList<>();
			this.parent = null;
		}

		public void setParent(OrbitNode parent)
		{
			this.parent = parent;
			parent.addChild(this);
		}

		private void addChild(OrbitNode child)
		{
			children.add(child);
		}
		
		public void collectAncestors(Set<OrbitNode> ancestors) {
			if (parent != null) {
				ancestors.add(parent);
				parent.collectAncestors(ancestors);
			}
		}
		
		public int countConnectionsToRoot() {
			return countConnectionsRecursive(parent);
		}
		
		private int countConnectionsRecursive(OrbitNode nextNodeUp) {
			if (nextNodeUp == null) {
				return 0;
			}
			if (nextNodeUp.parent == null) {
				return 1;
			}
			else {
				return countConnectionsRecursive(nextNodeUp.parent) + 1;
			}
		}
		
		public int countConnectionsToTarget(OrbitNode nextNodeUp, OrbitNode target) {
			if (nextNodeUp == null || nextNodeUp.equals(target)) {
				return 0;
			}
			if (nextNodeUp.parent == null || nextNodeUp.parent.equals(target)) {
				return 1;
			}
			else {
				return countConnectionsToTarget(nextNodeUp.parent, target) + 1;
			}
		}
		
		public String toString(String tabLevel) {
			StringBuilder builder = new StringBuilder(tabLevel).append(name).append("\n");
			String nextTabLevel = tabLevel + "    ";
			for (OrbitNode child : children)
			{
				builder.append(child.toString(nextTabLevel));
			}
			return builder.toString();
		}
		
		@Override
		public String toString() {
			return toString("");
		}

		@Override
		public int hashCode()
		{
			int hash = 7;
			hash = 67 * hash + Objects.hashCode(this.name);
			return hash;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (obj == null)
			{
				return false;
			}
			if (getClass() != obj.getClass())
			{
				return false;
			}
			final OrbitNode other = (OrbitNode) obj;
			return Objects.equals(this.name, other.name);
		}
		
	}

}
