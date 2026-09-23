package com.super_x.view.DriverView;

import com.super_x.config.FirebaseConfig;
import com.super_x.config.CloudinaryConfig;
import com.super_x.controller.drivercontroller.VehicleRegistrationController;
import com.super_x.dao.driverdao.VehicleDAO;
import com.super_x.model.drivermodel.VehicleModel;

import com.cloudinary.utils.ObjectUtils;

import com.super_x.view.HomePage;
import com.super_x.view.Login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Map;

public class VehicleRegistration {

    private Scene vehicleRegistrationScene;

    // =====================================================
    // VEHICLE TEXT FIELDS
    // =====================================================

    private TextField vehicleNameTextField;
    private TextField vehiclePlateTextField;
    private TextField vehicleCapacityTextField;
    private TextField vehicleColourTextField;
    private TextField manufacturingYearTextField;

    // =====================================================
    // VEHICLE COMBO BOXES
    // =====================================================

    private ComboBox<String> vehicleTypeComboBox;
    private ComboBox<String> fuelTypeComboBox;

    // =====================================================
    // SELECTED VEHICLE DOCUMENTS
    // =====================================================

    private File rcFile;
    private File insuranceFile;

    // =====================================================
    // CURRENT DRIVER
    // =====================================================

    private String driverEmail;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public VehicleRegistration(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    // =====================================================
    // MAIN SCENE
    // =====================================================

    public Scene getVehicleRegistrationScene() {

        StackPane root = new StackPane();

        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, " +
                        "#eef7ec 0%, #d9eddb 45%, #b3d9aa 100%);"
        );

        Circle halo1 = new Circle(
                220,
                Color.web("#88d08b", 0.16)
        );

        halo1.setTranslateX(-560);
        halo1.setTranslateY(-280);

        Circle halo2 = new Circle(
                180,
                Color.web("#bbf7d0", 0.14)
        );

        halo2.setTranslateX(520);
        halo2.setTranslateY(-240);

        Circle halo3 = new Circle(
                160,
                Color.web("#4ade80", 0.08)
        );

        halo3.setTranslateX(420);
        halo3.setTranslateY(260);

        VBox card = new VBox();

        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(30));

        card.setStyle(
                "-fx-background-color: rgba(255,255,255,0.88);" +
                        " -fx-effect: dropshadow(gaussian, " +
                        "rgba(34, 197, 94, 0.18), 35, 0, 0, 18);"
        );

        // =====================================================
        // HEADER
        // =====================================================

