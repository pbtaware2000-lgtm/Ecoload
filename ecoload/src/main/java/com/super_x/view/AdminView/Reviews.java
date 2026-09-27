package com.super_x.view.AdminView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.super_x.NavigationService;
import com.super_x.config.FirebaseConfig;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Reviews {

    private NavigationService navigationService;

    public void setNavigationService(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    // ============================================================
    // COLORS
    // ============================================================

    private static final String DARK_GREEN = "#123D2A";
    private static final String CLICK_GREEN = "#1F7A4D";
    private static final String HOVER_GREEN = "#D8EBDD";

    // UPDATED BACKGROUND COLOR
    private static final String LIGHT_BG = "#E6F1E8";

    // UPDATED BORDER COLOR
    private static final String BORDER = "#C9DCCF";

    private static final String TEXT_DARK = "#1D2923";
    private static final String TEXT_GRAY = "#6B756F";
    private static final String WHITE = "#FFFFFF";

    private Button activeButton;
    private VBox reviewsTable;
    private VBox reviewRows;

    private BorderPane root;

    private final List<ReviewData> firebaseReviews =
        new ArrayList<>();

private Label overallRatingValue;
private Label totalReviewsValue;
private Label positiveReviewsValue;

    // ============================================================
    // GET CONTENT
    // ============================================================

    public VBox getContent() {

        VBox content = createReviewsContent();

        VBox.setVgrow(
                content,
                Priority.ALWAYS
        );

        return content;
    }

    // ============================================================
    // GET REVIEWS SCENE
    // ============================================================

    public Scene getReviewsScene() {

        root = new BorderPane();

        root.setStyle(
                "-fx-background-color: " + LIGHT_BG + ";"
        );

        VBox sidebar = createSidebar();

        VBox mainArea = new VBox();

        HBox topBar = createTopBar();

        VBox content = createReviewsContent();

        VBox.setVgrow(
                content,
                Priority.ALWAYS
        );

        mainArea.getChildren().addAll(
                topBar,
                content
        );

        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(root);
    }

    // ============================================================
    // SIDEBAR
    // ============================================================

    private VBox createSidebar() {

        VBox sidebar = new VBox();

        sidebar.setPrefWidth(250);
        sidebar.setMinWidth(250);
        sidebar.setMaxWidth(250);

        sidebar.setPadding(
                new Insets(25, 14, 18, 14)
        );

        sidebar.setSpacing(8);

        sidebar.setStyle(
                "-fx-background-color: " + DARK_GREEN + ";"
        );

        VBox logoBox = createLogo();

        VBox menu = new VBox(7);

        Button dashboardButton =
                createMenuButton(
                        "▣",
                        "Dashboard",
                        false
                );

        Button driversButton =
                createMenuButton(
                        "♟",
                        "Drivers & Users",
                        false
                );

        Button pendingButton =
                createMenuButton(
                        "↳",
                        "Pending Drivers",
                        false
                );

        Button loadsButton =
                createMenuButton(
                        "♧",
                        "Loads & Trips",
                        false
                );

        Button analyticsButton =
                createMenuButton(
                        "▥",
                        "Analytics",
                        false
                );

        Button reviewsButton =
                createMenuButton(
                        "★",
                        "Reviews",
                        true
                );

        Button supportButton =
                createMenuButton(
                        "♧",
                        "Support",
                        false
                );

        menu.getChildren().addAll(
                dashboardButton,
                driversButton,
                pendingButton,
                loadsButton,
                analyticsButton,
                reviewsButton,
                supportButton
        );

        activeButton = reviewsButton;

        dashboardButton.setOnAction(e ->
                setActiveButton(dashboardButton)
        );

        driversButton.setOnAction(e ->
                setActiveButton(driversButton)
        );

        pendingButton.setOnAction(e ->
                setActiveButton(pendingButton)
        );

        loadsButton.setOnAction(e ->
                setActiveButton(loadsButton)
        );

        analyticsButton.setOnAction(e ->
                setActiveButton(analyticsButton)
        );

        reviewsButton.setOnAction(e ->
                setActiveButton(reviewsButton)
        );

        supportButton.setOnAction(e ->
                setActiveButton(supportButton)
        );

        Region spacer = new Region();

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS
        );

        Button logoutButton =
                createMenuButton(
                        "↪",
                        "Logout",
                        false
                );

        logoutButton.setOnAction(e -> {
            System.out.println("Logout clicked");
        });

        sidebar.getChildren().addAll(
                logoBox,
                createVerticalSpace(25),
                menu,
                spacer,
                logoutButton
        );

        return sidebar;
    }

    // ============================================================
    // LOGO
    // ============================================================

    private VBox createLogo() {

        VBox box = new VBox(2);

        box.setPadding(
                new Insets(0, 8, 0, 8)
        );

        Label logo = new Label("EcoLoad");

        logo.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 23px;" +
                "-fx-font-weight: bold;"
        );

        Label subtitle = new Label("PRECISION");

        subtitle.setStyle(
                "-fx-text-fill: #C5D8CE;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;"
        );

        Label subtitle2 = new Label("LOGISTICS");

        subtitle2.setStyle(
                "-fx-text-fill: #C5D8CE;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;"
        );

        box.getChildren().addAll(
                logo,
                subtitle,
                subtitle2
        );

        return box;
    }

    // ============================================================
    // SIDEBAR BUTTON
    // ============================================================

    private Button createMenuButton(
            String icon,
            String text,
            boolean active
    ) {

        Label iconLabel = new Label(icon);

        iconLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 17px;"
        );

        Label textLabel = new Label(text);

        textLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;"
        );

        HBox content = new HBox(
                13,
                iconLabel,
                textLabel
        );

        content.setAlignment(
                Pos.CENTER_LEFT
        );

        Button button = new Button();

        button.setGraphic(content);

        button.setAlignment(
                Pos.CENTER_LEFT
        );

        button.setMaxWidth(
                Double.MAX_VALUE
        );

        button.setPrefHeight(43);

        button.setPadding(
                new Insets(
                        0,
                        12,
                        0,
                        12
                )
        );

        button.setCursor(Cursor.HAND);

        if (active) {

            button.setStyle(
                    "-fx-background-color: " +
                    CLICK_GREEN +
                    ";" +
                    "-fx-background-radius: 9;"
            );

        } else {

            button.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 9;"
            );
        }

        button.setOnMouseEntered(e -> {

            if (button != activeButton) {

                button.setStyle(
                        "-fx-background-color: #20533B;" +
                        "-fx-background-radius: 9;"
                );
            }

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(100),
                            button
                    );

            scale.setToX(1.02);
            scale.setToY(1.02);
            scale.play();
        });

        button.setOnMouseExited(e -> {

            if (button == activeButton) {

                button.setStyle(
                        "-fx-background-color: " +
                        CLICK_GREEN +
                        ";" +
                        "-fx-background-radius: 9;"
                );

            } else {

                button.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 9;"
                );
            }

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(100),
                            button
                    );

            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        return button;
    }

    // ============================================================
    // ACTIVE BUTTON
    // ============================================================

    private void setActiveButton(Button button) {

        if (activeButton != null) {

            activeButton.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 9;"
            );
        }

        activeButton = button;

        activeButton.setStyle(
                "-fx-background-color: " +
                CLICK_GREEN +
                ";" +
                "-fx-background-radius: 9;"
        );
    }

    // ============================================================
    // TOP BAR
    // ============================================================

    private HBox createTopBar() {

        return AdminTopBar
                .create(navigationService)
                .build();
    }

    // ============================================================
    // REVIEWS CONTENT
    // ============================================================

