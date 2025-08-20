// MajorArcana.java
package com.tarotgame.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enum representing all 22 Major Arcana cards with their meanings.
 * Provides factory methods for creating Card instances.
 */
public enum MajorArcana {
    THE_FOOL(0, "The Fool", 
        "New beginnings, innocence, spontaneity, free spirit, originality", 
        "Recklessness, foolishness, risk-taking, inconsideration"),
    
    THE_MAGICIAN(1, "The Magician", 
        "Manifestation, resourcefulness, power, inspired action, creativity", 
        "Manipulation, poor planning, untapped talents, deception"),
    
    THE_HIGH_PRIESTESS(2, "The High Priestess", 
        "Intuition, sacred knowledge, divine feminine, the subconscious mind", 
        "Secrets, disconnected from intuition, withdrawal, silence"),
    
    THE_EMPRESS(3, "The Empress", 
        "Femininity, beauty, nature, nurturing, abundance, creativity", 
        "Creative block, dependence on others, smothering, lack of growth"),
    
    THE_EMPEROR(4, "The Emperor", 
        "Authority, establishment, structure, father figure, leadership", 
        "Tyranny, rigidity, coldness, domination, excessive control"),
    
    THE_HIEROPHANT(5, "The Hierophant", 
        "Spiritual wisdom, religious beliefs, conformity, tradition, institutions", 
        "Personal beliefs, freedom, challenging the status quo, rebellion"),
    
    THE_LOVERS(6, "The Lovers", 
        "Love, harmony, relationships, values alignment, choices", 
        "Disharmony, imbalance, misalignment of values, trust issues"),
    
    THE_CHARIOT(7, "The Chariot", 
        "Control, willpower, success, determination, direction", 
        "Lack of control, lack of direction, aggression, scattered energy"),
    
    STRENGTH(8, "Strength", 
        "Strength, courage, persuasion, influence, compassion", 
        "Self-doubt, lack of confidence, abuse of power, weakness"),
    
    THE_HERMIT(9, "The Hermit", 
        "Soul searching, introspection, inner guidance, solitude", 
        "Isolation, loneliness, withdrawal, lost your way, paranoia"),
    
    WHEEL_OF_FORTUNE(10, "Wheel of Fortune", 
        "Good luck, karma, life cycles, destiny, turning point", 
        "Bad luck, lack of control, clinging to control, unwelcome changes"),
    
    JUSTICE(11, "Justice", 
        "Justice, fairness, truth, cause and effect, law", 
        "Unfairness, lack of accountability, dishonesty, bias"),
    
    THE_HANGED_MAN(12, "The Hanged Man", 
        "Suspension, restriction, letting go, sacrifice, martyrdom", 
        "Delays, resistance, stalling, indecision, apathy"),
    
    DEATH(13, "Death", 
        "Endings, transformation, transition, letting go, rebirth", 
        "Resistance to change, personal transformation, inner purging"),
    
    TEMPERANCE(14, "Temperance", 
        "Balance, moderation, patience, purpose, meaning", 
        "Imbalance, excess, self-healing, re-alignment, rushed"),
    
    THE_DEVIL(15, "The Devil", 
        "Shadow self, attachment, addiction, restriction, sexuality", 
        "Releasing limiting beliefs, exploring dark thoughts, detachment"),
    
    THE_TOWER(16, "The Tower", 
        "Sudden change, upheaval, chaos, revelation, awakening", 
        "Personal transformation, fear of change, averting disaster"),
    
    THE_STAR(17, "The Star", 
        "Hope, faith, purpose, renewal, spirituality, healing", 
        "Lack of faith, despair, self-trust, disconnection from spirit"),
    
    THE_MOON(18, "The Moon", 
        "Illusion, fear, anxiety, subconscious, intuition", 
        "Release of fear, repressed emotion, inner confusion, unveiling"),
    
    THE_SUN(19, "The Sun", 
        "Positivity, fun, warmth, success, vitality, enlightenment", 
        "Inner child, feeling down, overly optimistic, delayed success"),
    
    JUDGEMENT(20, "Judgement", 
        "Judgement, rebirth, inner calling, absolution, awakening", 
        "Self-doubt, inner critic, ignoring the call, harsh judgement"),
    
    THE_WORLD(21, "The World", 
        "Completion, integration, accomplishment, travel, fulfillment", 
        "Seeking external validation, incomplete, lack of achievement");

    private final int number;
    private final String name;
    private final String uprightMeaning;
    private final String reversedMeaning;

    MajorArcana(int number, String name, String uprightMeaning, String reversedMeaning) {
        this.number = number;
        this.name = name;
        this.uprightMeaning = uprightMeaning;
        this.reversedMeaning = reversedMeaning;
    }

    /**
     * Creates a new Card instance for this Major Arcana.
     * 
     * @return A new Card with this arcana's properties
     */
    public Card createCard() {
        return new Card(number, name, uprightMeaning, reversedMeaning);
    }

    /**
     * Creates a complete deck of all Major Arcana cards.
     * 
     * @return List containing all 22 Major Arcana cards in order
     */
    public static List<Card> createFullDeck() {
        List<Card> deck = new ArrayList<>();
        for (MajorArcana arcana : values()) {
            deck.add(arcana.createCard());
        }
        return deck;
    }

    /**
     * Gets a specific Major Arcana by its number.
     * 
     * @param number The arcana number (0-21)
     * @return The corresponding MajorArcana enum value
     * @throws IllegalArgumentException if number is out of range
     */
    public static MajorArcana getByNumber(int number) {
        return Arrays.stream(values())
            .filter(arcana -> arcana.number == number)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid arcana number: " + number));
    }

    /**
     * Creates a card by arcana number.
     * 
     * @param number The arcana number (0-21)
     * @return A new Card instance
     * @throws IllegalArgumentException if number is out of range
     */
    public static Card createCardByNumber(int number) {
        return getByNumber(number).createCard();
    }

    // Getters for enum properties
    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getUprightMeaning() {
        return uprightMeaning;
    }

    public String getReversedMeaning() {
        return reversedMeaning;
    }
}