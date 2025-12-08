package com.jeffrpowell.adventofcode.aoc2025;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.algorithms.Point3DUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;

import javafx.geometry.Point3D;

public class Day8 extends Solution2025<Rule>{
    @Override
    public int getDay() {
        return 8;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", "(\\d+,\\d+,\\d+)");
    }

    record Pairing(Point3D ptI, Point3D ptJ){}

    @Override
    protected String part1(List<Rule> input) {
        List<Point3D> junctionPts = input.stream().map(r -> r.getPoint3D(0)).collect(Collectors.toList());
        Map<Pairing, Double> distances = new HashMap<>();
        for (int i = 0; i < junctionPts.size() - 1; i++) {
            Point3D ptI = junctionPts.get(i);
            for (int j = i+1; j < junctionPts.size(); j++) {
                Point3D ptJ = junctionPts.get(j);
                distances.put(new Pairing(ptI, ptJ), Point3DUtils.getEuclideanDistance(ptI, ptJ));
            }
        }
        DisjointSet<Point3D> forest = new DisjointSet<>(junctionPts);
        distances.entrySet()
            .stream()
            .sorted(Comparator.comparing(Map.Entry::getValue))
            .limit(1000)
            .map(Map.Entry::getKey)
            .forEach(pair -> forest.union(pair.ptI(), pair.ptJ()));
        return Long.toString(forest.part1Answer());
    }

    @Override
    protected String part2(List<Rule> input) {
        List<Point3D> junctionPts = input.stream().map(r -> r.getPoint3D(0)).collect(Collectors.toList());
        Map<Pairing, Double> distances = new HashMap<>();
        for (int i = 0; i < junctionPts.size() - 1; i++) {
            Point3D ptI = junctionPts.get(i);
            for (int j = i+1; j < junctionPts.size(); j++) {
                Point3D ptJ = junctionPts.get(j);
                distances.put(new Pairing(ptI, ptJ), Point3DUtils.getEuclideanDistance(ptI, ptJ));
            }
        }
        DisjointSet<Point3D> forest = new DisjointSet<>(junctionPts);
        distances.entrySet()
            .stream()
            .sorted(Comparator.comparing(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .forEach(pair -> forest.union(pair.ptI(), pair.ptJ()));
        return Long.toString(forest.part2Answer());
    }

    private static class DisjointSet<T> {
        private class Node {
            private Node parent;
            private int size;
            public Node() {
                this.parent = this;
                this.size = 1;
            }
            public Node getParent() {
                return parent;
            }
            public int getSize() {
                return size;
            }
            public void setParent(Node parent) {
                this.parent = parent;
            }
            public void setSize(int size) {
                this.size = size;
            }
        }
        private Map<T, Node> nodes = new HashMap<>();
        private Pairing lastJoinedPair = null;

        public DisjointSet(Collection<T> disjointTrees) {
            this.nodes = disjointTrees.stream()
                .collect(Collectors.toMap(
                    Function.identity(),
                    tree -> new Node()
                ));
        }

        private Node find(T tree) {
            Node treeNode = nodes.get(tree);
            return find(treeNode);
        }

        private Node find(Node treeNode) {
            if (treeNode.getParent() != treeNode) {
                treeNode.setParent(find(treeNode.getParent()));
                return treeNode.getParent();
            }
            else {
                return treeNode;
            }
        }

        public void union(T treeOne, T treeTwo) {
            Node tallerTreeNode = find(treeOne);
            Node shorterTreeNode = find(treeTwo);

            if (tallerTreeNode == shorterTreeNode) {
                return;
            }

            if (tallerTreeNode.getSize() < shorterTreeNode.getSize()) {
                Node temp = tallerTreeNode;
                tallerTreeNode = shorterTreeNode;
                shorterTreeNode = temp;
            }

            shorterTreeNode.setParent(tallerTreeNode);
            tallerTreeNode.setSize(tallerTreeNode.getSize() + shorterTreeNode.getSize());
            shorterTreeNode.setSize(0);
            lastJoinedPair = new Pairing((Point3D)treeOne, (Point3D)treeTwo);
        }

        public long part1Answer() {
            return nodes.values().stream()
                .map(Node::getSize)
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToLong(Integer::longValue)
                .reduce(Math::multiplyExact).orElseThrow();
        }

        public long part2Answer() {
            return Double.valueOf(lastJoinedPair.ptI().getX() * lastJoinedPair.ptJ().getX()).longValue();
        }
    }
}
