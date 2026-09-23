package com.super_x.view.DriverView;

import com.super_x.model.drivermodel.ActiveTripManager;
import com.super_x.model.drivermodel.Trip;
import com.super_x.model.drivermodel.DriverModel;
import com.super_x.model.drivermodel.CurrentDriver;
import com.super_x.model.drivermodel.TripStatus;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.super_x.view.HomePage;
import com.super_x.view.UserView.TripTracking;


public class DriverDashoard {

        public Scene dashBoardScene;

        private final String GREEN = "#0B6B2A";
        private final String DARK_GREEN = "#075A24";
        private final String BG = "#e6f1e8";
        private final String TEXT = "#202820";
        private final String BORDER = "#E3EAE5";

        private Trip currentTrip;
        private int totalEarnings;
       

        private final ActiveTripManager activeTripManager = ActiveTripManager.getInstance();

        public void setCurrentTrip(Trip trip) {
                this.currentTrip = trip;
        }
        public Scene getDashBoardScene() {

                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color: " + BG + ";");

                // ============================================
                // LEFT SIDEBAR
                // ============================================
                VBox sidebar = DriverNavigation.createSidebar("Dashboard");
                root.setLeft(sidebar);

                // ============================================
                // MAIN CONTENT
                // ============================================
                VBox mainContent = createMainContent();

                // ============================================
                // BOTTOM STATUS BAR
                // ============================================
                HBox bottomBar = createBottomBar();
                StackPane dashboardArea = new StackPane(mainContent, bottomBar);
                StackPane.setAlignment(mainContent, Pos.TOP_CENTER);
                StackPane.setAlignment(bottomBar, Pos.BOTTOM_CENTER);
                root.setCenter(dashboardArea);

                // ============================================
                // SCENE
                // ============================================
                Scene scene = new Scene(root, 1536, 750);

                dashBoardScene = scene;
                return dashBoardScene;
        }

        // =========================================================
        // LOAD CURRENT DRIVER ACTIVE TRIP
        // =========================================================

        private void loadCurrentDriverTrip() {

                try {

                        DriverModel currentDriver = CurrentDriver.getInstance().getDriver();

                        if (currentDriver == null
                                        || currentDriver.getEmail() == null
                                        || currentDriver.getEmail()
                                                        .trim()
                                                        .isEmpty()) {

                                System.out.println(
                                                "Current driver not available.");

                                currentTrip = null;
                                return;
                        }

                        String driverEmail = currentDriver.getEmail().trim();

                        currentTrip = activeTripManager.getOrLoadForDriver(
                                        driverEmail);

                        if (currentTrip != null) {

                                System.out.println(
                                                "Current Driver Email: "
                                                                + driverEmail);

                                System.out.println(
                                                "Current Trip ID: "
                                                                + currentTrip.getTripId());

                                System.out.println(
                                                "Current Trip Status: "
                                                                + currentTrip.getStatus());

                        } else {

                                System.out.println(
                                                "No active trip found for: "
                                                                + driverEmail);
                        }

                } catch (Exception e) {

                        e.printStackTrace();

                        currentTrip = null;
                }
        }

