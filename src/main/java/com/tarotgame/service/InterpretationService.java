
package com.tarotgame.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarotgame.model.Card;

/**
 * Service class responsible for loading and providing tarot card interpretations
 * from external JSON file. Supports position-aware readings for Past/Present/Future spreads.
 */
public class InterpretationService {
    
    private static final String MEANINGS_FILE = "/card_meanings.json";
    private final Map<Integer, CardMeaning> cardMeanings;
    private final ObjectMapper objectMapper;
    
    /**
     * Position types for tarot readings
     */
    public enum Position {
        PAST("pastContext"),
        PRESENT("presentContext"), 
        FUTURE("futureContext"),
        GENERAL("general");
        
        private final String jsonKey;
        
        Position(String jsonKey) {
            this.jsonKey = jsonKey;
        }
        
        public String getJsonKey() {
            return jsonKey;
        }
    }

    /**
     * Create a Card instance from JSON data
     * This integrates with your existing Card class to create fully functional cards
     */
    public Card createCard(int arcanaNumber) {
        CardMeaning cardMeaning = getCardMeaning(arcanaNumber);
        
        // Get general meanings for the Card constructor
        String uprightMeaning = cardMeaning.getUpright().getGeneral();
        String reversedMeaning = cardMeaning.getReversed().getGeneral();
        
        return new Card(arcanaNumber, cardMeaning.getName(), uprightMeaning, reversedMeaning);
    }
    
    /**
     * Create all Major Arcana cards
     * Returns array of all 22 cards with meanings loaded from JSON
     */
    public Card[] createAllCards() {
        Card[] cards = new Card[22];
        for (int i = 0; i <= 21; i++) {
            cards[i] = createCard(i);
        }
        return cards;
    }
    
    /**
     * Inner class to represent card meaning data
     */
    public static class CardMeaning {
        private int id;
        private String name;
        private Interpretation upright;
        private Interpretation reversed;
        
        public CardMeaning() {}
        
        // Getters and setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Interpretation getUpright() { return upright; }
        public void setUpright(Interpretation upright) { this.upright = upright; }
        
        public Interpretation getReversed() { return reversed; }
        public void setReversed(Interpretation reversed) { this.reversed = reversed; }
    }
    
    /**
     * Inner class to represent interpretation data (upright/reversed)
     */
    public static class Interpretation {
        private List<String> keywords;
        private String general;
        private String pastContext;
        private String presentContext;
        private String futureContext;
        
        public Interpretation() {}
        
        // Getters and setters
        public List<String> getKeywords() { return keywords; }
        public void setKeywords(List<String> keywords) { this.keywords = keywords; }
        
        public String getGeneral() { return general; }
        public void setGeneral(String general) { this.general = general; }
        
        public String getPastContext() { return pastContext; }
        public void setPastContext(String pastContext) { this.pastContext = pastContext; }
        
        public String getPresentContext() { return presentContext; }
        public void setPresentContext(String presentContext) { this.presentContext = presentContext; }
        
        public String getFutureContext() { return futureContext; }
        public void setFutureContext(String futureContext) { this.futureContext = futureContext; }
        
        /**
         * Get meaning based on position
         */
        public String getMeaningByPosition(Position position) {
            return switch (position) {
                case PAST -> pastContext;
                case PRESENT -> presentContext;
                case FUTURE -> futureContext;
                case GENERAL -> general;
                default -> general;
            };
        }
    }
    
    /**
     * Constructor - automatically loads card meanings
     */
    public InterpretationService() {
        this.objectMapper = new ObjectMapper();
        this.cardMeanings = new HashMap<>();
        loadCardMeanings();
    }
    
