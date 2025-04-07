package org.asarenski.JavaCraps.core;

import java.util.Random;

/**
 * Utility class for simulating dice rolls in the Craps game.
 * Handles rolling two six-sided dice and calculating their sum.
 */
public class Dice {
    private static final int SIDES = 6;
    private final Random random;
    private int value;  // Total value of both dice
    private int die1;   // Value of first die
    private int die2;   // Value of second die

    public Dice() {
        this.random = new Random();
        reset();
    }

    /**
     * Resets the dice to their initial state.
     */
    public void reset() {
        this.value = 0;
        this.die1 = 0;
        this.die2 = 0;
    }

    /**
     * Simulates rolling two six-sided dice and returns their sum.
     * The result is stored as the last roll.
     * @return The sum of the two dice (2-12)
     */
    public int roll() {
        die1 = rollSingleDie();
        die2 = rollSingleDie();
        value = die1 + die2;
        return value;
    }

    /**
     * Simulates rolling a single six-sided die.
     * @return A random number between 1 and 6
     */
    private int rollSingleDie() {
        return random.nextInt(SIDES) + 1;
    }

    /**
     * Gets the total value of the last roll.
     * @return The sum of the last roll, or 0 if no roll has been made
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the value of the first die from the last roll.
     * @return The value of the first die, or 0 if no roll has been made
     */
    public int getDie1() {
        return die1;
    }

    /**
     * Gets the value of the second die from the last roll.
     * @return The value of the second die, or 0 if no roll has been made
     */
    public int getDie2() {
        return die2;
    }
} 