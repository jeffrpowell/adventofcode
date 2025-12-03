package com.jeffrpowell.adventofcode.aoc2020;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;
import com.jeffrpowell.adventofcode.algorithms.Direction;
import com.jeffrpowell.adventofcode.aoc2020.Day20.Tile;

public class Day20Test
{
	private Solution<?> day;
    private Day20.Tile tile;

	@BeforeEach
	public void setUp()
	{
		day = new Day20();
        tile = new Day20.Tile(1);
        tile.addLine("#.##");
        tile.addLine("...#");
        tile.addLine("#..#");
        tile.addLine("..#.");
        tile.calculateHashes();
	}
    
    @Disabled
    public void testTileHashCircle() {
        Tile.Hash leftHash = new Tile.Hash(".#.#", Direction.LEFT);
        Tile.Hash topHash = new Tile.Hash("#.##", Direction.UP);
        Tile.Hash rightHash = new Tile.Hash(".###", Direction.RIGHT);
        Tile.Hash bottomHash = new Tile.Hash("..#.", Direction.DOWN);
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircle), "hashCircle is off");
    }
    
    //<editor-fold defaultstate="collapsed" desc="setOrientation hashCircle">
    
    @Disabled
    public void testSetOrientation_left_to_left() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = ".###".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.LEFT, leftHash);
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircle.stream().map(Tile.Hash::getHash).collect(Collectors.toList())), "left to left hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_top_to_top() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = ".###".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.UP, topHash);
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircle.stream().map(Tile.Hash::getHash).collect(Collectors.toList())), "top to top hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_right_to_right() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = ".###".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.RIGHT, rightHash);
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircle.stream().map(Tile.Hash::getHash).collect(Collectors.toList())), "right to right hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_bottom_to_bottom() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = ".###".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.DOWN, bottomHash);
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircle.stream().map(Tile.Hash::getHash).collect(Collectors.toList())), "bottom to bottom hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_right_to_left() {
        int leftHash = "#.#.".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = ".###".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.LEFT, rightHash);
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircle.stream().map(Tile.Hash::getHash).collect(Collectors.toList())), "right to left hashes are off");
    }
    /*
    @Disabled
    public void testSetOrientation_left_to_top() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = ".###".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.UP, leftHash);
        assertTrue(List.of(bottomHash, leftHash, topHash, rightHash).equals(tile.hashCircle.stream().map(Tile.Hash::getHash).collect(Collectors.toList())), "left to top hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_left_to_right() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = ".###".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.RIGHT, leftHash);
        assertTrue(List.of(rightHash, bottomHash, leftHash, topHash).equals(tile.hashCircle.stream().map(Tile.Hash::getHash).collect(Collectors.toList())), "left to right hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_left_to_bottom() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = ".###".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.DOWN, leftHash);
        assertTrue(List.of(topHash, rightHash, bottomHash, leftHash).equals(tile.hashCircle.stream().map(Tile.Hash::getHash).collect(Collectors.toList())), "left to bottom hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_right_to_top() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = ".###".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.UP, rightHash);
        assertTrue(List.of(topHash, rightHash, bottomHash, leftHash).equals(tile.hashCircle.stream().map(Tile.Hash::getHash).collect(Collectors.toList())), "right to top hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_bottom_to_top() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = ".###".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.UP, bottomHash);
        assertTrue(List.of(rightHash, bottomHash, leftHash, topHash).equals(tile.hashCircle.stream().map(Tile.Hash::getHash).collect(Collectors.toList())), "left to top hashes are off");
    }
    //</editor-fold>
    /*
    //<editor-fold defaultstate="collapsed" desc="setOrientation hashCircleFlipped">
    @Disabled
    public void testSetOrientation_flipped_left_to_left() {
        int leftHash = "###.".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.LEFT, leftHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircleFlipped), "left to left flipped hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_flipped_top_to_top() {
        int leftHash = "###.".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.UP, topHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircleFlipped), "top to top flipped hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_flipped_right_to_right() {
        int leftHash = "###.".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.RIGHT, rightHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircleFlipped), "right to right flipped hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_flipped_bottom_to_bottom() {
        int leftHash = "###.".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.DOWN, bottomHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircleFlipped), "bottom to bottom flipped hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_flipped_left_to_top() {
        int leftHash = "###.".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.UP, leftHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(bottomHash, leftHash, topHash, rightHash).equals(tile.hashCircleFlipped), "left to top flipped hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_flipped_left_to_right() {
        int leftHash = "###.".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.RIGHT, leftHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(rightHash, bottomHash, leftHash, topHash).equals(tile.hashCircleFlipped), "left to right flipped hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_flipped_left_to_bottom() {
        int leftHash = "###.".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.DOWN, leftHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(topHash, rightHash, bottomHash, leftHash).equals(tile.hashCircleFlipped), "left to bottom flipped hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_flipped_right_to_top() {
        int leftHash = "###.".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.UP, rightHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(topHash, rightHash, bottomHash, leftHash).equals(tile.hashCircleFlipped), "right to top flipped hashes are off");
    }
    
    @Disabled
    public void testSetOrientation_flipped_bottom_to_top() {
        int leftHash = "###.".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.UP, bottomHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(rightHash, bottomHash, leftHash, topHash).equals(tile.hashCircleFlipped), "left to top flipped hashes are off");
    }
    //</editor-fold>
    */
    //<editor-fold defaultstate="collapsed" desc="setOrientation final">
    
    @Disabled
    public void testSetOrientation_final_left_to_left() {
        int hash = ".#.#".hashCode();
        tile.setOrientation(Direction.LEFT, hash);
        List<String> expected = List.of(
            "#.##",
            "...#",
            "#..#",
            "..#."
        );
        assertTrue(expected.equals(tile.finalOrientation), "left to left is off");
    }
    
    @Disabled
    public void testSetOrientation_final_top_to_top() {
        int hash = "#.##".hashCode();
        tile.setOrientation(Direction.UP, hash);
        List<String> expected = List.of(
            "#.##",
            "...#",
            "#..#",
            "..#."
        );
        assertTrue(expected.equals(tile.finalOrientation), "top to top is off");
    }
    
    @Disabled
    public void testSetOrientation_final_right_to_right() {
        int hash = "###.".hashCode();
        tile.setOrientation(Direction.RIGHT, hash);
        List<String> expected = List.of(
            "#.##",
            "...#",
            "#..#",
            "..#."
        );
        assertTrue(expected.equals(tile.finalOrientation), "right to right is off");
    }
    
    @Disabled
    public void testSetOrientation_final_bottom_to_bottom() {
        int hash = ".#..".hashCode();
        tile.setOrientation(Direction.DOWN, hash);
        List<String> expected = List.of(
            "#.##",
            "...#",
            "#..#",
            "..#."
        );
        assertTrue(expected.equals(tile.finalOrientation), "bottom to bottom is off");
    }
    
    @Disabled
    public void testSetOrientation_final_right_to_left() {
        int hash = "###.".hashCode();
        tile.setOrientation(Direction.LEFT, hash);
        List<String> expected = List.of(
            ".#..",
            "#..#",
            "#...",
            "##.#"
        );
        assertTrue(expected.equals(tile.finalOrientation), "right to left is off");
    }
    
    @Disabled
    public void testSetOrientation_final_left_to_top() {
        int hash = ".#.#".hashCode();
        tile.setOrientation(Direction.UP, hash);
        List<String> expected = List.of(
            ".#.#",
            "....",
            "#..#",
            ".###"
        );
        assertTrue(expected.equals(tile.finalOrientation), "left to top is off");
    }
    
    @Disabled
    public void testSetOrientation_final_left_to_right() {
        int hash = "#.#.".hashCode();
        tile.setOrientation(Direction.RIGHT, hash);
        List<String> expected = List.of(
            ".#..",
            "#..#",
            "#...",
            "##.#"
        );
        assertTrue(expected.equals(tile.finalOrientation), "left to right is off");
    }
    
    @Disabled
    public void testSetOrientation_final_left_to_bottom() {
        int hash = "#.#.".hashCode();
        tile.setOrientation(Direction.DOWN, hash);
        List<String> expected = List.of(
            "###.",
            "#..#",
            "....",
            "#.#."
        );
        assertTrue(expected.equals(tile.finalOrientation), "left to bottom is off");
    }
    
    @Disabled
    public void testSetOrientation_final_right_to_top() {
        int hash = ".###".hashCode();
        tile.setOrientation(Direction.UP, hash);
        List<String> expected = List.of(
            "###.",
            "#..#",
            "....",
            "#.#."
        );
        assertTrue(expected.equals(tile.finalOrientation), "right to top is off");
    }
    
    @Disabled
    public void testSetOrientation_final_bottom_to_top() {
        int hash = "..#.".hashCode();
        tile.setOrientation(Direction.UP, hash);
        List<String> expected = List.of(
            ".#..",
            "#..#",
            "#...",
            "##.#"
        );
        assertTrue(expected.equals(tile.finalOrientation), "bottom to top is off");
    }
    //</editor-fold>
    /*
    //<editor-fold defaultstate="collapsed" desc="setOrientation final flipped">
    
    @Disabled
    public void testSetOrientation_final_flipped_left_to_left() {
        int hash = ".###".hashCode();
        tile.setOrientation(Direction.LEFT, hash);
        List<String> expected = List.of(
            "##.#",
            "#...",
            "#..#",
            ".#.."
        );
        assertTrue(expected.equals(tile.finalOrientation), "left to left flipped is off");
    }
    
    @Disabled
    public void testSetOrientation_final_flipped_top_to_top() {
        int hash = "##.#".hashCode();
        tile.setOrientation(Direction.UP, hash);
        List<String> expected = List.of(
            "##.#",
            "#...",
            "#..#",
            ".#.."
        );
        assertTrue(expected.equals(tile.finalOrientation), "top to top flipped is off");
    }
    
    @Disabled
    public void testSetOrientation_final_flipped_right_to_right() {
        int hash = "#.#.".hashCode();
        tile.setOrientation(Direction.RIGHT, hash);
        List<String> expected = List.of(
            "##.#",
            "#...",
            "#..#",
            ".#.."
        );
        assertTrue(expected.equals(tile.finalOrientation), "right to right flipped is off");
    }
    
    @Disabled
    public void testSetOrientation_final_flipped_bottom_to_bottom() {
        int hash = "..#.".hashCode();
        tile.setOrientation(Direction.DOWN, hash);
        List<String> expected = List.of(
            "##.#",
            "#...",
            "#..#",
            ".#.."
        );
        assertTrue(expected.equals(tile.finalOrientation), "bottom to bottom flipped is off");
    }
    
    @Disabled
    public void testSetOrientation_final_flipped_right_to_left() {
        int hash = "#.#.".hashCode();
        tile.setOrientation(Direction.LEFT, hash);
        List<String> expected = List.of(
            "..#.",
            "#..#",
            "...#",
            "#.##"
        );
        assertTrue(expected.equals(tile.finalOrientation), "right to left flipped is off");
    }
    
    @Disabled
    public void testSetOrientation_final_flipped_left_to_top() {
        int hash = ".###".hashCode();
        tile.setOrientation(Direction.UP, hash);
        List<String> expected = List.of(
            ".###",
            "#..#",
            "....",
            ".#.#"
        );
        assertTrue(expected.equals(tile.finalOrientation), "left to top flipped is off");
    }
    
    @Disabled
    public void testSetOrientation_final_flipped_left_to_right() {
        int hash = ".###".hashCode();
        tile.setOrientation(Direction.RIGHT, hash);
        List<String> expected = List.of(
            "..#.",
            "#..#",
            "...#",
            "#.##"
        );
        assertTrue(expected.equals(tile.finalOrientation), "left to right flipped is off");
    }
    
    @Disabled
    public void testSetOrientation_final_flipped_left_to_bottom() {
        int hash = ".###".hashCode();
        tile.setOrientation(Direction.DOWN, hash);
        List<String> expected = List.of(
            "#.#.",
            "....",
            "#..#",
            "###."
        );
        assertTrue(expected.equals(tile.finalOrientation), "left to bottom flipped is off");
    }
    
    @Disabled
    public void testSetOrientation_final_flipped_right_to_top() {
        int hash = "#.#.".hashCode();
        tile.setOrientation(Direction.UP, hash);
        List<String> expected = List.of(
            "#.#.",
            "....",
            "#..#",
            "###."
        );
        assertTrue(expected.equals(tile.finalOrientation), "right to top flipped is off");
    }
    
    @Disabled
    public void testSetOrientation_final_flipped_bottom_to_top() {
        int hash = "..#.".hashCode();
        tile.setOrientation(Direction.UP, hash);
        List<String> expected = List.of(
            "..#.",
            "#..#",
            "...#",
            "#.##"
        );
        assertTrue(expected.equals(tile.finalOrientation), "bottom to top flipped is off");
    }
    //</editor-fold>
    */
	public void testPart2()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("273", day.parseAndRunPart2(input));
	}
}