private VBox createReviewsContent() {

    VBox content = new VBox();

    content.setPadding(
            new Insets(
                    28,
                    30,
                    20,
                    30
            )
    );

    content.setSpacing(18);

    content.setStyle(
            "-fx-background-color: " +
            LIGHT_BG +
            ";"
    );

    // ========================================================
    // TITLE
    // ========================================================

    VBox heading = new VBox(5);

    Label title =
            new Label("App Reviews");

    title.setStyle(
            "-fx-font-size: 26px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " +
            TEXT_DARK +
            ";"
    );

    Label description =
            new Label(
                    "View and manage reviews and feedback"
            );

    description.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " +
            TEXT_GRAY +
            ";"
    );

    heading.getChildren().addAll(
            title,
            description
    );

    // ========================================================
    // STATISTICS
    // ========================================================

    HBox stats = new HBox(24);

    VBox ratingCard =
            createStatCard(
                    "Overall Rating",
                    "Loading..."
            );

    VBox totalCard =
            createStatCard(
                    "Total Reviews",
                    "Loading..."
            );

    VBox positiveCard =
            createStatCard(
                    "Positive Reviews",
                    "Loading..."
            );

    overallRatingValue =
            (Label) ratingCard
                    .getProperties()
                    .get("valueLabel");

    totalReviewsValue =
            (Label) totalCard
                    .getProperties()
                    .get("valueLabel");

    positiveReviewsValue =
            (Label) positiveCard
                    .getProperties()
                    .get("valueLabel");

    HBox.setHgrow(
            ratingCard,
            Priority.ALWAYS
    );

    HBox.setHgrow(
            totalCard,
            Priority.ALWAYS
    );

    HBox.setHgrow(
            positiveCard,
            Priority.ALWAYS
    );

    stats.getChildren().addAll(
            ratingCard,
            totalCard,
            positiveCard
    );

    // ========================================================
    // FILTER
    // ========================================================

    HBox filterRow = new HBox();

    filterRow.setAlignment(
            Pos.CENTER_LEFT
    );

    filterRow.setSpacing(12);

    Label filterLabel =
            new Label("Filter by Rating:");

    filterLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " +
            TEXT_DARK +
            ";"
    );

    ComboBox<String> ratingFilter =
            new ComboBox<>();

    ratingFilter.getItems().addAll(
            "All Ratings",
            "★★★★★ 5 Stars",
            "★★★★ 4 Stars",
            "★★★ 3 Stars",
            "★★ 2 Stars",
            "★ 1 Star"
    );

    ratingFilter.setValue(
            "All Ratings"
    );

    ratingFilter.setPrefWidth(180);

    ratingFilter.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " +
            BORDER +
            ";" +
            "-fx-border-radius: 7;" +
            "-fx-background-radius: 7;"
    );

    ratingFilter.valueProperty().addListener(
            (obs, oldValue, newValue) -> {

                if (newValue != null) {

                    loadReviews(newValue);
                }
            }
    );

    filterRow.getChildren().addAll(
            filterLabel,
            ratingFilter
    );

    // ========================================================
    // USER REVIEWS TITLE
    // ========================================================

    Label reviewsTitle =
            new Label("User Reviews");

    reviewsTitle.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " +
            TEXT_DARK +
            ";"
    );

    // ========================================================
    // USER REVIEWS TABLE
    // ========================================================

    VBox table =
            createReviewsTable();

    /*
     * IMPORTANT:
     * Allow only the table section to grow.
     */
    VBox.setVgrow(
            table,
            Priority.ALWAYS
    );

    // ========================================================
    // ADD EVERYTHING
    // ========================================================

    content.getChildren().addAll(
            heading,
            stats,
            filterRow,
            reviewsTitle,
            table
    );

    return content;
}

    // ============================================================
    // STAT CARD
    // ============================================================
    private VBox createStatCard(
        String title,
        String value
) {

    VBox card = new VBox();

   card.setPadding(
        new Insets(24)
);

card.setSpacing(12);

card.setMinHeight(135);
card.setPrefHeight(135);

card.setMaxHeight(Double.MAX_VALUE);

    card.setCursor(Cursor.HAND);

    card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " +
            BORDER +
            ";" +
            "-fx-border-radius: 12;"
    );

    // ========================================================
    // TITLE LABEL
    // ========================================================

    Label titleLabel =
            new Label(title);

    titleLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " +
            TEXT_GRAY +
            ";"
    );

    // ========================================================
    // VALUE LABEL
    // ========================================================

    Label valueLabel =
            new Label(value);

    valueLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " +
            TEXT_DARK +
            ";"
    );

    // ========================================================
    // STORE VALUE LABEL
    // ========================================================

    card.getProperties().put(
            "valueLabel",
            valueLabel
    );

    // ========================================================
    // ADD LABELS TO CARD
    // ========================================================

    card.getChildren().addAll(
            titleLabel,
            valueLabel
    );

    // ========================================================
    // HOVER EFFECT
    // ========================================================

    card.setOnMouseEntered(e -> {

        card.setStyle(
                "-fx-background-color: " +
                HOVER_GREEN +
                ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: " +
                CLICK_GREEN +
                ";" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 1.5;"
        );

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(120),
                        card
                );

        scale.setToX(1.03);
        scale.setToY(1.03);
        scale.play();
    });

    card.setOnMouseExited(e -> {

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: " +
                BORDER +
                ";" +
                "-fx-border-radius: 12;"
        );

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(120),
                        card
                );

        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.play();
    });

    return card;
}



    // ============================================================
    // REVIEWS TABLE
    // ============================================================

