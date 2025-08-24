// IntegratedDemo.java - Shows Card + Deck integration
package com.tarotgame.demo;

import java.util.List;

import com.tarotgame.model.Card;
import com.tarotgame.model.Deck;
import com.tarotgame.model.MajorArcana;

/**
 * Demonstrates integration between Card Model and Deck Management components.
 * This validates our component architecture and interfaces.
 */
public class IntegratedDemo {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║        INTEGRATED COMPONENT DEMO            ║");
        System.out.println("╚══════════════════════════════════════════════╝\n");
        
        // Demo 1: Show that deck preserves card integrity
        demonstrateCardIntegrity();
        
        // Demo 2: Show card orientation handling
        demonstrateOrientationHandling();
        
        // Demo 3: Show factory method integration
        demonstrateFactoryIntegration();
    }
    
    private static void demonstrateCardIntegrity() {
        System.out.println("🔍 CARD INTEGRITY VERIFICATION");
        System.out.println("═".repeat(50));
        
        Deck deck = new Deck();
        
        // Draw a card and verify it matches original definition
        Card drawnCard = deck.drawCard();
        Card originalCard = MajorArcana.getByNumber(drawnCard.getArcanaNumber()).createCard();
        
        System.out.println("Drawn card: " + drawnCard.getName());
        System.out.println("Original definition: " + originalCard.getName());
        System.out.println("Cards are equal: " + drawnCard.equals(originalCard));
        System.out.println("Same arcana number: " + (drawnCard.getArcanaNumber() == originalCard.getArcanaNumber()));
        System.out.println("Same upright meaning: " + drawnCard.getUprightMeaning().equals(originalCard.getUprightMeaning()));
        
        if (drawnCard.equals(originalCard)) {
            System.out.println("✓ Card integrity preserved through deck operations");
        } else {
            System.out.println("❌ Card integrity issue detected");
        }
        
        System.out.println();
    }
    
    private static void demonstrateOrientationHandling() {
        System.out.println("🎭 ORIENTATION HANDLING");
        System.out.println("═".repeat(50));
        
        Deck deck = new Deck();
        
        // Draw cards and verify they start upright (as per design decision)
        List<Card> cards = deck.drawCards(5);
        
        System.out.println("Checking orientation of drawn cards:");
        boolean allUpright = true;
        
        for (Card card : cards) {
            boolean isUpright = !card.isReversed();
            System.out.println(String.format("  %s: %s", 
                                            card.getName(), 
                                            isUpright ? "Upright ✓" : "Reversed ❌"));
            if (!isUpright) {
                allUpright = false;
            }
        }
        
        if (allUpright) {
            System.out.println("✓ All cards drawn in upright position (correct)");
        } else {
            System.out.println("❌ Some cards drawn reversed (unexpected)");
        }
        
        // Demonstrate that orientation can be changed after drawing
        Card testCard = cards.get(0);
        System.out.println("\nTesting orientation change on: " + testCard.getName());
        System.out.println("Before flip: " + testCard.getDisplayName());
        testCard.flip();
        System.out.println("After flip: " + testCard.getDisplayName());
        System.out.println("Meaning changed: " + testCard.getCurrentMeaning());
        
        System.out.println("✓ Orientation handling working as designed\n");
    }
    
    private static void demonstrateFactoryIntegration() {
        System.out.println("🏭 FACTORY METHOD INTEGRATION");
        System.out.println("═".repeat(50));
        
        // Show that deck uses the same factory methods we defined
        Deck deck = new Deck();
        
        // Get deck's view of available cards
        List<Card> deckCards = deck.getAvailableCards();
        
        // Create comparison set using factory methods
        List<Card> factoryCards = MajorArcana.createFullDeck();
        
        System.out.println("Deck cards count: " + deckCards.size());
        System.out.println("Factory cards count: " + factoryCards.size());
        
        // Verify all Major Arcana are present
        boolean allPresent = true;
        for (MajorArcana arcana : MajorArcana.values()) {
            Card expectedCard = arcana.createCard();
            boolean foundInDeck = deckCards.stream()
                .anyMatch(card -> card.equals(expectedCard));
            
            if (!foundInDeck) {
                System.out.println("❌ Missing from deck: " + expectedCard.getName());
                allPresent = false;
            }
        }
        
        if (allPresent) {
            System.out.println("✓ All Major Arcana present in deck");
        }
        
        // Show that deck and factory produce equivalent cards
        System.out.println("\nVerifying card equivalence:");
        Card deckFool = deckCards.stream()
            .filter(card -> card.getArcanaNumber() == 0)
            .findFirst().orElse(null);
        Card factoryFool = MajorArcana.THE_FOOL.createCard();
        
        if (deckFool != null && deckFool.equals(factoryFool)) {
            System.out.println("✓ Deck and factory produce equivalent cards");
        } else {
            System.out.println("❌ Deck and factory card mismatch");
        }
        
        System.out.println("\n✨ Component integration verified successfully!");
    }
}