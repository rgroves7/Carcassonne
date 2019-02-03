package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Class representing a Carcassonne board
 */
public class Board {
    private Tile[][] grid;
    private ArrayList<SegmentFeature> features;
    private ArrayList<Cloister> cloisters;
    private final int maxWidth = 72;
    private boolean empty = true;

    final int boardWidth = 2*maxWidth + 3;


    /**
     * Creates a board with the starting tile at (maxWidth, maxWidth) with enough
     * "empty" spaces surrounding it, represented by null, for any possible games.
     * @param startingTile is the tile that is placed in the middle
     */
    Board(Tile startingTile) {
        this.grid = new Tile[boardWidth][boardWidth];
        features = new ArrayList<>();
        cloisters = new ArrayList<>();
        placeTile(maxWidth+1, maxWidth + 1, startingTile);
        empty = false;
    }

    Tile getTile(int x, int y) {
        return grid[y][x];
    }


    /**
     * Finds the feature at board location x, y and tile location tileX, tileY
     * @param x board x coordinate
     * @param y bord y coordinate
     * @param tileX tile x coordinate
     * @param tileY tile y coordinate
     * @return the feature at the specified location, or null if one does not exist
     */
    Feature featureAtLocation(int x, int y, int tileX, int tileY) {
        if (tileX == 1 && tileY == 1) {
            for (Cloister c : cloisters) {
                if (c.getXloc() == x && c.getYloc() == y) return c;
            }
        }
        for (SegmentFeature f : features) {
            ArrayList<Segment> segs = f.getSegmentsAtLoc(x, y);
            for (Segment s : segs) {
                if (s.occupiesLocation(tileX, tileY)) return f;
            }
        }
        return null;
    }
    /**
     * Checks if a tile can be placed at a certain location on the board, meaning
     * there is no tile currently there, there is at least one currently placed
     * adjacent tile, and there are no conflicting adjacent edges
     * @requires 1 <= x < boardWidth - 1 && 1 <= y < boardWidth - 1
     * @param x the x coordinate of the potential location of the tile
     * @param y the y coordinate of the potential location of the tile
     * @param t the tile being placed
     * @return true if possible and false otherwise
     */
    boolean fits(int x, int y, Tile t) {
        if (empty) return true;
        if (x < 1 || y < 1 || x >= boardWidth - 1 || y >= boardWidth - 1) return false;
        if (grid[y][x] != null) return false;
        int abutting = 0;
        if (grid[y][x-1] != null) {
            abutting ++;
            Tile leftTile = grid[y][x-1];
            if (leftTile.getRight() != t.getLeft()) return false;

        }
        if (grid[y][x+1] != null) {
            abutting ++;
            Tile rightTile = grid[y][x+1];
            if (rightTile.getLeft() != t.getRight()) return false;
        }
        if (grid[y+1][x] != null) {
            abutting ++;
            Tile bottomTile = grid[y+1][x];
            if (bottomTile.getTop() != t.getBottom()) return false;
        }
        if (grid[y-1][x] != null) {
            abutting ++;
            Tile topTile = grid[y-1][x];
            if (topTile.getBottom() != t.getTop())return false;
        }
        return (abutting != 0);
    }

    /**
     * Returns true if there exists a legal placement on the board for
     * any rotation of a tile t
     * @param t the tile being considered
     * @return true if there is a legal placement and false otherwise
     */
    boolean isLegalPlacement(Tile t) {
        Tile copy = t.TileCopy();
        for (int i = 1; i < boardWidth - 1; i++) {
            for (int j = 1; j < boardWidth - 1; j++) {
                if (fits(i, j, copy)) return true;
                copy.rotate();
                if (fits(i, j, copy)) return true;
                copy.rotate();
                if (fits(i, j, copy)) return true;
                copy.rotate();
                if (fits(i, j, copy)) return true;
            }
        }
        return false;
    }

