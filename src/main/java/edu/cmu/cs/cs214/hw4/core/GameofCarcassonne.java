package edu.cmu.cs.cs214.hw4.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.yaml.snakeyaml.Yaml;

public class GameofCarcassonne {
    private final Tile[] deck;
    private final int deckSize = 71;
    private Board B;
    private final Player[] playerOrder;
    private final int numPlayers;
    private int turnPlayer;
    private int turnTile;
    private boolean currentTilePlaced = false;
    private int lastPlacedX;
    private int lasPlacedY;
    private HashSet<Feature> PlaceableFeatures = new HashSet<>();
    private boolean inPlay = true;

    private final List<GameChangeListener> listeners = new ArrayList<>();

    /**
     * Initializes a game with a shuffled deck, a board with the starting tile
     * placed, and the playerOrder determined by an array of player names.
     * @param players is the names of the players
     */
    public GameofCarcassonne(String[] players) {
        deck = new Tile[deckSize];
        DeckBean deckInstructions = parse("src/main/resources/Deck.yml");
        B = new Board(deckInstructions.getStartingtile());
        int i = 0;
        for (Tile tb : deckInstructions.getTiles()) {
            for (int j = 0; j < tb.getQuantity(); j++) {
                assert(i < deckSize);
                deck[i] = tb.TileCopy();
                i++;
            }
        }
        shuffle();
        assert(i == deckSize);

        numPlayers = players.length;
        playerOrder = new Player[numPlayers];
        for (int p = 0; p < numPlayers; p++) {
            playerOrder[p] = new Player (players[p]);
        }
        turnPlayer = 0;
        turnTile = 0;
    }

    public void addGameChangeListener(GameChangeListener l) {
        listeners.add(l);
        for (Player p : playerOrder) {
            p.addGameChangeListener(l);
        }
    }