private VBox createReviewsTable() {

    reviewsTable = new VBox();

    // ========================================================
    // OUTER TABLE
    // ========================================================

    reviewsTable.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + DARK_GREEN + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;"
    );

    // ========================================================
    // STATIC TABLE HEADER
    // ========================================================

    HBox header = createTableRow(
            "USER",
            "TYPE",
            "RATING",
            "DATE",
            "ACTION",
            true
    );

    // ========================================================
    // SCROLLABLE REVIEW ROWS
    // ========================================================

    reviewRows = new VBox();

    reviewRows.setStyle(
            "-fx-background-color: white;"
    );

    // ========================================================
    // SCROLL PANE
    // ========================================================

    ScrollPane reviewScrollPane =
            new ScrollPane(reviewRows);

    reviewScrollPane.setFitToWidth(true);
    reviewScrollPane.setPannable(true);

    reviewScrollPane.setVbarPolicy(
            ScrollPane.ScrollBarPolicy.AS_NEEDED
    );

    reviewScrollPane.setHbarPolicy(
            ScrollPane.ScrollBarPolicy.NEVER
    );

    reviewScrollPane.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;"
    );

    VBox.setVgrow(
            reviewScrollPane,
            Priority.ALWAYS
    );

    // ========================================================
    // ADD HEADER + SCROLL AREA
    // ========================================================

    reviewsTable.getChildren().addAll(
            header,
            reviewScrollPane
    );

    VBox.setVgrow(
            reviewScrollPane,
            Priority.ALWAYS
    );

    // ========================================================
    // LOAD FIREBASE REVIEWS
    // ========================================================

    loadReviews("All Ratings");

    return reviewsTable;
}



    // ============================================================
    // LOAD / FILTER REVIEWS
    // ============================================================


