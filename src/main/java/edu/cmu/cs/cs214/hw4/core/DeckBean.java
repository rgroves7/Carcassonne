package edu.cmu.cs.cs214.hw4.core;

/**
 * Describes the distribution of tiles in a Carcassonne deck
 */
public class DeckBean {
    /**
     * The starting tile, to be placed in the center of the board
     */
    private Tile startingtile;
    public Tile getStartingtile() {return startingtile;};
    public void setStartingtile(Tile startingtile) {
        this.startingtile = startingtile;
    }

    /**
     * All the remaining tiles, to be duplicated according to their
     * quantity and shuffled into a deck
     */
    private Tile[] tiles;
    public Tile[] getTiles() {return tiles;};
    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }


}