    /**
     * Parses instructions for the deck from a yml file
     * @param fileName is the file containing the deck instructions
     * @return the deck instructions
     */
    private static DeckBean parse(String fileName) {
        Yaml yaml = new Yaml();
        try (InputStream is = new FileInputStream(fileName)) {
            return yaml.loadAs(is, DeckBean.class);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File " + fileName + " not found!");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error when reading " + fileName + "!");
        }
    }

    /**
     * Randomly shuffles the deck.
     */
    private void shuffle() {
        Tile[] newDeck = new Tile[deckSize];
        Random random = new Random();
        for (int i = 0; i < deckSize; i++) {
            int newI = random.nextInt(deckSize);
            Tile temp = deck[i];
            deck[i] = deck[newI];
            deck[newI] = temp;
        }
    }

    /**
     * Number of players in the game
     * @return number of players
     */
    public int numPlayers() {return playerOrder.length;}

    /**
     * Array of players representing the player order
     * @return playerOrder
     */
    public Player[] playerOrder() {return playerOrder;}

    /**
     * Maximum width of the board
     * @return board width
     */
    public int boardWidth() {return B.boardWidth;}

    /**
     * Tile at location x, y
     * @param x x coordinate of location
     * @param y y coordinate of location
     * @return tile at x, y
     */
    public Tile getTile(int x, int y) {
        return B.getTile(x, y);
    }

    /**
     * Returns the name of the player whose turn it is
     * @return the name player whose turn it is
     */
    public Player currentPlayer() {return playerOrder[turnPlayer];}

    /**
     * Returns the tile that the player is responsible for placing
     * @return the current tile
     */
    public Tile currentTile() {
        if (inPlay) return deck[turnTile];
        return null;
    }

    /**
     * Selects tile from the deck until one is drawn that has a valid
     * placement on the board, and returns that tile
     */
    private void drawTile() {
        turnTile ++;
        while (turnTile < deckSize && !(B.isLegalPlacement(currentTile()))) {
            turnTile++;
        }
        currentTilePlaced = false;
        if (turnTile == deckSize) {
            B.endgame();
            for (GameChangeListener l : listeners) {
                l.gameEnded(highestScoringPlayer());
            }
            inPlay = false;
        }
    }

    /**
     * Draws a new tile and moves to the next player
     */
    public void nextTurn() {
        if (currentTilePlaced) drawTile();
        if (inPlay) {
            turnPlayer = (turnPlayer + 1) % numPlayers;
            for (GameChangeListener l : listeners) {
                l.turnChanged();
            }
        }
    }

    /**
     * Rotates the current tile if the current tile hasn't been placed
     * yet
     */
    public void rotateCurrentTile() {
        if (!(currentTilePlaced) && inPlay) {
            currentTile().rotate();
            for (GameChangeListener l : listeners) {
                l.tileRotated();
            }
        }
    }

    /**
     * If legal, places the current tile at location x, y and sets placeableFeatures
     * as the features created or extended by the tile that currently have
     * no followers
     * @param x x coordinate of tile location
     * @param y y coordinate of tile location
     * @return true if the placement was successful
     */
    public boolean placeTile(int x, int y) {
        if (currentTilePlaced || !(inPlay)) return false;
        try {
            PlaceableFeatures = B.placeTile(x, y, currentTile());
            for (GameChangeListener l : listeners) {
                l.tilePlaced(x, y);
            }
            currentTilePlaced = true;
            lastPlacedX = x;
            lasPlacedY = y;
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * If the current tile has been placed, places a follower of the
     * current player on the current tile at tile location tileX, tileY
     * @param tileX x coordinate on tile
     * @param tileY y coordinate on tile
     * @return true if the placement was successful
     */
    public boolean placeFollower(int tileX, int tileY) {
        if (!(currentTilePlaced) || !(inPlay)) return false;
        Feature f = B.featureAtLocation(lastPlacedX, lasPlacedY, tileX, tileY);
        if (PlaceableFeatures.contains(f)) {
            if (!(currentPlayer().placeFollower(lastPlacedX, lasPlacedY, f))) return false;
            for (GameChangeListener l : listeners) {
                l.followerPlaced(lastPlacedX, lasPlacedY, tileX, tileY, true);
            }
            f.checkComplete();
            nextTurn();
            return true;
        }
        for (GameChangeListener l : listeners) {
            l.followerPlaced(lastPlacedX, lasPlacedY, tileX, tileY, false);
        }
        return false;
    }

    /**
     * The player with the highest score, or null if there's a tie
     * @return player with the highest score
     */
    private Player highestScoringPlayer() {
        assert(!(inPlay));
        int maxScore = -1;
        Player winner = null;
        for (Player p : playerOrder) {
            if (p.score() > maxScore) {
                winner = p;
                maxScore = p.score();
            }
            else if (p.score() == maxScore) return null;
        }
        return winner;
    }


    /**
     * Represents the deck as a string with the tile names in order and dashes
     * around the tile being played on the current turn
     * @return the deck as a string
     */
    private String deckToString() {
        String s = "";
        for (int i = 0; i < deckSize; i++) {
            if (i == turnTile) s = s + "- " + deck[i].getName() + " - ";
            else if (deck[i] != null) s = s + deck[i].getName() + " ";
        }
        return s;
    }

    /**
     * Represents the players as strings with their names in turn order and dashes
     * around the player whose turn it is
     * @return players as a string
     */
    private String playersToString() {
        String s = "";
        for (int p = 0; p < numPlayers; p++) {
            if (p == turnPlayer) s = s + "- " + playerOrder[p].toString() + "- ";
            else s = s + playerOrder[p].toString() + " ";
        }
        return s;
    }

    String turnToString() {
        return "\n Deck: " + deckToString() + " " + playersToString();
    }
    /**
     * Represents the current state of the game as a string
     * @return a string representing the current state of the game
     */
    @Override
    public String toString() {
        return "Board: \n" + B.toString() + turnToString();
    }

}
