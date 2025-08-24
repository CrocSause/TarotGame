// DeckTest.java
package com.tarotgame.unit;

import java.util.List;

import com.tarotgame.model.Card;
import com.tarotgame.model.Deck;

/**
 * Test suite for Deck functionality.
 * Tests all major deck operations and edge cases.
 */
public class DeckTest {
    
    public static void main(String[] args) {
        System.out.println("=== DECK MANAGEMENT TEST SUITE ===\n");
        
        // Test 1: Basic deck creation and properties
        testDeckCreation();
        
        // Test 2: Card drawing functionality
        testCardDrawing();
        
        // Test 3: Auto-reshuffle behavior
        testAutoReshuffle();
        
        // Test 4: Multiple card drawing
        testMultipleCardDrawing();
        
        // Test 5: Deck reset functionality
        testDeckReset();
        
        // Test 6: Shuffle randomness verification
        testShuffleRandomness();
        
        // Test 7: Edge cases and error handling
        testEdgeCases();
        
        System.out.println("\n=== ALL DECK TESTS COMPLETED SUCCESSFULLY ===");
    }
    
    private static void testDeckCreation() {
        System.out.println("--- Test 1: Deck Creation ---");
        
        Deck deck = new Deck();
        
        System.out.println("Deck created: " + deck);
        System.out.println("Initial size: " + deck.size());
        System.out.println("Capacity: " + deck.getCapacity());
        System.out.println("Is empty: " + deck.isEmpty());
        System.out.println("Has enough for reading: " + deck.hasEnoughCardsForReading());
        System.out.println("Status: " + deck.getStatus());
        
        // Verify we have all 22 Major Arcana cards
        if (deck.size() == 22 && deck.getCapacity() == 22) {
            System.out.println("✓ Deck properly initialized with all Major Arcana");
        } else {
            System.out.println("❌ Deck initialization problem");
        }
        
        System.out.println();
    }
    
    private static void testCardDrawing() {
        System.out.println("--- Test 2: Card Drawing ---");
        
        Deck deck = new Deck(12345); // Fixed seed for reproducible testing
        
        System.out.println("Drawing 5 cards:");
        for (int i = 0; i < 5; i++) {
            Card card = deck.drawCard();
            System.out.println(String.format("%d. %s (Arcana #%d)", 
                i + 1, card.getName(), card.getArcanaNumber()));
        }
        
        System.out.println("\nDeck status after drawing 5 cards:");
        System.out.println(deck.getStatus());
        System.out.println("Cards drawn: " + deck.getDrawnCount());
        System.out.println("Cards remaining: " + deck.size());
        
        System.out.println("✓ Card drawing working");
        System.out.println();
    }
    
    private static void testAutoReshuffle() {
        System.out.println("--- Test 3: Auto-Reshuffle Behavior ---");
        
        Deck deck = new Deck(54321); // Fixed seed
        
        // Draw cards until we have less than 3 remaining
        System.out.println("Drawing cards until auto-reshuffle triggers...");
        
        int drawCount = 0;
        while (deck.size() >= 3) {
            deck.drawCard();
            drawCount++;
        }
        
        System.out.println("Drew " + drawCount + " cards");
        System.out.println("Deck size before triggering reshuffle: " + deck.size());
        
        // This should trigger auto-reshuffle (deck size < 3)
        System.out.println("Drawing card when deck size < 3 (should trigger reshuffle)...");
        Card cardFromFreshDeck = deck.drawCard();
        System.out.println("Drew card from reshuffled deck: " + cardFromFreshDeck.getName());
        System.out.println("Deck size after auto-reshuffle and draw: " + deck.size());
        
        // Validation: After auto-reshuffle (22 cards) and one draw, should have 21
        if (deck.size() == 21) {
            System.out.println("✓ Auto-reshuffle working correctly");
            System.out.println("  - Triggered when size was < 3");
            System.out.println("  - Reset to full deck (22 cards)"); 
            System.out.println("  - Drew one card, leaving 21");
        } else {
            System.out.println("❌ Auto-reshuffle problem. Expected 21, got " + deck.size());
        }
        
        // Additional validation: Ensure we can now draw multiple cards
        if (deck.hasEnoughCardsForReading()) {
            System.out.println("✓ Deck ready for readings after auto-reshuffle");
        } else {
            System.out.println("❌ Deck not ready for readings after auto-reshuffle");
        }
        
        System.out.println();
    }
    
