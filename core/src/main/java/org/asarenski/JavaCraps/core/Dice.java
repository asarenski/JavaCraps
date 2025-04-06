package org.asarenski.JavaCraps.core;

import java.util.Random;

/**
 * Utility class for simulating dice rolls in the Craps game.
 * Handles rolling two six-sided dice and calculating their sum.
 */
public class Dice {
    private static final int SIDES = 6;
    private final Random random;
    private int lastRoll;

    public Dice() {
        this.random = new Random();
        this.lastRoll = 0;
    }

    /**
     * Simulates rolling two six-sided dice and returns their sum.
     * The result is stored as the last roll.
     * @return The sum of the two dice (2-12)
     */
    public int roll() {
        int die1 = rollSingleDie();
        int die2 = rollSingleDie();
        lastRoll = die1 + die2;
        return lastRoll;
    }

    /**
     * Simulates rolling a single six-sided die.
     * @return A random number between 1 and 6
     */
    private int rollSingleDie() {
        return random.nextInt(SIDES) + 1;
    }

    /**
     * Gets the result of the last roll.
     * @return The sum of the last roll, or 0 if no roll has been made
     */
    public int getLastRoll() {
        return lastRoll;
    }
} 