// Main.java - Entry point for testing
package com.tarotgame;

/**
 * Main entry point for the Tarot Game application.
 * Currently runs Card Model and Deck Management demonstrations.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("🎴 TAROT GAME - COMPONENT TESTING SUITE");
        System.out.println("═".repeat(60) + "\n");
        
        // Phase 1: Card Model Testing
        System.out.println("PHASE 1: CARD MODEL COMPONENT");
        System.out.println("-".repeat(40));
        com.tarotgame.model.CardModelTest.main(args);
        
        System.out.println("\n" + "═".repeat(60) + "\n");
        
        // Phase 2: Deck Management Testing
        System.out.println("PHASE 2: DECK MANAGEMENT COMPONENT");
        System.out.println("-".repeat(40));
        com.tarotgame.model.DeckTest.main(args);
        
        System.out.println("\n" + "═".repeat(60) + "\n");
        
        // Demo Phase: Component Demonstrations
        System.out.println("DEMONSTRATION PHASE");
        System.out.println("-".repeat(40));
        
        System.out.println("\n>>> Card Model Demo <<<");
        CardModelDemo.main(args);
        
        System.out.println("\n>>> Deck Management Demo <<<");
        DeckDemo.main(args);
        
        System.out.println("\n>>> Integrated Components Demo <<<");
        IntegratedDemo.main(args);
        
        System.out.println("\n" + "═".repeat(60));
        System.out.println("🎉 ALL COMPONENT TESTING COMPLETED SUCCESSFULLY!");
        System.out.println("   Ready to proceed to next development phase.");
        System.out.println("═".repeat(60));
    }
}