package com.tarotgame.ui.javafx;

import com.tarotgame.engine.GameEngine;
import com.tarotgame.model.Card;
import com.tarotgame.service.ReadingSystem;
import com.tarotgame.service.ReadingSystem.Reading;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * JavaFX controller for the tarot reading interface.
 * Provides a complete reading experience with card display and interpretation.
 */
public class ReadingViewController {
    
    private final GameEngine gameEngine;
    private final Stage primaryStage;
    private Stage readingStage;
    private Reading currentReading;
    
    // UI Components
    private VBox mainContainer;
    private TextField questionField;
    private Button startReadingButton;
    private VBox cardDisplayArea;
    private VBox interpretationArea;
    private Button newReadingButton;
    private Button closeButton;
    private Label statusLabel;
    
    public ReadingViewController(GameEngine gameEngine, Stage primaryStage) {
        this.gameEngine = gameEngine;
        this.primaryStage = primaryStage;
    }
    
    /**
     * Show the reading interface
     */
    public void showReadingInterface() {
        if (readingStage != null) {
            readingStage.toFront();
            return;
        }
        
        createReadingWindow();
        readingStage.show();
    }
    
    /**
     * Create and setup the reading window
     */
    private void createReadingWindow() {
        readingStage = new Stage();
        readingStage.initModality(Modality.NONE);
        readingStage.initOwner(primaryStage);
        readingStage.setTitle("Tarot Reading");
        readingStage.setWidth(900);
        readingStage.setHeight(700);
        readingStage.setMinWidth(700);
        readingStage.setMinHeight(500);
        
        // Create main layout
        BorderPane rootPane = new BorderPane();
        rootPane.setPadding(new Insets(15));
        
        // Setup components
        setupHeader(rootPane);
        setupMainContent(rootPane);
        setupFooter(rootPane);
        
        // Create scene and apply basic styling
        Scene scene = new Scene(rootPane);
        scene.getStylesheets().add("data:text/css," + getCustomCSS());
        readingStage.setScene(scene);
        
        // Handle window closing
        readingStage.setOnCloseRequest(e -> {
            readingStage = null;
        });
    }
    