// ============================================================
// LOAD / FILTER REVIEWS
// ============================================================

private void loadReviews(
        String selectedRating
) {

    if (reviewRows == null) {
        return;
    }

    // Clear ONLY review rows
    reviewRows.getChildren().clear();

    Label loading =
            new Label(
                    "Loading reviews from Firebase..."
            );

    loading.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " +
            TEXT_GRAY +
            ";" +
            "-fx-padding: 18;"
    );

    reviewRows.getChildren().add(
            loading
    );

    loadAppReviewsFromFirebase(
            selectedRating
    );
}

private void loadAppReviewsFromFirebase(
        String selectedRating
) {

    CompletableFuture.runAsync(() -> {

        try {

            ApiFuture<QuerySnapshot> future =
                    FirebaseConfig.getFireStore()
                            .collection("reviews")
                            .get();

            QuerySnapshot snapshot =
                    future.get();

            List<ReviewData> loadedReviews =
                    new ArrayList<>();

            for (DocumentSnapshot doc :
                    snapshot.getDocuments()) {

                String targetType =
                        getString(
                                doc,
                                "targetType"
                        );

                String targetMail =
                        getString(
                                doc,
                                "targetMail"
                        );

                String status =
                        getString(
                                doc,
                                "status"
                        );

                /*
                 * ONLY APP REVIEWS
                 */

                if (!"APP".equalsIgnoreCase(
                        targetType
                )) {
                    continue;
                }

                if (!"APP".equalsIgnoreCase(
                        targetMail
                )) {
                    continue;
                }

                if (!"ACTIVE".equalsIgnoreCase(
                        status
                )) {
                    continue;
                }

                int rating =
                        getRating(doc);

                String reviewerMail =
                        getString(
                                doc,
                                "reviewerMail"
                        );

                String reviewerRole =
                        getString(
                                doc,
                                "reviewerRole"
                        );

                String comment =
                        getString(
                                doc,
                                "comment"
                        );

                String createdAt =
                        getString(
                                doc,
                                "createdAt"
                        );

                loadedReviews.add(
                        new ReviewData(
                                doc.getId(),
                                reviewerMail,
                                reviewerRole,
                                rating,
                                comment,
                                createdAt
                        )
                );
            }

            /*
             * Newest reviews first
             */
            loadedReviews.sort(
                    Comparator.comparing(
                            ReviewData::getCreatedAt,
                            Comparator.nullsLast(
                                    Comparator.reverseOrder()
                            )
                    )
            );

            Platform.runLater(() -> {

                firebaseReviews.clear();

                firebaseReviews.addAll(
                        loadedReviews
                );

                updateStatistics(
                        loadedReviews
                );

                renderFirebaseReviews(
                        loadedReviews,
                        selectedRating
                );
            });

        }catch (Exception e) {

    e.printStackTrace();

    Platform.runLater(() -> {

        if (reviewRows == null) {
            return;
        }

        reviewRows.getChildren().clear();

        Label error =
                new Label(
                        "Unable to load reviews from Firebase."
                );

        error.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #B42318;" +
                "-fx-padding: 18;"
        );

        reviewRows.getChildren().add(
                error
        );
    });
}


    });
}
private String getString(
        DocumentSnapshot doc,
        String field
) {

    Object value =
            doc.get(field);

    return value == null
            ? ""
            : String.valueOf(value);
}
private int getRating(
        DocumentSnapshot doc
) {

    Object value =
            doc.get("rating");

    if (value instanceof Number) {
        return ((Number) value).intValue();
    }

    try {

        return Integer.parseInt(
                String.valueOf(value)
        );

    } catch (Exception e) {

        return 0;
    }
}
private void renderFirebaseReviews(
        List<ReviewData> reviews,
        String selectedRating
) {

    if (reviewRows == null) {
        return;
    }

    // Clear ONLY the scrollable review rows
    // Header remains untouched
    reviewRows.getChildren().clear();

    int requiredRating =
            getSelectedRatingNumber(
                    selectedRating
            );

    for (ReviewData review : reviews) {

        if (requiredRating != 0 &&
                review.getRating()
                        != requiredRating) {

            continue;
        }

        reviewRows.getChildren().add(
                createReviewRow(
                        review.getReviewerMail(),
                        review.getReviewerRole(),
                        toStars(
                                review.getRating()
                        ),
                        review.getComment(),
                        review.getCreatedAt()
                )
        );
    }

    // ========================================================
    // NO REVIEWS
    // ========================================================

    if (reviewRows.getChildren().isEmpty()) {

        Label empty =
                new Label(
                        "No APP reviews found."
                );

        empty.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " +
                TEXT_GRAY +
                ";" +
                "-fx-padding: 18;"
        );

        reviewRows.getChildren().add(
                empty
        );
    }
}

