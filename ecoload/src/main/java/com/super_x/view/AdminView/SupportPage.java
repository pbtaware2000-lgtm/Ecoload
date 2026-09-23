package com.super_x.view.AdminView;

import com.super_x.NavigationService;
import com.super_x.controller.admincontroller.SupportController;
import com.super_x.model.adminmodel.SupportTicket;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;
import java.util.Locale;

/**
 * EcoLoad Admin - Support Management Center
 *
 * Features:
 * - Firebase support ticket loading
 * - Search
 * - Reported By filter
 * - Status filter
 * - Category filter
 * - Priority filter
 * - Support ticket table
 * - Black text on selected table row
 * - Right-side ticket details drawer
 * - Assign ticket
 * - Change status
 * - Send response
 * - Mark resolved
 *
 * IMPORTANT:
 * This class does NOT create a Stage.
 * Main.java / NavigationService handles the application's only Stage.
 */
public class SupportPage {

        // ============================================================
        // NAVIGATION / CONTROLLER
        // ============================================================

        private NavigationService navigationService;

        private final SupportController supportController;

        // ============================================================
        // DATA
        // ============================================================

        private final ObservableList<SupportTicket> allTickets = FXCollections.observableArrayList();

        private final ObservableList<SupportTicket> filteredTickets = FXCollections.observableArrayList();

        private TableView<SupportTicket> ticketTable;

        // ============================================================
        // FILTERS
        // ============================================================

        private TextField searchField;

        private ComboBox<String> reportedByFilter;

        private ComboBox<String> statusFilter;

        private ComboBox<String> categoryFilter;

        private ComboBox<String> priorityFilter;

        // ============================================================
        // DRAWER
        // ============================================================

        private VBox ticketDrawer;

        private StackPane drawerOverlay;

        private Label drawerTicketId;

        private Label drawerSubject;

        private Label userNameLabel;

        private Label userTypeLabel;

        private Label phoneLabel;

        private Label shipmentLabel;

        private Label tripLabel;

        private Label vehicleLabel;

        private Label descriptionLabel;

        private ComboBox<String> drawerStatus;

        private ComboBox<String> drawerAssigned;

        private VBox timelineBox;

        private TextArea responseArea;

        private SupportTicket selectedTicket;

        // ============================================================
        // SUMMARY
        // ============================================================

        private Label totalTicketsLabel;

        private Label openTicketsLabel;

        private Label progressTicketsLabel;

        private Label resolvedTicketsLabel;

        // ============================================================
        // CONSTRUCTOR
        // ============================================================

        public SupportPage() {

                supportController = new SupportController();
        }

        // ============================================================
        // NAVIGATION SERVICE
        // ============================================================

        public void setNavigationService(
                        NavigationService navigationService) {

                this.navigationService = navigationService;
        }

        // ============================================================
        // MAIN CONTENT
        // ============================================================

        public VBox getContent() {

                StackPane root = createPageRoot();

                loadTicketsFromFirebase();

                return createRootVBox(root);
        }

        // ============================================================
        // SCENE
        // ============================================================

        public Scene getScene() {

                StackPane root = createPageRoot();

                loadTicketsFromFirebase();

                return new Scene(root);
        }

        // ============================================================
        // PAGE ROOT
        // ============================================================

        private StackPane createPageRoot() {

                VBox mainContent = new VBox(20);

                mainContent.setPadding(
                                new Insets(25));

                mainContent.setStyle(
                                "-fx-background-color: #F3FBF5;");

                Node header = createHeader();

                HBox summaryCards = createSummaryCards();

                VBox ticketsSection = createTicketsSection();

                mainContent.getChildren().addAll(
                                header,
                                summaryCards,
                                ticketsSection);

                VBox.setVgrow(
                                ticketsSection,
                                Priority.ALWAYS);

                ScrollPane scrollPane = new ScrollPane(mainContent);

                scrollPane.setFitToWidth(true);

                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                scrollPane.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.AS_NEEDED);

                scrollPane.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-background: transparent;");

                StackPane root = new StackPane(scrollPane);

                // --------------------------------------------------------
                // DRAWER OVERLAY
                // --------------------------------------------------------

                drawerOverlay = new StackPane();

                drawerOverlay.setPickOnBounds(false);

                createTicketDrawer();

                StackPane.setAlignment(
                                ticketDrawer,
                                Pos.CENTER_RIGHT);

                drawerOverlay.getChildren().add(
                                ticketDrawer);

                root.getChildren().add(
                                drawerOverlay);

