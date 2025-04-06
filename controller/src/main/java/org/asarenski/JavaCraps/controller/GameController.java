package org.asarenski.JavaCraps.controller;

import org.asarenski.JavaCraps.core.GameEngine;
import org.asarenski.JavaCraps.core.GameState;
import org.asarenski.JavaCraps.core.Player;
import org.asarenski.JavaCraps.core.MoveValidator;

/**
 * Controller class responsible for managing the game state and flow.
 * Coordinates between the game engine, player, and move validation.
 */
public class GameController {
    private final GameEngine gameEngine;
    private final Player player;
    private MoveValidator moveValidator;

    /**
     * Creates a new GameController with the specified player.
     * @param player the player for this game session
     */
    public GameController(Player player) {
        this.player = player;
        this.gameEngine = new GameEngine();
        this.gameEngine.getPlayer().copyState(player);
        this.moveValidator = new MoveValidator(gameEngine.getGameState(), player);
    }

    /**
     * Gets the current game engine instance.
     * @return the game engine
     */
    public GameEngine getGameEngine() {
        return gameEngine;
    }

    /**
     * Gets the current player instance.
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the current game state.
     * @return the current game state
     */
    public GameState getGameState() {
        return gameEngine.getGameState();
    }

    /**
     * Starts a new game round with the specified bet amount.
     * @param betAmount the amount the player wants to bet
     * @return true if the round was started successfully, false otherwise
     */
    public boolean startNewRound(double betAmount) {
        resetRound();
        if (!moveValidator.isValidBet((int)betAmount)) {
            return false;
        }
        if (!player.placeBet((int)betAmount)) {
            return false;
        }
        gameEngine.getPlayer().copyState(player);
        this.moveValidator = new MoveValidator(gameEngine.getGameState(), gameEngine.getPlayer());
        return true;
    }

    /**
     * Rolls the dice and processes the result.
     * @return the total value of the dice roll
     * @throws IllegalStateException if a roll is attempted when not allowed
     */
    public int roll() {
        if (!moveValidator.canRoll()) {
            throw new IllegalStateException("Cannot roll dice at this time");
        }
        int result = gameEngine.rollDice();
        player.copyState(gameEngine.getPlayer());
        return result;
    }

    /**
     * Gets the current point value.
     * @return the point value, or 0 if in come out roll phase
     */
    public int getPoint() {
        return gameEngine.getGameState().getPoint();
    }

    /**
     * Checks if the game is in the point phase.
     * @return true if in point phase, false if in come out roll
     */
    public boolean isInPointPhase() {
        return gameEngine.getGameState().getCurrentPhase() == GameState.Phase.POINT_PHASE;
    }

    /**
     * Checks if the current round is over.
     * @return true if the round is over, false otherwise
     */
    public boolean isRoundOver() {
        return gameEngine.getGameState().getGameStatus() != GameState.Status.PLAYING;
    }

    /**
     * Checks if the game session is over (player has won or lost).
     * @return true if the game session is over, false otherwise
     */
    public boolean isGameSessionOver() {
        return player.hasWon() || player.hasLost();
    }

    /**
     * Gets the minimum bet amount allowed.
     * @return the minimum bet amount
     */
    public int getMinimumBet() {
        return Player.getMinimumBet();
    }

    /**
     * Resets the game state for a new round.
     */
    public void resetRound() {
        gameEngine.resetGame();
        gameEngine.getPlayer().copyState(player);
    }
} 