package edu.cmu.cs.cs214.hw4.core;


public interface GameChangeListener {
    /**
     * The current tile has been placed at position x, y
     * @param x x coordinate of tile location
     * @param y y coordinate of tile location
     */
    void tilePlaced(int x, int y);

    /**
     * The turn has changed
     */
    void tileRotated();

    /**
     * The turn has changed
     */
    void turnChanged();

    /**
     * A follower has been placed at position x, y on the board and position
     * tileX, tileY on the tile
     * @param x x coordinate on the board
     * @param y y coordinate on the board
     * @param tileX x coordinate on the tile
     * @param tileY y coordinate on the tile
     * @param success true if the placement is successful and false otherwise
     */
    void followerPlaced(int x, int y, int tileX, int tileY, boolean success);

    /**
     * Player p's score has increased
     * @param p the player whose score has changed
     */
    void scoreChanged(Player p);

    /**
     * A follower belonging to player p has been returned at location x, y
     * @param x x coordinate of returned follower
     * @param y y coordinate of returned follower
     * @param p player whose follower has been returned
     */
    void followerReturned(int x, int y, Player p);

    /**
     * The game is over
     * @param winner the player who won the game. null if there is a tie
     */
    void gameEnded(Player winner);

}