    /**
     * Finds all the features on the board that are extended by a segment
     * s if it is added at x,y
     * @param x x coordinate of segment location
     * @param y y coordinate of segment location
     * @param s segment being added
     * @return List of extended features
     */
    private ArrayList<SegmentFeature> extendedFeatures(int x, int y, Segment s) {
        ArrayList<SegmentFeature> extendedFeatures = new ArrayList<>();
        for (SegmentFeature f : features) {
            if (f.checkExtendSegment(x, y, s)) extendedFeatures.add(f);
        }
        return extendedFeatures;
    }

    /**
     * Places the segment in location x, y, and updates features. If
     * s extends multiple features, these features are combined into
     * one
     * @param x the x coordinate of the new segment
     * @param y the y coordinate of the new segment
     * @param s the new segment
     * @returns extended feature
     */
    private Feature placeSegment(int x, int y, Segment s) {
        ArrayList<SegmentFeature> ef = extendedFeatures(x, y, s);
        if (ef.size() == 1) {
            SegmentFeature f = ef.get(0);
            f.extendSegment(x, y, s);
            return f;
        }
        else if (ef.size() > 1) {
            SegmentFeature f = ef.get(0);
            f.extendSegment(x, y, s);
            SegmentFeature newF = f.combineFeatures(ef);
            for (SegmentFeature r : ef) {
                features.remove(r);
            }
            features.add(newF);
            return newF;
        }
        else {
            if (s.getFeature() == SegType.cloister) {
                Cloister c = new Cloister(x, y, s, getNewCloisterSize(x, y));
                cloisters.add(c);
                return c;
            }
            if (s.getFeature() == SegType.field) {
                Field f = new Field (x, y, s, features);
                features.add(f);
                return f;
            }
            SegmentFeature newF = s.newFeature(x, y);
            features.add(newF);
            return newF;
        }
    }

    /**
     * Places tile on board by placing every segment in the tile and
     * also considering which surrounding cloisters it may be extending
     * @param x x coordinate of new tile
     * @param y y coordinate of new tile
     * @param t new tile
     * @returns set of extended features with no followers
     */
    HashSet<Feature> placeTile(int x, int y, Tile t) {
        if (!(fits(x, y, t))) throw new IllegalStateException();
        HashSet<Feature> fs = new HashSet<>();
        for (Segment s : t.getSegments()) {
            fs.add(placeSegment(x, y, s));
        }
        for (Cloister c : cloisters) {
            c.extendTile(x, y);
        }
        Iterator<Feature> f = fs.iterator();
        while (f.hasNext()) {
            Feature feat = f.next();
            if (!(feat.noFollowers())) f.remove();
        }
        grid[y][x] = t;
        return fs;
    }

    /**
     * Starting size of cloister if a new one is created at x, y
     * @param x hypothetical x coordinate of new cloister
     * @param y hypothetical y coordinate of new cloister
     * @ensures result <= 9
     * @return the starting size as an int
     */
    private int getNewCloisterSize(int x, int y) {
        int count = 0;
        if (grid[y-1][x+1]!=null) count++;
        if (grid[y-1][x-1]!=null) count++;
        if (grid[y-1][x]!=null) count++;
        if (grid[y+1][x]!=null) count++;
        if (grid[y+1][x-1]!=null) count++;
        if (grid[y+1][x+1]!=null) count++;
        if (grid[y][x+1]!=null) count++;
        if (grid[y][x-1]!=null) count++;
        assert(count <= 8);
        return count + 1;
    }

    /**
     * Goes through all the incomplete features and gives the appropriate
     * followers endgame points
     */
    void endgame() {
        for (SegmentFeature f : features) f.endgame();
        for (Cloister c : cloisters) c.endgame();
    }

    /**
     * Represents the board as a string with dashes in blank spaces and tile
     * names in their respective places
     * @return the board as a string
     */
    @Override
    public String toString() {
        String s = "";
        for (Tile[] row : grid) {
            for (Tile t : row) {
                if (t == null) s = s + "- ";
                else s = s + t.getName() + " ";
            }
            s = s + "\n";
        }
        String fs = "";
        for (SegmentFeature f : features) {
            fs = fs + f.toStringWithBoard();
        }
        for (Cloister c : cloisters) {
            fs = fs + c.toString();
        }
        return s + fs;
    }

}
