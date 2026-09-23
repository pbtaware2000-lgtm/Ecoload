package com.super_x.view.DriverView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.super_x.dao.driverdao.VehicleDAO;
import com.super_x.model.drivermodel.CurrentDriver;
import com.super_x.model.drivermodel.DriverModel;
import com.super_x.model.drivermodel.VehicleModel;
import com.super_x.config.FirebaseConfig;
import com.super_x.view.HomePage;
import javafx.collections.FXCollections;
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
import javafx.stage.FileChooser;

public class Profile {

        private DriverModel getLoggedInDriver() {

    return CurrentDriver.getInstance().getDriver();
}
DriverModel driver = getLoggedInDriver();

    private Scene MyPrfileScen;

    // Colors
    private static final String GREEN = "#0B6B1F";
    private static final String GREEN_HOVER = "#075817";
    private static final String LIGHT_GREEN = "#EAF4EA";
    private static final String PAGE = "#F7F9F7";
    private static final String WHITE = "#FFFFFF";
    private static final String BORDER = "#DCE3DE";
    private static final String DARK = "#17221B";
    private static final String MUTED = "#66736B";
    private static final String RED = "#C62828";
    private static final String LIGHT_RED = "#FFF3F3";
    private final String BG = "#e6f1e8";

    // Profile fields
    private TextField fullName;
    private TextField email;
    private TextField mobile;
    private TextField dob;
    private TextArea address;

    // Vehicle fields
    private TextField vehicleModel;
    private TextField registration;
    private TextField vehicleType;
    private TextField payload;
    private VehicleModel currentVehicle;

    private RadioButton diesel;
    private RadioButton evHybrid;

    // Header/profile
    private Label profileName;
    private Label activeStatus;

    private ImageView profileImage;

    // Original values
    private String oldName;
    private String oldEmail;
    private String oldMobile;
    private String oldDob;
    private String oldAddress;

    private String oldVehicleModel;
    private String oldRegistration;
    private String oldVehicleType;
    private String oldPayload;
    private String oldFuel;

    private final List<DocumentData> documents = new ArrayList<>();


    private void loadVehicle() {

    DriverModel driver =
            CurrentDriver.getInstance().getDriver();

    if (driver == null) {
        System.out.println("No logged-in driver.");
        return;
    }

    String email = driver.getEmail();

    if (email == null || email.isBlank()) {
        System.out.println("Driver email is empty.");
        return;
    }

    try {

        VehicleDAO vehicleDAO =
                new VehicleDAO(
                        FirebaseConfig.getFireStore());

        currentVehicle =
                vehicleDAO.getVehicleByDriverEmail(email);

        if (currentVehicle != null) {

            System.out.println(
                    "Vehicle loaded: "
                            + currentVehicle.getVehicleName());

            System.out.println(
                    "Plate: "
                            + currentVehicle.getVehiclePlateNumber());

        } else {

            System.out.println(
                    "No vehicle found for: " + email);
        }

    } catch (Exception e) {

        e.printStackTrace();
    }
}

    private String getInitials(String name) {

    if (name == null || name.trim().isEmpty()) {
        return "DR";
    }

    String[] parts = name.trim().split("\\s+");

    if (parts.length == 1) {
        return parts[0]
                .substring(0, 1)
                .toUpperCase();
    }

    return (
            parts[0].substring(0, 1) +
            parts[parts.length - 1].substring(0, 1)
    ).toUpperCase();
}

    // =========================================================
    // PROFILE PAGE SCENE
    // =========================================================

