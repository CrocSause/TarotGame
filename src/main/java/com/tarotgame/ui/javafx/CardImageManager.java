package com.tarotgame.ui.javafx;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tarotgame.model.Card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Manages loading and caching of tarot card images.
 * Handles both resource-based and file system image loading with fallbacks.
 */
public class CardImageManager {
    
    private static final String RESOURCE_PATH = "/com/tarotgame/resources/images/cards/";
    private static final String FALLBACK_RESOURCE_PATH = "/images/";
    private static final double CARD_WIDTH = 46.0;
    private static final double CARD_HEIGHT = 81.0;
    
    // Image cache to avoid repeated loading
    private final Map<String, Image> imageCache = new ConcurrentHashMap<>();
    
    // Singleton instance
    private static CardImageManager instance;
    
    /**
     * Card naming convention mapping Major Arcana numbers to file names
     */
    private static final Map<Integer, String> CARD_FILENAMES = new HashMap<>();
    
    static {
        // Initialize card filename mappings
        CARD_FILENAMES.put(0, "00_the_fool.png");
        CARD_FILENAMES.put(1, "01_the_magician.png");
        CARD_FILENAMES.put(2, "02_the_high_priestess.png");
        CARD_FILENAMES.put(3, "03_the_empress.png");
        CARD_FILENAMES.put(4, "04_the_emperor.png");
        CARD_FILENAMES.put(5, "05_the_hierophant.png");
        CARD_FILENAMES.put(6, "06_the_lovers.png");
        CARD_FILENAMES.put(7, "07_the_chariot.png");
        CARD_FILENAMES.put(8, "08_strength.png");
        CARD_FILENAMES.put(9, "09_the_hermit.png");
        CARD_FILENAMES.put(10, "10_wheel_of_fortune.png");
        CARD_FILENAMES.put(11, "11_justice.png");
        CARD_FILENAMES.put(12, "12_the_hanged_man.png");
        CARD_FILENAMES.put(13, "13_death.png");
        CARD_FILENAMES.put(14, "14_temperance.png");
        CARD_FILENAMES.put(15, "15_the_devil.png");
        CARD_FILENAMES.put(16, "16_the_tower.png");
        CARD_FILENAMES.put(17, "17_the_star.png");
        CARD_FILENAMES.put(18, "18_the_moon.png");
        CARD_FILENAMES.put(19, "19_the_sun.png");
        CARD_FILENAMES.put(20, "20_judgement.png");
        CARD_FILENAMES.put(21, "21_the_world.png");
    }
    
