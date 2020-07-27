package edu.cmu.cs.cs214.hw4.core;

import java.util.List;

/**
 * RoadFeature is a variant of Feature and implement with Feature interface
 * based on Delegation. It is constructed by road segments.
 */
public class RoadFeature implements Feature {
    private Feature delegator;
    private boolean complete;
    private int type;
    private static final int ROAD = 2;

    RoadFeature(int featureID) {
        delegator = new DelegationFeature(featureID);
        this.complete = false;
        this.type = ROAD;
    }

    /**
     * For a single feature, there would be one or more meeples on this feature
     * Return a part of segments that a specific Meeple occupied
     *
     * @return list of segments
     */
    @Override
    public List<Segment> getSegments() {
        return delegator.getSegments();
    }

    /**
     * get the number of segments
     * @return the number of segments
     */
    public int getNumSegments() {
        return delegator.getNumSegments();
    }

    /**
     * Accessor of feature's id
     *
     * @return id
     */
    public int getFeatureID() {
        return this.delegator.getFeatureID();
    }

    /**
     * Add a segment into the feature
     *
     * @param segment
     */
    @Override
    public void addSegment(Segment segment) {
        if (segment.getType() == 2) {
            delegator.addSegment(segment);
        }
    }

    @Override
    public List<Position> getMeeplePos() {
        return this.delegator.getMeeplePos();
    }

    /**
     * Add a meeple in the feature
     *
     * @param playerID
     */
    @Override
    public void addMeeple(int playerID, Position position) {
        this.delegator.addMeeple(playerID, position);
    }

    /**
     * Add a real segment into realSegmentsList
     *
     * @param segment
     */
    @Override
    public void addRealSegment(Segment segment) {
        delegator.addRealSegment(segment);
    }

    /**
     * Accessor of the realSegmentsList
     *
     * @return
     */
    @Override
    public List<Segment> getRealSegments() {
        return delegator.getRealSegments();
    }

    /**
     * Give the meeple back to the Player
     *
     * @return boolean -> true if add returned successfully
     */
    @Override
    public List<Integer> returnMeeple() {
        return delegator.returnMeeple();
    }

    /**
     * Set the complete to be true;
     */
    public void setComplete() {
        this.complete = true;
    }

    /**
     * Accessor of the complete
     *
     * @return true if the feature is complete
     */
    public boolean isComplete() {
        return this.complete;
    }

    /**
     * Accessor of the number of the meeples in this feature
     *
     * @return the number of the meeples in this feature
     */
    public int getMeepleNum() {
        return delegator.getMeepleNum();
    }

    /**
     * Function that used to increase the number of shield
     */
    @Override
    public void addShield() {
        this.delegator.addShield();
    }

    /**
     * Accessor of the number of the shield
     *
     * @return the number of the shield
     */
    public int getNumShield() {
        return this.delegator.getNumShield();
    }

    /**
     * @return complete
     */
    public boolean checkIsComplete() {
        return this.complete;
    }

    /**
     * Accessor of numTiles
     *
     * @return the number of tiles
     */
    public int getNumTiles() {
        return 0;
    }

    /**
     * Accessor of the type
     * @return type
     */
    public int getType() {
        return this.type;
    }
}
