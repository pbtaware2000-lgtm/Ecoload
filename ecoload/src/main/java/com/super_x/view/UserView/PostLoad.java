package com.super_x.view.UserView;

import javafx.geometry.Insets;

import com.super_x.controller.usercontroller.LoadController;

import com.super_x.model.usermodel.CurrentUser;
import com.super_x.model.usermodel.UserModel;
import com.super_x.model.usermodel.Load;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.ScrollPane;

public class PostLoad {

        UserModel currentUser = CurrentUser.getInstance().getUser();

        private TextField receiverNameField;
        private LoadController loadController = new LoadController();

        // =========================================================
        // COLORS
        // =========================================================

        private static final String GREEN = "#087A20";
        private static final String DARK_GREEN = "#075D19";
        private static final String LIGHT_GREEN = "#DDF2E1";
        private static final String LIGHT_BG = "#F6F8F7";
        private static final String FIELD_BG = "#F8F7F7";
        private static final String BORDER = "#D7DAD8";
        private static final String TEXT = "#163C39";
        private static final String MUTED = "#89908C";

        // =========================================================
        // FORM CONTROLS
        // =========================================================

        private TextField pickupField;
        private TextField dropField;

        private ComboBox<String> loadTypeCombo;
        private TextField weightField;
        private ComboBox<String> unitCombo;

        private ComboBox<String> truckTypeCombo;
        private TextField offerPriceField;

        private DatePicker pickupDate;
        private TextField pickupTime;

        private DatePicker deliveryDate;
        private TextField deliveryTime;

        // =========================================================
        // ROUTE INFORMATION
        // =========================================================

        private Label routeLabel;
        private Label distanceLabel;
        private Label travelTimeLabel;

        // =========================================================
        // CONSTRUCTOR
        // =========================================================

        public PostLoad() {
                // UI is created inside getPostLoadScene()
        }

        // =========================================================
        // GET POST LOAD SCENE
        // =========================================================

        public Scene getpostloadScene() {

                BorderPane root = new BorderPane();

                root.setStyle(
                                "-fx-background-color: " + LIGHT_BG + ";");

                // =====================================================
                // LEFT SIDEBAR - STATIC
                // =====================================================

                VBox sidebar = UserNavigation.createSidebar(
                                "Post Load");

                root.setLeft(sidebar);

                // =====================================================
                // RIGHT SIDE
                // =====================================================

                BorderPane content = new BorderPane();

                content.setStyle(
                                "-fx-background-color: " + LIGHT_BG + ";");

                // =====================================================
                // TOP NAVBAR - STATIC
                // =====================================================

                HBox navbar = UserNavigation.createNavbar();

                content.setTop(navbar);

                // =====================================================
                // MAIN PAGE CONTENT
                // =====================================================

                HBox mainLayout = new HBox(28);

                mainLayout.setPadding(
                                new Insets(
                                                30,
                                                28,
                                                40,
                                                28));

                mainLayout.setAlignment(
                                Pos.TOP_CENTER);

                // IMPORTANT:
                // Keep natural size of HBox.
                // Do not allow ScrollPane to destroy card layout.
                mainLayout.setMinHeight(
                                Region.USE_PREF_SIZE);

                mainLayout.setPrefHeight(
                                Region.USE_COMPUTED_SIZE);

                // =====================================================
                // LEFT CARD
                // =====================================================

                VBox leftCard = createPostShipmentCard();

                // =====================================================
                // RIGHT PANEL
                // =====================================================

                VBox rightPanel = createRightPanel();

                mainLayout.getChildren().addAll(
                                leftCard,
                                rightPanel);

                // =====================================================
                // WRAPPER
                //
                // VERY IMPORTANT:
                // ScrollPane resizes this VBox instead of directly
                // resizing mainLayout.
                // =====================================================

                VBox scrollContent = new VBox();

                scrollContent.setAlignment(
                                Pos.TOP_CENTER);

                scrollContent.setFillWidth(true);

                scrollContent.setMinHeight(
                                Region.USE_PREF_SIZE);

                scrollContent.setPrefHeight(
                                Region.USE_COMPUTED_SIZE);

                scrollContent.getChildren().add(
                                mainLayout);

                // =====================================================
                // SCROLL PANE
                // =====================================================

                ScrollPane scrollPane = new ScrollPane();

                scrollPane.setContent(
                                scrollContent);

                // Width follows available center area
                scrollPane.setFitToWidth(true);

                // DO NOT use fitToHeight(true)
                scrollPane.setFitToHeight(false);

                // Only vertical scrolling
                scrollPane.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.AS_NEEDED);

                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                // Mouse wheel scrolling
                scrollPane.setPannable(true);

