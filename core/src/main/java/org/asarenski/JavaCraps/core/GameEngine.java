package org.asarenski.JavaCraps.core;

/**
 * Core game engine that manages the game flow and coordinates between different components.
 */
public class GameEngine {
    private final GameState gameState;
    private final Player player;
    private final Dice dice;

    public GameEngine() {
        this.gameState = new GameState();
        this.player = new Player();
        this.dice = new Dice();
    }

    /**
     * Resets the game to its initial state.
     */
    public void resetGame() {
        gameState.reset();
        player.reset();
        dice.reset();
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
        int total = dice.roll();
        Boolean outcome = gameState.checkOutcome(total);
        if (outcome != null) {
            player.updateBalance(outcome);
        }
        return total;
    }

    /**
     * Gets the total value of the last dice roll.
     * @return The total of the last dice roll
     */
    public int getLastRoll() {
        return dice.getValue();
    }

    /**
     * Checks if the game is over (player has won or lost).
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return player.hasWon() || player.hasLost() || 
               gameState.getGameStatus() == GameState.Status.WIN || 
               gameState.getGameStatus() == GameState.Status.LOSE;
    }

    // Getters
    public GameState getGameState() {
        return gameState;
    }

    public Player getPlayer() {
        return player;
    }
} 