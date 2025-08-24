package com.tarotgame;

import com.tarotgame.engine.GameEngine;
import com.tarotgame.ui.ConsoleUI;

/**
 * Production entry point for the Tarot Game application.
 * Launches the interactive console interface.
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            System.out.println("🎴 Initializing Tarot Game...");
            
            // Initialize game engine
            GameEngine engine = new GameEngine();
            
            if (engine.isReady()) {
                System.out.println("✓ Game engine initialized successfully");
                
                // Launch Console UI
                ConsoleUI ui = new ConsoleUI(engine);
                ui.start();
                
            } else {
                System.err.println("❌ Failed to initialize game engine");
                if (engine.hasError()) {
                    System.err.println("   Error: " + engine.getLastError());
                }
                System.err.println("   Please check your installation and try again.");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Application startup failed: " + e.getMessage());
        }
    }
}