    public Scene getMyProfile() {

        // =========================================================
        // MAIN ROOT
        // =========================================================
        loadVehicle();
        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: " + BG + ";");

        // =========================================================
        // LEFT SIDEBAR
        // =========================================================

        root.setLeft(
                DriverNavigation.createSidebar("Profile"));

        // =========================================================
        // NAVBAR
        // =========================================================

        HBox navbar = DriverNavigation.createNavbar();

        // =========================================================
        // MAIN CONTENT
        // =========================================================

        ScrollPane scroll = new ScrollPane(
                createMainContent());

        scroll.setFitToWidth(true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scroll.setStyle(
                "-fx-background-color: " + PAGE + ";" +
                        "-fx-background: " + PAGE + ";" +
                        "-fx-border-color: transparent;");

        // =========================================================
        // CENTER AREA
        // NAVBAR + SCROLL PANE
        // =========================================================

        VBox centerContent = new VBox();

        centerContent.getChildren().addAll(
                navbar,
                scroll);

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        // =========================================================
        // SET CENTER
        // =========================================================

        root.setCenter(
                centerContent);

        // =========================================================
        // CREATE SCENE
        // =========================================================

        if (MyPrfileScen == null) {

            MyPrfileScen = new Scene(
                    root,
                    1536,
                    750);
        }

        return MyPrfileScen;
    }

    // =========================================================
    // MAIN CONTENT
    // =========================================================

    private VBox createMainContent() {

        VBox content = new VBox(22);

        content.setPadding(
                new Insets(28, 35, 40, 35));

        content.setFillWidth(true);

        content.getChildren().addAll(
                createProfileSection(),
                createStatistics(),
                createInformationArea(),
                createDocumentsSection());

        return content;
    }

    // =========================================================
    // PROFILE SECTION
    // =========================================================

    private VBox createProfileSection() {

        VBox card = new VBox();

        card.setPadding(
                new Insets(10));

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 18;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 18;");

        StackPane avatar = createLargeAvatar();

        DriverModel driver = getLoggedInDriver();

String driverName =
        driver != null && driver.getUsername() != null
                ? driver.getUsername()
                : "Driver";

profileName = label(
        driverName,
        27,
        DARK,
        true);

        Label verified = label(
                "✓  Verified Driver",
                13,
                GREEN,
                true);

        verified.setStyle(
                "-fx-background-color: " + LIGHT_GREEN + ";" +
                        "-fx-text-fill: " + GREEN + ";" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 7 13;");

        Label id = label(
                "ID: DRV-2025-1001",
                13,
                MUTED,
                false);

        Circle greenCircle = new Circle(
                5,
                Color.web(GREEN));

        activeStatus = label(
                "Active",
                13,
                GREEN,
                true);

        HBox status = new HBox(
                7,
                greenCircle,
                activeStatus);

        VBox info = new VBox(
                3,
                profileName,
                verified,
                id,
                status);

        info.setAlignment(
                Pos.CENTER_LEFT);

        HBox profile = new HBox(
                5,
                avatar,
                info);

        profile.setAlignment(
                Pos.CENTER_LEFT);

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        Button save = greenButton(
                "Save Changes");

        Button cancel = normalButton(
                "Cancel");

        Button password = outlineButton(
                "Change Password");

        save.setOnAction(
                e -> saveChanges());

        cancel.setOnAction(
                e -> cancelChanges());

        password.setOnAction(
                e -> changePassword());

        HBox buttons = new HBox(
                10,
                save,
                cancel,
                password);

        buttons.setAlignment(
                Pos.CENTER_RIGHT);

        HBox top = new HBox(
                profile,
                spacer,
                buttons);

        top.setAlignment(
                Pos.CENTER_LEFT);

        card.getChildren().add(top);

        return card;
    }

    private StackPane createLargeAvatar() {

        final double SIZE = 90;

        // =========================================================
        // BACKGROUND
        // =========================================================

        Circle background = new Circle(
                SIZE / 2,
                Color.web("#DCEADF"));

        // =========================================================
        // INITIALS
        // =========================================================
DriverModel driver = getLoggedInDriver();

String driverName =
        driver != null
                ? driver.getUsername()
                : "Driver";

Label initials = label(
        getInitials(driverName),
        30,
        GREEN,
        true);
        // =========================================================
        // PROFILE IMAGE
        // =========================================================

        profileImage = new ImageView();

        profileImage.setFitWidth(SIZE);
        profileImage.setFitHeight(SIZE);

        // Keep image proportions
        profileImage.setPreserveRatio(true);
        profileImage.setSmooth(true);

        // =========================================================
        // CIRCULAR CLIP
        // =========================================================

        Circle clip = new Circle(
                SIZE / 2,
                SIZE / 2,
                SIZE / 2);

        profileImage.setClip(clip);

        // Initially show initials
        profileImage.setVisible(false);

        // =========================================================
        // IMAGE CONTAINER
        // =========================================================

        StackPane imageContainer = new StackPane(
                background,
                initials,
                profileImage);

        imageContainer.setPrefSize(SIZE, SIZE);
        imageContainer.setMinSize(SIZE, SIZE);
        imageContainer.setMaxSize(SIZE, SIZE);

        imageContainer.setCursor(Cursor.HAND);

        imageContainer.setOnMouseClicked(
                e -> chooseImage());

        // =========================================================
        // CAMERA BUTTON
        // =========================================================

        Circle cameraCircle = new Circle(
                17,
                Color.web(GREEN));

        Label camera = label(
                "📷",
                10,
                WHITE,
                false);

        StackPane cameraButton = new StackPane(
                cameraCircle,
                camera);

        cameraButton.setPrefSize(34, 34);
        cameraButton.setMinSize(34, 34);
        cameraButton.setMaxSize(34, 34);

        cameraButton.setCursor(Cursor.HAND);

        cameraButton.setOnMouseClicked(
                e -> chooseImage());

        // =========================================================
        // FINAL AVATAR
        // =========================================================

        StackPane result = new StackPane(
                imageContainer,
                cameraButton);

        result.setPrefSize(SIZE, SIZE);
        result.setMinSize(SIZE, SIZE);
        result.setMaxSize(SIZE, SIZE);

        StackPane.setAlignment(
                cameraButton,
                Pos.BOTTOM_RIGHT);

        return result;
    }

    private void chooseImage() {

        FileChooser chooser = new FileChooser();

        chooser.setTitle("Select Profile Photo");

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files",
                        "*.png",
                        "*.jpg",
                        "*.jpeg"));

        File file = chooser.showOpenDialog(
                HomePage.homeStage);

        if (file == null) {
            return;
        }

        Image image = new Image(
                file.toURI().toString());

        profileImage.setImage(image);

        profileImage.setVisible(true);
    }

