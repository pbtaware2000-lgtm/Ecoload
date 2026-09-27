package com.super_x.view.AdminView;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
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
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.io.InputStream;

public class AdminProfile {

    // =========================================================
    // COLORS
    // =========================================================

    private final String DARK_GREEN = "#004B3A";
    private final String HOVER_GREEN = "#075E49";
    private final String CLICK_GREEN = "#075E49";
    private final String NUMBER_GREEN = "#2D9950";

    private final String BG_COLOR = "#EEF8F4";
    private final String BORDER_COLOR = "#D5DBDE";
    private final String HOVER_BORDER = "#075E49";
    private final String GRAY_COLOR = "#697278";
    private final String LIGHT_GREEN = "#E8F3EE";
    private final String VERY_LIGHT_GREEN = "#F4FAF7";
    private final String SUCCESS_GREEN = "#269B58";

    // =========================================================
    // EDITABLE FIELDS
    // =========================================================

    private TextField fullNameField;
    private TextField emailField;
    private TextField phoneField;
    private TextField usernameField;
    private TextField departmentField;
    private TextField roleField;

    private Button editButton;

    // =========================================================
    // PROFILE CARD LABELS
    // =========================================================

    private Label profileNameLabel;
    private Label profileRoleLabel;
    private Label profileEmailLabel;

    // =========================================================
    // PROFILE IMAGE
    // =========================================================

    private StackPane profileAvatar;

    // =========================================================
    // CONTENT
    // =========================================================

    public VBox getContent() {

        VBox page = new VBox();

        VBox content =
                createProfileContent();

        VBox.setVgrow(
                content,
                Priority.ALWAYS
        );

        page.getChildren().add(
                content
        );

        return page;
    }

    // =========================================================
    // SCENE
    // =========================================================

    public Scene getAdminProfileScene() {

        BorderPane root =
                new BorderPane();

        root.setStyle(
                "-fx-background-color: " +
                BG_COLOR + ";"
        );

        VBox sidebar =
                createSidebar();

        VBox mainArea =
                new VBox();

        // =====================================================
        // TOP BAR
        // =====================================================

        AdminTopBar topBar =
                AdminTopBar.create();

        HBox topBarView =
                topBar.build();

        // =====================================================
        // CONTENT
        // =====================================================

        VBox content =
                createProfileContent();

        VBox.setVgrow(
                content,
                Priority.ALWAYS
        );

        mainArea.getChildren().addAll(
                topBarView,
                content
        );

        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(
                root,
                1200,
                750
        );
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private VBox createSidebar() {

        VBox sidebar =
                new VBox();

        sidebar.setPrefWidth(250);
        sidebar.setMinWidth(220);
        sidebar.setMaxWidth(300);

        sidebar.setPadding(
                new Insets(
                        25,
                        16,
                        20,
                        16
                )
        );

        sidebar.setStyle(
                "-fx-background-color: " +
                DARK_GREEN +
                ";"
        );

        // =====================================================
        // LOGO
        // =====================================================

        HBox logoBox =
                new HBox(12);

        logoBox.setAlignment(
                Pos.CENTER_LEFT
        );

        ImageView logoImageView =
                createSidebarLogo();

        VBox logoText =
                new VBox(1);

        logoText.setAlignment(
                Pos.CENTER_LEFT
        );

        Label ecoLoad =
                new Label("EcoLoad");

        ecoLoad.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        25
                )
        );

        ecoLoad.setTextFill(
                Color.WHITE
        );

        Label precision =
                new Label(
                        "PRECISION LOGISTICS"
                );