    /**
     * Setup the header section
     */
    private void setupHeader(BorderPane rootPane) {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10, 0, 20, 0));
        
        Label titleLabel = new Label("Three-Card Tarot Reading");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label subtitleLabel = new Label("Past • Present • Future");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d; -fx-font-style: italic;");
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        rootPane.setTop(header);
    }
    
    /**
     * Setup the main content area
     */
    private void setupMainContent(BorderPane rootPane) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(10));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        
        setupQuestionSection();
        setupCardDisplayArea();
        setupInterpretationArea();
        
        scrollPane.setContent(mainContainer);
        rootPane.setCenter(scrollPane);
    }
    
    /**
     * Setup the footer with action buttons
     */
    private void setupFooter(BorderPane rootPane) {
        HBox footer = new HBox(15);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20, 0, 10, 0));
        
        newReadingButton = new Button("New Reading");
        newReadingButton.setVisible(false);
        styleButton(newReadingButton, "primary");
        newReadingButton.setOnAction(e -> startNewReading());
        
        closeButton = new Button("Close");
        styleButton(closeButton, "secondary");
        closeButton.setOnAction(e -> readingStage.close());
        
        // Status label
        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");
        
        HBox leftSide = new HBox(newReadingButton);
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
     * Setup the question input section
     */
    private void setupQuestionSection() {
        VBox questionSection = new VBox(10);
        questionSection.setAlignment(Pos.CENTER);
        questionSection.setPadding(new Insets(10));
        questionSection.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10px;");
        questionSection.setMaxWidth(600);
        
        Label promptLabel = new Label("Focus on a question or intention (optional):");
        promptLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #495057;");
        
        questionField = new TextField();
        questionField.setPromptText("What guidance do you seek?");
        questionField.setStyle("-fx-font-size: 14px; -fx-pref-height: 35px;");
        questionField.setMaxWidth(400);
        
        startReadingButton = new Button("Begin Reading");
        styleButton(startReadingButton, "primary");
        startReadingButton.setOnAction(e -> performReading());
        
        questionSection.getChildren().addAll(promptLabel, questionField, startReadingButton);
        mainContainer.getChildren().add(questionSection);
    }
    
    /**
     * Setup the card display area
     */
    private void setupCardDisplayArea() {
        cardDisplayArea = new VBox(15);
        cardDisplayArea.setAlignment(Pos.CENTER);
        cardDisplayArea.setVisible(false);
        
        Label cardsLabel = new Label("Your Cards");
        cardsLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        cardDisplayArea.getChildren().add(cardsLabel);
        mainContainer.getChildren().add(cardDisplayArea);
    }
    
    /**
     * Setup the interpretation area
     */
    private void setupInterpretationArea() {
        interpretationArea = new VBox(15);
        interpretationArea.setAlignment(Pos.CENTER);
        interpretationArea.setVisible(false);
        
        Label interpretationLabel = new Label("Your Reading");
        interpretationLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        interpretationArea.getChildren().add(interpretationLabel);
        mainContainer.getChildren().add(interpretationArea);
    }
    
    /**
     * Perform the tarot reading
     */
    private void performReading() {
        if (!gameEngine.isReady()) {
            showErrorAlert("Engine Not Ready", "The game engine is not ready to perform a reading.");
            return;
        }
        
        // Disable the start button and show loading
        startReadingButton.setDisable(true);
        startReadingButton.setText("Drawing Cards...");
        statusLabel.setText("Consulting the cards...");
        
        // Perform reading in background thread to avoid blocking UI
        Platform.runLater(() -> {
            try {
                GameEngine.EngineResult result = gameEngine.performReading();
                
                if (result.isSuccess() && result.hasReading()) {
                    currentReading = result.getReading();
                    displayReading();
                } else {
                    showErrorAlert("Reading Failed", 
                        "Failed to perform reading: " + result.getMessage());
                    resetToInitialState();
                }
            } catch (Exception e) {
                showErrorAlert("Unexpected Error", 
                    "An unexpected error occurred: " + e.getMessage());
                resetToInitialState();
            }
        });
    }
    
    /**
     * Display the reading results
     */
    private void displayReading() {
        // Hide question section
        questionField.getParent().setVisible(false);
        
        // Show and populate card display
        displayCards();
        
        // Show and populate interpretation
        displayInterpretation();
        
        // Show new reading button
        newReadingButton.setVisible(true);
        
        // Update status
        statusLabel.setText("Reading completed: " + currentReading.getReadingId());
    }
    
    /**
     * Display the three cards
     */
    private void displayCards() {
        cardDisplayArea.getChildren().clear();
        
        Label cardsLabel = new Label("Your Cards");
        cardsLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        cardDisplayArea.getChildren().add(cardsLabel);
        
        HBox cardsContainer = new HBox(30);
        cardsContainer.setAlignment(Pos.CENTER);
        
        ReadingSystem.ReadingPosition[] positions = ReadingSystem.ReadingPosition.values();
        Card[] cards = currentReading.getCards();
        
        for (int i = 0; i < positions.length; i++) {
            VBox cardBox = createCardDisplay(positions[i], cards[i]);
            cardsContainer.getChildren().add(cardBox);
        }
        
        cardDisplayArea.getChildren().add(cardsContainer);
        cardDisplayArea.setVisible(true);
        
        // Animate card appearance
        animateCardAppearance();
    }
    
    /**
     * Create a visual representation of a single card
     */
    private VBox createCardDisplay(ReadingSystem.ReadingPosition position, Card card) {
        VBox cardBox = new VBox(10);
        cardBox.setAlignment(Pos.CENTER);
        cardBox.setPadding(new Insets(15));
        cardBox.setMaxWidth(200);
        cardBox.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-width: 1px; " +
            "-fx-background-radius: 8px; " +
            "-fx-border-radius: 8px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );
        
        // Position label
        Label positionLabel = new Label(position.getDisplayName().toUpperCase());
        positionLabel.setStyle(
            "-fx-font-size: 12px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #6c757d; " +
            "-fx-letter-spacing: 1px;"
        );
        
        // Card representation (placeholder rectangle)
        VBox cardRect = new VBox();
        cardRect.setPrefSize(120, 180);
        cardRect.setAlignment(Pos.CENTER);
        cardRect.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #4a90e2, #357abd); " +
            "-fx-background-radius: 5px; " +
            "-fx-border-color: #2c3e50; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 5px;"
        );
        
        // Card back pattern (decorative)
        Text cardPattern = new Text("✦");
        cardPattern.setStyle("-fx-fill: white; -fx-font-size: 40px;");
        cardRect.getChildren().add(cardPattern);
        
        // Card name
        Label cardName = new Label(card.getDisplayName());
        cardName.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #2c3e50; " +
            "-fx-text-alignment: center; " +
            "-fx-wrap-text: true;"
        );
        cardName.setTextAlignment(TextAlignment.CENTER);
        cardName.setMaxWidth(180);
        
        cardBox.getChildren().addAll(positionLabel, cardRect, cardName);
        return cardBox;
    }
    
    /**
     * Display the reading interpretation
     */
    private void displayInterpretation() {
        interpretationArea.getChildren().clear();
        
        Label interpretationLabel = new Label("Your Reading");
        interpretationLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        interpretationArea.getChildren().add(interpretationLabel);
        
        // Reading metadata
        VBox metadataBox = new VBox(5);
        metadataBox.setAlignment(Pos.CENTER);
        metadataBox.setPadding(new Insets(10));
        
        Label readingId = new Label("Reading ID: " + currentReading.getReadingId());
        Label timestamp = new Label(currentReading.getFormattedTimestamp());
        readingId.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");
        timestamp.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");
        
        String question = questionField.getText().trim();
        if (!question.isEmpty()) {
            Label questionLabel = new Label("Question: " + question);
            questionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d; -fx-font-style: italic;");
            metadataBox.getChildren().add(questionLabel);
        }
        
        metadataBox.getChildren().addAll(readingId, timestamp);
        interpretationArea.getChildren().add(metadataBox);
        
        // Individual card interpretations
        VBox individualInterpretations = new VBox(15);
        ReadingSystem.ReadingPosition[] positions = ReadingSystem.ReadingPosition.values();
        String[] interpretations = currentReading.getInterpretation().getIndividualInterpretations();
        Card[] cards = currentReading.getCards();
        
        for (int i = 0; i < positions.length; i++) {
            VBox cardInterpretation = createCardInterpretation(
                positions[i], cards[i], interpretations[i]);
            individualInterpretations.getChildren().add(cardInterpretation);
        }
        
        interpretationArea.getChildren().add(individualInterpretations);
        
        // Overall interpretation
        VBox overallSection = new VBox(10);
        overallSection.setPadding(new Insets(20, 10, 10, 10));
        overallSection.setStyle(
            "-fx-background-color: #f8f9fa; " +
            "-fx-background-radius: 10px; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 10px;"
        );
        
        Label overallLabel = new Label("Overall Guidance");
        overallLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        TextArea overallText = new TextArea(currentReading.getInterpretation().getOverallInterpretation());
        overallText.setWrapText(true);
        overallText.setEditable(false);
        overallText.setPrefRowCount(4);
        overallText.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-control-inner-background: transparent; " +
            "-fx-text-fill: #495057; " +
            "-fx-font-size: 14px; " +
            "-fx-border-color: transparent; " +
            "-fx-focus-color: transparent;"
        );
        
        overallSection.getChildren().addAll(overallLabel, overallText);
        interpretationArea.getChildren().add(overallSection);
        
        interpretationArea.setVisible(true);
    }
    
    /**
     * Create interpretation display for a single card
     */
    private VBox createCardInterpretation(ReadingSystem.ReadingPosition position, Card card, String interpretation) {
        VBox cardInterpBox = new VBox(8);
        cardInterpBox.setPadding(new Insets(15));
        cardInterpBox.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-width: 1px; " +
            "-fx-background-radius: 8px; " +
            "-fx-border-radius: 8px;"
        );
        
        Label positionHeader = new Label(position.getDisplayName().toUpperCase());
        positionHeader.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #4a90e2;"
        );
        
        Label cardLabel = new Label("Card: " + card.getDisplayName());
        cardLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #6c757d;");
        
        TextArea interpretationText = new TextArea(interpretation);
        interpretationText.setWrapText(true);
        interpretationText.setEditable(false);
        interpretationText.setPrefRowCount(3);
        interpretationText.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-control-inner-background: transparent; " +
            "-fx-text-fill: #495057; " +
            "-fx-font-size: 13px; " +
            "-fx-border-color: transparent; " +
            "-fx-focus-color: transparent;"
        );
        
        cardInterpBox.getChildren().addAll(positionHeader, cardLabel, interpretationText);
        return cardInterpBox;
    }
    
    /**
     * Animate card appearance
     */
    private void animateCardAppearance() {
        cardDisplayArea.setOpacity(0);
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), cardDisplayArea);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        
        PauseTransition pause = new PauseTransition(Duration.millis(300));
        
        SequentialTransition sequence = new SequentialTransition(pause, fadeIn);
        sequence.play();
    }
    
    /**
     * Start a new reading
     */
    private void startNewReading() {
        resetToInitialState();
    }
    
    /**
     * Reset to initial state for new reading
     */
    private void resetToInitialState() {
        // Reset UI elements
        questionField.setText("");
        questionField.getParent().setVisible(true);
        cardDisplayArea.setVisible(false);
        interpretationArea.setVisible(false);
        newReadingButton.setVisible(false);
        
        // Reset button
        startReadingButton.setDisable(false);
        startReadingButton.setText("Begin Reading");
        
        // Clear status
        statusLabel.setText("");
        
        // Clear current reading
        currentReading = null;
    }
    
    /**
     * Style a button with the specified style class
     */
    private void styleButton(Button button, String styleClass) {
        button.setPrefHeight(35);
        button.setMinWidth(120);
        
        if ("primary".equals(styleClass)) {
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
            
        } else {
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
    
    /**
     * Show error alert
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(readingStage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Get custom CSS for the reading interface
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
