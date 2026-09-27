package com.super_x.view.UserView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TextField;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javafx.stage.FileChooser;

import java.io.File;

import com.super_x.model.usermodel.UserModel;
import com.super_x.view.HomePage;
import com.super_x.view.Login;
import com.super_x.config.N8nEmailService;
import com.super_x.controller.usercontroller.UserRegistrationController;

public class UserRegistration {

    // =========================================================
    // COLORS
    // =========================================================

    private final String GREEN = "#16A34A";

    private final String BG = "#F0FDF4";

    private final String INPUT = "#F8FAF9";

    /*
     * Common border color for ALL input fields.
     */
    private final String BORDER = "#CDE8D5";

    private final String TEXT = "#050505";

    private final String MUTED = "#718078";

    // =========================================================
    // MAIN SCENE
    // =========================================================

    private TextField usernameField;
    private TextField phoneField;
    private TextField emailField;
    private TextField gstField;
    private ComboBox<String> businessTypeField;
    private TextField businessLicenseField;
    private TextArea addressField;
    private TextField cityField;
    private TextField stateField;
    private TextField pinCodeField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private File selectedBusinessDocument;

    private UserRegistrationController controller;
    private N8nEmailService n8nEmailService;

    public UserRegistration() {
        controller = new UserRegistrationController();
        n8nEmailService = new N8nEmailService();
    }

    public Scene getTransporterRegistrationScene() {

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: " + BG + ";");

        // =====================================================
        // MAIN CARD
        // =====================================================

        BorderPane card = new BorderPane();

        card.setMaxWidth(1450);
        card.setMaxHeight(700);

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-radius: 20;" +
                        "-fx-effect: dropshadow(" +
                        "gaussian, rgba(0,0,0,0.12), 25, 0.15, 0, 5);");

        // =====================================================
        // LEFT PANEL
        // =====================================================

        VBox leftPanel = createLeftPanel();

        card.setLeft(leftPanel);

        // =====================================================
        // RIGHT PANEL
        // =====================================================

        BorderPane rightPanel = createRightPanel();

        card.setCenter(rightPanel);

        // =====================================================
        // WRAPPER
        // =====================================================

        StackPane wrapper = new StackPane(card);

        wrapper.setPadding(
                new Insets(25));

        root.setCenter(wrapper);

        // =====================================================
        // SCENE
        // =====================================================

