package com.tarotgame.service;

import java.util.List;

import com.tarotgame.model.Card;

/**
 * Test class for InterpretationService
 * Provides comprehensive testing of card loading, interpretation, and reading generation
 */
public class InterpretationServiceTest {
    
    public static void main(String[] args) {
        System.out.println("=== INTERPRETATION SERVICE TEST ===\n");
        
        try {
            // Initialize the service
            InterpretationService service = new InterpretationService();
            
            // Test 1: Service Loading
            testServiceLoading(service);
            
            // Test 2: Individual Card Access
            testIndividualCardAccess(service);
            
            // Test 3: Position-Aware Interpretations
            testPositionAwareInterpretations(service);
            
            // Test 4: Keywords and Names
            testKeywordsAndNames(service);
            
            // Test 5: Complete Reading Generation
            testCompleteReading(service);
            
            // Test 6: Error Handling
            testErrorHandling(service);

            // Test 7: Card Creation Integration
            testCardCreation(service);
            
            System.out.println("\n=== ALL TESTS COMPLETED SUCCESSFULLY ===");
            
        } catch (Exception e) {
            System.err.println("TEST FAILED: " + e.getMessage());
        }
    }
    
    /**
     * Test 1: Verify service loads correctly
     */
    private static void testServiceLoading(InterpretationService service) {
        System.out.println("TEST 1: Service Loading");
        System.out.println("Is loaded: " + service.isLoaded());
        System.out.println("Cards loaded: " + service.getLoadedCardCount());
        System.out.println("Expected 22 Major Arcana cards: " + (service.getLoadedCardCount() == 22 ? "✓ PASS" : "✗ FAIL"));
        System.out.println();
    }
    
    /**
     * Test 2: Test individual card access
     */
    private static void testIndividualCardAccess(InterpretationService service) {
        System.out.println("TEST 2: Individual Card Access");
        
        // Test The Fool (arcana 0)
        System.out.println("--- The Fool (Arcana: 0) ---");
        String foolName = service.getCardName(0);
        System.out.println("Name: " + foolName);
        
        String foolUpright = service.getCardInterpretation(0, false, InterpretationService.Position.GENERAL);
        System.out.println("Upright: " + foolUpright);
        
        String foolReversed = service.getCardInterpretation(0, true, InterpretationService.Position.GENERAL);
        System.out.println("Reversed: " + foolReversed);
        
        // Test The World (arcana 21)
        System.out.println("\n--- The World (Arcana: 21) ---");
        String worldName = service.getCardName(21);
        System.out.println("Name: " + worldName);
        
        String worldUpright = service.getCardInterpretation(21, false, InterpretationService.Position.GENERAL);
        System.out.println("Upright: " + worldUpright);
        System.out.println();
    }
    
    /**
     * Test 3: Position-aware interpretations
     */
    private static void testPositionAwareInterpretations(InterpretationService service) {
        System.out.println("TEST 3: Position-Aware Interpretations");
        System.out.println("--- The Magician (ID: 1) in different positions ---");
        
        String pastContext = service.getCardInterpretation(1, false, InterpretationService.Position.PAST);
        String presentContext = service.getCardInterpretation(1, false, InterpretationService.Position.PRESENT);
        String futureContext = service.getCardInterpretation(1, false, InterpretationService.Position.FUTURE);
        
        System.out.println("Past: " + pastContext);
        System.out.println("Present: " + presentContext);
        System.out.println("Future: " + futureContext);
        System.out.println();
    }
    
    /**
     * Test 4: Keywords and names
     */
    private static void testKeywordsAndNames(InterpretationService service) {
        System.out.println("TEST 4: Keywords and Names");
        System.out.println("--- The High Priestess (ID: 2) ---");
        
        List<String> uprightKeywords = service.getCardKeywords(2, false);
        List<String> reversedKeywords = service.getCardKeywords(2, true);
        
        System.out.println("Upright keywords: " + uprightKeywords);
        System.out.println("Reversed keywords: " + reversedKeywords);
        System.out.println();
    }
    
