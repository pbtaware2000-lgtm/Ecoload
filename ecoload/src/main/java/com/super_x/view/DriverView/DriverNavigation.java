package com.super_x.view.DriverView;

import com.super_x.config.FirebaseConfig;
import com.super_x.controller.drivercontroller.SupportController;
import com.super_x.dao.driverdao.SupportDAO;
import com.super_x.dao.driverdao.VehicleDAO;
import com.super_x.model.drivermodel.CurrentDriver;
import com.super_x.model.drivermodel.DriverModel;
import com.super_x.model.drivermodel.VehicleModel;

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
import com.super_x.view.HomePage;
import com.super_x.view.Login;
import com.super_x.view.Chatbot.EcoloadAssistantOverlay;

/** Shared dashboard navigation used by every driver-facing screen. */
public final class DriverNavigation {
        private static final String GREEN = "#014B3A";
        private static final String LIGHT_GREEN = "#075F49";
        private static final String BORDER = "#075F49";

        private DriverNavigation() {
        }

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
                                                "-fx-border-width: 0 1 0 0;"

                );

                // =========================================================
                // ECOLOAD DRIVER LOGO
                // =========================================================

                ImageView logo = new ImageView(
                                new Image(
                                                DriverNavigation.class.getResourceAsStream(
                                                                "/assets/icons/EcoloadLogodriver.png")));

                logo.setFitWidth(225);
                logo.setPreserveRatio(true);

                HBox logoBox = new HBox();
                logoBox.setAlignment(Pos.CENTER);
                logoBox.getChildren().add(logo);

                logoBox.setPadding(new Insets(5, 0, 0, 0));

                VBox menu = new VBox(0);
                menu.getChildren().addAll(
                                menuButton("▦", "Dashboard", activePage),
                                menuButton("▣", "Available Loads", activePage),
                                menuButton("♧", "Active Trip", activePage),
                                menuButton("↶", "Trip History", activePage),
                                menuButton("₹", "Earnings", activePage),
                                menuButton("💬", "Ecoload Assistant", activePage),
                                menuButton("☆", "Ratings", activePage),
                                menuButton("♧", "Support", activePage),
                                menuButton("AI", "AI Vehicle Health", activePage),
                                truckWalaButton(activePage));

                Region spacer = new Region();
                menu.getChildren().removeIf(node -> node instanceof Button button
                                && button.getGraphic() instanceof HBox graphic
                                && graphic.getChildren().stream().anyMatch(child -> child instanceof Label label
                                && "Ecoload Assistant".equals(label.getText())));
                VBox.setVgrow(spacer, Priority.ALWAYS);

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

                        CurrentDriver.getInstance().clearDriver();

                        HomePage.homeStage.setScene(
                                        new Login().getScene());
                });

                sidebar.getChildren().addAll(logoBox, menu, spacer, logout);
                EcoloadAssistantOverlay.installForDriver();
                return sidebar;
        }

        public static HBox createNavbar() {

                HBox header = new HBox();

                header.setAlignment(
                                Pos.CENTER_LEFT);

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
                // =========================================================
                // WELCOME TEXT
                // =========================================================

                DriverModel driver = CurrentDriver.getInstance().getDriver();

                String name = "Driver";
                String initialsText = "DR";

                if (driver != null) {

                        if (driver.getUsername() != null &&
                                        !driver.getUsername().isBlank()) {

                                name = driver.getUsername();

                                String[] parts = name.trim().split("\\s+");

                                if (parts.length >= 2) {

                                        initialsText = (parts[0].substring(0, 1) +
                                                        parts[parts.length - 1].substring(0, 1)).toUpperCase();

                                } else {

                                        initialsText = parts[0].substring(0, 1).toUpperCase();
                                }
                        }
                }
                Label welcome = new Label("Welcome Back, " + name);

                welcome.setStyle(
                                "-fx-font-size: 22px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #014B3A;");

                // =========================================================
                // SPACER
                // =========================================================

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                // =========================================================
                // VEHICLE INFORMATION
                // =========================================================
                DriverModel driver1 = CurrentDriver.getInstance().getDriver();

                String vehicleNumber = "No Vehicle";
                String vehicleName = "Vehicle";

                if (driver1 != null &&
                                driver1.getEmail() != null) {

                        try {

                                VehicleDAO vehicleDAO = new VehicleDAO(
                                                FirebaseConfig.getFireStore());

                                VehicleModel vehicle = vehicleDAO.getVehicleByDriverEmail(
                                                driver1.getEmail());

                                if (vehicle != null) {

                                        if (vehicle.getVehiclePlateNumber() != null &&
                                                        !vehicle.getVehiclePlateNumber().isBlank()) {

                                                vehicleNumber = vehicle.getVehiclePlateNumber();
                                        }

                                        if (vehicle.getVehicleName() != null &&
                                                        !vehicle.getVehicleName().isBlank()) {

                                                vehicleName = vehicle.getVehicleName();
                                        }
                                }

                        } catch (Exception e) {

                                e.printStackTrace();
                        }
                }

                Label number = new Label(vehicleNumber);

                number.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #014B3A;");

                Label vehicleNameLabel = new Label(vehicleName);

                vehicleNameLabel.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #64748B;");

                VBox vehicle = new VBox(
                                2,
                                number,
                                vehicleNameLabel);

                vehicle.setAlignment(
                                Pos.CENTER_RIGHT);

                // =========================================================
                // PROFILE CIRCLE
                // =========================================================

                Circle profile = new Circle(
                                21,
                                Color.web("#D8F5DA"));

                Label initials = new Label(initialsText);

                initials.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: " + GREEN + ";");

                // =========================================================
                // PROFILE CONTAINER
                // =========================================================

                StackPane profileBox = new StackPane(
                                profile,
                                initials);

                profileBox.setPrefSize(
                                42,
                                42);

                profileBox.setMinSize(
                                42,
                                42);

                profileBox.setMaxSize(
                                42,
                                42);

                profileBox.setCursor(
                                Cursor.HAND);

                // =========================================================
                // PROFILE HOVER EFFECT
                // =========================================================

                profileBox.setOnMouseEntered(event -> {

                        profile.setFill(
                                        Color.web("#B9EDBD"));

                });

                profileBox.setOnMouseExited(event -> {

                        profile.setFill(
                                        Color.web("#D8F5DA"));

                });

                // =========================================================
                // OPEN MY PROFILE
                // =========================================================

                profileBox.setOnMouseClicked(event -> {

                        Profile profilePage = new Profile();

                        Scene profileScene = profilePage.getMyProfile();

                        HomePage.homeStage.setScene(
                                        profileScene);
                });

                // =========================================================
                // ADD EVERYTHING TO NAVBAR
                // =========================================================

                header.getChildren().addAll(
                                welcome,
                                spacer,
                                vehicle,
                                new Separator(
                                                Orientation.VERTICAL),
                                profileBox);

                return header;
        }

        private static Button menuButton(String icon, String text, String activePage) {

                boolean selected = text.equals(activePage);
                Button button = new Button();
                button.setGraphic(menuGraphic(icon, text, selected));
                button.setAlignment(Pos.CENTER_LEFT);

                button.setPadding(new Insets(10, 16, 10, 18));

                button.setMaxWidth(Double.MAX_VALUE);

                button.setCursor(Cursor.HAND);

                String normalStyle = selected
                                ? "-fx-background-color: #075F49;" +
                                                "-fx-background-radius: 10;"
                                : "-fx-background-color: transparent;" +
                                                "-fx-background-radius: 12;";

                button.setStyle(normalStyle);

                button.setOnMouseEntered(event -> {

                        button.setStyle(
                                        "-fx-background-color: " + LIGHT_GREEN + ";" +
                                                        "-fx-text-fill: white;" +
                                                        "-fx-background-radius: 12;");
                });

                button.setOnMouseExited(event -> {

                        button.setStyle(normalStyle);

                });

                button.setOnAction(event -> navigate(text));

                return button;
        }

        private static HBox menuGraphic(String icon, String text, boolean selected) {

                Label iconLabel = new Label(icon);

                iconLabel.setStyle(
                                "-fx-font-size: 23px;" +
                                                "-fx-text-fill: #FFFFFF;");

                Label textLabel = new Label(text);

                textLabel.setStyle(
                                "-fx-font-size: 16px;" +
                                                "-fx-font-weight: " + (selected ? "bold" : "normal") + ";" +
                                                "-fx-text-fill: #FFFFFF;");

                return new HBox(15, iconLabel, textLabel);
        }

        private static Button truckWalaButton(String activePage) {

                Button button = new Button();

                boolean selected = "Truck Wala".equals(activePage);

                button.setGraphic(
                                menuGraphic("🎵", "Truck Wala", selected));

                button.setAlignment(Pos.CENTER_LEFT);
                button.setPadding(new Insets(10, 16, 10, 18));
                button.setMaxWidth(Double.MAX_VALUE);
                button.setCursor(Cursor.HAND);

                String normalStyle = selected
                                ? "-fx-background-color: #075F49;" +
                                                "-fx-background-radius: 12;"
                                : "-fx-background-color: transparent;" +
                                                "-fx-background-radius: 12;";

                String hoverStyle = "-fx-background-color: #0A7057;" +
                                "-fx-background-radius: 12;";

                button.setStyle(normalStyle);

                button.setOnMouseEntered(event -> {
                        button.setStyle(hoverStyle);
                });

                button.setOnMouseExited(event -> {
                        button.setStyle(normalStyle);
                });

                // Open Truck Wala
                button.setOnAction(event -> {

                        System.out.println("Truck Wala clicked!");

                        try {

                                TruckWala truckWala = new TruckWala();

                                Scene scene = truckWala.getTruckWalaScene();

                                HomePage.homeStage.setScene(scene);

                                System.out.println("Truck Wala scene opened!");

                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                });

                return button;
        }

        private static void navigate(String page) {
                Scene scene = switch (page) {
                        case "Dashboard" -> new DriverDashoard().getDashBoardScene();
                        case "Available Loads" -> new Available_Loads().getAvailable_LoadsScene();
                        case "Active Trip" -> new ActiveTrip().getTripDetailsScene();
                        case "Trip History" -> new TripHistory().getTripHistoryScene();
                        case "Earnings" -> new DriverEarnings().getDriverEarningsScene();
                        case "Ratings" -> new Ratings().getRatingsPageScene();
                        case "AI Vehicle Health" -> new VehicleHealth().getVehicleHealthScene();
                        case "Support" -> {

                    CurrentDriver currentDriver = CurrentDriver.getInstance();

                    if (currentDriver.getDriver() == null) {
                        System.err.println(
                                "No logged-in driver found.");
                        yield null;
                    }

                    String driverEmail = currentDriver.getDriver().getEmail();

                    if (driverEmail == null ||
                            driverEmail.isBlank()) {

                        System.err.println(
                                "Driver email not found.");

                        yield null;
                    }

                    SupportDAO supportDAO = new SupportDAO(
                            FirebaseConfig.getFireStore());

                    SupportController supportController = new SupportController(
                            supportDAO);

                    Support support = new Support(
                            supportController,
                            driverEmail);

                    yield support.getSupportPageScene();
                }
                        default -> null;
                };
                if (scene != null) {
                        HomePage.homeStage.setScene(scene);
                }
        }
}
