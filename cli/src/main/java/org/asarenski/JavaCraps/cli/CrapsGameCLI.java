package org.asarenski.JavaCraps.cli;

import org.asarenski.JavaCraps.controller.GameController;
import org.asarenski.JavaCraps.core.RoundEngine;
import org.asarenski.JavaCraps.core.Player;
import org.asarenski.JavaCraps.core.RoundState;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;

import java.io.InputStream;

@Command(
    name = "craps",
    mixinStandardHelpOptions = true,
    version = "1.0",
    description = "Play a game of Craps from the command line"
)
public class CrapsGameCLI implements Runnable {

    @Option(names = {"-p", "--player"}, description = "Player name", defaultValue = "Player")
    private String playerName;

    @Option(names = {"-b", "--bankroll"}, description = "Initial bankroll (must be positive)", defaultValue = "100")
    private int initialBankroll;

    private final TerminalView view;
    private GameController controller;

    public CrapsGameCLI() {
        this(System.in);
    }

    /**
     * Creates a new CrapsGameCLI with a custom input source.
     * @param inputStream The input stream to read from
     */
    public CrapsGameCLI(InputStream inputStream) {
        this.view = new TerminalView(inputStream);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CrapsGameCLI()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        if (initialBankroll <= 0) {
            throw new ParameterException(new CommandLine(this), "Invalid bankroll value: must be positive");
        }
        try {
            Player player = new Player(playerName, initialBankroll);
            this.controller = new GameController(player);
            view.showWelcome();
            playGame();
        } finally {
            view.close();
        }
    }

    private void playGame() {
        while (true) {
            playRound();
            
            // Ask to play again first
            if (!view.askPlayAgain()) {
                break;
            }
            
            // Then check if game is over
            if (controller.isGameSessionOver()) {
                view.showGameOutcome(
                    controller.getPlayer().hasWon(),
                    controller.getPlayer().getBalance()
                );
                break;
            }
            
            controller.resetRound();
        }
    }

    private void playRound() {
        view.displayGameState(controller.getPlayer(), controller.getRoundState());
        
        // Get bet
        int bet = view.getBetAmount(controller.getMinimumBet(), controller.getPlayer().getBalance());
        if (bet == -1) { // User wants to quit
            return;
        }
        
        // Start new round with bet
        if (!controller.startNewRound(bet)) {
            handleRoundOutcome(bet);
            return;
        }

        // Main game loop
        do {
            if (!view.promptForRoll()) {
                return; // User wants to quit
            }

            try {
                // Roll the dice and get values
                int roll = controller.roll();
                int[] diceValues = controller.getRoundEngine().getDiceValues();
                view.showRollResult(diceValues[0], diceValues[1], controller.getRoundState());
                
                if (controller.isRoundOver()) {
                    handleRoundOutcome(bet);
                    break;
                } else if (controller.isPointPhase()) {
                    view.displayGameState(controller.getPlayer(), controller.getRoundState());
                }
            } catch (IllegalStateException e) {
                // If we can't roll, show the outcome and end the round
                handleRoundOutcome(bet);
                return;
            }
        } while (!controller.isRoundOver());
    }

    private void handleRoundOutcome(int betAmount) {
        boolean isWin = controller.getRoundState().getGameStatus() == RoundState.Status.WIN;
        view.showRoundOutcome(isWin, betAmount);
    }
} 