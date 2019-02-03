package edu.cmu.cs.cs214.hw4.core;

import org.junit.Test;

public class TileEdgeGetters {
    private TestTiles T = new TestTiles();
    private TestTileBean ts = T.getTiles();

    @Test
    public void edgeGetters() {
        Tile O = ts.getO();
        Tile D = ts.getD();

        assert(O.getTop() == EdgeType.CITY);
        assert(O.getRight() == EdgeType.ROAD);
        assert(O.getBottom() == EdgeType.ROAD);
        assert(O.getLeft() == EdgeType.CITY);

        assert(D.getTop() == EdgeType.ROAD);
        assert(D.getBottom() == EdgeType.ROAD);
        assert(D.getRight() == EdgeType.CITY);
        assert(D.getLeft() == EdgeType.FIELD);

        O.rotate();
        O.rotate();

        assert(O.getTop() == EdgeType.ROAD);
        assert(O.getRight() == EdgeType.CITY);
        assert(O.getBottom() == EdgeType.CITY);
        assert(O.getLeft() == EdgeType.ROAD);

        D.rotate();
        assert(D.getTop() == EdgeType.FIELD);
        assert(D.getBottom() == EdgeType.CITY);
        assert(D.getRight() == EdgeType.ROAD);
        assert(D.getLeft() == EdgeType.ROAD);
    }

}
