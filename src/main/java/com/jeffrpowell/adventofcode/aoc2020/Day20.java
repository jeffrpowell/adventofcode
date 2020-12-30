package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.Direction;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Map<Point2D, Tile> placedTiles = new HashMap<>();
        
        //Set the top-left corner piece
        Long firstTile = classifiedTiles.get(Tile.Location.CORNER).get(0);
        placedTiles.put(new Point2D.Double(0, 0), tiles.get(firstTile));
        System.out.println(tiles.get(firstTile).raw.stream().collect(Collectors.joining("\n")));
        System.out.println();
        for (Integer hash : tiles.get(firstTile).getAllHashes()) {
            List<Tile> matchingTiles = tilesByHash.get(hash);
            if (matchingTiles.size() > 1) {
                tiles.get(firstTile).setOrientation(Direction.RIGHT, hash);
                System.out.println(tiles.get(firstTile).finalOrientation.stream().collect(Collectors.joining("\n")));
                System.out.println();
                break;
            }
        }
        Tile lastPlacedTile = tiles.get(firstTile);
        
        // Set the top edge pieces
        boolean done = false;
        int column = 0;
        while (!done) {
            int hash = lastPlacedTile.getNextHash(Direction.RIGHT); //traveling along the top edge pieces, left-to-right; so reference the last piece's right hash
            final long lastId = lastPlacedTile.id;
            List<Tile> matchingTiles = tilesByHash.get(hash); 
            if (matchingTiles.size() == 1) {
                done = true;
            }
            else {
                column++;
                Tile nextTile = matchingTiles.stream().filter(t -> t.id != lastId).findAny().get();
                System.out.println(nextTile.raw.stream().collect(Collectors.joining("\n")));
                System.out.println();
                nextTile.setOrientation(Direction.LEFT, hash);
                System.out.println(nextTile.finalOrientation.stream().collect(Collectors.joining("\n")));
                System.out.println();
                placedTiles.put(new Point2D.Double(column, 0), nextTile);
                lastPlacedTile = nextTile;
            }
        }
        
        // Set the right edge pieces
        done = false;
        int row = 0;
        while (!done) {
            int hash = lastPlacedTile.getNextHash(Direction.DOWN);
            final long lastId = lastPlacedTile.id;
            List<Tile> matchingTiles = tilesByHash.get(hash);
            if (matchingTiles.size() == 1) {
                done = true;
            }
            else {
                row++;
                Tile nextTile = matchingTiles.stream().filter(t -> t.id != lastId).findAny().get();
                nextTile.setOrientation(Direction.UP, hash);
                placedTiles.put(new Point2D.Double(column, row), nextTile);
                lastPlacedTile = nextTile;
            }
        }
        
        // Set the bottom edge pieces
        done = false;
        while (!done) {
            int hash = lastPlacedTile.getNextHash(Direction.LEFT);
            final long lastId = lastPlacedTile.id;
            List<Tile> matchingTiles = tilesByHash.get(hash);
            if (matchingTiles.size() == 1) {
                done = true;
            }
            else {
                column--;
                Tile nextTile = matchingTiles.stream().filter(t -> t.id != lastId).findAny().get();
                nextTile.setOrientation(Direction.RIGHT, hash);
                placedTiles.put(new Point2D.Double(column, row), nextTile);
                lastPlacedTile = nextTile;
            }
        }
        
        // Set the right edge pieces
        done = false;
        while (!done) {
            int hash = lastPlacedTile.getNextHash(Direction.UP);
            final long lastId = lastPlacedTile.id;
            List<Tile> matchingTiles = tilesByHash.get(hash);
            if (matchingTiles.size() == 1) {
                done = true;
            }
            else {
                row--;
                Tile nextTile = matchingTiles.stream().filter(t -> t.id != lastId).findAny().get();
                nextTile.setOrientation(Direction.DOWN, hash);
                placedTiles.put(new Point2D.Double(column, row), nextTile);
                lastPlacedTile = nextTile;
            }
        }
        
        System.out.println("\n\n-------------FINAL----------------");
        Tile def = new Tile(-1);
        def.finalOrientation = Stream.generate(() -> "..........").limit(10).collect(Collectors.toList());
        for (row = 0; row < 12; row++) {
            for (int line = 0; line < 10; line++) {
                for (int col = 0; col < 12; col++) {
                    Tile t = placedTiles.getOrDefault(new Point2D.Double(col, row), def);
                    System.out.print(" " + t.finalOrientation.get(line) + " ");
                }
                System.out.println();
            }
            System.out.println();
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
            for (Integer hash : t.getAllHashes()) {
                tilesByHash.putIfAbsent(hash, new ArrayList<>());
                tilesByHash.get(hash).add(t);
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
    
    public static class Tile {
        public enum Location {CORNER, EDGE, MIDDLE}
        List<String> raw;
        List<String> finalOrientation;
        Map<Direction, Integer> finalHashCircle;
        long id;
        List<Hash> hashCircle;
        boolean usingFlippedHashCircle;
        Location location;
        
        public Tile(long id) {
            this.id = id;
            this.raw = new ArrayList<>();
            this.location = Location.MIDDLE;
            this.hashCircle = new ArrayList<>();
            this.usingFlippedHashCircle = false;
            this.finalOrientation = null;
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
        
        public void calculateHashes() {
            List<String> rotatedCW = rotateTileCW(raw);
            hashCircle.add(new Hash(rotatedCW.get(0), Direction.LEFT));
            hashCircle.add(new Hash(raw.get(0), Direction.UP));
            hashCircle.add(new Hash(rotatedCW.get(rotatedCW.size() - 1), Direction.RIGHT));
            hashCircle.add(new Hash(raw.get(raw.size() - 1), Direction.DOWN));
            for (int i = 0; i < 4; i++) {
                hashCircle.get(i).setRightNeighbor(hashCircle.get((i + 1) % 4));
            }
        }
        
        public Set<Integer> getAllHashes() {
            return Stream.concat(hashCircle.stream().map(h -> h.hash), hashCircle.stream().map(h -> h.flippedHash)).collect(Collectors.toSet());
        }
        
        public void setOrientation(Direction d, int hash) {
            Hash matchingHash = hashCircle.stream().filter(h -> h.containsHash(hash)).findAny().get();
            int rotationDistance = matchingHash.rotateTo(d, d);
            setFinalOrientation(rotationDistance, matchingHash.flippedFromRaw);
        }
        
        private void setFinalOrientation(int rotationDistance, boolean flippedFromRaw) {
            List<String> rawRotatedCW = rotateTileCW(raw);
            if (flippedFromRaw) {
                finalOrientation = switch (rotationDistance) {
                    case 0, 4, -4 -> raw.stream().map(Tile::reverseString).collect(Collectors.toList());
                    case 1, -3 -> reverseStream(rawRotatedCW.stream()).collect(Collectors.toList());
                    case 2, -2 -> reverseStream(raw.stream()).collect(Collectors.toList());
                    case 3, -1 -> rawRotatedCW.stream().map(Tile::reverseString).collect(Collectors.toList());
                    default -> raw;
                };
            }
            else {
                finalOrientation = switch (rotationDistance) {
                    case 0, 4, -4 -> raw;
                    case 1, -3 -> rawRotatedCW;
                    case 2, -2 -> reverseStream(raw.stream()).map(Tile::reverseString).collect(Collectors.toList());
                    case 3, -1 -> reverseStream(rawRotatedCW.stream()).map(Tile::reverseString).collect(Collectors.toList());
                    default -> raw;
                };
            }
        }
        
        private static <T> Stream<T> reverseStream(Stream<T> s) {
            //https://stackoverflow.com/a/24011264
            return s.collect(Collector.of(
                ArrayDeque<T>::new,
                (deq, t) -> deq.addFirst(t),
                (d1, d2) -> { 
                    d2.addAll(d1); 
                    return d2; 
                })).stream();
        }
        
        public int getNextHash(Direction d) {
            Hash matchingHash = hashCircle.stream().filter(h -> h.getDirection() == d).findAny().get();
            return matchingHash.getHash();
        }
        
        private List<String> rotateTileCW(List<String> image) {
            char[][] mat = new char[image.size()][];
            for (int i = 0; i < image.size(); i++) {
                mat[i] = image.get(i).toCharArray();
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
            return Arrays.stream(ret).map(String::new).collect(Collectors.toList());
        }
        
        private static String reverseString(String s) {
            return new StringBuilder(s).reverse().toString();
        }
        
        @Override
        public String toString() {
            if (finalOrientation == null) {
                return Long.toString(id);
            }
            else {
                return finalOrientation.stream().map(s -> " " + s + " ").collect(Collectors.joining("\n"));
            }
        }
        
        static class Hash {
            int hash;
            int flippedHash;
            Direction direction;
            boolean flipped;
            boolean flippedFromRaw;
            private static final Set<Direction> TOP_LEFT = Set.of(Direction.UP, Direction.LEFT);
            private static final Set<Direction> BOTTOM_RIGHT = Set.of(Direction.DOWN, Direction.RIGHT);
            Hash rightNeighbor;

            public Hash(String edge, Direction direction) {
                this.direction = direction;
                if (direction == Direction.DOWN || direction == Direction.RIGHT) {
                    this.flippedHash = edge.hashCode();
                    this.hash = reverseString(edge).hashCode();
                    this.flipped = true;
                }
                else {
                    this.hash = edge.hashCode();
                    this.flippedHash = reverseString(edge).hashCode();
                    this.flipped = false;
                }
            }

            public void setRightNeighbor(Hash rightNeighbor) {
                this.rightNeighbor = rightNeighbor;
            }
            
            public int getHash() {
                return flipped ? flippedHash : hash;
            }

            public Direction getDirection() {
                return direction;
            }
            
            public boolean containsHash(int hashcode) {
                return hash == hashcode || flippedHash == hashcode;
            }
            
            public boolean isFlippedFromRaw() {
                return flippedFromRaw;
            }
            
            /**
             * 
             * @param originalNewSide
             * @param newSide
             * @return rotationDistance, +CW -CCW
             */
            public int rotateTo(Direction originalNewSide, Direction newSide) {
                int rotationDistance = direction.rotation90Distance(newSide);
                if (rotationDistance == 0) {
                    return 0;
                }
                if (TOP_LEFT.contains(direction) && BOTTOM_RIGHT.contains(newSide) ||
                    BOTTOM_RIGHT.contains(direction) && TOP_LEFT.contains(newSide)) {
                    flipped = !flipped;
                    flippedFromRaw = true;
                }
                direction = newSide;
                Direction neighborsNewSide = newSide.rotateRight90();
                if (neighborsNewSide != originalNewSide) {
                    rightNeighbor.rotateTo(originalNewSide, neighborsNewSide);
                }
                return rotationDistance;
            }

            @Override
            public int hashCode() {
                int h = 7;
                h = 41 * h + this.hash;
                h = 41 * h + this.flippedHash;
                h = 41 * h + Objects.hashCode(this.direction);
                h = 41 * h + (this.flipped ? 1 : 0);
                return h;
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
                final Hash other = (Hash) obj;
                if (this.hash != other.hash) {
                    return false;
                }
                if (this.flippedHash != other.flippedHash) {
                    return false;
                }
                if (this.flipped != other.flipped) {
                    return false;
                }
                return this.direction == other.direction;
            }
        }
    }
}
