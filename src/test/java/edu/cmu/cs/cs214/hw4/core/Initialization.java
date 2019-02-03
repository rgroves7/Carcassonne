package edu.cmu.cs.cs214.hw4.core;

import org.junit.Test;

public class Initialization {
    private GameofCarcassonne game = new GameofCarcassonne(new String[] {"Gianni", "Donatella", "Allegra", "Santo"});

    @Test
    public void initialGame() {
        System.out.print(game.toString());
    }

    @Test
    public void turnChanging() {
        game.nextTurn();
        System.out.print(game.turnToString());
        game.nextTurn();
        game.nextTurn();
        game.nextTurn();
        System.out.print(game.turnToString());
    }
}
