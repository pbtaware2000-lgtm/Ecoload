package com.super_x.view.AdminView;
import com.super_x.controller.admincontroller.UserController;
import com.super_x.model.adminmodel.User;

import com.super_x.dao.admindao.UserDAO;
//import com.finaladmin.model.User;

import java.util.List;

// import java.util.List;
import com.super_x.NavigationService;

import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
// import javafx.geometry.Rectangle2D;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import com.super_x.dao.admindao.DriverDAO;
import com.super_x.model.adminmodel.Driver;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class DriverTransporter {

    // =========================================================
    // NAVIGATION
    // =========================================================

    private NavigationService navigationService;

    private final UserDAO userDAO = new UserDAO();
    private final DriverDAO driverDAO = new DriverDAO();

    public void setNavigationService(
            NavigationService navigationService
    ) {
        this.navigationService = navigationService;
    }

    private void navigateTo(String pageName) {

        if (navigationService != null) {
            navigationService.navigate(pageName);
        }
    }

    // =========================================================
    // COLORS
    // =========================================================

    private final String DARK_GREEN = "#004B3A";
    private final String HOVER_GREEN = "#075E49";
    private final String CLICK_GREEN = "#075E49";

    private final String BG_COLOR = "#EEF8F4";
    private final String BORDER_COLOR = "#D5DBDE";

    private final String TEXT_COLOR = "#171A1C";

    // =========================================================
    // CONTROLS
    // =========================================================

    private ComboBox<String> userTypeCombo;

    private TextField nameSearchField;

    private TableView<RequestData> requestTable;

    private Label requestCountLabel;

    private final UserController userController =
        new UserController();

    // =========================================================
    // DATA
    // =========================================================

    private final ObservableList<RequestData> allRequests =
            FXCollections.observableArrayList();

    private FilteredList<RequestData> filteredRequests;

    // =========================================================
    // REQUEST DATA
    // =========================================================

    public static class RequestData {

    private String id;
    private String type;
    private String name;
    private String phone;
    private String applied;
    private String status;
    private String documents;
    private String email;

    public RequestData(
            String id,
            String type,
            String name,
            String phone,
            String applied,
            String status,
            String documents,
            String email
    ) {

        this.id = id;
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.applied = applied;
        this.status = status;
        this.documents = documents;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getApplied() {
        return applied;
    }

    public String getStatus() {
        return status;
    }

    public String getDocuments() {
        return documents;
    }

    // ==========================================
    // EMAIL
    // ==========================================

    public String getEmail() {
        return email;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ==========================================
    // GMAIL
    // ==========================================

    public String getGmail() {

        return name
                .toLowerCase()
                .replaceAll("[^a-z0-9]", ".")
                .replaceAll("\\.+", ".")
                .replaceAll("^\\.|\\.$", "")
                + "@gmail.com";
    }
}

    // =========================================================
    // GET CONTENT
    // =========================================================

    public VBox getContent() {

        loadRequestData();

        VBox content = createMainContent();

        VBox.setVgrow(
                content,
                Priority.ALWAYS
        );

        return content;
    }

    // =========================================================
    // FULL SCENE
    // =========================================================

    public Scene getDriverTransporterScene() {

        loadRequestData();

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color:" +
                BG_COLOR +
                ";"
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

        VBox content = createMainContent();

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

    // =========================================================
    // START
    // =========================================================

//     public void start(Stage stage) {

//         Scene scene =
//                 getDriverTransporterScene();

//         stage.setScene(scene);

//         stage.setTitle(
//                 "EcoLoad AI - Drivers & Users"
//         );

//         Rectangle2D screenBounds =
//                 getScreenBounds();

//         double screenWidth =
//                 screenBounds.getWidth();

//         double screenHeight =
//                 screenBounds.getHeight();

//         double windowWidth =
//                 Math.min(
//                         1600,
//                         screenWidth - 40
//                 );

//         double windowHeight =
//                 Math.min(
//                         950,
//                         screenHeight - 80
//                 );

//         stage.setWidth(windowWidth);
//         stage.setHeight(windowHeight);

//         stage.setMinWidth(1000);
//         stage.setMinHeight(650);

//         stage.setResizable(true);

//         stage.centerOnScreen();

//         stage.show();

//         if (
//                 screenWidth >= 1300 &&
//                 screenHeight >= 750
//         ) {

//             stage.setMaximized(true);
//         }
//     }

//     // =========================================================
//     // SCREEN
//     // =========================================================

//     private Rectangle2D getScreenBounds() {

//         return Screen
//                 .getPrimary()
//                 .getVisualBounds();
//     }

    // =========================================================
    // LOAD DATA
    // =========================================================

    private void loadRequestData() {

    allRequests.clear();

    // =====================================================
    // USERS FROM FIREBASE
    // =====================================================

    List<User> users = userDAO.getAllUsers();

    for (User user : users) {

        String id = user.getId();
        String name = user.getUsername();
        String phone = user.getPhone();
        String email = user.getEmail();
        String status = user.getStatus();

        if (id == null || id.isBlank()) {
            id = "N/A";
        }

        if (name == null || name.isBlank()) {
            name = "N/A";
        }

        if (phone == null || phone.isBlank()) {
            phone = "N/A";
        }

        if (email == null || email.isBlank()) {
            email = "N/A";
        }

        if (status == null || status.isBlank()) {
            status = "ACTIVE";
        }

        allRequests.add(
                new RequestData(
                        id,
                        "User",
                        name,
                        phone,
                        "",
                        status,
                        "N/A",
                        email
                )
        );

        System.out.println(
                "Added USER to Admin UI: "
                        + id
                        + " | "
                        + name
        );
    }

    // =====================================================
    // DRIVERS FROM FIREBASE
    // =====================================================

    List<Driver> drivers = driverDAO.getAllDrivers();

    for (Driver driver : drivers) {

        String id = driver.getId();
        String name = driver.getUsername();
        String phone = driver.getPhone();
        String email = driver.getEmail();
        String status = driver.getStatus();

        if (id == null || id.isBlank()) {
            id = "N/A";
        }

        if (name == null || name.isBlank()) {
            name = "N/A";
        }

        if (phone == null || phone.isBlank()) {
            phone = "N/A";
        }

        if (email == null || email.isBlank()) {
            email = "N/A";
        }

        if (status == null || status.isBlank()) {
            status = "APPROVED";
        }

        allRequests.add(
                new RequestData(
                        id,
                        "Driver",
                        name,
                        phone,
                        "",
                        status,
                        "N/A",
                        email
                )
        );

        System.out.println(
                "Added DRIVER to Admin UI: "
                        + id
                        + " | "
                        + name
                        + " | Status: "
                        + status
        );
    }

    System.out.println(
            "===================================="
    );

    System.out.println(
            "Total Drivers & Users loaded: "
                    + allRequests.size()
    );

    System.out.println(
            "===================================="
    );
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
                "-fx-background-color:" +
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
                        true
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

        Region spacer =
                new Region();

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS
        );

        Button logout =
                createMenuButton(
                        "↪",
                        "Logout",
                        false
                );

        logout.setStyle(
                "-fx-background-color:#B42335;" +
                "-fx-background-radius:9;"
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

            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);
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

        setMenuButtonStyle(
                button,
                active
        );

        button.setOnMouseEntered(event -> {

            button.setStyle(
                    "-fx-background-color:" +
                    HOVER_GREEN +
                    ";" +
                    "-fx-background-radius:9;"
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
        });

        return button;
    }

    // =========================================================
    // MENU STYLE
    // =========================================================

    private void setMenuButtonStyle(
            Button button,
            boolean active
    ) {

        if (active) {

            button.setStyle(
                    "-fx-background-color:" +
                    CLICK_GREEN +
                    ";" +
                    "-fx-background-radius:9;"
            );

        } else {

            button.setStyle(
                    "-fx-background-color:transparent;" +
                    "-fx-background-radius:9;"
            );
        }
    }

    // =========================================================
    // TOP BAR
    // =========================================================

    private HBox createTopBar() {

        return AdminTopBar.create(navigationService).build();
    }

    // =========================================================
    // MAIN CONTENT
    // =========================================================

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

        // =====================================================
        // TITLE
        // =====================================================

        VBox titleBox =
                new VBox(4);

        Label title =
                new Label(
                        "Drivers & Users"
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
                        "Manage and verify all driver and user registrations"
                );

        subtitle.setFont(
                Font.font(14)
        );

        subtitle.setTextFill(
                Color.web("#6B7280")
        );

        titleBox.getChildren().addAll(
                title,
                subtitle
        );

        // =====================================================
        // FILTERS
        // =====================================================

        HBox filters =
                new HBox(20);

        VBox searchBox =
                createNameSearch();

        VBox userTypeBox =
                createUserTypeFilter();

        HBox.setHgrow(
                searchBox,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                userTypeBox,
                Priority.ALWAYS
        );

        filters.getChildren().addAll(
                searchBox,
                userTypeBox
        );

        // =====================================================
        // TABLE
        // =====================================================

        VBox table =
                createRequestsTable();

        VBox.setVgrow(
                table,
                Priority.ALWAYS
        );

        content.getChildren().addAll(
                titleBox,
                filters,
                table
        );

        return content;
    }

    // =========================================================
    // SEARCH
    // =========================================================

    private VBox createNameSearch() {

        VBox box =
                createClickableBlock();

        HBox labelRow =
                new HBox(9);

        labelRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label icon =
                new Label("♟");

        icon.setFont(
                Font.font(17)
        );

        icon.setTextFill(
                Color.web("#2D9950")
        );

        Label label =
                new Label(
                        "Search Driver or User"
                );

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        labelRow.getChildren().addAll(
                icon,
                label
        );

        nameSearchField =
                new TextField();

        nameSearchField.setPromptText(
                "Search by name..."
        );

        nameSearchField.setMaxWidth(
                Double.MAX_VALUE
        );

        nameSearchField.setPrefHeight(43);

        nameSearchField.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#D4DCDD;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-font-size:13px;"
        );

        nameSearchField.textProperty()
                .addListener(
                        (obs, oldValue, newValue) ->
                                applyFilters()
                );

        box.getChildren().addAll(
                labelRow,
                nameSearchField
        );

        return box;
    }

    // =========================================================
    // TYPE FILTER
    // =========================================================

    private VBox createUserTypeFilter() {

        VBox box =
                createClickableBlock();

        HBox labelRow =
                new HBox(9);

        labelRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Label icon =
                new Label("▼");

        icon.setFont(
                Font.font(17)
        );

        icon.setTextFill(
                Color.web("#2D9950")
        );

        Label label =
                new Label(
                        "2. Select Type"
                );

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        labelRow.getChildren().addAll(
                icon,
                label
        );

        userTypeCombo =
                new ComboBox<>();

        userTypeCombo.getItems().addAll(
                "All Types",
                "Driver",
                "User"
        );

        // DEFAULT = ALL TYPES
        userTypeCombo.setValue(
                "All Types"
        );

        userTypeCombo.setMaxWidth(
                Double.MAX_VALUE
        );

        userTypeCombo.setPrefHeight(43);

        userTypeCombo.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#D4DCDD;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-font-size:13px;"
        );

        userTypeCombo.setOnAction(
                event ->
                        applyFilters()
        );

        box.getChildren().addAll(
                labelRow,
                userTypeCombo
        );

        return box;
    }

    // =========================================================
    // FILTER BLOCK
    // =========================================================

    private VBox createClickableBlock() {

        VBox box =
                new VBox(10);

        box.setPadding(
                new Insets(
                        15,
                        20,
                        17,
                        20
                )
        );

        box.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:" +
                BORDER_COLOR +
                ";" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;"
        );

        box.setOnMouseEntered(
                event ->
                        box.setStyle(
                                "-fx-background-color:white;" +
                                "-fx-border-color:" +
                                HOVER_GREEN +
                                ";" +
                                "-fx-border-width:1.5;" +
                                "-fx-border-radius:10;" +
                                "-fx-background-radius:10;"
                        )
        );

        box.setOnMouseExited(
                event ->
                        box.setStyle(
                                "-fx-background-color:white;" +
                                "-fx-border-color:" +
                                BORDER_COLOR +
                                ";" +
                                "-fx-border-radius:10;" +
                                "-fx-background-radius:10;"
                        )
        );

        return box;
    }

    // =========================================================
    // TABLE
    // =========================================================

    private VBox createRequestsTable() {

        VBox tableBox =
                new VBox();

        tableBox.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:" +
                BORDER_COLOR +
                ";" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;"
        );

        // =====================================================
        // TITLE ROW
        // =====================================================

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

        requestCountLabel =
                new Label(
                        "Drivers & Users (8)"
                );

        requestCountLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18
                )
        );

        requestCountLabel.setTextFill(
                Color.web("#172026")
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button export =
                new Button(
                        "⇩  Export"
                );

        export.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#83A99A;" +
                "-fx-text-fill:#176C4F;" +
                "-fx-border-radius:7;" +
                "-fx-background-radius:7;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:7 14 7 14;" +
                "-fx-cursor:hand;"
        );

        export.setOnAction(
                event ->
                        exportCurrentList()
        );

        titleRow.getChildren().addAll(
                requestCountLabel,
                spacer,
                export
        );

        // =====================================================
        // TABLE VIEW
        // =====================================================

        requestTable =
                new TableView<>();

        requestTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        requestTable.setPlaceholder(
                new Label(
                        "No drivers or users found"
                )
        );

        requestTable.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:transparent;"
        );

        // =====================================================
        // ID COLUMN
        // =====================================================

        TableColumn<RequestData, String> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getId()
                        )
        );

        // =====================================================
        // NAME COLUMN
        // =====================================================

        TableColumn<RequestData, String> nameColumn =
                new TableColumn<>("NAME");

        nameColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getName()
                        )
        );

        // =====================================================
        // MOBILE COLUMN
        // =====================================================

        TableColumn<RequestData, String> phoneColumn =
                new TableColumn<>("MOBILE NUMBER");

        phoneColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getPhone()
                        )
        );

        // =====================================================
        // GMAIL COLUMN
        // =====================================================

        TableColumn<RequestData, String> gmailColumn =
        new TableColumn<>("GMAIL");

