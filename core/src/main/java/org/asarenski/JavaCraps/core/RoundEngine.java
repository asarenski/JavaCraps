package org.asarenski.JavaCraps.core;

/**
 * Core engine that manages a single round of Craps and coordinates between different components.
 */
public class RoundEngine {
    private final GameState gameState;
    private final Player player;
    private final Dice dice;

    public RoundEngine(Player player) {
        this.gameState = new GameState();
        this.player = player;
        this.dice = new Dice();
    }

    /**
     * Resets the round state, including game state and dice, but preserves player state.
     */
    public void resetRound() {
        gameState.reset();
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

    /**
     * Gets the values of both dice from the last roll.
     * @return an array containing the values of both dice [die1, die2]
     */
    public int[] getDiceValues() {
        return new int[]{dice.getDie1(), dice.getDie2()};
    }
} 