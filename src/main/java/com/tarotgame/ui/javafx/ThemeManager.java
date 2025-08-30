package com.tarotgame.ui.javafx;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

/**
 * Centralized theme management for consistent styling across the Tarot Game application.
 * Colors inspired by traditional tarot card aesthetics with dark mystical themes.
 */
public class ThemeManager {
    
    // Core Color Palette (inspired by tarot card imagery)
    public static final String PRIMARY_DARK = "#1a1a1a";        // Deep black background
    public static final String SECONDARY_DARK = "#2c2c2c";      // Slightly lighter dark
    public static final String ACCENT_GOLD = "#d4af37";         // Mystical gold
    public static final String ACCENT_GOLD_LIGHT = "#f4d03f";   // Lighter gold
    public static final String ACCENT_GOLD_DARK = "#b7950b";    // Darker gold
    
    // Background Colors
    public static final String BACKGROUND_PRIMARY = "#0f0f0f";   // Main app background
    public static final String BACKGROUND_CARD = "#1e1e1e";     // Card/panel background
    public static final String BACKGROUND_INPUT = "#252525";    // Input field background
    public static final String BACKGROUND_HOVER = "#333333";    // Hover states
    
    // Text Colors
    public static final String TEXT_PRIMARY = "#f8f8f8";        // Main text (light)
    public static final String TEXT_SECONDARY = "#cccccc";      // Secondary text
    public static final String TEXT_MUTED = "#999999";         // Muted/hint text
    public static final String TEXT_ACCENT = "#d4af37";        // Accent text (gold)
    
    // Border and Accent Colors
    public static final String BORDER_PRIMARY = "#444444";      // Primary borders
    public static final String BORDER_ACCENT = "#d4af37";      // Gold borders
    public static final String SHADOW_COLOR = "rgba(0,0,0,0.6)"; // Drop shadows
    
    // Status Colors
    public static final String SUCCESS_COLOR = "#27ae60";       // Success states
    public static final String WARNING_COLOR = "#f39c12";       // Warning states
    public static final String ERROR_COLOR = "#e74c3c";         // Error states
    public static final String INFO_COLOR = "#3498db";          // Info states
    
    // Typography Scale
    public static final double FONT_SIZE_TITLE = 28.0;         // Main titles
    public static final double FONT_SIZE_SUBTITLE = 18.0;      // Subtitles
    public static final double FONT_SIZE_HEADER = 16.0;        // Section headers
    public static final double FONT_SIZE_BODY = 14.0;          // Body text
    public static final double FONT_SIZE_SMALL = 12.0;         // Small text
    public static final double FONT_SIZE_CAPTION = 10.0;       // Captions/hints
    
    // Spacing Scale
    public static final double SPACING_SMALL = 8.0;            // Small spacing
    public static final double SPACING_MEDIUM = 16.0;          // Medium spacing
    public static final double SPACING_LARGE = 24.0;           // Large spacing
    public static final double SPACING_XLARGE = 32.0;          // Extra large spacing
    
    // Component Dimensions
    public static final double BUTTON_HEIGHT = 36.0;           // Standard button height
    public static final double BUTTON_HEIGHT_SMALL = 28.0;     // Small button height
    public static final double BUTTON_MIN_WIDTH = 100.0;       // Minimum button width
    public static final double BORDER_RADIUS = 6.0;            // Standard border radius
    public static final double BORDER_WIDTH = 1.0;             // Standard border width
    
    // Window Sizing
    public static final double WINDOW_MIN_WIDTH = 700.0;       // Minimum window width
    public static final double WINDOW_MIN_HEIGHT = 500.0;      // Minimum window height
    public static final double WINDOW_DEFAULT_WIDTH = 850.0;   // Default window width
    public static final double WINDOW_DEFAULT_HEIGHT = 650.0;  // Default window height
    
    // Animation Durations (in milliseconds)
    public static final double ANIMATION_FAST = 200.0;         // Fast animations
    public static final double ANIMATION_MEDIUM = 400.0;       // Medium animations
    public static final double ANIMATION_SLOW = 800.0;         // Slow animations
    