private int getSelectedRatingNumber(
        String selectedRating
) {

    if (selectedRating == null ||
            "All Ratings".equals(
                    selectedRating
            )) {

        return 0;
    }

    if (selectedRating.startsWith("★★★★★"))
        return 5;

    if (selectedRating.startsWith("★★★★"))
        return 4;

    if (selectedRating.startsWith("★★★"))
        return 3;

    if (selectedRating.startsWith("★★"))
        return 2;

    if (selectedRating.startsWith("★"))
        return 1;

    return 0;
}

private String toStars(
        int rating
) {

    int safeRating =
            Math.max(
                    0,
                    Math.min(
                            5,
                            rating
                    )
            );

    return "★★★★★".substring(
            0,
            safeRating
    );
}
private void updateStatistics(
        List<ReviewData> reviews
) {

    if (overallRatingValue == null ||
            totalReviewsValue == null ||
            positiveReviewsValue == null) {

        return;
    }

    int total =
            reviews.size();

    double average = 0;

    if (total > 0) {

        int sum = 0;

        for (ReviewData review : reviews) {

            sum += review.getRating();
        }

        average =
                (double) sum / total;
    }

    long positiveCount =
            reviews.stream()
                    .filter(
                            r -> r.getRating() >= 4
                    )
                    .count();

    double positivePercentage =
            total == 0
                    ? 0
                    : ((double) positiveCount /
                       total) * 100.0;

    overallRatingValue.setText(
            String.format(
                    java.util.Locale.US,
                    "%.1f ★",
                    average
            )
    );

    totalReviewsValue.setText(
            String.valueOf(total)
    );

    positiveReviewsValue.setText(
            String.format(
                    java.util.Locale.US,
                    "%.0f%%",
                    positivePercentage
            )
    );
}

    // ============================================================
    // TABLE ROW
    // ============================================================

