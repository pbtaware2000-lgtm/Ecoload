package com.super_x.view.UserView;

import com.super_x.controller.usercontroller.CompletedTripController;
import com.super_x.controller.usercontroller.ReviewController;
import com.super_x.model.drivermodel.Trip;
import com.super_x.model.usermodel.CurrentUser;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class Ratings {

        // =========================================================
        // COLORS
        // =========================================================

        private static final String GREEN = "#0B6B2A";
        private static final String DARK_GREEN = "#075C48";
        private static final String LIGHT_GREEN = "#E9F8EC";
        private static final String BG = "#F4FAF6";
        private static final String BORDER = "#E3EAE5";
        private static final String TEXT_GRAY = "#6B716D";
        private static final String TEXT = "#18231D";
        private static final String GOLD = "#F4B400";

        private Trip completedTrip;
        private final ReviewController reviewController = new ReviewController();
        // =========================================================
        // MAIN SCENE
        // =========================================================

        public Scene getRatingsPageScene() {

                BorderPane root = new BorderPane();

                root.setStyle(
                                "-fx-background-color: " + BG + ";");

                // =====================================================
                // SIDEBAR
                // =====================================================

                root.setLeft(
                                UserNavigation.createSidebar("Rating & Review"));

                // =====================================================
                // MAIN PAGE
                // =====================================================

                BorderPane page = new BorderPane();

                page.setStyle(
                                "-fx-background-color: " + BG + ";");

                // =====================================================
                // NAVBAR
                // =====================================================

                page.setTop(
                                UserNavigation.createNavbar());

                // =====================================================
                // MAIN CONTENT
                // =====================================================

                VBox main = new VBox(22);

                main.setPadding(
                                new Insets(
                                                28,
                                                34,
                                                45,
                                                34));

                main.setFillWidth(true);

                main.setStyle(
                                "-fx-background-color: " + BG + ";");

                // =====================================================
                // PAGE HEADER
                // =====================================================

                Label title = new Label(
                                "Ratings & Reviews");

                title.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                25));

                title.setTextFill(
                                Color.web(TEXT));

                Label subtitle = new Label(
                                "Rate drivers for your completed trips and share your EcoLoad experience.");

                subtitle.setFont(
                                Font.font(
                                                "Arial",
                                                13));

                subtitle.setTextFill(
                                Color.web(TEXT_GRAY));

                VBox heading = new VBox(4);

                heading.getChildren().addAll(
                                title,
                                subtitle);

                // =====================================================
                // TAB BAR
                // =====================================================

                HBox tabBar = createReviewTabs();

                Button driverTab = (Button) tabBar.getChildren().get(0);

                Button appTab = (Button) tabBar.getChildren().get(1);

                // =====================================================
                // CONTENT CONTAINER
                // =====================================================

                StackPane contentContainer = new StackPane();

                VBox driverPage = createDriverReviewsPage();

                VBox appPage = createAppReviewsPage();

                driverPage.setVisible(true);
                driverPage.setManaged(true);

                appPage.setVisible(false);
                appPage.setManaged(false);

                contentContainer.getChildren().addAll(
                                driverPage,
                                appPage);

                // =====================================================
                // DRIVER TAB
                // =====================================================

                driverTab.setOnAction(e -> {

                        driverPage.setVisible(true);
                        driverPage.setManaged(true);

                        appPage.setVisible(false);
                        appPage.setManaged(false);

                        activateTab(
                                        driverTab,
                                        appTab);
                });

                // =====================================================
                // APP TAB
                // =====================================================

                appTab.setOnAction(e -> {

                        driverPage.setVisible(false);
                        driverPage.setManaged(false);

                        appPage.setVisible(true);
                        appPage.setManaged(true);

                        activateTab(
                                        appTab,
                                        driverTab);
                });

                // =====================================================
                // ADD CONTENT
                // =====================================================

                main.getChildren().addAll(
                                heading,
                                tabBar,
                                contentContainer);

                // =====================================================
                // CENTER
                // =====================================================

                page.setCenter(main);

                // =====================================================
                // SCROLL PANE
                // =====================================================

                ScrollPane scrollPane = new ScrollPane(page);

                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(false);

                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                scrollPane.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.AS_NEEDED);

                scrollPane.setStyle(
                                "-fx-background-color: " + BG + ";" +
                                                "-fx-background: " + BG + ";" +
                                                "-fx-border-color: transparent;");

                // =====================================================
                // ROOT CENTER
                // =====================================================

                root.setCenter(scrollPane);

                // =====================================================
                // SCENE
                // =====================================================

                return new Scene(
                                root,
                                1536,
                                750);
        }

        // =========================================================
        // REVIEW TABS
        // =========================================================

        private HBox createReviewTabs() {

                HBox tabs = new HBox(4);

                tabs.setPadding(
                                new Insets(5));

                tabs.setMaxWidth(
                                Double.MAX_VALUE);

                tabs.setPrefHeight(54);

                tabs.setFillHeight(true);

                tabs.setStyle(
                                "-fx-background-color: #E8F0EB;" +
                                                "-fx-background-radius: 12;");

                Button driver = new Button("Driver Reviews");

                Button app = new Button("App Reviews");

                driver.setPrefHeight(42);
                app.setPrefHeight(42);

                driver.setMaxWidth(
                                Double.MAX_VALUE);

                app.setMaxWidth(
                                Double.MAX_VALUE);

                driver.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                13));

                app.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                13));

                HBox.setHgrow(
                                driver,
                                Priority.ALWAYS);

                HBox.setHgrow(
                                app,
                                Priority.ALWAYS);

                activateTab(
                                driver,
                                app);

                // =====================================================
                // DRIVER HOVER
                // =====================================================

                driver.setOnMouseEntered(e -> {

                        driver.setStyle(
                                        "-fx-background-color: white;" +
                                                        "-fx-text-fill: " + GREEN + ";" +
                                                        "-fx-background-radius: 9;" +
                                                        "-fx-border-color: " + GREEN + ";" +
                                                        "-fx-border-width: 1.5;" +
                                                        "-fx-border-radius: 9;" +
                                                        "-fx-cursor: hand;");
                });

                driver.setOnMouseExited(e -> {

                        driver.setStyle(
                                        "-fx-background-color: white;" +
                                                        "-fx-text-fill: " + GREEN + ";" +
                                                        "-fx-background-radius: 9;" +
                                                        "-fx-border-color: transparent;" +
                                                        "-fx-border-width: 0;" +
                                                        "-fx-border-radius: 9;" +
                                                        "-fx-cursor: hand;");
                });

                // =====================================================
                // APP HOVER
                // =====================================================

                app.setOnMouseEntered(e -> {

                        app.setStyle(
                                        "-fx-background-color: white;" +
                                                        "-fx-text-fill: " + GREEN + ";" +
                                                        "-fx-background-radius: 9;" +
                                                        "-fx-border-color: " + GREEN + ";" +
                                                        "-fx-border-width: 1.5;" +
                                                        "-fx-border-radius: 9;" +
                                                        "-fx-cursor: hand;");
                });

                app.setOnMouseExited(e -> {

                        app.setStyle(
                                        "-fx-background-color: transparent;" +
                                                        "-fx-text-fill: " + TEXT_GRAY + ";" +
                                                        "-fx-background-radius: 9;" +
                                                        "-fx-border-color: transparent;" +
                                                        "-fx-border-width: 0;" +
                                                        "-fx-border-radius: 9;" +
                                                        "-fx-cursor: hand;");
                });

                tabs.getChildren().addAll(
                                driver,
                                app);

                return tabs;
        }

        // =========================================================
        // ACTIVE TAB
        // =========================================================

        private void activateTab(
                        Button active,
                        Button inactive) {

                active.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: " + GREEN + ";" +
                                                "-fx-background-radius: 9;" +
                                                "-fx-border-color: transparent;" +
                                                "-fx-cursor: hand;");

                inactive.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: " + TEXT_GRAY + ";" +
                                                "-fx-background-radius: 9;" +
                                                "-fx-border-color: transparent;" +
                                                "-fx-cursor: hand;");
        }

        // =========================================================
        // DRIVER REVIEWS PAGE
        // =========================================================

        private VBox createDriverReviewsPage() {
                String currentUserMail = CurrentUser.getInstance()
                                .getUser()
                                .getEmail();

                CompletedTripController completedTripController = new CompletedTripController();

                try {

                        completedTrip = completedTripController
                                        .getLatestCompletedTripForUser(
                                                        currentUserMail);

                } catch (Exception e) {

                        e.printStackTrace();

                        showMessage(
                                        "Unable to fetch completed trip.");
                }
                VBox page = new VBox(22);

                page.setFillWidth(true);

                // =====================================================
                // STATISTICS
                // =====================================================

                HBox stats = new HBox(18);

                stats.setFillHeight(true);

                VBox ratingCard = createStatCard(
                                "☆",
                                "4.7",
                                "/5",
                                "★★★★★",
                                "Average Driver Rating Given");

                VBox tripsCard = createStatCard(
                                "✓",
                                "42",
                                "",
                                "",
                                "Trips Reviewed");

                VBox positiveCard = createStatCard(
                                "♡",
                                "39",
                                "",
                                "",
                                "Positive Reviews");

                VBox satisfactionCard = createStatCard(
                                "☺",
                                "93%",
                                "",
                                "",
                                "Overall Satisfaction");

                stats.getChildren().addAll(
                                ratingCard,
                                tripsCard,
                                positiveCard,
                                satisfactionCard);

                for (Node node : stats.getChildren()) {

                        HBox.setHgrow(
                                        node,
                                        Priority.ALWAYS);
                }

                // =====================================================
                // BREAKDOWN + FEEDBACK
                // =====================================================

                HBox middleSection = new HBox(20);

                VBox breakdown = createDriverRatingBreakdown();

                VBox feedback = createDriverRatingForm();

                HBox.setHgrow(
                                breakdown,
                                Priority.ALWAYS);

                HBox.setHgrow(
                                feedback,
                                Priority.ALWAYS);

                middleSection.getChildren().addAll(
                                breakdown,
                                feedback);

                // =====================================================
                // RECENT HEADER
                // =====================================================

                HBox recentHeader = createRecentHeader(
                                "Recent Driver Reviews");

                // =====================================================
                // REVIEWS
                // =====================================================

                HBox reviews = new HBox(30);

                reviews.setAlignment(
                                Pos.CENTER);

                reviews.setFillHeight(true);

                reviews.getChildren().addAll(

                                createDriverReviewCard(
                                                "Priya Sharma",
                                                "Pune → Nashik • 14 Oct 2023",
                                                "Driver was punctual, professional and handled the trip smoothly. Communication was excellent.",
                                                5),
                                createDriverReviewCard(
                                                "Amit Verma",
                                                "Mumbai → Pune • 12 Oct 2023",
                                                "Very cooperative driver and helped with loading and unloading. Overall a good experience.",
                                                4),

                                createDriverReviewCard(
                                                "Sneha Kulkarni",
                                                "Pune → Mumbai • 10 Oct 2023",
                                                "Good driving and maintained the vehicle well. Would definitely recommend the driver.",
                                                5));

                // =====================================================
                // VIEW ALL
                // =====================================================

                HBox viewAll = createViewAll(
                                "View All Driver Reviews  →");

                page.getChildren().addAll(
                                stats,
                                middleSection,
                                recentHeader,
                                reviews,
                                viewAll);

                return page;
        }

        // =========================================================
        // APP REVIEWS PAGE
        // =========================================================

        private VBox createAppReviewsPage() {

                VBox page = new VBox(22);

                page.setFillWidth(true);

                // =====================================================
                // STATISTICS
                // =====================================================

                HBox stats = new HBox(18);

                stats.setFillHeight(true);

                VBox ratingCard = createStatCard(
                                "☆",
                                "4.6",
                                "/5",
                                "★★★★★",
                                "App Rating");

                VBox ratingsCard = createStatCard(
                                "✓",
                                "86",
                                "",
                                "",
                                "App Ratings");

                VBox positiveCard = createStatCard(
                                "♡",
                                "79",
                                "",
                                "",
                                "Positive Reviews");

                VBox satisfactionCard = createStatCard(
                                "☺",
                                "92%",
                                "",
                                "",
                                "User Satisfaction");

                stats.getChildren().addAll(
                                ratingCard,
                                ratingsCard,
                                positiveCard,
                                satisfactionCard);

                for (Node node : stats.getChildren()) {

                        HBox.setHgrow(
                                        node,
                                        Priority.ALWAYS);
                }

                // =====================================================
                // BREAKDOWN + FORM
                // =====================================================

                HBox middle = new HBox(20);

                VBox breakdown = createAppRatingBreakdown();

                VBox form = createAppReviewForm();

                HBox.setHgrow(
                                breakdown,
                                Priority.ALWAYS);

                HBox.setHgrow(
                                form,
                                Priority.ALWAYS);

                middle.getChildren().addAll(
                                breakdown,
                                form);

                // =====================================================
                // RECENT APP REVIEWS
                // =====================================================

                HBox recentHeader = createRecentHeader(
                                "Recent App Reviews");

                HBox reviews = new HBox(30);

                reviews.setAlignment(
                                Pos.CENTER);

                reviews.setFillHeight(true);

                reviews.getChildren().addAll(

                                createAppReviewCard(
                                                "Rahul Patil",
                                                "18 Oct 2023",
                                                "Very easy to create and manage "
                                                                + "pickup requests. The dashboard "
                                                                + "is simple and easy to understand.",
                                                5),

                                createAppReviewCard(
                                                "Amit Jadhav",
                                                "15 Oct 2023",
                                                "Good application with useful "
                                                                + "trip tracking features. "
                                                                + "The map sometimes takes time "
                                                                + "to load.",
                                                4),

                                createAppReviewCard(
                                                "Sneha Kulkarni",
                                                "11 Oct 2023",
                                                "The trip management and request "
                                                                + "features are excellent. "
                                                                + "Overall a useful application.",
                                                5));

                // =====================================================
                // VIEW ALL
                // =====================================================

                HBox viewAll = createViewAll(
                                "View All App Reviews  →");

                page.getChildren().addAll(
                                stats,
                                middle,
                                recentHeader,
                                reviews,
                                viewAll);

                return page;
        }

        // =========================================================
        // STAT CARD
        // =========================================================

        private VBox createStatCard(
                        String icon,
                        String value,
                        String suffix,
                        String stars,
                        String description) {

                VBox card = new VBox(10);

                card.setPadding(
                                new Insets(20));

                card.setMinHeight(170);

                card.setMaxWidth(
                                Double.MAX_VALUE);

                String normalStyle = "-fx-background-color: white;" +
                                "-fx-background-radius: 18;" +
                                "-fx-border-color: " + BORDER + ";" +
                                "-fx-border-width: 1;" +
                                "-fx-border-radius: 18;" +
                                "-fx-cursor: hand;";

                String hoverStyle = "-fx-background-color: white;" +
                                "-fx-background-radius: 18;" +
                                "-fx-border-color: " + GREEN + ";" +
                                "-fx-border-width: 1.5;" +
                                "-fx-border-radius: 18;" +
                                "-fx-cursor: hand;";

                card.setStyle(normalStyle);

                // =====================================================
                // ICON
                // =====================================================

                Label iconLabel = new Label(icon);

                iconLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                25));

                iconLabel.setTextFill(
                                Color.web(GREEN));

                StackPane iconBox = new StackPane(iconLabel);

                iconBox.setPrefSize(
                                43,
                                43);

                iconBox.setMaxSize(
                                43,
                                43);

                iconBox.setStyle(
                                "-fx-background-color: " +
                                                LIGHT_GREEN +
                                                ";" +
                                                "-fx-background-radius: 12;");

                // =====================================================
                // VALUE
                // =====================================================

                HBox numberBox = new HBox(2);

                numberBox.setAlignment(
                                Pos.BASELINE_LEFT);

                Label valueLabel = new Label(value);

                valueLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                39));

                valueLabel.setTextFill(
                                Color.web(TEXT));

                Label suffixLabel = new Label(suffix);

                suffixLabel.setFont(
                                Font.font(
                                                "Arial",
                                                18));

                suffixLabel.setTextFill(
                                Color.web(TEXT_GRAY));

                numberBox.getChildren().addAll(
                                valueLabel,
                                suffixLabel);

                // =====================================================
                // STARS
                // =====================================================

                Label starLabel = new Label(stars);

                starLabel.setFont(
                                Font.font(
                                                "Arial",
                                                21));

                starLabel.setTextFill(
                                Color.web(GOLD));

                // =====================================================
                // DESCRIPTION
                // =====================================================

                Label descriptionLabel = new Label(description);

                descriptionLabel.setFont(
                                Font.font(
                                                "Arial",
                                                13));

                descriptionLabel.setTextFill(
                                Color.web(TEXT_GRAY));

                // =====================================================
                // CONTENT
                // =====================================================

                card.getChildren().addAll(
                                iconBox,
                                numberBox);

                if (!stars.isEmpty()) {

                        card.getChildren().add(
                                        starLabel);
                }

                card.getChildren().add(
                                descriptionLabel);

                // =====================================================
                // HOVER ANIMATION
                // =====================================================

                TranslateTransition lift = new TranslateTransition(
                                Duration.millis(180),
                                card);

                ScaleTransition scale = new ScaleTransition(
                                Duration.millis(180),
                                card);

                card.setOnMouseEntered(e -> {

                        card.setStyle(hoverStyle);

                        lift.setToY(-5);

                        scale.setToX(1.015);
                        scale.setToY(1.015);

                        lift.playFromStart();
                        scale.playFromStart();
                });

                card.setOnMouseExited(e -> {

                        card.setStyle(normalStyle);

                        lift.setToY(0);

                        scale.setToX(1);
                        scale.setToY(1);

                        lift.playFromStart();
                        scale.playFromStart();
                });

                return card;
        }

        // =========================================================
        // DRIVER RATING BREAKDOWN
        // =========================================================

        private VBox createDriverRatingBreakdown() {

                VBox box = new VBox(17);

                box.setPadding(
                                new Insets(28));

                box.setMinWidth(400);

                box.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 18;" +
                                                "-fx-border-color: " + BORDER + ";" +
                                                "-fx-border-radius: 18;");

                Label title = new Label(
                                "Driver Rating Breakdown");

                title.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                20));

                title.setTextFill(
                                Color.web(TEXT));

                box.getChildren().add(title);

                box.getChildren().addAll(

                                createRatingRow(
                                                "5 Stars",
                                                "32",
                                                "76%",
                                                0.76),

                                createRatingRow(
                                                "4 Stars",
                                                "7",
                                                "17%",
                                                0.17),

                                createRatingRow(
                                                "3 Stars",
                                                "2",
                                                "5%",
                                                0.05),

                                createRatingRow(
                                                "2 Stars",
                                                "1",
                                                "2%",
                                                0.02),

                                createRatingRow(
                                                "1 Star",
                                                "0",
                                                "0%",
                                                0));

                return box;
        }

        // =========================================================
        // APP RATING BREAKDOWN
        // =========================================================

        private VBox createAppRatingBreakdown() {

                VBox box = new VBox(17);

                box.setPadding(
                                new Insets(28));

                box.setMinWidth(400);

                box.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 18;" +
                                                "-fx-border-color: " + BORDER + ";" +
                                                "-fx-border-radius: 18;");

                Label title = new Label(
                                "App Rating Breakdown");

                title.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                20));

                title.setTextFill(
                                Color.web(TEXT));

                box.getChildren().add(title);

                box.getChildren().addAll(

                                createRatingRow(
                                                "5 Stars",
                                                "62",
                                                "72%",
                                                0.72),

                                createRatingRow(
                                                "4 Stars",
                                                "15",
                                                "18%",
                                                0.18),

                                createRatingRow(
                                                "3 Stars",
                                                "6",
                                                "7%",
                                                0.07),

                                createRatingRow(
                                                "2 Stars",
                                                "2",
                                                "2%",
                                                0.02),

                                createRatingRow(
                                                "1 Star",
                                                "1",
                                                "1%",
                                                0.01));

                return box;
        }

        // =========================================================
        // RATING ROW
        // =========================================================

        private VBox createRatingRow(
                        String stars,
                        String count,
                        String percentage,
                        double progress) {

                VBox row = new VBox(5);

                HBox labels = new HBox();

                Label starLabel = new Label(
                                stars +
                                                " (" +
                                                count +
                                                ")");

                starLabel.setFont(
                                Font.font(
                                                "Arial",
                                                13));

                starLabel.setTextFill(
                                Color.web(TEXT));

                Label percentageLabel = new Label(percentage);

                percentageLabel.setFont(
                                Font.font(
                                                "Arial",
                                                13));

                percentageLabel.setTextFill(
                                Color.web(TEXT_GRAY));

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                labels.getChildren().addAll(
                                starLabel,
                                spacer,
                                percentageLabel);

                ProgressBar progressBar = new ProgressBar(progress);

                progressBar.setMaxWidth(
                                Double.MAX_VALUE);

                progressBar.setPrefHeight(12);

                progressBar.setStyle(
                                "-fx-accent: #0B7A33;" +
                                                "-fx-control-inner-background: #E8F5EC;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-border-radius: 8;");

                row.getChildren().addAll(
                                labels,
                                progressBar);

                return row;
        }

        // =========================================================
        // DRIVER FEEDBACK
        // =========================================================

        private VBox createDriverRatingForm() {

                VBox box = new VBox(15);

                box.setPadding(new Insets(28));

                box.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 18;" +
                                                "-fx-border-color: " + BORDER + ";" +
                                                "-fx-border-radius: 18;");

                // =====================================================
                // TITLE
                // =====================================================

                Label title = new Label("Rate Your Driver");

                title.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                20));

                title.setTextFill(
                                Color.web(TEXT));

                // =====================================================
                // SUBTITLE
                // =====================================================

                Label subtitle = new Label(
                                "Share your experience from your completed trip.");

                subtitle.setFont(
                                Font.font(
                                                "Arial",
                                                13));

                subtitle.setTextFill(
                                Color.web(TEXT_GRAY));

                // =====================================================
                // DRIVER INFO
                // =====================================================

                VBox driverInfo = new VBox(4);

                driverInfo.setPadding(
                                new Insets(14));

                driverInfo.setStyle(
                                "-fx-background-color: " +
                                                LIGHT_GREEN +
                                                ";" +
                                                "-fx-background-radius: 12;");

                String driverNameText = "No completed trip";

                String tripText = "No completed trip available";

                if (completedTrip != null) {

                        driverNameText = completedTrip.getDriverName();

                        tripText = completedTrip.getPickupLocation()
                                        + " → "
                                        + completedTrip.getDestination();
                }

                Label driverName = new Label(driverNameText);

                driverName.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                16));

                driverName.setTextFill(
                                Color.web(GREEN));

                Label trip = new Label(tripText);

                trip.setFont(
                                Font.font(
                                                "Arial",
                                                12));

                trip.setTextFill(
                                Color.web(TEXT_GRAY));

                Label status = new Label(
                                completedTrip != null
                                                ? "✓ Completed Trip"
                                                : "No Completed Trip");

                status.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                12));
                status.setTextFill(
                                Color.web(GREEN));

                driverInfo.getChildren().addAll(
                                driverName,
                                trip,
                                status);

                // =====================================================
                // RATING
                // =====================================================

                Label ratingLabel = new Label(
                                "How was your experience?");

                ratingLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                13));

                HBox stars = new HBox(4);

                stars.setAlignment(
                                Pos.CENTER_LEFT);

                ToggleGroup ratingGroup = new ToggleGroup();

                for (int i = 1; i <= 5; i++) {

                        ToggleButton star = new ToggleButton("☆");

                        star.setToggleGroup(
                                        ratingGroup);

                        star.setUserData(i);

                        star.setFont(
                                        Font.font(
                                                        "Arial",
                                                        28));

                        setEmptyStarStyle(star);

                        final int selected = i;

                        star.setOnAction(e -> {

                                for (Toggle toggle : ratingGroup.getToggles()) {

                                        ToggleButton button = (ToggleButton) toggle;

                                        int value = (int) button.getUserData();

                                        if (value <= selected) {

                                                setFilledStarStyle(button);

                                        } else {

                                                setEmptyStarStyle(button);
                                        }
                                }
                        });

                        stars.getChildren().add(star);
                }

                // =====================================================
                // REVIEW TEXT
                // =====================================================

                TextArea reviewArea = new TextArea();

                reviewArea.setPromptText(
                                "Tell us about your experience with the driver...");

                reviewArea.setWrapText(true);

                reviewArea.setPrefHeight(95);

                reviewArea.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #CDD4D0;" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-font-size: 13px;");

                // =====================================================
                // CHARACTER COUNTER
                // =====================================================

                Label counter = new Label("0/500");

                counter.setTextFill(
                                Color.web(TEXT_GRAY));

                reviewArea.textProperty().addListener(
                                (obs, oldText, newText) -> {

                                        if (newText.length() > 500) {

                                                reviewArea.setText(
                                                                newText.substring(0, 500));

                                                reviewArea.positionCaret(500);

                                                return;
                                        }

                                        counter.setText(
                                                        reviewArea.getText().length()
                                                                        + "/500");
                                });

                HBox counterBox = new HBox();

                counterBox.setAlignment(
                                Pos.CENTER_RIGHT);

                counterBox.getChildren().add(counter);

                // =====================================================
                // BUTTONS
                // =====================================================

                Button submit = new Button("Submit Review");

                submit.setPrefWidth(145);
                submit.setPrefHeight(42);

                submit.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                13));

                submit.setTextFill(Color.WHITE);

                submit.setStyle(
                                "-fx-background-color: " +
                                                GREEN +
                                                ";" +
                                                "-fx-background-radius: 9;" +
                                                "-fx-cursor: hand;");

                Button clear = new Button("Clear");

                clear.setPrefWidth(90);
                clear.setPrefHeight(42);

                clear.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                13));

                clear.setStyle(
                                "-fx-background-color: #E9E9E9;" +
                                                "-fx-text-fill: #444444;" +
                                                "-fx-background-radius: 9;" +
                                                "-fx-cursor: hand;");

                clear.setOnAction(e -> {

                        reviewArea.clear();

                        ratingGroup.selectToggle(null);

                        for (Toggle toggle : ratingGroup.getToggles()) {

                                setEmptyStarStyle(
                                                (ToggleButton) toggle);
                        }
                });

                // UI ONLY
                submit.setOnAction(e -> {

                        if (completedTrip == null) {

                                showMessage(
                                                "No completed trip available.");

                                return;
                        }

                        if (ratingGroup.getSelectedToggle() == null) {

                                showMessage(
                                                "Please select a driver rating.");

                                return;
                        }

                        if (reviewArea.getText()
                                        .trim()
                                        .isEmpty()) {

                                showMessage(
                                                "Please write a review.");

                                return;
                        }

                        int rating = (int) ratingGroup
                                        .getSelectedToggle()
                                        .getUserData();

                        String userMail = CurrentUser.getInstance()
                                        .getUser()
                                        .getEmail();

                        String driverMail = completedTrip.getDriverId();

                        String tripId = completedTrip.getTripId();

                        String comment = reviewArea.getText()
                                        .trim();

                        boolean success = reviewController.submitDriverReview(
                                        userMail,
                                        driverMail,
                                        tripId,
                                        rating,
                                        comment);

                        if (success) {

                                showMessage(
                                                "Driver review submitted successfully.");

                                reviewArea.clear();

                                ratingGroup.selectToggle(null);

                                for (Toggle toggle : ratingGroup.getToggles()) {

                                        setEmptyStarStyle(
                                                        (ToggleButton) toggle);
                                }

                        } else {

                                showMessage(
                                                "Unable to submit driver review.");
                        }
                });

                HBox buttons = new HBox(12);

                buttons.getChildren().addAll(
                                submit,
                                clear);

                // =====================================================
                // ADD CONTENT
                // =====================================================

                box.getChildren().addAll(
                                title,
                                subtitle,
                                driverInfo,
                                ratingLabel,
                                stars,
                                reviewArea,
                                counterBox,
                                buttons);

                return box;
        }

        // =========================================================
        // APP REVIEW FORM
        // =========================================================

        private VBox createAppReviewForm() {

                VBox box = new VBox(15);

                box.setPadding(
                                new Insets(28));

                box.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 18;" +
                                                "-fx-border-color: " + BORDER + ";" +
                                                "-fx-border-radius: 18;");

                Label title = new Label(
                                "Rate Your EcoLoad Experience");

                title.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                20));

                title.setTextFill(
                                Color.web(TEXT));

                Label subtitle = new Label(
                                "How would you rate your experience "
                                                + "with the EcoLoad app?");

                subtitle.setWrapText(true);

                subtitle.setFont(
                                Font.font(
                                                "Arial",
                                                13));

                subtitle.setTextFill(
                                Color.web(TEXT_GRAY));

                // =====================================================
                // STAR RATING
                // =====================================================

                Label ratingText = new Label("Rating:");

                ratingText.setFont(
                                Font.font(
                                                "Arial",
                                                13));

                HBox stars = new HBox(4);

                ToggleGroup ratingGroup = new ToggleGroup();

                for (int i = 1; i <= 5; i++) {

                        ToggleButton star = new ToggleButton("☆");

                        star.setToggleGroup(
                                        ratingGroup);

                        star.setUserData(i);

                        star.setFont(
                                        Font.font(
                                                        "Arial",
                                                        25));

                        setEmptyStarStyle(star);

                        final int selected = i;

                        star.setOnAction(e -> {

                                for (Toggle toggle : ratingGroup.getToggles()) {

                                        ToggleButton button = (ToggleButton) toggle;

                                        int value = (int) button.getUserData();

                                        if (value <= selected) {

                                                setFilledStarStyle(button);

                                        } else {

                                                setEmptyStarStyle(button);
                                        }
                                }
                        });

                        stars.getChildren().add(star);
                }

                HBox ratingLine = new HBox(12);

                ratingLine.setAlignment(
                                Pos.CENTER_LEFT);

                ratingLine.getChildren().addAll(
                                ratingText,
                                stars);

                // =====================================================
                // TEXT AREA
                // =====================================================

                TextArea reviewArea = new TextArea();

                reviewArea.setPromptText(
                                "Tell us about your experience with EcoLoad...");

                reviewArea.setWrapText(true);

                reviewArea.setPrefHeight(115);

                reviewArea.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #CDD4D0;" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-font-size: 14px;");

                // =====================================================
                // COUNTER
                // =====================================================

                Label counter = new Label("0/500");

                counter.setTextFill(
                                Color.web(TEXT_GRAY));

                reviewArea.textProperty().addListener(
                                (obs, oldText, newText) -> {

                                        if (newText.length() > 500) {

                                                reviewArea.setText(
                                                                newText.substring(
                                                                                0,
                                                                                500));

                                                reviewArea.positionCaret(500);

                                                return;
                                        }

                                        counter.setText(
                                                        reviewArea.getText().length()
                                                                        + "/500");
                                });

                HBox counterBox = new HBox();

                counterBox.setAlignment(
                                Pos.CENTER_RIGHT);

                counterBox.getChildren().add(
                                counter);

                // =====================================================
                // SUBMIT
                // =====================================================

                Button submit = new Button(
                                "Submit Review");

                submit.setPrefWidth(145);
                submit.setPrefHeight(42);

                submit.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                13));

                submit.setTextFill(
                                Color.WHITE);

                submit.setStyle(
                                "-fx-background-color: " +
                                                GREEN +
                                                ";" +
                                                "-fx-background-radius: 9;" +
                                                "-fx-cursor: hand;");

                submit.setOnMouseEntered(e -> submit.setStyle(
                                "-fx-background-color: " +
                                                DARK_GREEN +
                                                ";" +
                                                "-fx-background-radius: 9;" +
                                                "-fx-cursor: hand;"));

                submit.setOnMouseExited(e -> submit.setStyle(
                                "-fx-background-color: " +
                                                GREEN +
                                                ";" +
                                                "-fx-background-radius: 9;" +
                                                "-fx-cursor: hand;"));

                submit.setOnAction(e -> {

                        if (ratingGroup.getSelectedToggle() == null) {

                                showMessage("Please select an app rating.");
                                return;
                        }

                        if (reviewArea.getText().trim().isEmpty()) {

                                showMessage("Please write your feedback.");
                                return;
                        }

                        int rating = (int) ratingGroup
                                        .getSelectedToggle()
                                        .getUserData();

                        String currentUserMail = CurrentUser.getInstance()
                                        .getUser()
                                        .getEmail();

                        String comment = reviewArea.getText().trim();

                        boolean success = reviewController.submitAppReview(
                                        currentUserMail,
                                        rating,
                                        comment);

                        if (success) {

                                showMessage(
                                                "Thank you for reviewing EcoLoad!");

                                reviewArea.clear();

                                ratingGroup.selectToggle(null);

                                for (Toggle toggle : ratingGroup.getToggles()) {

                                        setEmptyStarStyle(
                                                        (ToggleButton) toggle);
                                }

                        } else {

                                showMessage(
                                                "Unable to submit app review.");
                        }
                });
                // =====================================================
                // CLEAR
                // =====================================================

                Button clear = new Button("Clear");

                clear.setPrefWidth(90);
                clear.setPrefHeight(42);

                clear.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                13));

                clear.setStyle(
                                "-fx-background-color: #E9E9E9;" +
                                                "-fx-text-fill: #444444;" +
                                                "-fx-background-radius: 9;" +
                                                "-fx-cursor: hand;");

                clear.setOnMouseEntered(e -> clear.setStyle(
                                "-fx-background-color: #DCDCDC;" +
                                                "-fx-text-fill: #222222;" +
                                                "-fx-background-radius: 9;" +
                                                "-fx-cursor: hand;"));

                clear.setOnMouseExited(e -> clear.setStyle(
                                "-fx-background-color: #E9E9E9;" +
                                                "-fx-text-fill: #444444;" +
                                                "-fx-background-radius: 9;" +
                                                "-fx-cursor: hand;"));

                clear.setOnAction(e -> {

                        reviewArea.clear();

                        ratingGroup.selectToggle(null);

                        for (Toggle toggle : ratingGroup.getToggles()) {

                                ToggleButton button = (ToggleButton) toggle;

                                setEmptyStarStyle(button);
                        }
                });

                HBox buttons = new HBox(12);

                buttons.getChildren().addAll(
                                submit,
                                clear);

                box.getChildren().addAll(
                                title,
                                subtitle,
                                ratingLine,
                                reviewArea,
                                counterBox,
                                buttons);

                return box;
        }

        // =========================================================
        // EMPTY STAR
        // =========================================================

        private void setEmptyStarStyle(
                        ToggleButton button) {

                button.setText("☆");

                button.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: #C9CECB;" +
                                                "-fx-padding: 2 5 2 5;" +
                                                "-fx-cursor: hand;");
        }

        // =========================================================
        // FILLED STAR
        // =========================================================

        private void setFilledStarStyle(
                        ToggleButton button) {

                button.setText("★");

                button.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: #F4B400;" +
                                                "-fx-padding: 2 5 2 5;" +
                                                "-fx-cursor: hand;");
        }

        // =========================================================
        // RECENT HEADER
        // =========================================================

        private HBox createRecentHeader(
                        String titleText) {

                HBox header = new HBox();

                header.setAlignment(
                                Pos.CENTER_LEFT);

                Label title = new Label(titleText);

                title.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                20));

                title.setTextFill(
                                Color.web(TEXT));

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                ComboBox<String> sortBox = new ComboBox<>();

                sortBox.getItems().addAll(
                                "Most Recent",
                                "Highest Rated",
                                "Lowest Rated");

                sortBox.setValue(
                                "Most Recent");

                sortBox.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #D9E1DC;" +
                                                "-fx-border-radius: 20;" +
                                                "-fx-background-radius: 20;");

                header.getChildren().addAll(
                                title,
                                spacer,
                                sortBox);

                return header;
        }

        // =========================================================
        // DRIVER REVIEW CARD
        // =========================================================

        private StackPane createDriverReviewCard(
                        String name,
                        String trip,
                        String review,
                        int rating) {

                Rectangle reviewBack = new Rectangle(
                                360,
                                220);

                reviewBack.setArcWidth(18);
                reviewBack.setArcHeight(18);

                reviewBack.setFill(
                                Color.web("#0B7A33"));

                reviewBack.setTranslateX(12);
                reviewBack.setTranslateY(12);

                reviewBack.setVisible(false);

                // =====================================================
                // CARD
                // =====================================================

                VBox card = new VBox(9);

                card.setPrefWidth(360);
                card.setMinWidth(360);
                card.setMaxWidth(360);

                card.setPrefHeight(220);

                card.setPadding(
                                new Insets(24));

                card.setStyle(
                                "-fx-background-color:white;" +
                                                "-fx-background-radius:18;" +
                                                "-fx-border-radius:18;" +
                                                "-fx-border-color:#E5E5E5;" +
                                                "-fx-border-width:1;");

                // =====================================================
                // SHADOW
                // =====================================================

                DropShadow shadow = new DropShadow();

                shadow.setRadius(10);
                shadow.setOffsetY(4);

                shadow.setColor(
                                Color.rgb(
                                                0,
                                                0,
                                                0,
                                                0.12));

                card.setEffect(shadow);

                // =====================================================
                // STARS
                // =====================================================

                Label stars = new Label(
                                createStars(rating));

                stars.setStyle(
                                "-fx-font-size:20px;" +
                                                "-fx-text-fill:#F4B400;");

                // =====================================================
                // REVIEW
                // =====================================================

                Label reviewText = new Label(
                                "\"" +
                                                review +
                                                "\"");

                reviewText.setWrapText(true);

                reviewText.setMaxWidth(310);

                reviewText.setPrefHeight(78);

                reviewText.setFont(
                                Font.font(
                                                "Arial",
                                                FontPosture.ITALIC,
                                                13));

                reviewText.setTextFill(
                                Color.web("#555B57"));

                // =====================================================
                // DRIVER NAME
                // =====================================================

                Label nameLabel = new Label(
                                "— " + name);

                nameLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                14));

                nameLabel.setTextFill(
                                Color.web(TEXT));

                // =====================================================
                // TRIP
                // =====================================================

                Label tripLabel = new Label(trip);

                tripLabel.setFont(
                                Font.font(
                                                "Arial",
                                                11));

                tripLabel.setTextFill(
                                Color.web(TEXT_GRAY));

                // =====================================================
                // CONTENT
                // =====================================================

                card.getChildren().addAll(
                                stars,
                                reviewText,
                                nameLabel,
                                tripLabel);

                // =====================================================
                // STACK
                // =====================================================

                StackPane reviewCard = new StackPane(
                                reviewBack,
                                card);

                reviewBack.toBack();

                // =====================================================
                // HOVER ANIMATION
                // =====================================================

                TranslateTransition lift = new TranslateTransition(
                                Duration.millis(220),
                                card);

                ScaleTransition scale = new ScaleTransition(
                                Duration.millis(220),
                                card);

                reviewCard.setOnMouseEntered(e -> {

                        reviewBack.setVisible(true);

                        lift.setToY(-12);

                        scale.setToX(1.03);
                        scale.setToY(1.03);

                        lift.playFromStart();
                        scale.playFromStart();
                });

                reviewCard.setOnMouseExited(e -> {

                        reviewBack.setVisible(false);

                        lift.setToY(0);

                        scale.setToX(1);
                        scale.setToY(1);

                        lift.playFromStart();
                        scale.playFromStart();
                });

                return reviewCard;
        }

        // =========================================================
        // APP REVIEW CARD
        // =========================================================

        private StackPane createAppReviewCard(
                        String name,
                        String date,
                        String review,
                        int rating) {

                Rectangle reviewBack = new Rectangle(
                                360,
                                220);

                reviewBack.setArcWidth(18);
                reviewBack.setArcHeight(18);

                reviewBack.setFill(
                                Color.web("#0B7A33"));

                reviewBack.setTranslateX(12);
                reviewBack.setTranslateY(12);

                reviewBack.setVisible(false);

                // =====================================================
                // CARD
                // =====================================================

                VBox card = new VBox(9);

                card.setPrefWidth(360);
                card.setMinWidth(360);
                card.setMaxWidth(360);

                card.setPrefHeight(220);

                card.setPadding(
                                new Insets(24));

                card.setStyle(
                                "-fx-background-color:white;" +
                                                "-fx-background-radius:18;" +
                                                "-fx-border-radius:18;" +
                                                "-fx-border-color:#E5E5E5;" +
                                                "-fx-border-width:1;");

                DropShadow shadow = new DropShadow();

                shadow.setRadius(10);
                shadow.setOffsetY(4);

                shadow.setColor(
                                Color.rgb(
                                                0,
                                                0,
                                                0,
                                                0.12));

                card.setEffect(shadow);

                // =====================================================
                // STARS
                // =====================================================

                Label stars = new Label(
                                createStars(rating));

                stars.setStyle(
                                "-fx-font-size:20px;" +
                                                "-fx-text-fill:#F4B400;");

                // =====================================================
                // REVIEW
                // =====================================================

                Label reviewText = new Label(
                                "\"" +
                                                review +
                                                "\"");

                reviewText.setWrapText(true);

                reviewText.setMaxWidth(310);

                reviewText.setPrefHeight(78);

                reviewText.setFont(
                                Font.font(
                                                "Arial",
                                                FontPosture.ITALIC,
                                                13));

                reviewText.setTextFill(
                                Color.web("#555B57"));

                // =====================================================
                // NAME
                // =====================================================

                Label nameLabel = new Label(
                                "— " + name);

                nameLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                14));

                nameLabel.setTextFill(
                                Color.web(TEXT));

                // =====================================================
                // DATE
                // =====================================================

                Label dateLabel = new Label(date);

                dateLabel.setFont(
                                Font.font(
                                                "Arial",
                                                11));

                dateLabel.setTextFill(
                                Color.web(TEXT_GRAY));

                // =====================================================
                // CONTENT
                // =====================================================

                card.getChildren().addAll(
                                stars,
                                reviewText,
                                nameLabel,
                                dateLabel);

                StackPane reviewCard = new StackPane(
                                reviewBack,
                                card);

                reviewBack.toBack();

                // =====================================================
                // HOVER
                // =====================================================

                TranslateTransition lift = new TranslateTransition(
                                Duration.millis(220),
                                card);

                ScaleTransition scale = new ScaleTransition(
                                Duration.millis(220),
                                card);

                reviewCard.setOnMouseEntered(e -> {

                        reviewBack.setVisible(true);

                        lift.setToY(-12);

                        scale.setToX(1.03);
                        scale.setToY(1.03);

                        lift.playFromStart();
                        scale.playFromStart();
                });

                reviewCard.setOnMouseExited(e -> {

                        reviewBack.setVisible(false);

                        lift.setToY(0);

                        scale.setToX(1);
                        scale.setToY(1);

                        lift.playFromStart();
                        scale.playFromStart();
                });

                return reviewCard;
        }

        // =========================================================
        // CREATE STARS
        // =========================================================

        private String createStars(
                        int rating) {

                StringBuilder stars = new StringBuilder();

                for (int i = 1; i <= 5; i++) {

                        if (i <= rating) {

                                stars.append("★");

                        } else {

                                stars.append("☆");
                        }
                }

                return stars.toString();
        }

        // =========================================================
        // VIEW ALL
        // =========================================================

        private HBox createViewAll(
                        String text) {

                HBox box = new HBox();

                box.setAlignment(
                                Pos.CENTER);

                Label label = new Label(text);

                label.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                14));

                label.setTextFill(
                                Color.web(GREEN));

                label.setStyle(
                                "-fx-cursor: hand;");

                label.setOnMouseEntered(e -> label.setTextFill(
                                Color.web(DARK_GREEN)));

                label.setOnMouseExited(e -> label.setTextFill(
                                Color.web(GREEN)));

                box.getChildren().add(label);

                return box;
        }

        // =========================================================
        // MESSAGE
        // =========================================================

        private void showMessage(
                        String message) {

                Alert alert = new Alert(
                                Alert.AlertType.INFORMATION);

                alert.setTitle("EcoLoad");

                alert.setHeaderText(null);

                alert.setContentText(message);

                alert.showAndWait();
        }
}