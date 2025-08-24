package com.tarotgame;

import com.tarotgame.engine.GameEngine;

/**
 * Production entry point for the Tarot Game application.
 * This will launch the main game interface once Console UI is implemented.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("🎴 WELCOME TO TAROT GAME");
        System.out.println("═".repeat(40));
        
        try {
            // Initialize game engine
            GameEngine engine = new GameEngine();
            
            if (engine.isReady()) {
                System.out.println("✓ Game engine initialized successfully");
                System.out.println("✓ " + engine.getQuickStatus());
                
                // TODO: Launch Console UI when implemented
                // ConsoleUI ui = new ConsoleUI(engine);
                // ui.start();
                
                System.out.println("\n🚧 Console UI coming soon!");
                System.out.println("   Run TestMain.java for testing and demos");
                
            } else {
                System.err.println("❌ Failed to initialize game engine");
                if (engine.hasError()) {
                    System.err.println("   Error: " + engine.getLastError());
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Application startup failed: " + e.getMessage());
        }
    }
}