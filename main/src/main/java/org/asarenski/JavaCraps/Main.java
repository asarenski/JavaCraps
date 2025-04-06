package org.asarenski.JavaCraps;

import org.asarenski.JavaCraps.cli.CrapsGameCLI;
import picocli.CommandLine;

/**
 * Main entry point for the JavaCraps game.
 * This class wires together all components and starts the game.
 */
public class Main {
    /**
     * Starts the game with the given command line arguments.
     * @param args Command line arguments
     * @return The exit code (0 for success, non-zero for failure)
     */
    public static int startGame(String[] args) {
        return new CommandLine(new CrapsGameCLI()).execute(args);
    }

    public static void main(String[] args) {
        System.exit(startGame(args));
    }
}