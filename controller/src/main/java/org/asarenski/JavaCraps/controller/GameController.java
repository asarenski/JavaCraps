package org.asarenski.JavaCraps.controller;

import org.asarenski.JavaCraps.core.GameEngine;
import org.asarenski.JavaCraps.core.Player;

/**
 * Controller class responsible for managing the game state and flow.
 */
public class GameController {
    private final GameEngine gameEngine;
    private final Player player;

    public GameController(Player player) {
        this.player = player;
        this.gameEngine = new GameEngine();
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Starts a new game round.
     * @param betAmount the amount the player wants to bet
     * @return true if the round was started successfully, false otherwise
     */
    public boolean startNewRound(double betAmount) {
        if (player.getBalance() < betAmount) {
            return false;
        }
        return gameEngine.placeBet((int)betAmount);
    }

    /**
     * Rolls the dice and processes the result.
     * @return true if the game continues, false if the round is over
     */
    public boolean roll() {
        int total = gameEngine.rollDice();
        return !gameEngine.isGameOver();
    }
} 