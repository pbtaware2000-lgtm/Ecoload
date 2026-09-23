package com.super_x.view.UserView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MatchedDrivers {

    // Shared UserSide palette, aligned with My Loads and Smart Load Advisor.
    private static final String GREEN = "#087A20";
    private static final String DARK_GREEN = "#075F1B";
    private static final String BACKGROUND = "#F6F7F5";
    private static final String TEXT = "#18352B";
    private static final String MUTED = "#71807A";
    private static final String BORDER = "#DFE9E4";

    private Scene matchedDriversScene;

    // =====================================================
    // LOCAL DRIVER DATA
    // =====================================================

    private final List<DriverData> allDrivers = new ArrayList<>();

    private final List<DriverData> filteredDrivers = new ArrayList<>();

    private int currentPage = 1;

    private final int driversPerPage = 4;

    private VBox driverContainer;

    private Label pageLabel;

    private Button previousButton;

    private Button nextButton;

    private TextField searchField;

    private ComboBox<String> scoreFilter;

    private ComboBox<String> truckFilter;

    private ComboBox<String> sortFilter;

    // =====================================================
    // SCENE
    // =====================================================

    public Scene getMatchedDriversScene() {

        loadLocalDrivers();

        filteredDrivers.clear();

        filteredDrivers.addAll(
                allDrivers);

        VBox main = new VBox(20);

        main.setPadding(
                new Insets(
                        28,
                        35,
                        28,
                        35));

        main.setStyle(
                "-fx-background-color: " + BACKGROUND + ";");

        // =====================================================
        // HEADER
        // =====================================================

        HBox header = new HBox();

        header.setAlignment(
                Pos.CENTER_LEFT);

        header.setPadding(
                new Insets(
                        0,
                        0,
                        2,
                        0));

        header.setStyle(
                "-fx-background-color: transparent;");

        VBox titleBox = new VBox(4);

        Label title = new Label(
                "Matched Drivers");

        title.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        36));

        title.setTextFill(
                Color.web(DARK_GREEN));

        Label subtitle = new Label(
                "View AI-matched drivers for your posted loads");

        subtitle.setFont(
                Font.font(
                        "Arial",
                        16));

        subtitle.setTextFill(
                Color.web("#666666"));

        titleBox.getChildren().addAll(
                title,
                subtitle);

        Region headerSpacer = new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS);

        header.getChildren().addAll(
                titleBox);

        main.getChildren().add(
                header);

        // =====================================================
        // ACTIVE LOAD CARD
        // =====================================================

        //HBox loadCard = createActiveLoadCard();

        // main.getChildren().add(
        //         loadCard);

        // =====================================================
        // MATCHED DRIVER TITLE
        // =====================================================

        Label matchedTitle = new Label(
                "AI Matched Drivers");

        matchedTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22));

        matchedTitle.setTextFill(
                Color.web(TEXT));

        matchedTitle.setPadding(
                new Insets(
                        2,
                        0,
                        0,
                        0));

        main.getChildren().add(
                matchedTitle);

        // =====================================================
        // SEARCH + FILTER BAR
        // =====================================================

        HBox filterBar = new HBox(12);

        filterBar.setAlignment(
                Pos.CENTER_LEFT);

        filterBar.setPadding(
                new Insets(
                        16,
                        18,
                        16,
                        18));

        filterBar.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 14;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-radius: 14;");

        // Search
        searchField = new TextField();

        searchField.setPromptText(
                "Search by Driver, Vehicle, or Route...");

        searchField.setPrefHeight(42);

        searchField.setPrefWidth(600);

        searchField.setStyle(
                "-fx-background-color: #F8FAFC;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-radius: 8;"
                        + "-fx-padding: 0 15 0 15;"
                        + "-fx-font-size: 13px;"
                        + "-fx-text-fill: " + TEXT + ";");

        // Match Score
        scoreFilter = new ComboBox<>();

        scoreFilter.getItems().addAll(
                "Match Score",
                "90% and above",
                "80% and above",
                "70% and above");

        scoreFilter.setValue(
                "Match Score");

        scoreFilter.setPrefHeight(42);

        scoreFilter.setPrefWidth(170);

        scoreFilter.setStyle(
                "-fx-background-color: #F8FAFC;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-radius: 8;"
                        + "-fx-font-size:13px;"
                        + "-fx-text-fill: " + TEXT + ";");

        // Truck Type
        truckFilter = new ComboBox<>();

        truckFilter.getItems().addAll(
                "Truck Type",
                "LPT",
                "Trailer",
                "Container",
                "Tanker");

        truckFilter.setValue(
                "Truck Type");

        truckFilter.setPrefHeight(42);

        truckFilter.setPrefWidth(170);

        truckFilter.setStyle(
                "-fx-background-color: #F8FAFC;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-radius: 8;"
                        + "-fx-font-size:13px;"
                        + "-fx-text-fill: " + TEXT + ";");

        // Sort
        sortFilter = new ComboBox<>();

        sortFilter.getItems().addAll(
                "Sort",
                "Highest Match",
                "Lowest Match",
                "Highest Rating",
                "Most Experience");

        sortFilter.setValue(
                "Sort");

        sortFilter.setPrefHeight(42);

        sortFilter.setPrefWidth(170);

        sortFilter.setStyle(
                "-fx-background-color: #F8FAFC;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-radius: 8;"
                        + "-fx-font-size:13px;"
                        + "-fx-text-fill: " + TEXT + ";");

        filterBar.getChildren().addAll(
                searchField,
                scoreFilter,
                truckFilter,
                sortFilter);

        main.getChildren().add(
                filterBar);

        // =====================================================
        // DRIVER CONTAINER
        // =====================================================

        driverContainer = new VBox(16);

        driverContainer.setPadding(
                new Insets(
                        5,
                        0,
                        5,
                        0));
        ScrollPane driverScrollPane = new ScrollPane(
                driverContainer);

        driverScrollPane.setFitToWidth(true);

        driverScrollPane.setPrefHeight(400);

        driverScrollPane.setStyle(
                "-fx-background-color: transparent;"
                        + "-fx-background: transparent;");

        VBox.setVgrow(
                driverScrollPane,
                Priority.ALWAYS);

        main.getChildren().add(
                driverScrollPane);

        // =====================================================
        // PAGINATION
        // =====================================================

        HBox pagination = createPagination();

        main.getChildren().add(
                pagination);

        // =====================================================
        // FILTER EVENTS
        // =====================================================

        searchField.textProperty()
                .addListener(
                        (obs, oldValue, newValue) -> applyFilters());

        scoreFilter.valueProperty()
                .addListener(
                        (obs, oldValue, newValue) -> applyFilters());

        truckFilter.valueProperty()
                .addListener(
                        (obs, oldValue, newValue) -> applyFilters());

        sortFilter.valueProperty()
                .addListener(
                        (obs, oldValue, newValue) -> applyFilters());

        refreshDriverCards();

        BorderPane mainContent = new BorderPane();
        mainContent.setTop(UserNavigation.createNavbar());
        mainContent.setCenter(main);

        BorderPane root = new BorderPane();
        root.setCenter(mainContent);
        root.setLeft(UserNavigation.createSidebar("Matched Drivers"));

        matchedDriversScene = new Scene(root, 1536, 750);

        return matchedDriversScene;
    }

    // =====================================================
    // ACTIVE LOAD CARD
    // =====================================================

    private HBox createActiveLoadCard() {

        HBox loadCard = new HBox(60);

        loadCard.setAlignment(
                Pos.CENTER_LEFT);

        loadCard.setPadding(
                new Insets(
                        14,
                        20,
                        14,
                        20));

        loadCard.setPrefHeight(68);

        loadCard.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 14;");

        // Load icon
        Label loadIcon = new Label("▣");

        loadIcon.setStyle(
                "-fx-background-color: #E8F5EA;"
                        + "-fx-background-radius: 8;"
                        + "-fx-text-fill: #087C2F;"
                        + "-fx-font-size: 15px;");

        loadIcon.setPadding(
                new Insets(7));

        // Load ID
        VBox loadIdBox = new VBox(1);

        Label activeLoad = new Label(
                "ACTIVE LOAD SELECTION");

        activeLoad.setStyle(
                "-fx-text-fill: #7B867F;"
                        + "-fx-font-size: 10px;"
                        + "-fx-font-weight: bold;");

        Label loadId = new Label("EL-9420");

        loadId.setStyle(
                "-fx-text-fill: #26332C;"
                        + "-fx-font-size: 15px;"
                        + "-fx-font-weight: bold;");

        loadIdBox.getChildren().addAll(
                activeLoad,
                loadId);

        // Divider
        Region divider = new Region();

        divider.setPrefWidth(1);

        divider.setPrefHeight(25);

        divider.setStyle(
                "-fx-background-color: #D8E5DC;");

        // Route
        VBox routeBox = new VBox(1);

        Label routeTitle = new Label("Route");

        routeTitle.setStyle(
                "-fx-text-fill: #7B867F;"
                        + "-fx-font-size: 10px;");

        Label route = new Label(
                "Pune → Nashik");

        route.setStyle(
                "-fx-text-fill: #26332C;"
                        + "-fx-font-size: 15px;"
                        + "-fx-font-weight: bold;");

        routeBox.getChildren().addAll(
                routeTitle,
                route);

        // Pickup Date
        VBox dateBox = new VBox(1);

        Label dateTitle = new Label(
                "Pickup Date");

        dateTitle.setStyle(
                "-fx-text-fill: #7B867F;"
                        + "-fx-font-size: 12px;"
                        + "-fx-font-weight: bold;");

        Label pickupDate = new Label(
                "Oct 25, 2023");

        pickupDate.setStyle(
                "-fx-text-fill: #26332C;"
                        + "-fx-font-size: 10px;");

        dateBox.getChildren().addAll(
                dateTitle,
                pickupDate);

        // Weight
        VBox weightBox = new VBox(1);

        Label weightTitle = new Label(
                "Total Weight");

        weightTitle.setStyle(
                "-fx-text-fill: #7B867F;"
                        + "-fx-font-size: 12px;"
                        + "-fx-font-weight: bold;");

        Label weight = new Label("6 Ton");

        weight.setStyle(
                "-fx-text-fill: #26332C;"
                        + "-fx-font-size: 10px;");

        weightBox.getChildren().addAll(
                weightTitle,
                weight);

        // Price
        VBox priceBox = new VBox(1);

        Label priceTitle = new Label(
                "Agreed Price");

        priceTitle.setStyle(
                "-fx-text-fill: #7B867F;"
                        + "-fx-font-size: 12px;"
                        + "-fx-font-weight: bold;");

        Label price = new Label("₹9,200");

        price.setStyle(
                "-fx-text-fill: #087C2F;"
                        + "-fx-font-size: 10px;"
                        + "-fx-font-weight: bold;");

        priceBox.getChildren().addAll(
                priceTitle,
                price);

        // Modify
        Button modifyLoad = new Button(
                "✎  Modify Load");

        modifyLoad.setPrefHeight(36);

        modifyLoad.setPadding(
                new Insets(
                        8,
                        18,
                        8,
                        18));

        applyGreenButtonStyle(
                modifyLoad);

        modifyLoad.setOnAction(
                event -> showInfo(
                        "Modify Load",
                        "Modify Load screen will be connected here."));

        // Add
        loadCard.getChildren().addAll(
                loadIcon,
                loadIdBox,
                divider,
                routeBox,
                dateBox,
                weightBox,
                priceBox,
                modifyLoad);

        return loadCard;
    }

    // =====================================================
    // LOCAL DRIVER DATA
    // =====================================================

    private void loadLocalDrivers() {

        allDrivers.clear();

        allDrivers.add(
                new DriverData(
                        "Sandeep Pawar",
                        96,
                        4.8,
                        "7 Years",
                        "LPT 2518",
                        "Pune",
                        "35 min"));

        allDrivers.add(
                new DriverData(
                        "Rahul Jadhav",
                        93,
                        4.7,
                        "6 Years",
                        "LPT 1618",
                        "Pimpri",
                        "48 min"));

        allDrivers.add(
                new DriverData(
                        "Amit Shinde",
                        89,
                        4.6,
                        "5 Years",
                        "Trailer",
                        "Pune",
                        "55 min"));

        allDrivers.add(
                new DriverData(
                        "Vikas Patil",
                        86,
                        4.5,
                        "4 Years",
                        "Container",
                        "Chakan",
                        "1 hr 10 min"));

        allDrivers.add(
                new DriverData(
                        "Rohit More",
                        83,
                        4.4,
                        "4 Years",
                        "Tanker",
                        "Nashik",
                        "1 hr 25 min"));

        allDrivers.add(
                new DriverData(
                        "Sachin Pawar",
                        81,
                        4.3,
                        "3 Years",
                        "LPT 2518",
                        "Mumbai",
                        "2 hr"));
    }

    // =====================================================
    // APPLY SEARCH + FILTER + SORT
    // =====================================================

    private void applyFilters() {

        String search = searchField
                .getText()
                .trim()
                .toLowerCase();

        String score = scoreFilter.getValue();

        String truck = truckFilter.getValue();

        String sort = sortFilter.getValue();

        List<DriverData> result = allDrivers
                .stream()
                .filter(
                        driver -> {

                            if (search.isEmpty()) {
                                return true;
                            }

                            return driver.name
                                    .toLowerCase()
                                    .contains(search)

                                    || driver.truckType
                                            .toLowerCase()
                                            .contains(search)

                                    || driver.location
                                            .toLowerCase()
                                            .contains(search)

                                    || "pune"
                                            .contains(search);
                        })
                .filter(
                        driver -> matchScoreFilter(
                                driver,
                                score))
                .filter(
                        driver -> truckFilterMatch(
                                driver,
                                truck))
                .collect(
                        Collectors.toList());

        // Sort
        // // SORT
        if ("Highest Match".equals(sort)) {

            result.sort(
                    Comparator.comparingInt(
                            (DriverData d) -> d.matchScore).reversed());

        } else if ("Lowest Match".equals(sort)) {

            result.sort(
                    Comparator.comparingInt(
                            (DriverData d) -> d.matchScore));

        } else if ("Highest Rating".equals(sort)) {

            result.sort(
                    Comparator.comparingDouble(
                            (DriverData d) -> d.rating).reversed());

        }

        filteredDrivers.clear();

        filteredDrivers.addAll(
                result);

        currentPage = 1;

        refreshDriverCards();
    }

    private boolean matchScoreFilter(
            DriverData driver,
            String filter) {

        if ("90% and above".equals(filter)) {
            return driver.matchScore >= 90;
        }

        if ("80% and above".equals(filter)) {
            return driver.matchScore >= 80;
        }

        if ("70% and above".equals(filter)) {
            return driver.matchScore >= 70;
        }

        return true;
    }

    private boolean truckFilterMatch(
            DriverData driver,
            String filter) {

        if ("Truck Type".equals(filter)) {
            return true;
        }

        return driver.truckType
                .toLowerCase()
                .contains(
                        filter.toLowerCase());
    }

    // =====================================================
    // DRIVER CARDS
    // =====================================================
    private void refreshDriverCards() {

        driverContainer.getChildren().clear();

        int totalDrivers = filteredDrivers.size();

        int totalPages = Math.max(
                1,
                (int) Math.ceil(
                        (double) totalDrivers
                                / driversPerPage));

        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int start = (currentPage - 1)
                * driversPerPage;

        int end = Math.min(
                start + driversPerPage,
                totalDrivers);

        if (totalDrivers == 0) {

            Label empty = new Label(
                    "No matching drivers found.");

            empty.setStyle(
                    "-fx-text-fill: #7B867F;"
                            + "-fx-font-size: 14px;");

            driverContainer.getChildren().add(
                    empty);

        } else {

            for (int i = start; i < end; i++) {

                DriverData driver = filteredDrivers.get(i);

                VBox card = createDriverCard(driver);

                driverContainer.getChildren().add(
                        card);
            }
        }

        pageLabel.setText(
                "Page "
                        + currentPage
                        + " of "
                        + totalPages);

        previousButton.setDisable(
                currentPage <= 1);

        nextButton.setDisable(
                currentPage >= totalPages);
    }

    // =====================================================
    // DRIVER CARD
    // =====================================================

    private VBox createDriverCard(
            DriverData driver) {

        VBox card = new VBox(16);

        card.setPadding(
                new Insets(20));

        card.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 18;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-radius: 18;");

        // Top row
        HBox top = new HBox();

        top.setAlignment(
                Pos.CENTER_LEFT);

        // Avatar
        Label avatar = new Label(
                getInitials(
                        driver.name));

        avatar.setMinSize(
                46,
                46);

        avatar.setPrefSize(
                46,
                46);

        avatar.setAlignment(
                Pos.CENTER);

        avatar.setStyle(
                "-fx-background-color: #E8F5EA;"
                        + "-fx-background-radius: 23;"
                        + "-fx-text-fill: " + GREEN + ";"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;");

        VBox nameBox = new VBox(4);

        Label name = new Label(
                driver.name);

        name.setStyle(
                "-fx-text-fill: " + TEXT + ";"
                        + "-fx-font-size: 18px;"
                        + "-fx-font-weight: bold;");

        Label truck = new Label(
                driver.truckType);

        truck.setStyle(
                "-fx-text-fill: " + MUTED + ";"
                        + "-fx-font-size: 14px;");

        nameBox.getChildren().addAll(
                name,
                truck);

        HBox driverIdentity = new HBox(
                12,
                avatar,
                nameBox);

        driverIdentity.setAlignment(
                Pos.CENTER_LEFT);

        Region topSpacer = new Region();

        HBox.setHgrow(
                topSpacer,
                Priority.ALWAYS);

        Label score = new Label(
                driver.matchScore
                        + "% Match");

        score.setStyle(
                "-fx-background-color: #DCFCE7;"
                        + "-fx-background-radius: 10;"
                        + "-fx-text-fill: " + DARK_GREEN + ";"
                        + "-fx-padding: 6 10 6 10;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;");

        top.getChildren().addAll(
                driverIdentity,
                topSpacer,
                score);

        card.getChildren().add(
                top);

        // Details
        HBox details = new HBox(36);

        details.setAlignment(
                Pos.CENTER_LEFT);

        details.getChildren().addAll(
                createDetail(
                        "★ Rating",
                        String.valueOf(
                                driver.rating)),
                createDetail(
                        "Experience",
                        driver.experience),
                createDetail(
                        "Vehicle",
                        driver.truckType),
                createDetail(
                        "Current Location",
                        driver.location),
                createDetail(
                        "ETA to Pickup",
                        driver.eta));

        card.getChildren().add(
                details);

        // Buttons
        HBox buttons = new HBox(10);

        buttons.setAlignment(
                Pos.CENTER_RIGHT);

        Button viewProfile = new Button(
                "View Profile");

        applyOutlineButtonStyle(
                viewProfile);

        Button assign = new Button(
                "Assign Driver");

        applyGreenButtonStyle(
                assign);

        viewProfile.setOnAction(
                event -> showDriverProfile(
                        driver));

        assign.setOnAction(
                event -> assignDriver(
                        driver));

        buttons.getChildren().addAll(
                viewProfile,
                assign);

        card.getChildren().add(
                buttons);

        // Hover
        card.setOnMouseEntered(
                event -> card.setStyle(
                        "-fx-background-color: white;"
                                + "-fx-background-radius: 18;"
                                + "-fx-border-color: " + GREEN + ";"
                                + "-fx-border-radius: 18;"
                                + "-fx-effect: dropshadow("
                                + "gaussian, "
                                + "rgba(0,0,0,0.12), "
                                + "10, "
                                + "0, "
                                + "0, "
                                + "4);"));

        card.setOnMouseExited(
                event -> card.setStyle(
                        "-fx-background-color: white;"
                                + "-fx-background-radius: 18;"
                                + "-fx-border-color: " + BORDER + ";"
                                + "-fx-border-radius: 18;"));

        return card;
    }

    // =====================================================
    // DRIVER DETAIL
    // =====================================================

    private VBox createDetail(
            String title,
            String value) {

        VBox box = new VBox(3);

        Label titleLabel = new Label(title);

        titleLabel.setStyle(
                "-fx-text-fill: " + MUTED + ";"
                        + "-fx-font-size: 14px;");

        Label valueLabel = new Label(value);

        valueLabel.setStyle(
                "-fx-text-fill: " + TEXT + ";"
                        + "-fx-font-size: 15px;"
                        + "-fx-font-weight: bold;");

        box.getChildren().addAll(
                titleLabel,
                valueLabel);

        return box;
    }

    // =====================================================
    // PAGINATION
    // =====================================================

    private HBox createPagination() {

        HBox pagination = new HBox(12);

        pagination.setAlignment(
                Pos.CENTER);

        previousButton = new Button(
                "Previous");

        nextButton = new Button(
                "Next");

        pageLabel = new Label(
                "Page 1");

        previousButton.setPrefHeight(40);

        nextButton.setPrefHeight(40);

        applyOutlineButtonStyle(
                previousButton);

        applyOutlineButtonStyle(
                nextButton);

        pageLabel.setStyle(
                "-fx-text-fill: " + TEXT + ";"
                        + "-fx-font-size: 14px;"
                        + "-fx-background-color: #E8F5EA;"
                        + "-fx-background-radius: 8;"
                        + "-fx-padding: 8 12;"
                        + "-fx-font-weight: bold;");

        previousButton.setOnAction(
                event -> {

                    if (currentPage > 1) {

                        currentPage--;

                        refreshDriverCards();
                    }
                });

        nextButton.setOnAction(
                event -> {

                    int totalPages = Math.max(
                            1,
                            (int) Math.ceil(
                                    (double) filteredDrivers.size()
                                            / driversPerPage));

                    if (currentPage < totalPages) {

                        currentPage++;

                        refreshDriverCards();
                    }
                });

        pagination.getChildren().addAll(
                previousButton,
                pageLabel,
                nextButton);

        return pagination;
    }

    // =====================================================
    // VIEW PROFILE
    // =====================================================

    private void showDriverProfile(
            DriverData driver) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION);

        alert.setTitle(
                "Driver Profile");

        alert.setHeaderText(
                driver.name);

        alert.setContentText(
                "AI Match Score : "
                        + driver.matchScore
                        + "%\n\n"

                        + "Rating : "
                        + driver.rating
                        + "\n\n"

                        + "Experience : "
                        + driver.experience
                        + "\n\n"

                        + "Vehicle : "
                        + driver.truckType
                        + "\n\n"

                        + "Current Location : "
                        + driver.location
                        + "\n\n"

                        + "ETA to Pickup : "
                        + driver.eta);

        alert.showAndWait();
    }

    // =====================================================
    // ASSIGN DRIVER
    // =====================================================

    private void assignDriver(
            DriverData driver) {

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION);

        confirmation.setTitle(
                "Assign Driver");

        confirmation.setHeaderText(
                "Assign Driver");

        confirmation.setContentText(
                "Assign "
                        + driver.name
                        + " to Load L026?");

        confirmation
                .showAndWait()
                .ifPresent(
                        result -> {

                            if (result == javafx.scene.control.ButtonType.OK) {

                                showInfo(
                                        "Driver Assigned",
                                        driver.name
                                                + " assigned to Load L026.");
                            }
                        });
    }

    // =====================================================
    // BUTTON STYLE
    // =====================================================

    private void applyGreenButtonStyle(
            Button button) {

        button.setStyle(
                "-fx-background-color: " + GREEN + ";"
                        + "-fx-background-radius: 8;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 10 16 10 16;");

        button.setOnMouseEntered(
                event -> button.setStyle(
                        "-fx-background-color: " + DARK_GREEN + ";"
                                + "-fx-background-radius: 8;"
                                + "-fx-text-fill: white;"
                                + "-fx-font-size: 14px;"
                                + "-fx-font-weight: bold;"
                                + "-fx-padding: 10 16 10 16;"));

        button.setOnMouseExited(
                event -> button.setStyle(
                        "-fx-background-color: " + GREEN + ";"
                                + "-fx-background-radius: 8;"
                                + "-fx-text-fill: white;"
                                + "-fx-font-size: 14px;"
                                + "-fx-font-weight: bold;"
                                + "-fx-padding: 10 16 10 16;"));
    }

    private void applyOutlineButtonStyle(
            Button button) {

        button.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: #BFD6C5;"
                        + "-fx-border-radius: 8;"
                        + "-fx-text-fill: " + DARK_GREEN + ";"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 10 16 10 16;");

        button.setOnMouseEntered(
                event -> button.setStyle(
                        "-fx-background-color: #E8F5EA;"
                                + "-fx-background-radius: 8;"
                                + "-fx-border-color: " + GREEN + ";"
                                + "-fx-border-radius: 8;"
                                + "-fx-text-fill: " + DARK_GREEN + ";"
                                + "-fx-font-size: 14px;"
                                + "-fx-font-weight: bold;"
                                + "-fx-padding: 10 16 10 16;"));

        button.setOnMouseExited(
                event -> button.setStyle(
                        "-fx-background-color: white;"
                                + "-fx-background-radius: 8;"
                                + "-fx-border-color: #BFD6C5;"
                                + "-fx-border-radius: 8;"
                                + "-fx-text-fill: " + DARK_GREEN + ";"
                                + "-fx-font-size: 14px;"
                                + "-fx-font-weight: bold;"
                                + "-fx-padding: 10 16 10 16;"));
    }

    // =====================================================
    // INFO ALERT
    // =====================================================

    private void showInfo(
            String title,
            String message) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    // =====================================================
    // INITIALS
    // =====================================================

    private String getInitials(
            String name) {

        String[] parts = name.split(" ");

        if (parts.length >= 2) {

            return String.valueOf(
                    parts[0].charAt(0))
                    +
                    String.valueOf(
                            parts[1].charAt(0));
        }

        return name
                .substring(0, 1)
                .toUpperCase();
    }

    // =====================================================
    // DRIVER MODEL
    // =====================================================

    private static class DriverData {

        String name;

        int matchScore;

        double rating;

        String experience;

        String truckType;

        String location;

        String eta;

        DriverData(
                String name,
                int matchScore,
                double rating,
                String experience,
                String truckType,
                String location,
                String eta) {

            this.name = name;

            this.matchScore = matchScore;

            this.rating = rating;

            this.experience = experience;

            this.truckType = truckType;

            this.location = location;

            this.eta = eta;
        }
    }
}
