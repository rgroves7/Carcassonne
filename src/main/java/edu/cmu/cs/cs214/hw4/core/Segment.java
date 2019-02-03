package edu.cmu.cs.cs214.hw4.core;

/**
 * Class representing a segment on a tile
 */
public class Segment {
    /**
     * The type of feature the segment represents
     */
    private SegType feature;
    public SegType getFeature() {return feature;}
    public void setFeature(SegType feature) {
        this.feature = feature;
    }

    /**
     * Coordinates of where on the tile the segment is located, where
     * a one means the segment occupies that coordinate and a zero means
     * it does not
     */
    private int[][] coords;
    public int[][] getCoords() {return coords;}
    public void setCoords(int[][] c) {this.coords = c;}

    public Segment() {}

    /**
     * Creates new segment of type f, which occupies coordinates in the
     * tile specified by c
     * @param f segment type
     * @param c tile coordinates
     */
    public Segment(SegType f, int[][] c) {
        feature = f;
        coords = c;
    }

    /**
     * Returns a copy of this segment with the same
     * feature and coordinates
     * @return copied segment
     */
    Segment SegmentCopy() {
        int[][] c = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                c[i][j] = coords[i][j];
            }
        }
        return new Segment(feature, c);
    }

    /**
     * Rotates segment once clockwise
     */
    void rotate() {
        int[][] newC = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newC[j][2 - i] = coords[i][j];
            }
        }
        coords = newC;
    }

    boolean occupiesLocation(int x, int y) {
        return (coords[y][x] == 1);
    }

    /**
     * Creates new SegmentFeature containing only s at position x, y.
     * @requires s.getFeature() != cloister && s.getFeature() != field
     * @param x new feature x location
     * @param y new feature y location
     * @return new SegmentFeature
     */
    SegmentFeature newFeature(int x, int y) {
        if (feature == SegType.city ||  feature == SegType.pennant) {
            return new City(x, y, this);
        }
        else {
            assert (feature == SegType.intersection || feature == SegType.road);
            return new Road(x, y, this);
        }
    }

    /**
     * Returns true if this segment has the same kind of feature as another segment
     * @param other segment being compared to
     * @return true if segments are the same kind and false otherwise
     */
    private boolean sameKind(Segment other) {
        if (feature == SegType.road || feature == SegType.intersection)
            return (other.feature == SegType.road || other.feature == SegType.intersection);
        if (feature == SegType.pennant || feature == SegType.city)
            return (other.feature == SegType.pennant || other.feature == SegType.city);
        return feature == other.feature;
    }

    /**
     * Supposing the segment is placed below another segment other, returns
     * true if the two segments connect
     * @param other the other segment
     * @return true if the two segments connect
     */
    boolean extendsBelow(Segment other) {
        if (!(sameKind(other))) return false;
        for (int i = 0; i < 3; i++) {
            if (coords[0][i] == 1 && other.coords[2][i] == 1) return true;
        }
        return false;
    }

    /**
     * Supposing the segment is placed above another segment other, returns
     * true if the two segments connect
     * @param other the other segment
     * @return true if the two segments connect
     */
    boolean extendsAbove(Segment other) {
        if (!(sameKind(other))) return false;
        for (int i = 0; i < 3; i++) {
                if (coords[2][i] == 1 && other.coords[0][i] == 1) return true;
        }
        return false;
    }

    /**
     * Supposing the segment is placed to the right of another segment other,
     * returns true if the two segments connect
     * @param other the other segment
     * @return true if the two segments connect
     */
    boolean extendsRight(Segment other) {
        if (!(sameKind(other))) return false;
        for (int i = 0; i < 3; i++) {
                if (coords[i][0] == 1 && other.coords[i][2] == 1) return true;
        }
        return false;
    }

    /**
     * Supposing the segment is placed to the left of another segment other,
     * returns true if the two segments connect
     * @param other the other segment
     * @return true if the two segments connect
     */
    boolean extendsLeft(Segment other) {
        if (!(sameKind(other))) return false;
        for (int i = 0; i < 3; i++) {
                if (coords[i][2] == 1 && other.coords[i][0] == 1) return true;
        }
        return false;
    }

    /**
     * Returns true if there's a 1 in c adjacent to i, j, but not diagonal
     * @param i column
     * @param j row
     * @param c coords
     * @return true if there's an adjacent 1 and false otherwise
     */
    private boolean adjacentCoordIsOne(int i, int j, int[][] c) {
        if (i == 0) {
            if (j == 0) return (c[j+1][i] == 1 || c[j][i+1] == 1);
            if (j == 2) return (c[j-1][i] == 1 || c[j][i+1] == 1);
            return (c[j-1][i] == 1 || c[j][i+1] == 1 || c[j+1][i] == 1);
        }
        if (i == 2) {
            if (j == 0) return (c[j+1][i] == 1 || c[j][i-1] == 1);
            if (j == 2) return (c[j-1][i] == 1 || c[j][i-1] == 1);
            return (c[j-1][i] == 1 || c[j][i-1] == 1 || c[j-1][i] == 1);
        }
        if (j == 0) return (c[j][i-1] == 1 || c[j][i+1] == 1 || c[j+1][i] == 1);
        if (j == 2) return (c[j][i-1] == 1 || c[j][i+1] == 1 || c[j-1][i] == 1);
        return (c[j - 1][i] == 1 || c[j][i + 1] == 1 || c[j + 1][i] == 1 ||
                c[j][i-1] == 1);
    }

    /**
     * Returns true if another segment touches this
     * segment if it were in the same tile
     * @param other other segment
     * @return true if they touch
     */
    boolean touchesWithinTile(Segment other) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)  {
                if (coords[j][i] == 1 &&
                    adjacentCoordIsOne(i, j, other.coords))
                    return true;
            }
        }
        return false;
    }

    /**
     * Returns a string with the name of the feature and the coordinates
     * as a 2d grid
     * @return string representing a segment
     */
    @Override
    public String toString() {
        String s = feature.name() + "\n";
        for (int[] r : coords) {
            for (int c : r) {
                s = s + c + " ";
            }
            s = s + "\n";
        }
        return s;
    }

}
