package org.asarenski.JavaCraps.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    @Test
    void testInitialValues() {
        Dice dice = new Dice();
        assertEquals(0, dice.getValue(), "Initial total value should be 0");
        assertEquals(0, dice.getDie1(), "Initial die1 value should be 0");
        assertEquals(0, dice.getDie2(), "Initial die2 value should be 0");
    }

    @RepeatedTest(1000)
    void testRollRange() {
        Dice dice = new Dice();
        int roll = dice.roll();
        assertTrue(roll >= 2 && roll <= 12, 
            "Roll should be between 2 and 12, but was: " + roll);
    }

    @Test
    void testValueMatchesRoll() {
        Dice dice = new Dice();
        int roll = dice.roll();
        assertEquals(roll, dice.getValue(), 
            "Total value should match the result of roll()");
    }

    @RepeatedTest(1000)
    void testIndividualDiceValues() {
        Dice dice = new Dice();
        dice.roll();
        int die1 = dice.getDie1();
        int die2 = dice.getDie2();
        
        // Test individual die ranges
        assertTrue(die1 >= 1 && die1 <= 6,
            "Die1 should be between 1 and 6, but was: " + die1);
        assertTrue(die2 >= 1 && die2 <= 6,
            "Die2 should be between 1 and 6, but was: " + die2);
        
        // Test that sum matches total value
        assertEquals(die1 + die2, dice.getValue(),
            "Sum of individual dice should equal total value");
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