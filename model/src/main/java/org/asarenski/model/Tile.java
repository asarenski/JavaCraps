package org.asarenski.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a tile on the Craps game board that can hold betting tokens.
 */
public class Tile {
    private final Coordinates coordinates;
    private final List<Token> tokens;
    private final String label;

    public Tile(Coordinates coordinates, String label) {
        this.coordinates = coordinates;
        this.label = label;
        this.tokens = new ArrayList<>();
    }

    public void addToken(Token token) {
        tokens.add(token);
    }

    public void removeToken(Token token) {
        tokens.remove(token);
    }

    public void clearTokens() {
        tokens.clear();
    }

    public List<Token> getTokens() {
        return new ArrayList<>(tokens);
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getLabel() {
        return label;
    }

    public double getTotalBetValue() {
        return tokens.stream()
                .mapToDouble(Token::getValue)
                .sum();
    }

    @Override
    public String toString() {
        return String.format("%s at %s with total bet: $%.2f", 
            label, coordinates, getTotalBetValue());
    }
} 