                // Remove ScrollPane border/background
                scrollPane.setStyle(
                                "-fx-background-color: " + LIGHT_BG + ";"
                                                + "-fx-border-color: transparent;");

                // =====================================================
                // ONLY THIS PART SCROLLS
                // =====================================================

                content.setCenter(
                                scrollPane);

                // =====================================================
                // STATIC SIDEBAR + STATIC TOPBAR
                // =====================================================

                root.setCenter(
                                content);

                // =====================================================
                // SCENE
                // =====================================================

                Scene scene = new Scene(
                                root,
                                1536,
                                750);

                return scene;
        }
        // =========================================================
        // LEFT POST SHIPMENT CARD
        // =========================================================

        private VBox createPostShipmentCard() {

                VBox card = new VBox();

                card.setPrefWidth(750);
                card.setMinWidth(750);
                card.setMaxWidth(750);

                card.setPadding(
                                new Insets(
                                                34,
                                                34,
                                                28,
                                                34));

                card.setStyle(
                                "-fx-background-color: white;"
                                                + "-fx-background-radius: 18;"
                                                + "-fx-border-color: #E1E4E1;"
                                                + "-fx-border-radius: 18;"
                                                + "-fx-effect: dropshadow("
                                                + "gaussian, rgba(0,0,0,0.08), "
                                                + "10, 0.15, 0, 3);");

                // =====================================================
                // TITLE
                // =====================================================

                HBox titleRow = new HBox(14);

                titleRow.setAlignment(
                                Pos.CENTER_LEFT);

                Label plus = new Label("+");

                plus.setAlignment(
                                Pos.CENTER);

                plus.setPrefSize(
                                48,
                                48);

                plus.setStyle(
                                "-fx-background-color: white;"
                                                + "-fx-border-color: " + GREEN + ";"
                                                + "-fx-border-width: 2.5;"
                                                + "-fx-border-radius: 50;"
                                                + "-fx-background-radius: 50;"
                                                + "-fx-text-fill: " + GREEN + ";"
                                                + "-fx-font-size: 25px;"
                                                + "-fx-font-weight: bold;");

                Label title = new Label(
                                "Post New Shipment");

                title.setFont(
                                Font.font(
                                                "System",
                                                FontWeight.BOLD,
                                                25));

                title.setTextFill(
                                Color.web(GREEN));

                titleRow.getChildren().addAll(
                                plus,
                                title);

                // =====================================================
                // GRID
                // =====================================================

                GridPane grid = new GridPane();

                grid.setHgap(20);
                grid.setVgap(22);

                ColumnConstraints left = new ColumnConstraints();

                left.setPercentWidth(50);

                ColumnConstraints right = new ColumnConstraints();

                right.setPercentWidth(50);

                grid.getColumnConstraints().addAll(
                                left,
                                right);

                // =====================================================
                // PICKUP LOCATION
                // =====================================================

                VBox pickupBox = createLabeledField(
                                "Pickup Location");

                pickupField = createTextField(
                                "City, State or Warehouse");

                pickupBox.getChildren().add(
                                pickupField);

                // =====================================================
                // DROP LOCATION
                // =====================================================

                VBox dropBox = createLabeledField(
                                "Drop Location");

                dropField = createTextField(
                                "Destination Address");

                dropBox.getChildren().add(
                                dropField);

                grid.add(
                                pickupBox,
                                0,
                                0);

                grid.add(
                                dropBox,
                                1,
                                0);
                // =====================================================
                // RECEIVER NAME
                // =====================================================

                VBox receiverBox = createLabeledField(
                                "Receiver Name");

                receiverNameField = createTextField(
                                "Enter receiver name");

                receiverBox.getChildren().add(
                                receiverNameField);
                grid.add(
                                receiverBox,
                                0,
                                1,
                                2,
                                1);

                // =====================================================
                // LOAD TYPE
                // =====================================================

                VBox loadTypeBox = createLabeledField(
                                "Load Type");

                loadTypeCombo = new ComboBox<>();

                loadTypeCombo.getItems().addAll(
                                "General",
                                "Perishable",
                                "Fragile",
                                "Electronics",
                                "Furniture",
                                "Heavy Goods");

                loadTypeCombo.setPromptText(
                                "Select Type");

                styleCombo(
                                loadTypeCombo);

                loadTypeBox.getChildren().add(
                                loadTypeCombo);

                // =====================================================
                // WEIGHT + UNIT
                // =====================================================

                VBox weightUnitBox = new VBox();

                HBox weightUnitRow = new HBox(12);

                VBox weightBox = createLabeledField(
                                "Weight");

                weightField = createTextField(
                                "0.00");

                weightField.setText(
                                "0.00");

                weightBox.getChildren().add(
                                weightField);

                VBox unitBox = createLabeledField(
                                "Unit");

                unitCombo = new ComboBox<>();

                unitCombo.getItems().addAll(
                                "Ton",
                                "Quintal");

                unitCombo.setValue(
                                "Ton");

                unitCombo.setPrefWidth(
                                88);

                styleCombo(
                                unitCombo);

                unitBox.getChildren().add(
                                unitCombo);

                HBox.setHgrow(
                                weightBox,
                                Priority.ALWAYS);

                weightUnitRow.getChildren().addAll(
                                weightBox,
                                unitBox);

                weightUnitBox.getChildren().add(
                                weightUnitRow);

                grid.add(
                                loadTypeBox,
                                0,
                                2);

                grid.add(
                                weightUnitBox,
                                1,
                                2);

                // =====================================================
                // TRUCK TYPE
                // =====================================================

                VBox truckBox = createLabeledField(
                                "Required Truck Type");

                truckTypeCombo = new ComboBox<>();

                truckTypeCombo.getItems().addAll(
                                "Mini Truck",
                                "Pickup Truck",
                                "LCV",
                                "Medium Truck",
                                "Heavy Truck",
                                "Trailer",
                                "Container Truck",
                                "Tanker");

                truckTypeCombo.setPromptText(
                                "Select Truck Type");

                styleCombo(
                                truckTypeCombo);

                truckBox.getChildren().add(
                                truckTypeCombo);

                // =====================================================
                // OFFER PRICE
                // =====================================================

                VBox priceBox = createLabeledField(
                                "Offer Price (₹)");

                offerPriceField = createTextField(
                                "₹ Enter amount");

                priceBox.getChildren().add(
                                offerPriceField);

                grid.add(
                                truckBox,
                                0,
                                3);

                grid.add(
                                priceBox,
                                1,
                                3);

                // =====================================================
                // PICKUP DATE
                // =====================================================

                VBox pickupDateBox = createLabeledField(
                                "Pickup Date & Time");

                HBox pickupDateTime = createDateTimeControls();

                pickupDate = (DatePicker) pickupDateTime
                                .getChildren()
                                .get(0);

                pickupTime = (TextField) pickupDateTime
                                .getChildren()
                                .get(1);

                pickupDateBox.getChildren().add(
                                pickupDateTime);

                // =====================================================
                // DELIVERY DATE
                // =====================================================

                VBox deliveryDateBox = createLabeledField(
                                "Delivery Date & Time");

                HBox deliveryDateTime = createDateTimeControls();

                deliveryDate = (DatePicker) deliveryDateTime
                                .getChildren()
                                .get(0);

                deliveryTime = (TextField) deliveryDateTime
                                .getChildren()
                                .get(1);

                deliveryDateBox.getChildren().add(
                                deliveryDateTime);

                grid.add(
                                pickupDateBox,
                                0,
                                4);

                grid.add(
                                deliveryDateBox,
                                1,
                                4);

                VBox.setMargin(
                                grid,
                                new Insets(
                                                28,
                                                0,
                                                0,
                                                0));

                card.getChildren().addAll(
                                titleRow,
                                grid);

                return card;
        }

        // =========================================================
        // LABELED FIELD
        // =========================================================

        private VBox createLabeledField(
                        String text) {

                VBox box = new VBox(7);

                Label label = new Label(
                                text);

                label.setFont(
                                Font.font(
                                                "System",
                                                FontWeight.BOLD,
                                                16));

                label.setTextFill(
                                Color.BLACK);

                box.getChildren().add(
                                label);

                return box;
        }

        // =========================================================
        // TEXT FIELD
        // =========================================================

        private TextField createTextField(
                        String prompt) {

                TextField field = new TextField();

                field.setPromptText(
                                prompt);

                field.setPrefHeight(
                                58);

                field.setMaxWidth(
                                Double.MAX_VALUE);

                field.setFont(
                                Font.font(
                                                "System",
                                                14));

                field.setStyle(
                                "-fx-background-color: " + FIELD_BG + ";"
                                                + "-fx-border-color: " + BORDER + ";"
                                                + "-fx-border-width: 1.2;"
                                                + "-fx-border-radius: 9;"
                                                + "-fx-background-radius: 9;"
                                                + "-fx-padding: 0 14 0 14;"
                                                + "-fx-prompt-text-fill: " + MUTED + ";");

                return field;
        }

        // =========================================================
        // COMBO BOX
        // =========================================================

        private void styleCombo(
                        ComboBox<String> combo) {

                combo.setPrefHeight(
                                58);

                combo.setMaxWidth(
                                Double.MAX_VALUE);

                combo.setStyle(
                                "-fx-background-color: " + FIELD_BG + ";"
                                                + "-fx-border-color: " + BORDER + ";"
                                                + "-fx-border-width: 1.2;"
                                                + "-fx-border-radius: 9;"
                                                + "-fx-background-radius: 9;"
                                                + "-fx-font-size: 14px;");
        }

        // =========================================================
        // DATE + TIME
        // =========================================================

        private HBox createDateTimeControls() {

                DatePicker date = new DatePicker();

                date.setPrefHeight(
                                58);

                date.setPrefWidth(
                                310);

                date.setPromptText(
                                "mm/dd/yyyy");

                date.setStyle(
                                "-fx-background-color: white;"
                                                + "-fx-border-color: #BFC3C0;"
                                                + "-fx-border-width: 1.2;"
                                                + "-fx-border-radius: 4;"
                                                + "-fx-background-radius: 4;");

                TextField time = new TextField();

                time.setPrefHeight(
                                58);

                time.setPrefWidth(
                                120);

                time.setPromptText(
                                "--:--");

                time.setStyle(
                                "-fx-background-color: " + FIELD_BG + ";"
                                                + "-fx-border-color: " + BORDER + ";"
                                                + "-fx-border-width: 1.2;"
                                                + "-fx-border-radius: 9;"
                                                + "-fx-background-radius: 9;"
                                                + "-fx-padding: 0 14 0 14;"
                                                + "-fx-prompt-text-fill: " + MUTED + ";");

                HBox box = new HBox(10);

                box.setAlignment(
                                Pos.CENTER_LEFT);

                box.getChildren().addAll(
                                date,
                                time);

                return box;
        }

        // =========================================================
        // RIGHT PANEL
        // =========================================================

        private VBox createRightPanel() {

                VBox panel = new VBox(24);

                panel.setPrefWidth(
                                400);

                panel.setMinWidth(
                                400);

                panel.setMaxWidth(
                                400);

                // =====================================================
                // ROUTE CARD
                // =====================================================

                VBox routeCard = createRouteCard();

                // =====================================================
                // POST LOAD BUTTON
                // =====================================================

                Button postLoad = new Button(
                                "Post Load  🚀");

                postLoad.setPrefHeight(
                                74);

                postLoad.setMaxWidth(
                                Double.MAX_VALUE);

                postLoad.setFont(
                                Font.font(
                                                "System",
                                                FontWeight.BOLD,
                                                21));

                postLoad.setStyle(
                                "-fx-background-color: " + GREEN + ";"
                                                + "-fx-background-radius: 14;"
                                                + "-fx-text-fill: white;"
                                                + "-fx-cursor: hand;");

                // =====================================================
                // HOVER EFFECT
                // =====================================================

                postLoad.setOnMouseEntered(
                                e -> postLoad.setStyle(
                                                "-fx-background-color: " + DARK_GREEN + ";"
                                                                + "-fx-background-radius: 14;"
                                                                + "-fx-text-fill: white;"
                                                                + "-fx-cursor: hand;"));

                postLoad.setOnMouseExited(
                                e -> postLoad.setStyle(
                                                "-fx-background-color: " + GREEN + ";"
                                                                + "-fx-background-radius: 14;"
                                                                + "-fx-text-fill: white;"
                                                                + "-fx-cursor: hand;"));

                // =====================================================
                // POST LOAD -> TERMS & CONDITIONS
                // =====================================================

                postLoad.setOnAction(
                                event -> {

                                        TermsAndConditions terms = new TermsAndConditions(
                                                        () -> {

                                                                // =================================================
                                                                // TERMS ACCEPTED
                                                                // =================================================

                                                                System.out.println(
                                                                                "Terms & Conditions accepted.");
                                                                String pickupLocation = pickupField.getText();
                                                                String destination = dropField.getText();

                                                                String loadType = loadTypeCombo.getValue();

                                                                double weight = Double.parseDouble(
                                                                                weightField.getText());

                                                                String weightUnit = unitCombo.getValue();

                                                                String truckType = truckTypeCombo.getValue();

                                                                double offerPrice = Double.parseDouble(
                                                                                offerPriceField.getText());

                                                                String pickupDateValue = pickupDate.getValue()
                                                                                .toString();

                                                                String pickupTimeValue = pickupTime.getText();

                                                                String deliveryDateValue = deliveryDate.getValue()
                                                                                .toString();

                                                                String deliveryTimeValue = deliveryTime.getText();
                                                                Load savedLoad = loadController.saveLoad(
                                                                                currentUser.getEmail(),
                                                                                currentUser.getUsername(),
                                                                                pickupLocation,
                                                                                destination,
                                                                                loadType,
                                                                                weight,
                                                                                weightUnit,
                                                                                truckType,
                                                                                offerPrice,
                                                                                pickupDateValue,
                                                                                pickupTimeValue,
                                                                                deliveryDateValue,
                                                                                deliveryTimeValue,
                                                                                receiverNameField.getText());

                                                                if (savedLoad == null) {

                                                                        System.out.println("Load could not be saved.");
                                                                        return;
                                                                }

                                                                System.out.println(
                                                                                "Load saved successfully: "
                                                                                                + savedLoad.getLoadId());

                                                                // =================================================
                                                                // POST LOAD ACTION
                                                                // =================================================
                                                                //
                                                                // Your existing LoadController
                                                                // backend logic can be called here.
                                                                //
                                                                // Example:
                                                                //
                                                                // LoadController controller =
                                                                // new LoadController();
                                                                //
                                                                // controller.postLoad(...);
                                                                //
                                                                // =================================================
                                                        });

                                        terms.show();
                                });

                // =====================================================
                // SAVE AS DRAFT
                // =====================================================

                Button draft = new Button(
                                "Save as Draft");

                draft.setPrefHeight(
                                48);

                draft.setMaxWidth(
                                Double.MAX_VALUE);

                draft.setStyle(
                                "-fx-background-color: white;"
                                                + "-fx-border-color: #555555;"
                                                + "-fx-border-width: 1.2;"
                                                + "-fx-border-radius: 26;"
                                                + "-fx-background-radius: 26;"
                                                + "-fx-text-fill: #333333;"
                                                + "-fx-font-size: 14px;"
                                                + "-fx-cursor: hand;");

                // =====================================================
                // CANCEL
                // =====================================================

                Button cancel = new Button(
                                "Cancel");

                cancel.setPrefHeight(
                                48);

                cancel.setMaxWidth(
                                Double.MAX_VALUE);

                cancel.setStyle(
                                "-fx-background-color: white;"
                                                + "-fx-border-color: #E53935;"
                                                + "-fx-border-width: 1.2;"
                                                + "-fx-border-radius: 26;"
                                                + "-fx-background-radius: 26;"
                                                + "-fx-text-fill: #E53935;"
                                                + "-fx-font-size: 14px;"
                                                + "-fx-cursor: hand;");

                HBox actionButtons = new HBox(14);

                HBox.setHgrow(
                                draft,
                                Priority.ALWAYS);

                HBox.setHgrow(
                                cancel,
                                Priority.ALWAYS);

                actionButtons.getChildren().addAll(
                                draft,
                                cancel);

                // =====================================================
                // ECOSAFE GUARANTEE
                // =====================================================

                VBox ecoSafe = createEcoSafeCard();

                panel.getChildren().addAll(
                                routeCard,
                                postLoad,
                                actionButtons,
                                ecoSafe);

                return panel;
        }

        // =========================================================
        // ROUTE CARD
        // =========================================================
        /* Removed AI recommendation implementation.
        private VBox createAIRecommendationCard() {

                VBox card = new VBox(12);

                card.setPadding(new Insets(20));

                card.setMinHeight(315);
                card.setPrefHeight(Region.USE_COMPUTED_SIZE);

                card.setStyle(
                                "-fx-background-color: white;"
                                                + "-fx-background-radius: 18;"
                                                + "-fx-border-color: #D7DAD8;"
                                                + "-fx-border-radius: 18;"
                                                + "-fx-effect: dropshadow("
                                                + "gaussian, rgba(0,0,0,0.08), "
                                                + "8, 0.15, 0, 2);");

                // =====================================================
                // AI TITLE
                // =====================================================

                Label title = new Label("🤖 EcoLoad AI Recommendation");

                title.setFont(
                                Font.font(
                                                "System",
                                                FontWeight.BOLD,
                                                18));

                title.setTextFill(
                                Color.web(GREEN));

                // =====================================================
                // AI STATUS
                // =====================================================

                Label status = new Label(
                                "AI will recommend the best driver after posting the load.");

                status.setWrapText(true);

                status.setFont(
                                Font.font(
                                                "System",
                                                13));

                status.setTextFill(
                                Color.web(MUTED));

                // =====================================================
                // DRIVER RESULT
                // =====================================================

                Label recommendation = new Label(
                                "No recommendation yet.");

                recommendation.setWrapText(true);

                recommendation.setFont(
                                Font.font(
                                                "System",
                                                FontWeight.BOLD,
                                                14));

                recommendation.setTextFill(
                                Color.web(TEXT));

                card.getChildren().addAll(
                                title,
                                status,
                                recommendation);

                return card;
        }

        private void updateAIRecommendationCard(
                        String aiResponse,
                        String loadId) {

                if (aiRecommendationCard == null) {
                        return;
                }

                aiRecommendationCard.getChildren().clear();

                Label title = new Label("🤖 EcoLoad AI Recommendation");

                title.setFont(
                                Font.font("System", FontWeight.BOLD, 18));
                title.setTextFill(Color.web(GREEN));

                Label status = new Label("✓ AI analysis completed");

                status.setFont(
                                Font.font("System", FontWeight.BOLD, 13));
                status.setTextFill(Color.web(GREEN));

                String cleanResponse = aiResponse
                                .replace("**", "")
                                .replace("\\r", "");

                String driverName = "Not available";
                String driverEmail = "Not available";
                String vehicleName = "Not available";
                String vehicleType = "Not available";
                String vehicleCapacity = "Not available";
                String reason = "No reason provided.";

                String[] lines = cleanResponse.split("\\n");

                for (String line : lines) {

                        line = line.trim();

                        if (line.isEmpty()) {
                                continue;
                        }

                        line = line.replaceFirst("^[*-]\\s*", "");

                        if (line.startsWith("Driver Name:")) {

                                driverName = line.substring("Driver Name:".length()).trim();

                        } else if (line.startsWith("Driver Email:")) {

                                driverEmail = line.substring("Driver Email:".length()).trim();

                        } else if (line.startsWith("Vehicle:")) {

                                vehicleName = line.substring("Vehicle:".length()).trim();

                        } else if (line.startsWith("Vehicle Type:")) {

                                vehicleType = line.substring("Vehicle Type:".length()).trim();

                        } else if (line.startsWith("Vehicle Capacity:")) {

                                vehicleCapacity = line.substring("Vehicle Capacity:".length()).trim();

                        } else if (line.startsWith("Reason:")) {

                                reason = line.substring("Reason:".length()).trim();
                        }
                }

                Label bestMatch = new Label("🏆 BEST DRIVER MATCH");

                bestMatch.setFont(
                                Font.font("System", FontWeight.BOLD, 14));
                bestMatch.setTextFill(Color.web(GREEN));

                Label driver = new Label(driverName);
                driver.setFont(
                                Font.font("System", FontWeight.BOLD, 20));
                driver.setTextFill(Color.web(TEXT));

                Label email = new Label(driverEmail);
                email.setFont(Font.font("System", 12));
                email.setTextFill(Color.web(MUTED));

                Label vehicle = new Label("🚛 " + vehicleName);

                vehicle.setFont(
                                Font.font("System", FontWeight.BOLD, 14));
                vehicle.setTextFill(Color.web(TEXT));

                Label vehicleInfo = new Label(
                                vehicleType
                                                + " • "
                                                + vehicleCapacity);

                vehicleInfo.setFont(Font.font("System", 13));
                vehicleInfo.setTextFill(Color.web(MUTED));

                Label reasonTitle = new Label("AI Reason");

                reasonTitle.setFont(
                                Font.font("System", FontWeight.BOLD, 13));
                reasonTitle.setTextFill(Color.web(TEXT));

                Label reasonLabel = new Label(reason);

                reasonLabel.setWrapText(true);
                reasonLabel.setMaxWidth(400);
                reasonLabel.setMaxHeight(40);
                reasonLabel.setFont(Font.font("System", 12));
                reasonLabel.setTextFill(Color.web(MUTED));

                // =====================================================
                // SEND TRANSPORT REQUEST
                // =====================================================

                Button sendRequest = new Button("Send Transport Request");

                sendRequest.setPrefHeight(45);
                sendRequest.setMaxWidth(Double.MAX_VALUE);

                sendRequest.setFont(
                                Font.font("System", FontWeight.BOLD, 14));

                sendRequest.setStyle(
                                "-fx-background-color: " + GREEN + ";"
                                                + "-fx-background-radius: 10;"
                                                + "-fx-text-fill: white;"
                                                + "-fx-cursor: hand;");

                final String selectedDriverEmail = driverEmail;
                final String selectedDriverName = driverName;
                final String selectedVehicleType = vehicleType;

                sendRequest.setOnAction(event -> {

                        try {

                                if (loadId == null || loadId.isBlank()) {

                                        status.setText("✗ Load ID is missing");
                                        status.setTextFill(Color.web("#C62828"));
                                        return;
                                }

                                if (selectedDriverEmail == null
                                                || selectedDriverEmail.isBlank()
                                                || "Not available".equalsIgnoreCase(
                                                                selectedDriverEmail)) {

                                        status.setText(
                                                        "✗ Driver information is missing");
                                        status.setTextFill(Color.web("#C62828"));
                                        return;
                                }

                                // =================================================
                                // GET EXISTING DRIVER + VEHICLE DATA
                                // =================================================

                                var firestore = com.super_x.config.FirebaseConfig
                                                .getFireStore();

                                DriverDAO driverDAO = new DriverDAO(firestore);

                                VehicleDAO vehicleDAO = new VehicleDAO(firestore);

                                var driverData = driverDAO.getDriverByEmail(
                                                selectedDriverEmail);

                                var vehicleData = vehicleDAO.getVehicleByDriverEmail(
                                                selectedDriverEmail);

                                // Current EcoLoad driver document ID is email.
                                String driverId = selectedDriverEmail;

                                // Read existing model properties safely.
                                String phone = readStringProperty(
                                                driverData,
                                                "getPhone",
                                                "getPhoneNumber",
                                                "getMobile");

                                String vehicleNumber = readStringProperty(
                                                vehicleData,
                                                "getVehicleNumber",
                                                "getNumber",
                                                "getRegistrationNumber");

                                String actualVehicleType = readStringProperty(
                                                vehicleData,
                                                "getVehicleType",
                                                "getType");

                                if (actualVehicleType == null
                                                || actualVehicleType.isBlank()) {

                                        actualVehicleType = selectedVehicleType;
                                }

                                // =================================================
                                // CREATE TRANSPORT REQUEST
                                // =================================================

                                transportRequestController
                                                .createTransportRequest(
                                                                loadId,
                                                                driverId,
                                                                selectedDriverName,
                                                                phone == null ? "" : phone,
                                                                vehicleNumber == null
                                                                                ? ""
                                                                                : vehicleNumber,
                                                                actualVehicleType == null
                                                                                ? ""
                                                                                : actualVehicleType);

                                // =================================================
                                // UPDATE UI
                                // =================================================

                                status.setText(
                                                "✓ Request sent to "
                                                                + selectedDriverName);

                                status.setTextFill(Color.web(GREEN));

                                sendRequest.setText("✓ Request Sent");
                                sendRequest.setDisable(true);

                                System.out.println();
                                System.out.println(
                                                "===== TRANSPORT REQUEST SENT =====");
                                System.out.println(
                                                "Load ID: " + loadId);
                                System.out.println(
                                                "Driver: " + selectedDriverName);
                                System.out.println(
                                                "Driver Email: " + selectedDriverEmail);
                                System.out.println(
                                                "==================================");

                        } catch (Exception e) {

                                System.err.println(
                                                "Failed to send transport request.");

                                e.printStackTrace();

                                status.setText(
                                                "✗ Failed to send request");
                                status.setTextFill(
                                                Color.web("#C62828"));
                        }
                });

                aiRecommendationCard.getChildren().addAll(
                                title,
                                status,
                                bestMatch,
                                driver,
                                email,
                                vehicle,
                                vehicleInfo,
                                reasonTitle,
                                reasonLabel,
                                sendRequest);
        }

        // =========================================================
        // SAFE MODEL PROPERTY READER
        // =========================================================

        private String readStringProperty(
                        Object object,
                        String... getterNames) {

                if (object == null) {
                        return "";
                }

                for (String getterName : getterNames) {

                        try {

                                Object value = object.getClass()
                                                .getMethod(getterName)
                                                .invoke(object);

                                if (value != null) {
                                        return value.toString().trim();
                                }

                        } catch (Exception ignored) {
                                // Try the next getter name.
                        }
                }

                return "";
        }

        */
        private VBox createRouteCard() {

                VBox card = new VBox();

                card.setPrefHeight(
                                315);

                card.setStyle(
                                "-fx-background-color: white;"
                                                + "-fx-background-radius: 20;"
                                                + "-fx-border-color: #E0E4E0;"
                                                + "-fx-border-radius: 20;");

                // =====================================================
                // ROUTE HEADER
                // =====================================================

                VBox greenHeader = new VBox(13);

                greenHeader.setPadding(
                                new Insets(
                                                31,
                                                28,
                                                30,
                                                28));

                greenHeader.setPrefHeight(
                                175);

                greenHeader.setStyle(
                                "-fx-background-color: linear-gradient("
                                                + "to right, #8FB6A0, #0B3D1D);"
                                                + "-fx-background-radius: 20 20 0 0;");

                Label routeTitle = new Label(
                                "ROUTE OVERVIEW");

                routeTitle.setFont(
                                Font.font(
                                                "System",
                                                FontWeight.BOLD,
                                                13));

                routeTitle.setTextFill(
                                Color.WHITE);

                routeLabel = new Label(
                                "Pickup → Destination");

                routeLabel.setFont(
                                Font.font(
                                                "System",
                                                FontWeight.BOLD,
                                                21));

                routeLabel.setTextFill(
                                Color.WHITE);

                greenHeader.getChildren().addAll(
                                routeTitle,
                                routeLabel);

                // =====================================================
                // DISTANCE
                // =====================================================

                HBox distanceRow = new HBox();

                distanceRow.setAlignment(
                                Pos.CENTER_LEFT);

                distanceRow.setPadding(
                                new Insets(
                                                20,
                                                28,
                                                20,
                                                28));

                Label distanceIcon = new Label("⌁");

                distanceIcon.setFont(
                                Font.font(
                                                "System",
                                                25));

                distanceIcon.setTextFill(
                                Color.web(GREEN));

                Label distanceText = new Label(
                                "Distance");

                distanceText.setFont(
                                Font.font(
                                                "System",
                                                14));

                distanceLabel = new Label(
                                "1,422 KM");

                distanceLabel.setFont(
                                Font.font(
                                                "System",
                                                14));

                Region distanceSpacer = new Region();

                HBox.setHgrow(
                                distanceSpacer,
                                Priority.ALWAYS);

                distanceRow.getChildren().addAll(
                                distanceIcon,
                                distanceText,
                                distanceSpacer,
                                distanceLabel);

                // =====================================================
                // DIVIDER
                // =====================================================

                Region divider = new Region();

                divider.setPrefHeight(
                                1);

                divider.setStyle(
                                "-fx-background-color: #E5E5E5;");

                // =====================================================
                // TRAVEL TIME
                // =====================================================

                HBox timeRow = new HBox();

                timeRow.setAlignment(
                                Pos.CENTER_LEFT);

                timeRow.setPadding(
                                new Insets(
                                                20,
                                                28,
                                                20,
                                                28));

                Label clock = new Label("◷");

                clock.setFont(
                                Font.font(
                                                "System",
                                                25));

                clock.setTextFill(
                                Color.web(GREEN));

                Label timeText = new Label(
                                "Est. Travel Time");

                timeText.setFont(
                                Font.font(
                                                "System",
                                                14));

                travelTimeLabel = new Label(
                                "~28 HRS");

                travelTimeLabel.setFont(
                                Font.font(
                                                "System",
                                                14));

                Region timeSpacer = new Region();

                HBox.setHgrow(
                                timeSpacer,
                                Priority.ALWAYS);

                timeRow.getChildren().addAll(
                                clock,
                                timeText,
                                timeSpacer,
                                travelTimeLabel);

                card.getChildren().addAll(
                                greenHeader,
                                distanceRow,
                                divider,
                                timeRow);

                return card;
        }

        // =========================================================
        // ECOSAFE GUARANTEE
        // =========================================================

        private VBox createEcoSafeCard() {

                VBox card = new VBox();

                card.setPrefHeight(
                                105);

                card.setPadding(
                                new Insets(18));

                card.setStyle(
                                "-fx-background-color: " + GREEN + ";"
                                                + "-fx-background-radius: 17;");

                // =====================================================
                // ICON
                // =====================================================

                Label icon = new Label("♢");

                icon.setAlignment(
                                Pos.CENTER);

                icon.setPrefSize(
                                48,
                                48);

                icon.setStyle(
                                "-fx-background-color: #E4F3E7;"
                                                + "-fx-background-radius: 11;"
                                                + "-fx-text-fill: " + GREEN + ";"
                                                + "-fx-font-size: 22px;");

                // =====================================================
                // TEXT
                // =====================================================

                Label title = new Label(
                                "EcoSafe Guarantee");

                title.setFont(
                                Font.font(
                                                "System",
                                                FontWeight.BOLD,
                                                14));

                title.setTextFill(
                                Color.WHITE);

                Label description = new Label(
                                "Your transaction is protected.\n"
                                                + "Payments are only released upon "
                                                + "successful delivery verification.");

                description.setFont(
                                Font.font(
                                                "System",
                                                11));

                description.setTextFill(
                                Color.WHITE);

                description.setWrapText(
                                true);

                VBox text = new VBox(4);

                text.getChildren().addAll(
                                title,
                                description);

                HBox row = new HBox(16);

                row.setAlignment(
                                Pos.CENTER_LEFT);

                row.getChildren().addAll(
                                icon,
                                text);

                card.getChildren().add(
                                row);

                return card;
        }
}
