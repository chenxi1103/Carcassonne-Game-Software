package edu.cmu.cs.cs214.hw4.core;

import java.util.List;

/**
 * Feature Interface declare methods that a feature should have. Feature in
 * Carcassonne game is used to calculate scores and is constructed by
 * corresponding segments.
 */
public interface Feature {
    /**
     * Get all the segments that a feature has
     *
     * @return list of segments
     */
    List<Segment> getSegments();

    /**
     * Accessor of feature's id
     *
     * @return id
     */
    int getFeatureID();

    /**
     * Add a segment into the feature
     *
     * @param segment segment
     */
    void addSegment(Segment segment);

    /**
     * Add a meeple into a feature
     *
     * @param playerID playerID
     * @param position Position of the meeple
     */
    void addMeeple(int playerID, Position position);

    /**
     * Accessor of the meeplePos list
     *
     * @return list of Position
     */
    List<Position> getMeeplePos();

    /**
     * Give the meeple back to the Player
     *
     * @return boolean -> true if add returned successfully
     */
    List<Integer> returnMeeple();

    /**
     * Set a feature is completed
     */
    void setComplete();

    /**
     * Get how many meeples are there in a feature
     *
     * @return the number of meeples
     */
    int getMeepleNum();

    /**
     * Add a real segment into feature (a real segment is not be merged, it
     * is used for check complete)
     *
     * @param segment segment
     */
    void addRealSegment(Segment segment);

    /**
     * Accessor of the feature complete
     *
     * @return true if the feature is already complete
     */
    boolean isComplete();

    /**
     * Add a shield in the feature (especially for CityFeature)
     */
    void addShield();

    /**
     * Accessor of the number of shield (especially for CityFeature)
     * @return numShield
     */
    int getNumShield();

    /**
     * Check if it is surrounded by 8 tiles (especially for MonasteryFeature)
     * @return true if it is surrounded by 8 tiles
     */
    boolean checkIsComplete();

    /**
     * Accessor of the number of the segments
     * @return the number of the segments
     */
    int getNumSegments();

    /**
     * Accessor of the Type
     * @return type
     */
    int getType();

    /**
     * Accessor of numTiles
     *
     * @return the number of tiles
     */
    int getNumTiles();

    /**
     * Accessor of the list of real segments
     *
     * @return a list of real segments in a feature
     */
    List<Segment> getRealSegments();
}
