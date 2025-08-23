// GameEngine.java
package com.tarotgame.engine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tarotgame.model.Deck;
import com.tarotgame.service.InterpretationService;
import com.tarotgame.service.ReadingSystem;
import com.tarotgame.service.ReadingSystem.Reading;

/**
 * Core game engine that orchestrates the complete tarot reading workflow.
 * Manages session state, coordinates services, and handles the reading lifecycle.
 * 
 * Key responsibilities:
 * - Service initialization and validation
 * - Session state management (deck, readings history)
 * - Reading workflow coordination
 * - Error handling and recovery
 * - User experience flow management
 */
public class GameEngine {
    
    // Core services and components
    private final InterpretationService interpretationService;
    private final ReadingSystem readingSystem;
    private Deck currentDeck;
    
    // Session state
    private final List<Reading> sessionReadings;
    private final LocalDateTime sessionStartTime;
    private GameState currentState;
    private String lastError;
    
    // Session statistics
    private int totalReadingsPerformed;
    private int deckResetsRequested;
    
    /**
     * Represents the current state of the game engine
     */
    public enum GameState {
        INITIALIZING("Initializing services..."),
        READY("Ready for readings"),
        PERFORMING_READING("Performing reading..."),
        ERROR("Error occurred"),
        SHUTDOWN("Engine shutdown");
        
        private final String description;
        
