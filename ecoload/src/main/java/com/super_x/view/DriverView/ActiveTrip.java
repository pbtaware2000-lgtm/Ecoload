package com.super_x.view.DriverView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.super_x.model.drivermodel.ActiveTripManager;
import com.super_x.model.drivermodel.CurrentDriver;
import com.super_x.model.drivermodel.DriverModel;
import com.super_x.model.drivermodel.Trip;
import com.super_x.dao.userdao.LoadDao;
import com.super_x.model.usermodel.Load;

public class ActiveTrip {

    private Scene tripDetailScene;
    private Trip currentTrip;
    private Load currentLoad;
    private final ActiveTripManager activeTripManager = ActiveTripManager.getInstance();
    private final LoadDao loadDao = new LoadDao();

   

    private final String BORDER = "#E5E7EB";
    private final String TEXT = "#171A1F";
    private final String MUTED = "#737780";
    private final String BG = "#e6f1e8";

    private final String GREEN = "#0B6B2A";

    public Scene getTripDetailsScene() {
        loadActiveTrip();

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color:" + BG + ";");

        // =========================================================
        // SIDEBAR
        // =========================================================

        root.setLeft(
                DriverNavigation.createSidebar("Active Trip"));

        // =========================================================
        // CONTENT AREA
        // =========================================================

        BorderPane contentArea = new BorderPane();

        contentArea.setTop(
                DriverNavigation.createNavbar());

        // =========================================================
        // MAIN CONTENT
        // =========================================================

        VBox content = new VBox(10);

        content.setPadding(
                new Insets(18, 28, 8, 28));

        // =========================================================
        // HEADER
        // =========================================================

        BorderPane heading = new BorderPane();

        VBox headingLeft = new VBox(6);

        Label tripDetails = new Label(
                "Trip Details");

        tripDetails.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        25));

        tripDetails.setTextFill(
                Color.web(TEXT));

        String routeText;

        if (currentTrip != null) {

        routeText =
                currentTrip.getPickupLocation()
                        + "   →   "
                        + currentTrip.getDestination();

        } else {

        routeText = "Route not available";
        }

        Label route = new Label(routeText);

        route.setFont(
                Font.font(
                        "Arial",
                        FontWeight.NORMAL,
                        15));

        route.setTextFill(
                Color.web(MUTED));

        headingLeft.getChildren().addAll(
                tripDetails,
                route);

        Label available = new Label(
                "●  AVAILABLE");

        available.setPadding(
                new Insets(7, 14, 7, 14));

        available.setTextFill(
                Color.web("#38945D"));

        available.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12));

        available.setStyle(
                "-fx-background-color: #EAF8EF;" +
                        "-fx-background-radius: 20;");

        heading.setLeft(
                headingLeft);

        Button deliveryProof = new Button("Delivery Proof");
        deliveryProof.setStyle(
                "-fx-background-color: #0B6B2A;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;");
        deliveryProof.setDisable(currentTrip == null || currentLoad == null);
        deliveryProof.setOnAction(event -> new DeliveryProofDialog(currentTrip, currentLoad)
                .show((Stage) deliveryProof.getScene().getWindow()));

        HBox tripActions = new HBox(10, available, deliveryProof);
        tripActions.setAlignment(Pos.CENTER_RIGHT);
        heading.setRight(tripActions);

        // =========================================================
        // ROUTE PREVIEW
        // =========================================================

        VBox routeContainer = new VBox();

        Label routeTitle = new Label(
                "ROUTE PREVIEW");

        routeTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13));

        routeTitle.setTextFill(
                Color.web("#383C42"));

        routeTitle.setPadding(
                new Insets(10, 18, 10, 18));

        HBox mapArea = createMapPreview();

        routeContainer.getChildren().addAll(
                routeTitle,
                mapArea);

        routeContainer.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;");

        // =========================================================
        // SHIPMENT CARDS SECTION
        // =========================================================

        VBox shipmentSection = createShipmentSection();

        // =========================================================
        // ADD CONTENT
        // =========================================================

        content.getChildren().addAll(
                heading,
                routeContainer,
                shipmentSection);

        contentArea.setCenter(
                content);

        root.setCenter(
                contentArea);

        // =========================================================
        // SCENE
        // =========================================================

        Scene scene = new Scene(
                root,
                1536,
                750);

        tripDetailScene = scene;

        return tripDetailScene;
    }

    private void loadActiveTrip() {

    try {

        DriverModel driver =
                CurrentDriver.getInstance().getDriver();

        if (driver == null ||
                driver.getEmail() == null ||
                driver.getEmail().trim().isEmpty()) {

            System.out.println("Current driver not found.");
            currentTrip = null;
            return;
        }

        currentTrip = activeTripManager.getOrLoadForDriver(
                driver.getEmail().trim());

        if (currentTrip != null) {

    System.out.println(
            "Active Trip Found: "
                    + currentTrip.getTripId()
    );

    System.out.println(
            "Route: "
                    + currentTrip.getPickupLocation()
                    + " → "
                    + currentTrip.getDestination()
    );

    // =====================================================
    // FETCH ACTUAL LOAD
    // =====================================================

    currentLoad =
            loadDao.getLoadById(
                    currentTrip.getLoadId()
            );

    if (currentLoad != null) {

        System.out.println(
                "Active Load Found: "
                        + currentLoad.getLoadId()
        );

        System.out.println(
                "Transporter: "
                        + currentLoad.getTransporterName()
        );

        System.out.println(
                "Receiver: "
                        + currentLoad.getReceiverName()
        );

        System.out.println(
                "Load Type: "
                        + currentLoad.getLoadType()
        );

        System.out.println(
                "Weight: "
                        + currentLoad.getWeight()
                        + " "
                        + currentLoad.getWeightUnit()
        );

        } else {

                System.out.println(
                        "Active Load data not found."
                );
        }

        } else {

            System.out.println(
                    "No active trip found."
            );
        }

    } catch (Exception e) {

        e.printStackTrace();
        currentTrip = null;
    }
}

    // =============================================================
    // MAP PREVIEW
    // =============================================================

    private HBox createMapPreview() {

        HBox mainBox = new HBox(10);

        mainBox.setPrefHeight(250);
        mainBox.setMinHeight(250);
        mainBox.setMaxHeight(250);

        mainBox.setPadding(
                new Insets(10));

        mainBox.setStyle(
                "-fx-background-color: #E8E7DC;");

        // =========================================================
        // LEFT SIDE - LOCATION
        // =========================================================

        VBox popup = new VBox(7);

        popup.setPrefWidth(230);
        popup.setMinWidth(230);
        popup.setMaxWidth(230);

        popup.setAlignment(
                Pos.TOP_LEFT);

        popup.setPadding(
                new Insets(22));

        popup.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #E5E5E5;" +
                        "-fx-border-radius: 10;");

        // =========================================================
        // PICKUP
        // =========================================================

       // =========================================================
        // PICKUP LOCATION
        // =========================================================

        Label pickupTitle = new Label(
                "PICKUP LOCATION"
        );

        pickupTitle.setTextFill(
                Color.web("#9A9DA2")
        );

        pickupTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11
                )
        );

        // =========================================================
        // DYNAMIC PICKUP LOCATION
        // =========================================================

        String pickupText;

        if (currentTrip != null) {

        pickupText =
                currentTrip.getPickupLocation();

        } else {

        pickupText =
                "Pickup not available";
        }

        Label pickupLocation =
                new Label(
                        pickupText
                );

        pickupLocation.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16)
                );
        // =========================================================
        // DROP OFF
        // =========================================================

       // =========================================================
