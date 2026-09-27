package com.super_x.view;

// =============================
// USER
// =============================
import com.super_x.controller.usercontroller.UserLoginController;
import com.super_x.model.usermodel.CurrentUser;
import com.super_x.model.usermodel.UserModel;
import com.super_x.view.DriverView.DriverDashoard;
import com.super_x.view.UserView.UserDashboard;

// =============================
// DRIVER
// =============================
import com.super_x.dao.driverdao.DriverAuthDAO;
import com.super_x.dao.driverdao.DriverDAO;
import com.super_x.model.drivermodel.CurrentDriver;
import com.super_x.model.drivermodel.DriverModel;

//admin

import com.super_x.NavigationService;
import com.super_x.view.AdminView.AdminDashboard;

// =============================
// FIREBASE CONFIG
// =============================
import com.super_x.config.FirebaseConfig;

import javafx.concurrent.Task;
// =============================
// JAVAFX
// =============================
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javafx.stage.Screen;

import com.super_x.config.N8nEmailService;
import java.security.SecureRandom;
import java.time.Instant;

public class Login {

        private Scene loginScene;

        // =========================================================
        // GET LOGIN SCENE
        // =========================================================

        public Scene getScene() {

                // =====================================================
                // SCREEN SIZE
                // =====================================================

                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

                double screenWidth = screenBounds.getWidth();

                double screenHeight = screenBounds.getHeight();

                // =====================================================
                // MAIN CONTAINER
                // =====================================================

                HBox mainBox = new HBox();

                mainBox.setPrefSize(
                                1200,
                                700);

                mainBox.setMinSize(
                                1200,
                                700);

                mainBox.setMaxSize(
                                1200,
                                700);

                mainBox.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 18;" +
                                                "-fx-border-color: #E5E7EB;" +
                                                "-fx-border-radius: 18;");

                // =====================================================
                // LEFT PANEL
                // =====================================================

                VBox leftPanel = new VBox();

                leftPanel.setPrefWidth(430);
                leftPanel.setMinWidth(430);
                leftPanel.setMaxWidth(430);

                leftPanel.setPadding(
                                new Insets(
                                                30,
                                                40,
                                                35,
                                                45));

                leftPanel.setStyle(
                                "-fx-background-color: #103D2F;" +
                                                "-fx-background-radius: 18 0 0 18;");

                // =====================================================
                // LOGO
                // =====================================================

                ImageView topLogo = createTopLogo();

                HBox logoContainer = new HBox();

                logoContainer.setAlignment(
                                Pos.CENTER_LEFT);

                logoContainer.getChildren().add(
                                topLogo);

                VBox brandBox = new VBox();

                brandBox.setAlignment(
                                Pos.TOP_LEFT);

                brandBox.getChildren().add(
                                logoContainer);

                // =====================================================
                // TRUCK IMAGE
                // =====================================================

                StackPane truckArea = createTruckArea();

                VBox.setVgrow(
                                truckArea,
                                Priority.ALWAYS);

                // =====================================================
                // FEATURES
                // =====================================================

                VBox features = new VBox(15);

                features.setAlignment(
                                Pos.BOTTOM_LEFT);

                features.getChildren().addAll(

                                createFeature(
                                                "♙",
                                                "Secure & Reliable",
                                                "Your data is protected with\nenterprise-grade security."),

                                createFeature(
                                                "▥",
                                                "Powerful Dashboard",
                                                "Get real-time insights and\nmanage operations efficiently."),

                                createFeature(
                                                "♧",
                                                "Complete Control",
                                                "Manage users, loads, trucks\nand more from one place."));

                // =====================================================
                // ADD LEFT COMPONENTS
                // =====================================================

                leftPanel.getChildren().addAll(
                                brandBox,
                                truckArea,
                                features);

                // =====================================================
                // RIGHT PANEL
                // =====================================================

                VBox rightPanel = createRightPanel();

                // =====================================================
                // ADD PANELS
                // =====================================================

                mainBox.getChildren().addAll(
                                leftPanel,
                                rightPanel);

