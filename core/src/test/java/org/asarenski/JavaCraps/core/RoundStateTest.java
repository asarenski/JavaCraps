package org.asarenski.JavaCraps.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoundStateTest {
    private RoundState roundState;

    @BeforeEach
    void setUp() {
        roundState = new RoundState();
    }

    @Test
    void testInitialState() {
        assertEquals(RoundState.Phase.COME_OUT_ROLL, roundState.getCurrentPhase());
        assertEquals(RoundState.Status.PLAYING, roundState.getGameStatus());
        assertEquals(0, roundState.getPoint());
    }

    @Test
    void testReset() {
        // First change the state
        roundState.enterPointPhase(6);
        
        // Then reset
        roundState.reset();
        
        // Verify reset state
        assertEquals(RoundState.Phase.COME_OUT_ROLL, roundState.getCurrentPhase());
        assertEquals(RoundState.Status.PLAYING, roundState.getGameStatus());
        assertEquals(0, roundState.getPoint());
    }

    @Test
    void testEnterPointPhase() {
        int testPoint = 6;
        roundState.enterPointPhase(testPoint);
        
        assertEquals(RoundState.Phase.POINT_PHASE, roundState.getCurrentPhase());
        assertEquals(testPoint, roundState.getPoint());
        assertEquals(RoundState.Status.PLAYING, roundState.getGameStatus());
    }

    @Test
    void testComeOutRollWins() {
        // Test natural 7
        Boolean result7 = roundState.checkOutcome(7);
        assertTrue(result7);
        assertEquals(RoundState.Status.WIN, roundState.getGameStatus());
        assertEquals(RoundState.Phase.COME_OUT_ROLL, roundState.getCurrentPhase());

        // Reset and test natural 11
        roundState.reset();
        Boolean result11 = roundState.checkOutcome(11);
        assertTrue(result11);
        assertEquals(RoundState.Status.WIN, roundState.getGameStatus());
        assertEquals(RoundState.Phase.COME_OUT_ROLL, roundState.getCurrentPhase());
    }

    @Test
    void testComeOutRollLoses() {
        // Test craps numbers
        int[] crapsNumbers = {2, 3, 12};
        for (int roll : crapsNumbers) {
            roundState.reset();
            Boolean result = roundState.checkOutcome(roll);
            assertFalse(result);
            assertEquals(RoundState.Status.LOSE, roundState.getGameStatus());
            assertEquals(RoundState.Phase.COME_OUT_ROLL, roundState.getCurrentPhase());
        }
    }

    @Test
    void testComeOutRollToPoint() {
        // Test establishing point numbers (4,5,6,8,9,10)
        int[] pointNumbers = {4, 5, 6, 8, 9, 10};
        for (int roll : pointNumbers) {
            roundState.reset();
            Boolean result = roundState.checkOutcome(roll);
            assertNull(result);
            assertEquals(RoundState.Phase.POINT_PHASE, roundState.getCurrentPhase());
            assertEquals(RoundState.Status.PLAYING, roundState.getGameStatus());
            assertEquals(roll, roundState.getPoint());
        }
    }

    @Test
    void testPointPhaseWin() {
        // Establish point
        roundState.enterPointPhase(6);
        
        // Roll the point
        Boolean result = roundState.checkOutcome(6);
        
        assertTrue(result);
        assertEquals(RoundState.Status.WIN, roundState.getGameStatus());
    }

    @Test
    void testPointPhaseLose() {
        // Establish point
        roundState.enterPointPhase(6);
        
        // Roll a seven
        Boolean result = roundState.checkOutcome(7);
        
        assertFalse(result);
        assertEquals(RoundState.Status.LOSE, roundState.getGameStatus());
    }

    @Test
    void testPointPhaseContinue() {
        // Establish point of 6
        roundState.enterPointPhase(6);
        
        // Roll something other than 6 or 7
        Boolean result = roundState.checkOutcome(5);
        
        assertNull(result);
        assertEquals(RoundState.Status.PLAYING, roundState.getGameStatus());
        assertEquals(RoundState.Phase.POINT_PHASE, roundState.getCurrentPhase());
        assertEquals(6, roundState.getPoint());
    }
} 