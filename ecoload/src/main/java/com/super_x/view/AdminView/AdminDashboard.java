package com.super_x.view.AdminView;

import com.super_x.controller.admincontroller.DashboardController;
import com.super_x.model.adminmodel.Driver;
import com.super_x.model.adminmodel.SupportTicket;
import com.super_x.model.adminmodel.User;

import com.super_x.dao.admindao.TripDAO;

import com.super_x.NavigationService;
import com.super_x.view.HomePage;
import com.super_x.view.Login;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import javafx.application.Platform;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * AdminDashboard - Centralized admin application shell/layout.
 * Manages sidebar, topbar, and content area for all admin pages.
 * Coordinates navigation across all admin sections.
 */
public class AdminDashboard {

        private static final String BORDER_COLOR = "#D5DBDE";
        private static final String GRAY_COLOR = "#697278";
        private static final String NUMBER_GREEN = "#2D9950";
        private static final String HOVER_BORDER = "#075E49";
        private static final String BG_COLOR = "#EEF8F4";

        private final DashboardController dashboardController;
        private final TripDAO tripDAO;

        private Label activeUsersNumberLabel;
        private Label sosAlertsNumberLabel;
        private Label inTransitTripsNumberLabel;
        private Label inTransitRoutesLabel;

        private VBox verificationRows;
        private VBox supportTicketRows;

        private Scene dashboardScene;
        private NavigationService navigationService;
        private StackPane contentArea;
        private AdminSidebar sidebar;
        private AdminTopBar topBar;

        // Keeps track of the page that was open before SOS Alerts.
        private String currentPage = "dashboard";
        private String pageBeforeAlerts = "dashboard";
        private boolean alertsOpen = false;

        // Admin pages are owned by AdminDashboard.
        private DriverTransporter drivers;
        private PendingDrivers pendingDrivers;
        private LoadsTrips loads;
        private Reports reports;
        private Reviews reviews;
        private SOSAlerts alerts;
        private SupportPage support;
        private AdminProfile profile;

        // Navigation title callback - Main will set the stage title
        private java.util.function.Consumer<String> titleUpdater;

        public AdminDashboard() {
                dashboardController = new DashboardController();
                tripDAO = new TripDAO();
        }

        public void setTitleUpdater(java.util.function.Consumer<String> updater) {
                this.titleUpdater = updater;
        }

        /**
         * Initializes the admin dashboard with all pages and navigation.
         * Main.java calls this during startup.
         */
        public void initialize(NavigationService navigationService) {
                this.navigationService = navigationService;

                // Create all admin pages here.
                drivers = new DriverTransporter();
                pendingDrivers = new PendingDrivers();
                loads = new LoadsTrips();
                reports = new Reports();
                reviews = new Reviews();
                alerts = new SOSAlerts();
                support = new SupportPage();
                profile = new AdminProfile();

                // Pages may also be opened as standalone scenes; give each one the
                // same router so its controls behave consistently.
                drivers.setNavigationService(navigationService);
                pendingDrivers.setNavigationService(navigationService);
                loads.setNavigationService(navigationService);
                reports.setNavigationService(navigationService);
                reviews.setNavigationService(navigationService);
                alerts.setNavigationService(navigationService);
                support.setNavigationService(navigationService);

                // Create sidebar and topbar
                sidebar = new AdminSidebar();
                topBar = AdminTopBar.create();

                // Build the scene (creates contentArea)
                getAdminDashboardScene();

                // Wire up navigation callbacks - Sidebar
                sidebar.setOnDashboard(() -> navigationService.showDashboard());
                sidebar.setOnDrivers(() -> navigationService.showDrivers());
                sidebar.setOnPendingDrivers(() -> navigationService.showPendingDrivers());
                sidebar.setOnLoadsTrips(() -> navigationService.showLoadsTrips());
                sidebar.setOnReports(() -> navigationService.showReports());
                sidebar.setOnReviews(() -> navigationService.showReviews());
                sidebar.setOnSupport(() -> navigationService.showSupport());
                // sidebar.setOnLogout(() -> System.exit(0));

                sidebar.setOnLogout(() -> {
                        Login login = new Login();

                        HomePage.homeStage.setScene(login.getScene());
                        HomePage.homeStage.setTitle("EcoLoad AI - Login");
                });

                // Wire up navigation callbacks - TopBar
                topBar.setOnProfile(() -> navigationService.showProfile());
                topBar.setOnSOSAlerts(() -> navigationService.toggleSOSAlerts());

                // Set the navigation handler to update page content
                navigationService.setNavigationHandler(this::handleGlobalNavigation);

                // Show the first page
                showPage("dashboard");
                loadDashboardData();
                loadVerificationData();
                loadSupportTicketData();
        }

