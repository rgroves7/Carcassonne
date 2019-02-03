package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.*;
import org.yaml.snakeyaml.Yaml;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class CarcassonneGui extends JPanel implements GameChangeListener {
    private final GameofCarcassonne game;

    private int turnIndex = 0;

    private final Color[] playerColors = new Color[]
        {Color.red, Color.blue, Color.orange, Color.green, Color.black};

    private final TileImages images;
    private final TileImage[][] boardImages;

    private final JLabel currentPlayerLabel;
    private final JLabel currentTileLabel;
    private TileImage currentTileImage;

    private final JButton rotateButton;

    private final JButton nextTurnButton;

    private final JLabel placeFollowerLabel;
    //3*3 grid of buttons that allow player to place a follower on the
    //tile they just played at that location
    private final JPanel placeFollowerLocations;

    private final JLabel[] PlayerScoreFollowerLabels;

    private JButton[][] tiles;

    private final int initialWidth = 9;
    private int currentWidth = 9;

    private final String WHITE_IMG = "src/main/resources/white.png";
    private final Icon noTile;

    {
        try {
            noTile = new ImageIcon(ImageIO.read(
                        new File(WHITE_IMG)).getSubimage(0, 0, 90, 90));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error when reading " + WHITE_IMG + "!");
        }
    }

    public CarcassonneGui(GameofCarcassonne g) {
        game = g;
        images = parse ("src/main/resources/TileImages.yml");

        boardImages = new TileImage[game.boardWidth()][game.boardWidth()];

        currentPlayerLabel = new JLabel();
        //Current player label shows the first player's name
        currentPlayerLabel.setText(game.currentPlayer().name() + "'s turn");

        currentTileLabel = new JLabel();
        //Current tile label shows the tile at the top of the deck
        currentTileImage = images.getTileImage(game.currentTile().getName()).copyTileImage();
        currentTileLabel.setIcon(new ImageIcon(currentTileImage.getImage()));

        rotateButton = new JButton();
        //Rotate button allows the player to rotate the tile before they
        //place it
        rotateButton.setText("rotate");
        rotateButton.addActionListener(e -> {
            game.rotateCurrentTile();
        });

        //Place follower label doesn't initially say anything, because
        //the first player can't place a follower until they place their
        //tile
        placeFollowerLabel = new JLabel();
        placeFollowerLocations = new JPanel();
        placeFollowerLocations.setMaximumSize(new Dimension(90, 90));
        placeFollowerLocations.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton b = new JButton();
                b.setMaximumSize(new Dimension(30, 30));
                int tileX = j;
                int tileY = i;
                b.addActionListener(e -> game.placeFollower(tileX, tileY));
                placeFollowerLocations.add(b);
            }
        }

        nextTurnButton = new JButton();
        //Next turn button doesn't initially say anything because the player
        //must first place their tile
        nextTurnButton.setText("");
        nextTurnButton.addActionListener(e -> {
            game.nextTurn();
        });

        //For each player, label should indicate at the beginning that their
        //score is 0 and they have 7 remaining followers
        PlayerScoreFollowerLabels = new JLabel[g.numPlayers()];
        for (int i = 0; i < game.numPlayers(); i++) {
            Player p = game.playerOrder()[i];
            Color c = playerColors[i];
            String text = "<html>" + p.name() + "<br/>" +
                    "score: " + p.score() + "<br/>" +
                    "remaining followers: " + p.getNumFollowers() +
                    "<br/>" + "<html>";
            PlayerScoreFollowerLabels[i] = new JLabel();
            PlayerScoreFollowerLabels[i].setForeground(c);
            PlayerScoreFollowerLabels[i].setText(text);
        }

        tiles = new JButton[g.boardWidth()][g.boardWidth()];

        setLayout(new BorderLayout());
        JPanel turnInformation = new JPanel();
        turnInformation.setLayout(new BoxLayout(turnInformation, BoxLayout.Y_AXIS));
        turnInformation.add(currentPlayerLabel);
        turnInformation.add(currentTileLabel);
        turnInformation.add(rotateButton);
        turnInformation.add(nextTurnButton);
        turnInformation.add(placeFollowerLabel);
        turnInformation.add(placeFollowerLocations);
        for (JLabel j: PlayerScoreFollowerLabels) {
            turnInformation.add(j);
        }
        add(turnInformation, BorderLayout.EAST);
        add(createBoard(initialWidth), BorderLayout.CENTER);
    }

    private static TileImages parse(String fileName) {
        Yaml yaml = new Yaml();
        try (InputStream is = new FileInputStream(fileName)) {
            return yaml.loadAs(is, TileImages.class);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File " + fileName + " not found!");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error when reading " + fileName + "!");
        }
    }

    /**
     * Creates a board of buttons with specified width containing all the
     * tiles that have been placed thus far
     * @param width width and height of the board
     * @return JScrollPane containing width*width buttons
     */
    private JScrollPane createBoard(int width) {
        JPanel board = new JPanel (new GridLayout(width, width));
        board.setLayout(new GridLayout(width, width));
        for(int r = 0; r < width; r++) {
            for(int c = 0; c < width; c++) {
                int boardRow = ((game.boardWidth() - 1)/2) - ((width - 1)/2) + r;
                int boardCol = ((game.boardWidth() - 1)/2) - ((width - 1)/2) + c;
                if (tiles[boardRow][boardCol] == null) {
                    JButton button = new JButton();
                    button.setPreferredSize(new Dimension(90, 90));
                    tiles[boardRow][boardCol] = button;
                    if (game.getTile(boardCol, boardRow) != null) {
                        TileImage i = images.getTileImage
                                (game.getTile(boardCol, boardRow).getName()).copyTileImage();
                        assert (i != null);
                        button.setIcon(new ImageIcon(i.getImage()));
                    }
                    button.addActionListener(e -> {
                        game.placeTile(boardCol, boardRow);
                    });
                    board.add(button);
                }
                else {
                    board.add(tiles[boardRow][boardCol]);
                }
            }
        }
        return new JScrollPane(board);
    }

    /**
     * The current tile has been placed at position x, y
     * @param col x coordinate of tile location
     * @param row y coordinate of tile location
     */
    public void tilePlaced(int col, int row) {
        BufferedImage i = currentTileImage.getImage();
        //Convert the row and column on the game board to the row and column with respect
        //to the current width of the tile buttons, and set that buttons icon to contain
        //the current tile image
        int windowRow = row - ((game.boardWidth() - 1)/2) + ((currentWidth - 1)/2);
        int windowCol = col - ((game.boardWidth() - 1)/2) + ((currentWidth - 1)/2);
        assert(windowRow >= 0 && windowRow < currentWidth);
        assert(windowCol >= 0 && windowCol < currentWidth);
        tiles[row][col].setIcon(new ImageIcon(i));
        boardImages[row][col] = currentTileImage;

        //If the tile is placed at the edge of the board, increase the board's width by 2
        if (windowRow == 0 || windowRow == currentWidth - 1 ||
                windowCol == 0 || windowCol == currentWidth - 1)
        {
            BorderLayout layout = (BorderLayout) getLayout();
            remove(layout.getLayoutComponent(BorderLayout.CENTER));
            add(createBoard(currentWidth + 2), BorderLayout.CENTER);
            currentWidth = currentWidth + 2;
        }

        //The player may now finish their turn or attempt to place a follower
        nextTurnButton.setText("Next turn");
        placeFollowerLabel.setText("<html>To place a follower, <br/> select where on " +
                "the tile <br/> you'd like to place it <html>");
        //Clear the current tile label to indicate that the tile has been placed
        currentTileLabel.setIcon(noTile);
        //Clear the rotate button to indicate that the player can no longer rotate the tile
        rotateButton.setText("");
    }

    /**
     * The current tile has been rotated
     */
    public void tileRotated() {
        BufferedImage i = currentTileImage.rotateImage();
        assert(i != null);
        currentTileLabel.setIcon(new ImageIcon(i));
    }

    /**
     * The turn has changed
     */
    public void turnChanged() {
        //Update the current tile label and current player label
        currentTileImage = images.getTileImage(game.currentTile().getName()).copyTileImage();
        currentTileLabel.setIcon(new ImageIcon(currentTileImage.getImage()));
        currentPlayerLabel.setText(game.currentPlayer().name() + "'s turn");
        //Clear the next turn and place follower buttons and indicate that the rotate
        //button can now be pressed
        nextTurnButton.setText("");
        rotateButton.setText("rotate");
        placeFollowerLabel.setText("");
        //Update the turn index
        turnIndex = (turnIndex + 1) % game.numPlayers();
    }

    /**
     * A follower has been placed at position x, y on the board and position
     * tileX, tileY on the tile
     * @param col x coordinate on the board
     * @param row y coordinate on the board
     * @param tileX x coordinate on the tile
     * @param tileY y coordinate on the tile
     * @param success true if the placement is successful and false otherwise
     */
    public void followerPlaced(int col, int row, int tileX, int tileY, boolean success) {
        //If a follower can't be placed at the selected location, indicate that in the place follower
        //label
        if (!(success)) placeFollowerLabel.setText("Looks like you can't place a follower there!");
        else {
            //Add a circle at the selected location and update the current players' score and follower
            //label to indicate that they have one fewer follower
            BufferedImage withCircle = currentTileImage.addCircle(tileX, tileY,
                    playerColors[turnIndex]);
            tiles[row][col].setIcon(new ImageIcon(withCircle));
            Player p = game.playerOrder()[turnIndex];
            String text = "<html>" + p.name() + "<br/>" +
                    "score: " + p.score() + "<br/>" +
                    "remaining followers: " + p.getNumFollowers() +
                    "<br/>" + "<html>";
            PlayerScoreFollowerLabels[turnIndex].setText(text);
        }
    }

    /**
     * Player p's score has increased
     * @param p the player whose score has changed
     */
    public void scoreChanged(Player p) {
        //Find the index of the player whose score changed in player
        //order
        int index = -1;
        for (int i = 0; i < game.numPlayers(); i++) {
            if (game.playerOrder()[i] == p) index = i;
        }
        assert(index != -1);
        //Update that player's score and follower label
        String text = "<html>" + p.name() + "<br/>" +
                "score: " + p.score() + "<br/>" +
                "remaining followers: " + p.getNumFollowers() +
                "<br/>" + "<html>";
        PlayerScoreFollowerLabels[index].setText(text);
    }

    /**
     * A follower belonging to player p has been returned at location x, y
     * @param col x coordinate of returned follower
     * @param row y coordinate of returned follower
     * @param p player whose follower has been returned
     */
    public void followerReturned(int col, int row, Player p) {
        //Find the index of the player whose follower was returned
        int index = -1;
        for (int i = 0; i < game.numPlayers(); i++) {
            if (game.playerOrder()[i] == p) index = i;
        }
        assert(index != -1);
        tiles[row][col].setIcon(new ImageIcon(boardImages[row][col].getImage()));
        //Update the player's score and follower label to indicate that they
        //have one more follower
        String text = "<html>" + p.name() + "<br/>" +
                "score: " + p.score() + "<br/>" +
                "remaining followers: " + p.getNumFollowers() +
                "<br/>" + "<html>";
        PlayerScoreFollowerLabels[index].setText(text);
    }

    /**
     * The game is over
     * @param winner the player who won the game. null if there is a tie
     */
    public void gameEnded(Player winner) {
        JFrame frame = (JFrame) SwingUtilities.getRoot(this);
        String finalscores = "";
        //Display all of the players' final scores
        for (Player p : game.playerOrder()) {
            finalscores = finalscores + p.name() + ": " + p.score() + "   ";
        }
        //Display the name of the winners, or state if it's a tie
        if(winner == null) {
            JOptionPane.showMessageDialog(frame,finalscores, "Tie!",JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, finalscores,winner.name() + " wins!",JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
