package com.tarotgame.ui.javafx;

import com.tarotgame.engine.GameEngine;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * JavaFX controller for the deck management interface.
 * Provides deck operations like reset, new deck creation, and status viewing.
 */
public class DeckViewController {
    
    private final GameEngine gameEngine;
    private final Stage primaryStage;
    private Stage deckStage;
    
    // UI Components
    private Label statusLabel;
    private TextArea deckInfoArea;
    private Button resetDeckButton;
    private Button createNewDeckButton;
    private Button refreshStatusButton;
    private Button closeButton;
    
    public DeckViewController(GameEngine gameEngine, Stage primaryStage) {
        this.gameEngine = gameEngine;
        this.primaryStage = primaryStage;
    }
    
    /**
     * Show the deck management interface
     */
    public void showDeckManagementInterface() {
        if (deckStage != null) {
            refreshDeckStatus();
            deckStage.toFront();
            return;
        }
        
        createDeckWindow();
        deckStage.show();
    }
    
    /**
     * Create and setup the deck management window
     */
    private void createDeckWindow() {
        deckStage = new Stage();
        deckStage.initModality(Modality.NONE);
        deckStage.initOwner(primaryStage);
        deckStage.setTitle("Deck Management");
        deckStage.setWidth(700);
        deckStage.setHeight(600);
        deckStage.setMinWidth(600);
        deckStage.setMinHeight(500);
        
        // Create main layout
        BorderPane rootPane = new BorderPane();
        rootPane.setPadding(new Insets(15));
        
        // Setup components
        setupHeader(rootPane);
        setupMainContent(rootPane);
        setupFooter(rootPane);
        
        // Create scene
        Scene scene = new Scene(rootPane);
        scene.getStylesheets().add("data:text/css," + getCustomCSS());
        deckStage.setScene(scene);
        
        // Load initial data
        refreshDeckStatus();
        
        // Handle window closing
        deckStage.setOnCloseRequest(e -> {
            deckStage = null;
        });
    }
    
