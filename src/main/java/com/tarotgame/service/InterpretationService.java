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
 * Enhanced service class responsible for loading and providing tarot card interpretations
 * from external JSON file. Supports position-aware readings for Past/Present/Future spreads
 * with sophisticated overall reading generation.
 */
public class InterpretationService {
    
    private static final String MEANINGS_FILE = "/com/tarotgame/resources/card_meanings.json";
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
     * Card themes for narrative generation
     */
    public enum CardTheme {
        BEGINNINGS(0, 1, 3, 6), // Fool, Magician, Empress, Lovers
        AUTHORITY(4, 5, 8, 11), // Emperor, Hierophant, Strength, Justice  
        INTROSPECTION(2, 9, 12, 18), // High Priestess, Hermit, Hanged Man, Moon
        TRANSFORMATION(13, 15, 16, 20), // Death, Devil, Tower, Judgement
        FULFILLMENT(10, 14, 17, 19, 21), // Wheel, Temperance, Star, Sun, World
        CHALLENGE(7, 12, 15, 16), // Chariot, Hanged Man, Devil, Tower
        SPIRITUAL(2, 5, 9, 17, 20); // High Priestess, Hierophant, Hermit, Star, Judgement
        
        private final int[] cardNumbers;
        
        CardTheme(int... cardNumbers) {
            this.cardNumbers = cardNumbers;
        }
        
        public boolean containsCard(int cardNumber) {
            for (int num : cardNumbers) {
                if (num == cardNumber) return true;
            }
            return false;
        }
    }
    
    /**
     * Narrative intensity based on card combinations and orientations
     */
    public enum NarrativeIntensity {
        GENTLE, MODERATE, INTENSE, TRANSFORMATIVE
    }
    
    /**
     * Inner class to hold reading analysis data
     */
    private static class ReadingAnalysis {
        final int reversedCount;
        final List<CardTheme> themes;
        final List<String> dominantKeywords;
        final NarrativeIntensity intensity;
        
