package com.tarotgame;

import com.tarotgame.manual.TestSuite;

/**
 * Test-focused entry point for the Tarot Game application.
 * Runs comprehensive test suite and demonstrations.
 */
public class TestMain {
    
    public static void main(String[] args) {
        if (args.length > 0) {
            String command = args[0].toLowerCase();
            
            switch (command) {
                case "tests", "test" -> TestSuite.main(args);
                case "unit" -> runUnitTestsOnly();
                    
                case "integration" -> runIntegrationTestsOnly();
                    
                case "demo" -> runDemosOnly();
                    
                default -> showUsage();
            }
        } else {
            // Run everything by default
            TestSuite.main(args);
        }
    }
    
    private static void runUnitTestsOnly() {
        System.out.println("Running Unit Tests Only...");
        // Implementation here
    }
    
    private static void runIntegrationTestsOnly() {
        System.out.println("Running Integration Tests Only...");
        // Implementation here
    }
    
    private static void runDemosOnly() {
        System.out.println("Running Demonstrations Only...");
        // Implementation here
    }
    
    private static void showUsage() {
        System.out.println("Usage: java TestMain [command]");
        System.out.println("Commands:");
        System.out.println("  test, tests    - Run all tests and demos");
        System.out.println("  unit          - Run unit tests only");
        System.out.println("  integration   - Run integration tests only");
        System.out.println("  demo          - Run demonstrations only");
    }
}