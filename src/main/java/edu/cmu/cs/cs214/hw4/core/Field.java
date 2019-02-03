package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;

/**
 * Class representing a field
 * @invariant !completed
 */
public class Field extends SegmentFeature {
    private ArrayList<SegmentFeature> feats;

    /**
     * Creates a feature at position x, y containing only
     * segment s
     * @param x x coordinate
     * @param y y coordinate
     * @param s segment
     */
    public Field(int x, int y, Segment s, ArrayList<SegmentFeature> fs) {
        super(x, y, s);
        feats = fs;
    }

    /**
     * Assigns feats to fs
     * @param fs array list that feats will be set to
     */
    void setFeats(ArrayList<SegmentFeature> fs) {
        feats = fs;
    }

    /**
     * A field is never completed so this returns 0
     * @return 0
     */
    public int finishedScore() {return 0;}

    /**
     * Calculates the score at the end of the game, assuming
     * the feature is incomplete
     * @return the incomplete score of the feature as an int
     */
    public int endgameScore() {
        int count = 0;
        for (SegmentFeature f : feats) {
            if (f.checkComplete() && isOverlappingCity(f)) {
                count += 3;
            }
        }
        return count;
    }

    /**
     * Checks if the feature is completed. A field is never complete
     * so this always returns false
     */
    public boolean checkComplete() {return false;}

    /**
     * Determines if another feature is a city that overlaps with
     * this field
     * @param other feature that may be an overlapping city
     * @return true if other is a city that overlaps with this
     */
    private boolean isOverlappingCity (Feature other) {
        if (!(other instanceof City)) return false;
        City c = (City)other;
        for (int i = 0; i < getxCoords().size(); i++) {
            int x = getxCoords().get(i);
            int y = getyCoords().get(i);
            Segment fieldSeg = getSegments().get(i);
            for (int j = 0; j < c.getxCoords().size(); j++) {
                int cx = c.getxCoords().get(j);
                int cy = c.getyCoords().get(j);
                Segment citySeg = c.getSegments().get(j);
                if (x == cx && y == cy &&
                fieldSeg.touchesWithinTile(citySeg)) return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Field \n" + super.toString();
    }


}
