package edu.cmu.cs.cs214.hw4.core;

public class Follower {
    private Player owner;
    private int xLoc;
    private int yLoc;

    Follower(Player p, int x, int y) {
        owner = p;
        xLoc = x;
        yLoc = y;
    }

    /**
     * Follower's owner
     * @return owner
     */
    Player getPlayer() {return owner;}

    /**
     * Returns follower to owner
     */
    void returnMe() {
        owner.returnFollower(xLoc, yLoc);
    }
}