        private void handleGlobalNavigation(String pageName) {

                // The same SOS bell is used as an OPEN/CLOSE toggle on every page.
                // If SOS is open, clicking the bell returns to exactly the page that
                // was open before SOS Alerts.
                if ("toggleAlerts".equals(pageName) || "alerts".equals(pageName)) {
                        if (alertsOpen) {
                                alertsOpen = false;
                                String restorePage = pageBeforeAlerts;
                                currentPage = restorePage;

                                if (titleUpdater != null) {
                                        titleUpdater.accept("EcoLoad AI - Admin");
                                }
                                sidebar.setActivePage(restorePage);
                                showPage(restorePage);
                        } else {
                                pageBeforeAlerts = currentPage;
                                alertsOpen = true;

                                if (titleUpdater != null) {
                                        titleUpdater.accept("EcoLoad AI - SOS Alerts");
                                }
                                sidebar.setActivePage("alerts");
                                showPage("alerts");
                        }
                        return;
                }

                // A normal sidebar/page navigation closes SOS mode and makes the
                // selected page the new current page.
                alertsOpen = false;
                currentPage = pageName;
                sidebar.setActivePage(pageName);

                // Update page title
                String title = "profile".equals(pageName)
                                ? "EcoLoad AI - Admin Profile"
                                : "EcoLoad AI - Admin";
                if (titleUpdater != null) {
                        titleUpdater.accept(title);
                }
                showPage(pageName);
        }

        private void navigateTo(String pageName) {
                // Always go through the global navigation handler so currentPage is
                // updated. This is essential for SOS toggle: Drivers & Users -> SOS
                // -> Drivers & Users, Loads & Trips -> SOS -> Loads & Trips, etc.
                handleGlobalNavigation(pageName);
        }

        /**
         * Changes only the right-side content.
         * The sidebar and topbar remain unchanged.
         */
        private void showPage(String pageName) {

                if (contentArea == null) {
                        return;
                }

                switch (pageName) {

                        case "dashboard":
                                contentArea.getChildren().setAll(createDashboardPage());
                                loadDashboardData();
                                loadVerificationData();
                                loadSupportTicketData();
                                break;

                        case "drivers":
                                contentArea.getChildren().setAll(drivers.getContent());
                                break;

                        case "pendingDrivers":
                                contentArea.getChildren().setAll(pendingDrivers.getContent());
                                break;

                        case "loads":
                                contentArea.getChildren().setAll(loads.getContent());
                                break;

                        case "reports":
                                contentArea.getChildren().setAll(reports.getContent());
                                break;

                        case "reviews":
                                contentArea.getChildren().setAll(reviews.getContent());
                                break;

                        case "alerts":
                                contentArea.getChildren().setAll(alerts.getContent());
                                break;

                        case "support":
                                contentArea.getChildren().setAll(support.getContent());
                                break;

                        case "profile":
                                contentArea.getChildren().setAll(profile.getContent());
                                break;

                        default:
                                contentArea.getChildren().setAll(createDashboardPage());
                                break;
                }
        }

        // =========================================================
        // GET ADMIN DASHBOARD SCENE
        // =========================================================

