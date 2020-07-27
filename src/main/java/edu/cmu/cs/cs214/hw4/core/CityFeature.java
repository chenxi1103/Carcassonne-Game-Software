package edu.cmu.cs.cs214.hw4.core;

import java.util.List;

/**
 * CityFeature class -> A variant of feature class
 */
public class CityFeature implements Feature {
    private int type;
    private static final int CITY = 3;
    private Feature delegator;
    private boolean complete;

    /**
     * Constructor of the CityFeature class
     *
     * @param featureID
     */
    CityFeature(int featureID) {
        this.delegator = new DelegationFeature(featureID);
        this.type = CITY;
    }

    /**
     * Accessor of the type
     * @return type
     */
    public int getType() {
        return this.type;
    }

    /**
     * Accessor of the number of the shield
     *
     * @return the number of shield
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
     * get the number of segments
     * @return the number of segments
     */
    public int getNumSegments() {
        return delegator.getNumSegments();
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
        if (segment.getType() == CITY) {
            delegator.addSegment(segment);
        }
    }

    /**
     * Add real segment into RealSegmentlist
     *
     * @param segment
     */
    @Override
    public void addRealSegment(Segment segment) {
        delegator.addRealSegment(segment);
    }

    /**
     * Accessor of the realSegments
     *
     * @return
     */
    @Override
    public List<Segment> getRealSegments() {
        return delegator.getRealSegments();
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
     * Get the meeple position list -> especially used for GUI display
     *
     * @return
     */
    @Override
    public List<Position> getMeeplePos() {
        return this.delegator.getMeeplePos();
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
     * Set the isComplete to be true
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
     * Function that used to increase the number of shield
     */
    @Override
    public void addShield() {
        this.delegator.addShield();
    }

    /**
     * Accessor of the number of the meeples in this feature
     *
     * @return the number of the meeples in a feature
     */
    public int getMeepleNum() {
        return delegator.getMeepleNum();
    }

    /**
     * Accessor of numTiles
     *
     * @return the number of tiles
     */
    public int getNumTiles() {
        return 0;
    }
}
