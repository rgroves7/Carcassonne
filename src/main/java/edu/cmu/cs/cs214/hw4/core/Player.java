package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private int score;
    private int numFollowers;

    private final List<GameChangeListener> listeners = new ArrayList<>();

    /**
     * Creates new player with name n, starting with score 0
     * and 7 followers
     * @param n the players name
     */
    Player(String n) {
        name = n;
        score = 0;
        numFollowers = 7;
    }

    void addGameChangeListener(GameChangeListener l) {listeners.add(l);}

    /**
     * Player's score
     * @return score
     */
    public int score() {
        return score;
    }

    /**
     * Player's name
     * @return name
     */
    public String name() {return name;}

    /**
     * Remaining followers in the player's possession
     * @return number of followers
     */
    public int getNumFollowers() {return numFollowers;}


    /**
     * Places a follower on feature F if feature doesn't currently contain any
     * @param F feature that the player is placing a follower on
     * @return true if possible and false otherwise
     */
    boolean placeFollower(int x, int y, Feature F) {
        if (numFollowers == 0) return false;
        numFollowers --;
        Follower fol = new Follower(this, x, y);
        return F.placeFollower(fol);
    }


    /**
     * Returns one follower to the player
     */
    void returnFollower(int x, int y) {
        this.numFollowers++;
        for (GameChangeListener l : listeners) {
            l.followerReturned(x, y, this);
        }
    }

    /**
     * Increases the players score by a specified int
     * @param s amount the score is increasing by
     */
    void addScore(int s) {

        this.score += s;
        for (GameChangeListener l : listeners) {
            l.scoreChanged(this);
        }
    }

    /**
     * Returns the players name as a string
     * @return name
     */
    @Override
    public String toString() {
        return name;
    }

}
