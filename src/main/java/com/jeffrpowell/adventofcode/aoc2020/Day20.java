package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
                  # 
#    ##    ##    ###
 #  #  #  #  #  #   
*/
public class Day20 extends Solution2020<String>{
    static final Pattern TILE_ID_PATTERN = Pattern.compile("Tile (\\d+):");
    
    @Override
    public int getDay() {
        return 20;
    }

    @Override
    public InputParser<String> getInputParser() {
        return InputParserFactory.getStringParser();
    }

    @Override
    protected String part1(List<String> input) {
        Map<Long, Tile> tiles = parseTiles(input);
        Map<Integer, List<Tile>> tilesByHash = buildTilesByHash(tiles.values());
        Map<Tile.Location, List<Long>> classifiedTiles = classifyTiles(tiles.values(), tilesByHash);
        return Long.toString(classifiedTiles.get(Tile.Location.CORNER).stream()
            .reduce(1L, Math::multiplyExact));
    }

    @Override
    protected String part2(List<String> input) {
        Map<Long, Tile> tiles = parseTiles(input);
        Map<Integer, List<Tile>> tilesByHash = buildTilesByHash(tiles.values());
        Map<Tile.Location, List<Long>> classifiedTiles = classifyTiles(tiles.values(), tilesByHash);
        Map<Point2D, Long> placedTiles = new HashMap<>();
        Long firstTile = classifiedTiles.get(Tile.Location.CORNER).get(0);
        placedTiles.put(new Point2D.Double(0, 0), firstTile);
        for (Integer hash : tiles.get(firstTile).hashCircle) {
            List<Tile> matchingTiles = tilesByHash.get(hash);
            if (matchingTiles.size() > 1) {
                Tile nextTile = matchingTiles.stream().filter(t -> t.id != firstTile).findAny().get();
                placedTiles.put(new Point2D.Double(1, 0), nextTile.id);
            }
        }
        return "";
    }
    
    private Map<Long, Tile> parseTiles(List<String> input) {
        Map<Long, Tile> tiles = new HashMap<>();
        Tile tile = new Tile(-1);
        for (String string : input) {
            if (string.endsWith(":")) {
                Matcher m = TILE_ID_PATTERN.matcher(string);
                m.matches();
                tile = new Tile(Integer.parseInt(m.group(1)));
                tiles.put(tile.getId(), tile);
            }
            else if (!string.isBlank()) {
                tile.addLine(string);
            }
        }
        return tiles;
    }
    
    private Map<Integer, List<Tile>> buildTilesByHash(Collection<Tile> tiles) {
        tiles.stream().forEach(Tile::calculateHashes);
        Map<Integer, List<Tile>> tilesByHash = new HashMap<>();
        for (Tile t : tiles) {
            for (Integer h : t.hashCircle) {
                tilesByHash.putIfAbsent(h, new ArrayList<>());
                tilesByHash.get(h).add(t);
            }
            for (Integer h : t.hashCircleFlipped) {
                tilesByHash.putIfAbsent(h, new ArrayList<>());
                tilesByHash.get(h).add(t);
            }
        }
        return tilesByHash;
    }
    
    private Map<Tile.Location, List<Long>> classifyTiles(Collection<Tile> tiles, Map<Integer, List<Tile>> tilesByHash) {
        Map<Long, List<Tile>> tilesWithUniqueEdges = tilesByHash.values().stream()
            .filter(tileList -> tileList.size() < 2)
            .flatMap(List::stream)
            .collect(Collectors.groupingBy(Tile::getId));
        tilesWithUniqueEdges.entrySet().stream()
            .filter(entry -> entry.getValue().size() < 3)
            .forEach(entry -> entry.getValue().get(0).setLocation(Tile.Location.EDGE));
        tilesWithUniqueEdges.entrySet().stream()
            .filter(entry -> entry.getValue().size() > 2)
            .forEach(entry -> entry.getValue().get(0).setLocation(Tile.Location.CORNER));
        return tiles.stream().collect(Collectors.groupingBy(Tile::getLocation, Collectors.mapping(Tile::getId, Collectors.toList())));
    }
    
