package org.asarenski.JavaCraps;

import org.asarenski.JavaCraps.cli.CrapsGameCLI;
import picocli.CommandLine;

import java.io.InputStream;

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
        return startGame(args, System.in);
    }

    /**
     * Starts the game with the given command line arguments and input stream.
     * @param args Command line arguments
     * @param inputStream The input stream to read from
     * @return The exit code (0 for success, non-zero for failure)
     */
    public static int startGame(String[] args, InputStream inputStream) {
        return new CommandLine(new CrapsGameCLI(inputStream)).execute(args);
    }

    public static void main(String[] args) {
        System.exit(startGame(args));
    }
}