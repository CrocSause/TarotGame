package com.tarotgame.manual;

import com.tarotgame.demo.ComponentDemo;
import com.tarotgame.demo.IntegratedDemo;
import com.tarotgame.integration.GameEngineTest;
import com.tarotgame.integration.ReadingSystemTest;
import com.tarotgame.unit.CardModelTest;
import com.tarotgame.unit.DeckTest;
import com.tarotgame.unit.InterpretationServiceTest;

/**
 * Centralized test suite runner for all Tarot Game components
 */
public class TestSuite {
    
    public static void main(String[] args) {
        System.out.println("🎴 TAROT GAME - COMPREHENSIVE TEST SUITE");
        System.out.println("═".repeat(60) + "\n");
        
        runUnitTests();
        runIntegrationTests();
        runDemonstrations();
        
        System.out.println("\n" + "═".repeat(60));
        System.out.println("🎉 ALL TESTS COMPLETED SUCCESSFULLY!");
        System.out.println("═".repeat(60));
    }
    
    private static void runUnitTests() {
        System.out.println("🔬 UNIT TESTS");
        System.out.println("-".repeat(40));
        
        System.out.println("Running Card Model Tests...");
        CardModelTest.main(new String[]{});
        
        System.out.println("Running Deck Tests...");
        DeckTest.main(new String[]{});
        
        System.out.println("Running Interpretation Service Tests...");
        InterpretationServiceTest.main(new String[]{});
    }
    
    private static void runIntegrationTests() {
        System.out.println("\n🔗 INTEGRATION TESTS");
        System.out.println("-".repeat(40));
        
        System.out.println("Running Reading System Tests...");
        ReadingSystemTest.main(new String[]{});
        
        System.out.println("Running Game Engine Tests...");
        GameEngineTest.main(new String[]{});
    }
    
    private static void runDemonstrations() {
        System.out.println("\n🎭 DEMONSTRATIONS");
        System.out.println("-".repeat(40));
        
        System.out.println("Running Component Demos...");
        ComponentDemo.main(new String[]{});
        
        System.out.println("Running Integrated Demo...");
        IntegratedDemo.main(new String[]{});
    }
}