        public Scene getAdminDashboardScene() {

                // If scene already created, return it (idempotent)
                if (dashboardScene != null) {
                        return dashboardScene;
                }

                // =====================================================
                // ROOT
                // =====================================================

                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color:" + BG_COLOR + ";");

                // =====================================================
                // SIDEBAR
                // =====================================================

                VBox sidebarVBox = sidebar.createSidebar();
                root.setLeft(sidebarVBox);

                // =====================================================
                // TOP / CENTER AREA
                // =====================================================

                VBox topCenterArea = new VBox();

                // Top bar
                HBox topBarHBox = topBar.build();
                topCenterArea.getChildren().add(topBarHBox);

                // Content area
                contentArea = new StackPane();
                contentArea.setMinWidth(0);
                contentArea.setMaxWidth(Double.MAX_VALUE);
                contentArea.setMinHeight(0);
                contentArea.setMaxHeight(Double.MAX_VALUE);

                VBox.setVgrow(contentArea, Priority.ALWAYS);
                topCenterArea.getChildren().add(contentArea);

                root.setCenter(topCenterArea);

                // =====================================================
                // CREATE SCENE
                // =====================================================

                dashboardScene = new Scene(root);

                return dashboardScene;
        }

        // =========================================================
        // DASHBOARD PAGE CONTENT
        // =========================================================

        /**
         * Creates the dashboard page VBox content.
         */
        private VBox createDashboardPage() {

                VBox page = new VBox();

                VBox content = createDashboardContent();

                VBox.setVgrow(content, Priority.ALWAYS);

                page.getChildren().addAll(content);

                return page;
        }

        /**
         * Creates the actual dashboard content with cards and information.
         */
        private VBox createDashboardContent() {
                VBox content = new VBox(20);
                content.setPadding(new Insets(30, 30, 25, 30));
                content.setFillWidth(true);
                HBox titleRow = new HBox();
                titleRow.setAlignment(Pos.CENTER_LEFT);
                VBox titles = new VBox(5);
                Label dashboard = new Label("Dashboard");
                dashboard.setFont(Font.font("Arial", FontWeight.BOLD, 31));
                dashboard.setTextFill(Color.web("#171A1C"));
                Label description = new Label("Overview of fleet operations and platform health.");
                description.setFont(
                                Font.font(15));

                description.setTextFill(
                                Color.web(
                                                GRAY_COLOR));

                titles.getChildren().addAll(
                                dashboard,
                                description);

                Region titleSpacer = new Region();

                HBox.setHgrow(
                                titleSpacer,
                                Priority.ALWAYS);

                titleRow.getChildren().addAll(
                                titles,
                                titleSpacer);

                // =====================================================
                // CARDS
                // =====================================================

                HBox cards = new HBox(20);

                cards.setFillHeight(true);

                VBox activeUsers = createCard(
                                "Active Users",
                                "Loading...",
                                "↗ +12% this week",
                                false);

                VBox trips = createCard(
                                "In-Transit Trips",
                                "Loading...",
                                "Loading...",
                                false);

                // VBox revenue = createCard(
                // "Revenue This Month",
                // "₹20,000",
                // "↗ +5.2% vs last mo",
                // false);

                VBox sos = createCard(
                                "●  Open SOS Alerts",
                                "Loading...",
                                "ACTION REQUIRED",
                                true);

                HBox.setHgrow(
                                activeUsers,
                                Priority.ALWAYS);

                HBox.setHgrow(
                                trips,
                                Priority.ALWAYS);

                // HBox.setHgrow(
                // revenue,
                // Priority.ALWAYS);

                HBox.setHgrow(
                                sos,
                                Priority.ALWAYS);

                cards.getChildren().addAll(
                                activeUsers,
                                trips,
                                // revenue,
                                sos);

                // =====================================================
                // LOWER SECTION
                // =====================================================

                HBox lowerSection = new HBox(20);

                VBox verification = createVerificationPanel();

                VBox support = createSupportPanel();

                HBox.setHgrow(verification, Priority.ALWAYS);
                HBox.setHgrow(support, Priority.ALWAYS);

                verification.prefWidthProperty()
                                .bind(lowerSection.widthProperty().multiply(0.55));

                support.prefWidthProperty()
                                .bind(lowerSection.widthProperty().multiply(0.45));

                lowerSection.getChildren().addAll(
                                verification,
                                support);

                // VBox.setVgrow(
                // lowerSection,
                // Priority.ALWAYS
                // );

                content.getChildren().addAll(
                                titleRow,
                                cards,
                                lowerSection);

                return content;
        }