// DROP-OFF LOCATION
// =========================================================

Label dropTitle = new Label(
        "DROP-OFF LOCATION"
);

dropTitle.setTextFill(
        Color.web("#9A9DA2")
);

dropTitle.setFont(
        Font.font(
                "Arial",
                FontWeight.BOLD,
                11
        )
);

// =========================================================
// DYNAMIC DROP-OFF LOCATION
// =========================================================

        String dropText;

        if (currentTrip != null) {

        dropText =
                currentTrip.getDestination();

        } else {

        dropText =
                "Destination not available";
        }

        Label dropLocation =
                new Label(
                        dropText
                );

        dropLocation.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16
                )
        );

        // =========================================================
        // DISTANCE
        // =========================================================

        Separator separator = new Separator();

        Label distanceTitle = new Label(
                "↗   APPROX. DISTANCE");

        distanceTitle.setTextFill(
                Color.web("#9A9DA2"));

        distanceTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11));

        Label distance = new Label(
                "213 km • 4 h 35 min");

        distance.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        16));

        popup.getChildren().addAll(
                pickupTitle,
                pickupLocation,
                dropTitle,
                dropLocation,
                separator,
                distanceTitle,
                distance);

        // =========================================================
        // RIGHT SIDE - ACTIVE TRIP
        // =========================================================

        DriverDashoard driverDashoard = new DriverDashoard();
        driverDashoard.setCurrentTrip(currentTrip);

        VBox activeTripBlock = driverDashoard.createActiveTrip();

        activeTripBlock.setPrefWidth(910);
        activeTripBlock.setMinWidth(910);
        activeTripBlock.setMaxWidth(910);

        activeTripBlock.setPrefHeight(230);
        activeTripBlock.setMinHeight(230);
        activeTripBlock.setMaxHeight(230);

        HBox.setHgrow(
                popup,
                Priority.ALWAYS);

        HBox.setHgrow(
                activeTripBlock,
                Priority.ALWAYS);

        mainBox.getChildren().addAll(
                popup,
                activeTripBlock);

        return mainBox;
    }

    // =============================================================
    // SHIPMENT SECTION
    // =============================================================

    private VBox createShipmentSection() {

        VBox section = new VBox(8);

        // ---------------------------------------------------------
        // SECTION HEADER
        // ---------------------------------------------------------

        HBox header = new HBox();

        Label title = new Label(
                "SHIPMENT CONTACTS");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15));

        title.setTextFill(
                Color.web("#33383E"));

        Label subtitle = new Label(
                "Transporter and receiver details");

        subtitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.NORMAL,
                        11));

        subtitle.setTextFill(
                Color.web("#858A91"));

        VBox heading = new VBox(
                2,
                title,
                subtitle);

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        Label scrollHint = new Label(
                "Scroll →");

        scrollHint.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        10));

        scrollHint.setTextFill(
                Color.web("#737780"));

        header.setAlignment(
                Pos.CENTER_LEFT);

        header.getChildren().addAll(
                heading,
                spacer,
                scrollHint);

        // ---------------------------------------------------------
        // HORIZONTAL CARDS
        // ---------------------------------------------------------

        HBox cards = new HBox(14);

        cards.setPadding(
                new Insets(4, 4, 10, 4));

        cards.setAlignment(
                Pos.TOP_LEFT);

        // =========================================================
        // CARD 1
        // =========================================================