        return new Scene(
                root,
                1536,
                750);
    }

    // =========================================================
    // LEFT PANEL
    // =========================================================

    private VBox createLeftPanel() {

        VBox left = new VBox();

        // =====================================================
        // REGISTRATION IMAGE
        // =====================================================

        Image image = new Image(
                getClass().getResourceAsStream(
                        "/assets/images/registration.png"));

        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(350);
        imageView.setFitHeight(700);

        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        // =====================================================
        // IMAGE BOX
        // =====================================================

        StackPane imageBox = new StackPane();

        imageBox.setPrefWidth(280);
        imageBox.setPrefHeight(250);

        imageBox.setMaxWidth(280);
        imageBox.setMaxHeight(250);

        imageBox.setStyle(
                "-fx-background-color: transparent;");

        imageBox.getChildren().add(
                imageView);

        left.getChildren().add(
                imageBox);

        return left;
    }

    // =========================================================
    // RIGHT PANEL
    // =========================================================

    private BorderPane createRightPanel() {

        BorderPane right = new BorderPane();

        right.setPadding(
                new Insets(
                        28,
                        35,
                        20,
                        35));

        right.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 0 20 20 0;");

        // =====================================================
        // HEADER
        // =====================================================

        VBox header = new VBox(5);

        Label title = new Label(
                "Create your Account");

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        27));

        title.setTextFill(
                Color.web("#151917"));

        Label subtitle = new Label(
                "Create your business account with EcoLoad " +
                        "and access our premium logistics network.");

        subtitle.setFont(
                Font.font(
                        "System",
                        13));

        subtitle.setTextFill(
                Color.web(MUTED));

        header.getChildren().addAll(
                title,
                subtitle);

        right.setTop(header);

        // =====================================================
        // FORM
        // =====================================================

        GridPane form = createFormGrid();

        VBox center = new VBox(form);

        VBox.setVgrow(
                form,
                Priority.ALWAYS);

        center.setPadding(
                new Insets(
                        16,
                        0,
                        5,
                        0));

        right.setCenter(center);

        // =====================================================
        // BOTTOM
        // =====================================================

        VBox bottom = createBottomSection();

        right.setBottom(bottom);

        return right;
    }

    // =========================================================
    // FORM GRID
    // =========================================================

    private GridPane createFormGrid() {

        GridPane grid = new GridPane();

        grid.setHgap(25);
        grid.setVgap(9);

        // =====================================================
        // MAIN COLUMNS
        // =====================================================

        ColumnConstraints col1 = new ColumnConstraints();

        col1.setPercentWidth(50);

        col1.setHgrow(
                Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();

        col2.setPercentWidth(50);

        col2.setHgrow(
                Priority.ALWAYS);

        grid.getColumnConstraints().addAll(
                col1,
                col2);

        // =====================================================
        // ROW 1
        // USERNAME
        // =====================================================

        usernameField = createField(
                "▦",
                "username");

        usernameField.setMaxWidth(
                Double.MAX_VALUE);

        grid.add(
                fieldGroup(
                        "Username",
                        usernameField),
                0,
                0);

        // =====================================================
        // PHONE
        // =====================================================

        phoneField = createField(
                "☎",
                "98765 43210");

        TextField code = new TextField("+91");

        code.setPrefWidth(75);
        code.setMinWidth(75);
        code.setMaxWidth(75);

        code.setPrefHeight(44);
        code.setMinHeight(44);
        code.setMaxHeight(44);

        styleInput(code);

        HBox phoneBox = new HBox(8);

        phoneBox.setMaxWidth(
                Double.MAX_VALUE);

        phoneField.setMaxWidth(
                Double.MAX_VALUE);

        HBox.setHgrow(
                phoneField,
                Priority.ALWAYS);

        phoneBox.getChildren().addAll(
                code,
                phoneField);

        grid.add(
                fieldGroup(
                        "Phone Number",
                        phoneBox),
                1,
                0);

        // =====================================================
        // ROW 2
        // EMAIL
        // =====================================================

        emailField = createField(
                "✉",
                "contact@company.com");

        emailField.setMaxWidth(
                Double.MAX_VALUE);

        grid.add(
                fieldGroup(
                        "Email Address",
                        emailField),
                0,
                1);

        // =====================================================
        // GST
        // =====================================================

        gstField = createField(
                "▣",
                "22AAAAA0000A1Z5");

        gstField.setMaxWidth(
                Double.MAX_VALUE);

        grid.add(
                fieldGroup(
                        "GST Number (Optional)",
                        gstField),
                1,
                1);

        // =====================================================
        // ROW 3
        // BUSINESS TYPE
        // =====================================================

        businessTypeField = new ComboBox<>();

        businessTypeField.getItems().addAll(
                "Transport Company",
                "Logistics Company",
                "Fleet Owner",
                "Individual Transporter",
                "Other");

        businessTypeField.setPromptText(
                "Select business type");

        businessTypeField.setPrefHeight(44);

        businessTypeField.setMinHeight(44);

        businessTypeField.setMaxHeight(44);

        businessTypeField.setMaxWidth(
                Double.MAX_VALUE);

        styleComboBox(
                businessTypeField);

        grid.add(
                fieldGroup(
                        "Business Type(Optional)",
                        businessTypeField),
                0,
                2);

        // =====================================================
        // BUSINESS LICENSE
        // =====================================================

        businessLicenseField = createField(
                "▣",
                "LIC-99002233");

        businessLicenseField.setMaxWidth(
                Double.MAX_VALUE);

        grid.add(
                fieldGroup(
                        "Business License Number(Optional)",
                        businessLicenseField),
                1,
                2);

        // =====================================================
        // ROW 4
        // COMPANY ADDRESS
        // =====================================================

        addressField = new TextArea();

        addressField.setPromptText(
                "Full business address");

        addressField.setPrefHeight(78);
        addressField.setMinHeight(78);
        addressField.setMaxHeight(78);

        addressField.setWrapText(true);

        addressField.setMaxWidth(
                Double.MAX_VALUE);

        styleTextArea(
                addressField);

        grid.add(
                fieldGroup(
                        "Address",
                        addressField),
                0,
                3);

        // =====================================================
        // CITY + STATE
        // =====================================================

        VBox location = createCityStateSection();

        grid.add(
                location,
                1,
                3);

        // =====================================================
        // ROW 5
        // UPLOAD
        // =====================================================

        VBox upload = createUploadBox();

        grid.add(
                fieldGroup(
                        "Business License / GST Certificate(Optional)",
                        upload),
                0,
                4);

        // =====================================================
        // PASSWORD + CONFIRM PASSWORD
        // =====================================================

        VBox passwordGroup = createPasswordSection();

        grid.add(
                passwordGroup,
                1,
                4);

        return grid;
    }

    // =========================================================
    // CITY + STATE SECTION
    // =========================================================

    private VBox createCityStateSection() {

        VBox location = new VBox(8);

        // =====================================================
        // LABEL GRID
        // =====================================================

        GridPane labelGrid = new GridPane();

        labelGrid.setHgap(10);

        ColumnConstraints cityLabelColumn = new ColumnConstraints();

        cityLabelColumn.setPercentWidth(50);

        cityLabelColumn.setHgrow(
                Priority.ALWAYS);

        ColumnConstraints stateLabelColumn = new ColumnConstraints();

        stateLabelColumn.setPercentWidth(50);

        stateLabelColumn.setHgrow(
                Priority.ALWAYS);

        labelGrid.getColumnConstraints().addAll(
                cityLabelColumn,
                stateLabelColumn);

        Label cityLabel = new Label("City");

        Label stateLabel = new Label("State");

        styleFieldLabel(
                cityLabel);

        styleFieldLabel(
                stateLabel);

        labelGrid.add(
                cityLabel,
                0,
                0);

        labelGrid.add(
                stateLabel,
                1,
                0);

        // =====================================================
        // CITY + STATE FIELDS
        // =====================================================

        cityField = createSimpleField(
                "City");

        stateField = createSimpleField(
                "State");

        cityField.setMaxWidth(
                Double.MAX_VALUE);

        stateField.setMaxWidth(
                Double.MAX_VALUE);

        HBox cityState = new HBox(10);

        cityState.setMaxWidth(
                Double.MAX_VALUE);

        HBox.setHgrow(
                cityField,
                Priority.ALWAYS);

        HBox.setHgrow(
                stateField,
                Priority.ALWAYS);

        cityState.getChildren().addAll(
                cityField,
                stateField);

        // =====================================================
        // PIN
        // =====================================================

        pinCodeField = createField(
                "⌖",
                "110001");

        pinCodeField.setMaxWidth(
                Double.MAX_VALUE);

        // =====================================================
        // ADD
        // =====================================================

        location.getChildren().addAll(
                labelGrid,
                cityState,
                pinCodeField);

        return location;
    }

    // =========================================================
    // PASSWORD SECTION
    // =========================================================
    private VBox createPasswordSection() {

        VBox passwordGroup = new VBox(4);

        passwordGroup.setMaxWidth(
                Double.MAX_VALUE);

        // =====================================================
        // LABEL GRID
        // =====================================================

        GridPane labelGrid = new GridPane();

        labelGrid.setHgap(10);

        ColumnConstraints passwordLabelColumn = new ColumnConstraints();

        passwordLabelColumn.setPercentWidth(50);
        passwordLabelColumn.setHgrow(
                Priority.ALWAYS);

        ColumnConstraints confirmLabelColumn = new ColumnConstraints();

        confirmLabelColumn.setPercentWidth(50);
        confirmLabelColumn.setHgrow(
                Priority.ALWAYS);

        labelGrid.getColumnConstraints().addAll(
                passwordLabelColumn,
                confirmLabelColumn);

        Label passwordLabel = new Label("Password");

        Label confirmLabel = new Label("Confirm Password");

        styleFieldLabel(passwordLabel);
        styleFieldLabel(confirmLabel);

        labelGrid.add(
                passwordLabel,
                0,
                0);

        labelGrid.add(
                confirmLabel,
                1,
                0);

        // =====================================================
        // CREATE ACTUAL PASSWORD FIELDS
        // =====================================================

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");

        // =====================================================
        // PASSWORD BOXES
        // =====================================================

        StackPane passwordBox = createPasswordFieldWithEye(
                passwordField,
                "Password");

        StackPane confirmBox = createPasswordFieldWithEye(
                confirmPasswordField,
                "Confirm Password");

        passwordBox.setMaxWidth(
                Double.MAX_VALUE);

        confirmBox.setMaxWidth(
                Double.MAX_VALUE);

        HBox passwords = new HBox(10);

        passwords.setMaxWidth(
                Double.MAX_VALUE);

        HBox.setHgrow(
                passwordBox,
                Priority.ALWAYS);

        HBox.setHgrow(
                confirmBox,
                Priority.ALWAYS);

        passwords.getChildren().addAll(
                passwordBox,
                confirmBox);

        // =====================================================
        // ADD
        // =====================================================

        passwordGroup.getChildren().addAll(
                labelGrid,
                passwords);

        return passwordGroup;
    }

    // =========================================================
    // FIELD LABEL
    // =========================================================

    private void styleFieldLabel(
            Label label) {

        label.setFont(
                Font.font(
                        "System",
                        12));

        label.setTextFill(
                Color.web(TEXT));
    }

    // =========================================================
    // BOTTOM SECTION
    // =========================================================

    private VBox createBottomSection() {

        VBox bottom = new VBox(10);

        // =====================================================
        // TERMS ROW
        // =====================================================

        HBox terms = new HBox(6);

        terms.setAlignment(
                Pos.CENTER_LEFT);

        // =====================================================
        // CHECKBOX
        // =====================================================

        CheckBox check = new CheckBox();

        // =====================================================
        // AGREEMENT TEXT
        // =====================================================

        Label agreeLabel = new Label(
                "I agree to the");

        agreeLabel.setFont(
                Font.font(
                        "System",
                        12));

        agreeLabel.setTextFill(
                Color.web("#555D59"));

        // =====================================================
        // TERMS LINK
        // =====================================================

        Hyperlink termsLink = new Hyperlink(
                "Terms & Conditions");

        termsLink.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        12));

        termsLink.setTextFill(
                Color.web(GREEN));

        termsLink.setPadding(
                Insets.EMPTY);

        termsLink.setBorder(
                Border.EMPTY);

        termsLink.setCursor(
                javafx.scene.Cursor.HAND);

        termsLink.setOnAction(
                e -> showTermsAndConditions());

        // =====================================================
        // AND
        // =====================================================

        Label andLabel = new Label(
                "and");

        andLabel.setFont(
                Font.font(
                        "System",
                        12));

        andLabel.setTextFill(
                Color.web("#555D59"));

        // =====================================================
        // PRIVACY
        // =====================================================

        Hyperlink privacyLink = new Hyperlink(
                "Privacy Policy");

        privacyLink.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        12));

        privacyLink.setTextFill(
                Color.web(GREEN));

        privacyLink.setPadding(
                Insets.EMPTY);

        privacyLink.setBorder(
                Border.EMPTY);

        privacyLink.setCursor(
                javafx.scene.Cursor.HAND);

        privacyLink.setOnAction(
                e -> showPrivacyPolicy());

        // =====================================================
        // COMPANY
        // =====================================================

        Label companyLabel = new Label(
                "of EcoLoad Logistics.");

        companyLabel.setFont(
                Font.font(
                        "System",
                        12));

        companyLabel.setTextFill(
                Color.web("#555D59"));

        terms.getChildren().addAll(
                check,
                agreeLabel,
                termsLink,
                andLabel,
                privacyLink,
                companyLabel);

        // =====================================================
        // CREATE ACCOUNT
        // =====================================================

        Button create = new Button(
                "Create Account     →");

        create.setPrefHeight(47);

        create.setMaxWidth(
                Double.MAX_VALUE);

        create.setCursor(
                javafx.scene.Cursor.HAND);

        create.setStyle(
                "-fx-background-color: linear-gradient(" +
                        "to right, #22C55E, #087A3E);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 11;");

        // =====================================================
        // CREATE ACCOUNT ACTION
        // =====================================================
        create.setOnAction(e -> {

            // =====================================================
            // TERMS
            // =====================================================

            if (!check.isSelected()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Terms Required",
                        "Please accept the Terms & Conditions.");

                return;
            }

            // =====================================================
            // PASSWORD
            // =====================================================

            String password = passwordField.getText();

            String confirmPassword = confirmPasswordField.getText();

            if (password.isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Password Required",
                        "Please enter a password.");

                return;
            }

            if (!password.equals(confirmPassword)) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Password Error",
                        "Password and Confirm Password do not match.");

                return;
            }

            // =====================================================
            // AUTH + USER MODEL
            // =====================================================

            UserModel user = controller.createUser(

                    usernameField.getText().trim(),

                    phoneField.getText().trim(),

                    emailField.getText().trim(),

                    gstField.getText().trim(),

                    businessTypeField.getValue(),

                    businessLicenseField.getText().trim(),

                    addressField.getText().trim(),

                    cityField.getText().trim(),

                    stateField.getText().trim(),

                    pinCodeField.getText().trim(),
                    passwordField.getText());

            // =====================================================
            // AUTH FAILED
            // =====================================================

            if (user == null) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Registration Failed",
                        "Email may already be registered.");

                return;
            }

            // =====================================================
            // FIRESTORE
            // =====================================================

            boolean saved = controller.saveUser(user);

            if (saved) {

                System.out.println(
                        "================================");

                System.out.println(
                        "REGISTRATION SUCCESSFUL");

                System.out.println(
                        "Email: " +
                                user.getEmail());

                System.out.println(
                        "Firestore Document: " +
                                user.getEmail());

                System.out.println("================================");
                Thread emailThread = new Thread(() -> {

                    n8nEmailService.sendWelcomeEmail(user);

                });

                emailThread.setDaemon(true);
                emailThread.start();
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Account Created",
                        "Account created successfully.");

            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Registration Failed",
                        "Authentication succeeded but "
                                + "profile could not be saved.");
            }

        });

        // -------------------------------------------------
        // DASHBOARD
        // -------------------------------------------------

        // UserDashboard dashboard = new UserDashboard();

        // HomePage.homeStage.setScene(dashboard.getTransporterDashboardScene());

        // HomePage.homeStage.show();

        // =====================================================
        // LOGIN
        // =====================================================

        HBox login = new HBox(5);

        login.setAlignment(
                Pos.CENTER);

        Label already = new Label(
                "Already have an account?");

        already.setFont(
                Font.font(
                        "System",
                        13));

        already.setTextFill(
                Color.web(MUTED));

        Hyperlink loginLink = new Hyperlink(
                "Login here");

        loginLink.setCursor(
                javafx.scene.Cursor.HAND);

        loginLink.setOnAction(e -> {

            Login login1 = new Login();

            HomePage.homeStage.setScene(
                    login1.getScene());

            HomePage.homeStage.show();
        });

        loginLink.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        13));

        loginLink.setTextFill(
                Color.web(GREEN));

        login.getChildren().addAll(
                already,
                loginLink);

        bottom.getChildren().addAll(
                terms,
                create,
                login);

        return bottom;
    }

    // =========================================================
    // TERMS & CONDITIONS
    // =========================================================

    private void showTermsAndConditions() {

        Dialog<Void> dialog = new Dialog<>();

        dialog.setTitle(
                "Terms & Conditions");

        DialogPane dialogPane = dialog.getDialogPane();

        dialogPane.setPrefWidth(700);
        dialogPane.setPrefHeight(550);

        dialogPane.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + GREEN + ";" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;");

        // =====================================================
        // HEADING
        // =====================================================

        Label title = new Label(
                "EcoLoad Logistics");

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        22));

        title.setTextFill(
                Color.web(GREEN));

        Label subtitle = new Label(
                "Terms & Conditions");

        subtitle.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        17));

        subtitle.setTextFill(
                Color.web(TEXT));

        VBox heading = new VBox(3);

        heading.getChildren().addAll(
                title,
                subtitle);

        // =====================================================
        // TERMS TEXT
        // =====================================================

        TextArea termsText = new TextArea();

        termsText.setEditable(false);

        termsText.setWrapText(true);

        termsText.setFocusTraversable(false);

        termsText.setStyle(
                "-fx-control-inner-background: white;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #D8E8DD;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #26332C;" +
                        "-fx-padding: 12;");

        termsText.setText(

                "1. ACCEPTANCE OF TERMS\n\n" +

                        "By creating an EcoLoad Logistics account, " +
                        "you acknowledge that you have read, understood, " +
                        "and agreed to these Terms & Conditions.\n\n" +

                        "2. ACCOUNT INFORMATION\n\n" +

                        "You are responsible for providing accurate, " +
                        "complete, and up-to-date information during " +
                        "registration. You must not provide false or " +
                        "misleading business information.\n\n" +

                        "3. BUSINESS DOCUMENTS\n\n" +

                        "Any business license, GST certificate, or other " +
                        "document uploaded to EcoLoad must be genuine, " +
                        "valid, and associated with the registered business.\n\n" +

                        "4. LOAD INFORMATION\n\n" +

                        "Users are responsible for ensuring that load, " +
                        "vehicle, material, destination, pricing, and " +
                        "other transportation information submitted " +
                        "through EcoLoad is accurate.\n\n" +

                        "5. TRANSPORTATION RESPONSIBILITY\n\n" +

                        "EcoLoad provides a logistics platform for connecting " +
                        "business users and transportation participants. " +
                        "Users remain responsible for complying with applicable " +
                        "transportation laws and regulations.\n\n" +

                        "6. PROHIBITED ACTIVITIES\n\n" +

                        "You must not use EcoLoad for illegal activities, " +
                        "fraudulent transactions, unauthorized transportation, " +
                        "misrepresentation, or any activity that violates " +
                        "applicable laws.\n\n" +

                        "7. ACCOUNT SECURITY\n\n" +

                        "You are responsible for maintaining the confidentiality " +
                        "of your account credentials and for activities performed " +
                        "through your account.\n\n" +

                        "8. ACCOUNT SUSPENSION\n\n" +

                        "EcoLoad may restrict, suspend, or terminate an account " +
                        "if there is evidence of misuse, fraudulent information, " +
                        "violation of these terms, or unlawful activity.\n\n" +

                        "9. PLATFORM USAGE\n\n" +

                        "EcoLoad may update, modify, or improve platform features " +
                        "from time to time in order to provide better services.\n\n" +

                        "10. ACCEPTANCE\n\n" +

                        "By selecting the agreement checkbox during registration, " +
                        "you confirm that you have read and accepted these " +
                        "Terms & Conditions.");

        // =====================================================
        // CONTENT
        // =====================================================

        VBox content = new VBox(15);

        content.setPadding(
                new Insets(20));

        VBox.setVgrow(
                termsText,
                Priority.ALWAYS);

        content.getChildren().addAll(
                heading,
                termsText);

        dialogPane.setContent(
                content);

        // =====================================================
        // CLOSE
        // =====================================================

        dialogPane.getButtonTypes().add(
                ButtonType.CLOSE);

        Button closeButton = (Button) dialogPane.lookupButton(
                ButtonType.CLOSE);

        styleDialogCloseButton(
                closeButton);

        dialog.showAndWait();
    }

    // =========================================================
    // PRIVACY POLICY
    // =========================================================

    private void showPrivacyPolicy() {

        Dialog<Void> dialog = new Dialog<>();

        dialog.setTitle(
                "Privacy Policy");

        DialogPane dialogPane = dialog.getDialogPane();

        dialogPane.setPrefWidth(700);
        dialogPane.setPrefHeight(550);

        dialogPane.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + GREEN + ";" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;");

        // =====================================================
        // HEADING
        // =====================================================

        Label title = new Label(
                "EcoLoad Logistics");

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        22));

        title.setTextFill(
                Color.web(GREEN));

        Label subtitle = new Label(
                "Privacy Policy");

        subtitle.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        17));

        subtitle.setTextFill(
                Color.web(TEXT));

        VBox heading = new VBox(3);

        heading.getChildren().addAll(
                title,
                subtitle);

        // =====================================================
        // PRIVACY TEXT
        // =====================================================

        TextArea privacyText = new TextArea();

        privacyText.setEditable(false);

        privacyText.setWrapText(true);

        privacyText.setFocusTraversable(false);

        privacyText.setStyle(
                "-fx-control-inner-background: white;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #D8E8DD;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #26332C;" +
                        "-fx-padding: 12;");

        privacyText.setText(

                "1. INTRODUCTION\n\n" +

                        "EcoLoad Logistics respects the privacy of its users " +
                        "and is committed to protecting information provided " +
                        "through the platform.\n\n" +

                        "2. INFORMATION WE COLLECT\n\n" +

                        "During registration, EcoLoad may collect information " +
                        "such as username, phone number, email address, business " +
                        "information, business address, GST information, license " +
                        "information, and uploaded business documents.\n\n" +

                        "3. USE OF INFORMATION\n\n" +

                        "Information may be used to create and manage your account, " +
                        "provide logistics services, communicate with you, verify " +
                        "business information, and improve the platform.\n\n" +

                        "4. BUSINESS DOCUMENTS\n\n" +

                        "Documents uploaded by users may be used for business " +
                        "verification and related platform services. Users should " +
                        "only upload documents that they are authorized to provide.\n\n" +

                        "5. ACCOUNT INFORMATION\n\n" +

                        "You are responsible for keeping your account information " +
                        "accurate and informing EcoLoad when important information " +
                        "needs to be updated.\n\n" +

                        "6. DATA SECURITY\n\n" +

                        "Reasonable technical and organizational measures should " +
                        "be used to protect user information against unauthorized " +
                        "access, modification, disclosure, or destruction.\n\n" +

                        "7. THIRD-PARTY SERVICES\n\n" +

                        "EcoLoad may use third-party services for functionality " +
                        "such as storage, authentication, communication, analytics, " +
                        "or other platform requirements. Such services may process " +
                        "information according to their applicable policies.\n\n" +

                        "8. DATA SHARING\n\n" +

                        "User information should only be shared where necessary " +
                        "to provide platform services, comply with legal obligations, " +
                        "protect the platform, or with appropriate authorization.\n\n" +

                        "9. DATA RETENTION\n\n" +

                        "Information may be retained for as long as reasonably " +
                        "necessary to provide services, maintain records, resolve " +
                        "disputes, meet legal requirements, or protect legitimate " +
                        "business interests.\n\n" +

                        "10. POLICY UPDATES\n\n" +

                        "This Privacy Policy may be updated when platform features, " +
                        "legal requirements, or data-processing practices change.\n\n" +

                        "11. CONTACT\n\n" +

                        "For questions regarding privacy or personal information, " +
                        "users should contact the EcoLoad Logistics support team.");

        // =====================================================
        // CONTENT
        // =====================================================

        VBox content = new VBox(15);

        content.setPadding(
                new Insets(20));

        VBox.setVgrow(
                privacyText,
                Priority.ALWAYS);

        content.getChildren().addAll(
                heading,
                privacyText);

        dialogPane.setContent(
                content);

        // =====================================================
        // CLOSE
        // =====================================================

        dialogPane.getButtonTypes().add(
                ButtonType.CLOSE);

        Button closeButton = (Button) dialogPane.lookupButton(
                ButtonType.CLOSE);

        styleDialogCloseButton(
                closeButton);

        dialog.showAndWait();
    }

    // =========================================================
    // DIALOG CLOSE BUTTON STYLE
    // =========================================================

    private void styleDialogCloseButton(
            Button closeButton) {

        closeButton.setCursor(
                javafx.scene.Cursor.HAND);

        closeButton.setStyle(
                "-fx-background-color: " + GREEN + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 8 25 8 25;");

        closeButton.setOnMouseEntered(
                event -> closeButton.setStyle(
                        "-fx-background-color: #12813C;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 20;" +
                                "-fx-padding: 8 25 8 25;"));

        closeButton.setOnMouseExited(
                event -> closeButton.setStyle(
                        "-fx-background-color: " + GREEN + ";" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 20;" +
                                "-fx-padding: 8 25 8 25;"));
    }

    // =========================================================
    // FIELD GROUP
    // =========================================================

    private VBox fieldGroup(
            String labelText,
            javafx.scene.Node field) {

        Label label = new Label(labelText);

        styleFieldLabel(
                label);

        VBox box = new VBox(4);

        box.getChildren().addAll(
                label,
                field);

        return box;
    }

    // =========================================================
    // TEXT FIELD
    // =========================================================

    private TextField createField(
            String icon,
            String prompt) {

        TextField field = new TextField();

        field.setPromptText(
                icon + "   " + prompt);

        field.setPrefHeight(44);

        field.setMinHeight(44);

        field.setMaxHeight(44);

        field.setMaxWidth(
                Double.MAX_VALUE);

        styleInput(
                field);

        return field;
    }

    // =========================================================
    // SIMPLE TEXT FIELD
    // =========================================================

    private TextField createSimpleField(
            String prompt) {

        TextField field = new TextField();

        field.setPromptText(
                prompt);

        field.setPrefHeight(44);

        field.setMinHeight(44);

        field.setMaxHeight(44);

        field.setMaxWidth(
                Double.MAX_VALUE);

        styleInput(
                field);

        return field;
    }

    // =========================================================
    // INPUT STYLE
    // =========================================================

    private void styleInput(
            TextInputControl field) {

        field.setStyle(
                "-fx-background-color: " + INPUT + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 9;" +
                        "-fx-padding: 0 12;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #17251D;");
    }

    // =========================================================
    // TEXT AREA STYLE
    // =========================================================

    private void styleTextArea(
            TextArea area) {

        area.setStyle(
                "-fx-control-inner-background: " + INPUT + ";" +
                        "-fx-background-color: " + INPUT + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 9;" +
                        "-fx-padding: 8;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #17251D;");
    }

    // =========================================================
    // COMBOBOX STYLE
    // =========================================================

    private void styleComboBox(
            ComboBox<String> combo) {

        combo.setStyle(
                "-fx-background-color: " + INPUT + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 9;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #17251D;");
    }

    // =========================================================
    // UPLOAD BOX
    // =========================================================

    private VBox createUploadBox() {

        VBox box = new VBox(2);

        box.setAlignment(
                Pos.CENTER);

        box.setPrefHeight(70);

        box.setMinHeight(70);

        box.setMaxHeight(70);

        box.setMaxWidth(
                Double.MAX_VALUE);

        box.setStyle(
                "-fx-background-color: #F0FDF4;" +
                        "-fx-border-color: #9AD8AE;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-style: dashed;" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 9;" +
                        "-fx-cursor: hand;");

        // =====================================================
        // ICON
        // =====================================================

        Label icon = new Label("☁");

        icon.setFont(
                Font.font(23));

        icon.setTextFill(
                Color.web(GREEN));

        // =====================================================
        // MAIN TEXT
        // =====================================================

        Label text = new Label(
                "Drag & drop or browse");

        text.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        12));

        // =====================================================
        // FORMATS
        // =====================================================

        Label formats = new Label(
                "PDF, PNG, JPG  •  MAX 5MB");

        formats.setFont(
                Font.font(
                        "System",
                        9));

        formats.setTextFill(
                Color.web("#999F9B"));

        box.getChildren().addAll(
                icon,
                text,
                formats);

        // =====================================================
        // FILE CHOOSER
        // =====================================================

        box.setOnMouseClicked(e -> {

            FileChooser chooser = new FileChooser();

            chooser.setTitle(
                    "Select Business Document");

            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Documents",
                            "*.pdf",
                            "*.png",
                            "*.jpg",
                            "*.jpeg"));

            File file = chooser.showOpenDialog(
                    box.getScene().getWindow());

            if (file != null) {

                // =============================================
                // MAXIMUM FILE SIZE = 10 MB
                // =============================================

                long maxFileSize = 10L * 1024L * 1024L;

                if (file.length() > maxFileSize) {

                    showAlert(
                            Alert.AlertType.WARNING,
                            "File Too Large",
                            "Business document must be smaller than 10 MB.");

                    selectedBusinessDocument = null;

                    text.setText(
                            "Drag & drop or browse");

                    text.setTextFill(
                            Color.web(MUTED));

                    return;
                }

                // =============================================
                // SAVE SELECTED FILE
                // =============================================

                selectedBusinessDocument = file;

                // =============================================
                // DISPLAY FILE NAME
                // =============================================

                text.setText(
                        file.getName());

                text.setTextFill(
                        Color.web(GREEN));
            }
        });

        return box;
    }

    // =========================================================
    // ALERT
    // =========================================================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert = new Alert(type);

        alert.setTitle(
                title);

        alert.setHeaderText(
                null);

        alert.setContentText(
                message);

        alert.showAndWait();
    }

    private StackPane createPasswordFieldWithEye(
            PasswordField passwordField,
            String prompt) {

        // =====================================================
        // PASSWORD FIELD
        // =====================================================

        passwordField.setPromptText(prompt);

        passwordField.setPrefHeight(44);
        passwordField.setMinHeight(44);
        passwordField.setMaxHeight(44);

        passwordField.setMaxWidth(
                Double.MAX_VALUE);

        styleInput(passwordField);

        // =====================================================
        // VISIBLE TEXT FIELD
        // =====================================================

        TextField visibleField = new TextField();

        visibleField.setPromptText(prompt);

        visibleField.setPrefHeight(44);
        visibleField.setMinHeight(44);
        visibleField.setMaxHeight(44);

        visibleField.setMaxWidth(
                Double.MAX_VALUE);

        styleInput(visibleField);

        visibleField.setVisible(false);
        visibleField.setManaged(false);

        // Keep password and visible text synchronized
        visibleField.textProperty()
                .bindBidirectional(
                        passwordField.textProperty());

        // =====================================================
        // EYE BUTTON
        // =====================================================

        Button eyeButton = new Button("👁");

        eyeButton.setPrefWidth(40);
        eyeButton.setPrefHeight(40);

        eyeButton.setCursor(
                javafx.scene.Cursor.HAND);

        eyeButton.setFocusTraversable(false);

        eyeButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #718078;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 0;" +
                        "-fx-cursor: hand;");

        // =====================================================
        // TOGGLE PASSWORD VISIBILITY
        // =====================================================

        eyeButton.setOnAction(event -> {

            boolean showing = visibleField.isVisible();

            visibleField.setVisible(!showing);
            visibleField.setManaged(!showing);

            passwordField.setVisible(showing);
            passwordField.setManaged(showing);

            eyeButton.setText(
                    showing
                            ? "👁"
                            : "🙈");
        });

        // =====================================================
        // FIELD CONTAINER
        // =====================================================

        StackPane fieldPane = new StackPane();

        fieldPane.setMaxWidth(
                Double.MAX_VALUE);

        fieldPane.getChildren().addAll(
                passwordField,
                visibleField);

        StackPane.setAlignment(
                eyeButton,
                Pos.CENTER_RIGHT);

        StackPane.setMargin(
                eyeButton,
                new Insets(
                        0,
                        5,
                        0,
                        0));

        fieldPane.getChildren().add(
                eyeButton);

        return fieldPane;
    }
}