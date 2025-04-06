package org.asarenski.JavaCraps.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    @Test
    void testInitialLastRoll() {
        Dice dice = new Dice();
        assertEquals(0, dice.getLastRoll(), "Initial last roll should be 0");
    }

    @RepeatedTest(1000)
    void testRollRange() {
        Dice dice = new Dice();
        int roll = dice.roll();
        assertTrue(roll >= 2 && roll <= 12, 
            "Roll should be between 2 and 12, but was: " + roll);
    }

    @Test
    void testLastRollMatchesRoll() {
        Dice dice = new Dice();
        int roll = dice.roll();
        assertEquals(roll, dice.getLastRoll(), 
            "Last roll should match the result of roll()");
    }

    @RepeatedTest(1000)
    void testRollDistribution() {
        Dice dice = new Dice();
        int roll = dice.roll();
        // Test that rolls are within valid bounds for two dice
        assertTrue(roll >= 2 && roll <= 12, 
            "Roll should be between 2 and 12, but was: " + roll);
        // Since we're using 2 dice, 1 and 13+ should never occur
        assertFalse(roll < 2 || roll > 12,
            "Roll should never be less than 2 or greater than 12, but was: " + roll);
    }
} 