                // =====================================================
                // ROOT
                // =====================================================

                StackPane root = new StackPane(mainBox);

                root.setPadding(
                                new Insets(25));

                root.setStyle(
                                "-fx-background-color: #F8FAF9;");

                // =====================================================
                // SCENE
                // =====================================================

                loginScene = new Scene(
                                root,
                                screenWidth,
                                screenHeight);

                return loginScene;
        }

        // =========================================================
        // LOGO
        // =========================================================

        private ImageView createTopLogo() {

                java.io.InputStream imageStream = getClass().getResourceAsStream(
                                "/assets/translogo.png");

                if (imageStream == null) {

                        System.out.println(
                                        "/assets/translogo.png not found");

                        return new ImageView();
                }

                Image logoImage = new Image(imageStream);

                ImageView logo = new ImageView(logoImage);

                logo.setFitWidth(115);

                logo.setFitHeight(70);

                logo.setPreserveRatio(true);

                logo.setSmooth(true);

                return logo;
        }

        // =========================================================
        // TRUCK AREA
        // =========================================================

        private StackPane createTruckArea() {

                StackPane area = new StackPane();

                area.setAlignment(
                                Pos.CENTER);

                java.io.InputStream imageStream = getClass().getResourceAsStream(
                                "/assets/images/welcomeback.jpeg");

                if (imageStream == null) {

                        System.out.println(
                                        "/assets/images/welcomeback.jpeg not found");

                        Label errorLabel = new Label(
                                        "Logo image not found");

                        errorLabel.setTextFill(
                                        Color.WHITE);

                        errorLabel.setFont(
                                        Font.font(
                                                        "Arial",
                                                        FontWeight.BOLD,
                                                        16));

                        area.getChildren().add(
                                        errorLabel);

                        return area;
                }

                Image image = new Image(imageStream);

                ImageView truckImage = new ImageView(image);

                truckImage.setFitWidth(300);

                truckImage.setFitHeight(200);

                truckImage.setPreserveRatio(true);

                truckImage.setSmooth(true);

                area.getChildren().add(
                                truckImage);

                Label tagline = new Label(
                                "Smart Logistics. Better Future.");

                tagline.setTextFill(
                                Color.WHITE);

                tagline.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.NORMAL,
                                                15));

                tagline.setOpacity(0.90);

                tagline.setTranslateY(-125);

                area.getChildren().add(
                                tagline);

                return area;
        }

        // =========================================================
        // FEATURE
        // =========================================================

        private HBox createFeature(
                        String icon,
                        String title,
                        String description) {

                HBox feature = new HBox(15);

                feature.setAlignment(
                                Pos.CENTER_LEFT);

                StackPane iconBox = new StackPane();

                iconBox.setPrefSize(
                                52,
                                52);

                iconBox.setMinSize(
                                52,
                                52);

                iconBox.setMaxSize(
                                52,
                                52);

                iconBox.setStyle(
                                "-fx-background-color: #1B563F;" +
                                                "-fx-background-radius: 12;");

                Label iconLabel = new Label(icon);

                iconLabel.setFont(
                                Font.font(22));

                iconLabel.setTextFill(
                                Color.web("#9AE67E"));

                iconBox.getChildren().add(
                                iconLabel);

                VBox textBox = new VBox(3);

                Label titleLabel = new Label(title);

                titleLabel.setTextFill(
                                Color.WHITE);

                titleLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                15));

                Label descriptionLabel = new Label(description);

                descriptionLabel.setTextFill(
                                Color.WHITE);

                descriptionLabel.setOpacity(0.85);

                descriptionLabel.setFont(
                                Font.font(
                                                "Arial",
                                                13));

                textBox.getChildren().addAll(
                                titleLabel,
                                descriptionLabel);

                feature.getChildren().addAll(
                                iconBox,
                                textBox);

                return feature;
        }

        // =========================================================
        // RIGHT PANEL
        // =========================================================

        private VBox createRightPanel() {

                VBox rightPanel = new VBox();

                rightPanel.setPrefWidth(770);

                rightPanel.setMinWidth(770);

                rightPanel.setMaxWidth(770);

                rightPanel.setPadding(
                                new Insets(
                                                55,
                                                70,
                                                45,
                                                70));

                rightPanel.setAlignment(
                                Pos.TOP_CENTER);

                rightPanel.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 0 18 18 0;");

                // =====================================================
                // LOGIN ICON
                // =====================================================

                Label loginIcon = new Label("♙");

                loginIcon.setFont(
                                Font.font(58));

                loginIcon.setTextFill(
                                Color.web("#3A9B55"));

                // =====================================================
                // TITLE
                // =====================================================

                Label title = new Label(
                                "Welcome Back");

                title.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                36));

                title.setTextFill(
                                Color.web("#111827"));

                // =====================================================
                // SUBTITLE
                // =====================================================

                Label subtitle = new Label(
                                "Welcome back! Please login to continue");

                subtitle.setFont(
                                Font.font(
                                                "Arial",
                                                18));

                subtitle.setTextFill(
                                Color.web("#667085"));

                // =====================================================
                // HEADING
                // =====================================================

                VBox heading = new VBox(7);

                heading.setAlignment(
                                Pos.CENTER);

                heading.getChildren().addAll(
                                loginIcon,
                                title,
                                subtitle);

                // =====================================================
                // LOGIN AS
                // =====================================================

                Label loginAsLabel = createLabel("Login As");

                ComboBox<String> loginAsComboBox = new ComboBox<>();

                loginAsComboBox.getItems().addAll(
                                "USER",
                                "DRIVER",
                                "ADMIN");

                loginAsComboBox.setValue("USER");

                loginAsComboBox.setMaxWidth(
                                Double.MAX_VALUE);

                loginAsComboBox.setPrefHeight(58);

                loginAsComboBox.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #D0D5DD;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-font-size: 16px;");

                // =====================================================
                // EMAIL
                // =====================================================

                Label emailLabel = createLabel("Email");

                TextField emailField = new TextField();

                emailField.setPromptText(
                                "Enter your email");

                emailField.setPrefHeight(58);

                styleTextField(emailField);

                // =====================================================
                // PASSWORD HEADER
                // =====================================================

                HBox passwordHeader = new HBox();

                passwordHeader.setAlignment(
                                Pos.CENTER_LEFT);

                Label passwordLabel = createLabel("Password");

                Region space = new Region();

                HBox.setHgrow(
                                space,
                                Priority.ALWAYS);

                Label forgotPassword = new Label(
                                "Forgot Password?");

                forgotPassword.setTextFill(
                                Color.web("#287A4A"));

                forgotPassword.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                15));

                forgotPassword.setStyle("-fx-cursor: hand;");
                forgotPassword.setOnMouseClicked(event -> showForgotPasswordFlow());

                passwordHeader.getChildren().addAll(
                                passwordLabel,
                                space,
                                forgotPassword);

                // =====================================================
                // PASSWORD
                // =====================================================

                PasswordField password = new PasswordField();

                password.setPromptText(
                                "Enter your password");

                password.setPrefHeight(58);

                styleTextField(password);

                // =====================================================
                // LOGIN BUTTON
                // =====================================================

                Button loginButton = new Button(
                                "🔒   Login");

                loginButton.setPrefHeight(60);

                loginButton.setMaxWidth(
                                Double.MAX_VALUE);

                loginButton.setTextFill(
                                Color.WHITE);

                loginButton.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                19));

                loginButton.setStyle(
                                "-fx-background-color: #2D9950;" +
                                                "-fx-background-radius: 9;" +
                                                "-fx-cursor: hand;");

                // =====================================================
                // HOVER
                // =====================================================

                loginButton.setOnMouseEntered(
                                e -> loginButton.setStyle(
                                                "-fx-background-color: #247F42;" +
                                                                "-fx-background-radius: 9;" +
                                                                "-fx-cursor: hand;"));

                loginButton.setOnMouseExited(
                                e -> loginButton.setStyle(
                                                "-fx-background-color: #2D9950;" +
                                                                "-fx-background-radius: 9;" +
                                                                "-fx-cursor: hand;"));

                // =====================================================
                // LOGIN BUTTON ACTION
                // =====================================================

                UserLoginController userLoginController = new UserLoginController();

                DriverAuthDAO driverAuthDAO = new DriverAuthDAO();

                DriverDAO driverDAO = new DriverDAO(
                                FirebaseConfig.getFireStore());

                loginButton.setOnAction(e -> {

                        String email = emailField.getText().trim();

                        String passwordText = password.getText();

                        String loginType = loginAsComboBox.getValue();

                        // =================================================
                        // BASIC VALIDATION
                        // =================================================

                        if (email.isEmpty() ||
                                        passwordText.isEmpty()) {

                                showAlert(
                                                Alert.AlertType.WARNING,
                                                "Login",
                                                "Please enter email and password.");

                                return;
                        }

                        if (loginType == null ||
                                        loginType.isEmpty()) {

                                showAlert(
                                                Alert.AlertType.WARNING,
                                                "Login",
                                                "Please select login type.");

                                return;
                        }

                        // =================================================
                        // USER LOGIN
                        // =================================================

                        if ("USER".equalsIgnoreCase(loginType)) {

                                // Disable button
                                loginButton.setDisable(true);
                                loginButton.setText("Logging in...");

                                Task<UserModel> userLoginTask = new Task<>() {

                                        @Override
                                        protected UserModel call() throws Exception {

                                                System.out.println("========== USER LOGIN ==========");
                                                System.out.println("Starting User Authentication...");

                                                long start = System.currentTimeMillis();

                                                UserModel user = userLoginController.loginUser(
                                                                email,
                                                                passwordText);

                                                long end = System.currentTimeMillis();

                                                System.out.println(
                                                                "User Login Time: "
                                                                                + (end - start)
                                                                                + " ms");

                                                if (user == null) {
                                                        throw new Exception(
                                                                        "Invalid email or password.");
                                                }

                                                return user;
                                        }
                                };

                                // =============================================
                                // USER LOGIN SUCCESS
                                // =============================================

                                userLoginTask.setOnSucceeded(event -> {

                                        UserModel user = userLoginTask.getValue();

                                        // Store logged-in user
                                        CurrentUser.getInstance()
                                                        .setUser(user);

                                        // Check role
                                        if ("USER".equalsIgnoreCase(
                                                        user.getRole())) {

                                                loginButton.setDisable(false);
                                                loginButton.setText("🔒   Login");

                                                UserDashboard userDashboard = new UserDashboard();

                                                HomePage.homeStage.setScene(
                                                                userDashboard
                                                                                .getTransporterDashboardScene());

                                        } else {

                                                loginButton.setDisable(false);
                                                loginButton.setText("🔒   Login");

                                                showAlert(
                                                                Alert.AlertType.ERROR,
                                                                "Login Error",
                                                                "This account is not a USER account.");
                                        }

                                });

                                // =============================================
                                // USER LOGIN FAILED
                                // =============================================

                                userLoginTask.setOnFailed(event -> {

                                        loginButton.setDisable(false);
                                        loginButton.setText("🔒   Login");

                                        Throwable exception = userLoginTask.getException();

                                        exception.printStackTrace();

                                        showAlert(
                                                        Alert.AlertType.ERROR,
                                                        "User Login Error",
                                                        exception.getMessage());
                                });

                                // =============================================
                                // START BACKGROUND THREAD
                                // =============================================

                                Thread userLoginThread = new Thread(userLoginTask);

                                userLoginThread.setDaemon(true);

                                userLoginThread.start();

                                return;
                        }

                        // =================================================
                        // DRIVER LOGIN
                        // =================================================

                        if ("DRIVER".equalsIgnoreCase(loginType)) {

                                // Disable button so user cannot click multiple times
                                loginButton.setDisable(true);
                                loginButton.setText("Logging in...");

                                Task<DriverLoginResult> driverLoginTask = new Task<>() {

                                        @Override
                                        protected DriverLoginResult call() throws Exception {

                                                // -----------------------------------------
                                                // 1. FIREBASE AUTHENTICATION
                                                // -----------------------------------------

                                                long authStart = System.currentTimeMillis();

                                                System.out.println("========== DRIVER LOGIN ==========");
                                                System.out.println("Starting Firebase Authentication...");

                                                String uid = driverAuthDAO.loginDriver(
                                                                email,
                                                                passwordText);

                                                long authEnd = System.currentTimeMillis();

                                                System.out.println(
                                                                "Firebase Authentication Time: "
                                                                                + (authEnd - authStart)
                                                                                + " ms");

                                                if (uid == null || uid.isEmpty()) {
                                                        throw new Exception("Invalid email or password.");
                                                }

                                                // -----------------------------------------
                                                // 2. GET DRIVER PROFILE
                                                // -----------------------------------------

                                                long firestoreStart = System.currentTimeMillis();

                                                System.out.println(
                                                                "Starting Firestore Driver Fetch...");

                                                DriverModel driver = driverDAO.getDriverByEmail(email);

                                                long firestoreEnd = System.currentTimeMillis();

                                                System.out.println(
                                                                "Firestore Driver Fetch Time: "
                                                                                + (firestoreEnd - firestoreStart)
                                                                                + " ms");

                                                if (driver == null) {
                                                        throw new Exception(
                                                                        "Driver profile not found.");
                                                }

                                                return new DriverLoginResult(uid, driver);
                                        }
                                };

                                // =============================================
                                // LOGIN SUCCESS
                                // =============================================

                                driverLoginTask.setOnSucceeded(event -> {

                                        DriverLoginResult result = driverLoginTask.getValue();

                                        DriverModel driver = result.driver;

                                        // -----------------------------------------
                                        // CHECK DRIVER STATUS
                                        // -----------------------------------------

                                        String status = driver.getStatus();

                                        // -----------------------------------------
                                        // PENDING
                                        // -----------------------------------------

                                        if ("PENDING".equalsIgnoreCase(status)) {

                                                loginButton.setDisable(false);
                                                loginButton.setText("🔒   Login");

                                                showAlert(
                                                                Alert.AlertType.WARNING,
                                                                "Request Pending",
                                                                "Your registration request is still pending admin approval.");

                                                return;
                                        }

                                        // -----------------------------------------
                                        // REJECTED
                                        // -----------------------------------------

                                        if ("REJECTED".equalsIgnoreCase(status)) {

                                                loginButton.setDisable(false);
                                                loginButton.setText("🔒   Login");

                                                showAlert(
                                                                Alert.AlertType.ERROR,
                                                                "Registration Rejected",
                                                                "Your driver registration request has been rejected by admin.");

                                                return;
                                        }

                                        // -----------------------------------------
                                        // APPROVED
                                        // -----------------------------------------

                                        if ("APPROVED".equalsIgnoreCase(status)) {

                                                CurrentDriver.getInstance()
                                                                .setDriver(driver);

                                                loginButton.setDisable(false);
                                                loginButton.setText("🔒   Login");

                                                DriverDashoard driverDashboard = new DriverDashoard();

                                                HomePage.homeStage.setScene(
                                                                driverDashboard.getDashBoardScene());

                                                return;
                                        }

                                        // -----------------------------------------
                                        // UNKNOWN STATUS
                                        // -----------------------------------------

                                        loginButton.setDisable(false);
                                        loginButton.setText("🔒   Login");

                                        showAlert(
                                                        Alert.AlertType.ERROR,
                                                        "Account Error",
                                                        "Unknown driver status: " + status);
                                });

                                // =============================================
                                // LOGIN FAILED
                                // =============================================

                                driverLoginTask.setOnFailed(event -> {

                                        loginButton.setDisable(false);
                                        loginButton.setText("🔒   Login");

                                        Throwable exception = driverLoginTask.getException();

                                        exception.printStackTrace();

                                        showAlert(
                                                        Alert.AlertType.ERROR,
                                                        "Driver Login Error",
                                                        exception.getMessage());
                                });

                                // =============================================
                                // START BACKGROUND TASK
                                // =============================================

                                Thread loginThread = new Thread(driverLoginTask);

                                loginThread.setDaemon(true);

                                loginThread.start();

                                return;
                        }

                        // =================================================
                        // ADMIN LOGIN
                        // =================================================
                        if ("ADMIN".equalsIgnoreCase(loginType)) {

                                System.out.println("========== ADMIN LOGIN ==========");

                                // Show loading state
                                loginButton.setDisable(true);
                                loginButton.setText("Logging in...");

                                Task<AdminDashboard> adminTask = new Task<>() {

                                        @Override
                                        protected AdminDashboard call() throws Exception {

                                                System.out.println("Starting Admin Dashboard loading...");

                                                // Your existing code
                                                AdminDashboard adminDashboard = new AdminDashboard();

                                                NavigationService navigationService = new NavigationService(
                                                                HomePage.homeStage);

                                                adminDashboard.initialize(navigationService);

                                                return adminDashboard;
                                        }
                                };

                                // =============================================
                                // ADMIN LOAD SUCCESS
                                // =============================================

                                adminTask.setOnSucceeded(event -> {

                                        try {

                                                AdminDashboard adminDashboard = adminTask.getValue();

                                                // Open dashboard
                                                HomePage.homeStage.setScene(
                                                                adminDashboard.getAdminDashboardScene());

                                                HomePage.homeStage.setTitle(
                                                                "EcoLoad AI - Admin");

                                        } catch (Exception ex) {

                                                ex.printStackTrace();

                                                loginButton.setDisable(false);
                                                loginButton.setText("🔒   Login");

                                                showAlert(
                                                                Alert.AlertType.ERROR,
                                                                "Admin Login Error",
                                                                ex.getMessage() != null
                                                                                ? ex.getMessage()
                                                                                : "Unable to open Admin Dashboard.");
                                        }
                                });

                                // =============================================
                                // ADMIN LOAD FAILED
                                // =============================================

                                adminTask.setOnFailed(event -> {

                                        Throwable exception = adminTask.getException();

                                        exception.printStackTrace();

                                        loginButton.setDisable(false);
                                        loginButton.setText("🔒   Login");

                                        showAlert(
                                                        Alert.AlertType.ERROR,
                                                        "Admin Login Error",
                                                        exception.getMessage() != null
                                                                        ? exception.getMessage()
                                                                        : "Unable to load Admin Dashboard.");
                                });

                                // =============================================
                                // START BACKGROUND THREAD
                                // =============================================

                                Thread adminThread = new Thread(adminTask);

                                adminThread.setDaemon(true);
                                adminThread.start();

                                return;
                        }

                        // =================================================
                        // UNKNOWN LOGIN TYPE
                        // =================================================

                        showAlert(
                                        Alert.AlertType.ERROR,
                                        "Login Error",
                                        "Invalid login type.");

                });

                // =====================================================
                // CREATE ACCOUNT LINK
                // =====================================================

                Label noAccountLabel = new Label("Don't have an account?");
                noAccountLabel.setTextFill(Color.web("#667085"));
                noAccountLabel.setFont(Font.font("Arial", 14));

                Label createAccountLink = new Label("Create Account");
                createAccountLink.setTextFill(Color.web("#287A4A"));
                createAccountLink.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                createAccountLink.setStyle("-fx-cursor: hand;");
                createAccountLink.setOnMouseEntered(e ->
                                createAccountLink.setStyle("-fx-cursor: hand; -fx-underline: true;"));
                createAccountLink.setOnMouseExited(e ->
                                createAccountLink.setStyle("-fx-cursor: hand;"));
                createAccountLink.setOnMouseClicked(e -> {
                        RolePage rolePage = new RolePage();
                        HomePage.homeStage.setScene(rolePage.getRolePageScene());
                });

                HBox createAccountBox = new HBox(5, noAccountLabel, createAccountLink);
                createAccountBox.setAlignment(Pos.CENTER);

                // =====================================================
                // SECURE ACCESS
                // =====================================================

                Region line1 = new Region();

                line1.setPrefHeight(1);

                line1.setStyle(
                                "-fx-background-color: #E5E7EB;");

                Region line2 = new Region();

                line2.setPrefHeight(1);

                line2.setStyle(
                                "-fx-background-color: #E5E7EB;");

                Label secureAccess = new Label(
                                "Secure Access");

                secureAccess.setTextFill(
                                Color.web("#667085"));

                secureAccess.setFont(
                                Font.font(
                                                "Arial",
                                                14));

                HBox secureBox = new HBox(15);

                secureBox.setAlignment(
                                Pos.CENTER);

                HBox.setHgrow(
                                line1,
                                Priority.ALWAYS);

                HBox.setHgrow(
                                line2,
                                Priority.ALWAYS);

                secureBox.getChildren().addAll(
                                line1,
                                secureAccess,
                                line2);

                // =====================================================
                // AUTHORIZED PERSONNEL
                // =====================================================

                Label authorized = new Label(
                                "◇   Restricted to authorized personnel only");

                authorized.setTextFill(
                                Color.web("#667085"));

                authorized.setFont(
                                Font.font(
                                                "Arial",
                                                14));

                // =====================================================
                // FORM
                // =====================================================

                VBox form = new VBox(10);

                form.setPrefWidth(600);

                form.setMaxWidth(600);

                form.getChildren().addAll(
                                loginAsLabel,
                                loginAsComboBox,

                                emailLabel,
                                emailField,

                                passwordHeader,
                                password,

                                loginButton,
                                createAccountBox);

                // =====================================================
                // SPACING
                // =====================================================

                Region gap1 = new Region();

                gap1.setPrefHeight(25);

                Region gap2 = new Region();

                gap2.setPrefHeight(25);

                Region gap3 = new Region();

                gap3.setPrefHeight(30);

                // =====================================================
                // ADD EVERYTHING
                // =====================================================

                rightPanel.getChildren().addAll(
                                heading,
                                gap1,
                                form,
                                gap2,
                                secureBox,
                                gap3,
                                authorized);

                return rightPanel;
        }

        // =========================================================
        // CREATE LABEL
        // =========================================================

        /**
         * Password recovery stays separate from normal login: it verifies a
         * short-lived email OTP before updating the existing Firebase Auth user.
         */
        private void showForgotPasswordFlow() {
                Dialog<ButtonType> dialog = recoveryDialog(
                                "Forgot Password",
                                "Enter Email",
                                "Enter the email address registered with your EcoLoad account.");
                TextField email = new TextField();
                email.setPromptText("Enter your email");
                email.setPrefHeight(44);
                styleTextField(email);
                dialog.getDialogPane().setContent(new VBox(10, email));

                Button send = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
                send.setText("Send OTP");
                send.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                        String address = email.getText().trim();
                        if (!isValidEmail(address)) {
                                event.consume();
                                showAlert(Alert.AlertType.WARNING, "Invalid Email", "Enter a valid email address.");
                                return;
                        }
                        try {
                                DriverAuthDAO auth = new DriverAuthDAO();
                                auth.accountExists(address);
                                String otp = generateOtp();
                                boolean sent = new N8nEmailService().sendPasswordResetOtp(address, otp);
                                if (!sent) {
                                        event.consume();
                                        showAlert(Alert.AlertType.ERROR, "OTP Not Sent", "We could not send an OTP. Please try again.");
                                        return;
                                }
                                dialog.close();
                                showOtpVerification(address, otp, Instant.now().plusSeconds(600));
                        } catch (Exception exception) {
                                event.consume();
                                showAlert(Alert.AlertType.WARNING, "Account Not Found", "No EcoLoad account was found for this email.");
                        }
                });
                dialog.showAndWait();
        }

        private void showOtpVerification(String email, String expectedOtp, Instant expiresAt) {
                Dialog<ButtonType> dialog = recoveryDialog(
                                "Forgot Password",
                                "Verify OTP",
                                "Enter the six-digit OTP sent to " + email + ". It expires in 10 minutes.");
                TextField otp = new TextField();
                otp.setPromptText("6-digit OTP");
                otp.setPrefHeight(44);
                styleTextField(otp);
                dialog.getDialogPane().setContent(new VBox(10, otp));
                Button verify = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
                verify.setText("Verify OTP");
                verify.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                        if (Instant.now().isAfter(expiresAt)) {
                                event.consume();
                                showAlert(Alert.AlertType.WARNING, "OTP Expired", "This OTP has expired. Start Forgot Password again to receive a new OTP.");
                                return;
                        }
                        if (!expectedOtp.equals(otp.getText().trim())) {
                                event.consume();
                                showAlert(Alert.AlertType.WARNING, "Incorrect OTP", "The OTP is incorrect. Please check the code and try again.");
                                return;
                        }
                        dialog.close();
                        showNewPasswordDialog(email);
                });
                dialog.showAndWait();
        }

        private void showNewPasswordDialog(String email) {
                Dialog<ButtonType> dialog = recoveryDialog(
                                "Forgot Password",
                                "New Password",
                                "Choose a new password for your EcoLoad account.");
                PasswordField password = new PasswordField();
                password.setPromptText("New password (minimum 6 characters)");
                password.setPrefHeight(44);
                styleTextField(password);
                PasswordField confirm = new PasswordField();
                confirm.setPromptText("Confirm new password");
                confirm.setPrefHeight(44);
                styleTextField(confirm);
                dialog.getDialogPane().setContent(new VBox(10, password, confirm));
                Button update = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
                update.setText("Update Password");
                update.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                        if (password.getText().length() < 6) {
                                event.consume();
                                showAlert(Alert.AlertType.WARNING, "Password Too Short", "Use at least 6 characters for the new password.");
                                return;
                        }
                        if (!password.getText().equals(confirm.getText())) {
                                event.consume();
                                showAlert(Alert.AlertType.WARNING, "Passwords Do Not Match", "Enter the same new password in both fields.");
                                return;
                        }
                        try {
                                new DriverAuthDAO().updatePassword(email, password.getText());
                                dialog.close();
                                showAlert(Alert.AlertType.INFORMATION, "Password Updated", "Your password was updated successfully. You can now sign in.");
                        } catch (Exception exception) {
                                event.consume();
                                showAlert(Alert.AlertType.ERROR, "Password Update Failed", "Unable to update your password. Please try again.");
                        }
                });
                dialog.showAndWait();
        }

        private Dialog<ButtonType> recoveryDialog(String title, String heading, String message) {
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle(title);
                dialog.setHeaderText(heading);
                dialog.setContentText(message);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
                dialog.getDialogPane().setStyle("-fx-font-family: Arial; -fx-font-size: 14px;");
                return dialog;
        }

        private boolean isValidEmail(String email) {
                return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        }

        private String generateOtp() {
                return String.format("%06d", new SecureRandom().nextInt(1_000_000));
        }

        private Label createLabel(
                        String text) {

                Label label = new Label(text);

                label.setTextFill(
                                Color.web("#344054"));

                label.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                15));

                return label;
        }

        // =========================================================
        // TEXT FIELD STYLE
        // =========================================================

        private void styleTextField(
                        TextField field) {

                field.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #D0D5DD;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 0 16;" +
                                                "-fx-font-size: 16px;");
        }

        private static class DriverLoginResult {

                private final String uid;
                private final DriverModel driver;

                public DriverLoginResult(
                                String uid,
                                DriverModel driver) {

                        this.uid = uid;
                        this.driver = driver;
                }
        }

        // =========================================================
        // ALERT
        // =========================================================

        private void showAlert(
                        Alert.AlertType alertType,
                        String title,
                        String message) {

                Alert alert = new Alert(alertType);

                alert.setTitle(title);

                alert.setHeaderText(null);

                alert.setContentText(message);

                alert.showAndWait();
        }

}