gmailColumn.setCellValueFactory(
        data ->
                new SimpleStringProperty(
                        data.getValue().getEmail()
                )
);

// =====================================================
// WIDTHS
// =====================================================

idColumn.setPrefWidth(180);
nameColumn.setPrefWidth(280);
phoneColumn.setPrefWidth(280);
gmailColumn.setPrefWidth(350);

requestTable.getColumns().addAll(
        idColumn,
        nameColumn,
        phoneColumn,
        gmailColumn
);

// =====================================================
// ROW HEIGHT
// =====================================================

requestTable.setFixedCellSize(55);

// =====================================================
// COLUMN STYLE
// =====================================================

styleTableColumn(idColumn);
styleTableColumn(nameColumn);
styleTableColumn(phoneColumn);
styleTableColumn(gmailColumn);
        // =====================================================
        // INTERACTIVE ROW HIGHLIGHT
        // =====================================================

        requestTable.setRowFactory(
                tableView -> {

                    TableRow<RequestData> row =
                            new TableRow<>();

                    row.setOnMouseEntered(
                            event -> {

                                if (!row.isEmpty()) {

                                    row.setStyle(
                                            "-fx-background-color:#E8F5F0;" +
                                            "-fx-cursor:hand;"
                                    );
                                }
                            }
                    );

                    row.setOnMouseExited(
                            event -> {

                                row.setStyle(
                                        ""
                                );
                            }
                    );

                    return row;
                }
        );

        VBox.setVgrow(
                requestTable,
                Priority.ALWAYS
        );

        tableBox.getChildren().addAll(
                titleRow,
                requestTable
        );

        // =====================================================
        // INITIAL FILTER
        // =====================================================

        applyFilters();

        return tableBox;
    }

    // =========================================================
    // STYLE TABLE COLUMN
    // =========================================================

    private void styleTableColumn(
            TableColumn<RequestData, String> column
    ) {

        column.setStyle(
                "-fx-alignment:CENTER-LEFT;"
        );

        column.setCellFactory(
                new Callback<
                        TableColumn<RequestData, String>,
                        TableCell<RequestData, String>
                        >() {

                    @Override
                    public TableCell<RequestData, String>
                    call(
                            TableColumn<RequestData, String> param
                    ) {

                        return new TableCell<>() {

                            @Override
                            protected void updateItem(
                                    String item,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        item,
                                        empty
                                );

                                if (
                                        empty ||
                                        item == null
                                ) {

                                    setText(null);

                                } else {

                                    setText(item);

                                    setTextFill(
                                            Color.web(
                                                    "#1C2529"
                                            )
                                    );

                                    setFont(
                                            Font.font(
                                                    "Arial",
                                                    12
                                            )
                                    );

                                    setAlignment(
                                            Pos.CENTER_LEFT
                                    );

                                    setPadding(
                                            new Insets(
                                                    0,
                                                    10,
                                                    0,
                                                    12
                                            )
                                    );

                                    setStyle(
                                            "-fx-alignment:CENTER-LEFT;"
                                    );
                                }
                            }
                        };
                    }
                }
        );
    }

    // =========================================================
    // APPLY FILTERS
    // =========================================================

    private void applyFilters() {

        if (requestTable == null) {
            return;
        }

        if (filteredRequests == null) {

            filteredRequests =
                    new FilteredList<>(
                            allRequests
                    );
        }

        String selectedType =
                userTypeCombo != null
                        ? userTypeCombo.getValue()
                        : "All Types";

        if (selectedType == null) {
            selectedType = "All Types";
        }

        String searchText = "";

        if (
                nameSearchField != null &&
                nameSearchField.getText() != null
        ) {

            searchText =
                    nameSearchField
                            .getText()
                            .trim()
                            .toLowerCase();
        }

        final String finalType =
                selectedType;

        final String finalSearch =
                searchText;

        // =====================================================
        // FILTER DATA
        // =====================================================

        filteredRequests.setPredicate(
                request -> {

                    boolean typeMatches =
                            finalType.equals(
                                    "All Types"
                            )
                            ||
                            request.getType()
                                    .equalsIgnoreCase(
                                            finalType
                                    );

                    boolean nameMatches =
                            finalSearch.isEmpty()
                            ||
                            request.getName()
                                    .toLowerCase()
                                    .contains(
                                            finalSearch
                                    );

                    return typeMatches &&
                           nameMatches;
                }
        );

        // =====================================================
        // SET TABLE DATA
        // =====================================================

        requestTable.setItems(
                filteredRequests
        );

        // =====================================================
        // COUNT
        // =====================================================

        int count =
                filteredRequests.size();

        // =====================================================
        // UPDATE TITLE
        // =====================================================

        updateRequestTitle(
                finalType,
                count
        );

        // =====================================================
        // UPDATE ID COLUMN
        // =====================================================

        updateIdColumnHeader(
                finalType
        );

        // =====================================================
        // REFRESH
        // =====================================================

        requestTable.refresh();
    }

    // =========================================================
    // UPDATE TITLE
    // =========================================================

    private void updateRequestTitle(
            String selectedType,
            int count
    ) {

        if (requestCountLabel == null) {
            return;
        }

        if (
                "Driver".equalsIgnoreCase(
                        selectedType
                )
        ) {

            requestCountLabel.setText(
                    "Drivers (" +
                    count +
                    ")"
            );

        } else if (
                "User".equalsIgnoreCase(
                        selectedType
                )
        ) {

            requestCountLabel.setText(
                    "Users (" +
                    count +
                    ")"
            );

        } else {

            requestCountLabel.setText(
                    "Drivers & Users (" +
                    count +
                    ")"
            );
        }
    }

    // =========================================================
    // UPDATE ID HEADER
    // =========================================================

    private void updateIdColumnHeader(
            String type
    ) {

        if (
                requestTable == null ||
                requestTable.getColumns().isEmpty()
        ) {
            return;
        }

        @SuppressWarnings("unchecked")
        TableColumn<RequestData, String> idColumn =
                (TableColumn<RequestData, String>)
                        requestTable
                                .getColumns()
                                .get(0);

        if (
                "Driver".equalsIgnoreCase(
                        type
                )
        ) {

            idColumn.setText(
                    "DRIVER ID"
            );

        } else if (
                "User".equalsIgnoreCase(
                        type
                )
        ) {

            idColumn.setText(
                    "USER ID"
            );

        } else {

            idColumn.setText(
                    "ID"
            );
        }
    }

    // =========================================================
    // EXPORT CURRENT LIST
    // =========================================================

    private void exportCurrentList() {

        if (
                filteredRequests == null ||
                filteredRequests.isEmpty()
        ) {

            System.out.println(
                    "Nothing to export."
            );

            return;
        }

        FileChooser fileChooser =
                new FileChooser();

        fileChooser.setTitle(
                "Export Drivers & Users"
        );

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "CSV Files",
                        "*.csv"
                )
        );

        fileChooser.setInitialFileName(
                getExportFileName()
        );

        Stage stage =
                (Stage) requestTable
                        .getScene()
                        .getWindow();

        File file =
                fileChooser.showSaveDialog(
                        stage
                );

        if (file == null) {
            return;
        }

        try (
                FileWriter writer =
                        new FileWriter(file)
        ) {

            // CSV HEADER

            writer.append("ID");
            writer.append(",");
            writer.append("NAME");
            writer.append(",");
            writer.append("MOBILE NUMBER");
            writer.append(",");
            writer.append("GMAIL");
            writer.append("\n");

            // DATA

            for (
                    RequestData request :
                    filteredRequests
            ) {

                writer.append(
                        csv(request.getId())
                );

                writer.append(",");

                writer.append(
                        csv(request.getName())
                );

                writer.append(",");

                writer.append(
                        csv(request.getPhone())
                );

                writer.append(",");

                writer.append(
                        csv(request.getEmail())
                );

                writer.append("\n");
            }

            writer.flush();

            System.out.println(
                    "Export successful: " +
                    file.getAbsolutePath()
            );

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // CSV VALUE
    // =========================================================

    private String csv(String value) {

        if (value == null) {
            return "";
        }

        return "\"" +
                value.replace(
                        "\"",
                        "\"\""
                ) +
                "\"";
    }

    // =========================================================
    // EXPORT FILE NAME
    // =========================================================

    private String getExportFileName() {

        String type =
                userTypeCombo != null
                        ? userTypeCombo.getValue()
                        : "All Types";

        if ("Driver".equalsIgnoreCase(type)) {

            return "Drivers.csv";

        } else if ("User".equalsIgnoreCase(type)) {

            return "Users.csv";

        } else {

            return "Drivers_Users.csv";
        }
    }
}
