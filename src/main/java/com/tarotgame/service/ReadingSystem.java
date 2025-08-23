package com.tarotgame.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import com.tarotgame.model.Card;
import com.tarotgame.model.Deck;

/**
 * Manages the creation and storage of tarot readings.
 * Integrates Deck and InterpretationService to create complete Past/Present/Future readings.
 */
public class ReadingSystem {
    
    private final InterpretationService interpretationService;
    private final Random random;
    
    // Constants for card reversal probability
    private static final double REVERSAL_PROBABILITY = 0.3; // 30% chance of reversed cards
    
    /**
     * Position names for the three-card spread
     */
    public enum ReadingPosition {
        PAST(0, "Past"),
        PRESENT(1, "Present"), 
        FUTURE(2, "Future");
        
        private final int index;
        private final String displayName;
        
        ReadingPosition(int index, String displayName) {
            this.index = index;
            this.displayName = displayName;
        }
        
        public int getIndex() { return index; }
        public String getDisplayName() { return displayName; }
    }
    
    /**
     * Represents a complete tarot reading with metadata
     */
    public static class Reading {
        private final Card[] cards;
        private final LocalDateTime timestamp;
        private final InterpretationService.ReadingInterpretation interpretation;
        private final String readingId;
        
        // Static counter for ensuring unique IDs
        private static long readingCounter = 0;
        
        /**
         * Creates a new reading
         */
        public Reading(Card[] cards, InterpretationService.ReadingInterpretation interpretation) {
            this.cards = cards.clone();
            this.timestamp = LocalDateTime.now();
            this.interpretation = interpretation;
            this.readingId = generateReadingId();
        }
        
        /**
         * Generate unique reading ID based on timestamp and counter
         */
        private synchronized String generateReadingId() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
            String timeStamp = timestamp.format(formatter);
            long currentCounter = ++readingCounter;
            return String.format("R%s-%03d", timeStamp, currentCounter);
        }
        
        // Getters
        public Card[] getCards() { return cards.clone(); }
        public LocalDateTime getTimestamp() { return timestamp; }
        public InterpretationService.ReadingInterpretation getInterpretation() { return interpretation; }
        public String getReadingId() { return readingId; }
        
        /**
         * Get card by position
         */
        public Card getCard(ReadingPosition position) {
            return cards[position.getIndex()];
        }
        
