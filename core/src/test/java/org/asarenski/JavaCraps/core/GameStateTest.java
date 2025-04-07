package org.asarenski.JavaCraps.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {
    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
    }

    @Test
    void testInitialState() {
        assertEquals(GameState.Phase.COME_OUT_ROLL, gameState.getCurrentPhase());
        assertEquals(GameState.Status.PLAYING, gameState.getGameStatus());
        assertEquals(0, gameState.getPoint());
    }

    @Test
    void testReset() {
        // First change the state
        gameState.enterPointPhase(6);
        
        // Then reset
        gameState.reset();
        
        // Verify reset state
        assertEquals(GameState.Phase.COME_OUT_ROLL, gameState.getCurrentPhase());
        assertEquals(GameState.Status.PLAYING, gameState.getGameStatus());
        assertEquals(0, gameState.getPoint());
    }

    @Test
    void testEnterPointPhase() {
        int testPoint = 6;
        gameState.enterPointPhase(testPoint);
        
        assertEquals(GameState.Phase.POINT_PHASE, gameState.getCurrentPhase());
        assertEquals(testPoint, gameState.getPoint());
        assertEquals(GameState.Status.PLAYING, gameState.getGameStatus());
    }

    @Test
    void testComeOutRollWins() {
        // Test natural 7
        Boolean result7 = gameState.checkOutcome(7);
        assertTrue(result7);
        assertEquals(GameState.Status.WIN, gameState.getGameStatus());
        assertEquals(GameState.Phase.COME_OUT_ROLL, gameState.getCurrentPhase());

        // Reset and test natural 11
        gameState.reset();
        Boolean result11 = gameState.checkOutcome(11);
        assertTrue(result11);
        assertEquals(GameState.Status.WIN, gameState.getGameStatus());
        assertEquals(GameState.Phase.COME_OUT_ROLL, gameState.getCurrentPhase());
    }

    @Test
    void testComeOutRollLoses() {
        // Test craps numbers
        int[] crapsNumbers = {2, 3, 12};
        for (int roll : crapsNumbers) {
            gameState.reset();
            Boolean result = gameState.checkOutcome(roll);
            assertFalse(result);
            assertEquals(GameState.Status.LOSE, gameState.getGameStatus());
            assertEquals(GameState.Phase.COME_OUT_ROLL, gameState.getCurrentPhase());
        }
    }

    @Test
    void testComeOutRollToPoint() {
        // Test establishing point numbers (4,5,6,8,9,10)
        int[] pointNumbers = {4, 5, 6, 8, 9, 10};
        for (int roll : pointNumbers) {
            gameState.reset();
            Boolean result = gameState.checkOutcome(roll);
            assertNull(result);
            assertEquals(GameState.Phase.POINT_PHASE, gameState.getCurrentPhase());
            assertEquals(GameState.Status.PLAYING, gameState.getGameStatus());
            assertEquals(roll, gameState.getPoint());
        }
    }

    @Test
    void testPointPhaseWin() {
        // Establish point
        gameState.enterPointPhase(6);
        
        // Roll the point
        Boolean result = gameState.checkOutcome(6);
        
        assertTrue(result);
        assertEquals(GameState.Status.WIN, gameState.getGameStatus());
    }

    @Test
    void testPointPhaseLose() {
        // Establish point
        gameState.enterPointPhase(6);
        
        // Roll a seven
        Boolean result = gameState.checkOutcome(7);
        
        assertFalse(result);
        assertEquals(GameState.Status.LOSE, gameState.getGameStatus());
    }

    @Test
    void testPointPhaseContinue() {
        // Establish point of 6
        gameState.enterPointPhase(6);
        
        // Roll something other than 6 or 7
        Boolean result = gameState.checkOutcome(5);
        
        assertNull(result);
        assertEquals(GameState.Status.PLAYING, gameState.getGameStatus());
        assertEquals(GameState.Phase.POINT_PHASE, gameState.getCurrentPhase());
        assertEquals(6, gameState.getPoint());
    }
} 