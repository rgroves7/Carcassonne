package edu.cmu.cs.cs214.hw4.core;

/**
 * Class representing a tile
 */
public class Tile {
    /**
     * Name of the tile in the official rules
     */
    private String name;
    public String getName() {return name;}
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Quantity of this type of type found in the deck
     */
    private int quantity;
    public int getQuantity() {return quantity;};
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Array of edge types, arranged clockwise starting from
     * the top
     */
    private EdgeType[] edges;
    public EdgeType[] getEdges() {return edges;};
    public void setEdges(EdgeType[] edges){
        this.edges = edges;
    }

    /**
     * Array of segments found on the tile
     */
    private Segment[] segments;
    public Segment[] getSegments() {return segments;};
    public void setSegments(Segment[] segments) {
        this.segments = segments;
    }

    public Tile() { }

    /**
     * Creates a new tile with edges e and segments s
     * @param e edges
     * @param s segments
     */
    public Tile(EdgeType[] e, Segment[] s) {
        edges = e;
        segments = s;
    }

    /**
     * Returns a copy of this tile with all the same
     * segments and edges
     * @return copied tile
     */
    Tile TileCopy() {
        EdgeType[] e = new EdgeType[4];
        Segment[] s = new Segment[segments.length];

        for (int i = 0; i < 4; i ++) {
            e[i] = edges[i];
        }

        for (int i = 0; i < segments.length; i ++) {
            s[i] = segments[i].SegmentCopy();
        }
        Tile t = new Tile(e, s);
        t.setName(name);
        t.setQuantity(quantity);
        return t;
    }

    /**
     * Returns the top edge of the tile
     * @return Returns the EdgeType of the top edge of the tile
     */
    EdgeType getTop() {return edges[0];}

    /**
     * Returns the right edge of the tile
     * @return Returns the EdgeType of the right edge of the tile
     */
    EdgeType getRight() {return edges[1];}

    /**
     * Returns the bottom edge of the tile
     * @return Returns the EdgeType of the bottom edge of the tile
     */
    EdgeType getBottom() {return edges[2];}

    /**
     * Returns the left edge of the tile
     * @return Returns the EdgeType of the left edge of the tile
     */
    EdgeType getLeft() {return edges[3];}

    /**
     * Rotates tile once clockwise
     */
    void rotate() {
        EdgeType[] newE = new EdgeType[4];
        for (int i = 0; i < 4; i++) {
            newE[(i+1)%4] = edges[i];
        }
        edges = newE;
        for(Segment seg : segments) {
            seg.rotate();
        }
    }

    /**
     * Represents the tile as the name, its array of edges
     * and each segment printed as a 2d grid
     * @return string representing the tile
     */
    @Override
    public String toString() {
        String s = getName() + "\n";
        for (EdgeType e : edges) {
            s = s + e.name() + " ";
        }
        s = s + "\n";
        for (Segment seg : segments) {
            s = s + seg.toString();
        }
        return s;
    }


}
