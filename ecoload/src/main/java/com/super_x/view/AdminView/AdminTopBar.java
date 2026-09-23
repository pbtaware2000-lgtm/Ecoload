package com.super_x.view.AdminView;

import com.super_x.NavigationService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.shape.SVGPath;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AdminTopBar {

        private static final String GREEN = "#123D2A";
        private static final String GRAY = "#697278";
        private static final String BORDER = "#D8E5E0";

        // =========================================================
        // SHARED PROFILE DATA
        // =========================================================

        private static Image currentProfileImage;

        private static String adminName = "Admin Profile";
        private static String adminRole = "System Administrator";
        private static String adminEmail = "admin@ecoload.ai";

        // =========================================================
        // ALL NAVBAR PROFILE IMAGE VIEWS
        // =========================================================

        /*
         * Every time a navbar is created, its ImageView is stored here.
         * When the profile image changes, all navbar ImageViews are updated.
         */
        private static final List<ImageView> navbarImageViews = new ArrayList<>();

        // =========================================================
        // NAVIGATION CALLBACKS
        // =========================================================

        private Runnable onProfile;
        private Runnable onSOSAlerts;

        // =========================================================
        // TOP BAR CONTROLS
        // =========================================================

        private Label navbarNameLabel;
        private Label navbarRoleLabel;
        private Label alertCountBadge;

        private AdminTopBar() {
        }

        // =========================================================
        // CREATE
        // =========================================================

        public static AdminTopBar create() {
                return new AdminTopBar();
        }

        public static AdminTopBar create(
                        NavigationService navigationService) {

                AdminTopBar topBar = new AdminTopBar();

                if (navigationService != null) {

                        topBar.setOnProfile(
                                        () -> navigationService.navigate("profile"));

                        topBar.setOnSOSAlerts(
                                        navigationService::toggleSOSAlerts);
                }

                return topBar;
        }

        // =========================================================
        // BUILD TOP BAR
        // =========================================================

        public HBox build() {

                HBox topBar = new HBox(18);

                topBar.setPrefHeight(84);
                topBar.setMinHeight(80);
                topBar.setMaxHeight(90);

                topBar.setPadding(
                                new Insets(10, 25, 10, 25));

                topBar.setAlignment(
                                Pos.CENTER_LEFT);

                topBar.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: " + BORDER + ";" +
                                                "-fx-border-width: 0 0 1 0;");

                // =====================================================
                // WELCOME MESSAGE
                // =====================================================

                VBox welcomeBox = new VBox(2);

                welcomeBox.setAlignment(
                                Pos.CENTER_LEFT);

                // =====================================================
                // WELCOME TITLE
                // =====================================================

                Label welcomeLabel = new Label("Welcome, Admin");

                welcomeLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                20));

                welcomeLabel.setTextFill(
                                Color.web("#075A24"));

                // =====================================================
                // WELCOME DESCRIPTION
                // =====================================================

                Label welcomeDescription = new Label(
                                "Here's what's happening with EcoLoad today.");

                welcomeDescription.setFont(
                                Font.font(
                                                "Arial",
                                                12));

                welcomeDescription.setTextFill(
                                Color.web("#7FAF96"));

                welcomeBox.getChildren().addAll(
                                welcomeLabel,
                                welcomeDescription);

                // =====================================================
                // SPACER
                // =====================================================

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                // =====================================================
                // SOS BUTTON
                // =====================================================

                // SOS BELL ICON
                SVGPath bellIcon = new SVGPath();

                bellIcon.setContent(
                                "M12 22c1.1 0 1.99-.9 1.99-2h-3.98" +
                                                "c0 1.1.89 2 1.99 2z" +
                                                "M18 16v-5c0-3.07-1.63-5.64-4.5-6.32" +
                                                "V4c0-.83-.67-1.5-1.5-1.5" +
                                                "S10.5 3.17 10.5 4v.68" +
                                                "C7.64 5.36 6 7.92 6 11v5l-2 2v1h16v-1z");

                bellIcon.setFill(Color.web("#C62828"));
                bellIcon.setScaleX(0.9);
                bellIcon.setScaleY(0.9);

                // Bell container
                StackPane bellGraphic = new StackPane();
                bellGraphic.setPrefSize(24, 24);

                // Add bell
                bellGraphic.getChildren().add(bellIcon);

                // Notification badge
                alertCountBadge = new Label("0");

                alertCountBadge.setMinSize(18, 18);
                alertCountBadge.setPrefSize(18, 18);
                alertCountBadge.setMaxSize(18, 18);

                alertCountBadge.setAlignment(Pos.CENTER);

                alertCountBadge.setStyle(
                                "-fx-background-color: #D32F2F;" +
                                                "-fx-background-radius: 50%;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 10px;" +
                                                "-fx-font-weight: bold;");

                // Position badge at top-right of bell
                StackPane.setAlignment(alertCountBadge, Pos.TOP_RIGHT);
                alertCountBadge.setTranslateX(6);
                alertCountBadge.setTranslateY(-5);

                bellGraphic.getChildren().add(alertCountBadge);

                // Button
                Button alertButton = new Button();
                alertButton.setGraphic(bellGraphic);

                alertButton.setTooltip(
                                new Tooltip("SOS Alerts"));
                alertButton.setPrefSize(48, 44);
                alertButton.setMinSize(48, 44);

                setAlertNormalStyle(alertButton);

                alertButton.setOnMouseEntered(e -> setAlertHoverStyle(alertButton));

                alertButton.setOnMouseExited(e -> setAlertNormalStyle(alertButton));

                alertButton.setOnAction(e -> {

                        if (onSOSAlerts != null) {
                                onSOSAlerts.run();
                        }
                });

                // =====================================================
                // PROFILE TEXT
                // =====================================================

                VBox profileText = new VBox(1);

                profileText.setAlignment(
                                Pos.CENTER_RIGHT);

                navbarNameLabel = new Label(
                                adminName);

                navbarNameLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                14));

                navbarNameLabel.setTextFill(
                                Color.web(GREEN));

                navbarRoleLabel = new Label(
                                adminRole);

                navbarRoleLabel.setFont(
                                Font.font(
                                                "Arial",
                                                11));

                navbarRoleLabel.setTextFill(
                                Color.web(GRAY));

                profileText.getChildren().addAll(
                                navbarNameLabel,
                                navbarRoleLabel);

                // =====================================================
                // PROFILE BUTTON
                // =====================================================

                Button profileButton = createProfileButton();

                profileButton.setOnAction(e -> {

                        if (onProfile != null) {
                                onProfile.run();
                        }
                });

                // =====================================================
                // ADD TO TOP BAR
                // =====================================================

                topBar.getChildren().addAll(
                                welcomeBox,
                                spacer,
                                alertButton,
                                profileText,
                                profileButton);

                // =====================================================
                // MARGINS
                // =====================================================

                HBox.setMargin(
                                alertButton,
                                new Insets(0, 4, 0, 0));

                HBox.setMargin(
                                profileText,
                                new Insets(0, 2, 0, 0));

                return topBar;
        }

        public void setSOSAlertCount(int count) {

    if (alertCountBadge == null) {
        return;
    }

    if (count <= 0) {
        alertCountBadge.setVisible(false);
        return;
    }

    alertCountBadge.setVisible(true);

    if (count > 99) {
        alertCountBadge.setText("99+");
        alertCountBadge.setMinSize(24, 18);
        alertCountBadge.setPrefSize(24, 18);
        alertCountBadge.setMaxSize(24, 18);
    } else {
        alertCountBadge.setText(String.valueOf(count));
        alertCountBadge.setMinSize(18, 18);
        alertCountBadge.setPrefSize(18, 18);
        alertCountBadge.setMaxSize(18, 18);
    }
}

        // =========================================================
        // CREATE PROFILE BUTTON
        // =========================================================

        private Button createProfileButton() {

                Button button = new Button();

                button.setPrefSize(44, 44);
                button.setMinSize(44, 44);
                button.setMaxSize(44, 44);

                button.setPadding(
                                Insets.EMPTY);

                setProfileButtonNormalStyle(button);

                StackPane stack = new StackPane();

                // =====================================================
                // FALLBACK
                // =====================================================

                Circle fallback = new Circle(
                                21,
                                Color.web("#DDEBE3"));

                Label person = new Label("👤");

                person.setFont(
                                Font.font(18));

                stack.getChildren().addAll(
                                fallback,
                                person);

                // =====================================================
                // IMAGE VIEW
                // =====================================================

                ImageView navbarImageView = new ImageView();

                navbarImageView.setFitWidth(42);
                navbarImageView.setFitHeight(42);

                navbarImageView.setPreserveRatio(false);
                navbarImageView.setSmooth(true);

                Circle clip = new Circle(
                                21,
                                21,
                                21);

                navbarImageView.setClip(clip);

                /*
                 * IMPORTANT:
                 * Store this navbar ImageView.
                 * When profile image changes, this ImageView will update.
                 */
                navbarImageViews.add(
                                navbarImageView);

                // =====================================================
                // CURRENT PROFILE IMAGE
                // =====================================================

                Image image = getCurrentProfileImage();

                if (image != null) {

                        stack.getChildren().clear();

                        navbarImageView.setImage(
                                        image);

                        stack.getChildren().add(
                                        navbarImageView);

                } else {

                        // =================================================
                        // DEFAULT RESOURCE IMAGE
                        // =================================================

                        InputStream stream = AdminTopBar.class.getResourceAsStream(
                                        "/assets/images/Admin_Profile_Logo.png");

                        if (stream != null) {

                                try {

                                        Image defaultImage = new Image(stream);

                                        currentProfileImage = defaultImage;

                                        stack.getChildren().clear();

                                        navbarImageView.setImage(
                                                        defaultImage);

                                        stack.getChildren().add(
                                                        navbarImageView);

                                } catch (Exception ignored) {

                                        System.out.println(
                                                        "Unable to load default profile image.");
                                }

                        } else {

                                System.out.println(
                                                "Default profile image not found: " +
                                                                "/assets/Admin_Profile_Logo.png");
                        }
                }

                button.setGraphic(stack);

                button.setTooltip(
                                new Tooltip("Open Admin Profile"));

                button.setOnMouseEntered(e -> setProfileButtonHoverStyle(button));

                button.setOnMouseExited(e -> setProfileButtonNormalStyle(button));

                return button;
        }

        // =========================================================
        // PROFILE BUTTON STYLE
        // =========================================================

        private void setProfileButtonNormalStyle(
                        Button button) {

                button.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-background-radius: 50%;" +
                                                "-fx-border-color: transparent;" +
                                                "-fx-cursor: hand;");
        }

        private void setProfileButtonHoverStyle(
                        Button button) {

                button.setStyle(
                                "-fx-background-color: #E8F3EE;" +
                                                "-fx-background-radius: 50%;" +
                                                "-fx-border-color: #B7D6C6;" +
                                                "-fx-border-radius: 50%;" +
                                                "-fx-cursor: hand;");
        }

        // =========================================================
        // ALERT BUTTON STYLE
        // =========================================================

        private void setAlertNormalStyle(
                        Button button) {

                button.setStyle(
                                "-fx-background-color: #FFF0F0;" +
                                                "-fx-background-radius: 22;" +
                                                "-fx-border-color: #F2B8B8;" +
                                                "-fx-border-radius: 22;" +
                                                "-fx-cursor: hand;");
        }

        private void setAlertHoverStyle(
                        Button button) {

                button.setStyle(
                                "-fx-background-color: #FFE1E1;" +
                                                "-fx-background-radius: 22;" +
                                                "-fx-border-color: #E89A9A;" +
                                                "-fx-border-radius: 22;" +
                                                "-fx-cursor: hand;");
        }

        // =========================================================
        // UPDATE PROFILE IMAGE
        // =========================================================

        public static void updateProfileImage(
                        Image image) {

                if (image == null) {
                        return;
                }

                // =====================================================
                // SAVE IMAGE GLOBALLY
                // =====================================================

                currentProfileImage = image;

                // =====================================================
                // UPDATE ALL EXISTING NAVBAR IMAGES
                // =====================================================

                for (ImageView imageView : navbarImageViews) {

                        if (imageView != null) {

                                imageView.setImage(
                                                image);
                        }
                }

                System.out.println(
                                "All navbar profile images updated.");
        }

        // =========================================================
        // GET PROFILE IMAGE
        // =========================================================

        public static Image getCurrentProfileImage() {

                return currentProfileImage;
        }

        // =========================================================
        // UPDATE PROFILE INFORMATION
        // =========================================================

        public static void updateProfileInfo(
                        String name,
                        String role,
                        String email) {

                if (name != null &&
                                !name.trim().isEmpty()) {

                        adminName = name.trim();
                }

                if (role != null &&
                                !role.trim().isEmpty()) {

                        adminRole = role.trim();
                }

                if (email != null &&
                                !email.trim().isEmpty()) {

                        adminEmail = email.trim();
                }
        }

        // =========================================================
        // GETTERS
        // =========================================================

        public static String getAdminName() {

                return adminName;
        }

        public static String getAdminRole() {

                return adminRole;
        }

        public static String getAdminEmail() {

                return adminEmail;
        }

        // =========================================================
        // CALLBACKS
        // =========================================================

        public void setOnProfile(
                        Runnable action) {

                this.onProfile = action;
        }

        public void setOnSOSAlerts(
                        Runnable action) {

                this.onSOSAlerts = action;
        }
}