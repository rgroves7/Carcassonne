package edu.cmu.cs.cs214.hw4.core;

import org.junit.Test;

public class FeatureCompletion {
    private TestTiles T = new TestTiles();
    private TestTileBean ts = T.getTiles();
    private int xo = 73;
    private int yo = 73;

    @Test
    public void completeRoad() {
        Board b1 = new Board(ts.getL());
        Tile J1 = ts.getJ().TileCopy();
        Tile J2 = ts.getJ().TileCopy();
        Tile P = ts.getP();

        J1.rotate();
        b1.placeTile(xo, yo-1, J1);

        J2.rotate();
        J2.rotate();
        J2.rotate();
        b1.placeTile(xo-1, yo, J2);

        b1.placeTile(xo-1, yo-1, P);

        // Looped road size 4
        System.out.print(b1.toString());

        Board b2 = new Board(ts.getD());
        Tile T = ts.getT();
        Tile J = ts.getJ().TileCopy();
        J.rotate();
        J.rotate();
        J.rotate();
        Tile L = ts.getL();
        b2.placeTile(xo, yo-1, T);
        b2.placeTile(xo, yo+1, J);
        b2.placeTile(xo+1, yo+1, L);

        // Road from city to intersection
        System.out.print(b2.toString());

        Board b3 = new Board(ts.getD());
        Tile V1 = ts.getV().TileCopy();
        J = ts.getJ().TileCopy();
        Tile U = ts.getU().TileCopy();
        P = ts.getP().TileCopy();
        P.rotate();
        P.rotate();
        P.rotate();
        Tile V2 = ts.getV().TileCopy();
        V2.rotate();

        b3.placeTile(xo, yo+1, V2);
        b3.placeTile(xo-1, yo+1, P);
        b3.placeTile(xo-1, yo, U);
        b3.placeTile(xo-1, yo-1, J);
        b3.placeTile(xo, yo-1, V1);

        // Looped road with no intersections
        System.out.print(b3.toString());

    }

    @Test
    public void completedCity() {
        Tile T = ts.getT().TileCopy();
        Tile I = ts.getI().TileCopy();
        Tile K = ts.getK().TileCopy();
        Tile M1 = ts.getM().TileCopy();
        Tile M2 = ts.getM().TileCopy();
        Tile V = ts.getV().TileCopy();

        T.rotate();
        T.rotate();

        K.rotate();
        K.rotate();

        V.rotate();
        V.rotate();

        M1.rotate();

        Board b1 = new Board(T);
        b1.placeTile(xo-1, yo, I);
        b1.placeTile(xo+1, yo, K);
        b1.placeTile(xo, yo+1, M2);

        // Incomplete city size 4
        System.out.print(b1.toString());

        b1.placeTile(xo-1, yo+1, M1);
        b1.placeTile(xo+1, yo+1, V);

        // Complete city size 5
        //4 fields should have score 3, one should have score 0
        System.out.print(b1.toString());

        Tile D = ts.getD().TileCopy();
        D.rotate();
        D.rotate();
        Tile C = ts.getC();
        Tile H = ts.getH();
        H.rotate();
        Tile P = ts.getP();
        P.rotate();
        P.rotate();
        P.rotate();
        I = ts.getI().TileCopy();
        T = ts.getT().TileCopy();
        Tile F = ts.getF();
        Tile E = ts.getE();
        E.rotate();
        Board b2 = new Board(D);
        b2.placeTile(xo-1, yo, C);
        b2.placeTile(xo-1, yo-1, P);
        b2.placeTile(xo-1, yo+1, H);
        b2.placeTile(xo-2, yo, T);
        b2.placeTile(xo-2, yo-1, I);
        b2.placeTile(xo-3, yo, F);
        b2.placeTile(xo-4, yo, E);

        System.out.print(b2.toString());


    }

    @Test
    public void completedCloister() {
        Tile A1 = ts.getA().TileCopy();

        Tile A2 = ts.getA().TileCopy();
        A2.rotate();
        A2.rotate();

        Tile F = ts.getF().TileCopy();
        Tile L = ts.getL().TileCopy();

        Tile V1 = ts.getV().TileCopy();
        V1.rotate();
        V1.rotate();

        Tile D1 = ts.getD().TileCopy();
        D1.rotate();
        D1.rotate();

        Tile P = ts.getP();

        Tile D2 = ts.getD().TileCopy();
        D2.rotate();
        D2.rotate();
        D2.rotate();

        Tile V2 = ts.getV();

        Board b = new Board(A1);

        b.placeTile(xo+1, yo, A2);
        b.placeTile(xo+1, yo+1, F);
        b.placeTile(xo, yo+1, L);
        b.placeTile(xo-1, yo+1, V1);
        b.placeTile(xo-1, yo, D1);
        b.placeTile(xo-1, yo-1, P);
        b.placeTile(xo, yo-1, D2);
        b.placeTile(xo+1, yo-1, V2);

        //One complete cloister, one incomplete cloister of size 6
        //Two complete roads, size 2 and 7
        System.out.print(b.toString());

        //Different tile placement order
        Board b2 = new Board(A2);
        b2.placeTile(xo, yo+1, F);
        b2.placeTile(xo-1, yo+1, L);
        b2.placeTile(xo-2, yo+1, V1);
        b2.placeTile(xo-2, yo, D1);
        b2.placeTile(xo-2, yo-1, P);
        b2.placeTile(xo, yo-1, V2);
        b2.placeTile(xo-1, yo-1, D2);
        b2.placeTile(xo-1, yo, A1);

        System.out.print(b2.toString());
    }
}
