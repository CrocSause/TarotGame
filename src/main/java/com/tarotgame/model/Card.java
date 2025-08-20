// Card.java
package com.tarotgame.model;

import java.util.Objects;

/**
 * Represents a single Tarot card from the Major Arcana.
 * Core properties are immutable while orientation state is mutable.
 */
public class Card {
    private final int arcanaNumber;
    private final String name;
    private final String uprightMeaning;
    private final String reversedMeaning;
    private boolean isReversed;

    /**
     * Creates a new Card with the specified properties.
     * Card starts in upright position by default.
     * 
     * @param arcanaNumber The card's position in Major Arcana (0-21)
     * @param name The card's name (e.g., "The Fool")
     * @param uprightMeaning The interpretation when upright
     * @param reversedMeaning The interpretation when reversed
     */
    public Card(int arcanaNumber, String name, String uprightMeaning, String reversedMeaning) {
        this.arcanaNumber = arcanaNumber;
        this.name = name;
        this.uprightMeaning = uprightMeaning;
        this.reversedMeaning = reversedMeaning;
        this.isReversed = false; // Default to upright
    }

    // Getters for immutable properties
    public int getArcanaNumber() {
        return arcanaNumber;
    }

    public String getName() {
        return name;
    }

    public String getUprightMeaning() {
        return uprightMeaning;
    }

    public String getReversedMeaning() {
        return reversedMeaning;
    }

    public boolean isReversed() {
        return isReversed;
    }

    /**
     * Sets the card's orientation.
     * 
     * @param reversed true for reversed, false for upright
     */
    public void setReversed(boolean reversed) {
        this.isReversed = reversed;
    }

    /**
     * Flips the card's current orientation.
     */
    public void flip() {
        this.isReversed = !this.isReversed;
    }

    /**
     * Gets the current meaning based on card orientation.
     * 
     * @return The appropriate meaning for current orientation
     */
    public String getCurrentMeaning() {
        return isReversed ? reversedMeaning : uprightMeaning;
    }

    /**
     * Gets the display name including orientation indicator.
     * 
     * @return Name with "(Reversed)" suffix if applicable
     */
    public String getDisplayName() {
        return isReversed ? name + " (Reversed)" : name;
    }

    /**
     * Resets card to upright position.
     */
    public void resetOrientation() {
        this.isReversed = false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return arcanaNumber == card.arcanaNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(arcanaNumber);
    }

    @Override
    public String toString() {
        return String.format("Card{%d: %s%s}", 
            arcanaNumber, 
            name, 
            isReversed ? " (Reversed)" : "");
    }

    /**
     * Provides a detailed string representation including meaning.
     * 
     * @return Formatted string with name, orientation, and current meaning
     */
    public String toDetailedString() {
        return String.format("%s%n%s", 
            getDisplayName(), 
            getCurrentMeaning());
    }
}