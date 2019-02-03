package edu.cmu.cs.cs214.hw4.core;

import org.junit.Test;

public class SegmentTouchingWithinTile {

    @Test
    public void touching() {
        Segment cornerfield = new Segment (SegType.field,
                new int[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 1}});
        Segment diagfield = new Segment (SegType.field,
                new int[][] {{0, 0, 1}, {0, 1, 0}, {1, 0, 0}});
        Segment diagcity = new Segment (SegType.city,
                new int[][] {{1, 1, 0}, {1, 0, 0}, {0, 0, 0}});
        assert(diagcity.touchesWithinTile(diagcity));
        assert(!(cornerfield.touchesWithinTile(diagcity)));
    }
}
