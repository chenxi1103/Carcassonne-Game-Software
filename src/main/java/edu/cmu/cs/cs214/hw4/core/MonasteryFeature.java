package edu.cmu.cs.cs214.hw4.core;

import java.util.List;

/**
 * MonasteryFeature is a variant of Feature and implement the Feature
 * Interface based on Delegation. It is constructed by MonasterySegment.
 */
public class MonasteryFeature implements Feature {
    private static final int COMPLETE = 9;
    private Feature delegator;
    private int numTiles;
    private boolean complete;
    private int type;

    /**
     * Constructor of MonasteryFeature class
     *
     * @param featureID
     */
    MonasteryFeature(int featureID) {
        this.delegator = new DelegationFeature(featureID);
        this.numTiles = 0;
        this.complete = false;
        this.type = 1;
    }

    /**
     * For a single feature, there would be one or more meeples on this feature
     * Return a part of segments that a specific Meeple occupied
     *
     * @return
     */
    @Override
    public List<Segment> getSegments() {
        return delegator.getSegments();
    }

    @Override
    public List<Position> getMeeplePos() {
        return this.delegator.getMeeplePos();
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
     * Accessor of numTiles
     *
     * @return the number of tiles
     */
    public int getNumTiles() {
        return this.numTiles;
    }

    /**
     * get the number of segments
     * @return the number of segments
     */
    public int getNumSegments() {
        return delegator.getNumSegments();
    }

    /**
     * Add a segment into the feature
     *
     * @param segment
     */
    @Override
    public void addSegment(Segment segment) {
        delegator.addSegment(segment);
        this.numTiles++;
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
     * Give the meeple back to the Player
     *
     * @return boolean -> true if add returned successfully
     */
    @Override
    public List<Integer> returnMeeple() {
        return delegator.returnMeeple();
    }

    /**
     * Function that used to add real segments
     *
     * @param segment
     */
    @Override
    public void addRealSegment(Segment segment) {
        delegator.addRealSegment(segment);
    }

    /**
     * Accessor of the realSegments list
     *
     * @return list of segments
     */
    @Override
    public List<Segment> getRealSegments() {
        return delegator.getRealSegments();
    }

    /**
     * Only if when there are 8 surrounding tiles, the feature is complete
     *
     * @return boolean -> true if it is complete
     */
    public boolean checkIsComplete() {
        if (numTiles == COMPLETE) {
            setComplete();
            return true;
        }
        return false;
    }

    /**
     * Set the complete to be true
     */
    public void setComplete() {
        this.complete = true;
    }

    /**
     * Accessor of the complete
     *
     * @return turn if the feature is complete
     */
    public boolean isComplete() {
        return this.complete;
    }

    /**
     * Accessor of the number of the meeple in the feature
     *
     * @return the number of the meeple in the feature
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
     * Accessor of the type
     * @return type
     */
    public int getType() {
        return this.type;
    }
}
