package edu.cmu.cs.cs214.hw4.core;

import org.junit.Test;

public class SegmentExtendsMethods {

    @Test
    public void extendRoads() {
        Segment straightroad = new Segment (SegType.road,
                new int[][] {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}});
        Segment curvedroad = new Segment (SegType.road,
                new int[][] {{0, 0, 0}, {0, 1, 1}, {0, 1, 0}});
        Segment shortroad = new Segment (SegType.intersection,
                new int[][] {{0, 0, 0}, {0, 0, 0}, {0, 1, 0}});

        assert(straightroad.extendsBelow(curvedroad));
        assert(!(straightroad.extendsLeft(curvedroad)));
        assert(!(straightroad.extendsAbove(curvedroad)));
        assert(!(straightroad.extendsRight(curvedroad)));

        assert(shortroad.extendsAbove(straightroad));

        straightroad.rotate();

        assert(straightroad.extendsRight(curvedroad));
        assert(!(straightroad.extendsLeft(curvedroad)));
        assert(!(straightroad.extendsAbove(curvedroad)));
        assert(!(straightroad.extendsBelow(curvedroad)));

        shortroad.rotate();
        shortroad.rotate();

        assert(shortroad.extendsBelow(curvedroad));
    }

    @Test
    public void extendFields() {
        Segment cornerfield = new Segment (SegType.field,
                new int[][] {{1, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        Segment diagfield = new Segment (SegType.field,
                new int[][] {{0, 0, 1}, {0, 1, 0}, {1, 0, 0}});

        assert(cornerfield.extendsRight(diagfield));
        assert(cornerfield.extendsBelow(diagfield));
        assert(!(cornerfield.extendsAbove(diagfield)));
        assert(!(cornerfield.extendsLeft(diagfield)));

        diagfield.rotate();

        assert(!(cornerfield.extendsRight(diagfield)));
        assert(!(cornerfield.extendsBelow(diagfield)));
        assert(!(cornerfield.extendsAbove(diagfield)));
        assert(!(cornerfield.extendsLeft(diagfield)));

        cornerfield.rotate();

        assert(cornerfield.extendsBelow(diagfield));
        assert(cornerfield.extendsLeft(diagfield));
        assert(!(cornerfield.extendsRight(diagfield)));
        assert(!(cornerfield.extendsAbove(diagfield)));
    }

    @Test
    public void extendCities() {
        Segment longcity = new Segment (SegType.pennant,
                new int[][] {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}});
        Segment cityedge = new Segment (SegType.city,
                new int[][] {{0, 1, 0}, {0, 0, 0}, {0, 0, 0}});
        Segment diagcity = new Segment (SegType.city,
                new int[][] {{1, 1, 0}, {1, 0, 0}, {0, 0, 0}});

        assert(cityedge.extendsBelow(longcity));
        assert(!(cityedge.extendsAbove(longcity)));
        assert(!(cityedge.extendsRight(longcity)));
        assert(!(cityedge.extendsLeft(longcity)));

        assert(longcity.extendsAbove(diagcity));
        assert(!(longcity.extendsBelow(diagcity)));
        assert(!(longcity.extendsRight(diagcity)));
        assert(!(longcity.extendsLeft(diagcity)));

        longcity.rotate();

        assert(!(cityedge.extendsBelow(longcity)));
        assert(!(cityedge.extendsAbove(longcity)));
        assert(!(cityedge.extendsRight(longcity)));
        assert(!(cityedge.extendsLeft(longcity)));

        assert(longcity.extendsLeft(diagcity));
        assert(!(longcity.extendsBelow(diagcity)));
        assert(!(longcity.extendsAbove(diagcity)));
        assert(!(longcity.extendsRight(diagcity)));
    }

    @Test
    public void extendCloisters() {
        Segment c1 = new Segment (SegType.cloister,
                new int[][] {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}});
        Segment c2 = new Segment (SegType.cloister,
                new int[][] {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}});

        assert(!(c1.extendsRight(c2)));
        assert(!(c1.extendsAbove(c2)));
        assert(!(c1.extendsBelow(c2)));
        assert(!(c1.extendsLeft(c2)));
    }

    @Test
    public void differentTypes() {
        Segment straightroad = new Segment (SegType.road,
                new int[][] {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}});
        Segment diagcity = new Segment (SegType.city,
                new int[][] {{1, 1, 0}, {1, 0, 0}, {0, 0, 0}});
        Segment edgefield = new Segment (SegType.field,
                new int[][] {{0, 0, 1}, {0, 0, 1}, {0, 0, 1}});
        assert(!(straightroad.extendsAbove(diagcity)));
        assert(!(edgefield.extendsLeft(diagcity)));
        straightroad.rotate();
        assert(!(straightroad.extendsRight(edgefield)));


    }
}