                return root;
        }

        private VBox createRootVBox(
                        StackPane root) {

                VBox wrapper = new VBox(root);

                VBox.setVgrow(
                                root,
                                Priority.ALWAYS);

                return wrapper;
        }

        // ============================================================
        // HEADER
        // ============================================================

        private HBox createHeader() {

                Label title = new Label("Support Center");

                title.setStyle(
                                "-fx-font-size: 27px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #173B2B;");

                Label subtitle = new Label(
                                "Manage support tickets, user issues, and customer requests from one place.");

                subtitle.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: #638071;");

                VBox text = new VBox(
                                5,
                                title,
                                subtitle);

                HBox header = new HBox(text);

                header.setAlignment(
                                Pos.CENTER_LEFT);

                return header;
        }

        // ============================================================
        // SUMMARY CARDS
        // ============================================================

        private HBox createSummaryCards() {

                totalTicketsLabel = new Label("0");

                openTicketsLabel = new Label("0");

                progressTicketsLabel = new Label("0");

                resolvedTicketsLabel = new Label("0");

                updateSummaryCounts();

                HBox cards = new HBox(18);

                cards.getChildren().addAll(

                                createSummaryCard(
                                                "Total Tickets",
                                                totalTicketsLabel,
                                                "All support tickets"),

                                createSummaryCard(
                                                "Open Tickets",
                                                openTicketsLabel,
                                                "Need attention"),

                                createSummaryCard(
                                                "In Progress",
                                                progressTicketsLabel,
                                                "Currently being handled"),

                                createSummaryCard(
                                                "Resolved",
                                                resolvedTicketsLabel,
                                                "Successfully resolved"));

                return cards;
        }

        private VBox createSummaryCard(
                        String title,
                        Label numberLabel,
                        String description) {

                Label titleLabel = new Label(title);

                titleLabel.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #658070;");

                numberLabel.setStyle(
                                "-fx-font-size: 27px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #173B2B;");

                Label descriptionLabel = new Label(description);

                descriptionLabel.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #8AA092;");

                VBox card = new VBox(
                                8,
                                titleLabel,
                                numberLabel,
                                descriptionLabel);

                card.setPadding(
                                new Insets(18));

                card.setPrefHeight(125);

                card.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-border-color: #CDE8D5;" +
                                                "-fx-border-radius: 12;" +
                                                "-fx-cursor: hand;");

                HBox.setHgrow(
                                card,
                                Priority.ALWAYS);

                card.setOnMouseEntered(e -> {

                        card.setStyle(
                                        "-fx-background-color: #F0FDF4;" +
                                                        "-fx-background-radius: 12;" +
                                                        "-fx-border-color: #86D69C;" +
                                                        "-fx-border-radius: 12;" +
                                                        "-fx-cursor: hand;");

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(120),
                                        card);

                        scale.setToX(1.02);
                        scale.setToY(1.02);
                        scale.play();
                });

                card.setOnMouseExited(e -> {

                        card.setStyle(
                                        "-fx-background-color: white;" +
                                                        "-fx-background-radius: 12;" +
                                                        "-fx-border-color: #CDE8D5;" +
                                                        "-fx-border-radius: 12;" +
                                                        "-fx-cursor: hand;");

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(120),
                                        card);

                        scale.setToX(1);
                        scale.setToY(1);
                        scale.play();
                });

                return card;
        }

        private void updateSummaryCounts() {

                if (totalTicketsLabel == null) {
                        return;
                }

                long open = allTickets.stream()
                                .filter(t -> "Open".equalsIgnoreCase(
                                                safe(t.getStatus())))
                                .count();

                long progress = allTickets.stream()
                                .filter(t -> "In Progress".equalsIgnoreCase(
                                                safe(t.getStatus())))
                                .count();

                long resolved = allTickets.stream()
                                .filter(t -> "Resolved".equalsIgnoreCase(
                                                safe(t.getStatus())))
                                .count();

                totalTicketsLabel.setText(
                                String.valueOf(
                                                allTickets.size()));

                openTicketsLabel.setText(
                                String.valueOf(open));

                progressTicketsLabel.setText(
                                String.valueOf(progress));

                resolvedTicketsLabel.setText(
                                String.valueOf(resolved));
        }

        // ============================================================
        // TICKET SECTION
        // ============================================================

        private VBox createTicketsSection() {

                Label sectionTitle = new Label("Support Tickets");

                sectionTitle.setStyle(
                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #173B2B;");

                // ========================================================
                // SEARCH
                // ========================================================

                searchField = new TextField();

                searchField.setPromptText(
                                "Search tickets...");

                searchField.setPrefHeight(40);

                applyNormalTextFieldStyle();

                searchField.focusedProperty()
                                .addListener(
                                                (obs, oldValue, focused) -> {

                                                        if (focused) {

                                                                searchField.setStyle(
                                                                                "-fx-background-color: #F0FDF4;" +
                                                                                                "-fx-background-radius: 8;"
                                                                                                +
                                                                                                "-fx-border-color: #70C98A;"
                                                                                                +
                                                                                                "-fx-border-radius: 8;"
                                                                                                +
                                                                                                "-fx-padding: 0 12;" +
                                                                                                "-fx-text-fill: #172033;");

                                                        } else {

                                                                applyNormalTextFieldStyle();
                                                        }
                                                });

                searchField.textProperty()
                                .addListener(
                                                (obs, oldValue, newValue) -> filterTickets());

                // ========================================================
                // FILTERS
                // ========================================================

                reportedByFilter = createFilter(
                                "All",
                                "User",
                                "Driver");

                statusFilter = createFilter(
                                "All",
                                "Open",
                                "In Progress",
                                "Resolved");

                categoryFilter = createFilter(
                                "All Categories",

                                "Tracking",
                                "TAT / Delay",
                                "Payment",
                                "Trip Update",
                                "Technical",
                                "Data Issue",
                                "Assignment",

                                "Payment Issue",
                                "Technical Issue",
                                "Trip Issue",
                                "Vehicle Issue",
                                "Load Issue",
                                "Account Issue",
                                "Document Issue",

                                "Other");

                priorityFilter = createFilter(
                                "All",
                                "Low",
                                "Medium",
                                "High");

                // ========================================================
                // FILTER EVENTS
                // ========================================================

                reportedByFilter.setOnAction(
                                e -> updateCategoryOptions());

                statusFilter.setOnAction(
                                e -> filterTickets());

                categoryFilter.setOnAction(
                                e -> filterTickets());

                priorityFilter.setOnAction(
                                e -> filterTickets());

                // ========================================================
                // FILTER ROW
                // ========================================================

                HBox filters = new HBox(10);

                filters.setAlignment(
                                Pos.CENTER_LEFT);

                filters.getChildren().addAll(

                                searchField,

                                createFilterBox(
                                                "Reported By",
                                                reportedByFilter),

                                createFilterBox(
                                                "Status",
                                                statusFilter),

                                createFilterBox(
                                                "Category",
                                                categoryFilter),

                                createFilterBox(
                                                "Priority",
                                                priorityFilter));

                HBox.setHgrow(
                                searchField,
                                Priority.ALWAYS);

                // ========================================================
                // TABLE
                // ========================================================

                ticketTable = createTicketTable();

                ticketTable.setPrefHeight(400);

                ticketTable.setMinHeight(300);

                ticketTable.setMaxHeight(450);

                VBox section = new VBox(
                                15,
                                sectionTitle,
                                filters,
                                ticketTable);

                section.setPadding(
                                new Insets(20));

                section.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-border-color: #CDE8D5;" +
                                                "-fx-border-radius: 12;");

                return section;
        }

        private void applyNormalTextFieldStyle() {

                if (searchField == null) {
                        return;
                }

                searchField.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-border-color: #CDE8D5;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-padding: 0 12;" +
                                                "-fx-text-fill: #172033;");
        }

        // ============================================================
        // FILTER BOX
        // ============================================================

        private VBox createFilterBox(
                        String labelText,
                        ComboBox<String> combo) {

                Label label = new Label(labelText);

                label.setStyle(
                                "-fx-font-size: 11px;" +
                                                "-fx-text-fill: #658070;" +
                                                "-fx-font-weight: bold;");

                VBox box = new VBox(
                                3,
                                label,
                                combo);

                return box;
        }

        // ============================================================
        // CREATE FILTER
        // ============================================================

        private ComboBox<String> createFilter(
                        String first,
                        String... values) {

                ComboBox<String> combo = new ComboBox<>();

                ObservableList<String> items = FXCollections.observableArrayList();

                items.add(first);

                for (String value : values) {
                        items.add(value);
                }

                combo.setItems(items);

                combo.getSelectionModel()
                                .selectFirst();

                combo.setPrefHeight(40);

                combo.setMinWidth(125);

                applyNormalComboStyle(combo);

                combo.focusedProperty()
                                .addListener(
                                                (obs, oldValue, focused) -> {

                                                        if (focused) {

                                                                combo.setStyle(
                                                                                "-fx-background-color: #F0FDF4;" +
                                                                                                "-fx-border-color: #70C98A;"
                                                                                                +
                                                                                                "-fx-border-radius: 8;"
                                                                                                +
                                                                                                "-fx-background-radius: 8;"
                                                                                                +
                                                                                                "-fx-text-fill: #172033;");

                                                        } else {

                                                                applyNormalComboStyle(combo);
                                                        }
                                                });

                return combo;
        }

        private void applyNormalComboStyle(
                        ComboBox<String> combo) {

                combo.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #CDE8D5;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-text-fill: #172033;");
        }

        // ============================================================
        // CATEGORY OPTIONS
        // ============================================================

        private void updateCategoryOptions() {

                if (reportedByFilter == null
                                || categoryFilter == null) {

                        return;
                }

                String reportedBy = safe(
                                reportedByFilter.getValue());

                String currentCategory = categoryFilter.getValue();

                ObservableList<String> categories = FXCollections.observableArrayList();

                categories.add(
                                "All Categories");

                if ("User".equalsIgnoreCase(
                                reportedBy)) {

                        categories.addAll(

                                        "Tracking",
                                        "TAT / Delay",
                                        "Payment",
                                        "Trip Update",
                                        "Technical",
                                        "Data Issue",
                                        "Assignment",
                                        "Other");

                } else if ("Driver".equalsIgnoreCase(
                                reportedBy)) {

                        categories.addAll(

                                        "Payment Issue",
                                        "Technical Issue",
                                        "Trip Issue",
                                        "Vehicle Issue",
                                        "Load Issue",
                                        "Account Issue",
                                        "Document Issue",
                                        "Other");

                } else {

                        categories.addAll(

                                        "Tracking",
                                        "TAT / Delay",
                                        "Payment",
                                        "Trip Update",
                                        "Technical",
                                        "Data Issue",
                                        "Assignment",

                                        "Payment Issue",
                                        "Technical Issue",
                                        "Trip Issue",
                                        "Vehicle Issue",
                                        "Load Issue",
                                        "Account Issue",
                                        "Document Issue",

                                        "Other");
                }

                categoryFilter.setItems(
                                categories);

                if (currentCategory != null
                                && categories.contains(
                                                currentCategory)) {

                        categoryFilter.setValue(
                                        currentCategory);

                } else {

                        categoryFilter.getSelectionModel()
                                        .selectFirst();
                }

                filterTickets();
        }

        // ============================================================
        // TABLE
        // ============================================================

        private TableView<SupportTicket> createTicketTable() {

                TableView<SupportTicket> table = new TableView<>();

                table.setItems(
                                filteredTickets);

                table.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);

                table.setPlaceholder(
                                new Label(
                                                "No support tickets found."));

                table.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #BFE6CA;" +
                                                "-fx-border-radius: 8;");

                // ========================================================
                // ROW FACTORY
                // ========================================================

                table.setRowFactory(tv -> {

                        TableRow<SupportTicket> row = new TableRow<>();

                        updateRowStyle(
                                        row,
                                        false,
                                        false);

                        row.hoverProperty()
                                        .addListener(
                                                        (obs, oldValue, isHover) -> {

                                                                if (row.isSelected()) {

                                                                        updateRowStyle(
                                                                                        row,
                                                                                        true,
                                                                                        false);

                                                                } else if (isHover
                                                                                && !row.isEmpty()) {

                                                                        updateRowStyle(
                                                                                        row,
                                                                                        false,
                                                                                        true);

                                                                } else {

                                                                        updateRowStyle(
                                                                                        row,
                                                                                        false,
                                                                                        false);
                                                                }
                                                        });

                        row.selectedProperty()
                                        .addListener(
                                                        (obs,
                                                                        oldSelected,
                                                                        newSelected) -> {

                                                                updateRowStyle(
                                                                                row,
                                                                                newSelected,
                                                                                row.isHover());
                                                        });

                        row.emptyProperty()
                                        .addListener(
                                                        (obs,
                                                                        oldEmpty,
                                                                        newEmpty) -> {

                                                                if (newEmpty) {

                                                                        updateRowStyle(
                                                                                        row,
                                                                                        false,
                                                                                        false);
                                                                }
                                                        });

                        return row;
                });

                // ========================================================
                // TICKET ID
                // ========================================================

                TableColumn<SupportTicket, String> id = new TableColumn<>(
                                "Ticket ID");

                id.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "ticketId"));

                // ========================================================
                // USER
                // ========================================================

                TableColumn<SupportTicket, String> user = new TableColumn<>(
                                "Reported By");

                user.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "reportedBy"));

                // ========================================================
                // TYPE
                // ========================================================

                TableColumn<SupportTicket, String> userType = new TableColumn<>(
                                "Type");

                userType.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "userType"));

                // ========================================================
                // CATEGORY
                // ========================================================

                TableColumn<SupportTicket, String> category = new TableColumn<>(
                                "Category");

                category.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "issueType"));

                // ========================================================
                // SUBJECT
                // ========================================================

                TableColumn<SupportTicket, String> subject = new TableColumn<>(
                                "Subject");

                subject.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "subject"));

                // ========================================================
                // PRIORITY
                // ========================================================

                TableColumn<SupportTicket, String> priority = new TableColumn<>(
                                "Priority");

                priority.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "priority"));

                // ========================================================
                // STATUS
                // ========================================================

                TableColumn<SupportTicket, String> status = new TableColumn<>(
                                "Status");

                status.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "status"));

                // ========================================================
                // CREATED
                // ========================================================

                TableColumn<SupportTicket, String> created = new TableColumn<>(
                                "Created");

                created.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "created"));

                // ========================================================
                // ASSIGNED
                // ========================================================

                TableColumn<SupportTicket, String> assigned = new TableColumn<>(
                                "Assigned To");

                assigned.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "assignedTo"));

                // ========================================================
                // FORCE BLACK TEXT
                // ========================================================

                makeTableColumnTextBlack(id);
                makeTableColumnTextBlack(user);
                makeTableColumnTextBlack(userType);
                makeTableColumnTextBlack(category);
                makeTableColumnTextBlack(subject);
                makeTableColumnTextBlack(priority);
                makeTableColumnTextBlack(status);
                makeTableColumnTextBlack(created);
                makeTableColumnTextBlack(assigned);

                // ========================================================
                // ACTION
                // ========================================================

                TableColumn<SupportTicket, Void> action = new TableColumn<>(
                                "Action");

                action.setCellFactory(
                                column -> new TableCell<>() {

                                        private final Button viewButton = new Button("View");

                                        {

                                                applyViewButtonNormalStyle();

                                                viewButton.setOnMouseEntered(
                                                                e -> applyViewButtonHoverStyle());

                                                viewButton.setOnMouseExited(
                                                                e -> applyViewButtonNormalStyle());

                                                viewButton.setOnAction(
                                                                e -> {

                                                                        if (getIndex() < 0
                                                                                        || getIndex() >= getTableView()
                                                                                                        .getItems()
                                                                                                        .size()) {

                                                                                return;
                                                                        }

                                                                        SupportTicket ticket = getTableView()
                                                                                        .getItems()
                                                                                        .get(
                                                                                                        getIndex());

                                                                        openTicketDrawer(
                                                                                        ticket);
                                                                });
                                        }

                                        private void applyViewButtonNormalStyle() {

                                                viewButton.setStyle(
                                                                "-fx-background-color: #EAF8EE;" +
                                                                                "-fx-text-fill: #15803D;" +
                                                                                "-fx-font-weight: bold;" +
                                                                                "-fx-background-radius: 7;" +
                                                                                "-fx-padding: 7 14;" +
                                                                                "-fx-cursor: hand;");
                                        }

                                        private void applyViewButtonHoverStyle() {

                                                viewButton.setStyle(
                                                                "-fx-background-color: #15803D;" +
                                                                                "-fx-text-fill: white;" +
                                                                                "-fx-font-weight: bold;" +
                                                                                "-fx-background-radius: 7;" +
                                                                                "-fx-padding: 7 14;" +
                                                                                "-fx-cursor: hand;");
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

                                                        setGraphic(
                                                                        viewButton);

                                                        setAlignment(
                                                                        Pos.CENTER);
                                                }
                                        }
                                });

                table.getColumns().addAll(

                                id,
                                user,
                                userType,
                                category,
                                subject,
                                priority,
                                status,
                                created,
                                assigned,
                                action);

                                // Make table header text black
