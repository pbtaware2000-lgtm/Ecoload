package com.super_x.view.DriverView;

import java.io.File;
import java.util.Map;

import com.cloudinary.utils.ObjectUtils;
import com.super_x.config.CloudinaryConfig;
import com.super_x.config.FirebaseConfig;
import com.super_x.config.N8nEmailService;
import com.super_x.controller.drivercontroller.DriverRegistrationController;
import com.super_x.dao.driverdao.DriverDAO;
import com.super_x.model.drivermodel.DriverModel;
import com.super_x.view.HomePage;
import com.super_x.view.Login;
import com.super_x.config.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

public class DriverRegistration {

    private TextField fullNameTextField;
    private TextField phoneTextField;
    private TextField emailTextField;
    private TextField licenseTextField;

    private PasswordField passwordField;
    private PasswordField confirmPasswordField;

    private File selectedLicenseFile;

    private Scene driverRegistrationScene;
    private N8nEmailService n8nEmailService;

    public DriverRegistration(){
        n8nEmailService = new N8nEmailService();
    }

    public Scene getDriverRegistrationScene() {

        StackPane root = new StackPane();

        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, "
                        + "#eef7ec 0%, #d9eddb 45%, #b3d9aa 100%);"
        );

        Circle halo1 =
                new Circle(
                        220,
                        Color.web("#88d08b", 0.16)
                );

        halo1.setTranslateX(-560);
        halo1.setTranslateY(-280);

        Circle halo2 =
                new Circle(
                        180,
                        Color.web("#bbf7d0", 0.14)
                );

        halo2.setTranslateX(520);
        halo2.setTranslateY(-240);

        Circle halo3 =
                new Circle(
                        160,
                        Color.web("#4ade80", 0.08)
                );

        halo3.setTranslateX(420);
        halo3.setTranslateY(260);

        VBox card = new VBox();

        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setMaxWidth(1550);

        card.setStyle(
                "-fx-background-color: rgba(255,255,255,0.88);"
                        + "-fx-effect: dropshadow(gaussian, "
                        + "rgba(34, 197, 94, 0.18), 35, 0, 0, 18);"
        );

        HBox page = new HBox(32);

        page.setAlignment(Pos.CENTER);
        page.setPadding(new Insets(10));

        // =====================================================
        // LEFT PANEL
        // =====================================================

        VBox leftPanel = new VBox(24);

        leftPanel.setAlignment(Pos.TOP_CENTER);
        leftPanel.setPadding(new Insets(24));
        leftPanel.setPrefWidth(500);

        leftPanel.setStyle(
                "-fx-background-color: rgba(239, 249, 237, 0.95);"
                        + "-fx-background-radius: 28;"
        );

        // =====================================================
        // BRAND
        // =====================================================

        HBox brand = new HBox(10);

        brand.setAlignment(Pos.CENTER_LEFT);

        ImageView logo = new ImageView();

        try {

            logo.setImage(
                    new Image(
                            getClass()
                                    .getResourceAsStream(
                                            "/assets/icons/EcoloadLogo.png"
                                    )
                    )
            );

        } catch (Exception ex) {

            logo.setImage(
                    new Image(
                            "https://via.placeholder.com/120x120.png?text=Logo"
                    )
            );
        }

        logo.setFitWidth(120);
        logo.setFitHeight(120);

        brand.getChildren().add(logo);

        // =====================================================
        // HERO IMAGE
        // =====================================================

        ImageView hero = new ImageView();

        try {

            hero.setImage(
                    new Image(
                            getClass()
                                    .getResourceAsStream(
                                            "/assets/images/DriverRegistration.png"
                                    )
                    )
            );

        } catch (Exception ex) {

            hero.setImage(
                    new Image(
                            "https://via.placeholder.com/320x240.png?text=EcoLoad"
                    )
            );
        }

        hero.setFitWidth(320);
        hero.setPreserveRatio(true);
        hero.setSmooth(true);

        hero.setStyle(
                "-fx-effect: dropshadow(gaussian, "
                        + "rgba(16, 185, 129, 0.18), 18, 0, 0, 8);"
        );

        // =====================================================
        // HERO TEXT
        // =====================================================

        VBox heroText = new VBox(8);

        heroText.setAlignment(Pos.TOP_LEFT);

        Label heroTitle =
                new Label(
                        "Drive.\nDeliver.\nGrow."
                );

        heroTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        32
                )
        );

        heroTitle.setTextFill(
                Color.web("#14532d")
        );

        Label heroSub =
                new Label(
                        "Become a part of the EcoLoad family today."
                );

        heroSub.setWrapText(true);

        heroSub.setTextFill(
                Color.web("#475569")
        );

        heroSub.setFont(
                Font.font("Arial", 14)
        );

        heroText.getChildren().addAll(
                heroTitle,
                heroSub
        );

        // =====================================================
        // BADGE
        // =====================================================

        HBox badgeRow = new HBox(10);

        badgeRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Circle dot1 =
                new Circle(
                        8,
                        Color.web("#d1fae5")
                );

        Circle dot2 =
                new Circle(
                        8,
                        Color.web("#a7f3d0")
                );

        Circle dot3 =
                new Circle(
                        8,
                        Color.web("#34d399")
                );

        Label joinLabel =
                new Label(
                        "Join 5k+ drivers"
                );

        joinLabel.setFont(
                Font.font("Arial", 13)
        );

        joinLabel.setTextFill(
                Color.web("#166534")
        );

        badgeRow.getChildren().addAll(
                dot1,
                dot2,
                dot3,
                joinLabel
        );

        leftPanel.getChildren().addAll(
                brand,
                hero,
                heroText,
                badgeRow
        );

        // =====================================================
        // RIGHT PANEL
        // =====================================================

        VBox rightPanel = new VBox(20);

        rightPanel.setAlignment(
                Pos.TOP_LEFT
        );

        rightPanel.setPadding(
                new Insets(24)
        );

        rightPanel.setPrefWidth(800);

        // =====================================================
        // TITLE
        // =====================================================

        Label sectionTitle =
                new Label(
                        "Driver Registration"
                );

        sectionTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        28
                )
        );

        sectionTitle.setTextFill(
                Color.web("#14532d")
        );

        Label sectionSubtitle =
                new Label(
                        "Create your account and start your journey with EcoLoad."
                );

        sectionSubtitle.setFont(
                Font.font("Arial", 15)
        );

        sectionSubtitle.setTextFill(
                Color.web("#475569")
        );

        // =====================================================
        // FULL NAME
        // =====================================================

        fullNameTextField =
                new TextField();

        fullNameTextField.setPromptText(
                "Your Name"
        );

        VBox fullNameField =
                createLabeledField(
                        "Full Name",
                        fullNameTextField
                );

        // =====================================================
        // PHONE + EMAIL
        // =====================================================

        HBox row1 = new HBox(16);

        row1.setAlignment(
                Pos.CENTER_LEFT
        );

        phoneTextField =
                new TextField();

        phoneTextField.setPromptText(
                "+91 (555) 000-0000"
        );

        emailTextField =
                new TextField();

        emailTextField.setPromptText(
                "kishor@ecoload.com"
        );

        VBox phoneField =
                createLabeledField(
                        "Phone Number",
                        phoneTextField
                );

        VBox emailField =
                createLabeledField(
                        "Email Address",
                        emailTextField
                );

        HBox.setHgrow(
                phoneField,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                emailField,
                Priority.ALWAYS
        );

        row1.getChildren().addAll(
                phoneField,
                emailField
        );

        // =====================================================
        // LICENSE NUMBER
        // =====================================================

        licenseTextField =
                new TextField();

        licenseTextField.setPromptText(
                "DL-8829-XXXX"
        );

        VBox licenseField =
                createLabeledField(
                        "Driving License Number",
                        licenseTextField
                );

        // =====================================================
        // LICENSE UPLOAD
        // =====================================================

        Label uploadTitle =
                new Label(
                        "Upload License"
                );

        uploadTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.SEMI_BOLD,
                        13
                )
        );

        uploadTitle.setTextFill(
                Color.web("#14532d")
        );

        VBox uploadBox =
                new VBox(14);

        uploadBox.setPadding(
                new Insets(20)
        );

        uploadBox.setAlignment(
                Pos.CENTER
        );

        uploadBox.setStyle(
                "-fx-border-color: rgba(16, 185, 129, 0.4);"
                        + "-fx-border-style: dashed;"
                        + "-fx-border-radius: 16;"
                        + "-fx-background-color: rgba(16, 185, 129, 0.06);"
                        + "-fx-background-radius: 16;"
        );

        uploadBox.setCursor(
                Cursor.HAND
        );

        Label uploadLabel =
                new Label(
                        "Drag and drop your document here or Browse"
                );

        uploadLabel.setFont(
                Font.font("Arial", 14)
        );

        uploadLabel.setTextFill(
                Color.web("#166534")
        );

        Label uploadHint =
                new Label(
                        "PDF, JPG, PNG up to 10MB"
                );

        uploadHint.setFont(
                Font.font("Arial", 12)
        );

        uploadHint.setTextFill(
                Color.web("#4b5563")
        );

        uploadBox.getChildren().addAll(
                uploadLabel,
                uploadHint
        );

        FileChooser fileChooser =
                new FileChooser();

        fileChooser.setTitle(
                "Upload License Document"
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

        uploadBox.setOnMouseClicked(event -> {

            selectedLicenseFile =
                    fileChooser.showOpenDialog(
                            null
                    );

            if (selectedLicenseFile != null) {

                // =============================================
                // FILE SIZE VALIDATION
                // =============================================

                long maxFileSize =
                        10L * 1024L * 1024L;

                if (selectedLicenseFile.length()
                        > maxFileSize) {

                    showAlert(
                            "File Too Large",
                            "License file must be smaller than 10 MB."
                    );

                    selectedLicenseFile =
                            null;

                    uploadLabel.setText(
                            "Drag and drop your document here or Browse"
                    );

                    uploadHint.setText(
                            "PDF, JPG, PNG up to 10MB"
                    );

                    return;
                }

                uploadLabel.setText(
                        selectedLicenseFile.getName()
                );

                uploadHint.setText(
                        "Ready to upload"
                );
            }
        });

        VBox uploadField =
                new VBox(
                        8,
                        uploadTitle,
                        uploadBox
                );

        uploadField.setAlignment(
                Pos.TOP_LEFT
        );

        // =====================================================
        // PASSWORD ROW
        // =====================================================

        HBox passwordRow =
                new HBox(16);

        passwordRow.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox passwordBox =
                createLabeledPasswordFieldWithEye(
                        "Password",
                        false
                );

        VBox confirmPasswordBox =
                createLabeledPasswordFieldWithEye(
                        "Confirm Password",
                        true
                );

        HBox.setHgrow(
                passwordBox,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                confirmPasswordBox,
                Priority.ALWAYS
        );

        passwordRow.getChildren().addAll(
                passwordBox,
                confirmPasswordBox
        );

        // =====================================================
        // CREATE ACCOUNT BUTTON
        // =====================================================

        Button createBtn =
                new Button(
                        "Create Account  →"
                );

        createBtn.setPrefHeight(52);

        createBtn.setMaxWidth(
                Double.MAX_VALUE
        );

        createBtn.setDefaultButton(true);

        createBtn.setOnAction(e -> {

            try {

                // =============================================
                // 1. GET FORM VALUES
                // =============================================

                String username =
                        fullNameTextField
                                .getText()
                                .trim();

                String phone =
                        phoneTextField
                                .getText()
                                .trim();

                String email =
                        emailTextField
                                .getText()
                                .trim();

                String licenseNumber =
                        licenseTextField
                                .getText()
                                .trim();

                // =============================================
                // 2. VALIDATION
                // =============================================

                if (username.isEmpty()) {

                    showAlert(
                            "Validation Error",
                            "Please enter your name."
                    );

                    return;
                }

                if (phone.isEmpty()) {

                    showAlert(
                            "Validation Error",
                            "Please enter your phone number."
                    );

                    return;
                }

                if (email.isEmpty()) {

                    showAlert(
                            "Validation Error",
                            "Please enter your email."
                    );

                    return;
                }

                if (licenseNumber.isEmpty()) {

                    showAlert(
                            "Validation Error",
                            "Please enter your driving license number."
                    );

                    return;
                }

                // =============================================
                // LICENSE FILE VALIDATION
                // =============================================

                if (selectedLicenseFile == null) {

                    showAlert(
                            "Validation Error",
                            "Please upload your driving license."
                    );

                    return;
                }

                // =============================================
                // PASSWORD VALIDATION
                // =============================================

                String password =
                        passwordField
                                .getText();

                String confirmPassword =
                        confirmPasswordField
                                .getText();

                if (password == null ||
                        password.isEmpty()) {

                    showAlert(
                            "Validation Error",
                            "Please enter your password."
                    );

                    return;
                }

                if (confirmPassword == null ||
                        confirmPassword.isEmpty()) {

                    showAlert(
                            "Validation Error",
                            "Please confirm your password."
                    );

                    return;
                }

                if (!password.equals(
                        confirmPassword
                )) {

                    showAlert(
                            "Validation Error",
                            "Passwords do not match."
                    );

                    return;
                }

                // =============================================
                // 3. CREATE DRIVER MODEL
                // =============================================

                DriverModel driver =
                        new DriverModel();

                driver.setUsername(
                        username
                );

                driver.setPhone(
                        phone
                );

                driver.setEmail(
                        email
                );

                driver.setDrivingLicenseNumber(
                        licenseNumber
                );
                driver.setRole("DRIVER");

                createBtn.setDisable(true);
                Thread emailThread = new Thread(() -> {

                    n8nEmailService.sendWelcomeEmail(driver);

                });

                emailThread.setDaemon(true);
                emailThread.start();

                // =============================================
                // 4. UPLOAD LICENSE TO CLOUDINARY
                // =============================================

                

                showAlert(
                        "Uploading License",
                        "Your driving license is being uploaded. "
                                + "Please click OK and wait."
                );

                String licenseUrl =
                        uploadToCloudinary(
                                selectedLicenseFile,
                                "ecoload/drivers/licenses"
                        );

                // =============================================
                // 5. SAVE CLOUDINARY URL IN MODEL
                // =============================================

                driver.setLicenseurl(
                        licenseUrl
                );

                System.out.println(
                        "License URL saved in DriverModel:"
                );

                System.out.println(
                        licenseUrl
                );

                // =============================================
                // 6. CREATE DAO
                // =============================================

                DriverDAO driverDAO =
                        new DriverDAO(
                                FirebaseConfig.getFireStore()
                        );

                // =============================================
                // 7. CREATE CONTROLLER
                // =============================================

                DriverRegistrationController controller = new DriverRegistrationController(
                                driverDAO
                        );

                // =============================================
                // 8. REGISTER DRIVER
                // =============================================

                boolean success =
                        controller.registerDriver(
                                driver,
                                password,
                                confirmPassword
                        );

                // =============================================
                // 9. SUCCESS
                // =============================================

                if (success) {

                    showAlert(
                            "Registration Successful",
                            "Driver registered successfully. "
                                    + "Now register your vehicle."
                    );

                    VehicleRegistration vehicleRegistration =
                            new VehicleRegistration(
                                    driver.getEmail()
                            );

                    HomePage.homeStage.setScene(
                            vehicleRegistration
                                    .getVehicleRegistrationScene()
                    );

                } else {

                    // =========================================
                    // DUPLICATE EMAIL
                    // =========================================

                    showAlert(
                            "Registration Failed",
                            "This email is already registered."
                    );
                }

            } catch (Exception ex) {

                ex.printStackTrace();

                showAlert(
                        "Registration Error",
                        ex.getMessage() != null
                                ? ex.getMessage()
                                : "Something went wrong."
                );

            } finally {

                createBtn.setDisable(false);
            }
        });

        createBtn.setStyle(
                "-fx-background-color: #047857;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 16;"
                        + "-fx-font-weight: bold;"
                        + "-fx-background-radius: 16;"
        );

        createBtn.setOnMouseEntered(
                e -> createBtn.setStyle(
                        "-fx-background-color: #065f46;"
                                + "-fx-text-fill: white;"
                                + "-fx-font-size: 16;"
                                + "-fx-font-weight: bold;"
                                + "-fx-background-radius: 16;"
                                + "-fx-cursor:hand"
                )
        );

        createBtn.setOnMouseExited(
                e -> createBtn.setStyle(
                        "-fx-background-color: #047857;"
                                + "-fx-text-fill: white;"
                                + "-fx-font-size: 16;"
                                + "-fx-font-weight: bold;"
                                + "-fx-background-radius: 16;"
                )
        );

        // =====================================================
        // LOGIN LINK
        // =====================================================

        Label loginLabel =
                new Label(
                        "Already have an account?"
                );

        loginLabel.setFont(
                Font.font("Arial", 13)
        );

        loginLabel.setTextFill(
                Color.web("#475569")
        );

        Hyperlink loginLink =
                new Hyperlink(
                        "Login"
                );

        loginLink.setOnAction(e -> {

            Login login =
                    new Login();

            HomePage.homeStage.setScene(
                    login.getScene()
            );
        });

        loginLink.setFont(
                Font.font("Arial", 13)
        );

        loginLink.setTextFill(
                Color.web("#047857")
        );

        loginLink.setBorder(null);

        loginLink.setPadding(
                new Insets(0)
        );

        HBox loginBox =
                new HBox(
                        5,
                        loginLabel,
                        loginLink
                );

        loginBox.setAlignment(
                Pos.CENTER
        );

        // =====================================================
        // ADD RIGHT PANEL CONTENT
        // =====================================================

        rightPanel.getChildren().addAll(
                sectionTitle,
                sectionSubtitle,
                fullNameField,
                row1,
                licenseField,
                uploadField,
                passwordRow,
                createBtn,
                loginBox
        );

        // =====================================================
        // PAGE
        // =====================================================

        page.getChildren().addAll(
                leftPanel,
                rightPanel
        );

        card.getChildren().addAll(
                page
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

        driverRegistrationScene =
                scene;

        return driverRegistrationScene;
    }

    // =========================================================
    // CLOUDINARY UPLOAD
    // =========================================================

    private String uploadToCloudinary(
            File file,
            String folder
    ) throws Exception {

        if (file == null) {

            throw new Exception(
                    "License file is not selected."
            );
        }

        System.out.println(
                "Uploading license file: "
                        + file.getName()
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

        Object secureUrl =
                uploadResult.get(
                        "secure_url"
                );

        if (secureUrl == null) {

            throw new Exception(
                    "Cloudinary did not return a secure URL."
            );
        }

        System.out.println(
                "Cloudinary upload successful."
        );

        System.out.println(
                "URL: " + secureUrl
        );

        return secureUrl.toString();
    }

    // =========================================================
    // PASSWORD FIELD
    // =========================================================

    private VBox createLabeledPasswordFieldWithEye(
            String labelText,
            boolean isConfirmPassword) {

        Label label =
                new Label(labelText);

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.SEMI_BOLD,
                        13
                )
        );

        label.setTextFill(
                Color.web("#14532d")
        );

        PasswordField field =
                new PasswordField();

        field.setPromptText(
                labelText
        );

        field.setPrefHeight(48);

        field.setStyle(
                "-fx-background-radius: 16;"
                        + "-fx-border-radius: 16;"
                        + "-fx-border-color: rgba(16, 185, 129, 0.35);"
                        + "-fx-border-width: 1;"
                        + "-fx-background-color: white;"
        );

        TextField visibleField =
                new TextField();

        visibleField.setPromptText(
                labelText
        );

        visibleField.setPrefHeight(48);

        visibleField.setStyle(
                "-fx-background-radius: 16;"
                        + "-fx-border-radius: 16;"
                        + "-fx-border-color: rgba(16, 185, 129, 0.35);"
                        + "-fx-border-width: 1;"
                        + "-fx-background-color: white;"
        );

        visibleField.setVisible(false);
        visibleField.setManaged(false);

        visibleField.textProperty()
                .bindBidirectional(
                        field.textProperty()
                );

        StackPane passwordPane =
                new StackPane(
                        field,
                        visibleField
                );

        passwordPane.setMaxWidth(
                Double.MAX_VALUE
        );

        HBox.setHgrow(
                passwordPane,
                Priority.ALWAYS
        );

        Button eyeButton =
                new Button("👁");

        eyeButton.setStyle(
                "-fx-background-color: transparent;"
                        + "-fx-cursor: hand;"
                        + "-fx-font-size: 16;"
        );

        eyeButton.setPrefSize(
                44,
                44
        );

        eyeButton.setOnAction(event -> {

            boolean showing =
                    visibleField.isVisible();

            visibleField.setVisible(
                    !showing
            );

            visibleField.setManaged(
                    !showing
            );

            field.setVisible(
                    showing
            );

            field.setManaged(
                    showing
            );

            eyeButton.setText(
                    showing
                            ? "👁"
                            : "🙈"
            );
        });

        HBox inputRow =
                new HBox(
                        8,
                        passwordPane,
                        eyeButton
                );

        inputRow.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                passwordPane,
                Priority.ALWAYS
        );

        VBox inputBox =
                new VBox(
                        6,
                        label,
                        inputRow
                );

        inputBox.setAlignment(
                Pos.TOP_LEFT
        );

        // =====================================================
        // KEEP CORRECT PASSWORD REFERENCES
        // =====================================================

        if (isConfirmPassword) {

            confirmPasswordField =
                    field;

        } else {

            passwordField =
                    field;
        }

        return inputBox;
    }

    // =========================================================
    // NORMAL TEXT FIELD
    // =========================================================

    private VBox createLabeledField(
            String labelText,
            TextField field) {

        Label label =
                new Label(
                        labelText
                );

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.SEMI_BOLD,
                        13
                )
        );

        label.setTextFill(
                Color.web("#14532d")
        );

        field.setPrefHeight(48);

        field.setStyle(
                "-fx-background-radius: 16;"
                        + "-fx-border-radius: 16;"
                        + "-fx-border-color: rgba(16, 185, 129, 0.35);"
                        + "-fx-border-width: 1;"
                        + "-fx-background-color: white;"
        );

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

    // =========================================================
    // ALERT
    // =========================================================

    private void showAlert(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }
}