        // =========================================================
        // INTERACTIVE CARD
        // =========================================================

        private VBox createCard(
                        String title,
                        String number,
                        String bottomText,
                        boolean sos) {

                VBox card = new VBox(8);

                card.setPadding(
                                new Insets(
                                                18,
                                                20,
                                                15,
                                                20));

                card.setMinHeight(125);

                card.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: " +
                                                BORDER_COLOR + ";" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-background-radius: 10;");

                Label titleLabel = new Label(title);

                titleLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.NORMAL,
                                                14));

                titleLabel.setTextFill(
                                Color.web(
                                                GRAY_COLOR));

                Label numberLabel = new Label(number);

                if ("Active Users".equals(title)) {
                        activeUsersNumberLabel = numberLabel;
                }

                if ("In-Transit Trips".equals(title)) {
                        inTransitTripsNumberLabel = numberLabel;
                }

                if ("●  Open SOS Alerts".equals(title)) {
                        sosAlertsNumberLabel = numberLabel;
                }

                numberLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                32));

                if (sos) {

                        numberLabel.setTextFill(
                                        Color.web("#B42335"));

                } else {

                        numberLabel.setTextFill(
                                        Color.web(NUMBER_GREEN));
                }

                Label bottomLabel = new Label(bottomText);
                if ("In-Transit Trips".equals(title)) {
                        inTransitRoutesLabel = bottomLabel;
                }

                bottomLabel.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                12));

                if (sos) {

                        bottomLabel.setTextFill(
                                        Color.web("#9B3442"));

                } else {

                        bottomLabel.setTextFill(
                                        Color.web(GRAY_COLOR));
                }

                card.getChildren().addAll(
                                titleLabel,
                                numberLabel,
                                bottomLabel);

                // =====================================================
                // CARD HOVER
                // =====================================================

                card.setOnMouseEntered(event -> {

                        card.setStyle(
                                        "-fx-background-color: white;" +
                                                        "-fx-border-color: " +
                                                        "#4AA98C" + ";" +
                                                        "-fx-border-width: 2;" +
                                                        "-fx-border-radius: 10;" +
                                                        "-fx-background-radius: 10;");

                        ScaleTransition transition = new ScaleTransition(
                                        Duration.millis(150),
                                        card);

                        transition.setToX(0.97);

                        transition.setToY(0.97);

                        transition.play();
                });

                // =====================================================
                // CARD MOUSE EXIT
                // =====================================================

                card.setOnMouseExited(event -> {

                        card.setStyle(
                                        "-fx-background-color: white;" +
                                                        "-fx-border-color: " +
                                                        BORDER_COLOR + ";" +
                                                        "-fx-border-width: 1;" +
                                                        "-fx-border-radius: 10;" +
                                                        "-fx-background-radius: 10;");

                        ScaleTransition transition = new ScaleTransition(
                                        Duration.millis(150),
                                        card);

                        transition.setToX(1.0);

                        transition.setToY(1.0);

                        transition.play();
                });

                return card;
        }

        // =========================================================
        // NEW VERIFICATIONS
        // =========================================================

        private VBox createVerificationPanel() {

                VBox panel = new VBox(8);

                panel.setPadding(
                                new Insets(15));

                panel.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: " +
                                                BORDER_COLOR + ";" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-background-radius: 10;");

                addPanelHoverEffect(panel);

                Label heading = new Label(
                                "New Verifications");

                heading.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                18));

                heading.setTextFill(
                                Color.web("#354052"));

                HBox header = new HBox(heading);
                header.setAlignment(Pos.CENTER_LEFT);

                HBox tableHeader = createTableRow(
                                "TYPE",
                                "NAME",
                                "DATE",
                                "STATUS",
                                true);

                VBox rows = new VBox(2,

                                createTableRow(
                                                "Driver",
                                                "Rahul Sharma",
                                                "Oct 24, 2023",
                                                "Verified",
                                                false),

                                createTableRow(
                                                "Transporter",
                                                "Apex Logistics",
                                                "Oct 24, 2023",
                                                "Pending",
                                                false),

                                createTableRow(
                                                "Truck",
                                                "MH-12-AB-1234",
                                                "Oct 23, 2023",
                                                "Rejected",
                                                false),

                                createTableRow(
                                                "Driver",
                                                "Amit Patel",
                                                "Oct 22, 2023",
                                                "Pending",
                                                false),

                                createTableRow(
                                                "Driver",
                                                "Vikas Patil",
                                                "Oct 21, 2023",
                                                "Verified",
                                                false),

                                createTableRow(
                                                "Transporter",
                                                "Shree Logistics",
                                                "Oct 20, 2023",
                                                "Pending",
                                                false),

                                createTableRow(
                                                "Truck",
                                                "MH-14-CD-5678",
                                                "Oct 19, 2023",
                                                "Verified",
                                                false),

                                createTableRow(
                                                "Driver",
                                                "Suresh Kumar",
                                                "Oct 18, 2023",
                                                "Rejected",
                                                false),

                                createTableRow(
                                                "Transporter",
                                                "FastTrack Transport",
                                                "Oct 17, 2023",
                                                "Pending",
                                                false),

                                createTableRow(
                                                "Driver",
                                                "Rohit Jadhav",
                                                "Oct 16, 2023",
                                                "Verified",
                                                false));

                verificationRows = rows;

                ScrollPane verificationScroll = createCompactScrollPane(rows);
                panel.getChildren().addAll(header, tableHeader, verificationScroll);

                return panel;
        }

        // =========================================================
        // TABLE ROW
        // =========================================================

        private HBox createTableRow(
                        String type,
                        String name,
                        String date,
                        String status,
                        boolean header) {

                HBox row = new HBox();

                row.setMinHeight(38);
                row.setMaxWidth(Double.MAX_VALUE);
                row.setFillHeight(true);

                Label typeLabel = new Label(type);
                Label nameLabel = new Label(name);
                Label dateLabel = new Label(date);
                Label statusLabel = new Label(status);

                Label[] labels = {
                                typeLabel,
                                nameLabel,
                                dateLabel,
                                statusLabel
                };

                // Responsive column widths
                typeLabel.prefWidthProperty().bind(row.widthProperty().multiply(0.15));
                nameLabel.prefWidthProperty().bind(row.widthProperty().multiply(0.35));
                dateLabel.prefWidthProperty().bind(row.widthProperty().multiply(0.30));
                statusLabel.prefWidthProperty().bind(row.widthProperty().multiply(0.20));

                // Allow columns to grow with the available space
                typeLabel.setMaxWidth(Double.MAX_VALUE);
                nameLabel.setMaxWidth(Double.MAX_VALUE);
                dateLabel.setMaxWidth(Double.MAX_VALUE);
                statusLabel.setMaxWidth(Double.MAX_VALUE);

                for (Label label : labels) {

                        label.setWrapText(true);

                        if (header) {

                                label.setFont(
                                                Font.font(
                                                                "Arial",
                                                                FontWeight.BOLD,
                                                                11));

                                label.setTextFill(
                                                Color.web("#697278"));

                        } else {

                                label.setFont(
                                                Font.font(
                                                                "Arial",
                                                                FontWeight.NORMAL,
                                                                12));

                                label.setTextFill(
                                                Color.web("#454D52"));

                                label.setStyle("-fx-text-fill: #454D52;");
                        }
                }

                // Status styling
                if (!header) {

                        if (status.equalsIgnoreCase("Verified")) {

                                statusLabel.setStyle(
                                                "-fx-background-color: #DFF4E5;" +
                                                                "-fx-background-radius: 8;" +
                                                                "-fx-padding: 5 8 5 8;" +
                                                                "-fx-text-fill: #218838;" +
                                                                "-fx-font-weight: bold;");

                        } else if (status.equalsIgnoreCase("Pending")) {

                                statusLabel.setStyle(
                                                "-fx-background-color: #FFF3CD;" +
                                                                "-fx-background-radius: 8;" +
                                                                "-fx-padding: 5 8 5 8;" +
                                                                "-fx-text-fill: #B77900;" +
                                                                "-fx-font-weight: bold;");

                        } else if (status.equalsIgnoreCase("Rejected")) {

                                statusLabel.setStyle(
                                                "-fx-background-color: #FDE2E2;" +
                                                                "-fx-background-radius: 8;" +
                                                                "-fx-padding: 5 8 5 8;" +
                                                                "-fx-text-fill: #C62828;" +
                                                                "-fx-font-weight: bold;");
                        }
                }

                row.getChildren().addAll(
                                typeLabel,
                                nameLabel,
                                dateLabel,
                                statusLabel);

                return row;
        }
        // =========================================================
        // SUPPORT PANEL
        // =========================================================

        private VBox createSupportPanel() {

                VBox panel = new VBox(8);

                panel.setPadding(new Insets(15));

                panel.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: " + BORDER_COLOR + ";" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-background-radius: 10;");

                addPanelHoverEffect(panel);

                HBox heading = new HBox();

                Label title = new Label(
                                "Recent Support Tickets");

                title.setFont(
                                Font.font(
                                                "Arial",
                                                FontWeight.BOLD,
                                                18));

                title.setTextFill(
                                Color.web("#354052"));

                heading.setAlignment(Pos.CENTER_LEFT);
                heading.getChildren().add(title);

                HBox header = new HBox();

                header.setMinHeight(35);

                Label id = new Label("ID");
                Label subject = new Label("SUBJECT");
                Label user = new Label("USER");
                Label status = new Label("STATUS");

                id.setPrefWidth(90);
                subject.setPrefWidth(190);
                user.setPrefWidth(90);
                status.setPrefWidth(75);

                Label[] headers = {
                                id,
                                subject,
                                user,
                                status
                };

                for (Label label : headers) {

                        label.setFont(
                                        Font.font(
                                                        "Arial",
                                                        FontWeight.BOLD,
                                                        11));

                        label.setTextFill(
                                        Color.web("#697278"));
                }

                header.getChildren().addAll(
                                id,
                                subject,
                                user,
                                status);

                VBox ticketRows = new VBox(2);

                supportTicketRows = ticketRows;

                ScrollPane ticketScroll = createCompactScrollPane(ticketRows);

                panel.getChildren().addAll(
                                heading,
                                header,
                                ticketScroll);

                return panel;
        }
        // =========================================================
        // PANEL HOVER EFFECT
        // =========================================================

        private ScrollPane createCompactScrollPane(VBox content) {
                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setFitToWidth(true);
                scrollPane.setPannable(true);
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

                scrollPane.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-background: transparent;" +
                                                "-fx-border-color: transparent;");
                return scrollPane;
        }

        private void addPanelHoverEffect(
                        VBox panel) {

                panel.setOnMouseEntered(event -> {

                        panel.setStyle(
                                        "-fx-background-color: white;" +
                                                        "-fx-border-color: " +
                                                        HOVER_BORDER + ";" +
                                                        "-fx-border-width: 2;" +
                                                        "-fx-border-radius: 10;" +
                                                        "-fx-background-radius: 10;");

                        ScaleTransition transition = new ScaleTransition(
                                        Duration.millis(150),
                                        panel);

                        transition.setToX(0.985);

                        transition.setToY(0.985);

                        transition.play();
                });

                panel.setOnMouseExited(event -> {

                        panel.setStyle(
                                        "-fx-background-color: white;" +
                                                        "-fx-border-color: " +
                                                        BORDER_COLOR + ";" +
                                                        "-fx-border-width: 1;" +
                                                        "-fx-border-radius: 10;" +
                                                        "-fx-background-radius: 10;");

                        ScaleTransition transition = new ScaleTransition(
                                        Duration.millis(150),
                                        panel);

                        transition.setToX(1.0);

                        transition.setToY(1.0);

                        transition.play();
                });
        }

        // =========================================================
        // SUPPORT TICKET ROW
        // =========================================================

        private HBox createTicketRow(
                        String idText,
                        String subjectText,
                        String userText,
                        String statusText) {

                HBox row = new HBox();

                row.setMinHeight(40);
                row.setMinWidth(385);
                row.setPrefWidth(385);

                Label id = new Label(idText);
                Label subject = new Label(subjectText);
                Label user = new Label(userText);
                Label status = new Label(statusText);

                id.setPrefWidth(90);
                subject.setPrefWidth(190);
                user.setPrefWidth(90);
                status.setPrefWidth(75);

                Label[] labels = {
                                id,
                                subject,
                                user,
                                status
                };

                for (Label label : labels) {

                        label.setFont(
                                        Font.font(
                                                        "Arial",
                                                        FontWeight.NORMAL,
                                                        11));

                        label.setTextFill(
                                        Color.web("#454D52"));

                        label.setStyle(
                                        "-fx-text-fill: #454D52;");

                        label.setWrapText(true);
                }

                // =====================================================
                // OPEN / CLOSED STATUS
                // =====================================================

                if (statusText.equals("Open")) {

                        status.setStyle(
                                        "-fx-background-color: #F6E6E8;" +
                                                        "-fx-background-radius: 8;" +
                                                        "-fx-padding: 5 8 5 8;" +
                                                        "-fx-text-fill: #9D2532;");

                } else {

                        status.setStyle(
                                        "-fx-background-color: #E8ECEE;" +
                                                        "-fx-background-radius: 8;" +
                                                        "-fx-padding: 5 8 5 8;" +
                                                        "-fx-text-fill: #4B5563;");
                }

                row.getChildren().addAll(
                                id,
                                subject,
                                user,
                                status);

                return row;
        }

        private void loadDashboardData() {

                CompletableFuture.runAsync(() -> {

                        try {
                                // Get data from Firebase through DashboardController
                                int activeUsersCount = dashboardController.getActiveUsersCount();

                                int sosCount = dashboardController.getSOSAlertCount();

                                int inTransitTripsCount = tripDAO.getInTransitTripsCount();

                                int inTransitRoutesCount = tripDAO.getInTransitRoutesCount();

                                // Update JavaFX UI on JavaFX Application Thread
                                Platform.runLater(() -> {

                                        if (activeUsersNumberLabel != null) {
                                                activeUsersNumberLabel.setText(
                                                                String.format("%,d", activeUsersCount));
                                        }

                                        if (sosAlertsNumberLabel != null) {
                                                sosAlertsNumberLabel.setText(
                                                                String.valueOf(sosCount));
                                        }

                                        // Update SOS notification badge on the bell
                                        if (topBar != null) {
                                                topBar.setSOSAlertCount(sosCount);
                                        }
                                        if (inTransitTripsNumberLabel != null) {
                                                inTransitTripsNumberLabel.setText(
                                                                String.valueOf(inTransitTripsCount));
                                        }

                                        if (inTransitRoutesLabel != null) {
                                                inTransitRoutesLabel.setText(
                                                                "Across " + inTransitRoutesCount + " routes");
                                        }
                                });

                        } catch (Exception e) {

                                e.printStackTrace();

                                // If Firebase loading fails
                                Platform.runLater(() -> {

                                        if (activeUsersNumberLabel != null) {
                                                activeUsersNumberLabel.setText("0");
                                        }

                                        if (sosAlertsNumberLabel != null) {
                                                sosAlertsNumberLabel.setText("0");
                                        }
                                });
                        }
                });
        }

        private void loadVerificationData() {

                CompletableFuture.runAsync(() -> {

                        try {
                                List<Driver> drivers = dashboardController.getDriverVerifications();
                                System.out.println(
                                                "DASHBOARD VERIFICATION DRIVER COUNT = "
                                                                + drivers.size());

                                Platform.runLater(() -> {

                                        if (verificationRows == null) {
                                                return;
                                        }

                                        verificationRows.getChildren().clear();

                                        int count = 0;

                                        for (Driver driver : drivers) {

                                                if (count >= 10) {
                                                        break;
                                                }

                                                String name = driver.getUsername();

                                                if (name == null || name.isBlank()) {
                                                        name = driver.getEmail();
                                                }

                                                String status = driver.getStatus();

                                                if (status == null || status.isBlank()) {
                                                        status = "Pending";
                                                } else {
                                                        switch (status.toUpperCase()) {
                                                                case "APPROVED":
                                                                        status = "Verified";
                                                                        break;

                                                                case "REJECTED":
                                                                        status = "Rejected";
                                                                        break;

                                                                case "PENDING":
                                                                        status = "Pending";
                                                                        break;

                                                                default:
                                                                        status = status;
                                                        }
                                                }

                                                String date = "N/A";

                                                if (driver.getCreatedAt() != null && !driver.getCreatedAt().isBlank()) {
                                                        try {
                                                                String createdAt = driver.getCreatedAt();

                                                                // Remove fractional seconds and Z
                                                                if (createdAt.contains(".")) {
                                                                        createdAt = createdAt.substring(0,
                                                                                        createdAt.indexOf("."));
                                                                }

                                                                if (createdAt.endsWith("Z")) {
                                                                        createdAt = createdAt.substring(0,
                                                                                        createdAt.length() - 1);
                                                                }

                                                                java.time.LocalDateTime dateTime = java.time.LocalDateTime
                                                                                .parse(createdAt);

                                                                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                                                                                .ofPattern(
                                                                                                "dd MMM yyyy, HH:mm");

                                                                date = dateTime.format(formatter);

                                                        } catch (Exception e) {
                                                                date = driver.getCreatedAt();
                                                        }
                                                }

                                                verificationRows.getChildren().add(
                                                                createTableRow(
                                                                                "Driver",
                                                                                name,
                                                                                date,
                                                                                status,
                                                                                false));

                                                count++;
                                        }
                                });

                        } catch (Exception e) {

                                e.printStackTrace();
                        }
                });
        }

        private void loadSupportTicketData() {

                CompletableFuture.runAsync(() -> {

                        try {
                                List<SupportTicket> tickets = dashboardController.getAllSupportTickets();

                                Platform.runLater(() -> {

                                        if (supportTicketRows == null) {
                                                return;
                                        }

                                        supportTicketRows.getChildren().clear();

                                        int count = 0;

                                        for (SupportTicket ticket : tickets) {

                                                if (count >= 10) {
                                                        break;
                                                }

                                                String ticketId = ticket.getTicketId();

                                                if (ticketId == null || ticketId.isBlank()) {
                                                        ticketId = "N/A";
                                                }

                                                String subject = ticket.getSubject();

                                                if (subject == null || subject.isBlank()) {
                                                        subject = "No Subject";
                                                }

                                                String user = ticket.getUserName();

                                                if (user == null || user.isBlank()) {
                                                        user = ticket.getUserEmail();
                                                }

                                                if (user == null || user.isBlank()) {
                                                        user = "Unknown";
                                                }

                                                String status = ticket.getStatus();

                                                if (status == null || status.isBlank()) {
                                                        status = "Open";
                                                }

                                                supportTicketRows.getChildren().add(
                                                                createTicketRow(
                                                                                ticketId,
                                                                                subject,
                                                                                user,
                                                                                status));

                                                count++;
                                        }
                                });

                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                });
        }
}
