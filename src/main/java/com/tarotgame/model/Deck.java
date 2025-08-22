// Deck.java
package com.tarotgame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Manages a deck of Major Arcana tarot cards with shuffling and drawing functionality.
 * 
 * Key behaviors:
 * - Cards are consumed (removed) when drawn
 * - Auto-reshuffles when fewer than 3 cards remain before drawing
 * - All cards start in upright orientation (reading logic handles reversed state)
 * - Maintains separate collections for total deck and available cards
 */
public class Deck {
    private final List<Card> fullDeck;      // Complete deck reference (immutable)
    private final List<Card> availableCards; // Cards currently available for drawing
    private final Random random;
    
    // Constants
    private static final int MINIMUM_CARDS_FOR_READING = 3;
    
    /**
     * Creates a new deck with all Major Arcana cards.
     * Deck is automatically shuffled upon creation.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Deck() {
        this.random = new Random();
        this.fullDeck = MajorArcana.createFullDeck();
        this.availableCards = new ArrayList<>();
        reset(); // Initialize with shuffled deck
    }
    
    /**
     * Creates a deck with a specific random seed (useful for testing).
     * 
     * @param seed Random seed for reproducible shuffling
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Deck(long seed) {
        this.random = new Random(seed);
        this.fullDeck = MajorArcana.createFullDeck();
        this.availableCards = new ArrayList<>();
        reset(); // Initialize with shuffled deck
    }
    
    /**
     * Draws a single card from the deck.
     * Automatically reshuffles if fewer than 3 cards remain.
     * 
     * @return A card from the deck
     * @throws IllegalStateException if deck cannot be initialized (should not happen)
     */
    public Card drawCard() {
        // Check if we need to reshuffle before drawing
        if (availableCards.size() < MINIMUM_CARDS_FOR_READING) {
            reset(); // This will reshuffle the full deck
        }
        
        // Draw from the end of the list for O(1) performance
        return availableCards.remove(availableCards.size() - 1);
    }
    
    /**
     * Draws multiple cards from the deck.
     * Each card is removed as it's drawn.
     * 
     * @param count Number of cards to draw
     * @return List of drawn cards in the order they were drawn
     * @throws IllegalArgumentException if count is negative or greater than deck size
     */
    public List<Card> drawCards(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Cannot draw negative number of cards: " + count);
        }
        if (count == 0) {
            return new ArrayList<>();
        }
        
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            drawnCards.add(drawCard());
        }
        return drawnCards;
    }
    
    /**
     * Shuffles the deck using Fisher-Yates shuffle algorithm.
     * This method can be called manually, but auto-shuffle handles most cases.
     */
    public void shuffle() {
        // Fisher-Yates shuffle for uniform randomness
        for (int i = availableCards.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Collections.swap(availableCards, i, j);
        }
    }
    
    /**
     * Resets the deck to full state and shuffles.
     * All cards are returned to upright orientation.
     */
    public void reset() {
        // Clear available cards and create fresh copies from full deck
        availableCards.clear();
        
        // Create new card instances to avoid reference issues
        for (Card originalCard : fullDeck) {
            Card freshCard = MajorArcana.getByNumber(originalCard.getArcanaNumber()).createCard();
            // Ensure card is upright (though createCard() defaults to upright anyway)
            freshCard.resetOrientation();
            availableCards.add(freshCard);
        }
        
        // Shuffle the newly reset deck
        shuffle();
    }
    
    /**
     * Checks if the deck is empty.
     * 
     * @return true if no cards are available for drawing
     */
    public boolean isEmpty() {
        return availableCards.isEmpty();
    }
    
    /**
     * Gets the number of cards currently available for drawing.
     * 
     * @return Number of available cards
     */
    public int size() {
        return availableCards.size();
    }
    
    /**
     * Gets the total capacity of the deck (always 22 for Major Arcana).
     * 
     * @return Total number of cards in a complete deck
     */
    public int getCapacity() {
        return fullDeck.size();
    }
    
    /**
     * Gets the number of cards that have been drawn since last reset.
     * 
     * @return Number of cards drawn
     */
    public int getDrawnCount() {
        return getCapacity() - size();
    }
    
    /**
     * Checks if deck has enough cards for a standard reading.
     * 
     * @return true if at least 3 cards are available
     */
    public boolean hasEnoughCardsForReading() {
        return size() >= MINIMUM_CARDS_FOR_READING;
    }
    
    /**
     * Peeks at the top card without removing it.
     * Useful for debugging or preview functionality.
     * 
     * @return The next card that would be drawn, or null if empty
     */
    public Card peek() {
        if (availableCards.isEmpty()) {
            return null;
        }
        return availableCards.get(availableCards.size() - 1);
    }
    
    /**
     * Gets a copy of currently available cards (for debugging/display).
     * 
     * @return Unmodifiable list of available cards
     */
    public List<Card> getAvailableCards() {
        return Collections.unmodifiableList(new ArrayList<>(availableCards));
    }
    
    /**
     * Provides deck status information.
     * 
     * @return Formatted string with deck statistics
     */
    public String getStatus() {
        return String.format("Deck Status: %d/%d cards available (%d drawn)", 
                           size(), getCapacity(), getDrawnCount());
    }
    
    @Override
    public String toString() {
        return String.format("Deck[%d/%d cards available]", size(), getCapacity());
    }
}