    /**
     * Load card meanings from JSON file
     */
    private void loadCardMeanings() {
        try (InputStream inputStream = getClass().getResourceAsStream(MEANINGS_FILE)) {
            if (inputStream == null) {
                throw new RuntimeException("Could not find " + MEANINGS_FILE + " in classpath");
            }
            
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode majorArcanaNode = rootNode.get("majorArcana");
            
            if (majorArcanaNode != null && majorArcanaNode.isArray()) {
                for (JsonNode cardNode : majorArcanaNode) {
                    CardMeaning cardMeaning = objectMapper.treeToValue(cardNode, CardMeaning.class);
                    cardMeanings.put(cardMeaning.getId(), cardMeaning);
                }
            }
            
            System.out.println("Loaded " + cardMeanings.size() + " card meanings successfully.");
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to load card meanings: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get card meaning by arcana number
     */
    public CardMeaning getCardMeaning(int arcanaNumber) {
        CardMeaning meaning = cardMeanings.get(arcanaNumber);
        if (meaning == null) {
            throw new IllegalArgumentException("No meaning found for arcana number: " + arcanaNumber);
        }
        return meaning;
    }
    
    /**
     * Get specific interpretation for a card
     */
    public String getCardInterpretation(int arcanaNumber, boolean isReversed, Position position) {
        CardMeaning cardMeaning = getCardMeaning(arcanaNumber);
        Interpretation interpretation = isReversed ? cardMeaning.getReversed() : cardMeaning.getUpright();
        return interpretation.getMeaningByPosition(position);
    }
    
    /**
     * Get keywords for a card
     */
    public List<String> getCardKeywords(int arcanaNumber, boolean isReversed) {
        CardMeaning cardMeaning = getCardMeaning(arcanaNumber);
        Interpretation interpretation = isReversed ? cardMeaning.getReversed() : cardMeaning.getUpright();
        return new ArrayList<>(interpretation.getKeywords());
    }
    
    /**
     * Get card name by arcana number
     */
    public String getCardName(int arcanaNumber) {
        return getCardMeaning(arcanaNumber).getName();
    }
    
    /**
     * Generate a complete reading interpretation for multiple cards
     * Assumes cards array corresponds to [Past, Present, Future] positions
     */
    public ReadingInterpretation generateReading(Card[] cards) {
        if (cards == null || cards.length != 3) {
            throw new IllegalArgumentException("Reading requires exactly 3 cards for Past/Present/Future");
        }
        
        Position[] positions = {Position.PAST, Position.PRESENT, Position.FUTURE};
        String[] interpretations = new String[3];
        String[] cardNames = new String[3];
        
        for (int i = 0; i < 3; i++) {
            Card card = cards[i];
            interpretations[i] = getCardInterpretation(card.getArcanaNumber(), card.isReversed(), positions[i]);
            cardNames[i] = card.getDisplayName();
        }
        
        return new ReadingInterpretation(cardNames, interpretations, generateOverallReading(cards));
    }
    
    /**
     * Generate an overall reading that combines the three card meanings
     */
    private String generateOverallReading(Card[] cards) {
        StringBuilder overall = new StringBuilder();
        overall.append("OVERALL READING:\n\n");
        
        overall.append("Your past experiences ");
        if (cards[0].isReversed()) {
            overall.append("have created challenges that ");
        } else {
            overall.append("have provided wisdom that ");
        }
        
        overall.append("now influences your present situation, where ");
        if (cards[1].isReversed()) {
            overall.append("you may be facing obstacles. ");
        } else {
            overall.append("you have opportunities for growth. ");
        }
        
        overall.append("Looking ahead, your future ");
        if (cards[2].isReversed()) {
            overall.append("will require careful navigation of upcoming challenges, ");
            overall.append("but remember that reversed cards often indicate internal work or lessons to be learned.");
        } else {
            overall.append("holds promise and positive developments, ");
            overall.append("building upon the foundation you're creating now.");
        }
        
        return overall.toString();
    }
    
    /**
     * Check if service is properly loaded
     */
    public boolean isLoaded() {
        return cardMeanings != null && cardMeanings.size() == 22;
    }
    
    /**
     * Get number of loaded card meanings
     */
    public int getLoadedCardCount() {
        return cardMeanings.size();
    }
    
    /**
     * Inner class to represent a complete reading result
     */
    public static class ReadingInterpretation {
        private final String[] cardNames;
        private final String[] individualInterpretations;
        private final String overallInterpretation;
        
        public ReadingInterpretation(String[] cardNames, String[] individualInterpretations, String overallInterpretation) {
            this.cardNames = cardNames.clone();
            this.individualInterpretations = individualInterpretations.clone();
            this.overallInterpretation = overallInterpretation;
        }
        
        public String[] getCardNames() { return cardNames.clone(); }
        public String[] getIndividualInterpretations() { return individualInterpretations.clone(); }
        public String getOverallInterpretation() { return overallInterpretation; }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            String[] positions = {"PAST", "PRESENT", "FUTURE"};
            
            for (int i = 0; i < 3; i++) {
                sb.append(positions[i]).append(": ").append(cardNames[i]).append("\n");
                sb.append(individualInterpretations[i]).append("\n\n");
            }
            
            sb.append(overallInterpretation);
            return sb.toString();
        }
    }
}