package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.GameofCarcassonne;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> {
            showBoard(args);
        });
    }

    private static void showBoard(String[] players) {
        GameofCarcassonne game = new GameofCarcassonne(players);
        CarcassonneGui gameGUI = new CarcassonneGui(game);
        JFrame frame = new JFrame();
        game.addGameChangeListener(gameGUI);
        frame.setContentPane(gameGUI);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();

        frame.setVisible(true);
    }
}
