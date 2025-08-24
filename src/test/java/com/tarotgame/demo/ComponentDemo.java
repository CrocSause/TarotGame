package com.tarotgame.demo;

import java.util.List;
import java.util.Random;

import com.tarotgame.model.Card;
import com.tarotgame.model.Deck;
import com.tarotgame.model.MajorArcana;

/**
 * Consolidated demonstration of core components.
 * Combines Card Model and Deck Management demonstrations.
 */
public class ComponentDemo {
    @SuppressWarnings("unused")
    private static final Random random = new Random();
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║         COMPONENT DEMONSTRATION             ║");
        System.out.println("╚══════════════════════════════════════════════╝\n");
        
        // Card Model Demonstration
        demonstrateCardModel();
        
        // Deck Management Demonstration  
        demonstrateDeckManagement();
        
        // Integration Demonstration
        demonstrateIntegration();
    }
    
    private static void demonstrateCardModel() {
        System.out.println("🎴 CARD MODEL SHOWCASE");
        System.out.println("═".repeat(50));
        
        // Show sample cards with orientations
        Card[] sampleCards = {
            MajorArcana.THE_FOOL.createCard(),
            MajorArcana.THE_MAGICIAN.createCard(),
            MajorArcana.THE_WORLD.createCard()
        };
        
        sampleCards[1].flip(); // Make Magician reversed
        
        System.out.println("Sample Major Arcana Cards:");
        for (Card card : sampleCards) {
            System.out.printf("• %-25s | %s%n", 
                card.getDisplayName(), 
                truncate(card.getCurrentMeaning(), 35));
        }
        
        System.out.println("\n✨ " + MajorArcana.values().length + " Major Arcana cards available\n");
    }
    
    private static void demonstrateDeckManagement() {
        System.out.println("🎲 DECK MANAGEMENT SHOWCASE");
        System.out.println("═".repeat(50));
        
        Deck deck = new Deck();
        
        // Show initial state
        System.out.println("Fresh deck: " + deck.getStatus());
        
        // Draw some cards
        System.out.println("\nDrawing 5 cards:");
        for (int i = 0; i < 5; i++) {
            Card card = deck.drawCard();
            System.out.printf("%d. %s%n", i + 1, card.getName());
        }
        
        // Show updated state
        System.out.println("\nDeck after drawing: " + deck.getStatus());
        
        // Demonstrate auto-reshuffle
        System.out.println("\nTesting auto-reshuffle feature...");
        
        // Draw cards until near empty, then trigger reshuffle
        while (deck.size() >= 3) {
            deck.drawCard();
        }
        System.out.println("Before reshuffle: " + deck.size() + " cards remaining");
        
        Card cardFromReshuffledDeck = deck.drawCard();
        System.out.println("After auto-reshuffle: " + deck.size() + " cards remaining");
        System.out.println("Drew from fresh deck: " + cardFromReshuffledDeck.getName());
        
        System.out.println("\n✨ Auto-reshuffle keeps the game going!\n");
    }
    
    private static void demonstrateIntegration() {
        System.out.println("🔗 INTEGRATION SHOWCASE");
        System.out.println("═".repeat(50));
        
        // Simulate complete reading workflow
        Deck deck = new Deck();
        List<Card> reading = deck.drawCards(3);
        
        String[] positions = {"PAST", "PRESENT", "FUTURE"};
        
        System.out.println("Complete Three-Card Reading:");
        for (int i = 0; i < reading.size(); i++) {
            Card card = reading.get(i);
            System.out.printf("🔮 %-8s: %s%n", positions[i], card.getName());
            System.out.printf("   Meaning: %s%n", truncate(card.getCurrentMeaning(), 50));
        }
        
        System.out.println("\n✨ Ready for user interface integration!");
    }
    
    private static String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        int lastSpace = text.lastIndexOf(' ', maxLength - 3);
        return lastSpace > 0 ? text.substring(0, lastSpace) + "..." : text.substring(0, maxLength - 3) + "...";
    }
}