        HBox header = new HBox(20);

        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 24, 0));

        ImageView headerLogo = new ImageView();

        try {

            headerLogo.setImage(
                    new Image(
                            getClass()
                                    .getResourceAsStream(
                                            "/assets/icons/EcoloadLogo.png"
                                    )
                    )
            );

        } catch (Exception ex) {

            headerLogo.setImage(
                    new Image(
                            "https://via.placeholder.com/40x40.png?text=EL"
                    )
            );
        }

        headerLogo.setFitWidth(120);
        headerLogo.setFitHeight(100);
        headerLogo.setPreserveRatio(true);

        HBox headerLeft = new HBox(headerLogo);

        headerLeft.setAlignment(Pos.CENTER_LEFT);

        // =====================================================
        // PROFILE SECTION
        // =====================================================

        HBox profileSection = new HBox(10);

        profileSection.setAlignment(Pos.CENTER);
        profileSection.setCursor(Cursor.HAND);

        ImageView profileImage = new ImageView();

        try {

            profileImage.setImage(
                    new Image(
                            getClass()
                                    .getResourceAsStream(
                                            "/assets/images/driver.png"
                                    )
                    )
            );

        } catch (Exception ex) {

            profileImage.setImage(
                    new Image(
                            "https://via.placeholder.com/40x40.png?text=User"
                    )
            );
        }

        profileImage.setFitWidth(40);
        profileImage.setFitHeight(40);
        profileImage.setPreserveRatio(true);

        profileImage.setClip(
                new Circle(20, 20, 20)
        );

        Label userLabel = new Label("Johnathan Doe");

        userLabel.setStyle("-fx-font-weight:bold");
        userLabel.setFont(Font.font("Arial", 13));
        userLabel.setTextFill(
                Color.web("#475569")
        );

        profileSection.getChildren().addAll(
                userLabel,
                profileImage
        );

        profileSection.setOnMouseClicked(event -> {

            System.out.println(
                    "Profile clicked - opening user profile"
            );

            openUserProfile();
        });

        HBox headerRight = new HBox(12);

        headerRight.setAlignment(Pos.CENTER_RIGHT);

        headerRight.getChildren().add(
                profileSection
        );

        HBox.setHgrow(
                headerRight,
                Priority.ALWAYS
        );

        header.getChildren().addAll(
                headerLeft,
                headerRight
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        HBox mainContent = new HBox(40);

        mainContent.setAlignment(
                Pos.TOP_LEFT
        );

        // =====================================================
        // LEFT PANEL
        // =====================================================

        VBox leftPanel = new VBox(15);

        leftPanel.setAlignment(
                Pos.TOP_LEFT
        );

        leftPanel.setPrefWidth(480);
        leftPanel.setPrefHeight(
                Region.USE_COMPUTED_SIZE
        );

        Label formTitle = new Label(
                "Register Your Vehicle"
        );

        formTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22
                )
        );

        formTitle.setTextFill(
                Color.web("#14532d")
        );

        Label formSubtitle = new Label(
                "Complete your profile by adding your vehicle details. " +
                        "Once verified, you can start accepting transport " +
                        "requests across our sustainable network."
        );

        formSubtitle.setFont(
                Font.font("Arial", 13)
        );

        formSubtitle.setTextFill(
                Color.web("#475569")
        );

        formSubtitle.setWrapText(true);

        // =====================================================
        // VEHICLE INFORMATION
        // =====================================================

        Label vehicleInfoLabel = new Label(
                "🚙 Vehicle Information"
        );

        vehicleInfoLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        vehicleInfoLabel.setTextFill(
                Color.web("#14532d")
        );

        HBox vehicleNameRow = new HBox(12);

        vehicleNameRow.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox vehicleNameField =
                createLabeledTextField(
                        "Vehicle Name / Nickname",
                        "e.g. Green Express D1"
                );

        VBox vehiclePlateField =
                createLabeledTextField(
                        "Vehicle Plate Number",
                        "XX-00-XX-0000"
                );

        HBox.setHgrow(
                vehicleNameField,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                vehiclePlateField,
                Priority.ALWAYS
        );

        vehicleNameRow.getChildren().addAll(
                vehicleNameField,
                vehiclePlateField
        );

        HBox capacityTypeRow = new HBox(12);

        capacityTypeRow.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox capacityField =
                createLabeledTextField(
                        "Vehicle Capacity",
                        "0.0"
                );

        VBox typeField =
                createLabeledComboBox(
                        "Vehicle Type",
                        "Select vehicle type"
                );

        HBox.setHgrow(
                capacityField,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                typeField,
                Priority.ALWAYS
        );

        capacityTypeRow.getChildren().addAll(
                capacityField,
                typeField
        );

        HBox fuelColorRow = new HBox(90);

        fuelColorRow.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox fuelField =
                createLabeledComboBox(
                        "Fuel Type",
                        "Diesel"
                );

        VBox colorField =
                createLabeledTextField(
                        "Vehicle Colour",
                        "e.g. White"
                );

        HBox.setHgrow(
                fuelField,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                colorField,
                Priority.ALWAYS
        );

        fuelColorRow.getChildren().addAll(
                fuelField,
                colorField
        );

        VBox yearField =
                createLabeledTextField(
                        "Manufacturing Year",
                        "2024"
                );

        // =====================================================
        // VEHICLE DOCUMENTS
        // =====================================================

        Label vehicleDocsLabel = new Label(
                "📄 Vehicle Documents"
        );

        vehicleDocsLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        vehicleDocsLabel.setTextFill(
                Color.web("#14532d")
        );

        HBox docsRow = new HBox(16);

        docsRow.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox rcField =
                createUploadBox(
                        "Registration Certificate (RC)",
                        "RC_Label"
                );

        VBox insuranceField =
                createUploadBox(
                        "Vehicle Insurance",
                        "Insurance_Label"
                );

        HBox.setHgrow(
                rcField,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                insuranceField,
                Priority.ALWAYS
        );

        docsRow.getChildren().addAll(
                rcField,
                insuranceField
        );

        // =====================================================
        // SECURITY INFO
        // =====================================================

        HBox securityBox = new HBox(10);

        securityBox.setPadding(
                new Insets(12)
        );

        securityBox.setAlignment(
                Pos.TOP_LEFT
        );

        securityBox.setStyle(
                "-fx-background-color: rgba(16, 185, 129, 0.08);" +
                        " -fx-background-radius: 12;"
        );

        Label lockIcon = new Label("🔒");

        Label securityText = new Label(
                "Your documents are securely stored and encrypted " +
                        "with enterprise-grade protection."
        );

        securityText.setFont(
                Font.font("Arial", 12)
        );

        securityText.setTextFill(
                Color.web("#475569")
        );

        securityText.setWrapText(true);

        securityBox.getChildren().addAll(
                lockIcon,
                securityText
        );

        HBox.setHgrow(
                securityText,
                Priority.ALWAYS
        );

        leftPanel.getChildren().addAll(
                formTitle,
                formSubtitle,
                vehicleInfoLabel,
                vehicleNameRow,
                capacityTypeRow,
                fuelColorRow,
                yearField,
                vehicleDocsLabel,
                docsRow,
                securityBox
        );

        // =====================================================
        // RIGHT PANEL
        // =====================================================

        VBox rightPanel = new VBox(5);

        rightPanel.setAlignment(
                Pos.TOP_CENTER
        );

        rightPanel.setPrefWidth(400);
        rightPanel.setPrefHeight(600);

        rightPanel.setStyle(
                "-fx-background-color: rgba(16, 185, 129, 0.08);" +
                        " -fx-background-radius: 20;" +
                        " -fx-padding: 15;"
        );

        ImageView sustainableImage =
                new ImageView();

        try {

            sustainableImage.setImage(
                    new Image(
                            getClass()
                                    .getResourceAsStream(
                                            "/assets/images/vehicleregistration.png"
                                    )
                    )
            );

        } catch (Exception ex) {

            sustainableImage.setImage(
                    new Image(
                            "https://via.placeholder.com/240x200.png?text=Sustainable"
                    )
            );
        }

        sustainableImage.setFitWidth(350);
        sustainableImage.setFitHeight(580);
        sustainableImage.setPreserveRatio(false);

        rightPanel.getChildren().add(
                sustainableImage
        );

        mainContent.getChildren().addAll(
                rightPanel,
                leftPanel
        );

        HBox.setHgrow(
                leftPanel,
                Priority.ALWAYS
        );

        // =====================================================
        // BUTTONS
        // =====================================================

        HBox buttonBox = new HBox(16);

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttonBox.setPadding(
                new Insets(24, 0, 0, 0)
        );

        Button cancelBtn = new Button(
                "Cancel"
        );

        cancelBtn.setPrefWidth(120);
        cancelBtn.setPrefHeight(48);

        cancelBtn.setStyle(
                "-fx-background-color: transparent;" +
                        " -fx-text-fill: #475569;" +
                        " -fx-border-color: #ccc;" +
                        " -fx-border-width: 1;" +
                        " -fx-border-radius: 12;" +
                        " -fx-font-size: 14;" +
                        " -fx-font-weight: bold;" +
                        " -fx-cursor:hand"
        );

        Button registerBtn =
                new Button(
                        "Register Vehicle  →"
                );

        registerBtn.setPrefWidth(180);
        registerBtn.setPrefHeight(48);

        // =====================================================
        // REGISTER BUTTON
        // =====================================================

        registerBtn.setOnAction(e -> {

            try {

                // =================================================
                // GET FORM VALUES
                // =================================================

                String vehicleName =
                        getVehicleName();

                String vehiclePlateNumber =
                        getVehiclePlate();

                String capacityText =
                        getVehicleCapacity();

                String vehicleType =
                        getVehicleType();

                String fuelType =
                        getFuelType();

                String vehicleColour =
                        getVehicleColour();

                String manufacturingYearText =
                        getManufacturingYear();

                // =================================================
                // VALIDATION
                // =================================================

                if (vehicleName == null ||
                        vehicleName.trim().isEmpty()) {

                    showAlert(
                            "Validation Error",
                            "Please enter vehicle name."
                    );

                    return;
                }

                if (vehiclePlateNumber == null ||
                        vehiclePlateNumber.trim().isEmpty()) {

                    showAlert(
                            "Validation Error",
                            "Please enter vehicle plate number."
                    );

                    return;
                }

                if (capacityText == null ||
                        capacityText.trim().isEmpty()) {

                    showAlert(
                            "Validation Error",
                            "Please enter vehicle capacity."
                    );

                    return;
                }

                if (vehicleType == null ||
                        vehicleType.trim().isEmpty()) {

                    showAlert(
                            "Validation Error",
                            "Please select vehicle type."
                    );

                    return;
                }

                if (fuelType == null ||
                        fuelType.trim().isEmpty()) {

                    showAlert(
                            "Validation Error",
                            "Please select fuel type."
                    );

                    return;
                }

                if (vehicleColour == null ||
                        vehicleColour.trim().isEmpty()) {

                    showAlert(
                            "Validation Error",
                            "Please enter vehicle colour."
                    );

                    return;
                }

                if (manufacturingYearText == null ||
                        manufacturingYearText.trim().isEmpty()) {

                    showAlert(
                            "Validation Error",
                            "Please enter manufacturing year."
                    );

                    return;
                }

                // =================================================
                // DOCUMENT VALIDATION
                // =================================================

                if (rcFile == null) {

                    showAlert(
                            "RC Required",
                            "Please upload Registration Certificate."
                    );

                    return;
                }

                if (insuranceFile == null) {

                    showAlert(
                            "Insurance Required",
                            "Please upload vehicle insurance."
                    );

                    return;
                }

                // =================================================
                // CONVERT CAPACITY
                // =================================================

                double vehicleCapacity;

                try {

                    vehicleCapacity =
                            Double.parseDouble(
                                    capacityText.trim()
                            );

                } catch (NumberFormatException ex) {

                    showAlert(
                            "Invalid Capacity",
                            "Vehicle capacity must be a number."
                    );

                    return;
                }

                if (vehicleCapacity <= 0) {

                    showAlert(
                            "Invalid Capacity",
                            "Vehicle capacity must be greater than 0."
                    );

                    return;
                }

                // =================================================
                // CONVERT MANUFACTURING YEAR
                // =================================================

                int manufacturingYear;

                try {

                    manufacturingYear =
                            Integer.parseInt(
                                    manufacturingYearText.trim()
                            );

                } catch (NumberFormatException ex) {

                    showAlert(
                            "Invalid Year",
                            "Manufacturing year must be a valid number."
                    );

                    return;
                }

                // =================================================
                // CURRENT DRIVER
                // =================================================

                if (driverEmail == null ||
                        driverEmail.trim().isEmpty()) {

                    showAlert(
                            "Driver Error",
                            "Driver email is not available."
                    );

                    return;
                }

                // =================================================
                // UPLOAD RC TO CLOUDINARY
                // =================================================

                showAlert(
                        "Uploading",
                        "Uploading vehicle documents. Please wait..."
                );

                String rcUrl =
                        uploadToCloudinary(
                                rcFile,
                                "ecoload/vehicles/rc"
                        );

                // =================================================
                // UPLOAD INSURANCE TO CLOUDINARY
                // =================================================

                String insuranceUrl =
                        uploadToCloudinary(
                                insuranceFile,
                                "ecoload/vehicles/insurance"
                        );

                // =================================================
                // CREATE VEHICLE MODEL
                // =================================================

                VehicleModel vehicle =
                        new VehicleModel();

                vehicle.setDriverEmail(
                        driverEmail
                );

                vehicle.setVehicleName(
                        vehicleName
                );

                vehicle.setVehiclePlateNumber(
                        vehiclePlateNumber
                );

                vehicle.setVehicleCapacity(
                        vehicleCapacity
                );

                vehicle.setVehicleType(
                        vehicleType
                );

                vehicle.setFuelType(
                        fuelType
                );

                vehicle.setVehicleColour(
                        vehicleColour
                );

                vehicle.setManufacturingYear(
                        manufacturingYear
                );

                // =================================================
                // CLOUDINARY URLS
                // =================================================

                vehicle.setRegistrationCertificateUrl(
                        rcUrl
                );

                vehicle.setVehicleInsuranceUrl(
                        insuranceUrl
                );

                // =================================================
                // TIMESTAMPS
                // =================================================

                String now =
                        java.time.Instant
                                .now()
                                .toString();

                vehicle.setCreatedAt(now);
                vehicle.setUpdatedAt(now);

                // =================================================
                // SAVE VEHICLE TO FIRESTORE
                // =================================================

                VehicleDAO vehicleDAO =
                        new VehicleDAO(
                                FirebaseConfig.getFireStore()
                        );

                VehicleRegistrationController controller =
                        new VehicleRegistrationController(
                                vehicleDAO
                        );

                boolean vehicleSaved =
                        controller.registerVehicle(
                                vehicle
                        );

                if (!vehicleSaved) {

                    showAlert(
                            "Vehicle Registration Failed",
                            "A vehicle is already registered for this driver."
                    );

                    return;
                }

                // =================================================
                // SUCCESS
                // =================================================

                showAlert(
                        "Vehicle Registered",
                        "Vehicle registered successfully."
                );

                Login login =
                        new Login();

                HomePage.homeStage.setScene(
                        login.getScene()
                );

            } catch (Exception ex) {

                ex.printStackTrace();

                showAlert(
                        "Registration Error",
                        ex.getMessage() != null
                                ? ex.getMessage()
                                : "Something went wrong."
                );
            }
        });

        registerBtn.setStyle(
                "-fx-background-color: #047857;" +
                        " -fx-text-fill: white;" +
                        " -fx-font-size: 14;" +
                        " -fx-font-weight: bold;" +
                        " -fx-background-radius: 12;" +
                        " -fx-cursor:hand"
        );

        registerBtn.setOnMouseEntered(e ->
                registerBtn.setStyle(
                        "-fx-background-color: #065f46;" +
                                " -fx-text-fill: white;" +
                                " -fx-font-size: 14;" +
                                " -fx-font-weight: bold;" +
                                " -fx-background-radius: 12;" +
                                " -fx-cursor:hand"
                )
        );

        registerBtn.setOnMouseExited(e ->
                registerBtn.setStyle(
                        "-fx-background-color: #047857;" +
                                " -fx-text-fill: white;" +
                                " -fx-font-size: 14;" +
                                " -fx-font-weight: bold;" +
                                " -fx-background-radius: 12;" +
                                " -fx-cursor:hand"
                )
        );

        buttonBox.getChildren().addAll(
                cancelBtn,
                registerBtn
        );

        card.getChildren().addAll(
                mainContent,
                buttonBox
        );

        VBox.setVgrow(
                mainContent,
                Priority.ALWAYS
        );

        root.getChildren().addAll(
                halo1,
                halo2,
                halo3,
                card
        );

        Scene scene =
                new Scene(
                        root,
                        1536,
                        750
                );

        vehicleRegistrationScene =
                scene;

        return vehicleRegistrationScene;
    }

    // =====================================================
    // CLOUDINARY UPLOAD
    // =====================================================

    private String uploadToCloudinary(
            File file,
            String folder
    ) throws Exception {

        if (file == null) {

            throw new Exception(
                    "File is not selected."
            );
        }

        System.out.println(
                "Uploading file: " +
                        file.getName()
        );

        Map<?, ?> uploadResult =
                CloudinaryConfig
                        .getCloudinary()
                        .uploader()
                        .upload(
                                file,
                                ObjectUtils.asMap(
                                        "folder",
                                        folder,
                                        "resource_type",
                                        "auto"
                                )
                        );

        String secureUrl =
                uploadResult
                        .get("secure_url")
                        .toString();

        System.out.println(
                "Cloudinary upload successful:"
        );

        System.out.println(
                secureUrl
        );

        return secureUrl;
    }

    // =====================================================
    // FILE UPLOAD BOX
    // =====================================================

    private VBox createUploadBox(
            String labelText,
            String fieldName
    ) {

        Label label =
                new Label(labelText);

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.SEMI_BOLD,
                        12
                )
        );

        label.setTextFill(
                Color.web("#14532d")
        );

        VBox uploadArea =
                new VBox(12);

        uploadArea.setPadding(
                new Insets(24)
        );

        uploadArea.setAlignment(
                Pos.CENTER
        );

        uploadArea.setStyle(
                "-fx-border-color: rgba(16, 185, 129, 0.4);" +
                        " -fx-border-style: dashed;" +
                        " -fx-border-radius: 12;" +
                        " -fx-background-color: rgba(16, 185, 129, 0.06);" +
                        " -fx-background-radius: 12;"
        );

        uploadArea.setCursor(
                Cursor.HAND
        );

        Label uploadIcon =
                new Label("📄");

        uploadIcon.setFont(
                Font.font(
                        "Arial",
                        28
                )
        );

        Label uploadText =
                new Label(
                        "Drag & drop or browse"
                );

        uploadText.setFont(
                Font.font(
                        "Arial",
                        13
                )
        );

        uploadText.setTextFill(
                Color.web("#166534")
        );

        Label uploadHint =
                new Label(
                        "PDF, JPG up to 10MB"
                );

        uploadHint.setFont(
                Font.font(
                        "Arial",
                        11
                )
        );

        uploadHint.setTextFill(
                Color.web("#4b5563")
        );

        uploadArea.getChildren().addAll(
                uploadIcon,
                uploadText,
                uploadHint
        );

        FileChooser fileChooser =
                new FileChooser();

        fileChooser.setTitle(
                "Upload " + labelText
        );

        fileChooser.getExtensionFilters().addAll(

                new FileChooser.ExtensionFilter(
                        "Document Files",
                        "*.pdf",
                        "*.jpg",
                        "*.jpeg",
                        "*.png"
                ),

                new FileChooser.ExtensionFilter(
                        "All Files",
                        "*.*"
                )
        );

        uploadArea.setOnMouseClicked(event -> {

            File selectedFile =
                    fileChooser.showOpenDialog(
                            null
                    );

            if (selectedFile != null) {

                // =========================================
                // FILE SIZE VALIDATION - 10 MB
                // =========================================

                long maxFileSize =
                        10 * 1024 * 1024;

                if (selectedFile.length() >
                        maxFileSize) {

                    showAlert(
                            "File Too Large",
                            "Please select a file smaller than 10 MB."
                    );

                    return;
                }

                // =========================================
                // SAVE SELECTED FILE
                // =========================================

                if (fieldName.equals(
                        "RC_Label"
                )) {

                    rcFile =
                            selectedFile;
                }

                if (fieldName.equals(
                        "Insurance_Label"
                )) {

                    insuranceFile =
                            selectedFile;
                }

                uploadText.setText(
                        selectedFile.getName()
                );

                uploadHint.setText(
                        "Ready to upload"
                );
            }
        });

        VBox container =
                new VBox(
                        8,
                        label,
                        uploadArea
                );

        container.setAlignment(
                Pos.TOP_LEFT
        );

        return container;
    }

    // =====================================================
    // USER PROFILE
    // =====================================================

    private void openUserProfile() {

        System.out.println(
                "User profile page - to be implemented"
        );
    }

    // =====================================================
    // TEXT FIELD CREATION
    // =====================================================

    private VBox createLabeledTextField(
            String labelText,
            String placeholder
    ) {

        Label label =
                new Label(labelText);

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.SEMI_BOLD,
                        12
                )
        );

        label.setTextFill(
                Color.web("#14532d")
        );

        TextField field =
                new TextField();

        field.setPromptText(
                placeholder
        );

        field.setPrefHeight(44);

        field.setStyle(
                "-fx-background-radius: 12;" +
                        " -fx-border-radius: 12;" +
                        " -fx-border-color: rgba(16, 185, 129, 0.35);" +
                        " -fx-border-width: 1;" +
                        " -fx-background-color: white;"
        );

        switch (labelText) {

            case "Vehicle Name / Nickname":

                vehicleNameTextField =
                        field;

                break;

            case "Vehicle Plate Number":

                vehiclePlateTextField =
                        field;

                break;

            case "Vehicle Capacity":

                vehicleCapacityTextField =
                        field;

                break;

            case "Vehicle Colour":

                vehicleColourTextField =
                        field;

                break;

            case "Manufacturing Year":

                manufacturingYearTextField =
                        field;

                break;
        }

        VBox inputBox =
                new VBox(
                        6,
                        label,
                        field
                );

        inputBox.setAlignment(
                Pos.TOP_LEFT
        );

        return inputBox;
    }

    // =====================================================
    // COMBO BOX CREATION
    // =====================================================

    private VBox createLabeledComboBox(
            String labelText,
            String prompt
    ) {

        Label label =
                new Label(labelText);

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.SEMI_BOLD,
                        12
                )
        );

        label.setTextFill(
                Color.web("#14532d")
        );

        ComboBox<String> comboBox =
                new ComboBox<>();

        comboBox.setPromptText(
                prompt
        );

        comboBox.setPrefHeight(44);

        comboBox.setStyle(
                "-fx-background-radius: 12;" +
                        " -fx-border-radius: 12;" +
                        " -fx-border-color: rgba(16, 185, 129, 0.35);" +
                        " -fx-border-width: 1;" +
                        " -fx-background-color: white;"
        );

        if (labelText
                .toLowerCase()
                .contains("vehicle type")) {

            comboBox.getItems().addAll(
                    "Truck",
                    "Van",
                    "Pickup",
                    "Sedan",
                    "Container Truck"
            );

            vehicleTypeComboBox =
                    comboBox;

        } else if (labelText
                .toLowerCase()
                .contains("fuel")) {

            comboBox.getItems().addAll(
                    "Diesel",
                    "Petrol",
                    "CNG",
                    "Electric"
            );

            fuelTypeComboBox =
                    comboBox;
        }

        VBox inputBox =
                new VBox(
                        6,
                        label,
                        comboBox
                );

        inputBox.setAlignment(
                Pos.TOP_LEFT
        );

        return inputBox;
    }

    // =====================================================
    // TEXT FIELD SETTERS
    // =====================================================

    public void setVehicleName(
            String vehicleName
    ) {

        vehicleNameTextField.setText(
                vehicleName
        );
    }

    public void setVehiclePlate(
            String vehiclePlate
    ) {

        vehiclePlateTextField.setText(
                vehiclePlate
        );
    }

    public void setVehicleCapacity(
            String capacity
    ) {

        vehicleCapacityTextField.setText(
                capacity
        );
    }

    public void setVehicleColour(
            String colour
    ) {

        vehicleColourTextField.setText(
                colour
        );
    }

    public void setManufacturingYear(
            String year
    ) {

        manufacturingYearTextField.setText(
                year
        );
    }

    // =====================================================
    // COMBO BOX SETTERS
    // =====================================================

    public void setVehicleType(
            String vehicleType
    ) {

        vehicleTypeComboBox.setValue(
                vehicleType
        );
    }

    public void setFuelType(
            String fuelType
    ) {

        fuelTypeComboBox.setValue(
                fuelType
        );
    }

    // =====================================================
    // FILE SETTERS
    // =====================================================

    public void setRcFile(
            File rcFile
    ) {

        this.rcFile =
                rcFile;
    }

    public void setInsuranceFile(
            File insuranceFile
    ) {

        this.insuranceFile =
                insuranceFile;
    }

    // =====================================================
    // TEXT FIELD GETTERS
    // =====================================================

    public String getVehicleName() {

        return vehicleNameTextField
                .getText();
    }

    public String getVehiclePlate() {

        return vehiclePlateTextField
                .getText();
    }

    public String getVehicleCapacity() {

        return vehicleCapacityTextField
                .getText();
    }

    public String getVehicleColour() {

        return vehicleColourTextField
                .getText();
    }

    public String getManufacturingYear() {

        return manufacturingYearTextField
                .getText();
    }

    // =====================================================
    // COMBO BOX GETTERS
    // =====================================================

    public String getVehicleType() {

        return vehicleTypeComboBox
                .getValue();
    }

    public String getFuelType() {

        return fuelTypeComboBox
                .getValue();
    }

    // =====================================================
    // FILE GETTERS
    // =====================================================

    public File getRcFile() {

        return rcFile;
    }

    public File getInsuranceFile() {

        return insuranceFile;
    }

    // =====================================================
    // ALERT
    // =====================================================

    private void showAlert(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}