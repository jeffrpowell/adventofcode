package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.Solution;
import com.jeffrpowell.adventofcode.TestDataLoader;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Day20Test
{
	private Solution day;
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
    
    @Test
    public void testTileHashCircle() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = "###.".hashCode();
        int bottomHash = ".#..".hashCode();
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircle), "hashCircle is off");
    }
    
    @Test
    public void testTileHashCircleFlipped() {
        int leftHash = ".###".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = "..#.".hashCode();
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircleFlipped), "hashCircleFlipped is off");
    }
    
    //<editor-fold defaultstate="collapsed" desc="setOrientation hashCircle">
    
    @Test
    public void testSetOrientation_left_to_left() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = "###.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.LEFT, leftHash);
        assertFalse(tile.usingFlippedHashCircle, "shouldn't be using hashCircleFlipped");
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircle), "left to left hashes are off");
    }
    
    @Test
    public void testSetOrientation_top_to_top() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = "###.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.UP, topHash);
        assertFalse(tile.usingFlippedHashCircle, "shouldn't be using hashCircleFlipped");
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircle), "top to top hashes are off");
    }
    
    @Test
    public void testSetOrientation_right_to_right() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = "###.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.RIGHT, rightHash);
        assertFalse(tile.usingFlippedHashCircle, "shouldn't be using hashCircleFlipped");
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircle), "right to right hashes are off");
    }
    
    @Test
    public void testSetOrientation_bottom_to_bottom() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = "###.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.DOWN, bottomHash);
        assertFalse(tile.usingFlippedHashCircle, "shouldn't be using hashCircleFlipped");
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircle), "bottom to bottom hashes are off");
    }
    
    @Test
    public void testSetOrientation_right_to_left() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = "###.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.LEFT, rightHash);
        assertFalse(tile.usingFlippedHashCircle, "shouldn't be using hashCircleFlipped");
        assertTrue(List.of(rightHash, bottomHash, leftHash, topHash).equals(tile.hashCircle), "right to left hashes are off");
    }
    
    @Test
    public void testSetOrientation_left_to_top() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = "###.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.UP, leftHash);
        assertFalse(tile.usingFlippedHashCircle, "shouldn't be using hashCircleFlipped");
        assertTrue(List.of(bottomHash, leftHash, topHash, rightHash).equals(tile.hashCircle), "left to top hashes are off");
    }
    
    @Test
    public void testSetOrientation_left_to_right() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = "###.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.RIGHT, leftHash);
        assertFalse(tile.usingFlippedHashCircle, "shouldn't be using hashCircleFlipped");
        assertTrue(List.of(rightHash, bottomHash, leftHash, topHash).equals(tile.hashCircle), "left to right hashes are off");
    }
    
    @Test
    public void testSetOrientation_left_to_bottom() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = "###.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.DOWN, leftHash);
        assertFalse(tile.usingFlippedHashCircle, "shouldn't be using hashCircleFlipped");
        assertTrue(List.of(topHash, rightHash, bottomHash, leftHash).equals(tile.hashCircle), "left to bottom hashes are off");
    }
    
    @Test
    public void testSetOrientation_right_to_top() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = "###.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.UP, rightHash);
        assertFalse(tile.usingFlippedHashCircle, "shouldn't be using hashCircleFlipped");
        assertTrue(List.of(topHash, rightHash, bottomHash, leftHash).equals(tile.hashCircle), "right to top hashes are off");
    }
    
    @Test
    public void testSetOrientation_bottom_to_top() {
        int leftHash = ".#.#".hashCode();
        int topHash = "#.##".hashCode();
        int rightHash = "###.".hashCode();
        int bottomHash = ".#..".hashCode();
        tile.setOrientation(Direction.UP, bottomHash);
        assertFalse(tile.usingFlippedHashCircle, "shouldn't be using hashCircleFlipped");
        assertTrue(List.of(rightHash, bottomHash, leftHash, topHash).equals(tile.hashCircle), "left to top hashes are off");
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="setOrientation hashCircleFlipped">
    @Test
    public void testSetOrientation_flipped_left_to_left() {
        int leftHash = ".###".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.LEFT, leftHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircleFlipped), "left to left flipped hashes are off");
    }
    
    @Test
    public void testSetOrientation_flipped_top_to_top() {
        int leftHash = ".###".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.UP, topHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircleFlipped), "top to top flipped hashes are off");
    }
    
    @Test
    public void testSetOrientation_flipped_right_to_right() {
        int leftHash = ".###".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.RIGHT, rightHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircleFlipped), "right to right flipped hashes are off");
    }
    
    @Test
    public void testSetOrientation_flipped_bottom_to_bottom() {
        int leftHash = ".###".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.DOWN, bottomHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(leftHash, topHash, rightHash, bottomHash).equals(tile.hashCircleFlipped), "bottom to bottom flipped hashes are off");
    }
    
    @Test
    public void testSetOrientation_flipped_left_to_top() {
        int leftHash = ".###".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.UP, leftHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(bottomHash, leftHash, topHash, rightHash).equals(tile.hashCircleFlipped), "left to top flipped hashes are off");
    }
    
    @Test
    public void testSetOrientation_flipped_left_to_right() {
        int leftHash = ".###".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.RIGHT, leftHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(rightHash, bottomHash, leftHash, topHash).equals(tile.hashCircleFlipped), "left to right flipped hashes are off");
    }
    
    @Test
    public void testSetOrientation_flipped_left_to_bottom() {
        int leftHash = ".###".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.DOWN, leftHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(topHash, rightHash, bottomHash, leftHash).equals(tile.hashCircleFlipped), "left to bottom flipped hashes are off");
    }
    
    @Test
    public void testSetOrientation_flipped_right_to_top() {
        int leftHash = ".###".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.UP, rightHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(topHash, rightHash, bottomHash, leftHash).equals(tile.hashCircleFlipped), "right to top flipped hashes are off");
    }
    
    @Test
    public void testSetOrientation_flipped_bottom_to_top() {
        int leftHash = ".###".hashCode();
        int topHash = "##.#".hashCode();
        int rightHash = "#.#.".hashCode();
        int bottomHash = "..#.".hashCode();
        tile.setOrientation(Direction.UP, bottomHash);
        assertTrue(tile.usingFlippedHashCircle, "shouldn't be using hashCircle");
        assertTrue(List.of(rightHash, bottomHash, leftHash, topHash).equals(tile.hashCircleFlipped), "left to top flipped hashes are off");
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="setOrientation final">
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
    @Test
    public void testSetOrientation_final_left_to_right() {
        int hash = ".#.#".hashCode();
        tile.setOrientation(Direction.RIGHT, hash);
        List<String> expected = List.of(
            ".#..",
            "#..#",
            "#...",
            "##.#"
        );
        assertTrue(expected.equals(tile.finalOrientation), "left to right is off");
    }
    
    @Test
    public void testSetOrientation_final_left_to_bottom() {
        int hash = ".#.#".hashCode();
        tile.setOrientation(Direction.DOWN, hash);
        List<String> expected = List.of(
            "###.",
            "#..#",
            "....",
            "#.#."
        );
        assertTrue(expected.equals(tile.finalOrientation), "left to bottom is off");
    }
    
    @Test
    public void testSetOrientation_final_right_to_top() {
        int hash = "###.".hashCode();
        tile.setOrientation(Direction.UP, hash);
        List<String> expected = List.of(
            "###.",
            "#..#",
            "....",
            "#.#."
        );
        assertTrue(expected.equals(tile.finalOrientation), "right to top is off");
    }
    
    @Test
    public void testSetOrientation_final_bottom_to_top() {
        int hash = ".#..".hashCode();
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

    //<editor-fold defaultstate="collapsed" desc="setOrientation final flipped">
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
    @Test
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
    
	public void testPart2()
	{
		List<String> input = TestDataLoader.getTestData(day, 1);
		assertEquals("273", day.parseAndRunPart2(input));
	}

}
