package org.asarenski.JavaCraps.core;

/**
 * Represents the current state of a Craps round.
 */
public class RoundState {
    public enum Phase {
        COME_OUT_ROLL,
        POINT_PHASE
    }

    public enum Status {
        PLAYING,
        WIN,
        LOSE
    }

    private Phase currentPhase;
    private Status gameStatus;
    private int point;

    public RoundState() {
        reset();
    }

    /**
     * Resets the game state to initial values.
     */
    public void reset() {
        currentPhase = Phase.COME_OUT_ROLL;
        gameStatus = Status.PLAYING;
        point = 0;
    }

    /**
     * Transitions the game to Point Phase with the specified point number.
     * @param point The point number established
     */
    public void enterPointPhase(int point) {
        this.point = point;
        currentPhase = Phase.POINT_PHASE;
    }

    /**
     * Checks the outcome of a roll based on the current phase.
     * @param roll The total value of the dice roll
     * @return true if the roll results in a win, false for a loss, null if the game continues
     */
    public Boolean checkOutcome(int roll) {
        if (currentPhase == Phase.COME_OUT_ROLL) {
            if (roll == 7 || roll == 11) {
                gameStatus = Status.WIN;
                return true;
            } else if (roll == 2 || roll == 3 || roll == 12) {
                gameStatus = Status.LOSE;
                return false;
            }
            enterPointPhase(roll);
            return null;
        } else { // POINT_PHASE
            if (roll == point) {
                gameStatus = Status.WIN;
                return true;
            } else if (roll == 7) {
                gameStatus = Status.LOSE;
                return false;
            }
            return null;
        }
    }

    // Getters
    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public Status getGameStatus() {
        return gameStatus;
    }

    public int getPoint() {
        return point;
    }
} 