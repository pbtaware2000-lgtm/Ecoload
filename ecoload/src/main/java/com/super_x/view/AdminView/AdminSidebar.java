package com.super_x.view.AdminView;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Reusable admin sidebar component for all admin pages.
 * Provides navigation buttons and branded logo area.
 * All navigation is handled through Runnable callbacks.
 */
public class AdminSidebar {

    private static final String DARK_GREEN = "#004B3A";
    private static final String HOVER_GREEN = "#075E49";
    private static final String CLICK_GREEN = "#075E49";

    private VBox sidebar;
    private Button activeMenuButton;
    private final Map<String, Button> menuButtons = new HashMap<>();

    // Navigation callbacks
    private Runnable onDashboard;
    private Runnable onDrivers;
    private Runnable onPendingDrivers;
    private Runnable onLoadsTrips;
    private Runnable onReports;
    private Runnable onReviews;
    private Runnable onSupport;
    private Runnable onLogout;

    /**
     * Creates and returns the sidebar VBox.
     */
    public VBox createSidebar() {
        menuButtons.clear();

        sidebar = new VBox();
        sidebar.setMinWidth(250);
        sidebar.setPrefWidth(250);
        sidebar.setMaxWidth(250);
        sidebar.setPadding(new Insets(25, 16, 20, 16));
        sidebar.setStyle("-fx-background-color: " + DARK_GREEN + ";");

        // Logo section
        sidebar.getChildren().add(createLogoBox());

        // Menu section
        VBox menu = createMenuSection();
        sidebar.getChildren().add(menu);

        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().add(spacer);

        // Logout button
        Button logoutButton = createMenuButton("↪", "Logout", false);
        logoutButton.setStyle(
            "-fx-background-color: #B42335;" +
            "-fx-background-radius: 9;"
        );
        logoutButton.setOnMouseEntered(e ->
            logoutButton.setStyle(
                "-fx-background-color: #921E2D;" +
                "-fx-background-radius: 9;"
            )
        );
        logoutButton.setOnMouseExited(e ->
            logoutButton.setStyle(
                "-fx-background-color: #B42335;" +
                "-fx-background-radius: 9;"
            )
        );
        logoutButton.setOnAction(event -> {
            if (onLogout != null) {
                onLogout.run();
            }
        });

        sidebar.getChildren().add(logoutButton);

        return sidebar;
    }

    /**
     * Creates the logo/branding section.
     */
    private javafx.scene.layout.HBox createLogoBox() {
        javafx.scene.layout.HBox logoBox = new javafx.scene.layout.HBox(12);
        logoBox.setAlignment(Pos.CENTER_LEFT);

        ImageView logoView = new ImageView();
        java.io.InputStream logoStream = getClass().getResourceAsStream("/assets/images/Logo-removebg-preview.png");
        if (logoStream != null) {
            try {
                Image logoImage = new Image(logoStream);
                logoView.setImage(logoImage);
                logoView.setFitWidth(70);
                logoView.setFitHeight(70);
                logoView.setPreserveRatio(true);
                logoView.setSmooth(true);
            } catch (Exception e) {
                System.out.println("ERROR loading logo: " + e.getMessage());
            }
        }

        VBox logoText = new VBox(1);
        logoText.setAlignment(Pos.CENTER_LEFT);

        Label ecoLoad = new Label("EcoLoad");
        ecoLoad.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        ecoLoad.setTextFill(Color.WHITE);

        Label precision = new Label("PRECISION LOGISTICS");
        precision.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        precision.setTextFill(Color.web("#A7C7BC"));

        logoText.getChildren().addAll(ecoLoad, precision);
        logoBox.getChildren().addAll(logoView, logoText);

        return logoBox;
    }

