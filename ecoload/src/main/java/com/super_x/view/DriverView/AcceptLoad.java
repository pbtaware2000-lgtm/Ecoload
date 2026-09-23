package com.super_x.view.DriverView;

import com.super_x.config.FirebaseConfig;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.super_x.controller.drivercontroller.TripController;
import com.super_x.dao.driverdao.VehicleDAO;
import com.super_x.dao.userdao.LoadDao;
import com.super_x.model.drivermodel.CurrentDriver;
import com.super_x.model.drivermodel.DriverModel;
import com.super_x.model.drivermodel.VehicleModel;
import com.super_x.model.usermodel.Load;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AcceptLoad {

    // =========================================================
    // COLORS
    // =========================================================

    private static final String GREEN = "#0B6B2A";
    private static final String DARK_GREEN = "#07551F";
    private static final String LIGHT_GREEN = "#c6e6cd";
    private static final String BORDER_GREEN = "#B7DFC1";

    private static final String TEXT_DARK = "#202820";
    private static final String TEXT_GRAY = "#667169";
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_GRAY = "#F6F8F7";

    private boolean loadAccepted;

    // =========================================================
    // GET ACCEPT STATUS
    // =========================================================

    public boolean isLoadAccepted() {
        return loadAccepted;
    }

    // =========================================================
    // SHOW ACCEPT LOAD POPUP
    // =========================================================

    public void show(
            Stage owner,
            Load load
    ) {

        if (load == null) {
            return;
        }

        loadAccepted = false;

        Stage popup = new Stage();

        popup.initOwner(owner);
        popup.initModality(Modality.WINDOW_MODAL);
        popup.setTitle("Accept Load");
        popup.setResizable(false);

        // =====================================================
        // MAIN ROOT
        // =====================================================

        VBox root = new VBox(14);

        root.setPadding(
                new Insets(22)
        );

        root.setStyle(
                "-fx-background-color: "
                        + WHITE
                        + ";"
        );

        // =====================================================
        // HEADER
        // =====================================================

        HBox header = new HBox(12);

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        Label headerIcon =
                new Label("✓");

        headerIcon.setAlignment(
                Pos.CENTER
        );

        headerIcon.setPrefSize(
                42,
                42
        );

        headerIcon.setStyle(
                "-fx-background-color: "
                        + LIGHT_GREEN
                        + ";"
                        + "-fx-background-radius: 12;"
                        + "-fx-border-color: "
                        + BORDER_GREEN
                        + ";"
                        + "-fx-border-radius: 12;"
                        + "-fx-font-size: 21px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + GREEN
                        + ";"
        );

        VBox headerText =
                new VBox(3);

        Label heading =
                new Label("Accept Load");

        heading.setStyle(
                "-fx-font-size: 23px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + DARK_GREEN
                        + ";"
        );

        Label subtitle =
                new Label(
                        "Review the shipment details before accepting."
                );

        subtitle.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-text-fill: "
                        + TEXT_GRAY
                        + ";"
        );

        headerText.getChildren().addAll(
                heading,
                subtitle
        );

        header.getChildren().addAll(
                headerIcon,
                headerText
        );

        // =====================================================
        // LOAD SUMMARY CARD
        // =====================================================

        VBox summaryCard =
                new VBox(12);

        summaryCard.setPadding(
                new Insets(16)
        );

        summaryCard.setStyle(
                "-fx-background-color: "
                        + LIGHT_GREEN
                        + ";"
                        + "-fx-background-radius: 12;"
                        + "-fx-border-color: "
                        + BORDER_GREEN
                        + ";"
                        + "-fx-border-radius: 12;"
        );

        HBox summaryHeader =
                new HBox();

        Label summaryTitle =
                new Label("LOAD SUMMARY");

        summaryTitle.setStyle(
                "-fx-font-size: 11px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + GREEN
                        + ";"
        );

        Region summarySpacer =
                new Region();

        HBox.setHgrow(
                summarySpacer,
                Priority.ALWAYS
        );

        Label status =
                new Label("AVAILABLE");

        status.setStyle(
                "-fx-background-color: #D2F0D9;"
                        + "-fx-background-radius: 20;"
                        + "-fx-padding: 4 9;"
                        + "-fx-font-size: 9px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + GREEN
                        + ";"
        );

        summaryHeader.getChildren().addAll(
                summaryTitle,
                summarySpacer,
                status
        );

        // =====================================================
        // DETAILS GRID
        // =====================================================

        GridPane details =
                new GridPane();

        details.setHgap(30);
        details.setVgap(14);

        ColumnConstraints column1 =
                new ColumnConstraints();

        ColumnConstraints column2 =
                new ColumnConstraints();

        column1.setPercentWidth(50);
        column2.setPercentWidth(50);

        details.getColumnConstraints().addAll(
                column1,
                column2
        );

        // =====================================================
        // ROUTE
        // =====================================================

        addDetail(
                details,
                0,
                0,
                "ROUTE",
                load.getPickupLocation()
                        + " → "
                        + load.getDestination(),
                false
        );

        // =====================================================
        // FREIGHT OFFER
        // =====================================================

        addDetail(
                details,
                1,
                0,
                "FREIGHT OFFER",
                "₹"
                        + String.format(
                                "%,.0f",
                                load.getOfferPrice()
                        ),
                true
        );

        // =====================================================
        // WEIGHT
        // =====================================================

        addDetail(
                details,
                0,
                1,
                "WEIGHT",
                String.format(
                        "%.1f",
                        load.getWeight()
                )
                        + " "
                        + load.getWeightUnit(),
                false
        );

        // =====================================================
        // TRUCK TYPE
        // =====================================================

        addDetail(
                details,
                1,
                1,
                "TRUCK TYPE",
                load.getTruckType(),
                false
        );

        summaryCard.getChildren().addAll(
                summaryHeader,
                details
        );

        // =====================================================
        // TERMS HEADING
        // =====================================================

        HBox termsHeader =
                new HBox();

        Label termsHeading =
                new Label("Terms & Conditions");

        termsHeading.setStyle(
                "-fx-font-size: 16px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + TEXT_DARK
                        + ";"
        );

        Region termsSpacer =
                new Region();

        HBox.setHgrow(
                termsSpacer,
                Priority.ALWAYS
        );

        Label required =
                new Label(
                        "Required before acceptance"
                );

        required.setStyle(
                "-fx-font-size: 10px;"
                        + "-fx-text-fill: "
                        + TEXT_GRAY
                        + ";"
        );

        termsHeader.getChildren().addAll(
                termsHeading,
                termsSpacer,
                required
        );

        // =====================================================
        // TERMS GRID
        // =====================================================

        GridPane termsGrid =
                new GridPane();

        termsGrid.setHgap(10);
        termsGrid.setVgap(10);

        ColumnConstraints termsColumn1 =
                new ColumnConstraints();

        ColumnConstraints termsColumn2 =
                new ColumnConstraints();

        termsColumn1.setPercentWidth(50);
        termsColumn2.setPercentWidth(50);

        termsGrid.getColumnConstraints().addAll(
                termsColumn1,
                termsColumn2
        );

        // =====================================================
        // ROW 1
        // =====================================================

        termsGrid.add(
                createTerm(
                        "🚛",
                        "Vehicle Suitability",
                        "Ensure your vehicle is suitable for this load."
                ),
                0,
                0
        );

        termsGrid.add(
                createTerm(
                        "📍",
                        "Pickup On Time",
                        "Arrive at the pickup location at the scheduled time."
                ),
                1,
                0
        );

        // =====================================================
        // ROW 2
        // =====================================================

        termsGrid.add(
                createTerm(
                        "📄",
                        "Verify Documents",
                        "Check the load and required documents before departure."
                ),
                0,
                1
        );

        termsGrid.add(
                createTerm(
                        "📦",
                        "Secure Cargo",
                        "Ensure the cargo is properly loaded and secured."
                ),
                1,
                1
        );

        // =====================================================
        // ROW 3
        // =====================================================

        termsGrid.add(
                createTerm(
                        "🛡",
                        "Shipment Safety",
                        "Keep the shipment safe throughout the journey."
                ),
                0,
                2
        );

        termsGrid.add(
                createTerm(
                        "🚦",
                        "Safety Regulations",
                        "Follow all applicable traffic and safety regulations."
                ),
                1,
                2
        );

        // =====================================================
        // ROW 4
        // =====================================================

        termsGrid.add(
                createTerm(
                        "⚠",
                        "Report Issues",
                        "Report delays, accidents, or issues through EcoLoad support."
                ),
                0,
                3
        );

        termsGrid.add(
                createTerm(
                        "✓",
                        "Timely Delivery",
                        "Deliver the shipment to the specified destination within the agreed time."
                ),
                1,
                3
        );

        // =====================================================
        // ROW 5
        // =====================================================

        termsGrid.add(
                createTerm(
                        "✍",
                        "Proof of Delivery",
                        "Obtain proof of delivery from the recipient after delivery."
                ),
                0,
                4
        );

        termsGrid.add(
                createTerm(
                        "⊘",
                        "Prohibited Goods",
                        "Do not transport prohibited or undeclared goods."
                ),
                1,
                4
        );

        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane(
                        termsGrid
                );

        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setPrefHeight(
                235
        );

        scrollPane.setStyle(
                "-fx-background-color: transparent;"
                        + "-fx-background: transparent;"
                        + "-fx-border-color: transparent;"
        );

        // =====================================================
        // AGREEMENT
        // =====================================================

        HBox agreementBox =
                new HBox(10);

        agreementBox.setAlignment(
                Pos.CENTER_LEFT
        );

        agreementBox.setPadding(
                new Insets(
                        10,
                        12,
                        10,
                        12
                )
        );

        agreementBox.setStyle(
                "-fx-background-color: "
                        + LIGHT_GRAY
                        + ";"
                        + "-fx-background-radius: 9;"
                        + "-fx-border-color: #E1E7E3;"
                        + "-fx-border-radius: 9;"
        );

        CheckBox agreement =
                new CheckBox();

        Label agreementText =
                new Label(
                        "I have read and agree to the above Terms & Conditions"
                );

        agreementText.setWrapText(
                true
        );

        agreementText.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + TEXT_DARK
                        + ";"
        );

        agreementBox.getChildren().addAll(
                agreement,
                agreementText
        );

        // =====================================================
        // CANCEL BUTTON
        // =====================================================

        Button cancel =
                new Button("Cancel");

        cancel.setPrefHeight(40);
        cancel.setPrefWidth(95);

        cancel.setStyle(
                "-fx-background-color: white;"
                        + "-fx-text-fill: #000000;"
                        + "-fx-border-color: #C9D1CC;"
                        + "-fx-border-radius: 8;"
                        + "-fx-background-radius: 8;"
                        + "-fx-font-size: 12px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-cursor: hand;"
        );

        cancel.setOnAction(
                event -> popup.close()
        );

        // =====================================================
        // ACCEPT BUTTON
        // =====================================================

        Button accept =
                new Button("✓  Accept Load");

        accept.setPrefHeight(40);
        accept.setPrefWidth(150);

        accept.disableProperty().bind(
                agreement
                        .selectedProperty()
                        .not()
        );

        // Initial style
        accept.setStyle(
                "-fx-background-color: #3a9c54;"
                        + "-fx-text-fill: #ffffff;"
                        + "-fx-border-color: #11f14c;"
                        + "-fx-border-radius: 8;"
                        + "-fx-background-radius: 8;"
                        + "-fx-font-size: 12px;"
                        + "-fx-font-weight: bold;"
        );

        // =====================================================
        // ACCEPT BUTTON STYLE CHANGE
        // =====================================================

        agreement
                .selectedProperty()
                .addListener(
                        (
                                observable,
                                oldValue,
                                selected
                        ) -> {

                            if (selected) {

                                accept.setStyle(
                                        "-fx-background-color: #0B6B2A;"
                                                + "-fx-text-fill: white;"
                                                + "-fx-border-color: #0B6B2A;"
                                                + "-fx-border-radius: 8;"
                                                + "-fx-background-radius: 8;"
                                                + "-fx-font-size: 12px;"
                                                + "-fx-font-weight: bold;"
                                                + "-fx-cursor: hand;"
                                );

                            } else {

                                accept.setStyle(
                                        "-fx-background-color: #3b814d;"
                                                + "-fx-text-fill: #ffffff;"
                                                + "-fx-border-color: #00ff44;"
                                                + "-fx-border-radius: 8;"
                                                + "-fx-background-radius: 8;"
                                                + "-fx-font-size: 12px;"
                                                + "-fx-font-weight: bold;"
                                );
                            }
                        }
                );

        // =====================================================
        // ACCEPT LOAD ACTION
        // =====================================================

        accept.setOnAction(
                event -> {

                    try {

                        // =====================================
                        // CURRENT DRIVER
                        // =====================================

                        DriverModel currentDriver =
                                CurrentDriver
                                        .getInstance()
                                        .getDriver();

                        if (currentDriver == null) {

                            System.out.println(
                                    "Current driver not found."
                            );

                            return;
                        }

                        // =====================================
                        // GET DRIVER VEHICLE
                        // =====================================

                        VehicleDAO vehicleDAO =
                                new VehicleDAO(
                                        FirebaseConfig
                                                .getFireStore()
                                );

                        VehicleModel currentVehicle =
                                vehicleDAO
                                        .getVehicleByDriverEmail(
                                                currentDriver
                                                        .getEmail()
                                        );

                        if (currentVehicle == null) {

                            System.out.println(
                                    "Vehicle information not found."
                            );

                            return;
                        }

                        // =====================================
                        // TRUCK CAPACITY
                        // =====================================

                        double truckCapacity =
                                currentVehicle
                                        .getVehicleCapacity();

                        // =====================================
                        // CURRENT LOAD WEIGHT
                        // =====================================

                        double loadWeight =
                                load.getWeight();

                        System.out.println(
                                "================================="
                        );

                        System.out.println(
                                "LOAD ACCEPT REQUEST"
                        );

                        System.out.println(
                                "Truck Capacity: "
                                        + truckCapacity
                                        + " Ton"
                        );

                        System.out.println(
                                "Current Load Weight: "
                                        + loadWeight
                                        + " Ton"
                        );

                        // =====================================
                        // CHECK LOAD STATUS
                        // =====================================

                        // Available Loads exposes Accept only for the existing
                        // pending state.  Do not create/add a trip for a stale,
                        // accepted, cancelled, or completed selection.
                        if (load.getStatus() == null
                                || !load.getStatus().trim().equalsIgnoreCase("PENDING")) {
                            System.out.println("Load is no longer pending and cannot be accepted.");
                            return;
                        }

                        // =====================================
                        // FIRST CAPACITY CHECK
                        // =====================================

                        if (loadWeight > truckCapacity) {

                            System.out.println(
                                    "Load cannot be accepted."
                            );

                            System.out.println(
                                    "Required Capacity: "
                                            + loadWeight
                                            + " Ton"
                            );

                            System.out.println(
                                    "Available Capacity: "
                                            + truckCapacity
                                            + " Ton"
                            );

                            return;
                        }

                        // =====================================
                        // CREATE OR ADD TO ACTIVE TRIP
                        // =====================================

                        TripController tripController =
                                new TripController();

                        tripController.createOrAddTrip(

                                load.getLoadId(),

                                load.getUserId(),

                                currentDriver
                                        .getEmail(),

                                currentDriver
                                        .getUsername(),

                                load.getPickupLocation(),

                                load.getDestination(),

                                truckCapacity,

                                loadWeight
                        );

                        // =====================================
                        // UPDATE LOAD IN FIREBASE
                        // =====================================

                        LoadDao loadDao =
                                new LoadDao();

                        String acceptedAt =
                                new java.text
                                        .SimpleDateFormat(
                                                "yyyy-MM-dd HH:mm:ss"
                                        )
                                        .format(
                                                new java.util.Date()
                                        );

                        loadDao.updateLoadStatus(

                                load.getLoadId(),

                                "ACCEPTED",

                                currentDriver
                                        .getEmail(),

                                currentDriver
                                        .getUsername(),

                                acceptedAt
                        );

                        // =====================================
                        // UPDATE AI TRANSPORT REQUEST
                        // PENDING -> ACCEPTED
                        // =====================================

                        try {

                            Firestore db =
                                    FirebaseConfig.getFireStore();

                            for (QueryDocumentSnapshot requestDocument :
                                    db.collection("transportRequests")
                                            .whereEqualTo(
                                                    "loadId",
                                                    load.getLoadId())
                                            .whereEqualTo(
                                                    "driverId",
                                                    currentDriver.getEmail())
                                            .get()
                                            .get()
                                            .getDocuments()) {

                                requestDocument.getReference()
                                        .update("status", "ACCEPTED")
                                        .get();

                                System.out.println(
                                        "Transport Request status updated: "
                                                + requestDocument.getId()
                                                + " -> ACCEPTED");
                            }

                        } catch (Exception requestError) {

                            System.err.println(
                                    "Failed to update Transport Request status.");

                            requestError.printStackTrace();
                        }

                        // =====================================
                        // LOCAL LOAD OBJECT UPDATE
                        // =====================================

                        load.setStatus(
                                "ACCEPTED"
                        );

                        load.setDriverId(
                                currentDriver
                                        .getEmail()
                        );

                        load.setDriverName(
                                currentDriver
                                        .getUsername()
                        );

                        load.setAcceptedAt(
                                acceptedAt
                        );

                        // =====================================
                        // ACCEPT SUCCESS
                        // =====================================

                        loadAccepted = true;

                        System.out.println(
                                "Load accepted successfully."
                        );

                        System.out.println(
                                "Load Status: ACCEPTED"
                        );

                        System.out.println(
                                "Load will disappear "
                                        + "from Available Loads."
                        );

                        System.out.println(
                                "Load will appear "
                                        + "inside Active Trip."
                        );

                        System.out.println(
                                "================================="
                        );

                        popup.close();

                    } catch (Exception e) {

                        e.printStackTrace();

                        System.out.println(
                                "Failed to accept load."
                        );
                    }
                }
        );

        // =====================================================
        // BUTTON SPACER
        // =====================================================

        Region buttonSpacer =
                new Region();

        HBox.setHgrow(
                buttonSpacer,
                Priority.ALWAYS
        );

        HBox actions =
                new HBox(
                        10,
                        buttonSpacer,
                        cancel,
                        accept
                );

        actions.setAlignment(
                Pos.CENTER_RIGHT
        );

        // =====================================================
        // ADD EVERYTHING
        // =====================================================

        root.getChildren().addAll(

                header,

                summaryCard,

                new Separator(),

                termsHeader,

                scrollPane,

                agreementBox,

                actions
        );

        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(
                        root,
                        680,
                        700
                );

        popup.setScene(
                scene
        );

        popup.showAndWait();
    }

    // =============================================================
    // LOAD DETAIL
    // =============================================================

    private void addDetail(
            GridPane grid,
            int column,
            int row,
            String label,
            String value,
            boolean highlight
    ) {

        VBox detail =
                new VBox(3);

        Label name =
                new Label(label);

        name.setStyle(
                "-fx-font-size: 10px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #65736A;"
        );

        Label text =
                new Label(value);

        if (highlight) {

            text.setStyle(
                    "-fx-font-size: 18px;"
                            + "-fx-font-weight: bold;"
                            + "-fx-text-fill: "
                            + GREEN
                            + ";"
            );

        } else {

            text.setStyle(
                    "-fx-font-size: 14px;"
                            + "-fx-font-weight: bold;"
                            + "-fx-text-fill: "
                            + DARK_GREEN
                            + ";"
            );
        }

        detail.getChildren().addAll(
                name,
                text
        );

        grid.add(
                detail,
                column,
                row
        );
    }

    // =============================================================
    // TERM CARD
    // =============================================================

    private VBox createTerm(
            String icon,
            String title,
            String description
    ) {

        VBox card =
                new VBox(5);

        card.setPrefHeight(82);
        card.setMinHeight(82);

        card.setPadding(
                new Insets(10)
        );

        card.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 9;"
                        + "-fx-border-color: #E0E7E2;"
                        + "-fx-border-radius: 9;"
        );

        HBox top =
                new HBox(8);

        top.setAlignment(
                Pos.CENTER_LEFT
        );

        Label iconLabel =
                new Label(icon);

        iconLabel.setAlignment(
                Pos.CENTER
        );

        iconLabel.setMinSize(
                28,
                28
        );

        iconLabel.setPrefSize(
                28,
                28
        );

        iconLabel.setStyle(
                "-fx-background-color: "
                        + LIGHT_GREEN
                        + ";"
                        + "-fx-background-radius: 7;"
                        + "-fx-font-size: 14px;"
                        + "-fx-text-fill: "
                        + GREEN
                        + ";"
        );

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 11px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + TEXT_DARK
                        + ";"
        );

        top.getChildren().addAll(
                iconLabel,
                titleLabel
        );

        Label descriptionLabel =
                new Label(description);

        descriptionLabel.setWrapText(
                true
        );

        descriptionLabel.setStyle(
                "-fx-font-size: 10px;"
                        + "-fx-text-fill: "
                        + TEXT_GRAY
                        + ";"
        );

        card.getChildren().addAll(
                top,
                descriptionLabel
        );

        return card;
    }
}
