package org.asarenski.JavaCraps.core;

/**
 * Represents a player in the Craps game, managing their balance and bets.
 */
public class Player {
    private static final int STARTING_BALANCE = 100;
    private static final int MINIMUM_BET = 5;
    private static final int WINNING_BALANCE = 1000;

    private final String name;
    private int balance;
    private int currentBet;

    /**
     * Creates a new player with default name and starting balance.
     */
    public Player() {
        this("Player", STARTING_BALANCE);
    }

    /**
     * Creates a new player with the specified name and initial balance.
     * @param name The player's name
     * @param initialBalance The player's initial balance
     */
    public Player(String name, int initialBalance) {
        this.name = name;
        this.balance = initialBalance;
        this.currentBet = 0;
    }

    /**
     * Resets the player's state to initial values.
     */
    public void reset() {
        balance = STARTING_BALANCE;
        currentBet = 0;
    }

    /**
     * Attempts to place a bet.
     * @param amount The amount to bet
     * @return true if bet was successfully placed, false otherwise
     */
    public boolean placeBet(int amount) {
        if (!canBet(amount)) {
            return false;
        }
        currentBet = amount;
        balance -= amount;
        return true;
    }

    /**
     * Updates the player's balance based on game outcome.
     * @param won true if the player won, false if they lost
     */
    public void updateBalance(boolean won) {
        if (won) {
            balance += currentBet * 2; // Return original bet plus equal amount in winnings
        }
        currentBet = 0;
    }

    /**
     * Checks if the player can place the specified bet.
     * @param amount The amount to check
     * @return true if the bet can be placed, false otherwise
     */
    public boolean canBet(int amount) {
        return amount >= MINIMUM_BET && amount <= balance;
    }

    /**
     * Checks if the player has won the game by reaching the winning balance.
     * @return true if the player has won, false otherwise
     */
    public boolean hasWon() {
        return balance >= WINNING_BALANCE;
    }

    /**
     * Checks if the player has lost the game by running out of money.
     * @return true if the player has lost, false otherwise
     */
    public boolean hasLost() {
        return balance <= 0;
    }

    /**
     * Copies the state from another player instance.
     * @param other The player to copy state from
     */
    public void copyState(Player other) {
        this.balance = other.balance;
        this.currentBet = other.currentBet;
    }

    /**
     * Gets the player's name.
     * @return The player's name
     */
    public String getName() {
        return name;
    }

    // Getters
    public int getBalance() {
        return balance;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public static int getMinimumBet() {
        return MINIMUM_BET;
    }
} 