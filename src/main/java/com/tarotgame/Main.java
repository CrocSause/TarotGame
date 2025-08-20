// Main.java - Entry point for testing
package com.tarotgame;

/**
 * Main entry point for the Tarot Game application.
 * Currently runs the Card Model demonstration.
 */
public class Main {
    public static void main(String[] args) {
        // Run our Card Model tests
        System.out.println("Running Card Model Tests...\n");
        com.tarotgame.model.CardModelTest.main(args);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Run our Card Model demonstration
        CardModelDemo.main(args);
    }
}