        // =========================================================
        // MAIN CONTENT
        // =========================================================
        private VBox createMainContent() {

                VBox content = new VBox(12);
                content.setPadding(Insets.EMPTY);

                // =========================================================
                // HEADER
                // =========================================================

                HBox header = createHeader();

                // =========================================================
                // STAT CARDS
                // =========================================================

                HBox stats = new HBox(18);
                stats.setPadding(new Insets(10, 30, 0, 30));
                totalEarnings = new TripHistory().loadAndGetTotalEarnings();

                stats.getChildren().addAll(

                                statCard(
                                                "/assets/images/availiableLoadsicon.png",
                                                "Available Loads",
                                                "14"),

                                statCard(
                                                "/assets/images/earningicon.png",
                                                "Total Earnings",
                                                "₹2,450"),

                                statCard(
                                                "/assets/images/currentStatus.png",
                                                "Current Status",
                                                "Available"));

                // =========================================================
                // ACTIVE TRIP
                // =========================================================

                loadCurrentDriverTrip();

                String dashboardTripStatus = TripStatus.PICKUP.firestoreValue();

                if (currentTrip != null
                                && currentTrip.getStatus() != null) {

                        dashboardTripStatus = currentTrip.getStatus();
                }

                VBox activeTrip = createActiveTrip(
                                dashboardTripStatus);

                VBox.setMargin(
                                activeTrip,
                                new Insets(0, 30, 0, 30));

                // =========================================================
                // BOTTOM SECTION
                // TRIP RECORDS + PERFORMANCE + QUICK ACTIONS
                // =========================================================

                HBox bottomSection = new HBox(15);

                bottomSection.setPadding(
                                new Insets(0, 30, 0, 30));

                bottomSection.setAlignment(
                                Pos.TOP_LEFT);

                // =========================================================
                // LEFT - TRIP RECORDS
                // =========================================================

                VBox tripRecords = createTripRecords();

                tripRecords.setPrefWidth(600);
                tripRecords.setMinWidth(580);
                tripRecords.setMaxWidth(620);

                // =========================================================
                // MIDDLE - DRIVER PERFORMANCE
                // =========================================================

                VBox performanceCard = createPerformanceCard();

                performanceCard.setPrefWidth(300);
                performanceCard.setMinWidth(280);
                performanceCard.setMaxWidth(320);

                // =========================================================
                // RIGHT - QUICK ACTIONS
                // =========================================================

                VBox quickActionsCard = createQuickActions();

                quickActionsCard.setPrefWidth(300);
                quickActionsCard.setMinWidth(280);
                quickActionsCard.setMaxWidth(320);

                // =========================================================
                // ADD ALL THREE SIDE BY SIDE
                // =========================================================

                bottomSection.getChildren().addAll(
                                tripRecords,
                                performanceCard,
                                quickActionsCard);

                // =========================================================
                // ADD EVERYTHING TO MAIN CONTENT
                // =========================================================

                content.getChildren().addAll(
                                header,
                                stats,
                                activeTrip,
                                bottomSection);

                return content;
        }

        // =========================================================
        // HEADER
        // =========================================================

        private HBox createHeader() {

                return DriverNavigation.createNavbar();
        }

        // =========================================================
        // STAT CARD
        // =========================================================

        private VBox statCard(
                        String imagePath,
                        String title,
                        String value) {

                if ("/assets/images/earningicon.png".equals(imagePath)) {
                        title = "Total Earnings";
                        value = "₹" + String.format("%,d", totalEarnings);
                }

                VBox card = new VBox(8);
                card.setPadding(new Insets(15));
                card.setPrefHeight(100);
                card.setMinHeight(100);

                HBox.setHgrow(card, Priority.ALWAYS);

                addCardHoverEffect(card, 20);

                HBox row = new HBox(20);
                row.setAlignment(Pos.CENTER_LEFT);

                StackPane iconBox = new StackPane();
                iconBox.setMinSize(58, 58);
                iconBox.setPrefSize(58, 58);
                iconBox.setStyle(
                                "-fx-background-color: #99F29A;" +
                                                "-fx-background-radius: 20;");

                ImageView iconImage = new ImageView();
                iconImage.setFitWidth(42);
                iconImage.setFitHeight(42);
                iconImage.setPreserveRatio(true);
                iconImage.setSmooth(true);

                try {
                        iconImage.setImage(new Image(getClass().getResourceAsStream(imagePath)));
                } catch (Exception ex) {
                        Label iconLabel = new Label("?");
                        iconLabel.setStyle(
                                        "-fx-font-size: 28px;" +
                                                        "-fx-text-fill: " + DARK_GREEN + ";");
                        iconBox.getChildren().add(iconLabel);
                }

                iconBox.getChildren().add(iconImage);

                VBox text = new VBox(4);

                Label titleLabel = new Label(title);
                titleLabel.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: " + TEXT + ";");

                Label valueLabel = new Label(value);
                valueLabel.setStyle(
                                "-fx-font-size: 17px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: " + GREEN + ";");

                text.getChildren().addAll(titleLabel, valueLabel);

                row.getChildren().addAll(iconBox, text);

                card.getChildren().add(row);

                return card;
        }

        // =========================================================
        // REUSABLE CARD HOVER EFFECT
        // =========================================================

