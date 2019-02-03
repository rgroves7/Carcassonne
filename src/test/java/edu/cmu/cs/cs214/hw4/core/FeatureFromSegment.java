package edu.cmu.cs.cs214.hw4.core;

import org.junit.Test;

public class FeatureFromSegment {
    @Test
    public void newRoad() {
        Segment curvedroad = new Segment (SegType.road,
                new int[][] {{0, 0, 0}, {0, 1, 1}, {0, 1, 0}});
        Segment shortroad = new Segment (SegType.intersection,
                new int[][] {{0, 0, 0}, {0, 0, 0}, {0, 1, 0}});
        SegmentFeature r = curvedroad.newFeature(0, 0);
        SegmentFeature r2 = shortroad.newFeature(1, 0);
        assert (r instanceof Road);
        assert(!(r instanceof City));
        assert(!(r.checkComplete()));
        assert(r.size() == 1);

        assert (r2 instanceof Road);
        assert (!(r2 instanceof City));
        assert(!(r2.checkComplete()));
        assert(r2.size() == 1);
    }

    @Test
    public void newCity() {
        Segment longcity = new Segment (SegType.pennant,
                new int[][] {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}});
        Segment cityedge = new Segment (SegType.city,
                new int[][] {{0, 1, 0}, {0, 0, 0}, {0, 0, 0}});
        SegmentFeature lc = longcity.newFeature(0, 0);
        SegmentFeature ce = cityedge.newFeature(1, 0);

        assert (lc instanceof City);
        assert(!(lc instanceof Field));
        assert(!(lc.checkComplete()));
        assert(lc.size() == 1);

        assert (ce instanceof City);
        assert (!(ce instanceof Road));
        assert(!(ce.checkComplete()));
        assert(ce.size() == 1);
    }


    @Test
    public void newCloister() {
        Segment c = new Segment (SegType.cloister,
                new int[][] {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}});
        Cloister cf = new Cloister(0, 1, c, 4);

        assert(!(cf.checkComplete()));
        assert (cf.size() == 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalCloisterSize() {
        Segment c = new Segment (SegType.cloister,
                new int[][] {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}});
        Cloister badc = new Cloister(0,1, c, 10);
    }


}
