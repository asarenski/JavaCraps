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
        // 2. Enter to roll
        // 3. Enter to roll again (in case we enter point phase)
        // 4. No to play again
        String input = "10\n\n\n\n\n\n\nno\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        
        // Run the game with default player name and bankroll
        int exitCode = Main.startGame(new String[]{}, inputStream);
        assert(exitCode == 0) : "Game should exit with code 0";
        
        String output = outContent.toString();
        
        // Verify welcome message is shown
        assert(output.contains("Welcome to JavaCraps!")) : "Welcome message not found";
        
        // Verify game state is displayed
        assert(output.contains("Player's Balance: $")) : "Player balance not found";
        
        // Verify game phase is displayed
        assert(output.contains("Current Phase:")) : "Game phase not found";
    }

    @Test
    public void testCustomPlayerAndBankroll() {
        // Simulate a complete game session with:
        // 1. Bet amount: 10
        // 2. Enter to roll
        // 3. Enter to roll again (in case we enter point phase)
        // 4. No to play again
        String input = "10\n\n\n\n\n\n\nno\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        
        // Run the game with custom player name and bankroll
        int exitCode = Main.startGame(new String[]{"--player", "TestPlayer", "--bankroll", "200"}, inputStream);
        assert(exitCode == 0) : "Game should exit with code 0";
        
        String output = outContent.toString();
        
        // Verify welcome message is shown
        assert(output.contains("Welcome to JavaCraps!")) : "Welcome message not found";
        
        // Verify game state is displayed
        assert(output.contains("Player's Balance: $")) : "Player balance not found";
        
        // Verify game phase is displayed
        assert(output.contains("Current Phase:")) : "Game phase not found";
    }
} 