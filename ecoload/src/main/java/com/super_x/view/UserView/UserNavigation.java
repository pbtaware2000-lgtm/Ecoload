package com.super_x.view.UserView;

import com.super_x.model.usermodel.CurrentUser;
import com.super_x.model.usermodel.UserModel;
import com.super_x.view.HomePage;
import com.super_x.view.Login;
import com.super_x.view.Chatbot.EcoloadAssistantOverlay;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Shared navigation for Transport screens.
 */
public final class UserNavigation {

    private static final String GREEN = "#014B3A";
    private static final String BORDER = "#075F49";

    private UserNavigation() {
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    public static VBox createSidebar(String activePage) {

        VBox sidebar = new VBox();

        sidebar.setPrefWidth(280);
        sidebar.setMinWidth(280);
        sidebar.setMaxWidth(280);
        sidebar.setMaxHeight(Double.MAX_VALUE);

        sidebar.setPadding(new Insets(18, 18, 18, 18));
        sidebar.setSpacing(10);

        sidebar.setStyle(
                "-fx-background-color: " + GREEN + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-width: 0 1 0 0;");

        // =====================================================
        // ECOLOAD LOGO
        // =====================================================

        ImageView logo = new ImageView(
                new Image(
                        UserNavigation.class.getResourceAsStream(
                                "/assets/icons/EcoloadlogoUser.png")));

        logo.setFitWidth(225);
        logo.setFitHeight(200);
        logo.setPreserveRatio(true);

        HBox logoBox = new HBox();
        logoBox.setAlignment(Pos.CENTER);
        logoBox.getChildren().add(logo);
        logoBox.setPadding(new Insets(0, 0, 0, 0));

        // =====================================================
        // MENU
        // =====================================================

        VBox menu = new VBox(3);

        menu.getChildren().addAll(

                menuButton("🏠", "Dashboard", activePage),

                menuButton("➕", "Post Load", activePage),

                menuButton("📦", "My Loads", activePage),

                menuButton("👤", "Matched Drivers", activePage),

                menuButton("📍", "Trip Tracking", activePage),

                menuButton("📊", "Analytics", activePage),

                menuButton("🌱", "Eco Impact", activePage),

                menuButton("🤖", "Smart Load Advisor", activePage),

                menuButton("💬", "Ecoload Assistant", activePage),

                menuButton("⭐", "Rating & Review", activePage),

                menuButton("❓", "Support", activePage));

        // =====================================================
        // SPACER
        // =====================================================

        Region spacer = new Region();

        // The former assistant sidebar route is intentionally removed; the
        // floating launcher is available on every dashboard screen instead.
        menu.getChildren().removeIf(node -> node instanceof Button button
                && button.getGraphic() instanceof HBox graphic
                && graphic.getChildren().stream().anyMatch(child -> child instanceof Label label
                && "Ecoload Assistant".equals(label.getText())));

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS);

        // =====================================================
        // LOGOUT
        // =====================================================

        Button logout = new Button("↪  Logout");

        logout.setAlignment(Pos.CENTER);
        logout.setPadding(new Insets(15, 20, 15, 20));
        logout.setMaxWidth(Double.MAX_VALUE);
        logout.setCursor(Cursor.HAND);

        logout.setStyle(
                "-fx-background-color: rgb(234, 63, 63);" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-alignment: CENTER_LEFT;" +
                        "-fx-padding: 12px 18px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-cursor: hand;");

        logout.setOnMouseEntered(e -> logout.setStyle(
                "-fx-background-color: #ff0000;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-alignment: CENTER_LEFT;" +
                        "-fx-padding: 12px 18px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-cursor: hand;"));

        logout.setOnMouseExited(e -> logout.setStyle(
                "-fx-background-color: rgb(234, 64, 64);" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-alignment: CENTER_LEFT;" +
                        "-fx-padding: 12px 18px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-cursor: hand;"));

        logout.setOnAction(event -> {
            // Keep this if RolePage already exists
            HomePage.homeStage.setScene(
                    new Login().getScene());
        });

        // =====================================================
        // ADD EVERYTHING
        // =====================================================

        sidebar.getChildren().addAll(
                logoBox,
                menu,
                spacer,
                logout);

        EcoloadAssistantOverlay.installForTransporter();

        return sidebar;
    }

    // =========================================================
    // NAVBAR
    // Vehicle Name + Vehicle Number REMOVED
    // =========================================================

    public static HBox createNavbar() {

        HBox header = new HBox();

        header.setAlignment(Pos.CENTER_LEFT);

        header.setPadding(
                new Insets(0, 30, 0, 30));

        header.setPrefHeight(66);
        header.setMinHeight(66);
        header.setMaxHeight(66);

        header.setMaxWidth(
                Double.MAX_VALUE);

        header.setSpacing(20);

        header.setStyle(
                "-fx-background-color: #F8FAFC;" +
                        "-fx-border-color: #E2E8F0;" +
                        "-fx-border-width: 0 0 1 0;");

        // =====================================================
        // WELCOME TEXT
        // =====================================================

        UserModel user = CurrentUser.getInstance().getUser();
        Label welcome = new Label("Welcome Back, " + user.getUsername());

        welcome.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #014B3A;");

        // =====================================================
        // SPACER
        // =====================================================

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        // =====================================================
        // PROFILE
        // =====================================================

        Circle profile = new Circle(
                21,
                Color.web("#D8F5DA"));

        Label initials = new Label(getInitials(user.getUsername()));

        initials.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + GREEN + ";");

        StackPane profileBox = new StackPane(
                profile,
                initials);

        profileBox.setPrefSize(42, 42);
        profileBox.setMinSize(42, 42);
        profileBox.setMaxSize(42, 42);

        profileBox.setCursor(
                Cursor.HAND);

        // Hover

        profileBox.setOnMouseEntered(event -> profile.setFill(
                Color.web("#B9EDBD")));

        profileBox.setOnMouseExited(event -> profile.setFill(
                Color.web("#D8F5DA")));

        // =====================================================
        // PROFILE CLICK
        // =====================================================

        profileBox.setOnMouseClicked(event -> {
            Profile profile1 = new Profile();
            HomePage.homeStage.setScene(profile1.getProfileScene());

            System.out.println("Profile clicked");

        });

        // =====================================================
        // ADD EVERYTHING
        // =====================================================

        header.getChildren().addAll(
                welcome,
                spacer,
                new Separator(Orientation.VERTICAL),
                profileBox);

        return header;
    }

