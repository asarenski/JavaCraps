package org.asarenski.JavaCraps.cli;

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

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CrapsGameCLI()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        GameEngine game = new GameEngine();
        Player player = game.getPlayer();
        
        System.out.println("Welcome to JavaCraps!");
        System.out.printf("Player balance: $%d%n", player.getBalance());
        
        // TODO: Implement game loop and user interaction
        System.out.println("Game implementation coming soon!");
    }
} 