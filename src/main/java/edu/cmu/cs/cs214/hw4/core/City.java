package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;

/**
 * Class representing a city feature
 */
public class City extends SegmentFeature {
    /**
     * Creates a feature at position x, y containing only
     * segment s
     * @param x x coordinate
     * @param y y coordinate
     * @param s segment
     */
    public City(int x, int y, Segment s) {
        super(x, y, s);
    }

    /**
     * Finds the number of pennants in the city
     * @return number of pennants
     */
    private int numPennants() {
        int np = 0;
        for (Segment seg : getSegments()) {
            if (seg.getFeature() == SegType.pennant) np++;
        }
        return np;
    }

    /**
     * Calculates the score assuming the city is completed
     * @return the completed score of the feature as in int
     */
    public int finishedScore() {return 2*size() + 2*numPennants();}

    /**
     * Calculates the score at the end of the game, assuming
     * the city is incomplete
     * @return the incomplete score of the feature as an int
     */
    int endgameScore() {
        return size() + numPennants();
    }

    /**
     * Checks if the feature is completed and if so, returns all
     * followers and adjusts scores
     * @returns true if the feature is complete
     */
    public boolean checkComplete() {
        Segment allCity = (new Segment (SegType.city,
                new int[][]{{1, 1, 1},
                        {1, 1, 1},
                        {1, 1, 1}}));
        for (int i = 0; i < getxCoords().size(); i++) {
            int x = getxCoords().get(i);
            int y = getyCoords().get(i);
            if (doesNotContain(x+1, y, getxCoords(), getyCoords())
                    && (checkExtendSegment(x+1, y, allCity))) return false;

            if (doesNotContain(x-1, y, getxCoords(), getyCoords())
                    && (checkExtendSegment(x-1, y, allCity))) return false;

            if (doesNotContain(x, y+1, getxCoords(), getyCoords())
                    && (checkExtendSegment(x, y+1, allCity))) return false;

            if (doesNotContain(x, y-1, getxCoords(), getyCoords())
                    && (checkExtendSegment(x, y-1, allCity))) return false;
        }
        returnAllFollowers();
        return true;
    }

    @Override
    public String toString() {
        return "City \n" + super.toString();
    }



}
