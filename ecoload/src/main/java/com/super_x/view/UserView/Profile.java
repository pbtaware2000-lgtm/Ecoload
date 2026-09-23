package com.super_x.view.UserView;

import java.io.File;

import com.super_x.model.usermodel.CurrentUser;
import com.super_x.model.usermodel.UserModel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

public class Profile {

    private Scene profileScene;

    // EcoLoad colors
    private final String GREEN = "#0F7A3D";
    private final String DARK_GREEN = "#063B2E";
    private final String LIGHT_GREEN = "#F4FBF6";
    private final String BORDER = "#D8E8DE";

    public Profile() {
        createProfilePage();
    }

    public Scene getProfileScene() {
        return profileScene;
    }

    private void createProfilePage() {

        CurrentUser currentUser = CurrentUser.getInstance();

        if (!currentUser.isLoggedIn()) {
            return;
        }
        UserModel user = currentUser.getUser();

        // =========================================================
        // MAIN ROOT
        // =========================================================

        BorderPane mainroot = new BorderPane();
        mainroot.setLeft(UserNavigation.createSidebar("Profile"));

        BorderPane mainContent = new BorderPane();
        mainContent.setTop(UserNavigation.createNavbar());

        VBox root = new VBox(18);
        mainContent.setCenter(root);
        root.setPadding(new Insets(25));
        root.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.web(LIGHT_GREEN),
                                CornerRadii.EMPTY,
                                Insets.EMPTY)));

        // =========================================================
        // PAGE HEADER
        // =========================================================

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);

        Label title = new Label("Manage Profile");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setTextFill(Color.web(DARK_GREEN));

        Label subtitle = new Label(
                "View and manage your personal transporter information.");
        subtitle.setFont(Font.font("Arial", 15));
        subtitle.setTextFill(Color.web("#53645B"));

        titleBox.getChildren().addAll(title, subtitle);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        Button cancelButton = new Button("Cancel");
        styleOutlineButton(cancelButton);

        Button saveButton = new Button("✓  Save Changes");
        styleGreenButton(saveButton);

        HBox actionButtons = new HBox(12);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);
        actionButtons.getChildren().addAll(cancelButton, saveButton);

        header.getChildren().addAll(
                titleBox,
                headerSpacer,
                actionButtons);

        // =========================================================
        // PERSONAL INFORMATION
        // =========================================================

        VBox personalCard = createCard();

        Label personalTitle = sectionTitle("Personal Information");

        GridPane personalGrid = new GridPane();
        personalGrid.setHgap(28);
        personalGrid.setVgap(14);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);

        personalGrid.getColumnConstraints().addAll(col1, col2);

        TextField fullName = createTextField(user.getUsername());
        TextField phone = createTextField(user.getPhone());
        TextField email = createTextField(user.getEmail());

        DatePicker dob = new DatePicker();
        dob.setPromptText("Date of birth");
        styleDatePicker(dob);

        TextArea address = new TextArea(user.getAddress());
        address.setPrefRowCount(2);
        address.setWrapText(true);
        styleTextArea(address);

        personalGrid.add(fieldBox("Full Name", fullName), 0, 0);
        personalGrid.add(fieldBox("Phone Number", phone), 1, 0);

        personalGrid.add(fieldBox("Email Address", email), 0, 1);
        personalGrid.add(fieldBox("Date of Birth", dob), 1, 1);

        personalGrid.add(fieldBox("Address", address), 0, 2, 2, 1);

        personalCard.getChildren().addAll(
                personalTitle,
                personalGrid);

        // =========================================================
        // PROFILE SUMMARY CARD
        // =========================================================

        VBox profileCard = createCard();
        profileCard.setPrefWidth(300);
        profileCard.setMaxWidth(300);

        String username = user.getUsername();
        String initials = getInitials(username);
        CirclePane avatar = new CirclePane(initials);
        Label name = new Label(username);
        name.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        name.setTextFill(Color.web(DARK_GREEN));

        Label transporterType = new Label(user.getBusinessType());
        transporterType.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        transporterType.setTextFill(Color.web(GREEN));

        transporterType.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.web("#DDF5E6"),
                                new CornerRadii(20),
                                Insets.EMPTY)));

        transporterType.setPadding(new Insets(7, 12, 7, 12));

        HBox badgeBox = new HBox(transporterType);
        badgeBox.setAlignment(Pos.CENTER);

        Separator separator = new Separator();

        Label status = infoRow("Account Status", "● Active");
        Label memberSince = infoRow("Member Since", "14 Aug 2026");
        // Label updated = infoRow("Last Updated", "2 mins ago");

        Button resetpass = new Button("Reset Password");
        resetpass.setStyle(
                "-fx-background-color: #0B7D3B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 30px;" +
                        "-fx-padding: 12px 28px;" +
                        "-fx-cursor: hand;");
        resetpass.setOnAction(e -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Account Security");
            dialog.setHeaderText("Reset Your Password");

            VBox box = new VBox(15);
            box.setPadding(new Insets(25));
            box.setPrefWidth(400);
            box.setStyle("-fx-background-color: white;");

            Label title1 = new Label("Account Security");
            title1.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #12372A;");

            Label subtitle1 = new Label("Update your password to keep your account secure.");
            subtitle1.setStyle("-fx-font-size: 13px; -fx-text-fill: #666666;");

            PasswordField currentPassword = new PasswordField();
            currentPassword.setPromptText("Current Password");
            stylePasswordField(currentPassword);

            PasswordField newPassword = new PasswordField();
            newPassword.setPromptText("New Password");
            stylePasswordField(newPassword);

            PasswordField confirmPassword = new PasswordField();
            confirmPassword.setPromptText("Confirm New Password");
            stylePasswordField(confirmPassword);

            Button resetButton = new Button("Reset Password");
            resetButton.setStyle(
                    "-fx-background-color: #0B7D3B;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 25px;" +
                            "-fx-padding: 10px 22px;" +
                            "-fx-cursor: hand;");

            Button cancelButton1 = new Button("Cancel");
            cancelButton1.setStyle(
                    "-fx-background-color: #E8E8E8;" +
                            "-fx-text-fill: #333333;" +
                            "-fx-font-size: 14px;" +
                            "-fx-background-radius: 25px;" +
                            "-fx-padding: 10px 22px;" +
                            "-fx-cursor: hand;");

            HBox buttons = new HBox(12, cancelButton1, resetButton);
            buttons.setAlignment(Pos.CENTER_RIGHT);

            box.getChildren().addAll(
                    title1,
                    subtitle1,
                    currentPassword,
                    newPassword,
                    confirmPassword,
                    buttons);

            dialog.getDialogPane().setContent(box);

            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            Button closeButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
            closeButton.setVisible(false);
            closeButton.setManaged(false);

            cancelButton.setOnAction(event -> dialog.close());

            resetButton.setOnAction(event -> {
                if (currentPassword.getText().isEmpty() ||
                        newPassword.getText().isEmpty() ||
                        confirmPassword.getText().isEmpty()) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Account Security");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill in all password fields.");
                    alert.showAndWait();

                } else if (!newPassword.getText().equals(confirmPassword.getText())) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Account Security");
                    alert.setHeaderText(null);
                    alert.setContentText("New password and confirm password do not match.");
                    alert.showAndWait();

                } else {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Account Security");
                    alert.setHeaderText(null);
                    alert.setContentText("Password reset successfully!");
                    alert.showAndWait();

                    dialog.close();
                }
            });
            dialog.showAndWait();
        });

        profileCard.setAlignment(Pos.TOP_CENTER);

        profileCard.getChildren().addAll(
                avatar,
                name,
                badgeBox,
                separator,
                status,
                memberSince,
                resetpass);

        // =========================================================
        // TOP CONTENT
        // =========================================================

        HBox topContent = new HBox(22);
        topContent.setFillHeight(true);

        HBox.setHgrow(personalCard, Priority.ALWAYS);

        topContent.getChildren().addAll(
                profileCard,
                personalCard);

        // =========================================================
        // TRANSPORT SERVICES
        // =========================================================

        VBox transportCard = createCard();

        Label transportTitle = sectionTitle("Transport Services");

        GridPane transportGrid = new GridPane();
        transportGrid.setHgap(28);

        ColumnConstraints transportCol1 = new ColumnConstraints();
        transportCol1.setPercentWidth(50);

        ColumnConstraints transportCol2 = new ColumnConstraints();
        transportCol2.setPercentWidth(50);

        transportGrid.getColumnConstraints().addAll(
                transportCol1,
                transportCol2);

        // ComboBox<String> transporterType = new ComboBox<String>();
        // transporterType.getItems().add("Personal / Individual");
        // transporterType.setValue("Personal / Individual");
        // transporterType.setDisable(true);
        // transporterType.setMaxWidth(Double.MAX_VALUE);
        // styleComboBox(transporterType);

        ComboBox<String> primaryService = new ComboBox<>();
        primaryService.getItems().addAll(
                "House Shifting",
                "Local Transport",
                "Goods Transport",
                "Commercial Transport");
        primaryService.setValue("House Shifting");
        primaryService.setMaxWidth(Double.MAX_VALUE);
        styleComboBox(primaryService);

        transportGrid.add(
                fieldBox("Transporter Type", transporterType),
                0, 0);

        transportGrid.add(
                fieldBox("Primary Service", primaryService),
                1, 0);

        transportCard.getChildren().addAll(
                transportTitle,
                transportGrid);

        // =========================================================
        // HOUSE-SHIFTING MATERIALS
        // =========================================================

        VBox materialsCard = createCard();

        Label materialsTitle = sectionTitle("House-Shifting Materials");

        Label materialsDescription = new Label(
                "Select the types of materials you are equipped to handle.");
        materialsDescription.setFont(Font.font("Arial", 14));
        materialsDescription.setTextFill(Color.web("#64756C"));

        HBox materialButtons = new HBox(10);
        materialButtons.setAlignment(Pos.CENTER_LEFT);

        ToggleButton furniture = materialButton("Furniture");
        ToggleButton appliances = materialButton("Appliances");
        ToggleButton electronics = materialButton("Electronics");
        ToggleButton boxes = materialButton("Boxes / Cartons");
        ToggleButton household = materialButton("Household Items");
        ToggleButton other = materialButton("Other");

        materialButtons.getChildren().addAll(
                furniture,
                appliances,
                electronics,
                boxes,
                household,
                other);

        materialsCard.getChildren().addAll(
                materialsTitle,
                materialsDescription,
                materialButtons);

        // =========================================================
        // ACCOUNT SECURITY
        // =========================================================

        // VBox securityCard = createCard();

        // Label securityTitle = sectionTitle("Account Security");

        // GridPane securityGrid = new GridPane();
        // securityGrid.setHgap(28);

        // ColumnConstraints securityCol1 = new ColumnConstraints();
        // securityCol1.setPercentWidth(50);

        // ColumnConstraints securityCol2 = new ColumnConstraints();
        // securityCol2.setPercentWidth(50);

        // securityGrid.getColumnConstraints().addAll(
        // securityCol1,
        // securityCol2);

        // PasswordField newPassword = new PasswordField();
        // newPassword.setPromptText("New Password");
        // stylePasswordField(newPassword);

        // PasswordField confirmPassword = new PasswordField();
        // confirmPassword.setPromptText("Confirm Password");
        // stylePasswordField(confirmPassword);

        // securityGrid.add(
        // fieldBox("New Password", newPassword),
        // 0, 0);

        // securityGrid.add(
        // fieldBox("Confirm Password", confirmPassword),
        // 1, 0);

        // securityCard.getChildren().addAll(
        // securityTitle,
        // securityGrid);

        // =========================================================
        // IDENTITY VERIFICATION
        // =========================================================

        VBox verificationCard = createCard();

        Label verificationTitle = sectionTitle("Identity Verification");

        HBox verificationRow = new HBox(15);
        verificationRow.setAlignment(Pos.CENTER_LEFT);

        Label documentIcon = new Label("▣");
        documentIcon.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        documentIcon.setTextFill(Color.web(GREEN));

        VBox documentInfo = new VBox(4);

        Label documentName = new Label("Identity Verification");
        documentName.setFont(
                Font.font("Arial", FontWeight.BOLD, 15));

        Label documentStatus = new Label(
                "Verification completed");
        documentStatus.setFont(Font.font("Arial", 13));
        documentStatus.setTextFill(Color.web("#64756C"));

        documentInfo.getChildren().addAll(
                documentName,
                documentStatus);

        Region verificationSpacer = new Region();
        HBox.setHgrow(
                verificationSpacer,
                Priority.ALWAYS);

        Label verified = new Label("✓ Verified");
        verified.setFont(
                Font.font("Arial", FontWeight.BOLD, 13));
        verified.setTextFill(Color.web(GREEN));

        verified.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.web("#DDF5E6"),
                                new CornerRadii(20),
                                Insets.EMPTY)));

        verified.setPadding(
                new Insets(8, 15, 8, 15));

        verificationRow.getChildren().addAll(
                documentIcon,
                documentInfo,
                verificationSpacer,
                verified);

        verificationCard.getChildren().addAll(
                verificationTitle,
                verificationRow);

        // =========================================================
        // BOTTOM CONTENT
        // =========================================================

        HBox bottomContent = new HBox(22);

        VBox upload = createUploadBox();

        // HBox.setHgrow(securityCard, Priority.ALWAYS);
        HBox.setHgrow(verificationCard, Priority.ALWAYS);

        bottomContent.getChildren().addAll(
                upload,
                // securityCard,
                verificationCard);

        // =========================================================
        // BUTTON ACTIONS
        // =========================================================

        saveButton.setOnAction(event -> {

            user.setUsername(fullName.getText().trim());
            user.setEmail(email.getText().trim());
            user.setPhone(phone.getText().trim());
            user.setAddress(address.getText().trim());

            CurrentUser.getInstance().setUser(user);

            Alert alert = new Alert(
                    Alert.AlertType.INFORMATION);

            alert.setTitle("EcoLoad");
            alert.setHeaderText("Profile Updated");
            alert.setContentText(
                    "Your profile changes have been updated.");

            alert.showAndWait();
        });

        cancelButton.setOnAction(event -> {

            fullName.setText(user.getUsername());
            email.setText(user.getEmail());

            if (user.getPhone() != null) {
                phone.setText(user.getPhone());
            }

            if (user.getAddress() != null) {
                address.setText(user.getAddress());
            }

            dob.setValue(null);

            primaryService.setValue("House Shifting");

            furniture.setSelected(false);
            appliances.setSelected(false);
            electronics.setSelected(false);
            boxes.setSelected(false);
            household.setSelected(false);
            other.setSelected(false);
        });

        // =========================================================
        // ADD EVERYTHING TO ROOT
        // =========================================================

        root.getChildren().addAll(
                header,
                topContent,
                // transportCard,
                // materialsCard,
                bottomContent);

        // =========================================================
        // SCROLL VIEW
        // =========================================================

        ScrollPane scrollPane = new ScrollPane(mainContent);
        mainroot.setCenter(scrollPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setStyle(
                "-fx-background-color: " + LIGHT_GREEN + ";");

        // =========================================================
        // SCENE
        // =========================================================

        profileScene = new Scene(
                mainroot,
                1536,
                750);

        profileScene.setFill(
                Color.web(LIGHT_GREEN));
    }

    private void stylePasswordField(
            PasswordField field) {

        field.setPrefHeight(48);
        field.setMaxWidth(Double.MAX_VALUE);

        field.setStyle(
                "-fx-background-color: #FAFCFB;" +
                        "-fx-border-color: #D8E8DE;" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 9;" +
                        "-fx-padding: 0 14;" +
                        "-fx-font-size: 14px;");
    }

    private VBox createUploadBox() {

        VBox box = new VBox(2);

        box.setAlignment(
                Pos.CENTER);

        box.setPrefHeight(70);
        box.setPrefWidth(550);

        box.setStyle(
                "-fx-background-color: #F0FDF4;" +
                        "-fx-border-color: #9AD8AE;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-style: dashed;" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 9;" +
                        "-fx-cursor: hand;");

        Label icon = new Label("☁");

        icon.setFont(
                Font.font(23));

        icon.setTextFill(
                Color.web(GREEN));

        Label text = new Label(
                " Upload Business License/GST Certificate (Optional)\n             Drag & drop or browse");

        text.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        12));

        Label formats = new Label(
                "PDF, PNG, JPG  •  MAX 5MB");

        formats.setFont(
                Font.font("System", 9));

        formats.setTextFill(
                Color.web("#999F9B"));

        box.getChildren().addAll(
                icon,
                text,
                formats);

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

                text.setText(
                        file.getName());

                text.setTextFill(
                        Color.web(GREEN));
            }
        });

        return box;
    }

    private String getInitials(String name) {

        if (name == null || name.trim().isEmpty()) {
            return "U";
        }

        String[] parts = name.trim().split("\\s+");

        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }

        return (parts[0].substring(0, 1) +
                parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }

    // =============================================================
    // CARD
    // =============================================================

    private VBox createCard() {

        VBox card = new VBox(15);

        card.setPadding(new Insets(25));

        card.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.WHITE,
                                new CornerRadii(18),
                                Insets.EMPTY)));

        card.setBorder(
                new Border(
                        new BorderStroke(
                                Color.web(BORDER),
                                BorderStrokeStyle.SOLID,
                                new CornerRadii(18),
                                new BorderWidths(1))));

        return card;
    }

    // =============================================================
    // SECTION TITLE
    // =============================================================

    private Label sectionTitle(String text) {

        Label label = new Label(text);

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22));

        label.setTextFill(
                Color.web("#101C16"));

        return label;
    }

    // =============================================================
    // FIELD BOX
    // =============================================================

    private VBox fieldBox(
            String labelText,
            Control control) {

        VBox box = new VBox(7);

        Label label = new Label(labelText);

        label.setFont(
                Font.font("Arial", 13));

        label.setTextFill(
                Color.web("#33443B"));

        box.getChildren().addAll(
                label,
                control);

        VBox.setVgrow(
                control,
                Priority.NEVER);

        return box;
    }

    // =============================================================
    // TEXT FIELD
    // =============================================================

    private TextField createTextField(String value) {

        TextField field = new TextField(value);

        field.setPrefHeight(48);
        field.setMaxWidth(Double.MAX_VALUE);

        field.setStyle(
                "-fx-background-color: #FAFCFB;" +
                        "-fx-border-color: #D8E8DE;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 9;" +
                        "-fx-padding: 0 14;" +
                        "-fx-font-size: 14px;");

        return field;
    }

    // =============================================================
    // TEXT AREA
    // =============================================================

    private void styleTextArea(TextArea area) {

        area.setPrefHeight(75);
        area.setMaxWidth(Double.MAX_VALUE);

        area.setStyle(
                "-fx-background-color: #FAFCFB;" +
                        "-fx-border-color: #D8E8DE;" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 9;" +
                        "-fx-padding: 8;" +
                        "-fx-font-size: 14px;");
    }

    // =============================================================
    // DATE PICKER
    // =============================================================

    private void styleDatePicker(DatePicker picker) {

        picker.setPrefHeight(48);
        picker.setMaxWidth(Double.MAX_VALUE);

        picker.setStyle(
                "-fx-background-color: #FAFCFB;" +
                        "-fx-border-color: #D8E8DE;" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 9;" +
                        "-fx-font-size: 14px;");
    }

    // =============================================================
    // COMBO BOX
    // =============================================================

    private void styleComboBox(
            ComboBox<String> combo) {

        combo.setPrefHeight(48);

        combo.setStyle(
                "-fx-background-color: #FAFCFB;" +
                        "-fx-border-color: #D8E8DE;" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 9;" +
                        "-fx-font-size: 14px;");
    }

    // =============================================================
    // GREEN BUTTON
    // =============================================================

    private void styleGreenButton(Button button) {

        button.setPrefHeight(48);
        button.setPadding(
                new Insets(0, 22, 0, 22));

        button.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14));

        button.setTextFill(Color.WHITE);

        button.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.web(GREEN),
                                new CornerRadii(24),
                                Insets.EMPTY)));
    }

    // =============================================================
    // OUTLINE BUTTON
    // =============================================================

    private void styleOutlineButton(Button button) {

        button.setPrefHeight(48);
        button.setPadding(
                new Insets(0, 22, 0, 22));

        button.setFont(
                Font.font(
                        "Arial",
                        FontWeight.NORMAL,
                        14));

        button.setTextFill(
                Color.web(DARK_GREEN));

        button.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.WHITE,
                                new CornerRadii(24),
                                Insets.EMPTY)));

        button.setBorder(
                new Border(
                        new BorderStroke(
                                Color.web("#A7C4B4"),
                                BorderStrokeStyle.SOLID,
                                new CornerRadii(24),
                                new BorderWidths(1))));
    }

    // =============================================================
    // MATERIAL BUTTON
    // =============================================================

    private ToggleButton materialButton(
            String text) {

        ToggleButton button = new ToggleButton(text);

        button.setPrefHeight(42);

        button.setPadding(
                new Insets(0, 17, 0, 17));

        button.setFont(
                Font.font("Arial", 13));

        button.setTextFill(
                Color.web("#52645A"));

        button.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.WHITE,
                                new CornerRadii(22),
                                Insets.EMPTY)));

        button.setBorder(
                new Border(
                        new BorderStroke(
                                Color.web("#D0DED6"),
                                BorderStrokeStyle.SOLID,
                                new CornerRadii(22),
                                new BorderWidths(1))));

        button.selectedProperty().addListener(
                (obs, oldValue, selected) -> {

                    if (selected) {

                        button.setText("✓  " + text);

                        button.setTextFill(
                                Color.web(GREEN));

                        button.setBackground(
                                new Background(
                                        new BackgroundFill(
                                                Color.web("#DDF5E6"),
                                                new CornerRadii(22),
                                                Insets.EMPTY)));

                        button.setBorder(
                                new Border(
                                        new BorderStroke(
                                                Color.web("#72C394"),
                                                BorderStrokeStyle.SOLID,
                                                new CornerRadii(22),
                                                new BorderWidths(1))));

                    } else {

                        button.setText(text);

                        button.setTextFill(
                                Color.web("#52645A"));

                        button.setBackground(
                                new Background(
                                        new BackgroundFill(
                                                Color.WHITE,
                                                new CornerRadii(22),
                                                Insets.EMPTY)));
                    }
                });

        return button;
    }

    // =============================================================
    // PROFILE INFO ROW
    // =============================================================

    private Label infoRow(
            String labelText,
            String value) {

        Label label = new Label(
                labelText + "                         " + value);

        label.setMaxWidth(Double.MAX_VALUE);

        label.setFont(
                Font.font("Arial", 13));

        label.setTextFill(
                Color.web("#53645B"));

        label.setPadding(
                new Insets(8, 0, 8, 0));

        return label;
    }

    // =============================================================
    // SIMPLE AVATAR
    // =============================================================

    private static class CirclePane extends StackPane {

        CirclePane(String initials) {

            setPrefSize(90, 90);
            setMinSize(90, 90);
            setMaxSize(90, 90);

            setBackground(
                    new Background(
                            new BackgroundFill(
                                    Color.web("#CDEFD9"),
                                    new CornerRadii(50),
                                    Insets.EMPTY)));

            Label label = new Label(initials);

            label.setFont(
                    Font.font(
                            "Arial",
                            FontWeight.BOLD,
                            28));

            label.setTextFill(
                    Color.web("#0F7A3D"));

            getChildren().add(label);
        }
    }
}