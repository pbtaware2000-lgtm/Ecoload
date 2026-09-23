package com.super_x.view.DriverView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.time.format.DateTimeFormatter;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.super_x.config.FirebaseConfig;
import com.super_x.model.drivermodel.CurrentDriver;
import com.super_x.model.drivermodel.DriverModel;
import com.super_x.dao.userdao.LoadDao;

public class TripHistory {

    private Scene tripHistoryScene;

    private final ObservableList<Trip> allTrips = FXCollections.observableArrayList();

    private final ObservableList<Trip> filteredTrips = FXCollections.observableArrayList();

    private TableView<Trip> tripTable;

    private TextField searchField;
    private DatePicker fromDate;
    private DatePicker toDate;
    private ComboBox<String> statusFilter;

    private Label paginationLabel;
    private HBox paginationBox;

    private int currentPage = 1;

    private static final int ROWS_PER_PAGE = 5;

    private static final String GREEN = "#168A57";
    private static final String LIGHT_GREEN = "#E8F6EE";
    private static final String DARK = "#26312D";
    private static final String MUTED = "#6E7672";
    private static final String BORDER = "#D9E1DC";
    private static final String RED = "#D32F2F";
    private static final String ORANGE = "#E58A00";
    private final String BG = "#e6f1e8";

    public Scene getTripHistoryScene() {

        loadTripData();

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: " + BG + ";");

        VBox sidebar = DriverNavigation.createSidebar("Trip History");

        VBox mainContent = createMainContent();

        root.setLeft(sidebar);
        root.setCenter(mainContent);

        tripHistoryScene = new Scene(root, 1536, 750);
        updateTable();
        return tripHistoryScene;
    }

    /** Returns the exact total shown by the Trip History total-earnings card. */
    public int loadAndGetTotalEarnings() {
        loadTripData();
        return allTrips.stream().mapToInt(trip -> trip.earnings).sum();
    }

    // =========================================================
    // MAIN CONTENT
    // =========================================================

    private VBox createMainContent() {
        VBox page = new VBox(18);
        // page.setPadding(new Insets(0,0,10,0));

        page.setMaxWidth(
                Double.MAX_VALUE);

        page.setMaxHeight(
                Double.MAX_VALUE);

        HBox header = createHeader();

        HBox summaryCards = createSummaryCards();

        VBox filterBar = createFilterBar();

        VBox tableSection = createTripTable();

        HBox pagination = createPagination();

        VBox.setVgrow(
                tableSection,
                Priority.ALWAYS);

        page.getChildren().addAll(
                summaryCards,
                filterBar,
                tableSection,
                pagination);

        page.setPadding(new Insets(20, 20, 20, 20));

        VBox main = new VBox(header, page);

        return main;
    }

    // =========================================================
    // HEADER
    // =========================================================

    private HBox createHeader() {
        return DriverNavigation.createNavbar();

    }

    // =========================================================
    // SUMMARY CARDS
    // =========================================================

    private HBox createSummaryCards() {

        HBox cards = new HBox(15);

        int total = allTrips.size();

        long completed = allTrips.stream()
                .filter(t -> "Completed".equalsIgnoreCase(t.status))
                .count();

        long cancelled = allTrips.stream()
                .filter(t -> "Cancelled".equalsIgnoreCase(t.status))
                .count();

        int earnings = allTrips.stream()
                .mapToInt(t -> t.earnings)
                .sum();

        cards.getChildren().add(
                summaryCard(
                        "Total Trips",
                        String.valueOf(total),
                        "All recorded trips"));

        cards.getChildren().add(
                summaryCard(
                        "Completed",
                        String.valueOf(completed),
                        "Successfully completed"));

        cards.getChildren().add(
                summaryCard(
                        "Cancelled",
                        String.valueOf(cancelled),
                        "Cancelled trips"));

        cards.getChildren().add(
                summaryCard(
                        "Total Earnings",
                        "₹" + String.format("%,d", earnings),
                        "Trip earnings"));

        return cards;
    }

