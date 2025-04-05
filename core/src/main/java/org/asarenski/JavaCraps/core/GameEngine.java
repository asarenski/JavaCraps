package org.asarenski.JavaCraps.core;

import java.util.Random;

/**
 * Core game engine that manages the game flow and coordinates between different components.
 */
public class GameEngine {
    private final GameState gameState;
    private final Player player;
    private final Random random;

    public GameEngine() {
        this.gameState = new GameState();
        this.player = new Player();
        this.random = new Random();
    }

    /**
     * Resets the game to its initial state.
     */
    public void resetGame() {
        gameState.reset();
        player.reset();
    }

    /**
     * Attempts to place a bet for the current round.
     * @param amount The amount to bet
     * @return true if bet was successfully placed, false otherwise
     */
    public boolean placeBet(int amount) {
        return player.placeBet(amount);
    }

    /**
     * Rolls the dice and processes the outcome.
     * @return The total value of the dice roll
     */
    public int rollDice() {
        int die1 = random.nextInt(6) + 1;
        int die2 = random.nextInt(6) + 1;
        int total = die1 + die2;

        Boolean outcome = gameState.checkOutcome(total);
        if (outcome != null) {
            player.updateBalance(outcome);
        }

        return total;
    }

    /**
     * Gets the individual values of the last dice roll.
     * @return An array containing the values of both dice
     */
    public int[] getDiceValues() {
        int die1 = random.nextInt(6) + 1;
        int die2 = random.nextInt(6) + 1;
        return new int[]{die1, die2};
    }

    /**
     * Checks if the game is over (player has won or lost).
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return player.hasWon() || player.hasLost();
    }

    // Getters
    public GameState getGameState() {
        return gameState;
    }

    public Player getPlayer() {
        return player;
    }
} 