    private CardImageManager() {
        // Private constructor for singleton
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized CardImageManager getInstance() {
        if (instance == null) {
            instance = new CardImageManager();
        }
        return instance;
    }
    
    /**
     * Create an ImageView for the specified card
     */
    public ImageView createCardImageView(Card card) {
        return createCardImageView(card, 1.0);
    }
    
    /**
     * Create an ImageView for the specified card with scaling
     */
    public ImageView createCardImageView(Card card, double scale) {
        Image cardImage = loadCardImage(card);
        ImageView imageView = new ImageView(cardImage);
        
        // Set size with scaling
        imageView.setFitWidth(CARD_WIDTH * scale);
        imageView.setFitHeight(CARD_HEIGHT * scale);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        
        // Apply rotation if card is reversed
        if (card.isReversed()) {
            imageView.setRotate(180);
        }
        
        // Add CSS class for styling
        imageView.getStyleClass().add("tarot-card-image");
        
        return imageView;
    }
    
    /**
     * Load card image from resources or create fallback
     */
    public Image loadCardImage(Card card) {
        String filename = getCardFilename(card);
        String cacheKey = filename;
        
        // Check cache first
        if (imageCache.containsKey(cacheKey)) {
            return imageCache.get(cacheKey);
        }
        
        Image image = null;
        
        // Try to load from primary resource path
        image = loadImageFromResource(RESOURCE_PATH + filename);
        
        // Try fallback resource path
        if (image == null) {
            image = loadImageFromResource(FALLBACK_RESOURCE_PATH + filename);
        }
        
        // Create programmatic fallback if no image found
        if (image == null) {
            image = createFallbackCardImage(card);
        }
        
        // Cache the image
        imageCache.put(cacheKey, image);
        
        return image;
    }
    
    /**
     * Get filename for a card based on its arcana number
     */
    private String getCardFilename(Card card) {
        String filename = CARD_FILENAMES.get(card.getArcanaNumber());
        if (filename == null) {
            // Fallback naming convention
            filename = String.format("%02d_%s.png", 
                card.getArcanaNumber(), 
                card.getName().toLowerCase().replace(" ", "_").replace("-", "_"));
        }
        return filename;
    }
    
    /**
     * Load image from resource path
     */
    private Image loadImageFromResource(String resourcePath) {
        try {
            InputStream imageStream = getClass().getResourceAsStream(resourcePath);
            if (imageStream != null) {
                return new Image(imageStream, CARD_WIDTH, CARD_HEIGHT, true, true);
            }
        } catch (Exception e) {
            System.err.println("Failed to load image from resource: " + resourcePath + " - " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Create a programmatic fallback image when card art is missing
     */
    private Image createFallbackCardImage(Card card) {
        // For now, return a simple colored rectangle image
        // In a real implementation, you might use Canvas to draw a custom design
        try {
            // Create a simple colored image as fallback
            // This is a placeholder - JavaFX doesn't have a direct way to create
            // solid color images programmatically without Canvas
            
            // For now, we'll create a very small transparent image as fallback
            // In practice, you'd want to use a default card back image
            byte[] transparentPixel = new byte[4]; // RGBA
            transparentPixel[3] = (byte) 0; // Fully transparent
            
            // This is a minimal fallback - consider providing a default card back image
            return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==");
            
        } catch (Exception e) {
            System.err.println("Failed to create fallback image for card: " + card.getName());
            return null;
        }
    }
    
    /**
     * Preload all card images for better performance
     */
    public void preloadAllImages() {
        System.out.println("Preloading tarot card images...");
        
        for (int arcanaNumber = 0; arcanaNumber <= 21; arcanaNumber++) {
            String filename = CARD_FILENAMES.get(arcanaNumber);
            if (filename != null) {
                String cacheKey = filename;
                if (!imageCache.containsKey(cacheKey)) {
                    // Create a temporary card to get the image
                    // Note: This requires knowing card names, which might need Card creation
                    Image image = loadImageFromResource(RESOURCE_PATH + filename);
                    if (image == null) {
                        image = loadImageFromResource(FALLBACK_RESOURCE_PATH + filename);
                    }
                    
                    if (image != null) {
                        imageCache.put(cacheKey, image);
                    }
                }
            }
        }
        
        System.out.println("Preloaded " + imageCache.size() + " card images.");
    }
    
    /**
     * Clear image cache to free memory
     */
    public void clearCache() {
        imageCache.clear();
    }
    
    /**
     * Get cache statistics
     */
    public String getCacheStats() {
        return String.format("Image cache: %d images loaded", imageCache.size());
    }
    
    /**
     * Check if image exists for a card
     */
    public boolean hasImageForCard(Card card) {
        String filename = getCardFilename(card);
        
        // Check if already cached
        if (imageCache.containsKey(filename)) {
            return true;
        }
        
        // Check if resource exists
        InputStream stream = getClass().getResourceAsStream(RESOURCE_PATH + filename);
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // Ignore close error
            }
            return true;
        }
        
        // Check fallback location
        stream = getClass().getResourceAsStream(FALLBACK_RESOURCE_PATH + filename);
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // Ignore close error
            }
            return true;
        }
        
        return false;
    }
    
    /**
     * Get the standard card dimensions
     */
    public static double getCardWidth() {
        return CARD_WIDTH;
    }
    
    public static double getCardHeight() {
        return CARD_HEIGHT;
    }
    
    /**
     * Create a card back image view (for face-down cards)
     */
    public ImageView createCardBackImageView(double scale) {
        // Try to load a card back image
        Image cardBack = loadImageFromResource(RESOURCE_PATH + "card_back.png");
        if (cardBack == null) {
            cardBack = loadImageFromResource(FALLBACK_RESOURCE_PATH + "card_back.png");
        }
        
        if (cardBack != null) {
            ImageView imageView = new ImageView(cardBack);
            imageView.setFitWidth(CARD_WIDTH * scale);
            imageView.setFitHeight(CARD_HEIGHT * scale);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.getStyleClass().add("tarot-card-back");
            return imageView;
        }
        
        // Fallback: create simple styled rectangle
        ImageView fallback = new ImageView();
        fallback.setFitWidth(CARD_WIDTH * scale);
        fallback.setFitHeight(CARD_HEIGHT * scale);
        fallback.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #4a90e2, #357abd); " +
            "-fx-background-radius: 5px; " +
            "-fx-border-color: #2c3e50; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 5px;"
        );
        return fallback;
    }
}
