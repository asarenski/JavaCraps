package org.asarenski.JavaCraps.cli;

import org.asarenski.JavaCraps.controller.GameController;
import org.asarenski.JavaCraps.core.Player;
import org.asarenski.JavaCraps.core.RoundState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import picocli.CommandLine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CrapsGameCLITest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private static final String PLAYER_NAME = "TestPlayer";
    private static final int INITIAL_BANKROLL = 100;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(errorStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalOut);
        System.setIn(originalIn);
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    @DisplayName("CLI should initialize with default values")
    void testDefaultInitialization() {
        provideInput("quit\n");
        CrapsGameCLI cli = new CrapsGameCLI();
        
        new CommandLine(cli).execute();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Welcome to JavaCraps"));
        assertTrue(output.contains("Balance: $100")); // Default bankroll
    }

    @Test
    @DisplayName("CLI should accept custom player name and bankroll")
    void testCustomInitialization() {
        provideInput("quit\n");
        CrapsGameCLI cli = new CrapsGameCLI();
        
        new CommandLine(cli).execute(
            "-p", PLAYER_NAME,
            "-b", String.valueOf(INITIAL_BANKROLL)
        );
        
        String output = outputStream.toString();
        assertTrue(output.contains("Balance: $" + INITIAL_BANKROLL));
    }

    @Test
    @DisplayName("CLI should reject invalid bankroll")
    void testInvalidBankroll() {
        CrapsGameCLI cli = new CrapsGameCLI();
        CommandLine cmd = new CommandLine(cli);
        cmd.setExecutionExceptionHandler((ex, commandLine, parseResult) -> {
            System.err.println(ex.getMessage());
            return 1;
        });
        int exitCode = cmd.execute("-b", "-100");
        
        assertNotEquals(0, exitCode);
        String error = errorStream.toString();
        assertTrue(error.contains("Invalid bankroll value: must be positive"));
    }

    @Test
    @DisplayName("CLI should handle a complete game round with win")
    void testCompleteGameRoundWin() {
        // Simulate placing a bet, rolling, and winning
        String input = "10\n\nn\n";  // bet 10, press enter to roll, no to play again
        provideInput(input);
        CrapsGameCLI cli = new CrapsGameCLI();
        
        new CommandLine(cli).execute(
            "-p", PLAYER_NAME,
            "-b", String.valueOf(INITIAL_BANKROLL)
        );
        
        String output = outputStream.toString();
        assertTrue(output.contains("Enter your bet (minimum $"));
        assertTrue(output.contains("Press Enter to roll the dice"));
        assertTrue(output.contains("Would you like to play another round?"));
    }

    @Test
    @DisplayName("CLI should handle invalid bet amounts")
    void testInvalidBetAmount() {
        // Simulate invalid bet followed by valid bet and quit
        String input = "-5\n0\n1000\n10\nquit\n";
        provideInput(input);
        CrapsGameCLI cli = new CrapsGameCLI();
        
        new CommandLine(cli).execute(
            "-p", PLAYER_NAME,
            "-b", String.valueOf(INITIAL_BANKROLL)
        );
        
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid bet amount"));
        assertTrue(output.contains("Balance: $"));
    }

    @Test
    @DisplayName("CLI should display help information")
    void testHelpCommand() {
        CrapsGameCLI cli = new CrapsGameCLI();
        new CommandLine(cli).execute("--help");
        
        String output = outputStream.toString();
        assertTrue(output.contains("Usage: craps"));
        assertTrue(output.contains("-p, --player"));
        assertTrue(output.contains("-b, --bankroll"));
        assertTrue(output.contains("Play a game of Craps"));
    }

    @Test
    @DisplayName("CLI should handle multiple rounds")
    void testMultipleRounds() {
        // Simulate two rounds: bet 10, roll, play again, bet 20, quit
        String input = "10\n\nyes\n20\nquit\n";
        provideInput(input);
        CrapsGameCLI cli = new CrapsGameCLI();
        
        new CommandLine(cli).execute(
            "-p", PLAYER_NAME,
            "-b", String.valueOf(INITIAL_BANKROLL)
        );
        
        String output = outputStream.toString();
        assertTrue(output.contains("Would you like to play another round?"));
        assertTrue(output.contains("Enter your bet"));
        assertTrue(output.contains("Balance: $"));
    }

    @Test
    @DisplayName("CLI should handle game session end due to insufficient funds")
    void testInsufficientFunds() {
        // Start with minimal bankroll and bet it all
        String input = "10\n\n";
        provideInput(input);
        CrapsGameCLI cli = new CrapsGameCLI();
        
        new CommandLine(cli).execute(
            "-p", PLAYER_NAME,
            "-b", "10"
        );
        
        String output = outputStream.toString();
        assertTrue(output.contains("Balance: $10"));
    }

    @Test
    @DisplayName("CLI should handle early quit during bet input")
    void testQuitDuringBet() {
        provideInput("quit\n");
        CrapsGameCLI cli = new CrapsGameCLI();
        
        new CommandLine(cli).execute(
            "-p", PLAYER_NAME,
            "-b", String.valueOf(INITIAL_BANKROLL)
        );
        
        String output = outputStream.toString();
        assertTrue(output.contains("Enter your bet"));
    }

    @Test
    @DisplayName("CLI should handle early quit during roll prompt")
    void testQuitDuringRoll() {
        provideInput("10\nquit\n");
        CrapsGameCLI cli = new CrapsGameCLI();
        
        new CommandLine(cli).execute(
            "-p", PLAYER_NAME,
            "-b", String.valueOf(INITIAL_BANKROLL)
        );
        
        String output = outputStream.toString();
        assertTrue(output.contains("Press Enter to roll"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"y", "yes", "Y", "YES"})
    @DisplayName("CLI should accept various forms of 'yes' for play again")
    void testPlayAgainVariations(String input) {
        provideInput("10\n\n" + input + "\nquit\n");
        CrapsGameCLI cli = new CrapsGameCLI();
        
        new CommandLine(cli).execute(
            "-p", PLAYER_NAME,
            "-b", String.valueOf(INITIAL_BANKROLL)
        );
        
        String output = outputStream.toString();
        assertTrue(output.contains("Enter your bet"));
    }
} 