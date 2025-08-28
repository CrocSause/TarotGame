package com.tarotgame;

import java.util.Scanner;

import com.tarotgame.engine.GameEngine;
import com.tarotgame.ui.ConsoleUI;
import com.tarotgame.ui.javafx.TarotGameApplication;

import javafx.application.Application;

/**
 * Main launcher for the Tarot Game application.
 * Allows users to choose between Console UI and JavaFX GUI interfaces.
 */
public class Launcher {
    
    private static final String TITLE = """
        ╔══════════════════════════════════════════════╗
        ║              🎴 TAROT GAME 🎴               ║
        ║                 LAUNCHER                     ║
        ╚══════════════════════════════════════════════╝
        """;
    
    public static void main(String[] args) {
        System.out.println(TITLE);
        System.out.println("Choose your interface:");
        System.out.println();
        System.out.println("1. Console Interface (Full Featured)");
        System.out.println("2. JavaFX GUI Interface (Basic - In Development)");
        System.out.println("3. Auto-detect best interface");
        System.out.println();
        
        try (Scanner scanner = new Scanner(System.in)) {
            int choice = getInterfaceChoice(scanner);
            
            switch (choice) {
                case 1 -> launchConsoleInterface();
                case 2 -> launchJavaFXInterface();
                case 3 -> launchAutoDetectedInterface();
                default -> {
                    System.out.println("Invalid choice. Defaulting to console interface.");
                    launchConsoleInterface();
                }
            }
        } catch (Exception e) {
            System.err.println("Error during application startup: " + e.getMessage());
            System.err.println("Falling back to console interface...");
            launchConsoleInterface();
        }
    }
    
    /**
     * Get user's interface choice with input validation
     */
    private static int getInterfaceChoice(Scanner scanner) {
        while (true) {
            System.out.print("Enter your choice (1-3): ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                
                if (choice >= 1 && choice <= 3) {
                    return choice;
                } else {
                    System.out.println("Please enter a number between 1 and 3.");
                }
            } catch (Exception e) {
                scanner.nextLine(); // clear invalid input
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Launch the console interface
     */
    private static void launchConsoleInterface() {
        System.out.println();
        System.out.println("🖥️  Starting Console Interface...");
        System.out.println("════════════════════════════════");
        
        try {
            GameEngine engine = new GameEngine();
            
            if (engine.isReady()) {
                System.out.println("✓ Game engine initialized successfully");
                ConsoleUI ui = new ConsoleUI(engine);
                ui.start();
            } else {
                System.err.println("❌ Failed to initialize game engine");
                if (engine.hasError()) {
                    System.err.println("   Error: " + engine.getLastError());
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Console interface failed to start: " + e.getMessage());
        }
    }
    
    /**
     * Launch the JavaFX interface
     */
    private static void launchJavaFXInterface() {
        System.out.println();
        System.out.println("🖼️  Starting JavaFX GUI Interface...");
        System.out.println("════════════════════════════════════");
        System.out.println("Note: GUI interface is in development. Some features redirect to console version.");
        System.out.println();
        
        try {
            // Check if JavaFX is available
            if (!isJavaFXAvailable()) {
                System.err.println("❌ JavaFX is not available on this system.");
                System.err.println("   Falling back to console interface...");
                System.out.println();
                launchConsoleInterface();
                return;
            }
            
            // Launch JavaFX application
            Application.launch(TarotGameApplication.class);
            
        } catch (Exception e) {
            System.err.println("❌ JavaFX interface failed to start: " + e.getMessage());
            System.err.println("   This might be due to missing JavaFX runtime.");
            System.err.println("   Falling back to console interface...");
            System.out.println();
            launchConsoleInterface();
        }
    }
    
    /**
     * Auto-detect the best interface based on system capabilities
     */
    private static void launchAutoDetectedInterface() {
        System.out.println();
        System.out.println("🔍 Auto-detecting best interface...");
        
        if (isJavaFXAvailable() && !isHeadlessEnvironment()) {
            System.out.println("✓ JavaFX available and GUI environment detected");
            System.out.println("   Launching JavaFX interface...");
            launchJavaFXInterface();
        } else {
            System.out.println("ℹ️  GUI environment not available or JavaFX missing");
            System.out.println("   Launching console interface...");
            launchConsoleInterface();
        }
    }
    
    /**
     * Check if JavaFX is available
     */
    private static boolean isJavaFXAvailable() {
        try {
            Class.forName("javafx.application.Application");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    /**
     * Check if running in headless environment (no GUI support)
     */
    private static boolean isHeadlessEnvironment() {
        // Check if running in headless mode
        String headless = System.getProperty("java.awt.headless");
        if ("true".equals(headless)) {
            return true;
        }
        
        // Check if display is available (Unix/Linux)
        String display = System.getenv("DISPLAY");
        if (System.getProperty("os.name").toLowerCase().contains("linux") && display == null) {
            return true;
        }
        
        // For additional safety, try to get graphics environment
        try {
            java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
            return java.awt.GraphicsEnvironment.isHeadless();
        } catch (Exception e) {
            return true;
        }
    }
    
    /**
     * Display system information for debugging
     */
    @SuppressWarnings("unused")
    private static void displaySystemInfo() {
        System.out.println("System Information:");
        System.out.println("  OS: " + System.getProperty("os.name"));
        System.out.println("  Java Version: " + System.getProperty("java.version"));
        System.out.println("  JavaFX Available: " + isJavaFXAvailable());
        System.out.println("  Headless Environment: " + isHeadlessEnvironment());
        System.out.println();
    }
}