    // =========================================================
    // STATISTICS
    // =========================================================

    private HBox createStatistics() {

        HBox cards = new HBox(18);

        cards.getChildren().addAll(
                statistic(
                        "DRIVER SINCE",
                        "Jan 2021",
                        "Member since"),
                statistic(
                        "COMPLETED TRIPS",
                        "1,248",
                        "Successfully completed"),
                statistic(
                        "AVERAGE RATING",
                        "4.9 ★",
                        "Driver rating"),
                statistic(
                        "TOTAL EARNINGS",
                        "₹14.2L",
                        "Lifetime earnings"));

        return cards;
    }

    private VBox statistic(
            String title,
            String value,
            String description) {

        VBox card = new VBox(
                10,
                label(
                        title,
                        12,
                        MUTED,
                        false),
                label(
                        value,
                        26,
                        DARK,
                        true),
                label(
                        description,
                        12,
                        MUTED,
                        false));

        card.setPadding(
                new Insets(18));

        card.setPrefHeight(125);

        HBox.setHgrow(
                card,
                Priority.ALWAYS);

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 16;");

        // Hover
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: " + LIGHT_GREEN + ";" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: " + GREEN + ";" +
                        "-fx-border-radius: 16;" +
                        "-fx-border-width: 1.5;"));

        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 16;"));

        return card;
    }

    // =========================================================
    // PERSONAL + VEHICLE
    // =========================================================

    private HBox createInformationArea() {

        VBox personal = createPersonalCard();

        VBox vehicle = createVehicleCard();

        HBox box = new HBox(20);

        box.setFillHeight(true);
        personal.setFillWidth(true);
        vehicle.setFillWidth(true);

        HBox.setHgrow(
                personal,
                Priority.ALWAYS);

        HBox.setHgrow(
                vehicle,
                Priority.ALWAYS);
        box.getChildren().addAll(personal,vehicle);

        return box;
    }

    private VBox createPersonalCard() {

    VBox card = createCard();

    DriverModel driver = getLoggedInDriver();

    if (driver == null) {

        showError(
                "Profile Error",
                "No logged-in driver found.");

        return card;
    }

    fullName = textField(
            driver.getUsername() != null
                    ? driver.getUsername()
                    : "");

    email = textField(
            driver.getEmail() != null
                    ? driver.getEmail()
                    : "");

    mobile = textField(
            driver.getPhone() != null
                    ? driver.getPhone()
                    : "");

    /*
     * DriverModel currently does not contain Date of Birth.
     * So we leave this empty for now.
     */
    dob = textField("");

    address = new TextArea("");

    address.setWrapText(true);
    address.setPrefRowCount(3);

    styleTextArea(address);

    GridPane grid = new GridPane();

    grid.setHgap(15);
    grid.setVgap(14);

    addField(
            grid,
            "Full Name",
            fullName,
            0,
            0);

    addField(
            grid,
            "Email ID",
            email,
            1,
            0);

    addField(
            grid,
            "Mobile Number",
            mobile,
            0,
            1);

    addField(
            grid,
            "Date of Birth",
            dob,
            1,
            1);

        VBox accBox = createAccountActions();

        grid.add(accBox,0,2);


//     grid.add(fieldBox("Residential Address", accBox),
//             0,
//             2,
//             2,
//             1);

    ColumnConstraints c1 = new ColumnConstraints();
    ColumnConstraints c2 = new ColumnConstraints();

    c1.setPercentWidth(50);
    c2.setPercentWidth(50);

    c1.setHgrow(Priority.ALWAYS);
    c2.setHgrow(Priority.ALWAYS);

    grid.getColumnConstraints().addAll(
            c1,
            c2);

    card.getChildren().addAll(
            sectionTitle("Personal Information"),
            grid);

    saveOriginalPersonal();

    return card;
}

   private VBox createVehicleCard() {

    VBox card = createCard();

    // -----------------------------------------
    // NO VEHICLE
    // -----------------------------------------

    if (currentVehicle == null) {

        card.getChildren().addAll(
                sectionTitle("Vehicle Details"),
                label(
                        "No vehicle information found.",
                        14,
                        MUTED,
                        false));

        return card;
    }

    // -----------------------------------------
    // VEHICLE FIELDS
    // -----------------------------------------

    vehicleModel = textField(
            currentVehicle.getVehicleName() != null
                    ? currentVehicle.getVehicleName()
                    : "");

    registration = textField(
            currentVehicle.getVehiclePlateNumber() != null
                    ? currentVehicle.getVehiclePlateNumber()
                    : "");

    vehicleType = textField(
            currentVehicle.getVehicleType() != null
                    ? currentVehicle.getVehicleType()
                    : "");

    payload = textField(
            String.valueOf(
                    currentVehicle.getVehicleCapacity()));

    // -----------------------------------------
    // GRID
    // -----------------------------------------

    GridPane grid = new GridPane();

    grid.setHgap(15);
    grid.setVgap(14);

    addField(
            grid,
            "Vehicle Model",
            vehicleModel,
            0,
            0);

    addField(
            grid,
            "Registration Number",
            registration,
            1,
            0);

    addField(
            grid,
            "Vehicle Type",
            vehicleType,
            0,
            1);

    addField(
            grid,
            "Payload Capacity",
            payload,
            1,
            1);

    // -----------------------------------------
    // FUEL TYPE
    // -----------------------------------------

    diesel = new RadioButton("Diesel");

    evHybrid = new RadioButton("EV / Hybrid");

    ToggleGroup group = new ToggleGroup();

    diesel.setToggleGroup(group);
    evHybrid.setToggleGroup(group);

    String fuelType =
            currentVehicle.getFuelType();

    if (fuelType != null) {

        if (fuelType.equalsIgnoreCase("Diesel")) {

            diesel.setSelected(true);

        } else if (
                fuelType.equalsIgnoreCase("EV")
                        || fuelType.equalsIgnoreCase("Hybrid")
                        || fuelType.equalsIgnoreCase("EV / Hybrid")) {

            evHybrid.setSelected(true);
        }
    }

    VBox fuel = new VBox(
            7,
            label(
                    "Fuel Type",
                    12,
                    MUTED,
                    false),
            new HBox(
                    20,
                    diesel,
                    evHybrid));

    grid.add(
            fuel,
            0,
            2,
            2,
            1);

    // -----------------------------------------
    // PRIMARY VEHICLE
    // -----------------------------------------

    VBox primary = new VBox(
            5,
            label(
                    "Primary Vehicle",
                    12,
                    MUTED,
                    false),
            label(
                    "🚚  "
                            + currentVehicle.getVehicleName(),
                    14,
                    DARK,
                    true),
            label(
                    "✓ Fleet Linked - Verified",
                    12,
                    GREEN,
                    true));

    grid.add(
            primary,
            0,
            3,
            2,
            1);

    // -----------------------------------------
    // COLUMN WIDTHS
    // -----------------------------------------

    ColumnConstraints c1 =
            new ColumnConstraints();

    ColumnConstraints c2 =
            new ColumnConstraints();

    c1.setPercentWidth(50);
    c2.setPercentWidth(50);

    c1.setHgrow(Priority.ALWAYS);
    c2.setHgrow(Priority.ALWAYS);

    grid.getColumnConstraints()
            .addAll(c1, c2);

    // -----------------------------------------
    // ADD TO CARD
    // -----------------------------------------

    card.getChildren().addAll(
            sectionTitle("Vehicle Details"),
            grid);

    saveOriginalVehicle();

    return card;
}

    private VBox createDocumentsSection() {

        VBox section = new VBox(15);

        Label title = sectionTitle(
                "Upload Documents");

        Button upload = outlineButton(
                "＋ Upload New");

        upload.setOnAction(
                e -> uploadDocument());

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        HBox heading = new HBox(
                title,
                spacer,
                upload);

        heading.setAlignment(
                Pos.CENTER_LEFT);

        FlowPane flow = new FlowPane();

        flow.setHgap(15);
        flow.setVgap(15);

        for (DocumentData d : documents) {

            flow.getChildren().add(
                    documentCard(d));
        }

        section.getChildren().addAll(
                heading,
                flow);

        return section;
    }

    private VBox documentCard(
            DocumentData d) {

        VBox card = createCard();

        card.setPrefWidth(285);

        Label icon = label(
                "▤",
                25,
                GREEN,
                false);

        Label name = label(
                d.name,
                15,
                DARK,
                true);

        Label verified = label(
                "✓ " + d.status,
                12,
                GREEN,
                true);

        Label validity = label(
                d.validity,
                12,
                MUTED,
                false);

        VBox information = new VBox(
                5,
                name,
                verified,
                validity);

        HBox top = new HBox(
                12,
                icon,
                information);

        top.setAlignment(
                Pos.CENTER_LEFT);
        top.setPrefHeight(90);

        Separator line = new Separator();

        Button view = smallButton(
                "View");

        Button download = smallButton(
                "Download");

        Button more = smallButton(
                "•••");

        view.setOnAction(
                e -> viewDocument(d));

        download.setOnAction(
                e -> downloadDocument(d));

        more.setOnAction(
                e -> documentActions(d, more));

        HBox buttons = new HBox(
                8,
                view,
                download,
                more);

        card.getChildren().addAll(
                top,
                line,
                buttons);

        return card;
    }

    // =========================================================
    // UPLOAD
    // =========================================================

    private void uploadDocument() {

        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle(
                "Upload New Document");

        dialog.setHeaderText(
                "Add a new verified document");

        ComboBox<String> type = new ComboBox<>(
                FXCollections.observableArrayList(
                        "Driving License",
                        "RC Book",
                        "Insurance",
                        "Pollution Cert",
                        "Other"));

        type.getSelectionModel()
                .selectFirst();

        TextField name = textField("");

        name.setPromptText(
                "Document name");

        Label fileLabel = label(
                "No file selected",
                12,
                MUTED,
                false);

        Button choose = new Button(
                "Choose File");

        choose.setOnAction(e -> {

            FileChooser chooser = new FileChooser();

            chooser.setTitle(
                    "Select Document");

            File file = chooser.showOpenDialog(
                    null);

            if (file != null) {

                fileLabel.setText(
                        file.getName());
            }
        });

        VBox content = new VBox(
                12,
                fieldBox(
                        "Document Type",
                        type),
                fieldBox(
                        "Document Name",
                        name),
                choose,
                fileLabel);

        content.setPadding(
                new Insets(15));

        dialog.getDialogPane()
                .setContent(content);

        ButtonType upload = new ButtonType(
                "Upload",
                ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        upload,
                        ButtonType.CANCEL);

        dialog.showAndWait()
                .ifPresent(result -> {

                    if (result == upload) {

                        if (name.getText()
                                .trim()
                                .isEmpty()) {

                            showError(
                                    "Upload Error",
                                    "Please enter document name.");

                            return;
                        }

                        documents.add(
                                new DocumentData(
                                        name.getText(),
                                        "Verified",
                                        "Recently uploaded"));

                        showInfo(
                                "Upload Successful",
                                "Document uploaded successfully.");
                    }
                });
    }

    // =========================================================
    // DOCUMENT ACTIONS
    // =========================================================

    private void viewDocument(
            DocumentData d) {

        showInfo(
                d.name,
                "Document: " + d.name +
                        "\nStatus: " + d.status +
                        "\nValidity: " + d.validity +
                        "\nDocument ID: DOC-" +
                        Math.abs(
                                d.name.hashCode()));
    }

    private void downloadDocument(
            DocumentData d) {

        showInfo(
                "Download",
                d.name +
                        " download simulation completed.");
    }

    private void documentActions(
            DocumentData d,
            Button source) {

        ContextMenu menu = new ContextMenu();

        MenuItem view = new MenuItem(
                "View Document");

        MenuItem download = new MenuItem(
                "Download");

        MenuItem replace = new MenuItem(
                "Replace Document");

        view.setOnAction(
                e -> viewDocument(d));

        download.setOnAction(
                e -> downloadDocument(d));

        replace.setOnAction(
                e -> showInfo(
                        "Replace Document",
                        "Replace option selected for " +
                                d.name));

        menu.getItems().addAll(
                view,
                download,
                replace);

        menu.show(
                source,
                javafx.geometry.Side.BOTTOM,
                0,
                0);
    }

    // =========================================================
    // ACCOUNT ACTIONS
    // =========================================================

    private VBox createAccountActions() {

        VBox box = new VBox(12);

        box.setPadding(
                new Insets(20));

        box.setStyle(
                "-fx-background-color: " + LIGHT_RED + ";" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #E6B2B2;" +
                        "-fx-border-radius: 16;");

        Label title = label(
                "Account Actions",
                18,
                RED,
                true);

        Label description = label(
                "Manage your account visibility or request data deletion " +
                        "from the EcoLoad network.",
                13,
                MUTED,
                false);

        Button deactivate = dangerOutlineButton(
                "Deactivate Account");

        Button delete = dangerButton(
                "Delete Account");

        deactivate.setOnAction(
                e -> deactivateAccount());

        delete.setOnAction(
                e -> deleteAccount());

        HBox buttons = new HBox(
                10,
                deactivate,
                delete);

        box.getChildren().addAll(
                title,
                description,
                buttons);

        return box;
    }

    private void deactivateAccount() {

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION);

        alert.setTitle(
                "Deactivate Account");

        alert.setHeaderText(
                "Deactivate your account?");

        alert.setContentText(
                "Your EcoLoad driver account will become inactive.");

        ButtonType deactivate = new ButtonType(
                "Deactivate");

        alert.getButtonTypes().setAll(
                deactivate,
                ButtonType.CANCEL);

        alert.showAndWait()
                .ifPresent(result -> {

                    if (result == deactivate) {

                        activeStatus.setText(
                                "Inactive");

                        activeStatus.setTextFill(
                                Color.web(RED));

                        showInfo(
                                "Account Deactivated",
                                "Your account is now inactive.");
                    }
                });
    }

    private void deleteAccount() {

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION);

        alert.setTitle(
                "Delete Account");

        alert.setHeaderText(
                "Delete your EcoLoad account?");

        alert.setContentText(
                "This action cannot be undone.\n" +
                        "This demo will not delete any real database data.");

        ButtonType delete = new ButtonType(
                "Delete Account");

        alert.getButtonTypes().setAll(
                delete,
                ButtonType.CANCEL);

        alert.showAndWait()
                .ifPresent(result -> {

                    if (result == delete) {

                        showInfo(
                                "Delete Request",
                                "Account deletion request submitted.");
                    }
                });
    }

    // =========================================================
    // SAVE / CANCEL
    // =========================================================

    private void saveOriginalPersonal() {

        oldName = fullName.getText();
        oldEmail = email.getText();
        oldMobile = mobile.getText();
        oldDob = dob.getText();
        oldAddress = address.getText();
    }

    private void saveOriginalVehicle() {

        oldVehicleModel = vehicleModel.getText();

        oldRegistration = registration.getText();

        oldVehicleType = vehicleType.getText();

        oldPayload = payload.getText();

        oldFuel = diesel.isSelected()
                ? "Diesel"
                : "EV / Hybrid";
    }

    private void saveChanges() {

        if (fullName.getText()
                .trim()
                .isEmpty()) {

            showError(
                    "Validation",
                    "Full Name cannot be empty.");

            return;
        }

        if (!email.getText()
                .contains("@")) {

            showError(
                    "Validation",
                    "Enter a valid Email ID.");

            return;
        }

        if (mobile.getText()
                .trim()
                .isEmpty()) {

            showError(
                    "Validation",
                    "Mobile Number cannot be empty.");

            return;
        }

        oldName = fullName.getText();
        oldEmail = email.getText();
        oldMobile = mobile.getText();
        oldDob = dob.getText();
        oldAddress = address.getText();

        oldVehicleModel = vehicleModel.getText();

        oldRegistration = registration.getText();

        oldVehicleType = vehicleType.getText();

        oldPayload = payload.getText();

        oldFuel = diesel.isSelected()
                ? "Diesel"
                : "EV / Hybrid";

        profileName.setText(
                fullName.getText());

        showInfo(
                "Success",
                "Profile changes saved successfully.");
    }

    private void cancelChanges() {

        fullName.setText(oldName);
        email.setText(oldEmail);
        mobile.setText(oldMobile);
        dob.setText(oldDob);
        address.setText(oldAddress);

        vehicleModel.setText(
                oldVehicleModel);

        registration.setText(
                oldRegistration);

        vehicleType.setText(
                oldVehicleType);

        payload.setText(
                oldPayload);

        if ("Diesel".equals(oldFuel)) {
            diesel.setSelected(true);
        } else {
            evHybrid.setSelected(true);
        }

        profileName.setText(oldName);

        showInfo(
                "Cancelled",
                "Unsaved changes have been restored.");
    }

    // =========================================================
    // PASSWORD
    // =========================================================

    private void changePassword() {

        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle(
                "Change Password");

        dialog.setHeaderText(
                "Change your password");

        PasswordField current = new PasswordField();

        current.setPromptText(
                "Current Password");

        PasswordField newPassword = new PasswordField();

        newPassword.setPromptText(
                "New Password");

        PasswordField confirm = new PasswordField();

        confirm.setPromptText(
                "Confirm Password");

        VBox content = new VBox(
                12,
                fieldBox(
                        "Current Password",
                        current),
                fieldBox(
                        "New Password",
                        newPassword),
                fieldBox(
                        "Confirm Password",
                        confirm));

        content.setPadding(
                new Insets(15));

        dialog.getDialogPane()
                .setContent(content);

        ButtonType change = new ButtonType(
                "Change Password",
                ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        change,
                        ButtonType.CANCEL);

        dialog.showAndWait()
                .ifPresent(result -> {

                    if (result == change) {

                        if (current.getText()
                                .isEmpty()
                                || newPassword.getText()
                                        .isEmpty()
                                || confirm.getText()
                                        .isEmpty()) {

                            showError(
                                    "Password Error",
                                    "All password fields are required.");

                            return;
                        }

                        if (!newPassword.getText()
                                .equals(
                                        confirm.getText())) {

                            showError(
                                    "Password Error",
                                    "Passwords do not match.");

                            return;
                        }

                        if (newPassword.getText()
                                .length() < 6) {

                            showError(
                                    "Password Error",
                                    "Password must contain at least 6 characters.");

                            return;
                        }

                        showInfo(
                                "Success",
                                "Password changed successfully.");
                    }
                });
    }

    // =========================================================
    // UI HELPERS
    // =========================================================

    private VBox createCard() {

        VBox card = new VBox(15);

        card.setPadding(
                new Insets(20));

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 16;");

        return card;
    }

    private Label sectionTitle(
            String text) {

        return label(
                text,
                19,
                DARK,
                true);
    }

    private Label label(
            String text,
            double size,
            String color,
            boolean bold) {

        Label l = new Label(text);

        l.setStyle(
                "-fx-font-size: " + size + "px;" +
                        "-fx-text-fill: " + color + ";" +
                        (bold
                                ? "-fx-font-weight: bold;"
                                : ""));

        l.setWrapText(true);

        return l;
    }

    private TextField textField(
            String value) {

        TextField field = new TextField(value);

        field.setPrefHeight(40);

        field.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 9;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 9;" +
                        "-fx-padding: 0 12;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: " + DARK + ";");

        field.focusedProperty()
                .addListener(
                        (obs, oldValue, focused) -> {

                            if (focused) {

                                field.setStyle(
                                        "-fx-background-color: white;" +
                                                "-fx-background-radius: 9;"
                                                +
                                                "-fx-border-color: "
                                                + GREEN + ";" +
                                                "-fx-border-width: 1.5;"
                                                +
                                                "-fx-border-radius: 9;"
                                                +
                                                "-fx-padding: 0 12;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: " + DARK
                                                + ";");

                            } else {

                                field.setStyle(
                                        "-fx-background-color: white;" +
                                                "-fx-background-radius: 9;"
                                                +
                                                "-fx-border-color: "
                                                + BORDER + ";" +
                                                "-fx-border-radius: 9;"
                                                +
                                                "-fx-padding: 0 12;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: " + DARK
                                                + ";");
                            }
                        });

        return field;
    }

    private void styleTextArea(
            TextArea area) {

        area.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 9;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 9;" +
                        "-fx-padding: 8;" +
                        "-fx-font-size: 13px;");
    }

    private VBox fieldBox(
            String title,
            Control control) {

        Label titleLabel = label(
                title,
                12,
                MUTED,
                false);

        return new VBox(
                6,
                titleLabel,
                control);
    }

    private void addField(
            GridPane grid,
            String title,
            Control control,
            int column,
            int row) {

        grid.add(
                fieldBox(
                        title,
                        control),
                column,
                row);
    }

    // =========================================================
    // BUTTONS
    // =========================================================

    private Button greenButton(
            String text) {

        Button button = new Button(text);

        button.setPrefHeight(40);

        button.setPadding(
                new Insets(0, 18, 0, 18));

        String normal = "-fx-background-color: " + GREEN + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 9;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;";

        String hover = "-fx-background-color: " + GREEN_HOVER + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 9;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;";

        button.setStyle(normal);
        button.setCursor(Cursor.HAND);

        button.setOnMouseEntered(
                e -> button.setStyle(hover));

        button.setOnMouseExited(
                e -> button.setStyle(normal));

        return button;
    }

    private Button normalButton(
            String text) {

        Button button = new Button(text);

        button.setPrefHeight(40);

        String normal = "-fx-background-color: #F1F3F1;" +
                "-fx-text-fill: " + DARK + ";" +
                "-fx-background-radius: 9;" +
                "-fx-font-size: 13px;";

        String hover = "-fx-background-color: #E2E7E3;" +
                "-fx-text-fill: " + DARK + ";" +
                "-fx-background-radius: 9;" +
                "-fx-font-size: 13px;";

        button.setStyle(normal);
        button.setCursor(Cursor.HAND);

        button.setOnMouseEntered(
                e -> button.setStyle(hover));

        button.setOnMouseExited(
                e -> button.setStyle(normal));

        return button;
    }

    private Button outlineButton(
            String text) {

        Button button = new Button(text);

        button.setPrefHeight(40);

        String normal = "-fx-background-color: white;" +
                "-fx-text-fill: " + GREEN + ";" +
                "-fx-border-color: " + GREEN + ";" +
                "-fx-border-radius: 9;" +
                "-fx-background-radius: 9;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;";

        String hover = "-fx-background-color: " + LIGHT_GREEN + ";" +
                "-fx-text-fill: " + GREEN + ";" +
                "-fx-border-color: " + GREEN + ";" +
                "-fx-border-radius: 9;" +
                "-fx-background-radius: 9;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;";

        button.setStyle(normal);
        button.setCursor(Cursor.HAND);

        button.setOnMouseEntered(
                e -> button.setStyle(hover));

        button.setOnMouseExited(
                e -> button.setStyle(normal));

        return button;
    }

    private Button smallButton(
            String text) {

        Button button = new Button(text);

        button.setPrefHeight(30);

        String normal = "-fx-background-color: " + LIGHT_GREEN + ";" +
                "-fx-text-fill: " + GREEN + ";" +
                "-fx-background-radius: 7;" +
                "-fx-font-size: 11px;";

        String hover = "-fx-background-color: " + GREEN + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 7;" +
                "-fx-font-size: 11px;";

        button.setStyle(normal);
        button.setCursor(Cursor.HAND);

        button.setOnMouseEntered(
                e -> button.setStyle(hover));

        button.setOnMouseExited(
                e -> button.setStyle(normal));

        return button;
    }

    private Button dangerButton(
            String text) {

        Button button = new Button(text);

        button.setPrefHeight(38);

        String normal = "-fx-background-color: " + RED + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 8;" +
                "-fx-font-weight: bold;";

        String hover = "-fx-background-color: #A91F1F;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 8;" +
                "-fx-font-weight: bold;";

        button.setStyle(normal);
        button.setCursor(Cursor.HAND);

        button.setOnMouseEntered(
                e -> button.setStyle(hover));

        button.setOnMouseExited(
                e -> button.setStyle(normal));

        return button;
    }

    private Button dangerOutlineButton(
            String text) {

        Button button = new Button(text);

        button.setPrefHeight(38);

        String normal = "-fx-background-color: white;" +
                "-fx-text-fill: " + RED + ";" +
                "-fx-border-color: #E1A1A1;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;";

        String hover = "-fx-background-color: #FFE8E8;" +
                "-fx-text-fill: " + RED + ";" +
                "-fx-border-color: " + RED + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;";

        button.setStyle(normal);
        button.setCursor(Cursor.HAND);

        button.setOnMouseEntered(
                e -> button.setStyle(hover));

        button.setOnMouseExited(
                e -> button.setStyle(normal));

        return button;
    }

    // =========================================================
    // ALERTS
    // =========================================================

    private void showInfo(
            String title,
            String message) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void showError(
            String title,
            String message) {

        Alert alert = new Alert(
                Alert.AlertType.ERROR);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // =========================================================
    // DOCUMENT MODEL
    // =========================================================

    private static class DocumentData {

        String name;
        String status;
        String validity;

        DocumentData(
                String name,
                String status,
                String validity) {

            this.name = name;
            this.status = status;
            this.validity = validity;
        }
    }
}