        GameState(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * Result class for engine operations
     */
    public static class EngineResult {
        private final boolean success;
        private final String message;
        private final Reading reading;
        private final Exception error;
        
        private EngineResult(boolean success, String message, Reading reading, Exception error) {
            this.success = success;
            this.message = message;
            this.reading = reading;
            this.error = error;
        }
        
        public static EngineResult success(String message, Reading reading) {
            return new EngineResult(true, message, reading, null);
        }
        
        public static EngineResult success(String message) {
            return new EngineResult(true, message, null, null);
        }
        
        public static EngineResult failure(String message, Exception error) {
            return new EngineResult(false, message, null, error);
        }
        
        public static EngineResult failure(String message) {
            return new EngineResult(false, message, null, null);
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Reading getReading() { return reading; }
        public Exception getError() { return error; }
        
        public boolean hasReading() { return reading != null; }
        public boolean hasError() { return error != null; }
    }
    
    /**
     * Constructor - initializes the game engine with automatic service setup
     */
    public GameEngine() {
        this.sessionStartTime = LocalDateTime.now();
        this.sessionReadings = new ArrayList<>();
        this.totalReadingsPerformed = 0;
        this.deckResetsRequested = 0;
        this.currentState = GameState.INITIALIZING;
        this.lastError = null;
        
        // Initialize services
        this.interpretationService = new InterpretationService();
        this.readingSystem = new ReadingSystem(interpretationService);
        
        // Initialize game state
        initializeEngine();
    }
    
    /**
     * Constructor with custom random seed (for testing)
     */
    public GameEngine(long randomSeed) {
        this.sessionStartTime = LocalDateTime.now();
        this.sessionReadings = new ArrayList<>();
        this.totalReadingsPerformed = 0;
        this.deckResetsRequested = 0;
        this.currentState = GameState.INITIALIZING;
        this.lastError = null;
        
        // Initialize services with seed
        this.interpretationService = new InterpretationService();
        this.readingSystem = new ReadingSystem(interpretationService, randomSeed);
        
        // Initialize game state
        initializeEngine();
    }
    
    /**
     * Initialize the game engine and validate services
     */
    private void initializeEngine() {
        try {
            // Validate interpretation service
            if (!interpretationService.isLoaded()) {
                throw new RuntimeException("InterpretationService failed to load properly");
            }
            
            // Validate reading system
            if (!readingSystem.isServiceReady()) {
                throw new RuntimeException("ReadingSystem is not ready");
            }
            
            // Create initial deck
            this.currentDeck = new Deck();
            
            // Engine is ready
            this.currentState = GameState.READY;
            
        } catch (RuntimeException e) {
            this.currentState = GameState.ERROR;
            this.lastError = "Engine initialization failed: " + e.getMessage();
            throw new RuntimeException("Failed to initialize GameEngine", e);
        }
    }
    
    /**
     * Performs a tarot reading using the current deck
     */
    public EngineResult performReading() {
        if (currentState != GameState.READY) {
            return EngineResult.failure("Engine is not ready for reading. Current state: " + currentState);
        }
        
        try {
            currentState = GameState.PERFORMING_READING;
            
            // Perform the reading
            Reading reading = readingSystem.performReading(currentDeck);
            
            // Store reading in session history
            sessionReadings.add(reading);
            totalReadingsPerformed++;
            
            // Update state
            currentState = GameState.READY;
            lastError = null;
            
            return EngineResult.success("Reading completed successfully", reading);
            
        } catch (Exception e) {
            currentState = GameState.ERROR;
            lastError = "Reading failed: " + e.getMessage();
            return EngineResult.failure("Failed to perform reading: " + e.getMessage(), e);
        }
    }
    
    /**
     * Resets the current deck (fresh shuffle of all 22 cards)
     */
    public EngineResult resetDeck() {
        try {
            currentDeck.reset();
            deckResetsRequested++;
            
            // Clear any error state
            if (currentState == GameState.ERROR) {
                currentState = GameState.READY;
                lastError = null;
            }
            
            return EngineResult.success("Deck reset successfully. All 22 cards are now available.");
            
        } catch (Exception e) {
            currentState = GameState.ERROR;
            lastError = "Deck reset failed: " + e.getMessage();
            return EngineResult.failure("Failed to reset deck: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates a completely new deck (replaces current deck instance)
     */
    public EngineResult createNewDeck() {
        try {
            this.currentDeck = new Deck();
            deckResetsRequested++; // Count this as a reset for statistics
            
            // Clear any error state
            if (currentState == GameState.ERROR) {
                currentState = GameState.READY;
                lastError = null;
            }
            
            return EngineResult.success("New deck created successfully.");
            
        } catch (Exception e) {
            currentState = GameState.ERROR;
            lastError = "New deck creation failed: " + e.getMessage();
            return EngineResult.failure("Failed to create new deck: " + e.getMessage(), e);
        }
    }
    
    /**
     * Gets the most recent reading from this session
     */
    public Reading getLastReading() {
        if (sessionReadings.isEmpty()) {
            return null;
        }
        return sessionReadings.get(sessionReadings.size() - 1);
    }
    
    /**
     * Gets all readings performed in this session
     */
    public List<Reading> getSessionReadings() {
        return Collections.unmodifiableList(sessionReadings);
    }
    
    /**
     * Gets a specific reading by index (0-based, most recent = highest index)
     */
    public Reading getReading(int index) {
        if (index < 0 || index >= sessionReadings.size()) {
            return null;
        }
        return sessionReadings.get(index);
    }
    
    /**
     * Clears the session reading history (keeps current deck state)
     */
    public EngineResult clearReadingHistory() {
        int clearedCount = sessionReadings.size();
        sessionReadings.clear();
        return EngineResult.success("Cleared " + clearedCount + " readings from session history.");
    }
    
    /**
     * Recovers from error state by attempting to reinitialize
     */
    public EngineResult recoverFromError() {
        if (currentState != GameState.ERROR) {
            return EngineResult.success("Engine is not in error state. No recovery needed.");
        }
        
        try {
            // Try to create a new deck
            this.currentDeck = new Deck();
            
            // Verify services are still working
            if (!readingSystem.isServiceReady()) {
                throw new RuntimeException("ReadingSystem is not ready after recovery attempt");
            }
            
            currentState = GameState.READY;
            lastError = null;
            
            return EngineResult.success("Successfully recovered from error state.");
            
        } catch (RuntimeException e) {
            return EngineResult.failure("Recovery failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Gets comprehensive status information about the engine
     */
    public String getEngineStatus() {
        StringBuilder status = new StringBuilder();
        
        // Engine state
        status.append("═══ TAROT GAME ENGINE STATUS ═══\n");
        status.append("State: ").append(currentState).append(" - ").append(currentState.getDescription()).append("\n");
        status.append("Session Start: ").append(formatSessionTime()).append("\n");
        status.append("Uptime: ").append(getUptime()).append("\n\n");
        
        // Session statistics
        status.append("═══ SESSION STATISTICS ═══\n");
        status.append("Total Readings: ").append(totalReadingsPerformed).append("\n");
        status.append("Readings in History: ").append(sessionReadings.size()).append("\n");
        status.append("Deck Resets: ").append(deckResetsRequested).append("\n\n");
        
        // Current deck status
        status.append("═══ CURRENT DECK STATUS ═══\n");
        if (currentDeck != null) {
            status.append(currentDeck.getStatus()).append("\n");
        } else {
            status.append("No deck available\n");
        }
        status.append("\n");
        
        // Service status
        status.append("═══ SERVICE STATUS ═══\n");
        status.append(readingSystem.getServiceStatus()).append("\n");
        
        // Error information
        if (lastError != null) {
            status.append("\n═══ LAST ERROR ═══\n");
            status.append(lastError).append("\n");
        }
        
        return status.toString();
    }
    
    /**
     * Gets a quick status summary
     */
    public String getQuickStatus() {
        if (currentDeck == null) {
            return String.format("Engine: %s | No deck available", currentState);
        }
        return String.format("Engine: %s | %s | Readings: %d", 
                           currentState, 
                           currentDeck.getStatus().replace("Deck Status: ", ""), 
                           totalReadingsPerformed);
    }
    
    /**
     * Checks if the engine is ready to perform readings
     */
    public boolean isReady() {
        return currentState == GameState.READY;
    }
    
    /**
     * Checks if the engine is in an error state
     */
    public boolean hasError() {
        return currentState == GameState.ERROR;
    }
    
    /**
     * Gets the last error message
     */
    public String getLastError() {
        return lastError;
    }
    
    /**
     * Gets current game state
     */
    public GameState getCurrentState() {
        return currentState;
    }
    
    /**
     * Gets session statistics
     */
    public SessionStats getSessionStats() {
        return new SessionStats(
            totalReadingsPerformed,
            sessionReadings.size(),
            deckResetsRequested,
            sessionStartTime,
            getUptime()
        );
    }
    
    /**
     * Gracefully shutdown the engine
     */
    public void shutdown() {
        currentState = GameState.SHUTDOWN;
        // Any cleanup operations would go here
    }
    
    // Helper methods
    
    private String formatSessionTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a");
        return sessionStartTime.format(formatter);
    }
    
    private String getUptime() {
        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(sessionStartTime, now).toMinutes();
        if (minutes < 60) {
            return minutes + " minutes";
        } else {
            long hours = minutes / 60;
            long remainingMinutes = minutes % 60;
            return hours + "h " + remainingMinutes + "m";
        }
    }
    
    /**
     * Session statistics data class
     */
    public static class SessionStats {
        private final int totalReadings;
        private final int readingsInHistory;
        private final int deckResets;
        private final LocalDateTime sessionStart;
        private final String uptime;
        
        public SessionStats(int totalReadings, int readingsInHistory, int deckResets, 
                           LocalDateTime sessionStart, String uptime) {
            this.totalReadings = totalReadings;
            this.readingsInHistory = readingsInHistory;
            this.deckResets = deckResets;
            this.sessionStart = sessionStart;
            this.uptime = uptime;
        }
        
        // Getters
        public int getTotalReadings() { return totalReadings; }
        public int getReadingsInHistory() { return readingsInHistory; }
        public int getDeckResets() { return deckResets; }
        public LocalDateTime getSessionStart() { return sessionStart; }
        public String getUptime() { return uptime; }
        
        @Override
        public String toString() {
            return String.format("SessionStats[readings=%d, history=%d, resets=%d, uptime=%s]",
                               totalReadings, readingsInHistory, deckResets, uptime);
        }
    }
}