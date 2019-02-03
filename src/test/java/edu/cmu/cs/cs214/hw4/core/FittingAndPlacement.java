package edu.cmu.cs.cs214.hw4.core;

import org.junit.Test;

public class FittingAndPlacement {
    private TestTiles T = new TestTiles();
    private TestTileBean ts = T.getTiles();
    private Tile st = ts.getD();
    private int xo = 73;
    private int yo = 73;

    @Test
    public void fits() {
        Board b = new Board(st);
        Tile C = ts.getC();
        assert(!(b.fits(xo-1, yo, C)));
        assert(b.fits(xo+1, yo, C));
        b.placeTile(xo+1, yo, C);

        Tile J = ts.getJ();
        Tile I = ts.getI();

        assert(b.fits(xo+1, yo-1, I));
        assert(b.fits(xo, yo-1, J));

        b.placeTile(xo, yo-1, J);
        assert(!(b.fits(xo+1, yo-1, I)));

        Tile D = st.TileCopy();
        D.rotate();

        assert(b.fits(xo+1, yo-1, D));
        assert(b.fits(xo, yo-2, D));
        assert(!(b.fits(xo-1, yo, D)));

        b.placeTile(xo+1, yo-1, D);

        Tile A = ts.getA();
        A.rotate();
        A.rotate();

        assert(b.fits(xo, yo+1, A));
        assert(b.fits(xo+1, yo-2, A));
        assert(!(b.fits(xo+2, yo, A)));

        b.placeTile(xo, yo+1, A);

        System.out.print(b.toString());
    }

    @Test
    public void fitsScenario2() {
        Board b = new Board(st);

        Tile I = ts.getI().TileCopy();
        I.rotate();

        Tile E = ts.getE().TileCopy();
        Tile K = ts.getK().TileCopy();
        Tile T = ts.getT().TileCopy();
        Tile H = ts.getH().TileCopy();

        b.placeTile(xo+1, yo, I);
        b.placeTile(xo+1, yo+1, E);
        b.placeTile(xo, yo-1, T);
        b.placeTile(xo-1, yo-1, K);
        b.placeTile(xo+1, yo-1, H);
        System.out.print(b.toString());

    }

    @Test(expected = IllegalStateException.class)
    public void placeWithoutFit() {
        Board b = new Board(st);
        b.placeTile(xo+1, yo, ts.getA());
    }

    @Test
    public void isLegalPlacement() {
        Board b = new Board(st);
        Tile J = ts.getJ().TileCopy();
        J.rotate();
        J.rotate();
        J.rotate();
        b.placeTile(xo+1, yo, J);
        Tile C = ts.getC();
        assert(!(b.isLegalPlacement(C)));
        assert(b.isLegalPlacement(J));
        assert(b.isLegalPlacement(st));
    }
}
