package com.jeffrpowell.adventofcode.aoc2020;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        List<Tile> tiles = new ArrayList<>();
        Tile tile = new Tile(-1);
        for (String string : input) {
            if (string.endsWith(":")) {
                Matcher m = TILE_ID_PATTERN.matcher(string);
                m.matches();
                tile = new Tile(Integer.parseInt(m.group(1)));
                tiles.add(tile);
            }
            else if (!string.isBlank()) {
                tile.addLine(string);
            }
        }
        tiles.stream().forEach(Tile::calculateHashes);
        return "";
    }

    @Override
    protected String part2(List<String> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static class Tile {
        List<String> raw;
        int id;
        Set<Integer> hashes;
        
        public Tile(int id) {
            this.id = id;
            this.raw = new ArrayList<>();
            this.hashes = new HashSet<>();
        }
        
        public void addLine(String line) {
            raw.add(line);
        }
        
        public void calculateHashes() {
            hashes.add(raw.get(0).hashCode());
            hashes.add(raw.get(raw.size() - 1).hashCode());
            hashes.add(reverseString(raw.get(0)).hashCode());
            hashes.add(reverseString(raw.get(raw.size() - 1)).hashCode());
            List<String> rotatedRaw = rotateTile();
            hashes.add(rotatedRaw.get(0).hashCode());
            hashes.add(rotatedRaw.get(rotatedRaw.size() - 1).hashCode());
            hashes.add(reverseString(rotatedRaw.get(0)).hashCode());
            hashes.add(reverseString(rotatedRaw.get(rotatedRaw.size() - 1)).hashCode());
        }
        
        private static String reverseString(String s) {
            return new StringBuilder(s).reverse().toString();
        }
        
        private List<String> rotateTile() {
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
    }
}
