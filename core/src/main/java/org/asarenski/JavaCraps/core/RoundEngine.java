package org.asarenski.JavaCraps.core;

/**
 * Core game engine that handles game logic and state management.
 */
public class RoundEngine {
    private final RoundState roundState;
    private final Player player;
    private final Dice dice;

    public RoundEngine(Player player) {
        this.roundState = new RoundState();
        this.player = player;
        this.dice = new Dice();
    }

    /**
     * Resets the round state, including game state and dice, but preserves player state.
     */
    public void resetRound() {
        roundState.reset();
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
        Boolean outcome = roundState.checkOutcome(total);
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
        return roundState.getGameStatus() == RoundState.Status.WIN ||
                roundState.getGameStatus() == RoundState.Status.LOSE;
    }

    // Getters
    public RoundState getRoundState() {
        return roundState;
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