    private static class Tile {
        enum Location {CORNER, EDGE, MIDDLE}
        List<String> raw;
        List<String> rawRotatedCW;
        long id;
        List<Integer> hashCircle;
        List<Integer> hashCircleFlipped;
        boolean usingFlippedHashCircle;
        Location location;
        
        public Tile(long id) {
            this.id = id;
            this.raw = new ArrayList<>();
            this.location = Location.MIDDLE;
            this.hashCircle = new ArrayList<>();
            this.hashCircleFlipped = new ArrayList<>();
            this.usingFlippedHashCircle = false;
        }
        
        public void addLine(String line) {
            raw.add(line);
        }
        
        public void setLocation(Location l) {
            location = l;
        }

        public long getId() {
            return id;
        }

        public Location getLocation() {
            return location;
        }
        
        public void setOrientation(Direction d, int hash) {
            List<Integer> hashes;
            if (hashCircleFlipped.contains(hash)) {
                usingFlippedHashCircle = true;
                hashes = hashCircleFlipped;
            }
            else {
                usingFlippedHashCircle = false;
                hashes = hashCircle;
            }
            int hashPosition = hashes.indexOf(hash);
            switch (d) {
                case LEFT -> Collections.rotate(hashes, -hashPosition);
                case UP -> Collections.rotate(hashes, -hashPosition + 1);
                case RIGHT -> Collections.rotate(hashes, -hashPosition + 2);
                case DOWN -> Collections.rotate(hashes, -hashPosition + 3);
            }
        }
        
        public int getNextHash(Direction d) {
            List<Integer> hashes;
            if (usingFlippedHashCircle) {
                hashes = hashCircleFlipped;
            }
            else {
                hashes = hashCircle;
            }
            return switch (d) {
                case LEFT -> hashes.get(0);
                case UP -> hashes.get(1);
                case RIGHT -> hashes.get(2);
                case DOWN -> hashes.get(3);
                default -> hashes.get(0);
            };
        }
        
        public void calculateHashes() {
            rawRotatedCW = rotateTileCW();
            hashCircle.add(raw.get(0).hashCode());
            hashCircle.add(reverseString(rawRotatedCW.get(rawRotatedCW.size() - 1)).hashCode());
            hashCircle.add(reverseString(raw.get(raw.size() - 1)).hashCode());
            hashCircle.add(rawRotatedCW.get(0).hashCode());
            hashCircleFlipped.add(reverseString(raw.get(0)).hashCode());
            hashCircleFlipped.add(reverseString(rawRotatedCW.get(0)).hashCode());
            hashCircleFlipped.add(raw.get(raw.size() - 1).hashCode());
            hashCircleFlipped.add(rawRotatedCW.get(rawRotatedCW.size() - 1).hashCode());
        }
        
        private static String reverseString(String s) {
            return new StringBuilder(s).reverse().toString();
        }
        
        private List<String> rotateTileCW() {
            char[][] mat = new char[raw.size()][];
            for (int i = 0; i < raw.size(); i++) {
                mat[i] = raw.get(i).toCharArray();
            }
            //https://stackoverflow.com/a/2800033
            final int M = mat.length;
            final int N = mat[0].length;
            char[][] ret = new char[N][M];
            for (int r = 0; r < M; r++) {
                for (int c = 0; c < N; c++) {
                    ret[c][M-1-r] = mat[r][c];
                }
            }
            List<String> rotatedTile = new ArrayList<>();
            for (int i = 0; i < ret.length; i++) {
                rotatedTile.add(new String(ret[i]));
            }
            return rotatedTile;
        }
        
        @Override
        public String toString() {
            return Long.toString(id);
        }
    }
    
    private static class SearchAttempt {
        Point2D pt;
        Long tile;

        public SearchAttempt(Point2D pt, Long tile) {
            this.pt = pt;
            this.tile = tile;
        }
        
    }
    
    private static class PuzzleMat {
        Map<Point2D, Long> placedTiles;
    }
    
    private static class Generator {
        Map<Long, Tile> tiles;
        Map<Tile.Location, List<Long>> classifiedTiles;
    }
}
