package org.asarenski.JavaCraps.controller;

import org.asarenski.JavaCraps.core.RoundState;
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
        player = new Player("Test Player", 100);
        controller = new GameController(player);
    }

    @Test
    void testInitialState() {
        assertEquals(RoundState.Phase.COME_OUT_ROLL, controller.getRoundState().getCurrentPhase());
        assertEquals(100, controller.getPlayer().getBalance());
    }

    @Test
    void testStartNewRound() {
        assertTrue(controller.startNewRound(10));
        assertFalse(controller.startNewRound(1000)); // Invalid bet amount
    }

    @Test
    void testRoll() {
        // Cannot roll without placing a bet
        assertThrows(IllegalStateException.class, () -> controller.roll());

        // Place bet and roll
        controller.startNewRound(10);
        int roll = controller.roll();
        assertTrue(roll >= 2 && roll <= 12);
    }

    @Test
    void testGameFlow() {
        // Start new round
        assertTrue(controller.startNewRound(10));
        assertFalse(controller.isPointPhase());
        assertFalse(controller.isRoundOver());

        // Roll dice
        int roll = controller.roll();
        assertTrue(roll >= 2 && roll <= 12);
    }

    @Test
    void testResetRound() {
        controller.startNewRound(10);
        controller.roll();
        controller.resetRound();
        assertEquals(RoundState.Phase.COME_OUT_ROLL, controller.getRoundState().getCurrentPhase());
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