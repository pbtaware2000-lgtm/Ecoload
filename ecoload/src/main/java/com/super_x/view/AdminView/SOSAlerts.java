package com.super_x.view.AdminView;

import com.super_x.NavigationService;
import com.super_x.dao.admindao.SupportTicketDAO;
import com.super_x.model.adminmodel.SupportTicket;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SOSAlerts {

    private NavigationService navigationService;

    // =====================================================
    // FIREBASE DAO
    // =====================================================

    private final SupportTicketDAO supportTicketDAO =
            new SupportTicketDAO();

    // =====================================================
    // NAVIGATION
    // =====================================================

    public void setNavigationService(
            NavigationService navigationService) {

        this.navigationService = navigationService;
    }

    private void navigateTo(String pageName) {

        if (navigationService != null) {
            navigationService.navigate(pageName);
        }
    }

    // =====================================================
    // COLORS
    // =====================================================

    private final String DARK_GREEN = "#004B3A";
    private final String HOVER_GREEN = "#075E49";
    private final String CLICK_GREEN = "#075E49";
    private final String BG_COLOR = "#EEF8F4";
    private final String TEXT_COLOR = "#171A1C";

    // =====================================================
    // DATA
    // =====================================================

    private final List<SOSData> allAlerts =
            new ArrayList<>();

    private GridPane sosGrid;

    // =====================================================
    // SOS DATA CLASS
    // =====================================================

    private static class SOSData {

        String id;
        String driver;
        String phone;
        String location;
        String time;
        String status;
        String priority;

        SOSData(
                String id,
                String driver,
                String phone,
                String location,
                String time,
                String status,
                String priority
        ) {

            this.id = id;
            this.driver = driver;
            this.phone = phone;
            this.location = location;
            this.time = time;
            this.status = status;
            this.priority = priority;
        }
    }

    // =====================================================
    // GET CONTENT
    // =====================================================

    public VBox getContent() {

        loadSOSData();

        VBox content =
                createMainContent();

        VBox.setVgrow(
                content,
                Priority.ALWAYS
        );

        return content;
    }

    // =====================================================
    // GET SCENE
    // =====================================================

    public Scene getSOSAlertsScene() {

        loadSOSData();

        BorderPane root =
                new BorderPane();

        root.setStyle(
                "-fx-background-color: " + BG_COLOR + ";"
        );

        // =================================================
        // SIDEBAR
        // =================================================

        VBox sidebar =
                createSidebar();

        root.setLeft(sidebar);

        // =================================================
        // RIGHT SIDE
        // =================================================

        VBox rightSide =
                new VBox();

        rightSide.setFillWidth(true);

        // =================================================
        // TOP BAR
        // =================================================

        HBox topBar =
                createTopBar();

        // =================================================
        // MAIN CONTENT
        // =================================================

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

        return new Scene(root);
    }

    // =====================================================
    // LOAD DATA FROM FIREBASE
    // =====================================================

    private void loadSOSData() {

        allAlerts.clear();

        try {

            List<SupportTicket> tickets =
                    supportTicketDAO
                            .getActiveHighPriorityTickets();

            for (SupportTicket ticket : tickets) {

                String ticketId =
                        safe(ticket.getTicketId());

                String subject =
                        safe(ticket.getSubject());

                String userEmail =
                        safe(ticket.getUserEmail());

                String issueType =
                        safe(ticket.getIssueType());

                String created =
                        safe(ticket.getCreated());

                String priority =
                        safe(ticket.getPriority());

                allAlerts.add(
                        new SOSData(
                                ticketId,
                                subject,
                                userEmail,
                                issueType,
                                created,
                                "Active",
                                priority
                        )
                );
            }

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "SOS Alerts loaded from Firebase: "
                            + allAlerts.size()
            );

            System.out.println(
                    "======================================"
            );

        } catch (Exception e) {

            System.err.println(
                    "ERROR: Unable to load SOS alerts from Firebase."
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safe(String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "-";
        }

        return value;
    }

    // =====================================================
    // SIDEBAR
    // =====================================================

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
                "-fx-background-color: "
                        + DARK_GREEN + ";"
        );

        // =================================================
        // LOGO
        // =================================================

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

        // =================================================
        // MENU
        // =================================================

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

        Button dashboardButton =
                createMenuButton(
                        "▦",
                        "Dashboard",
                        false
                );

        dashboardButton.setOnAction(
                event ->
                        navigateTo("dashboard")
        );

        Button driverButton =
                createMenuButton(
                        "▰",
                        "Drivers & Users",
                        false
                );

        driverButton.setOnAction(
                event ->
                        navigateTo("drivers")
        );

        Button loadsButton =
                createMenuButton(
                        "♧",
                        "Loads & Trips",
                        false
                );

        loadsButton.setOnAction(
                event ->
                        navigateTo("loads")
        );

        Button reportsButton =
                createMenuButton(
                        "▥",
                        "Reports",
                        false
                );

        reportsButton.setOnAction(
                event ->
                        navigateTo("reports")
        );

        Button supportButton =
                createMenuButton(
                        "♧",
                        "Support",
                        false
                );

        supportButton.setOnAction(
                event ->
                        navigateTo("support")
        );

        menu.getChildren().addAll(
                dashboardButton,
                driverButton,
                loadsButton,
                reportsButton,
                supportButton
        );

        // =================================================
        // SPACER
        // =================================================

        Region spacer =
                new Region();

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS
        );

        // =================================================
        // LOGOUT
        // =================================================

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

        sidebar.getChildren().addAll(
                logoBox,
                menu,
                spacer,
                logout
        );

        return sidebar;
    }

    // =====================================================
    // SIDEBAR LOGO
    // =====================================================

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
                    new Image(logoStream);

            logoView.setImage(
                    logoImage
            );

            logoView.setFitWidth(70);
            logoView.setFitHeight(70);

            logoView.setPreserveRatio(
                    true
            );

            logoView.setSmooth(
                    true
            );

        } else {

            System.out.println(
                    "ERROR: Logo not found: "
                            + imagePath
            );
        }

        return logoView;
    }

    // =====================================================
    // MENU BUTTON
    // =====================================================

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

        button.setGraphic(
                box
        );

        setMenuButtonStyle(
                button,
                active
        );

        button.setOnMouseEntered(
                event -> {

                    button.setStyle(
                            "-fx-background-color: "
                                    + HOVER_GREEN + ";" +
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
                }
        );

        button.setOnMouseExited(
                event -> {

                    setMenuButtonStyle(
                            button,
                            active
                    );

                    ScaleTransition shrink =
                            new ScaleTransition(
                                    Duration.millis(120),
                                    button
                            );

                    shrink.setToX(1.0);
                    shrink.setToY(1.0);

                    shrink.play();
                }
        );

        return button;
    }

    // =====================================================
    // MENU STYLE
    // =====================================================

    private void setMenuButtonStyle(
            Button button,
            boolean active
    ) {

        if (active) {

            button.setStyle(
                    "-fx-background-color: "
                            + CLICK_GREEN + ";" +
                    "-fx-background-radius: 9;"
            );

        } else {

            button.setStyle(
                    "-fx-background-color: "
                            + DARK_GREEN + ";" +
                    "-fx-background-radius: 9;"
            );
        }
    }

    // =====================================================
    // TOP BAR
    // =====================================================

    private HBox createTopBar() {

        return AdminTopBar
                .create(navigationService)
                .build();
    }

    // =====================================================
    // MAIN CONTENT
    // =====================================================

    private VBox createMainContent() {

        VBox content =
                new VBox(18);

        content.setPadding(
                new Insets(
                        25,
                        30,
                        20,
                        30
                )
        );

        content.setFillWidth(true);

        VBox.setVgrow(
                content,
                Priority.ALWAYS
        );

        // =================================================
        // TITLE
        // =================================================

        Label title =
                new Label(
                        "SOS Alerts"
                );

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        28
                )
        );

        title.setTextFill(
                Color.web(TEXT_COLOR)
        );

        Label subtitle =
                new Label(
                        "Monitor and respond to emergency alerts in real time"
                );

        subtitle.setFont(
                Font.font(14)
        );

        subtitle.setTextFill(
                Color.web("#657078")
        );

        VBox titleBox =
                new VBox(
                        4,
                        title,
                        subtitle
                );

        // =================================================
        // ACTIVE ALERT TITLE
        // =================================================

        HBox activeTitle =
                new HBox(8);

        activeTitle.setAlignment(
                Pos.CENTER_LEFT
        );

        Label activeIcon =
                new Label("●");

        activeIcon.setFont(
                Font.font(17)
        );

        activeIcon.setTextFill(
                Color.web("#D32F2F")
        );

        Label activeText =
                new Label(
                        "Active Emergency Alerts"
                );

        activeText.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18
                )
        );

        activeText.setTextFill(
                Color.web("#172026")
        );

        activeTitle.getChildren().addAll(
                activeIcon,
                activeText
        );

        // =================================================
        // ACTIVE CARDS
        // =================================================

        HBox alertCards =
                createActiveAlertCards();

        alertCards.setMaxWidth(
                Double.MAX_VALUE
        );

        // =================================================
        // TABLE
        // =================================================

        VBox table =
                createAlertsTable();

        VBox.setVgrow(
                table,
                Priority.ALWAYS
        );

        content.getChildren().addAll(
                titleBox,
                activeTitle,
                alertCards,
                table
        );

        return content;
    }

    // =====================================================
    // ACTIVE ALERT CARDS
    // =====================================================

    private HBox createActiveAlertCards() {

        HBox cards =
                new HBox(15);

        cards.setMaxWidth(
                Double.MAX_VALUE
        );

        for (SOSData alert :
                allAlerts) {

            if (!"Active".equals(
                    alert.status
            )) {

                continue;
            }

            VBox card =
                    createActiveAlertCard(
                            alert
                    );

            cards.getChildren().add(
                    card
            );
        }

        for (Node node :
                cards.getChildren()) {

            HBox.setHgrow(
                    node,
                    Priority.ALWAYS
            );
        }

        return cards;
    }

    // =====================================================
    // SINGLE ACTIVE ALERT CARD
    // =====================================================

    private VBox createActiveAlertCard(
            SOSData alert
    ) {

        VBox card =
                new VBox(8);

        card.setPadding(
                new Insets(15)
        );

        card.setMinHeight(135);

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        card.setCursor(
                javafx.scene.Cursor.HAND
        );

        setAlertCardNormalStyle(
                card
        );

        HBox heading =
                new HBox();

        heading.setAlignment(
                Pos.CENTER_LEFT
        );

        Label sos =
                new Label("SOS");

        sos.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        11
                )
        );

        sos.setTextFill(
                Color.WHITE
        );

        sos.setPadding(
                new Insets(
                        5,
                        9,
                        5,
                        9
                )
        );

        sos.setStyle(
                "-fx-background-color: #D32F2F;" +
                "-fx-background-radius: 6;"
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Label priority =
                new Label(
                        alert.priority
                );

        priority.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        10
                )
        );

        priority.setTextFill(
                Color.web("#D32F2F")
        );

        heading.getChildren().addAll(
                sos,
                spacer,
                priority
        );

        Label driver =
                new Label(
                        alert.driver
                );

        driver.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        driver.setTextFill(
                Color.web("#172026")
        );

        driver.setWrapText(true);

        Label location =
                new Label(
                        "📍 " + alert.location
                );

        location.setFont(
                Font.font(12)
        );

        location.setTextFill(
                Color.web("#657078")
        );

        location.setWrapText(true);

        Label time =
                new Label(
                        "Time: " + alert.time
                );

        time.setFont(
                Font.font(11)
        );

        time.setTextFill(
                Color.web("#697278")
        );

        Button view =
                new Button(
                        "View Details"
                );

        view.setMaxWidth(
                Double.MAX_VALUE
        );

        view.setStyle(
                "-fx-background-color: #004B3A;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 6;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );

        view.setOnAction(
                event ->
                        showAlertDetails(alert)
        );

        card.getChildren().addAll(
                heading,
                driver,
                location,
                time,
                view
        );

        card.setOnMouseEntered(
                event -> {

                    card.setStyle(
                            "-fx-background-color: #FFFFFF;" +
                            "-fx-border-color: #D32F2F;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 12;" +
                            "-fx-background-radius: 12;" +
                            "-fx-effect: dropshadow(" +
                            "gaussian, rgba(211,47,47,0.22), " +
                            "14, 0.25, 0, 4);"
                    );

                    ScaleTransition grow =
                            new ScaleTransition(
                                    Duration.millis(150),
                                    card
                            );

                    grow.setToX(1.025);
                    grow.setToY(1.025);

                    grow.play();
                }
        );

        card.setOnMouseExited(
                event -> {

                    setAlertCardNormalStyle(
                            card
                    );

                    ScaleTransition shrink =
                            new ScaleTransition(
                                    Duration.millis(150),
                                    card
                            );

                    shrink.setToX(1.0);
                    shrink.setToY(1.0);

                    shrink.play();
                }
        );

        card.setOnMouseClicked(
                event -> {

                    if (event.getTarget()
                            instanceof Button) {

                        return;
                    }

                    showAlertDetails(
                            alert
                    );
                }
        );

        return card;
    }

    // =====================================================
    // ALERT CARD STYLE
    // =====================================================

    private void setAlertCardNormalStyle(
            VBox card
    ) {

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #E6CACA;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-effect: dropshadow(" +
                "gaussian, rgba(0,0,0,0.06), " +
                "5, 0.1, 0, 2);"
        );
    }

    // =====================================================
    // SOS ALERTS TABLE
    // =====================================================

    private VBox createAlertsTable() {

        VBox tableBox =
                new VBox();

        tableBox.setMinHeight(0);

        tableBox.setMaxWidth(
                Double.MAX_VALUE
        );

        VBox.setVgrow(
                tableBox,
                Priority.ALWAYS
        );

        tableBox.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #D9E0E2;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;"
        );

        // =================================================
        // TITLE ROW
        // =================================================

        HBox titleRow =
                new HBox();

        titleRow.setPadding(
                new Insets(
                        14,
                        18,
                        10,
                        18
                )
        );

        titleRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label title =
                new Label(
                        "SOS Alerts Table"
                );

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18
                )
        );

        title.setTextFill(
                Color.web("#172026")
        );

        /*
         * REFRESH BUTTON REMOVED COMPLETELY
         */

        titleRow.getChildren().add(
                title
        );

        // =================================================
        // TABLE GRID
        // =================================================

        sosGrid =
                new GridPane();

        sosGrid.setMaxWidth(
                Double.MAX_VALUE
        );

        sosGrid.setHgap(0);
        sosGrid.setVgap(0);

        // =================================================
        // COLUMN WIDTHS
        // =================================================

        sosGrid.getColumnConstraints()
                .clear();

        sosGrid.getColumnConstraints()
                .addAll(

                        percentage(9),   // ID
                        percentage(16),  // SUBJECT
                        percentage(19),  // EMAIL
                        percentage(16),  // ISSUE
                        percentage(12),  // TIME
                        percentage(10),  // STATUS
                        percentage(9),   // PRIORITY
                        percentage(9)    // ACTION
                );

        addSOSHeaders();

        addSOSRows();

        // =================================================
        // SCROLL PANE
        // =================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(
                sosGrid
        );

        /*
         * IMPORTANT:
         *
         * Width follows available table width.
         */
        scrollPane.setFitToWidth(true);

        /*
         * We DON'T fit height because we
         * want vertical scrolling.
         */
        scrollPane.setFitToHeight(false);

        /*
         * HORIZONTAL SCROLLBAR REMOVED
         */
        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        /*
         * VERTICAL SCROLLBAR ENABLED
         */
        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.ALWAYS
        );

        scrollPane.setPannable(true);

        scrollPane.setMinHeight(0);

        scrollPane.setMaxWidth(
                Double.MAX_VALUE
        );

        scrollPane.setMaxHeight(
                Double.MAX_VALUE
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;" +
                "-fx-border-color: transparent;"
        );

        // =================================================
        // VIEWPORT STYLE
        // =================================================

        scrollPane.lookupAll(
                ".viewport"
        ).forEach(
                node ->
                        node.setStyle(
                                "-fx-background-color: white;"
                        )
        );

        // =================================================
        // ADD TO TABLE BOX
        // =================================================

        tableBox.getChildren().addAll(
                titleRow,
                scrollPane
        );

        return tableBox;
    }

    // =====================================================
    // COLUMN WIDTH
    // =====================================================

    private ColumnConstraints percentage(
            double value
    ) {

        ColumnConstraints c =
                new ColumnConstraints();

        c.setPercentWidth(value);

        c.setHgrow(
                Priority.ALWAYS
        );

        c.setFillWidth(true);

        return c;
    }

    // =====================================================
    // TABLE HEADERS
    // =====================================================

    private void addSOSHeaders() {

        addTableHeader(
                sosGrid,
                "ID",
                0
        );

        addTableHeader(
                sosGrid,
                "SUBJECT",
                1
        );

        addTableHeader(
                sosGrid,
                "USER EMAIL",
                2
        );

        addTableHeader(
                sosGrid,
                "ISSUE TYPE",
                3
        );

        addTableHeader(
                sosGrid,
                "TIME",
                4
        );

        addTableHeader(
                sosGrid,
                "STATUS",
                5
        );

        addTableHeader(
                sosGrid,
                "PRIORITY",
                6
        );

        addTableHeader(
                sosGrid,
                "ACTION",
                7
        );
    }

    // =====================================================
    // TABLE ROWS
    // =====================================================

    private void addSOSRows() {

        int row = 1;

        for (SOSData alert :
                allAlerts) {

            addSOSRow(
                    alert,
                    row
            );

            row++;
        }
    }

    // =====================================================
    // ADD ROW
    // =====================================================

    private void addSOSRow(
            SOSData alert,
            int row
    ) {

        addCell(
                sosGrid,
                alert.id,
                0,
                row
        );

        addCell(
                sosGrid,
                alert.driver,
                1,
                row
        );

        addCell(
                sosGrid,
                alert.phone,
                2,
                row
        );

        addCell(
                sosGrid,
                alert.location,
                3,
                row
        );

        addCell(
                sosGrid,
                alert.time,
                4,
                row
        );

        addStatusCell(
                sosGrid,
                alert.status,
                5,
                row
        );

        addPriorityCell(
                sosGrid,
                alert.priority,
                6,
                row
        );

        addActionCell(
                sosGrid,
                alert,
                7,
                row
        );
    }

    // =====================================================
    // TABLE HEADER
    // =====================================================

    private void addTableHeader(
            GridPane grid,
            String text,
            int column
    ) {

        Label label =
                new Label(text);

        label.setMaxWidth(
                Double.MAX_VALUE
        );

        label.setPrefHeight(38);

        label.setAlignment(
                Pos.CENTER_LEFT
        );

        label.setPadding(
                new Insets(
                        0,
                        7,
                        0,
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

        label.setTextFill(
                Color.BLACK
        );

        label.setStyle(
                "-fx-background-color: #F6F8F8;" +
                "-fx-text-fill: black;" +
                "-fx-border-color: #E0E5E6;" +
                "-fx-border-width: 1 0 1 0;"
        );

        grid.add(
                label,
                column,
                0
        );
    }

    // =====================================================
    // NORMAL CELL
    // =====================================================

    private void addCell(
            GridPane grid,
            String text,
            int column,
            int row
    ) {

        Label label =
                new Label(text);

        /*
         * IMPORTANT:
         *
         * Long text wraps inside cell.
         */
        label.setWrapText(true);

        label.setMaxWidth(
                Double.MAX_VALUE
        );

        label.setMinHeight(55);

        label.setPrefHeight(
                Region.USE_COMPUTED_SIZE
        );

        label.setAlignment(
                Pos.CENTER_LEFT
        );

        label.setPadding(
                new Insets(
                        8,
                        7,
                        8,
                        10
                )
        );

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.NORMAL,
                        11
                )
        );

        label.setTextFill(
                Color.BLACK
        );

        label.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: black;" +
                "-fx-border-color: #E5E9EA;" +
                "-fx-border-width: 0 0 1 0;"
        );

        grid.add(
                label,
                column,
                row
        );
    }

    // =====================================================
    // STATUS CELL
    // =====================================================

    private void addStatusCell(
            GridPane grid,
            String status,
            int column,
            int row
    ) {

        Label label =
                new Label(status);

        label.setPadding(
                new Insets(
                        5,
                        8,
                        5,
                        8
                )
        );

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        10
                )
        );

        label.setTextFill(
                Color.BLACK
        );

        if ("Active".equals(status)) {

            label.setStyle(
                    "-fx-background-color: #FBE5E5;" +
                    "-fx-text-fill: black;" +
                    "-fx-background-radius: 6;"
            );

        } else {

            label.setStyle(
                    "-fx-background-color: #E5F5EB;" +
                    "-fx-text-fill: black;" +
                    "-fx-background-radius: 6;"
            );
        }

        StackPane pane =
                new StackPane(label);

        pane.setMinHeight(55);

        pane.setAlignment(
                Pos.CENTER_LEFT
        );

        pane.setPadding(
                new Insets(
                        0,
                        6,
                        0,
                        6
                )
        );

        pane.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #E5E9EA;" +
                "-fx-border-width: 0 0 1 0;"
        );

        grid.add(
                pane,
                column,
                row
        );
    }

    // =====================================================
    // PRIORITY CELL
    // =====================================================

    private void addPriorityCell(
            GridPane grid,
            String priority,
            int column,
            int row
    ) {

        Label label =
                new Label(priority);

        label.setPadding(
                new Insets(
                        5,
                        9,
                        5,
                        9
                )
        );

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        10
                )
        );

        label.setTextFill(
                Color.BLACK
        );

        if ("Critical".equalsIgnoreCase(
                priority
        )) {

            label.setStyle(
                    "-fx-background-color: #FBE5E5;" +
                    "-fx-text-fill: black;" +
                    "-fx-background-radius: 6;"
            );

        } else if ("High".equalsIgnoreCase(
                priority
        )) {

            label.setStyle(
                    "-fx-background-color: #FFF0DD;" +
                    "-fx-text-fill: black;" +
                    "-fx-background-radius: 6;"
            );

        } else {

            label.setStyle(
                    "-fx-background-color: #FFF6D8;" +
                    "-fx-text-fill: black;" +
                    "-fx-background-radius: 6;"
            );
        }

        StackPane pane =
                new StackPane(label);

        pane.setMinHeight(55);

        pane.setAlignment(
                Pos.CENTER_LEFT
        );

        pane.setPadding(
                new Insets(
                        0,
                        6,
                        0,
                        6
                )
        );

        pane.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #E5E9EA;" +
                "-fx-border-width: 0 0 1 0;"
        );

        grid.add(
                pane,
                column,
                row
        );
    }

    // =====================================================
    // ACTION CELL
    // =====================================================

    private void addActionCell(
            GridPane grid,
            SOSData alert,
            int column,
            int row
    ) {

        HBox actions =
                new HBox(6);

        actions.setAlignment(
                Pos.CENTER_LEFT
        );

        Button view =
                smallButton(
                        "View",
                        "#E8F1FA",
                        "#2367A5"
                );

        Button resolve =
                smallButton(
                        "Resolve",
                        "#E7F5EC",
                        "#18834B"
                );

        view.setOnAction(
                event ->
                        showAlertDetails(alert)
        );

        resolve.setOnAction(
                event -> {

                    alert.status =
                            "Resolved";

                    refreshTable();
                }
        );

        actions.getChildren().addAll(
                view,
                resolve
        );

        StackPane pane =
                new StackPane(actions);

        pane.setMinHeight(55);

        pane.setAlignment(
                Pos.CENTER_LEFT
        );

        pane.setPadding(
                new Insets(
                        0,
                        4,
                        0,
                        5
                )
        );

        pane.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #E5E9EA;" +
                "-fx-border-width: 0 0 1 0;"
        );

        grid.add(
                pane,
                column,
                row
        );
    }

    // =====================================================
    // SMALL BUTTON
    // =====================================================

    private Button smallButton(
            String text,
            String background,
            String textColor
    ) {

        Button button =
                new Button(text);

        button.setPrefHeight(30);

        button.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        10
                )
        );

        button.setTextFill(
                Color.web(textColor)
        );

        button.setStyle(
                "-fx-background-color: "
                        + background + ";" +
                "-fx-border-color: "
                        + textColor + ";" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(
                event -> {

                    ScaleTransition grow =
                            new ScaleTransition(
                                    Duration.millis(100),
                                    button
                            );

                    grow.setToX(1.05);
                    grow.setToY(1.05);

                    grow.play();
                }
        );

        button.setOnMouseExited(
                event -> {

                    ScaleTransition shrink =
                            new ScaleTransition(
                                    Duration.millis(100),
                                    button
                            );

                    shrink.setToX(1.0);
                    shrink.setToY(1.0);

                    shrink.play();
                }
        );

        return button;
    }

    // =====================================================
    // REFRESH TABLE
    // =====================================================

    /*
     * Refresh button is removed from UI,
     * but this method is kept because
     * Resolve button uses it.
     */

    private void refreshTable() {

        loadSOSData();

        if (sosGrid == null) {
            return;
        }

        sosGrid.getChildren().clear();

        addSOSHeaders();

        addSOSRows();
    }

    // =====================================================
    // ALERT DETAILS
    // =====================================================

    private void showAlertDetails(
            SOSData alert
    ) {

        Stage detailsStage =
                new Stage();

        detailsStage.setTitle(
                "SOS Alert - "
                        + alert.id
        );

        VBox root =
                new VBox(15);

        root.setPadding(
                new Insets(25)
        );

        root.setStyle(
                "-fx-background-color: white;"
        );

        Label title =
                new Label(
                        "SOS Alert Details"
                );

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22
                )
        );

        title.setTextFill(
                Color.web("#004B3A")
        );

        Label id =
                detailLabel(
                        "Ticket ID: "
                                + alert.id
                );

        Label subject =
                detailLabel(
                        "Subject: "
                                + alert.driver
                );

        Label email =
                detailLabel(
                        "User Email: "
                                + alert.phone
                );

        Label issueType =
                detailLabel(
                        "Issue Type: "
                                + alert.location
                );

        Label time =
                detailLabel(
                        "Created: "
                                + alert.time
                );

        Label priority =
                detailLabel(
                        "Priority: "
                                + alert.priority
                );

        Label status =
                detailLabel(
                        "Status: "
                                + alert.status
                );

        Button resolve =
                new Button(
                        "Resolve Alert"
                );

        resolve.setStyle(
                "-fx-background-color: #21824A;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 7;" +
                "-fx-padding: 8 20;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );

        resolve.setOnAction(
                event -> {

                    alert.status =
                            "Resolved";

                    detailsStage.close();

                    refreshTable();
                }
        );

        Button close =
                new Button(
                        "Close"
                );

        close.setStyle(
                "-fx-background-color: "
                        + DARK_GREEN + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 7;" +
                "-fx-padding: 8 20;" +
                "-fx-cursor: hand;"
        );

        close.setOnAction(
                event ->
                        detailsStage.close()
        );

        HBox buttons =
                new HBox(
                        10,
                        resolve,
                        close
                );

        buttons.setAlignment(
                Pos.CENTER_RIGHT
        );

        root.getChildren().addAll(
                title,
                id,
                subject,
                email,
                issueType,
                time,
                priority,
                status,
                buttons
        );

        Scene scene =
                new Scene(
                        root,
                        420,
                        430
                );

        detailsStage.setScene(
                scene
        );

        detailsStage.show();
    }

    // =====================================================
    // DETAIL LABEL
    // =====================================================

    private Label detailLabel(
            String text
    ) {

        Label label =
                new Label(text);

        label.setFont(
                Font.font(
                        "Arial",
                        14
                )
        );

        label.setTextFill(
                Color.BLACK
        );

        label.setWrapText(true);

        return label;
    }
}