    private VBox summaryCard(
            String title,
            String value,
            String description) {

        Label titleLabel = new Label(title);

        titleLabel.setFont(
                Font.font(
                        "Segoe UI",
                        13));

        titleLabel.setTextFill(
                Color.web(MUTED));

        Label valueLabel = new Label(value);

        valueLabel.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        24));

        valueLabel.setTextFill(
                Color.web(DARK));

        Label desc = new Label(description);

        desc.setFont(
                Font.font(
                        "Segoe UI",
                        11));

        desc.setTextFill(
                Color.web(MUTED));

        VBox card = new VBox(
                7,
                titleLabel,
                valueLabel,
                desc);

        card.setPadding(
                new Insets(15));

        card.setPrefHeight(105);

        card.setMaxWidth(
                Double.MAX_VALUE);

        HBox.setHgrow(
                card,
                Priority.ALWAYS);

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 16;");
        // Mouse hover
        card.setOnMouseEntered(e -> {
            card.setStyle(
                    "-fx-background-color: #E8F6EE;" +
                            "-fx-background-radius: 16;" +
                            "-fx-border-color: " + GREEN + ";" +
                            "-fx-border-radius: 16;" +
                            "-fx-border-width: 1.5;");
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 16;" +
                            "-fx-border-color: " + BORDER + ";" +
                            "-fx-border-radius: 16;");
        });

        return card;
    }

    // =========================================================
    // FILTER BAR
    // =========================================================

    private VBox createFilterBar() {

        Label searchLabel = new Label("Search");

        searchField = new TextField();
        searchField.setStyle(
                "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;");

        searchField.setPromptText(
                "Search trip, origin or destination...");

        searchField.setPrefHeight(40);

        searchField.textProperty()
                .addListener(
                        (obs, oldValue, newValue) -> {

                            currentPage = 1;

                            updateTable();
                        });

        Label fromLabel = new Label("From");

        fromDate = new DatePicker();
        fromDate.setPromptText("DD/MM/YY");

        fromDate.setConverter(new StringConverter<LocalDate>() {

            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yy");

            @Override
            public String toString(LocalDate date) {
                return date == null ? "" : format.format(date);
            }

            @Override
            public LocalDate fromString(String text) {
                if (text == null || text.isEmpty()) {
                    return null;
                }
                return LocalDate.parse(text, format);
            }
        });
        fromDate.setStyle(
                "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;");

        fromDate.setPrefHeight(40);

        fromDate.valueProperty()
                .addListener(
                        (obs, oldValue, newValue) -> {

                            currentPage = 1;

                            updateTable();
                        });

        Label toLabel = new Label("To");

        toDate = new DatePicker();
        toDate.setPromptText("DD/MM/YY");

        toDate.setConverter(new StringConverter<LocalDate>() {

            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yy");

            @Override
            public String toString(LocalDate date) {
                return date == null ? "" : format.format(date);
            }

            @Override
            public LocalDate fromString(String text) {
                if (text == null || text.isEmpty()) {
                    return null;
                }
                return LocalDate.parse(text, format);
            }
        });
        toDate.setStyle(
                "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;");

        toDate.setPrefHeight(40);

        toDate.valueProperty()
                .addListener(
                        (obs, oldValue, newValue) -> {

                            currentPage = 1;

                            updateTable();
                        });

        Label statusLabel = new Label("Status");

        statusFilter = new ComboBox<>();
        statusFilter.setStyle(
                "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;");

        statusFilter.getItems().addAll(
                "All Status",
                "Completed",
                "Cancelled",
                "In Progress");

        statusFilter.getSelectionModel()
                .selectFirst();

        statusFilter.setPrefHeight(40);

        statusFilter.valueProperty()
                .addListener(
                        (obs, oldValue, newValue) -> {

                            currentPage = 1;

                            updateTable();
                        });

        Button reset = new Button("Reset");

        reset.setPrefHeight(40);
        reset.setPrefHeight(40);
        reset.setMinHeight(40);
        reset.setMaxHeight(40);

        reset.setTranslateY(10);

        reset.setCursor(
                Cursor.HAND);

        reset.setOnAction(
                e -> resetFilters());

        reset.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: " + DARK + ";" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-radius: 9;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 9;");

        reset.setOnMousePressed(e -> {
            reset.setStyle(
                    "-fx-background-color: #168A57;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 13px;" +
                            "-fx-background-radius: 9;");
        });

        reset.setOnMouseReleased(e -> {
            reset.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-text-fill: #26312D;" +
                            "-fx-font-size: 13px;" +
                            "-fx-background-radius: 9;" +
                            "-fx-border-color: #D9E1DC;" +
                            "-fx-border-radius: 9;");
        });

        GridPane grid = new GridPane();

        grid.setHgap(12);
        grid.setVgap(6);

        ColumnConstraints c1 = new ColumnConstraints();

        c1.setPercentWidth(34);

        ColumnConstraints c2 = new ColumnConstraints();

        c2.setPercentWidth(16);

        ColumnConstraints c3 = new ColumnConstraints();

        c3.setPercentWidth(16);

        ColumnConstraints c4 = new ColumnConstraints();

        c4.setPercentWidth(16);

        ColumnConstraints c5 = new ColumnConstraints();

        c5.setPercentWidth(18);

        grid.getColumnConstraints()
                .addAll(
                        c1,
                        c2,
                        c3,
                        c4,
                        c5);

        VBox searchBox = filterBox(
                searchLabel,
                searchField);

        VBox fromBox = filterBox(
                fromLabel,
                fromDate);

        VBox toBox = filterBox(
                toLabel,
                toDate);

        VBox statusBox = filterBox(
                statusLabel,
                statusFilter);

        grid.add(searchBox, 0, 0);
        grid.add(fromBox, 1, 0);
        grid.add(toBox, 2, 0);
        grid.add(statusBox, 3, 0);
        grid.add(reset, 4, 0);

        VBox card = new VBox(grid);

        card.setPadding(
                new Insets(14));

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 16;");
        card.setOnMouseEntered(e -> {
            card.setStyle(
                    "-fx-background-color: #E8F6EE;" +
                            "-fx-background-radius: 16;" +
                            "-fx-border-color: " + GREEN + ";" +
                            "-fx-border-radius: 16;" +
                            "-fx-border-width: 1.5;");
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 16;" +
                            "-fx-border-color: " + BORDER + ";" +
                            "-fx-border-radius: 16;");
        });

        return card;
    }

    private VBox filterBox(
            Label label,
            javafx.scene.Node control) {

        label.setTextFill(
                Color.web(MUTED));

        label.setFont(
                Font.font(
                        "Segoe UI",
                        11));

        if (control instanceof TextField) {

            ((TextField) control)
                    .setMaxWidth(
                            Double.MAX_VALUE);

        } else if (control instanceof DatePicker) {

            ((DatePicker) control)
                    .setMaxWidth(
                            Double.MAX_VALUE);

        } else if (control instanceof ComboBox<?>) {

            ((ComboBox<?>) control)
                    .setMaxWidth(
                            Double.MAX_VALUE);
        }

        VBox box = new VBox(
                5,
                label,
                control);

        return box;
    }

    // =========================================================
    // TABLE
    // =========================================================

    private VBox createTripTable() {

        tripTable = new TableView<>();

        tripTable.setItems(
                filteredTrips);

        tripTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        tripTable.setPrefHeight(
                350);

        tripTable.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-radius: 14;");

        TableColumn<Trip, String> id = new TableColumn<>("Trip ID");

        id.setCellValueFactory(
                data -> data.getValue().tripIdProperty());

        TableColumn<Trip, String> date = new TableColumn<>("Date");

        date.setCellValueFactory(
                data -> data.getValue().dateProperty());

        TableColumn<Trip, String> route = new TableColumn<>("Route");

        route.setCellValueFactory(
                data -> data.getValue().routeProperty());

        TableColumn<Trip, String> vehicle = new TableColumn<>("Vehicle");

        vehicle.setCellValueFactory(
                data -> data.getValue().vehicleProperty());

        TableColumn<Trip, String> cargo = new TableColumn<>("Cargo");

        cargo.setCellValueFactory(
                data -> data.getValue().cargoProperty());

        TableColumn<Trip, String> distance = new TableColumn<>("Distance");

        distance.setCellValueFactory(
                data -> data.getValue().distanceProperty());

        TableColumn<Trip, String> earnings = new TableColumn<>("Earnings");

        earnings.setCellValueFactory(
                data -> data.getValue().earningsProperty());

        TableColumn<Trip, String> status = new TableColumn<>("Status");

        status.setCellValueFactory(
                data -> data.getValue().statusProperty());

        status.setCellFactory(column -> new TableCell<>() {

            @Override
            protected void updateItem(
                    String item,
                    boolean empty) {

                super.updateItem(
                        item,
                        empty);

                if (empty || item == null) {

                    setGraphic(null);
                    setText(null);

                    return;
                }

                Label badge = new Label(item);

                badge.setPadding(
                        new Insets(
                                5,
                                10,
                                5,
                                10));

                badge.setStyle(
                        "-fx-background-color: " +
                                statusColor(item) +
                                ";" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 10;" +
                                "-fx-font-size: 11px;");

                setGraphic(badge);
                setText(null);

                setAlignment(
                        Pos.CENTER);
            }
        });

        TableColumn<Trip, Void> action = new TableColumn<>("Action");

        action.setCellFactory(column -> new TableCell<>() {

            private final Button button = new Button("View Details");

            {

                button.setCursor(
                        Cursor.HAND);

                button.setStyle(
                        "-fx-background-color: " +
                                LIGHT_GREEN +
                                ";" +
                                "-fx-text-fill: " +
                                GREEN +
                                ";" +
                                "-fx-font-size: 11px;" +
                                "-fx-background-radius: 8;");

                button.setOnAction(
                        e -> {

                            Trip trip = getTableView()
                                    .getItems()
                                    .get(getIndex());

                            showTripDetails(
                                    trip);
                        });
            }

            @Override
            protected void updateItem(
                    Void item,
                    boolean empty) {

                super.updateItem(
                        item,
                        empty);

                if (empty) {

                    setGraphic(null);

                } else {

                    setGraphic(button);
                }
            }
        });

        tripTable.getColumns().addAll(
                id,
                date,
                route,
                vehicle,
                cargo,
                distance,
                earnings,
                status,
                action);

        Label tableTitle = new Label("Trip Records");

        tableTitle.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        17));

        tableTitle.setTextFill(
                Color.web(DARK));

        VBox box = new VBox(
                10,
                tableTitle,
                tripTable);

        VBox.setVgrow(
                tripTable,
                Priority.ALWAYS);

        return box;
    }

    // =========================================================
    // PAGINATION
    // =========================================================

    private HBox createPagination() {

        paginationLabel = new Label();

        paginationLabel.setTextFill(
                Color.web(MUTED));

        paginationBox = new HBox(6);

        paginationBox.setAlignment(
                Pos.CENTER_RIGHT);

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        HBox pagination = new HBox(
                10,
                paginationLabel,
                spacer,
                paginationBox);

        pagination.setAlignment(
                Pos.CENTER_LEFT);

        return pagination;
    }

    private void updatePagination() {

        paginationBox.getChildren()
                .clear();

        int total = filteredTrips.size();

        int totalPages = Math.max(
                1,
                (int) Math.ceil(
                        total /
                                (double) ROWS_PER_PAGE));

        if (currentPage > totalPages) {

            currentPage = totalPages;
        }

        int start = total == 0
                ? 0
                : ((currentPage - 1)
                        * ROWS_PER_PAGE) + 1;

        int end = Math.min(
                currentPage * ROWS_PER_PAGE,
                total);

        paginationLabel.setText(
                "Showing " +
                        start +
                        " to " +
                        end +
                        " of " +
                        total +
                        " trips");

        Button previous = pageButton("‹");

        previous.setDisable(
                currentPage <= 1);

        previous.setOnAction(
                e -> {

                    currentPage--;

                    updateTable();
                });

        paginationBox.getChildren()
                .add(previous);

        for (int i = 1; i <= totalPages; i++) {

            final int pageNumber = i;

            Button page = pageButton(
                    String.valueOf(i));

            if (i == currentPage) {

                page.setStyle(
                        "-fx-background-color: " +
                                GREEN +
                                ";" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 8;");
            }

            page.setOnAction(
                    e -> {

                        currentPage = pageNumber;

                        updateTable();
                    });

            paginationBox.getChildren()
                    .add(page);
        }

        Button next = pageButton("›");

        next.setDisable(
                currentPage >= totalPages);

        next.setOnAction(
                e -> {

                    currentPage++;

                    updateTable();
                });

        paginationBox.getChildren()
                .add(next);
    }

    private Button pageButton(
            String text) {

        Button button = new Button(text);

        button.setPrefSize(
                35,
                32);

        button.setCursor(
                Cursor.HAND);

        button.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: " + DARK + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 8;");

        return button;
    }

    // =========================================================
    // FILTERING / UPDATE
    // =========================================================

    private void updateTable() {

        String search = searchField == null
                ? ""
                : searchField.getText()
                        .trim()
                        .toLowerCase();

        String selectedStatus = statusFilter == null
                ? "All Status"
                : statusFilter
                        .getValue();

        LocalDate from = fromDate == null
                ? null
                : fromDate.getValue();

        LocalDate to = toDate == null
                ? null
                : toDate.getValue();

        List<Trip> result = allTrips.stream()
                .filter(trip -> {

                    if (search.isEmpty()) {
                        return true;
                    }

                    return trip.tripId
                            .toLowerCase()
                            .contains(search)
                            ||
                            trip.origin
                                    .toLowerCase()
                                    .contains(search)
                            ||
                            trip.destination
                                    .toLowerCase()
                                    .contains(search)
                            ||
                            trip.route
                                    .toLowerCase()
                                    .contains(search);
                })
                .filter(trip -> {

                    if (selectedStatus == null ||
                            selectedStatus.equals("All Status")) {

                        return true;
                    }

                    return trip.status.equals(
                            selectedStatus);
                })
                .filter(trip -> {

                    if (from == null) {
                        return true;
                    }

                    return !trip.dateValue.isBefore(
                            from);
                })
                .filter(trip -> {

                    if (to == null) {
                        return true;
                    }

                    return !trip.dateValue.isAfter(
                            to);
                })
                .collect(
                        Collectors.toList());

        filteredTrips.setAll(result);

        int totalPages = Math.max(
                1,
                (int) Math.ceil(
                        filteredTrips.size()
                                /
                                (double) ROWS_PER_PAGE));

        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int fromIndex = Math.min(
                (currentPage - 1)
                        * ROWS_PER_PAGE,
                filteredTrips.size());

        int toIndex = Math.min(
                fromIndex + ROWS_PER_PAGE,
                filteredTrips.size());

        ObservableList<Trip> pageData = FXCollections.observableArrayList(
                filteredTrips.subList(
                        fromIndex,
                        toIndex));

        if (tripTable != null) {

            tripTable.setItems(
                    pageData);
        }

        if (paginationBox != null) {

            updatePagination();
        }
    }

    private void resetFilters() {

        searchField.clear();

        fromDate.setValue(null);

        toDate.setValue(null);

        statusFilter
                .getSelectionModel()
                .selectFirst();

        currentPage = 1;

        updateTable();
    }

    // =========================================================
    // DETAILS / NOTIFICATION / PROFILE
    // =========================================================

    private void showTripDetails(
            Trip trip) {

        String details = "Trip ID: " + trip.tripId +
                "\nDate: " + trip.dateValue +
                "\nOrigin: " + trip.origin +
                "\nDestination: " + trip.destination +
                "\nRoute: " + trip.route +
                "\nVehicle: " + trip.vehicle +
                "\nCargo: " + trip.cargo +
                "\nDistance: " + trip.distance +
                "\nEarnings: ₹" + trip.earnings +
                "\nStatus: " + trip.status;

        showMessage(
                "Trip Details",
                details);
    }

    private void showMessage(
            String title,
            String message) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // =========================================================
    // DATA
    // =========================================================

    private void loadTripData() {

        // Always reload from Firebase so a newly completed trip
        // appears immediately when this page is opened.
        allTrips.clear();

        try {

            DriverModel driver =
                    CurrentDriver.getInstance().getDriver();

            if (driver == null
                    || driver.getEmail() == null
                    || driver.getEmail().trim().isEmpty()) {

                System.out.println(
                        "Trip History: current driver not found."
                );

                return;
            }

            Firestore db =
                    FirebaseConfig.getFireStore();

            String driverId =
                    driver.getEmail().trim();

            List<QueryDocumentSnapshot> documents =
                    db.collection("trips")
                            .whereEqualTo(
                                    "driverId",
                                    driverId
                            )
                            .get()
                            .get()
                            .getDocuments();

            LoadDao loadDao =
                    new LoadDao();

            System.out.println(
                    "========== TRIP HISTORY =========="
            );

            System.out.println(
                    "Driver: "
                            + driverId
            );

            System.out.println(
                    "Trips found: "
                            + documents.size()
            );

            for (QueryDocumentSnapshot document :
                    documents) {

                com.super_x.model.drivermodel.Trip trip =
                        document.toObject(
                                com.super_x.model.drivermodel.Trip.class
                        );

                if (trip == null) {
                    continue;
                }

                String uiStatus =
                        normalizeHistoryStatus(
                                trip.getStatus()
                        );

                LocalDate tripDate =
                        resolveTripDate(trip);

                String origin =
                        safeText(
                                trip.getPickupLocation(),
                                "Unknown"
                        );

                String destination =
                        safeText(
                                trip.getDestination(),
                                "Unknown"
                        );

                String route =
                        origin
                                + " → "
                                + destination;

                // The current Trip model does not store vehicle,
                // earnings, or cargo directly. Cargo/earnings are
                // reconstructed from the loads belonging to the trip.
                String cargo =
                        buildCargoText(
                                trip,
                                loadDao
                        );

                int earnings =
                        calculateTripEarnings(
                                trip,
                                loadDao
                        );

                String distance =
                        formatDistance(
                                trip.getDistanceKm()
                        );

                // Vehicle type is not stored in Trip.java.
                // Keep the dashboard readable without inventing
                // a historical vehicle field.
                String vehicle =
                        "Truck";

                allTrips.add(
                        new Trip(
                                safeText(
                                        trip.getTripId(),
                                        document.getId()
                                ),
                                tripDate,
                                origin,
                                destination,
                                route,
                                vehicle,
                                cargo,
                                distance,
                                earnings,
                                uiStatus
                        )
                );

                System.out.println(
                        "History Trip: "
                                + trip.getTripId()
                                + " | "
                                + uiStatus
                );
            }

            System.out.println(
                    "================================="
            );

        } catch (Exception e) {

            e.printStackTrace();

            showMessage(
                    "Trip History",
                    "Failed to load trip history from Firebase."
            );
        }
    }

    // =========================================================
    // NORMALIZE FIREBASE STATUS TO UI STATUS
    // =========================================================

    private String normalizeHistoryStatus(
            String status) {

        if (status == null
                || status.trim().isEmpty()) {

            return "In Progress";
        }

        String value =
                status.trim()
                        .toUpperCase();

        switch (value) {

            case "COMPLETED":
                return "Completed";

            case "CANCELLED":
            case "CANCELED":
                return "Cancelled";

            case "ACTIVE":
            case "IN_TRANSIT":
            case "PICKUP_COMPLETED":
            case "DELIVERY":
                return "In Progress";

            default:
                return "In Progress";
        }
    }

    // =========================================================
    // RESOLVE TRIP DATE
    // =========================================================

    private LocalDate resolveTripDate(
            com.super_x.model.drivermodel.Trip trip) {

        String timestamp =
                trip.getCompletedTime();

        if (timestamp == null
                || timestamp.trim().isEmpty()) {

            timestamp =
                    trip.getStartTime();
        }

        if (timestamp != null
                && !timestamp.trim().isEmpty()) {

            try {

                return LocalDateTime
                        .parse(
                                timestamp.trim(),
                                DateTimeFormatter.ofPattern(
                                        "yyyy-MM-dd HH:mm:ss"
                                )
                        )
                        .toLocalDate();

            } catch (DateTimeParseException ignored) {

                try {

                    return LocalDate.parse(
                            timestamp.trim()
                    );

                } catch (DateTimeParseException ignoredAgain) {
                    // Use today's date as a safe fallback for
                    // old/manual records without a valid timestamp.
                }
            }
        }

        return LocalDate.now();
    }

    // =========================================================
    // BUILD CARGO TEXT FROM ALL LOADS
    // =========================================================

    private String buildCargoText(
            com.super_x.model.drivermodel.Trip trip,
            LoadDao loadDao) {

        try {

            List<String> loadIds =
                    trip.getLoadIds();

            if (loadIds == null
                    || loadIds.isEmpty()) {

                loadIds =
                        new ArrayList<>();

                if (trip.getLoadId() != null
                        && !trip.getLoadId()
                                .trim()
                                .isEmpty()) {

                    loadIds.add(
                            trip.getLoadId()
                    );
                }
            }

            List<String> cargoNames =
                    new ArrayList<>();

            for (String loadId : loadIds) {

                if (loadId == null
                        || loadId.trim().isEmpty()) {

                    continue;
                }

                com.super_x.model.usermodel.Load load =
                        loadDao.getLoadById(
                                loadId
                        );

                if (load == null) {
                    continue;
                }

                String loadType =
                        load.getLoadType();

                if (loadType != null
                        && !loadType.trim().isEmpty()
                        && !cargoNames.contains(
                                loadType.trim()
                        )) {

                    cargoNames.add(
                            loadType.trim()
                    );
                }
            }

            if (cargoNames.isEmpty()) {
                return "Unknown";
            }

            return String.join(
                    " + ",
                    cargoNames
            );

        } catch (Exception e) {

            e.printStackTrace();

            return "Unknown";
        }
    }

    // =========================================================
    // SUM OFFER PRICE OF ALL LOADS
    // =========================================================

    private int calculateTripEarnings(
            com.super_x.model.drivermodel.Trip trip,
            LoadDao loadDao) {

        try {

            List<String> loadIds =
                    trip.getLoadIds();

            if (loadIds == null
                    || loadIds.isEmpty()) {

                loadIds =
                        new ArrayList<>();

                if (trip.getLoadId() != null
                        && !trip.getLoadId()
                                .trim()
                                .isEmpty()) {

                    loadIds.add(
                            trip.getLoadId()
                    );
                }
            }

            double total =
                    0.0;

            for (String loadId : loadIds) {

                if (loadId == null
                        || loadId.trim().isEmpty()) {

                    continue;
                }

                com.super_x.model.usermodel.Load load =
                        loadDao.getLoadById(
                                loadId
                        );

                if (load != null) {

                    total +=
                            load.getOfferPrice();
                }
            }

            return (int) Math.round(total);

        } catch (Exception e) {

            e.printStackTrace();

            return 0;
        }
    }

    // =========================================================
    // FORMAT DISTANCE
    // =========================================================

    private String formatDistance(
            double distanceKm) {

        if (distanceKm <= 0) {
            return "Distance N/A";
        }

        return String.format(
                "%.1f KM",
                distanceKm
        );
    }

    // =========================================================
    // SAFE TEXT
    // =========================================================

    private String safeText(
            String value,
            String fallback) {

        if (value == null
                || value.trim().isEmpty()) {

            return fallback;
        }

        return value;
    }

    // =========================================================
    // GET TRIPS FOR DASHBOARD
    // =========================================================

    public List<Trip> getAllTrips() {

        loadTripData();

        return allTrips;
    }

    private void addTrip(
            String id,
            String date,
            String origin,
            String destination,
            String route,
            String vehicle,
            String cargo,
            String distance,
            int earnings,
            String status) {

        allTrips.add(
                new Trip(
                        id,
                        LocalDate.parse(date),
                        origin,
                        destination,
                        route,
                        vehicle,
                        cargo,
                        distance,
                        earnings,
                        status));
    }

    private String statusColor(
            String status) {

        switch (status) {

            case "Completed":
                return GREEN;

            case "Cancelled":
                return RED;

            case "In Progress":
                return ORANGE;

            default:
                return MUTED;
        }
    }

    // =========================================================
    // TRIP MODEL
    // =========================================================

    public static class Trip {

        public String tripId;
        public LocalDate dateValue;
        public String origin;
        public String destination;
        public String route;
        public String vehicle;
        public String cargo;
        public String distance;
        public int earnings;
        public String status;

        Trip(
                String tripId,
                LocalDate dateValue,
                String origin,
                String destination,
                String route,
                String vehicle,
                String cargo,
                String distance,
                int earnings,
                String status) {

            this.tripId = tripId;
            this.dateValue = dateValue;
            this.origin = origin;
            this.destination = destination;
            this.route = route;
            this.vehicle = vehicle;
            this.cargo = cargo;
            this.distance = distance;
            this.earnings = earnings;
            this.status = status;
        }

        javafx.beans.property.StringProperty tripIdProperty() {
            return new javafx.beans.property.SimpleStringProperty(
                    tripId);
        }

        javafx.beans.property.StringProperty dateProperty() {
            return new javafx.beans.property.SimpleStringProperty(
                    dateValue.toString());
        }

        javafx.beans.property.StringProperty routeProperty() {
            return new javafx.beans.property.SimpleStringProperty(
                    route);
        }

        javafx.beans.property.StringProperty vehicleProperty() {
            return new javafx.beans.property.SimpleStringProperty(
                    vehicle);
        }

        javafx.beans.property.StringProperty cargoProperty() {
            return new javafx.beans.property.SimpleStringProperty(
                    cargo);
        }

        javafx.beans.property.StringProperty distanceProperty() {
            return new javafx.beans.property.SimpleStringProperty(
                    distance);
        }

        javafx.beans.property.StringProperty earningsProperty() {
            return new javafx.beans.property.SimpleStringProperty(
                    "₹" + String.format("%,d", earnings));
        }

        javafx.beans.property.StringProperty statusProperty() {
            return new javafx.beans.property.SimpleStringProperty(
                    status);
        }
    }
}
