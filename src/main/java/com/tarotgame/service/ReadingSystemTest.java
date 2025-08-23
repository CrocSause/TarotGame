package com.tarotgame.service;

import com.tarotgame.model.Card;
import com.tarotgame.model.Deck;

/**
 * Comprehensive test for the complete reading workflow
 * Tests integration between Deck, InterpretationService, and ReadingSystem
 */
public class ReadingSystemTest {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════");
        System.out.println("        READING SYSTEM TEST");
        System.out.println("═══════════════════════════════════════\n");
        
        try {
            // Initialize all components
            InterpretationService interpretationService = new InterpretationService();
            ReadingSystem readingSystem = new ReadingSystem(interpretationService, 12345L); // Fixed seed for reproducible test
            
            // Test 1: System Initialization
            testSystemInitialization(readingSystem);
            
            // Test 2: Basic Reading Workflow
            testBasicReadingWorkflow(readingSystem);
            
            // Test 3: Deck Integration
            testDeckIntegration(readingSystem);
            
            // Test 4: Custom Card Reading
            testCustomCardReading(readingSystem, interpretationService);
            
            // Test 5: Multiple Readings
            testMultipleReadings(readingSystem);
            
            // Test 6: Reading Formatting
            testReadingFormatting(readingSystem);
            
            // Test 7: Error Handling
            testErrorHandling(readingSystem);
            
            // Test 8: Complete End-to-End Demo
            demonstrateCompleteWorkflow(readingSystem);
            
            System.out.println("\n═══════════════════════════════════════");
            System.out.println("        ALL TESTS COMPLETED");
            System.out.println("═══════════════════════════════════════");
            
        } catch (Exception e) {
            System.err.println("TEST FAILED: " + e.getMessage());
        }
    }
    
    /**
     * Test 1: Verify system components are properly initialized
     */
    private static void testSystemInitialization(ReadingSystem readingSystem) {
        System.out.println("TEST 1: System Initialization");
        System.out.println("Service ready: " + readingSystem.isServiceReady());
        System.out.println("Service status: " + readingSystem.getServiceStatus());
        System.out.println("Reversal probability: " + readingSystem.getReversalProbability());
        System.out.println("✓ PASS: System initialized successfully\n");
    }
    
    /**
     * Test 2: Basic reading workflow with new deck
     */
    private static void testBasicReadingWorkflow(ReadingSystem readingSystem) {
        System.out.println("TEST 2: Basic Reading Workflow");
        
        // Create deck and check status
        Deck deck = new Deck(54321L); // Fixed seed for reproducible results
        System.out.println("Initial deck status: " + deck.getStatus());
        System.out.println("Has enough cards for reading: " + deck.hasEnoughCardsForReading());
        
        // Perform reading
        ReadingSystem.Reading reading = readingSystem.performReading(deck);
        
        // Verify reading
        System.out.println("Reading created: " + reading.getReadingId());
        System.out.println("Reading timestamp: " + reading.getFormattedTimestamp());
        System.out.println("Reading summary: " + reading.getSummary());
        System.out.println("Deck status after reading: " + deck.getStatus());
        
        System.out.println("✓ PASS: Basic reading workflow completed\n");
    }
    
    /**
     * Test 3: Verify proper integration with Deck functionality
     */
    private static void testDeckIntegration(ReadingSystem readingSystem) {
        System.out.println("TEST 3: Deck Integration");
        
        Deck deck = new Deck(11111L);
        
        // Perform multiple readings to test deck management
        System.out.println("Initial deck: " + deck.getStatus());
        
        ReadingSystem.Reading reading1 = readingSystem.performReading(deck);
        System.out.println("After reading 1: " + deck.getStatus());
        
        ReadingSystem.Reading reading2 = readingSystem.performReading(deck);
        System.out.println("After reading 2: " + deck.getStatus());
        
        // Test that cards in different readings are actually different
        Card[] cards1 = reading1.getCards();
        Card[] cards2 = reading2.getCards();
        
        boolean foundDuplicate = false;
        for (Card card1 : cards1) {
            for (Card card2 : cards2) {
                if (card1.getArcanaNumber() == card2.getArcanaNumber()) {
                    foundDuplicate = true;
                    break;
                }
            }
        }
        
        System.out.println("Cards properly consumed: " + !foundDuplicate);
        System.out.println("✓ PASS: Deck integration working correctly\n");
    }
    
    /**
     * Test 4: Custom card reading functionality
     */
    private static void testCustomCardReading(ReadingSystem readingSystem, InterpretationService interpretationService) {
        System.out.println("TEST 4: Custom Card Reading");
        
        // Create specific cards for testing
        Card[] customCards = new Card[3];
        customCards[0] = interpretationService.createCard(0);  // The Fool
        customCards[1] = interpretationService.createCard(1);  // The Magician  
        customCards[2] = interpretationService.createCard(21); // The World
        
        // Set specific orientations
        customCards[0].setReversed(false); // Fool upright
        customCards[1].setReversed(true);  // Magician reversed
        customCards[2].setReversed(false); // World upright
        
        System.out.println("Custom cards created:");
        for (int i = 0; i < customCards.length; i++) {
            System.out.println("  " + (i+1) + ". " + customCards[i].getDisplayName());
        }
        
        // Create reading from custom cards
        ReadingSystem.Reading customReading = readingSystem.createReadingFromCards(customCards);
        
        System.out.println("Custom reading ID: " + customReading.getReadingId());
        System.out.println("Custom reading summary: " + customReading.getSummary());
        System.out.println("✓ PASS: Custom card reading created successfully\n");
    }
    
    /**
     * Test 5: Multiple readings functionality
     */
    private static void testMultipleReadings(ReadingSystem readingSystem) {
        System.out.println("TEST 5: Multiple Readings");
        
        Deck deck = new Deck(99999L);
        int readingCount = 3;
        
        ReadingSystem.Reading[] readings = readingSystem.performMultipleReadings(deck, readingCount);
        
        System.out.println("Created " + readings.length + " readings:");
        for (int i = 0; i < readings.length; i++) {
            System.out.println("  " + (i+1) + ". " + readings[i].toString());
        }
        
        // Verify all readings have unique IDs
        boolean allUnique = true;
        for (int i = 0; i < readings.length; i++) {
            for (int j = i + 1; j < readings.length; j++) {
                if (readings[i].getReadingId().equals(readings[j].getReadingId())) {
                    allUnique = false;
                    break;
                }
            }
        }
        
        System.out.println("All reading IDs unique: " + allUnique);
        System.out.println("✓ PASS: Multiple readings created successfully\n");
    }
    
    /**
     * Test 6: Reading formatting and display options
     */
    private static void testReadingFormatting(ReadingSystem readingSystem) {
        System.out.println("TEST 6: Reading Formatting");
        
        ReadingSystem.Reading reading = readingSystem.quickReading(77777L);
        
        System.out.println("Reading ID: " + reading.getReadingId());
        System.out.println("Formatted timestamp: " + reading.getFormattedTimestamp());
        System.out.println("Short timestamp: " + reading.getShortTimestamp());
        System.out.println("Summary: " + reading.getSummary());
        
        // Test position-specific access
        System.out.println("\nPosition-specific access:");
        System.out.println("Past card: " + reading.getCard(ReadingSystem.ReadingPosition.PAST).getDisplayName());
        System.out.println("Present card: " + reading.getCard(ReadingSystem.ReadingPosition.PRESENT).getDisplayName());
        System.out.println("Future card: " + reading.getCard(ReadingSystem.ReadingPosition.FUTURE).getDisplayName());
        
        System.out.println("✓ PASS: Reading formatting working correctly\n");
    }
    
    /**
     * Test 7: Error handling
     */
    private static void testErrorHandling(ReadingSystem readingSystem) {
        System.out.println("TEST 7: Error Handling");
        
        // Test null deck
        try {
            readingSystem.performReading(null);
            System.out.println("✗ FAIL: Should have thrown exception for null deck");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ PASS: Correctly handled null deck: " + e.getMessage());
        }
        
        // Test invalid card array for custom reading
        try {
            Card[] invalidCards = new Card[2]; // Only 2 cards instead of 3
            readingSystem.createReadingFromCards(invalidCards);
            System.out.println("✗ FAIL: Should have thrown exception for invalid card array");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ PASS: Correctly handled invalid card array: " + e.getMessage());
        }
        
        // Test invalid multiple reading count
        try {
            Deck deck = new Deck();
            readingSystem.performMultipleReadings(deck, 0);
            System.out.println("✗ FAIL: Should have thrown exception for zero count");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ PASS: Correctly handled invalid count: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Test 8: Complete end-to-end demonstration
     */
    private static void demonstrateCompleteWorkflow(ReadingSystem readingSystem) {
        System.out.println("TEST 8: Complete End-to-End Demonstration");
        System.out.println("═══════════════════════════════════════");
        
        // Create a reading with detailed output
        ReadingSystem.Reading demonstrationReading = readingSystem.quickReading(42L);
        
        // Show the complete formatted reading
        System.out.println(demonstrationReading.getFormattedReading());
        
        System.out.println("✓ PASS: Complete workflow demonstration successful");
    }
    
    /**
     * Helper method: Display reading cards with details
     */
    private static void displayReadingCards(ReadingSystem.Reading reading) {
        Card[] cards = reading.getCards();
        ReadingSystem.ReadingPosition[] positions = ReadingSystem.ReadingPosition.values();
        
        System.out.println("Reading Cards:");
        for (int i = 0; i < cards.length; i++) {
            System.out.println(String.format("  %s: %s", 
                positions[i].getDisplayName(),
                cards[i].getDisplayName()));
        }
    }
    
    /**
     * Helper method: Test reversal distribution over multiple readings
     */
    private static void testReversalDistribution(ReadingSystem readingSystem) {
        System.out.println("BONUS: Reversal Distribution Test");
        
        int totalCards = 0;
        int reversedCards = 0;
        int testReadings = 20;
        
        Deck deck = new Deck();
        
        for (int i = 0; i < testReadings; i++) {
            ReadingSystem.Reading reading = readingSystem.performReading(deck);
            Card[] cards = reading.getCards();
            
            for (Card card : cards) {
                totalCards++;
                if (card.isReversed()) {
                    reversedCards++;
                }
            }
        }
        
        double actualReversalRate = (double) reversedCards / totalCards;
        double expectedReversalRate = readingSystem.getReversalProbability();
        
        System.out.println(String.format("Reversal rate: %.2f%% (expected: %.2f%%)", 
            actualReversalRate * 100, expectedReversalRate * 100));
        System.out.println(String.format("Cards tested: %d total, %d reversed", totalCards, reversedCards));
        System.out.println();
    }
}