package org.asarenski.JavaCraps.core;

/**
 * Validates game moves and actions to ensure they follow game rules.
 */
public class MoveValidator {
    private final RoundState roundState;
    private final Player player;

    public MoveValidator(RoundState roundState, Player player) {
        this.roundState = roundState;
        this.player = player;
    }

    /**
     * Validates if a bet can be placed.
     * @param amount The bet amount to validate
     * @return true if the bet is valid, false otherwise
     */
    public boolean isValidBet(int amount) {
        // Check if bet meets minimum requirement and player has sufficient balance
        if (!player.canBet(amount)) {
            return false;
        }

        // Check if game state allows betting
        return roundState.getGameStatus() == RoundState.Status.PLAYING;
    }

    /**
     * Validates if a roll can be made.
     * @return true if a roll is allowed, false otherwise
     */
    public boolean canRoll() {
        // Can only roll if the game is in progress and a bet has been placed
        return roundState.getGameStatus() == RoundState.Status.PLAYING 
            && player.getCurrentBet() > 0;
    }

    /**
     * Checks if the game can continue.
     * @return true if the game can continue, false if it should end
     */
    public boolean canContinueGame() {
        // Game cannot continue if player has won or lost
        if (player.hasWon() || player.hasLost()) {
            return false;
        }

        // Game cannot continue if we're not in a valid game state
        return roundState.getGameStatus() == RoundState.Status.PLAYING;
    }

    public boolean canPlaceBet(int betAmount) {
        return roundState.getGameStatus() == RoundState.Status.PLAYING
                && player.canBet(betAmount);
    }

    public boolean canStartNewRound() {
        return roundState.getGameStatus() == RoundState.Status.PLAYING;
    }
} 