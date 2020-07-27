package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Object class for Tile
 */
public class Tile {
    private static final int LEFT = 3;
    private static final int REMOVE = 4;
    // Tile needs to be put so it has position
    private Position position;
    // has monastery or not
    private boolean hasMonastery;
    // Segment information;
    private List<Segment> segmentList;
    private char tag;
    private String filePath;

    /**
     * Constructor for tile class
     *
     * @param segments segments
     * @param hasMonastery hasMonastery
     * @param tag tag
     */
    public Tile(List<Segment> segments, boolean hasMonastery, char tag) {
        this.segmentList = new ArrayList<>(segments);
        this.hasMonastery = hasMonastery;
        this.tag = tag;
        this.position = null;
    }

    /**
     * Set the orientation of each segment in the segmentList
     */
    public void initialOrientation() {
        for (int i = 0; i < segmentList.size(); i++) {
            segmentList.get(i).setOrientation(i);
        }
    }

    /**
     * Accessor of the segment's tag
     * @return tag
     */
    char getTag() {
        return tag;
    }

    /**
     * Accessor of the segmentList
     * @return deep copy of the segment list
     */
    List<Segment> getSegmentList() {
        return new ArrayList<Segment>(this.segmentList);
    }

    /**
     * Set the image filepath for tiles
     *
     * @param filePath
     */
    void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Accessor of the filePath
     * @return filePath
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * Player can set the tile orientation before place it on the map
     */
    public void rotate() {
        this.segmentList.add(0, this.segmentList.get(LEFT));
        this.segmentList.remove(REMOVE);
        for (int i = 0; i < segmentList.size(); i++) {
            segmentList.get(i).setOrientation(i);
        }
    }

    /**
     * Get the edge segment to check if it matches its neighbor
     * 0 -> top, 1 -> right, 2 -> bottom, 3 -> left, 4 -> center
     *
     * @param edge edge
     * @return Segment
     */
    public Segment getEdgeSegment(int edge) {
        return this.segmentList.get(edge);
    }

    /**
     * Accessor of the monastery
     *
     * @return true if the tile has a monastery
     */
    boolean getHasMonastery() {
        return this.hasMonastery;
    }

    /**
     * Function that used to set the position of the tile
     *
     * @param position position
     */
    public void setPosition(Position position) {
        this.position = new Position(position.getX(), position.getY());
    }

    /**
     * Accessor of the tile's position
     *
     * @return Position
     */
    Position getPosition() {
        return this.position;
    }

    /**
     * Override the equals function
     * @param obj
     * @return boolean -> true if they are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            Tile tile = (Tile) obj;
            return tile.getTag() == this.getTag()
                    && tile.getHasMonastery() ^ this.hasMonastery
                    && tile.getPosition().equals(this.position);
        }
        return false;
    }

    /**
     * Override the hashCode function
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.tag, this.hasMonastery, this.position) ;
    }

    @Override
    public String toString() {
        return "Tag: " + this.tag + ", Position: " + this.position.toString();
    }
}
