package org.asarenski.JavaCraps.cli;

import org.asarenski.JavaCraps.core.GameState;
import org.asarenski.JavaCraps.core.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TerminalViewTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private TerminalView view;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        view = new TerminalView();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testDisplayGameState() {
        Player player = new Player();
        GameState gameState = new GameState();
        
        view.displayGameState(player, gameState);
        
        String output = outContent.toString();
        assertTrue(output.contains("Balance: $100"));
        assertTrue(output.contains("COME OUT ROLL"));
    }

    @Test
    void testGetBetAmountValidInput() {
        provideInput("50\n");
        int bet = view.getBetAmount(5, 100);
        assertEquals(50, bet);
    }

    @Test
    void testGetBetAmountInvalidThenValid() {
        provideInput("3\n200\n50\n");
        int bet = view.getBetAmount(5, 100);
        assertEquals(50, bet);
        
        String output = outContent.toString();
        assertTrue(output.contains("Invalid bet amount"));
    }

    @Test
    void testGetBetAmountQuit() {
        provideInput("quit\n");
        int bet = view.getBetAmount(5, 100);
        assertEquals(-1, bet);
    }

    @Test
    void testPromptForRollValid() {
        provideInput("\n");
        assertTrue(view.promptForRoll());
    }

    @Test
    void testPromptForRollQuit() {
        provideInput("quit\n");
        assertFalse(view.promptForRoll());
    }

    @Test
    void testShowRules() {
        view.showRules();
        String output = outContent.toString();
        assertTrue(output.contains("CRAPS RULES:"));
        assertTrue(output.contains("Come Out Roll:"));
        assertTrue(output.contains("Point Phase:"));
    }

    @Test
    void testAskPlayAgainYes() {
        provideInput("yes\n");
        assertTrue(view.askPlayAgain());
    }

    @Test
    void testAskPlayAgainNo() {
        provideInput("no\n");
        assertFalse(view.askPlayAgain());
    }

    @Test
    void testShowRollResult() {
        view.showRollResult(3, 4);
        String output = outContent.toString();
        assertTrue(output.contains("You rolled a 3 and a 4 (7 total)"));
    }

    @Test
    void testShowRoundOutcomeWin() {
        view.showRoundOutcome(true, 50);
        String output = outContent.toString();
        assertTrue(output.contains("win $50!"));
    }

    @Test
    void testShowRoundOutcomeLose() {
        view.showRoundOutcome(false, 50);
        String output = outContent.toString();
        assertTrue(output.contains("lose $50!"));
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
        view = new TerminalView();
    }
} 