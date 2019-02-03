package edu.cmu.cs.cs214.hw4.core;

/**
 * Class representing a cloister.
 * @invariant size <= 9
 */
public class Cloister implements Feature{
    private final int xloc;
    private final int yloc;
    private int size;
    private Follower follower = null;

    /**
     * Creates a feature at position x, y containing only
     * segment s
     * @requires size <= 9
     * @param x x coordinate
     * @param y y coordinate
     * @param s segment
     */
    public Cloister(int x, int y, Segment s, int sz) {
        if (sz > 9 || sz < 0) throw new IllegalArgumentException();
        xloc = x;
        yloc = y;
        size = sz;
    }

    /**
     * The cloister itself plus the number of tiles surrounding it
     * @return the size, which is always less than 9
     */
    int size() { return size; }

    int getXloc() {return xloc;}
    int getYloc() {return yloc;}

    /**
     * Calculates the score assuming the cloister is complete
     * @return the completed score of the feature as in int
     */
    public int finishedScore() {return 9;}

    /**
     * If there is a follower in the cloister, give the corresponding
     * player the incomplete score
     */
    public void endgame() {
        if (!(checkComplete()) && follower != null) {
            follower.getPlayer().addScore(size);
            follower.returnMe();
        }
    }

    /**
     * @return true if a feature has no followers
     */
    public boolean noFollowers() {return (follower == null);}

    /**
     * If a new tile at x,y adds to the size of this cloister, adjust
     * the size as such. If the cloister is completed, return the
     * follower to the player and adjust their score (if there is one)
     * @param x x coordinate of new tile
     * @param y y coordinate of new tile
     */
    void extendTile(int x, int y) {
        if ((x == xloc && y == yloc + 1)
                || (x == xloc && y == yloc - 1)
                || (x == xloc + 1 && y == yloc + 1)
                || (x == xloc -1 && y == yloc - 1)
                || (x == xloc + 1 && y == yloc)
                || (x == xloc - 1 && y == yloc)
                || (x == xloc + 1  && y == yloc - 1)
                || (x == xloc - 1 && y == yloc + 1)) size++;
        checkComplete();
    }

    /**
     * Checks if the cloister is completed and if so, returns follower to
     * player and adjusts score
     * @return true if the cloister is complete and false otherwise
     */
    public boolean checkComplete() {
        if (size == 9) {
            if (follower != null) {
                follower.getPlayer().addScore(finishedScore());
                follower.returnMe();
            }
            return true;
        }
        return false;
    }

    /**
     * Places a follower owned by player p on the cloister if possible
     * @param f new follower
     * @return true if placement is possible and false otherwise
     */
    public boolean placeFollower(Follower f) {
        if (follower == null) {
            follower = f;
            return  true;
        }
        return false;
    }

    @Override
    public String toString() {
        String fol = "";
        if (follower != null) fol = fol + follower.toString();
        return "Cloister \n size: " + size + " " +
                " Location: " + xloc + "," + yloc + " " +
                "\n Completed: " + checkComplete() +
                "\n Follower: " + fol + "\n";
    }




}
