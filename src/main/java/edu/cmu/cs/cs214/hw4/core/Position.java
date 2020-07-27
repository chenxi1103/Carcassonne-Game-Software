package edu.cmu.cs.cs214.hw4.core;

import java.util.Objects;

/**
 * Object class that represents a position on the game map
 */
public class Position {

    private int x;
    private int y;

    /**
     * Constructor of the Position class
     *
     * @param x x
     * @param y y
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Accessor of the x coordinate
     *
     * @return x
     */
    public int getX() {
        return this.x;
    }

    /**
     * Accessor of the y coordinate
     *
     * @return y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Override the equals function
     * @param obj
     * @return boolean -> true if they are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position pos = (Position) obj;
            return this.x == pos.getX() && this.y == pos.getY();
        }
        return false;
    }

    /**
     * Override the hashCode function
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }
}
