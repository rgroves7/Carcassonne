package edu.cmu.cs.cs214.hw4.core;

import org.junit.Test;

public class PlaceFollower {
    private TestTiles T = new TestTiles();
    private TestTileBean ts = T.getTiles();

    @Test
    public void onFeature() {
        Segment straightroad = new Segment (SegType.road,
                new int[][] {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}});
        Feature r = straightroad.newFeature(0, 1);
        Player j = new Player("Jimmy");
        Player k = new Player("Kim");
        assert(j.placeFollower(0, 1, r));
        assert(!(k.placeFollower(0, 1, r)));
    }

    @Test
    public void onCloister() {
        Segment c1 = new Segment (SegType.cloister,
                new int[][] {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}});
        Cloister c = new Cloister(0, 1, c1, 2);
        Player j = new Player("Jimmy");
        Player k = new Player("Kim");
        assert(j.placeFollower(0, 1, c));
        assert(!(k.placeFollower(0, 1, c)));
    }

    @Test
    public void updateScore() {
        Segment cityedgeright = new Segment (SegType.city,
                new int[][] {{0, 0, 0}, {0, 0, 1}, {0, 0, 0}});
        Segment cityedgeleft = new Segment (SegType.city,
                new int[][] {{0, 0, 0}, {1, 0, 0}, {0, 0, 0}});
        SegmentFeature c = cityedgeright.newFeature(0, 0);
        Player dale = new Player("Dale");
        dale.placeFollower(0, 0, c);
        c.extendSegment(1, 0, cityedgeleft);
        System.out.print(c.toString());
        System.out.print(dale.score());
        assert(dale.score() == 4);
    }
}
