// CardModelDemo.java
package com.tarotgame;

import java.util.List;
import java.util.Random;

import com.tarotgame.model.Card;
import com.tarotgame.model.MajorArcana;

/**
 * Demonstration of the Card Model functionality.
 * This serves as our POC 1: "Card Showcase" as defined in the roadmap.
 */
public class CardModelDemo {
    private static final Random random = new Random();
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║           TAROT CARD MODEL DEMO             ║");
        System.out.println("╚══════════════════════════════════════════════╝\n");
        
        // Demo 1: Show all Major Arcana cards
        demonstrateAllCards();
        
        // Demo 2: Show card orientations
        demonstrateCardOrientations();
        
        // Demo 3: Simulate a simple 3-card draw
        demonstrateSimpleCardDraw();
    }
    
    private static void demonstrateAllCards() {
        System.out.println("📚 COMPLETE MAJOR ARCANA DECK");
        System.out.println("═".repeat(50));
        
        List<Card> fullDeck = MajorArcana.createFullDeck();
        
        for (Card card : fullDeck) {
            System.out.printf("%2d. %-20s | %s%n", 
                card.getArcanaNumber(), 
                card.getName(),
                truncateMeaning(card.getUprightMeaning(), 40));
        }
        
        System.out.println("\n✨ Total cards in Major Arcana: " + fullDeck.size());
        System.out.println();
    }
    
    private static void demonstrateCardOrientations() {
        System.out.println("🔄 CARD ORIENTATIONS DEMO");
        System.out.println("═".repeat(50));
        
        Card demoCard = MajorArcana.THE_TOWER.createCard();
        
        System.out.println("Selected card: " + demoCard.getName());
        System.out.println();
        
        // Show upright
        System.out.println("UPRIGHT POSITION:");
        System.out.println("- Display Name: " + demoCard.getDisplayName());
        System.out.println("- Meaning: " + demoCard.getCurrentMeaning());
        System.out.println();
        
        // Flip to reversed
        demoCard.flip();
        System.out.println("REVERSED POSITION:");
        System.out.println("- Display Name: " + demoCard.getDisplayName());
        System.out.println("- Meaning: " + demoCard.getCurrentMeaning());
        System.out.println();
    }
    
    private static void demonstrateSimpleCardDraw() {
        System.out.println("🎴 SIMPLE THREE-CARD DRAW SIMULATION");
        System.out.println("═".repeat(50));
        
        List<Card> deck = MajorArcana.createFullDeck();
        
        // Simulate shuffling by picking random cards
        Card card1 = deck.get(random.nextInt(deck.size()));
        Card card2 = deck.get(random.nextInt(deck.size()));
        Card card3 = deck.get(random.nextInt(deck.size()));
        
        // Randomly orient some cards
        if (random.nextBoolean()) card1.flip();
        if (random.nextBoolean()) card2.flip();
        if (random.nextBoolean()) card3.flip();
        
        System.out.println("Drawing three cards for Past, Present, Future...\n");
        
        displayCardInReading("PAST", card1, 
            "This card represents influences from your past that shape your current situation.");
        
        displayCardInReading("PRESENT", card2, 
            "This card represents your current situation and immediate circumstances.");
        
        displayCardInReading("FUTURE", card3, 
            "This card represents potential outcomes and future influences.");
        
        System.out.println("\n" + "─".repeat(60));
        System.out.println("💫 This completes your three-card reading!");
        System.out.println("   Remember: The future is not set in stone.");
        System.out.println("─".repeat(60));
    }
    
    private static void displayCardInReading(String position, Card card, String context) {
        System.out.println("🔮 " + position + ":");
        System.out.println("   Card: " + card.getDisplayName());
        System.out.println("   Meaning: " + card.getCurrentMeaning());
        System.out.println("   Context: " + context);
        System.out.println();
    }
    
    private static String truncateMeaning(String meaning, int maxLength) {
        if (meaning.length() <= maxLength) {
            return meaning;
        }
        
        int lastSpace = meaning.lastIndexOf(' ', maxLength - 3);
        if (lastSpace > 0) {
            return meaning.substring(0, lastSpace) + "...";
        } else {
            return meaning.substring(0, maxLength - 3) + "...";
        }
    }
}