    /**
     * Creates the menu buttons section.
     */
    private VBox createMenuSection() {
        VBox menu = new VBox(5);
        menu.setPadding(new Insets(45, 0, 0, 0));
        menu.setFillWidth(true);

        Button dashboardButton = createMenuButton("▦", "Dashboard", true);
        dashboardButton.setOnAction(event -> {
            setActiveButton(dashboardButton);
            if (onDashboard != null) onDashboard.run();
        });
        menuButtons.put("dashboard", dashboardButton);

        Button driverButton = createMenuButton("▰", "Drivers & Users", false);
        driverButton.setOnAction(event -> {
            setActiveButton(driverButton);
            if (onDrivers != null) onDrivers.run();
        });
        menuButtons.put("drivers", driverButton);

        Button pendingDriversButton = createMenuButton("↳", "Pending Drivers", false);
        pendingDriversButton.setOnAction(event -> {
            setActiveButton(pendingDriversButton);
            if (onPendingDrivers != null) onPendingDrivers.run();
        });
        menuButtons.put("pendingDrivers", pendingDriversButton);

        Button loadsButton = createMenuButton("♧", "Loads & Trips", false);
        loadsButton.setOnAction(event -> {
            setActiveButton(loadsButton);
            if (onLoadsTrips != null) onLoadsTrips.run();
        });
        menuButtons.put("loads", loadsButton);

        Button analyticsButton = createMenuButton("▥", "Analytics", false);
        analyticsButton.setOnAction(event -> {
            setActiveButton(analyticsButton);
            if (onReports != null) onReports.run();
        });
        menuButtons.put("reports", analyticsButton);

        Button reviewsButton = createMenuButton("★", "Reviews", false);
        reviewsButton.setOnAction(event -> {
            setActiveButton(reviewsButton);
            if (onReviews != null) onReviews.run();
        });
        menuButtons.put("reviews", reviewsButton);

        Button supportButton = createMenuButton("♧", "Support", false);
        supportButton.setOnAction(event -> {
            setActiveButton(supportButton);
            if (onSupport != null) onSupport.run();
        });
        menuButtons.put("support", supportButton);

        menu.getChildren().addAll(
            dashboardButton, driverButton, pendingDriversButton,
            loadsButton, analyticsButton, reviewsButton, supportButton
        );

        return menu;
    }

    /**
     * Creates a styled menu button.
     */
    private Button createMenuButton(String icon, String text, boolean active) {
        Button button = new Button();
        button.setPrefHeight(48);
        button.setMinHeight(44);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setCursor(javafx.scene.Cursor.HAND);

        Label iconLabel = new Label(icon);
        iconLabel.setPrefWidth(28);
        iconLabel.setFont(Font.font(20));
        iconLabel.setTextFill(Color.WHITE);

        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        textLabel.setTextFill(Color.WHITE);

        javafx.scene.layout.HBox box = new javafx.scene.layout.HBox(12, iconLabel, textLabel);
        box.setAlignment(Pos.CENTER_LEFT);
        button.setGraphic(box);

        if (active) {
            activeMenuButton = button;
        }

        applyMenuStyle(button, active);

        button.setOnMouseEntered(event -> {
            if (button != activeMenuButton) {
                button.setStyle(
                    "-fx-background-color: " + HOVER_GREEN + ";" +
                    "-fx-background-radius: 9;"
                );
            }
            ScaleTransition grow = new ScaleTransition(Duration.millis(120), button);
            grow.setToX(1.02);
            grow.setToY(1.02);
            grow.play();
        });

        button.setOnMouseExited(event -> {
            applyMenuStyle(button, button == activeMenuButton);
            ScaleTransition shrink = new ScaleTransition(Duration.millis(120), button);
            shrink.setToX(1.0);
            shrink.setToY(1.0);
            shrink.play();
        });

        return button;
    }

    /**
     * Sets button styling based on active state.
     */
    private void applyMenuStyle(Button button, boolean active) {
        if (active) {
            button.setStyle(
                "-fx-background-color: " + CLICK_GREEN + ";" +
                "-fx-background-radius: 9;" +
                "-fx-border-color: #72C4AA;" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 9;"
            );
        } else {
            button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 9;"
            );
        }
    }

    /**
     * Sets the active button.
     */
    private void setActiveButton(Button selectedButton) {
        if (activeMenuButton != null && activeMenuButton != selectedButton) {
            applyMenuStyle(activeMenuButton, false);
        }
        activeMenuButton = selectedButton;
        applyMenuStyle(activeMenuButton, true);
    }

    /**
     * Sets active page by key.
     */
    public void setActivePage(String pageName) {
        Button button = menuButtons.get(pageName);
        if (button != null) {
            setActiveButton(button);
        }
    }

    // Runnable setters for navigation callbacks
    public void setOnDashboard(Runnable action) { this.onDashboard = action; }
    public void setOnDrivers(Runnable action) { this.onDrivers = action; }
    public void setOnPendingDrivers(Runnable action) { this.onPendingDrivers = action; }
    public void setOnLoadsTrips(Runnable action) { this.onLoadsTrips = action; }
    public void setOnReports(Runnable action) { this.onReports = action; }
    public void setOnReviews(Runnable action) { this.onReviews = action; }
    public void setOnSupport(Runnable action) { this.onSupport = action; }
    public void setOnLogout(Runnable action) { this.onLogout = action; }
}
