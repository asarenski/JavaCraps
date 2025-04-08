package org.asarenski.JavaCraps.cli;

import org.asarenski.JavaCraps.core.RoundState;
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
    private Player player;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        view = new TerminalView();
        player = new Player("TestPlayer", 100);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testDisplayGameState() {
        RoundState roundState = new RoundState();
        view.displayGameState(player, roundState);
        String output = outContent.toString();
        assertTrue(output.contains("Balance: $100"));
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
        assertTrue(output.contains(TerminalView.ANSI_RED + "Invalid bet amount. Please try again." + TerminalView.ANSI_RESET));
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
        assertTrue(output.contains("CRAPS RULES"));
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
        boolean isComeOutRoll = true;   
        view.showRollResult(3, 4, isComeOutRoll, 0);
        String output = outContent.toString().trim();
        assertEquals("Roll: 3 + 4 = " + TerminalView.ANSI_GREEN + "7" + TerminalView.ANSI_RESET + " (total)", output);
    }

    @Test
    void testShowRoundOutcomeWin() {
        view.showRoundOutcome(true, 50);
        String output = outContent.toString().trim();
        assertTrue(output.contains("You win $50!"));
    }

    @Test
    void testShowRoundOutcomeLose() {
        view.showRoundOutcome(false, 50);
        String output = outContent.toString().trim();
        assertTrue(output.contains("You lose $50!"));
    }

    @Test
    void testShowRollResultComeOutRollNatural() {
        view.showRollResult(4, 3, true, 0);
        String output = outContent.toString().trim();
        assertEquals("Roll: 4 + 3 = " + TerminalView.ANSI_GREEN + "7" + TerminalView.ANSI_RESET + " (total)", output);
    }

    @Test
    void testShowRollResultComeOutWin() {
        view.showRollResult(7, 4, true, 0);
        String output = outContent.toString().trim();
        assertEquals("Roll: 7 + 4 = " + TerminalView.ANSI_GREEN + "11" + TerminalView.ANSI_RESET + " (total)", output);
    }

    @Test
    void testShowRollResultComeOutLose() {
        view.showRollResult(1, 1, true, 0);
        String output = outContent.toString().trim();
        assertEquals("Roll: 1 + 1 = " + TerminalView.ANSI_RED + "2" + TerminalView.ANSI_RESET + " (total)", output);
    }

    @Test
    void testShowRollResultPointPhaseWin() {
        view.showRollResult(3, 5, false, 8);
        String output = outContent.toString().trim();
        assertEquals("Roll: 3 + 5 = " + TerminalView.ANSI_GREEN + "8" + TerminalView.ANSI_RESET + " (total)", output);
    }

    @Test
    void testShowRollResultPointPhaseLose() {
        view.showRollResult(3, 4, false, 6);
        String output = outContent.toString().trim();
        assertEquals("Roll: 3 + 4 = " + TerminalView.ANSI_RED + "7" + TerminalView.ANSI_RESET + " (total)", output);
    }

    @Test
    void testShowRollResultPointPhaseContinue() {
        view.showRollResult(2, 3, false, 6);
        String output = outContent.toString().trim();
        assertEquals("Roll: 2 + 3 = " + TerminalView.ANSI_YELLOW + "5" + TerminalView.ANSI_RESET + " (total)", output);
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
        view = new TerminalView();
    }
} 