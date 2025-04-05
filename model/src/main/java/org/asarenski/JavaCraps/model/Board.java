package org.asarenski.JavaCraps.model;

/**
 * Represents the game board state for the Craps game.
 */
public class Board {
    private int point;
    private boolean isPointPhase;
    private int[] lastRoll;

    public Board() {
        this.point = 0;
        this.isPointPhase = false;
        this.lastRoll = new int[2];
    }

    public void setPoint(int point) {
        this.point = point;
        this.isPointPhase = true;
    }

    public void clearPoint() {
        this.point = 0;
        this.isPointPhase = false;
    }

    public void setLastRoll(int die1, int die2) {
        this.lastRoll[0] = die1;
        this.lastRoll[1] = die2;
    }

    public int getPoint() {
        return point;
    }

    public boolean isPointPhase() {
        return isPointPhase;
    }

    public int[] getLastRoll() {
        return lastRoll.clone();
    }

    public int getLastRollSum() {
        return lastRoll[0] + lastRoll[1];
    }
} 