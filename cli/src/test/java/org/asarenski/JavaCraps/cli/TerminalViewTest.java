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
        GameState mockState = new GameState(); // Start in COME_OUT_ROLL phase
        view.showRollResult(3, 4, mockState);
        String output = outContent.toString();
        assertTrue(output.contains("You rolled a 3 and a 4"));
        assertTrue(output.contains("7")); // Check that total is present
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

    @Test
    void testShowRollResultComeOutWin() {
        GameState mockState = new GameState(); // Start in COME_OUT_ROLL phase
        view.showRollResult(4, 3, mockState); // Total 7 (natural win)
        String output = outContent.toString();
        assertTrue(output.contains(TerminalView.ANSI_GREEN + "7" + TerminalView.ANSI_RESET));
    }

    @Test
    void testShowRollResultComeOutLose() {
        GameState mockState = new GameState(); // Start in COME_OUT_ROLL phase
        view.showRollResult(1, 1, mockState); // Total 2 (craps)
        String output = outContent.toString();
        assertTrue(output.contains(TerminalView.ANSI_RED + "2" + TerminalView.ANSI_RESET));
    }

    @Test
    void testShowRollResultPointPhaseWin() {
        GameState mockState = new GameState();
        mockState.enterPointPhase(8);
        view.showRollResult(3, 5, mockState); // Total 8 (matches point)
        String output = outContent.toString();
        assertTrue(output.contains(TerminalView.ANSI_GREEN + "8" + TerminalView.ANSI_RESET));
    }

    @Test
    void testShowRollResultPointPhaseLose() {
        GameState mockState = new GameState();
        mockState.enterPointPhase(6);
        view.showRollResult(3, 4, mockState); // Total 7 (seven out)
        String output = outContent.toString();
        assertTrue(output.contains(TerminalView.ANSI_RED + "7" + TerminalView.ANSI_RESET));
    }

    @Test
    void testShowRollResultPointPhaseContinue() {
        GameState mockState = new GameState();
        mockState.enterPointPhase(6);
        view.showRollResult(2, 3, mockState); // Total 5 (continue)
        String output = outContent.toString();
        assertTrue(output.contains(TerminalView.ANSI_YELLOW + "5" + TerminalView.ANSI_RESET));
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
        view = new TerminalView();
    }
} 