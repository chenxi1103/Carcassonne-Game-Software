package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Delegation class for Feature variants that implements Feature interface
 */
public class DelegationFeature implements Feature {
    private List<Segment> segmentList;
    private List<Segment> realSegments;
    // meeples stores the players' ID
    private List<Integer> meeples;
    private int featureID;
    private boolean complete;
    private List<Position> meeplePos;
    private int numShield;
    private int type;

    /**
     * Constructor for feature delegator
     *
     * @param featureID
     */
    DelegationFeature(int featureID) {
        this.segmentList = new ArrayList<>();
        this.realSegments = new ArrayList<>();
        this.meeples = new ArrayList<>();
        this.featureID = featureID;
        this.meeplePos = new ArrayList<>();
        this.numShield = 0;
        this.type = 0;
    }

    /**
     * Accessor of the type
     * @return type
     */
    public int getType() {
        return this.type;
    }

    /**
     * Function that used to increase the number of shield
     */
    @Override
    public void addShield() {
        this.numShield++;
    }

    /**
     * Accessor of the number of shield
     * @return the number of shield
     */
    @Override
    public int getNumShield() {
        return this.numShield;
    }

    /**
     * @return complete
     */
    public boolean checkIsComplete() {
        return this.complete;
    }

    /**
     * Accessor of the number of segments
     * @return numSegments
     */
    public int getNumSegments() {
        return this.segmentList.size();
    }

    /**
     * Accessor of feature's id
     *
     * @return id
     */
    public int getFeatureID() {
        return this.featureID;
    }

    /**
     * For a single feature, there would be one or more meeples on this feature
     * Return a part of segments that a specific Meeple occupied
     *
     * @return list of segment
     */
    @Override
    public List<Segment> getSegments() {
        return new ArrayList<>(this.segmentList);
    }

    /**
     * Accessor of the realSegments
     *
     * @return list of real segment
     */
    @Override
    public List<Segment> getRealSegments() {
        return new ArrayList<>(this.realSegments);
    }

    /**
     * Accessor of meeplePos
     *
     * @return list of Position
     */
    @Override
    public List<Position> getMeeplePos() {
        return new ArrayList<>(meeplePos);
    }

    /**
     * Add a meeple in the feature
     *
     * @param playerID
     */
    @Override
    public void addMeeple(int playerID, Position position) {
        this.meeples.add(playerID);
        this.meeplePos.add(position);
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
     * Add a segment into the feature
     *
     * @param segment
     */
    @Override
    public void addSegment(Segment segment) {
        this.segmentList.add(segment);
    }

    /**
     * Add a real segment to check complete of a feature
     *
     * @param segment segment
     */
    @Override
    public void addRealSegment(Segment segment) {
        this.realSegments.add(segment);
    }

    /**
     * Give the meeple back to the Player
     *
     * @return boolean -> true if add returned successfully
     */
    @Override
    public List<Integer> returnMeeple() {
        return new ArrayList<>(this.meeples);
    }

    /**
     * Set the complete to be true
     */
    @Override
    public void setComplete() {
        this.complete = true;
    }

    /**
     * Accessor of the number of the meeple in this feature
     *
     * @return the number of the meeple in this feature
     */
    @Override
    public int getMeepleNum() {
        return meeples.size();
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
