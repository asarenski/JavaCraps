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
        player = new Player("Test Player", 100);
        roundEngine = new RoundEngine(player);
    }

    @Test
    void testInitialState() {
        assertNotNull(roundEngine.getRoundState(), "RoundState should not be null");
        assertEquals(RoundState.Phase.COME_OUT_ROLL, roundEngine.getRoundState().getCurrentPhase(),
                "Initial phase should be COME_OUT_ROLL");
        assertEquals(RoundState.Status.PLAYING, roundEngine.getRoundState().getGameStatus(),
                "Initial status should be PLAYING");
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
        // First make some changes to the state
        roundEngine.placeBet(TEST_BET);
        roundEngine.rollDice();

        // Then reset
        roundEngine.resetRound();

        // Verify reset state
        assertEquals(RoundState.Phase.COME_OUT_ROLL, roundEngine.getRoundState().getCurrentPhase(),
                "Phase should be reset to COME_OUT_ROLL");
        assertEquals(RoundState.Status.PLAYING, roundEngine.getRoundState().getGameStatus(),
                "Status should be reset to PLAYING");
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
            assertEquals(RoundState.Status.WIN, roundEngine.getRoundState().getGameStatus(),
                "Game status should be WIN after natural");
            assertEquals(110, roundEngine.getPlayer().getBalance(),
                "Player should win their bet on natural");
        } else if (roll == 2 || roll == 3 || roll == 12) {
            // Craps loss
            assertTrue(roundEngine.isGameOver(),
                "Game should be over after craps");
            assertEquals(RoundState.Status.LOSE, roundEngine.getRoundState().getGameStatus(),
                "Game status should be LOSE after craps");
            assertEquals(90, roundEngine.getPlayer().getBalance(),
                "Player should lose their bet on craps");
        } else {
            // Point established
            assertEquals(RoundState.Phase.POINT_PHASE, roundEngine.getRoundState().getCurrentPhase(),
                "Should be in point phase after establishing point");
            assertEquals(roll, roundEngine.getRoundState().getPoint(),
                "Point should be set correctly");
            assertEquals(RoundState.Status.PLAYING, roundEngine.getRoundState().getGameStatus(),
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
                    assertEquals(RoundState.Status.WIN, roundEngine.getRoundState().getGameStatus(),
                        "Game status should be WIN after making point");
                    assertEquals(110, roundEngine.getPlayer().getBalance(),
                        "Player should win their bet on point");
                    break;
                } else if (pointRoll == 7) {
                    assertTrue(roundEngine.isGameOver(),
                        "Game should be over after seven-out");
                    assertEquals(RoundState.Status.LOSE, roundEngine.getRoundState().getGameStatus(),
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
        assertNotNull(roundEngine.getRoundState(),
            "RoundState should not be null");
        assertNotNull(roundEngine.getPlayer(),
            "Player should not be null");
    }

    @Test
    void testWinningComeOutRoll() {
        roundEngine.placeBet(TEST_BET);
        int roll;
        do {
            roundEngine.resetRound();
            roundEngine.placeBet(TEST_BET);
            roll = roundEngine.rollDice();
        } while (roll != 7 && roll != 11);

        assertEquals(RoundState.Status.WIN, roundEngine.getRoundState().getGameStatus(),
                "Status should be WIN after rolling 7 or 11");
    }

    @Test
    void testLosingComeOutRoll() {
        roundEngine.placeBet(TEST_BET);
        int roll;
        do {
            roundEngine.resetRound();
            roundEngine.placeBet(TEST_BET);
            roll = roundEngine.rollDice();
        } while (roll != 2 && roll != 3 && roll != 12);

        assertEquals(RoundState.Status.LOSE, roundEngine.getRoundState().getGameStatus(),
                "Status should be LOSE after rolling 2, 3, or 12");
    }

    @Test
    void testPointPhaseEntry() {
        roundEngine.placeBet(TEST_BET);
        int roll;
        do {
            roundEngine.resetRound();
            roundEngine.placeBet(TEST_BET);
            roll = roundEngine.rollDice();
        } while (roll == 7 || roll == 11 || roll == 2 || roll == 3 || roll == 12);

        assertEquals(RoundState.Phase.POINT_PHASE, roundEngine.getRoundState().getCurrentPhase(),
                "Should enter POINT_PHASE after rolling a point number");
        assertEquals(RoundState.Status.PLAYING, roundEngine.getRoundState().getGameStatus(),
                "Status should remain PLAYING in point phase");
    }

    @Test
    void testWinningPointPhase() {
        // Enter point phase first
        roundEngine.placeBet(TEST_BET);
        int point;
        do {
            roundEngine.resetRound();
            roundEngine.placeBet(TEST_BET);
            point = roundEngine.rollDice();
        } while (point == 7 || point == 11 || point == 2 || point == 3 || point == 12);

        // Roll until we hit the point
        int roll;
        do {
            roll = roundEngine.rollDice();
        } while (roll != point);

        assertEquals(RoundState.Status.WIN, roundEngine.getRoundState().getGameStatus(),
                "Status should be WIN after rolling the point");
    }

    @Test
    void testLosingPointPhase() {
        // Enter point phase first
        roundEngine.placeBet(TEST_BET);
        do {
            roundEngine.resetRound();
            roundEngine.placeBet(TEST_BET);
            roundEngine.rollDice();
        } while (roundEngine.getRoundState().getCurrentPhase() != RoundState.Phase.POINT_PHASE);

        // Roll until we get a 7
        int roll;
        do {
            roll = roundEngine.rollDice();
        } while (roll != 7);

        assertEquals(RoundState.Status.LOSE, roundEngine.getRoundState().getGameStatus(),
                "Status should be LOSE after rolling 7 in point phase");
    }
} 