private HBox createTableRow(
        String user,
        String type,
        String rating,
        String date,
        String action,
        boolean header
) {

    HBox row = new HBox();

    row.setAlignment(Pos.CENTER_LEFT);

    row.setPadding(
            new Insets(14, 12, 14, 12)
    );

    row.setSpacing(0);

    // Make row occupy complete table width
    row.setMaxWidth(Double.MAX_VALUE);

    // ========================================================
    // TABLE CELLS
    // ========================================================

    Label userCell =
            createCell(user, 0, header);

    Label typeCell =
            createCell(type, 0, header);

    Label ratingCell =
            createRatingCell(rating, 0, header);

    Label dateCell =
            createCell(date, 0, header);

    Label actionCell =
            createCell(action, 0, header);

    // ========================================================
    // COLUMN WIDTHS
    // ========================================================

    /*
     * Available table width is divided approximately as:
     *
     * USER    = 30%
     * TYPE    = 12%
     * RATING  = 18%
     * DATE    = 20%
     * ACTION  = 20%
     */

    userCell.prefWidthProperty().bind(
            row.widthProperty().multiply(0.30)
    );

    typeCell.prefWidthProperty().bind(
            row.widthProperty().multiply(0.12)
    );

    ratingCell.prefWidthProperty().bind(
            row.widthProperty().multiply(0.18)
    );

    dateCell.prefWidthProperty().bind(
            row.widthProperty().multiply(0.20)
    );

    actionCell.prefWidthProperty().bind(
            row.widthProperty().multiply(0.20)
    );

    // Minimum width = 0 so columns can resize
    userCell.setMinWidth(0);
    typeCell.setMinWidth(0);
    ratingCell.setMinWidth(0);
    dateCell.setMinWidth(0);
    actionCell.setMinWidth(0);

    // ========================================================
    // ADD CELLS
    // ========================================================

    row.getChildren().addAll(
            userCell,
            typeCell,
            ratingCell,
            dateCell,
            actionCell
    );

    // ========================================================
    // HEADER / ROW STYLE
    // ========================================================

    if (header) {

        row.setStyle(
                "-fx-background-color: #dcfce7;" +
                "-fx-border-color: " + DARK_GREEN + ";" +
                "-fx-border-width: 0 0 2 0;"
        );

    } else {

        row.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-width: 0 0 1 0;"
        );
    }

    return row;
}




    // ============================================================
    // RATING CELL
    // ============================================================
        private Label createRatingCell(
        String rating,
        double width,
        boolean header
) {

    Label label =
            new Label(rating);

    label.setMinWidth(0);

    label.setMaxWidth(Double.MAX_VALUE);

    label.setAlignment(
            Pos.CENTER_LEFT
    );

    if (header) {

        label.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " +
                TEXT_GRAY +
                ";"
        );

    } else {

        label.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #D4A017;"
        );
    }

    return label;
}
    // ============================================================
    // REVIEW ROW
    // ============================================================

        private HBox createReviewRow(
        String user,
        String type,
        String rating,
        String review,
        String date
) {

    HBox row =
            createTableRow(
                    user,
                    type,
                    rating,
                    date,
                    "",
                    false
            );

    Button viewButton =
            new Button("View");

    viewButton.setCursor(
            Cursor.HAND
    );

    setViewButtonStyle(
            viewButton,
            CLICK_GREEN
    );

    viewButton.setOnAction(e -> {

        showReviewDetails(
                user,
                type,
                rating,
                review,
                date
        );
    });

    viewButton.setOnMouseEntered(e -> {

        setViewButtonStyle(
                viewButton,
                "#165E3B"
        );
    });

    viewButton.setOnMouseExited(e -> {

        setViewButtonStyle(
                viewButton,
                CLICK_GREEN
        );
    });

    // Get last column = ACTION
    HBox actionBox =
            new HBox(viewButton);

    actionBox.setAlignment(
            Pos.CENTER_LEFT
    );

    actionBox.setMinWidth(0);
    actionBox.setMaxWidth(Double.MAX_VALUE);

    // Remove empty ACTION label
    row.getChildren().remove(
            row.getChildren().size() - 1
    );

    // Add actual button container
    row.getChildren().add(
            actionBox
    );

    return row;
}

    // ============================================================
    // VIEW BUTTON STYLE
    // ============================================================

    private void setViewButtonStyle(
            Button button,
            String color
    ) {

        button.setStyle(
                "-fx-background-color: " +
                color +
                ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 6 14;"
        );
    }

    // ============================================================
    // REVIEW DETAILS
    // ============================================================

    private void showReviewDetails(
            String user,
            String type,
            String rating,
            String review,
            String date
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Review Details"
        );

        alert.setHeaderText(
                "User Review"
        );

        Label userLabel =
                new Label(
                        "User: " + user
                );

        Label typeLabel =
                new Label(
                        "Type: " + type
                );

        Label ratingLabel =
                new Label(
                        "Rating: " + rating
                );

        Label reviewLabel =
                new Label(
                        "Review: " + review
                );

        Label dateLabel =
                new Label(
                        "Date: " + date
                );

        userLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " +
                TEXT_DARK +
                ";"
        );

        typeLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " +
                TEXT_GRAY +
                ";"
        );

        ratingLabel.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #D89B00;"
        );

        reviewLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: " +
                TEXT_DARK +
                ";"
        );

        dateLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " +
                TEXT_GRAY +
                ";"
        );

        VBox box =
                new VBox(
                        12,
                        userLabel,
                        typeLabel,
                        ratingLabel,
                        reviewLabel,
                        dateLabel
                );

        box.setPadding(
                new Insets(10)
        );

        alert.getDialogPane().setContent(
                box
        );

        alert.getDialogPane().setStyle(
                "-fx-background-color: white;"
        );

        Button okButton =
                (Button) alert.getDialogPane()
                        .lookupButton(
                                ButtonType.OK
                        );

        okButton.setStyle(
                "-fx-background-color: " +
                CLICK_GREEN +
                ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;"
        );

        alert.showAndWait();
    }

    // ============================================================
    // TABLE CELL
    // ============================================================

   private Label createCell(
        String text,
        double width,
        boolean header
) {

    Label label =
            new Label(text);

    label.setMinWidth(0);

    label.setMaxWidth(Double.MAX_VALUE);

    label.setWrapText(true);

    label.setAlignment(
            Pos.CENTER_LEFT
    );

    if (header) {

        label.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " +
                TEXT_GRAY +
                ";"
        );

    } else {

        label.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: " +
                TEXT_DARK +
                ";"
        );
    }

    return label;
}

    // ============================================================
    // UTILITY
    // ============================================================

    private Region createVerticalSpace(
            double height
    ) {

        Region region = new Region();

        region.setMinHeight(height);

        return region;
    }
    private static class ReviewData {

    private final String id;
    private final String reviewerMail;
    private final String reviewerRole;
    private final int rating;
    private final String comment;
    private final String createdAt;

    ReviewData(
            String id,
            String reviewerMail,
            String reviewerRole,
            int rating,
            String comment,
            String createdAt
    ) {

        this.id = id;
        this.reviewerMail = reviewerMail;
        this.reviewerRole = reviewerRole;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    String getReviewerMail() {
        return reviewerMail;
    }

    String getReviewerRole() {
        return reviewerRole;
    }

    int getRating() {
        return rating;
    }

    String getComment() {
        return comment;
    }

    String getCreatedAt() {
        return createdAt;
    }
}
}