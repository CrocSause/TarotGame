package com.tarotgame.ui.javafx;

import com.tarotgame.engine.GameEngine;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX Application entry point for the Tarot Game.
 * Provides a GUI alternative to the console interface.
 */
public class TarotGameApplication extends Application {
    
    private GameEngine gameEngine;
    private Stage primaryStage;
    private Scene mainScene;
    
    // UI Components
    private BorderPane rootPane;
    private Label statusLabel;
    
    @Override
    public void init() throws Exception {
        // Initialize game engine during JavaFX init phase
        try {
            gameEngine = new GameEngine();
            if (!gameEngine.isReady()) {
                throw new RuntimeException("Game engine failed to initialize: " + gameEngine.getLastError());
            }
        } catch (RuntimeException e) {
            // If initialization fails, we'll show error in start() method
            System.err.println("Failed to initialize game engine: " + e.getMessage());
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        if (gameEngine == null || !gameEngine.isReady()) {
            showInitializationError();
            return;
        }
        
        setupMainWindow();
        primaryStage.show();
    }
    
    /**
     * Setup the main application window
     */
    private void setupMainWindow() {
        primaryStage.setTitle("Tarot Game - JavaFX Edition");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        
        // Create main layout
        rootPane = new BorderPane();
        rootPane.setPadding(new Insets(10));
        
        // Setup components
        setupHeader();
        setupMainMenu();
        setupFooter();
        
        // Create scene
        mainScene = new Scene(rootPane);
        primaryStage.setScene(mainScene);
        
        // Handle window closing
        primaryStage.setOnCloseRequest(e -> {
            gameEngine.shutdown();
        });
    }
    
    /**
     * Setup the header section
     */
    private void setupHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        
        Label titleLabel = new Label("Tarot Game");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label subtitleLabel = new Label("Discover insights into your past, present, and future");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        rootPane.setTop(header);
    }
    
    /**
     * Setup the main menu section
     */
    private void setupMainMenu() {
        VBox menuPane = new VBox(15);
        menuPane.setAlignment(Pos.CENTER);
        menuPane.setPadding(new Insets(20));
        menuPane.setMaxWidth(300);
        
        // Main action buttons
        Button readingButton = createMenuButton("Get a Three-Card Reading", "primary");
        Button historyButton = createMenuButton("View Reading History", "secondary");
        Button statusButton = createMenuButton("Game Status & Statistics", "secondary");
        Button deckButton = createMenuButton("Deck Management", "secondary");
        Button aboutButton = createMenuButton("About Tarot Readings", "secondary");
        
        // Setup button actions
        readingButton.setOnAction(e -> performReading());
        historyButton.setOnAction(e -> viewHistory());
        statusButton.setOnAction(e -> showStatus());
        deckButton.setOnAction(e -> manageDeck());
        aboutButton.setOnAction(e -> showAbout());
        
        menuPane.getChildren().addAll(
            readingButton,
            historyButton, 
            statusButton,
            deckButton,
            aboutButton
        );
        
        // Center the menu
        BorderPane centerPane = new BorderPane();
        centerPane.setCenter(menuPane);
        rootPane.setCenter(centerPane);
    }
    
    /**
     * Setup the footer section with status
     */
    private void setupFooter() {
        VBox footer = new VBox(5);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        
        statusLabel = new Label();
        updateStatusLabel();
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888;");
        
        footer.getChildren().add(statusLabel);
        rootPane.setBottom(footer);
    }
    
    /**
     * Create a styled menu button
     */
    private Button createMenuButton(String text, String styleClass) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(40);
        
        if ("primary".equals(styleClass)) {
            button.setStyle(
                "-fx-background-color: #4a90e2; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-background-radius: 5px;"
            );
        } else {
            button.setStyle(
                "-fx-background-color: #f0f0f0; " +
                "-fx-text-fill: #333; " +
                "-fx-font-size: 14px; " +
                "-fx-background-radius: 5px;"
            );
        }
        
        // Hover effect
        button.setOnMouseEntered(e -> {
            if ("primary".equals(styleClass)) {
                button.setStyle(button.getStyle() + "-fx-background-color: #357abd;");
            } else {
                button.setStyle(button.getStyle() + "-fx-background-color: #e0e0e0;");
            }
        });
        
        button.setOnMouseExited(e -> {
            if ("primary".equals(styleClass)) {
                button.setStyle(button.getStyle().replace("-fx-background-color: #357abd;", ""));
            } else {
                button.setStyle(button.getStyle().replace("-fx-background-color: #e0e0e0;", ""));
            }
        });
        
        return button;
    }
    
    /**
     * Update the status label with current engine status
     */
    private void updateStatusLabel() {
        if (statusLabel != null && gameEngine != null) {
            statusLabel.setText("Status: " + gameEngine.getQuickStatus());
        }
    }
    
    /**
     * Handle reading action - placeholder for now
     */
    private void performReading() {
        ReadingViewController readingController = new ReadingViewController(gameEngine, primaryStage);
        readingController.showReadingInterface();
    }
    
    /**
     * Handle history action - placeholder
     */
    private void viewHistory() {
        showNotImplementedDialog("Reading History", 
            "The history interface is being developed. For now, please use the console version.");
    }
    
    /**
     * Handle status action - show basic status for now
     */
    private void showStatus() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Status");
        alert.setHeaderText("Current Game Status");
        
        StringBuilder status = new StringBuilder();
        status.append("Engine State: ").append(gameEngine.getCurrentState()).append("\n");
        status.append("Session Readings: ").append(gameEngine.getSessionReadings().size()).append("\n");
        status.append("Engine Status: ").append(gameEngine.getQuickStatus()).append("\n");
        
        alert.setContentText(status.toString());
        alert.showAndWait();
        
        updateStatusLabel();
    }
    
    /**
     * Handle deck management - placeholder
     */
    private void manageDeck() {
        showNotImplementedDialog("Deck Management", 
            "The deck management interface is being developed. For now, please use the console version.");
    }
    
    /**
     * Handle about action
     */
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Tarot Readings");
        alert.setHeaderText("Three-Card Reading");
        
        String aboutText = 
            """
            PAST - Influences from your past that shape the present
            PRESENT - Your current situation and immediate circumstances
            FUTURE - Potential outcomes and future influences
            
            Card Orientations:
            UPRIGHT - Positive aspects, direct meaning
            REVERSED - Challenges, blocked energy, or inner reflection
            
            The 22 Major Arcana cards represent major life themes,
            spiritual lessons, and significant life events.""";
        
        alert.setContentText(aboutText);
        alert.showAndWait();
    }
    
    /**
     * Show not implemented dialog
     */
    private void showNotImplementedDialog(String feature, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Feature In Development");
        alert.setHeaderText(feature + " - Coming Soon");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show initialization error
     */
    private void showInitializationError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Initialization Error");
        alert.setHeaderText("Failed to Initialize Game Engine");
        alert.setContentText("""
                             The game engine could not be initialized properly.
                             Please check your installation and try again.
                             
                             You can still use the console version of the game.""");
        alert.showAndWait();
        primaryStage.close();
    }
    
    @Override
    public void stop() throws Exception {
        if (gameEngine != null) {
            gameEngine.shutdown();
        }
    }
}
