package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;

public class Road extends SegmentFeature {
    private boolean looped;
    /**
     * Creates a feature at position x, y containing only
     * segment s
     * @param x x coordinate
     * @param y y coordinate
     * @param s segment
     */
    public Road(int x, int y, Segment s) {
        super(x, y, s);
    }

    /**
     * Calculates the score assuming the feature is completed
     * @return the completed score of the feature as in int
     */
    public int finishedScore() {return size();}

    /**
     * Calculates the score at the end of the game, assuming
     * the feature is incomplete
     * @return the incomplete score of the feature as an int
     */
    public int endgameScore() {return size();}

    /**
     * Checks if the feature is completed and if so, returns all
     * followers and adjusts scores
     * @returns true if the feature is complete
     */
    public boolean checkComplete() {
        int completeEnds = 0;
        for (Segment seg : getSegments()) {
            if (seg.getFeature() == SegType.intersection) completeEnds++;
        }
        assert (completeEnds<=2);
        if (completeEnds == 2 || looped) {
            returnAllFollowers();
            return true;
        }
        return false;
    }

    /**
     * Extends the road by segment s, and if segment s extends the road in
     * 2 places, then the road a looped and the looped field is updated accordingly
     * @param x the x coordinate of the new segment
     * @param y the y coordinate of the new segment
     * @param s the segment being added to the feature
     */
    @Override
    void extendSegment(int x, int y, Segment s) {
        int numExtends = 0;
        for (Segment top : getSegmentsAtLoc(x, y-1)) {
            if (s.extendsBelow(top)) numExtends++;
        }
        for (Segment bottom : getSegmentsAtLoc(x, y+1)) {
            if (s.extendsAbove(bottom)) numExtends++;
        }
        for (Segment left : getSegmentsAtLoc(x-1, y)) {
            if (s.extendsRight(left)) numExtends++;
        }
        for (Segment right : getSegmentsAtLoc(x+1, y)) {
            if (s.extendsLeft(right)) numExtends++;
        }
        assert(numExtends <= 2);
        if (numExtends == 2) looped = true;
        super.extendSegment(x, y, s);
    }

    @Override
    public String toString() {
        return "Road \n" + super.toString();
    }
}
