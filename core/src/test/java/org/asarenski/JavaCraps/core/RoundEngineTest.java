package org.asarenski.JavaCraps.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

class RoundEngineTest {
    private RoundEngine roundEngine;
    private Player player;
    private static final int TEST_BET = 10;

    @BeforeEach
    void setUp() {
        player = new Player();
        roundEngine = new RoundEngine(player);
    }

    @Test
    void testInitialState() {
        assertEquals(0, roundEngine.getLastRoll(),
            "Initial last roll should be 0");
        assertFalse(roundEngine.isGameOver(),
            "Game should not be over initially");
        assertEquals(GameState.Phase.COME_OUT_ROLL, roundEngine.getGameState().getCurrentPhase(),
            "Game should start in come out roll phase");
        assertEquals(GameState.Status.PLAYING, roundEngine.getGameState().getGameStatus(),
            "Game status should be PLAYING initially");
    }

    @RepeatedTest(1000)
    void testRollDiceRange() {
        int roll = roundEngine.rollDice();
        assertTrue(roll >= 2 && roll <= 12,
            "Roll should be between 2 and 12, but was: " + roll);
    }

    @Test
    void testRollMatchesLastRoll() {
        int roll = roundEngine.rollDice();
        assertEquals(roll, roundEngine.getLastRoll(),
            "Last roll should match the result of rollDice()");
    }

    @Test
    void testResetRound() {
        // Make a roll and place a bet
        roundEngine.placeBet(TEST_BET);
        roundEngine.rollDice();
        
        // Store player balance before reset
        int balanceBeforeReset = roundEngine.getPlayer().getBalance();
        
        // Reset the round
        roundEngine.resetRound();
        
        // Verify game state is reset but player state is preserved
        assertFalse(roundEngine.isGameOver(),
            "Game should not be over after reset");
        assertEquals(GameState.Phase.COME_OUT_ROLL, roundEngine.getGameState().getCurrentPhase(),
            "Game should be in come out roll phase after reset");
        assertEquals(GameState.Status.PLAYING, roundEngine.getGameState().getGameStatus(),
            "Game status should be PLAYING after reset");
        assertEquals(balanceBeforeReset, roundEngine.getPlayer().getBalance(),
            "Player balance should be preserved after round reset");
    }

    @Test
    void testPlaceBet() {
        assertTrue(roundEngine.placeBet(TEST_BET),
            "Should be able to place valid bet");
        assertFalse(roundEngine.placeBet(-5),
            "Should not be able to place negative bet");
        assertFalse(roundEngine.placeBet(101),
            "Should not be able to place bet larger than balance");
    }

    @RepeatedTest(100)
    void testGameOutcomes() {
        roundEngine.placeBet(TEST_BET);
        int roll = roundEngine.rollDice();
        
        if (roll == 7 || roll == 11) {
            // Natural win
            assertTrue(roundEngine.isGameOver(),
                "Game should be over after natural win");
            assertEquals(GameState.Status.WIN, roundEngine.getGameState().getGameStatus(),
                "Game status should be WIN after natural");
            assertEquals(110, roundEngine.getPlayer().getBalance(),
                "Player should win their bet on natural");
        } else if (roll == 2 || roll == 3 || roll == 12) {
            // Craps loss
            assertTrue(roundEngine.isGameOver(),
                "Game should be over after craps");
            assertEquals(GameState.Status.LOSE, roundEngine.getGameState().getGameStatus(),
                "Game status should be LOSE after craps");
            assertEquals(90, roundEngine.getPlayer().getBalance(),
                "Player should lose their bet on craps");
        } else {
            // Point established
            assertEquals(GameState.Phase.POINT_PHASE, roundEngine.getGameState().getCurrentPhase(),
                "Should be in point phase after establishing point");
            assertEquals(roll, roundEngine.getGameState().getPoint(),
                "Point should be set correctly");
            assertEquals(GameState.Status.PLAYING, roundEngine.getGameState().getGameStatus(),
                "Game should be PLAYING after point established");
            assertEquals(90, roundEngine.getPlayer().getBalance(),
                "Bet should be deducted from balance");
            
            // Roll until point or seven
            int pointRoll;
            do {
                pointRoll = roundEngine.rollDice();
                if (pointRoll == roll) {
                    assertTrue(roundEngine.isGameOver(),
                        "Game should be over after making point");
                    assertEquals(GameState.Status.WIN, roundEngine.getGameState().getGameStatus(),
                        "Game status should be WIN after making point");
                    assertEquals(110, roundEngine.getPlayer().getBalance(),
                        "Player should win their bet on point");
                    break;
                } else if (pointRoll == 7) {
                    assertTrue(roundEngine.isGameOver(),
                        "Game should be over after seven-out");
                    assertEquals(GameState.Status.LOSE, roundEngine.getGameState().getGameStatus(),
                        "Game status should be LOSE after seven-out");
                    assertEquals(90, roundEngine.getPlayer().getBalance(),
                        "Player should lose their bet on seven-out");
                    break;
                }
            } while (true);
        }
    }

    @Test
    void testGetters() {
        assertNotNull(roundEngine.getGameState(),
            "GameState should not be null");
        assertNotNull(roundEngine.getPlayer(),
            "Player should not be null");
    }
} 