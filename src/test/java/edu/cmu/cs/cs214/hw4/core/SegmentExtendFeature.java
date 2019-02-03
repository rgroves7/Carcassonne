package edu.cmu.cs.cs214.hw4.core;

import org.junit.Test;

import java.util.ArrayList;

public class SegmentExtendFeature {
    private Segment straightroad = new Segment (SegType.road,
            new int[][] {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}});
    private Segment curvedroad = new Segment (SegType.road,
            new int[][] {{0, 0, 0}, {0, 1, 1}, {0, 1, 0}});
    private Segment shortroad = new Segment (SegType.intersection,
            new int[][] {{0, 0, 0}, {0, 0, 0}, {0, 1, 0}});

    private Segment cornerfield = new Segment (SegType.field,
            new int[][] {{1, 0, 0}, {0, 0, 0}, {0, 0, 0}});
    private Segment diagfield = new Segment (SegType.field,
            new int[][] {{0, 0, 1}, {0, 1, 0}, {1, 0, 0}});

    private Segment longcity = new Segment (SegType.pennant,
            new int[][] {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}});
    private Segment cityedge = new Segment (SegType.city,
            new int[][] {{0, 1, 0}, {0, 0, 0}, {0, 0, 0}});
    private Segment diagcity = new Segment (SegType.city,
            new int[][] {{1, 1, 0}, {1, 0, 0}, {0, 0, 0}});

    @Test
    public void checkExtendRoad() {
        SegmentFeature f = straightroad.newFeature(1, 1);
        assert(f.checkExtendSegment(1, 0, curvedroad));
        assert(!(f.checkExtendSegment(1, 2, curvedroad)));

        curvedroad.rotate();
        curvedroad.rotate();
        assert(f.checkExtendSegment(1, 2, curvedroad));
        assert(!(f.checkExtendSegment(1, 0, curvedroad)));

        assert(f.checkExtendSegment(1, 0, shortroad));
        assert(!(f.checkExtendSegment(0, 1, shortroad)));

        assert(!(f.checkExtendSegment(0, 1, longcity)));
    }

    @Test
    public void checkExtendCity() {
        SegmentFeature c = longcity.newFeature(1, 1);

        assert(!(c.checkExtendSegment(1, 0, cityedge)));
        assert(c.checkExtendSegment(1, 2, cityedge));
        assert(c.checkExtendSegment(1, 2, diagcity));
        assert(!(c.checkExtendSegment(2, 1, diagcity)));
        assert(!(c.checkExtendSegment(1, 0, diagcity)));

        diagcity.rotate();
        diagcity.rotate();
        assert(c.checkExtendSegment(1, 0, diagcity));

        assert(!(c.checkExtendSegment(1, 0, shortroad)));
    }

    @Test
    public void checkExtendField() {
        SegmentFeature f = new Field (1, 1, cornerfield, new ArrayList<SegmentFeature>());

        assert (f.checkExtendSegment(0, 1, diagfield));
        assert(!(f.checkExtendSegment(1, 2, diagfield)));
        assert(f.checkExtendSegment(1, 0, diagfield));

        assert(!(f.checkExtendSegment(1, 0, diagcity)));
    }

}
