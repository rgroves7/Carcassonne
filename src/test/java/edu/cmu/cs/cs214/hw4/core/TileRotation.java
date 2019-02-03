package edu.cmu.cs.cs214.hw4.core;

import org.junit.Test;

public class TileRotation {
    @Test
    public void testRotate() {
        GameofCarcassonne game = new GameofCarcassonne(new String[] {"Cody", "Zach", "London"});
        Tile t = game.currentTile();
        System.out.print(t.toString());
        t.rotate();
        System.out.print(t.toString());
        t.rotate();
        System.out.print(t.toString());
        t.rotate();
        System.out.print(t.toString());
        t.rotate();
        System.out.print(t.toString());
    }

    @Test
    public void rotateCopy() {
        TestTiles T = new TestTiles();
        TestTileBean ts = T.getTiles();
        Tile A = ts.getA();
        Tile A2 = A.TileCopy();

        System.out.print(A.toString());
        System.out.print(A.toString());

        A.rotate();
        System.out.print(A.toString());
        System.out.print(A2.toString());
    }
}