    /**
     * Apply the main application theme to a root pane
     */
    public static void applyMainTheme(Pane rootPane) {
        rootPane.setStyle(
            "-fx-background-color: " + BACKGROUND_PRIMARY + ";"
        );
    }
    
    /**
     * Apply card/panel styling for content areas
     */
    public static void applyCardStyle(Pane pane) {
        pane.setStyle(
            "-fx-background-color: " + BACKGROUND_CARD + "; " +
            "-fx-border-color: " + BORDER_PRIMARY + "; " +
            "-fx-border-width: " + BORDER_WIDTH + "px; " +
            "-fx-border-radius: " + BORDER_RADIUS + "px; " +
            "-fx-background-radius: " + BORDER_RADIUS + "px; " +
            "-fx-effect: dropshadow(gaussian, " + SHADOW_COLOR + ", 8, 0, 0, 2);"
        );
    }
    
    /**
     * Apply accent card styling for highlighted areas
     */
    public static void applyAccentCardStyle(Pane pane) {
        pane.setStyle(
            "-fx-background-color: " + BACKGROUND_CARD + "; " +
            "-fx-border-color: " + BORDER_ACCENT + "; " +
            "-fx-border-width: " + (BORDER_WIDTH * 2) + "px; " +
            "-fx-border-radius: " + BORDER_RADIUS + "px; " +
            "-fx-background-radius: " + BORDER_RADIUS + "px; " +
            "-fx-effect: dropshadow(gaussian, " + ACCENT_GOLD + ", 10, 0, 0, 2);"
        );
    }
    
    /**
     * Style a title label
     */
    public static void styleTitle(Label label) {
        label.setStyle(
            "-fx-font-size: " + FONT_SIZE_TITLE + "px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: " + TEXT_PRIMARY + ";"
        );
    }
    
    /**
     * Style a subtitle label
     */
    public static void styleSubtitle(Label label) {
        label.setStyle(
            "-fx-font-size: " + FONT_SIZE_SUBTITLE + "px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: " + TEXT_ACCENT + ";"
        );
    }
    
    /**
     * Style a section header label
     */
    public static void styleHeader(Label label) {
        label.setStyle(
            "-fx-font-size: " + FONT_SIZE_HEADER + "px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: " + TEXT_PRIMARY + ";"
        );
    }
    
    /**
     * Style body text label
     */
    public static void styleBodyText(Label label) {
        label.setStyle(
            "-fx-font-size: " + FONT_SIZE_BODY + "px; " +
            "-fx-text-fill: " + TEXT_SECONDARY + ";"
        );
    }
    
    /**
     * Style muted/secondary text label
     */
    public static void styleMutedText(Label label) {
        label.setStyle(
            "-fx-font-size: " + FONT_SIZE_SMALL + "px; " +
            "-fx-text-fill: " + TEXT_MUTED + ";"
        );
    }
    
    /**
     * Style accent text label
     */
    public static void styleAccentText(Label label) {
        label.setStyle(
            "-fx-font-size: " + FONT_SIZE_BODY + "px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: " + TEXT_ACCENT + ";"
        );
    }
    
