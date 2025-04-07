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
        RoundState mockState = new RoundState();
        view.showRollResult(3, 4, mockState);
        String output = outContent.toString();
        assertTrue(output.contains("Roll: 3 + 4 = 7"));
    }

    @Test
    void testShowRoundOutcomeWin() {
        view.showRoundOutcome(true, 50);
        String output = outContent.toString();
        assertTrue(output.contains("win $50"));
    }

    @Test
    void testShowRoundOutcomeLose() {
        view.showRoundOutcome(false, 50);
        String output = outContent.toString();
        assertTrue(output.contains("lose $50"));
    }

    @Test
    void testShowRollResultComeOutRollNatural() {
        RoundState mockState = new RoundState();
        view.showRollResult(4, 3, mockState);
        String output = outContent.toString();
        assertTrue(output.contains("Roll: 4 + 3 = 7"));
    }

    @Test
    void testShowRollResultComeOutWin() {
        RoundState mockState = new RoundState();
        view.showRollResult(4, 3, mockState);
        String output = outContent.toString();
        assertTrue(output.contains("total)"));
    }

    @Test
    void testShowRollResultComeOutLose() {
        RoundState mockState = new RoundState();
        view.showRollResult(1, 1, mockState);
        String output = outContent.toString();
        assertTrue(output.contains("total)"));
    }

    @Test
    void testShowRollResultPointPhaseWin() {
        RoundState mockState = new RoundState();
        mockState.enterPointPhase(8);
        view.showRollResult(3, 5, mockState);
        String output = outContent.toString();
        assertTrue(output.contains("total)"));
    }

    @Test
    void testShowRollResultPointPhaseLose() {
        RoundState mockState = new RoundState();
        mockState.enterPointPhase(6);
        view.showRollResult(3, 4, mockState);
        String output = outContent.toString();
        assertTrue(output.contains("total)"));
    }

    @Test
    void testShowRollResultPointPhaseContinue() {
        RoundState mockState = new RoundState();
        mockState.enterPointPhase(6);
        view.showRollResult(2, 3, mockState);
        String output = outContent.toString();
        assertTrue(output.contains("total)"));
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
        view = new TerminalView();
    }
} 