        precision.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        10
                )
        );

        precision.setTextFill(
                Color.web("#A7C7BC")
        );

        logoText.getChildren().addAll(
                ecoLoad,
                precision
        );

        logoBox.getChildren().addAll(
                logoImageView,
                logoText
        );

        // =====================================================
        // MENU
        // =====================================================

        VBox menu =
                new VBox(5);

        menu.setPadding(
                new Insets(
                        45,
                        0,
                        0,
                        0
                )
        );

        menu.setFillWidth(true);

        Button dashboardButton =
                createMenuButton(
                        "▦",
                        "Dashboard",
                        false
                );

        Button driverButton =
                createMenuButton(
                        "▰",
                        "Drivers & Users",
                        false
                );

        Button loadsButton =
                createMenuButton(
                        "♧",
                        "Loads & Trips",
                        false
                );

        Button reportsButton =
                createMenuButton(
                        "▥",
                        "Reports",
                        false
                );

        Button supportButton =
                createMenuButton(
                        "♧",
                        "Support",
                        false
                );

        menu.getChildren().addAll(
                dashboardButton,
                driverButton,
                loadsButton,
                reportsButton,
                supportButton
        );

        // =====================================================
        // SPACER
        // =====================================================

        Region spacer =
                new Region();

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS
        );

        // =====================================================
        // LOGOUT
        // =====================================================

        Button logout =
                createMenuButton(
                        "↪",
                        "Logout",
                        false
                );

        setLogoutNormalStyle(
                logout
        );

        logout.setOnMouseEntered(event -> {

            logout.setStyle(
                    "-fx-background-color: #921E2D;" +
                    "-fx-background-radius: 9;" +
                    "-fx-cursor: hand;"
            );
        });

        logout.setOnMouseExited(event -> {

            setLogoutNormalStyle(
                    logout
            );
        });

        sidebar.getChildren().addAll(
                logoBox,
                menu,
                spacer,
                logout
        );

        return sidebar;
    }

    // =========================================================
    // LOGOUT STYLE
    // =========================================================

    private void setLogoutNormalStyle(
            Button button
    ) {

        button.setStyle(
                "-fx-background-color: #B42335;" +
                "-fx-background-radius: 9;" +
                "-fx-cursor: hand;"
        );
    }

    // =========================================================
    // SIDEBAR LOGO
    // =========================================================

    private ImageView createSidebarLogo() {

        ImageView logoView =
                new ImageView();

        String imagePath =
                "/assets/Logo-removebg-preview.png";

        InputStream logoStream =
                getClass().getResourceAsStream(
                        imagePath
                );

        if (logoStream != null) {

            Image logoImage =
                    new Image(
                            logoStream
                    );

            logoView.setImage(
                    logoImage
            );

            logoView.setFitWidth(70);
            logoView.setFitHeight(70);

            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);

        } else {

            System.out.println(
                    "ERROR: Logo not found: " +
                    imagePath
            );
        }

        return logoView;
    }

    // =========================================================
    // MENU BUTTON
    // =========================================================

    private Button createMenuButton(
            String icon,
            String text,
            boolean active
    ) {

        Button button =
                new Button();

        button.setPrefHeight(48);
        button.setMinHeight(44);

        button.setMaxWidth(
                Double.MAX_VALUE
        );

        button.setAlignment(
                Pos.CENTER_LEFT
        );

        Label iconLabel =
                new Label(icon);

        iconLabel.setPrefWidth(28);

        iconLabel.setFont(
                Font.font(20)
        );

        iconLabel.setTextFill(
                Color.WHITE
        );

        Label textLabel =
                new Label(text);

        textLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        textLabel.setTextFill(
                Color.WHITE
        );

        HBox box =
                new HBox(
                        12,
                        iconLabel,
                        textLabel
                );

        box.setAlignment(
                Pos.CENTER_LEFT
        );

        button.setGraphic(box);

        if (active) {

            button.setStyle(
                    "-fx-background-color: " +
                    CLICK_GREEN +
                    ";" +
                    "-fx-background-radius: 9;" +
                    "-fx-cursor: hand;"
            );

        } else {

            button.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 9;" +
                    "-fx-cursor: hand;"
            );
        }

        button.setOnMouseEntered(event -> {

            button.setStyle(
                    "-fx-background-color: " +
                    HOVER_GREEN +
                    ";" +
                    "-fx-background-radius: 9;" +
                    "-fx-cursor: hand;"
            );

            ScaleTransition grow =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            grow.setToX(1.02);
            grow.setToY(1.02);

            grow.play();
        });

        button.setOnMouseExited(event -> {

            if (active) {

                button.setStyle(
                        "-fx-background-color: " +
                        CLICK_GREEN +
                        ";" +
                        "-fx-background-radius: 9;" +
                        "-fx-cursor: hand;"
                );

            } else {

                button.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 9;" +
                        "-fx-cursor: hand;"
                );
            }

            ScaleTransition shrink =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            shrink.setToX(1.0);
            shrink.setToY(1.0);

            shrink.play();
        });

        button.setOnAction(event -> {

            System.out.println(
                    text + " clicked"
            );
        });

        return button;
    }

    // =========================================================
    // PROFILE CONTENT
    // =========================================================

    private VBox createProfileContent() {

        VBox content =
                new VBox(18);

        content.setPadding(
                new Insets(
                        25,
                        30,
                        25,
                        30
                )
        );

        content.setStyle(
                "-fx-background-color: " +
                BG_COLOR +
                ";"
        );

        // =====================================================
        // HEADING
        // =====================================================

        VBox headingText =
                new VBox(5);

        Label title =
                new Label(
                        "Admin Profile"
                );

        title.setTextFill(
                Color.web(DARK_GREEN)
        );

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        30
                )
        );

        Label subtitle =
                new Label(
                        "Manage your account information and administrator settings"
                );

        subtitle.setTextFill(
                Color.web(GRAY_COLOR)
        );

        subtitle.setFont(
                Font.font(
                        "Arial",
                        14
                )
        );

        headingText.getChildren().addAll(
                title,
                subtitle
        );

        // =====================================================
        // MAIN AREA
        // =====================================================

        HBox mainArea =
                new HBox(20);

        VBox leftColumn =
                createProfileLeftColumn();

        VBox rightColumn =
                createPersonalInformation();

        HBox.setHgrow(
                rightColumn,
                Priority.ALWAYS
        );

        mainArea.getChildren().addAll(
                leftColumn,
                rightColumn
        );

        VBox.setVgrow(
                mainArea,
                Priority.ALWAYS
        );

        content.getChildren().addAll(
                headingText,
                mainArea
        );

        return content;
    }

    // =========================================================
    // LEFT COLUMN
    // =========================================================

    private VBox createProfileLeftColumn() {

        VBox leftColumn =
                new VBox(18);

        leftColumn.setPrefWidth(300);
        leftColumn.setMinWidth(280);
        leftColumn.setMaxWidth(330);

        // =====================================================
        // PROFILE CARD
        // =====================================================

        VBox profileCard =
                new VBox(10);

        profileCard.setAlignment(
                Pos.CENTER
        );

        profileCard.setPadding(
                new Insets(22)
        );

        profileCard.setPrefHeight(275);

        setProfileCardNormalStyle(
                profileCard
        );

        // =====================================================
        // PROFILE IMAGE
        // =====================================================

        StackPane avatar =
                createLargeProfileImage();

        // =====================================================
        // NAME
        // =====================================================

        profileNameLabel =
                new Label(
                        AdminTopBar.getAdminName()
                );

        profileNameLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        21
                )
        );

        profileNameLabel.setTextFill(
                Color.web("#172033")
        );

        // =====================================================
        // ROLE
        // =====================================================

        profileRoleLabel =
                new Label(
                        AdminTopBar.getAdminRole()
                );

        profileRoleLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13
                )
        );

        profileRoleLabel.setTextFill(
                Color.web("#344054")
        );

        // =====================================================
        // EMAIL
        // =====================================================

        profileEmailLabel =
                new Label(
                        AdminTopBar.getAdminEmail()
                );

        profileEmailLabel.setFont(
                Font.font(
                        "Arial",
                        13
                )
        );

        profileEmailLabel.setTextFill(
                Color.web(GRAY_COLOR)
        );

        // =====================================================
        // ACTIVE STATUS
        // =====================================================

        HBox status =
                new HBox(6);

        status.setAlignment(
                Pos.CENTER
        );

        status.setPadding(
                new Insets(
                        6,
                        14,
                        6,
                        14
                )
        );

        status.setStyle(
                "-fx-background-color: #E7F7ED;" +
                "-fx-background-radius: 20;"
        );

        Circle dot =
                new Circle(
                        4,
                        Color.web(SUCCESS_GREEN)
                );

        Label active =
                new Label("Active");

        active.setTextFill(
                Color.web(SUCCESS_GREEN)
        );

        active.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12
                )
        );

        status.getChildren().addAll(
                dot,
                active
        );

        profileCard.getChildren().addAll(
                avatar,
                profileNameLabel,
                profileRoleLabel,
                profileEmailLabel,
                status
        );

        // =====================================================
        // PROFILE CARD HOVER
        // =====================================================

        profileCard.setOnMouseEntered(event -> {

            setProfileCardHoverStyle(
                    profileCard
            );

            ScaleTransition grow =
                    new ScaleTransition(
                            Duration.millis(150),
                            profileCard
                    );

            grow.setToX(1.025);
            grow.setToY(1.025);

            grow.play();
        });

        profileCard.setOnMouseExited(event -> {

            setProfileCardNormalStyle(
                    profileCard
            );

            ScaleTransition shrink =
                    new ScaleTransition(
                            Duration.millis(150),
                            profileCard
                    );

            shrink.setToX(1.0);
            shrink.setToY(1.0);

            shrink.play();
        });

        // =====================================================
        // STAT CARDS
        // =====================================================

        VBox loginCard =
                statCard(
                        "↪",
                        "TOTAL LOGINS",
                        "1,248"
                );

        VBox accountCard =
                statCard(
                        "▣",
                        "ACCOUNT CREATED",
                        "Jan 2026"
                );

        leftColumn.getChildren().addAll(
                profileCard,
                loginCard,
                accountCard
        );

        return leftColumn;
    }

    // =========================================================
    // PROFILE CARD NORMAL STYLE
    // =========================================================

    private void setProfileCardNormalStyle(
            VBox card
    ) {

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: " +
                        BORDER_COLOR +
                        ";" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"
        );
    }

    // =========================================================
    // PROFILE CARD HOVER STYLE
    // =========================================================

    private void setProfileCardHoverStyle(
            VBox card
    ) {

        card.setStyle(
                "-fx-background-color: " +
                        VERY_LIGHT_GREEN +
                        ";" +
                "-fx-border-color: " +
                        HOVER_BORDER +
                        ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"
        );
    }

    // =========================================================
    // LARGE PROFILE IMAGE
    // =========================================================

    private StackPane createLargeProfileImage() {

        profileAvatar =
                new StackPane();

        profileAvatar.setPrefSize(
                110,
                110
        );

        profileAvatar.setMinSize(
                110,
                110
        );

        profileAvatar.setMaxSize(
                110,
                110
        );

        // =====================================================
        // IMAGE VIEW
        // =====================================================

        ImageView imageView =
                new ImageView();

        imageView.setFitWidth(95);
        imageView.setFitHeight(95);

        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        Circle clip =
                new Circle(
                        47.5,
                        47.5,
                        47.5
                );

        imageView.setClip(clip);

        // =====================================================
        // GET SHARED IMAGE
        // =====================================================

        Image currentImage =
                AdminTopBar.getCurrentProfileImage();

        if (currentImage != null) {

            imageView.setImage(
                    currentImage
            );

        } else {

            Circle fallback =
                    new Circle(
                            47.5,
                            Color.web("#DCEFE7")
                    );

            Label icon =
                    new Label("👤");

            icon.setFont(
                    Font.font(45)
            );

            profileAvatar.getChildren().addAll(
                    fallback,
                    icon
            );

            // Try loading default image
            InputStream stream =
                    getClass().getResourceAsStream(
                            "/assets/Admin_Profile_Logo.png"
                    );

            if (stream != null) {

                try {

                    Image defaultImage =
                            new Image(stream);

                    imageView.setImage(
                            defaultImage
                    );

                    AdminTopBar.updateProfileImage(
                            defaultImage
                    );

                } catch (Exception ignored) {
                }
            }
        }

        profileAvatar.getChildren().add(
                imageView
        );

        // =====================================================
        // PLUS BUTTON
        // =====================================================

        Button plusButton =
                new Button("+");

        plusButton.setPrefSize(
                32,
                32
        );

        plusButton.setMinSize(
                32,
                32
        );

        plusButton.setMaxSize(
                32,
                32
        );

        plusButton.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22
                )
        );

        plusButton.setTextFill(
                Color.WHITE
        );

        setPlusNormalStyle(
                plusButton
        );

        StackPane.setAlignment(
                plusButton,
                Pos.BOTTOM_RIGHT
        );

        StackPane.setMargin(
                plusButton,
                new Insets(
                        0,
                        3,
                        3,
                        0
                )
        );

        // =====================================================
        // PLUS HOVER
        // =====================================================

        plusButton.setOnMouseEntered(event -> {

            setPlusHoverStyle(
                    plusButton
            );

            ScaleTransition grow =
                    new ScaleTransition(
                            Duration.millis(100),
                            plusButton
                    );

            grow.setToX(1.1);
            grow.setToY(1.1);

            grow.play();
        });

        plusButton.setOnMouseExited(event -> {

            setPlusNormalStyle(
                    plusButton
            );

            ScaleTransition shrink =
                    new ScaleTransition(
                            Duration.millis(100),
                            plusButton
                    );

            shrink.setToX(1.0);
            shrink.setToY(1.0);

            shrink.play();
        });

        // =====================================================
        // PLUS CLICK
        // =====================================================

        plusButton.setOnAction(event -> {

            changeProfileImage(
                    plusButton
            );
        });

        profileAvatar.getChildren().add(
                plusButton
        );

        return profileAvatar;
    }

    // =========================================================
    // PLUS NORMAL STYLE
    // =========================================================

    private void setPlusNormalStyle(
            Button button
    ) {

        button.setStyle(
                "-fx-background-color: " +
                        DARK_GREEN +
                        ";" +
                "-fx-background-radius: 50%;" +
                "-fx-border-color: white;" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 50%;" +
                "-fx-padding: 0;" +
                "-fx-cursor: hand;"
        );
    }

    // =========================================================
    // PLUS HOVER STYLE
    // =========================================================

    private void setPlusHoverStyle(
            Button button
    ) {

        button.setStyle(
                "-fx-background-color: " +
                        HOVER_GREEN +
                        ";" +
                "-fx-background-radius: 50%;" +
                "-fx-border-color: white;" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 50%;" +
                "-fx-padding: 0;" +
                "-fx-cursor: hand;"
        );
    }

    // =========================================================
    // CHANGE PROFILE IMAGE
    // =========================================================

    private void changeProfileImage(
            Button sourceButton
    ) {

        FileChooser fileChooser =
                new FileChooser();

        fileChooser.setTitle(
                "Choose Admin Profile Image"
        );

        // =====================================================
        // IMAGE FILTER
        // =====================================================

        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter(
                        "Image Files (*.png, *.jpg, *.jpeg)",
                        "*.png",
                        "*.jpg",
                        "*.jpeg"
                );

        fileChooser.getExtensionFilters().add(
                imageFilter
        );

        // =====================================================
        // WINDOW
        // =====================================================

        Window window =
                sourceButton
                        .getScene()
                        .getWindow();

        // =====================================================
        // OPEN FILE CHOOSER
        // =====================================================

        File selectedFile =
                fileChooser.showOpenDialog(
                        window
                );

        if (selectedFile == null) {
            return;
        }

        // =====================================================
        // LOAD IMAGE
        // =====================================================

        try {

            Image newImage =
                    new Image(
                            selectedFile
                                    .toURI()
                                    .toString()
                    );

            if (newImage.isError()) {

                System.out.println(
                        "ERROR: Unable to load selected image."
                );

                return;
            }

            // =================================================
            // UPDATE PROFILE PAGE IMAGE
            // =================================================

            updateLargeProfileImage(
                    newImage
            );

            // =================================================
            // UPDATE ALL NAVBAR IMAGES
            // =================================================

            AdminTopBar.updateProfileImage(
                    newImage
            );

            System.out.println(
                    "Profile image successfully changed."
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // UPDATE LARGE PROFILE IMAGE
    // =========================================================

    private void updateLargeProfileImage(
            Image image
    ) {

        if (profileAvatar == null ||
                image == null) {

            return;
        }

        ImageView imageView = null;

        for (javafx.scene.Node node :
                profileAvatar.getChildren()) {

            if (node instanceof ImageView) {

                imageView =
                        (ImageView) node;

                break;
            }
        }

        if (imageView != null) {

            imageView.setImage(
                    image
            );
        }
    }

    // =========================================================
    // STAT CARD
    // =========================================================

    private VBox statCard(
            String icon,
            String title,
            String value
    ) {

        VBox card =
                new VBox();

        card.setPadding(
                new Insets(15)
        );

        card.setPrefHeight(78);
        card.setMinHeight(75);

        setStatCardNormalStyle(
                card
        );

        card.setOnMouseEntered(event -> {

            card.setStyle(
                    "-fx-background-color: " +
                            VERY_LIGHT_GREEN +
                            ";" +
                    "-fx-border-color: " +
                            HOVER_BORDER +
                            ";" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 10;" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;"
            );

            ScaleTransition grow =
                    new ScaleTransition(
                            Duration.millis(140),
                            card
                    );

            grow.setToX(1.02);
            grow.setToY(1.02);

            grow.play();
        });

        card.setOnMouseExited(event -> {

            setStatCardNormalStyle(
                    card
            );

            ScaleTransition shrink =
                    new ScaleTransition(
                            Duration.millis(140),
                            card
                    );

            shrink.setToX(1.0);
            shrink.setToY(1.0);

            shrink.play();
        });

        HBox row =
                new HBox(12);

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        StackPane iconBox =
                new StackPane();

        iconBox.setPrefSize(
                42,
                42
        );

        iconBox.setMinSize(
                42,
                42
        );

        iconBox.setMaxSize(
                42,
                42
        );

        iconBox.setStyle(
                "-fx-background-color: " +
                        LIGHT_GREEN +
                        ";" +
                "-fx-background-radius: 10;"
        );

        Label iconLabel =
                new Label(icon);

        iconLabel.setFont(
                Font.font(
                        "Arial",
                        20
                )
        );

        iconLabel.setTextFill(
                Color.web(DARK_GREEN)
        );

        iconBox.getChildren().add(
                iconLabel
        );

        VBox text =
                new VBox(2);

        Label titleLabel =
                new Label(title);

        titleLabel.setTextFill(
                Color.web(GRAY_COLOR)
        );

        titleLabel.setFont(
                Font.font(
                        "Arial",
                        10
                )
        );

        Label valueLabel =
                new Label(value);

        valueLabel.setTextFill(
                Color.web(NUMBER_GREEN)
        );

        valueLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        17
                )
        );

        text.getChildren().addAll(
                titleLabel,
                valueLabel
        );

        row.getChildren().addAll(
                iconBox,
                text
        );

        card.getChildren().add(
                row
        );

        return card;
    }

    // =========================================================
    // STAT CARD STYLE
    // =========================================================

    private void setStatCardNormalStyle(
            VBox card
    ) {

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: " +
                        BORDER_COLOR +
                        ";" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"
        );
    }

    // =========================================================
    // PERSONAL INFORMATION
    // =========================================================

    private VBox createPersonalInformation() {

        VBox card =
                new VBox();

        setPersonalCardNormalStyle(
                card
        );

        // =====================================================
        // HEADER
        // =====================================================

        HBox header =
                new HBox();

        header.setPadding(
                new Insets(
                        20,
                        22,
                        18,
                        22
                )
        );

        VBox headerText =
                new VBox(5);

        Label title =
                new Label(
                        "Personal Information"
                );

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        19
                )
        );

        title.setTextFill(
                Color.web("#172033")
        );

        Label subtitle =
                new Label(
                        "Update your personal details and role information."
                );

        subtitle.setFont(
                Font.font(
                        "Arial",
                        13
                )
        );

        subtitle.setTextFill(
                Color.web(GRAY_COLOR)
        );

        headerText.getChildren().addAll(
                title,
                subtitle
        );

        // =====================================================
        // EDIT BUTTON
        // =====================================================

        editButton =
                new Button("Edit");

        editButton.setPrefHeight(36);
        editButton.setPrefWidth(75);

        editButton.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13
                )
        );

        setEditButtonStyle();

        editButton.setOnMouseEntered(event -> {

            editButton.setStyle(
                    "-fx-background-color: " +
                            HOVER_GREEN +
                            ";" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 7;" +
                    "-fx-cursor: hand;"
            );
        });

        editButton.setOnMouseExited(event -> {

            setEditButtonStyle();
        });

        editButton.setOnAction(event -> {

            enableEditing();
        });

        Region headerSpacer =
                new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS
        );

        header.getChildren().addAll(
                headerText,
                headerSpacer,
                editButton
        );

        Separator separator =
                new Separator();

        // =====================================================
        // FORM
        // =====================================================

        GridPane form =
                new GridPane();

        form.setPadding(
                new Insets(
                        20,
                        22,
                        20,
                        22
                )
        );

        form.setHgap(20);
        form.setVgap(16);

        ColumnConstraints col1 =
                new ColumnConstraints();

        col1.setPercentWidth(50);

        ColumnConstraints col2 =
                new ColumnConstraints();

        col2.setPercentWidth(50);

        form.getColumnConstraints().addAll(
                col1,
                col2
        );

        // =====================================================
        // FIELDS
        // =====================================================

        VBox fullName =
                formField(
                        "FULL NAME",
                        AdminTopBar.getAdminName()
                );

        VBox email =
                formField(
                        "EMAIL ADDRESS",
                        AdminTopBar.getAdminEmail()
                );

        VBox phone =
                formField(
                        "PHONE NUMBER",
                        "+91 XXXXX XXXXX"
                );

        VBox username =
                formField(
                        "USERNAME",
                        "admin"
                );

        VBox department =
                formField(
                        "DEPARTMENT",
                        "Operations & Management"
                );

        VBox role =
                formField(
                        "ROLE",
                        AdminTopBar.getAdminRole()
                );

        form.add(
                fullName,
                0,
                0
        );

        form.add(
                email,
                1,
                0
        );

        form.add(
                phone,
                0,
                1
        );

        form.add(
                username,
                1,
                1
        );

        form.add(
                department,
                0,
                2
        );

        form.add(
                role,
                1,
                2
        );

        // =====================================================
        // SECURITY
        // =====================================================

        Separator secondSeparator =
                new Separator();

        VBox security =
                new VBox(6);

        security.setPadding(
                new Insets(
                        18,
                        22,
                        18,
                        22
                )
        );

        Label securityTitle =
                new Label(
                        "Security"
                );

        securityTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        17
                )
        );

        securityTitle.setTextFill(
                Color.web(DARK_GREEN)
        );

        Label securityText =
                new Label(
                        "Your account is protected with administrator-level access."
                );

        securityText.setFont(
                Font.font(
                        "Arial",
                        13
                )
        );

        securityText.setTextFill(
                Color.web(GRAY_COLOR)
        );

        security.getChildren().addAll(
                securityTitle,
                securityText
        );

        card.getChildren().addAll(
                header,
                separator,
                form,
                secondSeparator,
                security
        );

        VBox.setVgrow(
                card,
                Priority.ALWAYS
        );

        return card;
    }

    // =========================================================
    // FORM FIELD
    // =========================================================

    private VBox formField(
            String labelText,
            String value
    ) {

        VBox box =
                new VBox(6);

        box.setPadding(
                new Insets(8)
        );

        box.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: transparent;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
        );

        Label label =
                new Label(labelText);

        label.setTextFill(
                Color.web(GRAY_COLOR)
        );

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        10
                )
        );

        TextField field =
                new TextField(value);

        field.setPrefHeight(40);

        field.setFont(
                Font.font(
                        "Arial",
                        13
                )
        );

        field.setEditable(false);

        setFieldNormalStyle(
                field
        );

        field.focusedProperty().addListener(
                (observable, oldValue, newValue) -> {

                    if (newValue) {

                        setFieldFocusedStyle(
                                field
                        );

                    } else {

                        setFieldNormalStyle(
                                field
                        );
                    }
                }
        );

        field.setOnMouseEntered(event -> {

            if (!field.isFocused()) {

                field.setStyle(
                        "-fx-background-color: #F8FCFA;" +
                        "-fx-border-color: " +
                                HOVER_BORDER +
                                ";" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 8 12;"
                );
            }
        });

        field.setOnMouseExited(event -> {

            if (!field.isFocused()) {

                setFieldNormalStyle(
                        field
                );
            }
        });

        box.getChildren().addAll(
                label,
                field
        );

        // =====================================================
        // SAVE FIELD REFERENCE
        // =====================================================

        switch (labelText) {

            case "FULL NAME":
                fullNameField = field;
                break;

            case "EMAIL ADDRESS":
                emailField = field;
                break;

            case "PHONE NUMBER":
                phoneField = field;
                break;

            case "USERNAME":
                usernameField = field;
                break;

            case "DEPARTMENT":
                departmentField = field;
                break;

            case "ROLE":
                roleField = field;
                break;

            default:
                break;
        }

        return box;
    }

    // =========================================================
    // FIELD NORMAL STYLE
    // =========================================================

    private void setFieldNormalStyle(
            TextField field
    ) {

        field.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-border-color: " +
                        BORDER_COLOR +
                        ";" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 8 12;"
        );
    }

    // =========================================================
    // FIELD FOCUSED STYLE
    // =========================================================

    private void setFieldFocusedStyle(
            TextField field
    ) {

        field.setStyle(
                "-fx-background-color: #F8FFFB;" +
                "-fx-border-color: " +
                        HOVER_BORDER +
                        ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 8 12;"
        );
    }

    // =========================================================
    // PERSONAL CARD STYLE
    // =========================================================

    private void setPersonalCardNormalStyle(
            VBox card
    ) {

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: " +
                        BORDER_COLOR +
                        ";" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;"
        );
    }

    // =========================================================
    // EDIT BUTTON STYLE
    // =========================================================

    private void setEditButtonStyle() {

        editButton.setStyle(
                "-fx-background-color: " +
                        CLICK_GREEN +
                        ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 7;" +
                "-fx-cursor: hand;"
        );
    }

    // =========================================================
    // ENABLE EDITING
    // =========================================================

    private void enableEditing() {

        fullNameField.setEditable(true);
        emailField.setEditable(true);
        phoneField.setEditable(true);
        usernameField.setEditable(true);
        departmentField.setEditable(true);
        roleField.setEditable(true);

        editButton.setText(
                "Save"
        );

        editButton.setStyle(
                "-fx-background-color: " +
                        SUCCESS_GREEN +
                        ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 7;" +
                "-fx-cursor: hand;"
        );

        editButton.setOnAction(
                event -> saveProfile()
        );

        fullNameField.requestFocus();

        System.out.println(
                "Profile editing enabled."
        );
    }

    // =========================================================
    // SAVE PROFILE
    // =========================================================

    private void saveProfile() {

        // =====================================================
        // UPDATE SHARED PROFILE DATA
        // =====================================================

        AdminTopBar.updateProfileInfo(
                fullNameField.getText(),
                roleField.getText(),
                emailField.getText()
        );

        // =====================================================
        // UPDATE PROFILE CARD
        // =====================================================

        if (profileNameLabel != null) {

            profileNameLabel.setText(
                    AdminTopBar.getAdminName()
            );
        }

        if (profileRoleLabel != null) {

            profileRoleLabel.setText(
                    AdminTopBar.getAdminRole()
            );
        }

        if (profileEmailLabel != null) {

            profileEmailLabel.setText(
                    AdminTopBar.getAdminEmail()
            );
        }

        // =====================================================
        // DISABLE EDITING
        // =====================================================

        fullNameField.setEditable(false);
        emailField.setEditable(false);
        phoneField.setEditable(false);
        usernameField.setEditable(false);
        departmentField.setEditable(false);
        roleField.setEditable(false);

        // =====================================================
        // CHANGE SAVE BACK TO EDIT
        // =====================================================

        editButton.setText(
                "Edit"
        );

        setEditButtonStyle();

        editButton.setOnAction(
                event -> enableEditing()
        );

        System.out.println(
                "Profile information saved successfully."
        );
    }
}