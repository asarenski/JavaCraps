package org.asarenski.JavaCraps.model;

/**
 * Represents a betting token in the Craps game.
 */
public class Token {
    private double value;
    private TokenType type;

    public enum TokenType {
        PASS_LINE,
        DONT_PASS
    }

    public Token(double value, TokenType type) {
        this.value = value;
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public TokenType getType() {
        return type;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s bet: $%.2f", type.toString(), value);
    }
} 