        /**
         * Get formatted timestamp
         */
        public String getFormattedTimestamp() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");
            return timestamp.format(formatter);
        }
        
        /**
         * Get short timestamp for lists
         */
        public String getShortTimestamp() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
            return timestamp.format(formatter);
        }
        
        /**
         * Get reading summary (card names only)
         */
        public String getSummary() {
            StringBuilder summary = new StringBuilder();
            ReadingPosition[] positions = ReadingPosition.values();
            
            for (int i = 0; i < positions.length; i++) {
                if (i > 0) summary.append(" | ");
                summary.append(positions[i].getDisplayName())
                       .append(": ")
                       .append(cards[i].getDisplayName());
            }
            return summary.toString();
        }
        
        /**
         * Complete formatted reading
         */
        public String getFormattedReading() {
            StringBuilder formatted = new StringBuilder();
            
            // Header
            formatted.append("═══════════════════════════════════════\n");
            formatted.append("           TAROT READING\n");
            formatted.append("═══════════════════════════════════════\n");
            formatted.append("Reading ID: ").append(readingId).append("\n");
            formatted.append("Date: ").append(getFormattedTimestamp()).append("\n");
            formatted.append("═══════════════════════════════════════\n\n");
            
            // Individual card interpretations
            ReadingPosition[] positions = ReadingPosition.values();
            String[] interpretations = interpretation.getIndividualInterpretations();
            
            for (int i = 0; i < positions.length; i++) {
                formatted.append("【 ").append(positions[i].getDisplayName().toUpperCase()).append(" 】\n");
                formatted.append("Card: ").append(cards[i].getDisplayName()).append("\n");
                formatted.append(interpretations[i]).append("\n\n");
            }
            
            // Overall interpretation
            formatted.append("═══════════════════════════════════════\n");
            formatted.append(interpretation.getOverallInterpretation()).append("\n");
            formatted.append("═══════════════════════════════════════\n");
            
            return formatted.toString();
        }
        
        @Override
        public String toString() {
            return String.format("Reading[%s] - %s", readingId, getSummary());
        }
    }
    
    /**
     * Constructor
     */
    public ReadingSystem(InterpretationService interpretationService) {
        this.interpretationService = interpretationService;
        this.random = new Random();
    }
    
    /**
     * Constructor with custom random seed (for testing)
     */
    public ReadingSystem(InterpretationService interpretationService, long randomSeed) {
        this.interpretationService = interpretationService;
        this.random = new Random(randomSeed);
    }
    
    /**
     * Performs a complete three-card reading using the provided deck
     */
    public Reading performReading(Deck deck) {
        if (deck == null) {
            throw new IllegalArgumentException("Deck cannot be null");
        }
        
        if (!deck.hasEnoughCardsForReading()) {
            throw new IllegalStateException("Deck must have at least 3 cards for a reading");
        }
        
        // Draw three cards from the deck
        List<Card> drawnCards = deck.drawCards(3);
        Card[] readingCards = drawnCards.toArray(new Card[3]);
        
        // Apply random reversals to cards
        applyRandomReversals(readingCards);
        
        // Generate interpretation using the service
        InterpretationService.ReadingInterpretation interpretation = 
            interpretationService.generateReading(readingCards);
        
        // Create and return complete reading
        return new Reading(readingCards, interpretation);
    }
    
    /**
     * Creates a reading from pre-selected cards (useful for testing or custom readings)
     */
    public Reading createReadingFromCards(Card[] cards) {
        if (cards == null || cards.length != 3) {
            throw new IllegalArgumentException("Must provide exactly 3 cards for a reading");
        }
        
        // Clone cards to avoid modifying originals
        Card[] readingCards = new Card[3];
        for (int i = 0; i < 3; i++) {
            readingCards[i] = cloneCard(cards[i]);
        }
        
        // Generate interpretation
        InterpretationService.ReadingInterpretation interpretation = 
            interpretationService.generateReading(readingCards);
        
        return new Reading(readingCards, interpretation);
    }
    
    /**
     * Performs multiple readings and returns them as a list
     */
    public Reading[] performMultipleReadings(Deck deck, int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count must be at least 1");
        }
        
        Reading[] readings = new Reading[count];
        for (int i = 0; i < count; i++) {
            readings[i] = performReading(deck);
            // No need for Thread.sleep anymore since we have a counter-based ID system
        }
        
        return readings;
    }
    
    /**
     * Apply random reversals to cards based on probability
     */
    private void applyRandomReversals(Card[] cards) {
        for (Card card : cards) {
            if (random.nextDouble() < REVERSAL_PROBABILITY) {
                card.setReversed(true);
            }
        }
    }
    
    /**
     * Clone a card (create a copy with same properties)
     */
    private Card cloneCard(Card original) {
        Card clone = new Card(
            original.getArcanaNumber(),
            original.getName(),
            original.getUprightMeaning(),
            original.getReversedMeaning()
        );
        clone.setReversed(original.isReversed());
        return clone;
    }
    
    /**
     * Validate that the InterpretationService is properly loaded
     */
    public boolean isServiceReady() {
        return interpretationService != null && interpretationService.isLoaded();
    }
    
    /**
     * Get interpretation service statistics
     */
    public String getServiceStatus() {
        if (interpretationService == null) {
            return "InterpretationService: Not initialized";
        }
        return String.format("InterpretationService: %d cards loaded, Ready: %s", 
            interpretationService.getLoadedCardCount(),
            interpretationService.isLoaded());
    }
    
    /**
     * Set reversal probability (for customization)
     */
    public void setReversalProbability(double probability) {
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0");
        }
        // Note: Would need to make REVERSAL_PROBABILITY non-final to implement this
        // For now, this is a placeholder for future enhancement
    }
    
    /**
     * Get current reversal probability
     */
    public double getReversalProbability() {
        return REVERSAL_PROBABILITY;
    }
    
    /**
     * Quick reading method that creates a new deck and performs one reading
     * Useful for simple use cases
     */
    public Reading quickReading() {
        Deck deck = new Deck();
        return performReading(deck);
    }
    
    /**
     * Quick reading with custom deck seed
     */
    public Reading quickReading(long deckSeed) {
        Deck deck = new Deck(deckSeed);
        return performReading(deck);
    }
}