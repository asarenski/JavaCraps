package org.asarenski.JavaCraps;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;

/**
 * Integration tests for the Main class.
 * Tests the complete game flow with simulated user input.
 */
public class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testCompleteGameFlow() {
        // Simulate a complete game session with:
        // 1. Bet amount: 10
        // 2. Multiple enters for rolls (more than needed to handle any game path)
        // 3. No to play again
        String input = "10\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nno\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        
        // Run the game with default player name and bankroll
        int exitCode = Main.startGame(new String[]{}, inputStream);
        assert(exitCode == 0) : "Game should exit with code 0";
        
        String output = outContent.toString();
        
        // Verify welcome message is shown
        assert(output.contains("Welcome to JavaCraps!")) : "Welcome message not found";
        
        // Verify game state is displayed (more specific checks)
        assert(output.contains("Player's Balance: $")) : "Player balance not found";
        assert(output.contains("Current Phase:")) : "Game phase not found";
        
        // Verify the game progressed (either won, lost, or asked to play again)
        assert(output.contains("You win") || output.contains("You lose") || 
               output.contains("Would you like to play another round?")) : 
               "Game did not progress to completion";
    }

    @Test
    public void testCustomPlayerAndBankroll() {
        // Simulate a complete game session with:
        // 1. Bet amount: 10
        // 2. Multiple enters for rolls (more than needed to handle any game path)
        // 3. No to play again
        String input = "10\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nno\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        
        // Run the game with custom player name and bankroll
        int exitCode = Main.startGame(new String[]{"--player", "TestPlayer", "--bankroll", "200"}, inputStream);
        assert(exitCode == 0) : "Game should exit with code 0";
        
        String output = outContent.toString();
        
        // Verify welcome message is shown
        assert(output.contains("Welcome to JavaCraps!")) : "Welcome message not found";
        
        // Verify game state is displayed with custom values
        assert(output.contains("TestPlayer's Balance: $")) : "Custom player name not found";
        assert(output.contains("Current Phase:")) : "Game phase not found";
        
        // Verify the game progressed (either won, lost, or asked to play again)
        assert(output.contains("You win") || output.contains("You lose") || 
               output.contains("Would you like to play another round?")) : 
               "Game did not progress to completion";
    }
} 