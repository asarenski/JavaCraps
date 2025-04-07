package org.asarenski.JavaCraps.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {
    private GameEngine gameEngine;
    private Player player;
    private static final int TEST_BET = 10;

    @BeforeEach
    void setUp() {
        player = new Player();
        gameEngine = new GameEngine(player);
    }

    @Test
    void testInitialState() {
        assertEquals(0, gameEngine.getLastRoll(),
            "Initial last roll should be 0");
        assertFalse(gameEngine.isGameOver(),
            "Game should not be over initially");
        assertEquals(GameState.Phase.COME_OUT_ROLL, gameEngine.getGameState().getCurrentPhase(),
            "Game should start in come out roll phase");
        assertEquals(GameState.Status.PLAYING, gameEngine.getGameState().getGameStatus(),
            "Game status should be PLAYING initially");
    }

    @RepeatedTest(1000)
    void testRollDiceRange() {
        int roll = gameEngine.rollDice();
        assertTrue(roll >= 2 && roll <= 12,
            "Roll should be between 2 and 12, but was: " + roll);
    }

    @Test
    void testRollMatchesLastRoll() {
        int roll = gameEngine.rollDice();
        assertEquals(roll, gameEngine.getLastRoll(),
            "Last roll should match the result of rollDice()");
    }

    @Test
    void testResetGame() {
        // Make a roll and place a bet
        gameEngine.placeBet(TEST_BET);
        gameEngine.rollDice();
        
        // Reset the game
        gameEngine.resetGame();
        
        // Verify game state is reset
        assertFalse(gameEngine.isGameOver(),
            "Game should not be over after reset");
        assertEquals(GameState.Phase.COME_OUT_ROLL, gameEngine.getGameState().getCurrentPhase(),
            "Game should be in come out roll phase after reset");
        assertEquals(GameState.Status.PLAYING, gameEngine.getGameState().getGameStatus(),
            "Game status should be PLAYING after reset");
        assertEquals(100, gameEngine.getPlayer().getBalance(),
            "Player balance should be reset to starting value");
    }

    @Test
    void testPlaceBet() {
        assertTrue(gameEngine.placeBet(TEST_BET),
            "Should be able to place valid bet");
        assertFalse(gameEngine.placeBet(-5),
            "Should not be able to place negative bet");
        assertFalse(gameEngine.placeBet(101),
            "Should not be able to place bet larger than balance");
    }

    @RepeatedTest(100)
    void testGameOutcomes() {
        gameEngine.placeBet(TEST_BET);
        int roll = gameEngine.rollDice();
        
        if (roll == 7 || roll == 11) {
            // Natural win
            assertTrue(gameEngine.isGameOver(),
                "Game should be over after natural win");
            assertEquals(GameState.Status.WIN, gameEngine.getGameState().getGameStatus(),
                "Game status should be WIN after natural");
            assertEquals(110, gameEngine.getPlayer().getBalance(),
                "Player should win their bet on natural");
        } else if (roll == 2 || roll == 3 || roll == 12) {
            // Craps loss
            assertTrue(gameEngine.isGameOver(),
                "Game should be over after craps");
            assertEquals(GameState.Status.LOSE, gameEngine.getGameState().getGameStatus(),
                "Game status should be LOSE after craps");
            assertEquals(90, gameEngine.getPlayer().getBalance(),
                "Player should lose their bet on craps");
        } else {
            // Point established
            assertEquals(GameState.Phase.POINT_PHASE, gameEngine.getGameState().getCurrentPhase(),
                "Should be in point phase after establishing point");
            assertEquals(roll, gameEngine.getGameState().getPoint(),
                "Point should be set correctly");
            assertEquals(GameState.Status.PLAYING, gameEngine.getGameState().getGameStatus(),
                "Game should be PLAYING after point established");
            assertEquals(90, gameEngine.getPlayer().getBalance(),
                "Bet should be deducted from balance");
            
            // Roll until point or seven
            int pointRoll;
            do {
                pointRoll = gameEngine.rollDice();
                if (pointRoll == roll) {
                    assertTrue(gameEngine.isGameOver(),
                        "Game should be over after making point");
                    assertEquals(GameState.Status.WIN, gameEngine.getGameState().getGameStatus(),
                        "Game status should be WIN after making point");
                    assertEquals(110, gameEngine.getPlayer().getBalance(),
                        "Player should win their bet on point");
                    break;
                } else if (pointRoll == 7) {
                    assertTrue(gameEngine.isGameOver(),
                        "Game should be over after seven-out");
                    assertEquals(GameState.Status.LOSE, gameEngine.getGameState().getGameStatus(),
                        "Game status should be LOSE after seven-out");
                    assertEquals(90, gameEngine.getPlayer().getBalance(),
                        "Player should lose their bet on seven-out");
                    break;
                }
            } while (true);
        }
    }

    @Test
    void testGetters() {
        assertNotNull(gameEngine.getGameState(),
            "GameState should not be null");
        assertNotNull(gameEngine.getPlayer(),
            "Player should not be null");
    }
} 