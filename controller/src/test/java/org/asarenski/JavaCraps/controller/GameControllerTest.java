package org.asarenski.JavaCraps.controller;

import org.asarenski.JavaCraps.core.GameState;
import org.asarenski.JavaCraps.core.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    private GameController controller;
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
        controller = new GameController(player);
    }

    @Test
    void testInitialState() {
        assertNotNull(controller.getRoundEngine());
        assertNotNull(controller.getPlayer());
        assertEquals(GameState.Phase.COME_OUT_ROLL, controller.getGameState().getCurrentPhase());
        assertEquals(0, controller.getPoint());
        assertFalse(controller.isInPointPhase());
        assertFalse(controller.isRoundOver());
        assertFalse(controller.isGameSessionOver());
    }

    @Test
    void testStartNewRoundWithValidBet() {
        assertTrue(controller.startNewRound(10));
        assertEquals(90, player.getBalance()); // Starting balance 100 - bet 10
    }

    @Test
    void testStartNewRoundWithInvalidBet() {
        assertFalse(controller.startNewRound(0)); // Below minimum
        assertFalse(controller.startNewRound(1000)); // Above balance
        assertEquals(100, player.getBalance()); // Balance should remain unchanged
    }

    @Test
    void testRollWithoutBet() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            controller.roll();
        });
        assertTrue(exception.getMessage().contains("Cannot roll dice"));
    }

    @Test
    void testValidGameFlow() {
        // Place bet and start round
        assertTrue(controller.startNewRound(10));
        
        // Roll dice
        int roll = controller.roll();
        assertTrue(roll >= 2 && roll <= 12);
        
        // Check game state after roll
        if (roll == 7 || roll == 11) {
            assertTrue(controller.isRoundOver());
        } else if (roll == 2 || roll == 3 || roll == 12) {
            assertTrue(controller.isRoundOver());
        } else {
            assertEquals(roll, controller.getPoint());
            assertTrue(controller.isInPointPhase());
            assertFalse(controller.isRoundOver());
        }
    }

    @RepeatedTest(100)
    void testMultipleRollsStayInValidRange() {
        controller.startNewRound(10);
        int roll = controller.roll();
        assertTrue(roll >= 2 && roll <= 12, "Roll should be between 2 and 12");
    }

    @Test
    void testResetRound() {
        controller.startNewRound(10);
        controller.roll();
        controller.resetRound();
        
        assertEquals(GameState.Phase.COME_OUT_ROLL, controller.getGameState().getCurrentPhase());
        assertEquals(0, controller.getPoint());
        assertFalse(controller.isInPointPhase());
    }

    @Test
    void testGetMinimumBet() {
        assertTrue(controller.getMinimumBet() > 0);
        assertEquals(5, controller.getMinimumBet()); // Minimum bet should be 5
    }

    @Test
    void testPlayerStatePersistsBetweenRounds() {
        // Initial balance should be 100
        int startingBalance = player.getBalance();
        assertEquals(100, startingBalance);

        // First round
        assertTrue(controller.startNewRound(10));
        int balanceAfterFirstBet = player.getBalance();
        assertEquals(startingBalance - 10, balanceAfterFirstBet);
        controller.roll();
        controller.resetRound();
        int balanceAfterFirstRound = player.getBalance();

        // Second round - verify we can bet with our current balance
        assertTrue(controller.startNewRound(20));
        assertEquals(balanceAfterFirstRound - 20, player.getBalance());
        controller.roll();
        controller.resetRound();
        int balanceAfterSecondRound = player.getBalance();

        // Third round - verify we can still place bets
        assertTrue(controller.startNewRound(30));
        assertEquals(balanceAfterSecondRound - 30, player.getBalance());
    }
} 