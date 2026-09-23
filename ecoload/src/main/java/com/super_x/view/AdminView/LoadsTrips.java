
package com.super_x.view.AdminView;

import com.super_x.NavigationService;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.super_x.dao.admindao.TripDAO;
import com.super_x.model.adminmodel.Trip;

import java.util.List;

public class LoadsTrips {

    private NavigationService navigationService;
    private final TripDAO tripDAO = new TripDAO();

    private List<Trip> firebaseTrips = new java.util.ArrayList<>();

    public void setNavigationService(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    private void navigateTo(String pageName) {
        if (navigationService != null) {
            navigationService.navigate(pageName);
        }
    }
    private void loadTripsFromFirebase() {

    System.out.println(
            "Loading trips from Firebase..."
    );

    try {

        firebaseTrips =
                tripDAO.getAllTrips();

        System.out.println(
                "Trips loaded into JavaFX: "
                + firebaseTrips.size()
        );

    } catch (Exception e) {

        System.err.println(
                "Error loading trips into JavaFX."
        );

        e.printStackTrace();

        firebaseTrips =
                new java.util.ArrayList<>();
    }
}

    // =========================================================
    // SCENE
    // =========================================================

    private Scene loadsTripsScene;

    private BorderPane root;

    // =========================================================
    // COLORS
    // =========================================================
     private final String DARK_GREEN = "#004B3A";

    // Light green when cursor is on button
    private final String HOVER_GREEN = "#075E49";

    // Dark green when button is clicked
    private final String CLICK_GREEN = "#075E49";

    private final String NUMBER_GREEN = "#2D9950";

    private final String BG_COLOR = "#EEF8F4";

    private final String BORDER_COLOR = "#D5DBDE";

    private final String HOVER_BORDER = "#075E49";

    private final String GRAY_COLOR = "#697278";
     private final String TEXT_COLOR = "#171A1C";
/* 
    private final String DARK_GREEN = "#004B3A";
    private final String HOVER_GREEN = "#075E49";
    private final String CLICK_GREEN = "#003528";
    private final String BG_COLOR = "#E3F2EC";
    private final String BORDER_COLOR = "#D5DBDE";
    private final String GRAY_COLOR = "#697278";
    private final String TEXT_COLOR = "#171A1C";
    private final String ROW_HOVER_COLOR = "#F1F7F5";
    */

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public LoadsTrips() {

        createLoadsTripsScene();

        /*
         * Main.java does not need to be changed.
         * When this Scene is attached to a Stage,
         * responsive screen settings are automatically applied.
         */
        setupResponsiveScreen();
    }

    // =========================================================
    // CREATE SCENE
    // =========================================================

    private void createLoadsTripsScene() {

        root = new BorderPane();

        root.setStyle(
                "-fx-background-color: " + BG_COLOR + ";"
        );

        // =====================================================
        // SIDEBAR
        // =====================================================

        VBox sidebar = createSidebar();

        root.setLeft(sidebar);

        // =====================================================
        // RIGHT SIDE
        // =====================================================

        VBox rightSide = new VBox();

        HBox topBar = createTopBar();

        VBox content = createLoadsTripsContent();

        VBox.setVgrow(
                content,
                Priority.ALWAYS
        );

        rightSide.getChildren().addAll(
                topBar,
                content
        );

        root.setCenter(rightSide);

        // =====================================================
        // RESPONSIVE RIGHT SIDE
        // =====================================================

        VBox.setVgrow(
                topBar,
                Priority.NEVER
        );

        VBox.setVgrow(
                content,
                Priority.ALWAYS
        );

        // =====================================================
        // SCENE
        // =====================================================

        loadsTripsScene = new Scene(root);

        // =====================================================
        // SCENE SIZE
        // =====================================================

        loadsTripsScene.setFill(
                Color.web(BG_COLOR)
        );
    }

    // =========================================================
    // RESPONSIVE SCREEN SETTINGS
    // =========================================================

    private void setupResponsiveScreen() {

        loadsTripsScene.windowProperty().addListener(
                (observable, oldWindow, newWindow) -> {

                    if (newWindow instanceof Stage) {

                        Stage stage =
                                (Stage) newWindow;

                        applyResponsiveScreen(stage);
                    }
                }
        );

        /*
         * In case the Scene is already attached to a Stage.
         */
        Platform.runLater(() -> {

            if (loadsTripsScene.getWindow()
                    instanceof Stage) {

                Stage stage =
                        (Stage) loadsTripsScene.getWindow();

                applyResponsiveScreen(stage);
            }
        });
    }

    // =========================================================
    // APPLY RESPONSIVE SCREEN
    // =========================================================

    private void applyResponsiveScreen(Stage stage) {

        /*
         * Get available desktop screen area.
         *
         * This does not force the Stage to a fixed size.
         * It only makes sure the application can use
         * the available desktop area correctly.
         */

        Rectangle2D screenBounds =
                Screen.getPrimary()
                        .getVisualBounds();

        /*
         * Minimum window size
         */

        stage.setMinWidth(1000);
        stage.setMinHeight(650);

        /*
         * If the Stage is not maximized,
         * keep it inside the available desktop area.
         */

        if (!stage.isMaximized()) {

            double width =
                    Math.min(
                            Math.max(
                                    stage.getWidth(),
                                    1000
                            ),
                            screenBounds.getWidth()
                    );

            double height =
                    Math.min(
                            Math.max(
                                    stage.getHeight(),
                                    650
                            ),
                            screenBounds.getHeight()
                    );

            stage.setWidth(width);
            stage.setHeight(height);

            double x =
                    Math.max(
                            screenBounds.getMinX(),
                            Math.min(
                                    stage.getX(),
                                    screenBounds.getMaxX()
                                            - width
                            )
                    );

            double y =
                    Math.max(
                            screenBounds.getMinY(),
                            Math.min(
                                    stage.getY(),
                                    screenBounds.getMaxY()
                                            - height
                            )
                    );

            stage.setX(x);
            stage.setY(y);
        }

        /*
         * Responsive sidebar.
         */

        if (root.getLeft() instanceof VBox) {

            VBox sidebar =
                    (VBox) root.getLeft();

            sidebar.prefWidthProperty().bind(
                    root.widthProperty()
                            .multiply(0.205)
            );

            sidebar.minWidthProperty().set(220);
            sidebar.maxWidthProperty().set(300);
        }
    }

    // =========================================================
    // GET SCENE
    // =========================================================

    /**
     * Returns only the right-side content for the shared AdminDashboard.
     * No sidebar is created here.
     */
    public VBox getContent() {

        VBox content = createLoadsTripsContent();

        VBox.setVgrow(content, Priority.ALWAYS);

        return content;
    }

    public Scene getLoadsTripsScene() {

        return loadsTripsScene;
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private VBox createSidebar() {

        VBox sidebar = new VBox();

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

        HBox logoBox = new HBox(12);

        logoBox.setAlignment(
                Pos.CENTER_LEFT
        );

        ImageView logoImageView =
                createSidebarLogo();

        VBox logoText = new VBox(1);

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

        VBox menu = new VBox(5);

        menu.setPadding(
                new Insets(
                        45,
                        0,
                        0,
                        0
                )
        );

        menu.setFillWidth(true);

        // =====================================================
        // DASHBOARD
        // =====================================================

        Button dashboardButton =
                createMenuButton(
                        "▦",
                        "Dashboard",
                        false
                );
        dashboardButton.setOnAction(event -> navigateTo("dashboard"));

        // =====================================================
        // DRIVERS & TRANSPORTERS
        // =====================================================

        Button driverButton =
                createMenuButton(
                        "▰",
                        "Drivers & Users",
                        false
                );
        driverButton.setOnAction(event -> navigateTo("drivers"));

        // =====================================================
        // LOADS & TRIPS
        // =====================================================

        Button loadsButton =
                createMenuButton(
                        "♧",
                        "Loads & Trips",
                        true
                );
        loadsButton.setOnAction(event -> navigateTo("loads"));

        // =====================================================
        // REPORTS
        // =====================================================

        Button reportsButton =
                createMenuButton(
                        "▥",
                        "Reports",
                        false
                );
        reportsButton.setOnAction(event -> navigateTo("reports"));

        // =====================================================
        // SOS ALERTS
        // =====================================================// =====================================================
        // SUPPORT
        // =====================================================

        Button supportButton =
                createMenuButton(
                        "♧",
                        "Support",
                        false
                );
        supportButton.setOnAction(event -> navigateTo("support"));

        // =====================================================
        // SETTINGS
        // =====================================================


        // =====================================================
        // ADD MENU
        // =====================================================

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

        Region spacer = new Region();

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

        logout.setStyle(
                "-fx-background-color: #C62828;" +
                "-fx-background-radius: 9;"
        );

        // =====================================================
        // SIDEBAR
        // =====================================================

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

        // =====================================================
        // ICON
        // =====================================================

        Label iconLabel =
                new Label(icon);

        iconLabel.setPrefWidth(28);

        iconLabel.setFont(
                Font.font(20)
        );

        iconLabel.setTextFill(
                Color.WHITE
        );

        // =====================================================
        // TEXT
        // =====================================================

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

        // =====================================================
        // CONTENT
        // =====================================================

        HBox box =
                new HBox(
                        12,
                        iconLabel,
                        textLabel
                );

        box.setAlignment(
                Pos.CENTER_LEFT
        );

        button.setGraphic(
                box
        );

        // =====================================================
        // INITIAL STYLE
        // =====================================================

        if (active) {

            button.setStyle(
                    "-fx-background-color: " +
                    CLICK_GREEN + ";" +
                    "-fx-background-radius: 9;"
            );

        } else {

            button.setStyle(
                    "-fx-background-color: " +
                    DARK_GREEN + ";" +
                    "-fx-background-radius: 9;"
            );
        }

        // =====================================================
        // HOVER
        // =====================================================

        button.setOnMouseEntered(event -> {

            button.setStyle(
                    "-fx-background-color: " +
                    HOVER_GREEN + ";" +
                    "-fx-background-radius: 9;"
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

            if (active) {

                button.setStyle(
                        "-fx-background-color: " +
                        CLICK_GREEN + ";" +
                        "-fx-background-radius: 9;"
                );

            } else {

                button.setStyle(
                        "-fx-background-color: " +
                        DARK_GREEN + ";" +
                        "-fx-background-radius: 9;"
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

        // =====================================================
        // CLICK
        // =====================================================

        button.setOnAction(event -> {

            button.setStyle(
                    "-fx-background-color: " +
                    CLICK_GREEN + ";" +
                    "-fx-background-radius: 9;"
            );

            System.out.println(
                    text + " clicked"
            );
        });

        return button;
    }

    // =========================================================
    // TOP BAR
    // =========================================================

    private HBox createTopBar() {
        return AdminTopBar.create(navigationService).build();
    }

    // =========================================================
    // ADMIN PROFILE IMAGE
    // =========================================================

    private Button createAdminProfileImage() {

        Button profileButton =
                new Button();

        profileButton.setPrefSize(
                45,
                45
        );

        profileButton.setMinSize(
                45,
                45
        );

        profileButton.setMaxSize(
                45,
                45
        );

        profileButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;" +
                "-fx-padding: 0;" +
                "-fx-background-radius: 50;" +
                "-fx-border-radius: 50;"
        );

        String imagePath =
                "/assets/Admin_Profile_Logo.png";

        java.io.InputStream profileStream =
                getClass().getResourceAsStream(
                        imagePath
                );

        if (profileStream != null) {

            Image profileImage =
                    new Image(
                            profileStream
                    );

            ImageView profileImageView =
                    new ImageView(
                            profileImage
                    );

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

            profileImageView.setClip(
                    clip
            );

            profileButton.setGraphic(
                    profileImageView
            );

        } else {

            System.out.println(
                    "ERROR: Admin profile image not found: "
                    + imagePath
            );

            Label profileIcon =
                    new Label("♙");

            profileIcon.setFont(
                    Font.font(24)
            );

            profileButton.setGraphic(
                    profileIcon
            );
        }

        // =====================================================
        // HOVER
        // =====================================================

        profileButton.setOnMouseEntered(event -> {

            profileButton.setStyle(
                    "-fx-background-color: #E4F0EB;" +
                    "-fx-border-color: #075E49;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 50;" +
                    "-fx-background-radius: 50;" +
                    "-fx-padding: 0;"
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

        // =====================================================
        // EXIT
        // =====================================================

        profileButton.setOnMouseExited(event -> {

            profileButton.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-border-color: transparent;" +
                    "-fx-padding: 0;" +
                    "-fx-background-radius: 50;" +
                    "-fx-border-radius: 50;"
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

        // =====================================================
        // CLICK
        // =====================================================

        profileButton.setOnAction(event -> {

            System.out.println(
                    "Admin Profile button clicked"
            );
        });

        return profileButton;
    }

    // =========================================================
    // LOADS & TRIPS CONTENT
    // =========================================================

    private VBox createLoadsTripsContent() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(
                        30,
                        30,
                        25,
                        30
                )
        );

        content.setFillWidth(true);

        // =====================================================
        // TITLE
        // =====================================================

        VBox titleBox =
                new VBox(5);

        Label title =
                new Label(
                        "Loads & Trips"
                );

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        31
                )
        );

        title.setTextFill(
                Color.web(TEXT_COLOR)
        );

        Label subtitle =
                new Label(
                        "Monitor and manage all active and closed trips."
                );

        subtitle.setFont(
                Font.font(15)
        );

        subtitle.setTextFill(
                Color.web(
                        GRAY_COLOR
                )
        );

        titleBox.getChildren().addAll(
                title,
                subtitle
        );

        // =====================================================
        // TABLE
        // =====================================================

        VBox table =
                createTripsTable();

        VBox.setVgrow(
                table,
                Priority.ALWAYS
        );

        content.getChildren().addAll(
                titleBox,
                table
        );

        return content;
    }

    // =========================================================
    // TRIPS TABLE
    // =========================================================

    private VBox createTripsTable() {

        VBox tableBox =
                new VBox();

        tableBox.setMaxWidth(
                Double.MAX_VALUE
        );

        tableBox.setMaxHeight(
                Double.MAX_VALUE
        );

        tableBox.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: " +
                BORDER_COLOR + ";" +
                "-fx-border-radius: 12;"
        );

        VBox.setVgrow(
                tableBox,
                Priority.ALWAYS
        );

        // =====================================================
        // TABLE TITLE
        // =====================================================

        HBox tableTitle =
                new HBox();

        tableTitle.setPrefHeight(65);

        tableTitle.setMinHeight(60);

        tableTitle.setPadding(
                new Insets(
                        0,
                        25,
                        0,
                        25
                )
        );

        tableTitle.setAlignment(
                Pos.CENTER_LEFT
        );

        Label trips =
                new Label(
                        "Trips List"
                );

        trips.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        20
                )
        );

        trips.setTextFill(
                Color.web(
                        "#1F2937"
                )
        );

        Region titleSpacer = new Region();
        HBox.setHgrow(titleSpacer, Priority.ALWAYS);

        Label filterLabel = new Label("Filter by status:");
        filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        filterLabel.setTextFill(Color.web("#4B5563"));

        ComboBox<String> statusFilter = new ComboBox<>();
        // statusFilter.getItems().addAll(
        //         "All Statuses",
        //         "In Transit",
        //         "Delivered",
        //         "Closed",
        //         "Completed"
        // );
        statusFilter.getItems().addAll(
        "All Statuses",
        "In Transit",
        "Delivered",
        "Closed",
        "Completed",
        "Cancelled",
        "Pending"
);
        statusFilter.setValue("All Statuses");
        statusFilter.setPrefWidth(150);
        statusFilter.setStyle(
                "-fx-background-color:#F8FBF9;" +
                "-fx-border-color:" + BORDER_COLOR + ";" +
                "-fx-border-radius:6;" +
                "-fx-background-radius:6;"
        );

        tableTitle.getChildren().addAll(
                trips,
                titleSpacer,
                filterLabel,
                statusFilter
        );

        HBox.setMargin(filterLabel, new Insets(0, 8, 0, 0));

        // =====================================================
        // GRID
        // =====================================================

        GridPane grid =
                new GridPane();
        loadTripsFromFirebase();

        grid.setMinWidth(850);

        grid.setPrefWidth(1000);

        grid.setMaxWidth(
                Double.MAX_VALUE
        );

        grid.setHgap(0);

        grid.setVgap(0);

        // =====================================================
        // COLUMN WIDTHS
        // TOTAL = 100%
        // =====================================================

        grid.getColumnConstraints().addAll(
                column(9.7),
                column(10.7),
                column(21.4),
                column(19.4),
                column(14.6),
                column(14.5),
                column(9.7)
        );

        // =====================================================
        // HEADER
        // =====================================================

        addHeader(grid, "TRIP ID", 0);
        addHeader(grid, "LOAD ID", 1);
        addHeader(grid, "ROUTE", 2);
        addHeader(grid, "DRIVER / TRANSPORTER", 3);
        addHeader(grid, "VEHICLE", 4);
        addHeader(grid, "STARTED ON", 5);
        addHeader(grid, "STATUS", 6);

        // =====================================================
        // DATA ROW 1
        // =====================================================
        // =====================================================
// FIREBASE TRIP DATA
// =====================================================

int row = 1;

for (Trip trip : firebaseTrips) {

    String tripId =
            safeString(trip.getTripId());

    String loadId =
            safeString(trip.getLoadId());

    String pickup =
            safeString(trip.getPickupLocation());

    String destination =
            safeString(trip.getDestination());

    String route =
            pickup + "  →  " + destination;

    String driver =
            safeString(trip.getDriverName());

    String initials =
            getInitials(driver);

    String vehicle =
            "N/A";

    String started =
            formatDateTime(trip.getStartTime());

    String status =
            formatTripStatus(
                    trip.getStatus()
            );

    addTripRow(
            grid,
            row,
            tripId,
            loadId,
            route,
            initials,
            driver,
            vehicle,
            started,
            status
    );

    row++;
}

        // addTripRow(
        //         grid,
        //         1,
        //         "TRP-1001",
        //         "LD-2025-5123",
        //         "Pune  →  Nashik",
        //         "RK",
        //         "Ramesh Kadam",
        //         "Tata 407",
        //         "24 Oct, 08:30 AM",
        //         "In Transit"
        // );

        // // =====================================================
        // // DATA ROW 2
        // // =====================================================

        // addTripRow(
        //         grid,
        //         2,
        //         "TRP-1002",
        //         "LD-2025-5124",
        //         "Mumbai  →  Surat",
        //         "AS",
        //         "Amit Singh",
        //         "Ashok Leyland",
        //         "23 Oct, 10:15 AM",
        //         "Delivered"
        // );

        // // =====================================================
        // // DATA ROW 3
        // // =====================================================

        // addTripRow(
        //         grid,
        //         3,
        //         "TRP-1003",
        //         "LD-2025-5125",
        //         "Delhi  →  Jaipur",
        //         "SK",
        //         "Suresh Kumar",
        //         "Mahindra Blazo",
        //         "22 Oct, 06:00 AM",
        //         "Closed"
        // );

        // // =====================================================
        // // DATA ROW 4
        // // =====================================================

        // addTripRow(
        //         grid,
        //         4,
        //         "TRP-1004",
        //         "LD-2025-5126",
        //         "Bangalore  →  Chennai",
        //         "VP",
        //         "Vikram Patil",
        //         "Eicher Pro",
        //         "25 Oct, 09:00 AM",
        //         "Completed"
        // );

        statusFilter.setOnAction(event ->
                filterTripRows(grid, statusFilter.getValue())
        );

        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane(
                        grid
                );

        scrollPane.setFitToWidth(true);

        scrollPane.setFitToHeight(false);

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setPannable(true);

        scrollPane.setVvalue(0);

        scrollPane.setHvalue(0);

        // =====================================================
        // MOUSE WHEEL
        // =====================================================

        scrollPane.addEventFilter(
                ScrollEvent.SCROLL,
                event -> {

                    if (event.isShiftDown()) {
                        return;
                    }

                    double delta =
                            event.getDeltaY();

                    if (Math.abs(delta) > 0) {

                        double current =
                                scrollPane.getVvalue();

                        double movement =
                                delta / 500.0;

                        double newValue =
                                current - movement;

                        newValue =
                                Math.max(
                                        0,
                                        Math.min(
                                                1,
                                                newValue
                                        )
                                );

                        scrollPane.setVvalue(
                                newValue
                        );

                        event.consume();
                    }
                }
        );

        // =====================================================
        // SHIFT + MOUSE WHEEL
        // =====================================================

        scrollPane.addEventFilter(
                ScrollEvent.SCROLL,
                event -> {

                    if (event.isShiftDown()) {

                        double delta =
                                event.getDeltaY();

                        double current =
                                scrollPane.getHvalue();

                        double movement =
                                delta / 500.0;

                        double newValue =
                                current - movement;

                        newValue =
                                Math.max(
                                        0,
                                        Math.min(
                                                1,
                                                newValue
                                        )
                                );

                        scrollPane.setHvalue(
                                newValue
                        );

                        event.consume();
                    }
                }
        );

        // =====================================================
        // CURSOR
        // =====================================================

        scrollPane.setOnMouseEntered(event -> {

            scrollPane.setCursor(
                    Cursor.DEFAULT
            );
        });

        // =====================================================
        // STYLE
        // =====================================================

        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: white;" +
                "-fx-border-color: transparent;" +
                "-fx-padding: 0;"
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        // =====================================================
        // ADD TO TABLE
        // =====================================================

        tableBox.getChildren().addAll(
                tableTitle,
                scrollPane
        );

        return tableBox;
    }

    // =========================================================
// SAFE STRING
// =========================================================

// private String safeString(String value) {

//     if (value == null ||
//             value.isBlank()) {

//         return "N/A";
//     }

//     return value;
// }
// =========================================================
// SAFE STRING
// =========================================================

private String safeString(String value) {

    if (value == null ||
            value.isBlank()) {

        return "N/A";
    }

    return value;
}

// =========================================================
// FORMAT FIREBASE DATE/TIME
// =========================================================

private String formatDateTime(Object value) {

    if (value == null) {

        return "N/A";
    }

    return value.toString();
}
// =========================================================
// GET DRIVER INITIALS
// =========================================================

private String getInitials(String name) {

    if (name == null || name.trim().isEmpty()) {
        return "NA";
    }

    String[] parts =
            name.trim().split("\\s+");

    if (parts.length == 1) {

        String first =
                parts[0];

        return first.substring(
                0,
                Math.min(2, first.length())
        ).toUpperCase();
    }

    String firstInitial =
            parts[0].substring(0, 1);

    String lastInitial =
            parts[parts.length - 1]
                    .substring(0, 1);

    return (
            firstInitial +
            lastInitial
    ).toUpperCase();
}
        // =========================================================
    // FORMAT FIREBASE TRIP STATUS
    // =========================================================

    private String formatTripStatus(String firebaseStatus) {

        if (firebaseStatus == null ||
                firebaseStatus.isBlank()) {

            return "Unknown";
        }

        switch (firebaseStatus.toUpperCase()) {

            case "ACTIVE":
                return "In Transit";

            case "DELIVERED":
                return "Delivered";

            case "CLOSED":
                return "Closed";

            case "COMPLETED":
                return "Completed";

            case "CANCELLED":
                return "Cancelled";

            case "PENDING":
                return "Pending";

            default:
                return firebaseStatus;
        }
    }

    

    // =========================================================
    // FILTER TRIP ROWS
    // =========================================================


    /** Shows only rows that match the status selected in the Trips List filter. */
    private void filterTripRows(
        GridPane grid,
        String selectedStatus
) {

    int row = 1;

    for (Trip trip : firebaseTrips) {

        String firebaseStatus =
                formatTripStatus(
                        trip.getStatus()
                );

        boolean show =
                "All Statuses".equals(
                        selectedStatus
                )
                ||
                selectedStatus.equals(
                        firebaseStatus
                );

        for (javafx.scene.Node node :
                grid.getChildren()) {

            Integer nodeRow =
                    GridPane.getRowIndex(node);

            if (nodeRow != null &&
                    nodeRow == row) {

                node.setVisible(show);
                node.setManaged(show);
            }
        }

        row++;
    }
}
//     private void filterTripRows(GridPane grid, String selectedStatus) {
//         String[] rowStatuses = { "In Transit", "Delivered", "Closed", "Completed" };

//         for (javafx.scene.Node node : grid.getChildren()) {
//             Integer rowIndex = GridPane.getRowIndex(node);
//             if (rowIndex == null || rowIndex == 0) {
//                 continue;
//             }

//             boolean show = "All Statuses".equals(selectedStatus)
//                     || selectedStatus.equals(rowStatuses[rowIndex - 1]);
//             node.setVisible(show);
//             node.setManaged(show);
//         }
//     }

    // =========================================================
    // COLUMN
    // =========================================================

    private ColumnConstraints column(
            double percentage
    ) {

        ColumnConstraints c =
                new ColumnConstraints();

        c.setPercentWidth(
                percentage
        );

        c.setFillWidth(true);

        return c;
    }

    // =========================================================
    // HEADER
    // =========================================================

    private void addHeader(
            GridPane grid,
            String text,
            int column
    ) {

        Label label =
                new Label(
                        text
                );

        label.setPrefHeight(53);

        label.setMaxWidth(
                Double.MAX_VALUE
        );

        label.setAlignment(
                Pos.CENTER_LEFT
        );

        label.setPadding(
                new Insets(
                        0,
                        8,
                        0,
                        20
                )
        );

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11
                )
        );

        label.setTextFill(
                Color.web("#3F484D")
        );

        label.setStyle(
                "-fx-background-color: #FAFBFB;" +
                "-fx-border-color: " +
                BORDER_COLOR + ";" +
                "-fx-border-width: 1 0 1 0;"
        );

        grid.add(
                label,
                column,
                0
        );
    }

    // =========================================================
    // TRIP ROW
    // =========================================================

    private void addTripRow(
            GridPane grid,
            int row,
            String tripId,
            String loadId,
            String route,
            String initials,
            String driver,
            String vehicle,
            String started,
            String status
    ) {

        addNormalCell(
                grid,
                tripId,
                0,
                row,
                true
        );

        addNormalCell(
                grid,
                loadId,
                1,
                row,
                false
        );

        addRouteCell(
                grid,
                route,
                2,
                row
        );

        addDriverCell(
                grid,
                initials,
                driver,
                3,
                row
        );

        addVehicleCell(
                grid,
                vehicle,
                4,
                row
        );

        addNormalCell(
                grid,
                started,
                5,
                row,
                false
        );

        addStatusCell(
                grid,
                status,
                6,
                row
        );

        addRowHoverEffect(
                grid,
                row
        );

        addRowClickEffect(
                grid,
                row,
                tripId
        );
    }

    // =========================================================
    // ROW HOVER EFFECT
    // =========================================================

    private void addRowHoverEffect(
            GridPane grid,
            int row
    ) {

        for (javafx.scene.Node node :
                grid.getChildren()) {

            Integer nodeRow =
                    GridPane.getRowIndex(node);

            if (nodeRow != null &&
                    nodeRow == row) {

                node.setOnMouseEntered(event -> {

                    for (javafx.scene.Node rowNode :
                            grid.getChildren()) {

                        Integer currentRow =
                                GridPane.getRowIndex(rowNode);

                        if (currentRow != null &&
                                currentRow == row) {

                            rowNode.setStyle(
                                    "-fx-background-color: " +
                                    "#F5FAF8" + ";" +
                                    "-fx-border-color: #D8E5E0;" +
                                    "-fx-border-width: 0 0 1 0;"
                            );
                        }
                    }
                });

                node.setOnMouseExited(event -> {

                    for (javafx.scene.Node rowNode :
                            grid.getChildren()) {

                        Integer currentRow =
                                GridPane.getRowIndex(rowNode);

                        if (currentRow != null &&
                                currentRow == row) {

                            restoreRowStyle(
                                    rowNode
                            );
                        }
                    }
                });
            }
        }
    }

    // =========================================================
    // ROW CLICK EFFECT
    // =========================================================

    private void addRowClickEffect(
            GridPane grid,
            int row,
            String tripId
    ) {

        for (javafx.scene.Node node :
                grid.getChildren()) {

            Integer nodeRow =
                    GridPane.getRowIndex(node);

            if (nodeRow != null &&
                    nodeRow == row) {

                node.setOnMouseClicked(event -> {

                    System.out.println(
                            "Selected Trip: "
                            + tripId
                    );

                    if (event.getClickCount() == 2) {

                        System.out.println(
                                "Opening Trip Details: "
                                + tripId
                        );
                    }
                });
            }
        }
    }

    // =========================================================
    // RESTORE ROW STYLE
    // =========================================================

    private void restoreRowStyle(
            javafx.scene.Node node
    ) {

        if (node instanceof Label) {

            Label label =
                    (Label) node;

            String currentText =
                    label.getText();

            if (currentText.equals("In Transit") ||
                    currentText.equals("Delivered") ||
                    currentText.equals("Closed") ||
                    currentText.equals("Cancelled")) {

                return;
            }

            label.setStyle(
                    "-fx-border-color: #E2E6E7;" +
                    "-fx-border-width: 0 0 1 0;" +
                    "-fx-background-color: transparent;"
            );

        } else if (node instanceof StackPane) {

            StackPane pane =
                    (StackPane) node;

            pane.setStyle(
                    "-fx-border-color: #E2E6E7;" +
                    "-fx-border-width: 0 0 1 0;" +
                    "-fx-background-color: transparent;"
            );
        }
    }

    // =========================================================
    // NORMAL CELL
    // =========================================================

    private void addNormalCell(
            GridPane grid,
            String text,
            int column,
            int row,
            boolean blue
    ) {

        Label label =
                new Label(
                        text
                );

        label.setMinHeight(90);

        label.setMaxWidth(
                Double.MAX_VALUE
        );

        label.setWrapText(true);

        label.setAlignment(
                Pos.CENTER_LEFT
        );

        label.setPadding(
                new Insets(
                        0,
                        10,
                        0,
                        20
                )
        );

        label.setFont(
                Font.font(13)
        );

        if (blue) {

            label.setTextFill(
                    Color.web("#1F5DB3")
            );

        } else {

            label.setTextFill(
                    Color.web("#30383C")
            );
        }

        label.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: #E2E6E7;" +
                "-fx-border-width: 0 0 1 0;"
        );

        grid.add(
                label,
                column,
                row
        );
    }

    // =========================================================
    // ROUTE CELL
    // =========================================================

    private void addRouteCell(
            GridPane grid,
            String route,
            int column,
            int row
    ) {

        Label label =
                new Label(
                        route
                );

        label.setMinHeight(90);

        label.setMaxWidth(
                Double.MAX_VALUE
        );

        label.setWrapText(true);

        label.setAlignment(
                Pos.CENTER_LEFT
        );

        label.setPadding(
                new Insets(
                        0,
                        10,
                        0,
                        20
                )
        );

        label.setFont(
                Font.font(
                        "Arial",
                        13
                )
        );

        label.setTextFill(
                Color.web(
                        "#1F2937"
                )
        );

        label.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: #E2E6E7;" +
                "-fx-border-width: 0 0 1 0;"
        );

        grid.add(
                label,
                column,
                row
        );
    }

