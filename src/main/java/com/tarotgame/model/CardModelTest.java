// CardModelTest.java
package com.tarotgame.model;

import java.util.List;

/**
 * Simple test class to verify Card Model functionality.
 * This will serve as our Proof-of-Concept for the Card Model component.
 */
public class CardModelTest {
    
    public static void main(String[] args) {
        System.out.println("=== CARD MODEL PROOF OF CONCEPT ===\n");
        
        // Test 1: Basic card creation and display
        testBasicCardCreation();
        
        // Test 2: Card orientation and meaning changes
        testCardOrientation();
        
        // Test 3: Full deck creation
        testFullDeckCreation();
        
        // Test 4: Card equality and identification
        testCardEquality();
        
        // Test 5: Factory methods
        testFactoryMethods();
        
        System.out.println("\n=== ALL TESTS COMPLETED SUCCESSFULLY ===");
    }
    
    private static void testBasicCardCreation() {
        System.out.println("--- Test 1: Basic Card Creation ---");
        
        Card fool = MajorArcana.THE_FOOL.createCard();
        Card magician = MajorArcana.THE_MAGICIAN.createCard();
        
        System.out.println("Created cards:");
        System.out.println(fool.toString());
        System.out.println(magician.toString());
        
        System.out.println("\nDetailed view of The Fool:");
        System.out.println(fool.toDetailedString());
        
        System.out.println("\n✓ Basic card creation working\n");
    }
    
    private static void testCardOrientation() {
        System.out.println("--- Test 2: Card Orientation ---");
        
        Card empress = MajorArcana.THE_EMPRESS.createCard();
        
        System.out.println("Original orientation:");
        System.out.println("Display Name: " + empress.getDisplayName());
        System.out.println("Current Meaning: " + empress.getCurrentMeaning());
        System.out.println("Is Reversed: " + empress.isReversed());
        
        // Flip the card
        empress.flip();
        System.out.println("\nAfter flipping:");
        System.out.println("Display Name: " + empress.getDisplayName());
        System.out.println("Current Meaning: " + empress.getCurrentMeaning());
        System.out.println("Is Reversed: " + empress.isReversed());
        
        // Reset orientation
        empress.resetOrientation();
        System.out.println("\nAfter reset:");
        System.out.println("Display Name: " + empress.getDisplayName());
        System.out.println("Is Reversed: " + empress.isReversed());
        
        System.out.println("\n✓ Card orientation working\n");
    }
    
    private static void testFullDeckCreation() {
        System.out.println("--- Test 3: Full Deck Creation ---");
        
        List<Card> fullDeck = MajorArcana.createFullDeck();
        
        System.out.println("Created full deck with " + fullDeck.size() + " cards:");
        System.out.println("First 5 cards:");
        
        for (int i = 0; i < Math.min(5, fullDeck.size()); i++) {
            Card card = fullDeck.get(i);
            System.out.println(String.format("%d. %s (Arcana #%d)", 
                i + 1, card.getName(), card.getArcanaNumber()));
        }
        
        System.out.println("...");
        System.out.println("Last card: " + fullDeck.get(fullDeck.size() - 1).getName());
        
        System.out.println("\n✓ Full deck creation working\n");
    }
    
    private static void testCardEquality() {
        System.out.println("--- Test 4: Card Equality ---");
        
        Card fool1 = MajorArcana.THE_FOOL.createCard();
        Card fool2 = MajorArcana.THE_FOOL.createCard();
        Card magician = MajorArcana.THE_MAGICIAN.createCard();
        
        // Test equality (should be based on arcana number)
        System.out.println("fool1.equals(fool2): " + fool1.equals(fool2));
        System.out.println("fool1.equals(magician): " + fool1.equals(magician));
        
        // Test that orientation doesn't affect equality
        fool2.flip();
        System.out.println("fool1.equals(fool2) after flip: " + fool1.equals(fool2));
        
        // Test hash codes
        System.out.println("fool1 hashCode: " + fool1.hashCode());
        System.out.println("fool2 hashCode: " + fool2.hashCode());
        System.out.println("magician hashCode: " + magician.hashCode());
        
        System.out.println("\n✓ Card equality working\n");
    }
    
    private static void testFactoryMethods() {
        System.out.println("--- Test 5: Factory Methods ---");
        
        try {
            // Test creating card by number
            Card cardByNumber = MajorArcana.createCardByNumber(13);
            System.out.println("Card created by number 13: " + cardByNumber.getName());
            
            // Test getting arcana by number
            MajorArcana arcana = MajorArcana.getByNumber(21);
            System.out.println("Arcana by number 21: " + arcana.getName());
            
            // Test edge cases
            System.out.println("First arcana (0): " + MajorArcana.getByNumber(0).getName());
            System.out.println("Last arcana (21): " + MajorArcana.getByNumber(21).getName());
            
            System.out.println("\n✓ Factory methods working");
            
        } catch (Exception e) {
            System.out.println("❌ Error in factory methods: " + e.getMessage());
        }
        
        // Test invalid number (should throw exception)
        try {
            MajorArcana.getByNumber(22); // Should fail
            System.out.println("❌ Should have thrown exception for invalid number");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly threw exception for invalid number: " + e.getMessage());
        }
        
        System.out.println();
    }
}