if (currentLoad != null) {

    String orderId =
            "#" + currentTrip.getLoadId();

    String transporter =
            currentLoad.getTransporterName();

    String receiver =
            currentLoad.getReceiverName();

    String goods =
            currentLoad.getLoadType();

    String weight =
            currentLoad.getWeight()
                    + " "
                    + currentLoad.getWeightUnit();

    cards.getChildren().add(
            createShipmentCard(
                    orderId,
                    transporter,
                    "Contact unavailable",
                    receiver,
                    "Contact unavailable",
                    goods,
                    weight
            )
        );

        } else {

        cards.getChildren().add(
                new Label("No active shipment found.")
        );
}


        // ---------------------------------------------------------
        // SCROLL PANE
        // ---------------------------------------------------------

        ScrollPane scrollPane = new ScrollPane(cards);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(false);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setPannable(true);

        scrollPane.setPrefHeight(242);
        scrollPane.setMinHeight(242);
        scrollPane.setMaxHeight(242);

        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;" +
                        "-fx-border-color: transparent;");

        section.getChildren().addAll(
                header,
                scrollPane);

        return section;
    }

    // =============================================================
    // SHIPMENT CARD
    // =============================================================

    private VBox createShipmentCard(
            String orderId,
            String transporter,
            String transporterPhone,
            String receiver,
            String receiverPhone,
            String goods,
            String weight) {

        VBox card = new VBox(9);

        card.setPrefWidth(300);
        card.setMinWidth(300);
        card.setMaxWidth(300);

        card.setPrefHeight(220);
        card.setMinHeight(220);
        card.setMaxHeight(220);

        card.setPadding(
                new Insets(14));

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #DDE5DF;" +
                        "-fx-border-radius: 12;");

        // =========================================================
        // CARD HEADER
        // =========================================================

        HBox orderHeader = new HBox();

        Label order = new Label(
                orderId);

        order.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11));

        order.setTextFill(
                Color.web(GREEN));

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        Label status = new Label(
                "ACTIVE");

        status.setPadding(
                new Insets(4, 8, 4, 8));

        status.setStyle(
                "-fx-background-color: #EAF8EF;" +
                        "-fx-background-radius: 20;" +
                        "-fx-text-fill: #249A60;" +
                        "-fx-font-size: 9px;" +
                        "-fx-font-weight: bold;");

        orderHeader.getChildren().addAll(
                order,
                spacer,
                status);

        // =========================================================
        // TRANSPORTER
        // =========================================================

        VBox transporterBox = createContactBox(
                "TRANSPORTER",
                transporter,
                transporterPhone,
                "#EDF6FF",
                "#2574B9",
                "JL");

        // =========================================================
        // RECEIVER
        // =========================================================

        VBox receiverBox = createContactBox(
                "RECEIVER",
                receiver,
                receiverPhone,
                "#FAF1FF",
                "#8A43B7",
                getInitials(receiver));

        // =========================================================
        // CONTACTS GRID
        // =========================================================

        VBox contacts = new VBox(6);

        contacts.getChildren().addAll(
                transporterBox,
                receiverBox);

        // =========================================================
        // GOODS
        // =========================================================

        HBox goodsBox = new HBox();

        goodsBox.setAlignment(
                Pos.CENTER_LEFT);

        Label goodsLabel = new Label(
                "📦  " + goods + "  •  " + weight);

        goodsLabel.setPadding(
                new Insets(6, 8, 6, 8));

        goodsLabel.setMaxWidth(
                Double.MAX_VALUE);

        goodsLabel.setStyle(
                "-fx-background-color: #F1F7F3;" +
                        "-fx-background-radius: 6;" +
                        "-fx-text-fill: #0B6B2A;" +
                        "-fx-font-size: 10px;" +
                        "-fx-font-weight: bold;");

        HBox.setHgrow(
                goodsLabel,
                Priority.ALWAYS);

        goodsBox.getChildren().add(
                goodsLabel);

        // =========================================================
        // VIEW DETAILS
        // =========================================================

        Button viewButton = new Button(
                "View Goods Details  →");

        viewButton.setMaxWidth(
                Double.MAX_VALUE);

        viewButton.setPrefHeight(34);

        viewButton.setStyle(
                "-fx-background-color: #0B6B2A;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 7;" +
                        "-fx-font-size: 10px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;");

        viewButton.setOnAction(
                event -> showGoodsDetails(
                        orderId,
                        transporter,
                        transporterPhone,
                        receiver,
                        receiverPhone,
                        goods,
                        weight));

        // =========================================================
        // ADD CONTENT
        // =========================================================

        card.getChildren().addAll(
                orderHeader,
                contacts,
                goodsBox,
                viewButton);

        return card;
    }

    // =============================================================
    // CONTACT BOX
    // =============================================================

    private VBox createContactBox(
            String type,
            String name,
            String phone,
            String background,
            String textColor,
            String initials) {

        VBox box = new VBox(3);

        box.setPadding(
                new Insets(8, 10, 8, 10));

        box.setPrefHeight(57);
        box.setMinHeight(57);
        box.setMaxHeight(57);

        box.setStyle(
                "-fx-background-color: " + background + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-opacity: 1.0;");

        // =====================================================
        // TOP ROW
        // =====================================================

        HBox top = new HBox(9);

        top.setAlignment(
                Pos.CENTER_LEFT);

        // =====================================================
        // AVATAR
        // =====================================================

        Circle avatar = new Circle(18);

        avatar.setFill(
                Color.WHITE);

        avatar.setStroke(
                Color.web(textColor));

        avatar.setStrokeWidth(1.3);

        Label initialsLabel = new Label(
                initials);

        initialsLabel.setTextFill(
                Color.web(textColor));

        initialsLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        9));

        StackPane avatarBox = new StackPane(
                avatar,
                initialsLabel);

        avatarBox.setPrefSize(36, 36);
        avatarBox.setMinSize(36, 36);
        avatarBox.setMaxSize(36, 36);

        // =====================================================
        // TEXT
        // =====================================================

        VBox details = new VBox(1);

        // Type
        Label typeLabel = new Label(
                type);

        typeLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        8));

        typeLabel.setTextFill(
                Color.web("#52605A"));

        typeLabel.setStyle(
                "-fx-text-fill: #52605A;");

        // Name
        Label nameLabel = new Label(
                name);

        nameLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11));

        nameLabel.setTextFill(
                Color.web("#18231D"));

        nameLabel.setStyle(
                "-fx-text-fill: #18231D;" +
                        "-fx-font-weight: bold;");

        nameLabel.setWrapText(false);

        // Phone
        Label phoneLabel = new Label(
                "☎  " + phone);

        phoneLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.NORMAL,
                        9));

        phoneLabel.setTextFill(
                Color.web("#4F5B54"));

        phoneLabel.setStyle(
                "-fx-text-fill: #4F5B54;");

        details.getChildren().addAll(
                typeLabel,
                nameLabel,
                phoneLabel);

        top.getChildren().addAll(
                avatarBox,
                details);

        box.getChildren().add(
                top);

        return box;
    }

    // =============================================================
    // VIEW GOODS DETAILS
    // =============================================================

    private void showGoodsDetails(
            String orderId,
            String transporter,
            String transporterPhone,
            String receiver,
            String receiverPhone,
            String goods,
            String weight) {

        Stage popup = new Stage();

        popup.setTitle(
                "Load Details");

        popup.setResizable(
                false);

        popup.initModality(
                Modality.APPLICATION_MODAL);

        VBox root = new VBox(15);

        root.setPadding(
                new Insets(22));

        root.setStyle(
                "-fx-background-color: white;");

        // =========================================================
        // HEADER
        // =========================================================

        HBox header = new HBox();

        VBox titleBox = new VBox(3);

        Label title = new Label(
                "Load Details");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22));

        title.setTextFill(
                Color.web(GREEN));

        Label orderLabel = new Label(
                orderId);

        orderLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11));

        orderLabel.setTextFill(
                Color.web("#7A847D"));

        titleBox.getChildren().addAll(
                title,
                orderLabel);

        header.getChildren().add(
                titleBox);

        // =========================================================
        // GOODS CARD
        // =========================================================

        VBox goodsCard = new VBox(8);

        goodsCard.setPadding(
                new Insets(16));

        goodsCard.setStyle(
                "-fx-background-color: #EAF8EF;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #B7DFC1;" +
                        "-fx-border-radius: 10;");

        Label goodsTitle = new Label(
                "GOODS INFORMATION");

        goodsTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        10));

        goodsTitle.setTextFill(
                Color.web(GREEN));

        Label goodsName = new Label(
                goods);

        goodsName.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18));

        goodsName.setTextFill(
                Color.web(TEXT));

        Label weightLabel = new Label(
                "Weight: " + weight);

        weightLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.NORMAL,
                        12));

        weightLabel.setTextFill(
                Color.web("#5D6860"));

        goodsCard.getChildren().addAll(
                goodsTitle,
                goodsName,
                weightLabel);

        // =========================================================
        // TRANSPORTER + RECEIVER
        // =========================================================

        HBox people = new HBox(12);

        VBox transporter1 = createPopupPerson(
                "TRANSPORTER",
                transporter,
                transporterPhone,
                "#308de9",
                "#2574B9");

        VBox receiverBox = createPopupPerson(
                "RECEIVER",
                receiver,
                receiverPhone,
                "#9e5ac3",
                "#8A43B7");

        HBox.setHgrow(
                transporter1,
                Priority.ALWAYS);

        HBox.setHgrow(
                receiverBox,
                Priority.ALWAYS);

        people.getChildren().addAll(
                transporter1,
                receiverBox);

        // =========================================================
        // CLOSE
        // =========================================================

        HBox bottom = new HBox();

        bottom.setAlignment(
                Pos.CENTER_RIGHT);

        Button close = new Button(
                "Close");

        close.setPrefWidth(
                100);

        close.setPrefHeight(
                36);

        close.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: #202820;" +
                        "-fx-border-color: #C9D1CC;" +
                        "-fx-border-radius: 7;" +
                        "-fx-background-radius: 7;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;");

        close.setOnAction(
                event -> popup.close());

        bottom.getChildren().add(
                close);

        root.getChildren().addAll(
                header,
                goodsCard,
                people,
                bottom);

        Scene scene = new Scene(
                root,
                600,
                390);

        popup.setScene(
                scene);

        popup.showAndWait();
    }

    // =============================================================
    // POPUP PERSON CARD
    // =============================================================

    private VBox createPopupPerson(
            String title,
            String name,
            String phone,
            String background,
            String textColor) {

        VBox box = new VBox(5);

        box.setPadding(
                new Insets(12));

        box.setPrefWidth(
                280);

        box.setStyle(
                "-fx-background-color: " + background + ";" +
                        "-fx-background-radius: 9;" +
                        "-fx-border-color: #E1E7E3;" +
                        "-fx-border-radius: 9;");

        Label titleLabel = new Label(
                title);

        titleLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        9));

        titleLabel.setTextFill(
                Color.web("#7A847D"));

        Label nameLabel = new Label(
                name);

        nameLabel.setWrapText(
                true);

        nameLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12));

        nameLabel.setTextFill(
                Color.web(TEXT));

        Label phoneLabel = new Label(
                "☎  " + phone);

        phoneLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.NORMAL,
                        10));

        phoneLabel.setTextFill(
                Color.web(textColor));

        box.getChildren().addAll(
                titleLabel,
                nameLabel,
                phoneLabel);

        return box;
    }

    // =============================================================
    // GET INITIALS
    // =============================================================

   private String getInitials(String name) {

    if (name == null || name.trim().isEmpty()) {
        return "NA";
    }

    String[] words =
            name.trim().split("\\s+");

    if (words.length == 1) {

        return words[0]
                .substring(
                        0,
                        Math.min(
                                2,
                                words[0].length()
                        )
                )
                .toUpperCase();
    }

    return (
            words[0].substring(0, 1)
                    + words[words.length - 1]
                            .substring(0, 1)
    ).toUpperCase();
}
}
