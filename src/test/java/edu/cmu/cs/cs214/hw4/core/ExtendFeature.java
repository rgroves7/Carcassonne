package edu.cmu.cs.cs214.hw4.core;

import org.junit.Test;

public class ExtendFeature {
    private Segment straightroad = new Segment (SegType.road,
            new int[][] {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}});
    private Segment curvedroad = new Segment (SegType.road,
            new int[][] {{0, 0, 0}, {0, 1, 1}, {0, 1, 0}});
    private Segment shortroad = new Segment (SegType.intersection,
            new int[][] {{0, 0, 0}, {0, 0, 0}, {0, 1, 0}});


    private Segment longcity = new Segment (SegType.pennant,
            new int[][] {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}});
    private Segment cityedge = new Segment (SegType.city,
            new int[][] {{0, 1, 0}, {0, 0, 0}, {0, 0, 0}});


    @Test
    public void extend() {
        SegmentFeature f = new Road (1, 1, straightroad);
        f.extendSegment(1, 0, curvedroad);
        assert(f.size() == 2);
        assert(!(f.checkComplete()));
    }

    @Test (expected = AssertionError.class)
    public void wrongPlacement() {
        SegmentFeature f = longcity.newFeature(1, 1);
        f.extendSegment(0, 1, cityedge);
    }

    @Test (expected = AssertionError.class)
    public void wrongType() {
        SegmentFeature f = longcity.newFeature(1, 1);
        f.extendSegment(1, 0, shortroad);
    }

}
