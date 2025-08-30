package com.tarotgame.ui.javafx;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.tarotgame.engine.GameEngine;
import com.tarotgame.model.Card;
import com.tarotgame.service.ReadingSystem;
import com.tarotgame.service.ReadingSystem.Reading;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * JavaFX controller for the reading history interface.
 * Displays session readings with detailed view capabilities.
 */
public class HistoryViewController {
    
    private final GameEngine gameEngine;
    private final Stage primaryStage;
    private Stage historyStage;
    
    // UI Components
    private ListView<ReadingListItem> readingsList;
    private VBox detailsArea;
    private Button viewDetailsButton;
    private Button clearHistoryButton;
    private Button refreshButton;
    private Button closeButton;
    private Label statusLabel;
    
    public HistoryViewController(GameEngine gameEngine, Stage primaryStage) {
        this.gameEngine = gameEngine;
        this.primaryStage = primaryStage;
    }
    
    /**
     * Show the history interface
     */
    public void showHistoryInterface() {
        if (historyStage != null) {
            refreshHistoryList();
            historyStage.toFront();
            return;
        }
        
        createHistoryWindow();
        historyStage.show();
    }
    
    /**
     * Create and setup the history window
     */
    private void createHistoryWindow() {
        historyStage = new Stage();
        historyStage.initModality(Modality.NONE);
        historyStage.initOwner(primaryStage);
        historyStage.setTitle("Reading History");
        historyStage.setWidth(1000);
        historyStage.setHeight(700);
        historyStage.setMinWidth(800);
        historyStage.setMinHeight(500);
        
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
        historyStage.setScene(scene);
        
        // Load initial data
        refreshHistoryList();
        
        // Handle window closing
        historyStage.setOnCloseRequest(e -> {
            historyStage = null;
        });
    }
    
