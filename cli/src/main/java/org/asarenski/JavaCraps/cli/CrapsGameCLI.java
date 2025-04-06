package org.asarenski.JavaCraps.cli;

import org.asarenski.JavaCraps.controller.GameController;
import org.asarenski.JavaCraps.core.GameEngine;
import org.asarenski.JavaCraps.core.Player;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "craps",
    mixinStandardHelpOptions = true,
    version = "1.0",
    description = "Play a game of Craps from the command line"
)
public class CrapsGameCLI implements Runnable {

    @Option(names = {"-p", "--player"}, description = "Player name", defaultValue = "Player")
    private String playerName;

    @Option(names = {"-b", "--bankroll"}, description = "Initial bankroll", defaultValue = "100")
    private int initialBankroll;

    private final TerminalView view;
    private final GameController controller;

    public CrapsGameCLI() {
        this.view = new TerminalView();
        Player player = new Player();
        this.controller = new GameController(player);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CrapsGameCLI()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        try {
            view.showWelcome();
            playGame();
        } finally {
            view.close();
        }
    }

    private void playGame() {
        while (!controller.isGameSessionOver()) {
            playRound();
            if (controller.isGameSessionOver()) {
                view.showGameOutcome(
                    controller.getPlayer().hasWon(),
                    controller.getPlayer().getBalance()
                );
                break;
            }
            if (!view.askPlayAgain()) {
                break;
            }
            controller.resetRound();
        }
    }

    private void playRound() {
        view.displayGameState(controller.getPlayer(), controller.getGameState());
        
        // Get bet
        int bet = view.getBetAmount(controller.getMinimumBet(), controller.getPlayer().getBalance());
        if (bet == -1) { // User wants to quit
            return;
        }
        
        if (!controller.startNewRound(bet)) {
            view.showRoundOutcome(false, bet);
            return;
        }

        // Main game loop
        while (!controller.isRoundOver()) {
            if (!view.promptForRoll()) {
                return; // User wants to quit
            }

            int[] diceValues = controller.getGameEngine().getDiceValues();
            view.showRollResult(diceValues[0], diceValues[1]);
            
            int roll = controller.roll();
            
            if (controller.isRoundOver()) {
                boolean won = controller.getGameState().getGameStatus() == 
                    org.asarenski.JavaCraps.core.GameState.Status.WIN;
                view.showRoundOutcome(won, bet);
            } else if (controller.isInPointPhase()) {
                view.displayGameState(controller.getPlayer(), controller.getGameState());
            }
        }
    }
} 