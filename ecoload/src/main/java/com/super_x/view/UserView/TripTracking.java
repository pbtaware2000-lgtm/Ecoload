package com.super_x.view.UserView;

import java.util.List;

import com.super_x.dao.userdao.LoadDao;
import com.super_x.config.FirebaseConfig;
import com.super_x.dao.driverdao.DriverDAO;
import com.super_x.dao.driverdao.VehicleDAO;
import com.super_x.dao.delivery.DeliveryProofDAO;
import com.super_x.model.delivery.DeliveryProof;
import com.super_x.model.drivermodel.ActiveTripManager;
import com.super_x.model.drivermodel.DriverModel;
import com.super_x.model.drivermodel.Trip;
import com.super_x.model.drivermodel.TripStatus;
import com.super_x.model.drivermodel.VehicleModel;
import com.super_x.model.usermodel.CurrentUser;
import com.super_x.model.usermodel.Load;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.concurrent.Task;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class TripTracking {

    private Trip currentTrip;
    private Load currentLoad;
    private DriverModel assignedDriver;
    private VehicleModel assignedVehicle;

    private final LoadDao loadDao = new LoadDao();
    private final ActiveTripManager activeTripManager = ActiveTripManager.getInstance();
    private final DriverDAO driverDao = new DriverDAO(FirebaseConfig.getFireStore());
    private final VehicleDAO vehicleDao = new VehicleDAO(FirebaseConfig.getFireStore());
    private final DeliveryProofDAO deliveryProofDAO = new DeliveryProofDAO();

    private VBox timelineArea;
    private Label statusLocationLabel;

    // =========================================================
    // COLORS
    // =========================================================

    private static final String GREEN = "#087A36";
    private static final String DARK_GREEN = "#05652D";
    private static final String BACKGROUND = "#F3F8F5";
    private static final String TEXT = "#17211B";
    private static final String MUTED = "#69746D";
    private static final String BORDER = "#DDE7E1";

    // =========================================================
    // SET CURRENT TRIP
    // =========================================================

    public void setCurrentTrip(Trip trip) {
        this.currentTrip = trip;
        loadCurrentLoad();
        loadAssignedDriver();
    }

    // =========================================================
    // MAIN SCENE
    // =========================================================

    public Scene getTripTrackingScene() {

        loadActiveTrip();
        activeTripManager.addObserver(trip -> javafx.application.Platform.runLater(() -> {
            setCurrentTrip(trip);
            refreshTimeline();
        }));

        BorderPane root = new BorderPane();

        BorderPane mainContent = new BorderPane();
        mainContent.setTop(UserNavigation.createNavbar());

        root.setStyle(
                "-fx-background-color: " + BACKGROUND + ";");

        root.setTop(createHeader());

        HBox main = new HBox(18);

        main.setPadding(
                new Insets(
                        22,
                        24,
                        22,
                        24));

        VBox left = new VBox(18);
        VBox right = new VBox(18);

        left.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(
                left,
                Priority.ALWAYS);

        right.setPrefWidth(310);
        right.setMinWidth(290);
        right.setMaxWidth(330);

        VBox activeTripCard = createActiveTrip();

        left.getChildren().addAll(
                activeTripCard,
                createLiveLocationCard());

        right.getChildren().addAll(
                createDriverCard(),
                createLoadInventoryCard(),
                createDeliveryProofCard());

        main.getChildren().addAll(
                left,
                right);

        root.setCenter(main);

        // Keep the existing tracking layout intact while allowing the whole
        // tracking area (header and both columns) to grow beyond the window.
        ScrollPane trackingScroll = new ScrollPane(root);
        trackingScroll.setFitToWidth(true);
        trackingScroll.setFitToHeight(false);
        trackingScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        trackingScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        trackingScroll.setStyle(
                "-fx-background-color: " + BACKGROUND + ";" +
                        "-fx-background: " + BACKGROUND + ";" +
                        "-fx-border-color: transparent;");
        mainContent.setCenter(trackingScroll);

        BorderPane mainroot = new BorderPane();
        mainroot.setLeft(
                UserNavigation.createSidebar("Trip Tracking"));
        mainroot.setCenter(mainContent);

        return new Scene(
                mainroot,
                1536,
                750);
    }

    // =========================================================
    // LOAD ACTIVE TRIP
    // =========================================================

    private void loadActiveTrip() {

        try {
            if (CurrentUser.getInstance().getUser() == null
                    || CurrentUser.getInstance().getUser().getEmail() == null) return;
            // Always refresh this user's Firebase active-trip record when the
            // page opens; do not reuse a prior driver's/session's trip object.
            currentTrip = activeTripManager.refreshForUser(
                    CurrentUser.getInstance().getUser().getEmail().trim());

            loadCurrentLoad();
            loadAssignedDriver();

            if (currentTrip != null) {
                System.out.println(
                        "Active Trip Found: " +
                                currentTrip.getTripId());

                System.out.println(
                        "Route: " +
                                currentTrip.getPickupLocation() +
                                " → " +
                                currentTrip.getDestination());
            } else {
                System.out.println("No active trip found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCurrentLoad() {

        currentLoad = null;

        if (currentTrip == null ||
                currentTrip.getLoadId() == null) {
            return;
        }

        try {
            currentLoad =
                    loadDao.getLoadById(
                            currentTrip.getLoadId());

            if (currentLoad != null) {
                System.out.println(
                        "Load Type: " +
                                currentLoad.getLoadType());

                System.out.println(
                        "Weight: " +
                                currentLoad.getWeight() +
                                " " +
                                currentLoad.getWeightUnit());
            } else {
                System.out.println("Load data not found.");
            }

        } catch (Exception e) {
            System.out.println(
                    "Load fetch error: " + e.getMessage());
        }
    }

    /**
     * Resolves driver data strictly from the current trip's assigned driverId.
     * The load is not used to guess a driver and no driver-name search occurs.
     */
    private void loadAssignedDriver() {

        assignedDriver = null;
        assignedVehicle = null;

        if (currentTrip == null || currentTrip.getDriverId() == null
                || currentTrip.getDriverId().isBlank()) {
            return;
        }

        try {
            String driverId = currentTrip.getDriverId().trim();
            assignedDriver = driverDao.getDriverByEmail(driverId);
            assignedVehicle = vehicleDao.getVehicleByDriverEmail(driverId);
        } catch (Exception e) {
            System.out.println("Assigned driver fetch error: " + e.getMessage());
        }
    }

    // =========================================================
    // HEADER
    // =========================================================

    private HBox createHeader() {

        HBox header = new HBox();

        header.setPrefHeight(76);
        header.setAlignment(Pos.CENTER_LEFT);

        header.setPadding(
                new Insets(
                        0,
                        28,
                        0,
                        28));

        header.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #E1E9E4;" +
                "-fx-border-width: 0 0 1 0;");

        VBox titleBox = new VBox(3);

        Label title = new Label("Trip Tracking");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        23));

        title.setTextFill(Color.web(TEXT));

        Label subtitle = new Label(
                "Monitor your active deliveries in real time");

        subtitle.setFont(
                Font.font("Arial", 12));

        subtitle.setTextFill(Color.web(MUTED));

        titleBox.getChildren().addAll(
                title,
                subtitle);

        header.getChildren().add(titleBox);

        return header;
    }

    // =========================================================
    // ACTIVE TRIP CARD
    // =========================================================

    public VBox createActiveTrip() {

        VBox card = new VBox();

        addCardHoverEffect(card, 20);

        // ---------------------------------------------------------
        // TOP
        // ---------------------------------------------------------

        HBox top = new HBox();

        top.setPadding(
                new Insets(
                        12,
                        18,
                        10,
                        18));

        top.setAlignment(Pos.CENTER_LEFT);

        VBox tripTitle = new VBox(6);

        Label active = new Label("●  ACTIVE TRIP");

        active.setStyle(
                "-fx-background-color: " + GREEN + ";" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 6 13 6 13;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;");

        String tripText;
        String routeText;

        if (currentTrip != null) {
            tripText =
                    "Trip #" + currentTrip.getTripId() +
                    " • Load #" + currentTrip.getLoadId();

            routeText =
                    safeText(currentTrip.getPickupLocation()) +
                    "  →  " +
                    safeText(currentTrip.getDestination());
        } else {
            tripText = "No Active Trip";
            routeText = "Route unavailable";
        }

        Label order = new Label(tripText);

        order.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #202520;");

        Label routeLabel = new Label(routeText);

        routeLabel.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: " + MUTED + ";" +
                "-fx-font-weight: bold;");

        tripTitle.getChildren().addAll(
                active,
                order,
                routeLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button mapButton = new Button("⌖ View on Map");

        mapButton.setCursor(Cursor.HAND);
        mapButton.setStyle(
                "-fx-background-color: " + GREEN + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 9 15 9 15;");

        mapButton.setOnMouseEntered(e ->
                mapButton.setStyle(
                        "-fx-background-color: " + DARK_GREEN + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 9 15 9 15;"));

        mapButton.setOnMouseExited(e ->
                mapButton.setStyle(
                        "-fx-background-color: " + GREEN + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 9 15 9 15;"));

        mapButton.setOnAction(e -> showMapMessage());

        top.getChildren().addAll(
                tripTitle,
                spacer,
                mapButton);

        // ---------------------------------------------------------
        // TIMELINE
        // ---------------------------------------------------------

        timelineArea = new VBox();

        timelineArea.setPadding(
                new Insets(
                        4,
                        10,
                        1,
                        10));

        timelineArea.getChildren().add(
                createTimeline());

        // ---------------------------------------------------------
        // STATUS MESSAGE
        // ---------------------------------------------------------

        statusLocationLabel = new Label(
                getLocationMessage());

        statusLocationLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11));

        statusLocationLabel.setTextFill(
                Color.web(GREEN));

        HBox statusBox = new HBox();

        statusBox.setAlignment(Pos.CENTER_LEFT);

        statusBox.setPadding(
                new Insets(
                        10,
                        14,
                        10,
                        14));

        statusBox.setStyle(
                "-fx-background-color: #E3FBE3;" +
                "-fx-background-radius: 20;");

        statusBox.getChildren().add(
                statusLocationLabel);

        VBox.setMargin(
                statusBox,
                new Insets(
                        3,
                        18,
                        12,
                        18));

        card.setUserData(statusLocationLabel);

        card.getChildren().addAll(
                top,
                new Separator(),
                timelineArea,
                statusBox);

        return card;
    }

    // =========================================================
    // CREATE SAME TIMELINE AS DRIVER DASHBOARD
    // =========================================================

    private HBox createTimeline() {

        HBox timeline = new HBox();
        timeline.setPrefWidth(910);
        timeline.setMaxWidth(Double.MAX_VALUE);

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

        TripStatus progress = TripStatus.fromFirestore(
                currentTrip == null ? null : currentTrip.getStatus());
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
                loadSchedule(),
                tripPickup(),
                "🚚",
                pickupCompleted,
                pickupCurrent);

        TimelineStep dispatch = new TimelineStep(
                "Dispatch",
                recordedOrUnavailable(currentTrip == null ? null : currentTrip.getStartTime()),
                tripPickup(),
                "📦",
                dispatchCompleted,
                dispatchCurrent);

        TimelineStep transit = new TimelineStep(
                "In Transit",
                recordedOrUnavailable(currentTrip == null ? null : currentTrip.getStartTime()),
                tripRoute(),
                "🚛",
                transitCompleted,
                transitCurrent);

        TimelineStep arrived = new TimelineStep(
                "Arrived",
                "Not recorded",
                tripDestination(),
                "📍",
                arrivedCompleted,
                arrivedCurrent);

        TimelineStep delivered = new TimelineStep(
                "Delivered",
                recordedOrUnavailable(currentTrip == null ? null : currentTrip.getCompletedTime()),
                tripDestination(),
                "📦✓",
                deliveredCompleted,
                deliveredCurrent);

        List<TimelineStep> steps = List.of(
                pickup,
                dispatch,
                transit,
                arrived,
                delivered);

        for (TimelineStep step : steps) {
            HBox.setHgrow(step, Priority.ALWAYS);
            step.setCursor(Cursor.HAND);
        }

        timeline.getChildren().addAll(
                pickup,
                dispatch,
                transit,
                arrived,
                delivered);

        return timeline;
    }

    // =========================================================
    // REFRESH TIMELINE
    // =========================================================

    private void refreshTimeline() {

        if (timelineArea == null) {
            return;
        }

        timelineArea.getChildren().setAll(
                createTimeline());

        if (statusLocationLabel != null) {
            statusLocationLabel.setText(
                    getLocationMessage());
        }
    }

    // =========================================================
    // LOCATION MESSAGE
    // =========================================================

    private String getLocationMessage() {

        if (currentTrip == null ||
                currentTrip.getStatus() == null) {
            return "⌖   No active trip available.";
        }

        TripStatus progress = TripStatus.fromFirestore(currentTrip.getStatus());
        if (progress != null) {
            String place = (progress == TripStatus.ARRIVED || progress == TripStatus.DELIVERED)
                    ? safeText(currentTrip.getDestination())
                    : safeText(currentTrip.getPickupLocation());
            return progress == TripStatus.DELIVERED
                    ? "Delivered to " + place + "."
                    : "Current trip status: " + progress.firestoreValue() + " — " + place + ".";
        }

        return "Current trip status: " + safeText(currentTrip.getStatus()) + ".";
    }

    /** Loads a proof only when its stored userId belongs to the current user. */
    private VBox createDeliveryProofCard() {
        VBox card = createCard();
        Label state = new Label("Loading delivery proof...");
        state.setTextFill(Color.web(MUTED));
        card.getChildren().addAll(sectionTitle("Delivery Proof"), state);
        String tripId = currentTrip == null ? null : currentTrip.getTripId();
        String loadId = currentLoad == null ? null : currentLoad.getLoadId();
        String userId = CurrentUser.getInstance().getUser() == null ? null : CurrentUser.getInstance().getUser().getEmail();
        if (tripId == null || loadId == null || userId == null || userId.isBlank()) {
            state.setText("Delivery proof is not available yet.");
            return card;
        }
        Task<DeliveryProof> task = new Task<>() {
            @Override protected DeliveryProof call() throws Exception { return deliveryProofDAO.getProofForUser(tripId, loadId, userId.trim()); }
        };
        task.setOnSucceeded(event -> {
            DeliveryProof proof = task.getValue();
            if (proof == null) { state.setText("Delivery proof is not available yet."); return; }
            card.getChildren().setAll(sectionTitle("Delivery Proof"), deliveryProofDetails(proof));
        });
        task.setOnFailed(event -> { System.err.println("Unable to load delivery proof: " + task.getException()); state.setText("Unable to load delivery proof."); });
        Thread worker = new Thread(task, "delivery-proof-load"); worker.setDaemon(true); worker.start();
        return card;
    }

    private VBox deliveryProofDetails(DeliveryProof proof) {
        Label status = new Label(safeText(proof.deliveryStatus()));
        status.setStyle("-fx-background-color: #E3FBE3; -fx-text-fill: #087A36; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 5 10 5 10;");
        String deliveredAt = proof.deliveredAt() == null ? "Not recorded" : proof.deliveredAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        VBox details = new VBox(8, status,
                detailRow("Receiver", safeText(proof.receiverName())),
                detailRow("Delivered", deliveredAt));
        HBox images = new HBox(10, proofImage("Delivery photo", proof.deliveryPhotoUrl()), proofImage("Signature", proof.signatureUrl()));
        Button viewProof = new Button("View Delivery Proof");
        viewProof.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        viewProof.setOnAction(event -> showDeliveryProof(proof, (Stage) viewProof.getScene().getWindow()));
        details.getChildren().addAll(images, viewProof);
        return details;
    }

    private VBox proofImage(String caption, String url) {
        Label title = new Label(caption); title.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: " + MUTED + ";");
        ImageView image = new ImageView(); image.setFitWidth(125); image.setFitHeight(80); image.setPreserveRatio(true); image.setSmooth(true);
        if (url != null && !url.isBlank()) image.setImage(new Image(url, true));
        VBox box = new VBox(4, title, image); return box;
    }

    private void showDeliveryProof(DeliveryProof proof, Stage owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner); dialog.initModality(Modality.APPLICATION_MODAL); dialog.setTitle("Delivery Proof");
        String deliveredAt = proof.deliveredAt() == null ? "Not recorded" : proof.deliveredAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        ImageView photo = fullProofImage(proof.deliveryPhotoUrl());
        ImageView signature = fullProofImage(proof.signatureUrl());
        VBox root = new VBox(12, sectionTitle("Delivery Proof"),
                detailRow("Status", safeText(proof.deliveryStatus())), detailRow("Receiver", safeText(proof.receiverName())),
                detailRow("Delivered", deliveredAt), new Label("Delivery Photo"), photo, new Label("Receiver Signature"), signature);
        root.setPadding(new Insets(20)); root.setStyle("-fx-background-color: " + BACKGROUND + ";");
        dialog.setScene(new Scene(root, 520, 620)); dialog.showAndWait();
    }

    private ImageView fullProofImage(String url) {
        ImageView image = new ImageView(); image.setFitWidth(470); image.setFitHeight(210); image.setPreserveRatio(true); image.setSmooth(true);
        if (url != null && !url.isBlank()) image.setImage(new Image(url, true));
        return image;
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
            setMinWidth(0);

            HBox iconRow = new HBox();
            iconRow.setAlignment(Pos.CENTER);

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
                            ((completed || current) ? GREEN : "#BEC8C0") + ";");

            rightLine.setStyle(
                    "-fx-background-color: " +
                            (completed ? GREEN : "#BEC8C0") + ";");

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
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: " +
                    (current ? "#202520" : "white") + ";");

            StackPane circleBox = new StackPane(
                    circle,
                    iconLabel);

            circleBox.setMinWidth(45);

            iconRow.getChildren().addAll(
                    leftLine,
                    circleBox,
                    rightLine);

            Label titleLabel = new Label(title);

            titleLabel.setStyle(
                    "-fx-font-size: 16px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: " +
                    ((completed || current) ? GREEN : "#333833") + ";");

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
    }

    // =========================================================
    // LIVE LOCATION CARD
    // =========================================================

    private VBox createLiveLocationCard() {

        VBox card = createCard();

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label mapIcon = new Label("▣");

        mapIcon.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18));

        mapIcon.setTextFill(Color.web(GREEN));

        Label title = new Label("Live Location");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        17));

        title.setTextFill(Color.web(TEXT));

        HBox titleBox = new HBox(9);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        titleBox.getChildren().addAll(
                mapIcon,
                title);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox speed = metric(
                "CURRENT SPEED",
                "Not available");

        VBox ping = metric(
                "LAST PING",
                "Not available");

        header.getChildren().addAll(
                titleBox,
                spacer,
                speed,
                ping);

        HBox footer = new HBox(70);

        footer.setPadding(
                new Insets(
                        8,
                        2,
                        0,
                        2));

        footer.getChildren().addAll(
                footerMetric(
                        "Total Distance",
                        currentTrip == null ? "Not available" : String.valueOf(currentTrip.getDistanceKm()),
                        currentTrip == null ? "" : "km"),
                footerMetric(
                        "Remaining",
                        "Not available",
                        ""),
                footerMetric(
                        "Estimated Time",
                        currentTrip == null ? "Not available" : recordedOrUnavailable(currentTrip.getEta()),
                        ""));

        card.getChildren().addAll(
                header,
                footer);

        return card;
    }

    // =========================================================
    // DRIVER CARD
    // =========================================================

    private VBox createDriverCard() {

        VBox card = createCard();

        Label heading = sectionTitle(
                "ASSIGNED DRIVER");

        HBox driver = new HBox(12);
        driver.setAlignment(Pos.CENTER_LEFT);

        StackPane avatar = new StackPane();

        avatar.setPrefSize(58, 58);

        avatar.setStyle(
                "-fx-background-color: #DDECE2;" +
                "-fx-background-radius: 12;");

        Label person = new Label("♟");

        person.setFont(
                Font.font(
                        "Arial",
                        28));

        avatar.getChildren().add(person);

        VBox driverInfo = new VBox(5);

        Label name = new Label(assignedDriver == null
                ? "Driver details unavailable"
                : safeText(assignedDriver.getUsername()));

        name.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16));

        name.setTextFill(Color.web(TEXT));

        // The existing driver document does not contain a rating field.
        Label rating = new Label("Rating unavailable");

        rating.setFont(
                Font.font("Arial", 10));

        rating.setTextFill(Color.web(GREEN));

        driverInfo.getChildren().addAll(
                name,
                rating);

        driver.getChildren().addAll(
                avatar,
                driverInfo);

        card.getChildren().addAll(
                heading,
                driver,
                new Separator(),
                detailRow(
                        "Vehicle",
                        assignedVehicle == null
                                ? "Not available"
                                : safeText(assignedVehicle.getVehiclePlateNumber())),
                detailRow(
                        "Type",
                        assignedVehicle == null
                                ? "Not available"
                                : safeText(assignedVehicle.getVehicleType())));

        Button contact = new Button(
                "☎   Contact Driver");

        contact.setMaxWidth(Double.MAX_VALUE);
        contact.setPrefHeight(40);

        contact.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11));

        contact.setStyle(
                "-fx-background-color: #E6E5E4;" +
                "-fx-background-radius: 12;" +
                "-fx-text-fill: #252525;" +
                "-fx-cursor: hand;");

        card.getChildren().add(contact);

        return card;
    }

    // =========================================================
    // LOAD INVENTORY
    // =========================================================

    private VBox createLoadInventoryCard() {

        VBox card = createCard();

        Label heading = sectionTitle(
                "LOAD INVENTORY");

        VBox load = new VBox(5);

        load.setPadding(new Insets(13));

        load.setStyle(
                "-fx-background-color: #F8F4F3;" +
                "-fx-background-radius: 12;");

        Label loadTitle = new Label("LOAD TYPE");

        loadTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        9));

        String loadType =
                currentLoad != null
                        ? safeText(currentLoad.getLoadType())
                        : "No Load";

        Label loadValue = new Label(loadType);

        loadValue.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14));

        loadValue.setTextFill(Color.web(TEXT));

        load.getChildren().addAll(
                loadTitle,
                loadValue);

        HBox stats = new HBox(10);

        String weightText =
                currentLoad != null
                        ? safeText(currentLoad.getWeight()) +
                          " " +
                          safeText(currentLoad.getWeightUnit())
                        : "N/A";

        VBox weight = inventoryBox(
                "WEIGHT",
                weightText);

        String offerText =
                currentLoad != null
                        ? "₹" + String.format(
                                "%,.0f",
                                currentLoad.getOfferPrice())
                        : "N/A";

        VBox value = inventoryBox(
                "OFFER PRICE",
                offerText);

        HBox.setHgrow(weight, Priority.ALWAYS);
        HBox.setHgrow(value, Priority.ALWAYS);

        stats.getChildren().addAll(
                weight,
                value);

        card.getChildren().addAll(
                heading,
                load,
                stats);

        return card;
    }

    // =========================================================
    // CARD
    // =========================================================

    private VBox createCard() {

        VBox card = new VBox(15);

        card.setPadding(new Insets(20));

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 16;" +
                "-fx-effect: dropshadow(" +
                "gaussian," +
                "rgba(20,60,35,0.08)," +
                "12,0,0,3" +
                ");");

        return card;
    }

    // =========================================================
    // CARD HOVER
    // =========================================================

    private void addCardHoverEffect(
            VBox card,
            double radius) {

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: " + radius + ";" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: " + radius + ";" +
                "-fx-effect: dropshadow(" +
                "gaussian," +
                "rgba(20,60,35,0.08)," +
                "12,0,0,3" +
                ");");

        card.setOnMouseEntered(
                e -> card.setStyle(
                        "-fx-background-color: white;" +
                        "-fx-background-radius: " + radius + ";" +
                        "-fx-border-color: #BFD9C8;" +
                        "-fx-border-radius: " + radius + ";" +
                        "-fx-effect: dropshadow(" +
                        "gaussian," +
                        "rgba(8,122,54,0.16)," +
                        "18,0,0,4" +
                        ");"));

        card.setOnMouseExited(
                e -> card.setStyle(
                        "-fx-background-color: white;" +
                        "-fx-background-radius: " + radius + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: " + radius + ";" +
                        "-fx-effect: dropshadow(" +
                        "gaussian," +
                        "rgba(20,60,35,0.08)," +
                        "12,0,0,3" +
                        ");"));
    }

    // =========================================================
    // SECTION TITLE
    // =========================================================

    private Label sectionTitle(String text) {

        Label label = new Label(text);

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        10));

        label.setTextFill(Color.web(MUTED));

        return label;
    }

    // =========================================================
    // METRIC
    // =========================================================

    private VBox metric(
            String title,
            String value) {

        VBox box = new VBox(2);
        box.setAlignment(Pos.CENTER_RIGHT);

        Label titleLabel = new Label(title);

        titleLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        8));

        titleLabel.setTextFill(Color.web(MUTED));

        Label valueLabel = new Label(value);

        valueLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16));

        valueLabel.setTextFill(Color.web(TEXT));

        box.getChildren().addAll(
                titleLabel,
                valueLabel);

        return box;
    }

    // =========================================================
    // FOOTER METRIC
    // =========================================================

    private VBox footerMetric(
            String title,
            String value,
            String unit) {

        VBox box = new VBox(5);

        Label titleLabel = new Label(title);

        titleLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        9));

        titleLabel.setTextFill(Color.web(MUTED));

        HBox valueBox = new HBox(5);

        Label valueLabel = new Label(value);

        valueLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18));

        valueLabel.setTextFill(Color.web(TEXT));

        Label unitLabel = new Label(unit);

        unitLabel.setFont(
                Font.font("Arial", 10));

        unitLabel.setTextFill(Color.web(MUTED));

        valueBox.getChildren().addAll(
                valueLabel,
                unitLabel);

        box.getChildren().addAll(
                titleLabel,
                valueBox);

        return box;
    }

    // =========================================================
    // DETAIL ROW
    // =========================================================

    private HBox detailRow(
            String leftText,
            String rightText) {

        HBox row = new HBox();

        Label left = new Label(leftText);

        left.setFont(Font.font("Arial", 10));
        left.setTextFill(Color.web(MUTED));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label right = new Label(rightText);

        right.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        10));

        right.setTextFill(Color.web(TEXT));

        row.getChildren().addAll(
                left,
                spacer,
                right);

        return row;
    }

    // =========================================================
    // INVENTORY BOX
    // =========================================================

    private VBox inventoryBox(
            String title,
            String value) {

        VBox box = new VBox(5);

        box.setPadding(new Insets(12));

        box.setStyle(
                "-fx-background-color: #F8F4F3;" +
                "-fx-background-radius: 11;");

        Label titleLabel = new Label(title);

        titleLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        8));

        titleLabel.setTextFill(Color.web(MUTED));

        Label valueLabel = new Label(value);

        valueLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14));

        valueLabel.setTextFill(Color.web(TEXT));

        box.getChildren().addAll(
                titleLabel,
                valueLabel);

        return box;
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private String tripPickup() {
        return currentTrip == null ? "Not available" : recordedOrUnavailable(currentTrip.getPickupLocation());
    }

    private String tripDestination() {
        return currentTrip == null ? "Not available" : recordedOrUnavailable(currentTrip.getDestination());
    }

    private String tripRoute() {
        if (currentTrip == null) return "Not available";
        return tripPickup() + " → " + tripDestination();
    }

    private String loadSchedule() {
        if (currentLoad == null) return "Not recorded";
        String date = safeText(currentLoad.getPickupDate()).trim();
        String time = safeText(currentLoad.getPickupTime()).trim();
        if (date.isEmpty() && time.isEmpty()) return "Not recorded";
        return (date + " " + time).trim();
    }

    private String recordedOrUnavailable(String value) {
        return value == null || value.isBlank() ? "Not recorded" : value.trim();
    }

    private String safeText(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private void showMapMessage() {

        if (currentTrip == null) {
            System.out.println("No current trip available for map.");
            return;
        }

        System.out.println(
                "View on Map: " +
                        currentTrip.getPickupLocation() +
                        " → " +
                        currentTrip.getDestination());
    }
}
