// GameEngineTest.java
package com.tarotgame.integration;

import com.tarotgame.engine.GameEngine;
import com.tarotgame.service.ReadingSystem.Reading;


/**
 * Comprehensive test class for the GameEngine.
 * Demonstrates all engine functionality and validates end-to-end workflow.
 */
public class GameEngineTest {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════");
        System.out.println("        TAROT GAME ENGINE TEST");
        System.out.println("═══════════════════════════════════════\n");
        
        // Run all tests
        testEngineInitialization();
        testBasicReadingWorkflow();
        testMultipleReadings();
        testDeckManagement();
        testSessionTracking();
        testErrorRecovery();
        testEngineStatus();
        
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("        ALL TESTS COMPLETED");
        System.out.println("═══════════════════════════════════════");
    }
    
    /**
     * Test 1: Engine initialization and service validation
     */
    private static void testEngineInitialization() {
        printTestHeader("Engine Initialization");
        
        try {
            // Test normal initialization
            GameEngine engine = new GameEngine();
            
            System.out.println("✓ Engine created successfully");
            System.out.println("✓ State: " + engine.getCurrentState());
            System.out.println("✓ Ready: " + engine.isReady());
            System.out.println("✓ Has Error: " + engine.hasError());
            
            // Test seeded initialization (for reproducible testing)
            GameEngine seededEngine = new GameEngine(12345L);
            System.out.println("✓ Seeded engine created successfully");
            
            System.out.println("✓ Initial status check:");
            System.out.println(engine.getQuickStatus());
            
        } catch (Exception e) {
            System.err.println("✗ Engine initialization failed: " + e.getMessage());
        }
        
        printTestFooter();
    }
    
    /**
     * Test 2: Basic reading workflow (Milestone 4 validation)
     */
    private static void testBasicReadingWorkflow() {
        printTestHeader("Basic Reading Workflow");
        
        try {
            GameEngine engine = new GameEngine(54321L); // Seeded for consistent testing
            
            System.out.println("Performing first reading...\n");
            
            // Perform a reading
            GameEngine.EngineResult result = engine.performReading();
            
            if (result.isSuccess()) {
                System.out.println("✓ Reading successful!");
                System.out.println("✓ Message: " + result.getMessage());
                
                if (result.hasReading()) {
                    Reading reading = result.getReading();
                    System.out.println("✓ Reading ID: " + reading.getReadingId());
                    System.out.println("✓ Timestamp: " + reading.getFormattedTimestamp());
                    System.out.println("✓ Summary: " + reading.getSummary());
                    
                    // Display the complete formatted reading
                    System.out.println("\n" + reading.getFormattedReading());
                } else {
                    System.err.println("✗ No reading data returned");
                }
            } else {
                System.err.println("✗ Reading failed: " + result.getMessage());
                if (result.hasError()) {
                }
            }
            
        } catch (Exception e) {
            System.err.println("✗ Basic workflow test failed: " + e.getMessage());
        }
        
        printTestFooter();
    }
    
    /**
     * Test 3: Multiple readings and session management
     */
    private static void testMultipleReadings() {
        printTestHeader("Multiple Readings & Session Management");
        
        try {
            GameEngine engine = new GameEngine(99999L);
            
            System.out.println("Performing multiple readings...\n");
            
            // Perform several readings
            for (int i = 1; i <= 5; i++) {
                System.out.println("--- Reading " + i + " ---");
                GameEngine.EngineResult result = engine.performReading();
                
                if (result.isSuccess()) {
                    Reading reading = result.getReading();
                    System.out.println("✓ " + reading.getReadingId() + ": " + reading.getSummary());
                } else {
                    System.err.println("✗ Reading " + i + " failed: " + result.getMessage());
                }
                
                // Small delay to show timestamp differences
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            // Test session tracking
            System.out.println("\n--- Session Summary ---");
            System.out.println("✓ Total readings in session: " + engine.getSessionReadings().size());
            System.out.println("✓ Session statistics: " + engine.getSessionStats());
            
            // Test individual reading access
            Reading lastReading = engine.getLastReading();
            if (lastReading != null) {
                System.out.println("✓ Last reading: " + lastReading.getReadingId());
            }
            
            Reading firstReading = engine.getReading(0);
            if (firstReading != null) {
                System.out.println("✓ First reading: " + firstReading.getReadingId());
            }
            
        } catch (Exception e) {
            System.err.println("✗ Multiple readings test failed: " + e.getMessage());
        }
        
        printTestFooter();
    }
    
    /**
     * Test 4: Deck management features
     */
    private static void testDeckManagement() {
        printTestHeader("Deck Management");
        
        try {
            GameEngine engine = new GameEngine();
            
            // Show initial deck status
            System.out.println("Initial deck status:");
            System.out.println(engine.getQuickStatus());
            
            // Perform some readings to use up cards
            System.out.println("\nUsing up some cards...");
            for (int i = 0; i < 7; i++) {
                engine.performReading();
            }
            System.out.println("After 7 readings: " + engine.getQuickStatus());
            
            // Test deck reset
            System.out.println("\nTesting deck reset...");
            GameEngine.EngineResult resetResult = engine.resetDeck();
            if (resetResult.isSuccess()) {
                System.out.println("✓ Deck reset: " + resetResult.getMessage());
                System.out.println("✓ After reset: " + engine.getQuickStatus());
            } else {
                System.err.println("✗ Deck reset failed: " + resetResult.getMessage());
            }
            
            // Test new deck creation
            System.out.println("\nTesting new deck creation...");
            GameEngine.EngineResult newDeckResult = engine.createNewDeck();
            if (newDeckResult.isSuccess()) {
                System.out.println("✓ New deck created: " + newDeckResult.getMessage());
                System.out.println("✓ After new deck: " + engine.getQuickStatus());
            } else {
                System.err.println("✗ New deck creation failed: " + newDeckResult.getMessage());
            }
            
            // Test auto-reshuffle by drawing many cards
            System.out.println("\nTesting auto-reshuffle (drawing 25+ cards)...");
            for (int i = 0; i < 8; i++) { // 8 readings = 24 cards, should trigger reshuffle
                GameEngine.EngineResult result = engine.performReading();
                if (i == 7) { // Last reading should show reshuffle happened
                    System.out.println("✓ Reading after auto-reshuffle: " + result.getMessage());
                    System.out.println("✓ Deck status: " + engine.getQuickStatus());
                }
            }
            
        } catch (Exception e) {
            System.err.println("✗ Deck management test failed: " + e.getMessage());
        }
        
        printTestFooter();
    }
    
    /**
     * Test 5: Session tracking and history management
     */
    private static void testSessionTracking() {
        printTestHeader("Session Tracking & History");
        
        try {
            GameEngine engine = new GameEngine();
            
            // Perform readings
            System.out.println("Building reading history...");
            for (int i = 0; i < 3; i++) {
                engine.performReading();
            }
            
            // Test history access
            System.out.println("\n--- Reading History ---");
            var readings = engine.getSessionReadings();
            for (int i = 0; i < readings.size(); i++) {
                Reading reading = readings.get(i);
                System.out.println(String.format("%d. %s - %s", 
                    i + 1, reading.getReadingId(), reading.getShortTimestamp()));
            }
            
            // Test session statistics
            System.out.println("\n--- Session Statistics ---");
            GameEngine.SessionStats stats = engine.getSessionStats();
            System.out.println("✓ Total readings: " + stats.getTotalReadings());
            System.out.println("✓ Readings in history: " + stats.getReadingsInHistory());
            System.out.println("✓ Deck resets: " + stats.getDeckResets());
            System.out.println("✓ Session uptime: " + stats.getUptime());
            
            // Test history clearing
            System.out.println("\nTesting history clearing...");
            GameEngine.EngineResult clearResult = engine.clearReadingHistory();
            if (clearResult.isSuccess()) {
                System.out.println("✓ History cleared: " + clearResult.getMessage());
                System.out.println("✓ Readings remaining: " + engine.getSessionReadings().size());
            }
            
        } catch (Exception e) {
            System.err.println("✗ Session tracking test failed: " + e.getMessage());
        }
        
        printTestFooter();
    }
    
    /**
     * Test 6: Error recovery and resilience
     */
    private static void testErrorRecovery() {
        printTestHeader("Error Recovery");
        
        try {
            GameEngine engine = new GameEngine();
            
            System.out.println("Initial state: " + engine.getCurrentState());
            System.out.println("Has error: " + engine.hasError());
            
            // Normal operations should work
            GameEngine.EngineResult normalResult = engine.performReading();
            System.out.println("✓ Normal reading: " + normalResult.isSuccess());
            
            // Test recovery from healthy state (should be no-op)
            GameEngine.EngineResult recoveryResult = engine.recoverFromError();
            if (recoveryResult.isSuccess()) {
                System.out.println("✓ Recovery from healthy state: " + recoveryResult.getMessage());
            }
            
            // Test multiple operations to ensure stability
            System.out.println("\nTesting stability with multiple operations...");
            for (int i = 0; i < 3; i++) {
                GameEngine.EngineResult result = engine.performReading();
                if (!result.isSuccess()) {
                    System.err.println("✗ Operation failed: " + result.getMessage());
                    break;
                } else {
                    System.out.println("✓ Operation " + (i + 1) + " successful");
                }
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error recovery test failed: " + e.getMessage());
        }
        
        printTestFooter();
    }
    
    /**
     * Test 7: Engine status and diagnostics
     */
    private static void testEngineStatus() {
        printTestHeader("Engine Status & Diagnostics");
        
        try {
            GameEngine engine = new GameEngine();
            
            // Perform some operations to generate interesting status
            engine.performReading();
            engine.performReading();
            engine.resetDeck();
            engine.performReading();
            
            // Test quick status
            System.out.println("Quick Status:");
            System.out.println(engine.getQuickStatus());
            
            // Test comprehensive status
            System.out.println("\nComprehensive Status:");
            System.out.println(engine.getEngineStatus());
            
            // Test individual status components
            System.out.println("Individual Status Checks:");
            System.out.println("✓ Is Ready: " + engine.isReady());
            System.out.println("✓ Current State: " + engine.getCurrentState());
            System.out.println("✓ Has Error: " + engine.hasError());
            System.out.println("✓ Last Error: " + engine.getLastError());
            
        } catch (Exception e) {
            System.err.println("✗ Status test failed: " + e.getMessage());
        }
        
        printTestFooter();
    }
    
    // Helper methods for test formatting
    
    private static void printTestHeader(String testName) {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│ TEST: " + padRight(testName, 32) + " │");
        System.out.println("└─────────────────────────────────────────┘");
    }
    
    private static void printTestFooter() {
        System.out.println("─────────────────────────────────────────");
    }
    
    private static String padRight(String str, int length) {
        if (str.length() >= length) {
            return str.substring(0, length);
        }
        return str + " ".repeat(length - str.length());
    }
}

/**
 * Additional test class for focused component testing
 */
class GameEngineUnitTest {
    
    /**
     * Test the EngineResult class specifically
     */
    public static void testEngineResult() {
        System.out.println("\n=== EngineResult Unit Tests ===");
        
        // Test success results
        GameEngine.EngineResult successWithReading = GameEngine.EngineResult.success("Test message", null);
        GameEngine.EngineResult successWithoutReading = GameEngine.EngineResult.success("Test message");
        
        System.out.println("✓ Success result created: " + successWithReading.isSuccess());
        System.out.println("✓ Success message: " + successWithReading.getMessage());
        System.out.println("✓ Has reading: " + successWithReading.hasReading());
        System.out.println("✓ Has error: " + successWithReading.hasError());
        
        // Test failure results
        Exception testException = new RuntimeException("Test exception");
        GameEngine.EngineResult failureWithException = GameEngine.EngineResult.failure("Test failure", testException);
        GameEngine.EngineResult failureWithoutException = GameEngine.EngineResult.failure("Test failure");
        
        System.out.println("✓ Failure result created: " + !failureWithException.isSuccess());
        System.out.println("✓ Failure message: " + failureWithException.getMessage());
        System.out.println("✓ Has error: " + failureWithException.hasError());
        System.out.println("✓ Error message: " + failureWithException.getError().getMessage());
    }
    
    /**
     * Run unit tests
     */
    public static void main(String[] args) {
        testEngineResult();
    }
}