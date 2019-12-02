package com.jeffrpowell.adventofcode.aoc2018.solutions;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.jeffrpowell.adventofcode.InputParser;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class Day6 extends Solution2018<Point2D>
{
    private final PriorityQueue<Seek> queue;
    private final SetMultimap<Point2D, Seek> ptToSeekIndex;
    private final SetMultimap<Point2D, Point2D> closestRoot;
    private final Set<Point2D> rootsWithInfiniteArea;
    private Point2D topLeft;
    private Point2D bottomRight;

    public Day6() {
        this.queue = new PriorityQueue();
        this.ptToSeekIndex = MultimapBuilder.hashKeys().hashSetValues().build();
        this.closestRoot = MultimapBuilder.hashKeys().hashSetValues().build();
        this.rootsWithInfiniteArea = new HashSet<>();
    }
        
	@Override
	public int getDay()
	{
		return 6;
	}
	
	@Override
	public InputParser<Point2D> getInputParser()
	{
		return null; //Added this getInputParser architecture way after the fact, and haven't needed to write a Point2D parser yet
	}
	
    @Override
    public String part1(List<Point2D> input) {
        setGridBounds(input);
        for (Point2D pt : input) {
            Seek rootSeek = new Seek(0, pt, null);
            if (ptToSeekIndex.containsKey(pt) || closestRoot.containsKey(pt)) {
                //Two roots are next to each other; the other one enqueued a d-1 seek before this root was recorded
                ptToSeekIndex.removeAll(pt);
                closestRoot.removeAll(pt);
            }
            ptToSeekIndex.put(pt, rootSeek);
            closestRoot.put(pt, pt);
            generateNeighborSeeksAndEnqueue(rootSeek);
        }
        while(!queue.isEmpty()) {
            Seek s = queue.poll();
            if (ptToSeekIndex.containsKey(s.getPt())) {
                Set<Seek> comparisonPts = ptToSeekIndex.get(s.getPt());
                int minDistance = comparisonPts.stream().map(Seek::getDistanceTravelled).reduce(Integer.MAX_VALUE, BinaryOperator.minBy(Integer::compare));
                if (s.getDistanceTravelled() == minDistance) {
                    recordNewClosestRoot(s);
                }
                //ELSE: should be s.getDistanceTravelled() > minDistance ;; no-op because gen'd neighbors will also have too great a distance, and BFS says we've already visited them
                //We should never see s.getDistanceTravelled() < minDistance, because BFS
            }
            else {
                recordNewClosestRoot(s);
                generateNeighborSeeksAndEnqueue(s);
            }
        }
        findInfiniteRoots();
        Map<Point2D, Point2D> dedupedClosestRoot = closestRoot.asMap().entrySet().stream()
            .filter(entry -> entry.getValue().size() == 1)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().iterator().next()
            ));
        SetMultimap<Point2D, Point2D> rootToClosestPoints = MultimapBuilder.hashKeys().hashSetValues().build();
        for (Map.Entry<Point2D, Point2D> ptToRoot : dedupedClosestRoot.entrySet()) {
            rootToClosestPoints.put(ptToRoot.getValue(), ptToRoot.getKey());
        }
        return Integer.toString(rootToClosestPoints.asMap().entrySet().stream()
            .filter(entry -> !rootsWithInfiniteArea.contains(entry.getKey()))
            .reduce(0, (maxSoFar, entry) -> Math.max(maxSoFar, entry.getValue().size()), BinaryOperator.maxBy(Integer::compare)));
    }

    private void setGridBounds(List<Point2D> input) {
        int maxX = input.stream().map(Point2D::getX).reduce(BinaryOperator.maxBy(Double::compare)).get().intValue();
        int minX = input.stream().map(Point2D::getX).reduce(BinaryOperator.minBy(Double::compare)).get().intValue();
        int maxY = input.stream().map(Point2D::getY).reduce(BinaryOperator.maxBy(Double::compare)).get().intValue();
        int minY = input.stream().map(Point2D::getY).reduce(BinaryOperator.minBy(Double::compare)).get().intValue();
        topLeft = new Point2D.Double(minX, minY);
        bottomRight = new Point2D.Double(maxX, maxY);
    }
    
    private void recordNewClosestRoot(Seek s) {
        Set<Point2D> closestRootsSoFar = closestRoot.get(s.getPt());
        if (!setContainsAny(closestRootsSoFar, closestRoot.get(s.getParent().getPt()))) {
            closestRoot.putAll(s.getPt(), closestRoot.get(s.getParent().getPt()));
            ptToSeekIndex.put(s.getPt(), s);
        }
    }
    
    private boolean setContainsAny(Set<Point2D> haystack, Set<Point2D> needles) {
        return needles.stream().anyMatch(haystack::contains);
    }
    
    private void generateNeighborSeeksAndEnqueue(Seek seek) {
        Point2D pt = seek.getPt();
        List<Seek> neighbors = new ArrayList<>();
        if (pt.getX() > topLeft.getX()) {
            neighbors.add(new Seek(seek.getDistanceTravelled() + 1, new Point2D.Double(pt.getX() - 1, pt.getY()), seek));
        }
        if (pt.getX() < bottomRight.getX()) {
            neighbors.add(new Seek(seek.getDistanceTravelled() + 1, new Point2D.Double(pt.getX() + 1, pt.getY()), seek));
        }
        if (pt.getY() > topLeft.getY()) {
            neighbors.add(new Seek(seek.getDistanceTravelled() + 1, new Point2D.Double(pt.getX(), pt.getY() - 1), seek));
        }
        if (pt.getY() < bottomRight.getY()) {
            neighbors.add(new Seek(seek.getDistanceTravelled() + 1, new Point2D.Double(pt.getX(), pt.getY() + 1), seek));
        }
        if (seek.getParent() != null) {
            neighbors.stream().filter(s -> !s.getPt().equals(seek.getParent().getPt())).forEach(queue::offer);
        }
        else {
            neighbors.stream().forEach(queue::offer);
        }
    }
    
    private void findInfiniteRoots() {
        Set<Point2D> pointsToCheck = new HashSet<>();
        for (double x = topLeft.getX(); x <= bottomRight.getX(); x++) {
            pointsToCheck.add(new Point2D.Double(x, topLeft.getY()));
            pointsToCheck.add(new Point2D.Double(x, bottomRight.getY()));
        }
        for (double y = topLeft.getY(); y < bottomRight.getY(); y++) {
            pointsToCheck.add(new Point2D.Double(topLeft.getX(), y));
            pointsToCheck.add(new Point2D.Double(bottomRight.getX(), y));
        }
        for (Point2D pt : pointsToCheck) {
            Set<Point2D> closestRootsForPt = closestRoot.get(pt);
            if (closestRootsForPt.size() == 1) {
                rootsWithInfiniteArea.add(closestRootsForPt.iterator().next());
            }
        }
    }

    private static class Seek implements Comparable<Seek>{
        private final int distanceTravelled;
        private final Point2D pt;
        private final Seek parent;

        public Seek(int distanceTravelled, Point2D pt, Seek parent) {
            this.distanceTravelled = distanceTravelled;
            this.pt = pt;
            this.parent = parent;
        }

        public int getDistanceTravelled() {
            return distanceTravelled;
        }

        public Point2D getPt() {
            return pt;
        }

        public Seek getParent() {
            return parent;
        }

        @Override
        public int compareTo(Seek o) {
            return Integer.compare(distanceTravelled, o.getDistanceTravelled());
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + this.distanceTravelled;
            hash = 37 * hash + Objects.hashCode(this.pt);
            hash = 37 * hash + Objects.hashCode(this.parent);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Seek other = (Seek) obj;
            if (this.distanceTravelled != other.distanceTravelled)
                return false;
            if (!Objects.equals(this.pt, other.pt))
                return false;
            return Objects.equals(this.parent, other.parent);
        }
        
        @Override
        public String toString() {
            return distanceTravelled + " - " + printParent() + " -> " + printPt();
        }
        
        private String printParent() {
            return parent == null ? "" : parent.getPt().getX() + "," + parent.getPt().getY();
        }
        
        private String printPt() {
            return getPt().getX() + "," + getPt().getY();
        }
    }
	
    @Override
    public String part2(List<Point2D> input) {
        setGridBounds(input);
		int ptsInRegion = 0;
		for (double x = topLeft.getX(); x <= bottomRight.getX(); x++)
		{
			for (double y = topLeft.getY(); y <= bottomRight.getY(); y++) {
				if (distanceToRoots(input, x, y) < 10000) {
					ptsInRegion++;
				}
			}
		}
		return Integer.toString(ptsInRegion);
    }
	
	private int distanceToRoots(List<Point2D> roots, double x, double y) {
		return roots.stream().reduce(0D, (total, root) -> total + Math.abs(x - root.getX()) + Math.abs(y - root.getY()), (a, b) -> a + b).intValue();
	}
}
