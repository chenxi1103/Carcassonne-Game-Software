package edu.cmu.cs.cs214.hw4.core;

/**
 * Object class that represents the player in the game
 */
public class Player {
    // Every player has 7 meeple at the beginning of the game
    private static final int INITIALMEEPLE = 7;
    private int leftMeeple;
    // ID that indicate the order of the player
    private int id;
    // Player's current score
    private int score;
//    private Tile currTile;

    /**
     * Constructor of the classPva
     *
     * @param id Given by the game system
     */
    Player(int id) {
        this.id = id;
        this.leftMeeple = INITIALMEEPLE;
        this.score = 0;
    }

    /**
     * Player place a meeple on the segment
     *
     * @return boolean -> true if meeple is placed successfully
     */
    boolean placeMeeple() {
        this.leftMeeple--;
        if (this.leftMeeple < 0) {
            this.leftMeeple = 0;
            return false;
        }
        return true;
    }

    /**
     * return how many meeple a player left
     * @return the number of left meeples
     */
    int getLeftMeeple() {
        return this.leftMeeple;
    }

    /**
     * get back one meeple
     */
    void returnMeeple() {
        if (this.leftMeeple < INITIALMEEPLE) {
            this.leftMeeple++;
        }
    }

    /**
     * Update score when the user's scores need to be added
     *
     * @param score
     */
    void addScore(int score) {
        this.score += score;
    }

    /**
     * Accessor of the player's scores
     *
     * @return the player's scores
     */
    int getScore() {
        return this.score;
    }
}
