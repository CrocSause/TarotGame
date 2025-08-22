// DeckDemo.java
package com.tarotgame;

import java.util.List;

import com.tarotgame.model.Card;
import com.tarotgame.model.Deck;

/**
 * Demonstration of Deck Management functionality.
 * This serves as POC 2: "Magic 8-Ball Style" from our roadmap.
 */
public class DeckDemo {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║           DECK MANAGEMENT DEMO              ║");
        System.out.println("╚══════════════════════════════════════════════╝\n");
        
        // Demo 1: Basic deck operations
        demonstrateBasicOperations();
        
        // Demo 2: Auto-reshuffle behavior
        demonstrateAutoReshuffle();
        
        // Demo 3: Three-card reading simulation
        demonstrateThreeCardReading();
        
        // Demo 4: Multiple readings simulation
        demonstrateMultipleReadings();
    }
    
    private static void demonstrateBasicOperations() {
        System.out.println("🎴 BASIC DECK OPERATIONS");
        System.out.println("═".repeat(50));
        
        Deck deck = new Deck();
        
        System.out.println("Freshly created deck:");
        System.out.println("  " + deck.getStatus());
        System.out.println("  Has enough cards for reading: " + deck.hasEnoughCardsForReading());
        System.out.println();
        
        System.out.println("Drawing first 5 cards:");
        for (int i = 0; i < 5; i++) {
            Card card = deck.drawCard();
            System.out.println(String.format("  %d. %s", i + 1, card.getName()));
        }
        
        System.out.println("\nDeck status after drawing 5 cards:");
        System.out.println("  " + deck.getStatus());
        System.out.println("  Next card (peek): " + (deck.peek() != null ? deck.peek().getName() : "None"));
        
        System.out.println("\n✨ Basic operations completed!\n");
    }
    
    private static void demonstrateAutoReshuffle() {
        System.out.println("🔄 AUTO-RESHUFFLE DEMONSTRATION");
        System.out.println("═".repeat(50));
        
        Deck deck = new Deck();
        
        System.out.println("Simulating extensive card drawing to trigger reshuffle...");
        
        int totalDrawn = 0;
        int reshuffleCount = 0;
        
        // Draw cards until we've seen several reshuffles
        while (reshuffleCount < 2) {
            int sizeBefore = deck.size();
            Card card = deck.drawCard();
            int sizeAfter = deck.size();
            totalDrawn++;
            
            // Check if reshuffle occurred (deck size increased)
            if (sizeAfter > sizeBefore) {
                reshuffleCount++;
                System.out.println(String.format("  🔄 RESHUFFLE #%d occurred! Drew: %s", 
                                                reshuffleCount, card.getName()));
                System.out.println(String.format("     Deck size: %d → %d", sizeBefore, sizeAfter));
            } else if (totalDrawn <= 10 || totalDrawn % 10 == 0) {
                // Show some draws for context
                System.out.println(String.format("  Card #%d: %s (Deck: %d cards)", 
                                                totalDrawn, card.getName(), sizeAfter));
            }
        }
        
        System.out.println(String.format("\n✨ Total cards drawn: %d, Reshuffles triggered: %d\n", 
                                        totalDrawn, reshuffleCount));
    }
    
    private static void demonstrateThreeCardReading() {
        System.out.println("🔮 THREE-CARD READING SIMULATION");
        System.out.println("═".repeat(50));
        
        Deck deck = new Deck();
        
        System.out.println("Performing a Past/Present/Future reading...\n");
        
        List<Card> reading = deck.drawCards(3);
        String[] positions = {"PAST", "PRESENT", "FUTURE"};
        String[] contexts = {
            "Influences from your past that shape your current situation",
            "Your current situation and immediate circumstances", 
            "Potential outcomes and future influences"
        };
        
        for (int i = 0; i < reading.size(); i++) {
            Card card = reading.get(i);
            System.out.println(String.format("🌟 %s:", positions[i]));
            System.out.println(String.format("   Card: %s", card.getName()));
            System.out.println(String.format("   Meaning: %s", card.getCurrentMeaning()));
            System.out.println(String.format("   Context: %s", contexts[i]));
            System.out.println();
        }
        
        System.out.println(String.format("Deck status after reading: %s", deck.getStatus()));
        System.out.println("✨ Your reading is complete!\n");
    }
    
    private static void demonstrateMultipleReadings() {
        System.out.println("📚 MULTIPLE READINGS SIMULATION");
        System.out.println("═".repeat(50));
        
        Deck deck = new Deck();
        
        System.out.println("Simulating multiple clients getting readings...\n");
        
        for (int readingNum = 1; readingNum <= 4; readingNum++) {
            System.out.println(String.format("--- Reading #%d ---", readingNum));
            
            // Show deck status before reading
            System.out.println("Deck status: " + deck.getStatus());
            
            if (!deck.hasEnoughCardsForReading()) {
                System.out.println("⚠ Deck low on cards - will auto-reshuffle");
            }
            
            List<Card> cards = deck.drawCards(3);
            
            System.out.println("Cards drawn:");
            for (int i = 0; i < cards.size(); i++) {
                Card card = cards.get(i);
                String position = (i == 0) ? "Past" : (i == 1) ? "Present" : "Future";
                System.out.println(String.format("  %s: %s", position, card.getName()));
            }
            
            System.out.println("Remaining cards: " + deck.size());
            System.out.println();
        }
        
        System.out.println("✨ Multiple readings completed successfully!");
        
        // Show final deck statistics
        System.out.println("\n📊 Final Deck Statistics:");
        System.out.println("  " + deck.getStatus());
        System.out.println("  Total cards drawn: " + deck.getDrawnCount());
        System.out.println("  Reshuffles triggered: " + (deck.getDrawnCount() / 22)); // Approximate
    }
}