package edu.cmu.cs.cs214.hw4.core;

/**
 * Interface representing features
 */
public interface Feature {
    /**
     * Checks if the feature is completed and if so, returns all
     * followers and adjusts scores
     * @returns true if the feature is complete
     */
    boolean checkComplete();

    /**
     * Calculates the score assuming the feature is completed
     * @return the completed score of the feature as in int
     */
    int finishedScore();

    /**
     * If the feature is incomplete at the end of the game, calculates the
     * score and gives the points to the appropriate players
     */
    void endgame();

    /**
     * Places a follower if feature doesn't currently contain any
     * @param f the new follower
     * @return true if possible and false otherwise
     */
    boolean placeFollower(Follower f);

    /**
     * @return true if a feature has no followers
     */
    boolean noFollowers();
}