Platform.runLater(() -> {
    table.lookupAll(".column-header .label").forEach(node -> {
        if (node instanceof Label label) {
            label.setTextFill(Color.BLACK);
            label.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
        }
    });
});

                return table;
        }

        // ============================================================
        // ROW STYLE
        // ============================================================

        private void updateRowStyle(
                        TableRow<SupportTicket> row,
                        boolean selected,
                        boolean hover) {

                if (row.isEmpty()) {

                        row.setStyle(
                                        "-fx-background-color: white;");

                        return;
                }

                if (selected) {

                        row.setStyle(
                                        "-fx-background-color: #DCFCE7;" +
                                                        "-fx-text-fill: #172033;");

                } else if (hover) {

                        row.setStyle(
                                        "-fx-background-color: #F0FDF4;" +
                                                        "-fx-text-fill: #172033;");

                } else {

                        row.setStyle(
                                        "-fx-background-color: white;" +
                                                        "-fx-text-fill: #172033;");
                }
        }

        // ============================================================
        // FORCE TABLE CELL TEXT BLACK
        // ============================================================

        private void makeTableColumnTextBlack(
                        TableColumn<SupportTicket, String> column) {

                column.setCellFactory(
                                col -> {

                                        TableCell<SupportTicket, String> cell = new TableCell<SupportTicket, String>() {

                                                @Override
                                                protected void updateItem(
                                                                String item,
                                                                boolean empty) {

                                                        super.updateItem(
                                                                        item,
                                                                        empty);

                                                        if (empty) {

                                                                setText(null);

                                                        } else {

                                                                setText(
                                                                                safe(item));
                                                        }

                                                        /*
                                                         * IMPORTANT:
                                                         *
                                                         * This keeps table text BLACK
                                                         * even when the row is selected.
                                                         */
                                                        setTextFill(Color.BLACK);

                                                        setStyle(
                                                                        "-fx-text-fill: black;" +
                                                                                        "-fx-font-size: 13px;");
                                                }
                                        };

                                        return cell;
                                });
        }

        // ============================================================
        // FILTER TICKETS
        // ============================================================

        private void filterTickets() {

                String search = searchField == null
                                ? ""
                                : safe(
                                                searchField.getText())
                                                .trim()
                                                .toLowerCase(
                                                                Locale.ROOT);

                String reportedBy = reportedByFilter == null
                                ? "All"
                                : safe(
                                                reportedByFilter.getValue());

                String status = statusFilter == null
                                ? "All"
                                : safe(
                                                statusFilter.getValue());

                String category = categoryFilter == null
                                ? "All Categories"
                                : safe(
                                                categoryFilter.getValue());

                String priority = priorityFilter == null
                                ? "All"
                                : safe(
                                                priorityFilter.getValue());

                filteredTickets.clear();

                for (SupportTicket ticket : allTickets) {

                        if (ticket == null) {
                                continue;
                        }

                        String ticketId = safeLower(
                                        ticket.getTicketId());

                        String userName = safeLower(
                                        ticket.getReportedBy());

                        String userType = safeLower(
                                        ticket.getUserType());

                        String issueType = safeLower(
                                        ticket.getIssueType());

                        String subject = safeLower(
                                        ticket.getSubject());

                        boolean matchesSearch = search.isEmpty()
                                        || ticketId.contains(search)
                                        || userName.contains(search)
                                        || userType.contains(search)
                                        || issueType.contains(search)
                                        || subject.contains(search);

                        boolean matchesReportedBy = "All".equalsIgnoreCase(
                                        reportedBy)
                                        ||
                                        safe(
                                                        ticket.getUserType())
                                                        .equalsIgnoreCase(
                                                                        reportedBy);

                        boolean matchesStatus = "All".equalsIgnoreCase(
                                        status)
                                        ||
                                        safe(
                                                        ticket.getStatus())
                                                        .equalsIgnoreCase(
                                                                        status);

                        boolean matchesCategory = "All Categories".equalsIgnoreCase(
                                        category)
                                        ||
                                        safe(
                                                        ticket.getIssueType())
                                                        .equalsIgnoreCase(
                                                                        category);

                        boolean matchesPriority = "All".equalsIgnoreCase(
                                        priority)
                                        ||
                                        safe(
                                                        ticket.getPriority())
                                                        .equalsIgnoreCase(
                                                                        priority);

                        if (matchesSearch
                                        && matchesReportedBy
                                        && matchesStatus
                                        && matchesCategory
                                        && matchesPriority) {

                                filteredTickets.add(
                                                ticket);
                        }
                }

                if (ticketTable != null) {

                        ticketTable.refresh();
                }
        }

        // ============================================================
        // SAFE STRING
        // ============================================================

        private String safe(
                        String value) {

                return value == null
                                ? ""
                                : value;
        }

        private String safeLower(
                        String value) {

                return safe(value)
                                .toLowerCase(
                                                Locale.ROOT);
        }

        // ============================================================
        // FIREBASE LOADING
        // ============================================================

        private void loadTicketsFromFirebase() {

                new Thread(() -> {

                        try {

                                System.out.println(
                                                "Loading support tickets from Firebase...");

                                List<SupportTicket> tickets = supportController.getAllTickets();

                                Platform.runLater(() -> {

                                        allTickets.clear();

                                        if (tickets != null) {

                                                allTickets.addAll(
                                                                tickets);
                                        }

                                        filteredTickets.setAll(
                                                        allTickets);

                                        updateSummaryCounts();

                                        updateCategoryOptions();

                                        filterTickets();

                                        if (ticketTable != null) {

                                                ticketTable.refresh();
                                        }

                                        System.out.println(
                                                        "Support UI updated with "
                                                                        + allTickets.size()
                                                                        + " tickets.");
                                });

                        } catch (Exception e) {

                                e.printStackTrace();

                                Platform.runLater(() -> {

                                        showAlert(
                                                        "Firebase Error",
                                                        "Unable to load support tickets.\n\n"
                                                                        + safe(
                                                                                        e.getMessage()));
                                });
                        }

                }).start();
        }

        // ============================================================
        // RIGHT SIDE DRAWER
        // ============================================================

        private void createTicketDrawer() {

                ticketDrawer = new VBox();

                ticketDrawer.setPrefWidth(
                                470);

                ticketDrawer.setMinWidth(
                                470);

                ticketDrawer.setMaxWidth(
                                470);

                ticketDrawer.setMaxHeight(
                                Double.MAX_VALUE);

                ticketDrawer.setVisible(
                                false);

                ticketDrawer.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #CDE8D5;" +
                                                "-fx-border-width: 1 0 1 1;");

                ticketDrawer.setEffect(
                                new DropShadow(
                                                20,
                                                Color.rgb(
                                                                0,
                                                                0,
                                                                0,
                                                                0.18)));

                createDrawerContent();
        }

        // ============================================================
        // DRAWER CONTENT
        // ============================================================

        private void createDrawerContent() {

                HBox header = new HBox();

                header.setPadding(
                                new Insets(
                                                18,
                                                20,
                                                18,
                                                20));

                header.setAlignment(
                                Pos.CENTER_LEFT);

                drawerTicketId = new Label(
                                "Support Ticket");

                drawerTicketId.setStyle(
                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #173B2B;");

                Button closeButton = new Button("✕");

                closeButton.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-text-fill: #64748B;" +
                                                "-fx-cursor: hand;");

                closeButton.setOnAction(
                                e -> closeTicketDrawer());

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                header.getChildren().addAll(
                                drawerTicketId,
                                spacer,
                                closeButton);

                Separator headerSeparator = new Separator();

                VBox content = new VBox(18);

                content.setPadding(
                                new Insets(20));

                drawerSubject = new Label();

                drawerSubject.setWrapText(
                                true);

                drawerSubject.setStyle(
                                "-fx-font-size: 19px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #173B2B;");

                content.getChildren().add(
                                drawerSubject);

                content.getChildren().add(
                                createUserInformation());

                content.getChildren().add(
                                createDescriptionSection());

                content.getChildren().add(
                                createAdminControls());

                content.getChildren().add(
                                createTimelineSection());

                content.getChildren().add(
                                createResponseSection());

                Label responseTime = new Label(
                                "Response time: Usually within 2–4 hours");

                responseTime.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #7B8494;");

                content.getChildren().add(
                                responseTime);

                ScrollPane scrollPane = new ScrollPane(content);

                scrollPane.setFitToWidth(
                                true);

                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                scrollPane.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background: white;");

                VBox.setVgrow(
                                scrollPane,
                                Priority.ALWAYS);

                ticketDrawer.getChildren().clear();

                ticketDrawer.getChildren().addAll(
                                header,
                                headerSeparator,
                                scrollPane);
        }

        // ============================================================
        // USER INFORMATION
        // ============================================================

        private VBox createUserInformation() {

                Label title = sectionLabel(
                                "User Information");

                userNameLabel = infoLabel();

                userTypeLabel = infoLabel();

                phoneLabel = infoLabel();

                shipmentLabel = infoLabel();

                tripLabel = infoLabel();

                vehicleLabel = infoLabel();

                GridPane grid = new GridPane();

                grid.setHgap(15);

                grid.setVgap(10);

                addInfoRow(
                                grid,
                                0,
                                "User:",
                                userNameLabel);

                addInfoRow(
                                grid,
                                1,
                                "Type:",
                                userTypeLabel);

                addInfoRow(
                                grid,
                                2,
                                "Phone:",
                                phoneLabel);

                addInfoRow(
                                grid,
                                3,
                                "Shipment ID:",
                                shipmentLabel);

                addInfoRow(
                                grid,
                                4,
                                "Trip ID:",
                                tripLabel);

                addInfoRow(
                                grid,
                                5,
                                "Vehicle No:",
                                vehicleLabel);

                VBox box = new VBox(
                                12,
                                title,
                                grid);

                return createSectionBox(
                                box);
        }

        private void addInfoRow(
                        GridPane grid,
                        int row,
                        String key,
                        Label value) {

                Label keyLabel = new Label(key);

                keyLabel.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #7B8494;");

                grid.add(
                                keyLabel,
                                0,
                                row);

                grid.add(
                                value,
                                1,
                                row);
        }

        // ============================================================
        // DESCRIPTION
        // ============================================================

        private VBox createDescriptionSection() {

                Label title = sectionLabel(
                                "Issue Description");

                descriptionLabel = new Label();

                descriptionLabel.setWrapText(
                                true);

                descriptionLabel.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #4B5563;" +
                                                "-fx-line-spacing: 3px;");

                VBox box = new VBox(
                                10,
                                title,
                                descriptionLabel);

                return createSectionBox(
                                box);
        }

        // ============================================================
        // ADMIN CONTROLS
        // ============================================================

        private VBox createAdminControls() {

                Label title = sectionLabel(
                                "Admin Actions");

                drawerStatus = new ComboBox<>();

                drawerStatus.setItems(
                                FXCollections.observableArrayList(
                                                "Open",
                                                "In Progress",
                                                "Resolved"));

                drawerStatus.setPrefHeight(38);

                drawerAssigned = new ComboBox<>();

                drawerAssigned.setItems(
                                FXCollections.observableArrayList(
                                                "Unassigned",
                                                "Admin Support",
                                                "Admin",
                                                "Support Team",
                                                "Technical Team",
                                                "Payment Team"));

                drawerAssigned.setPrefHeight(38);

                HBox statusRow = createControlRow(
                                "Status",
                                drawerStatus);

                HBox assignedRow = createControlRow(
                                "Assigned To",
                                drawerAssigned);

                drawerStatus.setOnAction(
                                e -> {

                                        if (selectedTicket == null) {
                                                return;
                                        }

                                        String newStatus = drawerStatus.getValue();

                                        if (newStatus == null
                                                        || newStatus.trim().isEmpty()) {

                                                return;
                                        }

                                        selectedTicket.setStatus(
                                                        newStatus);

                                        ticketTable.refresh();

                                        updateSummaryCounts();

                                        updateTimeline();
                                });

                drawerAssigned.setOnAction(
                                e -> {

                                        if (selectedTicket == null) {
                                                return;
                                        }

                                        String assigned = drawerAssigned.getValue();

                                        if (assigned == null
                                                        || assigned.trim().isEmpty()) {

                                                assigned = "Unassigned";
                                        }

                                        /*
                                         * CORRECT:
                                         *
                                         * setAssignedTo() receives the
                                         * assigned person/team.
                                         *
                                         * DO NOT pass getCreatedTime()
                                         * here.
                                         */
                                        selectedTicket.setAssignedTo(
                                                        assigned);

                                        ticketTable.refresh();

                                        updateTimeline();
                                });

                VBox box = new VBox(
                                12,
                                title,
                                statusRow,
                                assignedRow);

                return createSectionBox(
                                box);
        }

        // ============================================================
        // CONTROL ROW
        // ============================================================

        private HBox createControlRow(
                        String title,
                        ComboBox<String> combo) {

                Label label = new Label(title);

                label.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #5F6878;" +
                                                "-fx-font-weight: bold;");

                combo.setPrefWidth(
                                220);

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                HBox row = new HBox(
                                10,
                                label,
                                spacer,
                                combo);

                row.setAlignment(
                                Pos.CENTER_LEFT);

                return row;
        }

        // ============================================================
        // TIMELINE
        // ============================================================

        private VBox createTimelineSection() {

                Label title = sectionLabel(
                                "Support Action Timeline");

                timelineBox = new VBox(12);

                VBox box = new VBox(
                                12,
                                title,
                                timelineBox);

                return createSectionBox(
                                box);
        }

        private void updateTimeline() {

                if (timelineBox == null) {
                        return;
                }

                timelineBox.getChildren()
                                .clear();

                if (selectedTicket == null) {
                        return;
                }

                // --------------------------------------------------------
                // SUBMITTED
                // --------------------------------------------------------

                addTimelineItem(
                                "✓",
                                "Ticket Submitted",
                                safe(
                                                selectedTicket.getCreatedTime()));

                // --------------------------------------------------------
                // ASSIGNED
                // --------------------------------------------------------

                String assignedTo = safe(
                                selectedTicket.getAssignedTo());

                if (!assignedTo.isEmpty()
                                && !assignedTo.equalsIgnoreCase(
                                                "Unassigned")) {

                        addTimelineItem(
                                        "✓",
                                        "Ticket Assigned",
                                        assignedTo);
                }

                // --------------------------------------------------------
                // IN PROGRESS
                // --------------------------------------------------------

                String status = safe(
                                selectedTicket.getStatus());

                if (status.equalsIgnoreCase(
                                "In Progress")) {

                        addTimelineItem(
                                        "●",
                                        "Under Review",
                                        "Currently");
                }

                // --------------------------------------------------------
                // RESOLVED
                // --------------------------------------------------------

                if (status.equalsIgnoreCase(
                                "Resolved")) {

                        addTimelineItem(
                                        "✓",
                                        "Marked Resolved",
                                        "Completed");
                }
        }

        private void addTimelineItem(
                        String icon,
                        String action,
                        String time) {

                Label iconLabel = new Label(icon);

                iconLabel.setStyle(
                                "-fx-font-size: 15px;" +
                                                "-fx-text-fill: #16A34A;" +
                                                "-fx-font-weight: bold;");

                Label actionLabel = new Label(action);

                actionLabel.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #374151;");

                Label timeLabel = new Label(
                                safe(time));

                timeLabel.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #8A93A3;");

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                HBox row = new HBox(
                                10,
                                iconLabel,
                                actionLabel,
                                spacer,
                                timeLabel);

                row.setAlignment(
                                Pos.CENTER_LEFT);

                timelineBox.getChildren()
                                .add(row);
        }

        // ============================================================
        // RESPONSE
        // ============================================================

        private VBox createResponseSection() {

                Label title = sectionLabel(
                                "Admin Response");

                responseArea = new TextArea();

                responseArea.setPromptText(
                                "Write your response here...");

                responseArea.setWrapText(
                                true);

                responseArea.setPrefRowCount(
                                5);

                responseArea.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #CDE8D5;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #172033;");

                Button sendButton = createPrimaryButton(
                                "Send Response");

                sendButton.setOnAction(
                                e -> sendResponse());

                Button resolvedButton = createSuccessButton(
                                "Mark Resolved");

                resolvedButton.setOnAction(
                                e -> markResolved());

                HBox buttons = new HBox(
                                8,
                                sendButton,
                                resolvedButton);

                buttons.setAlignment(
                                Pos.CENTER_LEFT);

                VBox box = new VBox(
                                10,
                                title,
                                responseArea,
                                buttons);

                return createSectionBox(
                                box);
        }

        // ============================================================
        // SEND RESPONSE
        // ============================================================

        private void sendResponse() {

                if (selectedTicket == null) {

                        showAlert(
                                        "No Ticket Selected",
                                        "Please select a support ticket first.");

                        return;
                }

                String response = safe(
                                responseArea.getText()).trim();

                if (response.isEmpty()) {

                        showAlert(
                                        "Response Required",
                                        "Please write a response before sending.");

                        return;
                }

                // --------------------------------------------------------
                // CHANGE STATUS
                // --------------------------------------------------------

                selectedTicket.setStatus(
                                "In Progress");

                // --------------------------------------------------------
                // ASSIGN IF UNASSIGNED
                // --------------------------------------------------------

                if ("Unassigned".equalsIgnoreCase(
                                safe(
                                                selectedTicket.getAssignedTo()))) {

                        selectedTicket.setAssignedTo(
                                        "Admin Support");
                }

                responseArea.clear();

                drawerStatus.setValue(
                                "In Progress");

                drawerAssigned.setValue(
                                safe(
                                                selectedTicket.getAssignedTo()));

                updateTimeline();

                ticketTable.refresh();

                updateSummaryCounts();

                showAlert(
                                "Response Sent",
                                "Your response has been sent successfully.");
        }

        // ============================================================
        // MARK RESOLVED
        // ============================================================

        private void markResolved() {

                if (selectedTicket == null) {

                        showAlert(
                                        "No Ticket Selected",
                                        "Please select a support ticket first.");

                        return;
                }

                selectedTicket.setStatus(
                                "Resolved");

                drawerStatus.setValue(
                                "Resolved");

                ticketTable.refresh();

                updateSummaryCounts();

                updateTimeline();

                showAlert(
                                "Ticket Resolved",
                                safe(
                                                selectedTicket.getTicketId())
                                                + " has been marked as resolved.");
        }

        // ============================================================
        // OPEN DRAWER
        // ============================================================

        private void openTicketDrawer(
                        SupportTicket ticket) {

                if (ticket == null) {
                        return;
                }

                selectedTicket = ticket;

                // --------------------------------------------------------
                // TICKET ID
                // --------------------------------------------------------

                drawerTicketId.setText(
                                "Support Ticket "
                                                + safe(
                                                                ticket.getTicketId()));

                // --------------------------------------------------------
                // SUBJECT
                // --------------------------------------------------------

                drawerSubject.setText(
                                safe(
                                                ticket.getSubject()));

                // --------------------------------------------------------
                // USER INFORMATION
                // --------------------------------------------------------

                userNameLabel.setText(
                                safe(
                                                ticket.getUserName()));

                userTypeLabel.setText(
                                safe(
                                                ticket.getUserType()));

                phoneLabel.setText(
                                safe(
                                                ticket.getPhone()));

                shipmentLabel.setText(
                                safe(
                                                ticket.getShipmentId()));

                tripLabel.setText(
                                safe(
                                                ticket.getTripId()));

                vehicleLabel.setText(
                                safe(
                                                ticket.getVehicleNo()));

                // --------------------------------------------------------
                // DESCRIPTION
                // --------------------------------------------------------

                descriptionLabel.setText(
                                safe(
                                                ticket.getDescription()));

                // --------------------------------------------------------
                // STATUS
                // --------------------------------------------------------

                String status = safe(
                                ticket.getStatus());

                if (status.isEmpty()) {

                        status = "Open";
                }

                drawerStatus.setValue(
                                status);

                // --------------------------------------------------------
                // ASSIGNED
                // --------------------------------------------------------

                String assigned = safe(
                                ticket.getAssignedTo());

                if (assigned.isEmpty()) {

                        assigned = "Unassigned";
                }

                drawerAssigned.setValue(
                                assigned);

                // --------------------------------------------------------
                // RESPONSE
                // --------------------------------------------------------

                responseArea.clear();

                // --------------------------------------------------------
                // TIMELINE
                // --------------------------------------------------------

                updateTimeline();

                // --------------------------------------------------------
                // SHOW DRAWER
                // --------------------------------------------------------

                ticketDrawer.setVisible(
                                true);

                ticketDrawer.applyCss();

                ticketDrawer.setTranslateX(
                                ticketDrawer.getPrefWidth());

                TranslateTransition transition = new TranslateTransition(
                                Duration.millis(250),
                                ticketDrawer);

                transition.setToX(0);

                transition.play();
        }

        // ============================================================
        // CLOSE DRAWER
        // ============================================================

        private void closeTicketDrawer() {

                if (ticketDrawer == null
                                || !ticketDrawer.isVisible()) {

                        return;
                }

                TranslateTransition transition = new TranslateTransition(
                                Duration.millis(200),
                                ticketDrawer);

                transition.setToX(
                                ticketDrawer.getPrefWidth());

                transition.setOnFinished(
                                e -> {

                                        ticketDrawer.setVisible(
                                                        false);

                                        ticketDrawer.setTranslateX(
                                                        0);

                                        selectedTicket = null;
                                });

                transition.play();
        }

        // ============================================================
        // UI HELPERS
        // ============================================================

        private VBox createSectionBox(
                        VBox content) {

                content.setPadding(
                                new Insets(16));

                content.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: #CDE8D5;" +
                                                "-fx-border-radius: 10;");

                return content;
        }

        private Label sectionLabel(
                        String text) {

                Label label = new Label(text);

                label.setStyle(
                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #173B2B;");

                return label;
        }

        private Label infoLabel() {

                Label label = new Label();

                label.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #374151;");

                return label;
        }

        // ============================================================
        // PRIMARY BUTTON
        // ============================================================

        private Button createPrimaryButton(
                        String text) {

                Button button = new Button(text);

                button.setStyle(
                                "-fx-background-color: #16A34A;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-padding: 9 15;" +
                                                "-fx-cursor: hand;");

                button.setOnMouseEntered(
                                e -> button.setStyle(
                                                "-fx-background-color: #15803D;" +
                                                                "-fx-text-fill: white;" +
                                                                "-fx-font-weight: bold;" +
                                                                "-fx-background-radius: 7;" +
                                                                "-fx-padding: 9 15;" +
                                                                "-fx-cursor: hand;"));

                button.setOnMouseExited(
                                e -> button.setStyle(
                                                "-fx-background-color: #16A34A;" +
                                                                "-fx-text-fill: white;" +
                                                                "-fx-font-weight: bold;" +
                                                                "-fx-background-radius: 7;" +
                                                                "-fx-padding: 9 15;" +
                                                                "-fx-cursor: hand;"));

                return button;
        }

        // ============================================================
        // SUCCESS BUTTON
        // ============================================================

        private Button createSuccessButton(
                        String text) {

                Button button = new Button(text);

                button.setStyle(
                                "-fx-background-color: #15803D;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-padding: 9 12;" +
                                                "-fx-cursor: hand;");

                button.setOnMouseEntered(
                                e -> button.setStyle(
                                                "-fx-background-color: #166534;" +
                                                                "-fx-text-fill: white;" +
                                                                "-fx-font-weight: bold;" +
                                                                "-fx-background-radius: 7;" +
                                                                "-fx-padding: 9 12;" +
                                                                "-fx-cursor: hand;"));

                button.setOnMouseExited(
                                e -> button.setStyle(
                                                "-fx-background-color: #15803D;" +
                                                                "-fx-text-fill: white;" +
                                                                "-fx-font-weight: bold;" +
                                                                "-fx-background-radius: 7;" +
                                                                "-fx-padding: 9 12;" +
                                                                "-fx-cursor: hand;"));

                return button;
        }

        // ============================================================
        // ALERT
        // ============================================================

        private void showAlert(
                        String title,
                        String message) {

                Alert alert = new Alert(
                                Alert.AlertType.INFORMATION);

                alert.setTitle(
                                title);

                alert.setHeaderText(
                                null);

                alert.setContentText(
                                message);

                alert.showAndWait();
        }
}