    /**
     * Setup the header section
     */
    private void setupHeader(BorderPane rootPane) {
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10, 0, 20, 0));
        
        Label titleLabel = new Label("Deck Management");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label subtitleLabel = new Label("Manage your tarot deck settings and status");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d; -fx-font-style: italic;");
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        rootPane.setTop(header);
    }
    
    /**
     * Setup the main content area
     */
    private void setupMainContent(BorderPane rootPane) {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(10));
        
        // Current status section
        setupStatusSection(mainContent);
        
        // Deck information section
        setupDeckInfoSection(mainContent);
        
        // Action buttons section
        setupActionButtonsSection(mainContent);
        
        rootPane.setCenter(mainContent);
    }
    
    /**
     * Setup the status section
     */
    private void setupStatusSection(VBox mainContent) {
        VBox statusSection = new VBox(10);
        statusSection.setAlignment(Pos.CENTER);
        statusSection.setPadding(new Insets(15));
        statusSection.setStyle(
            "-fx-background-color: #f8f9fa; " +
            "-fx-background-radius: 10px; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 10px;"
        );
        
        Label statusHeaderLabel = new Label("Current Deck Status");
        statusHeaderLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #495057; -fx-text-alignment: center;");
        statusLabel.setTextAlignment(TextAlignment.CENTER);
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        
        refreshStatusButton = new Button("Refresh Status");
        styleButton(refreshStatusButton, "secondary");
        refreshStatusButton.setOnAction(e -> refreshDeckStatus());
        
        statusSection.getChildren().addAll(statusHeaderLabel, statusLabel, refreshStatusButton);
        mainContent.getChildren().add(statusSection);
    }
    
    /**
     * Setup the deck information section
     */
    private void setupDeckInfoSection(VBox mainContent) {
        VBox infoSection = new VBox(10);
        infoSection.setPadding(new Insets(15));
        infoSection.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-background-radius: 10px; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 10px;"
        );
        
        Label infoHeaderLabel = new Label("Deck Information");
        infoHeaderLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        deckInfoArea = new TextArea();
        deckInfoArea.setWrapText(true);
        deckInfoArea.setEditable(false);
        deckInfoArea.setPrefRowCount(8);
        deckInfoArea.setStyle(
            "-fx-background-color: #f8f9fa; " +
            "-fx-control-inner-background: #f8f9fa; " +
            "-fx-text-fill: #495057; " +
            "-fx-font-size: 13px; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-radius: 5px; " +
            "-fx-font-family: 'Courier New', monospace;"
        );
        
        VBox.setVgrow(deckInfoArea, Priority.ALWAYS);
        
        infoSection.getChildren().addAll(infoHeaderLabel, deckInfoArea);
        mainContent.getChildren().add(infoSection);
    }
    
    /**
     * Setup the action buttons section
     */
    private void setupActionButtonsSection(VBox mainContent) {
        VBox actionSection = new VBox(15);
        actionSection.setAlignment(Pos.CENTER);
        actionSection.setPadding(new Insets(15));
        actionSection.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-background-radius: 10px; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 10px;"
        );
        
        Label actionHeaderLabel = new Label("Deck Actions");
        actionHeaderLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Action buttons grid
        HBox buttonRow1 = new HBox(15);
        buttonRow1.setAlignment(Pos.CENTER);
        
        HBox buttonRow2 = new HBox(15);
        buttonRow2.setAlignment(Pos.CENTER);
        
        // Reset deck button
        resetDeckButton = new Button("Reset Deck");
        resetDeckButton.setPrefWidth(150);
        styleButton(resetDeckButton, "primary");
        resetDeckButton.setOnAction(e -> confirmResetDeck());
        
        // Create new deck button
        createNewDeckButton = new Button("Create New Deck");
        createNewDeckButton.setPrefWidth(150);
        styleButton(createNewDeckButton, "warning");
        createNewDeckButton.setOnAction(e -> confirmCreateNewDeck());
        
        buttonRow1.getChildren().addAll(resetDeckButton, createNewDeckButton);
        
        // Add explanatory text
        VBox explanationBox = new VBox(8);
        explanationBox.setAlignment(Pos.CENTER);
        explanationBox.setPadding(new Insets(10));
        explanationBox.setStyle("-fx-background-color: #e7f3ff; -fx-background-radius: 5px;");
        
        Label resetExplain = new Label("Reset Deck: Shuffles all 22 cards back into the current deck");
        resetExplain.setStyle("-fx-font-size: 12px; -fx-text-fill: #0066cc;");
        resetExplain.setWrapText(true);
        resetExplain.setTextAlignment(TextAlignment.CENTER);
        
        Label newDeckExplain = new Label("New Deck: Creates a completely fresh deck instance");
        newDeckExplain.setStyle("-fx-font-size: 12px; -fx-text-fill: #cc6600;");
        newDeckExplain.setWrapText(true);
        newDeckExplain.setTextAlignment(TextAlignment.CENTER);
        
        explanationBox.getChildren().addAll(resetExplain, newDeckExplain);
        
        actionSection.getChildren().addAll(actionHeaderLabel, buttonRow1, explanationBox);
        mainContent.getChildren().add(actionSection);
    }
    
    /**
     * Setup the footer with close button
     */
    private void setupFooter(BorderPane rootPane) {
        HBox footer = new HBox(15);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20, 0, 10, 0));
        
        closeButton = new Button("Close");
        styleButton(closeButton, "secondary");
        closeButton.setOnAction(e -> deckStage.close());
        
        footer.getChildren().add(closeButton);
        rootPane.setBottom(footer);
    }
    
    /**
     * Refresh the deck status display
     */
    private void refreshDeckStatus() {
        // Update status label
        String quickStatus = gameEngine.getQuickStatus();
        statusLabel.setText(quickStatus);
        
        // Update detailed information area
        StringBuilder info = new StringBuilder();
        
        info.append("=== DECK MANAGEMENT INFORMATION ===\n\n");
        
        // Engine status
        info.append("ENGINE STATUS:\n");
        info.append("  State: ").append(gameEngine.getCurrentState()).append("\n");
        info.append("  Ready: ").append(gameEngine.isReady() ? "Yes" : "No").append("\n");
        info.append("  Has Error: ").append(gameEngine.hasError() ? "Yes" : "No").append("\n");
        if (gameEngine.hasError()) {
            info.append("  Last Error: ").append(gameEngine.getLastError()).append("\n");
        }
        info.append("\n");
        
        // Session statistics
        GameEngine.SessionStats stats = gameEngine.getSessionStats();
        info.append("SESSION STATISTICS:\n");
        info.append("  Total Readings: ").append(stats.getTotalReadings()).append("\n");
        info.append("  Readings in History: ").append(stats.getReadingsInHistory()).append("\n");
        info.append("  Deck Resets: ").append(stats.getDeckResets()).append("\n");
        info.append("  Session Uptime: ").append(stats.getUptime()).append("\n");
        info.append("\n");
        
        // Deck information
        info.append("DECK COMPOSITION:\n");
        info.append("  Total Cards: 22 Major Arcana\n");
        info.append("  Cards Range: The Fool (0) to The World (21)\n");
        info.append("  Auto-Reshuffle: When < 3 cards remain\n");
        info.append("\n");
        
        // Deck operations explanation
        info.append("DECK OPERATIONS:\n");
        info.append("  Reset Deck:\n");
        info.append("    - Reshuffles all 22 cards back into current deck\n");
        info.append("    - Maintains existing deck instance\n");
        info.append("    - Clears any error states\n");
        info.append("\n");
        info.append("  Create New Deck:\n");
        info.append("    - Creates completely new deck instance\n");
        info.append("    - Resets all internal deck state\n");
        info.append("    - Counted as a reset in statistics\n");
        info.append("\n");
        
        // Current detailed status
        String engineStatus = gameEngine.getEngineStatus();
        if (engineStatus != null && !engineStatus.isEmpty()) {
            info.append("DETAILED ENGINE STATUS:\n");
            info.append(engineStatus);
        }
        
        deckInfoArea.setText(info.toString());
        
        // Update button states based on engine status
        boolean engineReady = gameEngine.isReady();
        resetDeckButton.setDisable(!engineReady);
        createNewDeckButton.setDisable(!engineReady);
        
        if (!engineReady) {
            statusLabel.setText(statusLabel.getText() + " (Actions disabled - engine not ready)");
        }
    }
    
    /**
     * Confirm and execute deck reset
     */
    private void confirmResetDeck() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(deckStage);
        alert.setTitle("Reset Deck");
        alert.setHeaderText("Reset Current Deck?");
        alert.setContentText("""
                             This will shuffle all 22 Major Arcana cards back into the deck.
                             
                             Any previously drawn cards will be returned to the deck.
                             This action cannot be undone.
                             
                             Continue with deck reset?""");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                performDeckReset();
            }
        });
    }
    
    /**
     * Execute deck reset operation
     */
    private void performDeckReset() {
        GameEngine.EngineResult result = gameEngine.resetDeck();
        
        if (result.isSuccess()) {
            showSuccessAlert("Deck Reset", "Success", result.getMessage());
            refreshDeckStatus();
        } else {
            showErrorAlert("Reset Failed", "Failed to reset deck: " + result.getMessage());
        }
    }
    
    /**
     * Confirm and execute new deck creation
     */
    private void confirmCreateNewDeck() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(deckStage);
        alert.setTitle("Create New Deck");
        alert.setHeaderText("Create New Deck Instance?");
        alert.setContentText("""
                             This will create a completely new deck, replacing the current one.
                             
                             All deck state will be reset to initial conditions.
                             This action cannot be undone.
                             
                             Continue with new deck creation?""");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                performNewDeckCreation();
            }
        });
    }
    
    /**
     * Execute new deck creation operation
     */
    private void performNewDeckCreation() {
        GameEngine.EngineResult result = gameEngine.createNewDeck();
        
        if (result.isSuccess()) {
            showSuccessAlert("New Deck Created", "Success", result.getMessage());
            refreshDeckStatus();
        } else {
            showErrorAlert("Creation Failed", "Failed to create new deck: " + result.getMessage());
        }
    }
    
    /**
     * Style a button with the specified style class
     */
    private void styleButton(Button button, String styleClass) {
        button.setPrefHeight(35);
        button.setMinWidth(120);
        
        switch (styleClass) {
            case "primary" -> {
                button.setStyle(
                    "-fx-background-color: #4a90e2; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 14px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-cursor: hand;"
                );
                button.setOnMouseEntered(e -> 
                    button.setStyle(button.getStyle() + "-fx-background-color: #357abd;"));
                button.setOnMouseExited(e -> 
                    button.setStyle(button.getStyle().replace("-fx-background-color: #357abd;", "")));
            }
            case "warning" -> {
                button.setStyle(
                    "-fx-background-color: #f39c12; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 14px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-cursor: hand;"
                );
                button.setOnMouseEntered(e -> 
                    button.setStyle(button.getStyle() + "-fx-background-color: #e67e22;"));
                button.setOnMouseExited(e -> 
                    button.setStyle(button.getStyle().replace("-fx-background-color: #e67e22;", "")));
            }
            default -> { // secondary
                button.setStyle(
                    "-fx-background-color: #6c757d; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 14px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-cursor: hand;"
                );
                button.setOnMouseEntered(e -> 
                    button.setStyle(button.getStyle() + "-fx-background-color: #5a6268;"));
                button.setOnMouseExited(e -> 
                    button.setStyle(button.getStyle().replace("-fx-background-color: #5a6268;", "")));
            }
        }
    }
    
    /**
     * Show success alert
     */
    private void showSuccessAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(deckStage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show error alert
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(deckStage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Get custom CSS for the deck management interface
     */
    private String getCustomCSS() {
        return """
            .scroll-pane {
                -fx-background-color: transparent;
            }
            .scroll-pane .viewport {
                -fx-background-color: transparent;
            }
            .scroll-pane .content {
                -fx-background-color: transparent;
            }
            .text-area {
                -fx-background-insets: 0;
                -fx-background-radius: 0;
            }
            .text-area .content {
                -fx-background-color: transparent;
            }
            """;
    }
}
