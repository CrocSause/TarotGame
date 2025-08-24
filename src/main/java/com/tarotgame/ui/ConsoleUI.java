package com.tarotgame.ui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.tarotgame.engine.GameEngine;
import com.tarotgame.service.ReadingSystem.Reading;

/**
 * Interactive Console User Interface for the Tarot Game.
 * Provides a complete menu-driven experience for tarot readings.
 */
public class ConsoleUI {
    
    private final GameEngine gameEngine;
    private final Scanner scanner;
    private boolean running;
    
    // UI Constants
    private static final String TITLE_BORDER = "╔══════════════════════════════════════════════╗";
    private static final String TITLE_LINE =   "║              🎴 TAROT GAME 🎴               ║";
    private static final String BORDER =       "╚══════════════════════════════════════════════╝";
    private static final String SEPARATOR =    "═".repeat(50);
    private static final String DIVIDER =      "─".repeat(50);
    
    public ConsoleUI(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.scanner = new Scanner(System.in);
        this.running = false;
    }
    
    /**
     * Main entry point for the Console UI
     */
    public void start() {
        try (scanner) {
            if (!gameEngine.isReady()) {
                displayError("Game Engine is not ready. Cannot start UI.");
                return;
            }
            
            running = true;
            displayWelcome();
            
            while (running) {
                try {
                    displayMainMenu();
                    int choice = getMenuChoice(1, 7);
                    handleMainMenuChoice(choice);
                } catch (Exception e) {
                    displayError("An unexpected error occurred: " + e.getMessage());
                    pauseForUser();
                }
            }
            
            displayGoodbye();
        }
    }
    
    /**
     * Display welcome screen
     */
    private void displayWelcome() {
        clearScreen();
        System.out.println(TITLE_BORDER);
        System.out.println(TITLE_LINE);
        System.out.println(BORDER);
        System.out.println();
        System.out.println("Welcome to the mystical world of Tarot!");
        System.out.println("Discover insights into your past, present, and future.");
        System.out.println();
        System.out.println("🔮 " + gameEngine.getQuickStatus());
        System.out.println();
        pauseForUser();
    }
    
    /**
     * Display main menu options
     */
    private void displayMainMenu() {
        clearScreen();
        System.out.println("🎴 TAROT GAME - MAIN MENU");
        System.out.println(SEPARATOR);
        System.out.println();
        System.out.println("1. 🔮 Get a Three-Card Reading");
        System.out.println("2. 📚 View Reading History");
        System.out.println("3. ⚙️  Game Status & Statistics");
        System.out.println("4. 🔄 Deck Management");
        System.out.println("5. 📖 About Tarot Readings");
        System.out.println("6. 🧹 Clear Reading History");
        System.out.println("7. 🚪 Exit");
        System.out.println();
        System.out.println(DIVIDER);
    }
    