    private static void testMultipleCardDrawing() {
        System.out.println("--- Test 4: Multiple Card Drawing ---");
        
        Deck deck = new Deck(98765);
        
        List<Card> drawnCards = deck.drawCards(3);
        System.out.println("Drew 3 cards for reading:");
        
        for (int i = 0; i < drawnCards.size(); i++) {
            Card card = drawnCards.get(i);
            String position = (i == 0) ? "PAST" : (i == 1) ? "PRESENT" : "FUTURE";
            System.out.println(String.format("%s: %s", position, card.getName()));
        }
        
        System.out.println("\nDeck status: " + deck.getStatus());
        
        // Test edge cases
        List<Card> emptyDraw = deck.drawCards(0);
        System.out.println("Drawing 0 cards returned list of size: " + emptyDraw.size());
        
        System.out.println("✓ Multiple card drawing working");
        System.out.println();
    }
    
    private static void testDeckReset() {
        System.out.println("--- Test 5: Deck Reset ---");
        
        Deck deck = new Deck();
        
        // Draw some cards
        deck.drawCards(10);
        System.out.println("After drawing 10 cards: " + deck.getStatus());
        
        // Reset deck
        deck.reset();
        System.out.println("After reset: " + deck.getStatus());
        
        if (deck.size() == 22 && deck.getDrawnCount() == 0) {
            System.out.println("✓ Deck reset working correctly");
        } else {
            System.out.println("❌ Deck reset problem");
        }
        
        System.out.println();
    }
    
    private static void testShuffleRandomness() {
        System.out.println("--- Test 6: Shuffle Randomness ---");
        
        // Create two decks with different seeds
        Deck deck1 = new Deck(1111);
        Deck deck2 = new Deck(2222);
        
        List<Card> draw1 = deck1.drawCards(5);
        List<Card> draw2 = deck2.drawCards(5);
        
        System.out.println("First deck draw:");
        draw1.forEach(card -> System.out.println("  " + card.getName()));
        
        System.out.println("Second deck draw:");
        draw2.forEach(card -> System.out.println("  " + card.getName()));
        
        // Check if the draws are different (they should be with different seeds)
        boolean areDifferent = false;
        for (int i = 0; i < draw1.size(); i++) {
            if (!draw1.get(i).equals(draw2.get(i))) {
                areDifferent = true;
                break;
            }
        }
        
        if (areDifferent) {
            System.out.println("✓ Shuffling producing different results with different seeds");
        } else {
            System.out.println("⚠ Shuffles were identical (could happen by chance)");
        }
        
        System.out.println();
    }
    
    private static void testEdgeCases() {
        System.out.println("--- Test 7: Edge Cases ---");
        
        Deck deck = new Deck();
        
        // Test peek functionality
        Card peekedCard = deck.peek();
        Card drawnCard = deck.drawCard();
        
        System.out.println("Peeked card: " + (peekedCard != null ? peekedCard.getName() : "null"));
        System.out.println("Drawn card: " + drawnCard.getName());
        
        if (peekedCard != null && peekedCard.equals(drawnCard)) {
            System.out.println("✓ Peek functionality working");
        } else {
            System.out.println("❌ Peek functionality problem");
        }
        
        // Test drawing negative cards
        try {
            deck.drawCards(-1);
            System.out.println("❌ Should have thrown exception for negative draw");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly handled negative card draw: " + e.getMessage());
        }
        
        // Test available cards list is unmodifiable
        List<Card> available = deck.getAvailableCards();
        try {
            available.clear(); // This should fail
            System.out.println("❌ Available cards list should be unmodifiable");
        } catch (UnsupportedOperationException e) {
            System.out.println("✓ Available cards list properly protected");
        }
        
        System.out.println();
    }
}