    // =========================================================
    // MENU BUTTON
    // =========================================================

    private static Button menuButton(
        String icon,
        String text,
        String activePage) {

    boolean selected = text.equals(activePage);

    Button button = new Button();

    button.setGraphic(
            menuGraphic(
                    icon,
                    text,
                    selected));

    button.setAlignment(Pos.CENTER_LEFT);
    button.setPadding(new Insets(8, 14, 8, 16));
    button.setMaxWidth(Double.MAX_VALUE);
    button.setCursor(Cursor.HAND);

    // ==============================
    // NORMAL / SELECTED STYLE
    // ==============================

    String normalStyle;

    if (selected) {

        normalStyle =
                "-fx-background-color: #075F49;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: white;";

    } else {

        normalStyle =
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: white;";
    }

    button.setStyle(normalStyle);

    // ==============================
    // HOVER
    // ==============================

    button.setOnMouseEntered(event -> {

        if (selected) {

            // Keep selected button color
            button.setStyle(
                    "-fx-background-color: #075F49;" +
                    "-fx-background-radius: 10;" +
                    "-fx-text-fill: white;");

        } else {

            // Hover color for unselected button
            button.setStyle(
                    "-fx-background-color: #075F49;" +
                    "-fx-background-radius: 10;" +
                    "-fx-text-fill: white;");
        }
    });

    button.setOnMouseExited(event -> {

        // Always restore the correct state
        button.setStyle(normalStyle);
    });

    // ==============================
    // CLICK
    // ==============================

    button.setOnAction(event -> navigate(text));

    return button;
}

    // =========================================================
    // MENU GRAPHIC
    // =========================================================

    private static HBox menuGraphic(
            String icon,
            String text,
            boolean selected) {

        Label iconLabel = new Label(
                icon);

        iconLabel.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-text-fill: #FFFFFF;");

        Label textLabel = new Label(
                text);

        textLabel.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: " +
                        (selected ? "bold" : "normal") + ";" +
                        "-fx-text-fill: #FFFFFF;");

        return new HBox(
                10,
                iconLabel,
                textLabel);
    }

    private static String getInitials(String name) {

        if (name == null || name.trim().isEmpty()) {
            return "U";
        }

        String[] parts = name.trim().split("\\s+");

        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }

        return (parts[0].substring(0, 1) +
                parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }

    // =========================================================
    // NAVIGATION
    // =========================================================

    private static void navigate(String page) {

        Scene scene;

        switch (page) {

            case "Dashboard":
                scene = new UserDashboard().getTransporterDashboardScene();
                break;

            case "My Loads":
                scene = new MyLoads().getMyLoadsScene();
                break;

            case "Post Load":
                scene = new PostLoad().getpostloadScene();
                break;

            case "Matched Drivers":
                scene = new MatchedDrivers().getMatchedDriversScene();
                break;

            case "Trip Tracking":
                scene = new TripTracking().getTripTrackingScene();
                break;

            case "Analytics":
                scene = new Analytics().getAnalyticsScene();
                break;

            case "Eco Impact":
                scene = new EcoImpact().getEcoImpactScene();
                break;

            case "Smart Load Advisor":
                scene = new SmartLoadAdvisor().getSmartLoadAdvisorScene();
                break;

            case "Rating & Review":
                scene = new Ratings().getRatingsPageScene();
                break;

            case "Support":
                scene = new Support().getSupportPageScene();
                break;

            default:
                scene = new UserDashboard().getTransporterDashboardScene();
                break;
        }

        HomePage.homeStage.setScene(scene);
        HomePage.homeStage.show();
    }
}