    // =========================================================
    // DRIVER CELL
    // =========================================================

    private void addDriverCell(
            GridPane grid,
            String initials,
            String driver,
            int column,
            int row
    ) {

        HBox box =
                new HBox(10);

        box.setAlignment(
                Pos.CENTER_LEFT
        );

        // =====================================================
        // AVATAR
        // =====================================================

        Circle circle =
                new Circle(
                        17,
                        Color.web("#E5E9EA")
                );

        StackPane avatar =
                new StackPane();

        avatar.getChildren().add(
                circle
        );

        Label initialLabel =
                new Label(
                        initials
                );

        initialLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        10
                )
        );

        initialLabel.setTextFill(
                Color.web("#354047")
        );

        avatar.getChildren().add(
                initialLabel
        );

        // =====================================================
        // DRIVER NAME
        // =====================================================

        Label name =
                new Label(
                        driver
                );

        name.setFont(
                Font.font(13)
        );

        name.setWrapText(true);

        name.setTextFill(
                Color.web(
                        "#1F2937"
                )
        );

        box.getChildren().addAll(
                avatar,
                name
        );

        // =====================================================
        // CELL
        // =====================================================

        StackPane cell =
                new StackPane(
                        box
                );

        cell.setMinHeight(90);

        cell.setMaxWidth(
                Double.MAX_VALUE
        );

        cell.setAlignment(
                Pos.CENTER_LEFT
        );

        cell.setPadding(
                new Insets(
                        0,
                        10,
                        0,
                        20
                )
        );

        cell.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: #E2E6E7;" +
                "-fx-border-width: 0 0 1 0;"
        );

        grid.add(
                cell,
                column,
                row
        );
    }

    // =========================================================
    // VEHICLE CELL
    // =========================================================

    private void addVehicleCell(
            GridPane grid,
            String vehicle,
            int column,
            int row
    ) {

        Label label =
                new Label(
                        vehicle
                );

        label.setPadding(
                new Insets(
                        6,
                        9,
                        6,
                        9
                )
        );

        label.setWrapText(true);

        label.setFont(
                Font.font(11)
        );

        label.setTextFill(
                Color.web("#30383C")
        );

        label.setStyle(
                "-fx-background-color: #F0F2F2;" +
                "-fx-border-color: #D9DEDF;" +
                "-fx-border-radius: 4;" +
                "-fx-background-radius: 4;"
        );

        StackPane cell =
                new StackPane(
                        label
                );

        cell.setMinHeight(90);

        cell.setAlignment(
                Pos.CENTER_LEFT
        );

        cell.setPadding(
                new Insets(
                        0,
                        8,
                        0,
                        20
                )
        );

        cell.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: #E2E6E7;" +
                "-fx-border-width: 0 0 1 0;"
        );

        grid.add(
                cell,
                column,
                row
        );
    }

    // =========================================================
    // STATUS CELL
    // =========================================================

    private void addStatusCell(
            GridPane grid,
            String status,
            int column,
            int row
    ) {

        Label label =
                new Label(
                        status
                );

        label.setPadding(
                new Insets(
                        7,
                        10,
                        7,
                        10
                )
        );

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        10
                )
        );

        String bg;
        String text;

        // =====================================================
        // STATUS COLORS
        // =====================================================

        switch (status) {

            case "In Transit":

                bg = "#E2EDFF";
                text = "#2459A5";

                break;

            case "Delivered":

                bg = "#DDF6E5";
                text = "#18733D";

                break;

            case "Closed":

                bg = "#F0F2F3";
                text = "#30383C";

                break;

            case "Completed":

                bg = "#E8F4EE";
                text = "#176C4F";

                break;

            default:

                bg = "#FBE1E1";
                text = "#C83232";

                break;
        }

        label.setStyle(
                "-fx-background-color: " +
                bg + ";" +
                "-fx-text-fill: " +
                text + ";" +
                "-fx-background-radius: 15;"
        );

        StackPane cell =
                new StackPane(
                        label
                );

        cell.setMinHeight(90);

        cell.setAlignment(
                Pos.CENTER_LEFT
        );

        cell.setPadding(
                new Insets(
                        0,
                        8,
                        0,
                        20
                )
        );

        cell.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: #E2E6E7;" +
                "-fx-border-width: 0 0 1 0;"
        );

        grid.add(
                cell,
                column,
                row
        );
    }
}
