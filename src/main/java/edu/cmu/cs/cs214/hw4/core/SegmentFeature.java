package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Class representing a feature that can have multiple segments
 */
public abstract class SegmentFeature implements Feature {
    private int size;
    private ArrayList<Integer> xCoords;
    private ArrayList<Integer> yCoords;
    private ArrayList<Segment> segments;
    private ArrayList<Follower> followers;
    private HashSet<Player> haveFollowers;


    /**
     * Creates a new feature at position x, y containing only
     * the segment s
     * @param x x coordinate of new feature
     * @param y y coordinate of new feature
     * @param s only segment of new feature
     */
    public SegmentFeature(int x, int y, Segment s) {
        size = 1;
        xCoords = new ArrayList<>();
        yCoords = new ArrayList<>();
        segments = new ArrayList<>();
        followers = new ArrayList<>();
        haveFollowers = new HashSet<>();

        segments.add(s);
        xCoords.add(x);
        yCoords.add(y);
    }

    /**
     * Calculates the score assuming the Feature is completed
     * @return the completed score of the feature as in int
     */
    public abstract int finishedScore();

    /**
     * Calculates the score at the end of the game, assuming
     * the feature is incomplete
     * @return the incomplete score of the feature as an int
     */
    abstract int endgameScore();

    /**
     * Checks if the feature is completed and if so, returns all
     * followers and adjusts scores
     * @returns true if the feature is complete
     */
    public abstract boolean checkComplete();


    /**
     * Return the size of the feature, or the number of tiles
     * it occupies
     * @return size
     */
    int size() {return size;}

    /**
     * Return x coordinates of the feature, which correspond to
     * segments in list segments
     * @return x coordinates
     */
    ArrayList<Integer> getxCoords() {return xCoords;}

    /**
     * Return y coordinates of the feature, which correspond to
     * segments in list segments
     * @return x coordinates
     */
    ArrayList<Integer> getyCoords() {return yCoords;}

    /**
     * Return list of segments in feature
     * @return segments
     */
    ArrayList<Segment> getSegments() {return segments;}

    /**
     * @return true if a feature has no followers
     */
    public boolean noFollowers() {return (haveFollowers.size() == 0);}


    /**
     * Places a follower if feature doesn't currently contain any
     * @param f the new follower
     * @return true if possible and false otherwise
     */
     public boolean placeFollower(Follower f) {
         if (followers.size() == 0) {
             followers.add(f);
             haveFollowers.add(f.getPlayer());
             return true;
         }
         else return false;
    }

    /**
     * Checks if it is possible to extend the feature by a segment at
     * position x,y, but does not actually do so
     * @param x the x coordinate of the new segment
     * @param y the y coordinate of the new segment
     * @param s the segment being added to the feature
     * @return true if the extension is possible and false otherwise
     */
    boolean checkExtendSegment(int x, int y, Segment s) {
        for (Segment top : getSegmentsAtLoc(x, y-1)) {
            if (s.extendsBelow(top)) return true;
        }
        for (Segment bottom : getSegmentsAtLoc(x, y+1)) {
            if (s.extendsAbove(bottom)) return true;
        }
        for (Segment left : getSegmentsAtLoc(x-1, y)) {
            if (s.extendsRight(left)) return true;
        }
        for (Segment right : getSegmentsAtLoc(x+1, y)) {
            if (s.extendsLeft(right)) return true;
        }
        return false;
    }

    /**
     * Extends this feature by the segment s, placed at x, y, assuming that
     * s does in fact extend this feature
     * @param x the x coordinate of the new segment
     * @param y the y coordinate of the new segment
     * @param s the segment being added to the feature
     */
    void extendSegment(int x, int y, Segment s) {
        assert(checkExtendSegment(x, y, s));
        xCoords.add(x);
        yCoords.add(y);
        segments.add(s);
        refreshSize();
        checkComplete();
    }

    /**
     * Returns true if a pair of coordinate lists xs and ys contain the coordinate x and y
     * @param x x coordinate
     * @param y y coordinate
     * @param xs x coordinate list
     * @param ys y coordinate list
     * @return true if the list don't contain the coordinates
     */
    boolean doesNotContain(int x, int y, ArrayList<Integer> xs, ArrayList<Integer> ys) {
        for (int i = 0; i < xs.size(); i++) {
            if (xs.get(i) == x && ys.get(i) == y) return false;
        }
        return true;
    }