    /**
     * Handle main menu choice
     */
    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1 -> performReading();
            case 2 -> viewReadingHistory();
            case 3 -> viewGameStatus();
            case 4 -> manageDeck();
            case 5 -> showAboutTarot();
            case 6 -> clearHistory();
            case 7 -> confirmExit();
            default -> {
                displayError("Invalid choice. Please try again.");
                pauseForUser();
            }
        }
    }
    
    /**
     * Perform a tarot reading
     */
    private void performReading() {
        clearScreen();
        System.out.println("🔮 TAROT READING");
        System.out.println(SEPARATOR);
        System.out.println();
        System.out.println("Preparing your three-card reading...");
        System.out.println("Focus on your question as the cards are drawn.");
        System.out.println();
        
        // Optional: Ask for a question
        System.out.print("💭 Would you like to focus on a specific question? (optional): ");
        String question = scanner.nextLine().trim();
        
        System.out.println();
        System.out.println("🎴 Drawing your cards...");
        
        // Add a bit of drama with loading dots
        displayLoadingAnimation();
        
        // Perform the reading
        GameEngine.EngineResult result = gameEngine.performReading();
        
        if (result.isSuccess() && result.hasReading()) {
            Reading reading = result.getReading();
            displayReading(reading, question);
        } else {
            displayError("Failed to perform reading: " + result.getMessage());
        }
        
        pauseForUser();
    }
    
    /**
     * Display a reading with beautiful formatting
     */
    private void displayReading(Reading reading, String question) {
        System.out.println();
        System.out.println("✨ YOUR TAROT READING ✨");
        System.out.println(SEPARATOR);
        
        if (!question.isEmpty()) {
            System.out.println("Question: " + question);
            System.out.println();
        }
        
        System.out.println("Reading ID: " + reading.getReadingId());
        System.out.println("Time: " + reading.getFormattedTimestamp());
        System.out.println();
        
        // Display the formatted reading
        System.out.println(reading.getFormattedReading());
        
        System.out.println();
        System.out.println("🌟 Remember: The cards provide guidance, but you create your own destiny.");
        System.out.println(SEPARATOR);
    }
    
    /**
     * View reading history
     */
    private void viewReadingHistory() {
        clearScreen();
        System.out.println("📚 READING HISTORY");
        System.out.println(SEPARATOR);
        System.out.println();
        
        List<Reading> history = gameEngine.getSessionReadings();
        
        if (history.isEmpty()) {
            System.out.println("📝 No readings in your history yet.");
            System.out.println("   Get your first reading from the main menu!");
        } else {
            System.out.println("You have " + history.size() + " reading(s) in your history:");
            System.out.println();
            
            for (int i = 0; i < history.size(); i++) {
                Reading reading = history.get(i);
                System.out.printf("%2d. %s - %s%n", 
                    i + 1, 
                    reading.getReadingId(), 
                    reading.getShortTimestamp());
                System.out.println("    " + reading.getSummary());
                System.out.println();
            }
            
            // Offer to view detailed reading
            System.out.println(DIVIDER);
            System.out.print("Enter reading number to view details (or 0 to return): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                if (choice > 0 && choice <= history.size()) {
                    displayDetailedReading(history.get(choice - 1));
                }
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Clear invalid input
                System.out.println("Invalid input. Returning to menu.");
            }
        }
        
        pauseForUser();
    }
    
    /**
     * Display a detailed view of a specific reading
     */
    private void displayDetailedReading(Reading reading) {
        clearScreen();
        System.out.println("🔍 DETAILED READING VIEW");
        System.out.println(SEPARATOR);
        System.out.println();
        
        System.out.println(reading.getFormattedReading());
        System.out.println();
        
        pauseForUser();
    }
    
    /**
     * View game status and statistics
     */
    private void viewGameStatus() {
        clearScreen();
        System.out.println("⚙️ GAME STATUS & STATISTICS");
        System.out.println(SEPARATOR);
        System.out.println();
        
        // Engine status
        System.out.println("🎮 Engine Status:");
        System.out.println("   " + gameEngine.getEngineStatus());
        System.out.println();
        
        // Session statistics
        GameEngine.SessionStats stats = gameEngine.getSessionStats();
        System.out.println("📊 Session Statistics:");
        System.out.println("   Total Readings: " + stats.getTotalReadings());
        System.out.println("   Readings in History: " + stats.getReadingsInHistory());
        System.out.println("   Deck Resets: " + stats.getDeckResets());
        System.out.println("   Session Uptime: " + stats.getUptime());
        System.out.println();
        
        pauseForUser();
    }
    
    /**
     * Deck management submenu
     */
    private void manageDeck() {
        clearScreen();
        System.out.println("🔄 DECK MANAGEMENT");
        System.out.println(SEPARATOR);
        System.out.println();
        System.out.println("Current status: " + gameEngine.getQuickStatus());
        System.out.println();
        System.out.println("1. 🔄 Reset Current Deck");
        System.out.println("2. 🆕 Create New Deck");
        System.out.println("3. ℹ️  Deck Information");
        System.out.println("4. 🔙 Return to Main Menu");
        System.out.println();
        
        int choice = getMenuChoice(1, 4);
        
        switch (choice) {
            case 1 -> resetDeck();
            case 2 -> createNewDeck();
            case 3 -> showDeckInfo();
            case 4 -> {
                return; // Return to main menu
            }
        }
    }
    
    /**
     * Reset the current deck
     */
    private void resetDeck() {
        System.out.println();
        System.out.println("🔄 Resetting deck...");
        
        GameEngine.EngineResult result = gameEngine.resetDeck();
        
        if (result.isSuccess()) {
            System.out.println("✅ " + result.getMessage());
        } else {
            displayError("Failed to reset deck: " + result.getMessage());
        }
        
        pauseForUser();
    }
    
    /**
     * Create a new deck
     */
    private void createNewDeck() {
        System.out.println();
        System.out.println("🆕 Creating new deck...");
        
        GameEngine.EngineResult result = gameEngine.createNewDeck();
        
        if (result.isSuccess()) {
            System.out.println("✅ " + result.getMessage());
        } else {
            displayError("Failed to create new deck: " + result.getMessage());
        }
        
        pauseForUser();
    }
    
    /**
     * Show detailed deck information
     */
    private void showDeckInfo() {
        clearScreen();
        System.out.println("ℹ️ DECK INFORMATION");
        System.out.println(SEPARATOR);
        System.out.println();
        System.out.println("🎴 This deck contains the 22 Major Arcana cards:");
        System.out.println("   From The Fool (0) to The World (21)");
        System.out.println();
        System.out.println("🔄 Auto-Reshuffle Feature:");
        System.out.println("   When fewer than 3 cards remain, the deck");
        System.out.println("   automatically reshuffles to ensure continuous play.");
        System.out.println();
        System.out.println("📊 Current Status:");
        System.out.println("   " + gameEngine.getQuickStatus());
        System.out.println();
        
        pauseForUser();
    }
    
    /**
     * Show information about tarot readings
     */
    private void showAboutTarot() {
        clearScreen();
        System.out.println("📖 ABOUT TAROT READINGS");
        System.out.println(SEPARATOR);
        System.out.println();
        System.out.println("🔮 Three-Card Reading:");
        System.out.println("   PAST    - Influences from your past that shape the present");
        System.out.println("   PRESENT - Your current situation and immediate circumstances");
        System.out.println("   FUTURE  - Potential outcomes and future influences");
        System.out.println();
        System.out.println("🎴 Card Orientations:");
        System.out.println("   UPRIGHT  - Positive aspects, direct meaning");
        System.out.println("   REVERSED - Challenges, blocked energy, or inner reflection");
        System.out.println();
        System.out.println("⭐ Major Arcana:");
        System.out.println("   The 22 Major Arcana cards represent major life themes,");
        System.out.println("   spiritual lessons, and significant life events.");
        System.out.println();
        System.out.println("🌟 Remember:");
        System.out.println("   Tarot provides guidance and insight, but you have the power");
        System.out.println("   to shape your own destiny through your choices and actions.");
        System.out.println();
        
        pauseForUser();
    }
    
    /**
     * Clear reading history
     */
    private void clearHistory() {
        clearScreen();
        System.out.println("🧹 CLEAR READING HISTORY");
        System.out.println(SEPARATOR);
        System.out.println();
        
        List<Reading> history = gameEngine.getSessionReadings();
        
        if (history.isEmpty()) {
            System.out.println("📝 No readings to clear.");
        } else {
            System.out.println("⚠️  You have " + history.size() + " reading(s) in your history.");
            System.out.println("   This action cannot be undone.");
            System.out.println();
            System.out.print("Are you sure you want to clear all readings? (y/N): ");
            
            String confirmation = scanner.nextLine().trim().toLowerCase();
            
            if (confirmation.equals("y") || confirmation.equals("yes")) {
                GameEngine.EngineResult result = gameEngine.clearReadingHistory();
                
                if (result.isSuccess()) {
                    System.out.println("✅ " + result.getMessage());
                } else {
                    displayError("Failed to clear history: " + result.getMessage());
                }
            } else {
                System.out.println("❌ History clearing cancelled.");
            }
        }
        
        pauseForUser();
    }
    
    /**
     * Confirm exit
     */
    private void confirmExit() {
        System.out.println();
        System.out.print("Are you sure you want to exit? (y/N): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("y") || confirmation.equals("yes")) {
            running = false;
        }
    }
    
    /**
     * Display goodbye message
     */
    private void displayGoodbye() {
        clearScreen();
        System.out.println(TITLE_BORDER);
        System.out.println("║                THANK YOU                     ║");
        System.out.println("║           for using Tarot Game               ║");
        System.out.println(BORDER);
        System.out.println();
        System.out.println("🌟 May the wisdom of the cards guide your path.");
        System.out.println("🎴 Until we meet again in the realm of mystery!");
        System.out.println();
    }
    
    // Utility Methods
    
    /**
     * Get a valid menu choice within range
     */
    private int getMenuChoice(int min, int max) {
        while (true) {
            try {
                System.out.print("Enter your choice (" + min + "-" + max + "): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("❌ Please enter a number between " + min + " and " + max + ".");
                }
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Clear invalid input
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }
    
    /**
     * Display error message with formatting
     */
    private void displayError(String message) {
        System.out.println();
        System.out.println("❌ ERROR: " + message);
        System.out.println();
    }
    
    /**
     * Pause for user input
     */
    private void pauseForUser() {
        System.out.println();
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Clear screen (works on most terminals)
     */
    private void clearScreen() {
        // Print multiple newlines for cross-platform compatibility
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    
    /**
     * Display loading animation for dramatic effect
     */
    private void displayLoadingAnimation() {
        String[] frames = {"🎴", "🔮", "✨"};
        
        for (int i = 0; i < 6; i++) {
            try {
                System.out.print(frames[i % frames.length] + " ");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println();
    }
}