        ReadingAnalysis(int reversedCount, List<CardTheme> themes, 
                       List<String> dominantKeywords, NarrativeIntensity intensity) {
            this.reversedCount = reversedCount;
            this.themes = themes;
            this.dominantKeywords = dominantKeywords;
            this.intensity = intensity;
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
     * ENHANCED: Generate an overall reading that considers specific cards and their themes
     */
    private String generateOverallReading(Card[] cards) {
        // Analyze the reading composition
        ReadingAnalysis analysis = analyzeReading(cards);
        
        StringBuilder overall = new StringBuilder();
        overall.append("OVERALL READING:\n\n");
        
        // Generate past narrative
        overall.append(generatePastNarrative(cards[0], analysis));
        overall.append(" ");
        
        // Generate present narrative with connection to past
        overall.append(generatePresentNarrative(cards[1], cards[0], analysis));
        overall.append(" ");
        
        // Generate future narrative with connection to present theme
        overall.append(generateFutureNarrative(cards[2], analysis));
        
        // Add thematic conclusion
        overall.append("\n\n");
        overall.append(generateThematicConclusion(analysis));
        
        return overall.toString();
    }
    
    /**
     * Analyze the overall composition of the reading
     */
    private ReadingAnalysis analyzeReading(Card[] cards) {
        int reversedCount = 0;
        List<CardTheme> themes = new ArrayList<>();
        List<String> dominantKeywords = new ArrayList<>();
        
        // Count reversed cards and gather themes
        for (Card card : cards) {
            if (card.isReversed()) reversedCount++;
            
            // Determine themes for this card
            for (CardTheme theme : CardTheme.values()) {
                if (theme.containsCard(card.getArcanaNumber())) {
                    themes.add(theme);
                    break;
                }
            }
            
            // Gather keywords
            List<String> keywords = getCardKeywords(card.getArcanaNumber(), card.isReversed());
            if (!keywords.isEmpty()) {
                dominantKeywords.add(keywords.get(0)); // Take first keyword as dominant
            }
        }
        
        // Determine narrative intensity
        NarrativeIntensity intensity = determineIntensity(cards, reversedCount, themes);
        
        return new ReadingAnalysis(reversedCount, themes, dominantKeywords, intensity);
    }
    
    /**
     * Generate past narrative based on the past card and overall context
     */
    private String generatePastNarrative(Card pastCard, ReadingAnalysis analysis) {
        int cardNum = pastCard.getArcanaNumber();
        boolean reversed = pastCard.isReversed();
        
        // Card-specific past narratives
        String baseNarrative = switch (cardNum) {
            case 0 -> // The Fool
                reversed ? "A reckless decision or missed opportunity in your past" 
                        : "A courageous leap of faith in your past";
            case 1 -> // The Magician  
                reversed ? "Unfocused energy or manipulation in your past"
                        : "A time of personal power and manifestation";
            case 2 -> // High Priestess
                reversed ? "Ignored intuition or hidden secrets from your past"
                        : "Deep spiritual wisdom gained through past experiences";
            case 3 -> // Empress
                reversed ? "Creative blocks or nurturing issues in your past"
                        : "A period of abundant creativity and growth";
            case 4 -> // Emperor
                reversed ? "Authoritarian control or rigid structures you've overcome"
                        : "Strong leadership and stability that shaped you";
            case 5 -> // Hierophant
                reversed ? "Rejection of tradition or spiritual rebellion in your past"
                        : "Traditional wisdom and spiritual guidance that grounded you";
            case 6 -> // Lovers
                reversed ? "Relationship conflicts or poor choices in love"
                        : "A significant relationship or important life choice";
            case 7 -> // Chariot
                reversed ? "Lost control or misdirected efforts in your past"
                        : "Determined action and victorious struggle";
            case 8 -> // Strength
                reversed ? "Self-doubt or abuse of power in your past"
                        : "Inner strength and compassionate courage";
            case 9 -> // Hermit
                reversed ? "Isolation or refusing to seek wisdom"
                        : "A period of soul-searching and inner discovery";
            case 10 -> // Wheel of Fortune
                reversed ? "Bad luck or resisting life's natural cycles"
                         : "Favorable changes and karmic rewards";
            case 11 -> // Justice
                reversed ? "Injustice or avoiding accountability"
                         : "Fair decisions and balanced judgment";
            case 12 -> // Hanged Man
                reversed ? "Stagnation or refusing to sacrifice"
                         : "Patient sacrifice that led to enlightenment";
            case 13 -> // Death
                reversed ? "Resistance to necessary changes in your past"
                         : "A major transformation that ended one chapter of your life";
            case 14 -> // Temperance
                reversed ? "Imbalance or impatience in past situations"
                         : "Harmonious blending and patient moderation";
            case 15 -> // Devil
                reversed ? "Breaking free from limiting beliefs or addictions"
                         : "Temptation or bondage that taught hard lessons";
            case 16 -> // Tower
                reversed ? "A personal crisis you narrowly avoided or internalized"
                         : "A sudden upheaval that shattered old foundations";
            case 17 -> // Star
                reversed ? "Lost hope or disconnection from your dreams"
                         : "Renewed hope and spiritual healing";
            case 18 -> // Moon
                reversed ? "Confusion or deception that you've overcome"
                         : "Intuitive insights emerging from uncertainty";
            case 19 -> // Sun
                reversed ? "Dimmed joy or delayed success in your past"
                         : "A time of pure joy and enlightened success";
            case 20 -> // Judgement
                reversed ? "Self-criticism or ignoring important calls to action"
                         : "A significant awakening or rebirth";
            case 21 -> // World
                reversed ? "An incomplete achievement or unfulfilled goal"
                         : "A significant accomplishment or completion";
            default -> 
                reversed ? "Challenging experiences that tested your resolve"
                        : "Formative experiences that built your character";
        };
        
        // Add connecting phrase based on intensity
        String connector = switch (analysis.intensity) {
            case GENTLE -> " has gently influenced";
            case MODERATE -> " has shaped";
            case INTENSE -> " has dramatically altered";
            case TRANSFORMATIVE -> " has fundamentally transformed";
        };
        
        return baseNarrative + connector;
    }
    
    /**
     * Generate present narrative with connection to past
     */
    private String generatePresentNarrative(Card presentCard, Card pastCard, ReadingAnalysis analysis) {
        int cardNum = presentCard.getArcanaNumber();
        boolean reversed = presentCard.isReversed();
        
        String presentSituation = switch (cardNum) {
            case 0 -> // The Fool
                reversed ? "your current situation, where impulsiveness may lead you astray"
                        : "your present moment, filled with new possibilities and fresh starts";
            case 1 -> // The Magician
                reversed ? "your current circumstances, where you may be struggling to focus your abilities"
                        : "your present reality, where you have all the tools needed for success";
            case 2 -> // High Priestess
                reversed ? "your current state, where you may be disconnected from your intuition"
                        : "your present awareness, guided by deep inner wisdom";
            case 3 -> // Empress
                reversed ? "your current phase, where creativity feels blocked"
                        : "your present abundance, where creativity and nurturing flourish";
            case 4 -> // Emperor
                reversed ? "your current situation, where control feels rigid or oppressive"
                        : "your present position of authority and structured leadership";
            case 5 -> // Hierophant
                reversed ? "your current rebellion against traditional expectations"
                        : "your present alignment with spiritual wisdom and tradition";
            case 6 -> // Lovers
                reversed ? "your current relationships, where harmony feels elusive"
                        : "your present choices, guided by love and aligned values";
            case 7 -> // Chariot
                reversed ? "your current path, where you may feel pulled in different directions"
                        : "your present journey, where determination drives you forward";
            case 8 -> // Strength
                reversed ? "your current struggles with self-doubt or harsh approaches"
                        : "your present mastery, combining strength with compassion";
            case 9 -> // Hermit
                reversed ? "your current isolation, which may be hindering growth"
                        : "your present introspection, seeking deeper truths";
            case 10 -> // Wheel of Fortune
                reversed ? "your current situation, where you may feel at the mercy of circumstances"
                         : "your present moment, where positive changes are in motion";
            case 11 -> // Justice
                reversed ? "your current dealings, where fairness seems absent"
                         : "your present accountability, where truth and balance prevail";
            case 12 -> // Hanged Man
                reversed ? "your current resistance to necessary patience"
                         : "your present suspension, gaining wisdom through waiting";
            case 13 -> // Death
                reversed ? "your current phase, where you're resisting necessary changes"
                         : "your present transformation, as old patterns fall away";
            case 14 -> // Temperance
                reversed ? "your current imbalance, seeking harmony"
                         : "your present equilibrium, blending all aspects gracefully";
            case 15 -> // Devil
                reversed ? "your current liberation from limiting patterns"
                         : "your present entanglement, recognizing what binds you";
            case 16 -> // Tower
                reversed ? "your current state, where you're rebuilding after disruption"
                         : "your present crisis, where sudden revelations demand attention";
            case 17 -> // Star
                reversed ? "your present disconnection from hope and purpose"
                         : "your current renewal, guided by hope and spiritual insight";
            case 18 -> // Moon
                reversed ? "your current clarity, emerging from confusion"
                         : "your present uncertainty, where intuition must guide you";
            case 19 -> // Sun
                reversed ? "your current phase, where joy feels temporarily dimmed"
                         : "your present radiance, where success and vitality shine";
            case 20 -> // Judgement
                reversed ? "your current self-judgment, perhaps too harsh"
                         : "your present awakening to a higher calling";
            case 21 -> // World
                reversed ? "your current near-completion, requiring final steps"
                         : "your present fulfillment, having achieved integration";
            default ->
                reversed ? "your current situation, where you face internal challenges"
                        : "your present circumstances, where growth opportunities abound";
        };
        
        // Add thematic connection if past and present share themes
        if (shareTheme(pastCard, presentCard)) {
            presentSituation += ". This continues the theme established in your past";
        }
        
        return presentSituation;
    }
    
    /**
     * Generate future narrative based on present momentum
     */
    private String generateFutureNarrative(Card futureCard, ReadingAnalysis analysis) {
        int cardNum = futureCard.getArcanaNumber();
        boolean reversed = futureCard.isReversed();
        
        String futureOutlook = switch (cardNum) {
            case 0 -> // The Fool
                reversed ? "Looking ahead, be cautious of hasty decisions and learn from past mistakes."
                        : "Your future holds exciting new beginnings and adventures waiting to unfold.";
            case 1 -> // The Magician
                reversed ? "In the future, focus on developing your skills before taking action."
                        : "Your future promises the successful manifestation of your goals.";
            case 2 -> // High Priestess
                reversed ? "Your future requires reconnecting with your intuitive wisdom."
                        : "Deep spiritual insights will illuminate your future path.";
            case 3 -> // Empress
                reversed ? "Future creativity will flow once current blocks are cleared."
                        : "Your future blossoms with abundant creativity and nurturing growth.";
            case 4 -> // Emperor
                reversed ? "Future success requires balancing authority with flexibility."
                        : "Your future is built on strong foundations and clear leadership.";
            case 5 -> // Hierophant
                reversed ? "Your future path may diverge from traditional expectations."
                        : "Spiritual wisdom and traditional values will guide your future.";
            case 6 -> // Lovers
                reversed ? "Future relationships require healing and better choices."
                        : "Love and aligned partnerships illuminate your future.";
            case 7 -> // Chariot
                reversed ? "Your future success requires regaining focus and direction."
                        : "Victory and purposeful progress define your future journey.";
            case 8 -> // Strength
                reversed ? "Future challenges will help you develop true inner strength."
                        : "Your future is empowered by compassionate strength and self-mastery.";
            case 9 -> // Hermit
                reversed ? "Your future benefits from seeking guidance and connection."
                        : "Wisdom gained through introspection will light your future way.";
            case 10 -> // Wheel of Fortune
                reversed ? "The future may bring challenges, but remember that all cycles eventually turn."
                         : "Your future is brightened by favorable circumstances and good fortune.";
            case 11 -> // Justice
                reversed ? "Future balance requires addressing current inequities."
                         : "Justice and fair outcomes await you in the future.";
            case 12 -> // Hanged Man
                reversed ? "Your future requires releasing resistance to change."
                         : "Patient sacrifice now will bring future enlightenment.";
            case 13 -> // Death
                reversed ? "Your future transformation begins when you embrace change."
                         : "Your future emerges renewed from current endings.";
            case 14 -> // Temperance
                reversed ? "Future harmony comes through balancing extremes."
                         : "Your future flows with perfect balance and integration.";
            case 15 -> // Devil
                reversed ? "Your future freedom comes from breaking current chains."
                         : "Future growth requires facing and overcoming temptations.";
            case 16 -> // Tower
                reversed ? "Your future stability emerges from current rebuilding."
                         : "Sudden revelations will clear your future path dramatically.";
            case 17 -> // Star
                reversed ? "Your future requires rebuilding faith in yourself and your dreams."
                         : "Hope and healing illuminate your path forward, bringing renewed purpose.";
            case 18 -> // Moon
                reversed ? "Your future clarity emerges as current illusions dissolve."
                         : "Trust your intuition as you navigate future mysteries.";
            case 19 -> // Sun
                reversed ? "Future success may be delayed, but joy will eventually break through."
                         : "Your future radiates with success, vitality, and pure joy.";
            case 20 -> // Judgement
                reversed ? "Your future awakening requires releasing self-criticism."
                         : "A powerful calling will transform your future path.";
            case 21 -> // World
                reversed ? "You're approaching completion, but final steps require patience and persistence."
                         : "Your future culminates in the successful achievement of your highest goals.";
            default ->
                reversed ? "The future will require careful navigation, but growth comes through challenge."
                        : "Your future path opens toward positive developments and continued growth.";
        };
        
        return futureOutlook;
    }
    
    /**
     * Generate thematic conclusion based on the overall reading
     */
    private String generateThematicConclusion(ReadingAnalysis analysis) {
        // Determine dominant theme
        Map<CardTheme, Integer> themeCount = new HashMap<>();
        for (CardTheme theme : analysis.themes) {
            themeCount.put(theme, themeCount.getOrDefault(theme, 0) + 1);
        }
        
        CardTheme dominantTheme = themeCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(CardTheme.FULFILLMENT);
        
        String conclusion = switch (dominantTheme) {
            case BEGINNINGS -> 
                "This reading speaks to new chapters and fresh starts. Trust in your ability to begin again.";
            case AUTHORITY -> 
                "Themes of leadership and personal power run through your reading. Step into your authority.";
            case INTROSPECTION -> 
                "Your reading calls for deep reflection and inner wisdom. The answers lie within.";
            case TRANSFORMATION -> 
                "Profound change is the central theme of your reading. Embrace the metamorphosis.";
            case CHALLENGE -> 
                "Your reading acknowledges significant challenges, but also your strength to overcome them.";
            case SPIRITUAL -> 
                "Spiritual growth and higher understanding illuminate your path forward.";
            default -> 
                "Your reading reveals a harmonious blend of experiences leading toward fulfillment.";
        };
        
        // Add intensity modifier
        String intensityNote = switch (analysis.intensity) {
            case GENTLE -> " This guidance comes with gentle encouragement.";
            case MODERATE -> " These themes will unfold at a steady, manageable pace.";
            case INTENSE -> " The energy around these themes is particularly strong right now.";
            case TRANSFORMATIVE -> " Expect these influences to bring profound, lasting change.";
        };
        
        return conclusion + intensityNote;
    }
    
    /**
     * Determine narrative intensity based on card combination
     */
    private NarrativeIntensity determineIntensity(Card[] cards, int reversedCount, List<CardTheme> themes) {
        // High intensity cards
        int[] highIntensityCards = {13, 15, 16, 20}; // Death, Devil, Tower, Judgement
        int intensityScore = 0;
        
        for (Card card : cards) {
            for (int highCard : highIntensityCards) {
                if (card.getArcanaNumber() == highCard) {
                    intensityScore += card.isReversed() ? 1 : 2;
                }
            }
        }
        
        // Factor in reversed cards
        intensityScore += reversedCount;
        
        // Determine intensity level
        if (intensityScore >= 5) return NarrativeIntensity.TRANSFORMATIVE;
        if (intensityScore >= 3) return NarrativeIntensity.INTENSE;
        if (intensityScore >= 1) return NarrativeIntensity.MODERATE;
        return NarrativeIntensity.GENTLE;
    }
    
    /**
     * Check if two cards share a thematic connection
     */
    private boolean shareTheme(Card card1, Card card2) {
        for (CardTheme theme : CardTheme.values()) {
            if (theme.containsCard(card1.getArcanaNumber()) && 
                theme.containsCard(card2.getArcanaNumber())) {
                return true;
            }
        }
        return false;
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