    /**
     * Setup the header section
     */
    private void setupHeader(BorderPane rootPane) {
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10, 0, 20, 0));
        
        Label titleLabel = new Label("Reading History");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label subtitleLabel = new Label("Review your past tarot readings");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d; -fx-font-style: italic;");
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        rootPane.setTop(header);
    }
    
    /**
     * Setup the main content area
     */
    private void setupMainContent(BorderPane rootPane) {
        HBox mainContent = new HBox(20);
        mainContent.setPadding(new Insets(10));
        
        // Left side: Reading list
        VBox listSection = setupReadingsList();
        listSection.setPrefWidth(350);
        listSection.setMinWidth(300);
        
        // Right side: Reading details
        VBox detailsSection = setupDetailsArea();
        HBox.setHgrow(detailsSection, Priority.ALWAYS);
        
        mainContent.getChildren().addAll(listSection, detailsSection);
        rootPane.setCenter(mainContent);
    }
    
    /**
     * Setup the readings list section
     */
    private VBox setupReadingsList() {
        VBox listSection = new VBox(10);
        
        Label listLabel = new Label("Session Readings");
        listLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        readingsList = new ListView<>();
        readingsList.setPrefHeight(400);
        readingsList.getStyleClass().add("reading-list");
        
        // Handle selection changes
        readingsList.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                updateDetailsArea(newSelection);
                viewDetailsButton.setDisable(newSelection == null);
            }
        );
        
        // Action buttons for the list
        HBox listButtons = new HBox(10);
        listButtons.setAlignment(Pos.CENTER);
        
        refreshButton = new Button("Refresh");
        styleButton(refreshButton, "secondary");
        refreshButton.setOnAction(e -> refreshHistoryList());
        
        viewDetailsButton = new Button("View Details");
        styleButton(viewDetailsButton, "primary");
        viewDetailsButton.setDisable(true);
        viewDetailsButton.setOnAction(e -> showDetailedView());
        
        clearHistoryButton = new Button("Clear All");
        styleButton(clearHistoryButton, "danger");
        clearHistoryButton.setOnAction(e -> confirmClearHistory());
        
        listButtons.getChildren().addAll(refreshButton, viewDetailsButton, clearHistoryButton);
        
        listSection.getChildren().addAll(listLabel, readingsList, listButtons);
        return listSection;
    }
    
    /**
     * Setup the details area section
     */
    private VBox setupDetailsArea() {
        detailsArea = new VBox(15);
        detailsArea.setPadding(new Insets(10));
        detailsArea.setStyle(
            "-fx-background-color: #f8f9fa; " +
            "-fx-background-radius: 10px; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 10px;"
        );
        
        Label detailsLabel = new Label("Reading Details");
        detailsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label instructionLabel = new Label("Select a reading from the list to view details here.");
        instructionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6c757d; -fx-font-style: italic;");
        instructionLabel.setWrapText(true);
        instructionLabel.setTextAlignment(TextAlignment.CENTER);
        instructionLabel.setAlignment(Pos.CENTER);
        
        detailsArea.getChildren().addAll(detailsLabel, instructionLabel);
        
        VBox container = new VBox(detailsArea);
        VBox.setVgrow(detailsArea, Priority.ALWAYS);
        return container;
    }
    
    /**
     * Setup the footer with action buttons
     */
    private void setupFooter(BorderPane rootPane) {
        HBox footer = new HBox(15);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20, 0, 10, 0));
        
        closeButton = new Button("Close");
        styleButton(closeButton, "secondary");
        closeButton.setOnAction(e -> historyStage.close());
        
        // Status label
        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");
        
        HBox leftSide = new HBox();
        HBox rightSide = new HBox(closeButton);
        HBox center = new HBox(statusLabel);
        center.setAlignment(Pos.CENTER);
        
        HBox.setHgrow(leftSide, Priority.ALWAYS);
        HBox.setHgrow(center, Priority.ALWAYS);
        HBox.setHgrow(rightSide, Priority.ALWAYS);
        leftSide.setAlignment(Pos.CENTER_LEFT);
        rightSide.setAlignment(Pos.CENTER_RIGHT);
        
        footer.getChildren().addAll(leftSide, center, rightSide);
        rootPane.setBottom(footer);
    }
    
    /**
     * Refresh the readings list
     */
    private void refreshHistoryList() {
        List<Reading> readings = gameEngine.getSessionReadings();
        readingsList.getItems().clear();
        
        if (readings.isEmpty()) {
            statusLabel.setText("No readings in session history");
        } else {
            // Add readings in reverse order (newest first)
            for (int i = readings.size() - 1; i >= 0; i--) {
                Reading reading = readings.get(i);
                readingsList.getItems().add(new ReadingListItem(reading, i + 1));
            }
            statusLabel.setText(readings.size() + " reading(s) in history");
        }
        
        // Clear details if list is empty
        if (readings.isEmpty()) {
            updateDetailsArea(null);
        }
    }
    
    /**
     * Update the details area with selected reading
     */
    private void updateDetailsArea(ReadingListItem selectedItem) {
        detailsArea.getChildren().clear();
        
        Label detailsLabel = new Label("Reading Details");
        detailsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        detailsArea.getChildren().add(detailsLabel);
        
        if (selectedItem == null) {
            Label instructionLabel = new Label("Select a reading from the list to view details here.");
            instructionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6c757d; -fx-font-style: italic;");
            instructionLabel.setWrapText(true);
            instructionLabel.setTextAlignment(TextAlignment.CENTER);
            instructionLabel.setAlignment(Pos.CENTER);
            detailsArea.getChildren().add(instructionLabel);
            return;
        }
        
        Reading reading = selectedItem.getReading();
        
        // Reading metadata
        VBox metadataBox = new VBox(8);
        metadataBox.setPadding(new Insets(10));
        metadataBox.setStyle("-fx-background-color: white; -fx-background-radius: 5px;");
        
        Label readingId = new Label("Reading ID: " + reading.getReadingId());
        Label timestamp = new Label("Date: " + reading.getFormattedTimestamp());
        Label readingNumber = new Label("Reading #" + selectedItem.getDisplayNumber());
        
        readingId.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");
        timestamp.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");
        readingNumber.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d; -fx-font-weight: bold;");
        
        metadataBox.getChildren().addAll(readingNumber, readingId, timestamp);
        detailsArea.getChildren().add(metadataBox);
        
        // Cards summary
        VBox cardsBox = new VBox(8);
        cardsBox.setPadding(new Insets(10));
        cardsBox.setStyle("-fx-background-color: white; -fx-background-radius: 5px;");
        
        Label cardsLabel = new Label("Cards Drawn");
        cardsLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        cardsBox.getChildren().add(cardsLabel);
        
        Card[] cards = reading.getCards();
        ReadingSystem.ReadingPosition[] positions = ReadingSystem.ReadingPosition.values();
        
        for (int i = 0; i < positions.length; i++) {
            HBox cardRow = new HBox(10);
            cardRow.setAlignment(Pos.CENTER_LEFT);
            
            Label positionLabel = new Label(positions[i].getDisplayName() + ":");
            positionLabel.setStyle("-fx-font-weight: bold; -fx-min-width: 60px;");
            
            Label cardLabel = new Label(cards[i].getDisplayName());
            cardLabel.setStyle("-fx-text-fill: #495057;");
            
            cardRow.getChildren().addAll(positionLabel, cardLabel);
            cardsBox.getChildren().add(cardRow);
        }
        
        detailsArea.getChildren().add(cardsBox);
        
        // Brief interpretation preview
        VBox interpretationBox = new VBox(8);
        interpretationBox.setPadding(new Insets(10));
        interpretationBox.setStyle("-fx-background-color: white; -fx-background-radius: 5px;");
        
        Label interpretationLabel = new Label("Interpretation Preview");
        interpretationLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Show first few lines of overall interpretation
        String fullInterpretation = reading.getInterpretation().getOverallInterpretation();
        String preview = truncateText(fullInterpretation, 200);
        
        TextArea previewArea = new TextArea(preview);
        previewArea.setWrapText(true);
        previewArea.setEditable(false);
        previewArea.setPrefRowCount(4);
        previewArea.setStyle(
            "-fx-background-color: #f8f9fa; " +
            "-fx-control-inner-background: #f8f9fa; " +
            "-fx-text-fill: #495057; " +
            "-fx-font-size: 12px; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-radius: 3px;"
        );
        
        interpretationBox.getChildren().addAll(interpretationLabel, previewArea);
        detailsArea.getChildren().add(interpretationBox);
    }
    
    /**
     * Show detailed view of selected reading
     */
    private void showDetailedView() {
        ReadingListItem selectedItem = readingsList.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;
        
        Reading reading = selectedItem.getReading();
        showDetailedReadingWindow(reading);
    }
    
    /**
     * Show detailed reading in a separate window
     */
    private void showDetailedReadingWindow(Reading reading) {
        Stage detailStage = new Stage();
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.initOwner(historyStage);
        detailStage.setTitle("Reading Details - " + reading.getReadingId());
        detailStage.setWidth(800);
        detailStage.setHeight(700);
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(20));
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        // Full reading display
        TextArea fullReading = new TextArea(reading.getFormattedReading());
        fullReading.setWrapText(true);
        fullReading.setEditable(false);
        fullReading.setStyle(
            "-fx-font-family: 'Courier New', monospace; " +
            "-fx-font-size: 12px; " +
            "-fx-background-color: #ffffff; " +
            "-fx-control-inner-background: #ffffff; " +
            "-fx-text-fill: #2c3e50; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-radius: 5px;"
        );
        
        VBox.setVgrow(fullReading, Priority.ALWAYS);
        content.getChildren().add(fullReading);
        
        // Close button
        Button closeDetailButton = new Button("Close");
        closeDetailButton.setOnAction(e -> detailStage.close());
        styleButton(closeDetailButton, "secondary");
        
        HBox buttonBox = new HBox(closeDetailButton);
        buttonBox.setAlignment(Pos.CENTER);
        content.getChildren().add(buttonBox);
        
        scrollPane.setContent(content);
        Scene scene = new Scene(scrollPane);
        detailStage.setScene(scene);
        detailStage.show();
    }
    
    /**
     * Confirm and clear history
     */
    private void confirmClearHistory() {
        List<Reading> readings = gameEngine.getSessionReadings();
        if (readings.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(historyStage);
            alert.setTitle("No History");
            alert.setHeaderText(null);
            alert.setContentText("There are no readings in your history to clear.");
            alert.showAndWait();
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(historyStage);
        alert.setTitle("Clear Reading History");
        alert.setHeaderText("Clear All Readings?");
        alert.setContentText("You have " + readings.size() + " reading(s) in your history.\n\n" +
                            "This action cannot be undone. Are you sure you want to clear all readings?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                GameEngine.EngineResult result = gameEngine.clearReadingHistory();
                if (result.isSuccess()) {
                    refreshHistoryList();
                    statusLabel.setText("History cleared successfully");
                } else {
                    showErrorAlert("Clear Failed", "Failed to clear history: " + result.getMessage());
                }
            }
        });
    }
    
    /**
     * Truncate text to specified length with ellipsis
     */
    private String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Style a button with the specified style class
     */
    private void styleButton(Button button, String styleClass) {
        button.setPrefHeight(30);
        button.setMinWidth(80);
        
        switch (styleClass) {
            case "primary" -> {
                button.setStyle(
                    "-fx-background-color: #4a90e2; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 12px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-cursor: hand;"
                );
                button.setOnMouseEntered(e -> 
                    button.setStyle(button.getStyle() + "-fx-background-color: #357abd;"));
                button.setOnMouseExited(e -> 
                    button.setStyle(button.getStyle().replace("-fx-background-color: #357abd;", "")));
            }
            case "danger" -> {
                button.setStyle(
                    "-fx-background-color: #dc3545; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 12px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-cursor: hand;"
                );
                button.setOnMouseEntered(e -> 
                    button.setStyle(button.getStyle() + "-fx-background-color: #c82333;"));
                button.setOnMouseExited(e -> 
                    button.setStyle(button.getStyle().replace("-fx-background-color: #c82333;", "")));
            }
            default -> { // secondary
                button.setStyle(
                    "-fx-background-color: #6c757d; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 12px; " +
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
     * Show error alert
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(historyStage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Get custom CSS for the history interface
     */
    private String getCustomCSS() {
        return """
            .reading-list .list-cell {
                -fx-padding: 8px 12px;
                -fx-border-color: transparent transparent #e9ecef transparent;
                -fx-border-width: 0 0 1px 0;
            }
            .reading-list .list-cell:selected {
                -fx-background-color: #e3f2fd;
                -fx-text-fill: #1976d2;
            }
            .reading-list .list-cell:hover {
                -fx-background-color: #f5f5f5;
            }
            .scroll-pane {
                -fx-background-color: transparent;
            }
            .scroll-pane .viewport {
                -fx-background-color: transparent;
            }
            .scroll-pane .content {
                -fx-background-color: transparent;
            }
            """;
    }
    
    /**
     * List item wrapper for readings
     */
    private static class ReadingListItem {
        private final Reading reading;
        private final int displayNumber;
        
        public ReadingListItem(Reading reading, int displayNumber) {
            this.reading = reading;
            this.displayNumber = displayNumber;
        }
        
        public Reading getReading() {
            return reading;
        }
        
        public int getDisplayNumber() {
            return displayNumber;
        }
        
        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
            String timestamp = reading.getTimestamp().format(formatter);
            
            // Get card summary
            Card[] cards = reading.getCards();
            String summary = cards[0].getName() + " | " + 
                           cards[1].getName() + " | " + 
                           cards[2].getName();
            
            return String.format("#%d - %s\n%s", displayNumber, timestamp, summary);
        }
    }
}