        private void addCardHoverEffect(Region card, int radius) {

                String normalStyle = "-fx-background-color: white;" +
                                "-fx-background-radius: " + radius + ";" +
                                "-fx-border-color: " + BORDER + ";" +
                                "-fx-border-width: 1;" +
                                "-fx-border-radius: " + radius + ";" +
                                "-fx-cursor: hand;";

                String hoverStyle = "-fx-background-color: white;" +
                                "-fx-background-radius: " + radius + ";" +
                                "-fx-border-color: " + GREEN + ";" +
                                "-fx-border-width: 1.5;" +
                                "-fx-border-radius: " + radius + ";" +
                                "-fx-cursor: hand;";

                card.setStyle(normalStyle);

                card.setOnMouseEntered(event -> {
                        card.setStyle(hoverStyle);
                });

                card.setOnMouseExited(event -> {
                        card.setStyle(normalStyle);
                });
        }

        // =========================================================
        // ACTIVE TRIP
        // =========================================================

        public VBox createActiveTrip() {

                if (currentTrip == null) {
                        loadCurrentDriverTrip();
                }

                TripTracking tripTracking = new TripTracking();

                if (currentTrip != null) {
                        tripTracking.setCurrentTrip(currentTrip);
                }

                return tripTracking.createActiveTrip();
        }

