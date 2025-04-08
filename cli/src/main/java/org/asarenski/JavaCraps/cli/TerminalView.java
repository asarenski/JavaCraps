package org.asarenski.JavaCraps.cli;

import org.asarenski.JavaCraps.core.RoundState;
import org.asarenski.JavaCraps.core.Player;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Handles all terminal-based user interactions and display formatting for the Craps game.
 */
public class TerminalView {
    private static final String SEPARATOR = "====================";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    private final Scanner scanner;

    /**
     * Creates a new TerminalView with System.in as the input source.
     */
    public TerminalView() {
        this(System.in);
    }

    /**
     * Creates a new TerminalView with a custom input source.
     * @param inputStream The input stream to read from
     */
    public TerminalView(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
    }

    /**
     * Displays the welcome message and game rules.
     */
    public void showWelcome() {
        System.out.println(SEPARATOR);
        System.out.println("Welcome to JavaCraps!");
        System.out.println("Type 'rules' at any time to view game rules, or 'quit' to exit.");
        System.out.println(SEPARATOR);
    }

    /**
     * Displays the current game state including balance and phase.
     * @param player The current player
     * @param roundState The current game state
     */
    public void displayGameState(Player player, RoundState roundState) {
        System.out.println("\nGame Status:");
        System.out.println("Balance: $" + player.getBalance());
        if (roundState.getCurrentPhase() == RoundState.Phase.POINT_PHASE) {
            System.out.println("Point: " + roundState.getPoint());
        }
        System.out.println(SEPARATOR);
    }

    /**
     * Prompts for and reads a bet amount from the user.
     * @param minimumBet The minimum allowed bet
     * @param balance The player's current balance
     * @return The bet amount entered by the user, or -1 for quit
     */
    public int getBetAmount(int minimumBet, int balance) {
        while (true) {
            System.out.printf("Enter your bet (minimum $%d, maximum $%d): ", minimumBet, balance);
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("quit")) {
                return -1;
            }
            if (input.equals("rules")) {
                showRules();
                continue;
            }
            
            try {
                int bet = Integer.parseInt(input);
                if (bet >= minimumBet && bet <= balance) {
                    return bet;
                }
                System.out.println(ANSI_RED + "Invalid bet amount. Please try again." + ANSI_RESET);
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "Please enter a valid number." + ANSI_RESET);
            }
        }
    }

    /**
     * Prompts the user to press Enter to roll the dice.
     * @return true to roll, false to quit
     */
    public boolean promptForRoll() {
        while (true) {
            System.out.print("Press Enter to roll the dice (or type 'quit' to exit): ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("quit")) {
                return false;
            }
            if (input.equals("rules")) {
                showRules();
                continue;
            }
            if (input.isEmpty()) {
                return true;
            }
        }
    }

    /**
     * Displays the result of a dice roll.
     * @param die1 First die value
     * @param die2 Second die value
     * @param roundState Current game state
     */
    public void showRollResult(int die1, int die2, boolean isComeOutRoll, int point) {
        int total = die1 + die2;
        String totalColor;
        if (isComeOutRoll) {
            if (total == 7 || total == 11) {
                totalColor = ANSI_GREEN;  // Natural win
            } else if (total == 2 || total == 3 || total == 12) {
                totalColor = ANSI_RED;    // Craps loss
            } else {
                totalColor = ANSI_YELLOW; // Point established
            }
        } else { // Point Phase
            if (total == point) {
                totalColor = ANSI_GREEN;  // Made the point
            } else if (total == 7) {
                totalColor = ANSI_RED;    // Seven out
            } else {
                totalColor = ANSI_YELLOW; // Continue rolling
            }
        }
        
        System.out.printf("Roll: %d + %d = %s%d%s (total)%n", 
            die1, die2, totalColor, total, ANSI_RESET);
    }

    /**
     * Displays the outcome of a round.
     * @param won Whether the player won
     * @param amount The amount won or lost
     */
    public void showRoundOutcome(boolean won, int amount) {
        String message = won ? 
            ANSI_GREEN + "You win $" + amount + "!" :
            ANSI_RED + "You lose $" + amount + "!";
        System.out.println(message + ANSI_RESET);
    }

    /**
     * Displays the game rules.
     */
    public void showRules() {
        System.out.println(SEPARATOR);
        System.out.println("CRAPS RULES:");
        System.out.println("1. Come Out Roll:");
        System.out.println("   - Roll 7 or 11: You WIN");
        System.out.println("   - Roll 2, 3, or 12: You LOSE");
        System.out.println("   - Roll 4, 5, 6, 8, 9, or 10: Sets the POINT");
        System.out.println("2. Point Phase:");
        System.out.println("   - Roll your point again: You WIN");
        System.out.println("   - Roll a 7: You LOSE");
        System.out.println("   - Any other roll: Continue rolling");
        System.out.println(SEPARATOR);
    }

    /**
     * Displays the final game outcome.
     * @param hasWon Whether the player won the game
     * @param finalBalance The player's final balance
     */
    public void showGameOutcome(boolean hasWon, int finalBalance) {
        System.out.println(SEPARATOR);
        if (hasWon) {
            System.out.println(ANSI_GREEN + "Congratulations! You've won the game!");
            System.out.printf("Final balance: $%d%n", finalBalance);
        } else {
            System.out.println(ANSI_RED + "Game Over! Better luck next time!");
            System.out.printf("Final balance: $%d%n", finalBalance);
        }
        System.out.println(ANSI_RESET + SEPARATOR);
    }

    /**
     * Asks if the player wants to play another round.
     * @return true if yes, false if no
     */
    public boolean askPlayAgain() {
        while (true) {
            System.out.print("Would you like to play another round? (yes/no): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("y")) {
                return true;
            }
            if (input.equals("no") || input.equals("n")) {
                return false;
            }
            System.out.println(ANSI_RED + "Please enter 'yes' or 'no'" + ANSI_RESET);
        }
    }

    /**
     * Formats the game phase for display.
     * @param phase The current game phase
     * @return A formatted string representing the phase
     */
    private String formatPhase(RoundState.Phase phase) {
        return phase == RoundState.Phase.COME_OUT_ROLL ? "Come Out Roll" : "Point Phase";
    }

    /**
     * Closes the scanner.
     */
    public void close() {
        scanner.close();
    }
} 