    /**
     * Test 5: Complete reading generation
     */
    private static void testCompleteReading(InterpretationService service) {
        System.out.println("TEST 5: Complete Reading Generation");
        System.out.println("--- Sample 3-Card Reading ---");
        
        // Create sample cards using the service's card creation method
        Card[] sampleCards = {
            service.createCard(0),   // The Fool - Past
            service.createCard(1),   // The Magician - Present  
            service.createCard(21)   // The World - Future
        };
        
        // Set orientations for the reading
        sampleCards[0].setReversed(false);  // The Fool upright
        sampleCards[1].setReversed(true);   // The Magician reversed
        sampleCards[2].setReversed(false);  // The World upright
        
        InterpretationService.ReadingInterpretation reading = service.generateReading(sampleCards);
        System.out.println(reading.toString());
        System.out.println();
    }
    
    /**
     * Test 6: Error handling
     */
    private static void testErrorHandling(InterpretationService service) {
        System.out.println("TEST 6: Error Handling");
        
        // Test invalid card arcana number
        try {
            service.getCardName(99);
            System.out.println("✗ FAIL: Should have thrown exception for invalid arcana number");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ PASS: Correctly handled invalid arcana number: " + e.getMessage());
        }
        
        // Test invalid reading array
        try {
            Card[] invalidCards = {service.createCard(0)}; // Only 1 card instead of 3
            service.generateReading(invalidCards);
            System.out.println("✗ FAIL: Should have thrown exception for invalid card array");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ PASS: Correctly handled invalid reading array: " + e.getMessage());
        }
        
        // Test null reading array
        try {
            service.generateReading(null);
            System.out.println("✗ FAIL: Should have thrown exception for null card array");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ PASS: Correctly handled null reading array: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Test 7: Card creation integration with your Card class
     */
    private static void testCardCreation(InterpretationService service) {
        System.out.println("TEST 7: Card Creation Integration");
        
        // Test creating individual cards
        Card fool = service.createCard(0);
        System.out.println("Created card: " + fool);
        System.out.println("Card's built-in meaning: " + fool.getCurrentMeaning());
        
        // Test flipping orientation
        fool.setReversed(true);
        System.out.println("After reversing: " + fool.getDisplayName());
        System.out.println("Reversed meaning: " + fool.getCurrentMeaning());
        
        // Test creating all cards
        Card[] allCards = service.createAllCards();
        System.out.println("Created " + allCards.length + " cards successfully");
        
        // Show first few cards
        System.out.println("First 5 cards:");
        for (int i = 0; i < 5; i++) {
            System.out.println("  " + allCards[i].toString());
        }
        System.out.println();
    }           
    
    /**
     * Helper method: Display all card names (useful for verification)
     */
    @SuppressWarnings("unused")
    private static void displayAllCardNames(InterpretationService service) {
        System.out.println("=== ALL MAJOR ARCANA CARDS ===");
        for (int i = 0; i <= 21; i++) {
            try {
                System.out.println(i + ": " + service.getCardName(i));
            } catch (Exception e) {
                System.out.println(i + ": ERROR - " + e.getMessage());
            }
        }
        System.out.println();
    }
    
    /**
     * Helper method: Test a complete card interpretation using your Card class
     */
    @SuppressWarnings("unused")
    private static void testCompleteCardInterpretation(InterpretationService service, int arcanaNumber) {
        System.out.println("=== COMPLETE INTERPRETATION: " + service.getCardName(arcanaNumber) + " ===");
        
        // Create the card using the service
        Card card = service.createCard(arcanaNumber);
        
        System.out.println("UPRIGHT:");
        System.out.println("  Card's built-in meaning: " + card.getCurrentMeaning());
        System.out.println("  Keywords: " + service.getCardKeywords(arcanaNumber, false));
        System.out.println("  Past: " + service.getCardInterpretation(arcanaNumber, false, InterpretationService.Position.PAST));
        System.out.println("  Present: " + service.getCardInterpretation(arcanaNumber, false, InterpretationService.Position.PRESENT));
        System.out.println("  Future: " + service.getCardInterpretation(arcanaNumber, false, InterpretationService.Position.FUTURE));
        
        // Flip the card and test reversed
        card.setReversed(true);
        System.out.println("\nREVERSED:");
        System.out.println("  Card's built-in meaning: " + card.getCurrentMeaning());
        System.out.println("  Keywords: " + service.getCardKeywords(arcanaNumber, true));
        System.out.println("  Past: " + service.getCardInterpretation(arcanaNumber, true, InterpretationService.Position.PAST));
        System.out.println("  Present: " + service.getCardInterpretation(arcanaNumber, true, InterpretationService.Position.PRESENT));
        System.out.println("  Future: " + service.getCardInterpretation(arcanaNumber, true, InterpretationService.Position.FUTURE));
        System.out.println();

        
    }
}
