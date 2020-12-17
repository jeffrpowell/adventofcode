package com.jeffrpowell.adventofcode;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PointND {
   private final double[] points;
   
   public PointND(double... pts) {
       this.points = pts;
   }
   
   public double getDimensionN(int n) {
       return points[n];
   }
   
   public Set<PointND> getAdjacentPts() {
		return getAdjacentPts(false);
	}
	
	public Set<PointND> getAdjacentPts(boolean includeDiagonalNeighbors) {
        Set<Integer> comboPieces = new HashSet<>();
        for (int i = 0; i < points.length; i++) {
            int modifier = 10 * i;
            //shifted up by one so we can use % 10 later
            comboPieces.add(0 + modifier);
            comboPieces.add(1 + modifier);
            comboPieces.add(2 + modifier);
        }
        Set<Set<Integer>> combos = Sets.combinations(comboPieces, points.length);
		if (includeDiagonalNeighbors) {
			return combos.stream()
                .map(this::translateComboPieceToPoint)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
		}
		else {
			throw new UnsupportedOperationException();
		}
	}
    
    private Optional<PointND> translateComboPieceToPoint(Set<Integer> combo) {
        List<Integer> orderedCombo = new ArrayList<>(combo);
        orderedCombo.sort(Integer::compare);
        double[] dimensionVector = new double[orderedCombo.size()];
        for (int i = 0; i < orderedCombo.size(); i++) {
            int modifier = 10 * i;
            int modifiedComboDimension = orderedCombo.get(i) - modifier;
            if (modifiedComboDimension < 0 || modifiedComboDimension > 2) {
                return Optional.empty();
            }
            dimensionVector[i] = modifiedComboDimension - 1;
        }
        PointND neighborPt = applyVector(dimensionVector);
        if (equals(neighborPt)) {
            return Optional.empty();
        }
        return Optional.of(neighborPt);
    }
    
    private PointND applyVector(double[] vector) {
        double[] dimensions = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            dimensions[i] = points[i] + vector[i];
        }
        return new PointND(dimensions);
    }
    
    private boolean doubleArrayEquals(double[] a, double[] a2) {
        if (a==a2)
            return true;
        if (a==null || a2==null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;
        
        for (int i = 0; i < a.length; i++) {
            if (!Double.valueOf(a[i]).equals(a2[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(points);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Arrays.hashCode(this.points);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PointND other = (PointND) obj;
        return doubleArrayEquals(this.points, other.points);
    }
    
    
}