    /**
     * Refreshes the size of the feature based on how many unique coordinates there are
     * in xCoords and yCoords
     */
    private void refreshSize() {
        ArrayList<Integer> uniquex = new ArrayList<>();
        ArrayList<Integer> uniquey = new ArrayList<>();
        for (int i = 0; i < this.xCoords.size(); i++) {
            int x = this.xCoords.get(i);
            int y = this.yCoords.get(i);
            if (doesNotContain(x, y, uniquex, uniquey)) {
                uniquex.add(x);
                uniquey.add(y);
            };
        }
        this.size = uniquex.size();
    }

    /**
     * Combines an array of features into one feature with the combined
     * size , segments, and followers of all of them
     * @param features the list of features being combined
     * @return the new, combined feature
     */
    SegmentFeature combineFeatures(ArrayList<SegmentFeature> features) {
        ArrayList<Integer> newxCoords = new ArrayList<>();
        ArrayList<Integer> newyCoords = new ArrayList<>();
        ArrayList<Segment> newSegs = new ArrayList<>();
        ArrayList<Follower> newFols = new ArrayList<>();
        HashSet<Player> newHaveFols = new HashSet<>();

        for (SegmentFeature f : features) {
            newxCoords.addAll(f.xCoords);
            newyCoords.addAll(f.yCoords);
            newSegs.addAll(f.segments);
            newFols.addAll(f.followers);
            newHaveFols.addAll(f.haveFollowers);
        }
        SegmentFeature newF = features.get(0);
        newF.xCoords = newxCoords;
        newF.yCoords = newyCoords;
        newF.segments = newSegs;
        newF.followers = newFols;
        newF.haveFollowers = newHaveFols;
        newF.refreshSize();
        newF.checkComplete();
        return newF;
    }

    /**
     * Finds all segments in the feature at coordinates x, y
     * @param x x coordinate of segments we want
     * @param y y coordinate of segments we want
     * @return list of segments at x, y
     */
    ArrayList<Segment> getSegmentsAtLoc(int x, int y) {
        ArrayList<Segment> s = new ArrayList<>();
        for (int i = 0; i < this.xCoords.size(); i++) {
            int xi = this.xCoords.get(i);
            int yi = this.yCoords.get(i);
            if (xi == x && yi == y) s.add(segments.get(i));
        }
        return s;
    }

    /**
     * Returns followers to their respective players after a feature is
     * completed and adjusts player scores
     */
    void returnAllFollowers() {
        for (Player p : maxFols()) {
            p.addScore(finishedScore());
        }
        for (Follower f : this.followers) {
            f.returnMe();
        }
        haveFollowers.clear();
        followers.clear();
    }

    /**
     * If the feature is incomplete gives the players with
     * the max number of followers the incomplete
     * score for the feature, assuming the game is over
     */
    public void endgame() {
        if (!(checkComplete())) {
            for (Player p : maxFols()) {
                p.addScore(endgameScore());
            }
            for (Follower f : followers) {
                f.returnMe();
            }
        }
    }

    /**
     * Calculates the number of followers that player p has on the board
     * @param p the player
     * @return number of followers
     */
    private int numFollowers(Player p) {
        int num = 0;
        for (Follower f : this.followers) {
            if (p == f.getPlayer()) num++;
        }
        return num;
    }


    /**
     * Calculates set of players all of whom possess the maximum number
     * of followers on the feature.
     * @return followers who can get points from the feature
     */
    private HashSet<Player> maxFols() {
        int mf = 0;
        HashSet<Player> res = new HashSet<>();
        for (Player p : this.haveFollowers) {
            if (numFollowers(p) > mf) {
                res.clear();
                res.add(p);
                mf = numFollowers(p);
            }
            else if (numFollowers(p) == mf) {
                res.add(p);
                mf = numFollowers(p);
            }
        }
        return res;
    }

    /**
     * Represents a feature as a string
     * @returns information about the feature as a string
     */
    @Override
    public String toString() {
        String segs = "";
        for (int i = 0; i < this.segments.size(); i++) {
            segs = segs + this.xCoords.get(i) + "," + this.yCoords.get(i) + " "
                    + this.segments.get(i).toString() + "\n";
        }
        String fol = "";
        for (Follower f: this.followers) {
            fol = fol + f.getPlayer().toString() + " ";
        }
        String score = "";
        if (checkComplete()) score = score + finishedScore();
        return "Size: " + this.size + "\n Followers: " + fol + "\n"
        + "Completed: " + checkComplete() + "\n" + segs;
    }

    /**
     * Represents features as a string, as well as displays feature scores
     * based on the board
     * @return features as a string
     */
    String toStringWithBoard() {
        int score = endgameScore();
        if (checkComplete()) score = finishedScore();
        return toString() + "Score: " + score + "\n\n";
    }
}
