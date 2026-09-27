
package com.super_x.view.AdminView;

import com.super_x.NavigationService;
import com.super_x.dao.admindao.TripDAO;
import com.super_x.dao.admindao.UserDAO;
import com.super_x.dao.admindao.ReviewDAO;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class Reports {

    private NavigationService navigationService;

    public void setNavigationService(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    private void navigateTo(String pageName) {
        if (navigationService != null) {
            navigationService.navigate(pageName);
        }
    }

    // =========================================================
    // SCENE
    // =========================================================

    private Scene reportsScene;

    // References to the chart cards currently displayed on the page.
    // These are used to export the exact JavaFX graphs into the PDF.
    private VBox totalUsersChartBox;
    private VBox loadStatusChartBox;
    private VBox dailyReviewsChartBox;
    private final TripDAO tripDAO = new TripDAO();
    private final UserDAO userDAO = new UserDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();
    private TripDAO.TripStatusDistribution statusDistribution =
            TripDAO.TripStatusDistribution.empty();
    private List<UserOverviewPoint> userOverviewData = List.of();
    private List<DailyReviewPoint> dailyReviewData = List.of();
    private long loadStatusRequestId;
    private long totalUsersRequestId;
    private long dailyReviewsRequestId;

    // =========================================================
    // STAGE
    // =========================================================

  //  private Stage reportsStage;

    // =========================================================
    // COLORS
    // =========================================================

    private static final String DARK_GREEN = "#004B3A";
    private static final String HOVER_GREEN = "#075E49";
    private static final String CLICK_GREEN = "#075E49";

    private static final String MAIN_BG = "#E3F2EC";
    private static final String BG_COLOR = "#EEF8F4";

    private static final String BORDER = "#D5DBDE";
    private static final String BORDER_COLOR = "#D5DBDE";
    private static final String HOVER_BORDER = "#075E49";

    private static final String TEXT = "#171A1C";
    private static final String GRAY = "#697278";
    private static final String GRAY_COLOR = "#697278";

    private static final String GREEN = "#249447";
    private static final String BLUE = "#3478D4";
    private static final String ORANGE = "#F79A1B";
    private static final String RED = "#D93636";

    private static final String TEXT_COLOR = "#171A1C";
    private static final String NUMBER_GREEN = "#2D9950";

    // Logout colors
    private static final String LOGOUT_RED = "#C62828";
    private static final String LOGOUT_HOVER_RED = "#B71C1C";

    // =========================================================
    // SET STAGE
    // =========================================================

   /*  public void setStage(Stage stage) {

        this.reportsStage = stage;

        Rectangle2D screenBounds =
                Screen.getPrimary().getVisualBounds();

        double screenWidth =
                screenBounds.getWidth();

        double screenHeight =
                screenBounds.getHeight();

        double stageWidth =
                screenWidth * 0.90;

        double stageHeight =
                screenHeight * 0.90;

        stageWidth =
                Math.max(stageWidth, 1000);

        stageHeight =
                Math.max(stageHeight, 650);

        stageWidth =
                Math.min(stageWidth, screenWidth);

        stageHeight =
                Math.min(stageHeight, screenHeight);

        stage.setWidth(stageWidth);
        stage.setHeight(stageHeight);

        stage.setX(
                screenBounds.getMinX()
                        + (screenWidth - stageWidth) / 2
        );

        stage.setY(
                screenBounds.getMinY()
                        + (screenHeight - stageHeight) / 2
        );

        stage.setMinWidth(1000);
        stage.setMinHeight(650);

        stage.setResizable(true);
    }*/

    // =========================================================
    // GET REPORTS SCENE
    // =========================================================

    /**
     * Returns only this page's right-side content.
     * AdminDashboard places it inside its shared content area.
     */
    public VBox getContent() {

        VBox content = createMainContent();

        VBox.setVgrow(content, Priority.ALWAYS);

        return content;
    }

    public Scene getReportsScene() {

        BorderPane root =
                new BorderPane();

        root.setStyle(
                "-fx-background-color: " +
                        MAIN_BG + ";"
        );

        VBox sidebar =
                createSidebar();

        root.setLeft(sidebar);

        VBox rightSide =
                new VBox();

        rightSide.setStyle(
                "-fx-background-color: " +
                        MAIN_BG + ";"
        );

        HBox topBar =
                createTopBar();

        VBox content =
                createMainContent();

        VBox.setVgrow(
                content,
                Priority.ALWAYS
        );

        rightSide.getChildren().addAll(
                topBar,
                content
        );

        root.setCenter(rightSide);

        reportsScene =
                new Scene(
                        root,
                        1200,
                        750
                );

        reportsScene.setFill(
                Color.web(MAIN_BG)
        );

        return reportsScene;
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private VBox createSidebar() {

        VBox sidebar =
                new VBox();

        sidebar.setMinWidth(250);
        sidebar.setPrefWidth(250);
        sidebar.setMaxWidth(250);

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
                        DARK_GREEN + ";"
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
                        false,
                        false
                );
        dashboardButton.setOnAction(event -> navigateTo("dashboard"));

        Button driverButton =
                createMenuButton(
                        "▰",
                        "Drivers & Users",
                        false,
                        false
                );
        driverButton.setOnAction(event -> navigateTo("drivers"));

        Button loadsButton =
                createMenuButton(
                        "♧",
                        "Loads & Trips",
                        false,
                        false
                );
        loadsButton.setOnAction(event -> navigateTo("loads"));

        Button reportsButton =
                createMenuButton(
                        "▥",
                        "Reports",
                        true,
                        false
                );
        reportsButton.setOnAction(event -> navigateTo("reports"));Button supportButton =
                createMenuButton(
                        "♧",
                        "Support",
                        false,
                        false
                );
        supportButton.setOnAction(event -> navigateTo("support"));

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
                        false,
                        true
                );

        sidebar.getChildren().addAll(
                logoBox,
                menu,
                spacer,
                logout
        );

        return sidebar;
    }

    // =========================================================
    // SIDEBAR LOGO
    // =========================================================

    private ImageView createSidebarLogo() {

        ImageView logoView =
                new ImageView();

        String logoPath =
                "/assets/Logo-removebg-preview.png";

        java.io.InputStream logoStream =
                getClass().getResourceAsStream(
                        logoPath
                );

        if (logoStream != null) {

            Image logoImage =
                    new Image(logoStream);

            logoView.setImage(
                    logoImage
            );

            logoView.setFitWidth(70);
            logoView.setFitHeight(70);

            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);

        } else {

            System.out.println(
                    "ERROR: Logo not found: "
                            + logoPath
            );
        }

        return logoView;
    }

    // =========================================================
    // SIDEBAR MENU BUTTON
    // =========================================================

    private Button createMenuButton(
            String icon,
            String text,
            boolean active,
            boolean logoutButton
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

        // =====================================================
        // NORMAL STYLE
        // =====================================================

        String normalStyle;

        if (logoutButton) {

            // Logout is always RED
            normalStyle =
                    "-fx-background-color: "
                            + LOGOUT_RED + ";"
                            + "-fx-background-radius: 9;";

        } else if (active) {

            normalStyle =
                    "-fx-background-color: "
                            + CLICK_GREEN + ";"
                            + "-fx-background-radius: 9;";

        } else {

            normalStyle =
                    "-fx-background-color: "
                            + DARK_GREEN + ";"
                            + "-fx-background-radius: 9;";
        }

        button.setStyle(
                normalStyle
        );

        // =====================================================
        // HOVER
        // =====================================================

        button.setOnMouseEntered(event -> {

            String hoverStyle;

            if (logoutButton) {

                // Logout remains RED on hover
                hoverStyle =
                        "-fx-background-color: "
                                + LOGOUT_HOVER_RED + ";"
                                + "-fx-background-radius: 9;";

            } else {

                hoverStyle =
                        "-fx-background-color: "
                                + HOVER_GREEN + ";"
                                + "-fx-background-radius: 9;";
            }

            button.setStyle(
                    hoverStyle
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

        // =====================================================
        // EXIT
        // =====================================================

        button.setOnMouseExited(event -> {

            button.setStyle(
                    normalStyle
            );

            ScaleTransition shrink =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            shrink.setToX(1.0);
            shrink.setToY(1.0);

            shrink.play();
        });

        // =====================================================
        // LOGOUT ACTION
        // =====================================================

        if (logoutButton) {

            button.setOnAction(event -> {

                Alert alert =
                        new Alert(
                                Alert.AlertType.CONFIRMATION
                        );

                alert.setTitle("Logout");
                alert.setHeaderText("Logout from EcoLoad?");
                alert.setContentText(
                        "Are you sure you want to logout?"
                );

                alert.showAndWait();
            });
        }

        return button;
    }

    // =========================================================
    // TOP BAR
    // =========================================================

    private HBox createTopBar() {
        return AdminTopBar.create(navigationService).build();
    }

    // =========================================================
    // ADMIN PROFILE
    // =========================================================

    private Button createAdminProfileImage() {

        Button profileButton =
                new Button();

        profileButton.setPrefSize(45, 45);

        profileButton.setMinSize(45, 45);
        profileButton.setMaxSize(45, 45);

        profileButton.setStyle(
                "-fx-background-color: transparent;"
                        + "-fx-border-color: transparent;"
                        + "-fx-padding: 0;"
                        + "-fx-background-radius: 50;"
                        + "-fx-border-radius: 50;"
        );

        String imagePath =
                "/assets/Admin_Profile_Logo.png";

        java.io.InputStream profileStream =
                getClass().getResourceAsStream(
                        imagePath
                );

        if (profileStream != null) {

            Image profileImage =
                    new Image(profileStream);

            ImageView profileImageView =
                    new ImageView(profileImage);

            profileImageView.setFitWidth(45);
            profileImageView.setFitHeight(45);

            profileImageView.setPreserveRatio(false);
            profileImageView.setSmooth(true);

            Circle clip =
                    new Circle(
                            22.5,
                            22.5,
                            22.5
                    );

            profileImageView.setClip(clip);

            profileButton.setGraphic(
                    profileImageView
            );

        } else {

            Label profileIcon =
                    new Label("♙");

            profileIcon.setFont(
                    Font.font(24)
            );

            profileButton.setGraphic(
                    profileIcon
            );
        }

        profileButton.setOnMouseEntered(event -> {

            profileButton.setStyle(
                    "-fx-background-color: #E4F0EB;"
                            + "-fx-border-color: #075E49;"
                            + "-fx-border-width: 2;"
                            + "-fx-border-radius: 50;"
                            + "-fx-background-radius: 50;"
                            + "-fx-padding: 0;"
            );

            ScaleTransition grow =
                    new ScaleTransition(
                            Duration.millis(120),
                            profileButton
                    );

            grow.setToX(1.08);
            grow.setToY(1.08);

            grow.play();
        });

        profileButton.setOnMouseExited(event -> {

            profileButton.setStyle(
                    "-fx-background-color: transparent;"
                            + "-fx-border-color: transparent;"
                            + "-fx-padding: 0;"
                            + "-fx-background-radius: 50;"
                            + "-fx-border-radius: 50;"
            );

            ScaleTransition shrink =
                    new ScaleTransition(
                            Duration.millis(120),
                            profileButton
                    );

            shrink.setToX(1.0);
            shrink.setToY(1.0);

            shrink.play();
        });

        profileButton.setOnAction(event -> {

            showMessage(
                    "Admin Profile",
                    "Admin Profile button clicked."
            );
        });

        return profileButton;
    }

    // =========================================================
    // MAIN CONTENT
    // =========================================================

    private VBox createMainContent() {

        VBox wrapper =
                new VBox();

        wrapper.setStyle(
                "-fx-background-color: " +
                        MAIN_BG + ";"
        );

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setPannable(true);

        scrollPane.setStyle(
                "-fx-background-color: " +
                        MAIN_BG + ";"
                        + "-fx-border-color: transparent;"
        );

        VBox content =
                new VBox(18);

        content.setPadding(
                new Insets(
                        22,
                        28,
                        22,
                        28
                )
        );

        content.setFillWidth(true);

        content.setStyle(
                "-fx-background-color: " +
                        MAIN_BG + ";"
        );

        // =====================================================
        // TITLE
        // =====================================================

        HBox titleRow =
                new HBox();

        titleRow.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox titleBox =
                new VBox(4);

        Label title =
                new Label(
                        "Reports & Analytics"
                );

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        27
                )
        );

        title.setTextFill(
                Color.web(TEXT_COLOR)
        );

        Label subtitle =
                new Label(
                        "Overview of platform performance and key metrics"
                );

        subtitle.setFont(
                Font.font(14)
        );

        subtitle.setTextFill(
                Color.web(GRAY)
        );

        titleBox.getChildren().addAll(
                title,
                subtitle
        );

        Region titleSpacer =
                new Region();

        HBox.setHgrow(
                titleSpacer,
                Priority.ALWAYS
        );

        Button date =
                new Button(
                        "▣   20 May 2025 - 27 May 2025   ⌄"
                );

        date.setPrefHeight(40);

        date.setStyle(
                "-fx-background-color: white;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-radius: 6;"
                        + "-fx-background-radius: 6;"
        );

        date.setOnAction(event -> selectDateRange(date));

        Button download =
                new Button(
                        "⇩  Download Report"
                );

        download.setPrefHeight(40);

        download.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13
                )
        );

        download.setTextFill(
                Color.WHITE
        );

        download.setStyle(
                "-fx-background-color: " + GREEN + ";"
                        + "-fx-background-radius: 6;"
        );

        addButtonAnimation(
                download,
                GREEN,
                "#1D7638"
        );

        download.setOnAction(event -> downloadReport(download));

        HBox actions =
                new HBox(
                        10,
                        date,
                        download
                );

        actions.setAlignment(
                Pos.CENTER
        );

        titleRow.getChildren().addAll(
                titleBox,
                titleSpacer,
                actions
        );

        // =====================================================
        // STAT CARDS
        // =====================================================

        FlowPane cards =
                new FlowPane();

        cards.setHgap(12);
        cards.setVgap(12);

        cards.setPrefWrapLength(1100);

        cards.setMaxWidth(
                Double.MAX_VALUE
        );

        VBox totalUsers =
                statCard(
                        "Total Users",
                        "1,204",
                        "↑ 12.5% from last month",
                        GREEN
                );

        VBox totalDrivers =
                statCard(
                        "Total Drivers",
                        "86",
                        "↑ 8.3% from last month",
                        BLUE
                );

        VBox totalTransporters =
                statCard(
                        "Total Users",
                        "42",
                        "↑ 10.7% from last month",
                        "#7B38D8"
                );

        VBox totalTrucks =
                statCard(
                        "Total Trucks",
                        "64",
                        "↑ 6.4% from last month",
                        ORANGE
                );

        VBox activeUsers =
                statCard(
                        "Active Users",
                        "312",
                        "↑ 9.1% from last month",
                        "#21845D"
                );

        VBox totalRevenue =
                statCard(
                        "Total Revenue",
                        "₹8.45L",
                        "↑ 18.6% from last month",
                        RED
                );

        makeCardClickable(
                totalUsers,
                "Total Users",
                "1,204",
                "Complete platform user statistics."
        );

        makeCardClickable(
                totalDrivers,
                "Total Drivers",
                "86",
                "Registered and active driver statistics."
        );

        makeCardClickable(
                totalTransporters,
                "Total Users",
                "42",
                "User registration and activity statistics."
        );

        makeCardClickable(
                totalTrucks,
                "Total Trucks",
                "64",
                "Registered truck and fleet statistics."
        );

        makeCardClickable(
                activeUsers,
                "Active Users",
                "312",
                "Currently active platform users."
        );

        makeCardClickable(
                totalRevenue,
                "Total Revenue",
                "₹8.45L",
                "Platform revenue and financial performance."
        );

        cards.getChildren().addAll(
                totalUsers,
                totalDrivers,
                totalTransporters,
                totalTrucks,
                activeUsers,
                totalRevenue
        );

        // =====================================================
        // CHART ROW
        // =====================================================

        HBox chartRow =
                new HBox(15);

        totalUsersChartBox =
                createTotalUsersChart();

        loadStatusChartBox =
                createLoadStatusChart();

        VBox revenue =
                totalUsersChartBox;

        VBox loadStatus =
                loadStatusChartBox;

        HBox.setHgrow(
                revenue,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                loadStatus,
                Priority.ALWAYS
        );

        revenue.setMaxWidth(
                Double.MAX_VALUE
        );

        loadStatus.setMaxWidth(
                Double.MAX_VALUE
        );

        chartRow.getChildren().addAll(
                revenue,
                loadStatus
        );

        // =====================================================
        // DAILY REVIEWS
        // =====================================================

        dailyReviewsChartBox =
                createDailyReviewsChart();

        VBox growth =
                dailyReviewsChartBox;

        growth.setMaxWidth(
                Double.MAX_VALUE
        );

        content.getChildren().addAll(
                titleRow,
                chartRow,
                growth
        );

        scrollPane.setContent(
                content
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        wrapper.getChildren().add(
                scrollPane
        );

        return wrapper;
    }

    // =========================================================
    // STAT CARD
    // =========================================================

    private VBox statCard(
            String title,
            String value,
            String growth,
            String color
    ) {

        VBox card =
                new VBox(7);

        card.setPrefHeight(125);

        card.setPrefWidth(160);

        card.setMinWidth(150);

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        card.setPadding(
                new Insets(14)
        );

        card.setCursor(
                javafx.scene.Cursor.HAND
        );

        card.setStyle(
                "-fx-background-color: white;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-radius: 8;"
                        + "-fx-background-radius: 8;"
        );

        /*if (reportsStage != null) {

            card.prefWidthProperty().bind(
                    reportsStage.widthProperty()
                            .multiply(0.13)
            );
        }*/

        HBox heading =
                new HBox(7);

        heading.setAlignment(
                Pos.CENTER_LEFT
        );

        Circle icon =
                new Circle(
                        18,
                        Color.web(
                                color,
                                0.12
                        )
                );

        Label iconText =
                new Label("●");

        iconText.setTextFill(
                Color.web(color)
        );

        StackPane iconBox =
                new StackPane(
                        icon,
                        iconText
                );

        Label titleLabel =
                new Label(title);

        titleLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12
                )
        );

        titleLabel.setTextFill(
                Color.web("#4E585D")
        );

        heading.getChildren().addAll(
                iconBox,
                titleLabel
        );

        Label valueLabel =
                new Label(value);

        valueLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        24
                )
        );

        valueLabel.setTextFill(
                Color.web(TEXT)
        );

        Label growthLabel =
                new Label(growth);

        growthLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        10
                )
        );

        growthLabel.setTextFill(
                Color.web(GREEN)
        );

        card.getChildren().addAll(
                heading,
                valueLabel,
                growthLabel
        );

        return card;
    }

    // =========================================================
    // CARD CLICK
    // =========================================================

    private void makeCardClickable(
            VBox card,
            String title,
            String value,
            String description
    ) {

        card.setOnMouseEntered(event -> {

            card.setStyle(
                    "-fx-background-color: #FFFFFF;"
                            + "-fx-border-color: #249447;"
                            + "-fx-border-width: 1.5;"
                            + "-fx-border-radius: 8;"
                            + "-fx-background-radius: 8;"
                            + "-fx-effect: dropshadow("
                            + "gaussian,"
                            + "rgba(0,0,0,0.16),"
                            + "12,"
                            + "0.15,"
                            + "0,"
                            + "4);"
            );

            ScaleTransition grow =
                    new ScaleTransition(
                            Duration.millis(150),
                            card
                    );

            grow.setToX(1.035);
            grow.setToY(1.035);

            grow.play();
        });

        card.setOnMouseExited(event -> {

            card.setStyle(
                    "-fx-background-color: white;"
                            + "-fx-border-color: " + BORDER + ";"
                            + "-fx-border-radius: 8;"
                            + "-fx-background-radius: 8;"
            );

            ScaleTransition shrink =
                    new ScaleTransition(
                            Duration.millis(150),
                            card
                    );

            shrink.setToX(1.0);
            shrink.setToY(1.0);

            shrink.play();
        });

        card.setOnMousePressed(event -> {

            ScaleTransition press =
                    new ScaleTransition(
                            Duration.millis(80),
                            card
                    );

            press.setToX(0.97);
            press.setToY(0.97);

            press.setAutoReverse(true);
            press.setCycleCount(2);

            press.play();
        });

        card.setOnMouseClicked(event -> {

            showCardDetails(
                    title,
                    value,
                    description
            );
        });
    }

    // =========================================================
    // CARD DETAILS
    // =========================================================

    private void showCardDetails(
            String title,
            String value,
            String description
    ) {

        BorderPane detailRoot =
                new BorderPane();

        detailRoot.setStyle(
                "-fx-background-color: " +
                        MAIN_BG + ";"
        );

        HBox top =
                new HBox();

        top.setPadding(
                new Insets(
                        18,
                        25,
                        18,
                        25
                )
        );

        top.setAlignment(
                Pos.CENTER_LEFT
        );

        top.setStyle(
                "-fx-background-color: white;"
                        + "-fx-border-color: #D7DCDE;"
                        + "-fx-border-width: 0 0 1 0;"
        );

        Button back =
                new Button(
                        "←  Back to Reports"
                );

        back.setPrefHeight(40);

        back.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13
                )
        );

        back.setTextFill(
                Color.WHITE
        );

        back.setStyle(
                "-fx-background-color: "
                        + DARK_GREEN + ";"
                        + "-fx-background-radius: 7;"
        );

        addButtonAnimation(
                back,
                DARK_GREEN,
                HOVER_GREEN
        );

       /*  back.setOnAction(event -> {

            showReportsAgain();
        });*/

        Label pageTitle =
                new Label(title);

        pageTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        24
                )
        );

        pageTitle.setTextFill(
                Color.web(TEXT)
        );

        HBox.setMargin(
                pageTitle,
                new Insets(
                        0,
                        0,
                        0,
                        25
                )
        );

        top.getChildren().addAll(
                back,
                pageTitle
        );

        detailRoot.setTop(top);

        VBox detailContent =
                new VBox(20);

        detailContent.setPadding(
                new Insets(30)
        );

        Label heading =
                new Label(
                        title + " Details"
                );

        heading.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        28
                )
        );

        heading.setTextFill(
                Color.web(TEXT)
        );

        Label desc =
                new Label(description);

        desc.setFont(
                Font.font(15)
        );

        desc.setTextFill(
                Color.web(GRAY)
        );

        VBox mainCard =
                new VBox(10);

        mainCard.setPadding(
                new Insets(25)
        );

        mainCard.setPrefWidth(420);

        mainCard.setStyle(
                "-fx-background-color: white;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-radius: 12;"
                        + "-fx-background-radius: 12;"
                        + "-fx-effect: dropshadow("
                        + "gaussian,"
                        + "rgba(0,0,0,0.10),"
                        + "12,"
                        + "0.1,"
                        + "0,"
                        + "3);"
        );

        Label valueTitle =
                new Label(
                        "Current Value"
                );

        valueTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        valueTitle.setTextFill(
                Color.web(GRAY)
        );

        Label bigValue =
                new Label(value);

        bigValue.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        42
                )
        );

        bigValue.setTextFill(
                Color.web(DARK_GREEN)
        );

        Label status =
                new Label(
                        "↑ Positive growth compared with last month"
                );

        status.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13
                )
        );

        status.setTextFill(
                Color.web(GREEN)
        );

        mainCard.getChildren().addAll(
                valueTitle,
                bigValue,
                status
        );

        detailContent.getChildren().addAll(
                heading,
                desc,
                mainCard
        );

        ScrollPane scroll =
                new ScrollPane(
                        detailContent
                );

        scroll.setFitToWidth(true);

        scroll.setStyle(
                "-fx-background-color: " +
                        MAIN_BG + ";"
                        + "-fx-border-color: transparent;"
        );

        detailRoot.setCenter(scroll);

        reportsScene =
                new Scene(detailRoot);

        reportsScene.setFill(
                Color.web(MAIN_BG)
        );

       /*  if (reportsStage != null) {

            reportsStage.setScene(
                    reportsScene
            );

            reportsStage.setTitle(
                    "EcoLoad - " + title
            );
        }*/
    }

    // =========================================================
    // BACK TO REPORTS
    // =========================================================

   /*  private void showReportsAgain() {

        if (reportsStage != null) {

            reportsStage.setScene(
                    getReportsScene()
            );

            reportsStage.setTitle(
                    "EcoLoad - Reports & Analytics"
            );
        }
    }
*/
    // =========================================================
    // BUTTON ANIMATION
    // =========================================================

    private void addButtonAnimation(
            Button button,
            String normalColor,
            String hoverColor
    ) {

        button.setOnMouseEntered(event -> {

            button.setStyle(
                    "-fx-background-color: "
                            + hoverColor + ";"
                            + "-fx-background-radius: 7;"
            );

            ScaleTransition grow =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            grow.setToX(1.04);
            grow.setToY(1.04);

            grow.play();
        });

        button.setOnMouseExited(event -> {

            button.setStyle(
                    "-fx-background-color: "
                            + normalColor + ";"
                            + "-fx-background-radius: 7;"
            );

            ScaleTransition shrink =
                    new ScaleTransition(
                            Duration.millis(120),
                            button
                    );

            shrink.setToX(1.0);
            shrink.setToY(1.0);

            shrink.play();
        });
    }

    // =========================================================
    // TOTAL USERS CHART
    // =========================================================

    private VBox createTotalUsersChart() {

        VBox box =
                new VBox(8);

        box.setPrefHeight(380);
        box.setMinHeight(350);

        box.setPadding(
                new Insets(15)
        );

        box.setStyle(
                "-fx-background-color: white;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-radius: 8;"
                        + "-fx-background-radius: 8;"
        );

        makeChartBlockInteractive(box);

        HBox heading =
                new HBox();

        heading.setAlignment(
                Pos.CENTER_LEFT
        );

        Label title =
                new Label(
                        "Total Users Overview"
                );

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        ComboBox<String> week = createPeriodSelector();

        heading.getChildren().addAll(
                title,
                spacer,
                week
        );

        CategoryAxis xAxis =
                new CategoryAxis();

        NumberAxis yAxis =
                new NumberAxis();

        yAxis.setLabel(
                "Users"
        );

        yAxis.setAutoRanging(true);

        LineChart<String, Number> chart =
                new LineChart<>(
                        xAxis,
                        yAxis
                );

        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setCreateSymbols(true);

        chart.setHorizontalGridLinesVisible(true);
        chart.setVerticalGridLinesVisible(false);

        chart.setMinHeight(200);

        XYChart.Series<String, Number> series =
                new XYChart.Series<>();

        chart.getData().add(series);

        week.setOnAction(event -> updateTotalUsersData(series, week.getValue()));
        updateTotalUsersData(series, week.getValue());

        chart.setStyle(
                "-fx-background-color: transparent;"
                        + "-fx-padding: 0;"
        );

        VBox.setVgrow(
                chart,
                Priority.ALWAYS
        );

        box.getChildren().addAll(
                heading,
                chart
        );

        return box;
    }

    // =========================================================
    // LOAD STATUS PIE CHART
    // =========================================================

    private VBox createLoadStatusChart() {

        VBox box =
                new VBox(8);

        box.setPrefHeight(480);
        box.setMinHeight(450);

        box.setPadding(
                new Insets(15)
        );

        box.setStyle(
                "-fx-background-color: white;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-radius: 8;"
                        + "-fx-background-radius: 8;"
        );

        makeChartBlockInteractive(box);

        HBox heading =
                new HBox();

        heading.setAlignment(
                Pos.CENTER_LEFT
        );

        Label title =
                new Label(
                        "Load Status Distribution"
                );

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        ComboBox<String> week = createPeriodSelector();

        heading.getChildren().addAll(
                title,
                spacer,
                week
        );

        PieChart pie =
                new PieChart();

        pie.setLabelsVisible(false);
        // The built-in legend shows the matching slice color for each entry.
        pie.setLegendVisible(true);
        pie.setStartAngle(90);

        pie.setMinHeight(340);

        week.setOnAction(event -> updateLoadStatusData(pie, week.getValue()));
        updateLoadStatusData(pie, week.getValue());

        pie.setStyle(
                "-fx-background-color: transparent;"
        );

        VBox.setVgrow(
                pie,
                Priority.ALWAYS
        );

        box.getChildren().addAll(
                heading,
                pie
        );

        return box;
    }

    // =========================================================
    // DAILY REVIEWS CHART
    // =========================================================

    private VBox createDailyReviewsChart() {

        VBox box =
                new VBox(8);

        box.setPrefHeight(360);
        box.setMinHeight(330);

        box.setPadding(
                new Insets(15)
        );

        box.setStyle(
                "-fx-background-color: white;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-radius: 8;"
                        + "-fx-background-radius: 8;"
        );

        makeChartBlockInteractive(box);

        HBox heading =
                new HBox();

        heading.setAlignment(
                Pos.CENTER_LEFT
        );

        Label title =
                new Label(
                        "Daily Reviews Given"
                );

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        ComboBox<String> year = createPeriodSelector();

        heading.getChildren().addAll(
                title,
                spacer,
                year
        );

        CategoryAxis xAxis =
                new CategoryAxis();

        NumberAxis yAxis =
                new NumberAxis();

        yAxis.setLabel(
                "Reviews"
        );

        BarChart<String, Number> chart =
                new BarChart<>(
                        xAxis,
                        yAxis
                );

        chart.setLegendVisible(false);
        chart.setAnimated(false);

        chart.setCategoryGap(15);
        chart.setBarGap(3);

        chart.setMinHeight(200);

        XYChart.Series<String, Number> series =
                new XYChart.Series<>();

        chart.getData().add(series);

        year.setOnAction(event -> updateDailyReviewsData(series, year.getValue()));
        updateDailyReviewsData(series, year.getValue());

        chart.setStyle(
                "-fx-background-color: transparent;"
        );

        VBox.setVgrow(
                chart,
                Priority.ALWAYS
        );

        box.getChildren().addAll(
                heading,
                chart
        );

        return box;
    }

    // =========================================================
    // CHART HOVER
    // =========================================================

    private void makeChartBlockInteractive(
            VBox block
    ) {

        block.setCursor(
                javafx.scene.Cursor.HAND
        );

        block.setOnMouseEntered(event -> {

            block.setStyle(
                    "-fx-background-color: #FFFFFF;"
                            + "-fx-border-color: #249447;"
                            + "-fx-border-width: 1.5;"
                            + "-fx-border-radius: 8;"
                            + "-fx-background-radius: 8;"
                            + "-fx-effect: dropshadow("
                            + "gaussian,"
                            + "rgba(0,0,0,0.16),"
                            + "12,"
                            + "0.15,"
                            + "0,"
                            + "4);"
            );

            ScaleTransition grow =
                    new ScaleTransition(
                            Duration.millis(150),
                            block
                    );

            grow.setToX(1.015);
            grow.setToY(1.015);

            grow.play();
        });

        block.setOnMouseExited(event -> {

            block.setStyle(
                    "-fx-background-color: white;"
                            + "-fx-border-color: " + BORDER + ";"
                            + "-fx-border-radius: 8;"
                            + "-fx-background-radius: 8;"
            );

            ScaleTransition shrink =
                    new ScaleTransition(
                            Duration.millis(150),
                            block
                    );

            shrink.setToX(1.0);
            shrink.setToY(1.0);

            shrink.play();
        });
    }

    // =========================================================
    // SIMPLE BUTTON HOVER
    // =========================================================

    private void addSimpleButtonHover(
            Button button
    ) {

        button.setOnMouseEntered(event -> {

            button.setStyle(
                    "-fx-background-color: #EAF3EF;"
                            + "-fx-border-color: #249447;"
                            + "-fx-border-radius: 5;"
                            + "-fx-background-radius: 5;"
            );

            ScaleTransition grow =
                    new ScaleTransition(
                            Duration.millis(100),
                            button
                    );

            grow.setToX(1.03);
            grow.setToY(1.03);

            grow.play();
        });

        button.setOnMouseExited(event -> {

            button.setStyle(
                    "-fx-background-color: white;"
                            + "-fx-border-color: " + BORDER + ";"
                            + "-fx-border-radius: 5;"
                            + "-fx-background-radius: 5;"
            );

            ScaleTransition shrink =
                    new ScaleTransition(
                            Duration.millis(100),
                            button
                    );

            shrink.setToX(1.0);
            shrink.setToY(1.0);

            shrink.play();
        });
    }

    // =========================================================
    // MESSAGE
    // =========================================================

    private void selectDateRange(Button dateButton) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Select Report Date Range");
        dialog.setHeaderText("Choose the dates to include in the analytics report");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        DatePicker start = new DatePicker(LocalDate.now().minusDays(30));
        DatePicker end = new DatePicker(LocalDate.now());
        HBox fields = new HBox(12, new Label("From:"), start, new Label("To:"), end);
        fields.setAlignment(Pos.CENTER_LEFT);
        fields.setPadding(new Insets(18));
        dialog.getDialogPane().setContent(fields);
        dialog.showAndWait();

        if (start.getValue() != null && end.getValue() != null) {
            dateButton.setText(start.getValue() + " - " + end.getValue());
        }
    }

    private void downloadReport(Button sourceButton) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Analytics Report");
        chooser.setInitialFileName("analytics-report.pdf");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF report", "*.pdf")
        );

        File destination = chooser.showSaveDialog(
                (Stage) sourceButton.getScene().getWindow()
        );

        if (destination == null) {
            return;
        }

        // Make sure the file has the PDF extension.
        if (!destination.getName().toLowerCase().endsWith(".pdf")) {
            destination = new File(destination.getAbsolutePath() + ".pdf");
        }

        try {
            createAnalyticsPdf(destination);

            showMessage(
                    "Download complete",
                    "PDF report saved to:\n" + destination.getAbsolutePath()
            );

        } catch (IOException exception) {
            showMessage(
                    "Download failed",
                    "Could not create the PDF report:\n" + exception.getMessage()
            );
        }
    }

    /**
     * Creates the real PDF report using Apache PDFBox.
     */
    private void createAnalyticsPdf(File destination) throws IOException {

        try (PDDocument document = new PDDocument()) {

            // =====================================================
            // PAGE 1 - SUMMARY / DATA
            // =====================================================

            PDPage summaryPage = new PDPage(PDRectangle.A4);
            document.addPage(summaryPage);

            try (PDPageContentStream content =
                         new PDPageContentStream(document, summaryPage)) {

                final float left = 50;
                final float pageWidth = PDRectangle.A4.getWidth();
                final float pageHeight = PDRectangle.A4.getHeight();

                float y = pageHeight - 50;

                writePdfText(
                        content,
                        "EcoLoad",
                        left,
                        y,
                        22,
                        Standard14Fonts.FontName.HELVETICA_BOLD
                );

                y -= 22;

                writePdfText(
                        content,
                        "PRECISION LOGISTICS",
                        left,
                        y,
                        9,
                        Standard14Fonts.FontName.HELVETICA
                );

                y -= 35;

                writePdfText(
                        content,
                        "REPORTS & ANALYTICS",
                        left,
                        y,
                        18,
                        Standard14Fonts.FontName.HELVETICA_BOLD
                );

                y -= 18;

                writePdfText(
                        content,
                        "Overview of platform performance and key metrics",
                        left,
                        y,
                        10,
                        Standard14Fonts.FontName.HELVETICA
                );

                y -= 25;
                drawPdfLine(content, left, y, pageWidth - left, y);
                y -= 25;

                writePdfText(
                        content,
                        "Report Date",
                        left,
                        y,
                        11,
                        Standard14Fonts.FontName.HELVETICA_BOLD
                );

                y -= 17;

                String today =
                        LocalDate.now().format(
                                DateTimeFormatter.ofPattern("dd MMMM yyyy")
                        );

                writePdfText(
                        content,
                        today,
                        left,
                        y,
                        10,
                        Standard14Fonts.FontName.HELVETICA
                );

                y -= 30;

                writePdfText(
                        content,
                        "SUMMARY",
                        left,
                        y,
                        14,
                        Standard14Fonts.FontName.HELVETICA_BOLD
                );

                y -= 22;

                String[][] summary = {
                        {"Total Users", "1,204"},
                        {"Total Drivers", "86"},
                        {"Total Users / Transporters", "42"},
                        {"Total Trucks", "64"},
                        {"Active Users", "312"},
                        {"Total Revenue", "Rs. 8.45L"}
                };

                for (String[] row : summary) {

                    writePdfText(
                            content,
                            row[0],
                            left,
                            y,
                            10,
                            Standard14Fonts.FontName.HELVETICA
                    );

                    writePdfText(
                            content,
                            row[1],
                            left + 230,
                            y,
                            10,
                            Standard14Fonts.FontName.HELVETICA_BOLD
                    );

                    y -= 18;
                }

                y -= 18;

                writePdfText(
                        content,
                        "TOTAL USERS OVERVIEW",
                        left,
                        y,
                        14,
                        Standard14Fonts.FontName.HELVETICA_BOLD
                );

                y -= 22;

                for (UserOverviewPoint point : userOverviewData) {

                    writePdfText(
                            content,
                            point.label(),
                            left,
                            y,
                            10,
                            Standard14Fonts.FontName.HELVETICA
                    );

                    writePdfText(
                            content,
                            String.valueOf(point.count()),
                            left + 230,
                            y,
                            10,
                            Standard14Fonts.FontName.HELVETICA_BOLD
                    );

                    y -= 18;
                }

                y -= 18;

                writePdfText(
                        content,
                        "LOAD STATUS DISTRIBUTION",
                        left,
                        y,
                        14,
                        Standard14Fonts.FontName.HELVETICA_BOLD
                );

                y -= 22;

                String[][] loadStatus = {
                        {"Active Load", String.valueOf(statusDistribution.getActive())},
                        {"In Transit", String.valueOf(statusDistribution.getInTransit())},
                        {"Delivered", String.valueOf(statusDistribution.getDelivered())},
                        {"Unanswered", String.valueOf(statusDistribution.getUnanswered())}
                };

                for (String[] row : loadStatus) {

                    writePdfText(
                            content,
                            row[0],
                            left,
                            y,
                            10,
                            Standard14Fonts.FontName.HELVETICA
                    );

                    writePdfText(
                            content,
                            row[1],
                            left + 230,
                            y,
                            10,
                            Standard14Fonts.FontName.HELVETICA_BOLD
                    );

                    y -= 18;
                }

                y -= 18;

                writePdfText(
                        content,
                        "DAILY REVIEWS GIVEN",
                        left,
                        y,
                        14,
                        Standard14Fonts.FontName.HELVETICA_BOLD
                );

                y -= 22;

                for (DailyReviewPoint point : dailyReviewData) {

                    writePdfText(
                            content,
                            point.label(),
                            left,
                            y,
                            9,
                            Standard14Fonts.FontName.HELVETICA
                    );

                    writePdfText(
                            content,
                            String.valueOf(point.count()),
                            left + 230,
                            y,
                            9,
                            Standard14Fonts.FontName.HELVETICA_BOLD
                    );

                    y -= 15;

                    if (y < 55) {
                        break;
                    }
                }

                writePdfText(
                        content,
                        "Generated by EcoLoad Admin Dashboard",
                        left,
                        30,
                        8,
                        Standard14Fonts.FontName.HELVETICA
                );
            }

            // =====================================================
            // PAGE 2 - TOTAL USERS + LOAD STATUS GRAPHS
            // =====================================================

            addChartPage(
                    document,
                    "Analytics Graphs",
                    totalUsersChartBox,
                    loadStatusChartBox
            );

            // =====================================================
            // PAGE 3 - DAILY REVIEWS GRAPH
            // =====================================================

            addChartPage(
                    document,
                    "Daily Reviews Given",
                    dailyReviewsChartBox
            );

            document.save(destination);
        }
    }

    /**
     * Adds one or more JavaFX chart cards to a PDF page as PNG images.
     *
     * This is important: the PDF receives the actual rendered JavaFX
     * charts, including lines/bars, axes, labels and the pie-chart legend.
     * Therefore the graphs in the PDF look like the graphs on the screen.
     */
    private void addChartPage(
            PDDocument document,
            String pageTitle,
            VBox... chartBoxes
    ) throws IOException {

        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        final float pageWidth = PDRectangle.A4.getWidth();
        final float pageHeight = PDRectangle.A4.getHeight();
        final float margin = 35;

        try (PDPageContentStream content =
                     new PDPageContentStream(document, page)) {

            writePdfText(
                    content,
                    "EcoLoad - " + pageTitle,
                    margin,
                    pageHeight - 35,
                    16,
                    Standard14Fonts.FontName.HELVETICA_BOLD
            );

            float currentY = pageHeight - 65;

            for (VBox chartBox : chartBoxes) {

                if (chartBox == null) {
                    continue;
                }

                // Make sure the node has a real size before snapshotting.
                chartBox.applyCss();
                chartBox.layout();

                if (chartBox.getWidth() <= 0 || chartBox.getHeight() <= 0) {
                    continue;
                }

                WritableImage image = snapshotNode(chartBox);

                if (image == null) {
                    continue;
                }

                ByteArrayOutputStream imageBytes =
                        new ByteArrayOutputStream();

                ImageIO.write(
                        SwingFXUtils.fromFXImage(image, null),
                        "png",
                        imageBytes
                );

                PDImageXObject pdfImage =
                        PDImageXObject.createFromByteArray(
                                document,
                                imageBytes.toByteArray(),
                                "analytics-chart"
                        );

                float availableWidth =
                        pageWidth - (margin * 2);

                float maxHeight =
                        chartBoxes.length == 1
                                ? pageHeight - 105
                                : (pageHeight - 105) / chartBoxes.length - 15;

                float imageWidth = availableWidth;
                // float imageHeight =
                //         imageWidth * image.getHeight() / image.getWidth();
                float imageHeight =
        imageWidth * (float) image.getHeight() / (float) image.getWidth();

                if (imageHeight > maxHeight) {
                    imageHeight = maxHeight;
                //     imageWidth =
                //             imageHeight * image.getWidth() / image.getHeight();
                imageWidth =
        imageHeight * (float) image.getWidth() / (float) image.getHeight();
                }

                float x =
                        (pageWidth - imageWidth) / 2;

                float y =
                        currentY - imageHeight;

                // White background behind the chart.
                content.saveGraphicsState();
                content.setNonStrokingColor(1f, 1f, 1f);
                content.addRect(
                        x - 5,
                        y - 5,
                        imageWidth + 10,
                        imageHeight + 10
                );
                content.fill();
                content.restoreGraphicsState();

                content.drawImage(
                        pdfImage,
                        x,
                        y,
                        imageWidth,
                        imageHeight
                );

                currentY =
                        y - 20;
            }

            writePdfText(
                    content,
                    "Generated by EcoLoad Admin Dashboard",
                    margin,
                    22,
                    8,
                    Standard14Fonts.FontName.HELVETICA
            );
        }
    }

    /**
     * Takes a screenshot of the actual JavaFX chart card.
     */
    private WritableImage snapshotNode(VBox node) {

        int width =
                Math.max(
                        1,
                        (int) Math.ceil(node.getBoundsInParent().getWidth())
                );

        int height =
                Math.max(
                        1,
                        (int) Math.ceil(node.getBoundsInParent().getHeight())
                );

        WritableImage image =
                new WritableImage(
                        width,
                        height
                );

        SnapshotParameters parameters =
                new SnapshotParameters();

        parameters.setFill(
                Color.WHITE
        );

        node.snapshot(
                parameters,
                image
        );

        return image;
    }

    /**
     * Writes one line of text to the PDF.
     */
    private void writePdfText(
            PDPageContentStream content,
            String text,
            float x,
            float y,
            float fontSize,
            Standard14Fonts.FontName fontName
    ) throws IOException {

        PDType1Font font = new PDType1Font(fontName);

        content.beginText();
        content.setFont(font, fontSize);
        content.newLineAtOffset(x, y);
        content.showText(sanitizePdfText(text));
        content.endText();
    }

    /**
     * Draws a horizontal line in the PDF.
     */
    private void drawPdfLine(
            PDPageContentStream content,
            float startX,
            float startY,
            float endX,
            float endY
    ) throws IOException {

        content.moveTo(startX, startY);
        content.lineTo(endX, endY);
        content.stroke();
    }

    /**
     * PDFBox's standard Helvetica font does not support the ₹ symbol.
     * Therefore the PDF uses "Rs." instead.
     */
    private String sanitizePdfText(String text) {
        return text
                .replace("₹", "Rs.")
                .replace("↑", "+")
                .replace("↓", "-")
                .replace("–", "-")
                .replace("—", "-");
    }

    private ComboBox<String> createPeriodSelector() {
        ComboBox<String> selector = new ComboBox<>();
        selector.getItems().addAll("Last 7 Days", "Last 30 Days", "Last 6 Months", "Last 12 Months");
        selector.setValue("Last 30 Days");
        selector.setPrefWidth(145);
        selector.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:" + BORDER + ";" +
                "-fx-border-radius:5;" +
                "-fx-background-radius:5;"
        );
        return selector;
    }

    private void updateTotalUsersData(XYChart.Series<String, Number> series, String period) {
        long requestId = ++totalUsersRequestId;
        series.getData().clear();

        CompletableFuture
                .supplyAsync(() -> userDAO.getRegistrationCounts(
                        userPeriodStart(period), ZoneId.systemDefault()))
                .thenAccept(registrations -> Platform.runLater(() -> {
                    if (requestId != totalUsersRequestId) {
                        return;
                    }

                    userOverviewData = userOverviewPoints(period, registrations);
                    series.getData().setAll(userOverviewData.stream()
                            .map(point -> new XYChart.Data<String, Number>(point.label(), point.count()))
                            .toList());
                }));
    }

    private Instant userPeriodStart(String period) {
        LocalDate today = LocalDate.now();
        LocalDate start = switch (period) {
            case "Last 30 Days" -> today.minusDays(29);
            case "Last 6 Months" -> today.withDayOfMonth(1).minusMonths(5);
            case "Last 12 Months" -> today.withDayOfMonth(1).minusMonths(11);
            default -> today.minusDays(6);
        };
        return start.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    private List<UserOverviewPoint> userOverviewPoints(
            String period, Map<LocalDate, Integer> registrations) {
        LocalDate today = LocalDate.now();
        List<UserOverviewPoint> points = new ArrayList<>();

        if ("Last 6 Months".equals(period) || "Last 12 Months".equals(period)) {
            int monthCount = "Last 6 Months".equals(period) ? 6 : 12;
            YearMonth currentMonth = YearMonth.from(today);
            for (int offset = monthCount - 1; offset >= 0; offset--) {
                YearMonth month = currentMonth.minusMonths(offset);
                int count = registrations.entrySet().stream()
                        .filter(entry -> YearMonth.from(entry.getKey()).equals(month))
                        .mapToInt(Map.Entry::getValue)
                        .sum();
                points.add(new UserOverviewPoint(month.format(DateTimeFormatter.ofPattern("MMM uuuu")), count));
            }
            return points;
        }

        int dayCount = "Last 30 Days".equals(period) ? 30 : 7;
        for (int offset = dayCount - 1; offset >= 0; offset--) {
            LocalDate day = today.minusDays(offset);
            points.add(new UserOverviewPoint(
                    day.format(DateTimeFormatter.ofPattern("dd MMM")),
                    registrations.getOrDefault(day, 0)));
        }
        return points;
    }

    private void updateDailyReviewsData(XYChart.Series<String, Number> series, String period) {
        long requestId = ++dailyReviewsRequestId;
        series.getData().clear();

        CompletableFuture
                .supplyAsync(() -> reviewDAO.getDailyReviewCounts(
                        reviewPeriodStart(period), ZoneId.systemDefault()))
                .thenAccept(reviewCounts -> Platform.runLater(() -> {
                    if (requestId != dailyReviewsRequestId) {
                        return;
                    }

                    dailyReviewData = dailyReviewPoints(period, reviewCounts);
                    series.getData().setAll(dailyReviewData.stream()
                            .map(point -> new XYChart.Data<String, Number>(point.label(), point.count()))
                            .toList());
                }));
    }

    private Instant reviewPeriodStart(String period) {
        return reviewPeriodStartDate(period)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
    }

    private LocalDate reviewPeriodStartDate(String period) {
        LocalDate today = LocalDate.now();
        return switch (period) {
            case "Last 30 Days" -> today.minusDays(29);
            case "Last 6 Months" -> today.minusMonths(6).plusDays(1);
            case "Last 12 Months" -> today.minusMonths(12).plusDays(1);
            default -> today.minusDays(6);
        };
    }

    private List<DailyReviewPoint> dailyReviewPoints(
            String period, Map<LocalDate, Integer> reviewCounts) {
        LocalDate start = reviewPeriodStartDate(period);
        LocalDate today = LocalDate.now();
        List<DailyReviewPoint> points = new ArrayList<>();

        for (LocalDate day = start; !day.isAfter(today); day = day.plusDays(1)) {
            points.add(new DailyReviewPoint(
                    day.format(DateTimeFormatter.ofPattern("dd MMM")),
                    reviewCounts.getOrDefault(day, 0)));
        }
        return points;
    }

    private void updateLoadStatusData(PieChart pie, String period) {
        long requestId = ++loadStatusRequestId;
        pie.setData(statusPieData(TripDAO.TripStatusDistribution.empty()));

        CompletableFuture
                .supplyAsync(() -> tripDAO.getStatusDistribution(statusPeriodStart(period)))
                .thenAccept(distribution -> Platform.runLater(() -> {
                    if (requestId != loadStatusRequestId) {
                        return;
                    }
                    statusDistribution = distribution;
                    pie.setData(statusPieData(distribution));
                }));
    }

    private Instant statusPeriodStart(String period) {
        Instant now = Instant.now();
        return switch (period) {
            case "Last 30 Days" -> now.minus(30, ChronoUnit.DAYS);
            case "Last 6 Months" -> LocalDate.now().minusMonths(6)
                    .atStartOfDay(ZoneId.systemDefault()).toInstant();
            case "Last 12 Months" -> LocalDate.now().minusMonths(12)
                    .atStartOfDay(ZoneId.systemDefault()).toInstant();
            default -> now.minus(7, ChronoUnit.DAYS);
        };
    }

    private javafx.collections.ObservableList<PieChart.Data> statusPieData(
            TripDAO.TripStatusDistribution distribution) {
        int total = distribution.getTotal();
        return FXCollections.observableArrayList(
                statusPieSlice("Active Load", distribution.getActive(), total),
                statusPieSlice("In Transit", distribution.getInTransit(), total),
                statusPieSlice("Delivered", distribution.getDelivered(), total),
                statusPieSlice("Unanswered", distribution.getUnanswered(), total));
    }

    private PieChart.Data statusPieSlice(String label, int count, int total) {
        double percentage = total == 0 ? 0 : count * 100.0 / total;
        return new PieChart.Data(String.format("%.1f%%  %s (%d)", percentage, label, count), count);
    }

    private record UserOverviewPoint(String label, int count) {
    }

    private record DailyReviewPoint(String label, int count) {
    }

    private void showMessage(
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