        public VBox createActiveTrip(String tripStatus) {

                // Make sure current active trip is loaded
                if (currentTrip == null) {
                        loadCurrentDriverTrip();
                }

                VBox card = new VBox();
                // card.setMargin(card, new Insets(0, 15, 0, 15));

                addCardHoverEffect(card, 20);

                // TOP
                HBox top = new HBox();
                top.setPadding(new Insets(6, 15, 5, 15));
                top.setAlignment(Pos.CENTER_LEFT);

                VBox tripTitle = new VBox(5);

                Label active = new Label("●  ACTIVE TRIP");

                active.setStyle(
                                "-fx-background-color: " + GREEN + ";" +
                                                "-fx-background-radius: 20;" +
                                                "-fx-padding: 4 10 4 10;" +
                                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: white;");

                Label order = new Label(
                                "Order #TRK-88291 • Electronics Shipment");

                order.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #202520;");

                tripTitle.getChildren().addAll(active, order);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button mapButton = new Button("🗺️ View on Map");

                mapButton.setStyle(
                                "-fx-background-color: " + GREEN + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 20;" +
                                                "-fx-padding: 9 15 9 15;");
                mapButton.setOnMouseEntered(event -> mapButton.setStyle(
                                "-fx-background-color: " + DARK_GREEN + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 16px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 20;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-padding: 12 18 12 18;"));
                mapButton.setOnMouseExited(event -> mapButton.setStyle(
                                "-fx-background-color: " + GREEN + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 20;" +
                                                "-fx-padding: 9 15 9 15;"));
                mapButton.setOnAction(e -> {

                        MapView mapView = new MapView();

                        if (currentTrip != null) {
                                mapView.show(
                                                HomePage.homeStage,
                                                currentTrip.getPickupLocation(),
                                                currentTrip.getDestination());
                        } else {
                                System.out.println("No current trip available.");
                        }
                });

                top.getChildren().addAll(
                                tripTitle,
                                spacer,
                                mapButton);

                // TIMELINE
                VBox timelineArea = new VBox();
                timelineArea.setPadding(new Insets(4, 10, 1, 10));

                HBox timeline = new HBox();

                // =========================================================
                // TIMELINE STATUS LOGIC
                // =========================================================

                boolean pickupCompleted = false;
                boolean dispatchCompleted = false;
                boolean transitCompleted = false;
                boolean arrivedCompleted = false;
                boolean deliveredCompleted = false;

                boolean pickupCurrent = false;
                boolean dispatchCurrent = false;
                boolean transitCurrent = false;
                boolean arrivedCurrent = false;
                boolean deliveredCurrent = false;

                TripStatus progress = TripStatus.fromFirestore(tripStatus);
                int progressIndex = progress.ordinal();
                pickupCompleted = progressIndex > TripStatus.PICKUP.ordinal();
                dispatchCompleted = progressIndex > TripStatus.DISPATCH.ordinal();
                transitCompleted = progressIndex > TripStatus.IN_TRANSIT.ordinal();
                arrivedCompleted = progressIndex > TripStatus.ARRIVED.ordinal();
                deliveredCompleted = progress == TripStatus.DELIVERED;
                pickupCurrent = progress == TripStatus.PICKUP;
                dispatchCurrent = progress == TripStatus.DISPATCH;
                transitCurrent = progress == TripStatus.IN_TRANSIT;
                arrivedCurrent = progress == TripStatus.ARRIVED;
                deliveredCurrent = progress == TripStatus.DELIVERED;
                TimelineStep pickup = new TimelineStep(
                                "Pickup",
                                "08:30 AM",
                                "Navi Mumbai",
                                "🚚",
                                pickupCompleted,
                                pickupCurrent);

                TimelineStep dispatch = new TimelineStep(
                                "Dispatch",
                                "10:15 AM",
                                "Logistics Hub",
                                "📦",
                                dispatchCompleted,
                                dispatchCurrent);

                TimelineStep transit = new TimelineStep(
                                "In Transit",
                                "Expected: 06:00 PM",
                                "Satara Highway",
                                "🚛",
                                transitCompleted,
                                transitCurrent);

                TimelineStep arrived = new TimelineStep(
                                "Arrived",
                                "TBD",
                                "Pune East",
                                "📍",
                                arrivedCompleted,
                                arrivedCurrent);

                TimelineStep delivered = new TimelineStep(
                                "Delivered",
                                "TBD",
                                "Recipient Hub",
                                "📦✓",
                                deliveredCompleted,
                                deliveredCurrent);

                HBox.setHgrow(pickup, Priority.ALWAYS);
                HBox.setHgrow(dispatch, Priority.ALWAYS);
                HBox.setHgrow(transit, Priority.ALWAYS);
                HBox.setHgrow(arrived, Priority.ALWAYS);
                HBox.setHgrow(delivered, Priority.ALWAYS);

                Label location = new Label(
                                "⌖   You are away from the delivery location.");

                location.setStyle(
                                "-fx-font-size: 10px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: " + GREEN + ";");

                if (progress == TripStatus.DELIVERED && currentTrip != null) {
                        String destination = currentTrip.getDestination();
                        location.setText(destination == null || destination.isBlank()
                                        ? "Trip completed."
                                        : "Delivered to " + destination + ".");
                }

                timeline.getChildren().addAll(
                                pickup,
                                dispatch,
                                transit,
                                arrived,
                                delivered);

                timelineArea.getChildren().add(timeline);

                // DISTANCE BAR
                HBox distanceBox = new HBox();
                distanceBox.setAlignment(Pos.CENTER_LEFT);
                distanceBox.setPadding(new Insets(7, 12, 7, 12));

                distanceBox.setStyle(
                                "-fx-background-color: #E3FBE3;" +
                                                "-fx-background-radius: 20;");

                Region spacer1Region = new Region();
                HBox.setHgrow(spacer1Region, Priority.ALWAYS);

                distanceBox.getChildren().addAll(
                                location,
                                spacer1Region);

                VBox.setMargin(distanceBox, new Insets(2, 15, 2, 15));

                Button progressButton = new Button(progress.nextAction());
                progressButton.setDisable(currentTrip == null || progress == TripStatus.DELIVERED);
                progressButton.setCursor(Cursor.HAND);
                progressButton.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white;"
                                + "-fx-font-weight: bold; -fx-background-radius: 16; -fx-padding: 8 16;");
                progressButton.setOnAction(event -> advanceTrip(progressButton));
                VBox.setMargin(progressButton, new Insets(0, 15, 12, 15));

                card.getChildren().addAll(
                                top,
                                new Separator(),
                                timelineArea,
                                distanceBox,
                                progressButton);

                return card;
        }

        /** The Firebase record is read before every transition and remains the source of truth. */
        private void advanceTrip(Button button) {
                if (currentTrip == null) return;
                try {
                        TripStatus current = TripStatus.fromFirestore(currentTrip.getStatus());
                        if (current == TripStatus.DELIVERED) return;
                        TripStatus next = current.next();
                        String now = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                        .format(new java.util.Date());
                        activeTripManager.updateStatus(next.firestoreValue(), now);
                        currentTrip = activeTripManager.getActiveTrip();
                        Stage stage = (Stage) button.getScene().getWindow();
                        stage.setScene(getDashBoardScene());
                } catch (Exception e) {
                        e.printStackTrace();
                        showSimpleAlert("Status Update Error", "Failed to save trip status.");
                }
        }
        // =========================================================
        // DRIVER PERFORMANCE
        // =========================================================

        private VBox createPerformanceCard() {

                VBox card = new VBox(10);

                card.setPadding(
                                new Insets(15));

                card.setPrefHeight(200);

                addCardHoverEffect(card, 18);

                // ---------------------------------------------------------
                // TITLE
                // ---------------------------------------------------------

                Label title = new Label("Driver Performance");

                title.setStyle(
                                "-fx-font-size: 17px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: " + TEXT + ";");

                // ---------------------------------------------------------
                // PERFORMANCE VALUES
                // ---------------------------------------------------------

                HBox trips = performanceRow(
                                "Completed Trips",
                                "18");

                HBox distance = performanceRow(
                                "Total Distance",
                                "4,250 KM");

                HBox earnings = performanceRow(
                                "Total Earnings",
                                "₹1,42,500");

                HBox rating = performanceRow(
                                "Driver Rating",
                                "★ 4.8");

                card.getChildren().addAll(
                                title,
                                trips,
                                distance,
                                earnings,
                                rating);

                return card;
        }

        private HBox performanceRow(String title, String value) {

                HBox row = new HBox();

                row.setAlignment(
                                Pos.CENTER_LEFT);

                Label titleLabel = new Label(title);

                titleLabel.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: " + TEXT + ";");

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                Label valueLabel = new Label(value);

                valueLabel.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: " + GREEN + ";");

                row.getChildren().addAll(
                                titleLabel,
                                spacer,
                                valueLabel);

                return row;
        }

        // =========================================================
        // QUICK ACTIONS
        // =========================================================

        private VBox createQuickActions() {

                VBox card = new VBox(8);

                card.setPadding(
                                new Insets(15));

                card.setPrefHeight(200);

                addCardHoverEffect(card, 18);

                // ---------------------------------------------------------
                // TITLE
                // ---------------------------------------------------------

                Label title = new Label("Quick Actions");

                title.setStyle(
                                "-fx-font-size: 17px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: " + TEXT + ";");

                // ---------------------------------------------------------
                // BUTTONS
                // ---------------------------------------------------------

                Button loads = quickActionButton(
                                "🚚  Available Loads");

                Button location = quickActionButton(
                                "📍  Update Location");

                Button earnings = quickActionButton(
                                "💰  Earnings");

                Button ratings = quickActionButton(
                                "⭐  My Ratings");

                card.getChildren().addAll(
                                title,
                                loads,
                                location,
                                earnings,
                                ratings);

                return card;
        }

        private Button quickActionButton(String text) {

                Button button = new Button(text);

                button.setMaxWidth(
                                Double.MAX_VALUE);

                button.setAlignment(
                                Pos.CENTER_LEFT);

                button.setCursor(
                                Cursor.HAND);

                button.setStyle(
                                "-fx-background-color: #E8F6EE;" +
                                                "-fx-text-fill: " + GREEN + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-padding: 7 10 7 10;");

                return button;
        }

        // =========================================================
        // TRIP RECORDS
        // =========================================================

        private VBox createTripRecords() {

                VBox card = new VBox(8);

                card.setPadding(
                                new Insets(10));

                addCardHoverEffect(card, 20);

                // =========================================================
                // HEADER
                // =========================================================

                HBox titleRow = new HBox();

                titleRow.setAlignment(
                                Pos.CENTER_LEFT);

                Label title = new Label("Trip Records");

                title.setStyle(
                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: " + TEXT + ";");

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                Button viewAll = new Button("View All");

                viewAll.setCursor(
                                Cursor.HAND);

                viewAll.setStyle(
                                "-fx-background-color: #E3FBE3;" +
                                                "-fx-text-fill: " + GREEN + ";" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-padding: 7 14 7 14;");

                viewAll.setOnAction(event -> {

                        TripHistory tripHistory = new TripHistory();

                        Stage stage = (Stage) viewAll.getScene().getWindow();

                        stage.setScene(
                                        tripHistory.getTripHistoryScene());
                });

                titleRow.getChildren().addAll(
                                title,
                                spacer,
                                viewAll);

                // =========================================================
                // TABLE
                // =========================================================

                TableView<TripHistory.Trip> table = new TableView<>();

                table.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);

                table.setPrefHeight(200);
                table.setMinHeight(210);

                table.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #D9E1DC;" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-border-radius: 12;");

                // =========================================================
                // TRIP ID
                // =========================================================

                TableColumn<TripHistory.Trip, String> idColumn = new TableColumn<>("Trip ID");

                idColumn.setCellValueFactory(
                                data -> data.getValue().tripIdProperty());

                // =========================================================
                // DATE
                // =========================================================

                TableColumn<TripHistory.Trip, String> dateColumn = new TableColumn<>("Date");

                dateColumn.setCellValueFactory(
                                data -> data.getValue().dateProperty());

                // =========================================================
                // ROUTE
                // =========================================================

                TableColumn<TripHistory.Trip, String> routeColumn = new TableColumn<>("Route");

                routeColumn.setCellValueFactory(
                                data -> data.getValue().routeProperty());

                // =========================================================
                // EARNINGS
                // =========================================================

                TableColumn<TripHistory.Trip, String> earningsColumn = new TableColumn<>("Earnings");

                earningsColumn.setCellValueFactory(
                                data -> data.getValue().earningsProperty());

                // =========================================================
                // STATUS
                // =========================================================

                TableColumn<TripHistory.Trip, String> statusColumn = new TableColumn<>("Status");

                statusColumn.setCellValueFactory(
                                data -> data.getValue().statusProperty());

                // =========================================================
                // STATUS STYLE
                // =========================================================

                statusColumn.setCellFactory(
                                column -> new TableCell<TripHistory.Trip, String>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                if (empty || item == null) {

                                                        setText(null);
                                                        setGraphic(null);

                                                        return;
                                                }

                                                Label badge = new Label(item);

                                                String color;

                                                if (item.equals("Completed")) {

                                                        color = GREEN;

                                                } else if (item.equals("Cancelled")) {

                                                        color = "#D32F2F";

                                                } else {

                                                        color = "#E58A00";
                                                }

                                                badge.setStyle(
                                                                "-fx-background-color: " +
                                                                                color + ";" +
                                                                                "-fx-text-fill: white;" +
                                                                                "-fx-font-size: 10px;" +
                                                                                "-fx-font-weight: bold;" +
                                                                                "-fx-background-radius: 10;" +
                                                                                "-fx-padding: 5 9 5 9;");

                                                setGraphic(badge);
                                                setText(null);

                                                setAlignment(
                                                                Pos.CENTER);
                                        }
                                });

                table.getColumns().addAll(
                                idColumn,
                                dateColumn,
                                routeColumn,
                                earningsColumn,
                                statusColumn);

                // =========================================================
                // GET DATA FROM TRIP HISTORY
                // =========================================================

                TripHistory tripHistory = new TripHistory();

                List<TripHistory.Trip> allTrips = tripHistory.getAllTrips();

                // Show latest 5 records
                int numberOfTrips = Math.min(5, allTrips.size());

                if (numberOfTrips > 0) {

                        table.getItems().addAll(
                                        allTrips.subList(
                                                        0,
                                                        numberOfTrips));
                }

                // =========================================================
                // ADD TO CARD
                // =========================================================

                card.getChildren().addAll(
                                titleRow,
                                table);

                return card;
        }

        private void activateTimelineStep(
                        List<TimelineStep> steps,
                        int clickedIndex,
                        Label locationLabel) {

                // ==========================================
                // 1. CHECK CURRENT TRIP
                // ==========================================

                if (currentTrip == null) {
                        loadCurrentDriverTrip();
                }

                if (currentTrip == null) {
                        System.out.println("No active trip found.");
                        return;
                }

                // ==========================================
                // 2. CONVERT TIMELINE CLICK TO DATABASE STATUS
                // ==========================================

                String newStatus;

                switch (clickedIndex) {

                        case 0:
                                // Pickup
                                newStatus = "ACTIVE";
                                break;

                        case 1:
                                // Dispatch
                                newStatus = "PICKUP_COMPLETED";
                                break;

                        case 2:
                                // In Transit
                                newStatus = "IN_TRANSIT";
                                break;

                        case 3:
                                // Arrived
                                newStatus = "DELIVERY";
                                break;

                        case 4:
                                // Delivered
                                newStatus = "COMPLETED";
                                break;

                        default:
                                return;
                }

                // ==========================================
                // 3. SAVE STATUS TO FIRESTORE
                // ==========================================

                try {

                        String currentTime = new java.text.SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm:ss")
                                        .format(new java.util.Date());

                        activeTripManager.updateStatus(newStatus, currentTime);

                        // ==========================================
                        // 4. UPDATE LOCAL TRIP OBJECT
                        // ==========================================

                        currentTrip = activeTripManager.getActiveTrip();

                        System.out.println(
                                        "Trip status saved successfully: "
                                                        + newStatus);

                        System.out.println(
                                        "Trip ID: "
                                                        + currentTrip.getTripId());

                } catch (Exception e) {

                        e.printStackTrace();

                        showSimpleAlert(
                                        "Status Update Error",
                                        e.getMessage() == null
                                                        ? "Failed to save trip status."
                                                        : e.getMessage());

                        return;
                }

                // ==========================================
                // 5. UPDATE DASHBOARD UI
                // ==========================================

                for (TimelineStep step : steps) {
                        step.updateState(false, false);
                }

                SequentialTransition transition = new SequentialTransition();

                for (int i = 0; i <= clickedIndex; i++) {

                        int index = i;

                        PauseTransition delay = new PauseTransition(
                                        Duration.millis(140));

                        delay.setOnFinished(event -> {

                                for (int j = 0; j < steps.size(); j++) {

                                        boolean completed = j < index;
                                        boolean current = j == index;

                                        steps.get(j).updateState(
                                                        completed,
                                                        current);
                                }
                        });

                        transition.getChildren().add(delay);
                }

                transition.setOnFinished(event -> {

                        if (clickedIndex == 4) {

                                locationLabel.setText(
                                                "✓ Delivered to Recipient Hub.");

                        } else {

                                locationLabel.setText(
                                                "⌖   You are away from the delivery location.");
                        }
                });

                transition.play();
        }

        // =========================================================
        // TIMELINE STEP
        // =========================================================

        private class TimelineStep extends VBox {

                private final Region leftLine;
                private final Region rightLine;
                private final Circle circle;
                private final Label iconLabel;

                TimelineStep(
                                String title,
                                String time,
                                String location,
                                String icon,
                                boolean completed,
                                boolean current) {

                        setAlignment(Pos.TOP_CENTER);
                        setSpacing(2);

                        // CONNECTING LINE
                        HBox iconRow = new HBox();

                        leftLine = new Region();
                        rightLine = new Region();
                        circle = new Circle(20);
                        iconLabel = new Label(icon);

                        HBox.setHgrow(leftLine, Priority.ALWAYS);
                        HBox.setHgrow(rightLine, Priority.ALWAYS);

                        leftLine.setPrefHeight(8);
                        leftLine.setMinHeight(8);
                        leftLine.setMaxHeight(8);
                        rightLine.setPrefHeight(8);
                        rightLine.setMinHeight(8);
                        rightLine.setMaxHeight(8);

                        leftLine.setStyle(
                                        "-fx-background-color: " +
                                                        (completed ? GREEN : "#BEC8C0") + ";");

                        rightLine.setStyle(
                                        "-fx-background-color: " +
                                                        (completed && !current ? GREEN : "#BEC8C0") + ";");

                        if (current) {
                                circle.setFill(Color.WHITE);
                                circle.setStroke(Color.web(GREEN));
                                circle.setStrokeWidth(5);
                        } else if (completed) {
                                circle.setFill(Color.web(GREEN));
                                circle.setStroke(null);
                        } else {
                                circle.setFill(Color.web("#C4CEC5"));
                                circle.setStroke(null);
                        }

                        iconLabel.setStyle(
                                        "-fx-font-size: 21px;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-text-fill: " +
                                                        (completed ? "white" : "#FFFFFF") + ";");

                        StackPane circleBox = new StackPane(
                                        circle,
                                        iconLabel);

                        circleBox.setMinWidth(45);
                        circleBox.setCursor(Cursor.HAND);
                        circleBox.setOnMouseClicked(event -> {
                                if (getParent() instanceof HBox timelineRow) {
                                        // the timeline icon click is handled on the parent timeline step
                                }
                        });

                        iconRow.setAlignment(Pos.CENTER);
                        iconRow.getChildren().addAll(
                                        leftLine,
                                        circleBox,
                                        rightLine);

                        Label titleLabel = new Label(title);

                        titleLabel.setStyle(
                                        "-fx-font-size: 16px;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-text-fill: " +
                                                        (completed ? GREEN : "#333833") + ";");

                        Label timeLabel = new Label(time);

                        timeLabel.setStyle(
                                        "-fx-font-size: 9px;" +
                                                        "-fx-text-fill: #404740;");

                        Label locationLabel = new Label(location);

                        locationLabel.setStyle(
                                        "-fx-font-size: 9px;" +
                                                        "-fx-text-fill: #303630;");

                        getChildren().addAll(
                                        iconRow,
                                        titleLabel,
                                        timeLabel,
                                        locationLabel);
                }

                void updateState(boolean completed, boolean current) {
                        leftLine.setStyle(
                                        "-fx-background-color: " +
                                                        ((completed || current) ? GREEN : "#BEC8C0") + ";");
                        rightLine.setStyle(
                                        "-fx-background-color: " +
                                                        (completed ? GREEN : "#BEC8C0") + ";");

                        if (current) {
                                circle.setFill(Color.WHITE);
                                circle.setStroke(Color.web(GREEN));
                                circle.setStrokeWidth(5);
                                iconLabel.setStyle(
                                                "-fx-font-size: 14px;" +
                                                                "-fx-font-weight: bold;" +
                                                                "-fx-text-fill: " + "#202520" + ";");
                        } else if (completed) {
                                circle.setFill(Color.web(GREEN));
                                circle.setStroke(null);
                                iconLabel.setStyle(
                                                "-fx-font-size: 14px;" +
                                                                "-fx-font-weight: bold;" +
                                                                "-fx-text-fill: white;");
                        } else {
                                circle.setFill(Color.web("#C4CEC5"));
                                circle.setStroke(null);
                                iconLabel.setStyle(
                                                "-fx-font-size: 14px;" +
                                                                "-fx-font-weight: bold;" +
                                                                "-fx-text-fill: white;");
                        }
                }
        }
        // =========================================================
        // BOTTOM STATUS BAR
        // =========================================================

        private HBox createBottomBar() {

                HBox bar = new HBox();

                bar.setPrefHeight(45);
                bar.setMinHeight(45);
                bar.setMaxHeight(45);
                bar.setPadding(new Insets(0, 30, 0, 30));
                bar.setAlignment(Pos.CENTER_LEFT);

                bar.setStyle(
                                "-fx-background-color: #eef2eb;");

                Label online = new Label("●  System Online");
                online.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: Green;-fx-font-weight:bold");

                Label connection = new Label("⌁  Strong Connection");
                connection.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: Green;-fx-font-weight:bold");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label gps = new Label("GPS Accuracy: 2.4m");
                gps.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: Green;-fx-font-weight:bold");

                // Label time = new Label("09:51 AM");
                Label time = new Label();

                time.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: Green;-fx-font-weight:bold");

                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

                Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

                        LocalDateTime now = LocalDateTime.now();
                        time.setText(
                                        now.format(timeFormatter)
                                                        + "  |  "
                                                        + now.format(dateFormatter));
                }),

                                new KeyFrame(Duration.seconds(1)));

                clock.setCycleCount(Timeline.INDEFINITE);
                clock.play();

                bar.setSpacing(30);

                bar.getChildren().addAll(
                                online,
                                connection,
                                spacer,
                                gps,
                                time);

                return bar;
        }

        private void showSimpleAlert(String title, String message) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);

                alert.showAndWait();
        }
}