    /**
     * Style a primary button
     */
    public static void stylePrimaryButton(Button button) {
        button.setPrefHeight(BUTTON_HEIGHT);
        button.setMinWidth(BUTTON_MIN_WIDTH);
        button.setStyle(
            "-fx-background-color: " + ACCENT_GOLD + "; " +
            "-fx-text-fill: " + PRIMARY_DARK + "; " +
            "-fx-font-size: " + FONT_SIZE_BODY + "px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: " + BORDER_RADIUS + "px; " +
            "-fx-border-radius: " + BORDER_RADIUS + "px; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(gaussian, " + SHADOW_COLOR + ", 4, 0, 0, 1);"
        );
        
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle().replace(
                "-fx-background-color: " + ACCENT_GOLD,
                "-fx-background-color: " + ACCENT_GOLD_LIGHT
            ));
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace(
                "-fx-background-color: " + ACCENT_GOLD_LIGHT,
                "-fx-background-color: " + ACCENT_GOLD
            ));
        });
    }
    
    /**
     * Style a secondary button
     */
    public static void styleSecondaryButton(Button button) {
        button.setPrefHeight(BUTTON_HEIGHT);
        button.setMinWidth(BUTTON_MIN_WIDTH);
        button.setStyle(
            "-fx-background-color: " + SECONDARY_DARK + "; " +
            "-fx-text-fill: " + TEXT_PRIMARY + "; " +
            "-fx-font-size: " + FONT_SIZE_BODY + "px; " +
            "-fx-background-radius: " + BORDER_RADIUS + "px; " +
            "-fx-border-color: " + BORDER_PRIMARY + "; " +
            "-fx-border-width: " + BORDER_WIDTH + "px; " +
            "-fx-border-radius: " + BORDER_RADIUS + "px; " +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle().replace(
                "-fx-background-color: " + SECONDARY_DARK,
                "-fx-background-color: " + BACKGROUND_HOVER
            ));
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace(
                "-fx-background-color: " + BACKGROUND_HOVER,
                "-fx-background-color: " + SECONDARY_DARK
            ));
        });
    }
    
    /**
     * Style a warning/caution button
     */
    public static void styleWarningButton(Button button) {
        button.setPrefHeight(BUTTON_HEIGHT);
        button.setMinWidth(BUTTON_MIN_WIDTH);
        button.setStyle(
            "-fx-background-color: " + WARNING_COLOR + "; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: " + FONT_SIZE_BODY + "px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: " + BORDER_RADIUS + "px; " +
            "-fx-border-radius: " + BORDER_RADIUS + "px; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(gaussian, " + SHADOW_COLOR + ", 4, 0, 0, 1);"
        );
        
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle().replace(
                "-fx-background-color: " + WARNING_COLOR,
                "-fx-background-color: #e67e22"
            ));
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace(
                "-fx-background-color: #e67e22",
                "-fx-background-color: " + WARNING_COLOR
            ));
        });
    }
    
    /**
     * Style a danger/destructive button
     */
    public static void styleDangerButton(Button button) {
        button.setPrefHeight(BUTTON_HEIGHT);
        button.setMinWidth(BUTTON_MIN_WIDTH);
        button.setStyle(
            "-fx-background-color: " + ERROR_COLOR + "; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: " + FONT_SIZE_BODY + "px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: " + BORDER_RADIUS + "px; " +
            "-fx-border-radius: " + BORDER_RADIUS + "px; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(gaussian, " + SHADOW_COLOR + ", 4, 0, 0, 1);"
        );
        
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle().replace(
                "-fx-background-color: " + ERROR_COLOR,
                "-fx-background-color: #c0392b"
            ));
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace(
                "-fx-background-color: #c0392b",
                "-fx-background-color: " + ERROR_COLOR
            ));
        });
    }
    
    /**
     * Style a text area for mystical theme
     */
    public static void styleTextArea(TextArea textArea) {
        textArea.setStyle(
            "-fx-background-color: " + BACKGROUND_INPUT + "; " +
            "-fx-control-inner-background: " + BACKGROUND_INPUT + "; " +
            "-fx-text-fill: " + TEXT_SECONDARY + "; " +
            "-fx-font-size: " + FONT_SIZE_BODY + "px; " +
            "-fx-border-color: " + BORDER_PRIMARY + "; " +
            "-fx-border-width: " + BORDER_WIDTH + "px; " +
            "-fx-border-radius: " + BORDER_RADIUS + "px; " +
            "-fx-background-radius: " + BORDER_RADIUS + "px; " +
            "-fx-focus-color: " + ACCENT_GOLD + "; " +
            "-fx-faint-focus-color: transparent;"
        );
    }
    
    /**
     * Get global CSS that applies theme-wide styling
     */
    public static String getGlobalCSS() {
        return """
            .root {
                -fx-font-family: 'Segoe UI', 'Arial', sans-serif;
                -fx-base: %s;
                -fx-background: %s;
                -fx-control-inner-background: %s;
                -fx-accent: %s;
                -fx-default-button: %s;
                -fx-focus-color: %s;
                -fx-faint-focus-color: transparent;
            }
            
            .scroll-pane {
                -fx-background: transparent;
                -fx-background-color: transparent;
            }
            
            .scroll-pane .viewport {
                -fx-background-color: transparent;
            }
            
            .scroll-pane .content {
                -fx-background-color: transparent;
            }
            
            .scroll-bar {
                -fx-background-color: %s;
            }
            
            .scroll-bar .track {
                -fx-background-color: %s;
            }
            
            .scroll-bar .thumb {
                -fx-background-color: %s;
                -fx-background-radius: 3px;
            }
            
            .scroll-bar .thumb:hover {
                -fx-background-color: %s;
            }
            
            .text-field {
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-border-radius: %spx;
                -fx-background-radius: %spx;
                -fx-text-fill: %s;
                -fx-prompt-text-fill: %s;
                -fx-font-size: %spx;
            }
            
            .text-field:focused {
                -fx-border-color: %s;
                -fx-effect: dropshadow(gaussian, %s, 4, 0, 0, 0);
            }
            
            .list-view {
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-border-radius: %spx;
                -fx-background-radius: %spx;
            }
            
            .list-cell {
                -fx-background-color: transparent;
                -fx-text-fill: %s;
                -fx-border-color: transparent transparent %s transparent;
                -fx-border-width: 0 0 1px 0;
                -fx-padding: %spx;
            }
            
            .list-cell:selected {
                -fx-background-color: %s;
                -fx-text-fill: %s;
                -fx-background-radius: %spx;
            }
            
            .list-cell:hover {
                -fx-background-color: %s;
            }
            
            .alert {
                -fx-background-color: %s;
            }
            
            .alert .header-panel {
                -fx-background-color: %s;
            }
            
            .alert .content {
                -fx-background-color: %s;
            }
            
            .alert .label {
                -fx-text-fill: %s;
            }
            """.formatted(
                // Root styles
                SECONDARY_DARK, BACKGROUND_PRIMARY, BACKGROUND_INPUT, ACCENT_GOLD, ACCENT_GOLD, ACCENT_GOLD,
                // Scroll bar styles
                BACKGROUND_CARD, SECONDARY_DARK, BORDER_PRIMARY, ACCENT_GOLD,
                // Text field styles
                BACKGROUND_INPUT, BORDER_PRIMARY, BORDER_RADIUS, BORDER_RADIUS, TEXT_SECONDARY, TEXT_MUTED, FONT_SIZE_BODY,
                // Text field focused
                ACCENT_GOLD, ACCENT_GOLD,
                // List view styles
                BACKGROUND_CARD, BORDER_PRIMARY, BORDER_RADIUS, BORDER_RADIUS,
                // List cell styles
                TEXT_SECONDARY, BORDER_PRIMARY, SPACING_SMALL,
                // List cell selected/hover
                ACCENT_GOLD_DARK, PRIMARY_DARK, BORDER_RADIUS, BACKGROUND_HOVER,
                // Alert styles
                BACKGROUND_CARD, SECONDARY_DARK, BACKGROUND_CARD, TEXT_PRIMARY
            );
    }
    
    /**
     * Get standard window insets/padding
     */
    public static javafx.geometry.Insets getStandardInsets() {
        return new javafx.geometry.Insets(SPACING_MEDIUM);
    }
    
    /**
     * Get large window insets/padding  
     */
    public static javafx.geometry.Insets getLargeInsets() {
        return new javafx.geometry.Insets(SPACING_LARGE);
    }
    
    /**
     * Get small window insets/padding
     */
    public static javafx.geometry.Insets getSmallInsets() {
        return new javafx.geometry.Insets(SPACING_SMALL);
    }
}
