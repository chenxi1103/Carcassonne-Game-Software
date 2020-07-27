package edu.cmu.cs.cs214.hw4.core;

import java.util.Objects;

/**
 * Segment class that represents the segments on a tile. A tile has five
 * segments. And there are four types of segments. The type of segments is
 * identified by attribute type.
 */
public class Segment {
    private boolean hasShield;
    // 0 -> grass 1 -> monastery, 2 -> road, 3 -> city
    private int type;
    private int featureID;
    private int orientation;
    private Position position;

    /**
     * Constructor of the Segment class
     *
     * @param type        type
     * @param hasShield   hasShield
     * @param orientation orientation
     */
    public Segment(int type, boolean hasShield, int orientation) {
        this.type = type;
        this.hasShield = hasShield;
        this.position = null;
        this.orientation = orientation;
    }

    /**
     * Set the orientation of the segment
     *
     * @param orientation orientation
     */
    void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    /**
     * Get the orientation of this segment
     *
     * @return orientation
     */
    int getOrientation() {
        return this.orientation;
    }

    /**
     * Set the position for the segment
     *
     * @param position
     */
    void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Get the position of the segment
     *
     * @return Position
     */
    public Position getPosition() {
        return new Position(this.position.getX(), this.position.getY());
    }

    /**
     * Accessor of the segment's type
     *
     * @return type
     */
    public int getType() {
        return this.type;
    }

    /**
     * Check if the segment has a shield on it
     *
     * @return true if it has
     */
    boolean getHasShield() {
        return this.hasShield;
    }

    /**
     * Every segment belongs to a feature
     * A setter to set segment's feature
     *
     * @param featureID featureID
     */
    public void setFeature(int featureID) {
        this.featureID = featureID;
    }

    /**
     * Accessor of the featureID
     *
     * @return featureID
     */
    public int getFeature() {
        return this.featureID;
    }

    /**
     * Override the equals function
     *
     * @param obj
     * @return boolean -> true if they are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Segment) {
            Segment segment = (Segment) obj;
            return segment.getType() == this.type
                    && segment.getPosition().equals(this.position)
                    && segment.getOrientation() == this.orientation
                    && segment.featureID == this.featureID;
        }
        return false;
    }

    /**
     * Override the hashCode function
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.position, this.featureID,
                this.orientation);
    }

    @Override
    public String toString() {
        return "Type: " + this.type + ", Position: "
                + this.position.toString() + ", FeatureID: "
                + this.featureID + ", Orientation: " + this.orientation;
    }


}
